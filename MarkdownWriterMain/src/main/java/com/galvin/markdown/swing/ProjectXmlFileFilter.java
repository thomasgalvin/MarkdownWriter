package com.galvin.markdown.swing;

import com.galvin.markdown.model.ProjectIo;
import java.io.File;

public class ProjectXmlFileFilter 
extends javax.swing.filechooser.FileFilter 
implements java.io.FileFilter
{
    private boolean acceptDirectories;
    
    public ProjectXmlFileFilter()
    {
        this( true );
    }
    
    public ProjectXmlFileFilter( boolean acceptDirectories )
    {
        this.acceptDirectories = acceptDirectories;
    }

    @Override
    public boolean accept( File file )
    {
        if( acceptDirectories && file.isDirectory() )
        {
            return true;
        }
        else if( file.getName().endsWith( ProjectIo.PROJECT_STRUCTURE_DOCUMENT_EXTENSION ) )
        {
            return true;
        }
        
        return false;
    }

    @Override
    public String getDescription()
    {
        return MarkdownServer.getMessages().fileChooserProjectFilterDescription();
    }
}