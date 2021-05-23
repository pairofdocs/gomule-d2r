package gomule.gui.desktop.generic;

import java.awt.Cursor;

/**
 * handler for actual gui element handling the display on the desktop
 * @author mbr
 *
 */
public interface GoMuleViewDisplayHandler 
{
	public void setTitle(String title);
	public void setCursor(Cursor pCursor);
	public void addDesktopListener(GoMuleDesktopListener pListener);
}
