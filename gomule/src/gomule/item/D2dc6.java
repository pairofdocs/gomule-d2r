/*******************************************************************************
 * 
 * Copyright 2007 Andy Theuninck & Randall
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

package gomule.item;

import gomule.util.*;

import java.awt.*;
import java.awt.image.BufferedImage;

public class D2dc6
{
	
    private BufferedImage b;
    private D2BitReader br;
//    private String filename;
    
    public D2dc6(String f)
    {
//        filename = f;
        br = new D2BitReader(f);
    }

    public void load_file()
    {
        if (b == null)
        {
            return;
        }
        br.set_byte_pos(0);
        br.skipBits(128);
        int directions = (int) br.read(32);
        int frames = (int) br.read(32);
        br.skipBits(32 * directions * frames);
//        int flip = (int) br.read(32);
        br.read(32);
//        int width = (int) br.read(32);
        br.read(32);
        int height = (int) br.read(32);
//        int offset_x = (int) br.read(32);
        br.read(32);
//        int offset_y = (int) br.read(32);
        br.read(32);
        br.skipBits(64);
//        int length = (int) br.read(32);
        br.read(32);
        int x = 0;
        int y = height - 1;
        while (y >= 0)
        {
            int current = (int) br.read(8);
            if (current == 0x80)
            {
                x = 0;
                y--;
            } else if (current > 0x80)
            {
                x += (current - 0x80);
            } else
            {
                while (current-- > 0)
                {
                    // set the pixel
                    // @ (x,y), color br.read(8)
                    b.setRGB(x++, y, D2Palette.get_color((int) br.read(8)));
                }
            }
        }
    }
    
    public Image getSingleImage()
    {
//        System.err.println( "***** " + br.getFileName() + "*****: " + Color.black.getRGB() );
        br.set_byte_pos(0);
        br.skipBits(128);
        int directions = (int) br.read(32);
        int frames = (int) br.read(32);
        br.skipBits(32 * directions * frames);
//        int flip = (int) br.read(32);
        br.read(32);
        int width = (int) br.read(32);
        int height = (int) br.read(32);
//        int offset_x = (int) br.read(32);
        br.read(32);
//        int offset_y = (int) br.read(32);
        br.read(32);
        br.skipBits(64);
//        int length = (int) br.read(32);
        br.read(32);
        int x = 0;
        int y = height - 1;
    	BufferedImage lImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        while (y >= 0)
        {
            int current = (int) br.read(8);
            if (current == 0x80)
            {
                x = 0;
                y--;
            } else if (current > 0x80)
            {
                x += (current - 0x80);
            } else
            {
                while (current-- > 0)
                {
                    // set the pixel
                    // @ (x,y), color br.read(8)
                    int lPalette = (int) br.read(8);
                    int lColor = D2Palette.get_color(lPalette);
//                    System.err.println("Test " + lPalette + " - " + lColor );
//                    if ( lPalette != 172 )
//                    {
                    Color lTest = new Color(lColor);
                    lImage.setRGB(x++, y, new Color(lTest.getRed(), lTest.getGreen(), lTest.getBlue(), 255).getRGB());
//                    }
//                    else
//                    {
//                        lImage.setRGB(x++, y, new Color(0,0,0,0).getRGB() );
//                    }
                }
            }
        }

        return lImage;
    }

    public void set_image(BufferedImage bi)
    {
        b = bi;
    }

    public void test()
    {

    }


}