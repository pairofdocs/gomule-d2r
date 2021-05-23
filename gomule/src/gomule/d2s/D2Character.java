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
public class D2Character extends D2ItemListAdapter
{
	public static final int BODY_INV_CONTENT   = 1;
	public static final int BODY_BELT_CONTENT  = 2; // the belt content
	public static final int BODY_CUBE_CONTENT  = 4;
	public static final int BODY_STASH_CONTENT = 5;
	public static final int BODY_CURSOR        = 10;
	public static final int BODY_HEAD          = 11;
	public static final int BODY_NECK          = 12;
	public static final int BODY_TORSO         = 13;
	public static final int BODY_RARM          = 14;
	public static final int BODY_LARM          = 15;
	public static final int BODY_LRING         = 16;
	public static final int BODY_RRING         = 17;
	public static final int BODY_BELT          = 18; // the belt itself
	public static final int BODY_BOOTS         = 19;
	public static final int BODY_GLOVES        = 20;
	public static final int BODY_RARM2         = 21;
	public static final int BODY_LARM2         = 22;
	public static final int GOLEM_SLOT         = 23;

	public static final int INVSIZEX = 10;
	public static final int INVSIZEY = 4;
	public static final int STASHSIZEX = 10;
	public static final int STASHSIZEY = 10;
	public static final int BELTSIZEX = 4;
	public static final int BELTSIZEY = 4;
	public static final int CUBESIZEX = 3;
	public static final int CUBESIZEY = 4;

	private D2BitReader iReader;
	private ArrayList iCharItems;
	private D2Item iCharCursorItem;
	private D2Item golemItem;
	private ArrayList iMercItems;
	private ArrayList iCorpseItems = new ArrayList();

	private String iCharName;
	private String iTitleString;
	private String cClass;
	private long iCharLevel;
	private int curWep = 0;
	private long lCharCode;
	private String iCharClass;
	private boolean iHC;

	private boolean[][]     iStashGrid;
	private boolean[][]     iInventoryGrid;
	private boolean[][]     iCubeGrid;
	private boolean[][]     iBeltGrid;
	private boolean[]       iEquipped;
	private boolean[]       iMerc;
	private boolean[]       iCorpse;

	private boolean[][][] iQuests = new boolean[3][5][6];
	private boolean[] cowKingDead = new boolean[3];

	private boolean[][][] iWaypoints = new boolean[3][5][9];

	private int[][] initSkills;
	private int[][] cSkills;
	private Point[] iSkillLocs;

	private int[][] setTracker = new int[33][2];

//	private int testCounter = 0;
//	private boolean fullChanged = false;
//	private ArrayList partialSetProps = new ArrayList();
//	private ArrayList fullSetProps = new ArrayList();

	private ArrayList plSkill;
	private long[] iReadStats = new long[16];
	private int[] cStats = new int[31];

	D2TxtFileItemProperties mercHireCol;
	private HashMap cMercInfo;
	private int[] mStats = new int[31];	

	private int lWoo;
	private int iWS;
	private int	iGF; 
	private int	iIF;
	private int iKF;
	private int iJF;
	private int iItemEnd;
	private byte iBeforeStats[];
	private byte iBeforeItems[];
	private byte iBetweenItems[];
	private byte iAfterItems[];

	public D2Character(String pFileName) throws Exception{
		super( pFileName );
		if ( iFileName == null || !iFileName.toLowerCase().endsWith(".d2s"))throw new Exception("Incorrect Character file name");
		iCharItems = new ArrayList();
		iMercItems = new ArrayList();
		iReader = new D2BitReader(iFileName);
		readChar();
		// clear status
		setModified(false);
	}

	private void readChar() throws Exception{
		iReader.set_byte_pos(4);
		long lVersion = iReader.read(32);
//		System.err.println("Version: " + lVersion );
		if (lVersion != 97)throw new Exception("Incorrect Character version: " + lVersion);
		iReader.set_byte_pos(8);
		long lSize = iReader.read(32);
		if (iReader.get_length() != lSize)throw new Exception("Incorrect FileSize: " + lSize);
		long lCheckSum2 = calculateCheckSum();
		iReader.set_byte_pos(12);
		boolean lChecksum = false;
		byte lCalcByte3 = (byte) ((0xff000000 & lCheckSum2) >>> 24);
		byte lCalcByte2 = (byte) ((0x00ff0000 & lCheckSum2) >>> 16);
		byte lCalcByte1 = (byte) ((0x0000ff00 & lCheckSum2) >>> 8);
		byte lCalcByte0 = (byte) (0x000000ff & lCheckSum2);
		byte lFileByte0 = (byte) iReader.read(8);
		byte lFileByte1 = (byte) iReader.read(8);
		byte lFileByte2 = (byte) iReader.read(8);
		byte lFileByte3 = (byte) iReader.read(8);
		if (lFileByte0 == lCalcByte0){
			if (lFileByte1 == lCalcByte1){
				if (lFileByte2 == lCalcByte2){
					if (lFileByte3 == lCalcByte3)lChecksum = true;
				}
			}
		}
		if (!lChecksum)throw new Exception("Incorrect Checksum");
//		long lWeaponSet = iReader.read(32);
		iReader.read(32);
		StringBuffer lCharName = new StringBuffer();
		for (int i = 0; i < 16; i++){
			long lChar = iReader.read(8);
			if (lChar != 0)lCharName.append((char) lChar);
		}
		iCharName = lCharName.toString();
		iReader.set_byte_pos(36);
		iReader.skipBits(2);
		iHC = iReader.read(1) == 1;
		iReader.set_byte_pos(37);
//		long lCharTitle = iReader.read(8);
		iReader.read(8);
		iReader.set_byte_pos(40);
		lCharCode = iReader.read(8);
		switch((int)lCharCode){
		case 0:
			cClass = "ama";
			break;
		case 1:
			cClass = "sor";
			break;
		case 2:
			cClass = "nec";
			break;
		case 3:
			cClass = "pal";
			break;
		case 4:
			cClass = "bar";
			break;
		case 5:
			cClass = "dru";
			break;
		case 6:
			cClass = "ass";
			break;
		}
		iReader.set_byte_pos(43);
		iCharLevel = iReader.read(8);
		if ( iCharLevel < 1 || iCharLevel > 99 )throw new Exception("Invalid char level: " + iCharLevel + " (should be between 1-99)");
		iCharClass =  D2TxtFile.getCharacterCode((int) lCharCode);
		iTitleString = " Lvl " + iCharLevel + " " + D2TxtFile.getCharacterCode((int) lCharCode);
		iReader.set_byte_pos(177);
		if(iReader.read(8) == 1);//MERC IS DEAD?
		iReader.skipBits(8);
		if(iReader.read(32) != 0){
			cMercInfo = new HashMap();
			iReader.skipBits(16);
			D2TxtFileItemProperties hireCol = (D2TxtFile.HIRE.searchColumns("Id", Long.toString(iReader.read(16))));
			cMercInfo.put("race", hireCol.get("Hireling"));
			cMercInfo.put("type", hireCol.get("SubType"));
			iReader.skipBits(-32);
			extractMercName(iReader.read(16), hireCol);
			iReader.skipBits(16);
			cMercInfo.put("xp", new Long(iReader.read(32)));
			setMercLevel(hireCol);
		}else{
			iReader.skipBits(64);
		}
		lWoo = iReader.findNextFlag("Woo!", 0);
		if ( lWoo == -1 )throw new Exception("Error: Act Quests block not found");
		if ( lWoo != 335 )System.err.println("Warning: Act Quests block not on expected position");
		iWS = iReader.findNextFlag("WS", lWoo);
		if ( iWS == -1 )throw new Exception("Error: Waypoints not found");
		int lW4 = iReader.findNextFlag("w4", lWoo);
		if ( lW4 == -1 )throw new Exception("Error: NPC State control block not found");
		if ( lW4 != 714 )System.err.println("Warning: NPC State control block not on expected position");
		iGF = iReader.findNextFlag("gf", lW4);
		if ( iGF == -1 )throw new Exception("Error: Stats block not found");
		if ( iGF != 765 )System.err.println("Warning: Stats block not on expected position");
		iIF = iReader.findNextFlag("if", iGF);
		if ( iIF == -1 )throw new Exception("Error: Skills block not found");
		iJF = iReader.findNextFlag("jf", iIF);
		if ( iJF == -1 )System.out.println("WTF is going on. Looks like it might be classic? USE WITH CARE!");
		iKF = iReader.findNextFlag("kf", iIF);
		if ( iKF != -1 )readGolem();
		if ( iIF < iGF )throw new Exception("Error: Stats / Skills not correct");
		readWaypoints();
		readQuests();
		try{	
			readStats();
		}catch (ArrayIndexOutOfBoundsException e){
			iIF = iReader.findNextFlag("if", iIF +1);
			if(iIF == -1){
				throw new Exception("Stats block is incorrect");
			}
			readStats();
		}
		iStashGrid = new boolean[STASHSIZEY][STASHSIZEX];
		iInventoryGrid = new boolean[INVSIZEY][INVSIZEX];
		iBeltGrid = new boolean[BELTSIZEY][BELTSIZEX];
		iCubeGrid = new boolean[CUBESIZEY][CUBESIZEX];
		iEquipped = new boolean[13];
		iMerc = new boolean[13];
		iCorpse = new boolean[13];
		clearGrid();
		readItems();
		readCorpse();
		readSkills();
		resetStats();
	}

