/*******************************************************************************
 * 
 * Copyright 2007 Randall
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

import gomule.util.*;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

import randall.util.*;

/**
 * @author Marco
 * 
 * Don't allow the dialog to close without a project ! (or stop the application
 * alltogether)
 */
public class D2ProjectSettingsDialog extends JDialog
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6365602357286303263L;
	private D2FileManager        iFileManager;
    private D2Project            iProject;

    private RandallPanel         iContent;

    private JTextField           iNewName;
    private JButton              iSaveNew;
    private DefaultComboBoxModel iProjectModel;
//    private JComboBox            iChangeProject;

    private JRadioButton         iTypeSC;
    private JRadioButton         iTypeHC;
    private JRadioButton         iTypeBoth;
    
    private JRadioButton         iBackupDay;
    private JRadioButton         iBackupWeek;
    private JRadioButton         iBackupMonth;
    private JRadioButton         iBackupNone;
    
//    private JButton 			 iColorUnique;
//    private JButton 			 iColorSet;
//    private JButton 			 iColorRare;
//    private JButton 			 iColorMagical;
//    private JButton 			 iColorCrafted;
//    private JButton 			 iColorSocketed;

    private JTextField           iFlavieOutputReportFileName;
    private JTextField           iFlavieOutputData;
    private JTextField           iFlavieOutputStyle;
    private JTextField           iFlavieOutputTitle;
    private JCheckBox            iIgnoreItems;
    private JCheckBox            iFlavieCountAll;
    private JCheckBox            iFlavieCountStash;
    private JCheckBox            iFlavieCountChar;
    private JCheckBox            iFlavieCountEthereal;

    public D2ProjectSettingsDialog(D2FileManager pFileManager)
    {
        super(pFileManager, true);
        iFileManager = pFileManager;
        setBounds(100, 100, 600, 450);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // close and save everything sofar
        iFileManager.closeWindows();

        iProject = iFileManager.getProject();
//        setTitle();

        iContent = new RandallPanel();

        setContentPane(iContent);

        iNewName = new JTextField();
        iNewName.getDocument().addDocumentListener(new RandallDocumentListener()
        {
            public void check()
            {
                String lNewName = iNewName.getText();
                if (lNewName == null || lNewName.trim().equals(""))
                {
                    iSaveNew.setEnabled(false);
                    iSaveNew.setToolTipText("Type a new project name");
                }
                else
                {
                    boolean lExists = false;
                    for (int i = 0; i < iProjectModel.getSize(); i++)
                    {
                        if (lNewName.equalsIgnoreCase((String) iProjectModel.getElementAt(i)))
                        {
                            lExists = true;
                        }
                    }
                    if (lExists)
                    {
                        iSaveNew.setEnabled(false);
                        iSaveNew.setToolTipText("Project allready exists");
                    }
                    else
                    {
                        iSaveNew.setEnabled(true);
                        iSaveNew.setToolTipText("Create project: " + lNewName);
                    }
                }
            }
        });
        iSaveNew = new JButton(D2ImageCache.getIcon("save.gif"));
        iSaveNew.setEnabled(false);
        iSaveNew.setToolTipText("Type a new project name");
        iSaveNew.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
//                setProject(iNewName.getText());
            }
        });

//        iProjectModel = new DefaultComboBoxModel();
////        checkProjectsModel();
//        iChangeProject = new JComboBox(iProjectModel);
//        iChangeProject.setSelectedItem(iProject.getProjectName());
//        iChangeProject.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent pEvent)
//            {
////                setProject((String) iChangeProject.getSelectedItem());
//            }
//        });

        iTypeSC = new JRadioButton("Softcore (SC) Only");
        iTypeSC.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setType(D2Project.TYPE_SC);
            }
        });
        iTypeHC = new JRadioButton("Hardcore (HC) Only");
        iTypeHC.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setType(D2Project.TYPE_HC);
            }
        });
        iTypeBoth = new JRadioButton("All (SC+HC+Unknown)");
        iTypeBoth.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setType(D2Project.TYPE_BOTH);
            }
        });
        ButtonGroup lType = new ButtonGroup();
        lType.add(iTypeSC);
        lType.add(iTypeHC);
        lType.add(iTypeBoth);
        
        iBackupDay = new JRadioButton("Day");
        iBackupDay.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setBackup(D2Project.BACKUP_DAY);
            }
        });
        iBackupWeek = new JRadioButton("Week");
        iBackupWeek.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setBackup(D2Project.BACKUP_WEEK);
            }
        });
        iBackupMonth = new JRadioButton("Month");
        iBackupMonth.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setBackup(D2Project.BACKUP_MONTH);
            }
        });
        iBackupNone = new JRadioButton("No Backup");
        iBackupNone.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setBackup(D2Project.BACKUP_NONE);
            }
        });
        ButtonGroup lBackup = new ButtonGroup();
        lBackup.add(iBackupDay);
        lBackup.add(iBackupWeek);
        lBackup.add(iBackupMonth);
        lBackup.add(iBackupNone);
        
