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

public class ItemObject
{
    private String       iName;
    private String       iInfo;
    private ArrayList    iItemInstances = new ArrayList();
    private SubCatObject iSubCatObject;
    private boolean      iRuneWord      = false;
    private String       iItemType;

    private String       iExtraDisplay;
    private ArrayList    iExtraDetect;

    public ItemObject(String pName, String pInfo, SubCatObject pSubCatObject)
    {
        iName = pName;
        iInfo = pInfo;
        iSubCatObject = pSubCatObject;
    }

    public String getName()
    {
        return iName;
    }

    public String getInfo()
    {
        return iInfo;
    }

    public void addItemInstance(D2ItemInterface pInstance)
    {
        iItemInstances.add(pInstance);
    }

    public ArrayList getInstances()
    {
        return iItemInstances;
    }

    public String toString()
    {
        return iName;
    }

    public SubCatObject getSubCatObject()
    {
        return iSubCatObject;
    }

    public void setSubCatObject(SubCatObject pSubCatObject)
    {
        iSubCatObject = pSubCatObject;
    }

    /**
     * @return Returns the runeWord.
     */
    public boolean isRuneWord()
    {
        return iRuneWord;
    }

    /**
     * @param pRuneWord
     *            The runeWord to set.
     */
    public void setRuneWord(boolean pRuneWord)
    {
        iRuneWord = pRuneWord;
    }

    /**
     * @return Returns the itemType.
     */
    public String getItemType()
    {
        return iItemType;
    }

    /**
     * @param pItemType
     *            The itemType to set.
     */
    public void setItemType(String pItemType)
    {
        iItemType = pItemType;
    }

    /**
     * @return Returns the extraDetect.
     */
    public ArrayList getExtraDetect()
    {
        return iExtraDetect;
    }

    /**
     * @param pExtraDetect
     *            The extraDetect to set.
     */
    public void setExtraDetect(ArrayList pExtraDetect)
    {
        iExtraDetect = pExtraDetect;
    }

    /**
     * @return Returns the extraDisplay.
     */
    public String getExtraDisplay()
    {
        return iExtraDisplay;
    }

    /**
     * @param pExtraDisplay
     *            The extraDisplay to set.
     */
    public void setExtraDisplay(String pExtraDisplay)
    {
        iExtraDisplay = pExtraDisplay;
    }
}