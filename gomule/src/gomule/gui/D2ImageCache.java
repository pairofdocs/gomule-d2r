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
package gomule.gui;

import gomule.item.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import javax.swing.*;

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2ImageCache
{
    private static HashMap sImages = new HashMap();
    private static HashMap sDC6Images = new HashMap();
    private static HashMap sIcon = new HashMap();
    
    public static Image getImage(String pImageName)
    {
    	return getImageAbsolute("resources" + File.separator + pImageName );
    }
    
    private static Image getImageAbsolute(String pImageName)
    {
        if ( sImages.containsKey(pImageName) )
        {
            return (Image) sImages.get(pImageName);
        }
        
        Image lImage = null;
        try
        {
            Image lLoadImage = ImageIO.read(new java.io.File( pImageName ));
            new ImageIcon(lLoadImage);
            
            lImage = new BufferedImage(lLoadImage.getWidth(null), lLoadImage.getHeight(null), BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D lGraphics = (Graphics2D) lImage.getGraphics();
            lGraphics.drawImage(lLoadImage,0,0,null);
            lGraphics.dispose();
        }
        catch ( IOException pEx )
        {
            lImage = null;
        }
        sImages.put(pImageName, lImage);
        
        return lImage;
    }

    public static Icon getIcon(String pIconName)
    {
    	return getIconAbsolute("resources" + File.separator + "icons" + File.separator + pIconName );
    }
    
    private static Icon getIconAbsolute(String pImageName)
    {
        if ( sIcon.containsKey(pImageName) )
        {
            return (Icon) sIcon.get(pImageName);
        }
        
        Icon lIcon = null;
        try
        {
            Image lLoadImage = ImageIO.read(new java.io.File( pImageName ));
            lIcon = new ImageIcon(lLoadImage);
        }
        catch ( IOException pEx )
        {
        	lIcon = null;
        }
        sIcon.put(pImageName, lIcon);
        
        return lIcon;
    }
    
    public static Image getDC6Image(D2Item pItem)
    {
        return getDC6Image( pItem.get_image() + ".dc6");
    }
    public static Image getDC6Image(String pFileName)
    {
    	String lFileName = "resources" + File.separator + "gfx" + File.separator + pFileName;
    	
    	if ( sDC6Images.containsKey( lFileName ) )
    	{
    		return (Image) sDC6Images.get( lFileName );
    	}
    	
        D2dc6 lD2S = new D2dc6(lFileName);
        lD2S.load_file();
        Image lImage = lD2S.getSingleImage();
        
        sDC6Images.put( lFileName, lImage );
        
        return lImage;
    }

    
    
}