//        iColorUnique = new JButton("Unique");
//        iTypeBoth.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent pEvent)
//            {
////                iProject.setType(D2Project.TYPE_BOTH);
//            }
//        });

        iFlavieOutputReportFileName = new JTextField();
        iFlavieOutputReportFileName.getDocument().addDocumentListener(new RandallDocumentListener()
        {
            public void check()
            {
                iProject.setReportName(iFlavieOutputReportFileName.getText());
            }
        });
        iFlavieOutputTitle = new JTextField();
        iFlavieOutputTitle.getDocument().addDocumentListener(new RandallDocumentListener()
        {
            public void check()
            {
                iProject.setReportTitle(iFlavieOutputTitle.getText());
            }
        });
        iFlavieOutputData = new JTextField();
        iFlavieOutputData.getDocument().addDocumentListener(new RandallDocumentListener()
        {
            public void check()
            {
                iProject.setDataName(iFlavieOutputData.getText());
            }
        });
        JButton lFlavieDataButton = new JButton("Search");
        lFlavieDataButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                JFileChooser lChooser = new JFileChooser();
                lChooser.setCurrentDirectory(new File("."));
                lChooser.setSelectedFile(new File("standard.dat"));
                RandallFileFilter filter = new RandallFileFilter("Dat files only");
                filter.addExtension("dat");
                lChooser.setFileFilter(filter);
                lChooser.setFileHidingEnabled(true);
                int returnVal = lChooser.showOpenDialog(iFileManager);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    iFlavieOutputData.setText(lChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        iFlavieOutputStyle = new JTextField();
        iFlavieOutputStyle.getDocument().addDocumentListener(new RandallDocumentListener()
        {
            public void check()
            {
                iProject.setStyleName(iFlavieOutputStyle.getText());
            }
        });
        JButton lFlaveStyleButton = new JButton("Search");
        lFlaveStyleButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                JFileChooser lChooser = new JFileChooser();
                lChooser.setCurrentDirectory(new File("."));
                lChooser.setSelectedFile(new File("standard.css"));
                RandallFileFilter filter = new RandallFileFilter("Style sheet files only");
                filter.addExtension("css");
                lChooser.setFileFilter(filter);
                lChooser.setFileHidingEnabled(true);
                int returnVal = lChooser.showOpenDialog(iFileManager);
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    iFlavieOutputStyle.setText(lChooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        
        iIgnoreItems = new JCheckBox("Ignore Common Items on Pickup");
        iIgnoreItems.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setIgnoreItem(iIgnoreItems.isSelected());
            }
        });
        
        
        iFlavieCountAll = new JCheckBox("All");
        iFlavieCountAll.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setCountAll(iFlavieCountAll.isSelected());
            }
        });
        iFlavieCountStash = new JCheckBox("Stash Items");
        iFlavieCountStash.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setCountStash(iFlavieCountStash.isSelected());
            }
        });
        iFlavieCountChar = new JCheckBox("Character Items");
        iFlavieCountChar.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setCountChar(iFlavieCountChar.isSelected());
            }
        });
        iFlavieCountEthereal = new JCheckBox("Ethereal Items");
        iFlavieCountEthereal.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
                iProject.setCountEthereal(iFlavieCountEthereal.isSelected());
            }
        });
        JButton iOk = new JButton("Ok");
        iOk.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent pEvent)
            {
               ((JDialog)iContent.getRootPane().getParent()).dispose();
            }
        });

//        iContent.addToPanel(new JLabel("New Project"), 0, 0, 1, RandallPanel.NONE);
//        iContent.addToPanel(iNewName, 1, 0, 1, RandallPanel.HORIZONTAL);
//        iContent.addToPanel(iSaveNew, 2, 0, 1, RandallPanel.NONE);
//        iContent.addToPanel(new JLabel("Change Project"), 0, 1, 1, RandallPanel.NONE);
//        iContent.addToPanel(iChangeProject, 1, 1, 2, RandallPanel.HORIZONTAL);
        iContent.addToPanel(new JLabel("Type selection: (for stashes naming convection only, names SC/HC should start with SC_ or HC_)"), 0, 10, 3, RandallPanel.NONE);
        
        RandallPanel lTypePanel = new RandallPanel();
        lTypePanel.addToPanel(iTypeSC, 0, 0, 1, RandallPanel.HORIZONTAL);
        lTypePanel.addToPanel(iTypeHC, 1, 0, 1, RandallPanel.HORIZONTAL);
        lTypePanel.addToPanel(iTypeBoth, 2, 0, 1, RandallPanel.HORIZONTAL);
        iContent.addToPanel(lTypePanel, 0, 20, 3, RandallPanel.HORIZONTAL);

        RandallPanel lBackupPanel = new RandallPanel();
        lBackupPanel.addToPanel(new JLabel("Create backupdir for each: "), 0, 0, 1, RandallPanel.NONE);
        lBackupPanel.addToPanel(iBackupDay, 1, 0, 1, RandallPanel.HORIZONTAL);
        lBackupPanel.addToPanel(iBackupWeek, 2, 0, 1, RandallPanel.HORIZONTAL);
        lBackupPanel.addToPanel(iBackupMonth, 3, 0, 1, RandallPanel.HORIZONTAL);
        lBackupPanel.addToPanel(iBackupNone, 4, 0, 1, RandallPanel.HORIZONTAL);
        lBackupPanel.addToPanel(iIgnoreItems, 0, 1, 1, RandallPanel.HORIZONTAL);
        iContent.addToPanel(lBackupPanel, 0, 30, 3, RandallPanel.HORIZONTAL);

        
        
