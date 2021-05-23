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
package gomule.dropCalc;

import gomule.dropCalc.items.Item;
import gomule.dropCalc.items.MiscItem;
import gomule.dropCalc.items.SetItem;
import gomule.dropCalc.items.UniqItem;
import gomule.dropCalc.items.WhiteItem;
import gomule.dropCalc.monsters.Boss;
import gomule.dropCalc.monsters.Champion;
import gomule.dropCalc.monsters.Minion;
import gomule.dropCalc.monsters.Monster;
import gomule.dropCalc.monsters.MonsterTuple;
import gomule.dropCalc.monsters.Regular;
import gomule.dropCalc.monsters.SuperUnique;
import gomule.dropCalc.monsters.Unique;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import randall.d2files.D2TblFile;
import randall.d2files.D2TxtFile;
import randall.d2files.D2TxtFileItemProperties;

public class DCNew {

//	CalcWriter CW = new CalcWriter("tempTCs.txt");


//	CalcWriter CW = null;
	ArrayList monID = new ArrayList();
	ArrayList normLvl = new ArrayList();
	ArrayList normTC1 = new ArrayList();

	ArrayList mainRegMonArray = new ArrayList();
	ArrayList mainMinMonArray = new ArrayList();
	ArrayList mainChampMonArray = new ArrayList();
	ArrayList mainUniqArray = new ArrayList();
	ArrayList mainSupUniqArray = new ArrayList();
	ArrayList mainBossArray = new ArrayList();

	ArrayList regItemArray = new ArrayList();
	ArrayList magItemArray = new ArrayList();
	ArrayList rareItemArray = new ArrayList();
	ArrayList setItemArray = new ArrayList();
	ArrayList uniqItemArray = new ArrayList();
	ArrayList miscItemArray = new ArrayList();

	public static void main(String[] args){

		new DCNew();

	}

