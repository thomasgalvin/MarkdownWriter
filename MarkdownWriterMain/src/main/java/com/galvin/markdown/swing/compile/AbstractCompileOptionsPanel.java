/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.compile;

import javax.swing.JPanel;

public class AbstractCompileOptionsPanel
extends JPanel
{
    private CompileDialog compileDialog;

    public AbstractCompileOptionsPanel( CompileDialog compileDialog )
    {
        this.compileDialog = compileDialog;
    }

    public CompileDialog getCompileDialog()
    {
        return compileDialog;
    }
    
    public void updatePreferences()
    {
    }
}
