/**
  Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
*/

package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.ImportFormat;
import javax.swing.JComboBox;


public class ImportFormatComboBox
extends JComboBox
{
    public ImportFormatComboBox()
    {
        super( ImportFormat.FORMATS );
    }
    
    public ImportFormat getSelectedFormat()
    {
        return getSelectedItem();
    }
    
    public void setSelectedFormat( ImportFormat format )
    {
        if( format != null )
        {
            setSelectedItem( format );
        }
        else
        {
            setSelectedItem( ImportFormat.MARKDOWN );
        }
    }
    
    @Override
    public ImportFormat getSelectedItem()
    {
        Object result = super.getSelectedItem();
        if( result != null && result instanceof ImportFormat )
        {
            return (ImportFormat)result;
        }
        
        System.out.println( "Something went wrong; using default import format" );
        return ImportFormat.MARKDOWN;
    }
}
