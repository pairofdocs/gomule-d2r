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

package gomule.item;

import gomule.util.*;
import java.awt.Color;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import randall.d2files.*;
import randall.flavie.*;

//an item class
//manages one item
//keeps the a copy of the bytes representing
//an item and a bitreader to manipulate them
//also stores most data from the item in

//this class is NOT designed to edit items
//any methods that allow the item's bytes
//to be written only exist to facillitate
//moving items. writing other item fields
//is not supported by this class
public class D2Item implements Comparable, D2ItemInterface {

	private D2PropCollection iProps = new D2PropCollection();

	private ArrayList iSocketedItems;

    // huff0 assemble
    private Object[] n2 = new Object[] {'2', 'n'};
    private Object[] n2x = new Object[] {n2, 'x'};
    private Object[] sn2x = new Object[] {'s', n2x};    // ['s', [['2', 'n'], 'x']]

    private Object[] blnk = new Object[] {};
    private Object[] jbl = new Object[] {'j', blnk};
    private Object[] fjbl = new Object[] {'5', jbl};

    private Object[] yjbl = new Object[] {'y', fjbl};   // ['y', ['5', ['j', []]]]
    private Object[] ejbl = new Object[] {'8', yjbl};

    private Object[] ejbh = new Object[] {ejbl, 'h'};
    private Object[] wu = new Object[] {'w', 'u'};

    private Object[] wubh = new Object[] {wu, ejbh};
    private Object[] wunx = new Object[] {wubh, sn2x}; // [[['w', 'u'], [['8', ['y', ['5', ['j', []]]]], 'h']], ['s', [['2', 'n'], 'x']]]

    private Object[] tm = new Object[] {'t', 'm'};
    private Object[] n7 = new Object[] {'9', '7'};

    private Object[] tmn7 = new Object[] {tm, n7};  // [['t', 'm'], ['9', '7']]

    private Object[] kf = new Object[] {'k', 'f'};
    private Object[] ckf = new Object[] {'c', kf};

    private Object[] ckfb = new Object[] {ckf, 'b'};
    private Object[] ckn7 = new Object[] {ckfb, tmn7};  // [[['c', ['k', 'f']], 'b'], [['t', 'm'], ['9', '7']]]

    private Object[] huff0 = new Object[] {wunx, ckn7};  // huff0 works ~~~~ -v'

    // huff1 assemble
    private Object[] f0 = new Object[] {'4', '0'};
    private Object[] of0 = new Object[] {'1', f0};

    private Object[] io = new Object[] {'i', 'o'};
    private Object[] ofio = new Object[] {of0, io};
    private Object[] aofio = new Object[] {'a', ofio};  // ['a', [['1', ['4', '0']], ['i', 'o']]]

    private Object[] rl = new Object[] {'r', 'l'};
    private Object[] r1io = new Object[] {rl, aofio};

    private Object[] zq = new Object[] {'z', 'q'};
    private Object[] zq3 = new Object[] {zq, '3'};

    private Object[] v6 = new Object[] {'v', '6'};
    private Object[] zqv6 = new Object[] {zq3, v6};

    private Object[] gzv6 = new Object[] {'g', zqv6};  // ['g', [[['z', 'q'], '3'], ['v', '6']]]
    private Object[] ed = new Object[] {'e', 'd'};

    private Object[] edp = new Object[] {ed, 'p'};
    private Object[] edv6 = new Object[] {edp, gzv6}; // [[[['e', 'd'], 'p'], ['g', [[['z', 'q'], '3'], ['v', '6']]]]

    private Object[] ed1io = new Object[] {edv6, r1io}; 
    private Object[] huff1 = new Object[] {' ', ed1io};

    private Object[] huff = new Object[] {huff0, huff1};

	private int flags;

	private short version;

	private short location;

	private short body_position;

	private short row;

	private short col;

	private short panel;

	private String item_type;

	private short iSocketNrFilled = 0;

	private short iSocketNrTotal = 0;

	private long fingerprint;

	private short ilvl;

	private short quality;

	private short gfx_num;

	private D2TxtFileItemProperties automod_info;

	private short[] rare_prefixes;

	private short[] rare_suffixes;

	private String personalization;

	private short width;

	private short height;

	private String image_file;

	private D2TxtFileItemProperties iItemType;

	private String iType;

	private String iType2;

	private boolean iEthereal;

	private boolean iSocketed;

	private boolean iThrow;

	private boolean iMagical;

	private boolean iRare;

	private boolean iCrafted;

	private boolean iSet;

	private boolean iUnique;

	private boolean iRuneWord;

	private boolean iSmallCharm;

	private boolean iLargeCharm;

	private boolean iGrandCharm;

	private boolean iJewel;

//	private boolean iEquipped = false;

	private boolean iGem;

	private boolean iStackable = false;

	private boolean iRune;

	private boolean iTypeMisc;

	private boolean iIdentified;

	private boolean iTypeWeapon;

	private boolean iTypeArmor;

	protected String iItemName;

	protected String iBaseItemName;

	private short iCurDur;

	private short iMaxDur;

	private short iDef;

	private short cBlock;

	private short iBlock;

	private short iInitDef;

	private short[] i1Dmg;
	private short[] i2Dmg;

	// 0 FOR BOTH 1 FOR 1H 2 FOR 2H
	private int iWhichHand;

	// private int iLvl;
	private String iFP;

	private String iGUID;

	private boolean iBody = false;

	private String iBodyLoc1;

	private String iBodyLoc2;

	private boolean iBelt = false;

	private D2BitReader iItem;

	private String iFileName;

	private boolean iIsChar;

	private int iCharLvl;

	private int iReqLvl = -1;

	private int iReqStr = -1;

	private int iReqDex = -1;

	private String iSetName;

	private int setSize;

	private String iItemQuality = "none";

	private short set_id;

	public D2Item(String pFileName, D2BitReader pFile, int pPos, long pCharLvl)
	throws Exception {
		iFileName = pFileName;
		iIsChar = iFileName.endsWith(".d2s");
		iCharLvl = (int) pCharLvl;

		try {
			pFile.set_byte_pos(pPos);
			read_item(pFile, pPos);
			int lCurrentReadLength = pFile.get_pos() - pPos * 8;
			int lNextJMPos = pFile.findNextFlag("JM", pFile.get_byte_pos());
			int lLengthToNextJM = lNextJMPos - pPos; //end of itemlist

			if (lLengthToNextJM < 0) {
				int lNextKFPos = pFile.findNextFlag("kf", pFile.get_byte_pos());
				int lNextJFPos = pFile.findNextFlag("jf", pFile.get_byte_pos());
				if (lNextJFPos >= 0) {

					lLengthToNextJM = lNextJFPos - pPos;

				} else if (lNextKFPos >= 0) {
					lLengthToNextJM = lNextKFPos - pPos;
				}

				else {
					// last item (for stash only)
					lLengthToNextJM = pFile.get_length() - pPos;
				}
			} else if ((lNextJMPos > pFile.findNextFlag("kf", pFile
					.get_byte_pos()))
					&& (pPos < pFile.findNextFlag("kf", pFile.get_byte_pos()))) {
				lLengthToNextJM = pFile
				.findNextFlag("kf", pFile.get_byte_pos())
				- pPos;
			} else if ((lNextJMPos > pFile.findNextFlag("jf", pFile
					.get_byte_pos()))
					&& (pPos < pFile.findNextFlag("jf", pFile.get_byte_pos()))) {

				lLengthToNextJM = pFile
				.findNextFlag("jf", pFile.get_byte_pos())
				- pPos;

			}

			//int lDiff = ((lLengthToNextJM * 8) - lCurrentReadLength);
			// if (lDiff > 7) {  // This is throwing an exception.     ltoNextJM*8 = 
			// 	throw new D2ItemException(
			// 			"Item not read complete, missing bits: " + lDiff
			// 			+ getExStr());
			// }

			// get position of end of current item
			int lDiff = (pFile.get_pos() - 1)/8 - pPos;   // want to go from 839 to 849 if there is an extra byte 0x00
			if ((pFile.get_pos()%8 == 0) && (this.iSocketed) && (this.iSocketNrFilled>0))
				lDiff = pFile.get_pos()/8 - pPos; // if no remainer when div by 8. do not subtract 1bit 

			pFile.set_byte_pos(pPos);  // set back to start of current item
			iItem = new D2BitReader(pFile.get_bytes(lDiff + 1));  // read len Bytes + 1 to cover end item byte
			
			pFile.set_byte_pos(pPos + lDiff);   // current item ended on 848, add 1 to get to 839
			// TODO:  **** check if extraByte has to be added to iItem = new D2Bitreader.   May have to complete partial bytes. items start on their own byte.

		} catch (D2ItemException pEx) {
			throw pEx;
		} catch (Exception pEx) {
			pEx.printStackTrace();
			throw new D2ItemException("Error: " + pEx.getMessage() + getExStr());
		}
	}

	// read basic information from the bytes
	// common to all items, then split based on
	// whether the item is an ear
	private void read_item(D2BitReader pFile, int pos) throws Exception {
		// pFile.skipBytes(2); // skip 'JM'.  JM isn't present for items in D2R
		flags = (int) pFile.unflip(pFile.read(32), 32); // 4 bytes

		iSocketed = check_flag(12);
		iEthereal = check_flag(23);
		iRuneWord = check_flag(27);
		iIdentified = check_flag(5);
		// version = (short) pFile.read(8);   // read 1 byte.     do not read this for D2R
		pFile.read(3);
		version = 101; // default to version 101 for v1.10+

		// pFile.skipBits(2);   // D2 1.14 does readbyte (8bits) then skip 2 bits --> 10bits skipped total
		
		// debugging helper
		byte[] barr = pFile.get_bytes(2);                       // does not increment position
		char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[barr.length * 2];
		for (int j = 0; j < barr.length; j++) {
			int v = barr[j] & 0xFF;
			hexChars[j * 2] = HEX_ARRAY[v >>> 4];
			hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
		}
		// System.err.println("Hex chars: " + String.valueOf(hexChars));
		
		// TODO: are the 3 bits (for version) correctly accounted for when writing an item?
		// pFile.skipBits(3);      // D2R     does    '101' bits          3bits skipped total  (item.ts  in d2s repo)
		location = (short) pFile.read(3);  // loc 2 -- belt

		body_position = (short) pFile.read(4);
		col = (short) pFile.read(4);
		row = (short) pFile.read(4);
		panel = (short) pFile.read(3);

		// flag 17 is an ear
		if (!check_flag(17)) {
            
			// add huffman encoding to readExtend
			readExtend(pFile);
		} else {
			read_ear(pFile);
		}

		//Need to tidy up the properties before the item mods are calculated.
		iProps.deleteUselessProperties();


		if (isTypeArmor() || isTypeWeapon()) {
//			Blunt does 150 damage to undead
			if (iType.equals("club") || iType.equals("scep")
					|| iType.equals("mace") || iType.equals("hamm")) {
				iProps.add(new D2Prop(122, new int[] { 150 }, 0));
			}
			applyItemMods();
		}
	}

