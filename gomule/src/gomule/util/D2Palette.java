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

package gomule.util;

// loads a d2-style palette
public class D2Palette{
  private D2Palette(){
    String s = java.io.File.separator;
    D2BitReader br = new D2BitReader("resources"+s+"palette.dat");
    colors = new int[256];
    for (int i = 0; i < 256; i++){
      //int blue = (int)br.read(8);
      //int green = (int)br.read(8);
      //int red = (int)br.read(8);
      //colors[i] = (red << 16) + (green << 8) + (blue << 0);
      colors[i] = ((int)br.read(24));
    }
  }

  static public int get_color(int code){
    if (p == null)
      p = new D2Palette();
    return p.get(code);
  }

  private int get(int code){
    return colors[code];
  }

  private int[] colors;
  static private D2Palette p = null;
}
