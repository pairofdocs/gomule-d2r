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

import java.util.*;

public class SubCatObject implements Comparable
{
    private String    iSubCat;
    private ArrayList iItemObjects = new ArrayList();
    private CatObject iCatObject;

    public SubCatObject(String pSubCat, CatObject pCatObject)
    {
        iSubCat = pSubCat;
        iCatObject = pCatObject;
    }

    public String toString()
    {
        return iSubCat;
    }

    public void addItemObject(ItemObject pItemObject)
    {
        iItemObjects.add(pItemObject);
    }

    public ArrayList getItemObjects()
    {
        return iItemObjects;
    }

    public int compareTo(Object pObject)
    {
        return toString().compareTo(pObject.toString());
    }

    public boolean equals(Object pObject)
    {
        return toString().equals(pObject.toString());
    }

    public CatObject getCatObject()
    {
        return iCatObject;
    }

    public void setCatObject(CatObject pCatObject)
    {
        iCatObject = pCatObject;
    }
}