package com.galvin.markdown.model;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.compilers.Markup;
import com.galvin.markdown.preferences.EditorPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import com.galvin.markdown.swing.tree.MarkdownTree;
import com.galvin.markdown.swing.tree.MarkdownTreeNode;
import com.galvin.markdown.templates.EmptyProject;
import com.galvin.markdown.util.MimeTypes;
import galvin.Lorem;
import galvin.StringUtils;
import galvin.swing.text.DocumentUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.ImageIcon;
import javax.swing.tree.TreeNode;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class LegacyProjectIo {

    private static final LegacyProjectIo.BackupFileComparator backupFileComparator = new LegacyProjectIo.BackupFileComparator();
    public static final String PROJECT_MODEL_VERSION = "1.0";
    public static final int BACKUP_COUNT = 10;
    public static final String PROJECT_STRUCTURE_DOCUMENT = "project.mdp.xml";
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
    public static boolean needsSaving( MarkdownTree tree ) {
        if( tree.needsSaving() ) {
            return true;
        }
        else {
            MarkdownTreeNode root = (MarkdownTreeNode)tree.getRootNode();
            return needsSaving( root );
        }
    }

    public static boolean needsSaving( MarkdownTreeNode treeNode ) {
        Node node = treeNode.getNode();
        if( node != null && node.needsSaving() ) {
            return true;
        }
        else {
            for( int i = 0; i < treeNode.getChildCount(); i++ ) {
                MarkdownTreeNode child = (MarkdownTreeNode)treeNode.getChildAt( i );
                if( needsSaving( child ) ) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void write( Project project, File projectDirectory )
        throws IOException {
        File file = new File( projectDirectory, DOCUMENTS_DIRECTORY );
        file.mkdirs();

        file = new File( projectDirectory, BACKUPS_DIRECTORY );
        file.mkdirs();

        file = new File( projectDirectory, SNAPSHOTS_DIRECTORY );
        file.mkdirs();

        file = new File( projectDirectory, EXPORT_DIRECTORY );
        file.mkdirs();

        for( Node node : project.getChildNodes() ) {
            write( node, projectDirectory );
        }

        File structureDoc = new File( projectDirectory, PROJECT_STRUCTURE_DOCUMENT );
        structureDoc.getParentFile().mkdirs();

        JAXBContext context;
        try {
            project.setStyleSheetFromDocument();

            context = JAXBContext.newInstance( Project.class );
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            marshaller.marshal( project, structureDoc );
        }
        catch( JAXBException ex ) {
            ex.printStackTrace();
            throw new IOException( "Unable to write xml output", ex );
        }
    }

    private static void write( Node node, File projectDirectory )
        throws IOException {
        if( node != null ) {
            node.prepareToSave();
            write( node, node.getManuscript(), projectDirectory, MANUSCRIPT );
            write( node, node.getDescription(), projectDirectory, DESCRIPTION );
            write( node, node.getSummary(), projectDirectory, SUMMARY );
            write( node, node.getNotes(), projectDirectory, NOTES );
            write( node, node.getImageResource(), projectDirectory );

            for( Node child : node.getChildNodes() ) {
                write( child, projectDirectory );
            }
        }
    }

    private static void write( Node node, MarkdownDocument document, File projectDirectory, String fileName )
        throws IOException {
        if( document != null ) {
            if( document.needsSaving() ) {
                File outputFile = getNodeFile( node, projectDirectory, fileName );
                outputFile.getParentFile().mkdirs();
                String documentText = DocumentUtils.getText( document );
                FileUtils.writeStringToFile( outputFile, documentText );
                document.setNeedsSaving( false );
                node.setModifiedDate( new GregorianCalendar() );
            }
        }
    }

    private static void write( Node node, ImageResource imageResource, File projectDirectory )
        throws IOException {
        if( imageResource != null ) {
            if( imageResource.needsSaving() ) {
                byte[] bytes = imageResource.getBytes();
                if( bytes != null ) {
                    String fileName = getFileName( imageResource );
                    if( fileName != null ) {
                        File outputFile = getNodeFile( node, projectDirectory, fileName );
                        outputFile.getParentFile().mkdirs();
                        FileUtils.writeByteArrayToFile( outputFile, bytes );
                        imageResource.setNeedsSaving( false );
                    }
                }
            }
        }
    }

    private static String getFileName( ImageResource imageResource ) {
        String imageType = imageResource.getMimeType();
        if( MimeTypes.MIME_TYPE_JPEG.equals( imageType ) ) {
            return RESOURCE_JPG;
        }
        else if( MimeTypes.MIME_TYPE_PNG.equals( imageType ) ) {
            return RESOURCE_PNG;
        }
        else if( MimeTypes.MIME_TYPE_GIF.equals( imageType ) ) {
            return RESOURCE_GIF;
        }

        return null;
    }

    public static void write( MarkdownTree tree, File projectDirectory )
        throws IOException {
        Project project = toProject( tree );
        write( project, projectDirectory );
    }

    ////////////////
    // Read projects
    ////////////////
    public static Project readProject( File projectStructureDocument )
        throws IOException {
        return readProject( projectStructureDocument, true );
    }

    public static Project readProject( File projectStructureDocument, boolean loadDocuments )
        throws IOException {
        System.out.println( "reading project: " + projectStructureDocument );
        try {
            File projectDirectory = projectStructureDocument.getParentFile();
            JAXBContext context = JAXBContext.newInstance( Project.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Project result = (Project)unmarshaller.unmarshal( projectStructureDocument );
            result.setProjectDirectory( projectDirectory );
            result.setParent();

            findOrphans( projectDirectory, result );

            if( loadDocuments ) {
                Node cover = result.getCover();
                if( cover != null ) {
                    readNode( cover, projectDirectory );
                }

                for( Node node : result.getChildNodes() ) {
                    readNode( node, projectDirectory );
                }

                result.readStyleSheetIntoDocument();
            }

            EditorPreferences preferences = MarkdownServer.getPreferences().getEditorPreferences();
            if( preferences.liveSpellCheck() ) {
                result.startSpellCheck();
            }
            else {
                result.stopSpellCheck();
            }

            return result;
        }
        catch( JAXBException ex ) {
            throw new IOException( "Unable to read xml", ex );
        }
    }

    private static void findOrphans( File projectDirectory, Project project )
        throws IOException {
        MarkdownMessages messages = MarkdownServer.getMessages();

        File documentsDir = new File( projectDirectory, DOCUMENTS_DIRECTORY );
        File[] docDirs = documentsDir.listFiles();
        for( File docDir : docDirs ) {
            if( !PROJECT_DICTIONARY_NAME.equals( docDir.getName() ) ) {
                if( !exists( project, docDir.getName() ) ) {
                    System.out.println( "adding " + docDir.getName() + " to trash." );
                    Node node = new Node();
                    node.setUuid( docDir.getName() );
                    node.setTitle( messages.untitled() );
                    readNode( node, projectDirectory );
                    project.getTrash().getChildNodes().add( node );
                }
            }
        }
    }

    private static boolean exists( Project project, String uuid ) {
        for( Node node : project.getChildNodes() ) {
            if( exists( node, uuid ) ) {
                return true;
            }
        }

        return false;
    }

    private static boolean exists( Node node, String uuid ) {
        if( uuid.equals( node.getUuid() ) ) {
            return true;
        }
        else {
            for( Node child : node.getChildNodes() ) {
                if( exists( child, uuid ) ) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void readNode( Node node, File projectDirectory )
        throws IOException {
        readNode( node, projectDirectory, NodeSection.MANUSCRIPT, node.getManuscript() );
        readNode( node, projectDirectory, NodeSection.DESCRIPTION, node.getDescription() );
        readNode( node, projectDirectory, NodeSection.SUMMARY, node.getSummary() );
        readNode( node, projectDirectory, NodeSection.NOTES, node.getNotes() );
        readImageIcon( node, projectDirectory );

        node.setProjectDirectory( projectDirectory );
        for( Node child : node.getChildNodes() ) {
            readNode( child, projectDirectory );
        }
    }

    private static void readNode( Node node, File projectDirectory, NodeSection nodeSection, MarkdownDocument document )
        throws IOException {
        document.setNode( node );
        document.setNodeSection( nodeSection );

        String fileName = MANUSCRIPT;
        if( NodeSection.DESCRIPTION.equals( nodeSection ) ) {
            fileName = DESCRIPTION;
        }
        else if( NodeSection.SUMMARY.equals( nodeSection ) ) {
            fileName = SUMMARY;
        }
        else if( NodeSection.NOTES.equals( nodeSection ) ) {
            fileName = NOTES;
        }

        File file = getNodeFile( node, projectDirectory, fileName );
        if( file.exists() && file.canRead() ) {
            String text = FileUtils.readFileToString( file );
            DocumentUtils.setText( document, text );
            document.setNeedsSaving( false );
        }
    }

    private static void readImageIcon( Node node, File projectDirectory )
        throws IOException {
        byte[] bytes = readImage( node, projectDirectory );
        if( bytes != null ) {
            ImageResource imageResource = node.getImageResource();
            ImageIcon imageIcon = new ImageIcon( bytes );
            imageResource.setImageIcon( imageIcon );
            imageResource.setBytes( bytes );
        }
    }

    public static byte[] readImage( Node node, File projectDirectory )
        throws IOException {
        ImageResource imageResource = node.getImageResource();
        String fileName = getFileName( imageResource );
        if( fileName != null ) {
            File file = getNodeFile( node, projectDirectory, fileName );
            if( file.exists() && file.canRead() ) {
                byte[] bytes = org.apache.commons.io.FileUtils.readFileToByteArray( file );
                return bytes;
            }
        }

        return null;
    }

    public static Node createImageResource( File file )
        throws IOException {
        if( file != null && file.canRead() ) {
            String mimeType = MimeTypes.getMimeType( file );
            if( MimeTypes.isImage( mimeType ) ) {
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

    //////////
    // Backups
    //////////
    public static String getDefaultBackupName() {
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MMM-d-k-m-s-S" );
        String name = format.format( new Date() );
        return name;
    }

    public static void createBackupZip( File projectDirectory )
        throws IOException {
        String name = getDefaultBackupName() + ".zip";
        createBackupZip( projectDirectory, name );
    }

    public static void createBackupZip( File projectDirectory, String backupTitle )
        throws IOException {
        createBackupZip( projectDirectory, BACKUPS_DIRECTORY, backupTitle );
    }

    public static void createSnapshotZip( File projectDirectory, String backupTitle )
        throws IOException {
        createBackupZip( projectDirectory, SNAPSHOTS_DIRECTORY, backupTitle );
    }

    private static void createBackupZip( File projectDirectory, String backupFolder, String backupTitle )
        throws IOException {
        if( !backupTitle.endsWith( ".zip" ) ) {
            backupTitle += ".zip";
        }

        File backupDirectory = new File( projectDirectory, backupFolder );
        backupDirectory.mkdirs();

        File outputFile = new File( backupDirectory, backupTitle );
        FileOutputStream fileOutputStream = new FileOutputStream( outputFile );
        ZipOutputStream zipOutputStream = new ZipOutputStream( fileOutputStream );

        File projectFile = new File( projectDirectory, PROJECT_STRUCTURE_DOCUMENT );
        String projectFileText = FileUtils.readFileToString( projectFile );

        byte[] bytes = projectFileText.toString().getBytes( "UTF-8" );
        ZipEntry entry = new ZipEntry( PROJECT_STRUCTURE_DOCUMENT );
        zipOutputStream.putNextEntry( entry );
        zipOutputStream.write( bytes );
        zipOutputStream.closeEntry();

        entry = new ZipEntry( DOCUMENTS_DIRECTORY + "/" );
        zipOutputStream.putNextEntry( entry );
        zipOutputStream.closeEntry();

        File documentsDirectory = new File( projectDirectory, DOCUMENTS_DIRECTORY );
        if( documentsDirectory.exists() ) {
            File[] nodeDirectories = documentsDirectory.listFiles();
            if( nodeDirectories != null ) {
                for( File nodeDirectory : nodeDirectories ) {
                    if( nodeDirectory.getName().startsWith( "." ) ) {
                        continue;
                    }

                    String nodeId = DOCUMENTS_DIRECTORY + "/" + nodeDirectory.getName() + "/";
                    entry = new ZipEntry( nodeId );
                    zipOutputStream.putNextEntry( entry );
                    zipOutputStream.closeEntry();

                    File[] nodeContents = nodeDirectory.listFiles();
                    if( nodeContents != null ) {
                        for( File nodeContent : nodeContents ) {
                            if( nodeContent.getName().startsWith( "." ) ) {
                                continue;
                            }

                            String nodeName = nodeContent.getName();
                            bytes = FileUtils.readFileToByteArray( nodeContent );

                            entry = new ZipEntry( nodeId + nodeName );
                            zipOutputStream.putNextEntry( entry );
                            zipOutputStream.write( bytes );
                            zipOutputStream.closeEntry();
                        }
                    }
                }
            }
        }

        zipOutputStream.close();
    }

    public static void createBackupDir( File projectDirectory )
        throws IOException {
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MMM-d-k-m-s-S" );
        String timestamp = format.format( new Date() );
        createBackupDir( projectDirectory, timestamp );
    }

    public static void createBackupDir( File projectDirectory, String backupTitle )
        throws IOException {
        File backupDirectory = new File( projectDirectory, BACKUPS_DIRECTORY );
        backupDirectory = new File( backupDirectory, backupTitle );
        backupDirectory.mkdirs();

        File projectFile = new File( projectDirectory, PROJECT_STRUCTURE_DOCUMENT );
        File backupProjectFile = new File( backupDirectory, PROJECT_STRUCTURE_DOCUMENT );
        FileUtils.copyFile( projectFile, backupProjectFile );

        File documentsDirectory = new File( projectDirectory, DOCUMENTS_DIRECTORY );
        if( documentsDirectory.exists() ) {
            FileUtils.copyDirectory( documentsDirectory, backupDirectory );
        }
    }

    public static void cleanBackups( File projectDirectory ) {
        File backupsDirectory = new File( projectDirectory, BACKUPS_DIRECTORY );
        backupsDirectory.lastModified();

        List<File> backupFiles = Arrays.asList( backupsDirectory.listFiles() );
        int size = backupFiles.size();
        if( size > BACKUP_COUNT ) {
            Collections.sort( backupFiles, backupFileComparator );
            int startIndex = 0;
            int endIndex = size - BACKUP_COUNT;

            backupFiles = backupFiles.subList( startIndex, endIndex );
            LegacyProjectIo.BackupCleaner backupCleaner = new LegacyProjectIo.BackupCleaner( backupFiles );
            backupCleaner.start();
        }
    }

    private static class BackupFileComparator
        implements Comparator {

        public int compare( Object one, Object two ) {
            if( one instanceof File && two instanceof File ) {
                File fileOne = (File)one;
                File fileTwo = (File)two;

                long diff = fileOne.lastModified() - fileTwo.lastModified();
                int intDiff = (int)diff;
                if( diff == intDiff ) {
                    return intDiff;
                }
                else if( diff > 0 ) {
                    return 1;
                }
                else if( diff < 0 ) {
                    return -1;
                }
            }

            return 0;
        }

    }

    private static class BackupCleaner
        extends Thread {

        private List<File> files;

        public BackupCleaner( List<File> files ) {
            this.files = files;
        }

        @Override
        public void run() {
            for( File file : files ) {
                try {
                    if( file.isDirectory() ) {
                        FileUtils.deleteDirectory( file );
                    }
                    else {
                        file.delete();
                    }
                }
                catch( Throwable t ) {
                    t.printStackTrace();
                }
            }
        }

    }

    ////////////////////
    // Write preferences
    ////////////////////
    public static void write( Preferences preferences, File preferencesFile )
        throws IOException {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance( Preferences.class );
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
            marshaller.marshal( preferences, preferencesFile );
        }
        catch( JAXBException ex ) {
            throw new IOException( "Unable to write xml output", ex );
        }
    }

    ///////////////////
    // Read preferences
    ///////////////////
    public static Preferences readPreferences( File preferencesFile )
        throws IOException {
        try {
            JAXBContext context = JAXBContext.newInstance( Preferences.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Preferences result = (Preferences)unmarshaller.unmarshal( preferencesFile );

            return result;
        }
        catch( JAXBException ex ) {
            throw new IOException( "Unable to read xml", ex );
        }
    }

    //////////////////
    // Tree to Project
    //////////////////
    public static Project toProject( MarkdownTree tree ) {
        MarkdownTreeNode root = (MarkdownTreeNode)tree.getRootNode();
        Project project = root.getProject();

        List<Node> nodeList = project.getChildNodes();
        nodeList.clear();

        List<MarkdownTreeNode> treeNodes = new ArrayList();
        for( int i = 0; i < root.getChildCount(); i++ ) {
            TreeNode treeNode = root.getChildAt( i );
            if( treeNode instanceof MarkdownTreeNode ) {
                MarkdownTreeNode markdownTreeNode = (MarkdownTreeNode)treeNode;
                if( !markdownTreeNode.ignoreWhenSaving() ) {
                    treeNodes.add( (MarkdownTreeNode)root.getChildAt( i ) );
                }
            }
            else {
                treeNodes.add( (MarkdownTreeNode)root.getChildAt( i ) );
            }
        }

        add( nodeList, treeNodes, tree );

        return project;
    }

    private static void add( List<Node> nodeList, List<MarkdownTreeNode> treeNodes, MarkdownTree tree ) {
        for( MarkdownTreeNode treeNode : treeNodes ) {
            Node childNode = treeNode.getNode();
            childNode.setExpanded( tree.isExpanded( treeNode ) );
            nodeList.add( childNode );

            List<Node> childNodeList = childNode.getChildNodes();
            childNodeList.clear();

            List<MarkdownTreeNode> childTreeNodes = new ArrayList();
            for( int i = 0; i < treeNode.getChildCount(); i++ ) {
                childTreeNodes.add( (MarkdownTreeNode)treeNode.getChildAt( i ) );
            }

            add( childNodeList, childTreeNodes, tree );
        }
    }

    ////////
    // Clone
    ////////
    public static Project clone( Project project, boolean shareDocuments, boolean reuseUuid )
        throws IOException {
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

        for( String value : project.getGenres() ) {
            result.getGenres().add( value );
        }

        for( String value : project.getTopics() ) {
            result.getTopics().add( value );
        }

        for( String value : project.getKeywords() ) {
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
        throws IOException {
        List<Node> result = new ArrayList();

        for( Node node : nodes ) {
            result.add( clone( node, projectDirectory, shareDocuments, reuseUuid ) );
        }

        return result;
    }

    public static Node clone( Node node, File projectDirectory, boolean shareDocuments, boolean reuseUuid )
        throws IOException {
        return clone( node, projectDirectory, shareDocuments, reuseUuid, true );
    }

    public static Node clone( Node node, File projectDirectory, boolean shareDocuments, boolean reuseUuid, boolean cloneChildren )
        throws IOException {
        Node result = new Node();

        if( reuseUuid ) {
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

        for( String keyWord : node.getKeywords() ) {
            result.getKeywords().add( keyWord );
        }

        if( shareDocuments ) {
            result.setManuscript( node.getManuscript() );
            result.setDescription( node.getDescription() );
            result.setSummary( node.getSummary() );
            result.setNotes( node.getNotes() );
        }
        else {
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

        if( cloneChildren ) {
            for( Node child : node.getChildNodes() ) {
                result.getChildNodes().add( clone( child, projectDirectory, shareDocuments, reuseUuid ) );
            }
        }

        return result;
    }

    public static List<Contributor> cloneContributors( List<Contributor> contributors ) {
        List<Contributor> result = new ArrayList();

        for( Contributor contributor : contributors ) {
            result.add( clone( contributor ) );
        }

        return result;
    }

    public static Contributor clone( Contributor contributor ) {
        Contributor result = new Contributor();

        result.setName( contributor.getName() );
        result.setSortByName( contributor.getSortByName() );
        result.setRole( contributor.getRole() );

        return result;
    }

    public static ImageResource cloneImageResource( Node node, File projectDirectory, ImageResource imageResource, boolean shareDocuments )
        throws IOException {
        ImageResource result = null;

        if( imageResource != null ) {
            result = new ImageResource();
            result.setImageIcon( imageResource.getImageIcon() );
            result.setMimeType( imageResource.getMimeType() );

            if( shareDocuments ) {
                result.setImageIcon( imageResource.getImageIcon() );
                result.setBytes( imageResource.getBytes() );
            }
            else {
                byte[] bytes = readImage( node, projectDirectory );
                if( bytes != null ) {
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
        throws IOException {
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
        result.setProjectContributorMarkup( compileOptions.getProjectContributorMarkup() );
        result.setNodeContributorMarkup( compileOptions.getNodeContributorMarkup() );

        return result;
    }

    public static void join( List<Node> nodes ) {
        if( nodes != null && nodes.size() > 1 ) {
            StringBuilder manuscript = new StringBuilder();
            StringBuilder description = new StringBuilder();
            StringBuilder summary = new StringBuilder();
            StringBuilder notes = new StringBuilder();

            for( int i = 0; i < nodes.size(); i++ ) {
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

    private static void joinIfNecessary( StringBuilder target, String text, boolean padding ) {
        if( !StringUtils.empty( text ) ) {
            if( padding ) {
                target.append( Markup.LINE_BREAK );
            }
            target.append( text );
        }
    }

    ////////////
    // To String
    ////////////
    public static String toString( Project project ) {
        return toString( project, false );
    }

    public static String toString( Project project, boolean includeManuscript ) {
        StringBuilder builder = new StringBuilder();
        builder.append( "Project: " );
        builder.append( project.getTitle() );
        builder.append( "\n" );

        for( Node node : project.getChildNodes() ) {
            toString( builder, node, 1, includeManuscript );
        }
        return builder.toString();
    }

    private static void toString( StringBuilder builder, Node node, int indent, boolean includeManuscript ) {
        for( int i = 0; i < indent; i++ ) {
            builder.append( "  " );
        }
        builder.append( node.getTitle() );

        if( includeManuscript ) {
            builder.append( ": " );
            builder.append( DocumentUtils.getText( node.getManuscript() ) );
        }

        builder.append( "\n" );

        for( Node child : node.getChildNodes() ) {
            toString( builder, child, indent + 1, includeManuscript );
        }
    }

    ////////
    // Trash
    ////////
    public static void removeFromProject( Node node, File projectDirectory )
        throws IOException {
        moveToGraveyard( node, projectDirectory );
    }

    public static void moveToGraveyard( Node node, File projectDirectory )
        throws IOException {
        File graveyard = new File( projectDirectory, GRAVEYARD_DIRECTORY );
        graveyard = new File( graveyard, node.getUuid() );
        graveyard.mkdirs();

        File nodeDir = getNodeDirectory( node, projectDirectory );

        if( nodeDir.isDirectory() ) {
            FileUtils.copyDirectory( nodeDir, graveyard );
        }
        else {
            graveyard = new File( graveyard, nodeDir.getName() );
            FileUtils.copyFile( nodeDir, graveyard );
        }

        FileUtils.forceDelete( nodeDir );

        stopSpellCheck( node );
    }

    //////////////
    // Spell Check
    //////////////
    public static void stopSpellCheck( Node node ) {
        node.getManuscript().stopSpellCheck();
        node.getDescription().stopSpellCheck();
        node.getSummary().stopSpellCheck();
        node.getNotes().stopSpellCheck();

        for( Node child : node.getChildNodes() ) {
            stopSpellCheck( child );
        }
    }

    ////////
    // Utils
    ////////
    private static File getNodeDirectory( Node node, File projectDirectory )
        throws IOException {
        String nodeDir = node.getUuid();
        File result = new File( projectDirectory, DOCUMENTS_DIRECTORY );
        result = new File( result, nodeDir );
        result.mkdirs();
        return result;
    }

    private static File getNodeFile( Node node, File projectDirectory, String document )
        throws IOException {
        File result = getNodeDirectory( node, projectDirectory );
        result = new File( result, document );
        return result;
    }

    public static File getImageBinary( Node node, File projectDirectory )
        throws IOException {
        File gifFile = getNodeFile( node, projectDirectory, RESOURCE_GIF );
        if( gifFile.exists() ) {
            return gifFile;
        }

        File jpgFile = getNodeFile( node, projectDirectory, RESOURCE_JPG );
        if( jpgFile.exists() ) {
            return jpgFile;
        }

        File pngFile = getNodeFile( node, projectDirectory, RESOURCE_PNG );
        if( pngFile.exists() ) {
            return pngFile;
        }

        return null;
    }

    public static String createImageCode( Node resourceNode ) {
        ImageResource resource = resourceNode.getImageResource();
        if( resource != null ) {
            String title = resourceNode.getTitle();
            StringBuilder result = new StringBuilder( 25 );
            result.append( "![](" );
            result.append( title );
            result.append( ")" );
            return result.toString();
        }

        return "";
    }

    public static void setCreationDate( Project project ) {
        for( Node node : project.getChildNodes() ) {
            setCreationDate( node, new GregorianCalendar() );
        }
    }

    public static void setCreationDate( Node node, GregorianCalendar calendar ) {
        node.setCreatedDate( calendar );
        node.setModifiedDate( calendar );

        for( Node child : node.getChildNodes() ) {
            setCreationDate( child, calendar );
        }
    }

    ////////////////
    // Project Stubs
    ////////////////
    public static Project createDefaultProject() {
        return new EmptyProject().getProject();
    }

    public static Project createDummyProject() {
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
        root.setTitle( messages.untitled() );
        root.setNodeType( NodeTypes.PROJECT );
        root.setCreatedDate( new GregorianCalendar() );
        root.setModifiedDate( new GregorianCalendar() );

        Node manuscript = new Node();
        manuscript.setTitle( messages.manuscript() );
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
        research.setTitle( messages.research() );
        research.setNodeType( NodeTypes.MARKDOWN );
        research.setCreatedDate( new GregorianCalendar() );
        research.setModifiedDate( new GregorianCalendar() );
        project.getChildNodes().add( research );

        Node resources = new Node();
        resources.setTitle( messages.resources() );
        resources.setNodeType( NodeTypes.RESOURCES );
        resources.setCreatedDate( new GregorianCalendar() );
        resources.setModifiedDate( new GregorianCalendar() );
        project.getChildNodes().add( resources );

        Node trash = new Node();
        trash.setTitle( messages.trash() );
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
        throws IOException {
        InputStream inputStream = ProjectIo.class.getResourceAsStream( DEFAULT_STYLE_SHEET );
        String css = IOUtils.toString( inputStream );
        return css;
    }

}
