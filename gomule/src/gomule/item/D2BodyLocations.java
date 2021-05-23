/*
 * Created on 11-mei-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gomule.item;

import java.awt.Point;
import java.util.*;

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2BodyLocations
{
    private String iLocation;
    private String iDisplay;
    
    public static final ArrayList sListAll = new ArrayList();
    
    public static final D2BodyLocations BODY_HEAD = new D2BodyLocations("head", "Head", sListAll);
    public static final D2BodyLocations BODY_TORS = new D2BodyLocations("tors", "Body", sListAll);
    public static final D2BodyLocations BODY_GLOV = new D2BodyLocations("glov", "Gloves", sListAll);
    public static final D2BodyLocations BODY_BELT = new D2BodyLocations("belt", "Belt", sListAll);
    public static final D2BodyLocations BODY_FEET = new D2BodyLocations("feet", "Boots", sListAll);
    public static final D2BodyLocations BODY_RARM = new D2BodyLocations("rarm", "Shield", sListAll);
    
    public static final D2BodyLocations BODY_ALL = new D2BodyLocations("all", "All", sListAll);
    
    public static final D2BodyLocations BODY_NECK = new D2BodyLocations("neck", "Amulet", null);
    public static final D2BodyLocations BODY_LRIN = new D2BodyLocations("lrin", "Ring", null);
    
    private D2BodyLocations(String pLocation, String pDisplay, ArrayList pListAll)
    {
        iLocation = pLocation;
        iDisplay = pDisplay;
        if ( pListAll != null )
        {
            pListAll.add(this);
        }
    }
    
    public String getLocation()
    {
        return iLocation;
    }
    
    public String toString()
    {
        return iDisplay;
    }
    
    public static ArrayList getArmorFilterList()
    {
        return sListAll;
    }
	public static Point[] generateSkillLocs(int lCharCode) {

		Point[] iSkillLocs = new Point[30];
		switch(lCharCode){
		case 0:
//			cClass = "ama";
			iSkillLocs[0] = new Point(112,64);
			iSkillLocs[1] = new Point(173,64); 
			iSkillLocs[2] = new Point(50,125); 
			iSkillLocs[3] = new Point(112,125); 
			iSkillLocs[4] = new Point(173,187); 
			iSkillLocs[5] = new Point(50,248); 
			iSkillLocs[6] = new Point(112,248); 
			iSkillLocs[7] = new Point(112,307); 
			iSkillLocs[8] = new Point(173,307); 
			iSkillLocs[9] = new Point(50,370);

			iSkillLocs[10] = new Point(50,64); 
			iSkillLocs[11] = new Point(173,64); 
			iSkillLocs[12] = new Point(112,125); 
			iSkillLocs[13] = new Point(50,187); 
			iSkillLocs[14] = new Point(112,187); 
			iSkillLocs[15] = new Point(173,248); 
			iSkillLocs[16] = new Point(50,307); 
			iSkillLocs[17] = new Point(112,307); 
			iSkillLocs[18] = new Point(50,370); 
			iSkillLocs[19] = new Point(173,370); 

			iSkillLocs[20] = new Point(50,64); 
			iSkillLocs[21] = new Point(112,125); 
			iSkillLocs[22] = new Point(173,125); 
			iSkillLocs[23] = new Point(50,187); 
			iSkillLocs[24] = new Point(173,187); 
			iSkillLocs[25] = new Point(112,248); 
			iSkillLocs[26] = new Point(173,248); 
			iSkillLocs[27] = new Point(50,307); 
			iSkillLocs[28] = new Point(112,370); 
			iSkillLocs[29] = new Point(173,370); 
			break;
		case 1:
//			cClass = "sor";
			iSkillLocs[0] = new Point(112,64);
			iSkillLocs[1] = new Point(173,64); 
			iSkillLocs[2] = new Point(50,125); 
			iSkillLocs[3] = new Point(50,187);
			iSkillLocs[4] = new Point(112,187);
			iSkillLocs[5] = new Point(50,248); 
			iSkillLocs[6] = new Point(173,248); 
			iSkillLocs[7] = new Point(112,307); 
			iSkillLocs[8] = new Point(112,370); 
			iSkillLocs[9] = new Point(173,370); 

			iSkillLocs[10] = new Point(112,64); 
			iSkillLocs[11] = new Point(50,125); 
			iSkillLocs[12] = new Point(173,125); 
			iSkillLocs[13] = new Point(50,187); 
			iSkillLocs[14] = new Point(112,187); 
			iSkillLocs[15] = new Point(112,248); 
			iSkillLocs[16] = new Point(173,248); 
			iSkillLocs[17] = new Point(50,307); 
			iSkillLocs[18] = new Point(173,307); 
			iSkillLocs[19] = new Point(112,370); 

			iSkillLocs[20] = new Point(112,64); 
			iSkillLocs[21] = new Point(173,64); 
			iSkillLocs[22] = new Point(50,125); 
			iSkillLocs[23] = new Point(112,125); 
			iSkillLocs[24] = new Point(173,187); 
			iSkillLocs[25] = new Point(112,248); 
			iSkillLocs[26] = new Point(50,307); 
			iSkillLocs[27] = new Point(173,307); 
			iSkillLocs[28] = new Point(50,370); 
			iSkillLocs[29] = new Point(112,370); 
			break;
		case 2:
//			cClass = "nec";
			iSkillLocs[0] = new Point(112,64);
			iSkillLocs[1] = new Point(50,125); 
			iSkillLocs[2] = new Point(173,125); 
			iSkillLocs[3] = new Point(112,187);
			iSkillLocs[4] = new Point(173,187);
			iSkillLocs[5] = new Point(50,248); 
			iSkillLocs[6] = new Point(112,248); 
			iSkillLocs[7] = new Point(50,307); 
			iSkillLocs[8] = new Point(173,307); 
			iSkillLocs[9] = new Point(112,370); 

			iSkillLocs[10] = new Point(112,64); 
			iSkillLocs[11] = new Point(173,64); 
			iSkillLocs[12] = new Point(50,125); 
			iSkillLocs[13] = new Point(112,125); 
			iSkillLocs[14] = new Point(173,187); 
			iSkillLocs[15] = new Point(50,248); 
			iSkillLocs[16] = new Point(112,248); 
			iSkillLocs[17] = new Point(173,307); 
			iSkillLocs[18] = new Point(50,370); 
			iSkillLocs[19] = new Point(112,370); 

			iSkillLocs[20] = new Point(50,64); 
			iSkillLocs[21] = new Point(173,64); 
			iSkillLocs[22] = new Point(112,125); 
			iSkillLocs[23] = new Point(50,187); 
			iSkillLocs[24] = new Point(173,187); 
			iSkillLocs[25] = new Point(112,248); 
			iSkillLocs[26] = new Point(50,307); 
			iSkillLocs[27] = new Point(112,307); 
			iSkillLocs[28] = new Point(112,370); 
			iSkillLocs[29] = new Point(173,370); 
			break;
		case 3:
//			cClass = "pal";
			iSkillLocs[0] = new Point(50,64);
			iSkillLocs[1] = new Point(173,64); 
			iSkillLocs[2] = new Point(112,125); 
			iSkillLocs[3] = new Point(50,187);
			iSkillLocs[4] = new Point(173,187);
			iSkillLocs[5] = new Point(50,248); 
			iSkillLocs[6] = new Point(112,248); 
			iSkillLocs[7] = new Point(50,307); 
			iSkillLocs[8] = new Point(173,307); 
			iSkillLocs[9] = new Point(112,370); 

			iSkillLocs[10] = new Point(50,64); 
			iSkillLocs[11] = new Point(112,125); 
			iSkillLocs[12] = new Point(173,125); 
			iSkillLocs[13] = new Point(50,187); 
			iSkillLocs[14] = new Point(50,248); 
			iSkillLocs[15] = new Point(112,248); 
			iSkillLocs[16] = new Point(112,307); 
			iSkillLocs[17] = new Point(173,307); 
			iSkillLocs[18] = new Point(50,370); 
			iSkillLocs[19] = new Point(173,370); 

			iSkillLocs[20] = new Point(50,64); 
			iSkillLocs[21] = new Point(173,64); 
			iSkillLocs[22] = new Point(112,125); 
			iSkillLocs[23] = new Point(173,125); 
			iSkillLocs[24] = new Point(50,187); 
			iSkillLocs[25] = new Point(173,187); 
			iSkillLocs[26] = new Point(112,248); 
			iSkillLocs[27] = new Point(50,307); 
			iSkillLocs[28] = new Point(112,370); 
			iSkillLocs[29] = new Point(173,370); 
			break;
		case 4:
//			cClass = "bar";
			iSkillLocs[0] = new Point(112,64);
			iSkillLocs[1] = new Point(50,125); 
			iSkillLocs[2] = new Point(173,125); 
			iSkillLocs[3] = new Point(112,187);
			iSkillLocs[4] = new Point(173,187);
			iSkillLocs[5] = new Point(50,248); 
			iSkillLocs[6] = new Point(112,248); 
			iSkillLocs[7] = new Point(173,307); 
			iSkillLocs[8] = new Point(50,370); 
			iSkillLocs[9] = new Point(112,370); 

			iSkillLocs[10] = new Point(50,64); 
			iSkillLocs[11] = new Point(112,64); 
			iSkillLocs[12] = new Point(173,64); 
			iSkillLocs[13] = new Point(50,125); 
			iSkillLocs[14] = new Point(112,125); 
			iSkillLocs[15] = new Point(173,125); 
			iSkillLocs[16] = new Point(50,187); 
			iSkillLocs[17] = new Point(173,248); 
			iSkillLocs[18] = new Point(50,307); 
			iSkillLocs[19] = new Point(173,370); 

			iSkillLocs[20] = new Point(50,64); 
			iSkillLocs[21] = new Point(173,64); 
			iSkillLocs[22] = new Point(50,125); 
			iSkillLocs[23] = new Point(112,125); 
			iSkillLocs[24] = new Point(173,187); 
			iSkillLocs[25] = new Point(50,248); 
			iSkillLocs[26] = new Point(112,307); 
			iSkillLocs[27] = new Point(173,307); 
			iSkillLocs[28] = new Point(50,370); 
			iSkillLocs[29] = new Point(112,370); 
			break;
		case 5:
//			cClass = "dru";
			iSkillLocs[0] = new Point(112,64);
			iSkillLocs[1] = new Point(173,64); 
			iSkillLocs[2] = new Point(50,125); 
			iSkillLocs[3] = new Point(112,125);
			iSkillLocs[4] = new Point(173,187);
			iSkillLocs[5] = new Point(50,248); 
			iSkillLocs[6] = new Point(112,248); 
			iSkillLocs[7] = new Point(173,307); 
			iSkillLocs[8] = new Point(50,370); 
			iSkillLocs[9] = new Point(112,370); 

			iSkillLocs[10] = new Point(50,64); 
			iSkillLocs[11] = new Point(112,64); 
			iSkillLocs[12] = new Point(173,125); 
			iSkillLocs[13] = new Point(50,187); 
			iSkillLocs[14] = new Point(173,187); 
			iSkillLocs[15] = new Point(50,248); 
			iSkillLocs[16] = new Point(112,248); 
			iSkillLocs[17] = new Point(112,307); 
			iSkillLocs[18] = new Point(173,307); 
			iSkillLocs[19] = new Point(50,370); 

			iSkillLocs[20] = new Point(50,64); 
			iSkillLocs[21] = new Point(50,125); 
			iSkillLocs[22] = new Point(173,125); 
			iSkillLocs[23] = new Point(50,187); 
			iSkillLocs[24] = new Point(173,187); 
			iSkillLocs[25] = new Point(112,248); 
			iSkillLocs[26] = new Point(50,307); 
			iSkillLocs[27] = new Point(112,307); 
			iSkillLocs[28] = new Point(50,370); 
			iSkillLocs[29] = new Point(112,370); 
			break;
		case 6:
//			cClass = "ass";
			iSkillLocs[0] = new Point(112,64);
			iSkillLocs[1] = new Point(50,125); 
			iSkillLocs[2] = new Point(173,125); 
			iSkillLocs[3] = new Point(50,187);
			iSkillLocs[4] = new Point(112,187);
			iSkillLocs[5] = new Point(173,248); 
			iSkillLocs[6] = new Point(50,307); 
			iSkillLocs[7] = new Point(112,307); 
			iSkillLocs[8] = new Point(50,370); 
			iSkillLocs[9] = new Point(173,370); 

			iSkillLocs[10] = new Point(112,64); 
			iSkillLocs[11] = new Point(173,64); 
			iSkillLocs[12] = new Point(50,125); 
			iSkillLocs[13] = new Point(112,187); 
			iSkillLocs[14] = new Point(173,187); 
			iSkillLocs[15] = new Point(50,248); 
			iSkillLocs[16] = new Point(112,248); 
			iSkillLocs[17] = new Point(173,307); 
			iSkillLocs[18] = new Point(50,370); 
			iSkillLocs[19] = new Point(112,370); 

			iSkillLocs[20] = new Point(112,64); 
			iSkillLocs[21] = new Point(173,64); 
			iSkillLocs[22] = new Point(50,125); 
			iSkillLocs[23] = new Point(173,125); 
			iSkillLocs[24] = new Point(112,187); 
			iSkillLocs[25] = new Point(50,248); 
			iSkillLocs[26] = new Point(173,248); 
			iSkillLocs[27] = new Point(50,307); 
			iSkillLocs[28] = new Point(173,307); 
			iSkillLocs[29] = new Point(112,370); 
			break;
		}
		return iSkillLocs;
	}
    
}
