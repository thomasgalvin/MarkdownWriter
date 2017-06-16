package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;


public class OpenProgressDialog
extends JDialog
{
    private JProgressBar progressBar;
    private JLabel label;

    public OpenProgressDialog()
    {
        MarkdownMessages messages = MarkdownServer.getMessages();
        
        setTitle( messages.progress() );
        label = new JLabel( messages.labelProgress() );
        
        progressBar = new JProgressBar();
        progressBar.setIndeterminate( true );
        
        setLayout( new BorderLayout() );
        getContentPane().add( label, BorderLayout.NORTH );
        getContentPane().add( progressBar, BorderLayout.CENTER );
        setSize( 300, 75 );
        GuiUtils.center( this );
    }
}
