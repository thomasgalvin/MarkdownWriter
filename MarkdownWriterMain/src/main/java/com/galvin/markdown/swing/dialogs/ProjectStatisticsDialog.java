/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.preferences.GeneralPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import com.galvin.markdown.util.Utils;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ProjectStatisticsDialog
    extends JDialog {

    public static final int DEFAULT_CHARACTERS_PER_WORD = 6;
    public static final int DEFAULT_WORDS_PER_PAGE = 250;
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JLabel characterLabel = new JLabel( messages.characterLabel() );
    private JLabel wordsLabel = new JLabel( messages.wordsLabel() );
    private JLabel pagesLabel = new JLabel( messages.pagesLabel() );
    private JLabel charactersPerWordLabel = new JLabel( messages.cpwLabel() );
    private JTextField charactersPerWordField = new JTextField();
    private JLabel wordsPerPageLabel = new JLabel( messages.wppLabel() );
    private JTextField wordsPerPageField = new JTextField();
    private JButton doneButton = new JButton( messages.okay() );
    private JButton refreshButton = new JButton( messages.refresh() );
    private JComponent[] labels = new JComponent[]{
        characterLabel, wordsLabel, pagesLabel, charactersPerWordLabel, wordsPerPageLabel
    };
    private JComponent[] fields = new JComponent[]{
        charactersPerWordField, wordsPerPageField
    };
    private Dimension labelSize = GuiUtils.sameSize( labels );
    private Dimension fieldSize = GuiUtils.sameSize( fields );
    private Dimension buttonSize = GuiUtils.sameSize( new JComponent[]{
        doneButton, refreshButton
    } );
    private Controller controller;

    public ProjectStatisticsDialog( Controller controller ) {
        this.controller = controller;

        setTitle( messages.statisticsDialogTitle() );
        setLayout( new BorderLayout() );
        getContentPane().add( new LayoutPanel(), BorderLayout.CENTER );

        doLayout();
        pack();
        GuiUtils.center( this );

        new Listener();
        GuiUtils.closeOnEscape( this );
    }

    public void loadPreferences() {
        Preferences preferences = MarkdownServer.getPreferences();
        GeneralPreferences generalPreferences = preferences.getGeneralPreferences();

        setCharactersPerWord( generalPreferences.getCharactersPerWord() );
        setWordsPerPage( generalPreferences.getWordsPerPage() );
    }

    public void writePreferences() {
        Preferences preferences = MarkdownServer.getPreferences();
        GeneralPreferences generalPreferences = preferences.getGeneralPreferences();

        int cpw = getCharactersPerWord();
        int wpp = getWordsPerPage();

        generalPreferences.setCharactersPerWord( cpw );
        generalPreferences.setWordsPerPage( wpp );
        MarkdownServer.writePreferences();
    }

    public void refresh() {
        try {
            long characters = 0;

            Project project = controller.getProject();
            List<Node> nodes = Utils.flatten( project.getManuscript().getChildNodes(), project.getProjectDirectory() );

            for( Node node : nodes ) {
                MarkdownDocument document = node.getManuscript();
                characters += document.getLength();
            }

            characterLabel.setText( messages.characterLabel() + " " + Utils.NUMBER_FORMATTER.format( characters ) );

            int cpw = getCharactersPerWord();
            long words = Math.max( 1, characters / cpw );
            wordsLabel.setText( messages.wordsLabel() + " " + Utils.NUMBER_FORMATTER.format( words ) );

            int wpp = getWordsPerPage();
            long pages = Math.max( 1, words / wpp );
            pagesLabel.setText( messages.pagesLabel() + " " + Utils.NUMBER_FORMATTER.format( pages ) );

            writePreferences();

        }
        catch( Throwable t ) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public int getCharactersPerWord() {
        try {
            String cpwText = charactersPerWordField.getText();
            int result = Integer.parseInt( cpwText );
            return result;
        }
        catch( Throwable t ) {
            System.out.println( "Number format exception" );
        }

        return DEFAULT_CHARACTERS_PER_WORD;
    }

    public void setCharactersPerWord( int cpw ) {
        charactersPerWordField.setText( "" + cpw );
    }

    public int getWordsPerPage() {
        try {
            String wppText = wordsPerPageField.getText();
            int result = Integer.parseInt( wppText );
            return result;
        }
        catch( Throwable t ) {
            System.out.println( "Number format exception" );
        }

        return DEFAULT_WORDS_PER_PAGE;
    }

    public void setWordsPerPage( int wpp ) {
        wordsPerPageField.setText( "" + wpp );
    }

    private class LayoutPanel
        extends JPanel {

        public LayoutPanel() {
            setLayout( null );
            add( characterLabel );
            add( wordsLabel );
            add( pagesLabel );
            add( charactersPerWordLabel );
            add( charactersPerWordField );
            add( wordsPerPageLabel );
            add( wordsPerPageField );
            add( doneButton );
            add( refreshButton );

            doLayout();
        }

        @Override
        public void doLayout() {
            Dimension size = getSize();

            labelSize.width = size.width - GuiUtils.PADDING * 2;
            for( JComponent component : labels ) {
                component.setSize( labelSize );
            }

            fieldSize.width = size.width - GuiUtils.PADDING * 2;
            for( JComponent component : fields ) {
                component.setSize( fieldSize );
            }

            int x = GuiUtils.PADDING;
            int y = GuiUtils.PADDING;

            characterLabel.setLocation( x, y );
            y += labelSize.height + GuiUtils.PADDING;

            wordsLabel.setLocation( x, y );
            y += labelSize.height + GuiUtils.PADDING;

            pagesLabel.setLocation( x, y );
            y += labelSize.height + GuiUtils.PADDING;

            //spacing
            y += GuiUtils.PADDING;

            charactersPerWordLabel.setLocation( x, y );
            y += labelSize.height + GuiUtils.PADDING;

            charactersPerWordField.setLocation( x, y );
            y += fieldSize.height + GuiUtils.PADDING;

            wordsPerPageLabel.setLocation( x, y );
            y += labelSize.height + GuiUtils.PADDING;

            wordsPerPageField.setLocation( x, y );
            y += fieldSize.height + GuiUtils.PADDING;

            y += buttonSize.height + GuiUtils.PADDING;
            setPreferredSize( new Dimension( 300, y ) );

            y = size.height - buttonSize.height - GuiUtils.PADDING;
            x = size.width - buttonSize.width - GuiUtils.PADDING;

            doneButton.setLocation( x, y );
            x -= buttonSize.width + GuiUtils.PADDING;

            refreshButton.setLocation( x, y );
        }

    }

    private class Listener
        implements ActionListener, DocumentListener {
        public Listener() {
            doneButton.addActionListener( this );
            refreshButton.addActionListener( this );

            charactersPerWordField.getDocument().addDocumentListener( this );
            wordsPerPageField.getDocument().addDocumentListener( this );
        }

        public void actionPerformed( ActionEvent ae ) {
            Object source = ae.getSource();
            if( source == doneButton ) {
                setVisible( false );
            }
            else if( source == refreshButton ) {
                refresh();
            }
        }

        public void insertUpdate( DocumentEvent de ) {
            writePreferences();
        }

        public void removeUpdate( DocumentEvent de ) {
            writePreferences();
        }

        public void changedUpdate( DocumentEvent de ) {
            if( de.toString().compareTo( "[]" ) != 0 ) {
                writePreferences();
            }
        }

    }

}
