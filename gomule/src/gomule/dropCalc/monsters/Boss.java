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
import randall.d2files.D2TblFile;
import randall.d2files.D2TxtFileItemProperties;

public class Boss extends Monster {


	boolean isQuest = false;

	public Boss(D2TxtFileItemProperties monRow, String monDiff, int monClass, int flag) {
		super( monRow,  monDiff, monClass,  flag);
		if(flag==1){
			isQuest = true;
		}
		this.monName =D2TblFile.getString(monRow.get("NameStr"));
		mTuples = new ArrayList();
		HashMap areas = findLocsBossMonster();
		findLevelsBossMonster(areas);
		String header = "TreasureClass1";
		if(isQuest){
			header = "TreasureClass4";
		}
		ArrayList initTCs = getInitTC(areas, header);
		mTuples = createTuples(areas, initTCs);

	}
	

	public String getRealName(){
		if(isQuest){
		return this.monName  + " (Q) - " +  this.monID;
		}else{
			return this.monName  + " - " +  this.monID;
		}
	}


	public boolean getQuest() {
		// TODO Auto-generated method stub
		return isQuest;
	}
	



}
