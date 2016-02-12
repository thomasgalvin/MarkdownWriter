/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class CompileDialog
    extends JDialog
{

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private FormatsCompileOptionsPanel formatOptions;
    private GeneralCompileOptionsPanel generalOptions;
    private SeparatorsCompileOptionsPanel separatorOptions;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JPanel buttonPanel = new JPanel();
    private JButton cancelButton = new JButton( messages.cancelCompile() );
    private JButton compileButton = new JButton( messages.doCompile() );
    private Controller controller;
    private CompileOptions compileOptions;
    private Project project;

    public CompileDialog( Controller controller )
    {
        this( controller, controller.getProjectFrame().getProject() );
    }
    
    public CompileDialog( Controller controller, Project project )
    {
        setTitle( messages.compileDialogTitle() );
        this.controller = controller;
        this.project = project;
        
        formatOptions = new FormatsCompileOptionsPanel( this );
        generalOptions = new GeneralCompileOptionsPanel( this );
        separatorOptions = new SeparatorsCompileOptionsPanel( this );

        tabbedPane.add( messages.outputCompileOptions(), formatOptions );
        tabbedPane.add( messages.includeCompileOptions(), generalOptions );
        tabbedPane.add( messages.separatorCompileOptions(), separatorOptions );

        GuiUtils.sameSize( new JComponent[] { cancelButton, compileButton } );
        
        buttonPanel.setLayout( new FlowLayout( FlowLayout.RIGHT ) );
        buttonPanel.add( cancelButton );
        buttonPanel.add( new JLabel( " " ) );
        buttonPanel.add( compileButton );
        
        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( tabbedPane, BorderLayout.CENTER );
        getContentPane().add( buttonPanel, BorderLayout.SOUTH );

        setSize( new Dimension( 750, 650 ) );
        GuiUtils.center( this );
        new Listener();
    }

    public void setCompileOptions( CompileOptions compileOptions )
    {
        this.compileOptions = compileOptions;
        if( compileOptions != null )
        {
            formatOptions.loadPreferences( compileOptions );
            generalOptions.loadPreferences( compileOptions );
            separatorOptions.loadPreferences( compileOptions );
        }
    }
    
    public void doCompile()
    {
        writePrefences();
        controller.fileExportProject( compileOptions );
    }
    
    public void updatePreferences()
    {
        formatOptions.updatePreferences();
        generalOptions.updatePreferences();
        separatorOptions.updatePreferences();
    }
    
    public void writePrefences()
    {
        Project targetProject = project;
        if( targetProject == null )
        {
            targetProject = controller.getProjectFrame().getProject();
        }
        compileOptions.setProject(  project );
        formatOptions.writePreferences( compileOptions );
        generalOptions.writePreferences( compileOptions );
        separatorOptions.writePreferences( compileOptions );
        compileOptions.refreshNodes();
        targetProject.setCompileOptions( compileOptions );
    }

    public FormatsCompileOptionsPanel getFormatOptions()
    {
        return formatOptions;
    }

    public GeneralCompileOptionsPanel getGeneralOptions()
    {
        return generalOptions;
    }

    public SeparatorsCompileOptionsPanel getSeparatorOptions() {
        return separatorOptions;
    }

    public void cancel()
    {
        setVisible( false );
        writePrefences();
    }
    
    
    @Override
    public void setVisible( boolean visible )
    {
        if( visible )
        {
            updatePreferences();
        }
        
        super.setVisible( visible );
    }
    
    private class Listener
    implements ActionListener
    {
        public Listener()
        {
            compileButton.addActionListener( this );
            cancelButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent ae )
        {
            Object source = ae.getSource();
            if( source == compileButton )
            {
                setVisible( false );
                doCompile();
            }
            else if( source == cancelButton )
            {
                cancel();
            }
        }
    }
}
