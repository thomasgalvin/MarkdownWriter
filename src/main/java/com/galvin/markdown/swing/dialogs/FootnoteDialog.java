/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FootnoteDialog
extends JDialog
{
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JLabel noteNameLabel = new JLabel( messages.dialogFootnoteTitle() );
    private JTextField noteNameField = new JTextField();
    private JLabel note = new JLabel( messages.dialogFootnoteBody() );
    private JTextField body = new JTextField();
    private JButton okButton = new JButton( messages.dialogFootnoteOk() );
    private JButton cancelButton = new JButton( messages.dialogFootnoteCancel() );
    private LayoutPanel layoutPanel = new LayoutPanel();
    private Controller controller;
    
    public FootnoteDialog( Controller controller )
    {
        super( controller.getProjectFrame().getWindow() );
        setTitle( messages.dialogHyperlinkTitle() );
        setModal( true );
        this.controller = controller;
        
        layoutPanel.doLayout();
        
        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( layoutPanel );
        getContentPane().setPreferredSize( layoutPanel.getPreferredSize() );
        pack();
        GuiUtils.center( this );
        
        new Listener();
        GuiUtils.closeOnEscape( this );
    }
    
    public void setText( String text )
    {
        noteNameField.setText( text );
    }
    
    public void setBody( String bodyText )
    {
        body.setText( bodyText );
    }
    
    public void formatInsertFootnote()
    {
        String text = noteNameField.getText();
        String url = body.getText();
        controller.formatInsertFootnote( text, url );
    }
    
    private class Listener
    implements ActionListener
    {
        public Listener()
        {
            okButton.addActionListener( this );
            cancelButton.addActionListener( this );
            noteNameField.addActionListener( this );
            body.addActionListener( this );
        }

        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();
            
            if( source == cancelButton )
            {
                setVisible( false );
            }
            else if( source == okButton || source == noteNameField || source == body )
            {
                setVisible( false );
                formatInsertFootnote();
            }
        }
    }
    
    private class LayoutPanel
            extends JPanel
    {
        private Dimension preferredSize;

        public LayoutPanel()
        {
            setLayout( null );

            add( noteNameLabel );
            add( noteNameField );
            add( note );
            add( body );
            add( okButton );
            add( cancelButton );
        }

        @Override
        public void doLayout()
        {
            Dimension size = getSize();
            size.width -= GuiUtils.PADDING * 2;
            size.height -= GuiUtils.PADDING * 2;

            JComponent[] labels = new JComponent[]
            {
                noteNameLabel, note
            };
            Dimension labelSize = GuiUtils.sameSize( labels );
            
            JComponent[] fields = new JComponent[]
            {
                noteNameField, body
            };
            
            Dimension fieldSize = GuiUtils.sameSize( fields );
            fieldSize.width = size.width - labelSize.width;
            fieldSize.width -= GuiUtils.PADDING;
            noteNameField.setSize( fieldSize );
            body.setSize( fieldSize );
            
            JComponent[] buttons = new JComponent[]
            {
                okButton, cancelButton
            };
            Dimension buttonSize = GuiUtils.sameSize( buttons );
            
            int rowHeight = Math.max(  labelSize.height, fieldSize.height );
            
            int labelX = GuiUtils.PADDING;
            int fieldX = GuiUtils.PADDING + labelSize.width + GuiUtils.PADDING;
            int y = GuiUtils.PADDING;
            
            noteNameLabel.setLocation( labelX, y );
            noteNameField.setLocation( fieldX, y );
            
            y += rowHeight;
            y += GuiUtils.PADDING;
            
            note.setLocation( labelX, y );
            body.setLocation( fieldX, y );
            
            y += rowHeight;
            y += GuiUtils.PADDING;
            
            int saveButtonX = ( size.width - buttonSize.width ) + GuiUtils.PADDING;
            int cancelButtonX = saveButtonX - GuiUtils.PADDING - buttonSize.width;
            okButton.setLocation( saveButtonX, y );
            cancelButton.setLocation( cancelButtonX, y );
            
            y += buttonSize.height;
            y += GuiUtils.PADDING;
            
            int width = labelSize.width + 300 + GuiUtils.PADDING * 4;
            int height = y;
            preferredSize = new Dimension( width, height );
        }
        
        @Override
        public Dimension getPreferredSize()
        {
            return preferredSize;
        }
    }
}