	// read ear related data from the bytes
	private void read_ear(D2BitReader pFile) {

		int eClass = (int) pFile.read(3);
		int eLevel = (int) (pFile.read(7));

		StringBuffer lCharName = new StringBuffer();
		for (int i = 0; i < 18; i++)
			// while(1==1)
		{
			long lChar = pFile.read(7);
			if (lChar != 0) {
				lCharName.append((char) lChar);
			} else {
				pFile.read(7);
				break;
			}
		}
		iItemType = D2TxtFile.search("ear");
		height = Short.parseShort(iItemType.get("invheight"));
		width = Short.parseShort(iItemType.get("invwidth"));
		image_file = iItemType.get("invfile");
		iBaseItemName = iItemName = lCharName.toString() + "'s Ear";

		iProps.add(new D2Prop(185, new int[] { eClass, eLevel }, 0, true, 39));

		// for (int i = 0; i < 18; i++) {
		// pFile.read(7); // name
		// }
	}

	// read non ear data from the bytes,
	// setting class variables for easier access
	private void readExtend(D2BitReader pFile) throws Exception {
		// 9,5 bytes already read (common data)
		item_type = "";
		// skip spaces or hashing won't work
		
		// read bytes and decode using HUFFMAN
		for (int i = 0; i < 32; i++) {          // 4 chars * 8bits each = 32 max bits needed to encode string
			int bitread = (int) pFile.read(1);  // convert to int
			
			if (huff[bitread].getClass().isArray())
				huff = (Object[]) huff[bitread];
			else {
				item_type += huff[bitread];
				if (item_type.length() == 4){
					item_type = item_type.stripTrailing(); // right strip ending ' '
					break;
				}
				huff = new Object[] {huff0, huff1};
			}
		} 

		// This is the old 1.14 loop to assemble item_type
		// for (int i = 0; i < 4; i++) {
		// 	// char is gotten from the nested list. 
		// 	// have to readbits and access index of HUFFMAN.  huff[0] or huff[1] bit 0/1
		
		//     // example 8 bits,  00010101,  skip3
		// 	char c = (char) pFile.read(8); // 4 bytes
		// 	System.err.println("char c:");
		// 	System.err.println(c);
		// 	if (c != 32) {
		// 		item_type += c;
		// 	}
		// }

		iItemType = D2TxtFile.search(item_type);
		height = Short.parseShort(iItemType.get("invheight"));
		width = Short.parseShort(iItemType.get("invwidth"));
		image_file = iItemType.get("invfile");

		String lD2TxtFileName = iItemType.getFileName();
		if (lD2TxtFileName != null) {
			iTypeMisc = ("Misc".equals(lD2TxtFileName));
			iTypeWeapon = ("weapons".equals(lD2TxtFileName));
			iTypeArmor = ("armor".equals(lD2TxtFileName));
		}

		iType = iItemType.get("type");
		iType2 = iItemType.get("type2");

		// Shields - block chance.
		if (isShield()) {
			cBlock = Short.parseShort(iItemType.get("block"));
		}

		// Requerements
		if (iTypeMisc) {
			iReqLvl = getReq(iItemType.get("levelreq"));
		} else if (iTypeArmor) {
			iReqLvl = getReq(iItemType.get("levelreq"));
			iReqStr = getReq(iItemType.get("reqstr"));

			D2TxtFileItemProperties qualSearch = D2TxtFile.ARMOR.searchColumns(
					"normcode", item_type);
			iItemQuality = "normal";
			if (qualSearch == null) {
				qualSearch = D2TxtFile.ARMOR.searchColumns("ubercode",
						item_type);
				iItemQuality = "exceptional";
				if (qualSearch == null) {
					qualSearch = D2TxtFile.ARMOR.searchColumns("ultracode",
							item_type);
					iItemQuality = "elite";
				}
			}

		} else if (iTypeWeapon) {
			iReqLvl = getReq(iItemType.get("levelreq"));
			iReqStr = getReq(iItemType.get("reqstr"));
			iReqDex = getReq(iItemType.get("reqdex"));

			D2TxtFileItemProperties qualSearch = D2TxtFile.WEAPONS
			.searchColumns("normcode", item_type);
			iItemQuality = "normal";
			if (qualSearch == null) {
				qualSearch = D2TxtFile.WEAPONS.searchColumns("ubercode",
						item_type);
				iItemQuality = "exceptional";
				if (qualSearch == null) {
					qualSearch = D2TxtFile.WEAPONS.searchColumns("ultracode",
							item_type);
					iItemQuality = "elite";
				}
			}
		}

		String lItemName = D2TblFile.getString(item_type);
		if (lItemName != null) {
			iItemName = lItemName;
			iBaseItemName = iItemName;
		}

		// flag 22 is a simple item (extend1)
		if (!check_flag(22)) {
			readExtend1(pFile);
		}

		// gold (?)
		if ("gold".equals(item_type)) {
			if (pFile.read(1) == 0) {
				pFile.read(12);
			} else {
				pFile.read(32);
			}
		}

		long lHasGUID = pFile.read(1);

		if (lHasGUID == 1) { // GUID ???
			if (iType.startsWith("rune") || iType.startsWith("gem")
					|| iType.startsWith("amu") || iType.startsWith("rin")
					|| isCharm()|| !isTypeMisc()) {

				iGUID = "0x" + Integer.toHexString((int) pFile.read(32))
				+ " 0x" + Integer.toHexString((int) pFile.read(32))
				+ " 0x" + Integer.toHexString((int) pFile.read(32));
			} else {
				pFile.read(3);
			}
		}

		// flag 22 is a simple item (extend2)
		if (!check_flag(22)) {
			readExtend2(pFile);
		}

		if (iType != null && iType2 != null && iType.startsWith("gem")) {
			if (iType2.equals("gem0") || iType2.equals("gem1")
					|| iType2.equals("gem2") || iType2.equals("gem3")
					|| iType2.equals("gem4")) {
				readPropertiesGems(pFile);
				iGem = true;
			}
		}

		if (iType != null && iType2 != null && iType.startsWith("rune")) {
			readPropertiesGems(pFile);
			iRune = true;
		}

		D2TxtFileItemProperties lItemType = D2TxtFile.ITEM_TYPES.searchColumns(
				"Code", iType);

		if (lItemType == null) {
			lItemType = D2TxtFile.ITEM_TYPES.searchColumns("Equiv1", iType);
			if (lItemType == null) {
				lItemType = D2TxtFile.ITEM_TYPES.searchColumns("Equiv2", iType);
			}
		}

		if ("1".equals(lItemType.get("Body"))) {
			iBody = true;
			iBodyLoc1 = lItemType.get("BodyLoc1");
			iBodyLoc2 = lItemType.get("BodyLoc2");
		}
		if ("1".equals(lItemType.get("Beltable"))) {
			iBelt = true;
			readPropertiesPots(pFile);
		}

		int lLastItem = (pFile.get_pos() - 1)/8;   // Set this correctly for last D2R item.  
		// saw for ISC item that bit position was 7176, exactly at byte 897.  subtract 1 bit since the pPos bit hasn't been read technically

		if (iSocketNrFilled > 0) {
			iSocketedItems = new ArrayList();

			for (int i = 0; i < iSocketNrFilled; i++) {
				int lStartNewItem = lLastItem + 1;  // from logging, this has the correct byte pos when dealing with socket item
				
				// int lStartNewItem = pFile.findNextFlag("JM", lLastItem);  // <<<<<<<<<< fix the JM reference
				D2Item lSocket = new D2Item(iFileName, pFile, lStartNewItem,
						iCharLvl);
				if (i == iSocketNrFilled-1)
					lLastItem = lStartNewItem + lSocket.getItemLength();  // debugging and see that the last socket item doesn't need the bits-1 subtraction
				else
					lLastItem = lStartNewItem + (lSocket.getItemLength()*8 -1)/8;  // Check if accurate for D2R. may need bits-1 /8
				
				iSocketedItems.add(lSocket);

				if (lSocket.isJewel()) {
					iProps.addAll(lSocket.getPropCollection(), 1);
				} else	if (isTypeWeapon()) {
					iProps.addAll(lSocket.getPropCollection(), 7);
				} else if (isTypeArmor()) {
					if (iType.equals("tors") || iType.equals("helm")
							|| iType.equals("phlm") || iType.equals("pelt")
							|| iType.equals("cloa") || iType.equals("circ")) {
						iProps.addAll(lSocket.getPropCollection(), 8);
					} else {
						iProps.addAll(lSocket.getPropCollection(), 9);
					}
				}
				if (lSocket.iReqLvl > iReqLvl) {
					iReqLvl = lSocket.iReqLvl;
				}

			}
		}

		if (iRuneWord) {
			ArrayList lList = new ArrayList();
			for (int i = 0; i < iSocketedItems.size(); i++) {
				lList.add(((D2Item) iSocketedItems.get(i)).getRuneCode());
			}

			D2TxtFileItemProperties lRuneWord = D2TxtFile.RUNES
			.searchRuneWord(lList);
			if (lRuneWord != null) {
				iItemName = D2TblFile.getString(lRuneWord.get("Name"));
			}
		}

		if (iSocketNrFilled > 0 && isNormal()) {
			iItemName = "Gemmed " + iItemName;
		}

		if (iItemName != null) {
			iItemName = iItemName.trim();

		}

		if (iBaseItemName != null) {
			iBaseItemName = iBaseItemName.trim();

		}

		if (iEthereal) {
			if (iReqStr != -1) {
				iReqStr -= 10;
			}
			if (iReqDex != -1) {
				iReqDex -= 10;
			}
		}
	}

