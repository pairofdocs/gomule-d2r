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
public class D2ViewChar extends JInternalFrame implements D2ItemContainer, D2ItemListListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7350581523641897831L;
	private D2CharPainterPanel       iCharPainter;
	private D2MercPainterPanel       iMercPainter;
	private D2CharCursorPainterPanel iCharCursorPainter;
	private D2Character         	 iCharacter;

	private JTextArea				 iMessage;
	private JTextArea				 CJT = new JTextArea();
	private JTextArea				 MJT = new JTextArea();

	private static final int         BG_WIDTH         = 626; //550;  // TODO: update to 626
	private static final int         BG_HEIGHT        = 435; //383;  // TODO: update to 457
	private static final int         BG_MERC_WIDTH    = 323;
	private static final int         BG_MERC_HEIGHT   = 187;
	private static final int         BG_CURSOR_WIDTH  = 78;
	private static final int         BG_CURSOR_HEIGHT = 135;

	// change only the X positions for stash-left + inv-right.     (add ~300 to inv,  and subtract ~300 for stash)
	// also search for hard coded pixel values
	private static final int         STASH_X          = 7;         // 327 - 320  --> 7
	private static final int         STASH_Y          = 9;
	private static final int         INV_X            = 18 + 308;        // 18 + 308  --> 326
	private static final int         INV_Y            = 255;
	private static final int         HEAD_X           = 135 + 308;
	private static final int         HEAD_Y           = 3;
	private static final int         NECK_X           = 205 + 308;
	private static final int         NECK_Y           = 30;
	private static final int         BODY_X           = 135 + 308;
	private static final int         BODY_Y           = 75;
	private static final int         L_ARM_X          = 250 + 308;
	private static final int         L_ARM_Y          = 50;
	private static final int         R_ARM_X          = 20 + 308;
	private static final int         R_ARM_Y          = 50;
	private static final int         GLOVES_X         = 18 + 308;
	private static final int         GLOVES_Y         = 175;
	private static final int         BELT_X           = 135 + 308;
	private static final int         BELT_Y           = 175;
	private static final int         BOOTS_X          = 250 + 308;
	private static final int         BOOTS_Y          = 175;
	private static final int         L_RING_X         = 91 + 308;
	private static final int         L_RING_Y         = 175;
	private static final int         R_RING_X         = 205 + 308;
	private static final int         R_RING_Y         = 175;
	private static final int         BELT_GRID_X      = 106;  // 426 - 320 --> 106
	private static final int         BELT_GRID_Y      = 309; //250;  
	private static final int         CUBE_X           = 8; // 328 - 320  --> 8
	private static final int         CUBE_Y           = 310; //251;
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
	private D2QuestPainterPanel lQuestPanel;
	private D2SkillPainterPanel lSkillPanel;
	private D2WayPainterPanel lWayPanel;
	private D2DeathPainterPanel iDeathPainter;
	private JTextArea lDump;
	private RandallPanel lDumpPanel;
	private JPopupMenu rightClickItem;
	private MouseEvent rightClickEvent;

	public D2ViewChar(D2FileManager pMainFrame, String pFileName)
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
		lTabs.addTab("Character", lCharPanel);
		setContentPane(lTabs);
		iCharPainter.build();

		JPanel lStatPanel = new JPanel();
		lStatPanel.setLayout(new BorderLayout());
		lTabs.addTab("Stats", lStatPanel);
		Box charMainBox = Box.createHorizontalBox();
		Box charMainBox2 = Box.createHorizontalBox();
//		charMainBox.setLayout(new )
		Box charStatsBox = Box.createHorizontalBox();
		Box charLabelBox = Box.createVerticalBox();
		Box charValueBox = Box.createVerticalBox();

		lSkillPanel = new D2SkillPainterPanel();

//		charMainBox.add(charStatsBox);

		CJT.setEditable(false);
		float[] bGrey = new float[3];
		bGrey = Color.RGBtoHSB(237, 237, 237, bGrey);
		CJT.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
		if(System.getProperty("os.name").toLowerCase().indexOf("mac") != -1){
			CJT.setFont( new Font( "Courier", Font.TRUETYPE_FONT, 11 ));
		}else{
			Font f = null;
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(new File("resources" + File.separator +  "Courier_New.ttf"));
				f = Font.createFont(Font.TRUETYPE_FONT, fis);
			} catch (FontFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally{
				if ( fis != null ){
					try{ 
						fis.close(); 
					}catch( IOException ioe ) {
						ioe.printStackTrace();
					}
				}
			}
			f = f.deriveFont((float)11);
			CJT.setFont(f);
		}


		charMainBox.add(CJT);
		charMainBox2.add(lSkillPanel);

		charStatsBox.add(charLabelBox);
		charStatsBox.add(Box.createRigidArea(new Dimension(10,0)));
		charStatsBox.add(charValueBox);
		lStatPanel.add(charMainBox, BorderLayout.LINE_START);
		lStatPanel.add(charMainBox2, BorderLayout.LINE_END);



		JPanel lQuestWPanel = new JPanel();

		lQuestWPanel.setLayout(new BorderLayout());
		lQuestPanel = new D2QuestPainterPanel();
		lWayPanel = new D2WayPainterPanel();
		lQuestWPanel.add(lQuestPanel, BorderLayout.LINE_START);
		lQuestWPanel.add(lWayPanel, BorderLayout.EAST);
		lTabs.addTab("Quest", lQuestWPanel);
		lQuestPanel.build();
		lWayPanel.build();
		lQuestWPanel.setBackground(Color.BLACK);
//		charLabelBox.add(new JLabel("Name: "));
//		charLabelBox.add(new JLabel("Class: "));
//		charLabelBox.add(new JLabel("Experience: "));
//		charLabelBox.add(new JLabel("Level:"));
//		charLabelBox.add(new JLabel("NOTIMP: "));
//		charLabelBox.add(Box.createRigidArea(new Dimension(0,10)));
//		charLabelBox.add(new JLabel(" "));
//		charLabelBox.add(new JLabel("Strength: "));
//		charLabelBox.add(new JLabel("Dexterity: "));
//		charLabelBox.add(new JLabel("Vitality: "));
//		charLabelBox.add(new JLabel("Energy: "));
//		charLabelBox.add(new JLabel("HP: "));
//		charLabelBox.add(new JLabel("Mana: "));
//		charLabelBox.add(new JLabel("Stamina: "));
//		charLabelBox.add(new JLabel("Defense: "));
//		charLabelBox.add(new JLabel("AR: "));
//		charLabelBox.add(Box.createRigidArea(new Dimension(0,10)));
//		charLabelBox.add(new JLabel("Fire: "));
//		charLabelBox.add(new JLabel("Lightning: "));
//		charLabelBox.add(new JLabel("Cold: "));
//		charLabelBox.add(new JLabel("Poision: "));
//		charLabelBox.add(new JLabel("MF: "));
//		charLabelBox.add(new JLabel("FR/W: "));
//		charLabelBox.add(Box.createRigidArea(new Dimension(0,50)));

//		charValueBox.add(iCharName);
//		charValueBox.add(iCharClass);
//		charValueBox.add(iCharExp);
//		charValueBox.add(iCharLevel);
//		charValueBox.add(iCharDead);
//		charValueBox.add(Box.createRigidArea(new Dimension(0,10)));
//		charValueBox.add(new JLabel("Naked/Gear"));
//		charValueBox.add(iCharStr);
//		charValueBox.add(iCharDex);
//		charValueBox.add(iCharVit);
//		charValueBox.add(iCharNrg);
//		charValueBox.add(iCharHP);
//		charValueBox.add(iCharMana);
//		charValueBox.add(iCharStam);
//		charValueBox.add(iCharDef);
//		charValueBox.add(iCharAR);
//		charValueBox.add(Box.createRigidArea(new Dimension(0,10)));
//		charValueBox.add(iCharFireRes);
//		charValueBox.add(iCharLightRes);
//		charValueBox.add(iCharColdRes);
//		charValueBox.add(iCharPoisRes);
//		charValueBox.add(iCharMF);
//		charValueBox.add(iCharFRW);
//		charValueBox.add(Box.createRigidArea(new Dimension(0,50)));


		JPanel lCursorPanel = new JPanel();
		lCursorPanel.setLayout(new BorderLayout());
		iCharCursorPainter = new D2CharCursorPainterPanel();
		lCursorPanel.add(new JLabel("For item viewing, no items can be put or removed from here"), BorderLayout.NORTH);

		iDeathPainter= new D2DeathPainterPanel();
		lCursorPanel.add(iDeathPainter, BorderLayout.WEST);
		Box B1 = Box.createHorizontalBox();
		Box B2 = Box.createHorizontalBox();

		Box V1 = Box.createVerticalBox();

		B2.add(new JLabel("Cursor:"));
		B2.add(Box.createRigidArea(new Dimension(40,0)));
		V1.add(B2);
		V1.add(B1);
		B1.add(iCharCursorPainter);
		B1.add(Box.createRigidArea(new Dimension(40, 0)));
		lCursorPanel.add(V1, BorderLayout.EAST);

//		lCursorPanel.add(Box.createRigidArea(new Dimension(10, 0)), BorderLayout.EAST);


		lTabs.addTab("Corpse", lCursorPanel);
		iCharCursorPainter.build();

		JPanel lMercPanel = new JPanel();
		lMercPanel.setLayout(new BorderLayout());
		iMercPainter = new D2MercPainterPanel();

		Box mercMainBox = Box.createHorizontalBox();
		Box mercMainBox2 = Box.createHorizontalBox();
		Box mercStatsBox = Box.createHorizontalBox();
		Box mercLabelBox = Box.createVerticalBox();
		Box mercValueBox = Box.createVerticalBox();

		MJT.setEditable(false);
		bGrey = Color.RGBtoHSB(237, 237, 237, bGrey);
		MJT.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
		MJT.setFont( new Font( "Courier", Font.TRUETYPE_FONT, 11 ));


		mercMainBox2.add(iMercPainter);
//		mercMainBox.add(mercStatsBox);
		mercMainBox.add(MJT);
		mercStatsBox.add(mercLabelBox);
		mercStatsBox.add(Box.createRigidArea(new Dimension(10,0)));
		mercStatsBox.add(mercValueBox);

//		mercLabelBox.add(new JLabel("Name: "));
//		mercLabelBox.add(new JLabel("Race: "));
//		mercLabelBox.add(new JLabel("Type: "));
//		mercLabelBox.add(new JLabel("Experience: "));
//		mercLabelBox.add(new JLabel("Level:"));
//		mercLabelBox.add(new JLabel("Dead?: "));
//		mercLabelBox.add(Box.createRigidArea(new Dimension(0,10)));
//		mercLabelBox.add(new JLabel(" "));
//		mercLabelBox.add(new JLabel("Strength: "));
//		mercLabelBox.add(new JLabel("Dexterity: "));
//		mercLabelBox.add(new JLabel("HP: "));
//		mercLabelBox.add(new JLabel("Defense: "));
//		mercLabelBox.add(new JLabel("AR: "));
//		mercLabelBox.add(Box.createRigidArea(new Dimension(0,10)));
//		mercLabelBox.add(new JLabel("Fire: "));
//		mercLabelBox.add(new JLabel("Lightning: "));
//		mercLabelBox.add(new JLabel("Cold: "));
//		mercLabelBox.add(new JLabel("Poision: "));
//		mercLabelBox.add(Box.createRigidArea(new Dimension(0,120)));

