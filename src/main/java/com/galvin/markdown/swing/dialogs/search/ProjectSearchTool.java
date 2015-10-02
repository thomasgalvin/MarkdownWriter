/**
  Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
*/

package com.galvin.markdown.swing.dialogs.search;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.Project;
import java.util.ArrayList;
import java.util.List;


public class ProjectSearchTool
{
    private Project project;
    private boolean manuscriptOnly;
    private boolean manuscripts = true;
    private boolean descriptions;
    private boolean summaries;
    private boolean notes;

    public ProjectSearchTool( Project project )
    {
        this.project = project;
    }

    public List<SearchResults> search()
    {
        List<SearchResults> result = new ArrayList();
        
        List<Node> nodes = new ArrayList();
        
        for( Node childNode : project.getChildNodes() )
        {
            if( manuscriptOnly && childNode.getUuid().equals( project.getManuscriptUuid() ))
            {
                addNodes( childNode, nodes );
                break;
            }
            else
            {
                addNodes( childNode, nodes );
            }
        }
        
        return result;
    }
    
    private void addNodes( Node node, List<Node> nodes )
    {
        if( !nodes.contains( node ) )
        {
            String nodeUuid = node.getUuid();
            if( !nodeUuid.equals( project.getManuscriptUuid() )
                && !nodeUuid.equals( project.getTrashUuid() )
                && !nodeUuid.equals( project.getResourcesUuid() ) )
            {
                nodes.add( node );
            }
        }
        
        for( Node childNode : node.getChildNodes() )
        {
            addNodes( childNode, nodes );
        }
    }
    
    public static class SearchResults
    {
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject( Project project )
    {
        this.project = project;
    }

    public boolean isManuscriptOnly()
    {
        return manuscriptOnly;
    }

    public void setManuscriptOnly( boolean manuscriptOnly )
    {
        this.manuscriptOnly = manuscriptOnly;
    }

    public boolean isManuscripts()
    {
        return manuscripts;
    }

    public void setManuscripts( boolean manuscripts )
    {
        this.manuscripts = manuscripts;
    }

    public boolean isDescriptions()
    {
        return descriptions;
    }

    public void setDescriptions( boolean descriptions )
    {
        this.descriptions = descriptions;
    }

    public boolean isSummaries()
    {
        return summaries;
    }

    public void setSummaries( boolean summaries )
    {
        this.summaries = summaries;
    }

    public boolean isNotes()
    {
        return notes;
    }

    public void setNotes( boolean notes )
    {
        this.notes = notes;
    }
    
}

