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
package randall.util;

import java.io.*;
import java.util.*;

public class RandallUtil
{
//	private static final String ICON_PATH = "/randall/images/";
//	private static HashMap iIcons = new HashMap();
	
//   public static ImageIcon getIcon( String iconName )
//    throws IOException
//    {
//        if( iIcons.containsKey( iconName))
//        {
//            return (ImageIcon) iIcons.get( iconName);
//        }
//
//        ImageIcon icon = null;
//        icon = loadImageIcon( iconName);
//        iIcons.put( iconName, icon);
//        return icon;
//    }
	
//    private static ImageIcon loadImageIcon( String iconName)
//    throws IOException
//	{
//	    ImageIcon icon = null;
//	    //Class klasse = getClass();
//	    InputStream inputStream =  InputStream.class.getResourceAsStream(ICON_PATH + iconName);
//	    if (inputStream != null)
//	    {
//	        byte[] buffer = new byte[0];
//	        byte[] tmpbuf = new byte[1024];
//	        while (true)
//	        {
//	            int laenge = inputStream.read(tmpbuf);
//	            if (laenge <= 0)
//	            {
//	                break;
//	            }
//	            byte[] newbuf = new byte[buffer.length + laenge];
//	            System.arraycopy(buffer, 0, newbuf, 0, buffer.length);
//	            System.arraycopy(tmpbuf, 0, newbuf, buffer.length, laenge);
//	            buffer = newbuf;
//	        }
//	        //create image
//	        icon = new ImageIcon(buffer);
//	        inputStream.close();
//	    }
//	    return icon;
//	}
    
	public static String merge(ArrayList pArrayList, String pJoin)
	{
		String lReturn = "";
		if ( pArrayList.size() > 0 )
		{
			lReturn += (String) pArrayList.get(0);
			for( int i = 1 ; i < pArrayList.size() ; i++ )
			{
				lReturn += pJoin + (String) pArrayList.get(i);
			}
		}
		return lReturn;
	}
	
//	public static int count(String pString, String pOccurance, boolean pIgnoreCase)
//	{
//	    int lCount = 0;
//	    
//	    int lIndex = pString.indexOf(pOccurance, 0);
//	    while ( lIndex != -1 )
//	    {
//	        lCount++;
//	        lIndex = pString.indexOf(pOccurance, lIndex+1);
//	    }
//	    
//	    return lCount;
//	}
	
    public static ArrayList split(String pString, String pSeparator, boolean pIgnoreCase)
    {
        ArrayList   lSplit = new ArrayList();
        int         lIndex = 0;
        int         lSeparator;
        String      lSubString;
        
        // For (faster) uppercase comparing
        String pCompStr;
        String pCompSep;
        if ( pIgnoreCase )
        {
        	pCompStr = (pString==null)?null:pString.toLowerCase();
        	pCompSep = (pSeparator==null)?",":pSeparator.toLowerCase();
        }
        else
        {
        	pCompStr = pString;
        	pCompSep = pSeparator;
        }
        
        // In case of lengths not being equal, split while not ignoring case.
        if ( pString != null && pCompStr != null && pCompStr.length() != pString.length() )
        {
        	// The compare without lowercase
        	pCompStr = pString;
        	pCompSep = pSeparator;
        }
        
        if ( pString != null )
        {
            while ( lIndex < pString.length() )
            {
                // find first Separator starting from lIndex
                lSeparator = pCompStr.indexOf(pCompSep, lIndex); // pCompStr.length()
                if ( lSeparator == -1 )
                {
                    lSubString = pString.substring(lIndex);
                    lIndex = pString.length();
                }
                else
                {
                    lSubString = pString.substring(lIndex, lSeparator);
                    lIndex = lSeparator + pSeparator.length();
                }
                lSplit.add( lSubString.trim() );
            }
        }
        return lSplit;
    }
    
    public static String fill(int pValue, int pDigits)
    {
        String lValue = Integer.toString(pValue);
        
        if ( lValue.length() > pDigits )
        {
            return lValue.substring(lValue.length()-pDigits);
        }
        
        while ( lValue.length() < pDigits )
        {
            lValue = "0" + lValue;
        }
        
        return lValue;
    }
    
    public static void checkDir(String pDir) throws Exception
    {
        File lDir = new File(pDir);
        
        if ( !lDir.exists() )
        {
            if ( !lDir.mkdirs() )
            {
                throw new Exception("Can not create backup dir: " + pDir );
            }
        }
        if ( !lDir.isDirectory() )
        {
            throw new Exception("File exists with name of backup dir: " + pDir );
        }
        if ( !lDir.canRead() )
        {
            throw new Exception("Can not read backup dir: " + pDir );
        }
        if ( !lDir.canWrite() )
        {
            throw new Exception("Can not write backup dir: " + pDir );
        }
    }

}
