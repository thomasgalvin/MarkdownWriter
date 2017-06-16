/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.compilers.MarkdownCompiler;
import com.galvin.markdown.compilers.CompilerProgressListener;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.swing.CompileThread;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class CompileProgressDialog
    extends JDialog
    implements CompilerProgressListener {
    private CompileThread compileThread;
    private JProgressBar progressBar;
    private JLabel label;

    public CompileProgressDialog( CompileThread compileThread, MarkdownCompiler compiler, CompileOptions compileOptions ) {
        this.compileThread = compileThread;
        MarkdownMessages messages = MarkdownServer.getMessages();

        setTitle( messages.progress() + " " + compileOptions.getProject().getTitle() );
        label = new JLabel( messages.labelProgress() );

        compiler.addListener( this );
        progressBar = new JProgressBar( 0, compileOptions.getProject().getManuscript().getChildNodes().size() );
        progressBar.setIndeterminate( true );

        setLayout( new BorderLayout() );
        getContentPane().add( new LayoutPanel(), BorderLayout.CENTER );
        setSize( 300, 100 );
        GuiUtils.center( this );

        addWindowListener( new WindowAdapter() {
            @Override
            public void windowClosing( WindowEvent e ) {
                try {
                    if( CompileProgressDialog.this.compileThread.isAlive() ) {
                        CompileProgressDialog.this.compileThread.interrupt();
                    }
                }
                catch( Throwable t ) {
                    t.printStackTrace();
                }
            }
        } );
    }

    public void progress( Project project, int complete, int total, MarkdownCompiler source ) {
        progressBar.setValue( complete );
        if( complete == total ) {
            setVisible( false );
        }
    }

    private class LayoutPanel
        extends JPanel {
        public LayoutPanel() {
            add( label );
            add( progressBar );
        }

        @Override
        public void doLayout() {
            Dimension size = getSize();
            Dimension labelSize = GuiUtils.preferredSize( label );
            Dimension progressSize = GuiUtils.preferredSize( progressBar );
            progressSize.width = size.width - GuiUtils.PADDING * 2;
            progressBar.setSize( progressSize );

            int x = GuiUtils.PADDING;
            int y = size.height - labelSize.height - progressSize.height - GuiUtils.PADDING * 4;
            y /= 2;

            label.setLocation( x, y );
            y += labelSize.height + GuiUtils.PADDING;
            progressBar.setLocation( x, y );
        }

    }

}
