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

import randall.d2files.D2TblFile;
import randall.d2files.D2TxtFileItemProperties;

public class MiscItem extends Item {

	public MiscItem(D2TxtFileItemProperties ItemRow) {

		super(ItemRow);

		ItemCode = ItemRow.get("code");
		getType();
		ItemRarity = getRarity(ItemType);
		getTC();
		this.ItemName = ItemRow.get("name");
		this.ItemQLvl = this.BaseQLvl;
		this.iClassSpec = setClassSpec();
		ItemClass = 3;

	}
	
	public HashMap getFinalProbSum(DCNew DC, int monSelection, int MF, int nPlayers, int nGroup, int QRecursions, boolean sevP){


		if(ItemCode.startsWith("cm") ||ItemCode.equals("jew") || ItemCode.equals("amu") || ItemCode.equals("rin")){
			mDrops = DC.findMonstersTCMisc(monSelection, this, MF, nPlayers, nGroup,QRecursions, sevP);
		}else{

				mDrops = DC.findMonstersTCTrueMisc(monSelection, this, MF, nPlayers, nGroup, QRecursions, sevP);
		}

		return mDrops;

	}
	
	public int getqLvl(){
		return 0;
	}
	
	
	public String getRealName() {

		return D2TblFile.getString(ItemCode);

	}

}
