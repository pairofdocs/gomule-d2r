/*******************************************************************************
 * 
 * Copyright 2007 Andy Theuninck, Randall & Silospen
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

package gomule.d2s;

import gomule.gui.*;
import gomule.item.*;
import gomule.util.*;

import java.awt.Point;
import java.io.*;
import java.util.*;

import randall.d2files.*;

//a character class
//manages one character file
//stores a filename, a bitreader
//to read from that file, and
//a vector of items
public class D2SharedStash extends D2ItemListAdapter
{
	public static final int BODY_STASH_CONTENT = 5;
	public static final int STASHSIZEX = 30; // 10 orig.  10*3 for 3 tabs in the shared stash
	public static final int STASHSIZEY = 10;
	
	private D2BitReader iReader;
    // need iStashItems1, iStashItems2, iStashItems3
	private ArrayList<ArrayList> iStashes;
	private ArrayList iStashItems1;
    private ArrayList iStashItems2;
    private ArrayList iStashItems3;
	private int iStashIdx;

	private D2Item iCharCursorItem;
	
	private String iCharName;
	private String iTitleString = ""; // is called in D2ViewSharedStash, which already outputs D2R Shared Stash (SC or HC)
	private String cClass;
	private long iCharLevel;
	private int curWep = 0;
	private long lCharCode;
	private String iCharClass;
	private boolean iHC;

	private boolean[][]     iStashGrid; 
	// grid for stash tab2 and 3?
	
//	private int testCounter = 0;
//	private boolean fullChanged = false;
//	private ArrayList partialSetProps = new ArrayList();
//	private ArrayList fullSetProps = new ArrayList();

	// private int lWoo;
    private int headstartFirst;
	
	private int iItemEnd1;
    private int iItemEnd2;
    private int iItemEnd3;
	

	public D2SharedStash(String pFileName) throws Exception{
		super( pFileName );
		if ( iFileName == null || !iFileName.toLowerCase().endsWith(".d2i"))throw new Exception("Incorrect Stash file name");
		iStashes = new ArrayList<ArrayList>();
		iStashItems1 = new ArrayList();
		iStashItems2 = new ArrayList();
		iStashItems3 = new ArrayList();
		iStashes.add(iStashItems1);
		iStashes.add(iStashItems2);
		iStashes.add(iStashItems3);
		
		iReader = new D2BitReader(iFileName);
		readStash();
		// clear status
		setModified(false);
	}

    // TODO: make this read a byte array for each shared stash
	private void readStash() throws Exception{
        // 55 aa 55 aa header    4 bytes 00     4bytes version
		iReader.set_byte_pos(8);
		long lVersion = iReader.read(32);
		System.err.println("Version: " + lVersion );
		if (lVersion != 97)throw new Exception("Incorrect Shared Stash version: " + lVersion);

		// iReader.set_byte_pos(8);
		long lGoldStash1 = iReader.read(32);  // byte pos 12
		// if (iReader.get_length() != lSize)throw new Exception("Incorrect FileSize: " + lSize);

        // TODO:  checksum.   is checksum as byte pos 16? --> Yes. (checksum is only on the item block, not the gold#)
		// long lCheckSum2 = calculateCheckSum();
		// iReader.set_byte_pos(16);
		boolean lChecksum = false;
		// byte lCalcByte3 = (byte) ((0xff000000 & lCheckSum2) >>> 24);
		// byte lCalcByte2 = (byte) ((0x00ff0000 & lCheckSum2) >>> 16);
		// byte lCalcByte1 = (byte) ((0x0000ff00 & lCheckSum2) >>> 8);
		// byte lCalcByte0 = (byte) (0x000000ff & lCheckSum2);
		// byte lFileByte0 = (byte) iReader.read(8);
		// byte lFileByte1 = (byte) iReader.read(8);
		// byte lFileByte2 = (byte) iReader.read(8);
		// byte lFileByte3 = (byte) iReader.read(8);
		// if (lFileByte0 == lCalcByte0){
		// 	if (lFileByte1 == lCalcByte1){
		// 		if (lFileByte2 == lCalcByte2){
		// 			if (lFileByte3 == lCalcByte3)lChecksum = true;
		// 		}
		// 	}
		// }
		// if (!lChecksum)throw new Exception("Incorrect Checksum");
        long lCheckSum = iReader.read(32);
        System.err.println("lCheckSum: " + lCheckSum);

//		long lWeaponSet = iReader.read(32);
		
		iStashGrid = new boolean[STASHSIZEY][STASHSIZEX];   // 3 tabs for shared stash pane
		
		clearGrid();
		iStashIdx = 0;
		int outBytePos = readItems(32);
		iStashIdx ++;
		outBytePos = readItems(outBytePos);
		iStashIdx ++;
		outBytePos = readItems(outBytePos);

		System.err.println("outBytePos: " + outBytePos);
		// TODO: read items for itemlist2, and 3. and draw item images on background
		
	}

	
	//TODO calc checksum for the itemblock (3 blocks total in D2R's shared stash file)
	private long calculateCheckSum(){
		iReader.set_byte_pos(0);
		long lCheckSum = 0; // unsigned integer checksum
		for (int i = 0; i < iReader.get_length(); i++){
			long lByte = iReader.read(8);
			if (i >= 12 && i <= 15)lByte = 0;
			long upshift = lCheckSum << 33 >>> 32;
			long add = lByte + ((lCheckSum >>> 31) == 1 ? 1 : 0);
			lCheckSum = upshift + add;
		}
		return lCheckSum;
	}

	
	// TODO
	private int readItems(int bytePosStart) throws Exception{
		int lFirstPos = iReader.findNextFlag("JM", bytePosStart);  // byte pos 64 for 1st stash tab/item block
		System.err.println("lFirstPos: " + lFirstPos);
		if (lFirstPos == -1)throw new Exception("Character items not found");
		int lLastItemEnd = lFirstPos + 2;  // byte pos 66
		iReader.set_byte_pos(lLastItemEnd);

		int num_items = (int) iReader.read(16);
		int lItemBlockStart = lFirstPos + 4;  // byte pos 68 for 1st stash tab/item block
		int lItemBlockEnd = lItemBlockStart;
		
		for (int i = 0; i < num_items; i++){
			// D2R d2s file doesn't have JM for each item. just 1JM at stat and 1 JM at end of item list
			int lItemStart = lItemBlockEnd;
			// update  lItemStart for following items. use Bit position to start reading items

			D2Item lItem = new D2Item(iFileName, iReader, lItemStart, iCharLevel);
			// debugging helper function added to D2Item.java    lItem.get_bytes_string()

			lLastItemEnd = iReader.get_byte_pos(); // use itemReader for bits position
			lItemBlockEnd = lLastItemEnd + 1;           // add 1 to get to start nextItem byte, works for 2nd, 3rd, 4th... items

			if ( lItem.isCursorItem()){
				if ( iCharCursorItem != null )throw new Exception("Double cursor item found");
				iCharCursorItem = lItem;
			}else{
				// TODO:  add to itemlist1, 2 or 3
				addCharItem(lItem);
				markCharGrid(lItem);
			}
		}
		return lItemBlockEnd;
	}

	


    // TODO: handle itemlist 1, 2, and 3
	public ArrayList getItemList(){
		ArrayList lList = new ArrayList();
		if ( iStashItems1 != null )lList.addAll( iStashItems1 );
		// TODO:  get istash2 and 3 as well
		// if ( iMercItems != null )lList.addAll( iMercItems );
		return lList;
	}

	public boolean containsItem(D2Item pItem){
		if ( iStashItems1.contains(pItem))return true;
		// if ( iMercItems.contains(pItem))return true;
		return false;
	}

	public void removeItem(D2Item pItem){
		if ( iStashItems1.remove(pItem) ){
			unmarkCharGrid(pItem);
		}else{
			// unmarkMercGrid(pItem);
			// iMercItems.remove(pItem);
		}
		setModified(true);
	}

	// TODO: is this needed for a shared stash? can it be removed?
	public void setCursorItem(D2Item pItem){
		iCharCursorItem = pItem;
		setModified(true);
		if ( iCharCursorItem != null ){
			iCharCursorItem.set_location((short) 4);
			iCharCursorItem.set_body_position((short) 0);
		}
	}
	// clear all the grids
	// grids keep track of which spots that items
	// can be place are occupied
	public void clearGrid(){
		for (int i = 0; i < STASHSIZEY; i++){
			for (int j = 0; j < STASHSIZEX; j++)iStashGrid[i][j] = false;
		}
	}

	public boolean markCharGrid(D2Item i){
		short panel = i.get_panel();
		int row, col, width, height, j, k;
		switch (panel){
		case 0: // equipped or on belt
			
			break;
		case BODY_STASH_CONTENT: // stash
			row = (int) i.get_row();
			col = (int) i.get_col();
			width = (int) i.get_width();
			height = (int) i.get_height();
			if ((row + height) > 10)return false;   // ***8*** stash size int and not a variable set here!
			if ((col + width) > 10)return false;    // ***6*** stash size
			for (j = row; j < row + height; j++){
				for (k = col; k < col + width; k++)iStashGrid[j][k + iStashIdx*10] = true;
			}
			break;
		}
		return true;
	}

	
	public boolean unmarkCharGrid(D2Item i){
		short panel = i.get_panel();
		int row, col, width, height, j, k;
		switch (panel){
		case 0: // equipped or on belt
			
			break;
		case BODY_STASH_CONTENT: // stash
			row = (int) i.get_row();
			col = (int) i.get_col();
			width = (int) i.get_width();
			height = (int) i.get_height();
			if ((row + height) > 10)return false;     // orig > 8
			if ((col + width) > 10)return false;      // orig > 6
			for (j = row; j < row + height; j++){
				for (k = col; k < col + width; k++)
					iStashGrid[j][k] = false;
			}
			break;
		}
		return true;
	}


	public void addCharItem(D2Item pItem){
		iStashes.get(iStashIdx).add(pItem);
		// iStashItems1.add(pItem);
		pItem.setCharLvl((int)iCharLevel);
		setModified(true);
	}

	public void removeCharItem(int i){
		// remove from stash1, 2, or 3
		iStashItems1.remove(i);
		setModified(true);
	}

	
	public boolean checkCharGrid(int panel, int x, int y, D2Item pItem){
		int i, j;
		int w = pItem.get_width();
		int h = pItem.get_height();
		switch (panel){
		case BODY_STASH_CONTENT:
			for (i = x; i < x + w; i++){
				for (j = y; j < y + h; j++){
					if (j >= iStashGrid.length || i >= iStashGrid[j].length || iStashGrid[j][i])return false;
				}
			}
			break;
		}
		return true;
	}


	public boolean checkCharPanel(int panel, int x, int y, D2Item pItem){
		System.err.println("checkCharPanel panel: " + panel);
		System.err.println("x: " + x + " y: " + y);
		switch (panel){
		case BODY_STASH_CONTENT:
			if (y >= 0 && y < iStashGrid.length){
				if (x >= 0 && x < iStashGrid[y].length)return iStashGrid[y][x];
			}
			return false;
		}
		return true;
	}

	// Debug:
	// -1 out of bounds for length 18
    //     at java.base/jdk.internal.util.Preconditions.outOfBounds(Preconditions.java:64)
    //     at java.base/jdk.internal.util.Preconditions.outOfBoundsCheckIndex(Preconditions.java:70)
    //     at java.base/jdk.internal.util.Preconditions.checkIndex(Preconditions.java:248)
    //     at java.base/java.util.Objects.checkIndex(Objects.java:372)
    //     at java.base/java.util.ArrayList.get(ArrayList.java:459)
    //     at gomule.d2s.D2SharedStash.getCharItem(D2SharedStash.java:502)
    //     at gomule.gui.D2ViewSharedStash$D2ItemPanel.getItem(D2ViewSharedStash.java:848)
	public int getCharItemIndex(int panel, int x, int y, int stashIdx){  // iRow,  iCol are input
		
		// Add logic for checking stash1, 2 and 3
		for (int i = 0; i < iStashes.get(stashIdx).size(); i++){
			D2Item temp_item = (D2Item) iStashes.get(stashIdx).get(i);
			if (temp_item.get_panel() == panel)	{
				// account for x pos (col) for stashes 2, 3
				int col = temp_item.get_col() + 10*stashIdx;
				int row = temp_item.get_row();
				if ((x >= col) && (x <= col + temp_item.get_width() - 1) && (y >= row) && (y <= row + temp_item.get_height() - 1)) {
					return i;
				}
			}
		}
		return -1;
	}

	

	// TODO. save shared Stash file by writing 3 block--stash tabs  (each has a checksum)
	public void saveInternal(D2Project pProject)
	{
		// backup file
		D2Backup.backup(pProject, iFileName, iReader);
		// build an a byte array that contains the
		// entire item list and insert it into
		// the open file in place of its current item list
		int lCharSize = 0;

		// TODO: handle stashes 1, 2 and 3
		for (int i = 0; i < iStashItems1.size(); i++){
			lCharSize += ((D2Item) iStashItems1.get(i)).get_bytes().length;
		}
		if ( iCharCursorItem != null ){
			lCharSize += iCharCursorItem.get_bytes().length;
		}
		
		// TODO go through byte arrays. only need the headers, itemNum, gold (and checksum)
		byte lWritenBytes[];
		byte[] lNewbytes = new byte[iBeforeStats.length + lWritenBytes.length + iBeforeItems.length + lCharSize + iBetweenItems.length + lMercSize + iAfterItems.length];
		int lPos = 0;
		System.arraycopy(iBeforeStats, 0, lNewbytes, lPos, iBeforeStats.length);
		lPos += iBeforeStats.length;

		System.arraycopy(lWritenBytes, 0, lNewbytes, lPos, lWritenBytes.length);
		lPos += lWritenBytes.length;

		System.arraycopy(iBeforeItems, 0, lNewbytes, lPos, iBeforeItems.length);
		lPos += iBeforeItems.length;

		int lCharItemCountPos = lPos - 2;      // <<<<< is this correct for D2R?  subtract 2 for JM ?
		int lMercItemCountPos = -1;           // <<<<< is this correct for D2R?
		// Start writing Items after 'JMxx' (JM and numItems)
		for (int i = 0; i < iStashItems1.size(); i++){
			byte[] item_bytes = ((D2Item) iStashItems1.get(i)).get_bytes();
			System.arraycopy(item_bytes, 0, lNewbytes, lPos, item_bytes.length);
			
			lPos += item_bytes.length;
		}
		if ( iCharCursorItem != null ){
			byte[] item_bytes = iCharCursorItem.get_bytes();
			// debug print the byte array[].  items end on byte. beginning 4bytes are item flags.
			// are the item bytes already in Huffman code?   since bytes are read and addItem is used
			System.arraycopy(item_bytes, 0, lNewbytes, lPos, item_bytes.length);
			lPos += item_bytes.length;
		}
		if (hasMerc()){
			// Merc start jf JM ... and end is kf.
			System.arraycopy(iBetweenItems, 0, lNewbytes, lPos, iBetweenItems.length); // iBetweenitems is correct (the corpse data)
			lPos += iBetweenItems.length;
			lMercItemCountPos = lPos - 2;     // lPos -2 is right when reading merc items.  iBetweenItems contains 'JMxx' for merc numItems

			for (int i = 0; i < iMercItems.size(); i++){
				byte[] item_bytes = ((D2Item) iMercItems.get(i)).get_bytes();
				System.arraycopy(item_bytes, 0, lNewbytes, lPos, item_bytes.length);
				lPos += item_bytes.length;
			}
		}
		if (iAfterItems.length > 0){
			// This array is created up at the top, where items are read from the file
			System.arraycopy(iAfterItems, 0, lNewbytes, lPos, iAfterItems.length);
		}
		iReader.setBytes(lNewbytes); // iReader.getFileContent()
		iReader.set_byte_pos(lCharItemCountPos);
		int lCharItemsCount = iStashItems1.size();
		if ( iCharCursorItem != null ){
			lCharItemsCount++;
		}
		iReader.write(lCharItemsCount, 16);
		if (hasMerc()){
			iReader.set_byte_pos(lMercItemCountPos);
			iReader.write(iMercItems.size(), 16);
		}
		// get all the bytes
		iReader.set_byte_pos(0);
		byte[] data = iReader.get_bytes(iReader.get_length());
//		byte[] oldchecksum = { data[12], data[13], data[14], data[15] };
		// clear the current checksum
		byte[] checksum = { 0, 0, 0, 0 }; // byte checksum
		iReader.setBytes(12, checksum);
		byte[] length = new byte[4];
		length[3] = (byte) ((0xff000000 & data.length) >>> 24);
		length[2] = (byte) ((0x00ff0000 & data.length) >>> 16);
		length[1] = (byte) ((0x0000ff00 & data.length) >>> 8);
		length[0] = (byte) (0x000000ff & data.length);
		iReader.setBytes(8, length);
		iReader.set_byte_pos(0);
		long lCheckSum = calculateCheckSum();
		checksum[3] = (byte) ((0xff000000 & lCheckSum) >>> 24);
		checksum[2] = (byte) ((0x00ff0000 & lCheckSum) >>> 16);
		checksum[1] = (byte) ((0x0000ff00 & lCheckSum) >>> 8);
		checksum[0] = (byte) (0x000000ff & lCheckSum);
		iReader.setBytes(12, checksum);
		iReader.save();
		setModified(false);
	}

	public void setGold(int pGold) throws Exception{
		if ( pGold < 0 )throw new Exception("gold must be greater than zero");
		if ( pGold > getGoldMax())throw new Exception("gold must be smaller than max" + getGoldMax() );
		setModified(true);
	}

	public void setGoldBank(int pGoldBank) throws Exception{
		if ( pGoldBank < 0 )throw new Exception("gold must be greater than zero");
		if ( pGoldBank > getGoldBankMax())throw new Exception("gold must be smaller than max" + getGoldBankMax() );
		setModified(true);
	}

	public int getGoldBankMax(){
		return 2500000;
//		int lMaxGold = 50000;
//		for ( int lLvl = 9 ; lLvl <=29 ; lLvl+=10 ){
//			if ( iCharLevel < lLvl )return lMaxGold;
//			lMaxGold += 50000;
//		}
//		if ( iCharLevel == 30 )return 200000;
//		if ( iCharLevel == 31 )return 800000;
//		lMaxGold = 850000;
//		for ( int lLvl = 33 ; lLvl <=99 ; lLvl+=2 ){
//			if ( iCharLevel <= lLvl )return lMaxGold;
//			lMaxGold += 50000;
//		}
//		return 0;
	}

	public void fullDump(PrintWriter pWriter){
		pWriter.println(fullDumpStr());
	}

	public String fullDumpStr(){
		StringBuffer out = new StringBuffer();
		
		if ( iStashItems1 != null ){
			for ( int i = 0 ; i < iStashItems1.size() ; i++){
				D2Item lItem = (D2Item) iStashItems1.get(i);
				out.append(lItem.itemDump(true));
				out.append("\n");
			}
		}
		
		return out.toString();
	}

	
	// TODO: stash2 and 3 logic to be added

	public D2Item getCharItem(int i, int stashIdx){
		return (D2Item) iStashes.get(stashIdx).get(i);
	}
	// Required method to implement
	public void addItem(D2Item item){
		;
	}
	// Required method to implement
	public int getNrItems(){
		return -1;
	}
	public int getNrItems(int stashIdx){
		return iStashes.get(stashIdx).size();
	}  // +size2, +size3     //old: + iMercItems.size()

	// public int getCharItemNr(){return iStashItems1.size();}
	// public int getMercItemNr(){return iMercItems.size();}
	// public int getCorpseItemNr(){return iCorpseItems.size();}
	
	// public Point[] getSkillLocs(){return iSkillLocs;}
	// public int[] getSkillListA(){return cSkills[0];}
	// public int[] getSkillListB(){return cSkills[1];}
	// public int[] getSkillListC(){return cSkills[2];}
	// public int[] getInitSkillListA(){return initSkills[0];}
	// public int[] getInitSkillListB(){return initSkills[1];}
	// public int[] getInitSkillListC(){return initSkills[2];}

	// public boolean[][][] getQuests(){return iQuests;}
	// public boolean getCowKingDead(int difficulty){return cowKingDead[difficulty];}
	// public boolean[][][] getWaypoints(){return iWaypoints;}

	// public int getGold(){return (int)iReadStats[14];}
	// public int getGoldMax(){return 10000*((int) iCharLevel);}
	// public int getGoldBank(){return (int)iReadStats[15];}

	public D2Item getCursorItem(){return iCharCursorItem;}

	// TODO: read shared stash name  if "SoftCore" in str then HC = false
	public boolean isSC(){return !iHC;}
	public boolean isHC(){return iHC;}
	
	public String getTitleString(){return iTitleString;}
	public String getFilename()	{return iFileName;}
	
}