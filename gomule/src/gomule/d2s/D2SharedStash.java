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
import java.nio.*;

import randall.d2files.*;

//a character class
//manages one character file
//stores a filename, a bitreader
//to read from that file, and
//a vector of items
public class D2SharedStash extends D2ItemListAdapter
{
	public static final int BODY_STASH_CONTENT = 5;
	public static final int STASHSIZEX = 16*3; // 10 orig.  10*3 for 3 tabs in the shared stash
	public static final int STASHSIZEY = 16;
	
	private D2BitReader iReader;
    // need iStashItems1, iStashItems2, iStashItems3
	private static final int NUM_SHARED_TABS  = 3;
	private ArrayList<ArrayList> iStashes;
	private ArrayList iStashItems1;
    private ArrayList iStashItems2;
    private ArrayList iStashItems3;
	
	private ArrayList<Long> lTabGolds;
	
	private D2Item iCharCursorItem;
	
	private String iCharName;
	private String iTitleString = ""; // is called in D2ViewSharedStash, which already outputs D2R Shared Stash (SC or HC)
	private String cClass;
	private long iCharLevel;
	private int curWep = 0;
	private long lCharCode;
	private String iCharClass;
	private boolean iHC;
	private boolean iSC;

	private File lFile;

	private boolean[][]     iStashGrid; 
	// grid for stash tab2 and 3?
	
//	private int testCounter = 0;
//	private boolean fullChanged = false;
//	private ArrayList partialSetProps = new ArrayList();
//	private ArrayList fullSetProps = new ArrayList();

	// private int lWoo;
	
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
		lTabGolds = new ArrayList<Long>();
		
		lFile = new File(iFileName);
        iSC = lFile.getName().toLowerCase().startsWith("sc_");
        iHC = lFile.getName().toLowerCase().startsWith("hc_");
        if ( !iSC && !iHC ){
            iSC = true;
            iHC = true;
        }

