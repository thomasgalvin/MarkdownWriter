/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.model;

import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import com.galvin.markdown.util.Utils;
import galvin.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "Node" )
@XmlAccessorType( XmlAccessType.FIELD )
public class Node {

    private String uuid = UUID.randomUUID().toString();
    private String nodeType;
    private String title;
    private String subtitle;
    private Calendar createdDate = new GregorianCalendar();
    private Calendar modifiedDate = new GregorianCalendar();
    private List<Contributor> contributors = new ArrayList();
    private List<String> keywords = new ArrayList();
    private List<Node> childNodes = new ArrayList();
    private String icon;
    private ImageResource imageResource;
    private int cursorStart;
    private int cursorEnd;
    private boolean expanded = true;
    private transient int level;
    private transient Project project;
    private transient File projectDirectory;
    private transient MarkdownDocument manuscript;
    private transient MarkdownDocument description;
    private transient MarkdownDocument summary;
    private transient MarkdownDocument notes;
    private String manuscriptText;
    private String descriptionText;
    private String summaryText;
    private String notesText;

    public boolean needsSaving() {
        boolean manuscriptNeedsSaving = manuscript != null && manuscript.needsSaving();
        boolean descriptionNeedsSaving = description != null && description.needsSaving();
        boolean summaryNeedsSaving = summary != null && summary.needsSaving();
        boolean notesNeedsSaving = notes != null && notes.needsSaving();
        boolean imageResourceNeedsSaving = imageResource != null && imageResource.needsSaving();

        return manuscriptNeedsSaving || descriptionNeedsSaving || summaryNeedsSaving || notesNeedsSaving || imageResourceNeedsSaving;
    }

    public void setNeedsSaving( boolean needsSaving ) {
        if( manuscript != null ) {
            manuscript.setNeedsSaving( needsSaving );
        }

        if( description != null ) {
            description.setNeedsSaving( needsSaving );
        }

        if( summary != null ) {
            summary.setNeedsSaving( needsSaving );
        }

        if( notes != null ) {
            notes.setNeedsSaving( needsSaving );
        }

        if( imageResource != null ) {
            imageResource.setNeedsSaving( needsSaving );
        }
    }

    public void prepareToSave() {
        if( manuscript != null ) {
            manuscriptText = manuscript.getText();
            if( manuscript.getNeedsSaving() ) {
                setModifiedDate( new GregorianCalendar() );
                manuscript.setNeedsSaving( false );
            }
        }
        else {
            manuscriptText = null;
        }

        if( description != null ) {
            descriptionText = description.getText();
            if( description.getNeedsSaving() ) {
                setModifiedDate( new GregorianCalendar() );
                description.setNeedsSaving( false );
            }
        }
        else {
            descriptionText = null;
        }

        if( summary != null ) {
            summaryText = summary.getText();

            if( summary.getNeedsSaving() ) {
                setModifiedDate( new GregorianCalendar() );
                summary.setNeedsSaving( false );
            }
        }
        else {
            summaryText = null;
        }

        if( notes != null ) {
            notesText = notes.getText();

            if( notes.getNeedsSaving() ) {
                setModifiedDate( new GregorianCalendar() );
                notes.setNeedsSaving( false );
            }
        }
        else {
            notesText = null;
        }
    }

    public void loadAllDocuments() {
        setManuscriptText( getManuscriptText() );
        setDescriptionText( getDescriptionText() );
        setSummaryText( getSummaryText() );
        setNotesText( getNotesText() );

        getManuscript().setNeedsSaving( false );
        getDescription().setNeedsSaving( false );
        getSummary().setNeedsSaving( false );
        getNotes().setNeedsSaving( false );

        if( imageResource != null && imageResource.getBytes() != null ) {
            imageResource.setImageIcon( new ImageIcon( imageResource.getBytes() ) );
        }
        else if( imageResource != null ) {
            imageResource.setImageIcon( null );
        }
    }

    public List<Node> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes( List<Node> childNodes ) {
        this.childNodes = childNodes;
    }

