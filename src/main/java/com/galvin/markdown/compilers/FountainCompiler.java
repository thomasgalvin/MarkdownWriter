package com.galvin.markdown.compilers;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import galvin.SystemUtils;
import galvin.swing.text.DocumentUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;

public class FountainCompiler
{

    private static final String TEXTPLAY = "/com/galvin/markdown/textplay/textplay.rb";
    private static final String TEXTPLAY_CSS = "/com/galvin/markdown/textplay/textplay.css";

    public static List<CompileResults> compile( CompileOptions compileOptions, MarkdownCompiler compiler )
        throws Exception
    {
        List<CompileResults> result = new ArrayList();
        
        CompileOptions asIsOptions = ProjectIo.clone( compileOptions );
        asIsOptions.setImportFormat( ImportFormat.MARKDOWN );
        asIsOptions.getExportFormats().clear();
        asIsOptions.getExportFormats().add( ExportFormat.AS_IS );
//        asIsOptions.setProject( compileOptions.getProject() );
        
        File workingDir = SystemUtils.getRandomTempDir();
        asIsOptions.setOutputDirectory( workingDir );

        List<CompileResults> asIsResult = compiler.compile( asIsOptions );
        
        if( !asIsResult.isEmpty() )
        {
            File fountainFile = asIsResult.get( 0 ).getFile();
            
            String fountainSource = FileUtils.readFileToString( fountainFile );
            FileUtils.write( fountainFile, fountainSource.trim() );
            File htmlFile = new File( workingDir, "manuscript.html" );
            
            FountainCompiler.fountainExport( fountainFile, htmlFile, false );

            String html = FileUtils.readFileToString( htmlFile ).trim();
            MarkdownDocument htmlDocument = new MarkdownDocument();
            DocumentUtils.setText( htmlDocument, html );

            Node node = new Node();
            node.setManuscript( htmlDocument );
            node.setNotes( htmlDocument );
            node.setSummary( htmlDocument );
            node.setDescription( htmlDocument );

            CompileOptions exportOptions = ProjectIo.clone( compileOptions );
            exportOptions.setIncludeTOC( false );

            String styleSheet = exportOptions.getProject().getStyleSheet();
            
            String hideToc = "div#TOC{ display:none !important; }\n" +
                             "h1.title{ display:none !important; }";
            if( !styleSheet.contains( hideToc ) )
            {
                styleSheet += hideToc + "\n\n";
            }
            
            String textPlayCss = getTextplayCss();
            if( !styleSheet.contains( textPlayCss ) )
            {
                styleSheet += textPlayCss + "\n\n";
            }
            
            exportOptions.getProject().setStyleSheet( styleSheet );

            exportOptions.setImportFormat( ImportFormat.HTML );
            exportOptions.getManuscript().clear();
            exportOptions.getManuscript().add( node );
            
            boolean asIs = false;
            if( exportOptions.getExportFormats().contains( ExportFormat.AS_IS ) )
            {
                asIs = true;
                exportOptions.getExportFormats().remove( ExportFormat.AS_IS );
            }

            List<CompileResults> results =  compiler.compile( exportOptions );
            
            if( asIs )
            {
                results.add( asIsResult.get( 0 ) );
            }
            
            return results;
        }
        else
        {
            for(ExportFormat format : compileOptions.getExportFormats())
            {
                result.add( new CompileResults( format, workingDir ) );
            }
        }

        return result;
    }
    
    
    
    public static void fountainExport( File inputFile, File outputFile, Boolean fdx )
        throws Exception
    {
//        System.out.println( "ruby reading from : " + inputFile );
//        System.out.println( "ruby writing to:    " + outputFile );
//        Desktop.getDesktop().open( inputFile.getParentFile() );
        
        ScriptingContainer container = new ScriptingContainer( LocalContextScope.THREADSAFE );
        
        InputStream inputStream = new FileInputStream( inputFile );
        container.setInput( inputStream );
        
        Writer outputStream = new FileWriter( outputFile );
        container.setOutput( outputStream );
        
//        File errorFile = new File( inputFile.getParentFile(), "error.txt" );        
//        container.setError( new FileWriter( errorFile ) );
        
        InputStream textplayStream = FountainCompiler.class.getResourceAsStream( TEXTPLAY );
        File rubyFile = new File( SystemUtils.getRandomTempDir(), "textplay.rb" );
        container.runScriptlet( textplayStream, rubyFile.getAbsolutePath() );
        
        container.terminate();
    }

    public static String getTextplayCss()
        throws Exception
    {
        InputStream stream = FountainCompiler.class.getResourceAsStream( TEXTPLAY_CSS );
        return IOUtils.toString( stream );
    }
}