	public DCNew(){
		D2TxtFile.constructTxtFiles("d2111");
		D2TblFile.readAllFiles("d2111");
		populateArrays();
		populateItemArrays();
//		System.out.println(D2TblFile.getString("Hell2"));

	}

//	public void writeMonstersTC(int TC, int nPlayers, int nGroup){


//	/**
//	* 0:Reg
//	* 1:Min
//	* 2:Champ
//	* 3:Unique
//	* 4:Superunique
//	* 5:Boss
//	*/
//	CW.writeData(TC+",");
//	for(int x = 0;x< mainRegMonArray.size();x=x+1){
//	((Monster)mainRegMonArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers,nGroup,-1);
//	ArrayList mTuples = ((Monster)mainRegMonArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("armo" + TC) || ((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("weap" + TC)){
////	System.out.println(D2TblFile.getString(D2TxtFile.LEVELS.searchColumns("Name",((MonsterTuple)mTuples.get(y)).AreaName).get("LevelName"))+ "," +((Monster)mainRegMonArray.get(x)).monName);
////	System.out.print("0-" + x+"-"+y +",");
//	CW.writeData("0-" + x+"-"+y +",");
//	}
//	}
//	}

//	for(int x = 0;x< mainMinMonArray.size();x=x+1){
//	((Monster)mainMinMonArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers,nGroup,-1);
//	ArrayList mTuples = ((Monster)mainMinMonArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("armo" + TC) || ((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("weap" + TC)){
////	System.out.print("1-" + x+"-"+y +",");
//	CW.writeData("1-" + x+"-"+y +",");
////	System.out.println(((Monster)mainMinMonArray.get(x)).monName+ "," +((Monster)mainMinMonArray.get(x)).getRealBossName() + ","+ D2TblFile.getString(D2TxtFile.LEVELS.searchColumns("Name",((MonsterTuple)mTuples.get(y)).AreaName).get("LevelName"))+ "," + ((Monster)mainMinMonArray.get(x)).monDiff);
//	}
//	}
//	}

//	for(int x = 0;x< mainChampMonArray.size();x=x+1){
//	((Monster)mainChampMonArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers,nGroup,-1);
//	ArrayList mTuples = ((Monster)mainChampMonArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("armo" + TC) || ((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("weap" + TC)){
////	System.out.print("2-" + x+"-"+y +",");
//	CW.writeData("2-" + x+"-"+y +",");
////	System.out.println(((Monster)mainMinMonArray.get(x)).monName+ "," +((Monster)mainMinMonArray.get(x)).getRealBossName() + ","+ D2TblFile.getString(D2TxtFile.LEVELS.searchColumns("Name",((MonsterTuple)mTuples.get(y)).AreaName).get("LevelName"))+ "," + ((Monster)mainMinMonArray.get(x)).monDiff);
//	}
//	}
//	}

//	for(int x = 0;x< mainUniqArray.size();x=x+1){
//	((Monster)mainUniqArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers,nGroup,-1);
//	ArrayList mTuples = ((Monster)mainUniqArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("armo" + TC) || ((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("weap" + TC)){
////	System.out.print("3-" + x+"-"+y +",");
//	CW.writeData("3-" + x+"-"+y +",");
////	System.out.println(((Monster)mainMinMonArray.get(x)).monName+ "," +((Monster)mainMinMonArray.get(x)).getRealBossName() + ","+ D2TblFile.getString(D2TxtFile.LEVELS.searchColumns("Name",((MonsterTuple)mTuples.get(y)).AreaName).get("LevelName"))+ "," + ((Monster)mainMinMonArray.get(x)).monDiff);
//	}
//	}
//	}

//	for(int x = 0;x< mainSupUniqArray.size();x=x+1){
//	if(((Monster)mainSupUniqArray.get(x)).getMonName().equals("Fangskin") || ((SuperUnique)mainSupUniqArray.get(x)).getSUID().startsWith("ancientbarb")){
//	continue;
//	}
//	((Monster)mainSupUniqArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers,nGroup,-1);
//	ArrayList mTuples = ((Monster)mainSupUniqArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("armo" + TC) || ((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("weap" + TC)){
////	System.out.print("4-" + x+"-"+y +",");
//	CW.writeData("4-" + x+"-"+y +",");
////	System.out.println(((Monster)mainMinMonArray.get(x)).monName+ "," +((Monster)mainMinMonArray.get(x)).getRealBossName() + ","+ D2TblFile.getString(D2TxtFile.LEVELS.searchColumns("Name",((MonsterTuple)mTuples.get(y)).AreaName).get("LevelName"))+ "," + ((Monster)mainMinMonArray.get(x)).monDiff);
//	}
//	}
//	}

//	for(int x = 0;x< mainBossArray.size();x=x+1){
//	((Monster)mainBossArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers,nGroup,-1);
//	ArrayList mTuples = ((Monster)mainBossArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("armo" + TC) || ((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey("weap" + TC)){
////	System.out.print("5-" + x+"-"+y +",");
//	CW.writeData("5-" + x+"-"+y +",");
////	System.out.println(((Monster)mainMinMonArray.get(x)).monName+ "," +((Monster)mainMinMonArray.get(x)).getRealBossName() + ","+ D2TblFile.getString(D2TxtFile.LEVELS.searchColumns("Name",((MonsterTuple)mTuples.get(y)).AreaName).get("LevelName"))+ "," + ((Monster)mainMinMonArray.get(x)).monDiff);
//	}
//	}
//	}

//	CW.writeData("\n");
//	}

//	public HashMap findMonstersTC(String key, double d, int monSelection, int nPlayers, int nGroup){

//	//SHOULD BE HASHMAP?
//	HashMap monsterTCList = new HashMap();
//	/**
//	* 0:Reg
//	* 1:Min
//	* 2:Champ
//	* 3:Unique
//	* 4:Superunique
//	* 5:Boss
//	*/

//	switch (monSelection){

//	case 0:

//	for(int x = 0;x< mainRegMonArray.size();x=x+1){
//	((Monster)mainRegMonArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainRegMonArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){
//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d));
//	}
//	}
//	}

//	break;
//	case 1:

//	for(int x = 0;x< mainMinMonArray.size();x=x+1){
//	((Monster)mainMinMonArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainMinMonArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d));
//	}
//	}
//	}
//	break;
//	case 2:
//	for(int x = 0;x< mainChampMonArray.size();x=x+1){
//	((Monster)mainChampMonArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainChampMonArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d));
//	}
//	}
//	}
//	break;
//	case 3:
//	for(int x = 0;x< mainUniqArray.size();x=x+1){
//	((Monster)mainUniqArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainUniqArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d));
//	}
//	}
//	}
//	break;
//	case 4:
//	for(int x = 0;x< mainSupUniqArray.size();x=x+1){
//	if(((Monster)mainSupUniqArray.get(x)).getMonName().equals("Fangskin") || ((SuperUnique)mainSupUniqArray.get(x)).getSUID().startsWith("ancientbarb")){
//	continue;
//	}
//	((Monster)mainSupUniqArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainSupUniqArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d));
//	}
//	}
//	}
//	break;
//	case 5:
//	for(int x = 0;x< mainBossArray.size();x=x+1){
//	((Monster)mainBossArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainBossArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d));
//	}
//	}
//	}
//	break;
//	}


//	return monsterTCList;
//	}

	public HashMap findMonstersTCGeneral(String key, double d, int monSelection, Item item, int MF, int nPlayers, int nGroup, int QRecursions, boolean sevP){

		//SHOULD BE HASHMAP?
		HashMap monsterTCList = new HashMap();
		/**
		 * 0:Reg
		 * 1:Min
		 * 2:Champ
		 * 3:Unique
		 * 4:Superunique
		 * 5:Boss
		 */

		switch (monSelection){

		case 0:

			for(int x = 0;x< mainRegMonArray.size();x=x+1){



				ArrayList mTuples = ((Monster)mainRegMonArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup,0, sevP);

					if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//						System.out.println(((MonsterTuple)mTuples.get(y)).getParent().getID());

						monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * generateRarityList((MonsterTuple)mTuples.get(y), item)* getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true)));
					}
				}
			}

			break;
		case 1:

			for(int x = 0;x< mainMinMonArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainMinMonArray.get(x)).getmTuples();

				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}




					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup,0, sevP);
					if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
						monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * generateRarityList((MonsterTuple)mTuples.get(y), item) * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true)));
					}
				}
			}
			break;
		case 2:
			for(int x = 0;x< mainChampMonArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainChampMonArray.get(x)).getmTuples();

				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup,0, sevP);
					if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
						monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * generateRarityList((MonsterTuple)mTuples.get(y), item)* getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true)));
					}
				}
			}
			break;
		case 3:
			for(int x = 0;x< mainUniqArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainUniqArray.get(x)).getmTuples();

				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup,0, sevP);
					if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
						monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * generateRarityList((MonsterTuple)mTuples.get(y), item)* getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true)));
					}
				}
			}
			break;
		case 4:
			for(int x = 0;x< mainSupUniqArray.size();x=x+1){
				if(((Monster)mainSupUniqArray.get(x)).getMonName().equals("Fangskin") || ((SuperUnique)mainSupUniqArray.get(x)).getSUID().startsWith("ancientbarb")){
					continue;
				}
				ArrayList mTuples = ((Monster)mainSupUniqArray.get(x)).getmTuples();

				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					if(((Monster)mainSupUniqArray.get(x)).getMonID().startsWith("Baal Subject")){
						((MonsterTuple)mTuples.get(y)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup,generateRarityList((MonsterTuple)mTuples.get(y), item) * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true), sevP);
						if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
							monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * generateRarityList((MonsterTuple)mTuples.get(y), item)* getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true)));
						}
					}else{
						((MonsterTuple)mTuples.get(y)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup,d * generateRarityList((MonsterTuple)mTuples.get(y), item) * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true), sevP);
						if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
							monsterTCList.put(mTuples.get(y),((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key));
						}
					}
				}
			}
			break;
		case 5:
			for(int x = 0;x< mainBossArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainBossArray.get(x)).getmTuples();

				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup,d * generateRarityList((MonsterTuple)mTuples.get(y), item)* getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true), sevP);
					if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
						monsterTCList.put(mTuples.get(y),((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key));
					}
				}
			}
			break;
		}


		return monsterTCList;
	}