	private void readExtend1(D2BitReader pFile) throws Exception {
		// extended item
		iSocketNrFilled = (short) pFile.read(3);
		fingerprint = pFile.read(32);
		iFP = "0x" + Integer.toHexString((int) fingerprint);
		ilvl = (short) pFile.read(7);
		quality = (short) pFile.read(4);
		iProps = new D2PropCollection();
		// check variable graphic flag
		gfx_num = -1;
		if (pFile.read(1) == 1) {
			gfx_num = (short) pFile.read(3);
			if (iItemType.get("namestr").compareTo("cm1") == 0) {
				iSmallCharm = true;
				image_file = "invch" + ((gfx_num) * 3 + 1);
			} else if (iItemType.get("namestr").compareTo("cm2") == 0) {
				iLargeCharm = true;
				image_file = "invch" + ((gfx_num) * 3 + 2);
			} else if (iItemType.get("namestr").compareTo("cm3") == 0) {
				iGrandCharm = true;
				image_file = "invch" + ((gfx_num) * 3 + 3);
			} else if (iItemType.get("namestr").compareTo("jew") == 0) {
				iJewel = true;
				image_file = "invjw" + (gfx_num + 1);
			} else {
				image_file += (gfx_num + 1);
			}
		}
		// check class info flag
		if (pFile.read(1) == 1) {
			automod_info = D2TxtFile.AUTOMAGIC.getRow((int) pFile.read(11) - 1);
		}

		// path determined by item quality
		switch (quality) {
		case 1: // low quality item
		{
			short low_quality = (short) pFile.read(3);

			switch (low_quality) {

			case 0: {
				iItemName = "Crude " + iItemName;
				break;
			}

			case 1: {
				iItemName = "Cracked " + iItemName;
				break;
			}

			case 2: {
				iItemName = "Damaged " + iItemName;
				break;
			}

			case 3: {
				iItemName = "Low Quality " + iItemName;
				break;
			}

			}

			break;
		}
		case 3: // high quality item
		{
			iItemName = "Superior " + iItemName;
			iBaseItemName = iItemName;
			// 3bytes, don't know what they are.
			pFile.read(3);
			break;
		}
		case 4: // magic item
		{
			iMagical = true;
			short magic_prefix = (short) pFile.read(11);
			short magic_suffix = (short) pFile.read(11);

			if (magic_suffix == 0) {
				magic_suffix = 10000;
			}

			D2TxtFileItemProperties lPrefix = D2TxtFile.PREFIX
			.getRow(magic_prefix);
			String lPreName = lPrefix.get("Name");
			if (lPreName != null && !lPreName.equals("")) {
				iItemName = D2TblFile.getString(lPreName) + " " + iItemName;
				int lPreReq = getReq(lPrefix.get("levelreq"));
				if (lPreReq > iReqLvl) {
					iReqLvl = lPreReq;
				}
			}

			D2TxtFileItemProperties lSuffix = D2TxtFile.SUFFIX
			.getRow(magic_suffix);
			String lSufName = lSuffix.get("Name");
			if (lSufName != null && !lSufName.equals("")) {
				iItemName = iItemName + " " + D2TblFile.getString(lSufName);
				int lSufReq = getReq(lSuffix.get("levelreq"));
				if (lSufReq > iReqLvl) {
					iReqLvl = lSufReq;
				}
			}
			applyAutomodLvl();
			break;
		}
		case 5: // set item
		{
			iSet = true;
			set_id = (short) pFile.read(12);
			if (gfx_num == -1) {
				String s = (String) iItemType.get("setinvfile");
				if (s.compareTo("") != 0)
					image_file = s;
			}

			D2TxtFileItemProperties lSet = D2TxtFile.SETITEMS.getRow(set_id);
			iItemName = D2TblFile.getString(lSet.get("index"));
			iSetName = lSet.get("set");

			setSize = (D2TxtFile.SETITEMS.searchColumnsMultipleHits("set",
					iSetName)).size();

			int lSetReq = getReq(lSet.get("lvl req"));
			if (lSetReq != -1 && lSetReq > iReqLvl) {
				iReqLvl = lSetReq;
			}

			applyAutomodLvl();
			addSetProperties(D2TxtFile.FULLSET.searchColumns("index", D2TxtFile.SETITEMS.getRow(set_id).get("set")));
			break;
		}
		case 7: {
			iUnique = true;
			short unique_id = (short) pFile.read(12);
			String s = iItemType.get("uniqueinvfile");
			if (s.compareTo("") != 0) {
				image_file = s;
			}

			D2TxtFileItemProperties lUnique = D2TxtFile.UNIQUES
			.getRow(unique_id);
			String lNewName = D2TblFile.getString(lUnique.get("index"));
			if (lNewName != null) {
				iItemName = lNewName;
			}


			if(lUnique.get("code").equals(item_type)){
				int lUniqueReq = getReq(lUnique.get("lvl req"));
				if (lUniqueReq != -1) {
					iReqLvl = lUniqueReq;
				}
			}
			applyAutomodLvl();
			break;
		}
		case 6: // rare item
		{
			iRare = true;
			iItemName = "Rare " + iItemName;
		}
		case 8: // also a rare item, do the same (one's probably crafted)
		{
			if(!iRare){
				iCrafted = true;
				iItemName = "Crafted " + iItemName;
			}
		}

		applyAutomodLvl();
		short rare_name_1 = (short) pFile.read(8);
		short rare_name_2 = (short) pFile.read(8);
		D2TxtFileItemProperties lRareName1 = D2TxtFile.RAREPREFIX
		.getRow(rare_name_1 - 156);
		D2TxtFileItemProperties lRareName2 = D2TxtFile.RARESUFFIX
		.getRow(rare_name_2 - 1);
		iItemName = D2TblFile.getString(lRareName1.get("name")) + " "
		+ D2TblFile.getString(lRareName2.get("name"));

		rare_prefixes = new short[3];
		rare_suffixes = new short[3];
		short pre_count = 0;
		short suf_count = 0;
		for (int i = 0; i < 3; i++) {
			if (pFile.read(1) == 1) {
				rare_prefixes[pre_count] = (short) pFile.read(11);
				D2TxtFileItemProperties lPrefix = D2TxtFile.PREFIX
				.getRow(rare_prefixes[pre_count]);
				pre_count++;
				String lPreName = lPrefix.get("Name");
				if (lPreName != null && !lPreName.equals("")) {
					int lPreReq = getReq(lPrefix.get("levelreq"));
					if (lPreReq > iReqLvl) {
						iReqLvl = lPreReq;
					}
				}

			}
			if (pFile.read(1) == 1) {
				rare_suffixes[suf_count] = (short) pFile.read(11);
				D2TxtFileItemProperties lSuffix = D2TxtFile.SUFFIX
				.getRow(rare_suffixes[suf_count]);
				suf_count++;
				String lSufName = lSuffix.get("Name");
				if (lSufName != null && !lSufName.equals("")) {
					int lSufReq = getReq(lSuffix.get("levelreq"));
					if (lSufReq > iReqLvl) {
						iReqLvl = lSufReq;
					}
				}
			}
		}

		if (isCrafted()) {
			iReqLvl = iReqLvl + 10 + (3 * (suf_count + pre_count));
		}
		break;

		case 2: {
			readTypes(pFile);
			break;
		}
		}

		// rune word
		if (check_flag(27)) {
			pFile.skipBits(12);
			pFile.skipBits(4);
		}
		// personalized
		if (check_flag(25)) {
			personalization = "";
			boolean lNotEnded = true;
			for (int i = 0; i < 15 && lNotEnded; i++) {
				char c = (char) pFile.read(7);
				if (c == 0) {
					lNotEnded = false;
				} else {
					personalization += c;
				}
			}
			if (lNotEnded == true) {
				pFile.read(7);
			}
		}
	}

	private void addSetProperties(D2TxtFileItemProperties fullsetRow) {

		for(int x = 2 ;x<6;x++){
			if(fullsetRow.get("PCode"+x+"a").equals(""))continue;
			iProps.addAll(D2TxtFile.propToStat(fullsetRow.get("PCode"+x+"a"), fullsetRow.get("PMin"+x+"a"), fullsetRow.get("PMax"+x+"a"), fullsetRow.get("PParam"+x+"a"), (20 + x)));
		}
		for(int x = 1 ;x<9;x++){
			if(fullsetRow.get("FCode"+x).equals(""))continue;
			iProps.addAll(D2TxtFile.propToStat(fullsetRow.get("FCode"+x), fullsetRow.get("FMin"+x), fullsetRow.get("FMax"+x), fullsetRow.get("FParam"+x), 26));
		}
	}

	private void readExtend2(D2BitReader pFile) throws Exception {
		if (isTypeArmor()) {
			iDef = (short) (pFile.read(11) - 10); // -10 ???
			iInitDef = iDef;
			iMaxDur = (short) pFile.read(8);

			if (iMaxDur != 0) {
				iCurDur = (short) pFile.read(9);
			}

		} else if (isTypeWeapon()) {
			if (iType.equals("tkni") || iType.equals("taxe")
					|| iType.equals("jave") || iType.equals("ajav")) {
				iThrow = true;
			}
			iMaxDur = (short) pFile.read(8);

			if (iMaxDur != 0) {
				iCurDur = (short) pFile.read(9);
			}

			if ((D2TxtFile.WEAPONS.searchColumns("code", item_type)).get(
			"1or2handed").equals("")
			&& !iThrow) {

				if ((D2TxtFile.WEAPONS.searchColumns("code", item_type)).get(
				"2handed").equals("1")) {
					iWhichHand = 2;
					i1Dmg = new short[4];
					i1Dmg[0] = i1Dmg[1] = Short.parseShort((D2TxtFile.WEAPONS
							.searchColumns("code", item_type))
							.get("2handmindam"));
					i1Dmg[2] = i1Dmg[3] = Short.parseShort((D2TxtFile.WEAPONS
							.searchColumns("code", item_type))
							.get("2handmaxdam"));
				} else {
					iWhichHand = 1;
					i1Dmg = new short[4];
					i1Dmg[0] = i1Dmg[1] = Short.parseShort((D2TxtFile.WEAPONS
							.searchColumns("code", item_type)).get("mindam"));
					i1Dmg[2] = i1Dmg[3] = Short.parseShort((D2TxtFile.WEAPONS
							.searchColumns("code", item_type)).get("maxdam"));
				}

			} else {
				iWhichHand = 0;
				if (iThrow) {
					i2Dmg = new short[4];
					i2Dmg[0] = i2Dmg[1] = Short
					.parseShort((D2TxtFile.WEAPONS.searchColumns(
							"code", item_type)).get("minmisdam"));
					i2Dmg[2] = i2Dmg[3] = Short
					.parseShort((D2TxtFile.WEAPONS.searchColumns(
							"code", item_type)).get("maxmisdam"));
				} else {
					i2Dmg = new short[4];
					i2Dmg[0] = i2Dmg[1] = Short
					.parseShort((D2TxtFile.WEAPONS.searchColumns(
							"code", item_type)).get("2handmindam"));
					i2Dmg[2] = i2Dmg[3] = Short
					.parseShort((D2TxtFile.WEAPONS.searchColumns(
							"code", item_type)).get("2handmaxdam"));
				}
				i1Dmg = new short[4];
				i1Dmg[0] = i1Dmg[1] = Short.parseShort((D2TxtFile.WEAPONS
						.searchColumns("code", item_type)).get("mindam"));
				i1Dmg[2] = i1Dmg[3] = Short.parseShort((D2TxtFile.WEAPONS
						.searchColumns("code", item_type)).get("maxdam"));
			}

			if ("1".equals(iItemType.get("stackable"))) {
				iStackable = true;
				iCurDur = (short) pFile.read(9);
			}
		} else if (isTypeMisc()) {
			if ("1".equals(iItemType.get("stackable"))) {
				iStackable = true;
				iCurDur = (short) pFile.read(9);
			}

		}

		if (iSocketed) {
			iSocketNrTotal = (short) pFile.read(4);
		}

		int[] lSet = new int[5];

		if (quality == 5) {
			for (int x = 0; x < 5; x++) {
				lSet[x] = (int) pFile.read(1);
			}
		}

		if(iJewel){
			readProperties(pFile, 1);
		}else{
			readProperties(pFile, 0);
		}
		if (quality == 5) {
			for (int x = 0; x < 5; x++) {
				if (lSet[x] == 1) {
					readProperties(pFile, x + 2);
				}
			}
		}
		if (iRuneWord) {
			readProperties(pFile, 0);
		}
	}