//        RandallPanel lColorPanel = new RandallPanel();
        
        RandallPanel lFlaviePanel = new RandallPanel(true);
        lFlaviePanel.setBorder("FLAVIE settings");

        lFlaviePanel.addToPanel(new JLabel("Report name (html): "), 0, 6, 1, RandallPanel.NONE);
        lFlaviePanel.addToPanel(iFlavieOutputReportFileName, 1, 6, 2, RandallPanel.HORIZONTAL);
        lFlaviePanel.addToPanel(new JLabel("Title of Report: "), 0, 8, 1, RandallPanel.NONE);
        lFlaviePanel.addToPanel(iFlavieOutputTitle, 1, 8, 2, RandallPanel.HORIZONTAL);
        lFlaviePanel.addToPanel(new JLabel("Data file: "), 0, 20, 1, RandallPanel.NONE);
        lFlaviePanel.addToPanel(iFlavieOutputData, 1, 20, 1, RandallPanel.HORIZONTAL);
        lFlaviePanel.addToPanel(lFlavieDataButton, 2, 20, 1, RandallPanel.NONE);
        lFlaviePanel.addToPanel(new JLabel("Styles file: "), 0, 21, 1, RandallPanel.NONE);
        lFlaviePanel.addToPanel(iFlavieOutputStyle, 1, 21, 1, RandallPanel.HORIZONTAL);
        lFlaviePanel.addToPanel(lFlaveStyleButton, 2, 21, 1, RandallPanel.NONE);
        lFlaviePanel.addToPanel(new JLabel("Count: "), 0, 30, 1, RandallPanel.NONE);
        lFlaviePanel.addToPanel(iFlavieCountAll, 1, 30, 1, RandallPanel.HORIZONTAL);
        lFlaviePanel.addToPanel(iFlavieCountStash, 1, 31, 1, RandallPanel.HORIZONTAL);
        lFlaviePanel.addToPanel(iFlavieCountChar, 1, 32, 1, RandallPanel.HORIZONTAL);
        lFlaviePanel.addToPanel(iFlavieCountEthereal, 1, 33, 1, RandallPanel.HORIZONTAL);

        
        iContent.addToPanel(lFlaviePanel, 0, 50, 3, RandallPanel.HORIZONTAL);
        iContent.addToPanel(iOk, 0, 51, 0, RandallPanel.NONE);
        iContent.finishDefaultPanel();
        
        setProjectValues();
    }
    
    protected void setProjectValues()
    {
        switch (iProject.getType())
        {
        case D2Project.TYPE_SC:
            iTypeSC.setSelected(true);
            break;
        case D2Project.TYPE_HC:
            iTypeHC.setSelected(true);
            break;
        default:
            iTypeBoth.setSelected(true);
            break;
        }
        
        switch (iProject.getBackup())
        {
        case D2Project.BACKUP_DAY:
            iBackupDay.setSelected(true);
            break;
        case D2Project.BACKUP_MONTH:
            iBackupMonth.setSelected(true);
            break;
        case D2Project.BACKUP_NONE:
        	iBackupNone.setSelected(true);
        	break;
        default:
            iBackupWeek.setSelected(true);
            break;
        }
        
        iFlavieOutputReportFileName.setText(iProject.getReportName());
        iFlavieOutputTitle.setText(iProject.getReportTitle());
        iFlavieOutputData.setText(iProject.getDataName());
        iFlavieOutputStyle.setText(iProject.getStyleName());
        iIgnoreItems.setSelected(iProject.getIgnoreItems());
        iFlavieCountAll.setSelected(iProject.isCountAll());
        iFlavieCountStash.setSelected(iProject.isCountStash());
        iFlavieCountChar.setSelected(iProject.isCountChar());
        iFlavieCountEthereal.setSelected(iProject.isCountEthereal());
    }

}

abstract class RandallDocumentListener implements DocumentListener
{
    abstract void check();

    public void insertUpdate(DocumentEvent e)
    {
        check();
    }

    public void removeUpdate(DocumentEvent e)
    {
        check();
    }

    public void changedUpdate(DocumentEvent e)
    {
        check();
    }

}