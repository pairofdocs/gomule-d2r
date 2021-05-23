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

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

public class RandallPanel extends JPanel
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6556940562813366360L;
	public static final Integer  NONE = new Integer(100);
    public static final Integer  HORIZONTAL = new Integer(101);
	public static final Integer  VERTICAL = new Integer(102);
    public static final Integer  BOTH = new Integer(103);
    
    public static final int ANCHOR_NORTHWEST = GridBagConstraints.NORTHWEST;
	public static final int ANCHOR_NORTHEAST = GridBagConstraints.NORTHEAST;

    private int iMarginXSize = 2;
    private int iMarginYSize = -1;
    private int iYPos = 0;

    private int iMargin = 1;
//	private boolean iMarginAllSides = false;

    // sub panel -> all elements on X or Y of 0 does not get a top or left margin
    private boolean iSubPanel = false;

	private TitledBorder titeledBorder = null;

    /**
     *   Creates a new RandallPanel with a double buffer and a flow layout.
     */
    public RandallPanel()
    {
        super();
		init();
    }

    public RandallPanel(boolean pSubPanel)
    {
        super();
		init();
		if ( pSubPanel )
		{
		    setSubPanel();
		}
    }

    public void setBorder(String pTitle)
    {
		titeledBorder = new TitledBorder(new EtchedBorder(), pTitle);
		setBorder(titeledBorder);
    }
    
    private void init()
    {
        setLayout(new GridBagLayout());
    }

    public void finishDefaultPanel()
    {
        iYPos = iYPos + 100;
        addToPanel(new JPanel(), 0, 250, 1, VERTICAL);
    }

    /**
     * Set the right column nr for the panel
     */
    public void setMarginRightColumnNr(int pColumnNr)
    {
		iMarginXSize = pColumnNr;
    }
    public void setMarginBottomRowNrColumnNr(int pRowNr)
	{
    	iMarginYSize = pRowNr;
    }
    public void setMarginRightBottom(int pColumnNr, int pRowNr)
	{
    	iMarginXSize = pColumnNr;
    	iMarginYSize = pRowNr;
    }
    
	public void setMargin(int pMargin)
	{
		iMargin = pMargin;
	}

//	public void setMarginAllSides(boolean pMarginAllSides)
//	{
//		iMarginAllSides = pMarginAllSides;
//	}

	/**
	 * Set the right column nr for the sub panel 
	 * (does not add left/upper margin when left/up coordinate is 0)
	 */
    public void setSubPanel()
    {
		iSubPanel = true;
    }

    public int getPanelColumnNr()
    {
        return iMarginXSize;
    }

    public void addToPanel(JComponent pComponent, int pX, int pY, int pSizeX, Object pConstraint)
    {
        addToPanel(pComponent, pX, pY, pSizeX, 1, pConstraint, -1.0, -1.0, -1);
    }
    public void addToPanel(JComponent pComponent, int pX, int pY, int pSizeX, double pWeightX, Object pConstraint)
    {
        addToPanel(pComponent, pX, pY, pSizeX, 1, pConstraint, pWeightX, -1.0, -1);
    }
    public void addToPanel(JComponent pComponent, int pX, int pY, int pSizeX, int pSizeY, Object pConstraint)
    {
		addToPanel(pComponent, pX, pY, pSizeX, pSizeY, pConstraint, -1.0, -1.0, -1);
    }
	public void addToPanel(JComponent pComponent, int pX, int pY, int pSizeX, int pSizeY, Object pConstraint, int pConstraintAnchor)
	{
		addToPanel(pComponent, pX, pY, pSizeX, pSizeY, pConstraint, -1.0, -1.0, pConstraintAnchor);
	}

	public void addToPanel(JComponent pComponent, int pX, int pY, int pSizeX, int pSizeY, Object pConstraint, double pWeightX, double pWeightY, int pConstraintAnchor)
	{
    	double 	lWeightX = 0.0;
        double 	lWeightY = 0.0;
        int    	lGridbagConstraint = GridBagConstraints.NONE;
        int		lGridbagAnchor = ANCHOR_NORTHWEST;

        int lMarginTop = iMargin;
        int lMarginLeft = iMargin;
        int lMarginBottom = 0;
//        if ( iMarginAllSides )
//        {
//			lMarginBottom = iMargin;
//        }
        int lMarginRight = iMargin;

        if ( pConstraint == HORIZONTAL)
        {
            lWeightX = 1.0;
            lGridbagConstraint = GridBagConstraints.HORIZONTAL;
        }
		if ( pConstraint == VERTICAL)
		{
			lWeightY = 1.0;
			lGridbagConstraint = GridBagConstraints.VERTICAL;
		}
        if ( pConstraint == BOTH)
        {
            lWeightX = 1.0;
            lWeightY = 1.0;
            lGridbagConstraint = GridBagConstraints.BOTH;
        }
        
        if ( pWeightX >= 0.0 )
        {
        	lWeightX = pWeightX;
        }
		if ( pWeightY >= 0.0 )
		{
			lWeightY = pWeightY;
		}
		if ( pConstraintAnchor >= 0 )
		{
			lGridbagAnchor = pConstraintAnchor;
		}

        if ( pX + pSizeX < iMarginXSize )
        {
            lMarginRight = 0;
        }
        if ( iMarginYSize != -1 && pY + pSizeY == iMarginYSize )
        {
        	lMarginBottom = iMargin;
        }
        if ( iSubPanel )
        {
			lMarginRight = 0;
            if ( pX == 0 )
            {
                lMarginLeft = 0;
            }
            if ( pY == 0 )
            {
                lMarginTop = 0;
            }
        }
        
//        if ( pComponent instanceof JCheckBox )
//        {
//        	lMarginLeft -= 4;
//        }

        this.add(pComponent, new GridBagConstraints(pX, pY, pSizeX, pSizeY, lWeightX, lWeightY
            ,lGridbagAnchor, lGridbagConstraint,
            new Insets(lMarginTop, lMarginLeft, lMarginBottom, lMarginRight), 0, 0));

        this.iYPos = pY + 1;

    }
	
	/**
	 * Removes the given Component from the Panel if not null  
	 * @param pComponent The Component to be removed
	 */
    public void removeFromPanel(JComponent pComponent)
    {
    	if(pComponent != null)
    	{
    		this.remove(pComponent);
    	}
    }
    
	public String getTitle()
	{
		return titeledBorder.getTitle();
	}

	public void setTitle(String pTitle)
	{
		titeledBorder.setTitle(pTitle);
	}
	
}