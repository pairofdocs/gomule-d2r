/*******************************************************************************
 * 
 * Copyright 2007 Andy Theuninck, Randall & Silospen
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
package gomule.gui;

import gomule.d2s.*;
import gomule.item.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;
import randall.util.*;

/**
 * @author Marco
 *  
 */
public class D2ViewSharedStash extends JInternalFrame implements D2ItemContainer, D2ItemListListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7350581523641897831L;
	private D2CharPainterPanel       iCharPainter;
	
	// private D2CharCursorPainterPanel iCharCursorPainter;
	private D2SharedStash         	 iSharedStash;

	private JTextArea				 iMessage;
	
	private static final int         BG_WIDTH         = 908; // 626; //550;   908 x 309 pixels
	private static final int         BG_HEIGHT        = 309; // 435; //383;  
	private static final int         BG_CURSOR_WIDTH  = 78;
	private static final int         BG_CURSOR_HEIGHT = 135;

	// change only the X positions for stash-left + inv-right.     (add ~300 to inv,  and subtract ~300 for stash)
	// also search for hard coded pixel values
	private static final int         NUM_SHARED_TABS  = 3;
	private static final int         STASH_X          = 7;         // 327 - 320  --> 7
	private static final int         STASH_Y          = 9;
	
	private static final int         GRID_SIZE        = 28;
	private static final int         GRID_SPACER      = 1;
	private static final int         CURSOR_X         = 12;
	private static final int         CURSOR_Y         = 14;

	private String                   iFileName;
	private D2FileManager            iFileManager;

	private int                      iWeaponSlot      = 1;
	private int						 iSkillSlot = 0;

	private JTextField               iGold;
	private JTextField               iGoldBank;
	private JTextField               iGoldMax;
	private JTextField               iGoldBankMax;
	private JRadioButton             iConnectGold;
	private JRadioButton             iConnectGoldBank;
	private JTextField               iTransferFree;

	private JButton					 iGoldTransferBtns[];
	private JTabbedPane lTabs = new JTabbedPane();
	
	private JPopupMenu rightClickItem;
	private MouseEvent rightClickEvent;

	public D2ViewSharedStash(D2FileManager pMainFrame, String pFileName)
	{
		super(pFileName, false, true, false, true);

		addInternalFrameListener(new InternalFrameAdapter()
		{
			public void internalFrameClosing(InternalFrameEvent e)
			{
				iFileManager.saveAll();
				closeView();
			}
		});

		ToolTipManager.sharedInstance().setDismissDelay(40000);
		ToolTipManager.sharedInstance().setInitialDelay(300);
//		ToolTip

		iFileManager = pMainFrame;
		iFileName = pFileName;



		JPanel lCharPanel = new JPanel();
		lCharPanel.setLayout(new BorderLayout());
		iCharPainter = new D2CharPainterPanel();
		lCharPanel.add(iCharPainter, BorderLayout.CENTER);
		lTabs.addTab("D2R Shared Stash", lCharPanel);
		setContentPane(lTabs);
		iCharPainter.build();


		
		// JPanel lCursorPanel = new JPanel();
		// lCursorPanel.setLayout(new BorderLayout());
		// // TODO: is this needed for a shared stash?
		// iCharCursorPainter = new D2CharCursorPainterPanel();
		// lCursorPanel.add(new JLabel("For item viewing, no items can be put or removed from here"), BorderLayout.NORTH);




		ButtonGroup lConnectGroup = new ButtonGroup();

		// TODO: implement bank buttons when saving the shared stash file is working
		// RandallPanel lBankPanel = new RandallPanel();
		// iGold = new JTextField();
		// iGold.setEditable(false);
		// iGoldMax = new JTextField();
		// iGoldMax.setEditable(false);
		// iConnectGold = new JRadioButton();
		// lConnectGroup.add(iConnectGold);

		// iGoldBank = new JTextField();
		// iGoldBank.setEditable(false);
		// iGoldBankMax = new JTextField();
		// iGoldBankMax.setEditable(false);
		// iConnectGoldBank = new JRadioButton();
		// lConnectGroup.add(iConnectGoldBank);
		// iConnectGoldBank.setSelected(true);

		// lBankPanel.addToPanel(new JLabel("Gold: "), 0, 0, 1, RandallPanel.NONE);
		// lBankPanel.addToPanel(iConnectGold, 1, 0, 1, RandallPanel.NONE);
		// lBankPanel.addToPanel(iGold, 2, 0, 1, RandallPanel.HORIZONTAL);
		// lBankPanel.addToPanel(iGoldMax, 3, 0, 1, RandallPanel.HORIZONTAL);
		// lBankPanel.addToPanel(new JLabel("Gold Stash: "), 0, 1, 1, RandallPanel.NONE);
		// lBankPanel.addToPanel(iConnectGoldBank, 1, 1, 1, RandallPanel.NONE);
		// lBankPanel.addToPanel(iGoldBank, 2, 1, 1, RandallPanel.HORIZONTAL);
		// lBankPanel.addToPanel(iGoldBankMax, 3, 1, 1, RandallPanel.HORIZONTAL);

		// RandallPanel lTransferPanel = new RandallPanel(true);
		// lTransferPanel.setBorder("Transfer");

		// iGoldTransferBtns = new JButton[8];

		// iGoldTransferBtns[0] = new JButton("to char");
		// iGoldTransferBtns[0].addActionListener(new ActionListener()
		// {
		// 	public void actionPerformed(ActionEvent pEvent)
		// 	{
		// 		transferToChar(10000);
		// 	}
		// });
		// JTextField lField10000 = new JTextField("10.000");
		// lField10000.setEditable(false);
		// iGoldTransferBtns[1] = new JButton("from char");
		// iGoldTransferBtns[1].addActionListener(new ActionListener()
		// {
		// 	public void actionPerformed(ActionEvent pEvent)
		// 	{
		// 		transferFromChar(10000);
		// 	}
		// });

		// iGoldTransferBtns[2] = new JButton("to char");
		// iGoldTransferBtns[2].addActionListener(new ActionListener()
		// {
		// 	public void actionPerformed(ActionEvent pEvent)
		// 	{
		// 		transferToChar(100000);
		// 	}
		// });
		// JTextField lField100000 = new JTextField("100.000");
		// lField100000.setEditable(false);
		// iGoldTransferBtns[3] = new JButton("from char");
		// iGoldTransferBtns[3].addActionListener(new ActionListener()
		// {
		// 	public void actionPerformed(ActionEvent pEvent)
		// 	{
		// 		transferFromChar(100000);
		// 	}
		// });

		// iGoldTransferBtns[4] = new JButton("to char");
		// iGoldTransferBtns[4].addActionListener(new ActionListener()
		// {
		// 	public void actionPerformed(ActionEvent pEvent)
		// 	{
		// 		transferToChar(1000000);
		// 	}
		// });
		// JTextField lField1000000 = new JTextField("1.000.000");
		// lField1000000.setEditable(false);
		// iGoldTransferBtns[5] = new JButton("from char");
		// iGoldTransferBtns[5].addActionListener(new ActionListener()
		// {
		// 	public void actionPerformed(ActionEvent pEvent)
		// 	{
		// 		transferFromChar(1000000);
		// 	}
		// });

		// iGoldTransferBtns[6] = new JButton("to char");
		// iGoldTransferBtns[6].addActionListener(new ActionListener()
		// {
		// 	public void actionPerformed(ActionEvent pEvent)
		// 	{
		// 		transferToChar(getTransferFree());
		// 	}
		// });
		// iTransferFree = new JTextField("10000");
		// iGoldTransferBtns[7] = new JButton("from char");
		// iGoldTransferBtns[7].addActionListener(new ActionListener()
		// {
		// 	public void actionPerformed(ActionEvent pEvent)
		// 	{
		// 		transferFromChar(getTransferFree());
		// 	}
		// });

		// lTransferPanel.addToPanel(iGoldTransferBtns[0], 0, 0, 1, RandallPanel.NONE);
		// lTransferPanel.addToPanel(lField10000, 1, 0, 1, RandallPanel.HORIZONTAL);
		// lTransferPanel.addToPanel(iGoldTransferBtns[1], 2, 0, 1, RandallPanel.NONE);

		// lTransferPanel.addToPanel(iGoldTransferBtns[2], 0, 1, 1, RandallPanel.NONE);
		// lTransferPanel.addToPanel(lField100000, 1, 1, 1, RandallPanel.HORIZONTAL);
		// lTransferPanel.addToPanel(iGoldTransferBtns[3], 2, 1, 1, RandallPanel.NONE);

		// lTransferPanel.addToPanel(iGoldTransferBtns[4], 0, 2, 1, RandallPanel.NONE);
		// lTransferPanel.addToPanel(lField1000000, 1, 2, 1, RandallPanel.HORIZONTAL);
		// lTransferPanel.addToPanel(iGoldTransferBtns[5], 2, 2, 1, RandallPanel.NONE);

		// lTransferPanel.addToPanel(iGoldTransferBtns[6], 0, 3, 1, RandallPanel.NONE);
		// lTransferPanel.addToPanel(iTransferFree, 1, 3, 1, RandallPanel.HORIZONTAL);
		// lTransferPanel.addToPanel(iGoldTransferBtns[7], 2, 3, 1, RandallPanel.NONE);

		// lBankPanel.addToPanel(lTransferPanel, 0, 10, 3, RandallPanel.HORIZONTAL);

		// lBankPanel.finishDefaultPanel();
		// lTabs.addTab("Bank", lBankPanel);
		

		lTabs.addMouseListener(new MyMouse());

		iMessage = new JTextArea();
		JScrollPane lScroll = new JScrollPane(iMessage);
		RandallPanel lMessagePanel = new RandallPanel();
		JButton lConnect = new JButton("Connect");
		lConnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				connect();
			}
		});
		JButton lDisconnect = new JButton("Disconnect");
		lDisconnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				disconnect(null);
			}
		});

		lMessagePanel.addToPanel(lConnect, 0, 0, 1, RandallPanel.HORIZONTAL);
		lMessagePanel.addToPanel(lDisconnect, 1, 0, 1, RandallPanel.HORIZONTAL);
		lMessagePanel.addToPanel(lScroll, 0, 1, 2, RandallPanel.BOTH);
		lTabs.addTab("Messages", lMessagePanel);
		iMessage.setText("Nothing done, disconnected");

		// TODO: implement item moving, deleting when saving the shared stash is working
		// JMenuItem item;
		// JMenuItem item2;
		// JMenuItem item3;
		// rightClickItem = new JPopupMenu();
		// rightClickItem.add(item = new JMenuItem("Delete?"));
		// rightClickItem.add(item2 = new JMenuItem("View Item"));
		// item3 = new JMenuItem("Extended Item Info");
		// rightClickItem.add(new JPopupMenu.Separator());
		// rightClickItem.add("Cancel");

		// item.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent event) {      

		// 		if(event.getActionCommand().equals("Delete?")){



		// 			D2ItemPanel lItemPanel = new D2ItemPanel(rightClickEvent, true, false, false);
		// 			if (lItemPanel.getPanel() != -1)
		// 			{
		// 				// if there is an item to grab, grab it
		// 				if (lItemPanel.isItem())
		// 				{
		// 					D2Item lTemp = lItemPanel.getItem();

		// 					int check = JOptionPane.showConfirmDialog(null, "Delete " + lTemp.getName() + "?");
		// 					if(check == 0){
		// 						iSharedStash.unmarkCharGrid(lTemp);
		// 						iSharedStash.removeCharItem(lItemPanel.getItemIndex());  // iStashIdx needed
		// 						setCursorDropItem();
		// 						// if(lTemp.statModding()){
		// 						// 	iSharedStash.updateCharStats("P", lTemp);
		// 						// 	paintCharStats();
		// 						// }
		// 					}
		// 				}
		// 			}
		// 		}

		// 	}
		// });

		// item2.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent event) {      

		// 		if(event.getActionCommand().equals("View Item")){
		// 			D2ItemPanel lItemPanel = new D2ItemPanel(rightClickEvent, true, false, false);
		// 			if (lItemPanel.getPanel() != -1)
		// 			{
		// 				// if there is an item to grab, grab it
		// 				if (lItemPanel.isItem())
		// 				{
		// 					D2Item lTemp = lItemPanel.getItem();
		// 					JFrame itemPanel = new JFrame();
		// 					JEditorPane report = new JEditorPane();
		// 					report.setContentType("text/html");

		// 					JScrollPane SP = new JScrollPane(report);
		// 					report.setBackground(Color.black);
		// 					//HTMLEditorKit htmlEditor = new HTMLEditorKit();
		// 					//report.setEditorKit(htmlEditor);
		// 					report.setForeground(Color.white);
		// 					report.setText("<html><font size=3 face=Dialog>"+lTemp.itemDumpHtml(true) + "</font></html>");
		// 					report.setCaretPosition(0);
		// 					itemPanel.add( SP);

		// 					itemPanel.setLocation((rightClickEvent.getComponent().getLocationOnScreen().x + rightClickEvent.getX()), (rightClickEvent.getComponent().getLocationOnScreen().y + rightClickEvent.getY()));
		// 					itemPanel.setSize(200,400);
		// 					itemPanel.setVisible(true);
		// 					itemPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// 				}
		// 			}


		// 		}

		// 	}
		// });

