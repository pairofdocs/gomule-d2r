/*******************************************************************************
 * 
 * Copyright 2008 Silospen
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
package gomule.dropCalc.monsters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import randall.d2files.D2TxtFile;
import randall.d2files.D2TxtFileItemProperties;

public class Monster {

	protected D2TxtFileItemProperties monRow;
	protected String monDiff;
	protected String monID;
	protected String monName;
	protected ArrayList mTuples;

	/**
	 * CLASS is now:
	 * 0 - Reg
	 * 1 - Min
	 * 2 - Champ
	 * 3 - Uniq
	 * 4 - SU
	 * 5 - Boss
	 */

	protected int monClass;



	/**
	 * Flag is ZERO if normal, ONE if modified.
	 * @param monRow
	 * @param monDiff
	 * @param flag
	 */

	public Monster(D2TxtFileItemProperties monRow, String monDiff, int monClass, int flag) {

		
		
		if(monClass == 1 && flag == 2){
			this.monID = monRow.get("Name");
		}else{
			this.monID = monRow.get("Id");
		}
		

		
		this.monDiff = monDiff;
		this.monRow = monRow;
		this.monClass = monClass;

	}




//	protected void setUpTuples() {
//	mTuples = new ArrayList();
//	HashMap areas = findLocsMonster();
//	enterMonLevel(areas);
//	ArrayList initTCs = getInitTC(areas);
//	mTuples = createTuples(areas, initTCs);

//	}

	protected ArrayList createTuples(HashMap areas, ArrayList initTCs) {

		ArrayList tOut = new ArrayList();
		Iterator it = areas.keySet().iterator();
		int counter = 0;
		while(it.hasNext()){
			String area = (String) it.next();
			tOut.add(new MonsterTuple(area, (Integer)areas.get(area), (String)initTCs.get(counter), this));

			counter = counter +1;
		}

		return tOut;
	}

	protected HashMap findLocsMonster(int cFlag){
		ArrayList monSearch = new ArrayList();
		HashMap monLvlAreas = new HashMap();

		String selector = "mon1";

		if(monDiff.equals("N")){
			if(cFlag==1){
				selector = "umon1";
			}
		}else{
			selector = "nmon1";
		}


		for(int x = 1;x<11;x=x+1){
			monSearch.clear();
			monSearch = D2TxtFile.LEVELS.searchColumnsMultipleHits(selector, monID);
			if(monSearch.size() > 0){
				for(int y = 0;y<monSearch.size();y=y+1){
					monLvlAreas.put(((D2TxtFileItemProperties)monSearch.get(y)).get("Name"), new Integer(0));
				}
			}
			selector = selector.substring(0, selector.length() - 1) + (new Integer(x+1)); 		
		}

		return monLvlAreas;
	}

	protected ArrayList getInitTC(HashMap monLvlAreas, String header){

		String initTC =  "";
		ArrayList tcArr = new ArrayList();
		Iterator it = monLvlAreas.keySet().iterator();
		while(it.hasNext()){
			String area = (String)it.next();
			if(this.getMonDiff().equals("N")){
				initTC = D2TxtFile.MONSTATS.searchColumns("Id", monID).get(header);
			}else if (this.getMonDiff().equals("NM")){
				initTC = bumpTC(D2TxtFile.MONSTATS.searchColumns("Id", monID).get(header+"(N)"), ((Integer)monLvlAreas.get(area)).intValue());
			}else{
				initTC = bumpTC(D2TxtFile.MONSTATS.searchColumns("Id", monID).get(header+"(H)"), ((Integer)monLvlAreas.get(area)).intValue());
			}
			tcArr.add(initTC);
		}

		return tcArr;
	}


	protected String bumpTC(String initTC, int lvl) {

		
		D2TxtFileItemProperties initTCRow = D2TxtFile.TCS.searchColumns("Treasure Class", initTC);
		if(initTCRow == null){
			return initTC;
		}
		if(initTCRow.get("level").equals("") || Integer.parseInt(initTCRow.get("level")) > lvl){
			return initTC;
		}

		while(Integer.parseInt(initTCRow.get("level")) != 0){
			if(Integer.parseInt(initTCRow.get("level"))<lvl){
				initTCRow = new D2TxtFileItemProperties(D2TxtFile.TCS, initTCRow.getRowNum()+1);
			}else{
				if(Integer.parseInt(initTCRow.get("level"))>lvl){
					return new D2TxtFileItemProperties(D2TxtFile.TCS, initTCRow.getRowNum()).get("Treasure Class");
				}else{
					return initTCRow.get("Treasure Class");
				}
			}
			if(initTCRow.get("level").equals("")){
				initTCRow = new D2TxtFileItemProperties(D2TxtFile.TCS, initTCRow.getRowNum()-1);
				return initTCRow.get("Treasure Class");
			}
		}
		initTCRow = new D2TxtFileItemProperties(D2TxtFile.TCS, initTCRow.getRowNum()-1);
		return initTCRow.get("Treasure Class");
	}