		iReader = new D2BitReader(iFileName);
		readStash();
		// clear status
		setModified(false);
	}

	private void readStash() throws Exception{
        // 55 aa 55 aa header    4 bytes 00     4bytes version
		iReader.set_byte_pos(8);
		long lVersion = iReader.read(32);
		// System.err.println("Version: " + lVersion );
		if (lVersion != 97)throw new Exception("Incorrect Shared Stash version: " + lVersion);

		// iReader.set_byte_pos(8);
		// long lTabGold1 = iReader.read(32);  // byte pos 12
		// if (iReader.get_length() != lSize)throw new Exception("Incorrect FileSize: " + lSize);

        //  checksum.   is checksum as byte pos 16? --> Yes. (checksum is only on the item block, not the gold#)
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
        
//		long lWeaponSet = iReader.read(32);
		
		iStashGrid = new boolean[STASHSIZEY][STASHSIZEX];   // 3 tabs for shared stash pane
		
		clearGrid();
		int outBytePos = readItems(0, 0);
		outBytePos = readItems(outBytePos, 1);
		outBytePos = readItems(outBytePos, 2);
	}

	
	// Not used in D2R
	// private long calculateCheckSum(){
	// 	iReader.set_byte_pos(0);
	// 	long lCheckSum = 0; // unsigned integer checksum
	// 	for (int i = 0; i < iReader.get_length(); i++){
	// 		long lByte = iReader.read(8);
	// 		if (i >= 12 && i <= 15)lByte = 0;
	// 		long upshift = lCheckSum << 33 >>> 32;
	// 		long add = lByte + ((lCheckSum >>> 31) == 1 ? 1 : 0);
	// 		lCheckSum = upshift + add;
	// 	}
	// 	return lCheckSum;
	// }

	private int readItems(int bytePosStart, int iStashIdx) throws Exception{
		iReader.set_byte_pos(bytePosStart + 12);
		long lTabGold1 = iReader.read(32);  // byte pos 12
		lTabGolds.add(lTabGold1);
		
		int lFirstPos = iReader.findNextFlag("JM", bytePosStart);  // byte pos 64 for 1st stash tab/item block
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
				// add to itemlist1, 2 or 3
				addCharItem(lItem, iStashIdx);
				markCharGrid(lItem, iStashIdx);
			}
		}
		return lItemBlockEnd;
	}

	
    // handle itemlist 1, 2, and 3
	public ArrayList getItemList(){
		ArrayList lList = new ArrayList();
		if ( iStashes != null ) {
			for (int i = 0; i < NUM_SHARED_TABS; i++) {
				lList.addAll( iStashes.get(i) );
			}
		}
		return lList;
	}

	public boolean containsItem(D2Item pItem){
		for (int i = 0; i < NUM_SHARED_TABS; i++) {
			if ( iStashes.get(i).contains(pItem))return true;
		}			
		// if ( iMercItems.contains(pItem))return true;
		return false;
	}

	public void removeItem(D2Item pItem){
		// need iStashes.get() then loop and remove
		for (int i = 0; i < NUM_SHARED_TABS; i++) {
			if ( iStashes.get(i).remove(pItem) ){
				unmarkCharGrid(pItem, i);
				break;
			}
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

	public boolean markCharGrid(D2Item i, int iStashIdx){
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
			// TODO: check if col is input as 30 or as 10 for stash 3.  row, col < 10 for items
			if ((row + height) > 10)return false;   // ***8*** stash size int and not a variable set here!
			if ((col + width) > 30)return false;    // ***6*** stash size
			for (j = row; j < row + height; j++){
				for (k = col; k < col + width; k++)iStashGrid[j][k + iStashIdx*10] = true;
			}
			break;
		}
		return true;
	}

	
	public boolean unmarkCharGrid(D2Item i, int iStashIdx){
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
			// TODO: check if col is input as 30 or as 10 for stash 3.  item's row and col are < 10
			if ((row + height) > 10)return false;     // orig > 8
			if ((col + width) > 10)return false;      // orig > 6
			for (j = row; j < row + height; j++){
				for (k = col; k < col + width; k++)
					iStashGrid[j][k + iStashIdx*10] = false;
			}
			break;
		}
		return true;
	}


	public void addCharItem(D2Item pItem, int iStashIdx){
		iStashes.get(iStashIdx).add(pItem);
		// iStashItems1.add(pItem);
		pItem.setCharLvl((int)iCharLevel);
		setModified(true);
	}

	public void removeCharItem(int i, int iStashIdx){
		// remove from stash1, 2, or 3
		iStashes.get(iStashIdx).remove(i);  // input stashIdx then iStashes.get(stashIdx).remove(i)
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
		// System.err.println("checkCharPanel() x, y: " + x + " , " + y);  // x,y:  29, 6  which is applicable to iStashGrid
		switch (panel){
		case BODY_STASH_CONTENT:
			if (y >= 0 && y < iStashGrid.length){
				if (x >= 0 && x < iStashGrid[y].length)return iStashGrid[y][x];
			}
			return false;
		}
		return true;
	}

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


	public void saveInternal(D2Project pProject)
	{
		// backup file
		D2Backup.backup(pProject, iFileName, iReader);
		// build an a byte array that contains the
		// entire item list and insert it into
		// the open file in place of its current item list
		// header for stash tabs. // int8 not uint8.  12-16 is gold, 16-20 is stash byte len, then JM and 2 bytes for itemNr
		byte stashHeader[] = {85, -86, 85, -86, 0, 0, 0, 0, 97, 0, 0, 0, 0, 0, 0, 0, -58, 1, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 00, 74, 77, 00, 00};
		
		// total stash tab size. e.g. totalTabSize: 762
		int totalTabSize = 0;
		for (int j = 0; j < NUM_SHARED_TABS ; j++){
			for (int i = 0; i < iStashes.get(j).size(); i++){
				byte[] item_bytes = ((D2Item) iStashes.get(j).get(i)).get_bytes();
				totalTabSize += item_bytes.length;
			}
		}
		
		byte[] lNewbytes = new byte[NUM_SHARED_TABS*stashHeader.length + totalTabSize]; // bytes of 3 stash tabs and 3 
		
		// write gold tab1
		System.arraycopy(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(lTabGolds.get(0).intValue()).array(), 0, stashHeader, 12, 4);
		// write tab byte length, account for 85 bytes of header (includes JM and item count)
		int lTabSize1 = 68;
		for (int j = 0; j < iStashes.get(0).size(); j++){
			lTabSize1 += ((D2Item) iStashes.get(0).get(j)).get_bytes().length;
		}
		System.arraycopy(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(lTabSize1).array(), 0, stashHeader, 12+4, 4);
		// write item number.  stashIdx
		// TODO: is uint16 needed here?
		System.arraycopy(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)iStashes.get(0).size()).array(), 0, stashHeader, 66, 2);
		// write items to lNewbytes (include header)
		System.arraycopy(stashHeader, 0, lNewbytes, 0, stashHeader.length);
		int lPos = stashHeader.length;
		for (int i = 0; i < iStashes.get(0).size(); i++){
			byte[] item_bytes = ((D2Item) iStashes.get(0).get(i)).get_bytes();
			System.arraycopy(item_bytes, 0, lNewbytes, lPos, item_bytes.length);
			lPos += item_bytes.length;
		}

		// TODO: start from idx0, loop through all tabs		
		for (int tt = 1; tt < NUM_SHARED_TABS; tt++) {
			lTabSize1 = 68;
			// write gold
			System.arraycopy(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(lTabGolds.get(tt).intValue()).array(), 0, stashHeader, 12, 4);
			for (int j = 0; j < iStashes.get(tt).size(); j++){
				lTabSize1 += ((D2Item) iStashes.get(tt).get(j)).get_bytes().length;
			}
			// write tab byte length, account for 85 bytes of header (includes JM and item count)
			System.arraycopy(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(lTabSize1).array(), 0, stashHeader, 12+4, 4);
			// write item number.  stashIdx
			System.arraycopy(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short)iStashes.get(tt).size()).array(), 0, stashHeader, 66, 2);
			// write items to lNewbytes (include header)
			System.arraycopy(stashHeader, 0, lNewbytes, lPos, stashHeader.length);
			lPos = lPos + stashHeader.length;
			for (int i = 0; i < iStashes.get(tt).size(); i++){
				byte[] item_bytes = ((D2Item) iStashes.get(tt).get(i)).get_bytes();
				System.arraycopy(item_bytes, 0, lNewbytes, lPos, item_bytes.length);
				lPos += item_bytes.length;
			}
		}

		iReader.setBytes(lNewbytes);
		iReader.set_byte_pos(0);
		iReader.save();
		setModified(false);
	}

