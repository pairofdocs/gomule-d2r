/*
 * Created on 11-mei-2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package gomule.gui;

import javax.swing.*;

/**
 * @author Marco
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class D2RadioButton extends JRadioButton
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3895689813117999958L;
	private Object iData;
    
    public D2RadioButton(Object pData)
    {
        super(pData.toString());
        iData = pData;
    }
    
    
    public Object getData()
    {
        return iData;
    }

}
