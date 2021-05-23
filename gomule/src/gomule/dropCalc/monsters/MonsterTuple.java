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

import gomule.dropCalc.DCNew;
import gomule.dropCalc.ProbTCRow;
import gomule.dropCalc.items.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import randall.d2files.D2TblFile;
import randall.d2files.D2TxtFile;
import randall.d2files.D2TxtFileItemProperties;

public class MonsterTuple {

	Monster mParent;
	String AreaName;
	String RealAreaName;
	int Level;
	String initTC;
	String calcd = "";
	int mUqual;
	int mSqual;

	//SO FOR EVERY INIT TC THERE IS A SETOF FINAL TCS MAPPED TO A SET OF FINAL TC PROBs
	HashMap finalTCs = new HashMap();
	HashMap finalMiscTCs = new HashMap();
	HashMap finalTrueMiscTCs = new HashMap();
//	private int mPicks;
	private String calcdM = "";

	public MonsterTuple(String AreaName, Integer level, String initTC, Monster mParent){
		this.mParent = mParent;
		this.AreaName = AreaName;
		this.Level = level.intValue();
		this.initTC = initTC;
		this.RealAreaName = D2TblFile.getString(D2TxtFile.LEVELS.searchColumns("Name",AreaName).get("LevelName"));
		setQualities();
	}

	private void setQualities() {


		D2TxtFileItemProperties qRow = D2TxtFile.TCS.searchColumns("Treasure Class", initTC);

		if(qRow != null){

			if(qRow.get("Unique").equals("")){
				mUqual = 0;
			}

			if(qRow.get("Set").equals("")){
				mSqual = 0;
			}

		}else{
			mUqual = 0;
			mSqual = 0;
		}
	}

	public int getUqual() {
		return mUqual;
	}

	public String getInitTC() {
		return initTC;
	}

	public void setFinalTCs(ArrayList allTCS) {



		allTCS = deleteDuplicated(allTCS);

		for(int x = 0;x<allTCS.size();x=x+1){
			for(int y = 0;y<((ProbTCRow)allTCS.get(x)).getTC().size();y=y+1){
				if(finalTCs.containsKey(((ProbTCRow)allTCS.get(x)).getTC().get(y))){
					finalTCs.put(((ProbTCRow)allTCS.get(x)).getTC().get(y), new Double(((Double)finalTCs.get(((ProbTCRow)allTCS.get(x)).getTC().get(y))).doubleValue() + ((Double)((ProbTCRow)allTCS.get(x)).getProb().get(y)).doubleValue()));
				}else{
					finalTCs.put(((ProbTCRow)allTCS.get(x)).getTC().get(y), ((ProbTCRow)allTCS.get(x)).getProb().get(y));
				}
			}
		}		
	}

	public void setFinalTrueMiscTCs(ArrayList allTCS) {



		allTCS = deleteMiscDuplicated(allTCS);

		for(int x = 0;x<allTCS.size();x=x+1){
			for(int y = 0;y<((ProbTCRow)allTCS.get(x)).getTC().size();y=y+1){
				if(finalTrueMiscTCs.containsKey(((ProbTCRow)allTCS.get(x)).getTC().get(y))){
					finalTrueMiscTCs.put(((ProbTCRow)allTCS.get(x)).getTC().get(y), new Double(((Double)finalTrueMiscTCs.get(((ProbTCRow)allTCS.get(x)).getTC().get(y))).doubleValue() + ((Double)((ProbTCRow)allTCS.get(x)).getProb().get(y)).doubleValue()));
				}else{
					finalTrueMiscTCs.put(((ProbTCRow)allTCS.get(x)).getTC().get(y), ((ProbTCRow)allTCS.get(x)).getProb().get(y));
				}
			}
		}		
	}

	public void setFinalMiscTCs(ArrayList miscTCS) {

		for(int x = 0;x<miscTCS.size();x=x+1){
			for(int y = 0;y<((ProbTCRow)miscTCS.get(x)).getTC().size();y=y+1){
//				if(((ProbTCRow)miscTCS.get(x)).getTC().get(y).equals("rin") || ((ProbTCRow)miscTCS.get(x)).getTC().get(y).equals("amu")|| ((ProbTCRow)miscTCS.get(x)).getTC().get(y).equals("jew")){
				finalMiscTCs.put(((ProbTCRow)miscTCS.get(x)).getTC().get(y), ((ProbTCRow)miscTCS.get(x)).getProb().get(y));
//				}
			}
		}

	}
	public ArrayList deleteDuplicated(ArrayList allTCS)
	{

		for(int x = 0;x<allTCS.size();x=x+1){

			ProbTCRow dup = (ProbTCRow) allTCS.get(x);
			for(int y = 0;y<dup.getTC().size();y=y+1){
				if(((String)dup.getTC().get(y)).indexOf("Equip")!=-1 || ((String)dup.getTC().get(y)).indexOf("Act") !=-1||((String)dup.getTC().get(y)).indexOf("gld") !=-1){
					dup.getTC().remove(dup.getTC().get(y));
					dup.getProb().remove(y);
					y=y-1;
				}
			}
		}

		return allTCS;

	}
	public ArrayList deleteMiscDuplicated(ArrayList allTCS)
	{

		for(int x = 0;x<allTCS.size();x=x+1){

			ProbTCRow dup = (ProbTCRow) allTCS.get(x);
			for(int y = 0;y<dup.getTC().size();y=y+1){
				if(((String)dup.getTC().get(y)).indexOf("Runes")!=-1 || ((String)dup.getTC().get(y)).indexOf("Act")!=-1||((String)dup.getTC().get(y)).indexOf("gld")!=-1 || ((String)dup.getTC().get(y)).indexOf("Jewelry") !=-1 || ((String)dup.getTC().get(y)).indexOf("Gem")!=-1){
					dup.getTC().remove(dup.getTC().get(y));
					dup.getProb().remove(y);
					y=y-1;
				}
			}
		}

		return allTCS;

	}
	public HashMap getFinalTCs() {

		return finalTCs;
	}

	public void printFinalTCs() {

		System.out.println(finalTCs);

	}

