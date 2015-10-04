/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.model;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.util.Utils;
import galvin.StringUtils;
import galvin.SystemUtils;
import galvin.dc.LanguageCode;
import galvin.swing.spell.SpellDictionaryUser;
import galvin.swing.spell.SpellUtils;
import galvin.swing.text.DocumentUtils;
import galvin.swing.text.macros.MacroList;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

@XmlRootElement(name = "Project")
@XmlAccessorType(XmlAccessType.FIELD)
public class Project
{

    private String projectModelVersion;
    private transient boolean unsupportedLegacyVersion;
    private String uuid = UUID.randomUUID().toString();
    private String title;
    private String subtitle;
    private LanguageCode langauge = LanguageCode.ENGLISH;
    private IdentifierScheme identifierScheme = IdentifierScheme.ISBN;
    private String identifier = UUID.randomUUID().toString();
    private Calendar createdDate = new GregorianCalendar();
    private Calendar modifiedDate = new GregorianCalendar();
    private List<Contributor> contributors = new ArrayList();
    private List<String> genres = new ArrayList();
    private List<String> topics = new ArrayList();
    private List<String> keywords = new ArrayList();
    private Node cover;
    private String styleSheet = "";
    private String projectDictionaryText = "";
    private String manuscriptUuid;
    private String resourcesUuid;
    private String trashUuid;
    private List<Node> childNodes = new ArrayList();
    private CompileOptions compileOptions = new CompileOptions();
    private MacroList projectMacros = new MacroList();
    private boolean synchronizeEditors = false;
    private String selectedNode = null;
    private transient File projectDirectory;
    private transient File projectFile;
    private transient File projectDictionaryFile = SystemUtils.getTempFile( ProjectIo.PROJECT_DICTIONARY_DOCUMENT );
    private transient RSyntaxDocument styleSheetDocument = new RSyntaxDocument( RSyntaxTextArea.SYNTAX_STYLE_CSS );