    public List<Contributor> getContributors() {
        return contributors;
    }

    public void setContributors( List<Contributor> contributors ) {
        this.contributors = contributors;
    }

    public Calendar getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate( Calendar createdDate ) {
        this.createdDate = createdDate;
    }

    public int getCursorEnd() {
        return cursorEnd;
    }

    public void setCursorEnd( int cursorEnd ) {
        this.cursorEnd = cursorEnd;
    }

    public int getCursorStart() {
        return cursorStart;
    }

    public void setCursorStart( int cursorStart ) {
        this.cursorStart = cursorStart;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon( String icon ) {
        this.icon = icon;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords( List<String> keywords ) {
        this.keywords = keywords;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel( int level ) {
        this.level = level;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public void setProjectDirectory( File projectDirectory ) {
        this.projectDirectory = projectDirectory;
    }

    public MarkdownDocument getManuscriptIfNotNull() {
        return manuscript;
    }

    public MarkdownDocument getManuscript() {
        if( manuscript == null ) {
            manuscript = new MarkdownDocument( this );
        }

        return manuscript;
    }

    public void setManuscript( MarkdownDocument manuscript ) {
        this.manuscript = manuscript;
    }

    public MarkdownDocument getDescriptionIfNotNull() {
        return description;
    }

    public MarkdownDocument getDescription() {
        if( description == null ) {
            description = new MarkdownDocument( this );
        }

        return description;
    }

    public void setDescription( MarkdownDocument description ) {
        this.description = description;
    }

    public MarkdownDocument getNotesIfNotNull() {
        return notes;
    }

    public MarkdownDocument getNotes() {
        if( notes == null ) {
            notes = new MarkdownDocument( this );
        }

        return notes;
    }

    public void setNotes( MarkdownDocument notes ) {
        this.notes = notes;
    }

    public MarkdownDocument getSummaryIfNotNull() {
        return summary;
    }

    public MarkdownDocument getSummary() {
        if( summary == null ) {
            summary = new MarkdownDocument( this );
        }

        return summary;
    }

    public void setSummary( MarkdownDocument summary ) {
        this.summary = summary;
    }

    public ImageResource getImageResource() {
        if( imageResource == null ) {
            imageResource = new ImageResource();
        }

        return imageResource;
    }

    public void setImageResource( ImageResource imageResource ) {
        this.imageResource = imageResource;
    }

    public Calendar getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate( Calendar modifiedDate ) {
        this.modifiedDate = modifiedDate;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType( String nodeType ) {
        this.nodeType = nodeType;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle( String subtitle ) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid( String uuid ) {
        this.uuid = uuid;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded( boolean expanded ) {
        this.expanded = expanded;
    }

    public Project getProject() {
        return project;
    }

    public void setProject( Project project ) {
        this.project = project;
    }

    public String getManuscriptText() {
        return manuscriptText;
    }

    public void setManuscriptText( String manuscriptText ) {
        this.manuscriptText = manuscriptText;
        getManuscript().setTextAndClearUndo( manuscriptText );
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText( String descriptionText ) {
        this.descriptionText = descriptionText;
        getDescription().setTextAndClearUndo( descriptionText );
    }

    public String getSummaryText() {
        return summaryText;
    }

    public void setSummaryText( String summaryText ) {
        this.summaryText = summaryText;
        getSummary().setTextAndClearUndo( summaryText );
    }

    public String getNotesText() {
        return notesText;
    }

    public void setNotesText( String notesText ) {
        this.notesText = notesText;
        getNotes().setTextAndClearUndo( notesText );
    }

    @Override
    public String toString() {
        return getTitle();
    }

    @Override
    public boolean equals( Object obj ) {
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final Node other = (Node)obj;
        if( ( this.uuid == null ) ? ( other.uuid != null ) : !this.uuid.equals( other.uuid ) ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + ( this.uuid != null ? this.uuid.hashCode() : 0 );
        return hash;
    }
}
