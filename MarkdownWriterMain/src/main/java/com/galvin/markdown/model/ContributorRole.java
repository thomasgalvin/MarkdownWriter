/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "ContributorRole" )
@XmlAccessorType( XmlAccessType.FIELD )
public class ContributorRole
{
    //via http://www.loc.gov/marc/relators/relaterm.html

    public static final ContributorRole AUTHOR = new ContributorRole( "Author", "aut" );
    public static final ContributorRole ADAPTER = new ContributorRole( "Adapter", "adp" );
    public static final ContributorRole AUTHOR_IN_QUOTATIONS = new ContributorRole( "Author in Quotations", "aqt" );
    public static final ContributorRole AUTHOR_OF_AFTERWAWRD = new ContributorRole( "Author of Afterward", "aft" );
    public static final ContributorRole AUTHOR_OF_INTRODUCTION = new ContributorRole( "Author of Introduction", "aui" );
    public static final ContributorRole COPYRIGHT_HOLDER = new ContributorRole( "Copyright Holder", "cph" );
    public static final ContributorRole COVER_DESIGNER = new ContributorRole( "Cover Designer", "cov" );
    public static final ContributorRole DESIGNER = new ContributorRole( "Designer", "dsr" );
    public static final ContributorRole DISTRIBUTOR = new ContributorRole( "Distributor", "dst" );
    public static final ContributorRole EDITOR = new ContributorRole( "Editor", "edt" );
    public static final ContributorRole INTERVIEWEE = new ContributorRole( "Interviewee", "ive" );
    public static final ContributorRole INTERVIEWER = new ContributorRole( "Interviewer", "ivr" );
    public static final ContributorRole LYRICIST = new ContributorRole( "Lyricist", "lyr" );
    public static final ContributorRole OTHER = new ContributorRole( "Other", "otr" );
    public static final ContributorRole PHOTOGRAPHER = new ContributorRole( "Photographer", "pht" );
    public static final ContributorRole PROOFREADER = new ContributorRole( "Proofreader", "pfr" );
    public static final ContributorRole PUBLISHER = new ContributorRole( "Publisher", "pbl" );
    public static final ContributorRole REPORTER = new ContributorRole( "Reporter", "rpt" );
    public static final ContributorRole RESEARCH_TEAM_HEAD = new ContributorRole( "Reasearch Team Head", "rth" );
    public static final ContributorRole RESEARCH_TEAM_MEMBER = new ContributorRole( "Reasearch Team Member", "rtm" );
    public static final ContributorRole REVIEWER = new ContributorRole( "Reviewer", "rev" );
    public static final ContributorRole SCIENTIFIC_ADVISOR = new ContributorRole( "Scientific Advisor", "sad" );
    public static final ContributorRole STANDARDS_BODY = new ContributorRole( "Standards Body", "stn" );
    public static final ContributorRole THESIS_ADVISOR = new ContributorRole( "Thesis Advisor", "ths" );
    public static final ContributorRole TRANSCRIBER = new ContributorRole( "Transcriber", "trc" );
    public static final ContributorRole TRANSLATOR = new ContributorRole( "Translator", "trl" );
    public static final ContributorRole WRITER_OF_ACCOMPANYING_MATERIAL = new ContributorRole( "Writer of Accompanying Material", "wam" );
    public static final ContributorRole[] ALL_ROLES = new ContributorRole[]
    {
        AUTHOR,
        ADAPTER,
        AUTHOR_IN_QUOTATIONS,
        AUTHOR_OF_AFTERWAWRD,
        AUTHOR_OF_INTRODUCTION,
        COPYRIGHT_HOLDER,
        COVER_DESIGNER,
        DESIGNER,
        DISTRIBUTOR,
        EDITOR,
        INTERVIEWEE,
        INTERVIEWER,
        LYRICIST,
        OTHER,
        PHOTOGRAPHER,
        PROOFREADER,
        PUBLISHER,
        REPORTER,
        RESEARCH_TEAM_HEAD,
        RESEARCH_TEAM_MEMBER,
        REVIEWER,
        SCIENTIFIC_ADVISOR,
        STANDARDS_BODY,
        THESIS_ADVISOR,
        TRANSCRIBER,
        TRANSLATOR,
        WRITER_OF_ACCOMPANYING_MATERIAL,
    };
    private String display;
    private String abbreviation;

    public ContributorRole()
    {
    }

    public ContributorRole( String display, String abbreviation )
    {
        this.display = display;
        this.abbreviation = abbreviation;
    }

    public String getAbbreviation()
    {
        return abbreviation;
    }

    public String getDisplay()
    {
        return display;
    }

    @Override
    public String toString()
    {
        return getDisplay();
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
        final ContributorRole other = (ContributorRole) obj;
        if( ( this.display == null ) ? ( other.display != null ) : !this.display.equals( other.display ) )
        {
            return false;
        }
        if( ( this.abbreviation == null ) ? ( other.abbreviation != null ) : !this.abbreviation.equals( other.abbreviation ) )
        {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 17 * hash + ( this.display != null ? this.display.hashCode() : 0 );
        hash = 17 * hash + ( this.abbreviation != null ? this.abbreviation.hashCode() : 0 );
        return hash;
    }
}
