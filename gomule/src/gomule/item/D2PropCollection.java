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

import gomule.util.D2BitReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import randall.d2files.D2TxtFile;
import randall.d2files.D2TxtFileItemProperties;

public class D2PropCollection extends ArrayList{


	/**
	 * 
	 */
	private static final long serialVersionUID = -6107521404310600035L;
	private boolean tidy;

	/**
	 * Clean up some of those useless properties
	 *
	 */
	public void deleteUselessProperties(){


		for(int x = 0;x<size();x++){

			//159 = Min throw damage
			//160 = Max throw damage
			//23 = 2h Min damage
			//24 = 2h Max damage
			//140 = Extra Blood
			//194 SOCKETS??
			if(((D2Prop)get(x)).getPNum() == 140 || ((D2Prop)get(x)).getPNum() == 194 || multipleMinMaxDmg((D2Prop)get(x))){
				remove(x);
				x--;
			}
		}
	}


	/**
	 * There are 6 types of +damage, min/max, secondary min/max and throw min/max. Generally all three are present on an item, but if there is only one present, then the others should be removed, leaving just base + damage on there.
	 * @param prop
	 * @return
	 */
	private boolean multipleMinMaxDmg(D2Prop prop) {

		if(prop.getPNum() == 159 || prop.getPNum() == 23 ){

			if(!containsProp(21)){
				prop.setPNum(21);
				return false;

			}else{
				return true;
			}

		}else if(prop.getPNum() == 160 || prop.getPNum() == 24 ){

			if(!containsProp(22)){
				prop.setPNum(22);
			}else{
				return true;
			}
		}
		return false;
	}

	private boolean containsProp(int propNum) {

		for(int x = 0;x<size();x++){
			if(((D2Prop)get(x)).getPNum() == propNum){
				return true;
			}
		}
		return false;
	}


	/**
	 * If 2 properties are present, combine them.
	 *
	 */
	private void combineProps(){

		for(int x = 0 ;x < size();x++){

			for(int y = 0;y<size();y++){

				if(get(x) == get(y))continue;

				if((((D2Prop)get(x)).getPNum() == ((D2Prop)get(y)).getPNum()) && (((D2Prop)get(x)).getQFlag() == ((D2Prop)get(y)).getQFlag())&& (((D2Prop)get(x)).getQFlag() == 0)){
					if(((D2Prop)get(x)).getPNum() == 107 || ((D2Prop)get(x)).getPNum() == 97 || ((D2Prop)get(x)).getPNum() == 188 || ((D2Prop)get(x)).getPNum() == 201 || ((D2Prop)get(x)).getPNum() == 198 || ((D2Prop)get(x)).getPNum() == 204 )continue;
					((D2Prop)get(x)).addPVals(((D2Prop)get(y)).getPVals());
					remove(y);
					y--;
				}
			}
		}
	}




	/**
	 * DeDupe properties including:
	 * Problem: L2 and L3 are guaranteed to be collected together. EG if we have 
	 * an emerald in a weapon with poison damage already present, it will still have
	 * (pMin, pMax, pLength) as a triple of consecutive values.
	 * 
	 * This is not the case for dedupe level 4. If we have an item with FireR, ColdR and 
	 * PoisR which is then socketed with an item with LightR, we will see a spread of 
	 * resistences. Therefore dedupe L4 needs to be different, comparing all possible 
	 * value, not just considering the next X values. 
	 * (DEDUPE LEVEL 2)
	 * Enhanced Damage
	 * Damage
	 * Fire Damage
	 * Lightning Damage
	 * Magic Damage
	 * 
	 * (DEDUPE LEVEL 3)
	 * Cold Damage
	 * Poison Damage
	 *
	 *(DEDUP LEVEL 4)
	 * Stats	
	 * Res
	 * Max Res
	 */