	public ProbTCRow lookupTCReturnSUBATOMICTCROW(String tcQuery){



		D2TxtFileItemProperties initTCRow = D2TxtFile.TCS.searchColumns("Treasure Class", tcQuery);
		String selector = "Item1";
		String probSelector = "Prob1";
		ArrayList thisSubTC = new ArrayList();
		ArrayList thisSubTCProb = new ArrayList();

		if(initTCRow != null){
			for(int x = 1;x<11;x=x+1){

//				if(initTCRow.get(selector).indexOf("Equip")!=-1 || initTCRow.get(selector).indexOf("weap")!=-1||initTCRow.get(selector).indexOf("armo")!=-1){
				if(!initTCRow.get(selector).equals("")){
					thisSubTC.add(initTCRow.get(selector));
					thisSubTCProb.add(new Double(Double.parseDouble(initTCRow.get(probSelector))));

//					if(((String)thisSubTC.get(x -1)).indexOf("Act")!=-1){
//					System.out.println("HM " + lookupTCReturnSUBTCS((String)thisSubTC.get(x-1)));
//					}
				}
//				}
				selector = selector.substring(0, selector.length() - 1) + (new Integer(x+1)); 
				probSelector = probSelector.substring(0, probSelector.length() - 1) + (new Integer(x+1)); 
			}
			if(!initTCRow.get("Unique").equals("")){
				if(Integer.parseInt(initTCRow.get("Unique")) > mUqual){
					mUqual = Integer.parseInt(initTCRow.get("Unique"));
				}
			}

			if(!initTCRow.get("Set").equals("")){
				if(Integer.parseInt(initTCRow.get("Set")) > mSqual){
					mSqual = Integer.parseInt(initTCRow.get("Set"));
				}
			}
		}




		return new ProbTCRow(thisSubTC, thisSubTCProb)	;

	}

	private void multiplyOut(ProbTCRow row, double counter) {
		for(int x = 0; x<row.getProb().size();x=x+1){
			row.setProb(((Double)row.getProb().get(x)).doubleValue() *  counter , x);
		}

	}
	public ProbTCRow constructTCPairs(ProbTCRow row) {

//		int totalProb = sum(row.getProb());		

		for(int x = 0;x<row.getTC().size();x=x+1){

			for(int y = x;y<row.getTC().size();y=y+1){
				if(row.getTC().get(y).equals(row.getTC().get(x)) && y!=x){
					row.getTC().remove(y);
					row.getProb().set(x, new Double((((Double)row.getProb().get(x)).doubleValue())+ (((Double)row.getProb().get(y)).doubleValue())));
					row.getProb().remove(y);
				}
			}
			row.getProb().set(x, new Double((((Double)row.getProb().get(x)).doubleValue()) / row.getTotProb()));
		}

//		System.out.println("By jove!");
		return(row);
	}


	public ProbTCRow getLastRow(ArrayList arr){

		return ((ProbTCRow)arr.get(arr.size()-1));

	}

	public void lookupBASETCReturnTrueMiscTCS(int nPlayers, int nplayersParty, double input, Item item, DCNew DC, int MF, boolean sevP) {


//		if(!item.getItemRow().get("type2").equals("")){/
		if(calcdM.equals(nPlayers+","+nplayersParty+","+input + "," + sevP)){
			return;
		}
//		if(mParent.getRealName().indexOf("Council Member (Bremm Sparkfist) councilmember3")){
//		System.out.println();
//		}
//		}

		mParent.clearFinalMT(this);

		String initCountess = null;
//		double miscCounter = 1;
		int keyRow = -1;
		ArrayList miscTCS = new ArrayList();

		if(mParent.getID().equals("duriel")){
			if(getInitTC().indexOf("Base") ==-1){
				miscTCS.add(lookupTCReturnSUBATOMICTCROW(this.getInitTC()));
				setInitTC((String)getLastRow(miscTCS).getTC().get(1));
				miscTCS.clear();
			}
		}


		if(mParent.getID().equals("The Countess")){
			if(getInitTC().indexOf("Countess Item") ==-1){
				initCountess = this.initTC;
				miscTCS.add(lookupTCReturnSUBATOMICTCROW(this.getInitTC()));
				setInitTC((String)((ProbTCRow)miscTCS.get(0)).getTC().get(0));
				miscTCS.clear();
			}

		}

		miscTCS.add(lookupTCReturnSUBATOMICTCROW(this.getInitTC()));

		if(mParent.getClassOfMon() == 2 || mParent.getClassOfMon() == 3|| this.getInitTC().indexOf("Champ") !=-1|| this.getInitTC().indexOf("Super")!=-1|| this.getInitTC().indexOf("Unique")!=-1 || ((String)getLastRow(miscTCS).getTC().get(0)).indexOf("Uitem") !=-1){
			miscTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(miscTCS).getTC().get(0))));
		}

		for(int x = 0;x<getLastRow(miscTCS).getTC().size();x++){
			if(((String)getLastRow(miscTCS).getTC().get(x)).indexOf("Good") !=-1){
//				System.out.println(x);
				keyRow = x;

			}
		}
		if(keyRow == -1){
			System.err.println("ERROR OCCURING KEYROW1? - " + mParent.getRealName());

			calcdM = nPlayers+","+nplayersParty+","+input;
			return;
		}

		miscTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(miscTCS).getTC().get(keyRow))));

