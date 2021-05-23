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

public class Regular extends Monster{

	public Regular(D2TxtFileItemProperties monRow, String monDiff, int monClass, int flag) {
		super( monRow,  monDiff, monClass,  flag);
		this.monName =D2TblFile.getString(monRow.get("NameStr"));

		mTuples = new ArrayList();
		HashMap areas = findLocsMonster(0);
		enterMonLevel(areas);
		ArrayList initTCs = getInitTC(areas,"TreasureClass1");
		mTuples = createTuples(areas, initTCs);
	}
	
	public void enterMonLevel(HashMap monLvlAreas){

		Iterator it = monLvlAreas.keySet().iterator();
		while(it.hasNext()){

			String area = (String)it.next();

			if(monDiff.equals("N")){

					monLvlAreas.put(area, new Integer(monRow.get("Level")));
			}else if (monDiff.equals("NM")){
					monLvlAreas.put(area, new Integer(D2TxtFile.LEVELS.searchColumns("Name", area).get("MonLvl2Ex"))) ;

			}else{

					monLvlAreas.put(area, new Integer(D2TxtFile.LEVELS.searchColumns("Name", area).get("MonLvl3Ex")));
			}

		}
	}



}
