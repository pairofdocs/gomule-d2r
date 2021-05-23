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
import randall.d2files.D2TxtFile;
import randall.d2files.D2TxtFileItemProperties;

public class Item {

	HashMap mDrops = new HashMap();
	D2TxtFileItemProperties ItemRow;
	String ItemName;
	String ItemCode;
	String ItemType;
	int ItemRarity;
	int ItemTC;
	int ItemQLvl;
	int BaseQLvl;
	int TCProbSum;
	boolean iClassSpec;
	/**
	 * 0 = Normal
	 * 1 = Uniq
	 * 2 = Set
	 */
	int iNUS;
	
	/**
	 * 0 = Norm
	 * 1 = Excep
	 * 2 = Elite
	 */
	int ItemQual;
	
	/**
	 * 0 = Wep
	 * 1 = Arm
	 * 2 = Misc
	 */
	int ItemClass;

	public Item(D2TxtFileItemProperties ItemRow){

		this.ItemRow = ItemRow;


	}




	public Item() {
		
	}




	protected void getTC() {

//		if(BaseQLvl%3 == 0){
//		ItemTC = BaseQLvl;
//		}else{
//		ItemTC = BaseQLvl + (3 - (BaseQLvl%3));
//		}

		ItemTC = BaseQLvl + (3 - (BaseQLvl%3))%3;
	}

	public String getRealName() {

		return D2TblFile.getString(ItemName);

	}

	public HashMap getFinalProbSum(DCNew DC, int monSelection, int MF, int nPlayers, int nGroup, int QRecursions, boolean sevP){

		return null;

	}

	protected void getType() {

//		if(ItemQuality.equals("UNIQ") || )

		D2TxtFileItemProperties IPointer = D2TxtFile.WEAPONS.searchColumns("code", ItemCode);
		this.ItemClass = 0;
		if(IPointer == null){
			IPointer = D2TxtFile.ARMOR.searchColumns("code", ItemCode);
			this.ItemClass = 1;
			if(IPointer == null){
				IPointer = D2TxtFile.MISC.searchColumns("code", ItemCode);
				this.ItemClass = 2;
			}
		}
		ItemType = IPointer.get("type");
		BaseQLvl = Integer.parseInt(IPointer.get("level"));
		
		if(ItemClass != 2){
		if(IPointer.get("normcode").equals(ItemCode)){
			ItemQual = 0;
		}else if(IPointer.get("ubercode").equals(ItemCode)){
			ItemQual = 1;
		}else if(IPointer.get("ultracode").equals(ItemCode)){
			ItemQual = 2;
		}
		}else{
			ItemQual = 3;
		}
		
		
	}

	protected int getRarity(String ItemType) {
//		if(ItemClass == 2 && ItemCode.equals("rin")){
//			return Integer.parseInt(D2TxtFile.UNIQUES.searchColumns("code", ItemCode).get("rarity"));
//		}
		return Integer.parseInt(D2TxtFile.ITEM_TYPES.searchColumns("Code", ItemType).get("Rarity"));

	}

	public int getItemClass(){
		return this.ItemClass;
	}



	public int getTCProbSum(){

		if(this.TCProbSum != 0){
			return this.TCProbSum;
		}
		int TCProbSum = 0;
		switch(ItemClass){
		case 0:

			for(int x = 0;x<D2TxtFile.WEAPONS.getRowSize();x++){
				if(!D2TxtFile.WEAPONS.getRow(x).get("level").equals("") && !D2TxtFile.WEAPONS.getRow(x).get("level").equals("0") && Integer.parseInt(D2TxtFile.WEAPONS.getRow(x).get("level")) + (3 - (Integer.parseInt(D2TxtFile.WEAPONS.getRow(x).get("level"))%3))%3 == ItemTC){

					TCProbSum = TCProbSum + getRarity(D2TxtFile.WEAPONS.getRow(x).get("type"));

				}
			}


			break;

		case 1:

			for(int x = 0;x<D2TxtFile.ARMOR.getRowSize();x++){
				if(!D2TxtFile.ARMOR.getRow(x).get("level").equals("") && !D2TxtFile.ARMOR.getRow(x).get("level").equals("0") && Integer.parseInt(D2TxtFile.ARMOR.getRow(x).get("level")) + (3 - (Integer.parseInt(D2TxtFile.ARMOR.getRow(x).get("level"))%3))%3 == ItemTC){

					TCProbSum = TCProbSum + getRarity(D2TxtFile.ARMOR.getRow(x).get("type"));

				}
			}

			break;
		}
		this.TCProbSum = TCProbSum;
		return TCProbSum;
	}

	public int getqLvl(){
		return ItemQLvl;
	}
	
	public int getBaseqLvl(){
		return BaseQLvl;
	}

	public boolean setClassSpec(){

		switch(ItemClass){
		case 0:


			if(ItemType.equals("abow") ||ItemType.equals("aspe") ||ItemType.equals("ajav") ||ItemType.equals("h2h") ||ItemType.equals("orb") ||ItemType.equals("h2h2") ){
				return true;
			}
			return false;
		case 1:


			if(ItemType.equals("phlm") ||ItemType.equals("pelt") ||ItemType.equals("ashd") ||ItemType.equals("head")){
				return true;
			}

			return false;
		}
		
		return false;

	}




	public boolean isClassSpec() {
		
		return iClassSpec;
	}




	public int getItemQual() {
		return ItemQual;
	}




	public int getiNUS() {
		return iNUS;
	}




	public String getItemCode() {
		return ItemCode;
		
	}




	public D2TxtFileItemProperties getItemRow() {
		// TODO Auto-generated method stub
		return ItemRow;
	}



}
