/*
 * Created on 8-jun-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gomule.gui;

import gomule.d2s.D2Character;
import gomule.d2x.D2Stash;
import gomule.item.*;
import gomule.util.*;

import java.io.*;
import java.util.*;

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2ItemListAll implements D2ItemList
{
    private D2FileManager	iFileManager;
    private D2Project		iProject;
    
    private ArrayList 		iList = new ArrayList();
    private ArrayList 		iD2ItemListListenerList = new ArrayList();
	private boolean iIgnoreItemListEvents = false;
    
    public D2ItemListAll(D2FileManager pFileManager, D2Project pProject)
    {
        iFileManager = pFileManager;
        iProject = pProject;
        
        ArrayList lFileNames = new ArrayList();
        
        lFileNames.addAll( iProject.getCharList() );
        lFileNames.addAll( iProject.getStashList() );
        
        for ( int i = 0 ; i < lFileNames.size() ; i++ )
        {
            try
            {
                D2ItemList lList = iFileManager.addItemList((String) lFileNames.get(i), null);
                iList.add( lList );
            }
            catch ( Exception pEx )
            {
                System.err.println("Error with: " + ((String) lFileNames.get(i)) );
                pEx.printStackTrace();
            }
        }
        fireD2ItemListEvent();
    }
    
    public void connect(String pFileName)
    {
        try
        {
            D2ItemList lList = iFileManager.addItemList(pFileName, null);
            // set listeners
            for ( int i = 0 ; i < iD2ItemListListenerList.size() ; i++ )
            {
                D2ItemListListener lListener = (D2ItemListListener) iD2ItemListListenerList.get(i);
                lList.addD2ItemListListener( lListener );
            }
            iList.add( lList );
            fireD2ItemListEvent();
        }
        catch ( Exception pEx )
        {
            pEx.printStackTrace();
        }
    }
    
    public void disconnect(String pFileName)
    {
        try
        {
            D2ItemList lList = iFileManager.getItemList(pFileName);
            
            if ( lList != null )
            {
                // remove listeners
                for ( int i = 0 ; i < iD2ItemListListenerList.size() ; i++ )
                {
                    D2ItemListListener lListener = (D2ItemListListener) iD2ItemListListenerList.get(i);
                    lList.removeD2ItemListListener( lListener );
                }
            
                iFileManager.removeItemList(pFileName, null);
            
                iList.remove( lList );
                fireD2ItemListEvent();
            }
        }
        catch ( Exception pEx )
        {
            pEx.printStackTrace();
        }
    }
    
    public ArrayList getAllContainers()
    {
        return iList;
    }
    
    public String getFilename()
    {
        return "all";
    }
    
    public String getFilename(D2Item pItem)
    {
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            if ( lItemList.containsItem(pItem) )
            {
            	if(lItemList.getFilename().toLowerCase().endsWith(".d2s")){
            		return ((D2Character)lItemList).getCharName() + ".d2s";
            	}else{
            		return ((D2Stash)lItemList).getFileNameEnd();
            	}
            }
        }
        
        return null;
    }
    
    public boolean containsItem(D2Item pItem)
    {
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            if ( lItemList.containsItem(pItem) )
            {
                return true;
            }
        }
        return false;
    }
    
    public void removeItem(D2Item pItem)
    {
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            if ( lItemList.containsItem(pItem) )
            {
                lItemList.removeItem(pItem);
                return;
            }
        }
    }

    public ArrayList getItemList()
    {
        ArrayList lList = new ArrayList();
        
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            lList.addAll( lItemList.getItemList() );
        }
        
        return lList;
    }

    public int getNrItems()
    {
        int  lNrItems = 0;
        
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            lNrItems += lItemList.getNrItems();
        }
        
        return lNrItems;
    }

    public boolean isModified()
    {
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            if ( lItemList.isModified() )
            {
                return true;
            }
        }
        
        return false;
    }

    public void addD2ItemListListener(D2ItemListListener pListener)
    {
        iD2ItemListListenerList.add(pListener);
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            lItemList.addD2ItemListListener(pListener);
        }
    }

    public void removeD2ItemListListener(D2ItemListListener pListener)
    {
        iD2ItemListListenerList.remove(pListener);
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            lItemList.removeD2ItemListListener(pListener);
        }
    }
    
    public void fireD2ItemListEvent()
    {
    	if(iIgnoreItemListEvents){
    		return;
    	}
    	for ( int i = 0 ; i < iD2ItemListListenerList.size() ; i++ )
        {
            D2ItemListListener lListener = (D2ItemListListener) iD2ItemListListenerList.get(i);
            lListener.itemListChanged();
        }
    }
    
    public boolean hasD2ItemListListener()
    {
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            if ( lItemList.hasD2ItemListListener() )
            {
                return true;
            }
        }
        
        return false;
    }

    public void save(D2Project pProject)
    {
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            if ( lItemList.isModified() )
            {
                lItemList.save(pProject);
            }
        }
    }

    public boolean isSC()
    {
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            if ( lItemList.isSC() )
            {
                return true;
            }
        }
        
        return false;
    }

    public boolean isHC()
    {
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            if ( lItemList.isHC() )
            {
                return true;
            }
        }
        
        return false;
    }
    
    public void fullDump(PrintWriter pWriter)
    {
        D2ItemList lItemList;
        for ( int i = 0 ; i < iList.size() ; i++ )
        {
            lItemList = (D2ItemList) iList.get(i);
            lItemList.fullDump( pWriter);
        }
    }
    
    public void initTimestamp()
    {
        throw new RuntimeException("Internal error: wrong calling");
    }

    public boolean checkTimestamp()
    {
//        throw new RuntimeException("Internal error: wrong calling");
        return true;
    }
    
    public void ignoreItemListEvents(){
    	iIgnoreItemListEvents  = true;
    }
    
    public void listenItemListEvents(){
    	iIgnoreItemListEvents = false;
    }

	public void addItem(D2Item pItem) {
		//Do Nothing!
	}

}