	private void readStats() throws Exception {
		// now copy the block into the Flavie bitreader 
		// (it can read integers unaligned to bytes which is needed here) 
		iReader.set_byte_pos(iGF);
		byte lInitialBytes[] = iReader.get_bytes(iIF - iGF);

		D2FileReader lReader = new D2FileReader(lInitialBytes);
		if ( lReader.getCounterInt(8) != 103 )throw new Exception("Stats Section not found");
		if ( lReader.getCounterInt(8) != 102 )throw new Exception("Stats Section not found");
		boolean lHasStats = true;
		while ( lHasStats )	{
			// read the stats
			int lID = lReader.getCounterInt(9);
			if ( lID == 0x1ff ){
				lHasStats = false;
			}else{
				D2TxtFileItemProperties lItemStatCost = D2TxtFile.ITEM_STAT_COST.getRow(lID);
				int lBits = Integer.parseInt( lItemStatCost.get("CSvBits") );
				long lValue = lReader.getCounterLong(lBits);
				iReadStats[lID] = lValue;
			}
		}

		// check writer (just to be sure)
		byte lWritenBytes[] = getCurrentStats();
		if ( lInitialBytes.length != lWritenBytes.length )throw new Exception("Stats writer check at reading: incorrect length");
		for ( int i = 0 ; i < lInitialBytes.length ; i++ ){
			if ( lInitialBytes[i] != lWritenBytes[i] )throw new Exception("Stats writer check at reading: incorrect byte at nr: " + i);
		}

	}

	private void resetStats() {

		plSkill = new ArrayList();
		cStats[0] = getCharInitStr();
		cStats[2] = getCharInitNrg();
		cStats[4] = getCharInitDex();
		cStats[6] = getCharInitVit();
		cStats[8] = getCharInitHP();
		cStats[10] = getCharInitMana();
		cStats[12] = getCharInitStam();
		int resCounter = 0;
		for(int x = 0;x<3;x=x+1){
			if(iQuests[x][4][2] == true)resCounter ++;
		}		
		cStats[18] = cStats[18] + (10*resCounter);
		cStats[19] = cStats[19] + (10*resCounter);
		cStats[20] = cStats[20] + (10*resCounter);
		cStats[21] = cStats[21] + (10*resCounter);
		if(hasMerc()){
			ArrayList hireArr = D2TxtFile.HIRE.searchColumnsMultipleHits("SubType", getMercType());
			for(int x = 0;x<hireArr.size();x =x+1){
				if(((D2TxtFileItemProperties)hireArr.get(x)).get("Version").equals("100") && Integer.parseInt(((D2TxtFileItemProperties)hireArr.get(x)).get("Level")) <= getMercLevel()){
					mercHireCol = (D2TxtFileItemProperties)hireArr.get(x);
				}
			}
			if(mercHireCol == null){
				for(int x = 0;x<hireArr.size();x =x+1){
					if(((D2TxtFileItemProperties)hireArr.get(x)).get("Version").equals("100") && Integer.parseInt(((D2TxtFileItemProperties)hireArr.get(x)).get("Level")) > getMercLevel()){
						mercHireCol = (D2TxtFileItemProperties)hireArr.get(x);
						break;
					}
				}
			}
			mStats[18] = mStats[19] = mStats[20] = mStats[21] = (int)Math.floor((Integer.parseInt(mercHireCol.get("Resist"))+ ((Double.parseDouble(mercHireCol.get("Resist/Lvl"))/(double)4)*(getMercLevel() - Integer.parseInt(mercHireCol.get("Level"))))));
			mStats[0] = getMercInitStr();
			mStats[4] = getMercInitDex();
			mStats[8] = getMercInitHP();
			for(int x = 0;x<iMercItems.size();x++){
				equipMercItem((D2Item)iMercItems.get(x));
			}
		}
		dealWithSkills();
		for(int x = 0;x<iCharItems.size();x++){
			equipItem((D2Item)iCharItems.get(x));
		}
	}

	private void generateItemStats(D2Item cItem, int[] cStats, ArrayList plSkill, int op, int qFlagM) {

		cItem.getPropCollection().calcStats(cStats, plSkill, (int)iCharLevel, op,qFlagM);
	}

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

	private void readWaypoints() {

		iReader.set_byte_pos(iWS);
		iReader.skipBytes(10);
		for(int f = 0;f<3;f=f+1){
			for(int y = 0;y<3;y=y+1){
				for(int x = 0;x<9;x=x+1){
					if(iReader.read(1) == 1)iWaypoints[f][y][x] = true;
				}
			}
			for(int x = 0;x<3;x=x+1){
				if(iReader.read(1) == 1)iWaypoints[f][3][x] = true;
			}
			for(int x = 0;x<9;x=x+1){
				if(iReader.read(1) == 1)iWaypoints[f][4][x] = true;
			}
			iReader.skipBits(1);
			iReader.skipBytes(19);
		}
	}

	private void readQuests() {

		iReader.set_byte_pos(lWoo);
		//Skip the Woo
		iReader.skipBytes(4);
		//Read in dwActs?
		for(int x = 0;x<4;x=x+1){
			iReader.read(8);
		}
		//N NM H loop
		for(int v = 0;v<3;v=v+1){
			//Read in Act 1, Act 2, Act 3 (Each act is 6 somethings)
			for(int g = 0;g<3;g=g+1){
				iReader.skipBytes(4);
				for(int f = 0;f<6;f=f+1){
					if(iReader.read(1) == 1)iQuests[v][g][f] = true;
					if(g == 0 && f== 3){
						iReader.skipBits(9);
						if(iReader.read(1) == 1)cowKingDead[v] = true;
						iReader.skipBits(5);
					}else{
						iReader.skipBits(15);
					}
				}
			}
			iReader.skipBytes(4);
			//Read in act 4 (only 3 bits)
			for(int f = 0;f<3;f=f+1){
				if(iReader.read(1) == 1)iQuests[v][3][f] = true;
				iReader.skipBits(15);
			}
			iReader.skipBytes(2);
			iReader.skipBytes(6);
			iReader.skipBytes(2);
			iReader.skipBytes(4);
			//Read in Act 5, back to 6 bits again.
			for(int f = 0;f<6;f=f+1){
				if(iReader.read(1) == 1)iQuests[v][4][f] = true;
				iReader.skipBits(15);
			}
			iReader.skipBytes(12);
		}
		//Sort Qs
		boolean[] tempQ = new boolean[6];

		for(int x = 0;x<iQuests.length;x++){
			for(int y = 0;y<iQuests[x].length;y++){
				switch(y){
				case 0:
					tempQ[0] = iQuests[x][y][0];
					tempQ[1] = iQuests[x][y][1];
					tempQ[2] = iQuests[x][y][3];
					tempQ[3] = iQuests[x][y][4];
					tempQ[4] = iQuests[x][y][2];
					tempQ[5] = iQuests[x][y][5];
					iQuests[x][y] = tempQ;
					break;
				case 2:
					tempQ[0] = iQuests[x][y][3];
					tempQ[1] = iQuests[x][y][2];
					tempQ[2] = iQuests[x][y][1];
					tempQ[3] = iQuests[x][y][0];
					tempQ[4] = iQuests[x][y][4];
					tempQ[5] = iQuests[x][y][5];
					iQuests[x][y] = tempQ;
					break;
				case 3:
					tempQ[0] = iQuests[x][y][0];
					tempQ[1] = iQuests[x][y][2];
					tempQ[2] = iQuests[x][y][1];
					tempQ[3] = iQuests[x][y][3];
					tempQ[4] = iQuests[x][y][4];
					tempQ[5] = iQuests[x][y][5];
					iQuests[x][y] = tempQ;
					break;
				}
				tempQ = new boolean[6];	
			}
		}
	}

	private byte[] getCurrentStats(){

		D2FileWriter lWriter = new D2FileWriter();
		lWriter.setCounterInt(8, 103);
		lWriter.setCounterInt(8, 102);
		for (int lStatNr = 0; lStatNr < iReadStats.length; lStatNr++){
			if (iReadStats[lStatNr] != 0){
				lWriter.setCounterInt(9, lStatNr);
				D2TxtFileItemProperties lItemStatCost = D2TxtFile.ITEM_STAT_COST.getRow(lStatNr);
				int lBits = Integer.parseInt(lItemStatCost.get("CSvBits"));
				lWriter.setCounterInt(lBits, (int)iReadStats[lStatNr]);
			}
		}
		lWriter.setCounterInt(9, 0x1FF);
		return lWriter.getCurrentContent();
	}

