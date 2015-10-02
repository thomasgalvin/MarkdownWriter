package com.galvin.markdown.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "IdentifierScheme" )
@XmlAccessorType( XmlAccessType.FIELD )
public class IdentifierScheme
{
    public static final IdentifierScheme ISBN = new IdentifierScheme( "ISBN", "ISBN" );
    public static final IdentifierScheme UUID = new IdentifierScheme( "UUID", "UUID" );
    public static final IdentifierScheme URI = new IdentifierScheme( "URI", "URI" );
    public static final IdentifierScheme CUSTOM = new IdentifierScheme( "Custom", "custom" );
    
    public String name;
    public String code;
    
    public IdentifierScheme()
    {
    }

    public IdentifierScheme( String name, String code )
    {
        this.name = name;
        this.code = code;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
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
    public String toString()
    {
        return getName();
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
        final IdentifierScheme other = (IdentifierScheme) obj;
        if( ( this.name == null ) ? ( other.name != null ) : !this.name.equals( other.name ) )
        {
            return false;
        }
        if( ( this.code == null ) ? ( other.code != null ) : !this.code.equals( other.code ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 13 * hash + ( this.name != null ? this.name.hashCode() : 0 );
        hash = 13 * hash + ( this.code != null ? this.code.hashCode() : 0 );
        return hash;
    }
}
