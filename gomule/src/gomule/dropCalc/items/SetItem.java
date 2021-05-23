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

import java.util.HashMap;

import randall.d2files.D2TxtFileItemProperties;

public class SetItem extends Item {

	public SetItem(D2TxtFileItemProperties ItemRow) {
		super(ItemRow);
		ItemCode = ItemRow.get("item");
		getType();
		ItemRarity = getRarity(ItemType);
		getTC();
		this.ItemName = ItemRow.get("index");
		this.ItemQLvl = Integer.parseInt(ItemRow.get("lvl"));
		this.iClassSpec = setClassSpec();
		iNUS = 2;
	}


	public HashMap getFinalProbSum(DCNew DC, int monSelection, int MF, int nPlayers, int nGroup, int QRecursions, boolean sevP){


		
		switch(ItemClass){
		case 0:

			mDrops = DC.findMonstersTCGeneral("weap" + this.ItemTC, (double)ItemRarity/(double)this.getTCProbSum(), monSelection, this, MF,  nPlayers,  nGroup,QRecursions, sevP);

			break;

		case 1:

			mDrops = DC.findMonstersTCGeneral("armo" + this.ItemTC,(double)ItemRarity/(double)this.getTCProbSum(), monSelection, this, MF, nPlayers, nGroup,QRecursions, sevP);

			break;
			
		case 2:



//			if(ItemCode.equals("rin") || ItemCode.equals("amu") ){
				mDrops = DC.findMonstersTCMisc(monSelection, this, MF, nPlayers, nGroup,QRecursions, sevP);
//			}
			break;
		}

		return mDrops;

	}

}
