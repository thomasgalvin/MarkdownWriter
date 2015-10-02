package com.galvin.markdown.search;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;

public class Hit {
    private String searchTerm;
    private String snippet;
    private Node node;
    private NodeSection section;
    private int startIndex;
    private int endIndex;

    public Hit() {
    }

    public Hit( String searchTerm, String snippet, Node node,
                NodeSection section, int startIndex,
                int endIndex ) {
        this.searchTerm = searchTerm;
        this.snippet = snippet;
        this.node = node;
        this.section = section;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    // getters and setters //
    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm( String searchTerm ) {
        this.searchTerm = searchTerm;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet( String snippet ) {
        this.snippet = snippet;
    }

    public Node getNode() {
        return node;
    }

    public void setNode( Node node ) {
        this.node = node;
    }

    public NodeSection getSection() {
        return section;
    }

    public void setSection( NodeSection section ) {
        this.section = section;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex( int startIndex ) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex( int endIndex ) {
        this.endIndex = endIndex;
    }

}