//	public HashMap findMonstersTC(String key, double d, int monSelection, SetItem item, int MF, int nPlayers, int nGroup){

//	HashMap monsterTCList = new HashMap();
//	/**
//	* 0:Reg
//	* 1:Min
//	* 2:Champ
//	* 3:Unique
//	* 4:Superunique
//	* 5:Boss
//	*/

//	switch (monSelection){

//	case 0:

//	for(int x = 0;x< mainRegMonArray.size();x=x+1){
//	if(item.getRealName().toLowerCase().indexOf("cow")){
//	if(!(((Monster)mainRegMonArray.get(x)).getMonID()).equals("hellbovine")){
//	continue;
//	}
//	}
//	((Monster)mainRegMonArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainRegMonArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){
//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * (1 - getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)),1)) ));
//	}
//	}
//	}

//	break;
//	case 1:

//	for(int x = 0;x< mainMinMonArray.size();x=x+1){
//	if(item.getRealName().toLowerCase().indexOf("cow")){

//	if(!(((Monster)mainMinMonArray.get(x)).getMonID()).equals("hellbovine")){
//	continue;
//	}
//	}
//	((Monster)mainMinMonArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainMinMonArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)),1) ));
//	}
//	}
//	}
//	break;
//	case 2:
//	for(int x = 0;x< mainChampMonArray.size();x=x+1){
//	if(item.getRealName().toLowerCase().indexOf("cow")){
//	if(!(((Monster)mainChampMonArray.get(x)).getMonID()).equals("hellbovine")){
//	continue;
//	}
//	}
//	((Monster)mainChampMonArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainChampMonArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)),1) ));
//	}
//	}
//	}
//	break;
//	case 3:
//	for(int x = 0;x< mainUniqArray.size();x=x+1){
//	if(item.getRealName().toLowerCase().indexOf("cow")){
//	if(!(((Monster)mainUniqArray.get(x)).getMonID()).equals("hellbovine")){
//	continue;
//	}
//	}
//	((Monster)mainUniqArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainUniqArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)),1) ));
//	}
//	}
//	}
//	break;
//	case 4:
//	for(int x = 0;x< mainSupUniqArray.size();x=x+1){
//	if(item.getRealName().toLowerCase().indexOf("cow")){
//	if(!(((Monster)mainSupUniqArray.get(x)).getMonID()).equals("The Cow King")){
//	continue;
//	}
//	}
//	if(((Monster)mainSupUniqArray.get(x)).getMonName().equals("Fangskin") || ((SuperUnique)mainSupUniqArray.get(x)).getSUID().startsWith("ancientbarb")){
//	continue;
//	}
//	((Monster)mainSupUniqArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainSupUniqArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)),1) ));
//	}
//	}
//	}
//	break;
//	case 5:
//	for(int x = 0;x< mainBossArray.size();x=x+1){
//	if(item.getRealName().toLowerCase().indexOf("cow")){
//	if(!(((Monster)mainBossArray.get(x)).getMonID()).equals("hellbovine")){
//	continue;
//	}
//	}
//	((Monster)mainBossArray.get(x)).lookupBASETCReturnATOMICTCS(nPlayers, nGroup);
//	ArrayList mTuples = ((Monster)mainBossArray.get(x)).getmTuples();

//	for(int y = 0;y<mTuples.size();y=y+1){

//	if(((MonsterTuple)mTuples.get(y)).getFinalTCs().containsKey(key)){
//	monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalTCs().get(key)).doubleValue() * d * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)),1) ));
//	}
//	}
//	}
//	break;
//	}


