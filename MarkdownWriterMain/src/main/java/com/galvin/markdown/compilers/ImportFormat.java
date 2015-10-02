package com.galvin.markdown.compilers;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement( name = "ImportFormat" )
@XmlAccessorType( XmlAccessType.FIELD )
public class ImportFormat
{
    public static final ImportFormat MARKDOWN = new ImportFormat( "Markdown (Pandoc)", ".md" );
    public static final ImportFormat MARKDOWN_STRICT = new ImportFormat( "Markdown (Strict)", ".md" );
    public static final ImportFormat MARKDOWN_PHP_EXTRA = new ImportFormat( "Markdown (PHP Extra)", ".md" );
    public static final ImportFormat MARKDOWN_GITHUB = new ImportFormat( "Markdown (GitHub)", ".md" );
    public static final ImportFormat MARKDOWN_MMD = new ImportFormat( "Markdown (MultiMarkdown)", ".md" );
    public static final ImportFormat HTML = new ImportFormat( "HTML", ".html" );
    public static final ImportFormat LATEX = new ImportFormat( "LaTeX", ".tex" );
    public static final ImportFormat FOUNTAIN = new ImportFormat( "Fountain", ".fountain" );
    public static final ImportFormat[] FORMATS = new ImportFormat[]
    {
        MARKDOWN,
        MARKDOWN_STRICT,
        MARKDOWN_PHP_EXTRA,
        MARKDOWN_GITHUB,
        MARKDOWN_MMD,
        HTML,
        LATEX,
        FOUNTAIN,
    };
    
    private String name;
    private String extension;
    
    public ImportFormat()
    {
    }

    public ImportFormat( String name, String extension )
    {
        this.name = name;
        this.extension = extension;
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
        final ImportFormat other = ( ImportFormat ) obj;
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
        hash = 67 * hash + ( this.name != null ? this.name.hashCode() : 0 );
        hash = 67 * hash + ( this.extension != null ? this.extension.hashCode() : 0 );
        return hash;
    }
}
