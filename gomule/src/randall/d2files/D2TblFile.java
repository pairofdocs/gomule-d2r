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
package randall.d2files;


import gomule.gui.D2FileManager;

import java.io.*;
import java.util.*;


/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2TblFile 
{
    private static String sMod;
    
    private static D2TblFile	ENG_PATCH;
    private static D2TblFile	ENG_EXP;
    private static D2TblFile	ENG_STRING;
    
    private HashMap	iHashMap = new HashMap();
    
    public static void readAllFiles(String pMod)
    {
        sMod = pMod;
        ENG_PATCH = new D2TblFile("patchstring");
        ENG_EXP = new D2TblFile("expansionstring");
        ENG_STRING = new D2TblFile("string");
        
//        System.err.println("Test: " + getString("chaos"));
    }
    
    public static String getString(String pKey)
    {
        String lValue = ENG_PATCH.getValue(pKey);
        
        if ( lValue == null )
        {
            lValue = ENG_EXP.getValue(pKey);
            if ( lValue == null )
            {
                lValue = ENG_STRING.getValue(pKey);
            }
        }
        
        return lValue;
    }
    
    private D2TblFile(String pFileName)
    {
        try 
        {
//            System.err.println("Attempt to read tbl file");
            String lFileName = sMod+File.separator+pFileName+".tbl";
            
            D2FileReader lFileReader = new D2FileReader(lFileName);

            lFileReader.getCounterInt(8);
//            System.err.println("CRCOffSet: " + lFileReader.getCounterInt(8));
            
            lFileReader.increaseCounter(8);
            
            int lNumElementsOffset = lFileReader.getCounterInt(16);
//            System.err.println("NumElementsOffset: " + lNumElementsOffset);
            int lHashTableSizeOffset = lFileReader.getCounterInt(16);
//            System.err.println("HashTableSizeOffset: " + lHashTableSizeOffset );
            
            lFileReader.increaseCounter(16);
            
            lFileReader.getCounterInt(8);
//            System.err.println("VersionOffset: " + lFileReader.getCounterInt(8));
            lFileReader.getCounterInt(16);
//            System.err.println("StringStartOffset: " + lFileReader.getCounterInt(16));
            
            lFileReader.increaseCounter(16);
            
            lFileReader.getCounterInt(16);
//            System.err.println("NumLoopsOffset: " + lFileReader.getCounterInt(16));

            lFileReader.increaseCounter(16);
            
            lFileReader.getCounterInt(16);
//            System.err.println("FileSizeOffset: " + lFileReader.getCounterInt(16));

            lFileReader.increaseCounter(16);
            
            for ( int i = 0 ; i < lNumElementsOffset ; i++ )
            {
                lFileReader.increaseCounter(2*8);
            }
            
            for ( int i = 0 ; i < lHashTableSizeOffset ; i++ )
            {
                lFileReader.increaseCounter(17*8);
            }
            
            int i = 0;
            String lKey = lFileReader.getCounterString();
            String lValue = lFileReader.getCounterString();
            while ( lKey != null )
            {
                i++;
//                System.err.println("K: " + lKey + ", V: " + lValue );
//                if ( lValue.toLowerCase().indexOf("to mana") != -1 )
//                {
//                    System.err.println("Test " + i + " K: " + lKey + ", V: " + lValue );
//                }
                
                if(lKey.startsWith("Skill") && lKey.indexOf("223") >0 && lValue.indexOf("wolf")>0){
                	//PROPLEM WITH PLAGUE POPPY (SKILL 223) BEING SET AND THEN WEREWOLF OVERWRITING IT FOR SOME REASON.
                	//THIS IGNORES THE SECOND WRITE OF WEREWOLF.
                }else{
                	iHashMap.put(lKey, lValue);
                }
//                System.out.println(lKey + " , " + lValue);
                lKey = lFileReader.getCounterString();
                lValue = lFileReader.getCounterString();
                
            }
//            lFileReader.getCounterInt(8);
//            System.err.println("Test: " + lFileReader.getCounterString(10));
            
//            System.err.println("Test: " + showBinary(lFileReader, 21) );
//            int lRead = lFileIn.read();
//            while ( lRead != -1 )
//            {
//                System.err.println("Test: " + lRead + " - " + ((char) lRead) );
//                
//                lRead = lFileIn.read();
//            }
        } 
        catch (Exception pEx) 
        {
            D2FileManager.displayErrorDialog(pEx);
        }
    }
    
    public String getValue(String pKey)
    {
        return (String) iHashMap.get(pKey);
    }
    
//	public String showBinary(D2FileReader pFileReader, int pNrBits)
//	{
//        String lString = "   ";
//		for( int i = 0 ; i < pNrBits ; i++ )
//		{
//		    boolean lTest = pFileReader.getCounterBoolean();
//		    lString += ((lTest)?1:0);
//		}
//		lString += "\n";
//	    
//		return lString;
//	}
	
    
}
