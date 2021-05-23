/*
 * Created on 01.09.2004
 */
package randall.flavie.filters;

import java.io.*;
import java.util.*;

import randall.flavie.*;
import randall.util.*;

/**
 * @author mbr
 */
public class FlavieDupeFilter implements FlavieItemFilter
{
//	private String			iDupeFile;
	private PrintStream 	iDupeOut = null;
	private HashMap 		iDupeList = new HashMap();
	private int				iDupeCounter = 0;
	
	public FlavieDupeFilter(String pDupeName, Reader pDupeFile) throws Exception
	{
//		iDupeFile = pDupeFile;
		BufferedReader lIn = new BufferedReader(pDupeFile);
		String lLine = lIn.readLine();
		while ( lLine != null )
		{
			// skip empty lines
			if ( !lLine.trim().equals("") )
			{
//				System.err.println("Test: " + lLine );
				// Find fingerprint in this line
				ArrayList lSplit = RandallUtil.split(lLine, " ", false);
				for ( int i = 0 ; i < lSplit.size() ; i++ )
				{
					if ( (String) lSplit.get(i) != null )
					{
						String lSplitted = ((String) lSplit.get(i)).trim();
						if ( lSplitted.startsWith("0x") )
						{
							iDupeList.put(lSplitted, lLine.trim() );
						}
					}
				}
			}
			lLine = lIn.readLine();
		}
		lIn.close();
		System.err.println("Dupelist " + pDupeName + " contains " + iDupeList.size() + " items." );
	}
	
	public HashMap getDupeList()
	{
		return iDupeList;
	}
	
	public void initialize() throws Exception
	{
		if ( iDupeOut != null )
		{
			throw new Exception("Dupe file allready initialised"); 
		}
		iDupeOut = new PrintStream(new FileOutputStream(Flavie.sMatchedDir + "matched.dupe.txt"));
		iDupeCounter = 0;
//		iDupeOut.println("Start dupe detection");
	}
	
	public void finish() throws Exception
	{
		if ( iDupeOut == null )
		{
			throw new Exception("Dupe file not initialised"); 
		}
//		iDupeOut.println("Dupe detection is finished");
		iDupeOut.close();
		iDupeOut = null;
	}
	
	public boolean check(D2ItemInterface pItemFound)
	{
		if ( iDupeList.containsKey(pItemFound.getFingerprint()) )
		{
			iDupeOut.println("Item " + pItemFound.getFingerprint() + "/" + pItemFound.getName() + " from file " + pItemFound.getFileName() + " is listed as a dupe");
			iDupeCounter++;
			return false;
		}
		return true;
	}
	
	public boolean check(String pFingerprint, String pItemname)
	{
		if ( iDupeList.containsKey(pFingerprint) )
		{
			iDupeOut.println("Item " + pFingerprint + "/" + pItemname + " is listed as a dupe");
			iDupeCounter++;
			return false;
		}
		return true;
	}
	public int getDupeCount()
	{
		return iDupeCounter;
	}
}
