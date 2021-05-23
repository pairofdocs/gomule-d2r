/*
 * Created on 14-mei-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gomule.item;

import java.util.*;
/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2WeaponTypes
{
    private String iType;
    private String iType2;
    private String iDisplay;
    
    public static final ArrayList sListAll = new ArrayList();
    
    public static final D2WeaponTypes WEAP_SWOR = new D2WeaponTypes("swor", "Sword");
    public static final D2WeaponTypes WEAP_SCEP = new D2WeaponTypes("scep", "Scepter");
    public static final D2WeaponTypes WEAP_MACE = new D2WeaponTypes("mace", "Mace");
    public static final D2WeaponTypes WEAP_CLUB = new D2WeaponTypes("club", "Club");
    public static final D2WeaponTypes WEAP_HAMM = new D2WeaponTypes("hamm", "Hammer");
    public static final D2WeaponTypes WEAP_AXE = new D2WeaponTypes("axe", "Axe");
    
    static
    {
        addNewLine();
    }
    
    public static final D2WeaponTypes WEAP_WAND = new D2WeaponTypes("wand", "Wand");
    public static final D2WeaponTypes WEAP_STAF = new D2WeaponTypes("staf", "Staff");
    public static final D2WeaponTypes WEAP_ORB = new D2WeaponTypes("orb", "Orb");
    public static final D2WeaponTypes WEAP_SPEA = new D2WeaponTypes("spea", "Spear");
    public static final D2WeaponTypes WEAP_POLE = new D2WeaponTypes("pole", "Polearm");
    public static final D2WeaponTypes WEAP_ASPE = new D2WeaponTypes("aspe", "Amazon Spear");
    
    static
    {
        addNewLine();
    }
    public static final D2WeaponTypes WEAP_BOW = new D2WeaponTypes("bow", "Bow");
    public static final D2WeaponTypes WEAP_XBOW = new D2WeaponTypes("xbow", "Crossbow");
    public static final D2WeaponTypes WEAP_ABOW = new D2WeaponTypes("abow", "Amazon Bow");
    public static final D2WeaponTypes WEAP_JAVE = new D2WeaponTypes("jave", "Javelin");
    public static final D2WeaponTypes WEAP_AJAV = new D2WeaponTypes("ajav", "Amazon Javelin");

    static
    {
        addNewLine();
    }
    public static final D2WeaponTypes WEAP_KNIF = new D2WeaponTypes("knif", "Knife");
    public static final D2WeaponTypes WEAP_H2H = new D2WeaponTypes("h2h", "h2h2", "Hand to Hand");
//    public static final D2WeaponTypes WEAP_H2H2 = new D2WeaponTypes("h2h2", "Hand to Hand 2");
    public static final D2WeaponTypes WEAP_TAXE = new D2WeaponTypes("taxe", "Throwing Axe");
    public static final D2WeaponTypes WEAP_TKNI = new D2WeaponTypes("tkni", "Throwing Knife");

//    static
//    {
//        addNewLine();
//    }
    
    public static final D2WeaponTypes WEAP_ALL = new D2WeaponTypes("all", "All");
    
    private static void addNewLine()
    {
        sListAll.add(new Object());
    }

    private D2WeaponTypes(String pType, String pDisplay)
    {
        this(pType, null, pDisplay);
    }
    
    private D2WeaponTypes(String pType, String pType2, String pDisplay)
    {
        iType = pType;
        iType2 = pType2;
        iDisplay = pDisplay;
        sListAll.add(this);
    }
    
    public boolean isType(String pType)
    {
        if ( iType.equals(pType) )
        {
            return true;
        }
        
        if ( iType2 != null && iType2.equals(pType) )
        {
            return true;
        }
        
        return false;
    }
    
    public String toString()
    {
        return iDisplay;
    }
    
    public static ArrayList getWeaponTypeList()
    {
        return sListAll;
    }

}