	private void applyAutomodLvl() {
		// modifies the level if the automod is higher
		if (automod_info == null) {
			return;
		}
		if (Integer.parseInt(automod_info.get("levelreq")) > iReqLvl) {
			iReqLvl = Integer.parseInt(automod_info.get("levelreq"));
		}

	}

	// MBR: unknown, but should be according to file format
	private void readTypes(D2BitReader pFile) {
		// charms ??
		if (isCharm()) {
//			long lCharm1 = pFile.read(1);
			pFile.read(1);
//			long lCharm2 = pFile.read(11);
			pFile.read(11);
			// System.err.println("Charm (?): " + lCharm1 );
			// System.err.println("Charm (?): " + lCharm2 );
		}

		// books / scrolls ??
		if ("tbk".equals(item_type) || "ibk".equals(item_type)) {
//			long lTomb = pFile.read(5);
			pFile.read(5);
			// System.err.println("Tome ID: " + lTomb );
		}

		if ("tsc".equals(item_type) || "isc".equals(item_type)) {
//			long lTomb = pFile.read(5);
			pFile.read(5);
			// System.err.println("Tome ID: " + lTomb );
		}

		// body ??
		if ("body".equals(item_type)) {
//			long lMonster = pFile.read(10);
			pFile.read(10);
			// System.err.println("Monster ID: " + lMonster );
		}
	}

	private void readPropertiesPots(D2BitReader pfile) {

		String[] statsToRead = { "stat1", "stat2" };

		for (int x = 0; x < statsToRead.length; x = x + 1) {

			if ((D2TxtFile.MISC.searchColumns("code", item_type)).get(
					statsToRead[x]).equals(""))
				continue;
			iProps.add(new D2Prop(Integer.parseInt((D2TxtFile.ITEM_STAT_COST.searchColumns("Stat", (D2TxtFile.MISC
					.searchColumns("code", item_type))
					.get(statsToRead[x]))).get("ID")),
					new int[] { Integer.parseInt(((D2TxtFile.MISC
							.searchColumns("code", item_type))
							.get(statsToRead[x].replaceFirst("stat",
							"calc")))) }, 0));
		}
	}

	private void readPropertiesGems(D2BitReader pFile) {
//		RUNES ARE GEMS TOO!!!!
		String[][] gemHeaders = { { "weaponMod1", "weaponMod2", "weaponMod3" },
				{ "helmMod1", "helmMod2", "helmMod3" },
				{ "shieldMod1", "shieldMod2", "shieldMod3" } };

		for (int x = 0; x < gemHeaders.length; x++) {

			for (int y = 0; y < gemHeaders[x].length; y++) {

				if (D2TxtFile.GEMS.searchColumns("code", item_type).get(
						gemHeaders[x][y] + "Code").equals(""))
					continue;
				iProps.addAll(D2TxtFile.propToStat(D2TxtFile.GEMS
						.searchColumns("code", item_type).get(
								gemHeaders[x][y] + "Code"), D2TxtFile.GEMS
								.searchColumns("code", item_type).get(
										gemHeaders[x][y] + "Min"), D2TxtFile.GEMS
										.searchColumns("code", item_type).get(
												gemHeaders[x][y] + "Max"), D2TxtFile.GEMS
												.searchColumns("code", item_type).get(
														gemHeaders[x][y] + "Param"), (x + 7)));
			}
		}
	}

	private void readProperties(D2BitReader pFile, int qFlag) {

		int rootProp = (int) pFile.read(9);

		while (rootProp != 511) {

			iProps.readProp(pFile, rootProp, qFlag);

			if (rootProp == 17) {
				iProps.readProp(pFile, 18, qFlag);
			} else if (rootProp == 48) {
				iProps.readProp(pFile, 49, qFlag);
			} else if (rootProp == 50) {
				iProps.readProp(pFile, 51, qFlag);
			} else if (rootProp == 52) {
				iProps.readProp(pFile, 53, qFlag);
			} else if (rootProp == 54) {
				iProps.readProp(pFile, 55, qFlag);
				iProps.readProp(pFile, 56, qFlag);
			} else if (rootProp == 57) {
				iProps.readProp(pFile, 58, qFlag);
				iProps.readProp(pFile, 59, qFlag);
			}
			rootProp = (int) pFile.read(9);
		}

	}

	private void applyItemMods() {

		int[] armourTriple = new int[] { 0, 0, 0 };
		int[] dmgTriple = new int[] { 0, 0, 0, 0, 0 };
		int[] durTriple = new int[] { 0, 0 };
		RequirementModifierAccumulator requirementModifierAccumulator = new RequirementModifierAccumulator();

		iProps.applyOp(iCharLvl);

		for (int x = 0; x < iProps.size(); x++) {
			if(((D2Prop) iProps.get(x)).getQFlag() != 0 && ((D2Prop) iProps.get(x)).getQFlag() != 12 && ((D2Prop) iProps.get(x)).getQFlag() != 13 && ((D2Prop) iProps.get(x)).getQFlag() != 14 && ((D2Prop) iProps.get(x)).getQFlag() != 15 && ((D2Prop) iProps.get(x)).getQFlag() != 16)continue;

			// +Dur
			if (((D2Prop) iProps.get(x)).getPNum() == 73) {
				durTriple[0] = durTriple[0]
				                         + ((D2Prop) iProps.get(x)).getPVals()[0];
			}

			// Dur%
			if (((D2Prop) iProps.get(x)).getPNum() == 75) {
				durTriple[1] = durTriple[1]
				                         + ((D2Prop) iProps.get(x)).getPVals()[0];
			}

			// +LvlReq
			if (((D2Prop) iProps.get(x)).getPNum() == 92) {
				requirementModifierAccumulator.accumulateLevelRequirement(((D2Prop) iProps.get(x)).getPVals()[0]);
			}

			// -Req
			if (((D2Prop) iProps.get(x)).getPNum() == 91) {
				requirementModifierAccumulator.accumulatePercentRequirements(((D2Prop) iProps.get(x)).getPVals()[0]);
			}

			// +Skills modify level
			if (((D2Prop) iProps.get(x)).getPNum() == 97
					|| ((D2Prop) iProps.get(x)).getPNum() == 107) {

				if (iReqLvl < Integer.parseInt(D2TxtFile.SKILLS.searchColumns(
						"skilldesc",
						D2TxtFile.SKILL_DESC.getRow(
								((D2Prop) iProps.get(x)).getPVals()[0]).get(
								"skilldesc")).get("reqlevel"))) {
					iReqLvl = (Integer.parseInt(D2TxtFile.SKILLS.searchColumns(
							"skilldesc",
							D2TxtFile.SKILL_DESC.getRow(
									((D2Prop) iProps.get(x)).getPVals()[0])
									.get("skilldesc")).get("reqlevel")));
				}
			}

			if (isTypeArmor()) {

				// EDef
				if (((D2Prop) iProps.get(x)).getPNum() == 16) {
					armourTriple[0] = armourTriple[0]
					                               + ((D2Prop) iProps.get(x)).getPVals()[0];
				}

				// +Def
				if (((D2Prop) iProps.get(x)).getPNum() == 31) {
					armourTriple[1] = armourTriple[1]
					                               + ((D2Prop) iProps.get(x)).getPVals()[0];
				}

				// +Def/lvl
				if (((D2Prop) iProps.get(x)).getPNum() == 214) {
					armourTriple[2] = armourTriple[2]
					                               + ((D2Prop) iProps.get(x)).getPVals()[0];
				}

				if (isShield()) {
					if (((D2Prop) iProps.get(x)).getPNum() == 20) {
						iBlock = (short) (cBlock + ((D2Prop) iProps.get(x))
								.getPVals()[0]);
					}
				}

			} else if (isTypeWeapon()) {

				// EDmg
				if (((D2Prop) iProps.get(x)).getPNum() == 17) {
					dmgTriple[0] = dmgTriple[0]
					                         + ((D2Prop) iProps.get(x)).getPVals()[0];
				}

				// MinDMg
				if (((D2Prop) iProps.get(x)).getPNum() == 21) {
					dmgTriple[1] = dmgTriple[1]
					                         + ((D2Prop) iProps.get(x)).getPVals()[0];
					if (((D2Prop) iProps.get(x)).getFuncN() == 31) {
						dmgTriple[2] = dmgTriple[2]
						                         + ((D2Prop) iProps.get(x)).getPVals()[1];
					}
				}

				// MaxDmg
				if (((D2Prop) iProps.get(x)).getPNum() == 22) {
					dmgTriple[2] = dmgTriple[2]
					                         + ((D2Prop) iProps.get(x)).getPVals()[0];
				}

				// MaxDmg/Lvl
				if (((D2Prop) iProps.get(x)).getPNum() == 218) {
					dmgTriple[3] = dmgTriple[3]
					                         + ((D2Prop) iProps.get(x)).getPVals()[0];
				}

				// MaxDmg%/lvl
				if (((D2Prop) iProps.get(x)).getPNum() == 219) {
					dmgTriple[4] = dmgTriple[4]
					                         + ((D2Prop) iProps.get(x)).getPVals()[0];
				}
			}
		}

		iReqLvl = iReqLvl + requirementModifierAccumulator.getLevelRequirement();
		double percentReqirementsModifier = requirementModifierAccumulator.getPercentRequirements() / (double) 100;
		iReqDex = iReqDex + ((int) (iReqDex * percentReqirementsModifier));
		iReqStr = iReqStr + ((int) (iReqStr * percentReqirementsModifier));

		if(isTypeWeapon()){

			if (iEthereal) {
				i1Dmg[0] = i1Dmg[1] = (short) Math
				.floor((((double) i1Dmg[1] / (double) 100) * (double) 50)
						+ i1Dmg[1]);
				i1Dmg[2] = i1Dmg[3] = (short) Math
				.floor((((double) i1Dmg[3] / (double) 100) * (double) 50)
						+ i1Dmg[3]);

				if (iWhichHand == 0) {
					i2Dmg[0] = i2Dmg[1] = (short) Math
					.floor((((double) i2Dmg[1] / (double) 100) * (double) 50)
							+ i2Dmg[1]);
					i2Dmg[2] = i2Dmg[3] = (short) Math
					.floor((((double) i2Dmg[3] / (double) 100) * (double) 50)
							+ i2Dmg[3]);
				}
			}

			i1Dmg[1] = (short) Math
			.floor((((double) i1Dmg[1] / (double) 100) * dmgTriple[0])
					+ (i1Dmg[1] + dmgTriple[1]));
			i1Dmg[3] = (short) Math
			.floor((((double) i1Dmg[3] / (double) 100) * (dmgTriple[0] + dmgTriple[4]))
					+ (i1Dmg[3] + (dmgTriple[2] + dmgTriple[3])));

			if (iWhichHand == 0) {
				i2Dmg[1] = (short) Math.floor((((double) i2Dmg[1] / (double) 100) * dmgTriple[0])
						+ (i2Dmg[1] + dmgTriple[1]));
				i2Dmg[3] = (short) Math.floor((((double) i2Dmg[3] / (double) 100) * (dmgTriple[0] +  dmgTriple[4]))
						+ (i2Dmg[3] + (dmgTriple[2] + dmgTriple[3])));
			}
			if (i1Dmg[1] > i1Dmg[3]) {
				i1Dmg[3] = (short) (i1Dmg[1] + 1);
			}



		}else if(isTypeArmor()){
			iDef = (short) Math
			.floor((((double) iInitDef / (double) 100) * armourTriple[0])
					+ (iInitDef + (armourTriple[1] + armourTriple[2])));
		}

		iMaxDur = (short) Math
		.floor((((double) iMaxDur / (double) 100) * durTriple[1])
				+ (iMaxDur + durTriple[0]));



	}

