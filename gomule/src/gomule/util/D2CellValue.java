/*
 * Created on 5-mrt-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gomule.util;

import gomule.item.*;

import java.awt.*;

/**
 * @author Marco & Silospen
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2CellValue
{
    private Object  iValue;
    private String	iTooltip;
    private Color	iForeground;
    
    public D2CellValue(Object pValue, D2Item pItem, D2Project pProject)
    {
        iValue = pValue;
        iForeground = pItem.getItemColor();
        
    }
    
    public D2CellValue(Object pValue, String pTooltip, D2Item pItem, D2Project pProject)
    {
        iValue = pValue;
        iTooltip = pTooltip;
        iForeground = pProject.getItemColor(pItem); 
    }
    
    /**
     * @return Returns the foreground.
     */
    public Color getForeground()
    {
        return iForeground;
    }
    /**
     * @return Returns the value.
     */
    public Object getValue()
    {
        return iValue;
    }
    
    public String getTooltip()
    {
        return iTooltip;
    }
}
