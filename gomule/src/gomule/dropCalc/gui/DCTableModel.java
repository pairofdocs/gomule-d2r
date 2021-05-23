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
package gomule.dropCalc.gui;

import gomule.dropCalc.monsters.MonsterTuple;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

public class DCTableModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6556329099541788418L;

	/**
	 * Getting an ArrayList of MonsterTuples
	 * Or A HashMap of (Tuple,Prob) pairs for items
	 */
//	public ArrayList mNames;
//	public ArrayList mAreas;
//	public ArrayList mProbs;

	public ArrayList tmRows;

	public ArrayList iTableModelListeners = new ArrayList();
	public int type = 0;
	public int iSelected = 2;
	public boolean dec = true;
	private int colCount = 3;
	private int chancePercent = 50;
	public DCTableModel(){
//		iItems.put("RAWR", );
//		this.setValueAt(iItems.get(0), 0, 0);
	}


	public Class getColumnClass(int c) {

		return String.class;
	}

	public int getColumnCount() {
		return colCount;
	}

	
	public void showChanceCol(){
		colCount = 4;
		fireTableStructureChanged();
	}
	
	public void hideChanceCol(){
		colCount = 3;
		fireTableStructureChanged();
	}
	
	public void refresh(HashMap iItems, int nDiff, int classKey, boolean dec){
		this.dec = dec;
		type = 1;
		tmRows = new ArrayList();

		Iterator it = iItems.keySet().iterator();
		while(it.hasNext()){

			MonsterTuple tSelected = (MonsterTuple) it.next();

			switch(nDiff){
			case 0:
				if(tSelected.getParent().getClassOfMon() == classKey){
					tmRows.add(new OutputRow(tSelected.getParent().getRealName() + " (" + tSelected.getParent().getMonDiff() + ")",tSelected.getArLvlName(),iItems.get(tSelected),
							Math.log(1-((double)chancePercent/100.0)) / Math.log( 1 -((Double)iItems.get(tSelected)).doubleValue())));	}
				break;

			case 1:
				if(tSelected.getParent().getClassOfMon()== classKey && tSelected.getParent().getMonDiff().equals("N")){
					tmRows.add(new OutputRow(tSelected.getParent().getRealName() + " (" + tSelected.getParent().getMonDiff() + ")",tSelected.getArLvlName(),iItems.get(tSelected),
							Math.log(1-((double)chancePercent/100.0)) / Math.log( 1 -((Double)iItems.get(tSelected)).doubleValue())));}
				break;

			case 2:
				if(tSelected.getParent().getClassOfMon()== classKey && tSelected.getParent().getMonDiff().equals("NM")){
					tmRows.add(new OutputRow(tSelected.getParent().getRealName() + " (" + tSelected.getParent().getMonDiff() + ")",tSelected.getArLvlName(),iItems.get(tSelected),
							Math.log(1-((double)chancePercent/100.0)) / Math.log( 1 -((Double)iItems.get(tSelected)).doubleValue())));}
				break;

			case 3:
				if(tSelected.getParent().getClassOfMon()== classKey && tSelected.getParent().getMonDiff().equals("H")){
					tmRows.add(new OutputRow(tSelected.getParent().getRealName() + " (" + tSelected.getParent().getMonDiff() + ")",tSelected.getArLvlName(),iItems.get(tSelected),
							Math.log(1-((double)chancePercent/100.0)) / Math.log( 1 -((Double)iItems.get(tSelected)).doubleValue())));}
				break;
			}
//			tmRows.add(new OutputRow(tSelected.getParent().getRealName() + " (" + tSelected.getParent().getTinyDiff() + ")",tSelected.getArLvlName(),iItems.get(tSelected)));
		}

		fireTableStructureChanged();
		fireTableChanged();
		sortCol(iSelected);
	}

	public void refresh(ArrayList mTuples, boolean dec){
		this.dec = dec;
		type = 0;
		tmRows = new ArrayList();

		for(int x = 0;x<mTuples.size();x=x+1){

			MonsterTuple tSelected = ((MonsterTuple)mTuples.get(x));
			Iterator TCIt = tSelected.getFinalTCs().keySet().iterator();

			while(TCIt.hasNext()){

				String tcArr = (String) TCIt.next();				
				tmRows.add(new OutputRow(tcArr,tSelected.getArLvlName(),tSelected.getFinalTCs().get(tcArr),
						Math.log(1-((double)chancePercent/100.0)) / Math.log( 1 -((Double)tSelected.getFinalTCs().get(tcArr)).doubleValue())));

			}
		}

		fireTableStructureChanged();
		fireTableChanged();
		sortCol(iSelected);
	}

	public String getColumnName(int arg0) {
		switch(type){
		case 0:
			switch (arg0)
			{
			case 0:
				return "TC";
			case 1:
				return "Area";
			case 2:
				return "Probability";
			case 3:
				return "Kills for "+chancePercent+"% confidence of drop" ;
			default:
				return "";
			}
		case 1:
			switch (arg0)
			{
			case 0:
				return "Name";
			case 1:
				return "Area";
			case 2:
				return "Probability";
			case 3:
				return "Kills for "+chancePercent+"% confidence of drop" ;
			default:
				return "";
			}
		}
		return "";
//		return columnNames[arg0];
	}

	public int getRowCount() {

		if(tmRows == null){
			return 0;
		}

		return tmRows.size();
	}

	public Object getValueAt(int row, int col) {
		switch(col){
		case 0:
			return ((OutputRow)tmRows.get(row)).getC0();
		case 1:
			return ((OutputRow)tmRows.get(row)).getC1();
		case 2:
			return ((OutputRow)tmRows.get(row)).getStrC2(dec);
		case 3:
			return ((OutputRow)tmRows.get(row)).getStrC3(dec);
		default:
			return new String("");
		}

//		return new Integer(type);

	}

	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}


	public void addTableModelListener(TableModelListener pListener)
	{
		iTableModelListeners.add(pListener);
	}

	public void removeTableModelListener(TableModelListener pListener)
	{
		iTableModelListeners.remove(pListener);
	}

	public void fireTableChanged()
	{
		fireTableChanged(new TableModelEvent(this));
	}

	public void fireTableChanged(TableModelEvent pEvent)
	{
		for (int i = 0; i < iTableModelListeners.size(); i++)
		{
			((TableModelListener) iTableModelListeners.get(i))
			.tableChanged(pEvent);
		}
	}

	public void setValueAt(Object value, int row, int col) {


	}


	public void reset() {
//		type = (type * -1) + 1;
		tmRows = new ArrayList();
		fireTableChanged();
		this.fireTableStructureChanged();

	}


	public void sortCol(int headerCol) {

		iSelected = headerCol;
		Collections.sort(tmRows, new Comparator()
		{
			public int compare(Object pObj1, Object pObj2)
			{
				OutputRow lItem1 = (OutputRow) pObj1;
				OutputRow lItem2 = (OutputRow) pObj2;


				switch(iSelected){

				case 0:

//					switch(type){
//					case 0:

//					case 1:

//					}
					return(lItem1.getC0().compareTo(lItem2.getC0()));


				case 1:

					return(lItem1.getC1().compareTo(lItem2.getC1()));


				case 2:



					return(lItem2.getObjC2().compareTo(lItem1.getObjC2()));
					
				case 3:
					
					return(lItem2.getObjC3().compareTo(lItem1.getObjC3()));

				}

//				for ( int i = 0 ; i < iSortList.size() ; i++ )
//				{
//				Object lSort = iSortList.get(i);

//				if ( lSort ==  HEADER[0] )
//				{
//				return lItem1.getName().compareTo(lItem2.getName());
//				}
//				else if ( lSort ==  HEADER[1] )
//				{
//				return lItem1.getReqLvl() - lItem2.getReqLvl();
//				} 
//				else if ( lSort ==  HEADER[2] )
//				{
//				return lItem1.getReqStr() - lItem2.getReqStr();
//				} 
//				else if ( lSort ==  HEADER[3] )
//				{
//				return lItem1.getReqDex() - lItem2.getReqDex();
//				} 
//				else if ( lSort ==  HEADER[4] )
//				{
//				String lFileName1 = ((D2ItemListAll) iStash).getFilename(lItem1);
//				String lFileName2 = ((D2ItemListAll) iStash).getFilename(lItem2);
//				return lFileName1.compareTo(lFileName2);
//				} 
//				}

				return 0;
			}
		});
		fireTableChanged();


//		switch(headerCol){

//		case 0:
////		ArrayList mNamesOld = mNames;
////		Collections.sort(mNames);
////		OrderColumns()
//		break;


//		case 1:
////		Collections.sort(mAreas);
//		break;


//		case 2:
////		Collections.sort(mProbs);
//		break;

//		}

	}


	public void setChanceCol(int chancePercent) {
		
		this.chancePercent = chancePercent;
		fireTableStructureChanged();
		fireTableChanged();
		fireTableDataChanged();
	}

}