	private boolean check_flag(int bit) {
		if (((flags >>> (32 - bit)) & 1) == 1)
			return true;
		else
			return false;
	}

	private int getReq(String pReq) {
		if (pReq != null) {
			String lReq = pReq.trim();
			if (!lReq.equals("") && !lReq.equals("0")) {
				try {
					return Integer.parseInt(lReq);
				} catch (Exception pEx) {
					// do nothing, no req
				}
			}
		}
		return -1;
	}

	private String getExStr() {
		return " (" + iItemName + ", " + iFP + ")";
	}

	private boolean isBodyLocation(String pLocation) {
		if (iBody) {
			if (pLocation.equals(iBodyLoc1)) {
				return true;
			}
			if (pLocation.equals(iBodyLoc2)) {
				return true;
			}
		}
		return false;
	}

	private String htmlStrip(StringBuffer htmlString) {
		String dumpStr =  htmlString.toString().replaceAll("<br>&#10;", System.getProperty("line.separator"));
		dumpStr =  dumpStr.replaceAll("&#32;", "");
		return dumpStr.replaceAll("<[^>]*>", "");
	}

	public String itemDumpHtml(boolean extended){
		return generatePropString(extended).toString();
	}

	public String itemDump(boolean extended){
		return htmlStrip(generatePropString(extended));
	}

	private StringBuffer generatePropString(boolean extended){
		StringBuffer propString = new StringBuffer("<html>");
		propString.append(generatePropStringNoHtmlTags(extended));
		return propString.append("</html>");
	}

	private StringBuffer generatePropStringNoHtmlTags(boolean extended){
		iProps.tidy();
		StringBuffer dispStr = new StringBuffer("<center>");
		String base = (Integer.toHexString(Color.white.getRGB())).substring(2, Integer.toHexString(Color.white.getRGB()).length());
		String rgb = (Integer.toHexString(getItemColor().getRGB())).substring(2, Integer.toHexString(getItemColor().getRGB()).length());
		if (personalization == null) {
			dispStr.append("<font color=\"#"+ base + "\">" + "<font color=\"#" + rgb + "\">"+ iItemName + "</font>" + "<br>&#10;");
		}else{
			dispStr.append("<font color=\"#"+ base + "\">" + "<font color=\"#" + rgb + "\">"+ personalization + "'s " + iItemName  + "</font>" + "<br>&#10;");
		}
		if (!iBaseItemName.equals(iItemName))dispStr.append("<font color=\"#" + rgb + "\">"+iBaseItemName + "</font>" + "<br>&#10;");
		if(isRuneWord()){
			dispStr.append("<font color=\"#" + rgb + "\">");
			for(int x=0;x<iSocketedItems.size();x++){
				dispStr.append((((D2Item)iSocketedItems.get(x)).getName().substring(0, ((D2Item)iSocketedItems.get(x)).getName().length() -5)));
			}
			dispStr.append("</font><br>&#10;");
		}
		if (isTypeWeapon() || isTypeArmor()) {
			if(isTypeWeapon()){
				if(iWhichHand == 0){
					if(iThrow){
						dispStr.append("Throw Damage: " + i2Dmg[1] + " - " + i2Dmg[3] + "<br>&#10;");
						dispStr.append("One Hand Damage: " + i1Dmg[1] + " - "+ i1Dmg[3] + "<br>&#10;");
					}else{
						dispStr.append("One Hand Damage: " + i1Dmg[1] + " - " + i1Dmg[3] + "<br>&#10;");
						dispStr.append("Two Hand Damage: " + i2Dmg[1] + " - " + i2Dmg[3] + "<br>&#10;");
					}
				} else if(iWhichHand == 1){
					dispStr.append("One Hand Damage: " + i1Dmg[1] + " - " + i1Dmg[3] + "<br>&#10;");
				} else{
					dispStr.append("Two Hand Damage: " + i1Dmg[1] + " - " + i1Dmg[3] + "<br>&#10;");
				}
			}else if(isTypeArmor()){
				dispStr.append("Defense: " + iDef + "<br>&#10;");
				if(isShield()){
					dispStr.append("Chance to Block: " + iBlock + "<br>&#10;");
				}
			}
			if (isStackable()) {
				dispStr.append("Quantity: " + iCurDur + "<br>&#10;");
			} else {
				if (iMaxDur == 0) {
					dispStr.append("Indestructible" + "<br>&#10;");
				} else {
					dispStr.append("Durability: " + iCurDur + " of " + iMaxDur + "<br>&#10;");
				}
			}
		}
		if (iReqLvl > 0)dispStr.append("Required Level: " + iReqLvl + "<br>&#10;");
		if (iReqStr > 0)dispStr.append("Required Strength: " + iReqStr + "<br>&#10;");
		if (iReqDex > 0)dispStr.append("Required Dexterity: " + iReqDex + "<br>&#10;");
		if (iFP != null)dispStr.append("Fingerprint: " + iFP + "<br>&#10;");
		if (iGUID != null)dispStr.append("GUID: " + iGUID + "<br>&#10;");
		if (ilvl != 0)dispStr.append("Item Level: " + ilvl + "<br>&#10;");
		dispStr.append("Version: " + get_version() + "<br>&#10;");
		if (!iIdentified)dispStr.append("Unidentified" + "<br>&#10;");

		dispStr.append(getItemPropertyString());

		if (extended){
			if (isSocketed()) {

				if (iSocketedItems != null) {
					dispStr.append("<br>&#10;");
					for (int x = 0; x < iSocketedItems.size(); x = x + 1) {
						if (((D2Item) iSocketedItems.get(x)) != null) {
							dispStr.append(((D2Item) iSocketedItems.get(x)).generatePropStringNoHtmlTags(false));
							if(x != iSocketedItems.size() -1){
								dispStr.append("<br>&#10;");
							}
						}
					}
				}
			}
		}

		return dispStr.append("</center>");
	}

	private StringBuffer getItemPropertyString(){


		StringBuffer dispStr = new StringBuffer("");

		if(isJewel()){
			dispStr.append(iProps.generateDisplay(1, iCharLvl));
		}else{
			dispStr.append(iProps.generateDisplay(0, iCharLvl));
		}
		if (isGem() || isRune()) {
			dispStr.append("Weapons: ");
			dispStr.append(iProps.generateDisplay(7, iCharLvl));
			dispStr.append("Armor: ");
			dispStr.append(iProps.generateDisplay(8, iCharLvl));
			dispStr.append("Shields: ");
			dispStr.append(iProps.generateDisplay(9, iCharLvl));
		}
		if (quality == 5) {

			for (int x = 12; x < 17; x++) {
				StringBuffer setBuf = iProps.generateDisplay(x, iCharLvl);
				if (setBuf.length() > 29) {
					dispStr.append("<font color=\"red\">Set (" + (x-10) + " items): ");
					dispStr.append(setBuf);
					dispStr.append("</font>");
				}
			}

			for (int x = 2; x < 7; x++) {
				StringBuffer setBuf = iProps.generateDisplay(x, iCharLvl);
				if (setBuf.length() > 29) {
					dispStr.append("Set (" + x + " items): ");
					dispStr.append(setBuf);
				}
			}
		}
		if (iEthereal) {
			dispStr.append("<font color=\"#4850b8\">Ethereal</font><br>&#10;");
		}
		if (iSocketNrTotal > 0) {
			dispStr.append(iSocketNrTotal + " Sockets (" + iSocketNrFilled	+ " used)<br>&#10;");
			if (iSocketedItems != null) {
				for (int i = 0; i < iSocketedItems.size(); i++) {
					dispStr.append("Socketed: " + ((D2Item) iSocketedItems.get(i)).getItemName() + "<br>&#10;");
				}
			}
		}

		if (quality == 5) {
			dispStr.append("<br>&#10;");
			for (int x = 32; x < 36; x++) {
				StringBuffer setBuf = iProps.generateDisplay(x, iCharLvl);
				if (setBuf.length() > 29) {
					dispStr.append("<font color=\"red\">(" + (x-30) + " items): ");
					dispStr.append(setBuf);
					dispStr.append("</font>");
				}
			}

			StringBuffer setBuf = iProps.generateDisplay(36, iCharLvl);
			if (setBuf.length() > 33) {
				dispStr.append(setBuf);
			}

		}

		return dispStr;
	}

