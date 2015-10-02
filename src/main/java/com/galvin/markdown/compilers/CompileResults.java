package com.galvin.markdown.compilers;

import java.io.File;

public class CompileResults
{
    private ExportFormat format;
    private File file;
    private boolean success = true;

    public CompileResults()
    {
    }

    public CompileResults( ExportFormat format, File file )
    {
        this.format = format;
        this.file = file;
    }

    public File getFile()
    {
        return file;
    }

    public void setFile( File file )
    {
        this.file = file;
    }

    public ExportFormat getFormat()
    {
        return format;
    }

    public void setFormat( ExportFormat format )
    {
        this.format = format;
    }

    public boolean success()
    {
        return success;
    }

    public void setSuccess( boolean success )
    {
        this.success = success;
    }
    
}
