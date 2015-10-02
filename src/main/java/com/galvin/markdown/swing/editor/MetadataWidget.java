/**
 Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.editor;

import com.galvin.markdown.model.Contributor;
import com.galvin.markdown.model.IdentifierScheme;
import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.swing.ContributorWidget;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.util.Utils;
import galvin.StringUtils;
import galvin.swing.GuiUtils;
import galvin.dc.LanguageCode;
import galvin.swing.LanguageSelector;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MetadataWidget
    extends JPanel
{

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private static final IdentifierScheme[] IDENTIFIER_SCHEMES = new IdentifierScheme[]
    {
        IdentifierScheme.ISBN, IdentifierScheme.UUID, IdentifierScheme.URI, IdentifierScheme.CUSTOM
    };
    private JLabel titleLabel = new JLabel( messages.metadataWidgetTitle() );
    private JTextField titleField = new JTextField();
    private JLabel subtitleLabel = new JLabel( messages.metadataWidgetSubtitle() );
    private JTextField subtitleField = new JTextField();
    private JLabel createdLabel = new JLabel( messages.metadataWidgetCreatedDate() );
    private JLabel createdValueLabel = new JLabel();
    private JLabel modifiedLabel = new JLabel( messages.metadataWidgetModifiedDate() );
    private JLabel modifiedValueLabel = new JLabel();
    private JLabel languageLabel = new JLabel( messages.metadataWidgetLanguage() );
    private LanguageSelector languageSelector = new LanguageSelector();
    private JLabel identifierLabel = new JLabel( messages.metadataWidgetIdentifier() );
    private JTextField identifierField = new JTextField();
    private JComboBox identifierSchemeComboBox = new JComboBox( IDENTIFIER_SCHEMES );
    private JLabel genresLabel = new JLabel( messages.metadataWidgetGenres() );
    private JTextArea genresTextArea = new JTextArea();
    private JScrollPane genresScrollPane = new JScrollPane( genresTextArea );
    private JLabel topicsLabel = new JLabel( messages.metadataWidgetTopics() );
    private JTextArea topicsTextArea = new JTextArea();
    private JScrollPane topicsScrollPane = new JScrollPane( topicsTextArea );
    private JLabel keywordsLabel = new JLabel( messages.metadataWidgetKeywords() );
    private JTextArea keywordsTextArea = new JTextArea();
    private JScrollPane keywordsScrollPane = new JScrollPane( keywordsTextArea );
    private JLabel contributorsLabel = new JLabel( messages.metadataWidgetContributors() );
    private List<ContributorBundle> contributorBundles = new ArrayList();
    private JButton addContributorButton = new JButton( messages.metadataWidgetAddContributor() );
    private JButton removeContributorButton = new JButton( messages.metadataWidgetRemoveContributor() );
    private JLabel[] nodeLabels = new JLabel[]
    {
        titleLabel, subtitleLabel, createdLabel, createdValueLabel, modifiedLabel, modifiedValueLabel, keywordsLabel, contributorsLabel
    };
    private JLabel[] allLabels = new JLabel[]
    {
        titleLabel, subtitleLabel, createdLabel, createdValueLabel, modifiedLabel, modifiedValueLabel, keywordsLabel, contributorsLabel, languageLabel, identifierLabel, genresLabel, topicsLabel
    };
    private JTextField[] nodeTextFields = new JTextField[]
    {
        titleField, subtitleField
    };
    private JTextField[] allTextFields = new JTextField[]
    {
        titleField, subtitleField, identifierField
    };
    private JTextArea[] nodeTextAreas = new JTextArea[]
    {
        keywordsTextArea
    };
    private JTextArea[] allTextAreas = new JTextArea[]
    {
        keywordsTextArea, genresTextArea, topicsTextArea
    };
    private JScrollPane[] nodeScrollPanes = new JScrollPane[]
    {
        keywordsScrollPane
    };
    private JScrollPane[] allScrollPanes = new JScrollPane[]
    {
        keywordsScrollPane, genresScrollPane, topicsScrollPane
    };
    private JButton[] buttons = new JButton[]
    {
        addContributorButton, removeContributorButton
    };
    private Dimension labelSize;
    private Dimension textFieldSize;
    private Dimension identifierFieldSize;
    private Dimension scrollPaneSize;
    private Dimension contriutorWidgetSize;
    private Dimension languageSelectorSize;
    private Dimension comboBoxSize;
    private Dimension buttonSize;
    private Project project;
    private Node node;
    private boolean fullMetadata;
    private boolean ignoreAllChanges;
    private Controller controller;

    public MetadataWidget( Controller controller, Project project )
    {
        this.controller = controller;
        fullMetadata = true;
        init();
        setProject( project );
    }

    public MetadataWidget( Controller controller, Node node )
    {
        this.controller = controller;
        fullMetadata = false;
        init();
        setNode( node );
    }

    public void refresh()
    {
        setProject( project );
    }

    private void setProject( Project project )
    {
        this.project = project;
        ignoreAllChanges = true;

        titleField.setText( StringUtils.neverNull( project.getTitle() ) );
        subtitleField.setText( StringUtils.neverNull( project.getSubtitle() ) );

        Calendar calendar = project.getCreatedDate();
        if( calendar != null )
        {
            Date date = calendar.getTime();
            createdValueLabel.setText( Utils.DATE_FORMAT.format( date ) );
        }

        calendar = project.getModifiedDate();
        if( calendar != null )
        {
            Date date = calendar.getTime();
            modifiedValueLabel.setText( Utils.DATE_FORMAT.format( date ) );
        }

        IdentifierScheme scheme = project.getIdentifierScheme();
        if( scheme != null )
        {
            identifierSchemeComboBox.setSelectedItem( scheme );
        }
        else
        {
            identifierSchemeComboBox.setSelectedItem( IdentifierScheme.ISBN );
        }
        identifierField.setText( StringUtils.neverNull( project.getIdentifier() ) );

        LanguageCode language = project.getLangauge();
        if( language != null )
        {
            languageSelector.setSelectedItem( language );
        }
        else
        {
            languageSelector.setSelectedItem( LanguageCode.ENGLISH_US );
        }

        keywordsTextArea.setText( StringUtils.cat( project.getKeywords() ) );
        genresTextArea.setText( StringUtils.cat( project.getGenres() ) );
        topicsTextArea.setText( StringUtils.cat( project.getTopics() ) );

        for( ContributorBundle bundle : contributorBundles )
        {
            remove( bundle.widget );
        }

        for( Contributor contributor : project.getContributors() )
        {
            addContributor( contributor );
        }

        ignoreAllChanges = false;
    }

    private void setNode( Node node )
    {
        this.node = node;
        ignoreAllChanges = true;

        titleField.setText( StringUtils.neverNull( node.getTitle() ) );
        subtitleField.setText( StringUtils.neverNull( node.getSubtitle() ) );

        Calendar calendar = node.getCreatedDate();
        if( calendar != null )
        {
            Date date = calendar.getTime();
            createdValueLabel.setText( Utils.DATE_FORMAT.format( date ) );
        }

        calendar = node.getModifiedDate();
        if( calendar != null )
        {
            Date date = calendar.getTime();
            modifiedValueLabel.setText( Utils.DATE_FORMAT.format( date ) );
        }

        keywordsTextArea.setText( StringUtils.cat( node.getKeywords() ) );

        for( Contributor contributor : node.getContributors() )
        {
            addContributor( contributor );
        }

        ignoreAllChanges = false;
    }

    private void init()
    {
        setLayout( null );

        if( fullMetadata )
        {
            labelSize = GuiUtils.sameSize( allLabels );
            scrollPaneSize = GuiUtils.sameSize( allScrollPanes );
            textFieldSize = GuiUtils.sameSize( allTextFields );

            add( languageSelector );
            add( identifierSchemeComboBox );

            for( JComponent component : allLabels )
            {
                add( component );
            }

            for( JComponent component : allScrollPanes )
            {
                add( component );
            }

            for( JComponent component : allTextFields )
            {
                add( component );
            }
        }
        else
        {
            labelSize = GuiUtils.sameSize( nodeLabels );
            scrollPaneSize = GuiUtils.sameSize( nodeScrollPanes );
            textFieldSize = GuiUtils.sameSize( nodeTextFields );

            for( JComponent component : nodeLabels )
            {
                add( component );
            }

            for( JComponent component : nodeScrollPanes )
            {
                add( component );
            }

            for( JComponent component : nodeTextFields )
            {
                add( component );
            }
        }

        languageSelectorSize = GuiUtils.preferredSize( languageSelector );

        identifierFieldSize = new Dimension( textFieldSize.width, textFieldSize.height );
        comboBoxSize = GuiUtils.preferredSize( identifierSchemeComboBox );


        buttonSize = GuiUtils.sameSize( buttons );
        add( addContributorButton );

        doLayout();
        new Listener();
    }

    @Override
    public void doLayout()
    {
        Dimension size = getSize();

        textFieldSize.width = size.width - labelSize.width - GuiUtils.PADDING * 3;
        GuiUtils.setSize( textFieldSize, allTextFields );

        scrollPaneSize.width = size.width - GuiUtils.PADDING * 2;
        scrollPaneSize.height = textFieldSize.height * 3;
        GuiUtils.setSize( scrollPaneSize, allScrollPanes );

        identifierFieldSize.width = textFieldSize.width - comboBoxSize.width - GuiUtils.PADDING;
        identifierField.setSize( identifierFieldSize );

        languageSelectorSize.width = size.width - labelSize.width - GuiUtils.PADDING * 3;
        languageSelector.setSize( languageSelectorSize );

        GuiUtils.preferredSize( createdValueLabel );
        GuiUtils.preferredSize( modifiedValueLabel );

        int labelX = GuiUtils.PADDING;
        int fieldX = labelX + GuiUtils.PADDING + labelSize.width;
        int comboX = fieldX + GuiUtils.PADDING + identifierFieldSize.width;
        int buttonX = size.width - GuiUtils.PADDING - buttonSize.width;
        int y = GuiUtils.PADDING;

        int rowHeight = Math.max( labelSize.height, textFieldSize.height );
        rowHeight = Math.max( rowHeight, comboBoxSize.height );
        rowHeight += GuiUtils.PADDING;

        titleLabel.setLocation( labelX, y );
        titleField.setLocation( fieldX, y );
        y += rowHeight;

        subtitleLabel.setLocation( labelX, y );
        subtitleField.setLocation( fieldX, y );
        y += rowHeight;

        createdLabel.setLocation( labelX, y );
        createdValueLabel.setLocation( fieldX, y );
        y += rowHeight;

        modifiedLabel.setLocation( labelX, y );
        modifiedValueLabel.setLocation( fieldX, y );
        y += rowHeight;

        if( fullMetadata )
        {
            languageLabel.setLocation( labelX, y );
            languageSelector.setLocation( fieldX, y );
            y += languageSelectorSize.height + GuiUtils.PADDING;

            identifierLabel.setLocation( labelX, y );
            identifierField.setLocation( fieldX, y );
            identifierSchemeComboBox.setLocation( comboX, y );
            y += rowHeight;
        }

        keywordsLabel.setLocation( labelX, y );
        y += labelSize.height + GuiUtils.PADDING;
        keywordsScrollPane.setLocation( labelX, y );
        y += scrollPaneSize.height + GuiUtils.PADDING;

        if( fullMetadata )
        {
            genresLabel.setLocation( labelX, y );
            y += labelSize.height + GuiUtils.PADDING;
            genresScrollPane.setLocation( labelX, y );
            y += scrollPaneSize.height + GuiUtils.PADDING;

            topicsLabel.setLocation( labelX, y );
            y += labelSize.height + GuiUtils.PADDING;
            topicsScrollPane.setLocation( labelX, y );
            y += scrollPaneSize.height + GuiUtils.PADDING;
        }

        contributorsLabel.setLocation( labelX, y );
        y += labelSize.height + GuiUtils.PADDING;

        if( !contributorBundles.isEmpty() )
        {
            for( ContributorBundle bundle : contributorBundles )
            {
                if( contriutorWidgetSize == null )
                {
                    contriutorWidgetSize = GuiUtils.preferredSize( bundle.widget );
                    contriutorWidgetSize.width = size.width - GuiUtils.PADDING * 2;
                }

                bundle.widget.setSize( contriutorWidgetSize );
                bundle.widget.setLabelSize( labelSize );
                bundle.widget.setFieldSize( textFieldSize );
                bundle.widget.doLayout();
                bundle.widget.setLocation( labelX, y );
                y += bundle.widget.getSize().height + GuiUtils.PADDING;

                bundle.removeButton.setLocation( buttonX, y );
                y += buttonSize.height + GuiUtils.PADDING;
            }
        }
        contriutorWidgetSize = null;

        addContributorButton.setLocation( buttonX, y );
        y += buttonSize.height + GuiUtils.PADDING;

        setPreferredSize( new Dimension( 500, y ) );
    }

    private class ContributorBundle
    {

        public ContributorWidget widget;
        public JButton removeButton;

        public ContributorBundle( ContributorWidget widget, JButton removeButton )
        {
            this.widget = widget;
            this.removeButton = removeButton;
        }
    }

    private ContributorBundle addContributor()
    {
        ContributorWidget widget = new ContributorWidget();
        removeContributorButton = new JButton( messages.metadataWidgetRemoveContributor() );
        removeContributorButton.setSize( buttonSize );

        add( widget );
        add( removeContributorButton );

        final ContributorBundle result = new ContributorBundle( widget, removeContributorButton );
        contributorBundles.add( result );

        removeContributorButton.addActionListener( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                removeContributor( result );
            }
        } );

        ContributorListener contributorListener = new ContributorListener();
        widget.addNameListener( contributorListener );
        widget.addRoleListener( contributorListener );

        doLayout();
        return result;
    }

    private void removeContributor( ContributorBundle bundle )
    {
        contributorBundles.remove( bundle );
        remove( bundle.widget );
        remove( bundle.removeButton );
        GuiUtils.forceRepaint( this );
        updateMetadata();
    }

    public void addContributor( Contributor contributor )
    {
        ContributorBundle bundle = addContributor();
        bundle.widget.setContributor( contributor );
        updateMetadata();
    }

    public void updateMetadata()
    {
        if( ignoreAllChanges )
        {
            return;
        }

        if( project != null )
        {
            project.setTitle( titleField.getText() );
            project.setSubtitle( subtitleField.getText() );
            project.setLangauge( languageSelector.getSelectedItem() );
            project.setIdentifierScheme( (IdentifierScheme) identifierSchemeComboBox.getSelectedItem() );
            project.setIdentifier( identifierField.getText() );
            project.setKeywords( StringUtils.tokenizeToList( keywordsTextArea.getText(), "\n" ) );
            project.setGenres( StringUtils.tokenizeToList( genresTextArea.getText(), "\n" ) );
            project.setTopics( StringUtils.tokenizeToList( topicsTextArea.getText(), "\n" ) );

            project.getContributors().clear();
            for( ContributorBundle bundle : contributorBundles )
            {
                Contributor contributor = bundle.widget.getContributor();
                if( contributor != null )
                {
                    project.getContributors().add( contributor );
                }

            }

            controller.repaintTree();
        }
        else if( node != null )
        {
            node.setTitle( titleField.getText() );
            node.setSubtitle( subtitleField.getText() );
            node.setKeywords( StringUtils.tokenizeToList( keywordsTextArea.getText(), "\n" ) );

            node.getContributors().clear();
            for( ContributorBundle bundle : contributorBundles )
            {
                Contributor contributor = bundle.widget.getContributor();
                if( contributor != null )
                {
                    node.getContributors().add( contributor );
                }

            }

            controller.repaintTree();
        }
    }

    private class Listener
        implements ActionListener, DocumentListener
    {

        public Listener()
        {
            titleField.getDocument().addDocumentListener( this );
            subtitleField.getDocument().addDocumentListener( this );
            languageSelector.addActionListener( this );
            identifierField.getDocument().addDocumentListener( this );
            identifierSchemeComboBox.addActionListener( this );
            genresTextArea.getDocument().addDocumentListener( this );
            topicsTextArea.getDocument().addDocumentListener( this );
            keywordsTextArea.getDocument().addDocumentListener( this );
            addContributorButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();
            if( source == addContributorButton )
            {
                addContributor();
            }
            else
            {
                updateMetadata();
            }
        }

        public void insertUpdate( DocumentEvent e )
        {
            updateMetadata();
        }

        public void removeUpdate( DocumentEvent e )
        {
            updateMetadata();
        }

        public void changedUpdate( DocumentEvent e )
        {
            if( e.toString().compareTo( "[]" ) != 0 )
            {
                updateMetadata();
            }
        }
    }

    private class ContributorListener
        implements ActionListener, DocumentListener
    {

        public void actionPerformed( ActionEvent e )
        {
            updateMetadata();
        }

        public void insertUpdate( DocumentEvent e )
        {
            updateMetadata();
        }

        public void removeUpdate( DocumentEvent e )
        {
            updateMetadata();
        }

        public void changedUpdate( DocumentEvent e )
        {
            if( e.toString().compareTo( "[]" ) != 0 )
            {
                updateMetadata();
            }
        }
    }
//    public static void main( String[] args )
//    {
//        try
//        {
//            Project project = new Project();
//            project.setTitle( "Title" );
//            project.setSubtitle( "Subtitle" );
//            project.setIdentifierScheme( IdentifierScheme.UUID );
//            project.setIdentifier( UUID.randomUUID().toString() );
//
//            project.getKeywords().add( "Keyword One" );
//            project.getKeywords().add( "Keyword Two" );
//            project.getKeywords().add( "Keyword Three" );
//
//            project.getGenres().add( "New Adult" );
//            project.getGenres().add( "Paranormal" );
//            project.getGenres().add( "Vampires" );
//
//            project.getTopics().add( "Sex" );
//            project.getTopics().add( "Violence" );
//            project.getTopics().add( "Sexy Violence" );
//            project.getTopics().add( "Violent Sex" );
//
//            Node node = new Node();
//            node.setTitle( "Title" );
//            node.setSubtitle( "Subtitle" );
//
//            node.getKeywords().add( "Keyword One" );
//            node.getKeywords().add( "Keyword Two" );
//            node.getKeywords().add( "Keyword Three" );
//
//            //MetadataWidget widget = new MetadataWidget( project );
//            MetadataWidget widget = new MetadataWidget( node );
//
//            ApplicationWindow window = new ApplicationWindow( "Metadata" );
//            window.getContentPane().setLayout( new BorderLayout() );
//            window.getContentPane().add( widget, BorderLayout.CENTER );
//            window.pack();
//            window.placeOnSecondaryScreen();
//            window.setVisible( true );
//        }
//        catch( Throwable t )
//        {
//            t.printStackTrace();
//        }
//    }
}
