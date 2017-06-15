/**
  Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
*/

package com.galvin.markdown.swing.dialogs.search;

import com.galvin.markdown.model.Node;
import java.util.ArrayList;
import java.util.List;


public class NodeSearchResults
{
    private Node node;
    private List<SearchResult> manuscriptResults = new ArrayList();
    private List<SearchResult> descriptionResults = new ArrayList();
    private List<SearchResult> summaryResults = new ArrayList();
    private List<SearchResult> notesResults = new ArrayList();

    public NodeSearchResults( Node node )
    {
        this.node = node;
    }

    public boolean hasHits(){
        return !manuscriptResults.isEmpty() ||
               !descriptionResults.isEmpty() ||
               !summaryResults.isEmpty() ||
               !notesResults.isEmpty();
    }
    
    public Node getNode()
    {
        return node;
    }

    public void setNode( Node node )
    {
        this.node = node;
    }

    public List<SearchResult> getManuscriptResults()
    {
        return manuscriptResults;
    }

    public void setManuscriptResults( List<SearchResult> manuscriptResults )
    {
        this.manuscriptResults = manuscriptResults;
    }

    public List<SearchResult> getDescriptionResults()
    {
        return descriptionResults;
    }

    public void setDescriptionResults( List<SearchResult> descriptionResults )
    {
        this.descriptionResults = descriptionResults;
    }

    public List<SearchResult> getSummaryResults()
    {
        return summaryResults;
    }

    public void setSummaryResults( List<SearchResult> summaryResults )
    {
        this.summaryResults = summaryResults;
    }

    public List<SearchResult> getNotesResults()
    {
        return notesResults;
    }

    public void setNotesResults( List<SearchResult> notesResults )
    {
        this.notesResults = notesResults;
    }
    
}
