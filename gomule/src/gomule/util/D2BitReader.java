/*******************************************************************************
 * 
 * Copyright 2007 Andy Theuninck & Randall
 * 
 * This file is part of gomule.
 * 
 * gomule is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * gomule is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * gomlue; if not, write to the Free Software Foundation, Inc., 51 Franklin St,
 * Fifth Floor, Boston, MA 02110-1301 USA
 *  
 ******************************************************************************/

package gomule.util;

import java.io.*;
import java.util.*;

// this class is for reading and writing on
// a bit level. i'd rename it, but then i'd
// have to go track down all the references
// to it in other classes
// it can be used on an entire file or just
// an array of bytes
public class D2BitReader
{
    String filename; // file to read from
    int    position; // current position (in bits)
    byte[] filedata; // all of the file, in memory

    // CONSTRUCTOR:
    // create a new bitreader for file f
    public D2BitReader(String f)
    {
        filename = f;
        position = 0;
        load_file();
    }

    // CONSTRUCTOR:
    // wrap a new bitreader around a byte array
    public D2BitReader(byte[] b)
    {
        filedata = b;
        position = 0;
    }

    public String getFileName()
    {
        return filename;
    }

    // load the file into a byte-array in
    // memory to avoid future file i/o
    // load it into a vector first then copy it
    // to a properly sized byte array
    public boolean load_file()
    {
        try
        {
            File lFile = new File(filename);
            if (lFile.exists())
            {
                if ( !lFile.canRead() )
                {
                    return false;
                }
                FileInputStream in = new FileInputStream(filename);
                byte[] data = new byte[1024];
                Vector v = new Vector();
                do
                {
                    int num = in.read(data);
                    if (num == -1)
                        break;
                    for (int i = 0; i < num; i++)
                        v.add(new Byte(data[i]));
                } while (true);
                filedata = new byte[v.size()];
                for (int i = 0; i < v.size(); i++)
                    filedata[i] = ((Byte) v.elementAt(i)).byteValue();
                in.close();
                return true;
            }
            else
            {
                // new empty file
                filedata = new byte[0];
                return true;
            }
        } catch (Exception ex)
        {
            System.out.println("Error loading file " + filename
                    + " into memory");
            return false;
        }
    }
    
    public boolean isNewFile()
    {
        return ( filedata != null && filedata.length == 0 );
    }

    // search for an ASCII string in the byte array
    // return an array of locations where it was found
    // there is no mechanism built in for a flag not
    // being found, so know what you're looking for
    public int[] find_flags(String flag)
    {
        //        String data = new String(filedata);
        Vector v = new Vector();
        byte[] target = flag.getBytes();
        // offset is maintained to keep
        // correct indexing stored as i move
        // forward by substring-ing
        /*
         * int offset = 0; do { int i = data.indexOf(flag); if (i == -1) break;
         * v.add(new Integer(i+offset)); System.out.println(filedata[i+offset]);
         * offset += i+1; data = data.substring(i+1); }while(true);
         */
        for (int i = 0; i < filedata.length; i++)
        {
            if (filedata[i] == target[0])
            {
                boolean found = true;
                for (int j = 0; j < target.length; j++)
                {
                    if (filedata[i + j] != target[j])
                    {
                        found = false;
                    }
                }
                if (found)
                {
                    v.add(new Integer(i));
                }
            }
        }
        int[] idata = new int[v.size()];
        for (int i = 0; i < v.size(); i++)
        {
            idata[i] = ((Integer) v.elementAt(i)).intValue();
        }
        return idata;
    }

    public int findNextFlag(String flag, int pPos)
    {
//        Vector v = new Vector();
        byte[] target = flag.getBytes();

        for (int i = pPos; i < filedata.length; i++)
        {
            if (filedata[i] == target[0])
            {
                boolean found = true;
                for (int j = 0; j < target.length; j++)
                {
                    if (filedata[i + j] != target[j])
                    {
                        found = false;
                    }
                }
                if (found)
                {
                    return i;
                }
            }
        }
        return -1;
    }

    // get the current position (in bits)
    public int get_pos()
    {
        return position;
    }
    
    public int get_byte_pos()
    {
    	return position / 8;
    }

    // set the current position (in bits)
    public void set_pos(int p)
    {
        position = p;
    }

    // set the current position (in bytes)
    public void set_byte_pos(int b)
    {
        position = 8 * b;
    }

    // skip forward s bits
    public void skipBits(int s)
    {
        position += s;
    }

    public void skipBytes(int s)
    {
        position += s*8;
    }

