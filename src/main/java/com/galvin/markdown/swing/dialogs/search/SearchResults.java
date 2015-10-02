/**
  Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
*/

package com.galvin.markdown.swing.dialogs.search;

import java.util.ArrayList;
import java.util.List;


public class SearchResults
{
    private String searchTerm;
    private boolean ignoreCase;
    private List<NodeSearchResults> results = new ArrayList();

    public SearchResults()
    {
    }

    public SearchResults( String searchTerm, boolean ignoreCase )
    {
        this.searchTerm = searchTerm;
        this.ignoreCase = ignoreCase;
    }

    public String getSearchTerm()
    {
        return searchTerm;
    }

    public void setSearchTerm( String searchTerm )
    {
        this.searchTerm = searchTerm;
    }

    public boolean ignoreCase()
    {
        return ignoreCase;
    }

    public void setIgnoreCase( boolean ignoreCase )
    {
        this.ignoreCase = ignoreCase;
    }

    public List<NodeSearchResults> getResults()
    {
        return results;
    }

    public void setResults( List<NodeSearchResults> results )
    {
        this.results = results;
    }
    
}