	public void toWriter(PrintWriter pw){
		pw.println();
		pw.print(itemDump(true));
	}


	public boolean isBodyLArm() {
		return isBodyLocation("larm");
	}

	public boolean isBodyRRin() {
		return isBodyLocation("rrin");
	}

	public boolean isBodyLRin() {
		return isBodyLocation("lrin");
	}

	public boolean isWeaponType(D2WeaponTypes pType) {
		if (iTypeWeapon) {
			if (pType.isType(iType)) {
				return true;
			}
		}
		return false;
	}

	public boolean isBodyLocation(D2BodyLocations pLocation) {
		if (iBody) {
			if (pLocation.getLocation().equals(iBodyLoc1)) {
				return true;
			}
			if (pLocation.getLocation().equals(iBodyLoc2)) {
				return true;
			}
		}
		return false;
	}



	public boolean isBelt() {
		return iBelt;
	}

	public boolean isCharm() {
		return (iSmallCharm || iLargeCharm || iGrandCharm);
	}

	public boolean isCharmSmall() {
		return iSmallCharm;
	}

	public boolean isCharmLarge() {
		return iLargeCharm;
	}

	public boolean isCharmGrand() {
		return iGrandCharm;
	}

	public boolean isJewel() {
		return iJewel;
	}

	// accessor for the row
	public short get_row() {
		return row;
	}

	// accessor for the column
	public short get_col() {
		return col;
	}

	// setter for the row
	// necessary for moving items
	public void set_row(short r) {
		iItem.set_byte_pos(7);
		iItem.skipBits(13);
		iItem.write((long) r, 4);
		row = r;
	}

	// setter for the column
	// necessary for moving items
	public void set_col(short c) {
		iItem.set_byte_pos(7);
		iItem.skipBits(9);
		iItem.write((long) c, 4);
		col = c;
	}

	public void set_location(short l) {
		iItem.set_byte_pos(7);
		iItem.skipBits(2);
		iItem.write((long) l, 3);
		location = l;
	}

	public void set_body_position(short bp) {
		iItem.set_byte_pos(7);
		iItem.skipBits(5);
		iItem.write((long) bp, 4);
		body_position = bp;
	}

	public void set_panel(short p) {
		iItem.set_byte_pos(7);
		iItem.skipBits(17);
		iItem.write((long) p, 3);
		panel = p;
	}

	public short get_location() {
		return location;
	}

	public short get_body_position() {
		return body_position;
	}

	public short get_panel() {
		return panel;
	}

	public short get_width() {
		return width;
	}

	public short get_height() {
		return height;
	}

	public String get_image() {
		return image_file;
	}

	public short getSetID(){
		return set_id;
	}

	public String get_version() {

		if (version == 0) {
			return "Legacy (pre 1.08)";
		}

		if (version == 1) {
			return "Classic";
		}
//2 is another version perhaps?
		if (version == 100) {
			return "Expansion";
		}

		if (version == 101) {
			return "Expansion 1.10+";
		}
//		System.out.println(version);

		return "UNKNOWN";
	}

	public long getSocketNrFilled() {
		return iSocketNrFilled;
	}

	public long getSocketNrTotal() {
		return iSocketNrTotal;
	}

	public byte[] get_bytes() {
		return iItem.getFileContent();
	}

	public int getItemLength() {
		return iItem.get_length();
	}

	public String getItemName() {
		return iItemName;
	}

	public String getName() {
		return iItemName;
	}

	public String getFingerprint() {
		return iFP;
	}

	public String getILvl() {
		return Short.toString(ilvl);
	}

	public int getReqLvl() {
		return iReqLvl;
	}

	public int getReqStr() {
		return iReqStr;
	}

	public int getReqDex() {
		return iReqDex;
	}

	public Color getItemColor() {
		if (isUnique()) {
			// return Color.yellow.darker().darker();
			return new Color(255, 222, 173);
		}
		if (isSet()) {
			return Color.green.darker();
		}
		if (isRare()) {
			return Color.yellow.brighter();
		}
		if (isMagical()) {
			return new Color(72, 118, 255);
		}
		if (isRune()) {
			return Color.red;
		}
		if (isCrafted()) {
			return Color.orange;
		}
		if (isRuneWord()) {
			return new Color(255, 222, 173);
		}
		if (isEthereal() || isSocketed()) {
			return Color.gray;
		}
		return Color.white;
	}

	public boolean isUnique() {
		return iUnique;
	}

	public boolean isSet() {
		return iSet;
	}

	public boolean isRuneWord() {
		return iRuneWord;
	}

	public boolean isCrafted() {
		return iCrafted;
	}

	public boolean isRare() {
		return iRare;
	}

	public boolean isMagical() {
		return iMagical;
	}

	public boolean isShield() {
		if (iType != null) {
			if (iType.equals("ashd") || iType.equals("shie")
					|| iType.equals("head")) {
				return true;
			}
		}
		return false;
	}

	public boolean isNormal() {
		return !(iMagical || iRare || iCrafted || iRuneWord || isRune() || iSet || iUnique);
	}

	public boolean isSocketFiller() {
		return isRune() || isJewel() || isGem();
	}

	public boolean isGem() {
		return iGem;
	}

	public boolean isRune() {
		return getRuneCode() != null;
	}

	public String getRuneCode() {
		if (iItemType != null) {
			if ("rune".equals(iItemType.get("type"))) {
				return iItemType.get("code");
			}
		}
		return null;
	}

	public boolean isEthereal() {
		return iEthereal;
	}

	public boolean isSocketed() {
		return iSocketed;
	}

	public boolean isStackable() {
		return iStackable;
	}

	public boolean isTypeMisc() {
		return iTypeMisc;
	}

	public boolean isTypeArmor() {
		return iTypeArmor;
	}

	public boolean isTypeWeapon() {
		return iTypeWeapon;
	}

	public boolean isCursorItem() {
		if (location != 0 && location != 2) {
			if (body_position == 0) {
				// System.err.println("location: " + location );
				return true;
			}
		}
		return false;
	}

	public int compareTo(Object pObject) {
		if (pObject instanceof D2Item) {
			String lItemName = ((D2Item) pObject).iItemName;
			if (iItemName == lItemName) {
				// also both "null"
				return 0;
			}
			if (iItemName == null) {
				return -1;
			}
			if (lItemName == null) {
				return 1;
			}
			return iItemName.compareTo(lItemName);
		}
		return -1;
	}

	public int getiDef() {
		return (int) iDef;
	}

	public boolean isCharacterItem(){

		//Belt or equipped
		if (get_location() == 1 || get_location() == 2) {
			return true;
		}else if(get_location() == 0){
			switch(get_panel()){
			case 1:
			case 4:
			case 5:
				return true;
			default:
				return false;
			}
		}else{
			return false;
		}

	}

