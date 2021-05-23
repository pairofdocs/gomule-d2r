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

import gomule.dropCalc.DCNew;
import gomule.dropCalc.items.Item;
import gomule.dropCalc.monsters.Monster;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class RealGUI extends JFrame {

	private static final long serialVersionUID = -9041550034028154852L;
	JMenuBar menuBar = new JMenuBar();
	JComboBox nPlayers = new JComboBox();
	JComboBox nGroup = new JComboBox();
	JComboBox nSearch = new JComboBox();
	JComboBox nClass = new JComboBox();
	JComboBox nType = new JComboBox();
	JComboBox nMonster = new JComboBox();
	JComboBox nDiff = new JComboBox();
	JComboBox nMItem = new JComboBox();
	JComboBox nMItemClass = new JComboBox();
	JComboBox nMItemQual = new JComboBox();
	JComboBox nMItemQual2 = new JComboBox();
	DCTableModel DCTm = new DCTableModel();
	JTable oResult = new JTable(DCTm);
	JScrollPane oScrollingArea = new JScrollPane(oResult);
	JTextField nMF = new JTextField();
	JButton bSearch = new JButton("Find");
	JButton bClear = new JButton("Clear");
	final DCNew DC = new DCNew();
	ArrayList nMonsterKey = new ArrayList();
	private JPanel pMonsters;
	private Box hM2;
	Box hMI1;
	Box hMI2;
	ArrayList nMItemKey = new ArrayList();
	private Box vMonsters;
	private JCheckBoxMenuItem cbMenuItem;
	private JCheckBoxMenuItem cbDec;
	private JCheckBoxMenuItem cbRat;
	private JCheckBoxMenuItem cbChanceCol;
	private Container iContentPane;
	private JMenuItem cbChanceInput;
	public RealGUI(){

		iContentPane  = this.getContentPane();
		this.setJMenuBar(menuBar);
		JMenu menu = new JMenu("Options");
		menuBar.add(menu);
		cbMenuItem = new JCheckBoxMenuItem("Use 7 picks");
		cbDec = new JCheckBoxMenuItem("Decimal Mode");
		cbRat = new JCheckBoxMenuItem("Odds Mode");
		menu.add(cbMenuItem);
		menu.addSeparator();
		menu.add(cbDec);
		cbDec.setSelected(true);
		menu.add(cbRat);
		menu.addSeparator();
		cbChanceCol = new JCheckBoxMenuItem("Add Kills Column (50%)");
		cbChanceInput = new JMenuItem("Change Kills %");
		cbChanceInput.setEnabled(false);
		menu.add(cbChanceCol);
		menu.add(cbChanceInput);
		
		Box vMain = Box.createVerticalBox();
		Box hSettings = Box.createHorizontalBox();
		vMain.add(hSettings);

		//Player information
		JPanel pPlayers = new JPanel();
		pPlayers.setBorder((new TitledBorder(null, ("Basics"), 
				TitledBorder.LEFT, TitledBorder.TOP)));
		Box vPlayers = Box.createVerticalBox();
		pPlayers.add(vPlayers);
		Box hP1 = Box.createHorizontalBox();
		hP1.add(new JLabel("N Players"));
		hP1.add(nPlayers);
		Box hP2 = Box.createHorizontalBox();
		hP2.add(new JLabel("N Group"));
		hP2.add(nGroup);
		Box hP3 = Box.createHorizontalBox();
		hP3.add(new JLabel("MF"));
		hP3.add(nMF);

		nMF.setDocument(new TextFieldLimiter(5));
		nMF.setText("0");

		Box hP4 = Box.createHorizontalBox();
		hP4.add(bSearch);
		hP4.add(Box.createRigidArea(new Dimension(3,0)));
		hP4.add(bClear);
		vPlayers.add(hP1);
		vPlayers.add(Box.createRigidArea(new Dimension(0,8)));
		vPlayers.add(hP2);
		vPlayers.add(Box.createRigidArea(new Dimension(0,8)));
		vPlayers.add(hP3);
		vPlayers.add(Box.createRigidArea(new Dimension(0,8)));
		vPlayers.add(hP4);
		hSettings.add(pPlayers);
		for(int x = 1;x<9;x=x+1){
			nGroup.addItem(Integer.toString(x));
			nPlayers.addItem(Integer.toString(x));
		}
		nGroup.setSelectedIndex(0);
		nPlayers.setSelectedIndex(0);
		//Search information
		JPanel pSearch = new JPanel();
		pSearch.setBorder((new TitledBorder(null, ("Filter..."), 
				TitledBorder.LEFT, TitledBorder.TOP)));
		Box vSearch = Box.createVerticalBox();
		pSearch.add(vSearch);
		Box hS1 = Box.createHorizontalBox();
		hS1.add(new JLabel("Search for..."));
		hS1.add(nSearch);
		Box hS2 = Box.createHorizontalBox();
		hS2.add(new JLabel("Mon Class"));
		hS2.add(nClass);
		Box hS3 = Box.createHorizontalBox();
		hS3.add(new JLabel("Difficulty"));
		hS3.add(nDiff);
//		vSearch.add(hS1);
//		vSearch.add(Box.createRigidArea(new Dimension(0,8)));
		vSearch.add(hS2);
		vSearch.add(Box.createRigidArea(new Dimension(0,8)));
		vSearch.add(hS3);
		hSettings.add(pSearch);

		nSearch.addItem("Monster");
		nSearch.addItem("Item");
		nSearch.setSelectedIndex(0);

		nClass.addItem("Regular");
		nClass.addItem("Minion");
		nClass.addItem("Champion");
		nClass.addItem("Unique");
		nClass.addItem("SuperUnique");
		nClass.addItem("Boss");
		nClass.setSelectedIndex(0);

		nDiff.addItem("All");
		nDiff.addItem("Normal");
		nDiff.addItem("Nightmare");
		nDiff.addItem("Hell");
		nDiff.setSelectedIndex(0);
		//Monster information
		pMonsters = new JPanel();
		pMonsters.setBorder((new TitledBorder(null, ("Search"), 
				TitledBorder.LEFT, TitledBorder.TOP)));
		vMonsters = Box.createVerticalBox();
		pMonsters.add(vMonsters);
		Box hM1 = Box.createHorizontalBox();
		hM1.add(new JLabel("Type"));
		hM1.add(nType);
		hM2 = Box.createHorizontalBox();
		hM2.add(new JLabel("Monster"));
		hM2.add(nMonster);

		vMonsters.add(hM1);
		vMonsters.add(Box.createRigidArea(new Dimension(0,8)));

		hSettings.add(pMonsters);

		nType.addItem("Atomic TC");
		nType.addItem("Item");
		nType.setSelectedIndex(0);

		refreshMonsterField();
		//Monster-Item information
		nMItemQual.addItem("Base Item");
		nMItemQual.addItem("Unique Item");
		nMItemQual.addItem("Set Item");
		nMItemQual.addItem("Rare Item");
		nMItemQual.addItem("Magic Item");
//		nMItemQual.addItem("Superior Item");
//		nMItemQual.addItem("White Item");
//		nMItemQual.addItem("Crude Item");
		
		nMItemQual.setSelectedIndex(0);
		nMItemQual2.addItem("All");
		nMItemQual2.addItem("Normal");
		nMItemQual2.addItem("Exceptional");
		nMItemQual2.addItem("Elite");
		nMItemQual2.addItem("Misc");
		nMItemQual2.setSelectedIndex(0);
		nMItemClass.addItem("Any");
		nMItemClass.addItem("Weapon");
		nMItemClass.addItem("Armor");
		nMItemClass.setSelectedIndex(0);
		hMI1 = Box.createHorizontalBox();
		hMI2 = Box.createHorizontalBox();
		vMonsters.add(hMI1);
		vMonsters.add(Box.createRigidArea(new Dimension(0,8)));
		vMonsters.add(hMI2);
		vMonsters.add(Box.createRigidArea(new Dimension(0,8)));
		vMonsters.add(hM2);
		refreshMonsterItemField();
//		pMItem = new JPanel();
//		pMItem.setBorder((new TitledBorder(null, ("Monsters"), 
//		TitledBorder.LEFT, TitledBorder.TOP)));
//		Box vMItem = Box.createVerticalBox();
//		pMItem.add(vMItem);
//		Box hMI1 = Box.createHorizontalBox();
//		hMI1.add(new JLabel("Type"));
//		hMI1.add(nType);
////		Box hMI2 = Box.createHorizontalBox();
////		hMI2.add(new JLabel("Monster"));
////		hMI2.add(nMonster);
//		vMItem.add(hMI1);
//		vMItem.add(Box.createRigidArea(new Dimension(0,8)));
////		vMItem.add(hMI2);
////		hSettings.add(pMItem);
////		pMItem.setVisible(false);

		//Results box
		vMain.add(oScrollingArea);
//		oResult
		oResult.setPreferredScrollableViewportSize(new Dimension(100, 500));
//		oScrollingArea.getViewport().setBackground(Color.white);
//		oResult.
		//Other window stuff
//		addWindowListener(new WindowAdapter() {
//			public void windowClosing(WindowEvent e) {
//				System.exit(0);
//			}
//		});
		setTitle("Diablo II 1.10+ Drop Calculator");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setupListeners();
		getContentPane().add(vMain);
		setSize(900, 650);
		setVisible(true);

	}

	private void setupListeners() {



		oResult.getTableHeader().addMouseListener( new MouseAdapter() 
		{
			public void mouseReleased(MouseEvent e) 
			{
				if ( e.getSource() instanceof JTableHeader )
				{
					JTableHeader lHeader = (JTableHeader) e.getSource();
					int lHeaderCol = lHeader.getColumnModel().getColumn(lHeader.columnAtPoint(new Point(e.getX(), e.getY()))).getModelIndex();

					if ( lHeaderCol != -1 && lHeader.getTable().getRowCount() > 0)
					{
						((DCTableModel)oResult.getModel()).sortCol(lHeaderCol);
					}
				}
			}
		});

		
		
		
		cbDec.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				
				if(cbRat.isSelected()){
					cbRat.setSelected(false);
				}else{
					cbDec.setSelected(true);
				}
				
				
				
//				System.out.println(cbRat.isSelected());
//				System.out.println(cbDec.isSelected());
//
//				if(!cbDec.isSelected()){
//					cbDec.setSelected(true);
//					cbRat.setSelected(false);
//				}else{
//					cbRat.setSelected(true);
//					cbDec.setSelected(false);
//				}

			}});
		
		cbRat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				
				if(cbDec.isSelected()){
					cbDec.setSelected(false);
				}else{
					cbRat.setSelected(true);
				}
				
			}});
		
		cbChanceInput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				activateChanceColumn(getChancePercent());
				
			}
		});
		
		cbChanceCol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				
				if(cbChanceCol.isSelected()){
					
					activateChanceColumn(50);
					cbChanceInput.setEnabled(true);
					
					}else{
					
						deactivateChanceColumn();
						cbChanceInput.setEnabled(false);
				}
				
			}

			private void deactivateChanceColumn() {
				DCTm.hideChanceCol();
				
			}

});
		
		
		
		nType.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {


				refreshMonster(nType.getSelectedIndex());


			}

			private void refreshMonster(int selectedIndex) {


				if(selectedIndex == 0){

					hM2.removeAll();
					hM2.add(new JLabel("Monster"));
					hM2.add(nMonster);
					hMI1.removeAll();
					hMI2.removeAll();
				}else{

					hM2.removeAll();
					hMI1.removeAll();
					hMI2.removeAll();
					hM2.add(new JLabel("Item"));
					hM2.add(nMItem);
					hMI1.add(new JLabel("Quality"));
					hMI1.add(nMItemQual);
					hMI2.add(new JLabel("Version"));
					hMI2.add(nMItemQual2);

				}

//				pMonsters.validate();
//				vMonsters.validate();
				validate();

			}});

		nDiff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				resetMonsterArrays();
				refreshMonsterField();

			}});

		nClass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				resetMonsterArrays();
				refreshMonsterField();

			}});

		nMItemQual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				resetMonsterItemArrays();
				refreshMonsterItemField();

			}});

		nMItemQual2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				resetMonsterItemArrays();
				refreshMonsterItemField();

			}});

		bClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

