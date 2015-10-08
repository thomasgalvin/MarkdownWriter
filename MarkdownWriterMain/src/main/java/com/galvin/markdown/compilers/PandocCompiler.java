/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.compilers;

import com.galvin.markdown.model.Contributor;
import com.galvin.markdown.model.ContributorRole;
import com.galvin.markdown.model.IdentifierScheme;
import com.galvin.markdown.model.ImageResource;
import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.preferences.MarkdownPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import com.galvin.markdown.util.MimeTypes;
import com.galvin.markdown.util.Utils;
import galvin.StringUtils;
import galvin.SystemUtils;
import galvin.dc.LanguageCode;
import galvin.swing.text.DocumentUtils;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;

public class PandocCompiler
    implements MarkdownCompiler
{

    private static final String RESOURCES = "Resources";
    private static final String COVER_IMAGE_JPEG = "cover.jpg";
    private static final String COVER_IMAGE_GIF = "cover.gif";
    private static final String COVER_IMAGE_PNG = "cover.png";
    private static final String CSS_METADATA_FILE = "styleMetadata.css";
    private static final String CSS_FILE = "style.css";
//    private static final String EPUB_TEMPLATE_LOCATION = "/com/galvin/markdown/templates/pandoc/epub.html";
//    private static final String EPUB_TEMPLATE = "epub.html";
    private List<CompilerProgressListener> listeners = new ArrayList();

    public File getPreview( CompileOptions compileOptions, Node node, NodeSection nodeSection )
        throws Exception
    {
        return getPreview( compileOptions, node, nodeSection, false );
    }

    public File getPreview( CompileOptions compileOptions, Node node, NodeSection nodeSection, boolean includeChildren )
        throws Exception
    {
        return new PreviewGenerator().getPreview( this, compileOptions, node, nodeSection, includeChildren );
    }

    public List<CompileResults> compile( CompileOptions compileOptions )
        throws Exception
    {
        if( ImportFormat.FOUNTAIN.equals( compileOptions.getImportFormat() ) )
        {
            return FountainCompiler.compile( compileOptions, this );
        }
        else
        {
            return pandocCompile( compileOptions );
        }
    }
    
    public List<CompileResults> pandocCompile( CompileOptions compileOptions )
        throws Exception    
    {
        List<CompileResults> results = new ArrayList();

        Project project = compileOptions.getProject();
        ImportFormat importFormat = compileOptions.getImportFormat();

        File outputDir = compileOptions.getOutputDirectory();
        File workingDir = SystemUtils.getRandomTempDir();

        outputDir.mkdirs();
        workingDir.mkdirs();

        List<Node> nodes = Utils.flatten( compileOptions.getManuscript(), compileOptions.getProject().getProjectDirectory() );
        List<File> sourceFiles = new ArrayList();

        List<ExportFormat> exportFormats = new ArrayList();
        exportFormats.addAll( compileOptions.getExportFormats() );


        File cssMetadataFile = new File( workingDir, CSS_METADATA_FILE );
        File cssFile = new File( workingDir, CSS_FILE );
        File coverImageFile = null;
        File metadataFile = null;

        String css = StringUtils.neverNull( DocumentUtils.getText( project.getStyleSheetDocument() ) );
        FileUtils.write( cssMetadataFile, css );
        FileUtils.write( cssFile, Markup.STYLE_START + css + Markup.STYLE_END );
        
        if( exportFormats.contains( ExportFormat.EPUB ) || exportFormats.contains( ExportFormat.MOBI ) )
        {
            coverImageFile = writeCoverFile( project, workingDir );
            metadataFile = writeMetadataFile( project, workingDir );
        }

        StringBuilder asIs = null;
        if( exportFormats.contains( ExportFormat.AS_IS ) )
        {
            asIs = new StringBuilder();
        }

        File workingResourcesDir = new File( workingDir, RESOURCES );
        workingResourcesDir.mkdirs();

        for(int index = 0; index < nodes.size(); index++)
        {
            String fileName = index + importFormat.getExtension();
            File sourceFile = new File( workingDir, fileName );

            Node currentNode = nodes.get( index );
            Node nextNode = null;

            if( index + 1 < nodes.size() )
            {
                nextNode = nodes.get( index + 1 );
            }

            MarkdownDocument document = Utils.getDocument( currentNode, compileOptions.getNodeSection() );
            if( document != null )
            {
                StringBuilder builder = new StringBuilder();
                compileSectionHeader( builder, currentNode, nextNode, compileOptions );

                String body = DocumentUtils.getText( document );
                builder.append( body );
                
                if( !StringUtils.empty( body ) )
                {
                    builder.append( "\n\n" );
                }

                if( NodeTypes.MARKDOWN.equals( currentNode.getNodeType() ) && StringUtils.empty( body ) )
                {
                    //no separator between empty documents
                }
                else
                {
                    String separator = StringUtils.neverNull( getSeparator( compileOptions, currentNode, nextNode ) );
                    builder.append( separator );
                }

                //HtmlEntities.replaceAll( builder );
                //String escaped = StringEscapeUtils.escapeHtml4( builder.toString() );
                FileUtils.write( sourceFile, builder.toString() );
                sourceFiles.add( sourceFile );

                writeResources( project, compileOptions, body, workingDir );

                if( asIs != null )
                {
                    System.out.println( "adding to as-is:\n" + builder.toString() + "\n\n" );
                    asIs.append( builder );
                    asIs.append( "\n\n" );
                }
            }
        }

        int exportCount = exportFormats.size();
        int currentFormat = 0;

        for(ExportFormat exportFormat : exportFormats)
        {
            if( getSupportedFormats().contains( exportFormat ) )
            {
                File workingFile = new File( workingDir, UUID.randomUUID().toString() + exportFormat.getExtension() );
                workingFile.getParentFile().mkdirs();

                File outputFile = new File( outputDir, getFileName( project, compileOptions.getNodeSection(), exportFormat ) );
                if( outputFile.exists() )
                {
                    outputFile.delete();
                }

                if( ExportFormat.WORDPRESS_HTML.equals( exportFormat ) )
                {
                    executePandoc( importFormat, ExportFormat.HTML_SNIPPET, compileOptions, metadataFile, coverImageFile, cssMetadataFile, cssFile, sourceFiles, workingFile );

                    String html = FileUtils.readFileToString( workingFile );
                    StringBuilder wphtml = new StringBuilder( html );
                    StringUtils.replaceAll( wphtml, "<p>", "" );
                    StringUtils.replaceAll( wphtml, "</p>", "\n" );
                    StringUtils.replaceAll( wphtml, "<br/>", "" );
                    StringUtils.replaceAll( wphtml, "<div style='page-break-after: always' ></div>", "<hr/>" );
                    FileUtils.writeStringToFile( workingFile, wphtml.toString() );
                    workingFile.renameTo( outputFile );
                }
                else if( ExportFormat.AS_IS.equals( exportFormat ) )
                {
                    if( asIs != null ){
                        FileUtils.writeStringToFile( workingFile, asIs.toString() );
                        workingFile.renameTo( outputFile );
                    }
                }
                else if( ExportFormat.MOBI.equals( exportFormat ) )
                {
                    File epubFile = new File( workingDir, UUID.randomUUID().toString() + ExportFormat.EPUB.getExtension() );
                    executePandoc( importFormat, ExportFormat.EPUB, compileOptions, metadataFile, coverImageFile, cssMetadataFile, cssFile, sourceFiles, epubFile );
                    executeKindlegen( epubFile, workingFile );
                    workingFile.renameTo( outputFile );
                }
                else if( SystemUtils.IS_MAC && ExportFormat.MICROSOFT_WORD_2003.equals( exportFormat ) )
                {
                    File docxFile = new File( workingDir, UUID.randomUUID().toString() + ExportFormat.MICROSOFT_WORD.getExtension() );
                    executePandoc( importFormat, ExportFormat.MICROSOFT_WORD, compileOptions, metadataFile, coverImageFile, cssMetadataFile, cssFile, sourceFiles, docxFile );
                    executeTextUtil( docxFile, workingFile, exportFormat );
                    workingFile.renameTo( outputFile );
                }
                else
                {
                    executePandoc( importFormat, exportFormat, compileOptions, metadataFile, coverImageFile, cssMetadataFile, cssFile, sourceFiles, workingFile );
                    workingFile.renameTo( outputFile );
                }

                CompileResults result = new CompileResults( exportFormat, outputFile );
                result.setSuccess( outputFile.exists() );
                results.add( result );

                currentFormat++;
                notfiyListeners( project, currentFormat, exportCount, this );
            }
        }

        if( exportFormats.contains( ExportFormat.XHTML )
            || exportFormats.contains( ExportFormat.HTML_SNIPPET )
            || exportFormats.contains( ExportFormat.WORDPRESS_HTML )
            || exportFormats.contains( ExportFormat.RTF )
            || exportFormats.contains( ExportFormat.LATEX )
            || exportFormats.contains( ExportFormat.MARKDOWN )
            || exportFormats.contains( ExportFormat.PLAIN_TEXT ) )
        {

            File resourcesDir = new File( outputDir, RESOURCES );
            resourcesDir.mkdirs();

            File[] workingResources = workingResourcesDir.listFiles();
            if( workingResources != null )
            {
                for(File workingResource : workingResources)
                {
                    File resource = new File( resourcesDir, workingResource.getName() );
                    workingResource.renameTo( resource );
                }
            }
        }

        return results;
    }

    private void writeResources( Project project, CompileOptions compileOptions, String documentBody, File exportDirectory )
        throws Exception
    {
        List<Node> resources = project.getResources().getChildNodes();
        if( resources != null )
        {
            for(Node resource : resources)
            {
                String outputFileName = getExportResourceName( resource );
                if( documentBody.contains( outputFileName ) )
                {
                    File outputFile = new File( exportDirectory, outputFileName );
                    if( !outputFile.exists() )
                    {
                        byte[] bytes = resource.getImageResource().getBytes();
                        org.apache.commons.io.FileUtils.writeByteArrayToFile( outputFile, bytes );
                    }
                }
            }
        }
    }

    public String getExportResourceName( Node resource )
    {
        return RESOURCES + "/" + Utils.getImageName( resource );
    }

    private File writeCoverFile( Project project, File outputDir )
        throws Exception
    {
        File coverImageFile = null;

        Node coverNode = project.getCover();
        if( coverNode != null )
        {
            ImageResource coverImageResource = coverNode.getImageResource();
            if( coverImageResource != null )
            {
                String coverImageName = null;

                if( MimeTypes.MIME_TYPE_JPEG.equals( coverImageResource.getMimeType() ) )
                {
                    coverImageName = COVER_IMAGE_JPEG;
                }
                else if( MimeTypes.MIME_TYPE_GIF.equals( coverImageResource.getMimeType() ) )
                {
                    coverImageName = COVER_IMAGE_GIF;
                }
                else if( MimeTypes.MIME_TYPE_PNG.equals( coverImageResource.getMimeType() ) )
                {
                    coverImageName = COVER_IMAGE_PNG;
                }

                if( coverImageName != null )
                {
                    File resourceDir = new File( outputDir, RESOURCES );
                    resourceDir.mkdirs();

                    coverImageFile = new File( resourceDir, coverImageName );
                    byte[] coverImageBytes = coverNode.getImageResource().getBytes();

                    FileUtils.writeByteArrayToFile( coverImageFile, coverImageBytes );
                }
            }
        }

        return coverImageFile;
    }

    private File writeMetadataFile( Project project, File outputDir )
        throws Exception
    {
        File metadataFile = new File( outputDir, "metadata.xml" );
        MarkdownMessages messages = MarkdownServer.getMessages();

        String title = StringUtils.neverNull( project.getTitle() );
        if( !StringUtils.empty( project.getSubtitle() ) )
        {
            title += " - " + project.getSubtitle();
        }

        if( StringUtils.empty( title ) )
        {
            title = messages.titleUntitledProject();
        }

        StringBuilder metadataSection = new StringBuilder();

        metadataSection.append( "<dc:title>" );
        metadataSection.append( title );
        metadataSection.append( "</dc:title>\n" );

        LanguageCode lang = project.getLangauge();
        if( lang == null )
        {
            lang = LanguageCode.ENGLISH_US;
        }
        metadataSection.append( "<dc:language>" );
        metadataSection.append( lang.getCode() );
        metadataSection.append( "</dc:language>\n" );

        IdentifierScheme identifierScheme = project.getIdentifierScheme();
        if( identifierScheme == null )
        {
            identifierScheme = IdentifierScheme.UUID;
        }

        String identifier = project.getIdentifier();
        if( StringUtils.empty( identifier ) )
        {
            identifier = UUID.randomUUID().toString();
        }

        if( identifierScheme != null && !StringUtils.empty( identifier ) )
        {
            metadataSection.append( "<dc:identifier id=\"BookId\" opf:scheme=\"" );
            metadataSection.append( identifierScheme.getCode() );
            metadataSection.append( "\">" );
            metadataSection.append( identifier );
            metadataSection.append( "</dc:identifier>\n" );
        }

        for(Contributor contributor : project.getContributors())
        {
            metadataSection.append( "<dc:creator opf:file-as=\"" );
            metadataSection.append( contributor.getSortByName() );
            metadataSection.append( "\" opf:role=\"" );
            metadataSection.append( contributor.getRole().getAbbreviation() );
            metadataSection.append( "\">" );
            metadataSection.append( contributor.getName() );
            metadataSection.append( "</dc:creator>\n" );
        }

        FileUtils.writeStringToFile( metadataFile, metadataSection.toString() );

        return metadataFile;
    }

    private File writeTitleBlockFile( Project project, File workingDir )
        throws Exception
    {
        File titleBlockFile = new File( workingDir, "title.txt" );

        StringBuilder contents = new StringBuilder();
        contents.append( "% " );

        String title = StringUtils.neverNull( project.getTitle() );
        if( !StringUtils.empty( project.getSubtitle() ) )
        {
            title += " - " + project.getSubtitle();
        }

        contents.append( "<span class=\"pandoc-auto-title\">" );
        contents.append( title );
        contents.append( "</span>" );
        contents.append( "\n" );

        StringBuilder contributors = new StringBuilder();
        for(Contributor contributor : project.getContributors())
        {
            if( contributors.length() != 0 )
            {
                contributors.append( ";" );
            }
            contributors.append( contributor.getName() );
        }
        contents.append( "% " );
        contents.append( contributors );
        contents.append( "\n\n" );

        FileUtils.writeStringToFile( titleBlockFile, contents.toString() );
        return titleBlockFile;
    }

    private static String getFileName( Project project, NodeSection nodeSection, ExportFormat exportFormat )
    {
        String fileName = project.getTitle();
        if( !NodeSection.MANUSCRIPT.equals( nodeSection ) )
        {
            fileName += " - " + nodeSection.name().toLowerCase();
        }
        fileName = fileName.replaceAll( "\\\\", "_" );
        fileName = fileName.replaceAll( "/", "_" );
        
        fileName += exportFormat.getExtension();
        return fileName;
    }

    private void compileSectionHeader( StringBuilder body, Node node, Node nextNode, CompileOptions compileOptions )
    {
        int level = node.getLevel();
        boolean includeTitle = includeTitle( compileOptions, node );
        if( includeTitle )
        {
            compileTitle( body, level, node, compileOptions );
        }

        boolean includeSubtitle = includeSubtitle( compileOptions, node );
        if( includeSubtitle )
        {
            compileSubtitle( body, level, node, compileOptions );
        }

        boolean includeContributors = compileOptions.includeContributors() && !node.getContributors().isEmpty();
        if( includeContributors )
        {
            compileContributors( body, node, compileOptions );
        }

        if( includeTitle || includeSubtitle || includeContributors )
        {
            if( nextNode != null )
            {
                if( NodeTypes.FOLDER.equals( nextNode.getNodeType() ) )
                {
                    body.append( compileOptions.getSeparatorTitleFolder() );
                }
                else
                {
                    body.append( compileOptions.getSeparatorTitleFile() );
                }
            }
            else
            {
                body.append( compileOptions.getSeparatorTitleFile() );
            }
        }
    }

    private void compileTitle( StringBuilder markdown, int level, Node node, CompileOptions compileOptions )
    {
        String title = node.getTitle();
        if( !StringUtils.empty( title ) )
        {
            for(int i = 0; i <= level; i++)
            {
                markdown.append( Markup.HEADER );
            }

            markdown.append( " " );
            markdown.append( title );
            markdown.append( " " );

            if( node.getNodeType().equals( NodeTypes.FOLDER ) )
            {
                markdown.append( Markup.PANDOC_GENERATED_TITLE_ATTRIBUTES_FOLDER );
            }
            else if( node.getNodeType().equals( NodeTypes.MARKDOWN ) )
            {
                markdown.append( Markup.PANDOC_GENERATED_TITLE_ATTRIBUTES_FILE );
            }
            else
            {
                markdown.append( Markup.PANDOC_GENERATED_TITLE_ATTRIBUTES );
            }

            markdown.append( Markup.PARAGRAPH_BREAK );
        }
    }

    private void compileSubtitle( StringBuilder markdown, int level, Node node, CompileOptions compileOptions )
    {
        String title = node.getSubtitle();
        if( !StringUtils.empty( title ) )
        {
            level++; //we want to go on level deeper for sub-titles
            for(int i = 0; i <= level; i++)
            {
                markdown.append( Markup.HEADER );
            }

            markdown.append( " " );
            markdown.append( title );
            markdown.append( " " );

            if( node.getNodeType().equals( NodeTypes.FOLDER ) )
            {
                markdown.append( Markup.PANDOC_GENERATED_TITLE_ATTRIBUTES_FOLDER );
            }
            else if( node.getNodeType().equals( NodeTypes.MARKDOWN ) )
            {
                markdown.append( Markup.PANDOC_GENERATED_TITLE_ATTRIBUTES_FILE );
            }
            else
            {
                markdown.append( Markup.PANDOC_GENERATED_TITLE_ATTRIBUTES );
            }


            markdown.append( Markup.PARAGRAPH_BREAK );
        }
    }

    private void compileContributors( StringBuilder body, Node node, CompileOptions compileOptions )
    {
        body.append( compileOptions.getNodeContributorMarkup() );

        List<String> contributorNames = new ArrayList();
        for(Contributor contributor : node.getContributors())
        {
            String name = StringUtils.neverNull( contributor.getName() );

            if( compileOptions.includeContributorRoles() )
            {
                ContributorRole role = contributor.getRole();
                if( role != null )
                {
                    name += ", ";
                    name += role.getDisplay();
                }
            }

            contributorNames.add( name );
        }

        body.append( StringUtils.csv( contributorNames ) );
        body.append( compileOptions.getNodeContributorMarkup() );
    }

    private boolean includeTitle( CompileOptions compileOptions, Node node )
    {
        if( NodeTypes.FOLDER.equals( node.getNodeType() ) && compileOptions.includeTitlesOfFolders() )
        {
            return true;
        }
        else if( NodeTypes.MARKDOWN.equals( node.getNodeType() ) && compileOptions.includeTitlesOfFiles() )
        {
            return true;
        }

        return false;
    }

    private boolean includeSubtitle( CompileOptions compileOptions, Node node )
    {
        if( NodeTypes.FOLDER.equals( node.getNodeType() ) && compileOptions.includeSubtitlesOfFolders() )
        {
            return true;
        }
        else if( NodeTypes.MARKDOWN.equals( node.getNodeType() ) && compileOptions.includeSubtitlesOfFiles() )
        {
            return true;
        }

        return false;
    }

    public static String getSeparator( CompileOptions compileOptions, Node currentNode, Node nextNode )
    {
        if( nextNode == null )
        {
            return compileOptions.getEndOfDocumentMarker();
        }

        String currentNodeType = StringUtils.neverNull( currentNode.getNodeType() );
        String nextNodeType = StringUtils.neverNull( nextNode.getNodeType() );

        if( NodeTypes.FOLDER.equals( currentNodeType ) )
        {
            if( NodeTypes.FOLDER.equals( nextNodeType ) )
            {
                return compileOptions.getSeparatorFolderFolder();
            }
            else //assume markdown
            {
                return compileOptions.getSeparatorFolderFile();
            }
        }
        else //assume markdown
        {
            if( NodeTypes.FOLDER.equals( nextNodeType ) )
            {
                return compileOptions.getSeparatorFileFolder();
            }
            else //assume markdown
            {
                return compileOptions.getSeparatorFileFile();
            }
        }
    }

    /////////////////
    // Pandoc command
    /////////////////
    public boolean executePandoc( ImportFormat fromFormat, ExportFormat toFormat, CompileOptions compileOptions, File metadataFile, File coverImageFile, File cssMetadataFile, File cssFile, List<File> sourceFiles, File outputFile )
        throws Exception
    {
        String[] command = getPandocCommand( fromFormat, toFormat, compileOptions, metadataFile, coverImageFile, cssMetadataFile, cssFile, sourceFiles, outputFile );
        Process p = Runtime.getRuntime().exec( command );
        p.waitFor();

        InputStream error = p.getErrorStream();
        String errorMessage = IOUtils.toString( error );
        System.out.println( "pandoc error:" );
        System.out.println( errorMessage );

        InputStream processInput = p.getInputStream();
        String processMessage = IOUtils.toString( processInput );
        System.out.println( "pandoc message:" );
        System.out.println( processMessage );

        return outputFile.exists();
    }

    public static String[] getPandocCommand( ImportFormat fromFormat, ExportFormat toFormat, CompileOptions compileOptions, File metadataFile, File coverImageFile, File cssMetadataFile, File cssFile, List<File> sourceFiles, File outputFile )
        throws Exception
    {
        Preferences preferences = MarkdownServer.getPreferences();
        MarkdownPreferences markdownPreferences = preferences.getMarkdownPreferences();

        List<String> commandSegments = new ArrayList();
        commandSegments.add( markdownPreferences.getPathToPandoc() );

        commandSegments.add( "--ascii" ); //use only ASCI characters (and HTML escape chars for weird stuff)
        commandSegments.add( "--normalize" ); //nom extra spaces, etc
        
        if( !ExportFormat.MARKDOWN.equals( toFormat ) )
        {
            commandSegments.add( "--smart" );
        }

        //standalone
        if( !ExportFormat.HTML_SNIPPET.equals( toFormat )
            && !ExportFormat.WORDPRESS_HTML.equals( toFormat ) )
        {
            commandSegments.add( "--standalone" );
        }

//        if( compileOptions.includeTOC() )
//        {
//            commandSegments.add( "--toc" );
//            commandSegments.add( "--toc-depth=" + compileOptions.getTocDepth() );
//        }

        if( ExportFormat.EPUB.equals( toFormat ) )
        {
            if( cssFile != null )
            {
                commandSegments.add( "--epub-stylesheet=" + cssMetadataFile.getAbsolutePath() );
            }

            if( coverImageFile != null )
            {
                commandSegments.add( "--epub-cover-image=" + coverImageFile.getAbsolutePath() );
            }

            if( metadataFile != null )
            {
                commandSegments.add( "--epub-metadata=" + metadataFile.getAbsolutePath() );
            }

            commandSegments.add( "--epub-chapter-level=" + compileOptions.getEpubChapterLevel() );
        }
        else if( ExportFormat.XHTML.equals( toFormat ) )
        {
            if( cssFile != null )
            {
                commandSegments.add( "-H" );
                commandSegments.add( cssFile.getAbsolutePath() );
            }
        }
        else if( ExportFormat.PDF.equals( toFormat ) )
        {
            commandSegments.add( "--latex-engine=" + markdownPreferences.getPathToPdfLatex() );
        }

        commandSegments.add( "-f" + getPandocFormat( fromFormat ) );
        commandSegments.add( "-o" );
        commandSegments.add( outputFile.getAbsolutePath() );

        for(File sourceFile : sourceFiles)
        {
            commandSegments.add( sourceFile.getAbsolutePath() );
        }

        System.out.println( "Pandoc command: " );
        for(String token : commandSegments)
        {
            System.out.print( token );
            System.out.print( " " );
        }
        System.out.println( "" );

        String[] result = new String[ commandSegments.size() ];
        result = commandSegments.toArray( result );
        return result;
    }

    ////////////
    // KindleGen
    ////////////
    public boolean executeKindlegen( File epubFile, File outputFile )
        throws Exception
    {
        String[] command = getKindleGenCommand( epubFile, outputFile );

        Process p = Runtime.getRuntime().exec( command );
        p.waitFor();

        InputStream error = p.getErrorStream();
        String errorMessage = IOUtils.toString( error );
        System.out.println( "KindleGen error:" );
        System.out.println( errorMessage );

        InputStream processInput = p.getInputStream();
        String processMessage = IOUtils.toString( processInput );
        System.out.println( "KindleGen message:" );
        System.out.println( processMessage );

        return outputFile.exists();
    }

    public static String[] getKindleGenCommand( File epubFile, File outputFile )
        throws Exception
    {
        Preferences preferences = MarkdownServer.getPreferences();
        MarkdownPreferences markdownPreferences = preferences.getMarkdownPreferences();

        String epubFilePath = epubFile.getAbsolutePath();
        String outputFilePath = outputFile.getName();

        List<String> commandSegments = new ArrayList();
        commandSegments.add( markdownPreferences.getPathToKindleGen() );
        commandSegments.add( epubFilePath );
        commandSegments.add( "-c2" );
        commandSegments.add( "-verbose" );
        commandSegments.add( "-o" );
        commandSegments.add( outputFilePath );

        for(String command : commandSegments)
        {
            System.out.print( command );
            System.out.print( " " );
        }
        System.out.println();

        String[] result = new String[ commandSegments.size() ];
        result = commandSegments.toArray( result );
        return result;
    }

    ///////////////
    // Mac TextUtil
    ///////////////
    public boolean executeTextUtil( File docxFile, File outputFile, ExportFormat format )
        throws Exception
    {
        if( SystemUtils.IS_MAC )
        {
            String[] commandSegments = getTextUtilCommand( docxFile, outputFile, format );
            Process p = Runtime.getRuntime().exec( commandSegments );
            p.waitFor();

            InputStream error = p.getErrorStream();
            String errorMessage = IOUtils.toString( error );
            System.out.println( "textutil error:" );
            System.out.println( errorMessage );

            InputStream processInput = p.getInputStream();
            String processMessage = IOUtils.toString( processInput );
            System.out.println( "textutil message:" );
            System.out.println( processMessage );

            return outputFile.exists();
        }
        else
        {
            MarkdownMessages messages = MarkdownServer.getMessages();
            String error = messages.errorExportFormatOnlySupportedOnMacPlaceholder();
            error = StringUtils.replaceAll( error, "${format}", format.getName() );
            throw new UnsupportedOperationException( error );
        }
    }

    public String[] getTextUtilCommand( File xhtmlFile, File outputFile, ExportFormat format )
    {
        String extension = format.getExtension().substring( 1 );

        String[] commandSegments = new String[]
        {
            "textutil",
            "-convert",
            extension,
            "-output",
            outputFile.getAbsolutePath(),
            xhtmlFile.getAbsolutePath(),
        };

        for(String command : commandSegments)
        {
            System.out.print( command );
            System.out.print( " " );
        }
        System.out.println();

        return commandSegments;
    }

    //////////
    // Formats
    //////////
    public List<ExportFormat> getSupportedFormats()
    {
        ExportFormat[] formats = new ExportFormat[]
        {
            ExportFormat.MARKDOWN,
            ExportFormat.AS_IS,
            ExportFormat.XHTML,
            ExportFormat.WORDPRESS_HTML,
            ExportFormat.HTML_SNIPPET,
            ExportFormat.EPUB,
            ExportFormat.PDF,
            ExportFormat.RTF,
            ExportFormat.PLAIN_TEXT,
            ExportFormat.MICROSOFT_WORD,
            ExportFormat.ODT,
        };

        List<ExportFormat> result = new ArrayList( Arrays.asList( formats ) );

        MarkdownPreferences markdownPreferences = MarkdownServer.getPreferences().getMarkdownPreferences();

        if( markdownPreferences.kindleGenSupported() )
        {
            result.add( ExportFormat.MOBI );
        }

        if( markdownPreferences.pdfLatexSupported() )
        {
            result.add( ExportFormat.LATEX );
        }

        if( SystemUtils.IS_MAC )
        {
            result.add( ExportFormat.MICROSOFT_WORD_2003 );
        }


        return result;
    }

    public static String getPandocFormat( ImportFormat export )
        throws Exception
    {
        if( ImportFormat.MARKDOWN.equals( export ) )
        {
            return "markdown";
        }
        else if( ImportFormat.MARKDOWN_STRICT.equals( export ) )
        {
            return "markdown_strict";
        }
        else if( ImportFormat.MARKDOWN_PHP_EXTRA.equals( export ) )
        {
            return "markdown_phpextra";
        }
        else if( ImportFormat.MARKDOWN_GITHUB.equals( export ) )
        {
            return "markdown_github";
        }
        else if( ImportFormat.MARKDOWN_MMD.equals( export ) )
        {
            return "markdown_mmd";
        }
        else if( ImportFormat.HTML.equals( export ) )
        {
            return "html";
        }
        else if( ImportFormat.LATEX.equals( export ) )
        {
            return "latex";
        }

        throw new Exception( "Import format: " + export.getName() + " is not suported." );
    }

    public static String getPandocFormat( ExportFormat export )
        throws Exception
    {
        if( ExportFormat.MARKDOWN.equals( export ) )
        {
            return "markdown";
        }
        else if( ExportFormat.XHTML.equals( export ) )
        {
            return "html";
        }
        if( ExportFormat.HTML_SNIPPET.equals( export ) )
        {
            return "html";
        }
        else if( ExportFormat.RTF.equals( export ) )
        {
            return "rtf";
        }
        else if( ExportFormat.PLAIN_TEXT.equals( export ) )
        {
            return "plain";
        }
        else if( ExportFormat.MICROSOFT_WORD.equals( export ) )
        {
            return "docx";
        }
        else if( ExportFormat.PDF.equals( export ) )
        {
            return "pdf";
        }
        else if( ExportFormat.EPUB.equals( export ) )
        {
            return "epub";
        }
        else if( ExportFormat.LATEX.equals( export ) )
        {
            return "latex";
        }

        throw new Exception( "Export format: " + export.getName() + " is not suported." );
    }

    ////////////
    // Listeners
    ////////////
    public void addListener( CompilerProgressListener listenener )
    {
        listeners.add( listenener );
    }

    public void removeListener( CompilerProgressListener listenener )
    {
        listeners.remove( listenener );
    }

    public void notfiyListeners( Project project, int complete, int total, MarkdownCompiler source )
    {
        int count = listeners.size();
        for(int i = count - 1; i >= 0; i--)
        {
            listeners.get( i ).progress( project, complete, total, source );
        }
    }
}