	public boolean isEquipped() {

		if (get_location() == 1 ) {
			return true;
		} else if (get_panel() == 1 && isCharm()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEquipped(int wepSlot) {

		if (get_location() == 1) {

			if(!isTypeWeapon() && !isShield())return true;
			if(wepSlot == 0){
				if(get_body_position() == 4 || get_body_position() == 5)return true;
			}else if(wepSlot == 1){
				if(get_body_position() == 11 || get_body_position() == 12)return true;
			}
			return false;
		} else if (get_panel() == 1 && isCharm()) {
			return true;
		} else {
			return false;
		}
	}

	public void setSetProps(int numItems) {
		// iSetProps.clear();
		/*
		 * if(numItems == 0){ iSetProps.clear(); }else if(numItems == 1){
		 * iSetProps.clear();
		 *
		 *
		 * }else
		 */

		// if (numItems == 2) {
		// iSetProps.addAll(iSet1);
		// } else if (numItems == 3) {
		// iSetProps.addAll(iSet1);
		// iSetProps.addAll(iSet2);
		// } else if (numItems == 4) {
		// iSetProps.addAll(iSet1);
		// iSetProps.addAll(iSet2);
		// iSetProps.addAll(iSet3);
		// } else if (numItems == 5) {
		// iSetProps.addAll(iSet1);
		// iSetProps.addAll(iSet2);
		// iSetProps.addAll(iSet3);
		// iSetProps.addAll(iSet4);
		// }
		// for(int x =0;x<iSetProps.size();x=x+1){
		// System.out.println(numItems + " VAL:
		// "+((D2ItemProperty)iSetProps.get(x)).getValue());
		// }
		if (isTypeArmor()) {
			// applyEDef();

		}
	}

	public int getSetSize() {
		return setSize;
	}

	public String getSetName() {
		return iSetName;
	}

	public boolean statModding() {

		if (iJewel || iGem || iRune) {
			return false;
		} else {

			return true;
		}
	}

	// public ArrayList getPerfectStringUS() {
	// D2ItemProperty[] outProp;
	// ArrayList outArrL = new ArrayList();
	// D2TxtFileItemProperties pRow;
	// if (this.isUnique()) {
	// pRow = D2TxtFile.UNIQUES.getRow(unique_id);
	// } else if (this.isSet()) {
	// pRow = D2TxtFile.SETITEMS.getRow(set_id);
	// } else {
	// pRow = D2TxtFile.RUNES.getRow(runeword_id);
	// }
	//
	// int counter = 1;
	// int max = 13;
	// if (this.isSet()) {
	// max = 10;
	// } else if (this.isRuneWord()) {
	// max = 8;
	// }
	// String prop = "prop";
	//
	// if (isRuneWord()) {
	// prop = "T1Code";
	// }
	//
	// while (counter < max) {
	// outProp = new D2ItemProperty[2];
	// if (!pRow.get(prop + counter).equals("")) {
	//
	// if (pRow.get(prop + counter).indexOf("*") != -1) {
	// break;
	// }
	//
	// // System.out.println(pRow.get("prop" + counter) + " -- " +
	// // pRow.get("min" + counter) + "-" + pRow.get("max" + counter));
	//
	// /*
	// * int lProp = Integer.parseInt((D2TxtFile.ITEM_STAT_COST
	// * .searchColumns("Stat", ((D2TxtFile.PROPS
	// * .searchColumns("code", (D2TxtFile.GEMS .searchColumns("code",
	// * item_type)) .get(interestingSubProp[x][y])))
	// * .get("stat1")))).get("ID"));
	// */
	//
	// int lProp = Integer.parseInt(D2TxtFile.ITEM_STAT_COST
	// .searchColumns(
	// "Stat",
	// (D2TxtFile.PROPS.searchColumns("code", pRow
	// .get(prop + counter)).get("stat1")))
	// .get("ID"));
	// if (pRow.get(prop + counter).equals("res-all")) {
	// lProp = 1337;
	// }
	//
	//
	//
	// if (lProp == 54 || lProp == 55 || lProp == 56) {
	// D2ItemProperty lProperty = new D2ItemProperty(101010,
	// iCharLvl, iItemName);
	// lProperty.set(101010, null, 0, 0);
	// outProp[0] = lProperty;
	// outProp[1] = lProperty;
	// outArrL.add(outProp);
	// if(pRow.get("prop" + counter).equals("dmg-cold")){
	// counter = counter + 1;
	// }else{
	// counter = counter + 3;
	// }
	// continue;
	// }
	//
	// if (lProp == 48 || lProp == 49) {
	// D2ItemProperty lProperty = new D2ItemProperty(101010,
	// iCharLvl, iItemName);
	// lProperty.set(101010, null, 0, 1);
	// outProp[0] = lProperty;
	// outProp[1] = lProperty;
	// outArrL.add(outProp);
	// if(pRow.get("prop" + counter).equals("dmg-fire")){
	// counter = counter + 1;
	// }else{
	// counter = counter + 2;
	// }
	// continue;
	// }
	//
	// if (lProp == 50 || lProp == 51) {
	// D2ItemProperty lProperty = new D2ItemProperty(101010,
	// iCharLvl, iItemName);
	// lProperty.set(101010, null, 0, 2);
	// outProp[0] = lProperty;
	// outProp[1] = lProperty;
	// outArrL.add(outProp);
	// if(pRow.get("prop" + counter).equals("dmg-ltng")){
	// counter = counter + 1;
	// }else{
	// counter = counter + 2;
	// }
	// continue;
	// }
	//
	// if (lProp == 52 || lProp == 53) {
	// D2ItemProperty lProperty = new D2ItemProperty(101010,
	// iCharLvl, iItemName);
	// lProperty.set(101010, null, 0, 3);
	// outProp[0] = lProperty;
	// outProp[1] = lProperty;
	// outArrL.add(outProp);
	// if(pRow.get("prop" + counter).equals("dmg-mag")){
	// counter = counter + 1;
	// }else{
	// counter = counter + 2;
	// }
	// continue;
	// }
	//
	// if (lProp == 57 || lProp == 58 || lProp == 59) {
	// D2ItemProperty lProperty = new D2ItemProperty(101010,
	// iCharLvl, iItemName);
	// lProperty.set(101010, null, 0, 4);
	// outProp[0] = lProperty;
	// outProp[1] = lProperty;
	// outArrL.add(outProp);
	// if(pRow.get("prop" + counter).equals("dmg-pois")){
	// counter = counter + 1;
	// }else{
	// counter = counter + 3;
	// }
	// continue;
	// }
	//
	// int lPropTemp = lProp;
	// D2ItemProperty lProperty = new D2ItemProperty(lPropTemp,
	// iCharLvl, iItemName);
	//
	// D2TxtFileItemProperties lItemStatCost = D2TxtFile.ITEM_STAT_COST
	// .getRow(lProperty.getPropNrs()[0]);
	//
	// String pMin = pRow.get("min" + counter);
	// String pMax = pRow.get("max" + counter);
	// String pParam = pRow.get("par" + counter);
	//
	// if (isRuneWord()) {
	// pMin = pRow.get("T1Min" + counter);
	// pMax = pRow.get("T1Max" + counter);
	// pParam = pRow.get("T1Param" + counter);
	// }
	//
	//
	// if (pRow.get(prop + counter).equals("dmg-norm")) {
	//
	// lProperty = new D2ItemProperty(21,
	// iCharLvl, iItemName);
	//
	// lItemStatCost = D2TxtFile.ITEM_STAT_COST
	// .getRow(lProperty.getPropNrs()[0]);
	//
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMin));
	//
	// outProp[0] = lProperty;
	// outProp[1] = lProperty;
	// outArrL.add(outProp);
	//
	// outProp = new D2ItemProperty[2];
	//
	// lProperty = new D2ItemProperty(22,
	// iCharLvl, iItemName);
	//
	// lItemStatCost = D2TxtFile.ITEM_STAT_COST
	// .getRow(lProperty.getPropNrs()[0]);
	//
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMax));
	//
	// outProp[0] = lProperty;
	// outProp[1] = lProperty;
	// outArrL.add(outProp);
	// counter ++;
	// continue;
	//
	//
	//
	// }
	//
	// if (lPropTemp == 201 || lPropTemp == 197 || lPropTemp == 199
	// || lPropTemp == 195 || lPropTemp == 198
	// || lPropTemp == 196) {
	//
	// if (!pMax.equals("")) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMax));
	// }
	//
	// if (!pParam.equals("")) {
	// // if(lPropTemp==198){
	//
	// try {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(D2TxtFile.SKILLS.searchColumns(
	// "skill", pParam).get("Id")));
	// } catch (NullPointerException e) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pParam));
	// }
	// // }else{
	// // lProperty.set(lPropTemp, lItemStatCost, 0,
	// // Long.parseLong(pParam));
	// // }
	// }
	//
	// if (!pMin.equals("")) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMin));
	// }
	// } else {
	//
	// if (!pMin.equals("")) {
	// if (lPropTemp == 204) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMax));
	// } else if (lPropTemp == 107) {
	// try {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(D2TxtFile.SKILLS
	// .searchColumns("skill", pParam)
	// .get("Id")));
	// } catch (NullPointerException e) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pParam));
	// }
	//
	//
	// } else {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMin));
	// }
	// }
	//
	// if (!pMax.equals("")) {
	// if (lPropTemp == 204) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMin));
	// } else if (lPropTemp == 107) {
	//
	// } else {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMax));
	// }
	// }
	//
	// if (!pParam.equals("")) {
	//
	// if (lPropTemp == 204 ||lPropTemp == 97) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(D2TxtFile.SKILLS.searchColumns(
	// "skill", pParam).get("Id")));
	//
	// // System.out.println(pParam);
	// // System.out.println(Long
	// // .parseLong(D2TxtFile.SKILLS.searchColumns(
	// // "skill", pParam).get("Id")));
	//
	// } else if (lPropTemp == 107) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMin));
	// } else {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pParam));
	// }
	// }
	// }
	// outProp[0] = lProperty;
	// lPropTemp = lProp;
	//
	// lProperty = new D2ItemProperty(lPropTemp, iCharLvl, iItemName);
	//
	// lItemStatCost = D2TxtFile.ITEM_STAT_COST.getRow(lProperty
	// .getPropNrs()[0]);
	//
	// pMin = pRow.get("min" + counter);
	// pMax = pRow.get("max" + counter);
	// pParam = pRow.get("par" + counter);
	//
	// if (isRuneWord()) {
	// pMin = pRow.get("T1Min" + counter);
	// pMax = pRow.get("T1Max" + counter);
	// pParam = pRow.get("T1Param" + counter);
	// }
	//
	// if (lPropTemp == 201 || lPropTemp == 197 || lPropTemp == 199
	// || lPropTemp == 195 || lPropTemp == 198
	// || lPropTemp == 196) {
	//
	// if (!pMax.equals("")) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMax));
	// }
	//
	// if (!pParam.equals("")) {
	// // if(lPropTemp==198){
	// try {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(D2TxtFile.SKILLS.searchColumns(
	// "skill", pParam).get("Id")));
	// } catch (NullPointerException e) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pParam));
	// }
	// // }else{
	// // lProperty.set(lPropTemp, lItemStatCost, 0,
	// // Long.parseLong(pParam));
	// // }
	// }
	//
	// if (!pMin.equals("")) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMin));
	// }
	// } else {
	// if (!pMax.equals("")) {
	// if (lPropTemp == 107) {
	//
	// } else {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMax));
	// }
	// }
	//
	// if (!pMin.equals("")) {
	// if (lPropTemp == 107) {
	// try {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(D2TxtFile.SKILLS
	// .searchColumns("skill", pParam)
	// .get("Id")));
	// } catch (NullPointerException e) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pParam));
	// }
	// } else {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMin));
	// }
	// }
	//
	// if (!pParam.equals("")) {
	// if (lPropTemp == 204||lPropTemp == 97) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(D2TxtFile.SKILLS.searchColumns(
	// "skill", pParam).get("Id")));
	// } else if (lPropTemp == 107) {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pMax));
	// } else {
	// lProperty.set(lPropTemp, lItemStatCost, 0, Long
	// .parseLong(pParam));
	// }
	// }
	// }
	//
	// // System.out.println(lProperty.getValue());
	// outProp[1] = lProperty;
	// outArrL.add(outProp);
	//
	// }
	// counter++;
	// }
	//
	// // System.out.println(applyPerfDef(tempProp));
	// return outArrL;
	// }

	public String[] getPerfectDef(ArrayList outArrL) {
		return null;
//		ArrayList tempProp = new ArrayList();
//		String[] out = new String[2];
//		for (int x = 0; x < outArrL.size(); x++) {
//		tempProp.add(((D2ItemProperty[]) outArrL.get(x))[0]);
//		}
//		out[1] = Long.toString(applyPerfDef(tempProp));
//		tempProp = new ArrayList();
//		for (int x = 0; x < outArrL.size(); x++) {
//		tempProp.add(((D2ItemProperty[]) outArrL.get(x))[1]);
//		}
//		out[0] = Long.toString(applyPerfDef(tempProp));

//		return out;
	}

	public String[] getPerfectDmg(ArrayList outArrL) {
		return null;
//		ArrayList tempProp = new ArrayList();
//		String[] out = new String[2];
//		for (int x = 0; x < outArrL.size(); x++) {
//		tempProp.add(((D2ItemProperty[]) outArrL.get(x))[0]);
//		}
//		tempProp = applyPerfEDmg(tempProp);
//		String outStr = "One Hand Damage: ";
//		for (int x = 0; x < tempProp.size(); x = x + 1) {
//		if (x == 0) {
//		outStr = outStr + tempProp.get(x) + " - ";
//		} else if (x == 1) {
//		outStr = outStr + tempProp.get(x) + "\n";
//		} else if (x == 2) {
//		if (iThrow) {
//		outStr = outStr + "Throw Damage: " + tempProp.get(x)
//		+ " - ";
//		} else {
//		outStr = outStr + "Two Hand Damage: " + tempProp.get(x)
//		+ " - ";
//		}
//		} else if (x == 3) {
//		outStr = outStr + tempProp.get(x) + "\n";
//		}
//		}
//		out[0] = outStr;
//		tempProp = new ArrayList();
//		for (int x = 0; x < outArrL.size(); x++) {
//		tempProp.add(((D2ItemProperty[]) outArrL.get(x))[1]);
//		}
//		tempProp = applyPerfEDmg(tempProp);

//		outStr = "One Hand Damage: ";
//		for (int x = 0; x < tempProp.size(); x = x + 1) {
//		if (x == 0) {
//		outStr = outStr + tempProp.get(x) + " - ";
//		} else if (x == 1) {
//		outStr = outStr + tempProp.get(x) + "\n";
//		} else if (x == 2) {
//		if (iThrow) {
//		outStr = outStr + "Throw Damage: " + tempProp.get(x)
//		+ " - ";
//		} else {
//		outStr = outStr + "Two Hand Damage: " + tempProp.get(x)
//		+ " - ";
//		}
//		} else if (x == 3) {
//		outStr = outStr + tempProp.get(x) + "\n";
//		}
//		}
//		out[1] = outStr;

//		return out;
	}

	public ArrayList getPerfectString() {

		// if (isUnique() || isSet()) {
		// return sortStats(getPerfectStringUS());
		// }else if(isRuneWord()){
		//
		// ArrayList perfArr = getPerfectStringUS();
		// for(int x = 0;x<iGemProps.size();x=x+1){
		// //D2ItemProperty[] tempP =
		// {(D2ItemProperty)iGemProps.get(x),(D2ItemProperty)iGemProps.get(x)};
		// //perfArr.add(tempP);
		// }
		//
		//
		// return sortStats(perfArr);
		// }

		return null;
	}

	// private ArrayList sortStats(ArrayList perfectStringUS) {
	//
	// int[] sortArr = new int[perfectStringUS.size()];
	// ArrayList outSorted = new ArrayList();
	// for (int x = 0; x < perfectStringUS.size(); x = x + 1) {
	// sortArr[x] = (((D2ItemProperty[]) perfectStringUS.get(x))[0])
	// .getiProp();
	//
	// }
	//
	// Arrays.sort(sortArr);
	//
	// for (int x = 0; x < sortArr.length; x = x + 1) {
	// D2ItemProperty[] obj = findObjProp(sortArr[x], perfectStringUS);
	// outSorted.add(x, obj);
	// perfectStringUS.remove(obj);
	// }
	//
	// return outSorted;
	// }

	// private D2ItemProperty[] findObjProp(int i, ArrayList perfectStringUS) {
	//
	// for (int x = 0; x < perfectStringUS.size(); x = x + 1) {
	// if ((((D2ItemProperty[]) perfectStringUS.get(x))[0]).getiProp() == i) {
	// return (D2ItemProperty[]) perfectStringUS.get(x);
	// }
	//
	// }
	//
	// return null;
	// }