// 		item3.addActionListener(new ActionListener() {
// 			public void actionPerformed(ActionEvent event) {      

// 				if(event.getActionCommand().equals("Extended Item Info")){
// //					D2ItemPanel lItemPanel = new D2ItemPanel(rightClickEvent, true, false, false);
// //					if (lItemPanel.getPanel() != -1)
// //					{
// //					// if there is an item to grab, grab it
// //					if (lItemPanel.isItem())
// //					{
// //					D2Item lTemp = lItemPanel.getItem();
// //					Box v1 = Box.createVerticalBox();

// //					Box h1 = Box.createHorizontalBox();

// //					JTextPane report = new JTextPane();
// //					JScrollPane SP = new JScrollPane(report);
// //					float[] bGrey = new float[3];
// //					bGrey = Color.RGBtoHSB(237, 237, 237, bGrey);
// //					report.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
// //					report.setForeground(Color.black);
// //					report.setText(lTemp.itemDumpHtml(false));
// //					report.setCaretPosition(0);

// //					try{
// //					if(lTemp.isUnique() || lTemp.isSet() || lTemp.isRuneWord()){
// //					ArrayList perfect = lTemp.getPerfectString();
// //					JTextPane reportBest = new JTextPane();
// //					JScrollPane SPBest = new JScrollPane(reportBest);
// //					reportBest.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
// //					reportBest.setForeground(Color.black);
// //					String bestStr = "BEST:\n\n";
// //					String[] perfDef = null;
// //					if(lTemp.isTypeArmor()){
// //					perfDef = lTemp.getPerfectDef(perfect);
// //					bestStr = bestStr  + "Defense: " + perfDef[0] + "\n";
// //					}else if(lTemp.isTypeWeapon()){
// //					bestStr = bestStr  + lTemp.getPerfectDmg(perfect)[1];
// //					}

