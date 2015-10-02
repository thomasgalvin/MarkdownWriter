/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "Contributor" )
@XmlAccessorType( XmlAccessType.FIELD )
public class Contributor
{
    public static final ContributorRole DEFAULT_ROLE = ContributorRole.AUTHOR;
    private String name;
    private String sortByName;
    private ContributorRole role;

    public Contributor()
    {}
    
    public Contributor( String name, String sortByName )
    {
        this( name, sortByName, DEFAULT_ROLE );
    }
    
    public Contributor( String name, String sortByName, ContributorRole role )
    {
        this.name = name;
        this.sortByName = sortByName;
        this.role = role;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public ContributorRole getRole()
    {
        return role;
    }

    public void setRole( ContributorRole role )
    {
        this.role = role;
    }

    public String getSortByName()
    {
        return sortByName;
    }

    public void setSortByName( String sortByName )
    {
        this.sortByName = sortByName;
    }

}
