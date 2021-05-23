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

package gomule.gui;

import gomule.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

public class D2ViewProject extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7147981187044642216L;
	private D2FileManager          	iFileManager;
	private D2Project              	iProject;

	private JTree                  	iTree;
	private DefaultTreeModel       	iTreeModel;
	private DefaultMutableTreeNode 	iChars;
	private DefaultMutableTreeNode 	iStashes;
	private CharTreeNode 			iAll;

	public D2ViewProject(D2FileManager pFileManager)
	{
		iFileManager = pFileManager;
		setLayout(new BorderLayout());
		iTreeModel = refreshTree();
		iTree = new JTree(iTreeModel)
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 3529087650697421360L;

			public void scrollRectToVisible(Rectangle aRect)
			{
				// disable scrolling
			}
		};
		iTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		iTree.setCellRenderer(new DefaultTreeCellRenderer()
		{
			/**
			 * 
			 */
			private static final long serialVersionUID = 4441305975881423913L;

			public Component getTreeCellRendererComponent(JTree pTree, Object pValue,
					boolean pSel,
					boolean pExpanded,
					boolean pLeaf, int pRow,
					boolean pHasFocus) 
			{
				Component lRenderer = super.getTreeCellRendererComponent( pTree,  pValue, pSel, 
						pExpanded, pLeaf, pRow, pHasFocus);

				if ( pValue instanceof CharTreeNode )
				{
					CharTreeNode lNode = (CharTreeNode) pValue;
					lRenderer.setForeground( lNode.getForeGround() );
				}

				return lRenderer;
			}
		});

		JScrollPane lScroll = new JScrollPane(iTree);
		add(lScroll, BorderLayout.CENTER);

		iTree.addMouseListener(new MouseListener()
 {
			private long		iLastRelease = -1;
			private long		iLastClickCount = 0;
			private TreePath	iLastTreePath = null;

			public void mousePressed(MouseEvent e){}
			public void mouseClicked(MouseEvent e) {}
			public void mouseReleased(MouseEvent e)
			{
				iFileManager.workCursor();
				try{
					if ( e.getX() >=0 && e.getX() <= iTree.getWidth()
							&& e.getY() >= 0 && e.getY() <= iTree.getHeight() )
					{
						TreePath lPath = iTree.getPathForLocation(e.getX(), e.getY());
						if(lPath == null)
						{
							return;
						}
						
						long lNow = System.currentTimeMillis();
						if ( lNow < iLastRelease + 500 ) {
							iLastClickCount++;
							if ( iLastTreePath != lPath ) {
								return;
							}
						} else {
							iLastClickCount = 1;
							iLastRelease = lNow;
							iLastTreePath = lPath;
						}
						
						if ( e.getButton() == MouseEvent.BUTTON1 && iLastClickCount == 2 )
						{
							Object lPathObjects[] = lPath.getPath();
							Object lLast = lPathObjects[lPathObjects.length - 1];
							if (lLast instanceof CharTreeNode)
							{
								((CharTreeNode) lLast).view();
							}
						}
						if ( e.getButton() == MouseEvent.BUTTON3 && iLastClickCount == 1 )
						{
							Object lPathObjects[] = lPath.getPath();
							Object lLast = lPathObjects[lPathObjects.length - 1];
							if (lLast instanceof CharTreeNode)
							{
								((CharTreeNode) lLast).startMenu(e.getX(), e.getY());
							}
						}
					}
				}finally{
					iFileManager.defaultCursor();
				}
			}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
		});
		iTree.addKeyListener(new KeyAdapter()
		{
			public void keyReleased(KeyEvent e) 
			{
				if ( (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) && iTree.getSelectionCount() >0 )
				{

					int lSelected[] = iTree.getSelectionRows();
					for ( int i = 0 ; i < lSelected.length ; i++ )
					{
						TreePath lPath = iTree.getPathForRow(lSelected[i]);
						Object lPathObjects[] = lPath.getPath();
						Object lLast = lPathObjects[lPathObjects.length - 1];
						if (lLast instanceof CharTreeNode)
						{
							((CharTreeNode) lLast).remove();
						}
					}
				}
				else if ( e.getModifiers() == KeyEvent.ALT_MASK )
				{
					if ( e.getKeyCode() == KeyEvent.VK_V || e.getKeyCode() == KeyEvent.VK_O  || e.getKeyCode() == KeyEvent.VK_M )
					{
						int lSelected[] = iTree.getSelectionRows();
						for ( int i = 0 ; i < lSelected.length ; i++ )
						{
							TreePath lPath = iTree.getPathForRow(lSelected[i]);
							Object lPathObjects[] = lPath.getPath();
							Object lLast = lPathObjects[lPathObjects.length - 1];
							if (lLast instanceof CharTreeNode)
							{
								((CharTreeNode) lLast).view();
							}
						}
					}
					else if ( e.getKeyCode() == KeyEvent.VK_C  )
					{
						int lSelected[] = iTree.getSelectionRows();
						for ( int i = 0 ; i < lSelected.length ; i++ )
						{
							TreePath lPath = iTree.getPathForRow(lSelected[i]);
							Object lPathObjects[] = lPath.getPath();
							Object lLast = lPathObjects[lPathObjects.length - 1];
							if (lLast instanceof CharTreeNode)
							{
								((CharTreeNode) lLast).close();
							}
						}
					}
					else if ( e.getKeyCode() == KeyEvent.VK_F  )
					{
						int lSelected[] = iTree.getSelectionRows();
						for ( int i = 0 ; i < lSelected.length ; i++ )
						{
							TreePath lPath = iTree.getPathForRow(lSelected[i]);
							Object lPathObjects[] = lPath.getPath();
							Object lLast = lPathObjects[lPathObjects.length - 1];
							if (lLast instanceof CharTreeNode)
							{
								((CharTreeNode) lLast).fullDump();
							}
						}
					}
				}
			}
		});
	}

	public void notifyFileOpened(String pFileName)
	{
		CharTreeNode lNode = searchNode(pFileName);
		if ( lNode != null )
		{
			lNode.fileOpened();
			iTree.repaint();
		}
	}

	public void notifyFileClosed(String pFileName)
	{
		CharTreeNode lNode = searchNode(pFileName);
		if ( lNode != null )
		{
			lNode.fileClosed();
			iTree.repaint();
		}
	}

	public void notifyItemListRead(String pItemListFileName)
	{
		CharTreeNode lNode = searchNode(pItemListFileName);
		if ( lNode != null )
		{
			lNode.itemListRead();
			iTree.repaint();
		}
	}

	public void notifyItemListClosed(String pItemListFileName)
	{
		CharTreeNode lNode = searchNode(pItemListFileName);
		if ( lNode != null )
		{
			lNode.itemListClosed();
			iTree.repaint();
		}
	}

	private CharTreeNode searchNode(String pFileName)
	{
		if ( pFileName.toLowerCase().endsWith(".d2s") )
		{
			return searchSingleNode(iChars, pFileName);
		}
		else if ( pFileName.toLowerCase().endsWith(".d2x") )
		{
			return searchSingleNode(iStashes, pFileName);
		}
		else if ( pFileName.equalsIgnoreCase("all") )
		{
			return iAll;
		}
		return null;
	}

	private CharTreeNode searchSingleNode(DefaultMutableTreeNode pTreeNode, String pFileName)
	{
		for ( int i = 0 ; i < pTreeNode.getChildCount() ; i++ )
		{
			CharTreeNode lNode = (CharTreeNode) pTreeNode.getChildAt(i);
			if ( lNode.getFilename().equalsIgnoreCase(pFileName) )
			{
				return lNode;
			}
		}
		System.err.println("D2ViewProject.searchNode() Not Found " + pFileName );
		return null;
	}

	public void setProject(D2Project pProject)
	{
		iProject = pProject;
		refreshTreeModel(true, true);
	}

	public void refreshTreeModel(boolean pExpandChar, boolean pExpandStash)
	{
		boolean lChar = false;
		boolean lStash = false;
		for (int i = 0; i < iTree.getRowCount(); i++)
		{
			TreePath lPath = iTree.getPathForRow(i);
			Object lPathObjects[] = lPath.getPath();
			if (lPathObjects.length > 1 && lPathObjects[lPathObjects.length - 1] == iChars)
			{
				lChar = iTree.isExpanded(i);
			}
			if (lPathObjects.length > 1 && lPathObjects[lPathObjects.length - 1] == iStashes)
			{
				lStash = iTree.isExpanded(i);
			}
		}
		iTreeModel = refreshTree();
		iTree.setModel(iTreeModel);
		for (int i = 0; i < iTree.getRowCount(); i++)
		{
			TreePath lPath = iTree.getPathForRow(i);
			Object lPathObjects[] = lPath.getPath();
			if (lPathObjects.length > 1 && lPathObjects[lPathObjects.length - 1] == iChars)
			{
				if (lChar || pExpandChar)
				{
					iTree.expandRow(i);
				}
			}
			if (lPathObjects.length > 1 && lPathObjects[lPathObjects.length - 1] == iStashes)
			{
				if (lStash || pExpandStash)
				{
					iTree.expandRow(i);
				}
			}
		}
	}

	private DefaultTreeModel refreshTree()
	{
		DefaultMutableTreeNode root = new DefaultMutableTreeNode("D2");

//		DefaultMutableTreeNode parent;

		iChars = new DefaultMutableTreeNode("characters");
		root.add(iChars);
		if ( iProject != null )
		{
			ArrayList lCharList = iProject.getCharList();
			for (int i = 0; i < lCharList.size(); i++)
			{
				iChars.add(new CharTreeNode((String) lCharList.get(i)));
			}
		}

		iStashes = new DefaultMutableTreeNode("stashes");
		root.add(iStashes);
		if ( iProject != null )
		{
			ArrayList lStashList = iProject.getStashList();
			for (int i = 0; i < lStashList.size(); i++)
			{
				iStashes.add(new CharTreeNode((String) lStashList.get(i)));
			}
		}

		iAll = new CharTreeNode("All");
		root.add( iAll );

		return new DefaultTreeModel(root);
	}

	private class CharTreeNode extends DefaultMutableTreeNode
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7061935461861570778L;
		private String	iFileName;
		private Color	iForeGround = Color.black;
		private boolean	iItemListRead = false;
		private boolean iFileOpened = false;

		public CharTreeNode(String pFileName)
		{
			super(getCharStr(pFileName));
			iFileName = pFileName;

			if ( !iFileName.toLowerCase().equals("all") )
			{
				File lTest = new File(iFileName);
				if ( !lTest.exists() || !lTest.isFile() || !lTest.canRead() || !lTest.canWrite() )
				{
					iForeGround = Color.red;
				}
			}
		}

		private String getFilename()
		{
			return iFileName;
		}

		public void itemListRead()
		{
			iItemListRead = true;
			iForeGround = Color.blue;
		}

		public void itemListClosed()
		{
			iItemListRead = false;
			iForeGround = Color.black;
		}

		public void fileOpened()
		{
			iFileOpened = true;
		}

		public void fileClosed()
		{
			iFileOpened = false;
		}

		public Color getForeGround()
		{
			return iForeGround;
		}

		public void view()
		{
			if ( iFileName.equalsIgnoreCase("all") )
			{
				// open All
//				System.err.println("All view not done yet");
				iFileManager.openStash("all", true);
			}
			if ( iFileName.toLowerCase().endsWith(".d2s") )
			{
				iFileManager.openChar(iFileName, true);
			}
			else if ( iFileName.toLowerCase().endsWith(".d2x") )
			{
				iFileManager.openStash(iFileName, true);
			}
		}

		public void close()
		{
			iFileManager.closeFileName( getFilename() );
		}

		public void fullDump()
		{
			iFileManager.singleTxtDump( getFilename());
		}

		public void remove()
		{
			iProject.deleteCharStash( getFilename() );
		}

		public void startMenu(int pX, int pY)
		{
			JPopupMenu lMenu = new JPopupMenu( );

//			JMenuItem lName = new JMenuItem( getFilename() );
			lMenu.add( new JLabel(getFilename()) );

//			lMenu.setLocation(pX, pY);

//			JMenu lList = new JMenu();

			if ( iFileOpened )
			{
				JMenuItem lOpen = new JMenuItem("Move to Top");
				lOpen.setAccelerator( KeyStroke.getKeyStroke(new Character('M'), KeyEvent.ALT_MASK) );
				lMenu.add(lOpen);
				lOpen.addActionListener(new ActionNodeView(this));

				JMenuItem lFullDump = new JMenuItem("Full Dump");
				lFullDump.setAccelerator( KeyStroke.getKeyStroke(new Character('F'), KeyEvent.ALT_MASK) );
//				lMenu.add(lFullDump);
				lFullDump.addActionListener(new ActionNodeFullDump(this));

				JMenuItem lClose = new JMenuItem("Close");
				lClose.setAccelerator( KeyStroke.getKeyStroke(new Character('C'), KeyEvent.ALT_MASK) );
				lMenu.add(lClose);
				lClose.addActionListener(new ActionNodeClose(this));
			}
			else
			{
				if ( iItemListRead )
				{
					JMenuItem lOpen = new JMenuItem("View");
					lOpen.setAccelerator( KeyStroke.getKeyStroke(new Character('V'), KeyEvent.ALT_MASK) );
					lMenu.add(lOpen);
					lOpen.addActionListener(new ActionNodeView(this));
				}
				else
				{
					JMenuItem lOpen = new JMenuItem("Open");
					lOpen.setAccelerator( KeyStroke.getKeyStroke(new Character('O'), KeyEvent.ALT_MASK) );
					lMenu.add(lOpen);
					lOpen.addActionListener(new ActionNodeView(this));
				}
			}

			JMenuItem lRemove = new JMenuItem("Remove from project");
			lRemove.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0) );
			lMenu.add(lRemove);
			lRemove.addActionListener(new ActionNodeRemove(this));

			lMenu.show(iTree, pX, pY);
