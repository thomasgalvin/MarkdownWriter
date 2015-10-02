package com.galvin.markdown.swing;

import galvin.swing.ApplicationWindow;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.net.URL;

public class ProjectWindow
    extends ApplicationWindow
    implements FocusListener
{

    private static final int INITIAL_DIVIDER_LOCATION = 350;
    private static final String FRAME_ICON = "com/galvin/markdown/swing/markdown-writer.png";
    private ProjectFrame projectFrame;

    public ProjectWindow( ProjectFrame projectFrame )
    {
        this.projectFrame = projectFrame;
        setExitOnClose( false );

        addFocusListener( this );

        getContentPane().add( projectFrame.getSplitPane() );
        setSize( 1250, 700 );
        projectFrame.getSplitPane().setDividerLocation( INITIAL_DIVIDER_LOCATION );
        center();

        URL url = ClassLoader.getSystemResource( FRAME_ICON );
        Toolkit kit = Toolkit.getDefaultToolkit();
        Image img = kit.createImage( url );
        setIconImage( img );
    }

    @Override
    public void closeApplicationWindow()
    {
        projectFrame.getController().fileSave();

        if( !projectFrame.getController().fileNeedsSaving() )
        {
            super.closeApplicationWindow();
            projectFrame.unregisterWithMarkdownServer();
            projectFrame.getProject().stopSpellCheck();
        }
    }

    public void closeApplicationWindowWithoutSaving()
    {
        super.closeApplicationWindow();
        projectFrame.unregisterWithMarkdownServerWithoutSaving();
    }

    public void focusGained( FocusEvent e )
    {
        MarkdownServer.setCurrentWindow( this );
    }

    public void focusLost( FocusEvent e )
    {
    }
}