	private void readSkills() {

		int[] skillC = new int[3];
		iSkillLocs = D2BodyLocations.generateSkillLocs((int)lCharCode);
		initSkills = new int[3][10];
		cSkills = new int[3][10];
		D2TxtFileItemProperties initRow = D2TxtFile.SKILLS.searchColumns("charclass", cClass);
		iReader.set_byte_pos(iIF);
		byte skillInitialBytes[] = iReader.get_bytes(32);
		D2FileReader skillReader = new D2FileReader(skillInitialBytes);
		skillReader.getCounterInt(8);
		skillReader.getCounterInt(8);
		int tree = 0;
		for(int x =0;x<30;x=x+1){
			tree = Integer.parseInt((D2TxtFile.SKILL_DESC.searchColumns("skilldesc", D2TxtFile.SKILLS.getRow(initRow.getRowNum() + x).get("skilldesc"))).get("SkillPage"));
			initSkills[tree-1][skillC[tree-1]] = skillReader.getCounterInt(8);
			skillC[tree-1]++;
		}
	}

	private void readCorpse() throws Exception {

		int corpseStart = iReader.findNextFlag("JM", iItemEnd+2);
		if(corpseStart < 0 || corpseStart > iKF || corpseStart > iJF)return;
		iReader.set_byte_pos(corpseStart);
		iReader.skipBytes(2);
		int num_items = (int) (iReader.read(8));
		int lLastItemEnd = iReader.get_byte_pos();
		for (int i = 0; i < num_items; i++){
			int lItemStart = iReader.findNextFlag("JM", lLastItemEnd);
			if (lItemStart == -1)throw new Exception("Corpse item " + (i + 1) + " not found.");
			D2Item lItem = new D2Item(iFileName, iReader, lItemStart, iCharLevel);
			lLastItemEnd = lItemStart + lItem.getItemLength();
			if ( lItem.isCursorItem() ){
				if ( iCharCursorItem != null )throw new Exception("Double cursor item found");
				iCharCursorItem = lItem;
			}else{
				addCorpseItem(lItem);
				markCorpseGrid(lItem);
			}
		}
	}

	private void readItems() throws Exception{
		int lFirstPos = iReader.findNextFlag("JM", iIF);  // 835
		if (lFirstPos == -1)throw new Exception("Character items not found");
		int lLastItemEnd = lFirstPos + 2;  // 837
		iReader.set_byte_pos(lLastItemEnd);

		int num_items = (int) iReader.read(16);
		int lCharStart = lFirstPos + 4;  // 839
		int lCharEnd = lCharStart;
		
		for (int i = 0; i < num_items; i++){
			System.err.println("IIIIIIIIIIIIIIIIIIIIIIIIII item " + String.valueOf(i) + " out of " + String.valueOf(num_items-1));
			// TODO: D2R d2s file doesn't have JM for each item. just 1JM at stat and 1 JM at end of item list
			
			int lItemStart = lCharEnd;
			//TODO: update  lItemStart for following items. use Bit position to start reading items

			D2Item lItem = new D2Item(iFileName, iReader, lItemStart, iCharLevel);
			// debugging helper function added to D2Item.java
			System.err.println("Item hex chars: " + lItem.get_bytes_string());
			System.err.println(" ");

			lLastItemEnd = iReader.get_byte_pos(); // use itemReader for bits position
			lCharEnd = lLastItemEnd + 1;           // add 1 to get to start nextItem byte, works for 2nd, 3rd, 4th... items

			if ( lItem.isCursorItem()){
				if ( iCharCursorItem != null )throw new Exception("Double cursor item found");
				iCharCursorItem = lItem;
			}else{
				addCharItem(lItem);
				markCharGrid(lItem);
			}
		}
		// print 2 items with:    D2Item tt = (D2Item)iCharItems.get(1);
		// System.err.println("Item.get(1) hex chars: " + String.valueOf(hexChars));     // both items match the hex -v'
		// System.err.println(" ");
		

		///    ************ check how the item gets saved to the cursor in D2ViewChar.java

	

		iItemEnd = lCharEnd;
		// TODO:         trace through Merc items.  disable these for now
		int lMercStart = -1;
		int lMercEnd = -1;
		lFirstPos = iReader.findNextFlag("jfJM", lCharEnd);
		
		// TODO:  add merc huffman decoding
		// if (lFirstPos != -1){
		// 	lLastItemEnd = lFirstPos + 4;
		// 	iReader.set_byte_pos(lLastItemEnd);
		// 	num_items = (int) iReader.read(16);
		// 	lMercStart = lFirstPos + 6;
		// 	lMercEnd = lMercStart;
		// 	for (int i = 0; i < num_items; i++){
		// 		int lItemStart = iReader.findNextFlag("JM", lLastItemEnd);
		// 		if (lItemStart == -1)throw new Exception("Merc item " + (i + 1) + " not found.");
		// 		D2Item lItem = new D2Item(iFileName, iReader, lItemStart, iCharLevel);
		// 		lLastItemEnd = lItemStart + lItem.getItemLength();
		// 		lMercEnd = lLastItemEnd;
		// 		addMercItem(lItem);
		// 		markMercGrid(lItem);
		// 	}
		// }
		iReader.set_byte_pos(0); // iReader.getFileContent()
		// before stats
		iBeforeStats = iReader.get_bytes(iGF);
		// goto after stats
		iReader.set_byte_pos(iIF);
		// before items
		iBeforeItems = iReader.get_bytes(lCharStart-iIF);
		if (lMercStart == -1){
			// between
			iBetweenItems = new byte[0];
			// goto after char
			iReader.set_byte_pos(lCharEnd);
			// after items
			iAfterItems = iReader.get_bytes(iReader.get_length() - lCharEnd);
		}else{
			// goto after char
			iReader.set_byte_pos(lCharEnd);
			// between
			iBetweenItems = iReader.get_bytes(lMercStart - lCharEnd);
			// goto after merc
			iReader.set_byte_pos(lMercEnd);
			// after items
			iAfterItems = iReader.get_bytes(iReader.get_length() - lMercEnd);
		}
	}

	private void readGolem() throws Exception {

		iReader.set_byte_pos(iKF);
		iReader.skipBytes(2);
		switch((int)iReader.read(8)){
		case 0:
			golemItem = null;
			return;		
		}
		int lItemStart = iReader.findNextFlag("JM", iKF);
		if (lItemStart != -1) {
//			throw new Exception("Golem item not found.");
			// Just do as if there is no golem item
			golemItem = null;
			return;		
		}
		golemItem = new D2Item(iFileName, iReader, lItemStart, iCharLevel);
	}

	private void setMercLevel(D2TxtFileItemProperties hireCol) {

		int xpPLev = Integer.parseInt(hireCol.get("Exp/Lvl"));
		long xpOut = 0;
		int lev = 0;
		do{
			xpOut = xpPLev * lev*lev*(lev+1);    		
			if(xpOut > ((Long)cMercInfo.get("xp")).longValue()){
				lev = lev -1;
				break;
			}else{
				lev = lev+1;
			}
		}while (true);
		cMercInfo.put("lvl", Integer.valueOf(lev));
	}

	private void extractMercName(long bitsIn, D2TxtFileItemProperties hireCol) {
		String nameStr = hireCol.get("NameFirst");
		int curNum = Integer.parseInt(nameStr.substring(nameStr.length() - 2, nameStr.length()));
		nameStr = nameStr.substring(0, nameStr.length() - 2);
		curNum = curNum + (int)bitsIn;
		if(curNum < 10){
			nameStr = nameStr + "0" + curNum;
		}else{
			nameStr = nameStr + curNum;
		}
		cMercInfo.put("name", D2TblFile.getString(nameStr));
	}