//		if(getLastRow(miscTCS).getTC().size()==2){
//		miscTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(miscTCS).getTC().get(0))));
//		}else{
//		miscTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(miscTCS).getTC().get(3))));
//		}




		ArrayList gemTree = new ArrayList();
		for(int x = 0 ;x<miscTCS.size();x++){
			gemTree.add(new ProbTCRow((ProbTCRow)miscTCS.get(x)));
		}

		parseGemTree(gemTree,keyRow);

		if(((String)getLastRow(miscTCS).getTC().get(getLastRow(miscTCS).getTC().size()-1)).indexOf("Runes")!=-1 && ((String)getLastRow(miscTCS).getTC().get(getLastRow(miscTCS).getTC().size()-2)).indexOf("Runes") !=-1){
			ArrayList runesArr1 = new ArrayList();
			ArrayList runesArr2 = new ArrayList();
			for(int x = 0 ;x<miscTCS.size();x++){
				runesArr1.add(new ProbTCRow((ProbTCRow)miscTCS.get(x)));
				runesArr2.add(new ProbTCRow((ProbTCRow)miscTCS.get(x)));
			}

			parseRuneTree(runesArr1,getLastRow(miscTCS).getTC().size()-1, keyRow);
			parseRuneTree(runesArr2,getLastRow(miscTCS).getTC().size()-2, keyRow);
			miscTCS.addAll(runesArr1);
			miscTCS.addAll(runesArr2);

		}else{
			if(((String)getLastRow(miscTCS).getTC().get(getLastRow(miscTCS).getTC().size()-1)).indexOf("Runes")!=-1){
				parseRuneTree(miscTCS,getLastRow(miscTCS).getTC().size()-1, keyRow);
			}
		}

		miscTCS.addAll(gemTree);



		this.setFinalTrueMiscTCs(miscTCS);
		/**
		 * APPLY PICKS...
		 * 
		 */






		if(mParent.getID().equals("The Countess")){

			HashMap countessArr = parseCountessRuneTree((String) lookupTCReturnSUBATOMICTCROW(initCountess).getTC().get(1));		


			Iterator it = countessArr.keySet().iterator();

			while(it.hasNext()){

				String tc = (String)it.next();

				if(finalTrueMiscTCs.containsKey(tc)){
					finalTrueMiscTCs.put(tc, new Double(((Double)(finalTrueMiscTCs.get(tc))).doubleValue() + (((Double)(countessArr.get(tc))).doubleValue())));
				}else{
					finalTrueMiscTCs.put(tc, countessArr.get(tc));
				}

			}



			setInitTC(initCountess);

		}else{
			applyMPicks(finalTrueMiscTCs, sevP);
		}





		calcdM = nPlayers+","+nplayersParty+","+input + "," + sevP;

	}

	public int factorial(int k)
	{
		int fact = 1;
		for (int i=1; i<=k; i++) {
			fact = fact * i;
		}

		return fact;

	}
	public double chanceDrop(int k){
		double noDrop = (double)19/(double)67; 
		return ((double)factorial(5) / (double)((factorial(5-k)) * factorial(k))) * (Math.pow(noDrop, (5 - k)))*(Math.pow((1 - noDrop), k));

	}

	private HashMap parseCountessRuneTree(String initTC) {






		ArrayList runesArr = new ArrayList();

//		System.out.println(initTC);

		double miscCounter = 1;

		runesArr.add(lookupTCReturnSUBATOMICTCROW(initTC));

		if(((String)getLastRow(runesArr).getTC().get(getLastRow(runesArr).getTC().size() - 1)).indexOf("Runes") !=-1){

			do{
				runesArr.add(lookupTCReturnSUBATOMICTCROW((String) getLastRow(runesArr).getTC().get(getLastRow(runesArr).getTC().size() - 1)));
			}while(((String)getLastRow(runesArr).getTC().get(getLastRow(runesArr).getTC().size() - 1)).indexOf("Runes") !=-1);

		}

		int noDrop = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class", initTC).get("NoDrop"));

		((ProbTCRow)runesArr.get(0)).setTotProb(((ProbTCRow)runesArr.get(0)).getTotProb() + noDrop) ;

		for(int x = 0; x< runesArr.size();x=x+1){

			constructTCPairs((ProbTCRow)runesArr.get(x));
			multiplyOut((ProbTCRow)runesArr.get(x), miscCounter);

			miscCounter =  ((((Double)((ProbTCRow)runesArr.get(x)).getProb().get(((ArrayList)((ProbTCRow)runesArr.get(x)).getProb()).size() - 1))).doubleValue());


		}


		runesArr = deleteMiscDuplicated(runesArr);
		HashMap finalTrueMiscTCs = new HashMap();

		for(int x = 0;x<runesArr.size();x=x+1){
			for(int y = 0;y<((ProbTCRow)runesArr.get(x)).getTC().size();y=y+1){
				if(finalTrueMiscTCs.containsKey(((ProbTCRow)runesArr.get(x)).getTC().get(y))){
					finalTrueMiscTCs.put(((ProbTCRow)runesArr.get(x)).getTC().get(y), new Double(((Double)finalTrueMiscTCs.get(((ProbTCRow)runesArr.get(x)).getTC().get(y))).doubleValue() + ((Double)((ProbTCRow)runesArr.get(x)).getProb().get(y)).doubleValue()));
				}else{
					finalTrueMiscTCs.put(((ProbTCRow)runesArr.get(x)).getTC().get(y), ((ProbTCRow)runesArr.get(x)).getProb().get(y));
				}
			}
		}	


		System.out.println("ITEM:" + this.finalTrueMiscTCs.get("r01"));
		System.out.println("RUNE:" + finalTrueMiscTCs.get("r01"));

		double pCI = ((Double)this.finalTrueMiscTCs.get("r01")).doubleValue();
		double pCR = ((Double)finalTrueMiscTCs.get("r01")).doubleValue();


		System.out.println("Value (" + this.mParent.getMonDiff()  + "): " + 
				(1 - (
						(Math.pow((1 - pCI),0) * Math.pow((1 - pCR),3)* chanceDrop(0)) +
						(Math.pow((1 - pCI),1) * Math.pow((1 - pCR),3)* chanceDrop(1)) + 
						(Math.pow((1 - pCI),2) * Math.pow((1 - pCR),3)* chanceDrop(2)) + 
						(Math.pow((1 - pCI),3) * Math.pow((1 - pCR),3)* chanceDrop(3)) +
						(Math.pow((1 - pCI),4) * Math.pow((1 - pCR),2)* chanceDrop(4)) +
						(Math.pow((1 - pCI),5) * Math.pow((1 - pCR),1)* chanceDrop(5))
				)));



		int picks = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",initTC).get("Picks"));
		Iterator pickIt = finalTrueMiscTCs.keySet().iterator();
		while(pickIt.hasNext()){
			String pickItStr = (String) pickIt.next();


//			Math.pow((1- ((Double)finalTrueMiscTCs.get(pickItStr)).doubleValue()), picks)


			finalTrueMiscTCs.put(pickItStr,new Double(1-(Math.pow((1- ((Double)finalTrueMiscTCs.get(pickItStr)).doubleValue()), picks))));

		}

		return finalTrueMiscTCs;
	}

	private void parseGemTree(ArrayList gemArr,int keyRow) {

//		if(initTC.indexOf("Wraith")&&mParent.getRealName().indexOf("Ghost")){
//		System.out.println();
//		}

		double miscCounter = 1;

		if(((String)(getLastRow(gemArr).getTC().get(0))).indexOf("Jewelry") ==-1){
			System.err.println(getLastRow(gemArr).getTC().get(0));
		}

		ProbTCRow ptcr = getLastRow(gemArr);



//		gemArr.add(lookupTCReturnSUBATOMICTCROW((String) getLastRow(gemArr).getTC().get(key)));

//		if(((String)getLastRow(gemArr).getTC().get(getLastRow(gemArr).getTC().size() - 1)).indexOf("Runes") !=-1){

//		do{
//		gemArr.add(lookupTCReturnSUBATOMICTCROW((String) getLastRow(gemArr).getTC().get(getLastRow(gemArr).getTC().size() - 1)));
//		}while(((String)getLastRow(gemArr).getTC().get(getLastRow(gemArr).getTC().size() - 1)).indexOf("Runes") !=-1);

//		}

//		if(!((String) getLastRow(gemArr).getTC().get(0)).equals("r01"))
//		{
//		System.out.println(getParent().getRealName() + " -- " +(String) getLastRow(gemArr).getTC().get(0));
//		}
		int picks = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Picks"));
//		mPicks  = picks;
		if(picks < 0){

			if(mParent.getClassOfMon()==2 || mParent.getClassOfMon()==3|| mParent.getClassOfMon()==4){
				constructTCPairs((ProbTCRow)gemArr.get(1));
				miscCounter =  (((Double)((ProbTCRow)gemArr.get(1)).getProb().get(keyRow)).doubleValue());
			}

			constructTCPairs((ProbTCRow)gemArr.get(2));
			multiplyOut((ProbTCRow)gemArr.get(2), miscCounter);

		}else{

			int noDrop = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class", this.getInitTC()).get("NoDrop"));
//			if(nPlayers>1){
//			double realnP = Math.floor(1 + ((double)(nPlayers -1)/(double)2) + ((double)(nplayersParty -1)/(double)2));
//			int probSum = ((ProbTCRow)allTCS.get(0)).getTotProb();
//			if(noDrop != 0){
//			noDrop = ( (int)Math.floor(((double)probSum)/(BigDecimal.valueOf(((((double)1)/(Math.pow((((double)noDrop)/((double)(noDrop + probSum))),realnP))))).subtract ((BigDecimal.valueOf(1)))).doubleValue()));
//			}
//			}
			((ProbTCRow)gemArr.get(0)).setTotProb(((ProbTCRow)gemArr.get(0)).getTotProb() + noDrop) ;

			constructTCPairs((ProbTCRow)gemArr.get(0));


			if(((ProbTCRow)gemArr.get(0)).getTC().size()==2){
				miscCounter =  (((Double)((ProbTCRow)gemArr.get(0)).getProb().get(0)).doubleValue());
			}else{

				miscCounter =  (((Double)((ProbTCRow)gemArr.get(0)).getProb().get(keyRow)).doubleValue());
			}

			if(mParent.getClassOfMon()==5 && ((Boss)mParent).getQuest()){
				miscCounter =  (((Double)((ProbTCRow)gemArr.get(0)).getProb().get(keyRow)).doubleValue());
			}


			constructTCPairs((ProbTCRow)gemArr.get(1));
			multiplyOut((ProbTCRow)gemArr.get(1), miscCounter);

			if(((String)((ProbTCRow)gemArr.get(1)).getTC().get(0)).indexOf("Equip") !=-1){
				miscCounter =  ((((Double)((ProbTCRow)gemArr.get(1)).getProb().get(1))).doubleValue());
				constructTCPairs((ProbTCRow)gemArr.get(2));
				multiplyOut((ProbTCRow)gemArr.get(2), miscCounter);
			}
		}

		for(int x = 1;x<ptcr.getTC().size();x++){
			if(((String) ptcr.getTC().get(x)).indexOf("Gem") !=-1){
				ProbTCRow modRow = (lookupTCReturnSUBATOMICTCROW((String) ptcr.getTC().get(x)));
				constructTCPairs(modRow);
				multiplyOut(modRow, ((Double)ptcr.getProb().get(x)).doubleValue());
				gemArr.add(modRow);
			}
//			System.out.println();
		}
//		System.out.println();


//		if(!((String)mpRow.getTC().get(0)).indexOf("Jewelry")){
//		System.err.println(getLastRow(gemArr).getTC().get(0));
//		}


//		((ProbTCRow)gemArr.get(f)).printTCs();



//		for(int x = f; x< gemArr.size();x=x+1){

//		constructTCPairs((ProbTCRow)gemArr.get(x));
//		multiplyOut((ProbTCRow)gemArr.get(x), miscCounter);


//		miscCounter =  ((((Double)((ProbTCRow)gemArr.get(x)).getProb().get(((ArrayList)((ProbTCRow)gemArr.get(x)).getProb()).size() - 1))).doubleValue());





//		}


	}

	private void parseRuneTree(ArrayList runesArr, int key, int keyRow) {


		double miscCounter = 1;

		runesArr.add(lookupTCReturnSUBATOMICTCROW((String) getLastRow(runesArr).getTC().get(key)));

		if(((String)getLastRow(runesArr).getTC().get(getLastRow(runesArr).getTC().size() - 1)).indexOf("Runes") !=-1){

			do{
				runesArr.add(lookupTCReturnSUBATOMICTCROW((String) getLastRow(runesArr).getTC().get(getLastRow(runesArr).getTC().size() - 1)));
			}while(((String)getLastRow(runesArr).getTC().get(getLastRow(runesArr).getTC().size() - 1)).indexOf("Runes") !=-1);

		}

//		if(!((String) getLastRow(runesArr).getTC().get(0)).equals("r01"))
//		{
//		System.out.println(getParent().getRealName() + " -- " +(String) getLastRow(runesArr).getTC().get(0));
//		}
		int f = 0;
		int picks = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Picks"));
//		mPicks  = picks;
		if(picks < 0){

			if(mParent.getClassOfMon()==2 || mParent.getClassOfMon()==3|| mParent.getClassOfMon()==4){
				constructTCPairs((ProbTCRow)runesArr.get(1));
				miscCounter =  (((Double)((ProbTCRow)runesArr.get(1)).getProb().get(keyRow)).doubleValue());
			}

			constructTCPairs((ProbTCRow)runesArr.get(2));
			multiplyOut((ProbTCRow)runesArr.get(2), miscCounter);
			miscCounter =  ((((Double)((ProbTCRow)runesArr.get(2)).getProb().get(key))).doubleValue());


			f=3;
		}else{

			int noDrop = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class", this.getInitTC()).get("NoDrop"));
//			if(nPlayers>1){
//			double realnP = Math.floor(1 + ((double)(nPlayers -1)/(double)2) + ((double)(nplayersParty -1)/(double)2));
//			int probSum = ((ProbTCRow)allTCS.get(0)).getTotProb();
//			if(noDrop != 0){
//			noDrop = ( (int)Math.floor(((double)probSum)/(BigDecimal.valueOf(((((double)1)/(Math.pow((((double)noDrop)/((double)(noDrop + probSum))),realnP))))).subtract ((BigDecimal.valueOf(1)))).doubleValue()));
//			}
//			}
			((ProbTCRow)runesArr.get(0)).setTotProb(((ProbTCRow)runesArr.get(0)).getTotProb() + noDrop) ;

			constructTCPairs((ProbTCRow)runesArr.get(0));


			if(((ProbTCRow)runesArr.get(0)).getTC().size()==2){
				miscCounter =  (((Double)((ProbTCRow)runesArr.get(0)).getProb().get(0)).doubleValue());
			}else{

				miscCounter =  (((Double)((ProbTCRow)runesArr.get(0)).getProb().get(keyRow)).doubleValue());
			}

			if(mParent.getClassOfMon()==5 && ((Boss)mParent).getQuest()){
				miscCounter =  (((Double)((ProbTCRow)runesArr.get(0)).getProb().get(keyRow)).doubleValue());
			}


			constructTCPairs((ProbTCRow)runesArr.get(1));
			multiplyOut((ProbTCRow)runesArr.get(1), miscCounter);
			if(((String)((ProbTCRow)runesArr.get(1)).getTC().get(0)).indexOf("Equip") !=-1){
				miscCounter =  ((((Double)((ProbTCRow)runesArr.get(1)).getProb().get(1))).doubleValue());
				constructTCPairs((ProbTCRow)runesArr.get(2));
				multiplyOut((ProbTCRow)runesArr.get(2), miscCounter);
				miscCounter =  ((((Double)((ProbTCRow)runesArr.get(2)).getProb().get(key))).doubleValue());
				f=3;
			}else{
				miscCounter =  ((((Double)((ProbTCRow)runesArr.get(1)).getProb().get(key))).doubleValue());
				f=2;
			}
		}


		for(int x = f; x< runesArr.size();x=x+1){

			constructTCPairs((ProbTCRow)runesArr.get(x));
			multiplyOut((ProbTCRow)runesArr.get(x), miscCounter);


			miscCounter =  ((((Double)((ProbTCRow)runesArr.get(x)).getProb().get(((ArrayList)((ProbTCRow)runesArr.get(x)).getProb()).size() - 1))).doubleValue());





		}


	}

	public void lookupBASETCReturnMiscTCS(int nPlayers, int nplayersParty, double input, Item item, DCNew DC, int MF, int QRecursions, boolean sevP) {

		if(calcdM.equals(nPlayers+","+nplayersParty+","+input + "," + sevP + "," + item.getItemCode())){
//			return;
		}

		mParent.clearFinalM(this);
		String initCountess = null;
		double miscCounter = 1;
		int keyRow = -1;
		ArrayList miscTCS = new ArrayList();

		if(mParent.getID().equals("duriel")){
			if(getInitTC().indexOf("Base") ==-1){
				miscTCS.add(lookupTCReturnSUBATOMICTCROW(this.getInitTC()));
				setInitTC((String)getLastRow(miscTCS).getTC().get(1));
				miscTCS.clear();
			}
		}


		if(mParent.getID().equals("The Countess")){
			if(getInitTC().indexOf("Countess Item") ==-1){
				initCountess = this.initTC;
				miscTCS.add(lookupTCReturnSUBATOMICTCROW(this.getInitTC()));
				setInitTC((String)((ProbTCRow)miscTCS.get(0)).getTC().get(0));
				miscTCS.clear();
			}

		}

		miscTCS.add(lookupTCReturnSUBATOMICTCROW(this.getInitTC()));

		if(mParent.getClassOfMon() == 2 || mParent.getClassOfMon() == 3|| this.getInitTC().indexOf("Champ") !=-1|| this.getInitTC().indexOf("Super") !=-1|| this.getInitTC().indexOf("Unique") !=-1 || ((String)getLastRow(miscTCS).getTC().get(0)).indexOf("Uitem") !=-1){
			miscTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(miscTCS).getTC().get(0))));
		}

		for(int x = 0;x<getLastRow(miscTCS).getTC().size();x++){
			if(((String)getLastRow(miscTCS).getTC().get(x)).indexOf("Good")!=-1){
//				System.out.println(x);
				keyRow = x;

			}
		}
		if(keyRow == -1){
			System.err.println("ERROR OCCURING KEYROW1? - " + mParent.getRealName());

			calcdM = nPlayers+","+nplayersParty+","+input;
			return;
		}

		miscTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(miscTCS).getTC().get(keyRow))));