//	return monsterTCList;
//	}

	private void populateItemArrays() {


		for(int x = 0;x<D2TxtFile.WEAPONS.getRowSize();x=x+1){
			if(D2TxtFile.WEAPONS.getRow(x).get("spawnable").equals("1")){
				regItemArray.add(new WhiteItem(D2TxtFile.WEAPONS.getRow(x)));
			}
		}

		for(int x = 0;x<D2TxtFile.ARMOR.getRowSize();x=x+1){
			if(D2TxtFile.ARMOR.getRow(x).get("spawnable").equals("1")){
				regItemArray.add(new WhiteItem(D2TxtFile.ARMOR.getRow(x)));
			}
		}

		for(int x = 0;x<D2TxtFile.UNIQUES.getRowSize();x=x+1){
			if(!D2TxtFile.UNIQUES.getRow(x).get("lvl").equals("0") && D2TxtFile.UNIQUES.getRow(x).get("enabled").equals("1")){
//				if(!D2TxtFile.UNIQUES.getRow(x).get("lvl").equals("0") && D2TxtFile.UNIQUES.getRow(x).get("enabled").equals("1") && !D2TxtFile.UNIQUES.getRow(x).get("code").startsWith("cm")&&!D2TxtFile.UNIQUES.getRow(x).get("code").equals("rin")&&!D2TxtFile.UNIQUES.getRow(x).get("code").equals("jew")&& !D2TxtFile.UNIQUES.getRow(x).get("code").equals("amu")){
				uniqItemArray.add(new UniqItem(D2TxtFile.UNIQUES.getRow(x)));
			}
		}

		for(int x = 0;x<D2TxtFile.SETITEMS.getRowSize();x=x+1){
//			if(!D2TxtFile.SETITEMS.getRow(x).get("item").equals("amu")){
			setItemArray.add(new SetItem(D2TxtFile.SETITEMS.getRow(x)));
//			}
		}

		for(int x = 0;x<D2TxtFile.MISC.getRowSize();x++){
			if(D2TxtFile.MISC.getRow(x).get("code").startsWith("cm") ||D2TxtFile.MISC.getRow(x).get("type").equals("jewl") ||D2TxtFile.MISC.getRow(x).get("type").equals("rune") || D2TxtFile.MISC.getRow(x).get("code").equals("amu") || D2TxtFile.MISC.getRow(x).get("code").equals("rin") || !D2TxtFile.MISC.getRow(x).get("type2").equals("")){
				miscItemArray.add(new MiscItem(D2TxtFile.MISC.getRow(x)));
			}
		}

		sortItemArrays();

	}

	private void sortItemArrays() {


		Collections.sort(regItemArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Item lItem1 = (Item) pObj1;
				Item lItem2 = (Item) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

		Collections.sort(magItemArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Item lItem1 = (Item) pObj1;
				Item lItem2 = (Item) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

		Collections.sort(rareItemArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Item lItem1 = (Item) pObj1;
				Item lItem2 = (Item) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

		Collections.sort(setItemArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Item lItem1 = (Item) pObj1;
				Item lItem2 = (Item) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

		Collections.sort(uniqItemArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Item lItem1 = (Item) pObj1;
				Item lItem2 = (Item) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});
		Collections.sort(miscItemArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Item lItem1 = (Item) pObj1;
				Item lItem2 = (Item) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});


	}

	public void populateArrays(){

		ArrayList SuBossCross = new ArrayList();
		ArrayList uniqueCheck = new ArrayList();

		SuBossCross.add("Radament");
		SuBossCross.add("The Summoner");
		SuBossCross.add("Griswold");
		SuBossCross.add("Nihlathak");

		for(int x = 0; x < D2TxtFile.MONSTATS.getRowSize();x=x+1){



			if(uniqueCheck.indexOf(D2TxtFile.MONSTATS.getRow(x).get("Id"))!=-1){
				continue;
			}
			uniqueCheck.add(D2TxtFile.MONSTATS.getRow(x).get("Id"));

			if(!D2TxtFile.MONSTATS.getRow(x).get("boss").equals("1") && D2TxtFile.MONSTATS.getRow(x).get("enabled").equals("1")&& D2TxtFile.MONSTATS.getRow(x).get("killable").equals("1") && !D2TxtFile.MONSTATS.getRow(x).get("TreasureClass1").equals("")&& !D2TxtFile.MONSTATS.getRow(x).get("TreasureClass1(N)").equals("")){
				mainRegMonArray.add(new Regular(D2TxtFile.MONSTATS.getRow(x), "N", 0,0));
				mainRegMonArray.add(new Regular(D2TxtFile.MONSTATS.getRow(x), "NM", 0,0));
				mainRegMonArray.add(new Regular(D2TxtFile.MONSTATS.getRow(x), "H", 0,0));

				mainChampMonArray.add(new Champion(D2TxtFile.MONSTATS.getRow(x), "N", 2,0));
				mainChampMonArray.add(new Champion(D2TxtFile.MONSTATS.getRow(x), "NM", 2,0));
				mainChampMonArray.add(new Champion(D2TxtFile.MONSTATS.getRow(x), "H", 2,0));


				if(!D2TxtFile.MONSTATS.getRow(x).get("MaxGrp").equals("0")){
					mainMinMonArray.add(new Minion(D2TxtFile.MONSTATS.getRow(x), "N", 1,0));					
					mainMinMonArray.add(new Minion(D2TxtFile.MONSTATS.getRow(x), "NM", 1,0));					
					mainMinMonArray.add(new Minion(D2TxtFile.MONSTATS.getRow(x), "H", 1,0));

					if(!D2TxtFile.MONSTATS.getRow(x).get("minion2").equals("")){
						mainMinMonArray.add(new Minion(D2TxtFile.MONSTATS.getRow(x), "N", 1,1));					
						mainMinMonArray.add(new Minion(D2TxtFile.MONSTATS.getRow(x), "NM", 1,1));					
						mainMinMonArray.add(new Minion(D2TxtFile.MONSTATS.getRow(x), "H", 1,1));
					}

				}


				mainUniqArray.add(new Unique(D2TxtFile.MONSTATS.getRow(x), "N", 3,0));
				mainUniqArray.add(new Unique(D2TxtFile.MONSTATS.getRow(x), "NM", 3,0));
				mainUniqArray.add(new Unique(D2TxtFile.MONSTATS.getRow(x), "H", 3,0));			



			} else if(D2TxtFile.MONSTATS.getRow(x).get("boss").equals("1") && D2TxtFile.MONSTATS.getRow(x).get("enabled").equals("1")&& D2TxtFile.MONSTATS.getRow(x).get("killable").equals("1") && !D2TxtFile.MONSTATS.getRow(x).get("TreasureClass1").equals("")&& !D2TxtFile.MONSTATS.getRow(x).get("TreasureClass1(N)").equals("")){
				mainBossArray.add(new Boss(D2TxtFile.MONSTATS.getRow(x), "N", 5,0));
				mainBossArray.add(new Boss(D2TxtFile.MONSTATS.getRow(x), "NM", 5,0));
				mainBossArray.add(new Boss(D2TxtFile.MONSTATS.getRow(x), "H", 5,0));
				if(!D2TxtFile.MONSTATS.getRow(x).get("TreasureClass4").equals("")){
					mainBossArray.add(new Boss(D2TxtFile.MONSTATS.getRow(x), "N", 5,1));
					mainBossArray.add(new Boss(D2TxtFile.MONSTATS.getRow(x), "NM", 5,1));
					mainBossArray.add(new Boss(D2TxtFile.MONSTATS.getRow(x), "H", 5,1));
				}
			}
		}

		for(int x = 0 ;x<D2TxtFile.SUPUNIQ.getRowSize();x=x+1){

			if(!D2TxtFile.SUPUNIQ.getRow(x).get("Class").equals("") && SuBossCross.indexOf(D2TxtFile.SUPUNIQ.getRow(x).get("Name"))==-1){
				mainSupUniqArray.add(new SuperUnique(D2TxtFile.SUPUNIQ.getRow(x), "N", 4,0));
				mainSupUniqArray.add(new SuperUnique(D2TxtFile.SUPUNIQ.getRow(x), "NM", 4,0));
				mainSupUniqArray.add(new SuperUnique(D2TxtFile.SUPUNIQ.getRow(x), "H", 4,0));
				if(!D2TxtFile.SUPUNIQ.getRow(x).get("MaxGrp").equals("0")){

					if(D2TxtFile.SUPUNIQ.getRow(x).get("Name") != null){
						mainMinMonArray.add(new Minion(D2TxtFile.SUPUNIQ.getRow(x), "N", 1,2));
						mainMinMonArray.add(new Minion(D2TxtFile.SUPUNIQ.getRow(x), "NM", 1,2));
						mainMinMonArray.add(new Minion(D2TxtFile.SUPUNIQ.getRow(x), "H", 1,2));

					}
				}
			}else if(!D2TxtFile.SUPUNIQ.getRow(x).get("Class").equals("") && SuBossCross.indexOf(D2TxtFile.SUPUNIQ.getRow(x).get("Name"))!=-1){
				if(!D2TxtFile.SUPUNIQ.getRow(x).get("MaxGrp").equals("0")){

					if(D2TxtFile.SUPUNIQ.getRow(x).get("Name") != null){
						mainMinMonArray.add(new Minion(D2TxtFile.SUPUNIQ.getRow(x), "N", 1,2));
						mainMinMonArray.add(new Minion(D2TxtFile.SUPUNIQ.getRow(x), "NM", 1,2));
						mainMinMonArray.add(new Minion(D2TxtFile.SUPUNIQ.getRow(x), "H", 1,2));

					}
				}
			}
		}
		uniqueCheck.clear();
		sortArrays();
	}

	private void sortArrays() {

		Collections.sort(mainRegMonArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Monster lItem1 = (Monster) pObj1;
				Monster lItem2 = (Monster) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

		Collections.sort(mainMinMonArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Monster lItem1 = (Monster) pObj1;
				Monster lItem2 = (Monster) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

		Collections.sort(mainChampMonArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Monster lItem1 = (Monster) pObj1;
				Monster lItem2 = (Monster) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

		Collections.sort(mainUniqArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Monster lItem1 = (Monster) pObj1;
				Monster lItem2 = (Monster) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

		Collections.sort(mainSupUniqArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Monster lItem1 = (Monster) pObj1;
				Monster lItem2 = (Monster) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

		Collections.sort(mainBossArray, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				Monster lItem1 = (Monster) pObj1;
				Monster lItem2 = (Monster) pObj2;

				return lItem1.getRealName().compareTo(lItem2.getRealName());


			}
		});

	}

//	public double getQualityUniq(Item item, int ilvl, int MF, MonsterTuple mon) {

////	All items have rarity 3 except class-specific items have rarity 1
////	, assassin claws have rarity 2, and wands, staves and sceptres (rods) have rarity 1.


////	switch()
//	D2TxtFileItemProperties ratioRow = null;
//	if(item.isClassSpec()){
//	ratioRow = D2TxtFile.ITEMRATIO.getRow(4);
//	}else{
//	ratioRow = D2TxtFile.ITEMRATIO.getRow(2);
//	}

//	double dChance = (Integer.parseInt(ratioRow.get("Unique")) - (((double)(ilvl - item.getBaseqLvl()))/((double)Integer.parseInt(ratioRow.get("UniqueDivisor"))))) * 128;
//	double eMF = ((double)(MF * 250))/((double)(MF + 250));

//	dChance = ((double)(dChance * 100))/((double)(100 + eMF));

//	if(dChance < Double.parseDouble(ratioRow.get("UniqueMin"))){
//	dChance = Double.parseDouble(ratioRow.get("UniqueMin"));
//	}

//	dChance = dChance - (dChance * (((double)mon.getUqual())/((double)1024)));
////	System.out.println(((double)128)/dChance);
//	return ((double)128)/dChance;
////	(ilvl - item.getqLvl())/

////	1) Find proper line in itemratio.txt.
////	2) Chance = (BaseChance - ((ilvl-qlvl)/Divisor)) * 128
////	3) if (we check for unique, set or rare quality) EffectiveMF=MF*Factor/(MF+Factor)
////	else EffectiveMF=MF
////	4) Chance= Chance* 100/(100+ EffectiveMF).
////	5) if Chance< MinChance chance == minchance
////	6) FinalChance=Chance-(Chance*QualityFactor/1024)
////	7) If (RND[ FinalChance ])<128 return Success
////	else return Fail

//	}


//	public double getQualityUniq(Item item, int ilvl, int MF, MonsterTuple mon) {

////	All items have rarity 3 except class-specific items have rarity 1
////	, assassin claws have rarity 2, and wands, staves and sceptres (rods) have rarity 1.
////	if(mon.getParent().getName().indexOf("andariel") && mon.getParent().getMonDiff().equals("NM")){
////	if(!((Boss)mon.getParent()).getQuest()){

////	System.out.println(mon.getParent().getName() + "  -  "+ "  -  "+((Boss)mon.getParent()).getQuest());
////	}
////	}

////	switch()
//	D2TxtFileItemProperties ratioRow = null;
//	if(item.isClassSpec()){
//	ratioRow = D2TxtFile.ITEMRATIO.getRow(4);
//	}else{
//	ratioRow = D2TxtFile.ITEMRATIO.getRow(2);
//	}

//	int dChance = (Integer.parseInt(ratioRow.get("Unique")) - (((ilvl - item.getBaseqLvl()))/(Integer.parseInt(ratioRow.get("UniqueDivisor"))))) * 128;
//	int eMF = ((MF * 250))/((MF + 250));

//	dChance = ((dChance * 100))/((100 + eMF));

//	if(dChance < Integer.parseInt(ratioRow.get("UniqueMin"))){
//	dChance = Integer.parseInt(ratioRow.get("UniqueMin"));
//	}

//	dChance = dChance - (dChance * mon.getUqual()/1024);
////	if(mon.getParent().getName().indexOf("andariel") && mon.getParent().getMonDiff().equals("NM")){
////	if(!((Boss)mon.getParent()).getQuest()){

////	System.out.println(mon.getParent().getName() + "  -  "+((double)128)/dChance+ "  -  "+((Boss)mon.getParent()).getQuest());
////	}
////	}
//	return ((double)128)/(double)dChance;
////	(ilvl - item.getqLvl())/

////	1) Find proper line in itemratio.txt.
////	2) Chance = (BaseChance - ((ilvl-qlvl)/Divisor)) * 128
////	3) if (we check for unique, set or rare quality) EffectiveMF=MF*Factor/(MF+Factor)
////	else EffectiveMF=MF
////	4) Chance= Chance* 100/(100+ EffectiveMF).
////	5) if Chance< MinChance chance == minchance
////	6) FinalChance=Chance-(Chance*QualityFactor/1024)
////	7) If (RND[ FinalChance ])<128 return Success
////	else return Fail

//	}


	public double generateRarityList(MonsterTuple monT, Item item){


		ArrayList list = new ArrayList();
		switch(item.getiNUS()){
		case 1:
			list = D2TxtFile.UNIQUES.searchColumnsMultipleHits("code", item.getItemCode());

			break;
		case 2:
			list = D2TxtFile.SETITEMS.searchColumnsMultipleHits("item", item.getItemCode());
			break;

		case 0:
			return 1;
		}

		double count = 0;
		for(int x = 0;x<list.size();x++){
			if(Integer.parseInt(((D2TxtFileItemProperties)list.get(x)).get("lvl")) <= monT.getLevel()){
				count = count + Integer.parseInt(((D2TxtFileItemProperties)list.get(x)).get("rarity"));
			}
		}

		return(Double.parseDouble(item.getItemRow().get("rarity"))/count);


	}

	public double getQuality(Item item, int ilvl, int MF, MonsterTuple mon, int recursions, boolean end) {


		String qual = "";
		int mfDiminishReturn = 0;

		switch(recursions){

		case -2:
			return 1;

		case -1:
			return 1;

		case 0:
			qual = "Unique";
			mfDiminishReturn = 250;
			break;

		case 1:
			qual = "Set";
			mfDiminishReturn = 500;
			break;

		case 2:
			qual = "Rare";
			mfDiminishReturn = 600;
			break;
		case 3:
			qual = "Magic";
			break;
		}

		double outChance;
		D2TxtFileItemProperties ratioRow = null;
		if(item.isClassSpec()){
			ratioRow = D2TxtFile.ITEMRATIO.getRow(4);
		}else{
			ratioRow = D2TxtFile.ITEMRATIO.getRow(2);
		}

		int dChance = (Integer.parseInt(ratioRow.get(qual)) - (((ilvl - item.getBaseqLvl()))/(Integer.parseInt(ratioRow.get(qual+"Divisor"))))) * 128;
		int eMF = 0;

		if(recursions ==3 || MF < 11){
			eMF = MF;
		}else{
			eMF = (((MF * mfDiminishReturn))/((MF + mfDiminishReturn)));
		}

		dChance = ((dChance * 100))/((100 + eMF));

		if(dChance < Integer.parseInt(ratioRow.get(qual+ "Min"))){
			dChance = Integer.parseInt(ratioRow.get(qual+"Min"));
		}

		dChance = dChance - (dChance * mon.getUqual()/1024);

		outChance = (double)128/(double)dChance;

		if(!end){
			return outChance;
		}

		for(int x = recursions -1;x>-1;x--){
			outChance = outChance * (1-getQuality(item, ilvl, MF, mon, x, false));

		}

//		System.out.println(qual + " - " + outChance);

		return outChance;

//		return ((double)128)/(double)dChance;

	}

//	public double getQualitySet(Item item, int ilvl, int MF, MonsterTuple mon) {

////	All items have rarity 3 except class-specific items have rarity 1
////	, assassin claws have rarity 2, and wands, staves and sceptres (rods) have rarity 1.


////	switch()
//	D2TxtFileItemProperties ratioRow = null;
//	if(item.isClassSpec()){
//	ratioRow = D2TxtFile.ITEMRATIO.getRow(4);
//	}else{
//	ratioRow = D2TxtFile.ITEMRATIO.getRow(2);
//	}

//	double dChance = (Integer.parseInt(ratioRow.get("Set")) - (((double)(ilvl - item.getBaseqLvl()))/((double)Integer.parseInt(ratioRow.get("SetDivisor"))))) * 128;
//	double eMF = ((double)(MF * 500))/((double)(MF + 500));

//	dChance = ((double)(dChance * 100))/((double)(100 + eMF));

//	if(dChance < Double.parseDouble(ratioRow.get("SetMin"))){
//	dChance = Double.parseDouble(ratioRow.get("SetMin"));
//	}

//	dChance = dChance - (dChance * (((double)mon.getSqual())/((double)1024)));
//	return ((double)128)/dChance;


//	}

	public ArrayList getRegItemArray() {
		return regItemArray;
	}

	public ArrayList getSetItemArray() {
		return setItemArray;
	}

	public ArrayList getUniqItemArray() {
		return uniqItemArray;
	}

	public ArrayList getMainRegMonArray() {
		return mainRegMonArray;
	}

	public ArrayList getMainMinMonArray() {
		return mainMinMonArray;
	}

	public ArrayList getMainChampMonArray() {
		return mainChampMonArray;
	}

	public ArrayList getMainUniqArray() {
		return mainUniqArray;
	}

	public ArrayList getMainSupUniqArray() {
		return mainSupUniqArray;
	}

	public ArrayList getMainBossArray() {
		return mainBossArray;
	}


	public HashMap findMonstersTCTrueMisc(int monSelection, Item item, int MF, int nPlayers, int nGroup, int QRecursions, boolean sevP) {

		HashMap monsterTCList = new HashMap();

		switch (monSelection){

		case 0:
			for(int x = 0;x< mainRegMonArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainRegMonArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnTrueMiscTCS(nPlayers, nGroup,0,item,this,MF, sevP);

					if(((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().containsKey(item.getItemCode())){
						monsterTCList.put(mTuples.get(y),(Double)((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().get(item.getItemCode()));
					}
				}
			}
			break;
		case 1:
			for(int x = 0;x< mainMinMonArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainMinMonArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnTrueMiscTCS(nPlayers, nGroup,0,item,this,MF, sevP);

					if(((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().containsKey(item.getItemCode())){
						monsterTCList.put(mTuples.get(y), ((Double)((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().get(item.getItemCode())));
					}
				}
			}
			break;
		case 2:
			for(int x = 0;x< mainChampMonArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainChampMonArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnTrueMiscTCS(nPlayers, nGroup,0,item,this,MF, sevP);

					if(((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().containsKey(item.getItemCode())){
						monsterTCList.put(mTuples.get(y), ((Double)((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().get(item.getItemCode())));
					}
				}
			}
			break;
		case 3:
			for(int x = 0;x< mainUniqArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainUniqArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnTrueMiscTCS(nPlayers, nGroup,0,item,this,MF, sevP);

					if(((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().containsKey(item.getItemCode())){
						monsterTCList.put(mTuples.get(y), ((Double)((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().get(item.getItemCode())));
					}
				}
			}
			break;
		case 4:
			for(int x = 0;x< mainSupUniqArray.size();x=x+1){
				if(((Monster)mainSupUniqArray.get(x)).getMonName().equals("Fangskin") || ((SuperUnique)mainSupUniqArray.get(x)).getSUID().startsWith("ancientbarb")){
					continue;
				}
				ArrayList mTuples = ((Monster)mainSupUniqArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnTrueMiscTCS(nPlayers, nGroup,0,item,this,MF, sevP);
//					System.out.println(((MonsterTuple)mTuples.get(y)).getParent().getRealName());
					if(((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().containsKey(item.getItemCode())){
						monsterTCList.put(mTuples.get(y), ((Double)((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().get(item.getItemCode())));
					}
				}
			}
			break;
		case 5:
			for(int x = 0;x< mainBossArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainBossArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnTrueMiscTCS(nPlayers, nGroup,0,item,this,MF, sevP);

					if(((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().containsKey(item.getItemCode())){
						monsterTCList.put(mTuples.get(y), ((Double)((MonsterTuple)mTuples.get(y)).getFinalTrueMiscTCs().get(item.getItemCode())));
					}
				}
			}
			break;
		}
		return monsterTCList;
	}

	public HashMap findMonstersTCMisc(int monSelection, Item item, int MF, int nPlayers, int nGroup, int QRecursions, boolean sevP) {

		HashMap monsterTCList = new HashMap();

		switch (monSelection){

		case 0:
			for(int x = 0;x< mainRegMonArray.size();x=x+1){

				ArrayList mTuples = ((Monster)mainRegMonArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){

					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}



					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnMiscTCS(nPlayers, nGroup,0,item,this,MF,QRecursions, sevP);

					if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey(item.getItemCode())){
						monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().get(item.getItemCode())).doubleValue() * generateRarityList((MonsterTuple)mTuples.get(y), item) * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true)));
					}
				}
			}
			break;
		case 1:
			for(int x = 0;x< mainMinMonArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainMinMonArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					if(((MonsterTuple)mTuples.get(y)).getInitTC().indexOf("Council")!=-1){
						((MonsterTuple)mTuples.get(y)).lookupBASETCReturnMiscTCS(nPlayers, nGroup,generateRarityList((MonsterTuple)mTuples.get(y), item),item,this,MF,QRecursions, sevP);

					}else{
						((MonsterTuple)mTuples.get(y)).lookupBASETCReturnMiscTCS(nPlayers, nGroup,0,item,this,MF,QRecursions, sevP);

					}
//					if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("rin") || ((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("amu")|| ((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("jew") ){
					if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey(item.getItemCode())){
						if(((MonsterTuple)mTuples.get(y)).getInitTC().indexOf("Council") == -1){
							monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().get(item.getItemCode())).doubleValue() * generateRarityList((MonsterTuple)mTuples.get(y), item) * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true)));
						}else{
							monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().get(item.getItemCode())).doubleValue()));

						}
					}
//					}
				}
			}
			break;
		case 2:
			for(int x = 0;x< mainChampMonArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainChampMonArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnMiscTCS(nPlayers, nGroup,0,item,this,MF,QRecursions, sevP);
					if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey(item.getItemCode())){
//						if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("rin") || ((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("amu")|| ((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("jew") ){
						monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().get(item.getItemCode())).doubleValue() * generateRarityList((MonsterTuple)mTuples.get(y), item) * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true)));
//						}
					}
				}
			}
			break;
		case 3:
			for(int x = 0;x< mainUniqArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainUniqArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnMiscTCS(nPlayers, nGroup,0,item,this,MF,QRecursions, sevP);
					if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey(item.getItemCode())){
//						if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("rin") || ((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("amu")|| ((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("jew") ){
						monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().get(item.getItemCode())).doubleValue() * generateRarityList((MonsterTuple)mTuples.get(y), item) * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions,true)));
