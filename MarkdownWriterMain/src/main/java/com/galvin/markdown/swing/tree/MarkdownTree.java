/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.tree;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.preferences.EditorPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.util.MimeTypes;
import galvin.StringUtils;
import galvin.swing.GuiUtils;
import galvin.swing.tree.DragAndDropTree;
import galvin.swing.tree.DraggableTreeNode;
import java.awt.Component;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class MarkdownTree
    extends DragAndDropTree {

    private static final int ROW_HEIGHT = 22;
    private Controller controller;
    private Project project;
    private MarkdownTreeNode trash;
    private MarkdownTreeNode config;
    private Node configNode;
    private MarkdownTreeNode metadata;
    private Node metadataNode;
    private MarkdownTreeNode styleSheet;
    private Node styleSheetNode;
    private MarkdownTreeNode coverImage;
    private Node coverImageNode;
    private boolean needsSaving;

    public MarkdownTree( Controller controller, Project project ) {
        super( new MarkdownTreeNode( project ) );
        this.project = project;
        this.controller = controller;

        setLargeModel( true );
        setCellRenderer( new MarkdownTreeCellRenderer() );
        setRowHeight( ROW_HEIGHT );

        Preferences preferences = MarkdownServer.getPreferences();
        EditorPreferences editorPreferences = preferences.getEditorPreferences();
        setBackground( editorPreferences.getBackgroundColor() );
        setForeground( editorPreferences.getTextColor() );

        MarkdownTreeNode root = (MarkdownTreeNode)getRootNode();

        configNode = new Node();

        configNode.setTitle( controller.getMessages().config() );
        configNode.setNodeType( NodeTypes.CONFIG );
        config = new MarkdownTreeNode( configNode );
        config.setIgnoreWhenSaving( true );
        root.add( config );

        metadataNode = new Node();
        metadataNode.setTitle( controller.getMessages().metadata() );
        metadataNode.setNodeType( NodeTypes.METADATA );
        metadata = new MarkdownTreeNode( metadataNode );
        metadata.setIgnoreWhenSaving( true );
        config.add( metadata );

        styleSheetNode = new Node();
        styleSheetNode.setTitle( controller.getMessages().styleSheet() );
        styleSheetNode.setNodeType( NodeTypes.STYLESHEET );
        styleSheet = new MarkdownTreeNode( styleSheetNode );
        styleSheet.setIgnoreWhenSaving( true );
        config.add( styleSheet );

        coverImageNode = new Node();
        coverImageNode.setTitle( controller.getMessages().coverImage() );
        coverImageNode.setNodeType( NodeTypes.COVER );
        coverImage = new MarkdownTreeNode( coverImageNode );
        coverImage.setIgnoreWhenSaving( true );
        config.add( coverImage );

        for( Node node : project.getChildNodes() ) {
            addNodes( root, node );
        }

        doExpansion( project.getChildNodes() );

        setRootVisible( false );
        setEditable( false );
        setNeedsSaving( false );

        addMouseListener( new ClickListener() );
    }

    public void projectRenamed() {
        getRootNode().setUserObject( project.getTitle() );
    }

    public MarkdownTreeNode addNodes( MarkdownTreeNode parentNode, Node markdownNode ) {
        MarkdownTreeNode treeNode = new MarkdownTreeNode( markdownNode );
        parentNode.add( treeNode );

        if( NodeTypes.TRASH.equals( markdownNode.getNodeType() ) ) {
            trash = treeNode;
        }

        for( Node node : markdownNode.getChildNodes() ) {
            addNodes( treeNode, node );
        }
        needsSaving = true;

        return treeNode;
    }

    private void doExpansion( List<Node> nodes ) {
        for( Node node : nodes ) {
            doExpansion( node.getChildNodes() );

            MarkdownTreeNode treeNode = getNode( node.getUuid() );
            if( treeNode != null ) {
                setExpanded( treeNode, node.isExpanded() );
            }
        }
    }

    public void deleteFromTree( DefaultMutableTreeNode node ) {
        if( trash != null && node != null ) {
            DefaultTreeModel model = (DefaultTreeModel)getModel();
            model.removeNodeFromParent( node );
        }
    }

    public void deleteFromTree( List<DefaultMutableTreeNode> nodes ) {
        for( DefaultMutableTreeNode node : nodes ) {
            deleteFromTree( node );
        }
    }

    public void moveToTrash( DefaultMutableTreeNode node ) {
        if( trash != null && node != null ) {
            DefaultTreeModel model = (DefaultTreeModel)getModel();
            if( !trash.isNodeDescendant( node ) ) {
                model.removeNodeFromParent( node );
                int index = trash.getChildCount();
                model.insertNodeInto( node, trash, index );
                needsSaving = true;
            }
        }
    }

    public void moveToTrash( List<DefaultMutableTreeNode> nodes ) {
        if( trash != null && nodes != null ) {
            for( DefaultMutableTreeNode node : nodes ) {
                moveToTrash( node );
            }
        }
    }

    public void emptyTrash() {
        if( trash != null ) {
            trash.removeAllChildren();
            needsSaving = true;
            GuiUtils.forceRepaint( this );
        }
    }

    @Override
    public void addChild( DefaultMutableTreeNode target, DefaultMutableTreeNode newNode ) {
        super.addChild( target, newNode );
        needsSaving = true;
    }

    @Override
    public void addSibling( DefaultMutableTreeNode target, DefaultMutableTreeNode newNode ) {
        super.addSibling( target, newNode );
        needsSaving = true;
    }

    @Override
    public void addSiblingAfter( DefaultMutableTreeNode target, DefaultMutableTreeNode newNode ) {
        super.addSiblingAfter( target, newNode );
        needsSaving = true;
    }

    public void addImageResource( File file )
        throws IOException {
        addImageResource( file, file.getName() );
    }

    public void addImageResource( File file, String name )
        throws IOException {
        if( MimeTypes.isImage( file ) ) {
            Node node = ProjectIo.createImageResource( file );
            node.setTitle( name );
            setUniqueResourceName( node );

            MarkdownTreeNode treeNode = new MarkdownTreeNode( node );
            addImageResource( treeNode );
        }
    }

    private void setUniqueResourceName( Node node ) {
        Node resources = project.getResources();
        MarkdownTreeNode resourcesNode = getNode( resources.getUuid() );
        if( resourcesNode != null ) {
            int count = 0;
            String title = node.getTitle();
            while( resourceWithNameExists( resourcesNode, title ) ) {
                count++;
                title = node.getTitle() + " (" + count + ")";
            }
            node.setTitle( title );
        }
    }

    private boolean resourceWithNameExists( MarkdownTreeNode resourcesNode, String title ) {
        int childCount = resourcesNode.getChildCount();
        for( int i = 0; i < childCount; i++ ) {
            MarkdownTreeNode child = (MarkdownTreeNode)resourcesNode.getChildAt( i );
            if( title.equals( child.getTitle() ) ) {
                return true;
            }
        }

        return false;
    }

    public void addImageResource( MarkdownTreeNode treeNode ) {
        Node resources = project.getResources();
        MarkdownTreeNode resourcesNode = getNode( resources.getUuid() );
        if( resourcesNode != null ) {
            String title = StringUtils.neverNull( treeNode.getTitle() ).toLowerCase();
            int addIndex = 0;

            int childCount = resourcesNode.getChildCount();
            for( int i = 0; i < childCount; i++ ) {
                MarkdownTreeNode child = (MarkdownTreeNode)resourcesNode.getChildAt( i );
                String childTitle = StringUtils.neverNull( child.getTitle() ).toLowerCase();
                if( childTitle.compareTo( title ) > 0 ) {
                    addIndex = i;
                    break;
                }
            }

            DefaultTreeModel model = (DefaultTreeModel)getModel();
            model.insertNodeInto( treeNode, resourcesNode, addIndex );
            project = ProjectIo.toProject( this );
        }
    }

    public void selectNode( String uuid ) {
        MarkdownTreeNode treeNode = getNode( uuid );
        if( treeNode != null ) {
            select( treeNode );
        }
    }

    public MarkdownTreeNode getNode( String uuid ) {
        MarkdownTreeNode treeNode = (MarkdownTreeNode)getRootNode();
        return getNode( uuid, treeNode );
    }

    private MarkdownTreeNode getNode( String uuid, MarkdownTreeNode treeNode ) {
        Project nodeProject = treeNode.getProject();
        if( nodeProject != null ) {
            if( uuid.equals( nodeProject.getUuid() ) ) {
                return treeNode;
            }
        }

        Node node = treeNode.getNode();
        if( node != null ) {
            if( uuid.equals( node.getUuid() ) ) {
                return treeNode;
            }
        }

        int childCount = treeNode.getChildCount();
        for( int i = 0; i < childCount; i++ ) {
            MarkdownTreeNode child = (MarkdownTreeNode)treeNode.getChildAt( i );
            MarkdownTreeNode result = getNode( uuid, child );
            if( result != null ) {
                return result;
            }
        }

        return null;
    }

    @Override
    public void drop( DropTargetDropEvent dropTargetDropEvent ) {
        boolean handled = false;
        for( DataFlavor dataFlavor : dropTargetDropEvent.getCurrentDataFlavors() ) {
            if( dataFlavor.isFlavorJavaFileListType() ) {
                handled = true;
                dropTargetDropEvent.acceptDrop( DnDConstants.ACTION_REFERENCE );
                Transferable transferable = dropTargetDropEvent.getTransferable();

                try {
                    Object object = transferable.getTransferData( dataFlavor );
                    List files = (List)object;
                    for( Object fileObject : files ) {
                        File file = (File)fileObject;
                        addImageResource( file );
                    }
                }
                catch( Throwable t ) {
                    t.printStackTrace();
                }
            }
            else {
                List<DraggableTreeNode> droppedNodes = getDroppedNodes( dropTargetDropEvent );
                for( DraggableTreeNode droppedNode : droppedNodes ) {
                    MarkdownTreeNode treeNode = (MarkdownTreeNode)droppedNode;
                    Node node = treeNode.getNode();
                    if( node != null ) {
                        node.setNeedsSaving( true );

                        if( node.getImageResource() != null ) {
                            try {
                                File imageFile = ProjectIo.getImageBinary( node, node.getProjectDirectory() );
                                if( imageFile != null ) {
                                    addImageResource( imageFile, node.getTitle() );
                                    handled = true;
                                }
                            }
                            catch( Throwable t ) {
                                t.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        if( !handled ) {
            super.drop( dropTargetDropEvent );
        }

        needsSaving = true;
    }

    @Override
    public void insertNodeAbove( DraggableTreeNode droppedNode, DraggableTreeNode dropTargetNode ) {
        if( isLocal( droppedNode ) ) {
            super.insertNodeAbove( droppedNode, dropTargetNode );
            return;
        }

        if( droppedNode instanceof MarkdownTreeNode ) {
            MarkdownTreeNode droppedMarkdownTreeNode = (MarkdownTreeNode)droppedNode;
            Node node = droppedMarkdownTreeNode.getNode();
            if( node != null ) {
                try {
                    Node clonedNode = ProjectIo.clone( node, node.getProjectDirectory(), false, false );
                    MarkdownTreeNode root = (MarkdownTreeNode)getRootNode();
                    droppedNode = addNodes( root, clonedNode );
                }
                catch( Throwable t ) {
                    t.printStackTrace();
                }
            }
        }

        super.insertNodeAbove( droppedNode, dropTargetNode );
    }

    @Override
    public void insertNodeBelow( DraggableTreeNode droppedNode, DraggableTreeNode dropTargetNode ) {
        if( isLocal( droppedNode ) ) {
            super.insertNodeBelow( droppedNode, dropTargetNode );
            return;
        }

        if( droppedNode instanceof MarkdownTreeNode ) {
            MarkdownTreeNode droppedMarkdownTreeNode = (MarkdownTreeNode)droppedNode;
            Node node = droppedMarkdownTreeNode.getNode();
            if( node != null ) {
                try {
                    Node clonedNode = ProjectIo.clone( node, node.getProjectDirectory(), false, false );
                    MarkdownTreeNode root = (MarkdownTreeNode)getRootNode();
                    droppedNode = addNodes( root, clonedNode );
                }
                catch( Throwable t ) {
                    t.printStackTrace();
                }
            }
        }

        super.insertNodeBelow( droppedNode, dropTargetNode );
    }

    @Override
    public void insertNodeAsNewChild( DraggableTreeNode droppedNode, DraggableTreeNode dropTargetNode ) {
        if( isLocal( droppedNode ) ) {
            super.insertNodeAsNewChild( droppedNode, dropTargetNode );
            return;
        }

        if( droppedNode instanceof MarkdownTreeNode ) {
            MarkdownTreeNode droppedMarkdownTreeNode = (MarkdownTreeNode)droppedNode;
            Node node = droppedMarkdownTreeNode.getNode();
            if( node != null ) {
                try {
                    Node clonedNode = ProjectIo.clone( node, node.getProjectDirectory(), false, false );
                    MarkdownTreeNode root = (MarkdownTreeNode)getRootNode();
                    droppedNode = addNodes( root, clonedNode );
                }
                catch( Throwable t ) {
                    t.printStackTrace();
                }
            }
        }

        super.insertNodeAsNewChild( droppedNode, dropTargetNode );
    }

    private boolean isLocal( DraggableTreeNode droppedNode ) {
        if( droppedNode instanceof MarkdownTreeNode ) {
            MarkdownTreeNode markdownTreeNode = (MarkdownTreeNode)droppedNode;
            String uuid = null;

            Node node = markdownTreeNode.getNode();
            if( node != null ) {
                uuid = node.getUuid();
            }
            else {
                Project project = markdownTreeNode.getProject();
                if( project != null ) {
                    uuid = project.getUuid();
                }
            }

            if( !StringUtils.empty( uuid ) ) {
                MarkdownTreeNode loadedNode = getNode( uuid );
                if( loadedNode != null ) {
                    return true;
                }
            }
        }

        return false;
    }

    public Controller getController() {
        return controller;
    }

    public boolean needsSaving() {
        return needsSaving;
    }

    public void setNeedsSaving( boolean needsSaving ) {
        this.needsSaving = needsSaving;
    }

    private class ClickListener
        extends MouseAdapter {

        @Override
        public void mouseReleased( MouseEvent event ) {
            if( SwingUtilities.isRightMouseButton( event ) ) {
                Point point = event.getPoint();
                Component sourceConponent = event.getComponent();
                TreePath path = getPathForLocation( point.x, point.y );
                TreePath[] paths = getSelectionPaths();

                if( path != null ) {
                    MarkdownTreeNode node = (MarkdownTreeNode)path.getLastPathComponent();
                    MarkdownTreeMenu menu = new MarkdownTreeMenu( MarkdownTree.this, node, paths );

                    menu.show( sourceConponent, event.getX(), event.getY() );
                    if( menu.getParent().getX() == 0 ) {
                        menu.show( sourceConponent, event.getX(), event.getY() - menu.getHeight() );
                    }
                }
            }
        }

    }

}
