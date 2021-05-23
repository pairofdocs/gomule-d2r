/*******************************************************************************
 * 
 * Copyright 2007 Randall (oriniginal the Flavie Reader)
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
package randall.d2files;

import gomule.gui.*;
import java.io.*;

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2FileReader 
{
//	private String	iFileName;
	protected byte	iBuffer[];
	
	private int		iCounterPos = 0;
	private int		iCounterBit = 0;
	
	public D2FileReader(String pFileName)
	{
		File lFile = new File(pFileName);
		if ( lFile.exists() && lFile.isFile() && lFile.canRead() )
		{
			try
			{
				iBuffer = new byte[(int) lFile.length()];
				FileInputStream lIn = new FileInputStream(lFile);
				lIn.read(iBuffer);
				lIn.close();
			}
			catch ( Exception pEx )
			{
				iBuffer = null;
                D2FileManager.displayErrorDialog(pEx);
			}
		}
	}

	public D2FileReader(byte pBuffer[])
	{
	    iBuffer = pBuffer;
	}
	
	public void increaseCounter(int pNrBits)
	{
		for ( int i = 0 ; i < pNrBits ; i++ )
		{
			iCounterBit++;
			if ( iCounterBit > 7 )
			{
				iCounterBit = 0;
				iCounterPos++;
			}
		}
	}
	
	public int getCounterPos()
	{
	    return iCounterPos;
	}
	
	public int getCounterBit()
	{
	    return iCounterBit;
	}
	
	public void setCounter(int pCharPos, int pBitNr)
	{
		iCounterPos = pCharPos;
		iCounterBit = pBitNr;
	}
	
	public boolean getCounterBoolean()
	{
		int lNr = 1;
		if ( iCounterBit > 0 )
		{
			lNr = lNr << iCounterBit;
		}
		
		boolean lBoolean =  ((iBuffer[iCounterPos] & lNr) > 0);
		
		increaseCounter(1);
		return lBoolean;
	}
	
	public String getCounterString()
	{
	    StringBuffer lBuffer = new StringBuffer();
	    
	    int lInt = 0;
	    try
	    {
	        lInt = getCounterInt(8);
	    }
	    catch ( Exception pEx )
	    {
	        return null;
	    }
	    while ( lInt != 0 )
	    {
		    try
		    {
		        lBuffer.append((char) lInt);
		        lInt = getCounterInt(8);
		    }
		    catch ( Exception pEx )
		    {
		        lInt = 0;
		    }
	    }
	    
	    return lBuffer.toString();
	}
	public String getCounterString(int pCharNr)
	{
		char lBuffer[] = new char[pCharNr];
		
		for ( int lCharNr = 0 ; lCharNr < pCharNr ; lCharNr ++ )
		{
			lBuffer[lCharNr] = 0;
			for ( int lBitNr=0 ; lBitNr < 8 ; lBitNr++)
			{
				// first read
				boolean lBit = getCounterBoolean();
				if ( lBit )
				{
					// now set
					int lNr = 1;
					if ( lBitNr > 0 )
					{
						lNr = 1 << lBitNr;
					}
					lBuffer[lCharNr] = (char) (lBuffer[lCharNr] | lNr);
				}
			}
		}

		return new String(lBuffer);
	}
	
	public long getCounterLong(int pBitNr)
	{
		long lInt = 0;
		
		boolean lIntCount[] = new boolean[pBitNr];
		
		for ( int i = 0 ; i < pBitNr ; i++ )
		{
			lIntCount[i] = getCounterBoolean();
		}
		
		for ( int i = pBitNr-1 ; i >= 0 ; i-- )
		{
			lInt = lInt << 1;
			if ( lIntCount[i] )
			{
				lInt++;
			}
			
		}
		
		return lInt;
	}
	
	
	public int getCounterInt(int pBitNr)
	{
		int lInt = 0;
		
		boolean lIntCount[] = new boolean[pBitNr];
		
		for ( int i = 0 ; i < pBitNr ; i++ )
		{
			lIntCount[i] = getCounterBoolean();
		}
		
		for ( int i = pBitNr-1 ; i >= 0 ; i-- )
		{
			lInt = lInt << 1;
			if ( lIntCount[i] )
			{
				lInt++;
			}
			
		}
		
		return lInt;
	}
	
	
	
}
