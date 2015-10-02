package com.galvin.markdown.swing.tree;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class MarkdownTreeMenu
        extends JPopupMenu
        implements ActionListener
{

    private MarkdownTree tree;
    private Controller controller;
    private MarkdownTreeNode treeNode;
    private Node node;
    private TreePath[] paths;
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JMenu addChild = new JMenu( messages.treeMenuAddChild() );
    private JMenuItem addChildText = new JMenuItem( messages.treeMenuAddText() );
    private JMenuItem addChildFolder = new JMenuItem( messages.treeMenuAddFolder() );
    
    private JMenu addSibling = new JMenu( messages.treeMenuAddSibling() );
    private JMenuItem addSiblingText = new JMenuItem( messages.treeMenuAddText() );
    private JMenuItem addSiblingFolder = new JMenuItem( messages.treeMenuAddFolder() );
    
    private JMenuItem rename = new JMenuItem( messages.treeMenuRename() );
    private JMenuItem delete = new JMenuItem( messages.treeMenuDelete() );
    private JMenuItem duplicate = new JMenuItem( messages.treeMenuDuplicate() );
    private JMenuItem join = new JMenuItem( messages.treeMenuJoin() );
    private JMenuItem convertToFile = new JMenuItem( messages.treeMenuConvertToFile() );
    private JMenuItem convertToFolder = new JMenuItem( messages.treeMenuConvertToFolder() );
    
    private JMenuItem emptyTrash = new JMenuItem( messages.treeMenuEmptyTrash() );

    public MarkdownTreeMenu( MarkdownTree tree, MarkdownTreeNode treeNode, TreePath[] paths )
    {
        this.tree = tree;
        this.controller = tree.getController();
        this.paths = paths;
        this.treeNode = treeNode;
        this.node = treeNode.getNode();

        addSibling.add( addSiblingText );
        addSibling.add( addSiblingFolder );
        
        addChild.add( addChildText );
        addChild.add( addChildFolder );
        
        add( addSibling );
        add( addChild );
        addSeparator();
        add( rename );
        addSeparator();

        String nodeType = NodeTypes.PROJECT;
        if( node != null )
        {
            nodeType = node.getNodeType();
        }
        
        if( NodeTypes.MARKDOWN.equals( nodeType ) )
        {
            add( convertToFolder );
        }
        else
        {
            add( convertToFile );
        }
        
        addSeparator();
        add( duplicate );
        add( join );
        addSeparator();
        add( delete );
        addSeparator();
        add( emptyTrash );

        if( paths == null || paths.length < 2 )
        {
            join.setEnabled( false );
        }
        
        if( paths != null && paths.length > 1 )
        {
            addChild.setEnabled( false );
            addChildText.setEnabled( false );
            addChildFolder.setEnabled( false );
            addSibling.setEnabled( false );
            addSiblingText.setEnabled( false );
            addSiblingFolder.setEnabled( false );
            rename.setEnabled( false );
            duplicate.setEnabled( false );
            convertToFile.setEnabled( false );
            convertToFolder.setEnabled( false );
        }
        
        if( NodeTypes.PROJECT.equals( nodeType ) ||
            NodeTypes.MANUSCRIPT.equals( nodeType ) ||
            NodeTypes.RESOURCES.equals( nodeType ) ||
            NodeTypes.TRASH.equals( nodeType ))
        {
            delete.setEnabled( false );
            convertToFile.setEnabled( false );
            convertToFolder.setEnabled( false );
            join.setEnabled( false );
        }
        
        addChildText.addActionListener( this );
        addSiblingText.addActionListener( this );
        addChildFolder.addActionListener( this );
        addSiblingFolder.addActionListener( this );
        rename.addActionListener( this );
        convertToFile.addActionListener( this );
        convertToFolder.addActionListener( this );
        duplicate.addActionListener( this );
        join.addActionListener( this );
        delete.addActionListener( this );
        emptyTrash.addActionListener( this );
    }

    public void actionPerformed( ActionEvent e )
    {
        Object source = e.getSource();
        if( source == addChildText )
        {
            controller.documentsNewChildFile( treeNode );
        }
        else if( source == addChildFolder )
        {
            controller.documentsNewChildFolder( treeNode );
        }
        if( source == addSiblingText )
        {
            controller.documentsNewSiblingFile( treeNode );
        }
        else if( source == addSiblingFolder )
        {
            controller.documentsNewSiblingFolder( treeNode );
        }
        else if( source == rename )
        {
            controller.documentsRename( treeNode );
        }
        else if( source == duplicate )
        {
            controller.documentsDuplicate( treeNode );
        }
        else if( source == join )
        {
            controller.documentsJoin();
        }
        else if( source == delete )
        {
            List<DefaultMutableTreeNode> nodes = tree.getSelectedNodes();
            if( !nodes.contains( treeNode ) )
            {
                nodes.add( treeNode );
            }
            
            controller.documentsDelete( nodes );
        }
        else if( source == convertToFile )
        {
            controller.documentsConvertToFile( treeNode );
        }
        else if( source == convertToFolder )
        {
            controller.documentsConvertToFolder( treeNode );
        }
        else if( source == emptyTrash )
        {
            controller.documentsEmptyTrash();
        }
    }
}
