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

package gomule;

import java.io.*;

import randall.d2files.*;

public class Dump
{
    public int check_bit(int num, int bit_num)
    {
        int result = num << (bit_num - 1) >>> 31;
        return result;
    }

    public void print_flags(int flags)
    {
        System.out
                .println("FLAGS: 1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32");
        System.out.print("FLAGS: ");
        for (int i = 0; i < 32; i++)
        {
            int digit = check_bit(flags, i + 1);
            if (digit == 1)
                System.out.print("1  ");
            else
                System.out.print("   ");
        }
        System.out.println();
    }

    public void print_d2s(String filename)
    {
        try
        {
            FileInputStream in = new FileInputStream(filename);
            byte[] bytes = new byte[10];
            while (in.read(bytes) != -1)
            {
                for (int i = 0; i < 10; i++)
                {
                    int unsigned = 0x000000ff & bytes[i];
                    if (unsigned > 20 && unsigned < 125)
                        System.out.print((char) unsigned + "\t");
                    else
                        System.out.print(unsigned + "\t");
                }
                System.out.println();
            }
        } catch (Exception ex)
        {
            System.out.println("I/O error");
        }
    }

    public static void main(String[] args)
    {
//        // set up text file tables
//        D2TxtTable t = D2TxtTable.get(D2TxtTable.BASE);
//        String s = java.io.File.separator;
//        t.add_file("resources" + s + "weapons.txt", "code");
//        t.add_file("resources" + s + "armor.txt", "code");
//        t.add_file("resources" + s + "misc.txt", "code");
//        t = D2TxtTable.get(D2TxtTable.UNIQUES);
//        t.add_file("resources" + s + "uniqueitems.txt", -1);
//        t = D2TxtTable.get(D2TxtTable.SETS);
//        t.add_file("resources" + s + "setitems.txt", 0);
		D2TxtFile.constructTxtFiles("d2111");
		D2TblFile.readAllFiles("d2111");

        //main gui = new main(args[0]);
        new Dump().print_d2s(args[0]);
        //new character(args[0]);
        //new filemanager();
    }
}

