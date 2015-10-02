/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.dialogs.search.SearchResults;
import com.galvin.markdown.swing.dialogs.search.SearchTool;
import com.galvin.markdown.util.Utils;
import galvin.swing.GuiUtils;
import galvin.swing.text.DocumentSearch;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.text.Document;

public class FindAndReplaceDialog
    extends JDialog
{

    private Controller controller;
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JLabel searchForLabel = new JLabel( messages.findAndReplaceSearchForLabel() );
    private JTextField searchForTextField = new JTextField();
    private JLabel replaceWithLabel = new JLabel( messages.findAndReplaceReplaceWithLabel() );
    private JTextField replaceWithTextField = new JTextField();
    private JButton findNextButton = new JButton( messages.findAndReplaceFindNextButton() );
    private JButton findAllButton = new JButton( messages.findAndReplaceFindAllButton() );
    private JButton replaceAllButton = new JButton( messages.findAndReplaceReplaceAllButton() );
    private JButton cancelButton = new JButton( messages.findAndReplaceCancelButton() );
    private JRadioButton currentDocumentRadioButton = new JRadioButton( messages.findAndReplaceCurrentDocumentRadioButton() );
    private JRadioButton allDocumentsRadioButton = new JRadioButton( messages.findAndReplaceAllDocumentsRadioButton() );
    private JCheckBox onlyDocumentsInManuscriptCheckBox = new JCheckBox( messages.findAndReplaceOnlyDocumentsInManuscriptCheckbox() );
    private JCheckBox manuscriptCheckBox = new JCheckBox( messages.findAndReplaceManuscriptCheckbox() );
    private JCheckBox summaryCheckBox = new JCheckBox( messages.findAndReplaceSummaryCheckbox() );
    private JCheckBox descriptionCheckBox = new JCheckBox( messages.findAndReplaceDescriptionCheckbox() );
    private JCheckBox notesCheckBox = new JCheckBox( messages.findAndReplaceNotesCheckbox() );
    private JCheckBox ignoreCaseCheckBox = new JCheckBox( messages.findAndReplaceIgnoreCaseCheckbox() );
    private ButtonGroup buttonGroup = new ButtonGroup();
    private LayoutPanel layoutPanel = new LayoutPanel();
    private JCheckBox[] multipleDocumentOptions = new JCheckBox[]
    {
        onlyDocumentsInManuscriptCheckBox, manuscriptCheckBox, summaryCheckBox, descriptionCheckBox, notesCheckBox
    };

    public FindAndReplaceDialog( Controller controller )
    {
        super( controller.getProjectFrame().getWindow() );
        this.controller = controller;

        layoutPanel.doLayout();

        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( layoutPanel );
        getContentPane().setPreferredSize( layoutPanel.getPreferredSize() );
        pack();
        GuiUtils.center( this );

        currentDocumentRadioButton.setSelected( true );
        manuscriptCheckBox.setSelected( true );
        summaryCheckBox.setSelected( false );
        descriptionCheckBox.setSelected( false );
        notesCheckBox.setSelected( false );

        currentDocumentOnly();

        new Listener();
        GuiUtils.closeOnEscape( this );
    }

    @Override
    public void setVisible( boolean visible )
    {
        if( visible )
        {
            searchForTextField.selectAll();
            searchForTextField.requestFocus();
            
            replaceWithTextField.selectAll();
        }
        super.setVisible( visible );
    }

    private List<Node> getNodes()
        throws Exception
    {
        Project project = ProjectIo.toProject( controller.getProjectFrame().getTree() );

        List<Node> result = null;

        if( currentDocumentRadioButton.isSelected() )
        {
            Node node = controller.getProjectFrame().getCurrentNode();
            result = new ArrayList();
            result.add( node );
        }
        else if( onlyDocumentsInManuscriptCheckBox.isSelected() )
        {
            Node manuscript = project.getManuscript();
            result = Utils.flatten( manuscript.getChildNodes(), project.getProjectDirectory() );
        }
        else
        {
            result = Utils.flatten( project.getChildNodes(), project.getProjectDirectory() );
        }

        return result;
    }

    private List<Document> getDocuments()
        throws Exception
    {
        List<Document> result = new ArrayList();

        if( currentDocumentRadioButton.isSelected() )
        {
            result.add( controller.getProjectFrame().getEditor().getDocument() );
        }
        else
        {

            List<Node> nodes = getNodes();
            if( nodes != null && !nodes.isEmpty() )
            {
                for(Node node : nodes)
                {
                    if( node != null )
                    {
                        if( manuscriptCheckBox.isSelected() )
                        {
                            if( node.getManuscript() != null )
                            {
                                result.add( node.getManuscript() );
                            }
                        }

                        if( descriptionCheckBox.isSelected() )
                        {
                            if( node.getDescription() != null )
                            {
                                result.add( node.getDescription() );
                            }
                        }

                        if( summaryCheckBox.isSelected() )
                        {
                            if( node.getSummary() != null )
                            {
                                result.add( node.getSummary() );
                            }
                        }

                        if( notesCheckBox.isSelected() )
                        {
                            if( node.getNotes() != null )
                            {
                                result.add( node.getNotes() );
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    public void cancel()
    {
        setVisible( false );
    }

    public void setSearchTerm( String searchTerm )
    {
        searchForTextField.setText( searchTerm );
    }

    public String getSearchTerm()
    {
        return searchForTextField.getText();
    }

    public void setReplaceWith( String replaceWith )
    {
        replaceWithTextField.setText( replaceWith );
    }

    public String getReplaceWith()
    {
        return replaceWithTextField.getText();
    }

    public boolean getIgnoreCase()
    {
        return ignoreCaseCheckBox.isSelected();
    }

    public void setIgnoreCase( boolean ignoreCase )
    {
        ignoreCaseCheckBox.setSelected( ignoreCase );
    }

    public void findNext()
    {
        setVisible( false );
        controller.editFindNext();
    }

    public void findAll()
    {
        try
        {
            setVisible( false );
            SearchResults results = SearchTool.findAll( getSearchTerm(), getIgnoreCase(), getDocuments() );
            SearchResultsDialog dialog = new SearchResultsDialog( controller, results );
            dialog.setVisible( true );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void replaceAll()
    {
        try
        {
            setVisible( false );
            List<Document> documents = getDocuments();
            DocumentSearch.replaceAllPlain( documents,
                                            searchForTextField.getText(),
                                            replaceWithTextField.getText(),
                                            ignoreCaseCheckBox.isSelected() );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void currentDocumentOnly()
    {
        for(JCheckBox checkbox : multipleDocumentOptions)
        {
            checkbox.setEnabled( false );
        }
    }

    private void allDocuments()
    {
        for(JCheckBox checkbox : multipleDocumentOptions)
        {
            checkbox.setEnabled( true );
        }
    }

    private class Listener
        implements ActionListener
    {

        public Listener()
        {
            searchForTextField.addActionListener( this );
            findNextButton.addActionListener( this );
            findAllButton.addActionListener( this );
            replaceAllButton.addActionListener( this );
            cancelButton.addActionListener( this );

            currentDocumentRadioButton.addActionListener( this );
            allDocumentsRadioButton.addActionListener( this );

            buttonGroup.add( currentDocumentRadioButton );
            buttonGroup.add( allDocumentsRadioButton );
        }

        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();

            if( source == cancelButton )
            {
                cancel();
            }
            else if( source == findNextButton || source == searchForTextField )
            {
                findNext();
            }
            else if( source == findAllButton )
            {
                findAll();
            }
            else if( source == replaceAllButton )
            {
                replaceAll();
            }
            else if( source == currentDocumentRadioButton )
            {
                currentDocumentOnly();
            }
            else if( source == allDocumentsRadioButton )
            {
                allDocuments();
            }
        }
    }

    private class LayoutPanel
        extends JPanel
    {

        private Dimension preferredSize;
        private JComponent[] labels = new JComponent[]
        {
            searchForLabel, replaceWithLabel
        };
        private JComponent[] fields = new JComponent[]
        {
            searchForTextField, replaceWithTextField
        };
        private JComponent[] buttons = new JComponent[]
        {
            findNextButton, findAllButton, replaceAllButton, cancelButton
        };
        private JComponent[] checkboxes = new JComponent[]
        {
            onlyDocumentsInManuscriptCheckBox, manuscriptCheckBox, summaryCheckBox, descriptionCheckBox, notesCheckBox, ignoreCaseCheckBox
        };
        private JComponent[] radios = new JComponent[]
        {
            currentDocumentRadioButton, allDocumentsRadioButton
        };
        private Dimension labelSize = GuiUtils.sameSize( labels );
        private Dimension fieldSize = GuiUtils.sameSize( fields );
        private Dimension buttonSize = GuiUtils.sameSize( buttons );
        private Dimension checkboxSize = GuiUtils.sameSize( checkboxes );
        private Dimension radioSize = GuiUtils.sameSize( radios );

        public LayoutPanel()
        {
            setLayout( null );

            add( searchForLabel );
            add( searchForTextField );
            add( replaceWithLabel );
            add( replaceWithTextField );

            add( findNextButton );
            add( findAllButton );
            add( replaceAllButton );
            add( cancelButton );

            add( currentDocumentRadioButton );
            add( allDocumentsRadioButton );
            add( onlyDocumentsInManuscriptCheckBox );

            add( manuscriptCheckBox );
            add( summaryCheckBox );
            add( descriptionCheckBox );
            add( notesCheckBox );
            add( ignoreCaseCheckBox );
        }

        @Override
        public void doLayout()
        {
            /*
            
             Search for:   [________________]
            
             Replace with: [________________]
            
             [] Ignore case
            
             () Current document
             () All documents
             [] Only documents in mansuscript
             [] Manuscript
             [] Summary
             [] Description
             [] Notes
            
             (Cancel) (Replace all) (Final all) (Find)
            
             */

            Dimension size = getSize();
            fieldSize.width = size.width - labelSize.width - GuiUtils.PADDING * 3;
            searchForTextField.setSize( fieldSize );
            replaceWithTextField.setSize( fieldSize );

            int x = GuiUtils.PADDING;
            int y = GuiUtils.PADDING;

            int fieldX = x + labelSize.width + GuiUtils.PADDING;
            int lineHeight = Math.max( labelSize.height, fieldSize.height ) + GuiUtils.PADDING;

            searchForLabel.setLocation( x, y );
            searchForTextField.setLocation( fieldX, y );
            y += lineHeight;

            replaceWithLabel.setLocation( x, y );
            replaceWithTextField.setLocation( fieldX, y );
            y += lineHeight;

            ignoreCaseCheckBox.setLocation( x, y );
            y += checkboxSize.height;
            y += GuiUtils.PADDING * 2;

            currentDocumentRadioButton.setLocation( x, y );
            y += radioSize.height;
            y += GuiUtils.PADDING;

            allDocumentsRadioButton.setLocation( x, y );
            y += radioSize.height;
            y += GuiUtils.PADDING;

            onlyDocumentsInManuscriptCheckBox.setLocation( x, y );
            y += radioSize.height;
            y += GuiUtils.PADDING;

            manuscriptCheckBox.setLocation( x, y );
            y += radioSize.height;
            y += GuiUtils.PADDING;

            descriptionCheckBox.setLocation( x, y );
            y += radioSize.height;
            y += GuiUtils.PADDING;

            summaryCheckBox.setLocation( x, y );
            y += radioSize.height;
            y += GuiUtils.PADDING;

            notesCheckBox.setLocation( x, y );
            y += radioSize.height;
            y += GuiUtils.PADDING;

            y += buttonSize.height + GuiUtils.PADDING;
            preferredSize = new Dimension( 500, y );

            y = size.height - buttonSize.height - GuiUtils.PADDING;

            x = size.width - GuiUtils.PADDING - buttonSize.width;
            findNextButton.setLocation( x, y );
            x -= GuiUtils.PADDING + buttonSize.width;

            findAllButton.setLocation( x, y );
            x -= GuiUtils.PADDING + buttonSize.width;

            replaceAllButton.setLocation( x, y );
            x -= GuiUtils.PADDING + buttonSize.width;

            cancelButton.setLocation( x, y );
        }

        @Override
        public Dimension getPreferredSize()
        {
            return preferredSize;
        }
    }

}
