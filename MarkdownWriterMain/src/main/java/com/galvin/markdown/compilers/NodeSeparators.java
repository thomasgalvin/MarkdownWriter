package com.galvin.markdown.compilers;

public class NodeSeparators
{
    private String separatorSameLevel = Markup.SECTION_BREAK;
    private String separatorHigherToLower = Markup.PARAGRAPH_BREAK;
    private String separatorLowerToHigher = Markup.PARAGRAPH_BREAK;
    private String endOfDocumentMarker = "";
    
    public String getSeparatorSameLevel() {
        return separatorSameLevel;
    }

    public void setSeparatorSameLevel( String separatorSameLevel ) {
        this.separatorSameLevel = separatorSameLevel;
    }

    public String getSeparatorHigherToLower() {
        return separatorHigherToLower;
    }

    public void setSeparatorHigherToLower( String separatorHigherToLower ) {
        this.separatorHigherToLower = separatorHigherToLower;
    }

    public String getSeparatorLowerToHigher() {
        return separatorLowerToHigher;
    }

    public void setSeparatorLowerToHigher( String separatorLowerToHigher ) {
        this.separatorLowerToHigher = separatorLowerToHigher;
    }

    public String getEndOfDocumentMarker() {
        return endOfDocumentMarker;
    }

    public void setEndOfDocumentMarker( String endOfDocumentMarker ) {
        this.endOfDocumentMarker = endOfDocumentMarker;
    }
    
}