//		mercValueBox.add(iMercName);
//		mercValueBox.add(iMercRace);
//		mercValueBox.add(iMercType);
//		mercValueBox.add(iMercExp);
//		mercValueBox.add(iMercLevel);
//		mercValueBox.add(iMercDead);
//		mercValueBox.add(Box.createRigidArea(new Dimension(0,10)));
//		mercValueBox.add(new JLabel("Naked/Gear"));
//		mercValueBox.add(iMercStr);
//		mercValueBox.add(iMercDex);
//		mercValueBox.add(iMercHP);
//		mercValueBox.add(iMercDef);
//		mercValueBox.add(iMercAR);
//		mercValueBox.add(Box.createRigidArea(new Dimension(0,10)));
//		mercValueBox.add(iMercFireRes);
//		mercValueBox.add(iMercLightRes);
//		mercValueBox.add(iMercColdRes);
//		mercValueBox.add(iMercPoisRes);
//		mercValueBox.add(Box.createRigidArea(new Dimension(0,120)));


//		lMercPanel.add(mercMainBox);
		lMercPanel.add(mercMainBox, BorderLayout.LINE_START);
		lMercPanel.add(mercMainBox2, BorderLayout.LINE_END);
		lTabs.addTab("Mercenary", lMercPanel);




		ButtonGroup lConnectGroup = new ButtonGroup();

		RandallPanel lBankPanel = new RandallPanel();
		iGold = new JTextField();
		iGold.setEditable(false);
		iGoldMax = new JTextField();
		iGoldMax.setEditable(false);
		iConnectGold = new JRadioButton();
		lConnectGroup.add(iConnectGold);

		iGoldBank = new JTextField();
		iGoldBank.setEditable(false);
		iGoldBankMax = new JTextField();
		iGoldBankMax.setEditable(false);
		iConnectGoldBank = new JRadioButton();
		lConnectGroup.add(iConnectGoldBank);
		iConnectGoldBank.setSelected(true);

		lBankPanel.addToPanel(new JLabel("Gold: "), 0, 0, 1, RandallPanel.NONE);
		lBankPanel.addToPanel(iConnectGold, 1, 0, 1, RandallPanel.NONE);
		lBankPanel.addToPanel(iGold, 2, 0, 1, RandallPanel.HORIZONTAL);
		lBankPanel.addToPanel(iGoldMax, 3, 0, 1, RandallPanel.HORIZONTAL);
		lBankPanel.addToPanel(new JLabel("Gold Stash: "), 0, 1, 1, RandallPanel.NONE);
		lBankPanel.addToPanel(iConnectGoldBank, 1, 1, 1, RandallPanel.NONE);
		lBankPanel.addToPanel(iGoldBank, 2, 1, 1, RandallPanel.HORIZONTAL);
		lBankPanel.addToPanel(iGoldBankMax, 3, 1, 1, RandallPanel.HORIZONTAL);

		RandallPanel lTransferPanel = new RandallPanel(true);
		lTransferPanel.setBorder("Transfer");

		iGoldTransferBtns = new JButton[8];

		iGoldTransferBtns[0] = new JButton("to char");
		iGoldTransferBtns[0].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				transferToChar(10000);
			}
		});
		JTextField lField10000 = new JTextField("10.000");
		lField10000.setEditable(false);
		iGoldTransferBtns[1] = new JButton("from char");
		iGoldTransferBtns[1].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				transferFromChar(10000);
			}
		});

		iGoldTransferBtns[2] = new JButton("to char");
		iGoldTransferBtns[2].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				transferToChar(100000);
			}
		});
		JTextField lField100000 = new JTextField("100.000");
		lField100000.setEditable(false);
		iGoldTransferBtns[3] = new JButton("from char");
		iGoldTransferBtns[3].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				transferFromChar(100000);
			}
		});

		iGoldTransferBtns[4] = new JButton("to char");
		iGoldTransferBtns[4].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				transferToChar(1000000);
			}
		});
		JTextField lField1000000 = new JTextField("1.000.000");
		lField1000000.setEditable(false);
		iGoldTransferBtns[5] = new JButton("from char");
		iGoldTransferBtns[5].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				transferFromChar(1000000);
			}
		});

		iGoldTransferBtns[6] = new JButton("to char");
		iGoldTransferBtns[6].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				transferToChar(getTransferFree());
			}
		});
		iTransferFree = new JTextField("10000");
		iGoldTransferBtns[7] = new JButton("from char");
		iGoldTransferBtns[7].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent pEvent)
			{
				transferFromChar(getTransferFree());
			}
		});

		lTransferPanel.addToPanel(iGoldTransferBtns[0], 0, 0, 1, RandallPanel.NONE);
		lTransferPanel.addToPanel(lField10000, 1, 0, 1, RandallPanel.HORIZONTAL);
		lTransferPanel.addToPanel(iGoldTransferBtns[1], 2, 0, 1, RandallPanel.NONE);

		lTransferPanel.addToPanel(iGoldTransferBtns[2], 0, 1, 1, RandallPanel.NONE);
		lTransferPanel.addToPanel(lField100000, 1, 1, 1, RandallPanel.HORIZONTAL);
		lTransferPanel.addToPanel(iGoldTransferBtns[3], 2, 1, 1, RandallPanel.NONE);

		lTransferPanel.addToPanel(iGoldTransferBtns[4], 0, 2, 1, RandallPanel.NONE);
		lTransferPanel.addToPanel(lField1000000, 1, 2, 1, RandallPanel.HORIZONTAL);
		lTransferPanel.addToPanel(iGoldTransferBtns[5], 2, 2, 1, RandallPanel.NONE);

		lTransferPanel.addToPanel(iGoldTransferBtns[6], 0, 3, 1, RandallPanel.NONE);
		lTransferPanel.addToPanel(iTransferFree, 1, 3, 1, RandallPanel.HORIZONTAL);
		lTransferPanel.addToPanel(iGoldTransferBtns[7], 2, 3, 1, RandallPanel.NONE);

		lBankPanel.addToPanel(lTransferPanel, 0, 10, 3, RandallPanel.HORIZONTAL);

		lBankPanel.finishDefaultPanel();
		lTabs.addTab("Bank", lBankPanel);
		lDumpPanel = new RandallPanel();
		lDump = new JTextArea();
		JScrollPane dumpScroll = new JScrollPane(lDump);
		dumpScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		dumpScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		lDumpPanel.addToPanel(dumpScroll, 0, 0, 1, RandallPanel.BOTH);
		lDumpPanel.setFont(new Font( "Monospaced", Font.TRUETYPE_FONT, 11 ));
//		HTMLEditorKit htmlEditor = new HTMLEditorKit();
//		lDump.setEditorKit(htmlEditor);
		//lDump.setPreferredSize(new Dimension(520,360));

		dumpScroll.setPreferredSize(new Dimension(520,360));
		lDump.setAutoscrolls(false);
		lDump.setVisible(true);
