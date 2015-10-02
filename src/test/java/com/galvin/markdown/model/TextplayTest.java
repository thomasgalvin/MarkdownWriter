package com.galvin.markdown.model;

import com.galvin.markdown.compilers.FountainCompiler;
import galvin.SystemUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jruby.embed.LocalContextScope;
import org.jruby.embed.ScriptingContainer;
import org.junit.Test;

public class TextplayTest
{
    private static final String TEXTPLAY = "/com/galvin/markdown/textplay/textplay.rb";
    private static final String FOUNTAIN_SOURCE = "/com/galvin/markdown/textplay/test.fountain";

    @Test
    public void testTextplay() throws Exception
    {
        ScriptingContainer container = new ScriptingContainer( LocalContextScope.THREADSAFE );
        
        InputStream fountainStream = TextplayTest.class.getResourceAsStream( FOUNTAIN_SOURCE );
        String fountainSource = IOUtils.toString( fountainStream );
        File inputFile = new File( "target/test.fountain" );
        FileUtils.write( inputFile, fountainSource );
        InputStream inputStream = new FileInputStream( inputFile );
        container.setInput( inputStream );
        
        File outputFile = new File( "target/test.html" );
        Writer outputStream = new FileWriter( outputFile );
        container.setOutput( outputStream );
        
        InputStream textplayStream = FountainCompiler.class.getResourceAsStream( TEXTPLAY );
        File rubyFile = new File( SystemUtils.getRandomTempDir(), "textplay.rb" );
        container.runScriptlet( textplayStream, rubyFile.getAbsolutePath() );
    }
}
