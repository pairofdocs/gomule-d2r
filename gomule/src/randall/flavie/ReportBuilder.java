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

import java.io.*;
import java.util.*;

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ReportBuilder 
{
//	private Flavie				iFlavie;
//	private FlavieSettingsPanel	iFlaviePanel;
	
	public ReportBuilder(Flavie pFlavie)
	{
//		iFlavie = pFlavie;
//		iFlaviePanel = iFlavie.getSettingsPanel();
	}
	
	public void buildReport(String pReportTitle, String pReportName, String pDataName, String pStyleName, ArrayList pDatFile, boolean pCountAll, boolean pCountEthereal, boolean pCountStash, boolean pCountChar ) throws Exception
	{
		if ( !pCountAll && !pCountStash && !pCountChar && !pCountEthereal )
		{
			throw new Exception("One of the options All, Stash, Character, Ethereal has to be enterred in the Flavie tab.");
		}
		
		if ( !pReportName.endsWith(".htm") && !pReportName.endsWith(".html") )
		{
			pReportName += ".html";
		}
		File lReport = new File(pReportName);
		if ( !lReport.exists() )
		{
			lReport.createNewFile();
		}
		PrintStream lOutReport = new PrintStream(new FileOutputStream(lReport));

		lOutReport.println("<html>");
		lOutReport.println("<head>");
		lOutReport.println("<title>" + pReportTitle + "</title>");
		lOutReport.println("</head>");
		
		if ( pStyleName != null && !pStyleName.trim().equals("") )
		{
			File lStyle = new File(pStyleName);
			lOutReport.println("<link rel=\"StyleSheet\" href=\""+ lStyle.getName() + "\" type=\"text/css\">");
		}

		lOutReport.println("<body class=body>");
		lOutReport.println("<table>");
		
		ArrayList lPercentages = new ArrayList();
		
		CatObject lCatObject = null;
		SubCatObject lSubCatObject = null;

		boolean lShowCounters = false;
		boolean lNewRow = false;
		boolean lNewCol = false;
		boolean lSubCat = false;
		
	    for ( int i = 0 ; i < pDatFile.size() ; i++ )
	    {
	        Object lObject = pDatFile.get(i);
	        if ( lObject instanceof TotalObject )
	        {
	            lShowCounters = true;
				PercentageCounter lPerc = new PercentageCounter((TotalObject) lObject);
				lPercentages.add(lPerc);
	        }
	        if ( lObject instanceof CatObject )
	        {
				int lNrItems = 0;

				int lNrItemsFound = 0;

				lCatObject = (CatObject) lObject;

				ArrayList lSubCatList = lCatObject.getSubCats();
				for ( int j = 0 ; j < lSubCatList.size() ; j++ )
				{
					SubCatObject lSubCatCompare = (SubCatObject) lSubCatList.get(j);
					
					ArrayList lItemObjectList = lSubCatCompare.getItemObjects();
					for ( int k = 0 ; k < lItemObjectList.size() ; k++ )
					{
						lNrItems++;
						ItemObject lItemObjectCompare = (ItemObject) lItemObjectList.get(k);
						int lNrItemsFoundTotal = 0;
						int lNrItemsFoundStash = 0;
						int lNrItemsFoundCharacter = 0;
						int lNrItemsFoundEthereal = 0;

						ArrayList lItemInstanceList = lItemObjectCompare.getInstances();
						for ( int l = 0 ; l < lItemInstanceList.size() ; l++ )
						{
						    lNrItemsFoundTotal++;
						    D2ItemInterface lInstance = (D2ItemInterface) lItemInstanceList.get(l);
							if ( lInstance.isCharacter() )
							{
								lNrItemsFoundCharacter++;
							}
							else
							{
								lNrItemsFoundStash++;
							}
							if ( lInstance.isEthereal() )
							{
							    lNrItemsFoundEthereal++;
							}
						}
						if ( pCountAll && lNrItemsFoundTotal > 0 )
						{
							lNrItemsFound++;
						}
						else if ( pCountStash && lNrItemsFoundStash > 0 )
						{
							lNrItemsFound++;
						}
						else if ( pCountChar && lNrItemsFoundCharacter > 0 )
						{
							lNrItemsFound++;
						}
						else if ( pCountEthereal && lNrItemsFoundEthereal > 0 )
						{
							lNrItemsFound++;
						}
					}
				}
				if ( lSubCat )
				{
					lSubCat = false;
					lOutReport.println("</table>");
				}
				if ( lCatObject.isNewRow() )
				{
					if ( lNewRow )
					{
						lOutReport.println("</p>");
						lOutReport.println("</td>");
						lOutReport.println("</tr>");
					}
					else if ( lNewCol )
					{
						lOutReport.println("</p>");
						lOutReport.println("</td>");
					}
					lNewRow = true;
					lNewCol = false;
					lOutReport.println("<tr>");
				}
				else
				{
					if ( lNewCol )
					{
						lOutReport.println("</p>");
						lOutReport.println("</td>");
					}
					lNewCol = true;
				}
				lOutReport.println("<td valign=top>");
				PercentageCounter lPerc = new PercentageCounter(lCatObject, lNrItems, lNrItemsFound);
				lPercentages.add(lPerc);
				lOutReport.println("<div class=cat>"+lCatObject.toString()+"("+lNrItemsFound+" of "+lNrItems+") ("+ lPerc.getPercentage(lNrItemsFound, lNrItems) +"%)</div>");
				lOutReport.println("<p>");
	        }
	        else if ( lObject instanceof SubCatObject )
	        {
	            lSubCatObject = (SubCatObject) lObject;

				if ( lSubCat )
				{
					lSubCat = false;
					lOutReport.println("</table><br>");
				}
				
				lSubCat = true;
				
				int lNrItems = 0;
				int lNrItemsFound = 0;
				
//				ArrayList lSubCatList = lCatObject.getSubCats();

				ArrayList lItemObjectList = lSubCatObject.getItemObjects();
				for ( int k = 0 ; k < lItemObjectList.size() ; k++ )
				{
					lNrItems++;
					ItemObject lItemObjectCompare = (ItemObject) lItemObjectList.get(k);
					int lNrItemsFoundTotal = 0;
					int lNrItemsFoundStash = 0;
					int lNrItemsFoundCharacter = 0;
					int lNrItemsFoundEthereal = 0;

					ArrayList lItemInstanceList = lItemObjectCompare.getInstances();
					for ( int l = 0 ; l < lItemInstanceList.size() ; l++ )
					{
					    lNrItemsFoundTotal++;
						D2ItemInterface lInstance = (D2ItemInterface) lItemInstanceList.get(l);
						if ( lInstance.isCharacter() )
						{
							lNrItemsFoundCharacter++;
						}
						else
						{
							lNrItemsFoundStash++;
						}
						if ( lInstance.isEthereal() )
						{
						    lNrItemsFoundEthereal++;
						}
					}
					if ( pCountAll && lNrItemsFoundTotal > 0 )
					{
						lNrItemsFound++;
					}
					else if ( pCountStash && lNrItemsFoundStash > 0 )
					{
						lNrItemsFound++;
					}
					else if ( pCountChar && lNrItemsFoundCharacter > 0 )
					{
						lNrItemsFound++;
					}
					else if ( pCountEthereal && lNrItemsFoundEthereal > 0 )
					{
						lNrItemsFound++;
					}
				}

				PercentageCounter lPerc = new PercentageCounter(lNrItems, lNrItemsFound);
				lOutReport.println("<div class=subcat>"+ lSubCatObject.toString() + " ("+lNrItemsFound+" of "+lNrItems+") ("+ lPerc.getPercentage(lNrItemsFound, lNrItems) +"%)</div>");
				lOutReport.println("<p>");
				lOutReport.println("<table>");
	        }
	        else if ( lObject instanceof ItemObject )
	        {
	            ItemObject lItemObject = (ItemObject) lObject;

//				ArrayList lItem = RandallUtil.split(lLine, ",", false);
	            
				int lNrItemsFoundTotal = 0;
				int lNrItemsFoundStash = 0;
				int lNrItemsFoundCharacter = 0;
				int lNrItemsFoundEthereal = 0;
				
				ArrayList lItemInstanceList = lItemObject.getInstances();
				for ( int l = 0 ; l < lItemInstanceList.size() ; l++ )
				{
				    lNrItemsFoundTotal++;
					D2ItemInterface lInstance = (D2ItemInterface) lItemInstanceList.get(l);
					if ( lInstance.isCharacter() )
					{
						lNrItemsFoundCharacter++;
					}
					else
					{
						lNrItemsFoundStash++;
					}
					if ( lInstance.isEthereal() )
					{
					    lNrItemsFoundEthereal++;
					}
				}
				
				String lCountStr = "";
				if ( pCountAll )
				{
				    lCountStr += "<td class=b>"+getReportDisplayNr(lNrItemsFoundTotal)+"</td>";
				}
				if ( pCountStash )
				{
				    lCountStr += "<td class=s>"+getReportDisplayNr(lNrItemsFoundStash)+"</td>";
				}
				if ( pCountChar )
				{
				    lCountStr += "<td class=c>"+getReportDisplayNr(lNrItemsFoundCharacter)+"</td>";
				}
				if ( pCountEthereal )
				{
				    lCountStr += "<td class=e>"+getReportDisplayNr(lNrItemsFoundEthereal)+"</td>";
				}
				
				if ( lItemObject.getInfo() == null || lItemObject.getInfo().equals("") )
//				if ( lItem.size() == 1 )
				{
					// &nbsp
					lOutReport.println("<tr>");
					lOutReport.println(lCountStr + "<td class=" + lCatObject.getStyle() + ">" + lItemObject.getName() + "</td>");
					lOutReport.println("</tr>");
				}
				else if ( lItemObject.getExtraDisplay() == null || lItemObject.getExtraDisplay().equals("")) 
				{
					lOutReport.println("<tr>");
					lOutReport.println(lCountStr + "<td class=" + lCatObject.getStyle() + ">" + lItemObject.getName() + "</td><td class=d>" + lItemObject.getInfo() + "</td>");
					lOutReport.println("</tr>");
				}
				else
				{
				    // xTODO: Fix
//		            System.err.println("Print Jewelry");

				    lOutReport.println("<tr>");
					lOutReport.println(lCountStr + "<td class=" + lCatObject.getStyle() + ">" + lItemObject.getName() + "</td><td class=d>" + lItemObject.getExtraDisplay() + "</td>");
					lOutReport.println("</tr>");
				}
	        }
	    }
	    
		if ( lSubCat )
		{
			lSubCat = false;
			lOutReport.println("</table>");
		}
		if ( lNewRow )
		{
			lOutReport.println("</p>");
			lOutReport.println("</td>");
			lOutReport.println("</tr>");
			lNewRow = false;
		}
		else if ( lNewCol )
		{
			lOutReport.println("</p>");
			lOutReport.println("</td>");
			lNewCol = false;
		}
		
		lOutReport.println("</table>");
		
		if ( lShowCounters && lPercentages.size() > 0 )
		{
		    lOutReport.println("<br>");
		    lOutReport.println("<table>");
		    lOutReport.println("<tr>");
		    lOutReport.println("<th>Name</th>");
		    lOutReport.println("<th>Total</th>");
		    lOutReport.println("<th>Found #</th>");
		    lOutReport.println("<th>Found %</th>");
		    lOutReport.println("<th>Not Found #</th>");
		    lOutReport.println("<th>Not Found %</th>");
		    lOutReport.println("</tr>");
		    for ( int i = 0 ; i < lPercentages.size() ; i++ )
		    {
		    	PercentageCounter lPerc = (PercentageCounter) lPercentages.get(i);
		    	lPerc.calculate();
			    lOutReport.println("<tr>");
			    lOutReport.println("<td class="+lPerc.getStyle()+">" + lPerc.getName() + "</td>");
			    lOutReport.println("<td class="+lPerc.getStyle()+">" + lPerc.getTotalNr() + "</td>");
			    lOutReport.println("<td class="+lPerc.getStyle()+">" + lPerc.getFoundNr() + "</td>");
			    lOutReport.println("<td class="+lPerc.getStyle()+">" + lPerc.getFoundPerc() + "%</td>");
			    lOutReport.println("<td class="+lPerc.getStyle()+">" + lPerc.getNotFoundNr() + "</td>");
			    lOutReport.println("<td class="+lPerc.getStyle()+">" + lPerc.getNotFoundPerc() + "%</td>");
			    lOutReport.println("</tr>");
		    }
		    
		    lOutReport.println("</table>");
		}
		
	    lOutReport.println("<br>");
		String lColors = "<table><tr><td class=cat>Colors: </td>";
		if ( pCountAll )
		{
		    lColors += "<td class=b>Total items nr</td>";
		}
		if ( pCountStash )
		{
		    lColors += "<td class=s>items in stash</td>";
		}
		if ( pCountChar )
		{
		    lColors += "<td class=c>items in character</td>";
		}
		if ( pCountEthereal )
		{
		    lColors += "<td class=e>Ethereal items</td>";
		}
		lColors += "</tr></table>";
		
		lOutReport.println(lColors);
		
		lOutReport.println("<p><center><font size=\"-1\">Powered by Flavie<font></center></p>");
		lOutReport.println("</body>");
		lOutReport.println("</html>");
		
		lOutReport.close();
	}
	
	private static String getReportDisplayNr(int pNr)
	{
		if ( pNr > 0 )
		{
			return Integer.toString(pNr);
		}
		return "";
	}
	
	
	public void buildItemlist(String pItemlistName, HashMap lAllItems) throws Exception
	{
		if ( !pItemlistName.endsWith(".txt") )
		{
		    pItemlistName += ".txt";
		}
		File lItemlist = new File(pItemlistName);
		if ( !lItemlist.exists() )
		{
			lItemlist.createNewFile();
		}
		PrintStream lOutItemlist = new PrintStream(new FileOutputStream(lItemlist));

		Iterator lIterator = lAllItems.keySet().iterator();
	    
	    while ( lIterator.hasNext() )
	    {
	        String lKey = (String) lIterator.next();
	        D2ItemInterface lItem = (D2ItemInterface) lAllItems.get(lKey);
	        
	        lOutItemlist.println(lItem.getFingerprint() + " " + lItem.getName());
	    }
	    
	    lOutItemlist.close();
	}
	

}