// //					for(int x = 0;x<perfect.size();x=x+1){
// //					bestStr = bestStr + (((D2ItemProperty[])perfect.get(x))[1].getValue()) + "\n";
// //					}

// //					reportBest.setText(bestStr);
// //					reportBest.setCaretPosition(0);

// //					JTextPane reportWorst = new JTextPane();
// //					JScrollPane SPWorst = new JScrollPane(reportWorst);
// //					reportWorst.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
// //					reportWorst.setForeground(Color.black);
// //					String WorstStr = "WORST:\n\n";

// //					if(lTemp.isTypeArmor()){
// //					WorstStr = WorstStr + "Defense: " + perfDef[1] + "\n";
// //					}else if(lTemp.isTypeWeapon()){
// //					WorstStr = WorstStr  + lTemp.getPerfectDmg(perfect)[0];
// //					}

// //					for(int x = 0;x<perfect.size();x=x+1){
// //					WorstStr = WorstStr + (((D2ItemProperty[])perfect.get(x))[0].getValue()) + "\n";
// //					}
// //					reportWorst.setText(WorstStr);
// //					reportWorst.setCaretPosition(0);


// //					h1.add(SPWorst);
// //					h1.add( SP);
// //					h1.add(SPBest);

// //					}
// //					if(lTemp.isRare()){

// //					String rareRealName = lTemp.getPreSuf();
// //					JTextPane rareName = new JTextPane();
// //					JScrollPane scP = new JScrollPane(rareName);
// //					rareName.setText("Your rare is a: " + rareRealName);
// //					rareName.setCaretPosition(0);
// //					rareName.setEditable(false);
// //					rareName.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
// //					v1.add(scP);
// //					h1.add( SP);
// //					}
// //					JFrame basePanel = new JFrame();

// //					basePanel.setLocation((rightClickEvent.getComponent().getLocationOnScreen().x + rightClickEvent.getX()), (rightClickEvent.getComponent().getLocationOnScreen().y + rightClickEvent.getY()));
// //					basePanel.setSize(800,300);
// //					basePanel.setVisible(true);
// //					basePanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



// //					v1.add(h1);



// //					basePanel.getContentPane().add(v1);
// //					}
// //					catch(Exception e){
// //					e.printStackTrace();
// //					System.err.println("Perfect strings suck.");
// //					}

// //					}
// //					}


// 				}

