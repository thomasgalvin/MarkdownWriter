/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.preferences;

import java.awt.Color;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "EditorPreferences" )
@XmlAccessorType( XmlAccessType.FIELD )
public class EditorPreferences
{
    private boolean showLineNumbers = true;
    private boolean highlightLines = true;
    private boolean liveSpellCheck = true;
    private boolean lineWrap = true;
    private boolean showInvisibles = false;
    private boolean softTabs = true;
    private int spacesPerTab = 4; 
    //private transient Color backgroundColor = Color.DARK_GRAY;
    private transient Color backgroundColor = new Color(41, 49, 52);
    private transient Color textColor = Color.CYAN;
    
    public boolean highlightLines()
    {
        return highlightLines;
    }

    public void setHighlightLines( boolean highlightLines )
    {
        this.highlightLines = highlightLines;
    }

    public boolean liveSpellCheck()
    {
        return liveSpellCheck;
    }

    public void setLiveSpellCheck( boolean liveSpellCheck )
    {
        this.liveSpellCheck = liveSpellCheck;
    }

    public boolean showInvisibles()
    {
        return showInvisibles;
    }

    public void setShowInvisibles( boolean showInvisibles )
    {
        this.showInvisibles = showInvisibles;
    }

    public boolean showLineNumbers()
    {
        return showLineNumbers;
    }

    public void setShowLineNumbers( boolean showLineNumbers )
    {
        this.showLineNumbers = showLineNumbers;
    }

    public boolean lineWrap()
    {
        return lineWrap;
    }

    public void setLineWrap( boolean lineWrap )
    {
        this.lineWrap = lineWrap;
    }

    public boolean softTabs()
    {
        return softTabs;
    }

    public void setSoftTabs( boolean softTabs )
    {
        this.softTabs = softTabs;
    }

    public int getSpacesPerTab()
    {
        return spacesPerTab;
    }

    public void setSpacesPerTab( int spacesPerTab )
    {
        this.spacesPerTab = spacesPerTab;
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public Color getTextColor()
    {
        return textColor;
    }
}