//		if(getLastRow(miscTCS).getTC().size()==2){
//		miscTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(miscTCS).getTC().get(0))));
//		}else{
//		miscTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(miscTCS).getTC().get(3))));
//		}


		if(((String)getLastRow(miscTCS).getTC().get(0)).indexOf("Jewelry") !=-1){

			miscTCS.add(lookupTCReturnSUBATOMICTCROW((String) getLastRow(miscTCS).getTC().get(0)));

		}else{
			System.err.println("Err?");
		}

//		System.out.println((String) getLastRow(miscTCS).getTC().get(0));

		int f = 0;
		int picks = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Picks"));
//		mPicks  = picks;
		if(picks < 0){

//			((ProbTCRow)miscTCS.get(1)).setTotProb(((ProbTCRow)miscTCS.get(1)).getTotProb()) ;
//			constructTCPairs((ProbTCRow)miscTCS.get(1));
//			if(mParent.getClassOfMon()==2 || mParent.getClassOfMon()==1 ){
//			miscCounter =  (((Double)((ProbTCRow)miscTCS.get(1)).getProb().get(1)).doubleValue());
//			}else{
//			miscCounter =  (((Double)((ProbTCRow)miscTCS.get(1)).getProb().get(0)).doubleValue());
//			}
			if(mParent.getClassOfMon()==2 || mParent.getClassOfMon()==3|| mParent.getClassOfMon()==4){
//				((ProbTCRow)miscTCS.get(1)).setTotProb(((ProbTCRow)miscTCS.get(1)).getTotProb()) ;
				constructTCPairs((ProbTCRow)miscTCS.get(1));
				miscCounter =  (((Double)((ProbTCRow)miscTCS.get(1)).getProb().get(keyRow)).doubleValue());
			}
			f=2;
		}else{

			int noDrop = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class", this.getInitTC()).get("NoDrop"));
//			if(nPlayers>1){
//			double realnP = Math.floor(1 + ((double)(nPlayers -1)/(double)2) + ((double)(nplayersParty -1)/(double)2));
//			int probSum = ((ProbTCRow)allTCS.get(0)).getTotProb();
//			if(noDrop != 0){
//			noDrop = ( (int)Math.floor(((double)probSum)/(BigDecimal.valueOf(((((double)1)/(Math.pow((((double)noDrop)/((double)(noDrop + probSum))),realnP))))).subtract ((BigDecimal.valueOf(1)))).doubleValue()));
//			}
//			}
			((ProbTCRow)miscTCS.get(0)).setTotProb(((ProbTCRow)miscTCS.get(0)).getTotProb() + noDrop) ;

			constructTCPairs((ProbTCRow)miscTCS.get(0));


			if(((ProbTCRow)miscTCS.get(0)).getTC().size()==2){
				miscCounter =  (((Double)((ProbTCRow)miscTCS.get(0)).getProb().get(0)).doubleValue());
			}else{

				miscCounter =  (((Double)((ProbTCRow)miscTCS.get(0)).getProb().get(keyRow)).doubleValue());
			}

			if(mParent.getClassOfMon()==5 && ((Boss)mParent).getQuest()){
				miscCounter =  (((Double)((ProbTCRow)miscTCS.get(0)).getProb().get(keyRow)).doubleValue());
			}

			f=1;
		}

		for(int x = f; x< miscTCS.size();x=x+1){

			if(!((String)((ProbTCRow)miscTCS.get(x)).getTC().get(0)).equals("gld")){
				constructTCPairs((ProbTCRow)miscTCS.get(x));
				multiplyOut((ProbTCRow)miscTCS.get(x), miscCounter);
//				if(((String)getLastRow(miscTCS).getTC().get(0)).indexOf("Jewelry") !=-1){
				if(((String)((ProbTCRow)miscTCS.get(x)).getTC().get(0)).indexOf("Equip") !=-1){
					miscCounter =  ((((Double)((ProbTCRow)miscTCS.get(x)).getProb().get(1))).doubleValue());
//					System.out.print(((ProbTCRow)miscTCS.get(x)).getTC().get(1) + " , ");
				}else{
					miscCounter =  ((((Double)((ProbTCRow)miscTCS.get(x)).getProb().get(0))).doubleValue());
//					System.out.print(((ProbTCRow)miscTCS.get(x)).getTC().get(0) + " , ");
				}
//				miscTCS.add(lookupTCReturnSUBATOMICTCROW((String) getLastRow(miscTCS).getTC().get(0)));
//				}
			}
		}

		this.setFinalMiscTCs(miscTCS);
		/**
		 * APPLY PICKS...
		 * 
		 */


