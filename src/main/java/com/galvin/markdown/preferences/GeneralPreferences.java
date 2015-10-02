/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.preferences;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "GeneralPreferences" )
@XmlAccessorType( XmlAccessType.FIELD )
public class GeneralPreferences
{
    private String dateFormat = "dd MMMM yyyy";
    private String timeFormat = "h:mm a";
    private String datetimeFormat = "dd MMMM yyyy h:mm a";
    private int charactersPerWord = 6;
    private int wordsPerPage = 250;

    public String getDateFormat()
    {
        return dateFormat;
    }

    public void setDateFormat( String dateFormat )
    {
        this.dateFormat = dateFormat;
    }

    public String getDatetimeFormat()
    {
        return datetimeFormat;
    }

    public void setDatetime( String datetime )
    {
        this.datetimeFormat = datetime;
    }

    public String getTimeFormat()
    {
        return timeFormat;
    }

    public void setTimeFormat( String timeFormat )
    {
        this.timeFormat = timeFormat;
    }

    public int getCharactersPerWord()
    {
        return charactersPerWord;
    }

    public void setCharactersPerWord( int charactersPerWord )
    {
        this.charactersPerWord = charactersPerWord;
    }

    public int getWordsPerPage()
    {
        return wordsPerPage;
    }

    public void setWordsPerPage( int wordsPerPage )
    {
        this.wordsPerPage = wordsPerPage;
    }
    
}
