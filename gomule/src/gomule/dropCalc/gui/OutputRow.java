/*******************************************************************************
 * 
 * Copyright 2008 Silospen
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
package gomule.dropCalc.gui;

import java.text.DecimalFormat;

public class OutputRow {

	DecimalFormat DM = new DecimalFormat("#.###############");
	String c0;
	String c1;
	double c2;
	double c3;

	public OutputRow(String c0, String c1, Object c2, double c3){
		this.c0 = c0;
		this.c1 = c1;
		this.c2 = ((Double)c2).doubleValue();
		this.c3 = c3;
	}

	public String getC0(){

		return c0;

	}

	public String getC1(){

		return c1;

	}

	public double getC2(){

		return c2;

	}

	public double getC3(){

		return c3;

	}

	public Double getObjC2(){

		return new Double(c2);	
	}
	
	public Double getObjC3(){

		return new Double(c3);	
	}

	public double roundDouble(double d, int places) {
		return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10,
				(double) places);
	}

	public String getStrC2(boolean dec) {
		if(dec){
			return DM.format(c2);

		}
		else{
			return "1:" + (int)Math.floor(1/c2);
		}

	}

	public String getStrC3(boolean dec) {
			return DM.format(c3);
	}

}
