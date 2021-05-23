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
package gomule.dropCalc;

import java.io.BufferedWriter;
import java.io.IOException;


public class CalcWriter {

	String fileName;
	BufferedWriter Bw;
	
	
	public CalcWriter(String fileName){
//		this.fileName = fileName;
//		try {
//			Bw = new BufferedWriter(new FileWriter(fileName));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
	}
	
	public boolean writeData(String dataToWrite){



		try {
			Bw.append(dataToWrite);
			Bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
		
	}
	
	
}
