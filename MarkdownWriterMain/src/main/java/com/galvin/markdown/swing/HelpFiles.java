package com.galvin.markdown.swing;

import galvin.SystemUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public final class HelpFiles
{
    private static final String ABOUT = "about.html";
    private static final String PACKAGE = "com/galvin/markdown/docs/";
    private static final String APPLICATION_HELP = "MarkdownWriterHelp.html";
    private static File helpDirectory;
    private static File about;
    private static File applicationHelp;
    
    private HelpFiles(){}
    
    private static void writeFiles() throws IOException
    {
        if( helpDirectory == null )
        {
            helpDirectory = SystemUtils.getRandomTempDir();
            helpDirectory.mkdirs();
            
            about = new File( helpDirectory, ABOUT );
            InputStream inputStream = HelpFiles.class.getClassLoader().getResourceAsStream( PACKAGE + ABOUT );
            FileOutputStream outputStream = new FileOutputStream( about );
            IOUtils.copy( inputStream, outputStream );
            
            applicationHelp = new File( helpDirectory, APPLICATION_HELP );
            inputStream = HelpFiles.class.getClassLoader().getResourceAsStream( PACKAGE + APPLICATION_HELP );
            outputStream = new FileOutputStream( applicationHelp );
            IOUtils.copy( inputStream, outputStream );
        }
    }
    
    public static File getAbout() throws IOException
    {
        writeFiles();
        return about;
    }
    
    public static File getApplicationHelp() throws IOException
    {
        writeFiles();
        return applicationHelp;
    }
}