//			lMenu.add()
		}

	} // end class CharTreeNode

	private abstract class NodeAction implements ActionListener
	{
		protected CharTreeNode iNode;

		public NodeAction(CharTreeNode pNode)
		{
			iNode = pNode;
		}
	}

	private class ActionNodeView extends NodeAction
	{
		public ActionNodeView(CharTreeNode pNode)
		{
			super(pNode);
		}

		public void actionPerformed(ActionEvent pEvent)
		{
			iNode.view();
		}
	}

	private class ActionNodeRemove extends NodeAction
	{
		public ActionNodeRemove(CharTreeNode pNode)
		{
			super(pNode);
		}

		public void actionPerformed(ActionEvent pEvent)
		{
			iNode.remove();
		}
	}

	private class ActionNodeClose extends NodeAction
	{
		public ActionNodeClose(CharTreeNode pNode)
		{
			super(pNode);
		}

		public void actionPerformed(ActionEvent pEvent)
		{
			iNode.close();
		}
	}

	private class ActionNodeFullDump extends NodeAction
	{
		public ActionNodeFullDump(CharTreeNode pNode)
		{
			super(pNode);
		}

		public void actionPerformed(ActionEvent pEvent)
		{
			iNode.fullDump();
		}
	}

	private static String getCharStr(String pFileName)
	{
		if ( pFileName.equalsIgnoreCase("all") )
		{
			return pFileName;
		}
		File lFile = new File(pFileName);
		return lFile.getName() + " (" + pFileName + ")";
	}

}