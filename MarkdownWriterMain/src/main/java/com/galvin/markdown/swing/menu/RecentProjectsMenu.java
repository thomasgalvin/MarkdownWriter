package com.galvin.markdown.swing.menu;

import com.galvin.markdown.model.RecentProject;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.apache.commons.lang3.StringUtils;

public class RecentProjectsMenu
    extends JMenu
{

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private Controller controller;
    private JMenuItem noRecentProjectsItem = new JMenuItem( messages.menuBarFileNoRecentProjects() );
    private JMenuItem clearRecentProjectsItem = new JMenuItem( messages.menuBarFileClearRecentProjects() );

    public RecentProjectsMenu( Controller controller )
    {
        setText( messages.menuBarFileRecentProjects() );
        this.controller = controller;
        noRecentProjectsItem.setEnabled( false );

        clearRecentProjectsItem.addActionListener( new ActionListener()
        {

            public void actionPerformed( ActionEvent ae )
            {
                MarkdownServer.clearRecentProjects();
            }
        } );

        reloadRecentProjects();
    }

    public void reloadRecentProjects()
    {
        removeAll();

        RecentProject[] recentProjects = MarkdownServer.getRecentProjects();
        if( recentProjects.length > 0 )
        {

            add( clearRecentProjectsItem );
            addSeparator();

            for(RecentProject recentProject : recentProjects)
            {
                add( new RecentProjectMenuItem( recentProject ) );
            }
        }
        else
        {
            add( noRecentProjectsItem );
        }
    }

    private class RecentProjectMenuItem
        extends JMenuItem
        implements ActionListener
    {

        private RecentProject recentProject;

        public RecentProjectMenuItem( RecentProject recentProject )
        {
            super( name( recentProject ) );
            
            if( recentProject.getProjectFile() != null ){
                setToolTipText( recentProject.getProjectFile().getAbsolutePath() );
            }
            
            this.recentProject = recentProject;
            addActionListener( this );
        }

        public void actionPerformed( ActionEvent e )
        {
            controller.fileOpen( recentProject.getProjectFile() );
        }
    }

    private static String name( RecentProject recentProject ) {
        String result = "";
        if( recentProject != null &&
            recentProject.getProjectFile() != null )
        {
            result = recentProject.getProjectFile().getName();
        }
        
        if( StringUtils.isBlank( result ) ){
            result = "Untitled";
        }
        
        return result;
    }
}
