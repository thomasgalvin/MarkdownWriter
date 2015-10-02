/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;
import com.galvin.markdown.swing.preferences.PreferencesDialog;
import java.io.File;

public class MarkdownApplicationAdapter
extends ApplicationAdapter
{

    @Override
    public void handleAbout( ApplicationEvent ae )
    {
        MarkdownServer.helpAbout();
        ae.setHandled( true );
    }

    @Override
    public void handleOpenFile( ApplicationEvent ae )
    {
        System.out.println( "Handling openFile" );
        String fileName = ae.getFilename();
        System.out.println( "    " + fileName );
        MarkdownServer.fileOpen( new File( fileName ) );
        ae.setHandled( true );
    }

    @Override
    public void handlePreferences( ApplicationEvent ae )
    {
        PreferencesDialog preferencesDialog = MarkdownServer.getPreferencesDialog();
        preferencesDialog.setVisible( true );
        ae.setHandled( true );
    }

    @Override
    public void handleQuit( ApplicationEvent ae )
    {
        MarkdownServer.exit();
        ae.setHandled( true );
    }
}