//		lDump.setBounds(6,7,175,179);
//		dumpScroll.setBounds(6,7,175,179);
		lTabs.addTab("Dump", lDumpPanel);

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

		JMenuItem item;
		JMenuItem item2;
		JMenuItem item3;
		rightClickItem = new JPopupMenu();
		rightClickItem.add(item = new JMenuItem("Delete?"));
		rightClickItem.add(item2 = new JMenuItem("View Item"));
		item3 = new JMenuItem("Extended Item Info");
		rightClickItem.add(new JPopupMenu.Separator());
		rightClickItem.add("Cancel");

		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {      

				if(event.getActionCommand().equals("Delete?")){



					D2ItemPanel lItemPanel = new D2ItemPanel(rightClickEvent, true, false, false);
					if (lItemPanel.getPanel() != -1)
					{
						// if there is an item to grab, grab it
						if (lItemPanel.isItem())
						{
							D2Item lTemp = lItemPanel.getItem();

							int check = JOptionPane.showConfirmDialog(null, "Delete " + lTemp.getName() + "?");
							if(check == 0){
								iCharacter.unmarkCharGrid(lTemp);
								iCharacter.removeCharItem(lItemPanel.getItemIndex());
								setCursorDropItem();
								if(lTemp.statModding()){
									iCharacter.updateCharStats("P", lTemp);
									paintCharStats();
								}
							}
						}
					}
				}

			}
		});

		item2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {      

				if(event.getActionCommand().equals("View Item")){
					D2ItemPanel lItemPanel = new D2ItemPanel(rightClickEvent, true, false, false);
					if (lItemPanel.getPanel() != -1)
					{
						// if there is an item to grab, grab it
						if (lItemPanel.isItem())
						{
							D2Item lTemp = lItemPanel.getItem();
							JFrame itemPanel = new JFrame();
							JEditorPane report = new JEditorPane();
							report.setContentType("text/html");

							JScrollPane SP = new JScrollPane(report);
							report.setBackground(Color.black);
							//HTMLEditorKit htmlEditor = new HTMLEditorKit();
							//report.setEditorKit(htmlEditor);
							report.setForeground(Color.white);
							report.setText("<html><font size=3 face=Dialog>"+lTemp.itemDumpHtml(true) + "</font></html>");
							report.setCaretPosition(0);
							itemPanel.add( SP);

							itemPanel.setLocation((rightClickEvent.getComponent().getLocationOnScreen().x + rightClickEvent.getX()), (rightClickEvent.getComponent().getLocationOnScreen().y + rightClickEvent.getY()));
							itemPanel.setSize(200,400);
							itemPanel.setVisible(true);
							itemPanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
						}
					}


				}

			}
		});

		item3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {      

				if(event.getActionCommand().equals("Extended Item Info")){
//					D2ItemPanel lItemPanel = new D2ItemPanel(rightClickEvent, true, false, false);
//					if (lItemPanel.getPanel() != -1)
//					{
//					// if there is an item to grab, grab it
//					if (lItemPanel.isItem())
//					{
//					D2Item lTemp = lItemPanel.getItem();
//					Box v1 = Box.createVerticalBox();

//					Box h1 = Box.createHorizontalBox();

//					JTextPane report = new JTextPane();
//					JScrollPane SP = new JScrollPane(report);
//					float[] bGrey = new float[3];
//					bGrey = Color.RGBtoHSB(237, 237, 237, bGrey);
//					report.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
//					report.setForeground(Color.black);
//					report.setText(lTemp.itemDumpHtml(false));
//					report.setCaretPosition(0);

//					try{
//					if(lTemp.isUnique() || lTemp.isSet() || lTemp.isRuneWord()){
//					ArrayList perfect = lTemp.getPerfectString();
//					JTextPane reportBest = new JTextPane();
//					JScrollPane SPBest = new JScrollPane(reportBest);
//					reportBest.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
//					reportBest.setForeground(Color.black);
//					String bestStr = "BEST:\n\n";
//					String[] perfDef = null;
//					if(lTemp.isTypeArmor()){
//					perfDef = lTemp.getPerfectDef(perfect);
//					bestStr = bestStr  + "Defense: " + perfDef[0] + "\n";
//					}else if(lTemp.isTypeWeapon()){
//					bestStr = bestStr  + lTemp.getPerfectDmg(perfect)[1];
//					}

//					for(int x = 0;x<perfect.size();x=x+1){
//					bestStr = bestStr + (((D2ItemProperty[])perfect.get(x))[1].getValue()) + "\n";
//					}

//					reportBest.setText(bestStr);
//					reportBest.setCaretPosition(0);

//					JTextPane reportWorst = new JTextPane();
//					JScrollPane SPWorst = new JScrollPane(reportWorst);
//					reportWorst.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
//					reportWorst.setForeground(Color.black);
//					String WorstStr = "WORST:\n\n";

//					if(lTemp.isTypeArmor()){
//					WorstStr = WorstStr + "Defense: " + perfDef[1] + "\n";
//					}else if(lTemp.isTypeWeapon()){
//					WorstStr = WorstStr  + lTemp.getPerfectDmg(perfect)[0];
//					}

//					for(int x = 0;x<perfect.size();x=x+1){
//					WorstStr = WorstStr + (((D2ItemProperty[])perfect.get(x))[0].getValue()) + "\n";
//					}
//					reportWorst.setText(WorstStr);
//					reportWorst.setCaretPosition(0);


//					h1.add(SPWorst);
//					h1.add( SP);
//					h1.add(SPBest);

//					}
//					if(lTemp.isRare()){

//					String rareRealName = lTemp.getPreSuf();
//					JTextPane rareName = new JTextPane();
//					JScrollPane scP = new JScrollPane(rareName);
//					rareName.setText("Your rare is a: " + rareRealName);
//					rareName.setCaretPosition(0);
//					rareName.setEditable(false);
//					rareName.setBackground(Color.getHSBColor(bGrey[0], bGrey[1], bGrey[2]));
//					v1.add(scP);
//					h1.add( SP);
//					}
//					JFrame basePanel = new JFrame();

//					basePanel.setLocation((rightClickEvent.getComponent().getLocationOnScreen().x + rightClickEvent.getX()), (rightClickEvent.getComponent().getLocationOnScreen().y + rightClickEvent.getY()));
//					basePanel.setSize(800,300);
//					basePanel.setVisible(true);
//					basePanel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



//					v1.add(h1);



//					basePanel.getContentPane().add(v1);
//					}
//					catch(Exception e){
//					e.printStackTrace();
//					System.err.println("Perfect strings suck.");
//					}

//					}
//					}


				}

			}
		});

		pack();
		setVisible(true);

		connect();
		itemListChanged();

		//        setModified(true);
	}

	public void paintMercStats(){

		MJT.setText(iCharacter.getMercStatString());
	}

	public void paintCharStats() {

		CJT.setText(iCharacter.getStatString());
		lSkillPanel.build();

	}

	public void connect()
	{
		if ( iCharacter != null )
		{
			return;
		}
		try
		{
			iCharacter = (D2Character) iFileManager.addItemList(iFileName, this);


			paintMercStats();
			paintCharStats();
			lSkillPanel.build();
			lQuestPanel.build();
			lWayPanel.build();

			iGold.setText(Integer.toString(iCharacter.getGold()));
			iGoldMax.setText(Integer.toString(iCharacter.getGoldMax()));
			iGoldBank.setText(Integer.toString(iCharacter.getGoldBank()));
			iGoldBankMax.setText(Integer.toString(iCharacter.getGoldBankMax()));

			for ( int i = 0 ; i < iGoldTransferBtns.length ; i++ )
			{
				iGoldTransferBtns[i].setEnabled(true);
			}

			itemListChanged();
			iMessage.setText("Character loaded");
		}
		catch ( Exception pEx )
		{
			disconnect(pEx);
			pEx.printStackTrace();
		}
	}

	public void disconnect(Exception pEx)
	{
		if ( iCharacter != null )
		{
			iFileManager.removeItemList(iFileName, this);
		}

		iCharacter = null;

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

		iGold.setText("");
		iGoldMax.setText("");
		iGoldBank.setText("");
		iGoldBankMax.setText("");

		for ( int i = 0 ; i < iGoldTransferBtns.length ; i++ )
		{
			iGoldTransferBtns[i].setEnabled(false);
		}

		itemListChanged();
//		System.gc();
	}

	public void transferToChar(int pGoldTransfer)
	{
		if (pGoldTransfer > 0)
		{
			try
			{
				// to char
				int lBank = iFileManager.getProject().getBankValue();
				if (pGoldTransfer > lBank)
				{
					// don't allow more as the bank has
					pGoldTransfer = lBank;
				}
				int lGoldChar;
				int lGoldMax;
				if (iConnectGold.isSelected())
				{
					lGoldChar = iCharacter.getGold();
					lGoldMax = iCharacter.getGoldMax();
				}
				else
				{
					lGoldChar = iCharacter.getGoldBank();
					lGoldMax = iCharacter.getGoldBankMax();
				}
				// char limit
				if (lGoldChar + pGoldTransfer > lGoldMax)
				{
					pGoldTransfer = lGoldMax - lGoldChar;
				}
				int lNewGold = lGoldChar + pGoldTransfer;
				int lNewGoldBank = lBank - pGoldTransfer;
				if (iConnectGold.isSelected())
				{
					iCharacter.setGold(lNewGold);
					iGold.setText(Integer.toString(iCharacter.getGold()));
				}
				else
				{
					iCharacter.setGoldBank(lNewGold);
					iGoldBank.setText(Integer.toString(iCharacter.getGoldBank()));
				}
				iFileManager.getProject().setBankValue(lNewGoldBank);
			}
			catch (Exception pEx)
			{
				D2FileManager.displayErrorDialog(pEx);
			}
		}

	}

	public void transferFromChar(int pGoldTransfer)
	{
		if (pGoldTransfer > 0)
		{
			try
			{
				int lGoldChar;
				if (iConnectGold.isSelected())
				{
					lGoldChar = iCharacter.getGold();
				}
				else
				{
					lGoldChar = iCharacter.getGoldBank();
				}

				if (pGoldTransfer > lGoldChar)
				{
					// don't allow more as the char has
					pGoldTransfer = lGoldChar;
				}

				// from char
				int lBank = iFileManager.getProject().getBankValue();

				int lNewGold = lGoldChar - pGoldTransfer;
				int lNewGoldBank = lBank + pGoldTransfer;

				if (iConnectGold.isSelected())
				{
					iCharacter.setGold(lNewGold);
					iGold.setText(Integer.toString(iCharacter.getGold()));
				}
				else
				{
					iCharacter.setGoldBank(lNewGold);
					iGoldBank.setText(Integer.toString(iCharacter.getGoldBank()));
				}
				iFileManager.getProject().setBankValue(lNewGoldBank);
			}
			catch (Exception pEx)
			{
				D2FileManager.displayErrorDialog(pEx);
			}
		}

	}

	public int getTransferFree()
	{
		try
		{
			return Integer.parseInt(iTransferFree.getText());
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

	public boolean isHC()
	{
		return iCharacter.isHC();
	}

	public boolean isSC()
	{
		return iCharacter.isSC();
	}

	public String getFileName()
	{
		return iFileName;
	}

	public boolean isModified()
	{
		return iCharacter.isModified();
	}

	public D2ItemList getItemLists()
	{
		return iCharacter;
	}

	public void closeView()
	{
		disconnect(null);
		iFileManager.removeFromOpenWindows(this);
	}

	public void itemListChanged()
	{

		String lTitle;
		if ( iCharacter == null )
		{
			lTitle = "Disconnected";
		}
		else
		{
			lTitle = iCharacter.getCharName();
			if (iCharacter == null)
			{
				lTitle += " (Error Reading File)";
			}
			else
			{
				lTitle += (( iCharacter.isModified() ) ? "*" : "");
				if (iCharacter.isSC())
				{
					lTitle += " (SC)";
				}
				else if (iCharacter.isHC())
				{
					lTitle += " (HC)";
				}
				lTitle += iCharacter.getTitleString();
			}
		}
		setTitle(lTitle);
		iCharPainter.build();
		if ( iMercPainter != null )
		{
			iMercPainter.build();
		}
		iCharCursorPainter.build();
		iDeathPainter.build();


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
			setRowCol(x, y);
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
			if ( iIsCursor )
			{
				return ( iCharacter.getCursorItem() != null );
			}
			if (iIsChar)
			{
				return iCharacter.checkCharPanel(iPanel, iRow, iCol, null);
			}
			if(iIsCorpse)
			{
				return iCharacter.checkCorpsePanel(iPanel,iRow, iCol, null);
			}
			return iCharacter.checkMercPanel(iPanel, iRow, iCol, null);
		}

		public int getItemIndex()
		{
			if (iIsChar)
			{
				return iCharacter.getCharItemIndex(iPanel, iRow, iCol);
			}
			if (iIsCorpse)
			{
				return iCharacter.getCorpseItemIndex(iPanel, iRow, iCol);
			}
			return iCharacter.getMercItemIndex(iPanel, iRow, iCol);
		}

		public D2Item getItem()
		{
			if ( iIsCursor )
			{
				return iCharacter.getCursorItem();
			}
			if ( iIsChar )
			{
				return iCharacter.getCharItem(iCharacter.getCharItemIndex(iPanel, iRow, iCol));
			}
			if(iIsCorpse){
				return iCharacter.getCorpseItem(iCharacter.getCorpseItemIndex(iPanel, iRow, iCol));
			}
			return iCharacter.getMercItem(iCharacter.getMercItemIndex(iPanel, iRow, iCol));
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
			}
			if (iIsChar && x >= STASH_X && x < STASH_X + 10 * GRID_SIZE + 10 * GRID_SPACER && y >= STASH_Y && y < STASH_Y + 10 * GRID_SIZE + 10 * GRID_SPACER)
			{
				return D2Character.BODY_STASH_CONTENT;
			}
			if (iIsChar && x >= BELT_GRID_X && x < BELT_GRID_X + 4 * GRID_SIZE + 4 * GRID_SPACER && y >= BELT_GRID_Y && y < BELT_GRID_Y + 4 * GRID_SIZE + 4 * GRID_SPACER)
			{
				return D2Character.BODY_BELT_CONTENT;
			}
			if (iIsChar && x >= INV_X && x < INV_X + 10 * GRID_SIZE + 10 * GRID_SPACER && y >= INV_Y && y < INV_Y + 4 * GRID_SIZE + 4 * GRID_SPACER)
			{
				return D2Character.BODY_INV_CONTENT;
			}
			if (iIsChar && x >= CUBE_X && x < CUBE_X + 3 * GRID_SIZE + 3 * GRID_SPACER && y >= CUBE_Y && y < INV_Y + 4 * GRID_SIZE + 4 * GRID_SPACER)
			{
				return D2Character.BODY_CUBE_CONTENT;
			}
			// merc & char
			if (x >= HEAD_X && x < HEAD_X + 2 * GRID_SIZE + 2 * GRID_SPACER && y >= HEAD_Y && y < HEAD_Y + 2 * GRID_SIZE + 2 * GRID_SPACER)
			{
				return D2Character.BODY_HEAD;
			}
			if ((iIsChar || iIsCorpse) && x >= NECK_X && x < NECK_X + 1 * GRID_SIZE + 1 * GRID_SPACER && y >= NECK_Y && y < NECK_Y + 1 * GRID_SIZE + 1 * GRID_SPACER)
			{
				return D2Character.BODY_NECK;
			}
			// merc & char
			if (x >= L_ARM_X && x < L_ARM_X + 2 * GRID_SIZE + 2 * GRID_SPACER && y >= L_ARM_Y && y < L_ARM_Y + 4 * GRID_SIZE + 4 * GRID_SPACER)
			{
				if ((!iIsChar && !iIsCorpse) || iWeaponSlot == 1)
				{
					// merc
					return D2Character.BODY_LARM;
				}
				else
				{
					return D2Character.BODY_LARM2;
				}
			}

			// golem?
			if((!iIsChar && !iIsCorpse) && x>= 258 && x<=314 && y>=223 && y<= 335){
				return 1337;
			}

			// merc & char
			if (x >= R_ARM_X && x < R_ARM_X + 2 * GRID_SIZE + 2 * GRID_SPACER && y >= R_ARM_Y && y < R_ARM_Y + 4 * GRID_SIZE + 4 * GRID_SPACER)
			{
				if ((!iIsChar && !iIsCorpse)  || iWeaponSlot == 1)
				{
					// merc
					return D2Character.BODY_RARM;
				}
				else
				{
					return D2Character.BODY_RARM2;
				}
			}
			// merc & char
			if (x >= BODY_X && x < BODY_X + 2 * GRID_SIZE + 2 * GRID_SPACER && y >= BODY_Y && y < BODY_Y + 3 * GRID_SIZE + 3 * GRID_SPACER)
			{
				return D2Character.BODY_TORSO;
			}
			if ((iIsChar || iIsCorpse)  && x >= GLOVES_X && x < GLOVES_X + 2 * GRID_SIZE + 2 * GRID_SPACER && y >= GLOVES_Y && y < GLOVES_Y + 2 * GRID_SIZE + 2 * GRID_SPACER)
			{
				return D2Character.BODY_GLOVES;
			}
			if ((iIsChar || iIsCorpse)  && x >= BELT_X && x < BELT_X + 2 * GRID_SIZE + 2 * GRID_SPACER && y >= BELT_Y && y < BELT_Y + 1 * GRID_SIZE + 1 * GRID_SPACER)
			{
				return D2Character.BODY_BELT;
			}
			if ((iIsChar || iIsCorpse)  && x >= BOOTS_X && x < BOOTS_X + 2 * GRID_SIZE + 2 * GRID_SPACER && y >= BOOTS_Y && y < BOOTS_Y + 2 * GRID_SIZE + 2 * GRID_SPACER)
			{
				return D2Character.BODY_BOOTS;
			}
			if ((iIsChar || iIsCorpse)  && x >= L_RING_X && x < L_RING_X + 1 * GRID_SIZE + 1 * GRID_SPACER && y >= L_RING_Y && y < L_RING_Y + 1 * GRID_SIZE + 1 * GRID_SPACER)
			{
				return D2Character.BODY_LRING;
			}
			if ((iIsChar || iIsCorpse)  && x >= R_RING_X && x < R_RING_X + 1 * GRID_SIZE + 1 * GRID_SPACER && y >= R_RING_Y && y < R_RING_Y + 1 * GRID_SIZE + 1 * GRID_SPACER)
			{
				return D2Character.BODY_RRING;
			}
			return -1;
		}

		// get row/col
		private void setRowCol(int x, int y)
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
					iRow = (x - INV_X) / (GRID_SIZE + GRID_SPACER);
					iCol = (y - INV_Y) / (GRID_SIZE + GRID_SPACER);
					//                    if (iChar.check_panel(panel, row, col))
					//                    {
					//                        temp_item = iChar.get_item_index(panel, row, col);
					//                    }
					break;
				case 2: // belted
					iRow = (x - BELT_GRID_X) / (GRID_SIZE + GRID_SPACER);
					iCol = 3 - ((y - BELT_GRID_Y) / (GRID_SIZE + GRID_SPACER));
					//                    if (iChar.check_panel(panel, row, col))
					//                    {
					//                        temp_item = iChar.get_item_index(panel, col, row);
					//                    }
					break;
				case 4: // cube
					iRow = (x - CUBE_X) / (GRID_SIZE + GRID_SPACER);
					iCol = (y - CUBE_Y) / (GRID_SIZE + GRID_SPACER);
					//                    if (iChar.check_panel(panel, row, col))
					//                    {
					//                        temp_item = iChar.get_item_index(panel, row, col);
					//                    }
					break;
				case 5: // stash
					iRow = (x - STASH_X) / (GRID_SIZE + GRID_SPACER);
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
			{
				iRow = 0;
				iCol = 0;
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
					if ( iCharacter == null )
					{
						return;
					}
					//                    System.err.println("Mouse Clicked: " + pEvent.getX() + ",
					// " + pEvent.getY() );
					if (pEvent.getButton() == MouseEvent.BUTTON1 /*
					 * &&
					 * pEvent.getClickCount() ==
					 * 1
					 */)
					{
						int lX = pEvent.getX();
						int lY = pEvent.getY();
						if (((lX >= 16 && lX <= 45) || (lX >= 247 && lX <= 276)) && (lY >= 24 && lY <= 44))
						{
							setWeaponSlot(1);
						}
						else if (((lX >= 51 && lX <= 80) || (lX >= 282 && lX <= 311)) && (lY >= 24 && lY <= 44))
						{
							setWeaponSlot(2);
						}
						// determine where the mouse click is
						D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, true, false, false);
						if (lItemPanel.getPanel() != -1)
						{
							// if there is an item to grab, grab it
							if (lItemPanel.isItem())
							{
								D2Item lTemp = lItemPanel.getItem();

								/**Code to remove potions when belt is removed!
								 * Thanks to Krikke.
								 */
								//System.out.println("isEquipped: " + lTemp.isEquipped() + " isABelt: " + lTemp.isABelt()); 
								if (lTemp.isEquipped() && lTemp.isABelt())
								{
									for (int y=0;y<iCharacter.getBeltPotions().size();y++){
										D2ViewClipboard.addItem((D2Item)iCharacter.getBeltPotions().get(y));
										iCharacter.unmarkCharGrid((D2Item)iCharacter.getBeltPotions().get(y));
									}
									for (int i=0;i<4;i++) {
										for (int j=1;j<4;j++) {
											if (iCharacter.getCharItemIndex(2, i, j) != -1) {
												iCharacter.removeCharItem(iCharacter.getCharItemIndex(2, i, j));
											}
										}
									}
								}


								iCharacter.unmarkCharGrid(lTemp);
								iCharacter.removeCharItem(lItemPanel.getItemIndex());
								D2ViewClipboard.addItem(lTemp);
								setCursorDropItem();
								if(lTemp.statModding()){
									iCharacter.updateCharStats("P", lTemp);
									paintCharStats();
								}

//								// redraw
//								build();
//								repaint();
							}
							else if (D2ViewClipboard.getItem() != null)
							{
								//	                    	System.err.println("Drop item");
								// since there is an item on the mouse, try to
								// drop it here

								D2Item lDropItem = D2ViewClipboard.getItem();
								//		                        int lDropWidth = lDropItem.get_width();
								//		                        int lDropHeight = lDropItem.get_height();
								//	                        int r = 0, c = 0;
								boolean drop = false;
								// non-equipped items, handle differently
								// because they require a row and column
								if (lItemPanel.getPanel() < 10)
								{
									// calculate row and column for the given
									// panel
									// with mouse coords x and y (split into an
									// int for
									// convenience)
									//	                            int temp = find_grid(panel, x, y);
									//	                            r = temp >> 16;
									//	                            c = temp & 0xffff;
									//                            r -= (D2MouseItem.get_mouse_x() /
									// GRID_SIZE);
									//                            c -= (D2MouseItem.get_mouse_y() /
									// GRID_SIZE);
									// if that area of the character is empty,
									// then update fields of the item and set
									// the 'drop' variable to true
									if (iCharacter.checkCharGrid(lItemPanel.getPanel(), lItemPanel.getRow(), lItemPanel.getColumn(), lDropItem))
									{
										switch (lItemPanel.getPanel())
										{
										case 2:
											lDropItem.set_location((short) 2);
											lDropItem.set_body_position((short) 0);
											lDropItem.set_col((short) (4 * lItemPanel.getColumn() + lItemPanel.getRow()));
											lDropItem.set_row((short) 0);
											lDropItem.set_panel((short) 0);
											break;
										case 1:
										case 4:
										case 5:
											lDropItem.set_location((short) 0);
											lDropItem.set_body_position((short) 0);
											lDropItem.set_row((short) lItemPanel.getColumn());
											lDropItem.set_col((short) lItemPanel.getRow());
											lDropItem.set_panel((short) lItemPanel.getPanel());
											break;
										}
										drop = true;
									}
								}
								// equipped items, a bit simpler
								// if that equipment slot is empty, update the
								// item's fields and set drop to true
								// r and c are set to width and height
								// for find_corner to deal with variable-size
								// objects in the hands
								// (note lack of item-type checking)
								else
								{
									if (!iCharacter.checkCharPanel(lItemPanel.getPanel(), 0, 0, lDropItem))
									{
										lDropItem.set_location((short) 1);
										lDropItem.set_body_position((short) (lItemPanel.getPanel() - 10));
										lDropItem.set_col((short) 0);
										lDropItem.set_row((short) 0);
										lDropItem.set_panel((short) 0);
										drop = true;
										//	                                r = lDropWidth;
										//	                                c = lDropHeight;
									}
								}
								// if the space to set the item is empty
								if (drop)
								{
									iCharacter.markCharGrid(lDropItem);
									// move the item to a new charcter, if
									// needed
									iCharacter.addCharItem(D2ViewClipboard.removeItem());

									// redraw
//									build();
//									repaint();

									setCursorPickupItem();
									if(lDropItem.statModding()){
										iCharacter.updateCharStats("D", lDropItem);
										paintCharStats();
									}
									//my_char.show_grid();
								}
							}
						}
					}else if (pEvent.getButton() == MouseEvent.BUTTON3){
						D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, true, false, false);
						if (lItemPanel.getPanel() != -1)
						{
							if (lItemPanel.isItem())
							{

								rightClickItem.show(D2ViewChar.this, pEvent.getX(), pEvent.getY()+35);
								rightClickEvent = pEvent;
							}
						}
					}
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
					if ( iCharacter == null )
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
							lCurrentMouse = lItemPanel.getItem();
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
									if (iCharacter.checkCharGrid(lItemPanel.getPanel(), lItemPanel.getRow(), lItemPanel.getColumn(), lDropItem))
									{
										drop = true;
									}
								}
								else
								{
									if (!iCharacter.checkCharPanel(lItemPanel.getPanel(), 0, 0, lDropItem))
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

		public void setWeaponSlot(int pWeaponSlot)
		{
			if(iWeaponSlot == pWeaponSlot){
				return;
			}

			iWeaponSlot = pWeaponSlot;
			build();
			//REMOVE ITEMS AND ADD ITEMS


			iCharacter.changeWep();
			paintCharStats();

//			repaint();
		}

		public void build()
		{
			Image lEmptyBackground;
			if (iWeaponSlot == 1)
			{
				lEmptyBackground = D2ImageCache.getImage("background.jpg");  //background_10x10
			}
			else
			{
				lEmptyBackground = D2ImageCache.getImage("background2.jpg"); //background2_10x10
			}

			int lWidth = lEmptyBackground.getWidth(D2CharPainterPanel.this);
			int lHeight = lEmptyBackground.getHeight(D2CharPainterPanel.this);

			iBackground = iFileManager.getGraphicsConfiguration().createCompatibleImage(lWidth, lHeight, Transparency.BITMASK);
//			iBackground = new BufferedImage(lWidth, lHeight, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D lGraphics = (Graphics2D) iBackground.getGraphics();

			lGraphics.drawImage(lEmptyBackground, 0, 0, D2CharPainterPanel.this);

			if ( iCharacter != null )
			{
				for (int i = 0; i < iCharacter.getCharItemNr(); i++)
				{
					D2Item temp_item = iCharacter.getCharItem(i);
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
							lGraphics.drawImage(lImage, INV_X + x * GRID_SIZE + x * GRID_SPACER, INV_Y + y * GRID_SIZE + y * GRID_SPACER, D2CharPainterPanel.this);
							break;
							// in the cube
						case 4:
							lGraphics.drawImage(lImage, CUBE_X + x * GRID_SIZE + x * GRID_SPACER, CUBE_Y + y * GRID_SIZE + y * GRID_SPACER, D2CharPainterPanel.this);
							break;
							// in the stash
						case 5:
							lGraphics.drawImage(lImage, STASH_X + x * GRID_SIZE + x * GRID_SPACER, STASH_Y + y * GRID_SIZE + y * GRID_SPACER, D2CharPainterPanel.this);
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
						int x = temp_item.get_col();
						int y = x / 4;
						x = x % 4;
						lGraphics.drawImage(lImage, BELT_GRID_X + x * GRID_SIZE + x * GRID_SPACER, BELT_GRID_Y + (3 - y) * GRID_SIZE + (3 - y) * GRID_SPACER, D2CharPainterPanel.this);
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
							lGraphics.drawImage(lImage, HEAD_X, HEAD_Y, D2CharPainterPanel.this);
							break;
							// neck / amulet (assume 1x1)
						case 2:
							lGraphics.drawImage(lImage, NECK_X, NECK_Y, D2CharPainterPanel.this);
							break;
						case 3:
							// body (assume 2x3
							lGraphics.drawImage(lImage, BODY_X, BODY_Y, D2CharPainterPanel.this);
							break;
							// right arm (give the whole 2x4)
							// biases are to center non-2x4 items
						case 4:
						case 11:
							if ((iWeaponSlot == 1 && body_position == 4) || (iWeaponSlot == 2 && body_position == 11))
							{
								w = temp_item.get_width();
								h = temp_item.get_height();
								wbias = 0;
								hbias = 0;
								if (w == 1)
									wbias += GRID_SIZE / 2;
								if (h == 3)
									hbias += GRID_SIZE / 2;
								else if (h == 2)
									hbias += GRID_SIZE;
								lGraphics.drawImage(lImage, R_ARM_X + wbias, R_ARM_Y + hbias, D2CharPainterPanel.this);
							}
							break;
							// left arm (give the whole 2x4)
						case 5:
						case 12:
							if ((iWeaponSlot == 1 && body_position == 5) || (iWeaponSlot == 2 && body_position == 12))
							{
								w = temp_item.get_width();
								h = temp_item.get_height();
								wbias = 0;
								hbias = 0;
								if (w == 1)
									wbias += GRID_SIZE / 2;
								if (h == 3)
									hbias += GRID_SIZE / 2;
								else if (h == 2)
									hbias += GRID_SIZE;
								lGraphics.drawImage(lImage, L_ARM_X + wbias, L_ARM_Y + hbias, D2CharPainterPanel.this);
							}
							break;
							// left ring (assume 1x1)
						case 6:
							lGraphics.drawImage(lImage, L_RING_X, L_RING_Y, D2CharPainterPanel.this);
							break;
							// right ring (assume 1x1)
						case 7:
							lGraphics.drawImage(lImage, R_RING_X, R_RING_Y, D2CharPainterPanel.this);
							break;
							// belt (assume 2x1)
						case 8:
							lGraphics.drawImage(lImage, BELT_X, BELT_Y, D2CharPainterPanel.this);
							break;
						case 9:
							// boots (assume 2x2)
							lGraphics.drawImage(lImage, BOOTS_X, BOOTS_Y, D2CharPainterPanel.this);
							break;
							// gloves (assume 2x2)
						case 10:
							lGraphics.drawImage(lImage, GLOVES_X, GLOVES_Y, D2CharPainterPanel.this);
							break;
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

	class D2MercPainterPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 2412158000132602811L;
		private Image iBackground;

		public D2MercPainterPanel()
		{
			setSize(BG_MERC_WIDTH, BG_MERC_HEIGHT);
			Dimension lSize = new Dimension(BG_MERC_WIDTH, BG_MERC_HEIGHT);
			setPreferredSize(lSize);

			addMouseListener(new MouseAdapter()
			{
				public void mouseReleased(MouseEvent pEvent)
				{
					if ( iCharacter == null )
					{
						return;
					}
					//                  System.err.println("Mouse Clicked: " + pEvent.getX() + ",
					// " + pEvent.getY() );
					if (pEvent.getButton() == MouseEvent.BUTTON1 /*
					 * &&
					 * pEvent.getClickCount() ==
					 * 1
					 */)
					{
						// determine where the mouse click is
						D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, false, false, false);
						if(lItemPanel.getPanel() == 1337){
							return;
						}
						if (lItemPanel.getPanel() != -1)
						{
							// if there is an item to grab, grab it
							if (lItemPanel.isItem())
							{




								D2Item lTemp = lItemPanel.getItem();

								iCharacter.unmarkMercGrid(lTemp);
								iCharacter.removeMercItem(lItemPanel.getItemIndex());
								D2ViewClipboard.addItem(lTemp);
								if(lTemp.statModding()){
									iCharacter.updateMercStats("P", lTemp);
									paintMercStats();
								}
								setCursorDropItem();

								// redraw
//								build();
//								repaint();
							}
							else if (D2ViewClipboard.getItem() != null)
							{
								//	                    	System.err.println("Drop item");
								// since there is an item on the mouse, try to
								// drop it here

								D2Item lDropItem = D2ViewClipboard.getItem();
								//		                        int lDropWidth = lDropItem.get_width();
								//		                        int lDropHeight = lDropItem.get_height();
								//	                        int r = 0, c = 0;
								boolean drop = false;
								// equipped items, a bit simpler
								// if that equipment slot is empty, update the
								// item's fields and set drop to true
								// r and c are set to width and height
								// for find_corner to deal with variable-size
								// objects in the hands
								// (note lack of item-type checking)
								if (!iCharacter.checkMercPanel(lItemPanel.getPanel(), 0, 0, lDropItem))
								{
									lDropItem.set_location((short) 1);
									lDropItem.set_body_position((short) (lItemPanel.getPanel() - 10));
									lDropItem.set_col((short) 0);
									lDropItem.set_row((short) 0);
									lDropItem.set_panel((short) 0);
									drop = true;
									//	                                r = lDropWidth;
									//	                                c = lDropHeight;
								}
								// if the space to set the item is empty
								if (drop)
								{
									iCharacter.markMercGrid(lDropItem);
									// move the item to a new charcter, if
									// needed
									iCharacter.addMercItem(D2ViewClipboard.removeItem());

									// redraw
//									build();
//									repaint();
									if(lDropItem.statModding()){
										iCharacter.updateMercStats("D", lDropItem);
										paintMercStats();
									}
									setCursorPickupItem();
									//my_char.show_grid();
								}
							}
						}
					}
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
					if ( iCharacter == null )
					{
						return;
					}
					//            	    restoreSubcomponentFocus();
					D2Item lCurrentMouse = null;

					D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, false, false, false);

					if(lItemPanel.getPanel() == 1337){
						if(iCharacter.getGolemItem() == null){
							return;
						}
						D2MercPainterPanel.this.setToolTipText(iCharacter.getGolemItem().itemDumpHtml(false));
						return;
					}

					if (lItemPanel.getPanel() != -1)
					{
						if (lItemPanel.isItem())
						{
							lCurrentMouse = lItemPanel.getItem();
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

								if (!iCharacter.checkMercPanel(lItemPanel.getPanel(), 0, 0, lDropItem))
								{
									drop = true;
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
						D2MercPainterPanel.this.setToolTipText(null);
					}
					else
					{
						D2MercPainterPanel.this.setToolTipText(lCurrentMouse.itemDumpHtml(false));
					}
				}
			});
		}

		public void build()
		{

			Image lEmptyBackground;
			if(iCharacter != null && iCharacter.getCharClass().equals("Necromancer")){
				lEmptyBackground = D2ImageCache.getImage("merc.jpg");
			}else{
				lEmptyBackground = D2ImageCache.getImage("merc2.jpg");
			}
			int lWidth = lEmptyBackground.getWidth(D2MercPainterPanel.this);
			int lHeight = lEmptyBackground.getHeight(D2MercPainterPanel.this);

			iBackground = iFileManager.getGraphicsConfiguration().createCompatibleImage(lWidth, lHeight, Transparency.BITMASK);
//			iBackground = new BufferedImage(lEmptyBackground.getWidth(lWidth, lHeight, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D lGraphics = (Graphics2D) iBackground.getGraphics();

			lGraphics.drawImage(lEmptyBackground, 0, 0, D2MercPainterPanel.this);

			if ( iCharacter != null )
			{
				for (int i = 0; i < iCharacter.getMercItemNr(); i++)
				{
					D2Item temp_item = iCharacter.getMercItem(i);
					Image lImage = D2ImageCache.getDC6Image(temp_item);
//					int location = temp_item.get_location();
					// on the body
					{
						int body_position = temp_item.get_body_position();
						int w, h, wbias, hbias;
						switch (body_position)
						{
						// head (assume 2x2)
						case 1:
							lGraphics.drawImage(lImage, HEAD_X, HEAD_Y, D2MercPainterPanel.this);
							break;
						case 3:
							// body (assume 2x3
							lGraphics.drawImage(lImage, BODY_X, BODY_Y, D2MercPainterPanel.this);
							break;
							// right arm (give the whole 2x4)
							// biases are to center non-2x4 items
						case 4:
							if ((iWeaponSlot == 1 && body_position == 4) || (iWeaponSlot == 2 && body_position == 11))
							{
								w = temp_item.get_width();
								h = temp_item.get_height();
								wbias = 0;
								hbias = 0;
								if (w == 1)
									wbias += GRID_SIZE / 2;
								if (h == 3)
									hbias += GRID_SIZE / 2;
								else if (h == 2)
									hbias += GRID_SIZE;
								lGraphics.drawImage(lImage, R_ARM_X + wbias, R_ARM_Y + hbias, D2MercPainterPanel.this);
							}
							break;
							// left arm (give the whole 2x4)
						case 5:
							if ((iWeaponSlot == 1 && body_position == 5) || (iWeaponSlot == 2 && body_position == 12))
							{
								w = temp_item.get_width();
								h = temp_item.get_height();
								wbias = 0;
								hbias = 0;
								if (w == 1)
									wbias += GRID_SIZE / 2;
								if (h == 3)
									hbias += GRID_SIZE / 2;
								else if (h == 2)
									hbias += GRID_SIZE;
								lGraphics.drawImage(lImage, L_ARM_X + wbias, L_ARM_Y + hbias, D2MercPainterPanel.this);
							}
							break;
						}
					}
				}

				//Paint golem item

				if(iCharacter.getGolemItem() != null){
					D2Item temp_item = iCharacter.getGolemItem();
					Image lImage = D2ImageCache.getDC6Image(temp_item);
					lGraphics.drawImage(lImage, 273, 225, D2MercPainterPanel.this);

				}

			}
			repaint();
		}

		public void paint(Graphics pGraphics)
		{
			super.paint(pGraphics);
			Graphics2D lGraphics = (Graphics2D) pGraphics;

			lGraphics.drawImage(iBackground, 0, 0, D2MercPainterPanel.this);
		}
	}

	class D2DeathPainterPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -4532690271347197756L;
		private Image iBackground;

		public D2DeathPainterPanel()
		{
			setSize(318, 247);
			Dimension lSize = new Dimension(318, 247);
			setPreferredSize(lSize);

			addMouseListener(new MouseAdapter()
			{
				public void mouseReleased(MouseEvent pEvent)
				{
					if ( iCharacter == null )
					{
						return;
					}
					//                    System.err.println("Mouse Clicked: " + pEvent.getX() + ",
					// " + pEvent.getY() );
					if (pEvent.getButton() == MouseEvent.BUTTON1 /*
					 * &&
					 * pEvent.getClickCount() ==
					 * 1
					 */)
					{
						int lX = pEvent.getX();
						int lY = pEvent.getY();
						if (((lX >= 16 && lX <= 45) || (lX >= 247 && lX <= 276)) && (lY >= 24 && lY <= 44))
						{
							setWeaponSlot(1);
						}
						else if (((lX >= 51 && lX <= 80) || (lX >= 282 && lX <= 311)) && (lY >= 24 && lY <= 44))
						{
							setWeaponSlot(2);
						}


					}
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
					if ( iCharacter == null )
					{
						return;
					}
					//            	    restoreSubcomponentFocus();
					D2Item lCurrentMouse = null;

					D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, false, false, true);
					if (lItemPanel.getPanel() != -1)
					{
						if (lItemPanel.isItem())
						{
							lCurrentMouse = lItemPanel.getItem();
						}

					}
					else
					{
						setCursorNormal();
					}
					if (lCurrentMouse == null)
					{
						D2DeathPainterPanel.this.setToolTipText(null);
					}
					else
					{
						D2DeathPainterPanel.this.setToolTipText(lCurrentMouse.itemDumpHtml(false));
					}
				}
			});
		}

		public void setWeaponSlot(int pWeaponSlot)
		{
			iWeaponSlot = pWeaponSlot;
			build();
		}

		public void build()
		{
			Image lEmptyBackground;
			if (iWeaponSlot == 1)
			{
				lEmptyBackground = D2ImageCache.getImage("dead.jpg");
			}
			else
			{
				lEmptyBackground = D2ImageCache.getImage("dead2.jpg");
			}

			int lWidth = lEmptyBackground.getWidth(D2DeathPainterPanel.this);
			int lHeight = lEmptyBackground.getHeight(D2DeathPainterPanel.this);

			iBackground = iFileManager.getGraphicsConfiguration().createCompatibleImage(lWidth, lHeight, Transparency.BITMASK);
//			iBackground = new BufferedImage(lWidth, lHeight, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D lGraphics = (Graphics2D) iBackground.getGraphics();

			lGraphics.drawImage(lEmptyBackground, 0, 0, D2DeathPainterPanel.this);

			if ( iCharacter != null )
			{
				for (int i = 0; i < iCharacter.getCorpseItemNr(); i++)
				{
					D2Item temp_item = iCharacter.getCorpseItem(i);
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
							lGraphics.drawImage(lImage, INV_X + x * GRID_SIZE + x * GRID_SPACER, INV_Y + y * GRID_SIZE + y * GRID_SPACER, D2DeathPainterPanel.this);
							break;
							// in the cube
						case 4:
							lGraphics.drawImage(lImage, CUBE_X + x * GRID_SIZE + x * GRID_SPACER, CUBE_Y + y * GRID_SIZE + y * GRID_SPACER, D2DeathPainterPanel.this);
							break;
							// in the stash
						case 5:
							lGraphics.drawImage(lImage, STASH_X + x * GRID_SIZE + x * GRID_SPACER, STASH_Y + y * GRID_SIZE + y * GRID_SPACER, D2DeathPainterPanel.this);
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
						int x = temp_item.get_col();
						int y = x / 4;
						x = x % 4;
						lGraphics.drawImage(lImage, BELT_GRID_X + x * GRID_SIZE + x * GRID_SPACER, BELT_GRID_Y + (3 - y) * GRID_SIZE + (3 - y) * GRID_SPACER, D2DeathPainterPanel.this);
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
							lGraphics.drawImage(lImage, HEAD_X, HEAD_Y, D2DeathPainterPanel.this);
							break;
							// neck / amulet (assume 1x1)
						case 2:
							lGraphics.drawImage(lImage, NECK_X, NECK_Y, D2DeathPainterPanel.this);
							break;
						case 3:
							// body (assume 2x3
							lGraphics.drawImage(lImage, BODY_X, BODY_Y, D2DeathPainterPanel.this);
							break;
							// right arm (give the whole 2x4)
							// biases are to center non-2x4 items
						case 4:
						case 11:
							if ((iWeaponSlot == 1 && body_position == 4) || (iWeaponSlot == 2 && body_position == 11))
							{
								w = temp_item.get_width();
								h = temp_item.get_height();
								wbias = 0;
								hbias = 0;
								if (w == 1)
									wbias += GRID_SIZE / 2;
								if (h == 3)
									hbias += GRID_SIZE / 2;
								else if (h == 2)
									hbias += GRID_SIZE;
								lGraphics.drawImage(lImage, R_ARM_X + wbias, R_ARM_Y + hbias, D2DeathPainterPanel.this);
							}
							break;
							// left arm (give the whole 2x4)
						case 5:
						case 12:
							if ((iWeaponSlot == 1 && body_position == 5) || (iWeaponSlot == 2 && body_position == 12))
							{
								w = temp_item.get_width();
								h = temp_item.get_height();
								wbias = 0;
								hbias = 0;
								if (w == 1)
									wbias += GRID_SIZE / 2;
								if (h == 3)
									hbias += GRID_SIZE / 2;
								else if (h == 2)
									hbias += GRID_SIZE;
								lGraphics.drawImage(lImage, L_ARM_X + wbias, L_ARM_Y + hbias, D2DeathPainterPanel.this);
							}
							break;
							// left ring (assume 1x1)
						case 6:
							lGraphics.drawImage(lImage, L_RING_X, L_RING_Y, D2DeathPainterPanel.this);
							break;
							// right ring (assume 1x1)
						case 7:
							lGraphics.drawImage(lImage, R_RING_X, R_RING_Y, D2DeathPainterPanel.this);
							break;
							// belt (assume 2x1)
						case 8:
							lGraphics.drawImage(lImage, BELT_X, BELT_Y, D2DeathPainterPanel.this);
							break;
						case 9:
							// boots (assume 2x2)
							lGraphics.drawImage(lImage, BOOTS_X, BOOTS_Y, D2DeathPainterPanel.this);
							break;
							// gloves (assume 2x2)
						case 10:
							lGraphics.drawImage(lImage, GLOVES_X, GLOVES_Y, D2DeathPainterPanel.this);
							break;
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

			lGraphics.drawImage(iBackground, 0, 0, D2DeathPainterPanel.this);
		}
	}

	class D2SkillPainterPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8956659406243697754L;
		private Image iBackground;
		private Image lEmptyBackground;

		public D2SkillPainterPanel()
		{
			setSize(284, 383);
			Dimension lSize = new Dimension(284, 383);
			setPreferredSize(lSize);
//			this.build();

			addMouseListener(new MouseAdapter()
			{
				public void mouseReleased(MouseEvent pEvent)
				{
					if ( iCharacter == null )
					{
						return;
					}
					//                    System.err.println("Mouse Clicked: " + pEvent.getX() + ",
					// " + pEvent.getY() );
					if (pEvent.getButton() == MouseEvent.BUTTON1 /*
					 * &&
					 * pEvent.getClickCount() ==
					 * 1
					 */)
					{
						int lX = pEvent.getX();
						int lY = pEvent.getY();
						if ((lX >= 208 && lX <= 283)  && (lY >= 300 && lY <= 388))
						{
							setSkillSlot(0);
						}
						else if ((lX >= 208 && lX <= 283) && (lY >= 201 && lY <= 291))
						{
							setSkillSlot(1);
						}else if ((lX >= 208 && lX <= 283) && (lY >= 100 && lY <= 192))
						{
							setSkillSlot(2);
						}
						// determine where the mouse click is
					}
				}



				private void setSkillSlot(int i) {



					iSkillSlot = i;
					build();
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

//			addMouseMotionListener(new MouseMotionAdapter()
//			{
//			public void mouseMoved(MouseEvent pEvent)
//			{
//			System.out.println(pEvent.getX()  + " , " + pEvent.getY());
//			}
//			});
		}

		public void build()
		{


			switch((int)iCharacter.getCharCode()){


			case 0:
				switch(iSkillSlot){
				case 0:
					lEmptyBackground = D2ImageCache.getImage("AmaArr.jpg");
					break;
				case 1:
					lEmptyBackground = D2ImageCache.getImage("AmaPass.jpg");
					break;
				case 2:
					lEmptyBackground = D2ImageCache.getImage("AmaJav.jpg");
					break;

				}
//				cClass = "ama"; 
				break;
			case 1:
				switch(iSkillSlot){
				case 0:
					lEmptyBackground = D2ImageCache.getImage("SorFir.jpg");
					break;
				case 1:
					lEmptyBackground = D2ImageCache.getImage("SorLig.jpg");
					break;
				case 2:
					lEmptyBackground = D2ImageCache.getImage("SorCol.jpg");
					break;

				}
//				cClass = "sor";
				break;
			case 2:
				switch(iSkillSlot){
				case 0:
					lEmptyBackground = D2ImageCache.getImage("NecCur.jpg");
					break;
				case 1:
					lEmptyBackground = D2ImageCache.getImage("NecPoi.jpg");
					break;
				case 2:
					lEmptyBackground = D2ImageCache.getImage("NecSum.jpg");
					break;

				}
//				cClass = "nec";
				break;
			case 3:
				switch(iSkillSlot){
				case 0:
					lEmptyBackground = D2ImageCache.getImage("PalCom.jpg");
					break;
				case 1:
					lEmptyBackground = D2ImageCache.getImage("PalOff.jpg");
					break;
				case 2:
					lEmptyBackground = D2ImageCache.getImage("PalDef.jpg");
					break;

				}
//				cClass = "pal";
				break;
			case 4:
				switch(iSkillSlot){
				case 0:
					lEmptyBackground = D2ImageCache.getImage("BarCom.jpg");
					break;
				case 1:
					lEmptyBackground = D2ImageCache.getImage("BarMas.jpg");
					break;
				case 2:
					lEmptyBackground = D2ImageCache.getImage("BarWar.jpg");
					break;

				}
//				cClass = "bar";
				break;
			case 5:
				switch(iSkillSlot){
				case 0:
					lEmptyBackground = D2ImageCache.getImage("DruSum.jpg");
					break;
				case 1:
					lEmptyBackground = D2ImageCache.getImage("DruSha.jpg");
					break;
				case 2:
					lEmptyBackground = D2ImageCache.getImage("DruEle.jpg");
					break;

				}
//				cClass = "dru";
				break;
			case 6:
				switch(iSkillSlot){
				case 0:
					lEmptyBackground = D2ImageCache.getImage("AssTra.jpg");
					break;
				case 1:
					lEmptyBackground = D2ImageCache.getImage("AssSha.jpg");
					break;
				case 2:
					lEmptyBackground = D2ImageCache.getImage("AssMar.jpg");
					break;

				}
//				cClass = "ass";
				break;


			}



//			lEmptyBackground = D2ImageCache.getImage("AmaArr.jpg");

			int lWidth = lEmptyBackground.getWidth(D2SkillPainterPanel.this);
			int lHeight = lEmptyBackground.getHeight(D2SkillPainterPanel.this);

			iBackground = iFileManager.getGraphicsConfiguration().createCompatibleImage(lWidth, lHeight, Transparency.BITMASK);
//			iBackground = new BufferedImage(lEmptyBackground.getWidth(lWidth, lHeight, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D lGraphics = (Graphics2D) iBackground.getGraphics();

			lGraphics.drawImage(lEmptyBackground, 0, 0, D2SkillPainterPanel.this);

			if(iCharacter != null){
				drawText(lGraphics, iSkillSlot);

			}
//			if ( iCharacter != null )
//			{
//			for (int i = 0; i < iCharacter.getMercItemNr(); i++)
//			{
//			D2Item temp_item = iCharacter.getMercItem(i);
//			Image lImage = D2ImageCache.getDC6Image(temp_item);
//			int location = temp_item.get_location();
//			// on the body
//			{
//			int body_position = temp_item.get_body_position();
//			int w, h, wbias, hbias;
//			switch (body_position)
//			{
//			// head (assume 2x2)
//			case 1:
//			lGraphics.drawImage(lImage, HEAD_X, HEAD_Y, D2SkillPainterPanel.this);
//			break;
//			case 3:
//			// body (assume 2x3
//			lGraphics.drawImage(lImage, BODY_X, BODY_Y, D2SkillPainterPanel.this);
//			break;
//			// right arm (give the whole 2x4)
//			// biases are to center non-2x4 items
//			case 4:
//			if ((iWeaponSlot == 1 && body_position == 4) || (iWeaponSlot == 2 && body_position == 11))
//			{
//			w = temp_item.get_width();
//			h = temp_item.get_height();
//			wbias = 0;
//			hbias = 0;
//			if (w == 1)
//			wbias += GRID_SIZE / 2;
//			if (h == 3)
//			hbias += GRID_SIZE / 2;
//			else if (h == 2)
//			hbias += GRID_SIZE;
//			lGraphics.drawImage(lImage, R_ARM_X + wbias, R_ARM_Y + hbias, D2SkillPainterPanel.this);
//			}
//			break;
//			// left arm (give the whole 2x4)
//			case 5:
//			if ((iWeaponSlot == 1 && body_position == 5) || (iWeaponSlot == 2 && body_position == 12))
//			{
//			w = temp_item.get_width();
//			h = temp_item.get_height();
//			wbias = 0;
//			hbias = 0;
//			if (w == 1)
//			wbias += GRID_SIZE / 2;
//			if (h == 3)
//			hbias += GRID_SIZE / 2;
//			else if (h == 2)
//			hbias += GRID_SIZE;
//			lGraphics.drawImage(lImage, L_ARM_X + wbias, L_ARM_Y + hbias, D2SkillPainterPanel.this);
//			}
//			break;
//			}
//			}
//			}
//			}
			repaint();
		}

		private void drawText(Graphics2D lGraphics, int skillSlot) {

			switch(iSkillSlot){
			case 0:
				lGraphics.drawString(iCharacter.getCharSkillRem() + "",238 , 69);
				for(int x = 0;x<10;x=x+1){
					lGraphics.setColor(Color.white);
					lGraphics.drawString(iCharacter.getInitSkillListA()[x] + "/",iCharacter.getSkillLocs()[x].x-10 , iCharacter.getSkillLocs()[x].y+2);
					if(iCharacter.getInitSkillListA()[x]!=(iCharacter.getSkillListA()[x])){
						lGraphics.setColor(Color.orange.brighter());
					}
					lGraphics.drawString(iCharacter.getSkillListA()[x]+"",iCharacter.getSkillLocs()[x].x+11 , iCharacter.getSkillLocs()[x].y+2);

				}
				break;
			case 1:
				lGraphics.drawString(iCharacter.getCharSkillRem() + "",238 , 69);
				for(int x = 0;x<10;x=x+1){
					lGraphics.setColor(Color.white);
					lGraphics.drawString(iCharacter.getInitSkillListB()[x] + "/",iCharacter.getSkillLocs()[x+10].x-10 , iCharacter.getSkillLocs()[x+10].y+2);

					if(iCharacter.getInitSkillListB()[x]!=(iCharacter.getSkillListB()[x])){
						lGraphics.setColor(Color.orange.brighter());
					}else{
						lGraphics.setColor(Color.white);
					}
					lGraphics.drawString(iCharacter.getSkillListB()[x]+"",iCharacter.getSkillLocs()[x+10].x+11 , iCharacter.getSkillLocs()[x+10].y+2);

				}
				break;
			case 2:
				lGraphics.drawString(iCharacter.getCharSkillRem() + "",238 , 69);
				for(int x = 0;x<10;x=x+1){
					lGraphics.setColor(Color.white);
					lGraphics.drawString(iCharacter.getInitSkillListC()[x] + "/",iCharacter.getSkillLocs()[x+20].x-10 , iCharacter.getSkillLocs()[x+20].y+2);

					if(iCharacter.getInitSkillListC()[x]!=(iCharacter.getSkillListC()[x])){
						lGraphics.setColor(Color.orange.brighter());
					}else{
						lGraphics.setColor(Color.white);
					}

					lGraphics.drawString(iCharacter.getSkillListC()[x]+"",iCharacter.getSkillLocs()[x+20].x+11 , iCharacter.getSkillLocs()[x+20].y+2);
				}
				break;

			}



		}

		public void paint(Graphics pGraphics)
		{
			super.paint(pGraphics);
			Graphics2D lGraphics = (Graphics2D) pGraphics;

			lGraphics.drawImage(iBackground, 0, 0, D2SkillPainterPanel.this);
		}
	}

	class D2QuestPainterPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1154694362190497678L;
		private Image iBackground;
		private int bgNum = 1;
		private Image lEmptyBackground;
		private Image tick;
		private Point[][] questLoc= {{new Point(24,93),new Point(113,93),new Point(197,93),new Point(24,178),new Point(113,178),new Point(197,178)},
				{new Point(47,93),new Point(136,93),new Point(220,93),new Point(47,178),new Point(136,178),new Point(220,178)},
				{new Point(71,93),new Point(160,93),new Point(244,93),new Point(71,178),new Point(160,178),new Point(244,178)}};
		private final  int cowKingX = 140;
		private final  int cowkingY = 233;
		public D2QuestPainterPanel()
		{
			tick = D2ImageCache.getImage("tick.jpg");
			setSize(286, 383);
			Dimension lSize = new Dimension(284, 383);
			setPreferredSize(lSize);
//			this.build();

//			addMouseMotionListener(new MouseMotionAdapter()
//			{
//			public void mouseMoved(MouseEvent pEvent)
//			{
//			System.out.println(pEvent.getX()  + " , " + pEvent.getY());
//			}
//			});

			addMouseListener(new MouseAdapter()
			{


				public void mouseReleased(MouseEvent pEvent)
				{
					if ( iCharacter == null )
					{
						return;
					}
					if (pEvent.getButton() == MouseEvent.BUTTON1)
					{
						int lX = pEvent.getX();
						int lY = pEvent.getY();
						if ((lX >= 10 && lX <= 60)  && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(1);
						}
						else if ((lX >= 65 && lX <= 115) && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(2);
						}else if ((lX >= 120 && lX <= 170) && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(3);
						}else if ((lX >= 175 && lX <= 225) && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(4);
						}else if ((lX >= 230 && lX <= 280) && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(5);
						}
						// determine where the mouse click is
					}
				}



				private void setQuestSlot(int i) {

					bgNum = i;
					build();
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

		}

		public void build()
		{
			lEmptyBackground = D2ImageCache.getImage("q" + bgNum + ".jpg");
			int lWidth = lEmptyBackground.getWidth(D2QuestPainterPanel.this);
			int lHeight = lEmptyBackground.getHeight(D2QuestPainterPanel.this);

			iBackground = iFileManager.getGraphicsConfiguration().createCompatibleImage(lWidth, lHeight, Transparency.BITMASK);
//			iBackground = new BufferedImage(lEmptyBackground.getWidth(lWidth, lHeight, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D lGraphics = (Graphics2D) iBackground.getGraphics();

			lGraphics.drawImage(lEmptyBackground, 0, 0, D2QuestPainterPanel.this);

			if(iCharacter != null){
				drawCompleted(lGraphics, bgNum);

			}
			repaint();
		}

		private void drawCompleted(Graphics2D lGraphics, int questSlot) {

			for(int f = 0;f<3;f=f+1){
				for(int y=0;y<iCharacter.getQuests()[f][questSlot -1].length;y=y+1){
					if(iCharacter.getQuests()[f][questSlot -1][y])lGraphics.drawImage(tick, questLoc[f][y].x, questLoc[f][y].y, D2QuestPainterPanel.this);
				}
				if(iCharacter.getCowKingDead(f) && (questSlot) == 1){
					lGraphics.drawImage(tick, cowKingX + (24*f), cowkingY , D2QuestPainterPanel.this);
				}
			}
		}

		public void paint(Graphics pGraphics)
		{
			super.paint(pGraphics);
			Graphics2D lGraphics = (Graphics2D) pGraphics;

			lGraphics.drawImage(iBackground, 0, 0, D2QuestPainterPanel.this);


		}
	}



	class D2WayPainterPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7583208611559380148L;
		private Image iBackground;
		private int bgNum = 1;
		private Image lEmptyBackground;
		private Image tick;
		private Point[] questLoc= {new Point(36,59),new Point(36,87),new Point(36,116),new Point(36,145),new Point(36,174),new Point(36,202),new Point(36,231),new Point(36,260),new Point(36,287)};
		public D2WayPainterPanel()
		{
			this.setBackground(Color.BLACK);
			tick = D2ImageCache.getImage("ticksm.jpg");
			setSize(286, 383);
			Dimension lSize = new Dimension(263, 360);
			setPreferredSize(lSize);
//			this.build();

//			addMouseMotionListener(new MouseMotionAdapter()
//			{
//			public void mouseMoved(MouseEvent pEvent)
//			{
//			System.out.println(pEvent.getX()  + " , " + pEvent.getY());
//			}
//			});

			addMouseListener(new MouseAdapter()
			{


				public void mouseReleased(MouseEvent pEvent)
				{
					if ( iCharacter == null )
					{
						return;
					}
					if (pEvent.getButton() == MouseEvent.BUTTON1)
					{
						int lX = pEvent.getX();
						int lY = pEvent.getY();
						if ((lX >= 8 && lX <= 54)  && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(1);
						}
						else if ((lX >= 58 && lX <= 106) && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(2);
						}else if ((lX >= 109 && lX <= 155) && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(3);
						}else if ((lX >= 159 && lX <= 207) && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(4);
						}else if ((lX >= 210 && lX <= 256) && (lY >= 0 && lY <= 26))
						{
							setQuestSlot(5);
						}
						// determine where the mouse click is
					}
				}



				private void setQuestSlot(int i) {

					bgNum = i;
					build();
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

		}

		public void build()
		{
			lEmptyBackground = D2ImageCache.getImage("w" + bgNum + ".jpg");
			int lWidth = lEmptyBackground.getWidth(D2WayPainterPanel.this);
			int lHeight = lEmptyBackground.getHeight(D2WayPainterPanel.this);

			iBackground = iFileManager.getGraphicsConfiguration().createCompatibleImage(lWidth, lHeight, Transparency.BITMASK);
//			iBackground = new BufferedImage(lEmptyBackground.getWidth(lWidth, lHeight, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D lGraphics = (Graphics2D) iBackground.getGraphics();

			lGraphics.drawImage(lEmptyBackground, 0, 0, D2WayPainterPanel.this);

			if(iCharacter != null){
				drawCompleted(lGraphics, bgNum);

			}
			repaint();
		}

		private void drawCompleted(Graphics2D lGraphics, int questSlot) {



			for(int f = 0;f<3;f=f+1){
				for(int y=0;y<iCharacter.getWaypoints()[f][questSlot -1].length;y=y+1){
					if(iCharacter.getWaypoints()[f][questSlot -1][y]){
						if(f==0){
							lGraphics.drawImage(tick, questLoc[y].x, questLoc[y].y, D2WayPainterPanel.this);
						}else if(f==1){
							lGraphics.drawImage(tick, questLoc[y].x+10, questLoc[y].y, D2WayPainterPanel.this);
						}else if(f==2){
							lGraphics.drawImage(tick, questLoc[y].x+20, questLoc[y].y, D2WayPainterPanel.this);
						}
					}

				}
			}


//			switch(questSlot){

//			case 1:

//			for(int f = 0;f<3;f=f+1){
//			for(int y=0;y<iCharacter.getWaypoints()[f][questSlot -1].length();y=y+1){
//			if(iCharacter.getWaypoints()[f][questSlot -1].charAt(y) == '1'){
//			if(f==0){
//			lGraphics.drawImage(tick, questLoc[y].x, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==1){
//			lGraphics.drawImage(tick, questLoc[y].x+10, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==2){
//			lGraphics.drawImage(tick, questLoc[y].x+20, questLoc[y].y, D2WayPainterPanel.this);
//			}
//			}

//			}
//			}

////			for(int x = 0;x<iCharacter.getWaypoints().length;x=x+1){
////			for(int y=0;y<iCharacter.getWaypoints()[x].length;x=x+1){
////			lGraphics.drawImage(tick, questLoc[x].x, questLoc[x].y, D2WayPainterPanel.this);
////			lGraphics.drawImage(tick, questLoc[x].x+10, questLoc[x].y, D2WayPainterPanel.this);
////			lGraphics.drawImage(tick, questLoc[x].x+20, questLoc[x].y, D2WayPainterPanel.this);
////			}
////			}
//			break;
//			case 2:
//			for(int f = 0;f<3;f=f+1){
//			for(int y=0;y<iCharacter.getWaypoints()[f][questSlot -1].length();y=y+1){
//			if(iCharacter.getWaypoints()[f][questSlot -1].charAt(y) == '1'){
//			if(f==0){
//			lGraphics.drawImage(tick, questLoc[y].x, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==1){
//			lGraphics.drawImage(tick, questLoc[y].x+10, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==2){
//			lGraphics.drawImage(tick, questLoc[y].x+20, questLoc[y].y, D2WayPainterPanel.this);
//			}
//			}

//			}
//			}
//			break;
//			case 3:
//			for(int f = 0;f<3;f=f+1){
//			for(int y=0;y<iCharacter.getWaypoints()[f][questSlot -1].length();y=y+1){
//			if(iCharacter.getWaypoints()[f][questSlot -1].charAt(y) == '1'){
//			if(f==0){
//			lGraphics.drawImage(tick, questLoc[y].x, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==1){
//			lGraphics.drawImage(tick, questLoc[y].x+10, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==2){
//			lGraphics.drawImage(tick, questLoc[y].x+20, questLoc[y].y, D2WayPainterPanel.this);
//			}
//			}

//			}
//			}
//			break;
//			case 4:
//			for(int f = 0;f<3;f=f+1){
//			for(int y=0;y<3;y=y+1){
//			if(iCharacter.getWaypoints()[f][questSlot -1].charAt(y) == '1'){
//			if(f==0){
//			lGraphics.drawImage(tick, questLoc[y].x, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==1){
//			lGraphics.drawImage(tick, questLoc[y].x+10, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==2){
//			lGraphics.drawImage(tick, questLoc[y].x+20, questLoc[y].y, D2WayPainterPanel.this);
//			}
//			}

//			}
//			}
//			break;
//			case 5:
//			for(int f = 0;f<3;f=f+1){
//			for(int y=0;y<iCharacter.getWaypoints()[f][questSlot -1].length();y=y+1){
//			if(iCharacter.getWaypoints()[f][questSlot -1].charAt(y) == '1'){
//			if(f==0){
//			lGraphics.drawImage(tick, questLoc[y].x, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==1){
//			lGraphics.drawImage(tick, questLoc[y].x+10, questLoc[y].y, D2WayPainterPanel.this);
//			}else if(f==2){
//			lGraphics.drawImage(tick, questLoc[y].x+20, questLoc[y].y, D2WayPainterPanel.this);
//			}
//			}

//			}
//			}
//			break;

//			}



		}

		public void paint(Graphics pGraphics)
		{
			super.paint(pGraphics);
			Graphics2D lGraphics = (Graphics2D) pGraphics;

			lGraphics.drawImage(iBackground, 0, 0, D2WayPainterPanel.this);


		}
	}

	class D2CharCursorPainterPanel extends JPanel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3335835168313769724L;
		private Image iBackground;

		public D2CharCursorPainterPanel()
		{
			setSize(BG_CURSOR_WIDTH, BG_CURSOR_HEIGHT);
			Dimension lSize = new Dimension(BG_CURSOR_WIDTH, BG_CURSOR_HEIGHT);
			setPreferredSize(lSize);

			addMouseListener(new MouseAdapter()
			{
				public void mouseReleased(MouseEvent pEvent)
				{
					if ( iCharacter == null )
					{
						return;
					}
					//                    System.err.println("Mouse Clicked: " + pEvent.getX() + ",
					// " + pEvent.getY() );
					if (pEvent.getButton() == MouseEvent.BUTTON1 /*
					 * &&
					 * pEvent.getClickCount() ==
					 * 1
					 */)
					{
						// determine where the mouse click is
						D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, true, true, false);
						if (lItemPanel.getPanel() != -1)
						{
							// if there is an item to grab, grab it
							if (lItemPanel.isItem())
							{
								D2Item lTemp = iCharacter.getCursorItem();
								iCharacter.setCursorItem(null);
								D2ViewClipboard.addItem(lTemp);
								setCursorDropItem();

								// redraw
								build();
								repaint();
							}
							else if (D2ViewClipboard.getItem() != null)
							{
								iCharacter.setCursorItem(D2ViewClipboard.removeItem());
								build();
								repaint();

								setCursorPickupItem();
							}
						}

//						// MBR: for now, disable dropping completely,
//						// it's not working
//						//	// System.err.println("Drop item");
//						//		                        // since there is an item on the mouse, try
//						// to drop it here
//						//		                    	
//						//		                        D2Item lDropItem = D2MouseItem.getItem();
//						//// int lDropWidth = lDropItem.get_width();
//						//// int lDropHeight = lDropItem.get_height();
//						//	// int r = 0, c = 0;
//						//		                        boolean drop = false;
//						//		                        // non-equipped items, handle differently
//						//		                        // because they require a row and column
//						//		                        // equipped items, a bit simpler
//						//		                        // if that equipment slot is empty, update
//						// the
//						//		                        // item's fields and set drop to true
//						//		                        // r and c are set to width and height
//						//		                        // for find_corner to deal with variable-size
//						//		                        // objects in the hands
//						//		                        // (note lack of item-type checking)
//						//	                            if
//						// (!iChar.checkCharPanel(lItemPanel.getPanel(),
//						// 0, 0, lDropItem))
//						//	                            {
//						//	                            	lDropItem.set_location((short) 1);
//						//	                            	lDropItem.set_body_position((short)
//						// (lItemPanel.getPanel() - 10));
//						//	                            	lDropItem.set_col((short) 0);
//						//	                            	lDropItem.set_row((short) 0);
//						//	                            	lDropItem.set_panel((short) 0);
//						//	                                drop = true;
//						//// r = lDropWidth;
//						//// c = lDropHeight;
//						//	                            }
//						//		                        // if the space to set the item is empty
//						//		                        if (drop)
//						//		                        {
//						//		                            iChar.markCharGrid(lDropItem);
//						//		                            // move the item to a new charcter, if needed
//						//	                                iChar.addCharItem(D2MouseItem.removeItem());
//						//		
//						//		                            setModified( true );
//						//		
//						//		                            // redraw
//						//		                            build();
//						//		                            repaint();
//						//		                            
//						//		                            setCursorPickupItem();
//						//		                            //my_char.show_grid();
//						//		                        }
//						}
//						}
					}
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
					if ( iCharacter == null )
					{
						return;
					}
					//            	    restoreSubcomponentFocus();
					D2Item lCurrentMouse = null;

					D2ItemPanel lItemPanel = new D2ItemPanel(pEvent, true, true, false);
					if (lItemPanel.getPanel() != -1)
					{
						if (lItemPanel.isItem())
						{
							lCurrentMouse = lItemPanel.getItem();
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
//								setCursorNormal();
								setCursorDropItem();

//								MBR: for now, disable dropping completely
//								D2Item lDropItem = D2ViewClipboard.getItem();
//								// int lDropWidth = lDropItem.get_width();
//								// int lDropHeight = lDropItem.get_height();

//								boolean drop = false;

//								if (!iChar.checkCharPanel(lItemPanel.getPanel(), 0, 0, lDropItem))
//								{
//								drop = true;
//								}
//								if (drop)
//								{
//								setCursorDropItem();
//								}
//								else
//								{
//								setCursorNormal();
//								}
							}
						}
					}
					else
					{
						setCursorNormal();
					}
					if (lCurrentMouse == null)
					{
						D2CharCursorPainterPanel.this.setToolTipText(null);
					}
					else
					{
						D2CharCursorPainterPanel.this.setToolTipText(lCurrentMouse.itemDumpHtml(false));
					}
				}
			});
		}

		public void build()
		{
			Image lEmptyBackground = D2ImageCache.getImage("cursor.jpg");

			int lWidth = lEmptyBackground.getWidth(D2CharCursorPainterPanel.this);
			int lHeight = lEmptyBackground.getHeight(D2CharCursorPainterPanel.this);

			iBackground = iFileManager.getGraphicsConfiguration().createCompatibleImage(lWidth, lHeight, Transparency.BITMASK);

			Graphics2D lGraphics = (Graphics2D) iBackground.getGraphics();

			lGraphics.drawImage(lEmptyBackground, 0, 0, D2CharCursorPainterPanel.this);

			if ( iCharacter != null )
			{
				D2Item lCursorItem = iCharacter.getCursorItem();
				if ( lCursorItem != null )
				{
					Image lImage = D2ImageCache.getDC6Image(lCursorItem);
					lGraphics.drawImage(lImage, CURSOR_X, CURSOR_Y, D2CharCursorPainterPanel.this);
				}
			}
			repaint();
		}

		public void paint(Graphics pGraphics)
		{
			super.paint(pGraphics);
			Graphics2D lGraphics = (Graphics2D) pGraphics;

			lGraphics.drawImage(iBackground, 0, 0, D2CharCursorPainterPanel.this);
		}
	}

	public void dumpChar() {

		String iChaString = iCharacter.fullDumpStr().replaceAll("<BR>", "\n");
		lDump.setText(iChaString);
		lDump.setCaretPosition(0);
		lDump.validate();
	}

	public D2Character getChar(){
		return iCharacter;
	}

	public void putOnCharacter(int areaCode, ArrayList dropList){

		iCharacter.ignoreItemListEvents();
		int dPanel = 0;
		int rMax = 0;
		int cMax = 0;
		switch(areaCode){
		case 0:
			//stash
			dPanel = 5;
			rMax = D2Character.STASHSIZEY;
			cMax = D2Character.STASHSIZEX;
			break;
		case 1:
			//inv
			dPanel = 1;
			rMax = D2Character.INVSIZEY;
			cMax = D2Character.INVSIZEX;
			break;
		case 2:
			//cube
			dPanel = 4;
			rMax = D2Character.CUBESIZEY;
			cMax = D2Character.CUBESIZEX;
			break;
		}
		try{
			for(int z = dropList.size()-1; z> -1;z--){
				D2Item lDropItem = (D2Item) dropList.get(z);
				for(int x = 0;x<rMax;x++){
					for(int y = 0;y<cMax;y++){
						if(iCharacter.checkCharGrid(dPanel, y, x, lDropItem)){
							lDropItem.set_panel((short) dPanel);
							lDropItem.set_location((short) 0);
							lDropItem.set_body_position((short) 0);
							lDropItem.set_row((short) x);
							lDropItem.set_col((short) y);
							iCharacter.markCharGrid(lDropItem);
							D2ViewClipboard.removeItem(lDropItem);
							iCharacter.addCharItem(lDropItem);
							iCharacter.equipItem(lDropItem);
							paintCharStats();
							x = rMax;
							y = cMax;
						}
					}
				}
			}
		}finally{
			iCharacter.listenItemListEvents();
			iCharacter.fireD2ItemListEvent();	
		}
	}

}