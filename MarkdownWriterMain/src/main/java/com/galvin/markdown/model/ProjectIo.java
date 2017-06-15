package com.galvin.markdown.model;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.compilers.Markup;
import com.galvin.markdown.compilers.NodeSeparators;
import com.galvin.markdown.preferences.EditorPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.tree.MarkdownTree;
import com.galvin.markdown.swing.tree.MarkdownTreeNode;
import com.galvin.markdown.templates.EmptyProject;
import com.galvin.markdown.util.MimeTypes;
import galvin.Lorem;
import galvin.StringUtils;
import galvin.SystemUtils;
import galvin.swing.text.DocumentUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ProjectIo
{

    public static final String[] PROJECT_MODEL_VERSIONS = new String[] { "2.0" };
    public static final String PROJECT_MODEL_VERSION = "2.0";
    public static final int BACKUP_COUNT = 10;
    public static final String PROJECT_STRUCTURE_DOCUMENT = "project.mdp.xml";
    public static final String PROJECT_STRUCTURE_DOCUMENT_EXTENSION = ".mdp.xml";
    public static final String PROJECT_DICTIONARY_DOCUMENT = "project.dat";
    public static final String MANUSCRIPT = "manuscript.md";
    public static final String DESCRIPTION = "description.md";
    public static final String SUMMARY = "summary.md";
    public static final String NOTES = "notes.md";
    public static final String RESOURCE_JPG = "image.jpg";
    public static final String RESOURCE_PNG = "image.png";
    public static final String RESOURCE_GIF = "image.gif";
    public static final String DOCUMENTS_DIRECTORY = "documents";
    public static final String BACKUPS_DIRECTORY = "backups";
    public static final String SNAPSHOTS_DIRECTORY = "snapshots";
    public static final String GRAVEYARD_DIRECTORY = "graveyard";
    public static final String EXPORT_DIRECTORY = "export";
    public static final String PROJECT_DICTIONARY_NAME = "dictionary.dic";
    public static final String PROJECT_DICTIONARY = DOCUMENTS_DIRECTORY + File.separator + PROJECT_DICTIONARY_NAME;
    private static final String DEFAULT_STYLE_SHEET = "/com/galvin/markdown/default-styles.css";

    /////////////////
    // Write projects
    /////////////////
    public static boolean needsSaving( MarkdownTree tree )
    {
        if( tree.needsSaving() )
        {
            return true;
        }
        else
        {
            MarkdownTreeNode root = (MarkdownTreeNode) tree.getRootNode();
            return needsSaving( root );
        }
    }

    public static boolean needsSaving( MarkdownTreeNode treeNode )
    {
        Node node = treeNode.getNode();
        if( node != null && node.needsSaving() )
        {
            return true;
        }
        else
        {
            for(int i = 0; i < treeNode.getChildCount(); i++)
            {
                MarkdownTreeNode child = (MarkdownTreeNode) treeNode.getChildAt( i );
                if( needsSaving( child ) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    public static void write( Project project, File projectFile )
        throws IOException
    {
        for(Node node : project.getChildNodes())
        {
            prepareToSave( node, projectFile );
        }

        JAXBContext context;
        try
        {
            project.setProjectModelVersion( PROJECT_MODEL_VERSION );
            project.setStyleSheetFromDocument();

            try
            {
                System.out.println( "Reading in project dictionary: " + project.getProjectDictionaryFile() );
                String projctDictionaryText = FileUtils.readFileToString( project.getProjectDictionaryFile() );
                //System.out.println( projctDictionaryText );
                project.setProjectDictionaryText( projctDictionaryText );
            }
            catch( Throwable t )
            {
                System.out.println( "Error writing project dictionary" );
                t.printStackTrace();
                project.setProjectDictionaryText( null );
            }

            /// two-stage save ///
            File tempFile = SystemUtils.getTempFileWithExtension( PROJECT_STRUCTURE_DOCUMENT_EXTENSION );
            
            context = JAXBContext.newInstance( Project.class );
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            marshaller.marshal( project, tempFile );
            tempFile.renameTo( projectFile );

            project.setUnsupportedLegacyVersion( false );
            for( Node node : project.getChildNodes() )
            {
                markeNeedsSaving( node, false );
            }
        }
        catch( JAXBException ex )
        {
            ex.printStackTrace();
            throw new IOException( "Unable to write xml output", ex );
        }
    }

    private static void prepareToSave( Node node, File projectDirectory )
        throws IOException
    {
        if( node != null )
        {
            node.prepareToSave();

            for(Node child : node.getChildNodes())
            {
                prepareToSave( child, projectDirectory );
            }
        }
    }
    
    private static void markeNeedsSaving( Node node, boolean needsSaving )
    {
        if( node != null )
        {
            node.setNeedsSaving( needsSaving );

            for(Node child : node.getChildNodes())
            {
                markeNeedsSaving( child, needsSaving );
            }
        }
    }

    public static void write( MarkdownTree tree, File projectDirectory )
        throws IOException
    {
        Project project = toProject( tree );
        write( project, projectDirectory );
    }

    ////////////////
    // Read projects
    ////////////////
    public static boolean isProjectModelVersionSupported( Project project )
    {
        String modelVersion = project.getProjectModelVersion();
        if( modelVersion != null )
        {
            modelVersion = modelVersion.trim();
        }

        for(String supportedVersion : PROJECT_MODEL_VERSIONS)
        {
            if( supportedVersion.equals( modelVersion ) )
            {
                return true;
            }
        }

        return false;
    }

    public static Project readProject( File projectStructureDocument )
        throws IOException
    {
        return readProject( projectStructureDocument, true );
    }

    public static Project readProject( File projectStructureDocument, boolean loadDocuments )
        throws IOException
    {
        System.out.println( "ProjectIo: reading project: " + projectStructureDocument );

        try
        {
            File projectDirectory = projectStructureDocument.getParentFile();
            JAXBContext context = JAXBContext.newInstance( Project.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Project result = (Project) unmarshaller.unmarshal( projectStructureDocument );

            if( !isProjectModelVersionSupported( result ) )
            {
                result = LegacyProjectIo.readProject( projectStructureDocument, loadDocuments );
                result.setUnsupportedLegacyVersion( true );
                return result;
            }

            result.setProjectDirectory( projectDirectory );
            result.setProjectFile( projectStructureDocument );
            result.setParent();
            result.readStyleSheetIntoDocument();

            if( result.getProjectDictionaryFile() == null )
            {
                result.setProjectDictionaryFile( SystemUtils.getTempFile( ProjectIo.PROJECT_DICTIONARY_DOCUMENT ) );
            }
            FileUtils.writeStringToFile( result.getProjectDictionaryFile(), StringUtils.neverNull( result.getProjectDictionaryText() ) );

            for(Node node : result.getChildNodes())
            {
                read( node );
            }

            EditorPreferences preferences = MarkdownServer.getPreferences().getEditorPreferences();
            if( preferences.liveSpellCheck() )
            {
                result.startSpellCheck();
            }
            else
            {
                result.stopSpellCheck();
            }

            SystemUtils.printMemory();
            return result;
        }
        catch( JAXBException ex )
        {
            throw new IOException( "Unable to read xml", ex );
        }
    }

    public static void read( Node node )
    {
        if( node != null )
        {
            node.loadAllDocuments();

            for(Node child : node.getChildNodes())
            {
                read( child );
            }
        }
    }

    public static Node createImageResource( File file )
        throws IOException
    {
        if( file != null && file.canRead() )
        {
            String mimeType = MimeTypes.getMimeType( file );
            if( MimeTypes.isImage( mimeType ) )
            {
                ImageResource imageResource = new ImageResource();
                imageResource.setMimeType( mimeType );

                byte[] bytes = FileUtils.readFileToByteArray( file );
                imageResource.setBytes( bytes );

                ImageIcon imageIcon = new ImageIcon( bytes );
                imageResource.setImageIcon( imageIcon );
                imageResource.setNeedsSaving( true );

                Node node = new Node();
                node.setTitle( file.getName() );
                node.setNodeType( NodeTypes.RESOURCE );
                node.setImageResource( imageResource );
                return node;
            }
        }

        return null;
    }

    ////////////////////
    // Write preferences
    ////////////////////
    public static void write( Preferences preferences, File preferencesFile )
        throws IOException
    {
        JAXBContext context;
        try
        {
            File tempFile = SystemUtils.getTempFileWithExtension( ".xml" );
            context = JAXBContext.newInstance( Preferences.class );
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            marshaller.marshal( preferences, tempFile );
            tempFile.renameTo( preferencesFile );
        }
        catch( JAXBException ex )
        {
            throw new IOException( "Unable to write xml output", ex );
        }
    }

    ///////////////////
    // Read preferences
    ///////////////////
    public static Preferences readPreferences( File preferencesFile )
        throws IOException
    {
        try
        {
            JAXBContext context = JAXBContext.newInstance( Preferences.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Preferences result = (Preferences) unmarshaller.unmarshal( preferencesFile );

            return result;
        }
        catch( JAXBException ex )
        {
            throw new IOException( "Unable to read xml", ex );
        }
    }

    //////////////////
    // Tree to Project
    //////////////////
    public static Project toProject( MarkdownTree tree )
    {
        MarkdownTreeNode root = (MarkdownTreeNode) tree.getRootNode();
        Project project = root.getProject();

        List<Node> nodeList = project.getChildNodes();
        nodeList.clear();

        List<MarkdownTreeNode> treeNodes = new ArrayList();
        for(int i = 0; i < root.getChildCount(); i++)
        {
            TreeNode treeNode = root.getChildAt( i );
            if( treeNode instanceof MarkdownTreeNode )
            {
                MarkdownTreeNode markdownTreeNode = (MarkdownTreeNode) treeNode;
                if( !markdownTreeNode.ignoreWhenSaving() )
                {
                    treeNodes.add( (MarkdownTreeNode) root.getChildAt( i ) );
                }
            }
            else
            {
                treeNodes.add( (MarkdownTreeNode) root.getChildAt( i ) );
            }
        }

        add( nodeList, treeNodes, tree );

        return project;
    }

    private static void add( List<Node> nodeList, List<MarkdownTreeNode> treeNodes, MarkdownTree tree )
    {
        for(MarkdownTreeNode treeNode : treeNodes)
        {
            Node childNode = treeNode.getNode();
            childNode.setExpanded( tree.isExpanded( treeNode ) );
            nodeList.add( childNode );

            List<Node> childNodeList = childNode.getChildNodes();
            childNodeList.clear();

            List<MarkdownTreeNode> childTreeNodes = new ArrayList();
            for(int i = 0; i < treeNode.getChildCount(); i++)
            {
                childTreeNodes.add( (MarkdownTreeNode) treeNode.getChildAt( i ) );
            }

            add( childNodeList, childTreeNodes, tree );
        }
    }

    ////////
    // Clone
    ////////
    public static Project clone( Project project, boolean shareDocuments, boolean reuseUuid )
        throws IOException
    {
        Project result = new Project();

        result.setUuid( project.getUuid() );
        result.setTitle( project.getTitle() );
        result.setSubtitle( project.getSubtitle() );
        result.setStyleSheet( project.getStyleSheet() );
        result.setLangauge( project.getLangauge() );
        result.setCreatedDate( project.getCreatedDate() );
        result.setModifiedDate( project.getModifiedDate() );
        result.setIdentifier( project.getIdentifier() );
        result.setIdentifierScheme( project.getIdentifierScheme() );
        result.setContributors( cloneContributors( project.getContributors() ) );
        result.setCompileOptions( clone( project.getCompileOptions() ) );
        result.getCompileOptions().setProject( result );

        for(String value : project.getGenres())
        {
            result.getGenres().add( value );
        }

        for(String value : project.getTopics())
        {
            result.getTopics().add( value );
        }

        for(String value : project.getKeywords())
        {
            result.getKeywords().add( value );
        }

        result.setManuscriptUuid( project.getManuscriptUuid() );
        result.setResourcesUuid( project.getResourcesUuid() );
        result.setTrashUuid( project.getTrashUuid() );
        result.setProjectDirectory( project.getProjectDirectory() );

        result.setChildNodes( cloneNodes( project.getChildNodes(), project.getProjectDirectory(), shareDocuments, reuseUuid ) );

        return result;
    }

    public static List<Node> cloneNodes( List<Node> nodes, File projectDirectory, boolean shareDocuments, boolean reuseUuid )
        throws IOException
    {
        List<Node> result = new ArrayList();

        for(Node node : nodes)
        {
            result.add( clone( node, projectDirectory, shareDocuments, reuseUuid ) );
        }

        return result;
    }

    public static Node clone( Node node, File projectDirectory, boolean shareDocuments, boolean reuseUuid )
        throws IOException
    {
        return clone( node, projectDirectory, shareDocuments, reuseUuid, true );
    }

    public static Node clone( Node node, File projectDirectory, boolean shareDocuments, boolean reuseUuid, boolean cloneChildren )
        throws IOException
    {
        Node result = new Node();

        if( reuseUuid )
        {
            result.setUuid( node.getUuid() );
        }

        result.setTitle( node.getTitle() );
        result.setSubtitle( node.getSubtitle() );
        result.setNodeType( node.getNodeType() );
        result.setIcon( node.getIcon() );
        result.setCreatedDate( node.getCreatedDate() );
        result.setModifiedDate( node.getModifiedDate() );
        result.setContributors( cloneContributors( node.getContributors() ) );
        result.setImageResource( cloneImageResource( node, projectDirectory, node.getImageResource(), shareDocuments ) );

        result.setManuscriptText( node.getManuscriptText() );
        result.setDescriptionText( node.getDescriptionText() );
        result.setSummaryText( node.getSummaryText() );
        result.setNotesText( node.getNotesText() );
        
        for(String keyWord : node.getKeywords())
        {
            result.getKeywords().add( keyWord );
        }

        if( shareDocuments )
        {
            result.setManuscript( node.getManuscript() );
            result.setDescription( node.getDescription() );
            result.setSummary( node.getSummary() );
            result.setNotes( node.getNotes() );
        }
        else
        {
            DocumentUtils.copyText( node.getManuscript(), result.getManuscript() );
            result.getManuscript().setNeedsSaving( true );

            DocumentUtils.copyText( node.getDescription(), result.getDescription() );
            result.getDescription().setNeedsSaving( true );

            DocumentUtils.copyText( node.getSummary(), result.getSummary() );
            result.getSummary().setNeedsSaving( true );

            DocumentUtils.copyText( node.getNotes(), result.getNotes() );
            result.getNotes().setNeedsSaving( true );

            result.setCursorStart( node.getCursorStart() );
            result.setCursorEnd( node.getCursorEnd() );
        }

        if( cloneChildren )
        {
            for(Node child : node.getChildNodes())
            {
                result.getChildNodes().add( clone( child, projectDirectory, shareDocuments, reuseUuid ) );
            }
        }

        return result;
    }

    public static List<Contributor> cloneContributors( List<Contributor> contributors )
    {
        List<Contributor> result = new ArrayList();

        for(Contributor contributor : contributors)
        {
            result.add( clone( contributor ) );
        }

        return result;
    }

    public static Contributor clone( Contributor contributor )
    {
        Contributor result = new Contributor();

        result.setName( contributor.getName() );
        result.setSortByName( contributor.getSortByName() );
        result.setRole( contributor.getRole() );

        return result;
    }

    public static ImageResource cloneImageResource( Node node, File projectDirectory, ImageResource imageResource, boolean shareDocuments )
        throws IOException
    {
        ImageResource result = null;

        if( imageResource != null )
        {
            result = new ImageResource();
            result.setImageIcon( imageResource.getImageIcon() );
            result.setMimeType( imageResource.getMimeType() );

            if( shareDocuments )
            {
                result.setImageIcon( imageResource.getImageIcon() );
                result.setBytes( imageResource.getBytes() );
            }
            else
            {
                byte[] bytes = imageResource.getBytes();
                if( bytes != null )
                {
                    result.setBytes( bytes );

                    ImageIcon imageIcon = new ImageIcon( bytes );
                    result.setImageIcon( imageIcon );

                    result.setNeedsSaving( true );
                }
            }
        }

        return result;
    }

    public static CompileOptions clone( CompileOptions compileOptions )
        throws IOException
    {
        CompileOptions result = new CompileOptions( compileOptions.getProject() );

        result.getExportFormats().clear();
        result.getExportFormats().addAll( compileOptions.getExportFormats() );

        result.setImportFormat( compileOptions.getImportFormat() );
        result.setManuscript( compileOptions.getManuscript() );
        result.setResources( compileOptions.getResources() );
        result.setOutputDirectory( compileOptions.getOutputDirectory() );
        result.setNodeSection( compileOptions.getNodeSection() );
        result.setIncludeTOC( compileOptions.includeTOC() );
        result.setTocDepth( compileOptions.getTocDepth() );
        result.setEpubChapterLevel( compileOptions.getEpubChapterLevel() );
        
        NodeSeparators separators = new NodeSeparators();
        result.setSeparators( separators );
        
        NodeSeparators originalSeparators = compileOptions.getSeparators();
        if( originalSeparators != null ){
            separators.setSeparatorSameLevel( originalSeparators.getSeparatorSameLevel() );
            separators.setSeparatorHigherToLower( originalSeparators.getSeparatorHigherToLower() );
            separators.setSeparatorLowerToHigher( originalSeparators.getSeparatorLowerToHigher() );
            separators.setEndOfDocumentMarker( originalSeparators.getEndOfDocumentMarker());
            
            separators.setCustomSameLevel( originalSeparators.getCustomSameLevel() );
            separators.setCustomHigherToLower( originalSeparators.getCustomHigherToLower() );
            separators.setCustomLowerToHigher( originalSeparators.getCustomLowerToHigher() );
            separators.setCustomEndOfDocument( originalSeparators.getCustomEndOfDocument() );
        }
        
        result.setProjectContributorMarkup( compileOptions.getProjectContributorMarkup() );
        result.setNodeContributorMarkup( compileOptions.getNodeContributorMarkup() );
        result.setIncludeContributors( compileOptions.includeContributors() );
        result.setIncludeContributorRoles( compileOptions.includeContributorRoles() );
        result.setIncludeTitlesOfFolders( compileOptions.includeTitlesOfFolders() );
        result.setIncludeTitlesOfFiles( compileOptions.includeTitlesOfFiles() );
        result.setIncludeSubtitlesOfFolders( compileOptions.includeSubtitlesOfFolders() );
        result.setIncludeSubtitlesOfFiles( compileOptions.includeSubtitlesOfFiles() );
        result.setGenerateTitleBlock( compileOptions.generateTitleBlock() );
        result.setSeparatorFolderFolder( compileOptions.getSeparatorFolderFolder() );
        result.setSeparatorFolderFile( compileOptions.getSeparatorFolderFile() );
        result.setSeparatorFileFolder( compileOptions.getSeparatorFileFolder() );
        result.setSeparatorFileFile( compileOptions.getSeparatorFileFile() );
        result.setSeparatorTitleFile( compileOptions.getSeparatorTitleFile() );
        result.setSeparatorTitleFolder( compileOptions.getSeparatorTitleFolder() );
        

        return result;
    }

    public static void join( List<Node> nodes )
    {
        if( nodes != null && nodes.size() > 1 )
        {
            StringBuilder manuscript = new StringBuilder();
            StringBuilder description = new StringBuilder();
            StringBuilder summary = new StringBuilder();
            StringBuilder notes = new StringBuilder();

            for(int i = 0; i < nodes.size(); i++)
            {
                Node node = nodes.get( i );
                boolean padding = i > 0;

                String newManuscript = DocumentUtils.getText( node.getManuscript() );
                joinIfNecessary( manuscript, newManuscript, padding );

                String newDescription = DocumentUtils.getText( node.getDescription() );
                joinIfNecessary( description, newDescription, padding );

                String newSummary = DocumentUtils.getText( node.getSummary() );
                joinIfNecessary( summary, newSummary, padding );

                String newNotes = DocumentUtils.getText( node.getNotes() );
                joinIfNecessary( notes, newNotes, padding );
            }

            Node node = nodes.get( 0 );
            DocumentUtils.setText( node.getManuscript(), manuscript );
            DocumentUtils.setText( node.getDescription(), description );
            DocumentUtils.setText( node.getSummary(), summary );
            DocumentUtils.setText( node.getNotes(), notes );
        }
    }

    private static void joinIfNecessary( StringBuilder target, String text, boolean padding )
    {
        if( !StringUtils.empty( text ) )
        {
            if( padding )
            {
                target.append( Markup.LINE_BREAK );
            }
            target.append( text );
        }
    }

    ////////////
    // To String
    ////////////
    public static String toString( Project project )
    {
        return toString( project, false );
    }

    public static String toString( Project project, boolean includeManuscript )
    {
        StringBuilder builder = new StringBuilder();
        builder.append( "Project: " );
        builder.append( project.getTitle() );
        builder.append( "\n" );

        for(Node node : project.getChildNodes())
        {
            toString( builder, node, 1, includeManuscript );
        }
        return builder.toString();
    }

    private static void toString( StringBuilder builder, Node node, int indent, boolean includeManuscript )
    {
        for(int i = 0; i < indent; i++)
        {
            builder.append( "  " );
        }
        builder.append( node.getTitle() );

        if( includeManuscript )
        {
            builder.append( ": " );
            builder.append( DocumentUtils.getText( node.getManuscript() ) );
        }

        builder.append( "\n" );

        for(Node child : node.getChildNodes())
        {
            toString( builder, child, indent + 1, includeManuscript );
        }
    }

    ////////
    // Trash
    ////////
    public static void removeFromProject( Node node, File projectDirectory )
        throws IOException
    {
        stopSpellCheck( node );
    }

    //////////////
    // Spell Check
    //////////////
    public static void stopSpellCheck( Node node )
    {
        node.getManuscript().stopSpellCheck();
        node.getDescription().stopSpellCheck();
        node.getSummary().stopSpellCheck();
        node.getNotes().stopSpellCheck();

        for(Node child : node.getChildNodes())
        {
            stopSpellCheck( child );
        }
    }

    ////////
    // Utils
    ////////
    private static File getNodeDirectory( Node node, File projectDirectory )
        throws IOException
    {
        String nodeDir = node.getUuid();
        File result = new File( projectDirectory, DOCUMENTS_DIRECTORY );
        result = new File( result, nodeDir );
        result.mkdirs();
        return result;
    }

    private static File getNodeFile( Node node, File projectDirectory, String document )
        throws IOException
    {
        File result = getNodeDirectory( node, projectDirectory );
        result = new File( result, document );
        return result;
    }

    public static File getImageBinary( Node node, File projectDirectory )
        throws IOException
    {
        File gifFile = getNodeFile( node, projectDirectory, RESOURCE_GIF );
        if( gifFile.exists() )
        {
            return gifFile;
        }

        File jpgFile = getNodeFile( node, projectDirectory, RESOURCE_JPG );
        if( jpgFile.exists() )
        {
            return jpgFile;
        }

        File pngFile = getNodeFile( node, projectDirectory, RESOURCE_PNG );
        if( pngFile.exists() )
        {
            return pngFile;
        }

        return null;
    }

    public static String createImageCode( Node resourceNode )
    {
        ImageResource resource = resourceNode.getImageResource();
        if( resource != null )
        {
            String title = resourceNode.getTitle();
            StringBuilder result = new StringBuilder( 25 );
            result.append( "![](" );
            result.append( title );
            result.append( ")" );
            return result.toString();
        }

        return "";
    }

    public static void setCreationDate( Project project )
    {
        for(Node node : project.getChildNodes())
        {
            setCreationDate( node, new GregorianCalendar() );
        }
    }

    public static void setCreationDate( Node node, GregorianCalendar calendar )
    {
        node.setCreatedDate( calendar );
        node.setModifiedDate( calendar );

        for(Node child : node.getChildNodes())
        {
            setCreationDate( child, calendar );
        }
    }

    ////////////////
    // Project Stubs
    ////////////////
    public static Project createDefaultProject()
    {
        return new EmptyProject().getProject();
    }

    public static Project createDummyProject()
    {
        MarkdownMessages messages = MarkdownServer.getMessages();

        Project project = new Project();
        project.setCreatedDate( new GregorianCalendar() );
        project.setModifiedDate( new GregorianCalendar() );
        project.setTitle( "A Collection of Public Domain Works" );

        Contributor poe = new Contributor( "Edgar Allen Poe", "Poe, Edgar Allen" );
        Contributor carroll = new Contributor( "Lewis Carroll", "Carroll, Lewis" );
        project.getContributors().add( poe );
        project.getContributors().add( carroll );

        Node root = new Node();
        root.setTitle( messages.titleUntitled() );
        root.setNodeType( NodeTypes.PROJECT );
        root.setCreatedDate( new GregorianCalendar() );
        root.setModifiedDate( new GregorianCalendar() );

        Node manuscript = new Node();
        manuscript.setTitle( messages.titleManuscript() );
        manuscript.setNodeType( NodeTypes.MANUSCRIPT );
        manuscript.setCreatedDate( new GregorianCalendar() );
        manuscript.setModifiedDate( new GregorianCalendar() );
        project.getChildNodes().add( manuscript );

        Node theRavenFolder = new Node();
        theRavenFolder.setTitle( "The Raven" );
        theRavenFolder.setSubtitle( "A Tale of Terror" );
        theRavenFolder.getContributors().add( poe );
        theRavenFolder.setNodeType( NodeTypes.MARKDOWN );
        theRavenFolder.setCreatedDate( new GregorianCalendar() );
        theRavenFolder.setModifiedDate( new GregorianCalendar() );
        manuscript.getChildNodes().add( theRavenFolder );

        Node theRavenFile = new Node();
        theRavenFile.setTitle( "The Raven" );
        theRavenFile.setNodeType( NodeTypes.MARKDOWN );
        theRavenFile.setCreatedDate( new GregorianCalendar() );
        theRavenFile.setModifiedDate( new GregorianCalendar() );
        DocumentUtils.setText( theRavenFile.getManuscript(), Lorem.raven() );
        DocumentUtils.setText( theRavenFile.getDescription(), "Description" );
        DocumentUtils.setText( theRavenFile.getSummary(), "Summary" );
        DocumentUtils.setText( theRavenFile.getNotes(), "Notes" );
        theRavenFolder.getChildNodes().add( theRavenFile );

        Node jabberwockyFolder = new Node();
        jabberwockyFolder.setTitle( "The Jabberwocky" );
        jabberwockyFolder.getContributors().add( carroll );
        jabberwockyFolder.setNodeType( NodeTypes.MARKDOWN );
        jabberwockyFolder.setCreatedDate( new GregorianCalendar() );
        jabberwockyFolder.setModifiedDate( new GregorianCalendar() );
        manuscript.getChildNodes().add( jabberwockyFolder );

        Node jabberwockyFile = new Node();
        jabberwockyFile.setTitle( "The Jabberwocky" );
        jabberwockyFile.setNodeType( NodeTypes.MARKDOWN );
        jabberwockyFile.setCreatedDate( new GregorianCalendar() );
        jabberwockyFile.setModifiedDate( new GregorianCalendar() );
        DocumentUtils.setText( jabberwockyFile.getManuscript(), Lorem.jabberwocky() );
        DocumentUtils.setText( jabberwockyFile.getDescription(), "Description" );
        DocumentUtils.setText( jabberwockyFile.getSummary(), "Summary" );
        DocumentUtils.setText( jabberwockyFile.getNotes(), "Notes" );
        jabberwockyFolder.getChildNodes().add( jabberwockyFile );

        Node loremFolder = new Node();
        loremFolder.setTitle( "Lorem Ipsum" );
        loremFolder.setNodeType( NodeTypes.MARKDOWN );
        loremFolder.setCreatedDate( new GregorianCalendar() );
        loremFolder.setModifiedDate( new GregorianCalendar() );
        manuscript.getChildNodes().add( loremFolder );

        Node loremFile = new Node();
        loremFile.setTitle( "Lorem Ipsum" );
        loremFile.setNodeType( NodeTypes.MARKDOWN );
        loremFile.setCreatedDate( new GregorianCalendar() );
        loremFile.setModifiedDate( new GregorianCalendar() );
        DocumentUtils.setText( loremFile.getManuscript(), Lorem.loremIpsumParagraphs() );
        DocumentUtils.setText( loremFile.getDescription(), "Description" );
        DocumentUtils.setText( loremFile.getSummary(), "Summary" );
        DocumentUtils.setText( loremFile.getNotes(), "Notes" );
        loremFolder.getChildNodes().add( loremFile );

        Node research = new Node();
        research.setTitle( messages.titleResearch() );
        research.setNodeType( NodeTypes.MARKDOWN );
        research.setCreatedDate( new GregorianCalendar() );
        research.setModifiedDate( new GregorianCalendar() );
        project.getChildNodes().add( research );

        Node resources = new Node();
        resources.setTitle( messages.titleResources() );
        resources.setNodeType( NodeTypes.RESOURCES );
        resources.setCreatedDate( new GregorianCalendar() );
        resources.setModifiedDate( new GregorianCalendar() );
        project.getChildNodes().add( resources );

        Node trash = new Node();
        trash.setTitle( messages.titleTrash() );
        trash.setNodeType( NodeTypes.TRASH );
        trash.setCreatedDate( new GregorianCalendar() );
        trash.setModifiedDate( new GregorianCalendar() );
        project.getChildNodes().add( trash );

        Node garbage = new Node();
        garbage.setTitle( "Throw me out!" );
        garbage.setNodeType( NodeTypes.MARKDOWN );
        garbage.setCreatedDate( new GregorianCalendar() );
        garbage.setModifiedDate( new GregorianCalendar() );
        trash.getChildNodes().add( garbage );

        project.setManuscriptUuid( manuscript.getUuid() );
        project.setResourcesUuid( resources.getUuid() );
        project.setTrashUuid( trash.getUuid() );

        return project;
    }

    public static String getDefaultStyleSheet()
        throws IOException
    {
        InputStream inputStream = ProjectIo.class.getResourceAsStream( DEFAULT_STYLE_SHEET );
        String css = IOUtils.toString( inputStream );
        return css;
    }
}
