/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.StringUtils;
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

public class HyperlinkEditorDialog
extends JDialog
{
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JLabel textLabel = new JLabel( messages.dialogHyperlinkText() );
    private JTextField textField = new JTextField();
    private JLabel urlLabel = new JLabel( messages.dialogHyperlinkUrl() );
    private JTextField urlField = new JTextField();
    private JButton okButton = new JButton( messages.dialogHyperlinkOk() );
    private JButton cancelButton = new JButton( messages.dialogHyperlinkCancel() );
    private LayoutPanel layoutPanel = new LayoutPanel();
    private Controller controller;
    
    public HyperlinkEditorDialog( Controller controller )
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
        textField.setText( text );
    }
    
    public void setUrl( String url )
    {
        urlField.setText( url );
    }
    
    @Override
    public void setVisible( boolean visible )
    {
        if( visible )
        {
            if( !StringUtils.empty( textField.getText() ) )
            {
                urlField.requestFocus();
            }
            
            textField.selectAll();
            urlField.selectAll();
        }
        super.setVisible( visible );
    }
    
    public void insertLink()
    {
        String text = textField.getText();
        String url = urlField.getText();
        controller.formatInsertLink( text, url );
    }
    
    private class Listener
    implements ActionListener
    {
        public Listener()
        {
            okButton.addActionListener( this );
            cancelButton.addActionListener( this );
            textField.addActionListener( this );
            urlField.addActionListener( this );
        }

        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();
            
            if( source == cancelButton )
            {
                setVisible( false );
            }
            else if( source == okButton || source == textField || source == urlField )
            {
                setVisible( false );
                insertLink();
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

            add( textLabel );
            add( textField );
            add( urlLabel );
            add( urlField );
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
                textLabel, urlLabel
            };
            Dimension labelSize = GuiUtils.sameSize( labels );
            
            JComponent[] fields = new JComponent[]
            {
                textField, urlField
            };
            
            Dimension fieldSize = GuiUtils.sameSize( fields );
            fieldSize.width = size.width - labelSize.width;
            fieldSize.width -= GuiUtils.PADDING;
            textField.setSize( fieldSize );
            urlField.setSize( fieldSize );
            
            JComponent[] buttons = new JComponent[]
            {
                okButton, cancelButton
            };
            Dimension buttonSize = GuiUtils.sameSize( buttons );
            
            int rowHeight = Math.max(  labelSize.height, fieldSize.height );
            
            int labelX = GuiUtils.PADDING;
            int fieldX = GuiUtils.PADDING + labelSize.width + GuiUtils.PADDING;
            int y = GuiUtils.PADDING;
            
            textLabel.setLocation( labelX, y );
            textField.setLocation( fieldX, y );
            
            y += rowHeight;
            y += GuiUtils.PADDING;
            
            urlLabel.setLocation( labelX, y );
            urlField.setLocation( fieldX, y );
            
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
