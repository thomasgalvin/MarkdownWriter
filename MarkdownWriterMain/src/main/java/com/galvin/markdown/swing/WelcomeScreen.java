package com.galvin.markdown.swing;

import com.galvin.markdown.model.RecentProject;
import com.galvin.markdown.templates.ProjectTemplate;
import galvin.swing.ApplicationWindow;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WelcomeScreen {

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JLabel projectTemplatesLabel = new JLabel( messages.labelProjectTemplates() );
    private JComboBox newProjectsComboBox = new JComboBox( MarkdownServer.getProjectTemplates() );
    private JButton newProjectButton = new JButton( messages.buttonCreateNew() );
    private JLabel recentProjectsLabel = new JLabel( messages.labelRecentProjects() );
    private JButton openRecentProjectButton = new JButton( messages.buttonOpenRecent() );
    private JComboBox recentProjectsComboBox = new JComboBox( MarkdownServer.getRecentProjects() );
    private JButton openProjectButton = new JButton( messages.buttonOpenExisting() );
    private ApplicationWindow window = new ApplicationWindow( messages.application() );
    private Dimension buttonSize = GuiUtils.sameSize( new JComponent[]{
        newProjectButton,
        openProjectButton,
        openRecentProjectButton
    } );
    private Dimension labelSize = GuiUtils.sameSize( new JComponent[]{
        projectTemplatesLabel, recentProjectsLabel
    } );
    private Dimension comboBoxSize = GuiUtils.sameSize( new JComponent[]{
        newProjectsComboBox, recentProjectsComboBox
    } );
    private LayoutPanel layoutPanel = new LayoutPanel();

    public WelcomeScreen() {
        window.getContentPane().setLayout( new BorderLayout() );
        window.getContentPane().add( layoutPanel, BorderLayout.CENTER );
        window.pack();
        window.setWidthPercentageOfScreen( 0.9 );
        window.center();

        openRecentProjectButton.setEnabled( recentProjectsComboBox.getItemCount() > 0 );

        new Listener();
    }

    public void setVisible( boolean visible ) {
        window.setVisible( visible );

        if( visible ) {
            window.toFront();
            window.requestFocus();
        }
    }

    private class LayoutPanel
        extends JPanel {

        public LayoutPanel() {
            setLayout( null );
            add( projectTemplatesLabel );
            add( newProjectsComboBox );
            add( newProjectButton );
            add( openProjectButton );
            add( recentProjectsLabel );
            add( openRecentProjectButton );
            add( recentProjectsComboBox );
            doLayout();
        }

        @Override
        public void doLayout() {
            Dimension size = getSize();

            comboBoxSize.width = size.width;
            comboBoxSize.width -= GuiUtils.PADDING * 3;
            comboBoxSize.width -= buttonSize.width;
            newProjectsComboBox.setSize( comboBoxSize );
            recentProjectsComboBox.setSize( comboBoxSize );

            int x1 = GuiUtils.PADDING;
            int x2 = size.width - buttonSize.width - GuiUtils.PADDING;
            int y = GuiUtils.PADDING;

            projectTemplatesLabel.setLocation( x1, y );
            y += labelSize.height + GuiUtils.PADDING;

            newProjectsComboBox.setLocation( x1, y );
            newProjectButton.setLocation( x2, y );
            y += Math.max( comboBoxSize.height, buttonSize.height ) + GuiUtils.PADDING;

            recentProjectsLabel.setLocation( x1, y );
            y += labelSize.height + GuiUtils.PADDING;

            recentProjectsComboBox.setLocation( x1, y );
            openRecentProjectButton.setLocation( x2, y );
            y += Math.max( comboBoxSize.height, buttonSize.height ) + GuiUtils.PADDING;

            //spacing
            y += labelSize.height + GuiUtils.PADDING;

            openProjectButton.setLocation( x2, y );
            y += buttonSize.height + GuiUtils.PADDING;

            setPreferredSize( new Dimension( 750, y ) );
        }

    }

    private class Listener
        implements ActionListener {

        public Listener() {
            newProjectButton.addActionListener( this );
            openProjectButton.addActionListener( this );
            openRecentProjectButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent e ) {
            new ActionThread( e ).start();
        }

    }

    private class ActionThread
        extends Thread {

        private ActionEvent e;

        public ActionThread( ActionEvent e ) {
            this.e = e;
        }

        @Override
        public void run() {
            Object source = e.getSource();
            if( source == newProjectButton ) {
                ProjectTemplate template = (ProjectTemplate)newProjectsComboBox.getSelectedItem();
                MarkdownServer.welcomeNewProject( template );
            }
            else if( source == openProjectButton ) {
                MarkdownServer.welcomeOpenProject();
            }
            else if( source == openRecentProjectButton ) {
                Object selectedObject = recentProjectsComboBox.getSelectedItem();
                if( selectedObject != null ) {
                    if( selectedObject instanceof RecentProject ) {
                        RecentProject recentProject = (RecentProject)selectedObject;
                        if( recentProject.getProjectFile() != null ) {
                            MarkdownServer.welcomeOpenRecentProject( recentProject.getProjectFile() );
                        }
                    }
                }
            }

            MarkdownServer.checkForOpenProjects();
        }

    }

}