// 	public void setGold(int pGold) throws Exception{
// 		if ( pGold < 0 )throw new Exception("gold must be greater than zero");
// 		if ( pGold > getGoldMax())throw new Exception("gold must be smaller than max" + getGoldMax() );
// 		setModified(true);
// 	}

// 	public void setGoldBank(int pGoldBank) throws Exception{
// 		if ( pGoldBank < 0 )throw new Exception("gold must be greater than zero");
// 		if ( pGoldBank > getGoldBankMax())throw new Exception("gold must be smaller than max" + getGoldBankMax() );
// 		setModified(true);
// 	}

// 	public int getGoldBankMax(){
// 		return 2500000;
// //		int lMaxGold = 50000;
// //		for ( int lLvl = 9 ; lLvl <=29 ; lLvl+=10 ){
// //			if ( iCharLevel < lLvl )return lMaxGold;
// //			lMaxGold += 50000;
// //		}
// //		if ( iCharLevel == 30 )return 200000;
// //		if ( iCharLevel == 31 )return 800000;
// //		lMaxGold = 850000;
// //		for ( int lLvl = 33 ; lLvl <=99 ; lLvl+=2 ){
// //			if ( iCharLevel <= lLvl )return lMaxGold;
// //			lMaxGold += 50000;
// //		}
// //		return 0;
// 	}

	public void fullDump(PrintWriter pWriter){
		pWriter.println(fullDumpStr());
	}

	public String fullDumpStr(){
		StringBuffer out = new StringBuffer();
		
		if ( iStashes != null ){
			// loop through 3 stash tabs, and items in each tab
			for (int i = 0; i < NUM_SHARED_TABS; i++) { 
				for ( int j = 0 ; j < iStashes.get(i).size() ; j++) {
					D2Item lItem = (D2Item) iStashes.get(i).get(j);
					out.append(lItem.itemDump(true));
					out.append("\n");
				}
			}
		}
		return out.toString();
	}

	
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

	public boolean isSC(){return !iHC;}
	public boolean isHC(){return iHC;}
	
	public String getTitleString(){return iTitleString;}
	public String getFilename()	{return iFileName;}
	public String getFileNameEnd(){
		return lFile.getName();
	}
	
}