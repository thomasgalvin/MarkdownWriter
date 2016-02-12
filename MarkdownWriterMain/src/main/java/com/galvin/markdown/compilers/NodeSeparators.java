package com.galvin.markdown.compilers;

public class NodeSeparators
{
    private String separatorSameLevel = Markup.PARAGRAPH_BREAK;
    private String separatorHigherToLower = Markup.PARAGRAPH_BREAK;
    private String separatorLowerToHigher = Markup.PARAGRAPH_BREAK;
    private String endOfDocumentMarker = "";
    
    private String customSameLevel;
    private String customHigherToLower;
    private String customLowerToHigher;
    private String customEndOfDocument;
    
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

    public String getCustomSameLevel() {
        return customSameLevel;
    }

    public void setCustomSameLevel( String customSameLevel ) {
        this.customSameLevel = customSameLevel;
    }

    public String getCustomHigherToLower() {
        return customHigherToLower;
    }

    public void setCustomHigherToLower( String customHigherToLower ) {
        this.customHigherToLower = customHigherToLower;
    }

    public String getCustomLowerToHigher() {
        return customLowerToHigher;
    }

    public void setCustomLowerToHigher( String customLowerToHigher ) {
        this.customLowerToHigher = customLowerToHigher;
    }

    public String getCustomEndOfDocument() {
        return customEndOfDocument;
    }

    public void setCustomEndOfDocument( String customEndOfDocument ) {
        this.customEndOfDocument = customEndOfDocument;
    }
    
}
