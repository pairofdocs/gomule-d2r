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

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public 	class PercentageCounter
{
    private TotalObject	iTotalObject;
    
    private CatObject	iCat;
    private int			iNrItems;
    private int			iNrItemsFound;
    
    public PercentageCounter(TotalObject pTotalObject)
    {
        iTotalObject = pTotalObject;
    }
    
    public PercentageCounter(CatObject pCat, int pNrItems, int pNrItemsFound)
    {
        iCat = pCat;
        iCat.setCounter(this);
        iNrItems = pNrItems;
        iNrItemsFound = pNrItemsFound;
    }
    
    public PercentageCounter(int pNrItems, int pNrItemsFound)
    {
        iNrItems = pNrItems;
        iNrItemsFound = pNrItemsFound;
    }
    
    public PercentageCounter()
    {
        iNrItems = 0;
        iNrItemsFound = 0;
    }
    
    public void add(PercentageCounter pCounter)
    {
        iNrItems += pCounter.iNrItems;
        iNrItemsFound += pCounter.iNrItemsFound;
    }
    
    public double getPercentage(int pNr, int pTotal)
    {
        return (Math.round(((pNr*10000.0)/pTotal))/100.0);
    }
    
    public void calculate()
    {
//        String lName = "Total";
//        String lClass = "cat";
        
        if ( iTotalObject != null )
        {
            PercentageCounter lCounter = iTotalObject.getPercentageCounter();
            iNrItemsFound = lCounter.iNrItemsFound;
            iNrItems = lCounter.iNrItems;

//            lName = iTotalObject.getDisplay();
//            lClass = iTotalObject.getStyle();
        }
//        else if ( iCat != null )
//        {
//            lName = iCat.toString();
//            lClass = iCat.getStyle();
//        }
//        return "<div class="+lClass+">"+lName+", Total: "+getFoundNr()+", found: "+iNrItemsFound + " (" + getPercentage(iNrItemsFound, iNrItems) +"%), not found: " + (iNrItems-iNrItemsFound)+ " (" + getPercentage((iNrItems-iNrItemsFound), iNrItems) + "%)</div>";

    }
    
    public String getName()
    {
        if ( iTotalObject != null )
        {
            return iTotalObject.getDisplay();
        }
        else if ( iCat != null )
        {
            return iCat.toString();
        }
    	return "";
    }
    
    public String getStyle()
    {
        if ( iTotalObject != null )
        {
            return iTotalObject.getStyle();
        }
        else if ( iCat != null )
        {
            return iCat.getStyle();
        }
        return "cat";
    }
    
    public int getTotalNr()
    {
    	return iNrItems;
    }
    
    public int getFoundNr()
    {
    	return iNrItemsFound;
    }
    
    public double getFoundPerc()
    {
    	return getPercentage(getFoundNr(), getTotalNr());
    }

    public int getNotFoundNr()
    {
    	return getTotalNr()-getFoundNr();
    }
    
    public double getNotFoundPerc()
    {
    	return getPercentage(getNotFoundNr(), getTotalNr());
    }

}