	private void dealWithSkills() {

		for(int s = 0;s<initSkills.length;s++){
			for(int t = 0;t<initSkills[s].length;t=t+1){
				cSkills[s][t] = initSkills[s][t];
			}
		}
		for(int x = 0;x<getPlusSkills().size();x=x+1){
			int[] pVals = ((D2Prop)getPlusSkills().get(x)).getPVals();
			switch(((D2Prop)getPlusSkills().get(x)).getPNum()){
			case(127):
				for(int s = 0;s<cSkills.length;s++){
					for(int t = 0;t<cSkills[s].length;t=t+1){
						if(cSkills[s][t] > 0)cSkills[s][t] = cSkills[s][t] + pVals[0];
					}
				}
			break;
			case(83):
				if(pVals[0] == this.getCharCode()){
					for(int s = 0;s<cSkills.length;s++){
						for(int t = 0;t<cSkills[s].length;t=t+1){
							if(cSkills[s][t] > 0)cSkills[s][t] = cSkills[s][t] + pVals[1];
						}
					}
				}	
			break;
			case(97):
				if(!D2TxtFile.SKILLS.getRow(pVals[0]).get("charclass").equals(cClass))continue;
			String page = D2TxtFile.SKILL_DESC.getRow(pVals[0]).get("SkillPage");
			int counter = 0;
			for(int z = pVals[0];z> -1;z=z-1){
				if(D2TxtFile.SKILLS.getRow(z).get("charclass").equals(cClass)){
					if(D2TxtFile.SKILL_DESC.getRow(z).get("SkillPage").equals(page)){
						counter++;
					}
				}
			}
			cSkills[Integer.parseInt(page)-1][counter-1] = cSkills[Integer.parseInt(page)-1][counter-1] + pVals[1] ;
			break;

			case(107):
				if(!D2TxtFile.SKILLS.getRow(pVals[0]).get("charclass").equals(cClass))continue;
			page = D2TxtFile.SKILL_DESC.getRow(pVals[0]).get("SkillPage");
			counter = 0;
			for(int z = pVals[0];z> -1;z=z-1){
				if(D2TxtFile.SKILLS.getRow(z).get("charclass").equals(cClass)){
					if(D2TxtFile.SKILL_DESC.getRow(z).get("SkillPage").equals(page)){
						counter++;
					}
				}
			}
			cSkills[Integer.parseInt(page)-1][counter-1] = cSkills[Integer.parseInt(page)-1][counter-1] + pVals[1] ;
			break;

			case(126):
				switch((int)lCharCode){
				case 0:
					if(cSkills[0][1] > 0)cSkills[0][1] = cSkills[0][1] + pVals[0];
					if(cSkills[0][4] > 0)cSkills[0][4] = cSkills[0][4] + pVals[0]; 
					if(cSkills[0][8] > 0)cSkills[0][8] = cSkills[0][8] + pVals[0]; 
					break;
				case 1:
					for(int t = 0;t<10;t=t+1){
						if(cSkills[0][t] > 0)cSkills[0][t] = cSkills[0][t] + pVals[0];
					}
					break;
				case 2:
					if(cSkills[0][3] > 0)cSkills[0][3] = cSkills[0][3] + pVals[0];
					if(cSkills[0][8] > 0)cSkills[0][8] = cSkills[0][8] + pVals[0];
					break;
				case 3:
					if(cSkills[0][1] > 0)cSkills[0][1] = cSkills[0][1] + pVals[0];
					break;
				case 5:
					if(cSkills[2][0] > 0)cSkills[2][0] = cSkills[2][0] + pVals[0];
					if(cSkills[2][1] > 0)cSkills[2][1] = cSkills[2][1] + pVals[0];
					if(cSkills[1][6] > 0)cSkills[1][6] = cSkills[1][6] + pVals[0];
					if(cSkills[2][3] > 0)cSkills[2][3] = cSkills[2][3] + pVals[0];
					if(cSkills[2][6] > 0)cSkills[2][6] = cSkills[2][6] + pVals[0];
					if(cSkills[2][8] > 0)cSkills[2][8] = cSkills[2][8] + pVals[0];
					break;
				case 6:
					if(cSkills[2][2] > 0)cSkills[2][2] = cSkills[2][2] + pVals[0];
					if(cSkills[2][6] > 0)cSkills[2][6] = cSkills[2][6] + pVals[0];
					if(cSkills[0][0] > 0)cSkills[0][0] = cSkills[0][0] + pVals[0];
					if(cSkills[0][4] > 0)cSkills[0][4] = cSkills[0][4] + pVals[0];
					if(cSkills[0][7] > 0)cSkills[0][7] = cSkills[0][7] + pVals[0];
					break;
				}
			break;
			case(188):
				for(int t = 0;t<10;t=t+1){
					if((pVals[0]-(getCharCode() * 8) ) > -1 && (pVals[0]-(getCharCode() * 8) ) < 4){  
						if(cSkills[pVals[0]-(getCharCode() * 8)][t] > 0)cSkills[pVals[0]-(getCharCode() * 8)][t] = cSkills[pVals[0]-(getCharCode() * 8)][t] + pVals[1];
					}
				}
			}
		}
	}

	public void changeWep(){

		switch(curWep){
		case 0:
			for(int x = 0;x<getCharItemNr();x=x+1){
				if(getCharItem(x).get_body_position() == 4 ||getCharItem(x).get_body_position() == 5){
					updateCharStats("P", getCharItem(x));
				}
			}	
			curWep = 1;	
			for(int x = 0;x<getCharItemNr();x=x+1){
				if(getCharItem(x).get_body_position() == 11 ||getCharItem(x).get_body_position() == 12){
					updateCharStats("D", getCharItem(x));	
				}
			}
			return;
		case 1:
			for(int x = 0;x<iCharItems.size();x=x+1){
				if(getCharItem(x).get_body_position() == 11 ||getCharItem(x).get_body_position() == 12){
					updateCharStats("P", getCharItem(x));	
				}
			}
			curWep = 0;
			for(int x = 0;x<iCharItems.size();x=x+1){
				if(getCharItem(x).get_body_position() == 4 ||getCharItem(x).get_body_position() == 5){
					updateCharStats("D", getCharItem(x));
				}
			}

			return;
		}
	}

	public int getARClassBonus(){
		if(getCharClass().equals("Barbarian") || getCharClass().equals("Paladin")){
			return 20;
		}else if(getCharClass().equals("Assasin")){
			return 15;
		}else if(getCharClass().equals("Amazon")|| getCharClass().equals("Druid")){
			return 5;
		}else if(getCharClass().equals("Necromancer")){
			return -10;
		}else if(getCharClass().equals("Sorceress")){
			return -15;
		}else{
			return 99999999;
		}
	}

	public ArrayList getItemList(){
		ArrayList lList = new ArrayList();
		if ( iCharItems != null )lList.addAll( iCharItems );
		if ( iMercItems != null )lList.addAll( iMercItems );
		return lList;
	}

	public boolean containsItem(D2Item pItem){
		if ( iCharItems.contains(pItem))return true;
		if ( iMercItems.contains(pItem))return true;
		return false;
	}

	public void removeItem(D2Item pItem){
		if ( iCharItems.remove(pItem) ){
			unmarkCharGrid(pItem);
		}else{
			unmarkMercGrid(pItem);
			iMercItems.remove(pItem);
		}
		setModified(true);
	}

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