// 			}
// 		});

		pack();
		setVisible(true);

		connect();
		itemListChanged();

		//        setModified(true);
	}

	
	public void connect()
	{
		if ( iSharedStash != null )
		{
			return;
		}
		try
		{
			iSharedStash = (D2SharedStash) iFileManager.addItemList(iFileName, this);

			// TODO set gold for 3 tabs of the shared stash file
			// iGold.setText(Integer.toString(iSharedStash.getGold()));
			// iGoldMax.setText(Integer.toString(iSharedStash.getGoldMax()));
			// iGoldBank.setText(Integer.toString(iSharedStash.getGoldBank()));
			// iGoldBankMax.setText(Integer.toString(iSharedStash.getGoldBankMax()));

			// for ( int i = 0 ; i < iGoldTransferBtns.length ; i++ )
			// {
			// 	iGoldTransferBtns[i].setEnabled(true);
			// }

			itemListChanged();
			iMessage.setText("Shared Stash loaded");
		}
		catch ( Exception pEx )
		{
			disconnect(pEx);
			pEx.printStackTrace();
		}
	}

	public void disconnect(Exception pEx)
	{
		if ( iSharedStash != null )
		{
			iFileManager.removeItemList(iFileName, this);
		}

		iSharedStash = null;

		String lText = "Character disconnected";

		if ( pEx != null )
		{
			lText += "\n";
			StackTraceElement trace[] = pEx.getStackTrace();
			for (int i = 0; i < trace.length; i++)
			{
				lText += "\tat " + trace[i] + "\n";
			}
		}
		iMessage.setText(lText);

		// iGold.setText("");
		// iGoldMax.setText("");
		// iGoldBank.setText("");
		// iGoldBankMax.setText("");

		// for ( int i = 0 ; i < iGoldTransferBtns.length ; i++ )
		// {
		// 	iGoldTransferBtns[i].setEnabled(false);
		// }

		itemListChanged();
//		System.gc();
	}

	// public void transferToChar(int pGoldTransfer)
	// {
	// 	if (pGoldTransfer > 0)
	// 	{
	// 		try
	// 		{
	// 			// to char
	// 			int lBank = iFileManager.getProject().getBankValue();
	// 			if (pGoldTransfer > lBank)
	// 			{
	// 				// don't allow more as the bank has
	// 				pGoldTransfer = lBank;
	// 			}
	// 			int lGoldChar;
	// 			int lGoldMax;
	// 			if (iConnectGold.isSelected())
	// 			{
	// 				lGoldChar = iSharedStash.getGold();
	// 				lGoldMax = iSharedStash.getGoldMax();
	// 			}
	// 			else
	// 			{
	// 				lGoldChar = iSharedStash.getGoldBank();
	// 				lGoldMax = iSharedStash.getGoldBankMax();
	// 			}
	// 			// char limit
	// 			if (lGoldChar + pGoldTransfer > lGoldMax)
	// 			{
	// 				pGoldTransfer = lGoldMax - lGoldChar;
	// 			}
	// 			int lNewGold = lGoldChar + pGoldTransfer;
	// 			int lNewGoldBank = lBank - pGoldTransfer;
	// 			if (iConnectGold.isSelected())
	// 			{
	// 				iSharedStash.setGold(lNewGold);
	// 				iGold.setText(Integer.toString(iSharedStash.getGold()));
	// 			}
	// 			else
	// 			{
	// 				iSharedStash.setGoldBank(lNewGold);
	// 				iGoldBank.setText(Integer.toString(iSharedStash.getGoldBank()));
	// 			}
	// 			iFileManager.getProject().setBankValue(lNewGoldBank);
	// 		}
	// 		catch (Exception pEx)
	// 		{
	// 			D2FileManager.displayErrorDialog(pEx);
	// 		}
	// 	}

	// }

	// public void transferFromChar(int pGoldTransfer)
	// {
	// 	if (pGoldTransfer > 0)
	// 	{
	// 		try
	// 		{
	// 			int lGoldChar;
	// 			if (iConnectGold.isSelected())
	// 			{
	// 				lGoldChar = iSharedStash.getGold();
	// 			}
	// 			else
	// 			{
	// 				lGoldChar = iSharedStash.getGoldBank();
	// 			}

	// 			if (pGoldTransfer > lGoldChar)
	// 			{
	// 				// don't allow more as the char has
	// 				pGoldTransfer = lGoldChar;
	// 			}

	// 			// from char
	// 			int lBank = iFileManager.getProject().getBankValue();

	// 			int lNewGold = lGoldChar - pGoldTransfer;
	// 			int lNewGoldBank = lBank + pGoldTransfer;

	// 			if (iConnectGold.isSelected())
	// 			{
	// 				iSharedStash.setGold(lNewGold);
	// 				iGold.setText(Integer.toString(iSharedStash.getGold()));
	// 			}
	// 			else
	// 			{
	// 				iSharedStash.setGoldBank(lNewGold);
	// 				iGoldBank.setText(Integer.toString(iSharedStash.getGoldBank()));
	// 			}
	// 			iFileManager.getProject().setBankValue(lNewGoldBank);
	// 		}
	// 		catch (Exception pEx)
	// 		{
	// 			D2FileManager.displayErrorDialog(pEx);
	// 		}
	// 	}

	// }

	// public int getTransferFree()
	// {
	// 	try
	// 	{
	// 		return Integer.parseInt(iTransferFree.getText());
	// 	}
	// 	catch (NumberFormatException e)
	// 	{
	// 		return 0;
	// 	}
	// }

	public boolean isHC()
	{
		return iSharedStash.isHC();
	}

	public boolean isSC()
	{
		return iSharedStash.isSC();
	}

	public String getFileName()
	{
		return iFileName;
	}

	public boolean isModified()
	{
		return iSharedStash.isModified();
	}

	public D2ItemList getItemLists()
	{
		return iSharedStash;
	}

	public void closeView()
	{
		disconnect(null);
		iFileManager.removeFromOpenWindows(this);
	}

	public void itemListChanged()
	{

		String lTitle;
		if ( iSharedStash == null )
		{
			lTitle = "Disconnected";
		}
		else
		{
			lTitle = "D2R Shared Stash";
			if (iSharedStash == null)
			{
				lTitle += " (Error Reading File)";
			}
			else
			{
				lTitle += (( iSharedStash.isModified() ) ? "*" : "");
				if (iSharedStash.isSC())
				{
					lTitle += " (SC)";
				}
				else if (iSharedStash.isHC())
				{
					lTitle += " (HC)";
				}
				lTitle += iSharedStash.getTitleString();
			}
		}
		setTitle(lTitle);
		iCharPainter.build();  // TODO go through build shared stash function 
		
		// iCharCursorPainter.build(); // TODO: is this needed for a shared stash?
		
	}

	public void setCursorPickupItem()
	{
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	public void setCursorDropItem()
	{
		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	public void setCursorNormal()
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	class MyMouse extends MouseAdapter{

		public void mouseClicked(MouseEvent e){

			if(lTabs.getSelectedIndex() == 6){
				dumpChar();
			}

		}

	}

	class D2ItemPanel
	{
		private boolean iIsChar;
		private boolean iIsCursor;
		private int     iPanel;
		private int     iRow;
		private int     iCol;
		private boolean iIsCorpse;

		public D2ItemPanel(MouseEvent pEvent, boolean pIsChar, boolean pIsCursor, boolean pIsCorpse)
		{
			iIsChar = pIsChar;
			iIsCursor = pIsCursor;
			iIsCorpse = pIsCorpse;
			int x = pEvent.getX();
			int y = pEvent.getY();

			iPanel = getMousePanel(x, y);
			// pass info about stash tab 1, 2 or 3 to setRowCol
			int stashIdx;
			if (pEvent.getX() < 315) {
				stashIdx = 0;
			}else if (pEvent.getX() < 613) {
				stashIdx = 1;
			}else{
				stashIdx = 2;
			}
			if (pEvent.getX() > 298 && pEvent.getX() < 311) {  // ***NOTE: hard-coded stash tab width here.
				iPanel = -1;
			}else if (pEvent.getX() > 598 && pEvent.getX() < 615) {  // expand spacer between tabs to avoid exceptions with mouseMove
				iPanel = -1;
			}
			setRowCol(x, y, stashIdx);
		}

		public int getPanel()
		{
			return iPanel;
		}

		public int getRow()
		{
			return iRow;
		}

		public int getColumn()
		{
			return iCol;
		}

		public boolean isItem()
		{
			// TODO: verify this is always false for a shared stash. there can't be a cursor item like there is for a character
			if ( iIsCursor )
			{
				return ( iSharedStash.getCursorItem() != null );
			}
			// if (iIsChar)
			// {
			// 	return iSharedStash.checkCharPanel(iPanel, iRow, iCol, null);
			// }
			// TODO: verify this will not be needed for a shared stash. there is no merc and no corpse
			// if(iIsCorpse)
			// {
			// 	return iSharedStash.checkCorpsePanel(iPanel,iRow, iCol, null);
			// }
			// return iSharedStash.checkMercPanel(iPanel, iRow, iCol, null);

			return iSharedStash.checkCharPanel(iPanel, iRow, iCol, null);
		}

		public int getItemIndex(int iStashIdx)
		{
			// if (iIsChar)
			// {
			// 	return iSharedStash.getCharItemIndex(iPanel, iRow, iCol);
			// }
			// if (iIsCorpse)
			// {
			// 	return iSharedStash.getCorpseItemIndex(iPanel, iRow, iCol);
			// }
			// return iSharedStash.getMercItemIndex(iPanel, iRow, iCol);
			// TODO: iStashIndex for stash tab 1, 2, or 3
			return iSharedStash.getCharItemIndex(iPanel, iRow, iCol, iStashIdx);
		}

		public D2Item getItem(int stashIdx)
		{
			// TODO: verify item cannot be a cursor item for a shared stash
			if ( iIsCursor )
			{
				return iSharedStash.getCursorItem();
			}

			// if ( iIsChar )
			// {
			// 	return iSharedStash.getCharItem(iSharedStash.getCharItemIndex(iPanel, iRow, iCol));
			// }
			
			// if(iIsCorpse){
			// 	return iSharedStash.getCorpseItem(iSharedStash.getCorpseItemIndex(iPanel, iRow, iCol));
			// }
			// return iSharedStash.getMercItem(iSharedStash.getMercItemIndex(iPanel, iRow, iCol));
			return iSharedStash.getCharItem(iSharedStash.getCharItemIndex(iPanel, iRow, iCol, stashIdx), stashIdx);  // iSharedStash.() , iStashIdx
			//                                                            add 4th param to getCharItemIndex
		}

		// calculate which panel (stash, inventory, equipment slot, etc)
		// the coordinates x and y lie in
		// belt_grid = panel 2
		// offset body positions by 10
		// return -1 on failure
		private int getMousePanel(int x, int y)
		{
			if (iIsCursor)
			{
				if (iIsChar && x >= CURSOR_X && x < CURSOR_X + 2 * GRID_SIZE + 2 * GRID_SPACER && y >= CURSOR_Y && y < CURSOR_Y + 4 * GRID_SIZE + 4 * GRID_SPACER)
				{
					return D2Character.BODY_CURSOR;
				}
				return -1;
			} // x > 7pixels  and x < (7px + 10slots ) * 3 for 3 stash tabs
			if (iIsChar && x >= STASH_X && x < NUM_SHARED_TABS*(STASH_X + 10 * GRID_SIZE + 10 * GRID_SPACER) && y >= STASH_Y && y < STASH_Y + 10 * GRID_SIZE + 10 * GRID_SPACER)
			{
				return D2Character.BODY_STASH_CONTENT;
			}
			
			return -1;
		}

		// get row/col
		private void setRowCol(int x, int y, int stashIdx)
		{
			//            int row, col;
			//            int temp_item = -1;
			// non-equppied: calculate row and column
			// then fetch the item if that item space
			// has an item on it
			if (iPanel < 10)
			{
				switch (iPanel)
				{
				case 1: // inventory
					// iRow = (x - INV_X) / (GRID_SIZE + GRID_SPACER);
					// iCol = (y - INV_Y) / (GRID_SIZE + GRID_SPACER);
					//                    if (iChar.check_panel(panel, row, col))
					//                    {
					//                        temp_item = iChar.get_item_index(panel, row, col);
					//                    }
					break;
				case 2: // belted
					// iRow = (x - BELT_GRID_X) / (GRID_SIZE + GRID_SPACER);
					// iCol = 3 - ((y - BELT_GRID_Y) / (GRID_SIZE + GRID_SPACER));
					//                    if (iChar.check_panel(panel, row, col))
					//                    {
					//                        temp_item = iChar.get_item_index(panel, col, row);
					//                    }
					break;
				case 4: // cube
					// iRow = (x - CUBE_X) / (GRID_SIZE + GRID_SPACER);
					// iCol = (y - CUBE_Y) / (GRID_SIZE + GRID_SPACER);
					//                    if (iChar.check_panel(panel, row, col))
					//                    {
					//                        temp_item = iChar.get_item_index(panel, row, col);
					//                    }
					break;
				case 5: // stash
					// account for extra space between stash tabs 1, 2 and 3.  304px is a stash tab. padding between tab1 and 2 is ~12px
					iRow = (x - STASH_X - stashIdx*(2*STASH_X)) / (GRID_SIZE + GRID_SPACER);
					iCol = (y - STASH_Y) / (GRID_SIZE + GRID_SPACER);
					//                    if (iChar.check_panel(panel, row, col))
					//                    {
					//                        temp_item = iChar.get_item_index(panel, row, col);
					//                    }
					break;
				}
			}
			// equipped
			// row and column are irrelevant, so they can be zero
			else
			{   // sharedStash items can only be in 'stash' -> case 5
				// iRow = 0;
				// iCol = 0;
				//                if (iChar.check_panel(panel, 0, 0))
				//                {
				//                    temp_item = iChar.get_item_index(panel, 0, 0);
				//                }
			}
			//            return temp_item;
		}

	}

	class D2CharPainterPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2159433491696507246L;
		private Image iBackground;

		public D2CharPainterPanel()
		{
			setSize(BG_WIDTH, BG_HEIGHT);
			Dimension lSize = new Dimension(BG_WIDTH, BG_HEIGHT);
			setPreferredSize(lSize);

			addMouseListener(new MouseAdapter()
			{


				public void mouseReleased(MouseEvent pEvent)
				{
					if ( iSharedStash == null )
					{
						return;
					}
					//                    System.err.println("Mouse Clicked: " + pEvent.getX() + ",
					// " + pEvent.getY() );
					// TODO: implement move, delete item when saving the shared stash is working
// 					if (pEvent.getButton() == MouseEvent.BUTTON1 /*
// 					 * &&
// 					 * pEvent.getClickCount() ==
// 					 * 1
// 					 */)
// 					{
// 						int lX = pEvent.getX();
// 						int lY = pEvent.getY();
						
// 						// determine where the mouse click is.
//                         // TODO:      need to determine which stash is clicked based on x position (greater than multiple of stash width)
// 						D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, true, false, false);
// 						if (lItemPanel.getPanel() != -1)
// 						{
// 							// if there is an item to grab, grab it
// 							if (lItemPanel.isItem())
// 							{
// 								D2Item lTemp = lItemPanel.getItem(0); //  TODO: logic for stash1, 2, or 3

// 								/**Code to remove potions when belt is removed!
// 								 * Thanks to Krikke.
// 								 */
// 								//System.out.println("isEquipped: " + lTemp.isEquipped() + " isABelt: " + lTemp.isABelt()); 
								
// 								iSharedStash.unmarkCharGrid(lTemp);
// 								iSharedStash.removeCharItem(lItemPanel.getItemIndex());  // iStashIdx needed
// 								D2ViewClipboard.addItem(lTemp);
// 								setCursorDropItem();
// 								// if(lTemp.statModding()){
// 								// 	iSharedStash.updateCharStats("P", lTemp);
// 								// }

// //								// redraw
// //								build();
// //								repaint();
// 							}
// 							else if (D2ViewClipboard.getItem() != null)
// 							{
// 								//	                    	System.err.println("Drop item");
// 								// since there is an item on the mouse, try to
// 								// drop it here

// 								D2Item lDropItem = D2ViewClipboard.getItem();
// 								//		                        int lDropWidth = lDropItem.get_width();
// 								//		                        int lDropHeight = lDropItem.get_height();
// 								//	                        int r = 0, c = 0;
// 								boolean drop = false;
// 								// non-equipped items, handle differently
// 								// because they require a row and column
// 								if (lItemPanel.getPanel() < 10)
// 								{
// 									// calculate row and column for the given
// 									// panel
// 									// with mouse coords x and y (split into an
// 									// int for
// 									// convenience)
// 									//	                            int temp = find_grid(panel, x, y);
// 									//	                            r = temp >> 16;
// 									//	                            c = temp & 0xffff;
// 									//                            r -= (D2MouseItem.get_mouse_x() /
// 									// GRID_SIZE);
// 									//                            c -= (D2MouseItem.get_mouse_y() /
// 									// GRID_SIZE);
// 									// if that area of the character is empty,
// 									// then update fields of the item and set
// 									// the 'drop' variable to true
// 									if (iSharedStash.checkCharGrid(lItemPanel.getPanel(), lItemPanel.getRow(), lItemPanel.getColumn(), lDropItem))
// 									{
// 										switch (lItemPanel.getPanel())
// 										{
// 										case 2:
// 											lDropItem.set_location((short) 2);
// 											lDropItem.set_body_position((short) 0);
// 											lDropItem.set_col((short) (4 * lItemPanel.getColumn() + lItemPanel.getRow()));
// 											lDropItem.set_row((short) 0);
// 											lDropItem.set_panel((short) 0);
// 											break;
// 										case 1:
// 										case 4:
// 										case 5:
// 											lDropItem.set_location((short) 0);
// 											lDropItem.set_body_position((short) 0);
// 											lDropItem.set_row((short) lItemPanel.getColumn());
// 											lDropItem.set_col((short) lItemPanel.getRow());
// 											lDropItem.set_panel((short) lItemPanel.getPanel());
// 											break;
// 										}
// 										drop = true;
// 									}
// 								}
// 								// equipped items, a bit simpler
// 								// if that equipment slot is empty, update the
// 								// item's fields and set drop to true
// 								// r and c are set to width and height
// 								// for find_corner to deal with variable-size
// 								// objects in the hands
// 								// (note lack of item-type checking)
// 								else
// 								{
// 									if (!iSharedStash.checkCharPanel(lItemPanel.getPanel(), 0, 0, lDropItem))
// 									{
// 										lDropItem.set_location((short) 1);
// 										lDropItem.set_body_position((short) (lItemPanel.getPanel() - 10));
// 										lDropItem.set_col((short) 0);
// 										lDropItem.set_row((short) 0);
// 										lDropItem.set_panel((short) 0);
// 										drop = true;
// 										//	                                r = lDropWidth;
// 										//	                                c = lDropHeight;
// 									}
// 								}
// 								// if the space to set the item is empty
// 								if (drop)
// 								{
// 									iSharedStash.markCharGrid(lDropItem);
// 									// move the item to a new charcter, if
// 									// needed
// 									iSharedStash.addCharItem(D2ViewClipboard.removeItem());

// 									// redraw
// //									build();
// //									repaint();

// 									setCursorPickupItem();
// 									// if(lDropItem.statModding()){
// 									// 	iSharedStash.updateCharStats("D", lDropItem);
// 									// 	paintCharStats();
// 									// }
// 									//my_char.show_grid();
// 								}
// 							}
// 						}
// 					} 
					// TODO:  implement deleting, moving items  when saving shared stash file works
					// else if (pEvent.getButton() == MouseEvent.BUTTON3){
					// 	D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, true, false, false);
					// 	if (lItemPanel.getPanel() != -1)
					// 	{
					// 		if (lItemPanel.isItem())
					// 		{

					// 			rightClickItem.show(D2ViewSharedStash.this, pEvent.getX(), pEvent.getY()+35);
					// 			rightClickEvent = pEvent;
					// 		}
					// 	}
					// }
				}



				public void mouseEntered(MouseEvent e)
				{
					setCursorNormal();
				}

				public void mouseExited(MouseEvent e)
				{
					setCursorNormal();
				}
			});
			addMouseMotionListener(new MouseMotionAdapter()
			{
				public void mouseMoved(MouseEvent pEvent)
				{
					if ( iSharedStash == null )
					{
						return;
					}
					//            	    restoreSubcomponentFocus();
					D2Item lCurrentMouse = null;

					D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, true, false, false);
					if (lItemPanel.getPanel() != -1)
					{
						if (lItemPanel.isItem())
						{	
							int stashIdx;
							if (pEvent.getX() < 315) {
								stashIdx = 0;
							}else if (pEvent.getX() < 615) {  // NOTE:  hard-coded px values.  also see D2ItemPanel
								stashIdx = 1;
							}else{
								stashIdx = 2;
							}
							lCurrentMouse = lItemPanel.getItem(stashIdx); // get x-pos of mouse to set stashIdx
						}

						if (lItemPanel.isItem())
						{
							setCursorPickupItem();
						}
						else
						{
							if (D2ViewClipboard.getItem() == null)
							{
								setCursorNormal();
							}
							else
							{
								D2Item lDropItem = D2ViewClipboard.getItem();
								//	                        int lDropWidth = lDropItem.get_width();
								//	                        int lDropHeight = lDropItem.get_height();

								boolean drop = false;

								if (lItemPanel.getPanel() < 10)
								{
									if (iSharedStash.checkCharGrid(lItemPanel.getPanel(), lItemPanel.getRow(), lItemPanel.getColumn(), lDropItem))
									{
										drop = true;
									}
								}
								else
								{
									if (!iSharedStash.checkCharPanel(lItemPanel.getPanel(), 0, 0, lDropItem))
									{
										drop = true;
									}
								}
								if (drop)
								{
									setCursorDropItem();
								}
								else
								{
									setCursorNormal();
								}
							}
						}
					}
					else
					{
						setCursorNormal();
					}
					if (lCurrentMouse == null)
					{
						D2CharPainterPanel.this.setToolTipText(null);
					}
					else
					{
						D2CharPainterPanel.this.setToolTipText(lCurrentMouse.itemDumpHtml(false));
					}
				}
			});
		}

		// TODO: this is not used for a shared stash
		public void setWeaponSlot(int pWeaponSlot)
		{
			if(iWeaponSlot == pWeaponSlot){
				return;
			}

			iWeaponSlot = pWeaponSlot;
			build();
			//REMOVE ITEMS AND ADD ITEMS

			
		}

		public void build()
		{
			Image lEmptyBackground;
			if (iWeaponSlot == 1)
			{
				lEmptyBackground = D2ImageCache.getImage("background-sharedstash.jpg");  //background_10x10
			}
			else
			{
				lEmptyBackground = D2ImageCache.getImage("background-sharedstash.jpg"); //background2_10x10
			}

			int lWidth = lEmptyBackground.getWidth(D2CharPainterPanel.this);
			int lHeight = lEmptyBackground.getHeight(D2CharPainterPanel.this);

			iBackground = iFileManager.getGraphicsConfiguration().createCompatibleImage(lWidth, lHeight, Transparency.BITMASK);
//			iBackground = new BufferedImage(lWidth, lHeight, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D lGraphics = (Graphics2D) iBackground.getGraphics();

			lGraphics.drawImage(lEmptyBackground, 0, 0, D2CharPainterPanel.this);

			if ( iSharedStash != null )
			{  // ****TODO:   add logic to determine which stash 1, 2 or 3 to draw the item.
				for (int j = 0; j < NUM_SHARED_TABS; j++) {  
					for (int i = 0; i < iSharedStash.getNrItems(j); i++)
					{
						D2Item temp_item = iSharedStash.getCharItem(i, j);
						Image lImage = D2ImageCache.getDC6Image(temp_item);
						int location = temp_item.get_location();
						// on one of the grids
						// these items have varying height and width
						// and a variable position, indexed from the
						// top left
						if (location == 0)
						{
							int panel = temp_item.get_panel();
							int x = temp_item.get_col();
							int y = temp_item.get_row();
	//						int w = temp_item.get_width();
	//						int h = temp_item.get_height();
							switch (panel)
							{
							// in the inventory
							case 1:
								//                    	System.err.println("Item loc 0 - 1 - " +
								// temp_item.get_name() + " - " + temp_item.get_image()
								// );
								// lGraphics.drawImage(lImage, INV_X + x * GRID_SIZE + x * GRID_SPACER, INV_Y + y * GRID_SIZE + y * GRID_SPACER, D2CharPainterPanel.this);
								break;
								// in the cube
							case 4:
								// lGraphics.drawImage(lImage, CUBE_X + x * GRID_SIZE + x * GRID_SPACER, CUBE_Y + y * GRID_SIZE + y * GRID_SPACER, D2CharPainterPanel.this);
								break;
								// in the stash
							case 5:
								lGraphics.drawImage(lImage, STASH_X + 304*j +x * GRID_SIZE + x * GRID_SPACER, STASH_Y + y * GRID_SIZE + y * GRID_SPACER, D2CharPainterPanel.this);
								break;
							}
						}
						// on the belt
						// belt row and col is indexed from the top
						// left, but this displays them from the
						// bottom right (so the 0th row items get
						// placed in the bottom belt row)
						// these items can all be assumed to be 1x1
						else if (location == 2)
						{
							// int x = temp_item.get_col();
							// int y = x / 4;
							// x = x % 4;
							// lGraphics.drawImage(lImage, BELT_GRID_X + x * GRID_SIZE + x * GRID_SPACER, BELT_GRID_Y + (3 - y) * GRID_SIZE + (3 - y) * GRID_SPACER, D2CharPainterPanel.this);
						}
						// on the body
						else
						{
							int body_position = temp_item.get_body_position();
							int w, h, wbias, hbias;
							switch (body_position)
							{
							// head (assume 2x2)
							case 1:
								
								break;
								// neck / amulet (assume 1x1)
							case 2:
								
								break;
							case 3:
								// body (assume 2x3
								
								break;
								// right arm (give the whole 2x4)
								// biases are to center non-2x4 items
							case 4:
							case 11:
								
								break;
								// left arm (give the whole 2x4)
							case 5:
							case 12:
								
								break;
								// left ring (assume 1x1)
							case 6:
								
								break;
								// right ring (assume 1x1)
							case 7:
								
								break;
								// belt (assume 2x1)
							case 8:
								
								break;
							case 9:
								// boots (assume 2x2)
								
								break;
								// gloves (assume 2x2)
							case 10:
								
								break;
							}
						}
					}
				}
                
			}
			repaint();
		}

		public void paint(Graphics pGraphics)
		{
			super.paint(pGraphics);
			Graphics2D lGraphics = (Graphics2D) pGraphics;

			lGraphics.drawImage(iBackground, 0, 0, D2CharPainterPanel.this);
		}
	}

	//TODO: is the char cursorpainter needed for a shared stash?