//				resetArrays();
				resetDisplay();
//				refreshMonsterField();

			}});

		bSearch.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

				refreshCalculator();

			}});

	}

	protected void refreshCalculator(){
		int mf = 0;
		
		try{
			mf = Integer.parseInt(nMF.getText());
		}catch(NumberFormatException n){
			nMF.setText("0");
		}
		

		if(nType.getSelectedIndex() == 0){

			Monster mSelected = ((Monster)nMonsterKey.get(nMonster.getSelectedIndex()));
			mSelected.lookupBASETCReturnATOMICTCS(nPlayers.getSelectedIndex()+ 1, nGroup.getSelectedIndex()+1,0,cbMenuItem.isSelected());
			((DCTableModel)oResult.getModel()).refresh(mSelected.getmTuples(),cbDec.isSelected());

		}else{
			Item mISelected =(Item)nMItemKey.get(nMItem.getSelectedIndex());
			HashMap mTuple = mISelected.getFinalProbSum(DC,nClass.getSelectedIndex(),mf,nPlayers.getSelectedIndex()+ 1, nGroup.getSelectedIndex()+1, nMItemQual.getSelectedIndex() - 1,cbMenuItem.isSelected());
			((DCTableModel)oResult.getModel()).refresh(mTuple, nDiff.getSelectedIndex(), nClass.getSelectedIndex(),cbDec.isSelected());

		}
	}


	protected void activateChanceColumn(int chancePercent) {
			
			DCTm.setChanceCol(chancePercent);
			DCTm.showChanceCol();
			refreshCalculator();
	}

	protected int getChancePercent() {
		
		String chancePercentStr = JOptionPane.showInputDialog(iContentPane,"Please Enter a Percentage (1-99, default 50%):");
		int chancePercent = 50;
		try{
			chancePercent = Integer.parseInt(chancePercentStr);
			if(chancePercent > 99 || chancePercent < 1){
				throw new NumberFormatException();
			}
		}catch(NumberFormatException n){
			JOptionPane.showMessageDialog(iContentPane, "Bad value entered, assuming 50%.", "Bad Value", JOptionPane.ERROR_MESSAGE);
			chancePercent = 50;
		}
		
		return chancePercent;
		
	}

	private void resetMonsterArrays(){
		nMonster.removeAllItems();
		nMonsterKey.clear();
	}

	private void resetMonsterItemArrays(){
		nMItem.removeAllItems();
		nMItemKey.clear();
	}

	private void resetDisplay(){

		((DCTableModel)oResult.getModel()).reset();
//		oResult.setText("");
//		DCTm.fireTableDataChanged();

	}

	private void refreshMonsterItemField(){
		switch(nMItemQual.getSelectedIndex()){
		case 0:
			switch(nMItemQual2.getSelectedIndex()){

			case 0:
				for(int x  = 0;x<DC.getRegItemArray().size();x++){
					nMItemKey.add(DC.getRegItemArray().get(x));
					nMItem.addItem(((Item)DC.getRegItemArray().get(x)).getRealName());
				}
				for(int x  = 0;x<DC.getMiscItemArray().size();x++){
					nMItemKey.add(DC.getMiscItemArray().get(x));
					nMItem.addItem(((Item)DC.getMiscItemArray().get(x)).getRealName());

				}


				break;
			case 1:
				for(int x  = 0;x<DC.getRegItemArray().size();x++){
					if(((Item)DC.getRegItemArray().get(x)).getItemQual() == 0){
						nMItemKey.add(DC.getRegItemArray().get(x));
						nMItem.addItem(((Item)DC.getRegItemArray().get(x)).getRealName());
					}
				}
				break;
			case 2:
				for(int x  = 0;x<DC.getRegItemArray().size();x++){
					if(((Item)DC.getRegItemArray().get(x)).getItemQual() == 1){
						nMItemKey.add(DC.getRegItemArray().get(x));
						nMItem.addItem(((Item)DC.getRegItemArray().get(x)).getRealName());
					}
				}
				break;
			case 3:
				for(int x  = 0;x<DC.getRegItemArray().size();x++){
					if(((Item)DC.getRegItemArray().get(x)).getItemQual() == 2){
						nMItemKey.add(DC.getRegItemArray().get(x));
						nMItem.addItem(((Item)DC.getRegItemArray().get(x)).getRealName());
					}
				}
				break;
			case 4:
				for(int x  = 0;x<DC.getMiscItemArray().size();x++){
					nMItemKey.add(DC.getMiscItemArray().get(x));
					nMItem.addItem(((Item)DC.getMiscItemArray().get(x)).getRealName());

				}
				break;

			}
			break;
		case 2:
			switch(nMItemQual2.getSelectedIndex()){

			case 0:
				for(int x  = 0;x<DC.getSetItemArray().size();x++){
					nMItemKey.add(DC.getSetItemArray().get(x));
					nMItem.addItem(((Item)DC.getSetItemArray().get(x)).getRealName());

				}
				break;
			case 1:
				for(int x  = 0;x<DC.getSetItemArray().size();x++){
					if(((Item)DC.getSetItemArray().get(x)).getItemQual() == 0){
						nMItemKey.add(DC.getSetItemArray().get(x));
						nMItem.addItem(((Item)DC.getSetItemArray().get(x)).getRealName());
					}
				}
				break;
			case 2:
				for(int x  = 0;x<DC.getSetItemArray().size();x++){
					if(((Item)DC.getSetItemArray().get(x)).getItemQual() == 1){
						nMItemKey.add(DC.getSetItemArray().get(x));
						nMItem.addItem(((Item)DC.getSetItemArray().get(x)).getRealName());
					}
				}
				break;
			case 3:
				for(int x  = 0;x<DC.getSetItemArray().size();x++){
					if(((Item)DC.getSetItemArray().get(x)).getItemQual() == 2){
						nMItemKey.add(DC.getSetItemArray().get(x));
						nMItem.addItem(((Item)DC.getSetItemArray().get(x)).getRealName());
					}
				}
				break;
			case 4:
				for(int x  = 0;x<DC.getSetItemArray().size();x++){
					if(((Item)DC.getSetItemArray().get(x)).getItemQual() == 3){
						nMItemKey.add(DC.getSetItemArray().get(x));
						nMItem.addItem(((Item)DC.getSetItemArray().get(x)).getRealName());
					}
				}
				break;
			}
			break;
		case 1:
			switch(nMItemQual2.getSelectedIndex()){

			case 0:
				for(int x  = 0;x<DC.getUniqItemArray().size();x++){
					nMItemKey.add(DC.getUniqItemArray().get(x));
					nMItem.addItem(((Item)DC.getUniqItemArray().get(x)).getRealName());

				}
				break;
			case 1:
				for(int x  = 0;x<DC.getUniqItemArray().size();x++){
					if(((Item)DC.getUniqItemArray().get(x)).getItemQual() == 0){
						nMItemKey.add(DC.getUniqItemArray().get(x));
						nMItem.addItem(((Item)DC.getUniqItemArray().get(x)).getRealName());
					}
				}
				break;
			case 2:
				for(int x  = 0;x<DC.getUniqItemArray().size();x++){
					if(((Item)DC.getUniqItemArray().get(x)).getItemQual() == 1){
						nMItemKey.add(DC.getUniqItemArray().get(x));
						nMItem.addItem(((Item)DC.getUniqItemArray().get(x)).getRealName());
					}
				}
				break;
			case 3:
				for(int x  = 0;x<DC.getUniqItemArray().size();x++){
					if(((Item)DC.getUniqItemArray().get(x)).getItemQual() == 2){
						nMItemKey.add(DC.getUniqItemArray().get(x));
						nMItem.addItem(((Item)DC.getUniqItemArray().get(x)).getRealName());
					}
				}
				break;
			case 4:
				for(int x  = 0;x<DC.getUniqItemArray().size();x++){
					if(((Item)DC.getUniqItemArray().get(x)).getItemQual() == 3){
						nMItemKey.add(DC.getUniqItemArray().get(x));
						nMItem.addItem(((Item)DC.getUniqItemArray().get(x)).getRealName());
					}
				}
				break;
			}
			break;
		default:
			switch(nMItemQual2.getSelectedIndex()){

			case 0:
				for(int x  = 0;x<DC.getRegItemArray().size();x++){
					nMItemKey.add(DC.getRegItemArray().get(x));
					nMItem.addItem(((Item)DC.getRegItemArray().get(x)).getRealName());
				}
				for(int x  = 0;x<DC.getMiscItemArray().size();x++){
					nMItemKey.add(DC.getMiscItemArray().get(x));
					nMItem.addItem(((Item)DC.getMiscItemArray().get(x)).getRealName());

				}


				break;
			case 1:
				for(int x  = 0;x<DC.getRegItemArray().size();x++){
					if(((Item)DC.getRegItemArray().get(x)).getItemQual() == 0){
						nMItemKey.add(DC.getRegItemArray().get(x));
						nMItem.addItem(((Item)DC.getRegItemArray().get(x)).getRealName());
					}
				}
				break;
			case 2:
				for(int x  = 0;x<DC.getRegItemArray().size();x++){
					if(((Item)DC.getRegItemArray().get(x)).getItemQual() == 1){
						nMItemKey.add(DC.getRegItemArray().get(x));
						nMItem.addItem(((Item)DC.getRegItemArray().get(x)).getRealName());
					}
				}
				break;
			case 3:
				for(int x  = 0;x<DC.getRegItemArray().size();x++){
					if(((Item)DC.getRegItemArray().get(x)).getItemQual() == 2){
						nMItemKey.add(DC.getRegItemArray().get(x));
						nMItem.addItem(((Item)DC.getRegItemArray().get(x)).getRealName());
					}
				}
				break;
			case 4:
				for(int x  = 0;x<DC.getMiscItemArray().size();x++){
					nMItemKey.add(DC.getMiscItemArray().get(x));
					nMItem.addItem(((Item)DC.getMiscItemArray().get(x)).getRealName());

				}
				break;

			}
			break;
		}
	}

	private void refreshMonsterField(){


		switch(nClass.getSelectedIndex()){
		case 0:
			switch(nDiff.getSelectedIndex()){
			case 0:
				for(int x = 0; x< DC.getMainRegMonArray().size();x=x+1){
					nMonsterKey.add(DC.getMainRegMonArray().get(x));
					nMonster.addItem((String)((Monster)DC.getMainRegMonArray().get(x)).getRealName() + " (" + (String)((Monster)DC.getMainRegMonArray().get(x)).getMonDiff() + ")");
				}
				break;
			case 1:
				for(int x = 0; x< DC.getMainRegMonArray().size();x=x+1){
					if(((String)((Monster)DC.getMainRegMonArray().get(x)).getMonDiff()).equals("N")){
						nMonsterKey.add(DC.getMainRegMonArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainRegMonArray().get(x)).getRealName() + " (" + (String)((Monster)DC.getMainRegMonArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 2:
				for(int x = 0; x< DC.getMainRegMonArray().size();x=x+1){
					if(((String)((Monster)DC.getMainRegMonArray().get(x)).getMonDiff()).equals("NM")){
						nMonsterKey.add(DC.getMainRegMonArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainRegMonArray().get(x)).getRealName() + " (" + (String)((Monster)DC.getMainRegMonArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 3:
				for(int x = 0; x< DC.getMainRegMonArray().size();x=x+1){
					if(((String)((Monster)DC.getMainRegMonArray().get(x)).getMonDiff()).equals("H")){		
						nMonsterKey.add(DC.getMainRegMonArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainRegMonArray().get(x)).getRealName() + " (" + (String)((Monster)DC.getMainRegMonArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			}
			break;
		case 1:
			switch(nDiff.getSelectedIndex()){
			case 0:
				for(int x = 0; x< DC.getMainMinMonArray().size();x=x+1){
					nMonsterKey.add(DC.getMainMinMonArray().get(x));
					nMonster.addItem((String)((Monster)DC.getMainMinMonArray().get(x)).getRealName() + " - " + " (" + (String)((Monster)DC.getMainMinMonArray().get(x)).getMonDiff() + ")");
				}
				break;
			case 1:
				for(int x = 0; x< DC.getMainMinMonArray().size();x=x+1){
					if(((String)((Monster)DC.getMainMinMonArray().get(x)).getMonDiff()).equals("N")){
						nMonsterKey.add(DC.getMainMinMonArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainMinMonArray().get(x)).getRealName() + " - " + " (" + (String)((Monster)DC.getMainMinMonArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 2:
				for(int x = 0; x< DC.getMainMinMonArray().size();x=x+1){
					if(((String)((Monster)DC.getMainMinMonArray().get(x)).getMonDiff()).equals("NM")){
						nMonsterKey.add(DC.getMainMinMonArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainMinMonArray().get(x)).getRealName() + " - " + " (" + (String)((Monster)DC.getMainMinMonArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 3:
				for(int x = 0; x< DC.getMainMinMonArray().size();x=x+1){
					if(((String)((Monster)DC.getMainMinMonArray().get(x)).getMonDiff()).equals("H")){	
						nMonsterKey.add(DC.getMainMinMonArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainMinMonArray().get(x)).getRealName() + " - " + " (" + (String)((Monster)DC.getMainMinMonArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			}
			break;
		case 2:
			switch(nDiff.getSelectedIndex()){
			case 0:
				for(int x = 0; x< DC.getMainChampMonArray().size();x=x+1){
					nMonsterKey.add(DC.getMainChampMonArray().get(x));
					nMonster.addItem((String)((Monster)DC.getMainChampMonArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainChampMonArray().get(x)).getMonDiff() + ")");
				}
				break;
			case 1:
				for(int x = 0; x< DC.getMainChampMonArray().size();x=x+1){
					if(((String)((Monster)DC.getMainChampMonArray().get(x)).getMonDiff()).equals("N")){
						nMonsterKey.add(DC.getMainChampMonArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainChampMonArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainChampMonArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 2:
				for(int x = 0; x< DC.getMainChampMonArray().size();x=x+1){
					if(((String)((Monster)DC.getMainChampMonArray().get(x)).getMonDiff()).equals("NM")){
						nMonsterKey.add(DC.getMainChampMonArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainChampMonArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainChampMonArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 3:
				for(int x = 0; x< DC.getMainChampMonArray().size();x=x+1){
					if(((String)((Monster)DC.getMainChampMonArray().get(x)).getMonDiff()).equals("H")){		
						nMonsterKey.add(DC.getMainChampMonArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainChampMonArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainChampMonArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			}
			break;
		case 3:
			switch(nDiff.getSelectedIndex()){
			case 0:
				for(int x = 0; x< DC.getMainUniqArray().size();x=x+1){
					nMonsterKey.add(DC.getMainUniqArray().get(x));
					nMonster.addItem((String)((Monster)DC.getMainUniqArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainUniqArray().get(x)).getMonDiff() + ")");
				}
				break;
			case 1:
				for(int x = 0; x< DC.getMainUniqArray().size();x=x+1){
					if(((String)((Monster)DC.getMainUniqArray().get(x)).getMonDiff()).equals("N")){
						nMonsterKey.add(DC.getMainUniqArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainUniqArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainUniqArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 2:
				for(int x = 0; x< DC.getMainUniqArray().size();x=x+1){
					if(((String)((Monster)DC.getMainUniqArray().get(x)).getMonDiff()).equals("NM")){
						nMonsterKey.add(DC.getMainUniqArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainUniqArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainUniqArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 3:
				for(int x = 0; x< DC.getMainUniqArray().size();x=x+1){
					if(((String)((Monster)DC.getMainUniqArray().get(x)).getMonDiff()).equals("H")){		
						nMonsterKey.add(DC.getMainUniqArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainUniqArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainUniqArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			}
			break;
		case 4:
			switch(nDiff.getSelectedIndex()){
			case 0:
				for(int x = 0; x< DC.getMainSupUniqArray().size();x=x+1){
					nMonsterKey.add(DC.getMainSupUniqArray().get(x));
					nMonster.addItem((String)((Monster)DC.getMainSupUniqArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainSupUniqArray().get(x)).getMonDiff() + ")");
				}
				break;
			case 1:
				for(int x = 0; x< DC.getMainSupUniqArray().size();x=x+1){
					if(((String)((Monster)DC.getMainSupUniqArray().get(x)).getMonDiff()).equals("N")){
						nMonsterKey.add(DC.getMainSupUniqArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainSupUniqArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainSupUniqArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 2:
				for(int x = 0; x< DC.getMainSupUniqArray().size();x=x+1){
					if(((String)((Monster)DC.getMainSupUniqArray().get(x)).getMonDiff()).equals("NM")){
						nMonsterKey.add(DC.getMainSupUniqArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainSupUniqArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainSupUniqArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 3:
				for(int x = 0; x< DC.getMainSupUniqArray().size();x=x+1){
					if(((String)((Monster)DC.getMainSupUniqArray().get(x)).getMonDiff()).equals("H")){			
						nMonsterKey.add(DC.getMainSupUniqArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainSupUniqArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainSupUniqArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			}
			break;
		case 5:
			switch(nDiff.getSelectedIndex()){
			case 0:
				for(int x = 0; x< DC.getMainBossArray().size();x=x+1){
					nMonsterKey.add(DC.getMainBossArray().get(x));
					nMonster.addItem((String)((Monster)DC.getMainBossArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainBossArray().get(x)).getMonDiff() + ")");
				}
				break;
			case 1:
				for(int x = 0; x< DC.getMainBossArray().size();x=x+1){
					if(((String)((Monster)DC.getMainBossArray().get(x)).getMonDiff()).equals("N")){
						nMonsterKey.add(DC.getMainBossArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainBossArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainBossArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 2:
				for(int x = 0; x< DC.getMainBossArray().size();x=x+1){
					if(((String)((Monster)DC.getMainBossArray().get(x)).getMonDiff()).equals("NM")){
						nMonsterKey.add(DC.getMainBossArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainBossArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainBossArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			case 3:
				for(int x = 0; x< DC.getMainBossArray().size();x=x+1){
					if(((String)((Monster)DC.getMainBossArray().get(x)).getMonDiff()).equals("H")){		
						nMonsterKey.add(DC.getMainBossArray().get(x));
						nMonster.addItem((String)((Monster)DC.getMainBossArray().get(x)).getRealName()+ " (" + (String)((Monster)DC.getMainBossArray().get(x)).getMonDiff() + ")");
					}
				}
				break;
			}
			break;
		}




//		nMonster.setSelectedIndex(0);	

	}

	public static void main(String [] args){

//		D2TxtFile.readAllFiles("d2111");
//		D2TblFile.readAllFiles("d2111");

//		new Regular(D2TxtFile.MONSTATS.getRow(3), "N", 0);


//		DCNew dropCNew = new DCNew();


		new RealGUI();

//		System.out.println(D2TblFile.getString("Cow King's Horns"));

//		D2TxtFileItemProperties ratioRow = D2TxtFile.ITEMRATIO.getRow(0);
//		System.out.println(ratioRow.get("Function"));

//		for(int x = 3;x<88;x=x+3){
//		System.out.println(x);
//		RG.DC.findMonstersTC(x);
//		}

////		RG.DC.findMonstersTC(87);
//		System.out.println("RAWR");
	}

	class TextFieldLimiter extends PlainDocument
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -93741452162148316L;
		int maxChar = -1;
		public TextFieldLimiter(int len){maxChar = len;}
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
		{
			if (str != null && maxChar > 0 && this.getLength() + str.length() > maxChar)
			{
				java.awt.Toolkit.getDefaultToolkit().beep();
				return;
			}
			super.insertString(offs, str, a);
		}
	}

}
