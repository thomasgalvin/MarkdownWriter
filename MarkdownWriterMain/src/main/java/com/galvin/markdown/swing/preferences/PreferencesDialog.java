/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.preferences;

import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import javax.swing.JDialog;

public class PreferencesDialog
extends JDialog
{
    
    private MarkdownPreferencePanel markdownPreferencePanel = new MarkdownPreferencePanel();
    
    public PreferencesDialog()
    {
        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( markdownPreferencePanel, BorderLayout.CENTER );
        pack();
        GuiUtils.center( this );
    }
}
