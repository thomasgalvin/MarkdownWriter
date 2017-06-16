/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.preferences;

import com.galvin.markdown.preferences.MarkdownPreferences;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MarkdownPreferencePanel
    extends JPanel {
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JLabel pathToPandocLabel = new JLabel( messages.pandocPathLabel() );
    private JTextField pathToPandocField = new JTextField();

    private JLabel pathPdfLatexLabel = new JLabel( messages.pdfLatexPathLabel() );
    private JTextField pathToPdfLatexField = new JTextField();

    private JLabel pathToKindleGenLabel = new JLabel( messages.kindleGenPathLabel() );
    private JTextField pathToKindleGenField = new JTextField();

    private Dimension fieldSize;
    private Dimension labelSize;
    private boolean updateGlobalPreferences = true;

    public MarkdownPreferencePanel( boolean updateGlobalPreferences ) {
        this();
        setUpdateGlobalPreferences( updateGlobalPreferences );
    }

    public MarkdownPreferencePanel() {
        setLayout( null );

        add( pathToPandocLabel );
        add( pathToPandocField );

        add( pathPdfLatexLabel );
        add( pathToPdfLatexField );

        add( pathToKindleGenLabel );
        add( pathToKindleGenField );

        fieldSize = GuiUtils.sameSize( new JComponent[]{ pathToPdfLatexField,
                                                         pathToPandocField,
                                                         pathToKindleGenField } );

        labelSize = GuiUtils.sameSize( new JComponent[]{ pathPdfLatexLabel,
                                                         pathToPandocLabel,
                                                         pathToKindleGenLabel } );

        doLayout();
        loadGlobalPreferences();
        new Listener();
    }

    @Override
    public void doLayout() {
        Dimension size = getSize();
        fieldSize.width = size.width - labelSize.width;
        fieldSize.width -= GuiUtils.PADDING * 3;
        pathToPdfLatexField.setSize( fieldSize );
        pathToPandocField.setSize( fieldSize );
        pathToKindleGenField.setSize( fieldSize );

        int x = GuiUtils.PADDING;
        int fieldX = GuiUtils.PADDING + labelSize.width + GuiUtils.PADDING;
        int y = GuiUtils.PADDING;

        pathToPandocLabel.setLocation( x, y );
        pathToPandocField.setLocation( fieldX, y );
        y += Math.max( fieldSize.height, labelSize.height ) + GuiUtils.PADDING;

        pathPdfLatexLabel.setLocation( x, y );
        pathToPdfLatexField.setLocation( fieldX, y );
        y += Math.max( fieldSize.height, labelSize.height ) + GuiUtils.PADDING;

        pathToKindleGenLabel.setLocation( x, y );
        pathToKindleGenField.setLocation( fieldX, y );
        y += Math.max( fieldSize.height, labelSize.height ) + GuiUtils.PADDING;

        int width = labelSize.width + 250;
        setPreferredSize( new Dimension( width, y ) );
    }

    public boolean updateGlobalPreferences() {
        return updateGlobalPreferences;
    }

    public void setUpdateGlobalPreferences( boolean updateGlobalPreferences ) {
        this.updateGlobalPreferences = updateGlobalPreferences;
    }

    public void loadGlobalPreferences() {
        MarkdownPreferences preferences = MarkdownServer.getPreferences().getMarkdownPreferences();
        loadPreferences( preferences );
    }

    public void loadPreferences( MarkdownPreferences preferences ) {
        pathToPandocField.setText( preferences.getPathToPandoc() );
        pathToPdfLatexField.setText( preferences.getPathToPdfLatex() );
        pathToKindleGenField.setText( preferences.getPathToKindleGen() );
    }

    public void writeGlobalPreferences() {
        MarkdownPreferences preferences = MarkdownServer.getPreferences().getMarkdownPreferences();
        writePreferences( preferences );
        MarkdownServer.writePreferences();
    }

    public void writePreferences( MarkdownPreferences preferences ) {
        preferences.setPathToPandoc( pathToPandocField.getText() );
        preferences.setPathToPdfLatex( pathToPdfLatexField.getText() );
        preferences.setPathToKindleGen( pathToKindleGenField.getText() );
    }

    private class Listener
        implements ActionListener, DocumentListener {
        private Listener() {
            pathToPdfLatexField.getDocument().addDocumentListener( this );
            pathToPandocField.getDocument().addDocumentListener( this );
            pathToKindleGenField.getDocument().addDocumentListener( this );
        }

        public void actionPerformed( ActionEvent ae ) {
            if( updateGlobalPreferences() ) {
                writeGlobalPreferences();
            }
        }

        public void insertUpdate( DocumentEvent de ) {
            if( updateGlobalPreferences() ) {
                writeGlobalPreferences();
            }
        }

        public void removeUpdate( DocumentEvent de ) {
            if( updateGlobalPreferences() ) {
                writeGlobalPreferences();
            }
        }

        public void changedUpdate( DocumentEvent de ) {
            if( de.toString().compareTo( "[]" ) != 0 ) {
                if( updateGlobalPreferences() ) {
                    writeGlobalPreferences();
                }
            }
        }

    }

}
