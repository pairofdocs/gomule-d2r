/*******************************************************************************
 * 
 * Copyright 2009 Silospen
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

import java.util.ArrayList;

import randall.d2files.D2TblFile;
import randall.d2files.D2TxtFile;
import randall.d2files.D2TxtFileItemProperties;

public class D2Prop {

	private int[] pVals;
	private int pNum;
	private int funcN;
	private int qFlag;
	private boolean opApplied;
	//Quality flag is there to seperate different types of items
	//0 = ordinary item
	//1 = Jewels
	//2 = Set 2 items
	//3 = Set 3 items
	//4 = Set 4 items
	//5 = Set 5 items
	//6 = Set ? Items
	//7 = Rune/Gem weapons
	//8 = Rune/Gem armor
	//9 = Rune/Gem shields
//  12 = Set2Activated
//	13 = Set3Activated
//	14 = Set4Activated
//	15 = Set5Activated
//	16 = Set?Activated

//  22 = Set2DeActivated
//	23 = Set3DeActivated
//	24 = Set4DeActivated
//	25 = Set5DeActivated
//	26 = FULLSetDeActivated
//  32 = Set2Activated
//	33 = Set3Activated
//	34 = Set4Activated
//	35 = Set5Activated
//	36 = FULLSetActivated


	public D2Prop(int pNum, int[] pVals, int qFlag){

		this.pNum = pNum;
		this.pVals = pVals;
		this.qFlag = qFlag;
	}

	public D2Prop(D2Prop newProp) {
		this.pNum = newProp.getPNum();
		this.pVals = (int[]) newProp.getPVals().clone();
		this.qFlag = 0;
	}


	public D2Prop(int pNum, int[] pVals, int qFlag, boolean opApplied, int funcN) {


		this.pNum = pNum;
		this.pVals = pVals;
		this.qFlag = qFlag;
		this.opApplied = opApplied;
		this.funcN = funcN;
	}

	public void setPNum(int pNum){
		this.pNum = pNum;
	}

	public void setPVals(int[] pVals){
		this.pVals = pVals;
	}

	public int getQFlag() {
		return qFlag;
	}

	public void setFuncN(int funcN){
		this.funcN = funcN;
	}

	public int[] getPVals() {

		return pVals;
	}

	public int getPNum(){

		return pNum;
	}

	public int getFuncN(){
		return funcN;
	}

	public void modifyVals(int funcN, int[] pVals){

		this.funcN = funcN;
		this.pVals = pVals;

	}

	public String generateDisplay(int qFlag, int cLvl) {

		if(this.qFlag != qFlag){
			return null;
		}

		String oString = D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstrpos"));

		//FUNCTION 0 means that you should use the txt files to find the print function to use. Otherwise, it should be a case of looking for custom funcs
		if(funcN == 0){

			if( D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descfunc") != null && ! D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descfunc").equals("")){
				funcN = Integer.parseInt( D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descfunc"));
			}
		}

		int dispLoc = 1;
		try{
			dispLoc= Integer.parseInt(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descval"));
		}catch(NumberFormatException e){
			//leave dispLoc as  = 1
		}

		//Max Durability
		if (pNum == 73){
			oString = "Maximum Durability";
			funcN =1;
		}else if(pNum == 92){
			oString = "Required Level";
			funcN = 1;
			dispLoc = 2;	
		}else if(pNum == 356){
			funcN = 40;
		}else if(pNum == 26 || pNum == 8){
			oString = "Replenishes Mana";
			funcN = 2;
			dispLoc = 2;
		}else if(pNum == 6){
			oString = "Replenishes Health";
			funcN = 2;
			dispLoc = 2;
		}else if(pNum == 126){
			if(pVals.length > 1){
				pVals[0] = pVals[1];
			}
		}

		switch(funcN){

		case(1):
			if(dispLoc == 1){
				if(oString.indexOf("%d") != -1){
					return oString.replaceAll("%d", Integer.toString(pVals[0]));	
				}else{
					if(pVals[0] > -1){
						return "+" + pVals[0]+ " " + oString;
					}else{
						return pVals[0]+ " " + oString;
					}
				}
			}else if(dispLoc == 2){

				if(pVals[0] > -1){
					return oString + " +" + pVals[0];
				}else{
					return oString + " " + pVals[0];
				}


			}else{
				return oString;
			}
		case(2):
			if(dispLoc == 1){
				return pVals[0] + "% " + oString;
			}else if(dispLoc == 2){
				return oString + " " + pVals[0] + "%" ;
			}else{
				return oString;
			}

		case(3):
			if(dispLoc == 1){
				return  pVals[0] + " " + oString;

			}else if(dispLoc == 2){
				return   oString  + " " + pVals[0];
			}else{
				return oString;
			}

		case(4):
			if(dispLoc == 1){

				if(pVals[0] > -1){
					return "+" + pVals[0]+"% " + oString;
				}else{
					return pVals[0]+"% " + oString;
				}



			}else if(dispLoc == 2){

				if(pVals[0] > -1){
					return oString + " +" + pVals[0]+"%" ;
				}else{
					return oString + " " +  pVals[0]+"%" ;
				}

			}else{
				return oString;
			}

		case(5):
			if(dispLoc == 1){
				return ((pVals[0]*100)/128) +"% " + oString;

			}else if(dispLoc == 2){
				return oString + " " + ((pVals[0]*100)/128) +"%" ;
			}else{
				return oString;
			}

		case(6):
			if(dispLoc == 1){
				return "+" + pVals[0]+" " + oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2"));

			}else if(dispLoc == 2){
				return oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2")) + " +" + pVals[0];
			}else{
				return oString;
			}

		case(7):
			if(dispLoc == 1){
				return pVals[0]+"% " + oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2"));

			}else if(dispLoc == 2){
				return oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2")) + pVals[0]+"%";
			}else{
				return oString;
			}

		case(8):
			if(dispLoc == 1){
				return "+" + pVals[0]+"% " + oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2"));

			}else if(dispLoc == 2){
				return  oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2")) + " +" + pVals[0]+"%";
			}else{
				return oString;
			}

		case(9):
			if(dispLoc == 1){
				return pVals[0]+" " + oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2"));

			}else if(dispLoc == 2){
				return oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2")) + " " + pVals[0];
			}else{
				return oString;
			}

		case(10):
			if(dispLoc == 1){
				return (pVals[0]*100)/128 +"% " + oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2"));

			}else if(dispLoc == 2){
				return oString + " " + D2TblFile.getString(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descstr2")) +  (pVals[0]*100)/128 +"%";
			}else{
				return oString;
			}

		case(11):

			return "Repairs 1 Durability in " + (100/pVals[0]) + " Seconds";

		case(12):
			if(dispLoc == 1){
				return "+" + pVals[0]+" " + oString;

			}else if(dispLoc == 2){
				return oString + " +" + pVals[0];
			}else{
				return oString;
			}

		case(13):

			return "+" + pVals[1] + " to " + D2TxtFile.getCharacterCode(pVals[0]) + " Skill Levels";

		case(14):

			return "+" + pVals[1] + " to " + getSkillTree(pVals[0]);

		case(15):

			oString = oString.replaceFirst("%d%", Integer.toString(pVals[2]));
			oString = oString.replaceAll("%d", Integer.toString(pVals[0]));
			D2TxtFileItemProperties lRow = D2TxtFile.SKILLS.getRow(pVals[1]);
			String lText = lRow.get("skilldesc");
			String lString = null;
			if ( !"".equals(lText) ) {
				lString = D2TblFile.getString(D2TxtFile.SKILL_DESC.searchColumns("skilldesc",lText).get("str name"));
			}
			if ( lString == null ) {
				lString = "Unknown";
			}
			return oString.replaceAll("%s", lString);

		case(16):

			oString = oString.replaceAll("%d", Integer.toString(pVals[1]));
		return oString.replaceAll("%s", D2TblFile.getString(D2TxtFile.SKILL_DESC.searchColumns("skilldesc",D2TxtFile.SKILLS.getRow(pVals[0]).get("skilldesc")).get("str name")));


		case(17):

			return "By time!? Oh shi....";


		case(18):
			return "By time!? Oh shi....";

		case(20):
			if(dispLoc == 1){
				return (pVals[0] * -1) + "% " + oString;
			}else if(dispLoc == 2){
				return  oString + " " +  (pVals[0] * -1) + "%";
			}else{
				return oString;
			}


		case(21):
			if(dispLoc == 1){
				return (pVals[0] * -1) + " " + oString;
			}else if(dispLoc == 2){
				return  oString + " " +  (pVals[0] * -1);
			}else{
				return oString;
			}

		case(23):

			return pVals[1] + "% " + oString + " " +  D2TblFile.getString(D2TxtFile.MONSTATS.getRow(pVals[0]).get("NameStr"));				

		case(24):

			oString = oString.replaceFirst("%d", Integer.toString(pVals[2]));
		oString = oString.replaceAll("%d", Integer.toString(pVals[3]));
		return "Level " + pVals[0] + " " + D2TblFile.getString(D2TxtFile.SKILL_DESC.searchColumns("skilldesc",D2TxtFile.SKILLS.getRow(pVals[1]).get("skilldesc")).get("str name")) + " " + oString;

		case(27):
			return "+" + pVals[1] + " to " + D2TblFile.getString(D2TxtFile.SKILL_DESC.searchColumns("skilldesc",D2TxtFile.SKILLS.getRow(pVals[0]).get("skilldesc")).get("str name")) + " " + D2TblFile.getString((D2TxtFile.SKILLS.getRow(D2TxtFile.SKILL_DESC.getRow(pVals[0]).getRowNum()).get("charclass").charAt(0) + "").toUpperCase() + D2TxtFile.SKILLS.getRow(D2TxtFile.SKILL_DESC.getRow(pVals[0]).getRowNum()).get("charclass").substring(1) + "Only");

		case(28):

			return "+" + pVals[1] + " to " + D2TblFile.getString(D2TxtFile.SKILL_DESC.searchColumns("skilldesc",D2TxtFile.SKILLS.getRow(pVals[0]).get("skilldesc")).get("str name"));


		//UNOFFICIAL PROPERTIES

		//Enhanced Damage
		case(30):

			return pVals[0] + "% Enhanced Damage";

		case(31):

			return "Adds " + pVals[0] + " - " + pVals[1] + " Damage";

		case(32):

			return "Adds " + pVals[0] + " - " + pVals[1] + " Fire Damage";

		case(33):

			return "Adds " + pVals[0] + " - " + pVals[1] + " Lightning Damage";

		case(34):

			return "Adds " + pVals[0] + " - " + pVals[1] + " Magic Damage";

		case(35):

			if(pVals[0] == pVals[1]){
				return "Adds " + pVals[0] + " Cold Damage Over " + Math.round((double)pVals[2]/25.0) + " Secs (" + pVals[2] + " Frames)";	
			}

		return "Adds " + pVals[0] + " - " + pVals[1] + " Cold Damage Over " + Math.round((double)pVals[2]/25.0) + " Secs (" + pVals[2] + " Frames)";

		case(36):

			if(pVals.length == 4){

				if(pVals[0] == pVals[1]){
					return "Adds " + Math.round(pVals[0]*((double)pVals[2]/(double)pVals[3])/256)  + " Poison Damage Over " + (int)Math.floor(((double)pVals[2]/(double)pVals[3])/25.0) + " Secs (" + pVals[2] + " Frames)";
				}

				return "Adds " + Math.round(pVals[0]*((double)pVals[2]/(double)pVals[3])/256) + " - " + Math.round(pVals[1]*((double)pVals[2]/(double)pVals[3])/256) + " Poison Damage Over " + (int)Math.floor(((double)pVals[2]/(double)pVals[3])/25.0) + " Secs (" + pVals[2] + " Frames)";

			}else{

				if(pVals[0] == pVals[1]){
					return "Adds " + Math.round(pVals[0]*(double)pVals[2]/256)  + " Poison Damage Over " + (int)Math.floor((double)pVals[2]/25.0) + " Secs (" + pVals[2] + " Frames)";
				}

				return "Adds " + Math.round(pVals[0]*(double)pVals[2]/256) + " - " + Math.round(pVals[1]*(double)pVals[2]/256) + " Poison Damage Over " + (int)Math.floor((double)pVals[2]/25.0) + " Secs (" + pVals[2] + " Frames)";
			}
		case (37):

			return "All Resistances +" + pVals[0];

		case (38):

			return "All Stats +" + pVals[0];

		case (39):

			return "Level " + pVals[1] + " " + D2TxtFile.getCharacterCode(pVals[0]);

		case (40):

			switch (pVals[0]){

			case 0:
				return "Found In Normal Difficulty";
			case 1:
				return "Found In Nightmare Difficulty";
			case 2:
				return "Found In Hell Difficulty";
			}
		}

		return "Unrecognized property: " + this.pNum;
	}

	public void applyOp(int cLvl) {

		if(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("op").equals(""))return;
		if(opApplied)return;

		int op = Integer.parseInt(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("op"));

		switch(op){

		case (2):
		case (4):
		case (5):

			if(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("op base").equals("level")){

				pVals[0] = (int)Math.floor(((double)(pVals[0] * cLvl)) / ((double)(Math.pow(2, Integer.parseInt(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("op param")))))); 
			}
		}
		opApplied = true;
	}

	public String getSkillTree(int lSkillNr){

		switch (lSkillNr)
		{
		case 0:
			return "Bow and Crossbow Skills (Amazon Only)";

		case 1:
			return "Passive and Magic Skills (Amazon Only)";

		case 2:
			return "Javelin and Spear Skills (Amazon Only)";

		case 8:
			return "Fire Skills (Sorceress Only)";

		case 9:
			return "Lightning Skills (Sorceress Only)";

		case 10:
			return "Cold Skills (Sorceress Only)";

		case 16:
			return "Curses (Necromancer only)";

		case 17:
			return "Poison and Bone Skills (Necromancer Only)";

		case 18:
			return "Summoning Skills (Necromancer Only)";

		case 24:
			return "Combat Skills (Paladin Only)";

		case 25:
			return "Offensive Aura Skills (Paladin Only)";

		case 26:
			return "Defensive Aura Skills (Paladin Only)";

		case 32:
			return "Combat Skills (Barbarian Only)";

		case 33:
			return "Masteries Skills (Barbarian Only)";

		case 34:
			return "Warcry Skills (Barbarian Only)";

		case 40:
			return "Summoning Skills (Druid Only)";

		case 41:
			return "Shape-Shifting Skills (Druid Only)";

		case 42:
			return "Elemental Skills (Druid Only)";

		case 48:
			return "Trap Skills (Assassin Only)";

		case 49:
			return "Shadow Discipline Skills (Assassin Only)";

		case 50:
			return "Martial Art Skills (Assassin Only)";

		}
		return "Unknown Tree (P 188)";

	}

	public void addPVals(int[] newVals) {

//		Poison length needs to keep track of the number of properties contributing to it. 
//		Therefore, [2] becomes the counter.
		if(getPNum() == 59){

			if(pVals.length < 2){
				pVals = new int[]{pVals[0], pVals[0], 0};
			}
			if(newVals.length < 2){
				newVals = new int[]{newVals[0], newVals[0], 0};
			}
			
			if(pVals[2] == 0){
				pVals  = new int[]{pVals[0], pVals[1], 1};
			}
			if(newVals[2] == 0){
				newVals  = new int[]{newVals[0], newVals[1], 1};
			}
		}

		int vLen = pVals.length;

		if(pVals.length > newVals.length){
			vLen = newVals.length;
		}

		for(int z = 0;z<vLen;z++){
			pVals[z] = pVals[z] + newVals[z];
		}

	}

	public int getDescPriority() {

		if(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descpriority").equals("")){
			if(pNum == 183){
				return 38;
			}else if(pNum == 184){
				return 69;
			}
			return 0;
		}else{		
			return Integer.parseInt(D2TxtFile.ITEM_STAT_COST.getRow(pNum).get("descpriority"));
		}
	}

	public void addCharMods(int[] outStats, ArrayList plSkill, int cLvl, int op, int qFlagMarker) {

		//If it's 0 we only want standard properties (non set)
		if(qFlagMarker == 0){
			if(qFlag != 0)return;
		}else{
			if(qFlag != 12 && qFlag != 13 && qFlag != 14 && qFlag != 15 && qFlag != 16)return;
		}
		
		if(!opApplied)applyOp(cLvl);

		/**
		 *0 Str 0 
		 *1 Str/lvl 220
		 *2 En 1
		 *3 En/lvl 222
		 *4 Dex 2
		 *5 Dex/lvl 221
		 *6 Vit 3
		 *7 Vit/lvl 223
		 *8 Life 7
		 *9 HP/lvl 216
		 *10 Mana 9
		 *11 Mana/Lvl 217
		 *12 Stam 11 
		 *13 Stam/lvl 242
		 *14 AR 19
		 *15 AR/% 119
		 *16 AR/lvl 224
		 *17 AR/%/Lvl 225
		 *18 FR 39
		 *19 LR 41
		 *20 CR 43 
		 *21 PR 45
		 *22 MF 80
		 *23 MF/Lvl 240
		 *24 FRW 96
		 *25 FCR 105
		 *26 IAS 93
		 *27 FHR 99
		 *28 GF 79
		 *29 GF/lvl 239
		 *30 AllSkill 127
		 *31 Block 20
		 */


		switch(pNum){

		case(0):
			outStats[0] = outStats[0] + (pVals[0]*op);
		break;
		case(1):
			outStats[2] = outStats[2] + (pVals[0]*op);
		break;
		case(2):
			outStats[4] = outStats[4] + (pVals[0]*op);
		break;
		case(3):
			outStats[6] = outStats[6] + (pVals[0]*op);
		break;
		case(7):
			outStats[8] = outStats[8] + (pVals[0]*op);
		break;
		case(9):
			outStats[10] = outStats[10] + (pVals[0]*op);
		break;
		case(11):
			outStats[12] = outStats[12] + (pVals[0]*op);
		break;
		case(19):
			outStats[14] = outStats[14] + (pVals[0]*op);
		break;
		case(20):
			outStats[30] = outStats[30] + (pVals[0]*op);
		break;
		case(39):
			outStats[18] = outStats[18] + (pVals[0]*op);
		break;
		case(41):
			outStats[19] = outStats[19] + (pVals[0]*op);
		break;
		case(43):
			outStats[20] = outStats[20] + (pVals[0]*op);
		break;
		case(45):
			outStats[21] = outStats[21] + (pVals[0]*op);
		break;
		case(79):
			outStats[28] = outStats[28] + (pVals[0]*op);
		break;
		case(80):
			outStats[22] = outStats[22] + (pVals[0]*op);
		break;
		case(93):
			outStats[26] = outStats[26] + (pVals[0]*op);
		break;
		case(96):
			outStats[24] = outStats[24] + (pVals[0]*op);
		break;
		case(99):
			outStats[27] = outStats[27] + (pVals[0]*op);
		break;
		case(105):
			outStats[25] = outStats[25] + (pVals[0]*op);
		break;
		case(119):
			outStats[15] = outStats[15] + (pVals[0]*op);
		break;
		case(216):
			outStats[9] = outStats[9] + (pVals[0]*op);
		break;
		case(217):
			outStats[11] = outStats[11] + (pVals[0]*op);
		break;
		case(220):
			outStats[1] = outStats[1] + (pVals[0]*op);
		break;
		case(221):
			outStats[5] = outStats[5] + (pVals[0]*op);
		break;
		case(222):
			outStats[3] = outStats[3] + (pVals[0]*op);
		break;
		case(223):
			outStats[7] = outStats[7] + (pVals[0]*op);
		break;
		case(224):
			outStats[16] = outStats[16] + (pVals[0]*op);
		break;
		case(225):
			outStats[17] = outStats[17] + (pVals[0]*op);
		break;
		case(239):
			outStats[29] = outStats[29] + (pVals[0]*op);
		break;
		case(240):
			outStats[23] = outStats[23] + (pVals[0]*op);
		break;
		case(242):
			outStats[13] = outStats[13] + (pVals[0]*op);
		break;
		// + SKILLS
		case(188):
		case(126):
		case(97):
		case(107):
		case(83):
		case(127):
			if(plSkill!= null){
				if(op == 1){
					plSkill.add(this);
				}else{
					plSkill.remove(this);
				}
			}
		}
	}

	public void setQFlag(int newQ) {
		qFlag = newQ;
	}
}
