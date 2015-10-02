/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing;

import com.galvin.markdown.model.ProjectIo;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ImageFileFilter
        extends FileFilter
{
    private static final String[] extensions = new String[]
    {
        ".jpg",
        ".jpeg",
        ".png",
        ".gif",
    };

    @Override
    public boolean accept( File file )
    {
        if( file.isDirectory() )
        {
            return true;
        }
        
        for( String extension : extensions )
        {
            if( file.getName().toLowerCase().endsWith( extension ) )
            {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String getDescription()
    {
        return MarkdownServer.getMessages().fileChooserImageFilterDescription();
    }
}