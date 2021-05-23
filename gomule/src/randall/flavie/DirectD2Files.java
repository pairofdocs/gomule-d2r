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
package randall.flavie;

import gomule.d2s.*;
import gomule.d2x.*;
import gomule.gui.D2FileManager;
import gomule.item.*;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import randall.d2files.D2TblFile;
import randall.d2files.D2TxtFile;

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DirectD2Files 
{
	private Flavie	iFlavie;

	public DirectD2Files(Flavie pFlavie)
	{
		iFlavie = pFlavie;
	}

	public void readDirectD2Files(ArrayList pDataObjects, ArrayList pFileNames) throws Exception
	{
		String errStr = "";
		File lMatchedDir = new File(Flavie.sMatchedDir);
		if ( lMatchedDir.exists() && !lMatchedDir.isDirectory() )
		{
			throw new Exception("If there is a file called matched in the Flavie directory, please delete it");
		}
		if ( !lMatchedDir.exists() )
		{
			throw new Exception("The directory called matched is missing, please create it");
		}

		File lDualFP = new File(Flavie.sMatchedDir + "matched.dualFP.txt");
		if ( !lDualFP.exists() )
		{
			lDualFP.createNewFile();
		}
		PrintStream lOutDualFP = new PrintStream(new FileOutputStream(lDualFP));

		try
		{
			iFlavie.initializeFilters();

			if ( pFileNames.size() == 0 )
			{
				throw new Exception("No files selected, please select files in the Files tab.");
			}

			for ( int i = 0; i < pFileNames.size() ; i++ )
			{
				String lD2FileName = (String) pFileNames.get(i);

				if ( lD2FileName == null || "".equals(lD2FileName.trim()) )
				{
					throw new Exception("Empty text filename");
				}
//				File lFile = new File(lD2FileName);
//				if ( !lFile.exists() )
//				{
//					throw new Exception("File " + lD2FileName + " does not exist");
//				}
//				else if ( !lFile.isFile() )
//				{
//					throw new Exception("File " + lD2FileName + " is not a file");
//				}
//				else if ( !lFile.canRead() )
//				{
//					throw new Exception("File " + lD2FileName + " can not be read");
//				}

				ArrayList lItems = null;

				if ( lD2FileName.endsWith(".d2s") )
				{
					try{
						D2Character lCharacter = new D2Character(lD2FileName);
						lItems = lCharacter.getItemList();
					}catch(Exception e){
						errStr = errStr + ("Error with char "+lD2FileName + "\n");
					}
				}
				else if ( lD2FileName.endsWith(".d2x") )
				{
					try{
						D2Stash lStash = new D2Stash(lD2FileName);
						lItems = lStash.getItemList();
					}catch(Exception e){
						errStr = errStr + ("Error with stash "+lD2FileName + "\n");
					}
				}

				if ( lItems != null )
				{
					for ( int lItemNr = 0 ; lItemNr < lItems.size() ; lItemNr++ )
					{
						D2Item lItem = (D2Item) lItems.get(lItemNr);
						matchItem(pDataObjects, lItem, lOutDualFP);
					}
				}
			}
		}
		finally
		{
			iFlavie.finishFilters();

			lOutDualFP.flush();
			lOutDualFP.close();
			if(!errStr.equals("")){
				JOptionPane.showMessageDialog(D2FileManager.getInstance().getContentPane(),
						"Some flavie reports failed (error msg below).\nOutput File: " + System.getProperty("user.dir") + File.separatorChar + iFlavie.getReportName() + ".html" + "\n\n" + errStr, 
						"Fail!", JOptionPane.ERROR_MESSAGE);
			}else{
				
				JOptionPane.showMessageDialog(D2FileManager.getInstance().getContentPane(),
						"Flavie says reports generated successfully.\nFile: " + System.getProperty("user.dir") + File.separatorChar + iFlavie.getReportName() + ".html", 
						"Success!", JOptionPane.INFORMATION_MESSAGE);			
			}
		}
	}

	public void matchItem(ArrayList pDataObjects, D2Item pItem, PrintStream pOutDualFP)
	{
		if ( pItem.getName() == null )
		{
			System.err.println("Item: null");
		}
		ItemObject lFound = null;
		String[] matchStr = null;

//		if ( pItem.getFingerprint() == null /*|| !iFlavie.iAllItems.containsKey(pItem.getAllItemsHash())*/ )
		{
			if ( iFlavie.checkFilters(pItem) )
			{
				if ( pItem.getFingerprint() != null )
				{
					if ( iFlavie.iAllItemsFP.containsKey(pItem.getFingerprint()) )
					{
						D2Item lOriginal = (D2Item) iFlavie.iAllItemsFP.get(pItem.getFingerprint());
						pOutDualFP.println("*** Item matched : " + pItem + " with FP " + pItem.getFingerprint() + "(" + pItem.getFileName() + ") Fingerprint was allready used by another item: " + lOriginal + " from file " + lOriginal.getFileName() + ". Item is different type and both are listed anyways." );
						iFlavie.iDualFPMatched++;
					}
					else
					{
						iFlavie.iAllItemsFP.put(pItem.getFingerprint(), pItem);
					}
					
					//Rainbow facet fix.
					if(pItem.isUnique() && pItem.isJewel()){
						matchStr = new String[2];
						for(int x = 0;x<pItem.getPropCollection().size();x++){
							if(((D2Prop)(pItem.getPropCollection().get(x))).getPNum() == 197){
								matchStr[0] = D2TblFile.getString(D2TxtFile.SKILL_DESC.searchColumns("skilldesc",D2TxtFile.SKILLS.getRow(((D2Prop)(pItem.getPropCollection().get(x))).getPVals()[1]).get("skilldesc")).get("str name"));
								matchStr[1] = "Die";
							}else if(((D2Prop)(pItem.getPropCollection().get(x))).getPNum() == 199){
								matchStr[0] = D2TblFile.getString(D2TxtFile.SKILL_DESC.searchColumns("skilldesc",D2TxtFile.SKILLS.getRow(((D2Prop)(pItem.getPropCollection().get(x))).getPVals()[1]).get("skilldesc")).get("str name"));
								matchStr[1] = "Level Up";
							}
						}
					}
					
				}
				else
				{
					if ( pItem.isRune() )
					{
						Long lRuneCount = (Long) iFlavie.iRuneCount.get(pItem.getName());
						if ( lRuneCount == null )
						{
							lRuneCount = new Long(1);
						}
						else 
						{
							lRuneCount = new Long(lRuneCount.longValue()+1);
						}
						iFlavie.iRuneCount.put(pItem.getName(), lRuneCount);
					}
				}
				for ( int lDataObjectNr = 0 ; lDataObjectNr < pDataObjects.size() ; lDataObjectNr++ )
				{
					ItemObject lItemObject = (ItemObject) pDataObjects.get(lDataObjectNr);

					SubCatObject lSubCatObj = lItemObject.getSubCatObject();
					CatObject lCatObj = null;

					lCatObj = lSubCatObj.getCatObject();

					if ( lCatObj.isSkiller() ) {
						if ( fitsSkiller(lCatObj, lItemObject, pItem) ) {
							lFound = lItemObject;
						}
					} else if ( lItemObject.getName().equals(pItem.getName()) )
					{
						if(pItem.isUnique() && pItem.isJewel() && matchStr[0].equals(lItemObject.getExtraDetect().get(0)) && matchStr[1].equals(lItemObject.getExtraDetect().get(1))){
							lFound = lItemObject;
						}
						
						if ( lCatObj.isUnique() && pItem.isUnique() && !pItem.isJewel() )
						{
							lFound = lItemObject;
						}
						else if ( lCatObj.isSet() && pItem.isSet() )
						{
							lFound = lItemObject;
						}
						else if ( lCatObj.isRune() && pItem.isRune() )
						{
							lFound = lItemObject;
						}
						else if ( lCatObj.isRuneWord() && pItem.isRuneWord() )
						{
							lFound = lItemObject;
						}
						else if ( lCatObj.isMisc() || lCatObj.isGem() )
						{
							lFound = lItemObject;
						}
					}
				}

				if ( lFound != null )
				{
					lFound.addItemInstance(pItem);
				}
				else
				{
					//		    System.err.println("Name not found: " + pItem.getName() );
				}
			}
		}
//		else
//		{
//		D2Item lOriginal = (D2Item) iFlavie.iAllItems.get(pItem.getAllItemsHash());
//		pOutDualFP.println("Item matched : " + pItem + " with FP " + pItem.getFingerprint() + "(" + pItem.getFileName() + ") Fingerprint was allready used by another item: " + lOriginal + " from file " + lOriginal.getFileName() );
//		iFlavie.iDualFPMatched++;
//		}
	}
	
	private boolean fitsSkiller(CatObject pCatObj, ItemObject pItemObject, D2Item pItem) {
		return ( pItem.getName().startsWith(pItemObject.getInfo())); 
		
//		return false;
	}

//	private String getRuneString(String pRuneName)
//	{
//		if ( pRuneName != null && pRuneName.endsWith(" Rune") && pRuneName.length() > 5 )
//		{
//			return pRuneName.substring(0, pRuneName.length()-5);
//		}
//		return pRuneName;
//	}

}
