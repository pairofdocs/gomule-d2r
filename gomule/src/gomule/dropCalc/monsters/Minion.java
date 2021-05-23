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

import randall.d2files.D2TblFile;
import randall.d2files.D2TxtFile;
import randall.d2files.D2TxtFileItemProperties;

public class Minion extends Monster{


	private String minionBoss;

	public Minion(D2TxtFileItemProperties monRow, String monDiff, int monClass, int flag) {
		super( monRow,  monDiff, monClass,  flag);

		
		if(D2TxtFile.SUPUNIQ.searchColumns("Name", monID) != null){

			if(D2TxtFile.MONSTATS.searchColumns("Id",D2TxtFile.SUPUNIQ.searchColumns("Name", monID).get("Class")).get("boss").equals("1")){
				this.minionBoss = monID;
				setUpBossMinionTuples(D2TxtFile.MONSTATS.searchColumns("Id",D2TxtFile.SUPUNIQ.searchColumns("Name", monID).get("Class")).get("minion1"));
				this.monName =D2TblFile.getString(D2TxtFile.MONSTATS.searchColumns("Id", this.monID).get("NameStr"));
			}else{
				this.minionBoss = monID;
				setUpMinionTuples(D2TxtFile.SUPUNIQ.searchColumns("Name", monID).get("Class"));
//				this.monID = 	
				this.monName =D2TblFile.getString(D2TxtFile.MONSTATS.searchColumns("Id", this.monID).get("NameStr"));
//				setUpTuples();
			}
		}else{
			if(D2TxtFile.MONSTATS.searchColumns("Id", monID).get("minion1").equals("")){
				this.minionBoss = monID;
				this.monName =D2TblFile.getString(D2TxtFile.MONSTATS.searchColumns("Id", this.monID).get("NameStr"));
				setUpTuples();
			}else{
				if(flag == 1){
					this.minionBoss = monID;
					flag = 0;

					setUpMinionTuples(D2TxtFile.MONSTATS.searchColumns("Id", monID).get("minion2"));
					this.monName =D2TblFile.getString(D2TxtFile.MONSTATS.searchColumns("Id", this.monID).get("NameStr"));
				}else{
					this.minionBoss = monID;

					setUpMinionTuples(D2TxtFile.MONSTATS.searchColumns("Id", monID).get("minion1"));
					this.monName =D2TblFile.getString(D2TxtFile.MONSTATS.searchColumns("Id", this.monID).get("NameStr"));
				}
			}

		}

	}

	private void setUpTuples() {
		mTuples = new ArrayList();
		HashMap areas = findLocsMonster(0);
		enterMonLevel(areas);
		ArrayList initTCs = getInitTC(areas,"TreasureClass1");
		mTuples = createTuples(areas, initTCs);

	}

	private void setUpBossMinionTuples(String newID) {
		mTuples = new ArrayList();
		this.monID = this.monID.toLowerCase();
		HashMap areas = findLocsBossMonster();
		findLevelsBossMonster(areas);
		this.monID = newID;
		ArrayList initTCs = getInitTC(areas,"TreasureClass1");
		mTuples = createTuples(areas, initTCs);


	}

	private void setUpMinionTuples(String newID) {
		mTuples = new ArrayList();
		HashMap areas = new HashMap();
		if(D2TxtFile.SUPUNIQ.searchColumns("Name", minionBoss) != null){
			findLocsSU(1, areas, minionBoss);
		}else{
		areas = findLocsMonster(0);
		}
		this.monID = newID;
		enterMonLevel(areas);
		ArrayList initTCs = getInitTC(areas,"TreasureClass1");

		mTuples = createTuples(areas, initTCs);


	}

	public void enterMonLevel(HashMap monLvlAreas){

		Iterator it = monLvlAreas.keySet().iterator();
		while(it.hasNext()){

			String area = (String)it.next();

			if(monDiff.equals("N")){

				monLvlAreas.put(area, new Integer(Integer.parseInt(D2TxtFile.MONSTATS.searchColumns("Id", monID).get("Level"))+3));

			}else if (monDiff.equals("NM")){

				monLvlAreas.put(area, new Integer(Integer.parseInt(D2TxtFile.LEVELS.searchColumns("Name", area).get("MonLvl2Ex"))+3)) ;

			}else{

				monLvlAreas.put(area, new Integer(Integer.parseInt(D2TxtFile.LEVELS.searchColumns("Name", area).get("MonLvl3Ex"))+3));
			}

		}
	}


	public String getRealName(){
		return this.monName + " (" + this.getRealBossName() + ") " + this.monID ;
	}

	public String getBoss() {
		return minionBoss;
	}

	public String getRealBossName(){
		if(D2TxtFile.SUPUNIQ.searchColumns("Name", minionBoss) == null){
			return D2TblFile.getString(D2TxtFile.MONSTATS.searchColumns("Id", this.minionBoss).get("NameStr"));
		}
		return D2TblFile.getString( getBoss());
	}

}
