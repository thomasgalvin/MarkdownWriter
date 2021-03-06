package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.StringUtils;
import galvin.swing.GuiUtils;
import galvin.swing.spell.SpellUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenameDocumentDialog
    extends JDialog {
    private static final Logger logger = LoggerFactory.getLogger( RenameDocumentDialog.class );
    private Controller controller;
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JLabel titleLabel = new JLabel( messages.labelTitle() );
    private JTextField titleTextField = new JTextField();
    private JLabel subTitleLabel = new JLabel( messages.labelSubtitle() );
    private JTextField subTitleTextField = new JTextField();
    private JButton okayButton = new JButton( messages.okay() );
    private JButton cancelButton = new JButton( messages.cancel() );
    private LayoutPanel layoutPanel;
    private Node node;

    public RenameDocumentDialog( Controller controller, Node node ) {
        super( controller.getProjectFrame().getWindow() );
        setTitle( messages.renameDialogTitle() );
        this.controller = controller;
        this.node = node;

        titleTextField.setText( StringUtils.neverNull( node.getTitle() ) );
        subTitleTextField.setText( StringUtils.neverNull( node.getSubtitle() ) );

        titleTextField.selectAll();
        subTitleTextField.selectAll();

        try {
            SpellUtils.setUpSpelling( titleTextField, controller.getProject().getProjectDictionary() );
            SpellUtils.setUpSpelling( subTitleTextField, controller.getProject().getProjectDictionary() );
        }
        catch( IOException ioe ) {
            logger.error( "Error loading project spell dictionary", ioe );
        }

        layoutPanel = new LayoutPanel();
        layoutPanel.doLayout();

        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( layoutPanel );
        getContentPane().setPreferredSize( layoutPanel.getPreferredSize() );

        pack();
        GuiUtils.center( this );

        new Listener();
        GuiUtils.closeOnEscape( this );
    }

    private class Listener
        implements ActionListener {

        public Listener() {
            titleTextField.addActionListener( this );
            subTitleTextField.addActionListener( this );
            okayButton.addActionListener( this );
            cancelButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent e ) {
            Object source = e.getSource();

            if( source == cancelButton ) {
                cancel();
            }
            else if( source == okayButton || source == cancelButton || source == titleTextField || source == subTitleTextField ) {
                rename();
            }
        }

    }

    public void cancel() {
        setVisible( false );
    }

    public void rename() {
        setVisible( false );

        node.setTitle( titleTextField.getText() );
        node.setSubtitle( subTitleTextField.getText() );

        controller.repaintTree();
    }

    private class LayoutPanel
        extends JPanel {

        private Dimension preferredSize;
        private JComponent[] labels = new JComponent[]{
            titleLabel, subTitleLabel
        };
        private JComponent[] fields = new JComponent[]{
            titleTextField, subTitleTextField
        };
        private JComponent[] buttons = new JComponent[]{
            okayButton, cancelButton
        };

        private Dimension labelSize = GuiUtils.sameSize( labels );
        private Dimension fieldSize = GuiUtils.sameSize( fields );
        private Dimension buttonSize = GuiUtils.sameSize( buttons );

        public LayoutPanel() {
            setLayout( null );
            add( titleLabel );
            add( titleTextField );
            add( subTitleLabel );
            add( subTitleTextField );
            add( okayButton );
            add( cancelButton );
        }

        @Override
        public void doLayout() {
            /*
             * Title:    [          ]
             * Subtitle: [          ]
             *           (OK)(Cancel)
             */

            Dimension size = getSize();
            size.width -= GuiUtils.PADDING * 2;
            size.height -= GuiUtils.PADDING * 2;

            fieldSize = GuiUtils.sameSize( fields );
            fieldSize.width = size.width - labelSize.width;
            fieldSize.width -= GuiUtils.PADDING;
            titleTextField.setSize( fieldSize );
            subTitleTextField.setSize( fieldSize );

            int rowHeight = Math.max( labelSize.height, fieldSize.height );

            int labelX = GuiUtils.PADDING;
            int fieldX = GuiUtils.PADDING + labelSize.width + GuiUtils.PADDING;
            int y = GuiUtils.PADDING;

            titleLabel.setLocation( labelX, y );
            titleTextField.setLocation( fieldX, y );

            y += rowHeight;
            y += GuiUtils.PADDING;

            subTitleLabel.setLocation( labelX, y );
            subTitleTextField.setLocation( fieldX, y );

            y += rowHeight;
            y += GuiUtils.PADDING;

            int saveButtonX = ( size.width - buttonSize.width ) + GuiUtils.PADDING;
            int cancelButtonX = saveButtonX - GuiUtils.PADDING - buttonSize.width;
            okayButton.setLocation( saveButtonX, y );
            cancelButton.setLocation( cancelButtonX, y );

            y += buttonSize.height;
            y += GuiUtils.PADDING;

            int width = labelSize.width + 300 + GuiUtils.PADDING * 4;
            int height = y;
            preferredSize = new Dimension( width, height );
        }

        @Override
        public Dimension getPreferredSize() {
            return preferredSize;
        }

    }

}
