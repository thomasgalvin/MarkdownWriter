package com.galvin.markdown.swing.menu;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.templates.ProjectTemplate;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class NewProjectMenu
        extends JMenu
{

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private Controller controller;

    public NewProjectMenu( Controller controller )
    {
        setText( messages.menuBarFileNew() );
        this.controller = controller;
        
        for( ProjectTemplate template : MarkdownServer.getProjectTemplates() )
        {
            add( new ProjectTemplateMenuItem( template ) );
        }
    }
    
    private class ProjectTemplateMenuItem
        extends JMenuItem
        implements ActionListener
    {
        private ProjectTemplate template;

        public ProjectTemplateMenuItem( ProjectTemplate template )
        {
            super( template.toString() );
            this.template = template;
            addActionListener( this );
        }

        public void actionPerformed( ActionEvent e )
        {
            MarkdownServer.welcomeNewProject( template );
        }
    }
}