//		if(picks < 0 ||picks >1){
			if(input != 0){
					input = input * DC.getQuality(item, getLevel(), MF,this, QRecursions, true);

				applyMPicks(input,  sevP);
			}else{
				applyMPicks(finalMiscTCs,  sevP);
			}
//		}else{
//			System.out.println();
//		}

		if(mParent.getID().equals("The Countess")){
			setInitTC(initCountess);
		}
		calcdM = nPlayers+","+nplayersParty+","+input + "," + sevP + "," + item.getItemCode();

	}

	public void lookupBASETCReturnATOMICTCS(int nPlayers, int nplayersParty, double input, boolean sevP) {



		if(calcd.equals(nPlayers+","+nplayersParty+","+input + "," + sevP)){
			return;
		}

		mParent.clearFinal(this);
		String initCountess = null;
		double counter = 1;
		ArrayList allTCS = new ArrayList();			

		if(mParent.getID().equals("duriel")){
			if(getInitTC().indexOf("Base") ==-1){
				allTCS.add(lookupTCReturnSUBATOMICTCROW(this.getInitTC()));
				setInitTC((String)getLastRow(allTCS).getTC().get(1));
				allTCS.clear();
			}
		}
		if(mParent.getID().equals("The Countess")){
			if(getInitTC().indexOf("Countess Item") ==-1){
				initCountess = this.initTC;
				allTCS.add(lookupTCReturnSUBATOMICTCROW(this.getInitTC()));
				setInitTC((String)((ProbTCRow)allTCS.get(0)).getTC().get(0));
				allTCS.clear();
			}

		}

		allTCS.add(lookupTCReturnSUBATOMICTCROW(this.getInitTC()));
		if(mParent.getClassOfMon()==2 || mParent.getClassOfMon()==3|| mParent.getClassOfMon()==4 || mParent.getID().equals("griswold")){
			allTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(allTCS).getTC().get(0))));
			//With SOME SUs, the last TC is empty for some reason. So remove it if it is!
			if(getLastRow(allTCS).getTC().isEmpty()){
				allTCS.remove(allTCS.size()-1);
			}
		}

		if(mParent.getClassOfMon()==2 || mParent.getClassOfMon()==0 ||mParent.getClassOfMon()==5 || mParent.getClassOfMon()==4 || mParent.getClassOfMon()==1){
			if(!mParent.getID().equals("griswold") && mParent.getClassOfMon()!=4){
				//Quest drops use a different row.
				if(mParent.getClassOfMon()==5 && ((Boss)mParent).getQuest()){
					allTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(allTCS).getTC().get(0))));
				}else
				{
					allTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(allTCS).getTC().get(1))));
				}
			}
			else{
				if(((String)getLastRow(allTCS).getTC().get(0)).indexOf("gld") !=-1){
					allTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(allTCS).getTC().get(1))));
				}else{
					allTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(allTCS).getTC().get(0))));
				}
			}

		}else{
			allTCS.add(lookupTCReturnSUBATOMICTCROW(((String)getLastRow(allTCS).getTC().get(0))));
		}

		if(((String)getLastRow(allTCS).getTC().get(getLastRow(allTCS).getTC().size() - 1)).indexOf("Equip") !=-1){

			do{
				allTCS.add(lookupTCReturnSUBATOMICTCROW((String) getLastRow(allTCS).getTC().get(getLastRow(allTCS).getTC().size() - 1)));
			}while(((String)getLastRow(allTCS).getTC().get(getLastRow(allTCS).getTC().size() - 1)).indexOf("Equip") !=-1);

		}

		int f = 0;
		int picks = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Picks"));
