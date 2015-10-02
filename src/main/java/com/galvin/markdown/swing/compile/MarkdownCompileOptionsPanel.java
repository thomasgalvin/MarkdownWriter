/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.compile;

import com.galvin.markdown.preferences.MarkdownPreferences;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.preferences.MarkdownPreferencePanel;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

public class MarkdownCompileOptionsPanel
extends AbstractCompileOptionsPanel
{
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private MarkdownPreferencePanel markdownOptions = new MarkdownPreferencePanel( false );
    private JButton setGlobalMakdownPreferences = new JButton( messages.setGlobalMakdownPreferences() );
    private JPanel buttonPanel = new JPanel();
    
    public MarkdownCompileOptionsPanel( CompileDialog compileDialog )
    {
        super( compileDialog );
        setLayout( new BorderLayout() );
        add( markdownOptions, BorderLayout.CENTER );
        
        buttonPanel.setLayout( new BorderLayout() );
        buttonPanel.add( setGlobalMakdownPreferences, BorderLayout.EAST );
        
        add( buttonPanel, BorderLayout.SOUTH );
        
        new Listener();
    }
    
    public void loadGlobalPreferences()
    {
        markdownOptions.loadGlobalPreferences();
    }
    
    public void loadPreferences( MarkdownPreferences preferences )
    {
        markdownOptions.loadPreferences( preferences );
    }
    
    public void writeGlobalPreferences()
    {
        markdownOptions.writeGlobalPreferences();
    }
    
    public void writePreferences( MarkdownPreferences preferences )
    {
        markdownOptions.writePreferences( preferences );
    }
    
    private class Listener
    implements ActionListener
    {
        public Listener()
        {
            setGlobalMakdownPreferences.addActionListener( this );
        }
        
        public void actionPerformed( ActionEvent ae )
        {
            Object source = ae.getSource();
            if( source == setGlobalMakdownPreferences )
            {
                writeGlobalPreferences();
            }
        }
    }
}
