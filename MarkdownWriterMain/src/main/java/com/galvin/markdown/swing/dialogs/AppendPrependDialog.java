package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import static galvin.StringUtils.neverNull;
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

public class AppendPrependDialog extends JDialog {
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private Controller controller;
    
    private JLabel prependLabel = new JLabel( messages.prependToLinesLabel() );
    private JTextField prepend = new JTextField();
    
    private JLabel appendLabel = new JLabel( messages.appendToLinesLabel() );
    private JTextField append = new JTextField();
    
    private JButton okButton = new JButton( messages.okay() );
    private JButton cancelButton = new JButton( messages.cancel() );
    
    private LayoutPanel layoutPanel = new LayoutPanel();

    public AppendPrependDialog( Controller controller ) {
        super( controller.getProjectFrame().getWindow() );
        this.controller = controller;
        setTitle( messages.appendPrependDialogTitle() );
        setModal( true );
        
        layoutPanel.doLayout();
        
        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( layoutPanel );
        getContentPane().setPreferredSize( layoutPanel.getPreferredSize() );
        pack();
        GuiUtils.center( this );
        
        new Listener();
        GuiUtils.closeOnEscape( this );
    }
    
    public void doAppendPrepend(){
        String prependText = neverNull( prepend.getText() );
        String appendText = neverNull( append.getText() );
        controller.bookendSelectedLines( prependText, appendText );
    }
    
    private class Listener
    implements ActionListener
    {
        public Listener()
        {
            okButton.addActionListener( this );
            cancelButton.addActionListener( this );
            prepend.addActionListener( this );
            append.addActionListener( this );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();
            
            if( source == cancelButton )
            {
                setVisible( false );
            }
            else if( source == okButton || source == prepend || source == append )
            {
                setVisible( false );
                doAppendPrepend();
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

            add( prependLabel );
            add( prepend );
            add( appendLabel );
            add( append );
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
                prependLabel, appendLabel
            };
            Dimension labelSize = GuiUtils.sameSize( labels );
            
            JComponent[] fields = new JComponent[]
            {
                prepend, append
            };
            
            Dimension fieldSize = GuiUtils.sameSize( fields );
            fieldSize.width = size.width - labelSize.width;
            fieldSize.width -= GuiUtils.PADDING;
            prepend.setSize( fieldSize );
            append.setSize( fieldSize );
            
            JComponent[] buttons = new JComponent[]
            {
                okButton, cancelButton
            };
            Dimension buttonSize = GuiUtils.sameSize( buttons );
            
            int rowHeight = Math.max(  labelSize.height, fieldSize.height );
            
            int labelX = GuiUtils.PADDING;
            int fieldX = GuiUtils.PADDING + labelSize.width + GuiUtils.PADDING;
            int y = GuiUtils.PADDING;
            
            prependLabel.setLocation( labelX, y );
            prepend.setLocation( fieldX, y );
            
            y += rowHeight;
            y += GuiUtils.PADDING;
            
            appendLabel.setLocation( labelX, y );
            append.setLocation( fieldX, y );
            
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