// 	class D2CharCursorPainterPanel extends JPanel
// 	{
// 		/**
// 		 * 
// 		 */
// 		private static final long serialVersionUID = 3335835168313769724L;
// 		private Image iBackground;

// 		public D2CharCursorPainterPanel()
// 		{
// 			setSize(BG_CURSOR_WIDTH, BG_CURSOR_HEIGHT);
// 			Dimension lSize = new Dimension(BG_CURSOR_WIDTH, BG_CURSOR_HEIGHT);
// 			setPreferredSize(lSize);

// 			addMouseListener(new MouseAdapter()
// 			{
// 				public void mouseReleased(MouseEvent pEvent)
// 				{
// 					if ( iSharedStash == null )
// 					{
// 						return;
// 					}
// 					//                    System.err.println("Mouse Clicked: " + pEvent.getX() + ",
// 					// " + pEvent.getY() );
// 					if (pEvent.getButton() == MouseEvent.BUTTON1 /*
// 					 * &&
// 					 * pEvent.getClickCount() ==
// 					 * 1
// 					 */)
// 					{
// 						// determine where the mouse click is
// 						D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, true, true, false);
// 						if (lItemPanel.getPanel() != -1)
// 						{
// 							// if there is an item to grab, grab it
// 							if (lItemPanel.isItem())
// 							{
// 								D2Item lTemp = iSharedStash.getCursorItem();
// 								iSharedStash.setCursorItem(null);
// 								D2ViewClipboard.addItem(lTemp);
// 								setCursorDropItem();