//		mPicks  = picks;
		if(picks < 0){

			((ProbTCRow)allTCS.get(1)).setTotProb(((ProbTCRow)allTCS.get(1)).getTotProb()) ;
			constructTCPairs((ProbTCRow)allTCS.get(1));
			if(mParent.getClassOfMon()==2 || mParent.getClassOfMon()==1 || initTC.indexOf("Champ")!= -1){
				counter =  (((Double)((ProbTCRow)allTCS.get(1)).getProb().get(1)).doubleValue());
			}else{
				counter =  (((Double)((ProbTCRow)allTCS.get(1)).getProb().get(0)).doubleValue());
			}

			f=2;
		}else{

			int noDrop = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class", this.getInitTC()).get("NoDrop"));
			if(nPlayers>1){
				double realnP = Math.floor(1 + ((double)(nPlayers -1)/(double)2) + ((double)(nplayersParty -1)/(double)2));
				int probSum = ((ProbTCRow)allTCS.get(0)).getTotProb();
				if(noDrop != 0){
					noDrop = ( (int)Math.floor(((double)probSum)/(BigDecimal.valueOf(((((double)1)/(Math.pow((((double)noDrop)/((double)(noDrop + probSum))),realnP))))).subtract ((BigDecimal.valueOf(1)))).doubleValue()));
				}
			}
			((ProbTCRow)allTCS.get(0)).setTotProb(((ProbTCRow)allTCS.get(0)).getTotProb() + noDrop) ;
			constructTCPairs((ProbTCRow)allTCS.get(0));

			//BOSS QUEST DROPS TAKE FROM 0th ENTRY
			if(mParent.getClassOfMon()==5 && ((Boss)mParent).getQuest()){
				counter =  (((Double)((ProbTCRow)allTCS.get(0)).getProb().get(0)).doubleValue());
			}else{
				counter =  (((Double)((ProbTCRow)allTCS.get(0)).getProb().get(1)).doubleValue());
			}
			f=1;
		}
		for(int x = f; x< allTCS.size();x=x+1){
			if(!((String)((ProbTCRow)allTCS.get(x)).getTC().get(0)).equals("gld")){
				constructTCPairs((ProbTCRow)allTCS.get(x));
				multiplyOut((ProbTCRow)allTCS.get(x), counter);
				if(((String)((ProbTCRow)allTCS.get(x)).getTC().get(((ProbTCRow)allTCS.get(x)).getTC().size() -1 )).indexOf("Equip") != -1){
					counter =  ((((Double)((ProbTCRow)allTCS.get(x)).getProb().get(((ProbTCRow)allTCS.get(x)).getProb().size() -1 ))).doubleValue());
				}
			}
		}

		this.setFinalTCs(allTCS);
		/**
		 * APPLY PICKS...
		 * 
		 */