    public Project()
    {
        compileOptions.setProject( this );

        try
        {
            if( !projectDictionaryFile.exists() )
            {
                projectDictionaryFile.deleteOnExit();
                FileUtils.writeStringToFile( projectDictionaryFile, projectDictionaryText );
            }
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }

        try
        {
            styleSheet = ProjectIo.getDefaultStyleSheet();
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    public List<Contributor> getContributors()
    {
        return contributors;
    }

    public void setContributors( List<Contributor> contributors )
    {
        this.contributors = contributors;
    }

    public Calendar getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate( Calendar createdDate )
    {
        this.createdDate = createdDate;
    }

    public Node getCover()
    {
        return cover;
    }

    public void setCover( Node cover )
    {
        this.cover = cover;
    }

    public List<String> getGenres()
    {
        return genres;
    }

    public void setGenres( List<String> genres )
    {
        this.genres = genres;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier( String identifier )
    {
        this.identifier = identifier;
    }

    public IdentifierScheme getIdentifierScheme()
    {
        return identifierScheme;
    }

    public void setIdentifierScheme( IdentifierScheme identifierScheme )
    {
        this.identifierScheme = identifierScheme;
    }

    public List<String> getKeywords()
    {
        return keywords;
    }

    public void setKeywords( List<String> keywords )
    {
        this.keywords = keywords;
    }

    public LanguageCode getLangauge()
    {
        return langauge;
    }

    public void setLangauge( LanguageCode langauge )
    {
        this.langauge = langauge;
    }

    public String getManuscriptUuid()
    {
        return manuscriptUuid;
    }

    public void setManuscriptUuid( String manuscriptUuid )
    {
        this.manuscriptUuid = manuscriptUuid;
    }

    public Calendar getModifiedDate()
    {
        return modifiedDate;
    }

    public void setModifiedDate( Calendar modifiedDate )
    {
        this.modifiedDate = modifiedDate;
    }

    public List<Node> getChildNodes()
    {
        return childNodes;
    }

    public void setChildNodes( List<Node> nodes )
    {
        this.childNodes = nodes;
    }

    public File getProjectDirectory()
    {
        return projectDirectory;
    }

    public void setProjectDirectory( File projectDirectory )
    {
        this.projectDirectory = projectDirectory;
    }

    public File getProjectFile()
    {
        return projectFile;
    }

    public void setProjectFile( File projectFile )
    {
        this.projectFile = projectFile;
    }

    public String getResourcesUuid()
    {
        return resourcesUuid;
    }

    public void setResourcesUuid( String resourcesUuid )
    {
        this.resourcesUuid = resourcesUuid;
    }

    public String getStyleSheet()
    {
        return styleSheet;
    }

    public void setStyleSheet( String styleSheet )
    {
        //System.out.println( "Adding style sheet to document:\n" + styleSheet );
        this.styleSheet = styleSheet;
        DocumentUtils.setText( styleSheetDocument, styleSheet );
    }

    public RSyntaxDocument getStyleSheetDocument()
    {
        return styleSheetDocument;
    }

    public void setStyleSheetDocument( RSyntaxDocument styleSheetDocument )
    {
        this.styleSheetDocument = styleSheetDocument;
    }

    public void setStyleSheetFromDocument()
    {
        styleSheet = DocumentUtils.getText( styleSheetDocument );
    }

    public void readStyleSheetIntoDocument()
    {
        DocumentUtils.setText( styleSheetDocument, styleSheet );
    }

    public String getProjectDictionaryText()
    {
        return projectDictionaryText;
    }

    public void setProjectDictionaryText( String projectDictionaryText )
    {
        this.projectDictionaryText = projectDictionaryText;
    }

    public File getProjectDictionaryFile()
    {
        return projectDictionaryFile;
    }

    public void setProjectDictionaryFile( File projectDictionaryFile )
    {
        this.projectDictionaryFile = projectDictionaryFile;
    }

    public String getSubtitle()
    {
        return subtitle;
    }

    public void setSubtitle( String subtitle )
    {
        this.subtitle = subtitle;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public List<String> getTopics()
    {
        return topics;
    }

    public void setTopics( List<String> topics )
    {
        this.topics = topics;
    }

    public String getTrashUuid()
    {
        return trashUuid;
    }

    public void setTrashUuid( String trashUuid )
    {
        this.trashUuid = trashUuid;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void resetUuid()
    {
        setUuid( UUID.randomUUID().toString() );
    }

    public void setUuid( String uuid )
    {
        this.uuid = uuid;
    }

    public CompileOptions getCompileOptions()
    {
        if( compileOptions != null )
        {
            compileOptions.setProject( this );
        }

        return compileOptions;
    }

    public void setCompileOptions( CompileOptions compileOptions )
    {
        this.compileOptions = compileOptions;
        compileOptions.setProject( this );
    }

    public MacroList getProjectMacros()
    {
        return projectMacros;
    }

    public void setProjectMacros( MacroList projectMacros )
    {
        this.projectMacros = projectMacros;
    }

    public Node getManuscript()
    {
        return getNode( manuscriptUuid );
    }

    public Node getTrash()
    {
        return getNode( trashUuid );
    }

    public Node getResources()
    {
        return getNode( resourcesUuid );
    }

    public boolean synchronizeEditors()
    {
        return synchronizeEditors;
    }

    public void setSynchronizeEditors( boolean synchronizeEditors )
    {
        this.synchronizeEditors = synchronizeEditors;
    }

    public String getProjectModelVersion()
    {
        return projectModelVersion;
    }

    public void setProjectModelVersion( String projectModelVersion )
    {
        this.projectModelVersion = projectModelVersion;
    }

    public boolean isUnsupportedLegacyVersion()
    {
        return unsupportedLegacyVersion;
    }

    public void setUnsupportedLegacyVersion( boolean unsupportedLegacyVersion )
    {
        this.unsupportedLegacyVersion = unsupportedLegacyVersion;
    }

    private Node getNode( String uuid )
    {
        for(Node node : getChildNodes())
        {
            Node result = getNode( node, uuid );
            if( result != null )
            {
                return result;
            }
        }

        return null;
    }

    private Node getNode( Node node, String uuid )
    {
        if( uuid.equals( node.getUuid() ) )
        {
            return node;
        }

        for(Node child : node.getChildNodes())
        {
            Node result = getNode( child, uuid );
            if( result != null )
            {
                return result;
            }
        }

        return null;
    }

//    @Override
//    public String toString()
//    {
//        return getTitle();
//    }
    public String getMetadata()
    {
        MarkdownMessages messages = MarkdownServer.getMessages();
        StringBuilder result = new StringBuilder();

        result.append( messages.metadataWidgetTitle() );
        result.append( " " );
        result.append( getTitle() );
        result.append( "\n" );

        result.append( messages.metadataWidgetSubtitle() );
        result.append( " " );
        result.append( getSubtitle() );
        result.append( "\n" );

        result.append( messages.metadataWidgetContributors() );
        result.append( "\n" );
        for(Contributor contributor : getContributors())
        {
            result.append( contributor.getName() );
            result.append( " (" );
            result.append( contributor.getSortByName() );
            result.append( "), " );
            result.append( contributor.getRole() );
            result.append( "\n" );
        }
        result.append( "\n" );

        result.append( messages.metadataWidgetCreatedDate() );
        result.append( ": " );
        result.append( Utils.DATE_FORMAT.format( getModifiedDate().getTime() ) );
        result.append( "\n" );

        result.append( messages.metadataWidgetModifiedDate() );
        result.append( " " );
        result.append( Utils.DATE_FORMAT.format( getModifiedDate().getTime() ) );
        result.append( "\n" );

        result.append( messages.metadataWidgetLanguage() );
        result.append( " " );
        result.append( getLangauge() );
        result.append( "\n" );

        result.append( getIdentifierScheme() );
        result.append( ": " );
        result.append( getIdentifier() );
        result.append( "\n" );
        result.append( "\n" );

        result.append( messages.metadataWidgetKeywords() );
        result.append( "\n" );
        result.append( StringUtils.cat( getKeywords() ) );
        result.append( "\n" );
        result.append( "\n" );

        result.append( messages.metadataWidgetGenres() );
        result.append( "\n" );
        result.append( StringUtils.cat( getGenres() ) );
        result.append( "\n" );
        result.append( "\n" );

        result.append( messages.metadataWidgetTopics() );
        result.append( "\n" );
        result.append( StringUtils.cat( getTopics() ) );

        return result.toString();
    }

    public void setParent()
    {
        for(Node node : childNodes)
        {
            setParent( node );
        }
    }

    public void setParent( Node node )
    {
        node.setProject( this );

        for(Node child : node.getChildNodes())
        {
            setParent( child );
        }
    }

    private transient SpellDictionaryUser projectDictionary;

    public SpellDictionaryUser getProjectDictionary()
        throws IOException
    {
        if( projectDictionary == null )
        {
            projectDictionary = SpellUtils.loadDictionary( projectDictionaryFile );
        }

        return projectDictionary;
    }

    public void stopSpellCheck()
    {
        for(Node node : getChildNodes())
        {
            stopSpellCheck( node );
        }
    }

    public void stopSpellCheck( Node node )
    {
        if( node.getManuscriptIfNotNull() != null )
        {
            node.getManuscriptIfNotNull().stopSpellCheck();
        }

        if( node.getNotesIfNotNull() != null )
        {
            node.getNotesIfNotNull().stopSpellCheck();
        }

        if( node.getSummaryIfNotNull() != null )
        {
            node.getSummaryIfNotNull().stopSpellCheck();
        }

        if( node.getDescriptionIfNotNull() != null )
        {
            node.getDescriptionIfNotNull().stopSpellCheck();
        }

        for(Node child : node.getChildNodes())
        {
            stopSpellCheck( child );
        }
    }

    public void startSpellCheck()
    {
        for(Node node : getChildNodes())
        {
            startSpellCheck( node );
        }
    }

    public void startSpellCheck( Node node )
    {
        if( node.getManuscriptIfNotNull() != null )
        {
            node.getManuscriptIfNotNull().startSpellCheck();
        }

        if( node.getNotesIfNotNull() != null )
        {
            node.getNotesIfNotNull().startSpellCheck();
        }

        if( node.getSummaryIfNotNull() != null )
        {
            node.getSummaryIfNotNull().startSpellCheck();
        }

        if( node.getDescriptionIfNotNull() != null )
        {
            node.getDescriptionIfNotNull().startSpellCheck();
        }

        for(Node child : node.getChildNodes())
        {
            startSpellCheck( child );
        }
    }

    public void setNeedsSaving( boolean needsSaving )
    {
        for(Node node : getChildNodes())
        {
            setNeedsSaving( node, needsSaving );
        }
    }

    private void setNeedsSaving( Node node, boolean needsSaving )
    {
        node.setNeedsSaving( needsSaving );

        for(Node child : node.getChildNodes())
        {
            setNeedsSaving( child, needsSaving );
        }
    }

    public String getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode( String selectedNode ) {
        this.selectedNode = selectedNode;
    }
    
}