	private void deDupeL4(){

		/**
		 * 183 and 184 are used as this ensures non null values for the search on the txt files.
		 */
		//Fire 39
		//Light 41
		//Cold 43
		//Poison 45
		ArrayList resMap = new ArrayList();

		//Str 0
		//Ener 1 
		//Dex 2 
		//Vit 3 
		ArrayList statMap = new ArrayList();

		for(int x = 0;x<size();x++){

			if(D2TxtFile.ITEM_STAT_COST.searchColumns("ID",Integer.toString(((D2Prop)get(x)).getPNum())).get("dgrp").equals("") || ((D2Prop)get(x)).getQFlag() != 0)continue;
			if(((D2Prop)get(x)).getPNum() == 0 ||((D2Prop)get(x)).getPNum() == 1 ||((D2Prop)get(x)).getPNum() == 2 ||((D2Prop)get(x)).getPNum() == 3)statMap.add(get(x));
			if(((D2Prop)get(x)).getPNum() == 39 ||((D2Prop)get(x)).getPNum() == 41 ||((D2Prop)get(x)).getPNum() == 43 ||((D2Prop)get(x)).getPNum() == 45)resMap.add(get(x));

		}

		if(resMap.size() == 4){
			int vMin = 1000;

			for(int y = 0;y<resMap.size();y++){

				if(((D2Prop)resMap.get(y)).getPVals()[0] < vMin){
					vMin =((D2Prop)resMap.get(y)).getPVals()[0];
				}
			}
			add(new D2Prop(183, new int[]{vMin},((D2Prop)resMap.get(0)).getQFlag() ,true,37));
			threshDelete(resMap, vMin);
		}

		if(statMap.size() == 4){

			int vMin = 1000;

			for(int y = 0;y<statMap.size();y++){

				if(((D2Prop)statMap.get(y)).getPVals()[0] < vMin){
					vMin =((D2Prop)statMap.get(y)).getPVals()[0];
				}
			}
			add(new D2Prop(184, new int[]{vMin},((D2Prop)statMap.get(0)).getQFlag() ,true,38));
			threshDelete(statMap, vMin);

		}




	}



	private void threshDelete(ArrayList valMap, int vMin) {

		for(int x = 0;x<valMap.size();x++){
			if(((D2Prop)valMap.get(x)).getPVals()[0] == vMin){
				remove(valMap.get(x));
			}else{
				((D2Prop)valMap.get(x)).setPVals(new int[]{((D2Prop)valMap.get(x)).getPVals()[0] - vMin});
			}
		}


	}


	private void deDupeProps(){

		//DeDupe L2 and L3

		for(int x = 0 ;x < size();x++){

			if(x+1<size()){
				//Enhanced Damage %
				if(((D2Prop)get(x)).getPNum() == 17 && ((D2Prop)get(x+1)).getPNum() == 18){

					((D2Prop)get(x)).modifyVals(30, ((D2Prop)get(x)).getPVals());

					remove(x+1);

				}

				//Damage
				if(((D2Prop)get(x)).getPNum() == 21 && ((D2Prop)get(x+1)).getPNum() == 22){

					((D2Prop)get(x)).modifyVals(31, new int[]{((D2Prop)get(x)).getPVals()[0],((D2Prop)get(x+1)).getPVals()[0]});

					remove(x+1);

				}

				//Fire Damage
				if(((D2Prop)get(x)).getPNum() == 48 && ((D2Prop)get(x+1)).getPNum() == 49){

					((D2Prop)get(x)).modifyVals(32, new int[]{((D2Prop)get(x)).getPVals()[0],((D2Prop)get(x+1)).getPVals()[0]});

					remove(x+1);

				}

				//Lightning Damage
				if(((D2Prop)get(x)).getPNum() == 50 && ((D2Prop)get(x+1)).getPNum() == 51){

					((D2Prop)get(x)).modifyVals(33, new int[]{((D2Prop)get(x)).getPVals()[0],((D2Prop)get(x+1)).getPVals()[0]});

					remove(x+1);

				}

				//Magic Damage
				if(((D2Prop)get(x)).getPNum() == 52 && ((D2Prop)get(x+1)).getPNum() == 53){

					((D2Prop)get(x)).modifyVals(34, new int[]{((D2Prop)get(x)).getPVals()[0],((D2Prop)get(x+1)).getPVals()[0]});

					remove(x+1);

				}

				if(x+2<size()){
					//Cold Damage
					if(((D2Prop)get(x)).getPNum() == 54 && ((D2Prop)get(x+1)).getPNum() == 55 && ((D2Prop)get(x+2)).getPNum() == 56){

						((D2Prop)get(x)).modifyVals(35, new int[]{((D2Prop)get(x)).getPVals()[0],((D2Prop)get(x+1)).getPVals()[0],((D2Prop)get(x+2)).getPVals()[0]});

						remove(x+2);
						remove(x+1);

					}

					//Poison Damage
					if(((D2Prop)get(x)).getPNum() == 57 && ((D2Prop)get(x+1)).getPNum() == 58 && ((D2Prop)get(x+2)).getPNum() == 59){

						if(((D2Prop)get(x+2)).getPVals().length > 1 &&((D2Prop)get(x+2)).getPVals()[2] != 0){
							((D2Prop)get(x)).modifyVals(36, new int[]{((D2Prop)get(x)).getPVals()[0],((D2Prop)get(x+1)).getPVals()[0],((D2Prop)get(x+2)).getPVals()[0], ((D2Prop)get(x+2)).getPVals()[2]});

						}else{
							((D2Prop)get(x)).modifyVals(36, new int[]{((D2Prop)get(x)).getPVals()[0],((D2Prop)get(x+1)).getPVals()[0],((D2Prop)get(x+2)).getPVals()[0]});
						}

						remove(x+2);
						remove(x+1);

					}
				}
			}
		}
	}