		for (int i = 0; i < iEquipped.length; i++)iEquipped[i] = false;
		for (int i = 0; i < iMerc.length; i++)iMerc[i] = false;
		for (int i = 0; i < iCorpse.length; i++)iCorpse[i] = false;
		for (int i = 0; i < BELTSIZEY; i++){
			for (int j = 0; j < BELTSIZEX; j++)iBeltGrid[i][j] = false;
		}
		for (int i = 0; i < STASHSIZEY; i++){
			for (int j = 0; j < STASHSIZEX; j++)iStashGrid[i][j] = false;
		}
		for (int i = 0; i < INVSIZEY; i++){
			for (int j = 0; j < INVSIZEX; j++)iInventoryGrid[i][j] = false;
		}
		for (int i = 0; i < CUBESIZEY; i++){
			for (int j = 0; j < CUBESIZEX; j++)iCubeGrid[i][j] = false;
		}
	}

	public boolean markCharGrid(D2Item i){
		short panel = i.get_panel();
		int row, col, width, height, j, k;
		switch (panel){
		case 0: // equipped or on belt
			int location = (int) i.get_location();
			if (location == 2){
				col = (int) i.get_col();
				row = col / 4;
				col = col % 4;
				width = (int) i.get_width();
				height = (int) i.get_height();
				if ((row + height) > 4)return false;
				if ((col + width) > 4)return false;
				for (j = row; j < row + height; j++){
					for (k = col; k < col + width; k++)iBeltGrid[j][k] = true;
				}
			}else if (location == 6){

			}else{
				int body_position = (int) i.get_body_position();
				if (iEquipped[body_position] == true){
					return false;
				}else{
					iEquipped[body_position] = true;
				}
			}
			break;
		case BODY_INV_CONTENT: // inventory
			row = (int) i.get_row();
			col = (int) i.get_col();
			width = (int) i.get_width();
			height = (int) i.get_height();
			if ((row + height) > 4)	return false;
			if ((col + width) > 10)return false;
			for (j = row; j < row + height; j++){
				for (k = col; k < col + width; k++)	iInventoryGrid[j][k] = true;
			}
			break;
		case BODY_CUBE_CONTENT: // cube
			row = (int) i.get_row();
			col = (int) i.get_col();
			width = (int) i.get_width();
			height = (int) i.get_height();
			if ((row + height) > 4)	return false;
			if ((col + width) > 3)return false;
			for (j = row; j < row + height; j++){
				for (k = col; k < col + width; k++)iCubeGrid[j][k] = true;
			}
			break;
		case BODY_STASH_CONTENT: // stash
			row = (int) i.get_row();
			col = (int) i.get_col();
			width = (int) i.get_width();
			height = (int) i.get_height();
			if ((row + height) > 10)return false;   // ***8*** stash size int and not a variable set here!
			if ((col + width) > 10)return false;    // ***6*** stash size
			for (j = row; j < row + height; j++){
				for (k = col; k < col + width; k++)iStashGrid[j][k] = true;
			}
			break;
		}
		return true;
	}

	public boolean markMercGrid(D2Item i){
		short panel = i.get_panel();
		if (panel == 0){
			int body_position = (int) i.get_body_position();
			if (iMerc[body_position - 1] == true){
				return false;
			}else{
				iMerc[body_position - 1] = true;
			}
		}
		return true;
	}

	public boolean markCorpseGrid(D2Item i){
		short panel = i.get_panel();
		switch (panel){
		case 0: // equipped or on belt
			int location = (int) i.get_location();
			if (location == 2){
			}else if (location == 6){
//				in socket
			}else{
				int body_position = (int) i.get_body_position();
				if (iCorpse[body_position] == true){
					return false;
				}else{
					iCorpse[body_position] = true;
				}
			}
			break;
		}
		return true;
	}

	public ArrayList getBeltPotions(){
		ArrayList lList = new ArrayList();
		for (int i=0;i<4;i++) {
			for (int j=1;j<4;j++) {
				int y = getCharItemIndex(2, i, j);
				if (y != -1)lList.add((D2Item)iCharItems.get(y));
			}
		}
		return lList;
	}

	public boolean unmarkCharGrid(D2Item i){
		short panel = i.get_panel();
		int row, col, width, height, j, k;
		switch (panel){
		case 0: // equipped or on belt
			int location = (int) i.get_location();
			// on the belt
			if (location == 2){
				col = (int) i.get_col();
				row = col / 4;
				col = col % 4;
				width = (int) i.get_width();
				height = (int) i.get_height();
				if ((row + height) > 4)return false;
				if ((col + width) > 4)return false;
				for (j = row; j < row + height; j++){
					for (k = col; k < col + width; k++)iBeltGrid[j][k] = false;
				}
			}else if (location == 6){
//				in socket?
			}else{
				int body_position = (int) i.get_body_position();
				iEquipped[body_position] = false;
			}
			break;
		case BODY_INV_CONTENT: // inventory
			row = (int) i.get_row();
			col = (int) i.get_col();
			width = (int) i.get_width();
			height = (int) i.get_height();
			if ((row + height) > 4)return false;
			if ((col + width) > 10)return false;
			for (j = row; j < row + height; j++){
				for (k = col; k < col + width; k++)	iInventoryGrid[j][k] = false;
			}
			break;
		case BODY_CUBE_CONTENT: // cube
			row = (int) i.get_row();
			col = (int) i.get_col();
			width = (int) i.get_width();
			height = (int) i.get_height();
			if ((row + height) > 4)return false;
			if ((col + width) > 3)return false;
			for (j = row; j < row + height; j++){
				for (k = col; k < col + width; k++)	iCubeGrid[j][k] = false;
			}
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

	public boolean unmarkMercGrid(D2Item i)	{
		short panel = i.get_panel();
		if (panel == 0){
			int body_position = (int) i.get_body_position();
			iMerc[body_position - 1] = false;
		}
		return true;
	}

	public void addCharItem(D2Item pItem){
		iCharItems.add(pItem);
		pItem.setCharLvl((int)iCharLevel);
		setModified(true);
	}

	public void addCorpseItem(D2Item pItem){
		iCorpseItems .add(pItem);
		pItem.setCharLvl((int)iCharLevel);
		setModified(true);
	}

	public void addMercItem(D2Item pItem){
		iMercItems.add(pItem);
		pItem.setCharLvl((int)iCharLevel);
		setModified(true);
	}

	public void removeCharItem(int i){
		iCharItems.remove(i);
		setModified(true);
	}

	public void removeMercItem(int i){
		iMercItems.remove(i);
		setModified(true);
	}

	public boolean checkCharGrid(int panel, int x, int y, D2Item pItem){
		int i, j;
		int w = pItem.get_width();
		int h = pItem.get_height();
		switch (panel){
		case BODY_INV_CONTENT:
			for (i = x; i < x + w; i++){
				for (j = y; j < y + h; j++){
					if (j >= iInventoryGrid.length || i >= iInventoryGrid[j].length || iInventoryGrid[j][i])return false;
				}
			}
			break;
		case BODY_BELT_CONTENT:
			if (!pItem.isBelt())return false;
			for (i = x; i < x + w; i++){
				for (j = y; j < y + h; j++){
					if (j >= iBeltGrid.length || i >= iBeltGrid[j].length || iBeltGrid[j][i])return false;
				}
			}
			break;
		case BODY_CUBE_CONTENT:
			for (i = x; i < x + w; i++){
				for (j = y; j < y + h; j++){
					if (j >= iCubeGrid.length || i >= iCubeGrid[j].length || iCubeGrid[j][i])return false;
				}
			}
			break;
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

	public boolean checkCorpsePanel(int panel, int x, int y, D2Item pItem){
		if (panel >= 10){
			if (pItem == null)return iCorpse[panel - 10];
			if (iCorpse[panel - 10])return true;
			switch (panel){
			case BODY_HEAD:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_HEAD))return false;
				break;
			case BODY_NECK:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_NECK))return false;
				break;
			case BODY_LARM:
			case BODY_LARM2:
				if (pItem.isBodyLArm())return false;
				break;
			case BODY_TORSO:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_TORS))return false;
				break;
			case BODY_RARM:
			case BODY_RARM2:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_RARM))return false;
				break;
			case BODY_GLOVES:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_GLOV))return false;
				break;
			case BODY_RRING:
				if (pItem.isBodyRRin())return false;
				break;
			case BODY_BELT:
				if ( pItem.isBodyLocation(D2BodyLocations.BODY_BELT))return false;
				break;
			case BODY_LRING:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_LRIN))return false;
				break;
			case BODY_BOOTS:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_FEET))return false;
				break;
			case BODY_CURSOR:
				return false;
			}
			return true;
		}
		switch (panel)
		{
		case BODY_INV_CONTENT:
			if (y >= 0 && y < iInventoryGrid.length){
				if (x >= 0 && x < iInventoryGrid[y].length)return iInventoryGrid[y][x];
			}
			return false;
		case BODY_BELT_CONTENT:
			if (y >= 0 && y < iBeltGrid.length){
				if (x >= 0 && x < iBeltGrid[y].length)return iBeltGrid[y][x];
			}
			return false;
		case BODY_CUBE_CONTENT:
			if (y >= 0 && y < iCubeGrid.length){
				if (x >= 0 && x < iCubeGrid[y].length)return iCubeGrid[y][x];
			}
			return false;
		case BODY_STASH_CONTENT:
			if (y >= 0 && y < iStashGrid.length){
				if (x >= 0 && x < iStashGrid[y].length)return iStashGrid[y][x];
			}
			return false;
		}
		return true;
	}

	public boolean checkCharPanel(int panel, int x, int y, D2Item pItem){
		if (panel >= 10){
			if (pItem == null)return iEquipped[panel - 10];
			if (iEquipped[panel - 10])return true;
			switch (panel){
			case BODY_HEAD:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_HEAD))return false;
				break;
			case BODY_NECK:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_NECK))return false;
				break;
			case BODY_LARM:
			case BODY_LARM2:
				if (pItem.isBodyLArm())	return false;
				break;
			case BODY_TORSO:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_TORS))return false;
				break;
			case BODY_RARM:
			case BODY_RARM2:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_RARM))return false;
				break;
			case BODY_GLOVES:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_GLOV))return false;
				break;
			case BODY_RRING:
				if (pItem.isBodyRRin())return false;
				break;
			case BODY_BELT:
				if ( pItem.isBodyLocation(D2BodyLocations.BODY_BELT))return false;
				break;
			case BODY_LRING:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_LRIN))return false;
				break;
			case BODY_BOOTS:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_FEET))return false;
				break;
			case BODY_CURSOR:
				return false;
			}
			return true;
		}
		switch (panel){
		case BODY_INV_CONTENT:
			if (y >= 0 && y < iInventoryGrid.length){
				if (x >= 0 && x < iInventoryGrid[y].length)return iInventoryGrid[y][x];
			}
			return false;
		case BODY_BELT_CONTENT:
			if (y >= 0 && y < iBeltGrid.length){
				if (x >= 0 && x < iBeltGrid[y].length)return iBeltGrid[y][x];
			}
			return false;
		case BODY_CUBE_CONTENT:
			if (y >= 0 && y < iCubeGrid.length){
				if (x >= 0 && x < iCubeGrid[y].length)return iCubeGrid[y][x];
			}
			return false;
		case BODY_STASH_CONTENT:
			if (y >= 0 && y < iStashGrid.length){
				if (x >= 0 && x < iStashGrid[y].length)return iStashGrid[y][x];
			}
			return false;
		}
		return true;
	}

	public boolean checkMercPanel(int panel, int x, int y, D2Item pItem){
		if (panel > 10){
			if (pItem == null)return iMerc[panel - 10 - 1];
			if (iMerc[panel - 10 - 1])return true;
			switch (panel){
			case BODY_HEAD:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_HEAD))return false;
				break;
			case BODY_LARM:
				if (pItem.isBodyLArm())return false;
				break;
			case BODY_TORSO:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_TORS))return false;
				break;
			case BODY_RARM:
				if (pItem.isBodyLocation(D2BodyLocations.BODY_RARM))return false;
				break;
			}
			return true;
		}
		return true;
	}

	public int getCharItemIndex(int panel, int x, int y){  // iRow,  iCol are input
		// System.err.println("int panel: " + String.valueOf(panel));
		// System.err.println("int x: " + String.valueOf(x));
		// System.err.println("int y: " + String.valueOf(y));
		if (panel == BODY_BELT_CONTENT){
			for (int i = 0; i < iCharItems.size(); i++){
				D2Item temp_item = (D2Item) iCharItems.get(i);
				if (temp_item.get_location() == panel){
					if (temp_item.get_col() == 4 * y + x)return i;
				}
			}
		}else if (panel >= 10){
			for (int i = 0; i < iCharItems.size(); i++){
				D2Item temp_item = (D2Item) iCharItems.get(i);
				if (temp_item.get_location() != 0 && temp_item.get_location() != 2){
					if (temp_item.get_panel() == 0){
						if (temp_item.get_body_position() == panel - 10){
							return i;
						}
					}
				}
			}
		}else{
			for (int i = 0; i < iCharItems.size(); i++){
				D2Item temp_item = (D2Item) iCharItems.get(i);
				if (temp_item.get_panel() == panel)	{
					int col = temp_item.get_col();
					int row = temp_item.get_row();
					// System.err.println("col: " + String.valueOf(col));
					// System.err.println("row: " + String.valueOf(row));
					if ((x >= col) && (x <= col + temp_item.get_width() - 1) && (y >= row) && (y <= row + temp_item.get_height() - 1)) {
						// System.err.println("found item, return item index " + String.valueOf(i));
						return i;
					}
				}
			}
		}
		return -1;
	}

	public int getCorpseItemIndex(int panel, int x, int y){
		if (panel == BODY_BELT_CONTENT){
			for (int i = 0; i < iCorpseItems.size(); i++){
				D2Item temp_item = (D2Item) iCorpseItems.get(i);
				if (temp_item.get_location() == panel){
					if (temp_item.get_col() == 4 * y + x)return i;
				}
			}
		}else if (panel >= 10){
			for (int i = 0; i < iCorpseItems.size(); i++){
				D2Item temp_item = (D2Item) iCharItems.get(i);
				if (temp_item.get_panel() == panel)	{
					int row = temp_item.get_col();
					int col = temp_item.get_row();
					if (x >= row && x <= row + temp_item.get_width() - 1 && y >= col && y <= col + temp_item.get_height() - 1)return i;
				}
			}
		}else{
			for (int i = 0; i < iCorpseItems.size(); i++){
				D2Item temp_item = (D2Item) iCorpseItems.get(i);
				if (temp_item.get_panel() == panel){
					int row = temp_item.get_col();
					int col = temp_item.get_row();
					if (x >= row && x <= row + temp_item.get_width() - 1 && y >= col && y <= col + temp_item.get_height() - 1)return i;
				}
			}
		}
		return -1;
	}

	public int getMercItemIndex(int panel, int x, int y){
		if (panel >= 10){
			for (int i = 0; i < iMercItems.size(); i++){
				D2Item temp_item = (D2Item) iMercItems.get(i);
				if (temp_item.get_panel() == 0){
					if (temp_item.get_body_position() == panel - 10)return i;
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
		int lCharSize = 0;
		System.err.println("lCharSize: " + String.valueOf(lCharSize));

		for (int i = 0; i < iCharItems.size(); i++){
			lCharSize += ((D2Item) iCharItems.get(i)).get_bytes().length;
		}
		System.err.println("lCharSize after iCharItems loop: " + String.valueOf(lCharSize));

		if ( iCharCursorItem != null ){
			lCharSize += iCharCursorItem.get_bytes().length;
		}
		int lMercSize = 0;
		if (hasMerc()){
			System.err.println("hasMerc true");
			for (int i = 0; i < iMercItems.size(); i++)	{
				lMercSize += ((D2Item) iMercItems.get(i)).get_bytes().length;
			}
		}
		byte lWritenBytes[] = getCurrentStats();
		byte[] lNewbytes = new byte[iBeforeStats.length + lWritenBytes.length + iBeforeItems.length + lCharSize + iBetweenItems.length + lMercSize + iAfterItems.length];
		int lPos = 0;
		System.arraycopy(iBeforeStats, 0, lNewbytes, lPos, iBeforeStats.length);
		lPos += iBeforeStats.length;
		System.err.println("lPos after 1st arraycopy: " + String.valueOf(lPos));

		System.arraycopy(lWritenBytes, 0, lNewbytes, lPos, lWritenBytes.length);
		lPos += lWritenBytes.length;
		System.err.println("lPos after 2nd arraycopy: " + String.valueOf(lPos));

		System.arraycopy(iBeforeItems, 0, lNewbytes, lPos, iBeforeItems.length);
		lPos += iBeforeItems.length;
		System.err.println("lPos after 3nd arraycopy: " + String.valueOf(lPos));

		int lCharItemCountPos = lPos - 2;      // <<<<< is this correct for D2R?  subtract 2 for JM ?
		int lMercItemCountPos = -1;           // <<<<< is this correct for D2R?
		for (int i = 0; i < iCharItems.size(); i++){
			D2Item item_temp = (D2Item) iCharItems.get(i);
			byte[] item_bytes = (item_temp).get_bytes();
			System.arraycopy(item_bytes, 0, lNewbytes, lPos, item_bytes.length);
			System.err.println("item_bytes in forloop " + String.valueOf(i) + " :" + item_temp.get_bytes_string());
			
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
			// Should this be enabled?    I never read the merc's items yet. so don't write anything
			// Merc start jf JM ... and end is kf.        so    lMercItemCountPos = lPos + 2  -->   4A(J) 4D(M) 00 00   is the corpse.
			System.err.println("hasMerc true after copying all items");
			System.arraycopy(iBetweenItems, 0, lNewbytes, lPos, iBetweenItems.length);
			lPos += iBetweenItems.length;
			lMercItemCountPos = lPos + 2;              // lPos - 2 <<<<< is this correct for D2R?     NO!  found it!    should be +2, skip the 2'JM' chars
			System.err.println("lMercItemCountPos: " + String.valueOf(lMercItemCountPos));

			for (int i = 0; i < iMercItems.size(); i++){
				byte[] item_bytes = ((D2Item) iMercItems.get(i)).get_bytes();
				System.arraycopy(item_bytes, 0, lNewbytes, lPos, item_bytes.length);
				lPos += item_bytes.length;
			}
			System.err.println("lPos after all mercitems copied: " + String.valueOf(lPos));
		}
		if (iAfterItems.length > 0){
			System.err.println("iAfterItems.length > 0");
			// This array is created up at the top, where items are read from the file
			System.arraycopy(iAfterItems, 0, lNewbytes, lPos, iAfterItems.length);
		}
		iReader.setBytes(lNewbytes); // iReader.getFileContent()
		iReader.set_byte_pos(lCharItemCountPos);
		int lCharItemsCount = iCharItems.size();
		if ( iCharCursorItem != null ){
			lCharItemsCount++;
		}
		iReader.write(lCharItemsCount, 16);
		if (hasMerc()){
			System.err.println("hasMerc true after writing lCharItemsCount");
			iReader.set_byte_pos(lMercItemCountPos);
			System.err.println("lMercItemCountPos :" + String.valueOf(lMercItemCountPos));

			iReader.write(iMercItems.size(), 16);         // <<<<<<<<<<  could this be the issue?,  writin 00 for merc itemssize?.  Yes. this was writing 0x00 at the last item byte.
			// Merc item reading should be fixed and this can be enabled and debugged.
			// For now.  0x00 is written before JM _ _ jfJM  (start of Merc).  the corpse bytes
			System.err.println("iMercItems.size() :" + String.valueOf(iMercItems.size()));
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
		iReadStats[14] = pGold;
		setModified(true);
	}

	public void setGoldBank(int pGoldBank) throws Exception{
		if ( pGoldBank < 0 )throw new Exception("gold must be greater than zero");
		if ( pGoldBank > getGoldBankMax())throw new Exception("gold must be smaller than max" + getGoldBankMax() );
		iReadStats[15] = pGoldBank;
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
		out.append(getStatString());
		out.append("\n\n");
		
		ArrayList skillArr = D2TxtFile.SKILLS.searchColumnsMultipleHits("charclass", cClass);
		String[] skillTrees = new String[]{"","",""};
		int[] skillCounter = new int[3];
		
		for(int x = 0;x<skillArr.size();x=x+1){
			int page = Integer.parseInt((D2TxtFile.SKILL_DESC.getRow(Integer.parseInt(((D2TxtFileItemProperties)skillArr.get(x)).get("Id")))).get("SkillPage"));
			skillTrees[page-1] = skillTrees[page-1] + D2TblFile.getString(D2TxtFile.SKILL_DESC.searchColumns("skilldesc",((D2TxtFileItemProperties)skillArr.get(x)).get("skilldesc")).get("str name"))+ ": " +  initSkills[page-1][skillCounter[page-1]] + "/" + cSkills[page-1][skillCounter[page-1]] + "\n";
			skillCounter[page -1] ++;
		}
		
		for(int x = 0;x<skillTrees.length;x++){
			out.append(skillTrees[x]);
			out.append("\n");
		}

		if ( iCharItems != null ){
			for ( int i = 0 ; i < iCharItems.size() ; i++){
				D2Item lItem = (D2Item) iCharItems.get(i);
				out.append(lItem.itemDump(true));
				out.append("\n");
			}
		}
		out.append("Mercenary:"+"\n");
		out.append("\n");
		
		out.append(getMercStatString());
		out.append("\n\n");
		
		if ( iMercItems != null ){
			for ( int i = 0 ; i < iMercItems.size() ; i++){
				D2Item lItem = (D2Item) iMercItems.get(i);
				out.append(lItem.itemDump(true));
				out.append("\n\n");
			}
		}
		return out.toString();
	}

	public void updateCharStats(String string, D2Item temp) {

		if(string.equals("P"))unequipItem(temp);
		if(string.equals("D"))equipItem(temp);
	}

	public void equipItem(D2Item item){
		if(!item.isEquipped(curWep))return;  
		if(item.isSet())addSetItem(item);
		generateItemStats(item, cStats, plSkill, 1,0);
		dealWithSkills();

	}

	public void unequipItem(D2Item item){
		if(!item.isEquipped(curWep))return;  
		if(item.isSet())remSetItem(item);
		generateItemStats(item, cStats, plSkill, -1,0);
		dealWithSkills();
	}

	private void addSetItem(D2Item item) {
		int setNo = D2TxtFile.FULLSET.searchColumns("index", D2TxtFile.SETITEMS.getRow(item.getSetID()).get("set")).getRowNum();
		setTracker[setNo][0] ++ ;
		for(int x = 0;x<iCharItems.size();x++){
			if(!((D2Item) iCharItems.get(x)).isEquipped(curWep))continue;
			if( D2TxtFile.FULLSET.searchColumns("index", D2TxtFile.SETITEMS.getRow(((D2Item)(iCharItems.get(x))).getSetID()).get("set")).getRowNum() == setNo){
				modSetProps(((D2Item) iCharItems.get(x)), setTracker[setNo], 1);
			}		
		}
	}

	private void modSetProps(D2Item sItem, int[] trackVal , int op){

		for(int x = 0;x<sItem.getPropCollection().size();x++){
			switch(op){
			case(1):
				if((((D2Prop)sItem.getPropCollection().get(x)).getQFlag() <= (trackVal[0]) && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() > 1 && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() < 7)){
					((D2Prop)sItem.getPropCollection().get(x)).setQFlag(((D2Prop)sItem.getPropCollection().get(x)).getQFlag() + 10);
					((D2Prop)sItem.getPropCollection().get(x)).addCharMods(cStats, plSkill, (int)iCharLevel, 1, 1);
				}else if((((D2Prop)sItem.getPropCollection().get(x)).getQFlag() <= (20+trackVal[0]) && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() > 21 && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() < 26)){
					//Add display for partial set
					((D2Prop)sItem.getPropCollection().get(x)).setQFlag(((D2Prop)sItem.getPropCollection().get(x)).getQFlag() + 10);
				}else if((sItem.getSetSize() == trackVal[0] && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() == 26)){
					//Add display for full set
					((D2Prop)sItem.getPropCollection().get(x)).setQFlag(((D2Prop)sItem.getPropCollection().get(x)).getQFlag() + 10);				
				}
			break;
			case(-1):
				if((((D2Prop)sItem.getPropCollection().get(x)).getQFlag() >= (trackVal[0]+10) && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() > 11 && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() < 17)){
					((D2Prop)sItem.getPropCollection().get(x)).addCharMods(cStats, plSkill, (int)iCharLevel, -1, 1);
					((D2Prop)sItem.getPropCollection().get(x)).setQFlag(((D2Prop)sItem.getPropCollection().get(x)).getQFlag() - 10);
				}else if( (((D2Prop)sItem.getPropCollection().get(x)).getQFlag() >= (30+trackVal[0]) && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() > 31 && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() < 36)){
					//Add display for partial set
					((D2Prop)sItem.getPropCollection().get(x)).setQFlag(((D2Prop)sItem.getPropCollection().get(x)).getQFlag() - 10);
				}else if((sItem.getSetSize() == trackVal[0] && ((D2Prop)sItem.getPropCollection().get(x)).getQFlag() == 36)){
					//Add Display for full set
					((D2Prop)sItem.getPropCollection().get(x)).setQFlag(((D2Prop)sItem.getPropCollection().get(x)).getQFlag() - 10);					
				}
			break;
			}
		}
		sItem.refreshItemMods();
	}

	private void remSetItem(D2Item item) {

		int setNo = D2TxtFile.FULLSET.searchColumns("index", D2TxtFile.SETITEMS.getRow(item.getSetID()).get("set")).getRowNum();
		//Since the item we have just removed is no longer equipped (so not in icharitems) we need
		//to remove it first.
		modSetProps(item, new int[]{0}, -1);
		for(int x = 0;x<iCharItems.size();x++){
			if(!((D2Item) iCharItems.get(x)).isEquipped(curWep))continue;
			if( D2TxtFile.FULLSET.searchColumns("index", D2TxtFile.SETITEMS.getRow(((D2Item)(iCharItems.get(x))).getSetID()).get("set")).getRowNum() == setNo){
				modSetProps(((D2Item) iCharItems.get(x)), setTracker[setNo], -1);
			}		
		}
		setTracker[setNo][0]  -- ;
	}

	public void updateMercStats(String string, D2Item dropItem) {

		if(string.equals("P"))unequipMercItem(dropItem);
		if(string.equals("D"))equipMercItem(dropItem);
	}

	public String getMercName() {
		if(!cMercInfo.containsKey("name"))return "";
		return (String) cMercInfo.get("name");
	}

	public String getMercType() {
		if(!cMercInfo.containsKey("type"))return "";
		return (String) cMercInfo.get("type");
	}

	public long getMercExp() {
		if(!cMercInfo.containsKey("xp"))return 0;
		return ((Long) cMercInfo.get("xp")).longValue();
	}

	public String getMercRace() {
		if(!cMercInfo.containsKey("race"))return "";
		return (String) cMercInfo.get("race");
	}

	public int getMercLevel() {
		if(!cMercInfo.containsKey("lvl"))return 0;
		return ((Integer) cMercInfo.get("lvl")).intValue();
	}

	public boolean getMercDead() {
		if(!cMercInfo.containsKey("dead"))return false;
		return ((Boolean) cMercInfo.get("dead")).booleanValue();
	}

	public long getCharDef() {
		int cDef = 0;
		for(int x = 0;x<iCharItems.size();x++){
			if(((D2Item)iCharItems.get(x)).isTypeArmor()){
				if(((D2Item)iCharItems.get(x)).isEquipped()){
					cDef = cDef + ((D2Item)iCharItems.get(x)).getiDef();
				}
			}
		}
		return  (int) (Math.floor((double)getCharDex()/(double)4)) + cDef;
	}

	public long getMercDef() {
		int cDef = 0;
		for(int x = 0;x<iMercItems.size();x++){
			if(((D2Item)iMercItems.get(x)).isTypeArmor()){
				if(((D2Item)iMercItems.get(x)).isEquipped()){
					cDef = cDef + ((D2Item)iMercItems.get(x)).getiDef();
				}
			}
		}
		return  (long)(Math.floor((double)getMercInitDex()/(double)4) +Integer.parseInt(mercHireCol.get("Defense"))+ (Integer.parseInt(mercHireCol.get("Def/Lvl"))*(getMercLevel() - Integer.parseInt(mercHireCol.get("Level"))))) + cDef;
	}

	public int getCharBlock() {
		int iBlock = 0;
		for(int x = 0;x<iCharItems.size();x++){
			if(((D2Item)iCharItems.get(x)).isEquipped(curWep)){
				iBlock = iBlock + ((D2Item)iCharItems.get(x)).getBlock();
			}
		}
		return (int)Math.floor(((cStats[30]+iBlock + Integer.parseInt(D2TxtFile.CHARSTATS.searchColumns("class", getCharClass()).get("BlockFactor"))) * (getCharDex() - 15))/(iCharLevel * 2));
	}

	public void addItem(D2Item item){equipItem(item);};
	public D2Item getCursorItem(){return iCharCursorItem;}
	public D2Item getCharItem(int i){return (D2Item) iCharItems.get(i);}
	public D2Item getMercItem(int i){return (D2Item) iMercItems.get(i);}
	public D2Item getCorpseItem(int i){return (D2Item) iCorpseItems.get(i);}
	public D2Item getGolemItem() {return golemItem;}

	public void equipMercItem(D2Item item){generateItemStats(item, mStats, null, 1,0);}
	public void unequipMercItem(D2Item item){generateItemStats(item, mStats, null,-1,0);}

	public int getNrItems(){return iCharItems.size() + iMercItems.size();}
	public int getCharItemNr(){return iCharItems.size();}
	public int getMercItemNr(){return iMercItems.size();}
	public int getCorpseItemNr(){return iCorpseItems.size();}

	public Point[] getSkillLocs(){return iSkillLocs;}
	public int[] getSkillListA(){return cSkills[0];}
	public int[] getSkillListB(){return cSkills[1];}
	public int[] getSkillListC(){return cSkills[2];}
	public int[] getInitSkillListA(){return initSkills[0];}
	public int[] getInitSkillListB(){return initSkills[1];}
	public int[] getInitSkillListC(){return initSkills[2];}

	public boolean[][][] getQuests(){return iQuests;}
	public boolean getCowKingDead(int difficulty){return cowKingDead[difficulty];}
	public boolean[][][] getWaypoints(){return iWaypoints;}

	public int getGold(){return (int)iReadStats[14];}
	public int getGoldMax(){return 10000*((int) iCharLevel);}
	public int getGoldBank(){return (int)iReadStats[15];}

	public boolean isSC(){return !iHC;}
	public boolean isHC(){return iHC;}
	public boolean hasMerc(){return cMercInfo != null;}
	public int getCharCode() {return (int)lCharCode;}
	public String getCharClass(){return iCharClass;}
	public String getTitleString(){return iTitleString;}
	public String getFilename()	{return iFileName;}
	public String getCharName(){return iCharName;}

	public long getCharExp(){return iReadStats[13];}
	public int getCharStr(){return cStats[0] + cStats[1];}
	public int getCharDex(){return cStats[4] + cStats[5];}
	public int getCharNrg(){return cStats[2] + cStats[3];}
	public int getCharVit(){return cStats[6] + cStats[7];}
	public int getCharRemStat(){return (int) iReadStats[4];}
	public int getCharRemSkill(){return (int) iReadStats[5];}
	public int getCharMana(){return cStats[10] + cStats[11];}
	public int getCharStam(){return cStats[12] + cStats[13];}
	public int getCharHP(){return cStats[8] + cStats[9];}
	public int getCharFireRes() {return cStats[18];}
	public int getCharColdRes() {return cStats[20];}
	public int getCharLightRes() {return cStats[19];}
	public int getCharPoisRes() {return cStats[21];}
	public int getCharInitStr(){return (int) iReadStats[0];}
	public int getCharInitDex(){return (int) iReadStats[2];}
	public int getCharInitNrg(){return (int) iReadStats[1];}
	public int getCharInitVit(){return (int) iReadStats[3];}
	public int getCharInitMana(){return (int) iReadStats[9]/256;}
	public int getCharInitStam(){return (int) iReadStats[11]/256;}
	public int getCharInitHP(){return (int) iReadStats[7]/256;}
	public int getCharInitDef(){return (int) (Math.floor((double)getCharInitDex()/(double)4));}
	public int getCharInitAR(){return ((getCharInitDex() * 5) - 35) + getARClassBonus();}
	public int getCharLevel(){return (int)iCharLevel;}
	public int getCharAR() {return (((getCharDex() * 5) - 35) + getARClassBonus() + cStats[14] + cStats[16]) * (1 + cStats[15] + cStats[17]);}

	public int getCharMF(){return cStats[22] + cStats[23];}
	public int getCharGF(){return cStats[28] + cStats[29];}
	public int getCharFHR(){return cStats[27];}
	public int getCharIAS(){return cStats[26];}
	public int getCharFRW(){return cStats[24];}
	public int getCharFCR(){return cStats[25];}
	public int getCharSkillRem(){return (int) iReadStats[5];}
	public ArrayList getPlusSkills(){return plSkill;}

	public int getMercInitStr(){return (int)Math.floor((Integer.parseInt(mercHireCol.get("Str"))+ ((Double.parseDouble(mercHireCol.get("Str/Lvl"))/(double)8)*(getMercLevel() - Integer.parseInt(mercHireCol.get("Level"))))));}
	public int getMercInitDex(){return (int)Math.floor((Integer.parseInt(mercHireCol.get("Dex"))+ ((Double.parseDouble(mercHireCol.get("Dex/Lvl"))/(double)8)*(getMercLevel() - Integer.parseInt(mercHireCol.get("Level"))))));}
	public int getMercInitHP(){return (int)Math.floor((Integer.parseInt(mercHireCol.get("HP"))+ ((Double.parseDouble(mercHireCol.get("HP/Lvl")))*(getMercLevel() - Integer.parseInt(mercHireCol.get("Level"))))));}
	public long getMercInitDef(){return (long)(Math.floor((double)getMercInitDex()/(double)4) +Integer.parseInt(mercHireCol.get("Defense"))+ (Integer.parseInt(mercHireCol.get("Def/Lvl"))*(getMercLevel() - Integer.parseInt(mercHireCol.get("Level")))));}
	public int getMercInitAR(){return ((getMercInitDex() * 5) - 35) + (int)Math.floor((Integer.parseInt(mercHireCol.get("AR"))+ ((Double.parseDouble(mercHireCol.get("AR/Lvl"))/(double)8)*(getMercLevel() - Integer.parseInt(mercHireCol.get("Level"))))));}
	public int getMercStr(){return mStats[0] + mStats[1];}
	public int getMercDex(){return mStats[4] + mStats[5];}
	public int getMercHP(){return mStats[8] + mStats[9];}
	public int getMercAR(){return (((getMercInitDex() * 5) - 35) + (int)Math.floor((Integer.parseInt(mercHireCol.get("AR"))+ ((Double.parseDouble(mercHireCol.get("AR/Lvl"))/(double)8)*(getMercLevel() - Integer.parseInt(mercHireCol.get("Level"))))))+ mStats[14] + mStats[16]) * (1 + mStats[15] + mStats[17]);}
	public int getMercFireRes(){return mStats[18];}
	public int getMercColdRes(){return mStats[20];}
	public int getMercLightRes(){return mStats[19];}
	public int getMercPoisRes(){return mStats[21];}

	public String getStatString() {
		return
		"Name:       " + getCharName() + "\n"+
		"Class:      " + getCharClass() + "\n"+
		"Experience: " + getCharExp() + "\n"+
		"Level:      " + getCharLevel() + "\n"+
		/*"NOTIMP:     " + getCharDead() + "\n"+*/ "\n"+"            Naked/Gear" + "\n"+
		"Strength:   " + getCharInitStr()+"/"+getCharStr() + "\n"+
		"Dexterity:  " + getCharInitDex()+"/"+getCharDex() + "\n"+
		"Vitality:   " + getCharInitVit()+"/"+getCharVit() + "\n"+
		"Energy:     " + getCharInitNrg()+"/"+getCharNrg() + "\n"+
		"HP:         " + getCharInitHP()+"/"+getCharHP() + "\n"+
		"Mana:       " + getCharInitMana()+"/"+getCharMana() + "\n"+
		"Stamina:    " + getCharInitStam()+"/"+getCharStam() + "\n"+
		"Defense:    " +getCharInitDef()+"/"+getCharDef() + "\n"+
		"AR:         " + getCharInitAR()+"/"+getCharAR() + "\n"+ "\n"+
		"Fire:       " + getCharFireRes()+"/"+(getCharFireRes()-40) +"/"+(getCharFireRes()-100) + "\n"+
		"Cold:       " + getCharColdRes()+"/"+(getCharColdRes()-40) +"/"+(getCharColdRes()-100) + "\n"+
		"Lightning:  " + getCharLightRes()+"/"+(getCharLightRes()-40) +"/"+(getCharLightRes()-100) + "\n"+
		"Poison:     " + getCharPoisRes()+"/"+(getCharPoisRes()-40) +"/"+(getCharPoisRes()-100) + "\n"+"\n"+
		"MF:         " + getCharMF() + "       Block:      "+ getCharBlock() + "\n"+
		"GF:         " +getCharGF()  + "       FR/W:       " +getCharFRW()+ "\n"+
		"FHR:        " +getCharFHR() + "       IAS:        " +getCharIAS()+ "\n"+
		"FCR:        " +getCharFCR();
	}

	public String getMercStatString() {

		if(hasMerc()){
			return	"Name:       " + getMercName() + "\n"+
			"Race:       " + getMercRace() + "\n"+
			"Type:       " + getMercType() + "\n"+
			"Experience: " + getMercExp() + "\n"+
			"Level:      " + getMercLevel() + "\n"+
			"Dead?:      " + getMercDead() + "\n"+ "\n"+"            Naked/Gear" + "\n"+
			"Strength:   " + getMercInitStr()+"/"+getMercStr() + "\n"+
			"Dexterity:  " + getMercInitDex()+"/"+getMercDex() + "\n"+
			"HP:         " + getMercInitHP()+"/"+getMercHP() + "\n"+
			"Defense:    " +getMercInitDef()+"/"+getMercDef() + "\n"+
			"AR:         " + getMercInitAR()+"/"+getMercAR() + "\n"+ "\n"+
			"Fire:       " + getMercFireRes()+"/"+(getMercFireRes()-40) +"/"+(getMercFireRes()-100) + "\n"+
			"Cold:       " + getMercColdRes()+"/"+(getMercColdRes()-40) +"/"+(getMercColdRes()-100) + "\n"+
			"Lightning:  " + getMercLightRes()+"/"+(getMercLightRes()-40) +"/"+(getMercLightRes()-100) + "\n"+
			"Poison:    " + getMercPoisRes()+"/"+(getMercPoisRes()-40) +"/"+(getMercPoisRes()-100);
		}else{
			return "";
		}

	}

}