// 								// redraw
// 								build();
// 								repaint();
// 							}
// 							else if (D2ViewClipboard.getItem() != null)
// 							{
// 								iSharedStash.setCursorItem(D2ViewClipboard.removeItem());
// 								build();
// 								repaint();

// 								setCursorPickupItem();
// 							}
// 						}

// //						// MBR: for now, disable dropping completely,
// //						// it's not working
// //						//	// System.err.println("Drop item");
// //						//		                        // since there is an item on the mouse, try
// //						// to drop it here
// //						//		                    	
// //						//		                        D2Item lDropItem = D2MouseItem.getItem();
// //						//// int lDropWidth = lDropItem.get_width();
// //						//// int lDropHeight = lDropItem.get_height();
// //						//	// int r = 0, c = 0;
// //						//		                        boolean drop = false;
// //						//		                        // non-equipped items, handle differently
// //						//		                        // because they require a row and column
// //						//		                        // equipped items, a bit simpler
// //						//		                        // if that equipment slot is empty, update
// //						// the
// //						//		                        // item's fields and set drop to true
// //						//		                        // r and c are set to width and height
// //						//		                        // for find_corner to deal with variable-size
// //						//		                        // objects in the hands
// //						//		                        // (note lack of item-type checking)
// //						//	                            if
// //						// (!iChar.checkCharPanel(lItemPanel.getPanel(),
// //						// 0, 0, lDropItem))
// //						//	                            {
// //						//	                            	lDropItem.set_location((short) 1);
// //						//	                            	lDropItem.set_body_position((short)
// //						// (lItemPanel.getPanel() - 10));
// //						//	                            	lDropItem.set_col((short) 0);
// //						//	                            	lDropItem.set_row((short) 0);
// //						//	                            	lDropItem.set_panel((short) 0);
// //						//	                                drop = true;
// //						//// r = lDropWidth;
// //						//// c = lDropHeight;
// //						//	                            }
// //						//		                        // if the space to set the item is empty
// //						//		                        if (drop)
// //						//		                        {
// //						//		                            iChar.markCharGrid(lDropItem);
// //						//		                            // move the item to a new charcter, if needed
// //						//	                                iChar.addCharItem(D2MouseItem.removeItem());
// //						//		
// //						//		                            setModified( true );
// //						//		
// //						//		                            // redraw
// //						//		                            build();
// //						//		                            repaint();
// //						//		                            
// //						//		                            setCursorPickupItem();
// //						//		                            //my_char.show_grid();
// //						//		                        }
// //						}
// //						}
// 					}
// 				}