//						}
					}
				}
			}
			break;
		case 4:
			for(int x = 0;x< mainSupUniqArray.size();x=x+1){
				if(((Monster)mainSupUniqArray.get(x)).getMonName().equals("Fangskin") || ((SuperUnique)mainSupUniqArray.get(x)).getSUID().startsWith("ancientbarb")){
					continue;
				}
				ArrayList mTuples = ((Monster)mainSupUniqArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}

//					System.out.println("RARITY: " + generateRarityList((MonsterTuple)mTuples.get(y), item) + " , QUALITY: " + getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions) + " , TOTAL: " + (generateRarityList((MonsterTuple)mTuples.get(y), item) * getQuality(item, ((MonsterTuple)mTuples.get(y)).getLevel(), MF,((MonsterTuple)mTuples.get(y)), QRecursions)));
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnMiscTCS(nPlayers, nGroup,generateRarityList((MonsterTuple)mTuples.get(y), item) ,item,this,MF,QRecursions, sevP);

//					if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("rin") || ((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("amu")|| ((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey("jew") ){
					if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey(item.getItemCode())){
						monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().get(item.getItemCode())).doubleValue()));
//						}
					}
				}
			}
			break;
		case 5:
			for(int x = 0;x< mainBossArray.size();x=x+1){
				ArrayList mTuples = ((Monster)mainBossArray.get(x)).getmTuples();
				for(int y = 0;y<mTuples.size();y=y+1){
					if(((MonsterTuple)mTuples.get(y)).getLevel() < item.getqLvl()){
						continue;
					}
					
					((MonsterTuple)mTuples.get(y)).lookupBASETCReturnMiscTCS(nPlayers, nGroup,generateRarityList((MonsterTuple)mTuples.get(y), item),item,this,MF,QRecursions, sevP);
					if(((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().containsKey(item.getItemCode())){
						monsterTCList.put(mTuples.get(y), new Double(((Double)((MonsterTuple)mTuples.get(y)).getFinalMiscTCs().get(item.getItemCode())).doubleValue()));
					}
				}
			}
			break;
		}

		return monsterTCList;
	}

	public ArrayList getMiscItemArray() {
		return miscItemArray;
	}
}