//	private ArrayList applyPerfEDmg(ArrayList iProperties) {

//	int ENDam = 0;
//	int ENMaxDam = 0;
//	int MinDam = 0;
//	int MaxDam = 0;
//	ArrayList out = new ArrayList();

//	for (int x = 0; x < iProperties.size(); x = x + 1) {
//	if (((D2ItemProperty) iProperties.get(x)).getiProp() == 17) {
//	ENDam = ENDam
//	+ ((D2ItemProperty) iProperties.get(x)).getRealValue();
//	}
//	if (((D2ItemProperty) iProperties.get(x)).getiProp() == 21) {
//	MinDam = MinDam
//	+ ((D2ItemProperty) iProperties.get(x)).getRealValue();
//	}
//	if (((D2ItemProperty) iProperties.get(x)).getiProp() == 22) {
//	MaxDam = MaxDam
//	+ ((D2ItemProperty) iProperties.get(x)).getRealValue();
//	}
//	if (((D2ItemProperty) iProperties.get(x)).getiProp() == 219) {
//	ENMaxDam = ENMaxDam
//	+ (int) Math.floor((((D2ItemProperty) iProperties
//	.get(x)).getRealValue() * 0.125)
//	* iCharLvl);
//	}
//	if (((D2ItemProperty) iProperties.get(x)).getiProp() == 218) {
//	MaxDam = MaxDam
//	+ (int) Math.floor((((D2ItemProperty) iProperties
//	.get(x)).getRealValue() * 0.125)
//	* iCharLvl);
//	}
//	}

//	out.add(String.valueOf((long) Math
//	.floor((((double) i1Dmg[0] / (double) 100) * ENDam)
//	+ (i1Dmg[0] + MinDam))));
//	out
//	.add(String
//	.valueOf((long) Math
//	.floor((((double) i1Dmg[2] / (double) 100) * (ENDam + ENMaxDam))
//	+ (i1Dmg[2] + MaxDam))));

//	if (iWhichHand == 0) {
//	out.add(String.valueOf((long) Math
//	.floor((((double) i2Dmg[0] / (double) 100) * ENDam)
//	+ (i2Dmg[0] + MinDam))));
//	out
//	.add(String
//	.valueOf((long) Math
//	.floor((((double) i2Dmg[2] / (double) 100) * (ENDam + ENMaxDam))
//	+ (i2Dmg[2] + MaxDam))));
//	}

//	return out;

//	}

//	private long applyPerfDef(ArrayList iProperties) {

//	// int ENDef = 0;
//	// int Def = 0;

//	// if (isSet()) {

//	// for (int x = 0; x < iSetProps.size(); x = x + 1) {
//	// if (((D2ItemProperty) iSetProps.get(x)).getiProp() == 16) {
//	// ENDef = ENDef
//	// + ((D2ItemProperty) iSetProps.get(x))
//	// .getRealValue();
//	// }
//	// if (((D2ItemProperty) iSetProps.get(x)).getiProp() == 31) {
//	// Def = Def
//	// + ((D2ItemProperty) iSetProps.get(x))
//	// .getRealValue();
//	// }
//	// if (((D2ItemProperty) iSetProps.get(x)).getiProp() == 214) {
//	// Def = Def
//	// + (int) Math.floor((((D2ItemProperty) iSetProps
//	// .get(x)).getRealValue() * 0.125)
//	// * iCharLvl);
//	// }
//	// }

//	// }

//	// for (int x = 0; x < iProperties.size(); x = x + 1) {
//	// if (((D2ItemProperty) iProperties.get(x)).getiProp() == 16) {
//	// ENDef = ENDef
//	// + ((D2ItemProperty) iProperties.get(x)).getRealValue();
//	// }
//	// if (((D2ItemProperty) iProperties.get(x)).getiProp() == 31) {
//	// Def = Def
//	// + ((D2ItemProperty) iProperties.get(x)).getRealValue();
//	// }
//	// if (((D2ItemProperty) iProperties.get(x)).getiProp() == 214) {
//	// Def = Def
//	// + (int) Math.floor((((D2ItemProperty) iProperties
//	// .get(x)).getRealValue() * 0.125)
//	// * iCharLvl);
//	// }
//	// }
//	// return (int) Math.floor((((double) iInitDef / (double) 100) * ENDef)
//	// + (iInitDef + Def));

//	return 0;
//	}

	public void setCharLvl(int pCharLvl) {
		iCharLvl = pCharLvl;
//		setCharLvl(iProperties, pCharLvl);
//		setCharLvl(iSet1, pCharLvl);
//		setCharLvl(iSet2, pCharLvl);
//		setCharLvl(iSet3, pCharLvl);
//		setCharLvl(iSet4, pCharLvl);
//		setCharLvl(iSet5, pCharLvl);
	}

	public String getPreSuf() {

		String retStr = "";
		for (int x = 0; x < rare_prefixes.length; x++) {

			if (rare_prefixes[x] > 1) {

				retStr = retStr
				+ D2TblFile.getString(D2TxtFile.PREFIX.getRow(
						rare_prefixes[x]).get("Name")) + " ";
			}
		}

		retStr = retStr + iBaseItemName + " ";

		for (int x = 0; x < rare_suffixes.length; x++) {

			if (rare_suffixes[x] > 1) {

				retStr = retStr
				+ D2TblFile.getString(D2TxtFile.SUFFIX.getRow(
						rare_suffixes[x]).get("Name")) + " ";
			}
		}

		return retStr;
	}

	public boolean conforms(String prop, int pVal, boolean min) {

		String dumpStr = itemDump(true);

		if (dumpStr.toLowerCase().indexOf(prop.toLowerCase()) != -1) {

			if (pVal == -1337) {
				return true;
			}
			Pattern propertyLinePattern = Pattern.compile("(\\n.*"+prop.toLowerCase()+".*\\n)", Pattern.UNIX_LINES);
			Matcher propertyPatternMatcher = propertyLinePattern.matcher("\n" + dumpStr.toLowerCase() + "\n" );
			while(propertyPatternMatcher.find()){
				Pattern pat = Pattern.compile("[^\\(?](\\d+)");
				Matcher mat = pat.matcher(propertyPatternMatcher.group());
				while (mat.find()) {
					if(mat.groupCount() > 0){
						if (min == true) {
							if (Integer.parseInt(mat.group(1)) >= pVal) {

								return true;
							}
						} else {
							if (Integer.parseInt(mat.group(1)) <= pVal) {

								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	public int getBlock() {
		return (int) this.cBlock;
	}

	public boolean isABelt() {
		if (iType.equals("belt")) {
			return true;
		} else {
			return false;
		}
	}

	public D2PropCollection getPropCollection() {
		return iProps;
	}

	public String getItemQuality() {
		return iItemQuality;
	}

	public String getFileName() {
		return iFileName;
	}

	public boolean isCharacter() {
		return iIsChar;
	}

	public void refreshItemMods(){
		if (isTypeArmor() || isTypeWeapon()) {
			applyItemMods();
		}
	}

	public String getBaseItemName(){
		if(!iItemName.equals(iBaseItemName)){
			return iBaseItemName;
		}

		return "";
	}

	public boolean isMoveable(){

		if(get_location() == 0 && get_panel() == 1 && (getName().toLowerCase().equals("horadric cube") ||isCharm() || getName().toLowerCase().equals("key") || getName().toLowerCase().indexOf("tome of") != -1)){
			//Inv
		}else if(get_location() == 2){
			//Belt
		}else if(get_location() == 0 && get_panel() == 5 && getName().toLowerCase().equals("horadric cube")){
			//Stash
		}else if(get_location() == 1){
			//equipped
		}else{
			return true;
		}
		return false;
	}

}
