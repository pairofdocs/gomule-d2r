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
	
public class CatObject implements Comparable
{
	private String		iCat;
	private ArrayList	iSubCats = new ArrayList();
	private String		iStyle;
	private String		iGroup;

	private boolean		iUnique;
	private boolean 	iSet;
	private boolean		iRune;
	private boolean		iRuneWord;
	private boolean		iMisc;
	private boolean		iGem;
	private boolean		iSkiller;
	
	private boolean		iNewRow;
	
	private PercentageCounter	iCounter;
	
	public CatObject(String pCat)
	{
		iCat = pCat;
	}
	
	public String toString()
	{
		return iCat;
	}
	
	public void addSubCat(SubCatObject pSubCatObject)
	{
		iSubCats.add(pSubCatObject);
	}
	public ArrayList getSubCats()
	{
		return iSubCats;
	}
	
	public int compareTo(Object pObject)
	{
		return toString().compareTo(pObject.toString());
	}
	
	public boolean equals(Object pObject)
	{
		return toString().equals(pObject.toString());
	}
	public String getStyle()
	{
		return iStyle;
	}
	public void setStyle( String pStyle )
	{
		iStyle = pStyle;
	}
    /**
     * @return Returns the newRow.
     */
    public boolean isNewRow() {
        return iNewRow;
    }
    /**
     * @param pNewRow The newRow to set.
     */
    public void setNewRow(boolean pNewRow) {
        iNewRow = pNewRow;
    }
    /**
     * @return Returns the counter.
     */
    public PercentageCounter getCounter() {
        return iCounter;
    }
    /**
     * @param pCounter The counter to set.
     */
    public void setCounter(PercentageCounter pCounter) {
        iCounter = pCounter;
    }

    public String getGroup() 
    {
        return iGroup;
    }

    public void setGroup(String pGroup) 
    {
        iGroup = pGroup;
        iUnique = "unique".equals(iGroup);
        iSet = "set".equals(iGroup);
        iRune = "rune".equals(iGroup);
        iRuneWord = "runeword".equals(iGroup);
        iGem = "gem".equals(iGroup);
        iMisc = "misc".equals(iGroup);
        iSkiller = "skiller".equals(iGroup);
    }
    
	public boolean isUnique()
	{
	    return iUnique;
	}
	
	public boolean isSet()
	{
	    return iSet;
	}
	
	public boolean isRuneWord()
	{
	    return iRuneWord;
	}
	
	public boolean isRune()
	{
	    return iRune;
	}
	
	public boolean isGem()
	{
	    return iGem;
	}
	
	public boolean isMisc()
	{
	    return iMisc;
	}
	
	public boolean isSkiller() {
		return iSkiller;
	}
	
}
