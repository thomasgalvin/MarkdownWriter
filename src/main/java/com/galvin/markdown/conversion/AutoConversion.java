/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.conversion;

import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.swing.ProjectXmlFileFilter;
import java.io.File;

public class AutoConversion
{

//    public static void main( String[] args )
//    {
//        try
//        {
//            String home = System.getProperty( "user.home" );
//            File homeDir = new File( home );
//            process( homeDir );
//            System.exit( 0 );
//        }
//        catch( Throwable t )
//        {
//            t.printStackTrace();
//        }
//    }

    public static void process( File file )
    {
        //System.out.println( "Processing: " + file );
        if( file.isDirectory() )
        {
            File[] children = file.listFiles();
            if( children != null )
            {
                for(File child : children)
                {
                    process( child );
                }
            }
        }
        else
        {
            ProjectXmlFileFilter filter = new ProjectXmlFileFilter( false );
            if( filter.accept( file ) )
            {
                if( !file.getAbsolutePath().contains( "com.apple" )
                    && !file.getAbsolutePath().contains( "/Library" )
                    && !file.getAbsolutePath().contains( "/.Trash" )
                    && !file.getAbsolutePath().contains( "xxx" ))
                {
                    convert( file );
                }
            }
        }
    }

    private static void convert( File file )
    {
        try
        {
            Project project = ProjectIo.readProject( file );
            if( project.isUnsupportedLegacyVersion() )
            {
                System.out.println( "Converting:     " + file );
                File convertedFile = new File( file.getParentFile(), project.getTitle() + ProjectIo.PROJECT_STRUCTURE_DOCUMENT_EXTENSION );
                System.out.println( "     saving as: " + convertedFile );
                ProjectIo.write( project, convertedFile );
                System.out.println( "     conversion complete." );
            }
            project = null;
            System.gc();
            Thread.sleep( 5000 );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
}
