package com.galvin.markdown.compilers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "ExportFormat" )
@XmlAccessorType( XmlAccessType.FIELD )
public class ExportFormat
{
    public static final ExportFormat MARKDOWN = new ExportFormat( "Markdown (Pandoc)", ".md", 0 );
    public static final ExportFormat AS_IS = new ExportFormat( "As-is", ".asis.md", 0 );
    public static final ExportFormat XHTML = new ExportFormat( "XHTML", ".html", 20 );
    public static final ExportFormat WORDPRESS_HTML = new ExportFormat( "Wordpress HTML", ".wp.html", 100 );
    public static final ExportFormat HTML_SNIPPET = new ExportFormat( "HTML Snippet", ".htm", 10 );
    public static final ExportFormat EPUB = new ExportFormat( "ePub", ".epub", 50 );
    public static final ExportFormat MOBI = new ExportFormat( "Kindle (mobi)", ".mobi", 200 );
    public static final ExportFormat ODT = new ExportFormat( "Open Document Format", ".odt", 200 );
    public static final ExportFormat PDF = new ExportFormat( "Adobe PDF", ".pdf", 100 );
    public static final ExportFormat RTF = new ExportFormat( "Rich Text", ".rtf", 100 );
    public static final ExportFormat PLAIN_TEXT = new ExportFormat( "Plain Text", ".txt", 100 );
    public static final ExportFormat MICROSOFT_WORD = new ExportFormat( "Microsoft Word (.docx)", ".docx", 100 );
    public static final ExportFormat MICROSOFT_WORD_2003 = new ExportFormat( "Microsoft Word (.doc)", ".doc", 100 );
    public static final ExportFormat LATEX = new ExportFormat( "Latex", ".tex", 50 );
    public static final ExportFormat[] FORMATS = new ExportFormat[]
    {
        EPUB,
        MOBI,
        XHTML,
        HTML_SNIPPET,
        WORDPRESS_HTML,
        PDF,
        MICROSOFT_WORD_2003,
        MICROSOFT_WORD,
        ODT,
        RTF,
        PLAIN_TEXT,
        MARKDOWN,
        LATEX,
        AS_IS,
    };
    
    private String name;
    private String extension;
    private int priority = 100;

    public ExportFormat()
    {
    }

    public ExportFormat( String name, String extension, int priority )
    {
        this.name = name;
        this.extension = extension;
        this.priority = priority;
    }

    @Override
    public String toString()
    {
        return getName();
    }
    
    public String getExtension()
    {
        return extension;
    }

    public void setExtension( String extension )
    {
        this.extension = extension;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority( int priority )
    {
        this.priority = priority;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj == null )
        {
            return false;
        }
        if( getClass() != obj.getClass() )
        {
            return false;
        }
        final ExportFormat other = ( ExportFormat ) obj;
        if( ( this.name == null ) ? ( other.name != null ) : !this.name.equals( other.name ) )
        {
            return false;
        }
        if( ( this.extension == null ) ? ( other.extension != null ) : !this.extension.equals( other.extension ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 61 * hash + ( this.name != null ? this.name.hashCode() : 0 );
        hash = 61 * hash + ( this.extension != null ? this.extension.hashCode() : 0 );
        return hash;
    }
    
    public static class Comparator
    implements java.util.Comparator
    {

        public int compare( Object t, Object t1 )
        {
            if( t instanceof ExportFormat && t1 instanceof ExportFormat )
            {
                ExportFormat one = (ExportFormat)t;
                ExportFormat two = (ExportFormat)t1;
                
                return one.getPriority() - two.getPriority();
            }
            else
            {
                return 0;
            }
        }
    }
}