//	public ArrayList getRealInitTC(){
//	return initTC;
//	}

	protected HashMap findLocsBossMonster(){
		HashMap monLvlAreas = new HashMap();

		if(monID.equals("andariel")){
			monLvlAreas.put("Act 1 - Catacombs 4", new Integer(0));
		}else if(monID.equals("duriel")){
			monLvlAreas.put("Act 2 - Duriel's Lair", new Integer(0));
		}else if(monID.equals("radament")){
			monLvlAreas.put("Act 2 - Sewer 1 C", new Integer(0));
		}else if(monID.equals("mephisto")){
			monLvlAreas.put("Act 3 - Mephisto 3", new Integer(0));
		}else if(monID.equals("diablo")){
			monLvlAreas.put("Act 4 - Diablo 1", new Integer(0));
		}else if(monID.equals("summoner")){
			monLvlAreas.put("Act 2 - Arcane", new Integer(0));
		}else if(monID.equals("izual")){
			monLvlAreas.put("Act 4 - Mesa 2", new Integer(0));
		}else if(monID.equals("bloodraven")){
			monLvlAreas.put("Act 1 - Graveyard", new Integer(0));
		}else if(monID.equals("diabloclone")){
		}else if(monID.equals("griswold")){
			monLvlAreas.put("Act 1 - Tristram", new Integer(0));
		}else if(monID.equals("nihlathakboss")){
			monLvlAreas.put("Act 5 - Temple Boss", new Integer(0));
		}else if(monID.equals("baalcrab")){
			monLvlAreas.put("Act 5 - World Stone", new Integer(0));
		}
		else if(monID.equals("putriddefiler1")){
			monLvlAreas.put("Act 5 - Temple 2", new Integer(0));
			monLvlAreas.put("Act 5 - Temple Boss", new Integer(0));
		}
		else if(monID.equals("putriddefiler2")){
			monLvlAreas.put("Act 5 - Baal Temple 1", new Integer(0));
			monLvlAreas.put("Act 5 - Temple Boss", new Integer(0));
		}
		else if(monID.equals("putriddefiler3")){
			monLvlAreas.put("Act 5 - Baal Temple 1", new Integer(0));
			monLvlAreas.put("Act 5 - Baal Temple 3", new Integer(0));
		}
		else if(monID.equals("putriddefiler4")){
			monLvlAreas.put("Act 5 - Baal Temple 3", new Integer(0));
		}
		else if(monID.equals("putriddefiler5")){
			monLvlAreas.put("Act 5 - Baal Temple 3", new Integer(0));
		}
		return monLvlAreas;
	}

	protected void findLevelsBossMonster(HashMap monLvlAreas) {

		for(int x = 0;x<monLvlAreas.size();x++){
			String selector = "Level";
			if(monDiff.equals("H")){
				selector = selector + "(H)";   
			}else if(monDiff.equals("NM")){
				selector = selector + "(N)";   
			}
			if(!monID.equals("diabloclone")){
				monLvlAreas.put(monLvlAreas.keySet().toArray()[x],new Integer(D2TxtFile.MONSTATS.searchColumns("Id", monID).get(selector)));
			}
		}
		return;
	}



	public String getID(){
		return this.monID;
	}


	public String getName(){
		return this.monID + "(" +this.monDiff+")";
	}

	public String getRealName(){

		return this.monName  + " - " +  this.monID;

	}






	public void lookupBASETCReturnATOMICTCS(int nPlayers, int nGroupSize, double input, boolean sevP) {

		for(int t = 0;t<mTuples.size();t=t+1){


			
		((MonsterTuple)mTuples.get(t)).lookupBASETCReturnATOMICTCS(nPlayers, nGroupSize, input, sevP);
		
		
		
		}
	}



	public void clearFinal() {

		for(int x = 0;x<mTuples.size();x=x+1){

			((MonsterTuple)mTuples.get(x)).getFinalTCs().clear();


		}


	}

	public HashMap getFinal() {

		HashMap finalList = new HashMap();
		for(int x = 0;x<mTuples.size();x=x+1){

			finalList.putAll(((MonsterTuple)mTuples.get(x)).getFinalTCs());


		}

		return finalList;


	}


	protected void findLocsSU(int i, HashMap lvlArr, String ID) {


		HashMap areaSUPair = new HashMap();


		areaSUPair.put("Bishibosh", "Act 1 - Wilderness 2");
		areaSUPair.put("Bonebreak","Act 1 - Crypt 1 A");
		areaSUPair.put("Coldcrow","Act 1 - Cave 2");
		areaSUPair.put("Rakanishu","Act 1 - Wilderness 3");
		areaSUPair.put("Treehead WoodFist","Act 1 - Wilderness 4");
		areaSUPair.put("Griswold","Act 1 - Tristram");
		areaSUPair.put("The Countess","Act 1 - Crypt 3 E");
		areaSUPair.put("Pitspawn Fouldog","Act 1 - Jail 2");
		areaSUPair.put("Flamespike the Crawler","Act 1 - Moo Moo Farm");
		areaSUPair.put("Boneash","Act 1 - Cathedral");
		areaSUPair.put("Radament","Act 2 - Sewer 1 C");
		areaSUPair.put("Bloodwitch the Wild","Act 2 - Tomb 2 Treasure");
		areaSUPair.put("Fangskin","Act 2 - Tomb 3 Treasure");
		areaSUPair.put("Beetleburst","Act 2 - Desert 3");
		areaSUPair.put("Leatherarm","Act 2 - Tomb 1 Treasure");
		areaSUPair.put("Coldworm the Burrower","Act 2 - Lair 1 Treasure");
		areaSUPair.put("Fire Eye","Act 2 - Basement 3");
		areaSUPair.put("Dark Elder","Act 2 - Desert 4");
		areaSUPair.put("The Summoner","Act 2 - Arcane");
		areaSUPair.put("Ancient Kaa the Soulless","Act 2 - Tomb Tal");
		areaSUPair.put("The Smith","Act 1 - Barracks");
		areaSUPair.put("Web Mage the Burning","Act 3 - Spider 2");
		areaSUPair.put("Witch Doctor Endugu","Act 3 - Dungeon 2 Treasure");
		areaSUPair.put("Stormtree","Act 3 - Kurast 1");
		areaSUPair.put("Sarina the Battlemaid","Act 3 - Temple 1");
		areaSUPair.put("Icehawk Riftwing","Act 3 - Sewer 1");
		areaSUPair.put("Ismail Vilehand","Act 3 - Travincal");
		areaSUPair.put("Geleb Flamefinger","Act 3 - Travincal");
		areaSUPair.put("Bremm Sparkfist","Act 3 - Mephisto 3");
		areaSUPair.put("Toorc Icefist","Act 3 - Travincal");
		areaSUPair.put("Wyand Voidfinger","Act 3 - Mephisto 3");
		areaSUPair.put("Maffer Dragonhand","Act 3 - Mephisto 3");
		areaSUPair.put("Winged Death","Act 1 - Moo Moo Farm");
		areaSUPair.put("The Tormentor","Act 1 - Moo Moo Farm");
		areaSUPair.put("Taintbreeder","Act 1 - Moo Moo Farm");
		areaSUPair.put("Riftwraith the Cannibal","Act 1 - Moo Moo Farm");
		areaSUPair.put("Infector of Souls","Act 4 - Diablo 1");
		areaSUPair.put("Lord De Seis","Act 4 - Diablo 1");
		areaSUPair.put("Grand Vizier of Chaos","Act 4 - Diablo 1");
		areaSUPair.put("The Cow King","Act 1 - Moo Moo Farm");
		areaSUPair.put("Corpsefire","Act 1 - Cave 1");
		areaSUPair.put("The Feature Creep","Act 4 - Lava 1");
		areaSUPair.put("Siege Boss","Act 5 - Siege 1");
		areaSUPair.put("Ancient Barbarian 1","Act 5 - Mountain Top");
		areaSUPair.put("Ancient Barbarian 2","Act 5 - Mountain Top");
		areaSUPair.put("Ancient Barbarian 3","Act 5 - Mountain Top");
		areaSUPair.put("Axe Dweller","Act 1 - Moo Moo Farm");
		areaSUPair.put("Bonesaw Breaker","Act 5 - Ice Cave 2");
		areaSUPair.put("Dac Farren","Act 5 - Siege 1");
		areaSUPair.put("Megaflow Rectifier","Act 5 - Barricade 1");
		areaSUPair.put("Eyeback Unleashed","Act 5 - Barricade 1");
		areaSUPair.put("Threash Socket","Act 5 - Barricade 2");
		areaSUPair.put("Pindleskin","Act 5 - Temple Entrance");
		areaSUPair.put("Snapchip Shatter","Act 5 - Ice Cave 3A");
		areaSUPair.put("Anodized Elite","Act 1 - Moo Moo Farm");
		areaSUPair.put("Vinvear Molech","Act 1 - Moo Moo Farm");
		areaSUPair.put("Sharp Tooth Sayer","Act 5 - Barricade 1");
		areaSUPair.put("Magma Torquer","Act 1 - Moo Moo Farm");
		areaSUPair.put("Blaze Ripper","Act 1 - Moo Moo Farm");
		areaSUPair.put("Frozenstein","Act 5 - Ice Cave 1A");
		areaSUPair.put("Nihlathak","Act 5 - Temple Boss");
		areaSUPair.put("Baal Subject 1","Act 5 - Throne Room");
		areaSUPair.put("Baal Subject 2","Act 5 - Throne Room");
		areaSUPair.put("Baal Subject 3","Act 5 - Throne Room");
		areaSUPair.put("Baal Subject 4","Act 5 - Throne Room");
		areaSUPair.put("Baal Subject 5","Act 5 - Throne Room");

		if(i==1){
			lvlArr.put(areaSUPair.get(ID), new Integer(0));
		}else{
			lvlArr.put(areaSUPair.get(ID), new Integer(0));

		}
	}


	public void clearFinal(MonsterTuple tuple) {

		((MonsterTuple)mTuples.get(mTuples.indexOf(tuple))).getFinalTCs().clear();

	}



	public ArrayList getmTuples() {
		return this.mTuples;
	}

	public String getMonName(){
		return monName;
	}



	public String getMonID() {
		return monID;
	}



	public int getClassOfMon() {
		return monClass;
	}



	public String getMonDiff() {
		return monDiff;
	}




	public void clearFinalM(MonsterTuple tuple) {

		((MonsterTuple)mTuples.get(mTuples.indexOf(tuple))).getFinalMiscTCs().clear();
		
	}




	public void clearFinalMT(MonsterTuple tuple) {
		((MonsterTuple)mTuples.get(mTuples.indexOf(tuple))).getFinalTrueMiscTCs().clear();
		
	}


//	public String getBossRealName() {
//	// TODO Auto-generated method stub
//	return D2TblFile.getString(getBoss());
//	}


}
