package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.ExportFormat;
import java.util.List;
import javax.swing.JCheckBox;

public class ExportFormatCheckBox
        extends JCheckBox
{

    private ExportFormat exportFormat;

    public ExportFormatCheckBox( ExportFormat exportFormat )
    {
        super( exportFormat.getName() );
        this.exportFormat = exportFormat;
    }

    public ExportFormat getExportFormat()
    {
        return exportFormat;
    }
    
    public void setEnabled( List<ExportFormat> exportFormats )
    {
        setEnabled( exportFormats.contains( exportFormat ) );
    }
    
    public void setSelected( List<ExportFormat> exportFormats )
    {
        setSelected( exportFormats.contains( exportFormat ) );
    }
}