//		if(picks < 0 ||picks >1){
		if(input != 0){
			if(mParent.getClassOfMon()!= 4 && mParent.getClassOfMon()!=5){
				System.out.println("RWAR");
			}
			applyPicks(input, sevP);
		}else{
			applyPicks(sevP);
		}

		if(mParent.getID().equals("The Countess")){
			setInitTC(initCountess);
		}

		calcd = nPlayers+","+nplayersParty+","+input + "," + sevP;
	}


	public void applyPicks(boolean sevP){

		int picks = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Picks"));


		if(picks > 1){
			if(picks > 6){
				if(sevP){
					int noDrop = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class", this.getInitTC()).get("NoDrop"));
					ProbTCRow ptcR = lookupTCReturnSUBATOMICTCROW(this.getInitTC());
					ptcR.setTotProb(ptcR.getTotProb() + noDrop);
					Iterator pickIt = this.getFinalTCs().keySet().iterator();
					while(pickIt.hasNext()){
						String pickItStr = (String) pickIt.next();
						this.getFinalTCs().put(pickItStr,new Double((1-(Math.pow((1- ((Double)this.getFinalTCs().get(pickItStr)).doubleValue()), 6))) + ((1-(Math.pow((1-((double)noDrop/(double)ptcR.getTotProb())), 6))) * ((Double)this.getFinalTCs().get(pickItStr)).doubleValue())));

					}
					return;
				}else{
					picks = picks -1;
				}
			}
			Iterator pickIt = this.getFinalTCs().keySet().iterator();
			while(pickIt.hasNext()){
				String pickItStr = (String) pickIt.next();
				this.getFinalTCs().put(pickItStr,new Double(1-(Math.pow((1- ((Double)this.getFinalTCs().get(pickItStr)).doubleValue()), picks))));

			}
		}
		if(picks == -4 && mParent.getClassOfMon()==4){
			int pow = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Prob1"));
			Iterator pickIt = this.getFinalTCs().keySet().iterator();
			while(pickIt.hasNext()){
				String pickItStr = (String) pickIt.next();
				this.getFinalTCs().put(pickItStr,new Double(1-(Math.pow((1- ((Double)this.getFinalTCs().get(pickItStr)).doubleValue()), pow))));
			}
		}
	}


	public void applyMPicks(HashMap Tcs, boolean sevP){

		int picks = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Picks"));


		if(picks > 1){
			if(picks > 6){
				if(sevP){
					int noDrop = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class", this.getInitTC()).get("NoDrop"));
					ProbTCRow ptcR = lookupTCReturnSUBATOMICTCROW(this.getInitTC());
					ptcR.setTotProb(ptcR.getTotProb() + noDrop);
					Iterator pickIt = this.getFinalTCs().keySet().iterator();
					while(pickIt.hasNext()){
						String pickItStr = (String) pickIt.next();
						this.getFinalTCs().put(pickItStr,new Double((1-(Math.pow((1- ((Double)this.getFinalTCs().get(pickItStr)).doubleValue()), 6))) + ((1-(Math.pow((1-((double)noDrop/(double)ptcR.getTotProb())), 6))) * ((Double)this.getFinalTCs().get(pickItStr)).doubleValue())));

					}
					return;
				}else{
					picks = picks -1;
				}
			}
			Iterator pickIt = Tcs.keySet().iterator();
			while(pickIt.hasNext()){
				String pickItStr = (String) pickIt.next();
				Tcs.put(pickItStr,new Double(1-(Math.pow((1- ((Double)Tcs.get(pickItStr)).doubleValue()), picks))));

			}
		}
		if(picks == -4 && mParent.getClassOfMon()==4){
			int pow = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Prob1"));
			Iterator pickIt = Tcs.keySet().iterator();
			while(pickIt.hasNext()){
				String pickItStr = (String) pickIt.next();
				Tcs.put(pickItStr,new Double(1-(Math.pow((1- ((Double)Tcs.get(pickItStr)).doubleValue()), pow))));
			}
		}
	}

	public void applyMPicks(double input, boolean sevP){

		int picks = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Picks"));


		if(picks > 1){
			if(picks > 6){
				if(sevP){
					int noDrop = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class", this.getInitTC()).get("NoDrop"));
					ProbTCRow ptcR = lookupTCReturnSUBATOMICTCROW(this.getInitTC());
					ptcR.setTotProb(ptcR.getTotProb() + noDrop);
					Iterator pickIt = this.getFinalTCs().keySet().iterator();
					while(pickIt.hasNext()){
						String pickItStr = (String) pickIt.next();
						this.getFinalTCs().put(pickItStr,new Double((1-(Math.pow((1- ((Double)this.getFinalTCs().get(pickItStr)).doubleValue()), 6))) + ((1-(Math.pow((1-((double)noDrop/(double)ptcR.getTotProb())), 6))) * ((Double)this.getFinalTCs().get(pickItStr)).doubleValue())));

					}
					return;
				}else{
					picks = picks -1;
				}
			}
			Iterator pickIt = this.getFinalMiscTCs().keySet().iterator();
			while(pickIt.hasNext()){
				String pickItStr = (String) pickIt.next();
//				if(mParent.getName().indexOf("The Cow King")){
//				this.getFinalMiscTCs().put(pickItStr,new Double((1-(Math.pow((1- (((Double)this.getFinalMiscTCs().get(pickItStr)).doubleValue())), picks))) *input));

//				}else{

				this.getFinalMiscTCs().put(pickItStr,new Double((1-(Math.pow((1- (((Double)this.getFinalMiscTCs().get(pickItStr)).doubleValue() *input)), picks)))));
//				}
			}
		}else if(picks == -4 && mParent.getClassOfMon()==4){
			int pow = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Prob1"));
			Iterator pickIt = this.getFinalMiscTCs().keySet().iterator();
			while(pickIt.hasNext()){
				String pickItStr = (String) pickIt.next();
				this.getFinalMiscTCs().put(pickItStr,new Double(1-(Math.pow((1- (((Double)this.getFinalMiscTCs().get(pickItStr)).doubleValue()*input)), pow))));
			}
		}else{

			Iterator pickIt = this.getFinalMiscTCs().keySet().iterator();
			while(pickIt.hasNext()){
				String pickItStr = (String) pickIt.next();
				this.getFinalMiscTCs().put(pickItStr,new Double(((Double)this.getFinalMiscTCs().get(pickItStr)).doubleValue()*input));
			}

		}
	}

	public void applyPicks(double input, boolean sevP){

		int picks = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Picks"));


		if(picks > 1){
			if(picks > 6){
				if(sevP){
					int noDrop = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class", this.getInitTC()).get("NoDrop"));
					ProbTCRow ptcR = lookupTCReturnSUBATOMICTCROW(this.getInitTC());
					ptcR.setTotProb(ptcR.getTotProb() + noDrop);
					Iterator pickIt = this.getFinalTCs().keySet().iterator();
					while(pickIt.hasNext()){
						String pickItStr = (String) pickIt.next();
						this.getFinalTCs().put(pickItStr,new Double((1-(Math.pow((1- ((Double)this.getFinalTCs().get(pickItStr)).doubleValue()), 6))) + ((1-(Math.pow((1-((double)noDrop/(double)ptcR.getTotProb())), 6))) * ((Double)this.getFinalTCs().get(pickItStr)).doubleValue())));

					}
					return;
				}else{
					picks = picks -1;
				}
			}
			Iterator pickIt = this.getFinalTCs().keySet().iterator();
			while(pickIt.hasNext()){
				String pickItStr = (String) pickIt.next();
				this.getFinalTCs().put(pickItStr,new Double(1-(Math.pow((1- (((Double)this.getFinalTCs().get(pickItStr)).doubleValue()*input)), picks))));

			}
		}
		if(picks == -4 && mParent.getClassOfMon()==4){
			int pow = Integer.parseInt(D2TxtFile.TCS.searchColumns("Treasure Class",this.getInitTC()).get("Prob1"));
			Iterator pickIt = this.getFinalTCs().keySet().iterator();
			while(pickIt.hasNext()){
				String pickItStr = (String) pickIt.next();
				this.getFinalTCs().put(pickItStr,new Double(1-(Math.pow((1- (((Double)this.getFinalTCs().get(pickItStr)).doubleValue()*input)), pow))));
			}
//			}else{
//			Iterator pickIt = this.getFinalTCs().keySet().iterator();
//			while(pickIt.hasNext()){
//			String pickItStr = (String) pickIt.next();
//			this.getFinalTCs().put(pickItStr,new Double(((Double)this.getFinalTCs().get(pickItStr)).doubleValue()*input));
//			}
		}
	}


	private void setInitTC(String string) {
		this.initTC = string;

	}

	public Monster getParent() {
		return this.mParent;
	}

	public String getArLvlName(){
		return this.RealAreaName + " (mlvl " + this.Level + ")";
	}

	public int getSqual() {
		return mSqual;
	}

	public int getLevel() {
		return Level;
	}

	public HashMap getFinalMiscTCs() {
		return finalMiscTCs;
	}

	public HashMap getFinalTrueMiscTCs() {
		return finalTrueMiscTCs;
	}
}