// 				public void mouseEntered(MouseEvent e)
// 				{
// 					setCursorNormal();
// 				}

// 				public void mouseExited(MouseEvent e)
// 				{
// 					setCursorNormal();
// 				}
// 			});
// 			addMouseMotionListener(new MouseMotionAdapter()
// 			{
// 				public void mouseMoved(MouseEvent pEvent)
// 				{
// 					if ( iSharedStash == null )
// 					{
// 						return;
// 					}
// 					//            	    restoreSubcomponentFocus();
// 					D2Item lCurrentMouse = null;

// 					D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, true, true, false);
// 					if (lItemPanel.getPanel() != -1)
// 					{
// 						if (lItemPanel.isItem())
// 						{
// 							lCurrentMouse = lItemPanel.getItem();
// 						}

// 						if (lItemPanel.isItem())
// 						{
// 							setCursorPickupItem();
// 						}
// 						else
// 						{
// 							if (D2ViewClipboard.getItem() == null)
// 							{
// 								setCursorNormal();
// 							}
// 							else
// 							{
// //								setCursorNormal();
// 								setCursorDropItem();

// //								MBR: for now, disable dropping completely
// //								D2Item lDropItem = D2ViewClipboard.getItem();
// //								// int lDropWidth = lDropItem.get_width();
// //								// int lDropHeight = lDropItem.get_height();

