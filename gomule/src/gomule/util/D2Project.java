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
package gomule.util;

import gomule.gui.*;
import gomule.item.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import randall.util.*;

/**
 * @author Marco
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class D2Project
{
	public static final String PROJECTS_DIR = "projects";

	private D2FileManager	   iFileManager;

	private String             iProjectName;
	private String             iProjectDir;
	private File               iFile;
	private JFileChooser       iCharDialog;
	private JFileChooser       iStashDialog;

	private ArrayList          iCharList    = new ArrayList();
	private ArrayList          iStashList   = new ArrayList();

	private int                iBank;

	public static final int    TYPE_SC      = 1;
	public static final int    TYPE_HC      = 2;
	public static final int    TYPE_BOTH    = 3;
	private int                iType        = TYPE_BOTH;

	public static final int    BACKUP_DAY   = 1;
	public static final int    BACKUP_WEEK  = 2;
	public static final int    BACKUP_MONTH = 3;
	public static final int    BACKUP_NONE  = 4;
	private int                iBackup      = BACKUP_WEEK;

	private String iReportName;
	private String iReportTitle;
	private String iDataName;
	private String iStyleName;
	private boolean iCountAll;
	private boolean iCountStash;
	private boolean iCountChar;
	private boolean iCountEthereal;

	private boolean iIgnoreItems;

	public D2Project(D2FileManager pFileManager, String pProjectName)
	{
		iFileManager = pFileManager;
		boolean lNew = false;

		iProjectName = pProjectName;
		iProjectDir = PROJECTS_DIR + File.separator + iProjectName;
		File lProjectDir = new File(iProjectDir);
		if (!lProjectDir.exists())
		{
			// if it doesn't exist, make it
			lProjectDir.mkdir();
			lNew = true;
		}

		iFile = new File(iProjectDir + File.separator + "project.properties");
		Properties lLoadProperties = new Properties();
		if (iFile.exists() && iFile.canRead())
		{
			try
			{
				FileInputStream lInputStream = new FileInputStream(iFile);
				lLoadProperties.load(lInputStream);
				lInputStream.close();
			}
			catch (Exception pEx)
			{
				D2FileManager.displayErrorDialog(pEx);
			}
		}
		else
		{
			lNew = true;
		}

		String lCharDir = lLoadProperties.getProperty("CharDir", ".");
		iCharDialog = new JFileChooser(lCharDir);
		RandallFileFilter lCharFilter = new RandallFileFilter(".d2s files");
		lCharFilter.addExtension("d2s");
		iCharDialog.setFileFilter(lCharFilter);
		iCharDialog.setFileHidingEnabled(true);

		String lStashDir = lLoadProperties.getProperty("StashDir", ".");
		iStashDialog = new JFileChooser(lStashDir);
		RandallFileFilter lStashFilter = new RandallFileFilter(".d2x files");
		lStashFilter.addExtension("d2x");
		iStashDialog.setFileFilter(lStashFilter);
		iStashDialog.setFileHidingEnabled(true);

		boolean lLoading = true;
		iCharList.clear();
		for (int i = 0; lLoading; i++)
		{
			String lChar = lLoadProperties.getProperty("char." + i);
			if (lChar != null)
			{
				iCharList.add(lChar);
			}
			else
			{
				lLoading = false;
			}
		}
		Collections.sort(iCharList);

		lLoading = true;
		iStashList.clear();
		for (int i = 0; lLoading; i++)
		{
			String lStash = lLoadProperties.getProperty("stash." + i);
			if (lStash != null)
			{
				iStashList.add(lStash);
			}
			else
			{
				lLoading = false;
			}
		}
		Collections.sort(iStashList);


		if ( lNew )
		{
			iBank = 0;
			iType = TYPE_BOTH;
			iIgnoreItems = true;
			iBackup = BACKUP_WEEK;
			iReportName = "Report";
			iReportTitle = "Flavie Report";
			iDataName = "standard.dat";
			iStyleName = "standard.css";
			iCountAll = true;
			iCountStash = true;
			iCountChar = true;
			iCountEthereal = true;
		}
		else
		{
			String lGold = lLoadProperties.getProperty("bank");
			iBank = 0;
			if (lGold != null)
			{
				try
				{
					iBank = Integer.parseInt(lGold);
				}
				catch (Exception pEx)
				{
					iBank = 0;
					D2FileManager.displayErrorDialog(pEx);
				}
			}

			String lType = lLoadProperties.getProperty("type");
			iType = TYPE_BOTH;
			if (lType != null)
			{
				try
				{
					iType = Integer.parseInt(lType);
				}
				catch (Exception pEx)
				{
					iType = TYPE_BOTH;
					D2FileManager.displayErrorDialog(pEx);
				}
			}

			String lBackup = lLoadProperties.getProperty("backup");

			try
			{
				if(lLoadProperties.getProperty("propDisplay").equals("true")){
					iIgnoreItems = true;
				}else{
					iIgnoreItems = false;
				}
			}
			catch (Exception pEx)
			{
				iIgnoreItems = true;
			}

			iBackup = BACKUP_WEEK;
			if (lBackup != null)
			{
				try
				{
					iBackup = Integer.parseInt(lBackup);
				}
				catch (Exception pEx)
				{
					iBackup = BACKUP_WEEK;
					D2FileManager.displayErrorDialog(pEx);
				}
			}

			iReportName = lLoadProperties.getProperty("ReportName");
			iReportTitle = lLoadProperties.getProperty("ReportTitle");
			iDataName = lLoadProperties.getProperty("DataName");
			iStyleName = lLoadProperties.getProperty("StyleName");

			iCountAll = getBooleanFromString(lLoadProperties.getProperty("countAll"), true);
			iCountStash = getBooleanFromString(lLoadProperties.getProperty("countStash"), true);
			iCountChar = getBooleanFromString(lLoadProperties.getProperty("countChar"), true);
			iCountEthereal = getBooleanFromString(lLoadProperties.getProperty("countEthereal"), true);

		} // end else not new
	}

	private boolean delDir(File dir){

		boolean delFail = true;    	
		if (dir.exists() && dir.canRead()){
			if (dir.isDirectory ()){
	            String[] dirCont = dir.list();
				for(int x= 0;x<dirCont.length;x++){
					delFail = delDir(new File(dir, dirCont[x]));
				}
			}
			if(delFail){
				return dir.delete();
			}
		}
		return false;
	}

	public boolean delProj(){

		return delDir(new File(iProjectDir));
	}

	private boolean getBooleanFromString(String pString, boolean pDefault)
	{
		if ( pString != null )
		{
			if ( pString.equalsIgnoreCase("true") )
			{
				return true;
			}
			if ( pString.equalsIgnoreCase("false") )
			{
				return false;
			}
		}
		return pDefault;
	}

	public int getBankValue()
	{
		return iBank;
	}

	public void setBankValue(int pBank)
	{
		iBank = pBank;
		D2ViewClipboard.refreshBank(this);
	}

	public int getType()
	{
		return iType;
	}

	public void setType(int pType)
	{
		iType = pType;
	}

	public int getBackup()
	{
		return iBackup;
	}

	public void setIgnoreItem(boolean iIgnoreItems)
	{
		this.iIgnoreItems =  iIgnoreItems;
	}

	public void setBackup(int pBackup)
	{
		iBackup = pBackup;
	}

	public ArrayList getCharList()
	{
		return iCharList;
	}

	public ArrayList getStashList()
	{
		return iStashList;
	}

	public void addChar(String pCharFileName)
	{
		if (!iCharList.contains(pCharFileName))
		{
			iCharList.add(pCharFileName);
			Collections.sort(iCharList);
			iFileManager.getViewProject().refreshTreeModel(true, false);
		}
	}

	public void addStash(String pStashFileName)
	{
		if ( pStashFileName.equalsIgnoreCase("all") )
		{
			return;
		}
		if (!iStashList.contains(pStashFileName))
		{
			iStashList.add(pStashFileName);
			Collections.sort(iStashList);
			iFileManager.getViewProject().refreshTreeModel(false, true);
//			D2ItemListAll lListAll = iFileManager.getAllItemList();
//			if ( lListAll != null )
//			{
//			lListAll.addFileName(pStashFileName);
//			}
		}
	}

	public void deleteCharStash(String pFilename)
	{
		iCharList.remove(pFilename);
		iStashList.remove(pFilename);
		iFileManager.closeFileName(pFilename);
		iFileManager.getViewProject().refreshTreeModel(false, false);
//		D2ItemListAll lListAll = iFileManager.getAllItemList();
//		if ( lListAll != null )
//		{
//		lListAll.removeFileName(pFilename);
//		}
	}

	public String getProjectName()
	{
		return iProjectName;
	}

	public String getProjectDir()
	{
		return iProjectDir;
	}

	public JFileChooser getCharDialog()
	{
		return iCharDialog;
	}

	public JFileChooser getStashDialog()
	{
		return iStashDialog;
	}

	public void saveProject()
	{
		// clear old
		Properties lSaveProperties = new Properties();
		String lCharPath = ".";
		try
		{
			lCharPath = iCharDialog.getCurrentDirectory().getCanonicalPath();
		}
		catch (IOException pEx)
		{
			D2FileManager.displayErrorDialog(pEx);
		}
		String lStashPath = ".";
		try
		{
			lStashPath = iStashDialog.getCurrentDirectory().getCanonicalPath();
		}
		catch (IOException pEx)
		{
			D2FileManager.displayErrorDialog(pEx);
		}

		lSaveProperties.put("CharDir", lCharPath);
		lSaveProperties.put("StashDir", lStashPath);

		for (int i = 0; i < iCharList.size(); i++)
		{
			lSaveProperties.put("char." + i, iCharList.get(i));
		}
		for (int i = 0; i < iStashList.size(); i++)
		{
			lSaveProperties.put("stash." + i, iStashList.get(i));
		}

		lSaveProperties.put("bank", Integer.toString(iBank));
		lSaveProperties.put("type", Integer.toString(iType));
		lSaveProperties.put("backup", Integer.toString(iBackup));

		// flavie settings
		lSaveProperties.put("ReportName", getStringSave(iReportName));
		lSaveProperties.put("ReportTitle", getStringSave(iReportTitle));
		lSaveProperties.put("DataName", getStringSave(iDataName));
		lSaveProperties.put("StyleName", getStringSave(iStyleName));

		lSaveProperties.put("countAll", getStringFromBoolean(iCountAll) );
		lSaveProperties.put("countStash", getStringFromBoolean(iCountStash) );
		lSaveProperties.put("countChar", getStringFromBoolean(iCountChar) );
		lSaveProperties.put("countEthereal", getStringFromBoolean(iCountEthereal) );
		lSaveProperties.put("propDisplay", getStringFromBoolean(iIgnoreItems) );

		try
		{ // iFile.getCanonicalPath()
			if (!iFile.exists())
			{
				iFile.createNewFile();
			}
			if (iFile.canWrite())
			{
				FileOutputStream lOutputStream = new FileOutputStream(iFile);
				lSaveProperties.store(lOutputStream, "#saved by GoMule");
				lOutputStream.flush();
				lOutputStream.close();
			}
		}
		catch (Exception pEx)
		{
			D2FileManager.displayErrorDialog(pEx);
		}

	}

	private String getStringSave(String pString)
	{
		if ( pString == null )
		{
			return "";
		}
		return pString;
	}

	private String getStringFromBoolean(boolean pBoolean)
	{
		return (pBoolean)?"true":"false";
	}


	/**
	 * @return Returns the countAll.
	 */
	public boolean isCountAll()
	{
		return iCountAll;
	}
	/**
	 * @param pCountAll The countAll to set.
	 */
	public void setCountAll(boolean pCountAll)
	{
		iCountAll = pCountAll;
	}
	/**
	 * @return Returns the countChar.
	 */
	public boolean isCountChar()
	{
		return iCountChar;
	}
	/**
	 * @param pCountChar The countChar to set.
	 */
	public void setCountChar(boolean pCountChar)
	{
		iCountChar = pCountChar;
	}
	/**
	 * @return Returns the countEthereal.
	 */
	public boolean isCountEthereal()
	{
		return iCountEthereal;
	}
	/**
	 * @param pCountEthereal The countEthereal to set.
	 */
	public void setCountEthereal(boolean pCountEthereal)
	{
		iCountEthereal = pCountEthereal;
	}
	/**
	 * @return Returns the countStash.
	 */
	public boolean isCountStash()
	{
		return iCountStash;
	}
	/**
	 * @param pCountStash The countStash to set.
	 */
	public void setCountStash(boolean pCountStash)
	{
		iCountStash = pCountStash;
	}
	/**
	 * @return Returns the dataName.
	 */
	public String getDataName()
	{
		return iDataName;
	}
	/**
	 * @param pDataName The dataName to set.
	 */
	public void setDataName(String pDataName)
	{
		iDataName = pDataName;
	}
	/**
	 * @return Returns the reportName.
	 */
	public String getReportName()
	{
		return iReportName;
	}
	/**
	 * @param pReportName The reportName to set.
	 */
	public void setReportName(String pReportName)
	{
		iReportName = pReportName;
	}
	/**
	 * @return Returns the reportTitle.
	 */
	public String getReportTitle()
	{
		return iReportTitle;
	}
	/**
	 * @param pReportTitle The reportTitle to set.
	 */
	public void setReportTitle(String pReportTitle)
	{
		iReportTitle = pReportTitle;
	}
	/**
	 * @return Returns the styleName.
	 */
	public String getStyleName()
	{
		return iStyleName;
	}
	/**
	 * @param pStyleName The styleName to set.
	 */
	public void setStyleName(String pStyleName)
	{
		iStyleName = pStyleName;
	}

	public boolean getIgnoreItems(){
		return iIgnoreItems;
	}

	public Color getItemColor(D2Item pItem)
	{
		if ( pItem.isUnique() )
		{
			return Color.yellow.darker();
		}
		if ( pItem.isSet() )
		{
			return Color.green.darker();
		}
		if ( pItem.isRare() )
		{
			return Color.yellow.brighter();
		}
		if ( pItem.isMagical() )
		{
			return Color.blue;
		}
		if ( pItem.isCrafted() )
		{
			return new Color(255,140,0);
		}
		if ( pItem.isEthereal() || pItem.isSocketed() )
		{
			return Color.gray;
		}
		return null;
	}

	public boolean clearProj() {
		
		for(int x= 0;x<iCharList.size();x++){
			deleteCharStash((String) iCharList.get(x));
			x--;
		}
		
		for(int x= 0;x<iStashList.size();x++){
			deleteCharStash((String) iStashList.get(x));
			x--;
		}
		//Error handling should be here, but bla bla bla.
		return true;
	}
}