	private ArrayList getPartialList(int qFlag) {

		ArrayList partialList = new ArrayList();
//		NEED TO ADD AS A NEW WITH STANDARD Q FLAG
		for(int x = 0;x<size();x++){
			if(((D2Prop)get(x)).getQFlag() == qFlag){
				//D2Prop constructor (d2Prop) sets QFlag to be 0
				partialList.add(new D2Prop((D2Prop)get(x)));
			}
		}

		return partialList;

	}

	private ArrayList getFullList() {
		return this;
	}

	public void tidy(){
		if(tidy)return;
		combineProps();
		deDupeProps();
		deDupeL4();
		sort();

		tidy = true;
	}


	public StringBuffer generateDisplay(int qFlag, int cLvl) {

		StringBuffer arrOut = new StringBuffer();
		if(qFlag < 7){
			arrOut.append("<font color=\"#4850b8\">");
		}else if(qFlag < 17){
			arrOut.append("<font color=\"#4850b8\">");
		}else if(qFlag < 36){
			arrOut.append("<font color=\"#ffdead\">");
		}else if(qFlag < 37){
			arrOut.append("<br><font color=\"#ffdead\">");
		}
		for(int x = 0;x<size();x++){
			String val = ((D2Prop)get(x)).generateDisplay(qFlag, cLvl);
			if(val != null && !val.equals("")){
				arrOut.append(val + "<br>&#10;");		
			}
		}



		return arrOut.append("</font>");
	}


	public void readProp(D2BitReader pFile, int rootProp, int qFlag) {

		D2TxtFileItemProperties pRow = D2TxtFile.ITEM_STAT_COST.getRow(rootProp);
		int readLength = Integer.parseInt(pRow.get("Save Bits"));
		int saveAdd = 0;
		if(!pRow.get("Save Add").equals("")){
			saveAdd = Integer.parseInt(pRow.get("Save Add"));
		}
		if (rootProp == 201 || rootProp == 197 || rootProp == 199
				|| rootProp == 195 || rootProp == 198 || rootProp == 196) {
			add(new D2Prop(rootProp, new int[] {(int)pFile.read(6)-saveAdd,(int)pFile.read(10)-saveAdd,(int)pFile.read(readLength) - saveAdd}, qFlag));
		} else if (rootProp == 204) {
			add(new D2Prop(rootProp, new int[] {(int)pFile.read(6)-saveAdd,(int)pFile.read(10)-saveAdd,(int)pFile.read(8)-saveAdd,(int)pFile.read(8)-saveAdd}, qFlag));
		} else if(!pRow.get("Save Param Bits").equals("")){
			add(new D2Prop(rootProp,new int[] {(int)pFile.read(Integer.parseInt(pRow.get("Save Param Bits"))) - saveAdd,(int)pFile.read(readLength) - saveAdd}, qFlag));
		} else {
			add(new D2Prop(rootProp,new int[] {(int)pFile.read(readLength) - saveAdd}, qFlag));
		}

	}

	public void addAll(D2PropCollection propCollection, int qFlag) {

		addAll(propCollection.getPartialList(qFlag));	
	}

	public void addAll(D2PropCollection propCollection) {
		addAll(propCollection.getFullList());

	}

	public void calcStats(int[] outStats, ArrayList plSkill, int cLvl, int op, int qFlagM){

		for(int x= 0;x<size();x++){

			((D2Prop)get(x)).addCharMods(outStats, plSkill, cLvl, op, qFlagM);
		}
	}


	public void sort(){

		Collections.sort(this, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				D2Prop p1 = (D2Prop) pObj1;
				D2Prop p2 = (D2Prop) pObj2;
				if(p2.getDescPriority() == p1.getDescPriority()){
					if(p2.getPNum() > p1.getPNum()){
						return 1;
					}
					return -1;
				}else if(p2.getDescPriority() > p1.getDescPriority()){
					return 1;
				}else{
					return -1;
				}
			}
		});
	}

	public void applyOp(int charLvl) {

		for(int x = 0;x<size();x++){
			((D2Prop)get(x)).applyOp(charLvl);
		}
	}
}