// //								boolean drop = false;

// //								if (!iChar.checkCharPanel(lItemPanel.getPanel(), 0, 0, lDropItem))
// //								{
// //								drop = true;
// //								}
// //								if (drop)
// //								{
// //								setCursorDropItem();
// //								}
// //								else
// //								{
// //								setCursorNormal();
// //								}
// 							}
// 						}
// 					}
// 					else
// 					{
// 						setCursorNormal();
// 					}
// 					if (lCurrentMouse == null)
// 					{
// 						D2CharCursorPainterPanel.this.setToolTipText(null);
// 					}
// 					else
// 					{
// 						D2CharCursorPainterPanel.this.setToolTipText(lCurrentMouse.itemDumpHtml(false));
// 					}
// 				}
// 			});
// 		}

// 		public void build()
// 		{
// 			Image lEmptyBackground = D2ImageCache.getImage("cursor.jpg");

// 			int lWidth = lEmptyBackground.getWidth(D2CharCursorPainterPanel.this);
// 			int lHeight = lEmptyBackground.getHeight(D2CharCursorPainterPanel.this);

// 			iBackground = iFileManager.getGraphicsConfiguration().createCompatibleImage(lWidth, lHeight, Transparency.BITMASK);

// 			Graphics2D lGraphics = (Graphics2D) iBackground.getGraphics();

// 			lGraphics.drawImage(lEmptyBackground, 0, 0, D2CharCursorPainterPanel.this);

// 			if ( iSharedStash != null )
// 			{
// 				D2Item lCursorItem = iSharedStash.getCursorItem();
// 				if ( lCursorItem != null )
// 				{
// 					Image lImage = D2ImageCache.getDC6Image(lCursorItem);
// 					lGraphics.drawImage(lImage, CURSOR_X, CURSOR_Y, D2CharCursorPainterPanel.this);
// 				}
// 			}
// 			repaint();
// 		}

// 		public void paint(Graphics pGraphics)
// 		{
// 			super.paint(pGraphics);
// 			Graphics2D lGraphics = (Graphics2D) pGraphics;

// 			lGraphics.drawImage(iBackground, 0, 0, D2CharCursorPainterPanel.this);
// 		}
// 	}

	public void dumpChar() {
        // TODO:  is this needed for shared stashes?

		String iChaString = iSharedStash.fullDumpStr().replaceAll("<BR>", "\n");
		// lDump.setText(iChaString);
		// lDump.setCaretPosition(0);
		// lDump.validate();
	}

	public D2SharedStash getChar(){
		return iSharedStash;
	}

	// public void putOnCharacter(int areaCode, ArrayList dropList){

	// 	iSharedStash.ignoreItemListEvents();
	// 	int dPanel = 0;
	// 	int rMax = 0;
	// 	int cMax = 0;
	// 	switch(areaCode){
	// 	case 0:
	// 		//stash
	// 		dPanel = 5;
	// 		rMax = D2Character.STASHSIZEY;
	// 		cMax = D2Character.STASHSIZEX;
	// 		break;
	// 	case 1:
	// 		//inv
	// 		dPanel = 1;
	// 		rMax = D2Character.INVSIZEY;
	// 		cMax = D2Character.INVSIZEX;
	// 		break;
	// 	case 2:
	// 		//cube
	// 		dPanel = 4;
	// 		rMax = D2Character.CUBESIZEY;
	// 		cMax = D2Character.CUBESIZEX;
	// 		break;
	// 	}
	// 	try{
	// 		for(int z = dropList.size()-1; z> -1;z--){
	// 			D2Item lDropItem = (D2Item) dropList.get(z);
	// 			for(int x = 0;x<rMax;x++){
	// 				for(int y = 0;y<cMax;y++){
	// 					if(iSharedStash.checkCharGrid(dPanel, y, x, lDropItem)){
	// 						lDropItem.set_panel((short) dPanel);
	// 						lDropItem.set_location((short) 0);
	// 						lDropItem.set_body_position((short) 0);
	// 						lDropItem.set_row((short) x);
	// 						lDropItem.set_col((short) y);
	// 						iSharedStash.markCharGrid(lDropItem);
	// 						D2ViewClipboard.removeItem(lDropItem);
	// 						iSharedStash.addCharItem(lDropItem);
	// 						iSharedStash.equipItem(lDropItem);
	// 						// paintCharStats();
	// 						x = rMax;
	// 						y = cMax;
	// 					}
	// 				}
	// 			}
	// 		}
	// 	}finally{
	// 		iSharedStash.listenItemListEvents();
	// 		iSharedStash.fireD2ItemListEvent();	
	// 	}
	// }

}