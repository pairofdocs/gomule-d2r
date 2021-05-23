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
package gomule.dropCalc.items;

import gomule.dropCalc.DCNew;
import gomule.dropCalc.monsters.MonsterTuple;

import java.util.HashMap;

import randall.d2files.D2TblFile;
import randall.d2files.D2TxtFileItemProperties;

public class WhiteItem extends Item {

	public WhiteItem(D2TxtFileItemProperties ItemRow) {
		super(ItemRow);
		ItemCode = ItemRow.get("code");
		getType();
		ItemRarity = getRarity(ItemType);
		getTC();
		this.ItemName = ItemRow.get("name");
		this.ItemQLvl = Integer.parseInt(ItemRow.get("level"));
		this.iNUS = 0;

	}


	public String getRealName() {

		return D2TblFile.getString(ItemCode);

	}

	public void getProbDrop(MonsterTuple MT){
		switch(ItemClass){
		case 0:

			System.out.println(MT.getFinalTCs().get("weap" + this.ItemTC));
			
			break;

		case 1:

			System.out.println(MT.getFinalTCs().get("armo" + this.ItemTC));
			
			break;
		}
		

	}
	
	public HashMap getFinalProbSum(DCNew DC, int monSelection, int MF, int nPlayers, int nGroup, int QRecursions, boolean sevP){
		


		switch(ItemClass){
		case 0:

			mDrops = DC.findMonstersTCGeneral("weap" + this.ItemTC, (double)ItemRarity/(double)this.getTCProbSum(), monSelection,this ,MF,nPlayers,nGroup, QRecursions, sevP);
			
			break;

		case 1:

			mDrops = DC.findMonstersTCGeneral("armo" + this.ItemTC, (double)ItemRarity/(double)this.getTCProbSum(), monSelection,this ,MF,nPlayers,nGroup, QRecursions, sevP);
					
			break;
		}
		return mDrops;
		
	}
	
	public HashMap getTuplesDrop(){
		return mDrops;
	}


	public String getItemTC() {
		switch(ItemClass){
		case 0:

			return "weap" + this.ItemTC;

		case 1:

			return "armo" + this.ItemTC;

		}
		return null;
	}
	


}
