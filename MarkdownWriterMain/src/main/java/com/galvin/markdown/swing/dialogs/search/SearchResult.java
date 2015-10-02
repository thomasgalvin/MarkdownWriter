/**
  Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
*/

package com.galvin.markdown.swing.dialogs.search;

import com.galvin.markdown.swing.editor.MarkdownDocument;


public class SearchResult
{
    private MarkdownDocument document;
    private String searchTerm;
    private String snippet;
    private int location;

    public SearchResult()
    {
    }

    public SearchResult( MarkdownDocument document, String searchTerm, String snippet, int location )
    {
        this.document = document;
        this.searchTerm = searchTerm;
        this.snippet = snippet;
        this.location = location;
    }

    public MarkdownDocument getDocument()
    {
        return document;
    }

    public void setDocument( MarkdownDocument document )
    {
        this.document = document;
    }

    public String getSearchTerm()
    {
        return searchTerm;
    }

    public void setSearchTerm( String searchTerm )
    {
        this.searchTerm = searchTerm;
    }

    public String getSnippet()
    {
        return snippet;
    }

    public void setSnippet( String snippet )
    {
        this.snippet = snippet;
    }

    public int getLocation()
    {
        return location;
    }

    public void setLocation( int location )
    {
        this.location = location;
    }
}
