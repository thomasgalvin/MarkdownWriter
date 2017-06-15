package com.galvin.markdown.swing.editor;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.preferences.EditorPreferences;
import com.galvin.markdown.preferences.GeneralPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.util.Utils;
import galvin.SystemUtils;
import galvin.swing.GuiUtils;
import galvin.swing.ScaledImage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Random;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class MarkdownEditorPanel
    extends JPanel
    implements FocusListener
{

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private Node node;
    private Project project;
    private ProjectMetadataWidget projectMetadataWidget;
    
    private MetadataWidget projectMetadataEditor;
    private ProjectCoverPanel projectCoverPanel;
    private StyleSheetPanel styleSheetPanel;
    
    private MarkdownEditor editor;
    private ScaledImage imageLabel;
    private StatisticsLabel statisticLabel;
    private MarkdownSectionSelectionComboBox comboBox = new MarkdownSectionSelectionComboBox();
    private ResourceSectionSelectionComboBox resourceComboBox = new ResourceSectionSelectionComboBox();
    private Controller controller;
    
    private Color backgroundColor;
    private Color textColor;

    public MarkdownEditorPanel( Controller controller )
    {
        this.controller = controller;
        setLayout( new BorderLayout() );
        new SectionSelectionActionListener();
        
        Preferences preferences = MarkdownServer.getPreferences();
        EditorPreferences editorPreferences = preferences.getEditorPreferences();
        backgroundColor = editorPreferences.getBackgroundColor();
        textColor = editorPreferences.getTextColor();
        
        setBackground( backgroundColor  );
        setForeground( textColor );
        
        if( !SystemUtils.IS_MAC )
        {
            comboBox.setBackground( backgroundColor  );
            comboBox.setForeground( textColor );
        }
    }

    public void edit( Node node )
    {
        this.node = node;
        this.project = null;

        removeAll();
        if( node != null )
        {
            String nodeType = node.getNodeType();
            if( NodeTypes.RESOURCE.equals( nodeType ) )
            {
                changeSection( resourceComboBox.getNodeSection() );
            }
            else
            {
                changeSection( comboBox.getNodeSection() );
            }
        }
        else
        {
            GuiUtils.forceRepaint( this );
        }
    }

    public void editMetadata( Project project )
    {
        this.project = project;
        this.node = null;

        removeAll();
        if( project != null )
        {
            if( projectMetadataEditor == null )
            {
                projectMetadataEditor = new MetadataWidget( controller, project );
            }

            projectMetadataEditor.refresh();
            add( projectMetadataEditor, BorderLayout.CENTER );
            GuiUtils.forceRepaint( this );
        }
    }
    
    public void editStylesheet( Project project )
    {
        this.project = project;
        this.node = null;

        removeAll();
        if( project != null )
        {
            if( styleSheetPanel == null )
            {
                styleSheetPanel = new StyleSheetPanel( controller );
            }

            //add( styleSheetPanel.getScrollPane(), BorderLayout.CENTER );
            add( new JScrollPane( styleSheetPanel ), BorderLayout.CENTER );
            GuiUtils.forceRepaint( this );
        }
    }
    
    public void editCover( Project project )
    {
        this.project = project;
        this.node = null;

        removeAll();
        if( project != null )
        {
            if( projectCoverPanel == null )
            {
                projectCoverPanel = new ProjectCoverPanel( controller );
            }

            projectCoverPanel.refresh();
            add( projectCoverPanel, BorderLayout.CENTER );
            GuiUtils.forceRepaint( this );
        }
    }
    
    public void edit( Project project )
    {
        this.project = project;
        this.node = null;

        removeAll();
        if( project != null )
        {
            if( projectMetadataWidget == null )
            {
                projectMetadataWidget = new ProjectMetadataWidget( controller );
            }

            projectMetadataWidget.update();
            add( projectMetadataWidget, BorderLayout.CENTER );
            GuiUtils.forceRepaint( this );
        }
    }

    public NodeSection getNodeSection()
    {
        if( node != null )
        {
            String nodeType = node.getNodeType();
            if( NodeTypes.RESOURCE.equals( nodeType ) )
            {
                return resourceComboBox.getNodeSection();
            }
            else
            {
                return comboBox.getNodeSection();
            }
        }

        return NodeSection.MANUSCRIPT;
    }

    public void changeSection( NodeSection nodeSection )
    {
//        if( editor != null )
//        {
//            editor.removeFocusListener( this );
//            remove( editor.getScrollPane() );
//        }
//        editor = null;

        if( statisticLabel != null )
        {
            statisticLabel.remove();
        }
        statisticLabel = null;

        if( imageLabel != null )
        {
            imageLabel.removeFocusListener( this );
            remove( imageLabel );
        }
        imageLabel = null;

        if( node != null )
        {
            removeAll();

            String nodeType = node.getNodeType();
            if( NodeTypes.MANUSCRIPT.equals( nodeType )
                || NodeTypes.RESOURCES.equals( nodeType )
                || NodeTypes.TRASH.equals( nodeType )
                || NodeTypes.PROJECT.equals( nodeType ) )
            {
                GuiUtils.forceRepaint( this );
                return;
            }
            else if( NodeTypes.RESOURCE.equals( nodeType ) )
            {
                add( resourceComboBox, BorderLayout.NORTH );
            }
            else
            {
                add( comboBox, BorderLayout.NORTH );
            }

            MarkdownDocument document = null;
            if( NodeSection.MANUSCRIPT.equals( nodeSection ) )
            {
                document = node.getManuscript();
            }
            else if( NodeSection.DESCRIPTION.equals( nodeSection ) )
            {
                document = node.getDescription();
            }
            else if( NodeSection.SUMMARY.equals( nodeSection ) )
            {
                document = node.getSummary();
            }
            else if( NodeSection.NOTES.equals( nodeSection ) )
            {
                document = node.getNotes();
            }

            if( document != null )
            {
                if( editor == null )
                {
                    editor = new MarkdownEditor( document );
                    editor.addFocusListener( this );
                }
                else
                {
                    editor.setDocument( document );
                }
                
                int selectionStart = document.getSelectionStart();
                
                if( selectionStart != -1 ){
                    editor.setCaretPosition( selectionStart );
                    
                    int selectionEnd = document.getSelectionEnd();
                    if( selectionEnd != -1 ){
                        editor.moveCaretPosition( selectionEnd );
                    }
                }

                statisticLabel = new StatisticsLabel( document );
                statisticLabel.setBackground( backgroundColor  );
                statisticLabel.setForeground( textColor );

                add( editor.getScrollPane(), BorderLayout.CENTER );
                add( statisticLabel, BorderLayout.SOUTH );
                requestFocus();
            }
            else if( NodeSection.IMAGE_RESOURCE.equals( nodeSection ) )
            {
                imageLabel = new ScaledImage( node.getImageResource().getImageIcon() );
                imageLabel.addFocusListener( this );
                add( imageLabel, BorderLayout.CENTER );
            }
            else if( NodeSection.METADATA.equals( nodeSection ) )
            {
                if( node != null )
                {
                    MetadataWidget metadataWidget = new MetadataWidget( controller, node );
                    JScrollPane scrollPane = new JScrollPane( metadataWidget );
                    add( scrollPane, BorderLayout.CENTER );
                    metadataWidget.requestFocus();
                }
            }
        }

        GuiUtils.forceRepaint( this );
    }

    public Node getNode()
    {
        return node;
    }

    public Project getProject()
    {
        return project;
    }

    public MarkdownEditor getEditor()
    {
        return editor;
    }

    @Override
    public void requestFocus()
    {
        if( editor != null )
        {
            editor.requestFocus();
        }
    }

    @Override
    public synchronized void addFocusListener( FocusListener focusListener )
    {
        super.addFocusListener( focusListener );
    }

    public void focusGained( FocusEvent e )
    {
        FocusEvent event = new FocusEvent( this, new Random().nextInt() );

        for( FocusListener listener : getFocusListeners() )
        {
            listener.focusGained( event );
        }
        
        controller.selectCurrentNodeInTree();
    }

    public void focusLost( FocusEvent e )
    {
        FocusEvent event = new FocusEvent( this, new Random().nextInt() );

        for( FocusListener listener : getFocusListeners() )
        {
            listener.focusLost( event );
        }
    }

    private class MarkdownSectionSelectionComboBox
        extends JComboBox
    {

        public MarkdownSectionSelectionComboBox()
        {
            super( new String[]
            {
                MarkdownServer.getMessages().nodeSectionMauscript(),
                MarkdownServer.getMessages().nodeSectionDescription(),
                MarkdownServer.getMessages().nodeSectionSummary(),
                MarkdownServer.getMessages().nodeSectionNotes(),
                MarkdownServer.getMessages().nodeSectionMetadata(),
            } );
        }

        public NodeSection getNodeSection()
        {
            int index = getSelectedIndex();

            if( index == 4 )
            {
                return NodeSection.METADATA;
            }
            else if( index == 3 )
            {
                return NodeSection.NOTES;
            }
            else if( index == 2 )
            {
                return NodeSection.SUMMARY;
            }
            else if( index == 1 )
            {
                return NodeSection.DESCRIPTION;
            }
            else
            {
                return NodeSection.MANUSCRIPT;
            }
        }

        public void setOpposingSelection()
        {
            resourceComboBox.setSelectedIndex( getSelectedIndex() );
        }
    }

    private class ResourceSectionSelectionComboBox
        extends JComboBox
    {

        public ResourceSectionSelectionComboBox()
        {
            super( new String[]
            {
                MarkdownServer.getMessages().nodeSectionImageResource(),
                MarkdownServer.getMessages().nodeSectionDescription(),
                MarkdownServer.getMessages().nodeSectionSummary(),
                MarkdownServer.getMessages().nodeSectionNotes(),
                MarkdownServer.getMessages().nodeSectionMetadata(),
            } );

        }

        public NodeSection getNodeSection()
        {
            int index = getSelectedIndex();

            if( index == 4 )
            {
                return NodeSection.METADATA;
            }
            else if( index == 3 )
            {
                return NodeSection.NOTES;
            }
            else if( index == 2 )
            {
                return NodeSection.SUMMARY;
            }
            else if( index == 1 )
            {
                return NodeSection.DESCRIPTION;
            }
            else
            {
                return NodeSection.IMAGE_RESOURCE;
            }
        }

        public void setOpposingSelection()
        {
            comboBox.setSelectedIndex( getSelectedIndex() );
        }
    }

    private class SectionSelectionActionListener
        implements ActionListener
    {

        public SectionSelectionActionListener()
        {
            comboBox.addActionListener( this );
            resourceComboBox.addActionListener( this );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {

            Object source = e.getSource();
            if( source == comboBox )
            {
                comboBox.setOpposingSelection();
                changeSection( comboBox.getNodeSection() );
            }
            else if( source == resourceComboBox )
            {
                resourceComboBox.setOpposingSelection();
                changeSection( resourceComboBox.getNodeSection() );
            }
        }
    }

    private class StatisticsLabel
        extends JLabel
        implements DocumentListener
    {

        private Document document;
        private int cpw;
        private int wpp;

        public StatisticsLabel( Document document )
        {
            this.document = document;
            document.addDocumentListener( this );

            refresh();
        }

        public void remove()
        {
            document.removeDocumentListener( this );
        }

        public void refresh()
        {
            Preferences preferences = MarkdownServer.getPreferences();
            GeneralPreferences generalPreferences = preferences.getGeneralPreferences();

            cpw = generalPreferences.getCharactersPerWord();
            wpp = generalPreferences.getWordsPerPage();

            long chars = document.getLength();
            long words = Math.max( 1, chars / cpw );
            long pages = Math.max( 1, words / wpp );

            long remainder = words - ( pages * wpp );
            if( remainder > 0 )
            {
                pages++;
            }

            StringBuilder text = new StringBuilder();
            text.append( "<html><center>" );

            if( node != null )
            {
                text.append( node.getTitle() );
                text.append( ": &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" );
            }

            text.append( messages.dialogStatisticsCharacters() );
            text.append( " " );
            text.append( Utils.NUMBER_FORMATTER.format( chars ) );
            text.append( " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" );

            text.append( messages.dialogStatisticsWords() );
            text.append( " " );
            text.append( Utils.NUMBER_FORMATTER.format( words ) );
            text.append( " &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" );

            text.append( messages.dialogStatisticsPages() );
            text.append( " " );
            text.append( Utils.NUMBER_FORMATTER.format( pages ) );
            text.append( "</center></html>" );

            setText( text.toString() );
        }

        public void insertUpdate( DocumentEvent de )
        {
            refresh();
        }

        public void removeUpdate( DocumentEvent de )
        {
            refresh();
        }

        public void changedUpdate( DocumentEvent de )
        {
            if( de.toString().compareTo( "[]" ) != 0 )
            {
                refresh();
            }
        }
    }
}
