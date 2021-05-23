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

import java.util.ArrayList;


public class ProbTCRow {

	ArrayList TC;
	ArrayList Prob;
	int totProb;
	
	public ProbTCRow(ArrayList TC, ArrayList Prob){
		this.Prob = Prob;
		this.TC = TC;
		this.totProb = sum(Prob);
		
	}
	
	public ProbTCRow(ProbTCRow ptcR){
		this.Prob = new ArrayList();
		this.TC = new ArrayList();
		
		for(int x = 0;x<ptcR.getProb().size();x++){
			Prob.add(new Double(((Double)ptcR.getProb().get(x)).doubleValue()));
			TC.add(new String(((String)ptcR.getTC().get(x))));
		}
		this.totProb = ptcR.totProb;
		
	}
	
	public ArrayList getTC(){
		return this.TC;
	}
	
	public ArrayList getProb(){
		return this.Prob;
	}
	
	public int getTotProb(){
		return this.totProb;
	}
	
	public void setTotProb(int totProb){
		this.totProb = totProb;
	}
		
		
	private int sum(ArrayList prob) {

		int out = 0;
		for(int x= 0;x<prob.size();x=x+1){
			out = out + (((Double)prob.get(x)).intValue());
		}

		return out;
		
	}

	public void setProb(double d, int x) {
		this.Prob.set(x,new Double(d));
		
	}
	public void printTCs(){
		for(int x = 0;x<TC.size();x++){
		System.out.print(TC.get(x) + " , ");
		}
		System.out.println();
	}
}
