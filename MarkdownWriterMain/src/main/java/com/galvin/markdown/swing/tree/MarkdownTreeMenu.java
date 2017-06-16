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
    implements ActionListener {

    private MarkdownTree tree;
    private Controller controller;
    private MarkdownTreeNode treeNode;
    private Node node;
    private TreePath[] paths;
    private MarkdownMessages messages = MarkdownServer.getMessages();

    private JMenuItem addSiblingText = new JMenuItem( messages.menuSibling() );
    private JMenuItem addChildText = new JMenuItem( messages.menuChild() );

    private JMenuItem rename = new JMenuItem( messages.menuRename() );
    private JMenuItem delete = new JMenuItem( messages.menuMoveToTrash() );
    private JMenuItem duplicate = new JMenuItem( messages.menuDuplicate() );
    private JMenuItem join = new JMenuItem( messages.menuJoinDocuments() );
    
    private JMenu fileExportMenu = new JMenu( messages.menuExport() );
    private JMenuItem fileExportProject = new JMenuItem( messages.menuExportProject() );
    private JMenuItem fileExportProjectUsingCurrentOptions = new JMenuItem( messages.menuExportProjectCurrentOptions() );
    private JMenuItem fileExportCurrentDocument = new JMenuItem( messages.menuExportDocument() );
    private JMenuItem fileExportCurrentDocumentUsingCurrentOptions = new JMenuItem( messages.menuExportDocumentCurrentOptions() );
    private JMenuItem fileExportCurrentDocumentAndChildren = new JMenuItem( messages.menuExportDocumentAndChildren() );
    private JMenuItem fileExportCurrentDocumentAndChildrenUsingCurrentOptions = new JMenuItem( messages.menuExportDocumentAndChildrenCurrentOptions() );
    
    private JMenu filePreviewMenu = new JMenu( messages.menuPreview() );
    private JMenuItem filePreviewCurrentDocument = new JMenuItem( messages.menuPreviewDocument() );
    private JMenuItem filePreviewCurrentDocumentAndChildren = new JMenuItem( messages.menuPreviewDocumentAndChildren() );

    private JMenuItem emptyTrash = new JMenuItem( messages.menuEmptyTrash() );

    public MarkdownTreeMenu( MarkdownTree tree, MarkdownTreeNode treeNode, TreePath[] paths ) {
        this.tree = tree;
        this.controller = tree.getController();
        this.paths = paths;
        this.treeNode = treeNode;
        this.node = treeNode.getNode();
        
        fileExportMenu.add( fileExportProject );
        fileExportMenu.add( fileExportProjectUsingCurrentOptions );
        fileExportMenu.addSeparator();
        fileExportMenu.add( fileExportCurrentDocument );
        fileExportMenu.add( fileExportCurrentDocumentUsingCurrentOptions );
        fileExportMenu.add( fileExportCurrentDocumentAndChildren );
        fileExportMenu.add( fileExportCurrentDocumentAndChildrenUsingCurrentOptions );
        
        filePreviewMenu.add( filePreviewCurrentDocument );
        filePreviewMenu.add( filePreviewCurrentDocumentAndChildren );

        add( addSiblingText );
        add( addChildText );
        addSeparator();
        add( rename );
        addSeparator();
        add( duplicate );
        add( join );
        addSeparator();
        add( fileExportMenu );
        add( filePreviewMenu );
        addSeparator();
        add( delete );
        addSeparator();
        add( emptyTrash );

        if( paths == null || paths.length < 2 ) {
            join.setEnabled( false );
        }

        if( paths != null && paths.length > 1 ) {
            addChildText.setEnabled( false );
            addSiblingText.setEnabled( false );
            rename.setEnabled( false );
            duplicate.setEnabled( false );
        }

        String nodeType = NodeTypes.PROJECT;
        if( node != null ) {
            nodeType = node.getNodeType();
        }

        if( NodeTypes.PROJECT.equals( nodeType )
            || NodeTypes.MANUSCRIPT.equals( nodeType )
            || NodeTypes.RESOURCES.equals( nodeType )
            || NodeTypes.TRASH.equals( nodeType ) ) {
            delete.setEnabled( false );
            join.setEnabled( false );
        }

        addChildText.addActionListener( this );
        addSiblingText.addActionListener( this );
        rename.addActionListener( this );
        duplicate.addActionListener( this );
        join.addActionListener( this );
        delete.addActionListener( this );
        emptyTrash.addActionListener( this );
        
        fileExportProject.addActionListener( this );
        fileExportProjectUsingCurrentOptions.addActionListener( this );
        fileExportCurrentDocument.addActionListener( this );
        fileExportCurrentDocumentUsingCurrentOptions.addActionListener( this );
        fileExportCurrentDocumentAndChildren.addActionListener( this );
        fileExportCurrentDocumentAndChildrenUsingCurrentOptions.addActionListener( this );
        
        filePreviewCurrentDocument.addActionListener( this );
        filePreviewCurrentDocumentAndChildren.addActionListener( this );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        Object source = e.getSource();
        if( source == addChildText ) {
            controller.documentsNewChildFile( treeNode );
        }
        if( source == addSiblingText ) {
            controller.documentsNewSiblingFile( treeNode );
        }
        else if( source == rename ) {
            controller.documentsRename( treeNode );
        }
        else if( source == duplicate ) {
            controller.documentsDuplicate( treeNode );
        }
        else if( source == join ) {
            controller.documentsJoin();
        }
        else if( source == fileExportProject ) {
            controller.fileExportProject();
        }
        else if( source == fileExportProjectUsingCurrentOptions ) {
            controller.fileExportProjectUsingCurrentOptions();
        }
        else if( source == fileExportCurrentDocument ) {
            controller.fileExportCurrentDocument();
        }
        else if( source == fileExportCurrentDocumentUsingCurrentOptions ) {
            controller.fileExportCurrentDocumentUsingCurrentOptions();
        }
        else if( source == fileExportCurrentDocumentAndChildren ) {
            controller.fileExportCurrentDocumentAndChildren();
        }
        else if( source == fileExportCurrentDocumentAndChildrenUsingCurrentOptions ) {
            controller.fileExportCurrentDocumentAndChildrenUsingCurrentOptionst();
        }
        else if( source == filePreviewCurrentDocument ) {
            controller.filePreview();
        }
        else if( source == filePreviewCurrentDocumentAndChildren ) {
            controller.filePreviewWithChildren();
        }
        else if( source == delete ) {
            List<DefaultMutableTreeNode> nodes = tree.getSelectedNodes();
            if( !nodes.contains( treeNode ) ) {
                nodes.add( treeNode );
            }

            controller.documentsDelete( nodes );
        }
        else if( source == emptyTrash ) {
            controller.documentsEmptyTrash();
        }
    }

}
