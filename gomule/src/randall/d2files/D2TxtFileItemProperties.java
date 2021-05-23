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

import java.util.ArrayList;


/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2TxtFileItemProperties 
{
    private D2TxtFile	iTxtFile;
    private int			iRowNr;
    
    public D2TxtFileItemProperties(D2TxtFile pTxtFile, int pRowNr)
    {
        iTxtFile = pTxtFile; // iTxtFile.getRowNr()
        iRowNr = pRowNr;
    }
    
    public String get(String pKey)
    {
//    	int x = 1;
//    	if(pKey.equals("str name")){
////    	while(x==1){
//    	int test = 221;
//    	if(D2TblFile.getString(iTxtFile.getValue(test, "str name")) != null){
//    	System.out.println(D2TblFile.getString(iTxtFile.getValue(test, "str name")));
//    	System.out.println((iTxtFile.getFileName()));
//    	System.out.println("bl");
////    	}
//    	}
//    	}
        return iTxtFile.getValue(iRowNr, pKey);
    }
    
    public String getFileName()
    {
        return iTxtFile.getFileName();
    }
    
    public String getName()
    {
//        if ( iTxtFile == D2TxtFile.MISC )
//        {
//            String lType = iTxtFile.getValue(iRowNr, "type");
//            if ( lType != null && !"".equals(lType.trim()))
//            {
//                D2TxtFileItemProperties lItemType = D2TxtFile.ITEMTYPES.searchColumns("Code", lType);
//                if ( lItemType != null )
//                {
//                    return lItemType.get("ItemType");
//                }
//            }
//        }
        return iTxtFile.getValue(iRowNr, "name");
    }
    
    public String getTblName()
    {
        return iTxtFile.getValue(iRowNr, "transtbl");
    }
    public int getRowNum()
    {
        return iRowNr;
    }
    
}
