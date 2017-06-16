package com.galvin.markdown.swing.editor;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ProjectMetadataWidget
    extends JPanel {

    private static MarkdownMessages messages = MarkdownServer.getMessages();
    private MetadataSelectionComboBox comboBox;
    private MetadataWidget metadataWidget;
    private JScrollPane metadataScrollPane;
    private ProjectCoverPanel coverImagePanel;
    private JScrollPane coverImageScrollPane;
    private StyleSheetPanel styleSheetPanel;
    private JScrollPane styleSheetScrollPane;
    private Controller controller;

    public ProjectMetadataWidget( Controller controller ) {
        this.controller = controller;

        comboBox = new MetadataSelectionComboBox();
        metadataWidget = new MetadataWidget( controller, controller.getProjectFrame().getProject() );
        metadataScrollPane = new JScrollPane( metadataWidget );
        coverImagePanel = new ProjectCoverPanel( controller );
        coverImageScrollPane = new JScrollPane( coverImagePanel );
        styleSheetPanel = new StyleSheetPanel( controller );
        styleSheetScrollPane = new JScrollPane( styleSheetPanel );

        setLayout( new BorderLayout() );
        add( comboBox, BorderLayout.NORTH );
        add( metadataScrollPane, BorderLayout.CENTER );

        new Listener();
    }

    public void update() {
        coverImagePanel.refresh();
        metadataWidget.refresh();
    }

    public void updateSelection() {
        remove( metadataScrollPane );
        remove( coverImageScrollPane );
        remove( styleSheetScrollPane );

        int index = comboBox.getSelectedIndex();
        if( index == 0 ) {
            add( metadataScrollPane );
        }
        else if( index == 1 ) {
            add( coverImageScrollPane );
        }
        else if( index == 2 ) {
            add( styleSheetScrollPane );
        }

        GuiUtils.forceRepaint( this );
    }

    private class MetadataSelectionComboBox
        extends JComboBox {

        public MetadataSelectionComboBox() {
            super( new String[]{
                messages.metadata(),
                messages.coverImage(),
                messages.styleSheet(), } );
        }

    }

    private class Listener
        implements ActionListener {
        public Listener() {
            comboBox.addActionListener( this );
        }

        public void actionPerformed( ActionEvent e ) {
            updateSelection();
        }

    }

}
