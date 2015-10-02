package com.galvin.markdown.swing.tree;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.model.Project;
import galvin.swing.tree.DraggableTreeNode;

public class MarkdownTreeNode
        extends DraggableTreeNode
{

    private Project project;
    private Node node;
    private boolean ignoreWhenSaving = false;

    public MarkdownTreeNode( Project project )
    {
        super( project.getTitle() );
        this.project = project;
    }

    public MarkdownTreeNode( Node node )
    {
        super( node.getTitle() );
        this.node = node;

        if( NodeTypes.TRASH.equals( node.getNodeType() ) )
        {
            setCanBeReparented( false );
        }
        else if( NodeTypes.CONFIG.equals( node.getNodeType() ) )
        {
            setDraggable( false );
            setCanBeReparented( false );
            setAllowMoreChildren( false );
        }
        else if( NodeTypes.METADATA.equals( node.getNodeType() ) )
        {
            setDraggable( false );
            setCanBeReparented( false );
            setAllowMoreChildren( false );
        }
        else if( NodeTypes.STYLESHEET.equals( node.getNodeType() ) )
        {
            setDraggable( false );
            setCanBeReparented( false );
            setAllowMoreChildren( false );
        }
        else if( NodeTypes.COVER.equals( node.getNodeType() ) )
        {
            setDraggable( false );
            setCanBeReparented( false );
            setAllowMoreChildren( false );
        }
        else if( NodeTypes.MANUSCRIPT.equals( node.getNodeType() ) )
        {
            setCanBeReparented( false );
        }
        else if( NodeTypes.RESOURCES.equals( node.getNodeType() ) )
        {
            setCanBeReparented( false );
            setAllowMoreChildren( false );
        }
        else if( NodeTypes.RESOURCE.equals( node.getNodeType() ) )
        {
            setDraggable( false );
            setCanBeReparented( false );
            setAllowMoreChildren( false );
        }
    }

    public Node getNode()
    {
        return node;
    }

    public Project getProject()
    {
        return project;
    }

    public boolean ignoreWhenSaving()
    {
        return ignoreWhenSaving;
    }

    public void setIgnoreWhenSaving( boolean ignoreWhenSaving )
    {
        this.ignoreWhenSaving = ignoreWhenSaving;
    }

    public String getTitle()
    {
        if( project != null )
        {
            return project.getTitle();
        }
        else if( node != null )
        {
            return node.getTitle();
        }
        else
        {
            return null;
        }
    }
}
