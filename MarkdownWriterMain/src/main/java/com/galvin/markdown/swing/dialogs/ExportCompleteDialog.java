/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.compilers.CompileResults;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ExportCompleteDialog
    extends JDialog
{

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JLabel successLabel = new JLabel();
    private JButton okayButton = new JButton( messages.success() );
    private JButton openFolderButton = new JButton( messages.openExportFolder() );
    private LayoutPanel layoutPanel;
    private File exportDirectory;
    private List<CompileResults> results;

    public ExportCompleteDialog( List<CompileResults> results )
    {
        setModal( true );
        
        this.results = results;
        this.exportDirectory = results.get( 0 ).getFile().getParentFile();
        
        StringBuilder builder = new StringBuilder();
        builder.append( "<html>" );
        builder.append( messages.exportCompleteLabel() );
        builder.append( "<br><ul>" );
        
        for( CompileResults result : results )
        {
            builder.append( "<li>" );
            builder.append( result.getFormat().getName() );
            builder.append( ": " );
            
            boolean success = result.success();
            if( success )
            {
                builder.append( "<font color='green'>" );
                builder.append( messages.successLabel() );
                builder.append( "</font>" );
            }
            else
            {
                builder.append( "<font color='red'>" );
                builder.append( messages.failureLabel() );
                builder.append( "</font>" );
            }
            builder.append( "</li>" );
        }
        
        builder.append( "</ul></html>" );
        successLabel.setText( builder.toString() );
        
        layoutPanel = new LayoutPanel();
        
        setTitle( messages.success() );
        setLayout( new BorderLayout() );
        add( layoutPanel, BorderLayout.CENTER );
        pack();
        GuiUtils.center( this );
        
        new Listener();
        GuiUtils.closeOnEscape( this );
    }

    public File getExportDirectory()
    {
        return exportDirectory;
    }

    public List<CompileResults> getResults()
    {
        return results;
    }

    private class LayoutPanel
        extends JPanel
    {

        private Dimension labelSize = GuiUtils.preferredSize( successLabel );
        private Dimension buttonSize = GuiUtils.sameSize( new JComponent[]
            {
                openFolderButton, okayButton
            } );

        public LayoutPanel()
        {
            setLayout( null );
            add( successLabel );
            add( openFolderButton );
            add( okayButton );

            doLayout();
        }

        @Override
        public void doLayout()
        {
            Dimension size = getSize();
            int x = GuiUtils.PADDING;
            int y = GuiUtils.PADDING;

            successLabel.setLocation( x, y );
            y += labelSize.height;
            y += GuiUtils.PADDING;

            okayButton.setLocation( x, y );
            x = size.width;
            x -= GuiUtils.PADDING;
            x = buttonSize.width;

            openFolderButton.setLocation( x, y );

            y += buttonSize.height;
            y += GuiUtils.PADDING;

            int width = Math.max( labelSize.width,
                                  buttonSize.width * 2 + GuiUtils.PADDING );
            setPreferredSize( new Dimension( width, y ) );
        }
    }

    private class Listener
        implements ActionListener
    {

        public Listener()
        {
            okayButton.addActionListener( this );
            openFolderButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent ae )
        {
            setVisible( false );

            Object source = ae.getSource();
            if( source == openFolderButton && exportDirectory != null )
            {
                if( Desktop.isDesktopSupported() )
                {
                    try
                    {
                        Desktop.getDesktop().open( exportDirectory );
                    }
                    catch( Throwable t )
                    {
                        t.printStackTrace();
                    }
                }
            }
        }
    }

}