    // read the next bits from the file
    // in theory you can request up to 64 bits
    // however, data is pulled from a byte-aligned block
    // of 8 bytes, so really you can pull 1 to 64 bits
    // from an 8 byte block, not 64 bits from anywhere
    // position (in bits) is advanced upon completion
    public long read(int bits)
    {
        // number of bytes represented by the position
        int byte_num = position / 8;
        // number of bits past the last byte mark
        int bits_past = position % 8;

        // put the current 8 bytes into a long
        long fixed_data = 0;
        for (int i = 0; i < 8; i++)
        {
            fixed_data = fixed_data << 8;
            if (byte_num + i < filedata.length)
            {
                int unsigned = 0x000000ff & flip(filedata[byte_num + i]);
                fixed_data += unsigned;
            } 
            else
            {
                fixed_data += 0;
            }
        }

        // shift to get the correct bits
        // discard leading bits
        fixed_data = fixed_data << bits_past;
        // move desired bits to the right side of the long
        fixed_data = fixed_data >>> (64 - bits);

        // advance position by the number of bits read
        position += bits;

        // give data as a long
        return unflip(fixed_data, bits);
    }

    // flips the last x bits of the long as specified by
    // 'bits'. This changes the number represented by
    // those bits back to the order expected by java
    // so they are evaluated to the proper number
    public long unflip(long l, int bits)
    {
        long ret = 0;
        for (int i = 0; i < bits; i++)
        {
            int bit = (int) ((l >> i) & 0x01);
            ret = ret << 1;
            ret += bit;
        }
        return ret;
    }

    // write the content of data's right bits into
    // the current specified number of bits
    // again, the 64 bits 'in theory' issue applies
    // the same way as it does in read
    // position (in bits) advanced on completion
    public void write(long data, int bits)
    {
        // get current position in bits and bytes
        int byte_num = position / 8;
        int bits_past = position % 8;

        // get the current bits into a long
        long writeable_data = 0;
        for (int i = 0; i < 8; i++)
        {
            writeable_data = writeable_data << 8;
            if (byte_num + i < filedata.length)
            {
                writeable_data += flip(filedata[byte_num + i]);
            } else
                writeable_data += 0;
        }

        // generate a mask to clear the bits
        // that are going to be written
        long mask = (1 << bits) - 1;
        mask = mask << (64 - bits - bits_past);
        mask = ~mask;

        // clear the bits
        writeable_data = writeable_data & mask;

        // move the data bits to be written into
        // the correct bit position
        data = unflip(data, bits);
        data = data << (64 - bits - bits_past);

        // set the bits to be written
        writeable_data = writeable_data | data;

        // put the bytes back
        for (int i = 7; i >= 0; i--)
        {
            long current_byte = writeable_data & 0xff;
            current_byte = unflip(current_byte, 8);
            if (byte_num + i < filedata.length)
                filedata[byte_num + i] = (byte) current_byte;
            writeable_data = writeable_data >>> 8;
        }

        // advance position
        position += bits;
    }

    // reverses the bits in a byte. Necessary
    // for reading properly accross byte marks
    public int flip(byte b)
    {
        int ret = 0;
        for (int i = 0; i < 8; i++)
        {
            int bit = (int) ((b >> i) & 0x01);
            ret = ret << 1;
            ret += bit;
        }
        return ret;
    }

    public byte getByte()
    {
        return filedata[position++];
    }

    // fetch 'num' bytes, starting at the current position
    public byte[] get_bytes(int num)
    {
        int bytepos = position / 8;
        byte[] ret = new byte[num];
        System.arraycopy(filedata, bytepos, ret, 0, num); // bytepos+num >= filedata.length
        return ret;
    }
    
    public byte[] getFileContent()
    {
        return filedata;
    }

    public int get_length()
    {
        return filedata.length;
    }

    public void setBytes(byte pNewBytes[])
    {
        filedata = pNewBytes;
    }
    
    public void setBytes(int pPos, byte pReplaceBytes[])
    {
        System.arraycopy(pReplaceBytes, 0, filedata, pPos, pReplaceBytes.length);
    }

//    // replace bytes between 'start' and 'end' (inclusive) with
//    // the data in newbytes
//    public void replace_bytes(int start, int end, byte[] newbytes)
//    {
//        byte[] tempdata = new byte[start + newbytes.length
//                + (filedata.length - end - 1)];
//
//        for (int i = 0; i < start; i++)
//            tempdata[i] = filedata[i];
//
//        for (int i = 0; i < newbytes.length; i++)
//            tempdata[start + i] = newbytes[i];
//
//        for (int i = 0; i < filedata.length - end - 1; i++)
//            tempdata[i + start + newbytes.length] = filedata[i + end + 1];
//
//        filedata = tempdata;
//
//    }

    public void save()
    {
        try
        {
            FileOutputStream out = new FileOutputStream(filename);
            out.write(filedata);
            out.close();
        } catch (Exception ex)
        {
            System.out.println("Error saving file " + filename);
        }
    }

    public void save(String f)
    {
        try
        {
            FileOutputStream out = new FileOutputStream(f);
            out.write(filedata);
            out.close();
        } catch (Exception ex)
        {
            System.out.println("Error saving file " + f);
        }
    }

}

