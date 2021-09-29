/*******************************************************************************
 * 
 * Copyright 2007 Randall
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
package gomule.d2x;

import gomule.gui.*;
import gomule.item.*;
import gomule.util.*;

import java.io.*;
import java.util.*;

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2Stash extends D2ItemListAdapter
{
//    private String		iFileName;
    private ArrayList	iItems;

    private D2BitReader	iBR;
    private boolean		iHC;
    private boolean		iSC;
    
    private int			iCharLvl = 75; // default char lvl for properties

	private File lFile;

    public boolean     d2rStash;
    
//    private int iItemlistStart;
//    private int iItemlistEnd;
    
    public D2Stash(String pFileName) throws Exception
    {
        super(pFileName);
        if ( iFileName == null || !iFileName.toLowerCase().endsWith(".d2x"))  // orig: .d2x
        {
            if (!iFileName.toLowerCase().endsWith(".d2i")) {
                throw new Exception("Incorrect Stash file name");    
            }
        }
        iItems = new ArrayList();
        
        lFile = new File(iFileName);
        
        iSC = lFile.getName().toLowerCase().startsWith("sc_");
        iHC = lFile.getName().toLowerCase().startsWith("hc_");
        
        if ( !iSC && !iHC )
        {
            iSC = true;
            iHC = true;
        }
        
        iBR = new D2BitReader(iFileName);
        
        if ( !iBR.isNewFile() )
        {
            // System.err.println("FileName: " + iFileName);
	        iBR.set_byte_pos(0);
	        byte lBytes[] = iBR.get_bytes(2);  // orig:  iBR.get_bytes(3)
	        String lStart = new String(lBytes);
            
	        if ( "JM".equals(lStart) ) // orig: "D2X"
	        {
                // keeping the function name as is. The stash is a D2R stash (.d2i), not an Atma stash
	            readAtmaItems();
	        }else if ("[85, -86]".equals(Arrays.toString(lBytes))) { // file begins with hex 55 aa 55 aa  (each stash tab begins like that)
                // System.err.println("D2R SharedStash file");
                readD2RStashItems();  // this function sets d2rStash = true
            }
	        // clear status
	        setModified(false);
        }
        else
        {
            // set status (for first save)
	        setModified(true);
        }
    }
    
    public String getFilename()
    {
        return iFileName;
    }
    
    public boolean isHC()
    {
        return iHC;
    }
    
    public boolean isSC()
    {
        return iSC;
    }
    
    public ArrayList getItemList()
    {
        return iItems;
    }
    
    public void addItem(D2Item pItem)
    {
        if ( pItem != null && !d2rStash)
        {
            iItems.add(pItem);
            pItem.setCharLvl(iCharLvl);
	        setModified(true);
        }
    }
    
    public boolean containsItem(D2Item pItem)
    {
        return iItems.contains(pItem);
    }
    
    public void removeItem(D2Item pItem)
    {
        if (!d2rStash) {
            iItems.remove(pItem);
            setModified(true);
        }
    }
    
    public ArrayList removeAllItems()
    {
        // item removal is disabled in D2FileManager and D2ViewStash
	    ArrayList lReturn = new ArrayList();
	    lReturn.addAll( iItems );
	    
	    iItems.clear();
        setModified(true);
	    
	    return lReturn;
    }
    
    
    public int getNrItems()
    {
        return iItems.size();
    }
    
    private void readAtmaItems() throws Exception
    {
        
		iBR.set_byte_pos(2);    // iBR.set_byte_pos( 7 );
		// long lOriginal = iBR.read( 32 );
        
	    // long lCalculated = calculateAtmaCheckSum();

	//    if ( lOriginal == lCalculated )
	//     {
	// 	    iBR.set_byte_pos(3);
	        
	//         long lNumItems = iBR.read(16);
	        
	//         long lVersionNr = iBR.read(16);
	        
	//         if ( lVersionNr == 97 )
	//         {
	//             readItems(lNumItems);
	//         }else{
	//         	throw new Exception("Stash Version Incorrect!");
	//         }
	//     }
        
	    
        long lNumItems = iBR.read(16);
        // long lVersionNr = iBR.read(16);
        readItems(lNumItems);
    }

    private void readD2RStashItems() throws Exception
    {        
		iBR.set_byte_pos(66);   
	    
        long lNumItems = iBR.read(16);
        // long lVersionNr = iBR.read(16);
        d2rStash = true;
        readItems(lNumItems);
    }
    
    private long calculateAtmaCheckSum()
    {
        long lCheckSum;
        lCheckSum = 0;
        
    	
		iBR.set_byte_pos( 0 );
		// calculate a new checksum
		for ( int i = 0 ; i < iBR.get_length() ; i++ )
		{
			long lByte = iBR.read( 8 );
			if ( i >= 7 && i <= 10 )
			{
				lByte = 0;
			}
			
			long upshift = lCheckSum << 33 >>> 32;
			long add = lByte + ( ( lCheckSum >>> 31 ) == 1 ? 1 : 0 );
			lCheckSum = upshift + add;
		}
    	
//		System.err.println("Test " + lOriginal + " - " + lCheckSum + " = " + (lOriginal == lCheckSum) );
        return lCheckSum;
    }
	    
    private void readItems(long pNumItems) throws Exception
    {
        int lLastItemEnd = 4; // 11;
        if (d2rStash) {
            lLastItemEnd = 68;  // byte index 68 after the header of each stash tab
        }
        for ( int i = 0 ; i < pNumItems ; i++ )
        {
            // System.err.println("lLastItemEnd: " + lLastItemEnd);
            // int lItemStart = iBR.findNextFlag("JM", lLastItemEnd);
            int lItemStart = lLastItemEnd;
            
            D2Item lItem = new D2Item(iFileName, iBR, lItemStart, iCharLvl);
            lLastItemEnd = lItemStart + lItem.getItemLength();
            iItems.add(lItem);
        }
        if (d2rStash) {
            // while loop.  find next JM and set position, then read items, can't do a recursive call to readItems since lLastItemEnd = 68 is hard-coded
            int lNextJM = iBR.findNextFlag("JM", lLastItemEnd);
            long numItems2 = 0;
            int lItemStart2 = 0;
            while (lNextJM > 0) {
                // System.err.println("lNextJM: find JM  " + lNextJM);
                // get numItems and read items
                iBR.set_byte_pos(lNextJM + 2);
                numItems2 = iBR.read(16);
                // System.err.println("numItems2: " + numItems2);

                lLastItemEnd = lNextJM + 4;  // JM + numItems(2bytes)
                for ( int i = 0 ; i < numItems2 ; i++ )
                {
                    // System.err.println("lLastItemEnd: " + lLastItemEnd);
                    lItemStart2 = lLastItemEnd;
                    
                    D2Item lItem = new D2Item(iFileName, iBR, lItemStart2, iCharLvl);
                    lLastItemEnd = lItemStart2 + lItem.getItemLength();
                    iItems.add(lItem);
                }
                lNextJM = iBR.findNextFlag("JM", lLastItemEnd);
            }
        }
    }
    
    public void saveInternal(D2Project pProject)
    {
        // backup file.  This will backup an atma format stash.  (in case the SharedStashV2 is backed up, it will be in atma format)
        D2Backup.backup(pProject, iFileName, iBR);

        int size = 0;
        for (int i = 0; i < iItems.size(); i++)
            size += ((D2Item) iItems.get(i)).get_bytes().length;
        byte[] newbytes = new byte[size+4]; // orig +11.  for D2R, there are 2bytes for JM and 2bytes for numItems
        // newbytes[0] = 'D';
        // newbytes[1] = '2';
        // newbytes[2] = 'X';
        newbytes[0] = 'J';     // for D2R, set first two bytes as 'JM'
        newbytes[1] = 'M';
        int pos = 4;  // orig 11
        for (int i = 0; i < iItems.size(); i++)
        {
            byte[] item_bytes = ((D2Item) iItems.get(i)).get_bytes();
            // write bytes starting at index 4. This works. index0,1,2,3 are JM and numItems
            for (int j = 0; j < item_bytes.length; j++)
                newbytes[pos++] = item_bytes[j];
        }
        
        iBR.setBytes(newbytes);
        
        iBR.set_byte_pos(2);  // orig 3
        iBR.write(iItems.size(), 16);
        // iBR.write(97,16); // version 96 for 1.14      // Not needed for D2R
//        iBR.replace_bytes(11, iBR.get_length(), newbytes);
        
        // long lCheckSum1 = calculateAtmaCheckSum();
//        System.err.println("CheckSum at saving: " + lCheckSum1 );
        
        // iBR.set_byte_pos(7);
        // iBR.write(lCheckSum1, 32);

        // iBR.set_byte_pos(7);
        // long lCheckSum2 = iBR.read(32);
        
//        long lCheckSum3 = calculateGoMuleCheckSum();
//        System.err.println("CheckSum after insert: " + lCheckSum3 );
        
        // if ( lCheckSum1 == lCheckSum2 )
        // {
        //     iBR.save();
	    //     setModified(false);
        // }
        // else
        // {
        //     System.err.println("Incorrect CheckSum");
        // }
        iBR.save();
        setModified(false);
    }
    
    public void fullDump(PrintWriter pWriter)
    {
        pWriter.println( iFileName );
	    pWriter.println();
        if ( iItems != null )
        {
            for ( int i = 0 ; i < iItems.size() ; i++ )
            {
                D2Item lItem = (D2Item) iItems.get(i);
                lItem.toWriter(pWriter);
            }
        }
        pWriter.println( "Finished: " + iFileName );
        pWriter.println();
    }
    
    public String getFileNameEnd(){
			return lFile.getName();
    }
    
}
