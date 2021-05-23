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

import java.io.*;
import java.util.*;
import java.util.zip.*;

import randall.d2files.*;
import randall.flavie.filters.*;
import randall.util.*;

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Flavie
{
	public static final String sMatchedDir = "."+File.separator;
	
    private String iReportName;
//    private String iReportTitle;
    private String iDataFile;
//    private String iStyleFile;
    
//	protected HashMap iAllItems = new HashMap();
	private ArrayList iDatFile = new ArrayList();
	protected HashMap iAllItemsFP = new HashMap();
	protected HashMap	iRuneCount = new HashMap();
    
	protected ArrayList iFilters = new ArrayList();
	
	private DataFileBuilder	iDataFileBuilder;
	private DirectD2Files	iDirectD2;
	private ReportBuilder	iReportBuilder;
	
	protected int iNotMatched = 0;
	protected int iNotMatchedType = 0;
	protected int iNormalMatched = 0;
	protected int iMultipleMatched = 0;
	protected int iDualFPMatched = 0;
	protected int iDupeMatched = 0;
	
    public Flavie(String pReportName, String pReportTitle, String pDataFile, String pStyleFile, ArrayList pFileNames, boolean pCountAll, boolean pCountEthereal, boolean pCountStash, boolean pCountChar) throws Exception
    {
        iReportName = pReportName;
//        iReportTitle = pReportTitle;
        iDataFile = pDataFile;
//        iStyleFile = pStyleFile;
        
        iReportBuilder = new ReportBuilder(this);
        iDirectD2 = new DirectD2Files(this);
        iDataFileBuilder = new DataFileBuilder(this);
        
		ArrayList lDataFileObjects = iDataFileBuilder.readDataFileObjects(iDataFile, iDatFile);
	    iDirectD2.readDirectD2Files(lDataFileObjects, pFileNames);
        
		File lDupeDirList = new File("dupelists");
		File lDupeFiles[] = lDupeDirList.listFiles();
		
		for ( int i = 0 ; i < lDupeFiles.length ; i++ )
		{
		    if ( lDupeFiles[i].getCanonicalPath().endsWith(".txt") )
		    {
		        iFilters.add(new FlavieDupeFilter(lDupeFiles[i].getCanonicalPath(), new FileReader(lDupeFiles[i].getCanonicalPath())));
		    }
		    if ( lDupeFiles[i].getCanonicalPath().endsWith(".zip") )
		    {
		        ZipFile lZip = new ZipFile(lDupeFiles[i].getCanonicalPath());
		        Enumeration lEnum = lZip.entries();
		        while ( lEnum.hasMoreElements() )
		        {
		            ZipEntry lEntry = (ZipEntry) lEnum.nextElement();
		            if ( lEntry.getName().endsWith(".txt") )
		            {
				        iFilters.add( new FlavieDupeFilter( lDupeFiles[i].getCanonicalPath()+":"+lEntry.getName(), new InputStreamReader(lZip.getInputStream(lEntry)) ));
		            }
		        }
		    }
		}
        
	
		iReportBuilder.buildReport(pReportTitle, pReportName, pDataFile, pStyleFile, iDatFile, pCountAll, pCountEthereal, pCountStash, pCountChar);
    }

	public boolean checkForRuneWord(String pName, String pRunes)
	{
//	    System.err.println("checkForRuneWord(" + pName + ", " + pRunes);
	    ArrayList lList = RandallUtil.split(pRunes, "-", false);
	    if ( lList.size() <= 1 )
	    {
	        return false;
	    }
//	    System.err.println("checkForRuneWord(" + pName + ", " + pRunes);
	    ArrayList lRuneList = new ArrayList();
	    for ( int i = 0 ; i < lList.size() ; i++ )
	    {
	        D2TxtFileItemProperties lProps = D2TxtFile.MISC.searchColumns("name", (String) lList.get(i) + " Rune");
	        if ( lProps == null )
	        {
	            return false;
	        }
	        lRuneList.add( lProps.get("code") );
	    }
//	    System.err.println("checkForRuneWord(" + pName + ", " + pRunes);
        return ( D2TxtFile.RUNES.searchRuneWord(lRuneList) != null);
	}
	
	public void initializeFilters() throws Exception
	{
		for ( int i = 0 ; i < iFilters.size() ; i++ )
		{
			((FlavieItemFilter) iFilters.get(i)).initialize();
		}
	}
	
	public void finishFilters() throws Exception
	{
		for ( int i = 0 ; i < iFilters.size() ; i++ )
		{
			if ( iFilters.get(i) instanceof FlavieDupeFilter )
			{
				iDupeMatched += ((FlavieDupeFilter) iFilters.get(i)).getDupeCount();
			}
			((FlavieItemFilter) iFilters.get(i)).finish();
		}
	}
	
	public boolean checkFilters(D2ItemInterface pItemFound)
	{
		boolean lAllFilters = true;
		for ( int i = 0 ; i < iFilters.size() && lAllFilters; i++ )
		{
			lAllFilters = ((FlavieItemFilter) iFilters.get(i)).check(pItemFound);
		}
		return lAllFilters;
	}
	
	public String getReportName(){
		
		return iReportName;
		
	}
	
	public boolean checkFilters(String pFingerprint, String pItemname)
	{
		boolean lAllFilters = true;
		for ( int i = 0 ; i < iFilters.size() && lAllFilters; i++ )
		{
			lAllFilters = ((FlavieItemFilter) iFilters.get(i)).check( pFingerprint, pItemname);
		}
		return lAllFilters;
	}
	
	
}
