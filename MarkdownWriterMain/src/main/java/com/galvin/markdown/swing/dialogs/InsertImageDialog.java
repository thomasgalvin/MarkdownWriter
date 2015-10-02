package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.model.ImageResource;
import com.galvin.markdown.model.Node;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import galvin.swing.ScaledImage;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class InsertImageDialog
        extends JDialog
{

    private static MarkdownMessages messages = MarkdownServer.getMessages();
    private ScaledImage image;
    private JComboBox imageComboBox = new JComboBox();
    private JButton cancelButton = new JButton( messages.dialogImageInsertCancel() );
    private JButton insertImageButton = new JButton( messages.dialogImageInsertOkay() );
    private LayoutPanel layoutPanel = new LayoutPanel();
    private Controller controller;
    private JButton[] buttons = new JButton[]
    {
        cancelButton, insertImageButton
    };
    private Dimension buttonSize = GuiUtils.sameSize( buttons );
    private Dimension comboBoxSize = GuiUtils.preferredSize( imageComboBox );

    public InsertImageDialog( Controller controller )
    {
        this.controller = controller;
        setLayout( new BorderLayout() );
        getContentPane().add( layoutPanel, BorderLayout.CENTER );
        
        setSize( new Dimension( 500, 500 ) );
        GuiUtils.center( this );
        
        refreshImageList();
        refreshImage();
        new ImageSelectionListener();
        new ButtonListener();
        GuiUtils.closeOnEscape( this );
    }
    
    @Override
    public void setVisible( boolean visible )
    {
        if( visible )
        {
            refreshImageList();
        }
        
        super.setVisible( visible );
    }

    public void insertImage()
    {
        Object selection = imageComboBox.getSelectedItem();
        if( selection instanceof Node )
        {
            Node node = (Node)selection;
            controller.formatInsertImage( node );
        }
    }

    public void refreshImage()
    {
        if( image != null )
        {
            layoutPanel.remove( image );
        }
        image = null;

        Object selection = imageComboBox.getSelectedItem();
        if( selection != null && selection instanceof Node )
        {
            Node node = ( Node ) selection;

            ImageResource resource = node.getImageResource();
            if( resource != null )
            {
                ImageIcon imageIcon = resource.getImageIcon();
                if( imageIcon != null )
                {
                    image = new ScaledImage( imageIcon );
                }
            }
        }

        if( image != null )
        {
            layoutPanel.add( image );
        }
        GuiUtils.forceRepaint( this );
    }
    
    public void refreshImageList()
    {
        Object selection = imageComboBox.getSelectedItem();
        imageComboBox.removeAllItems();
        
        Node resourceNode = controller.getProjectFrame().getProject().getResources();
        if( resourceNode != null )
        {
            for( Node node : resourceNode.getChildNodes() )
            {
                imageComboBox.addItem( node );
            }
        }
        
        if( selection != null )
        {
            imageComboBox.setSelectedItem( selection );
        }
    }

    private class ImageSelectionListener
            implements ActionListener
    {
        public ImageSelectionListener()
        {
            imageComboBox.addActionListener( this );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            refreshImage();
        }
    }

    private class ButtonListener
            implements ActionListener
    {

        public ButtonListener()
        {
            cancelButton.addActionListener( this );
            insertImageButton.addActionListener( this );
        }

        @Override
        public void actionPerformed( ActionEvent e )
        {
            setVisible( false );

            Object source = e.getSource();
            if( source == insertImageButton )
            {
                insertImage();
            }
        }
    }
    
    private class LayoutPanel
            extends JPanel
    {

        public LayoutPanel()
        {
            setLayout( null );
            add( imageComboBox );
            add( cancelButton );
            add( insertImageButton );
        }

        @Override
        public void doLayout()
        {
            Dimension size = getSize();

            comboBoxSize.width = size.width - GuiUtils.PADDING;
            imageComboBox.setSize( comboBoxSize );

            int x = GuiUtils.PADDING;
            int y = GuiUtils.PADDING;
            imageComboBox.setLocation( x, y );

            x = size.width - buttonSize.width - GuiUtils.PADDING;
            y = size.height - buttonSize.height - GuiUtils.PADDING;
            insertImageButton.setLocation( x, y );

            x -= buttonSize.width + GuiUtils.PADDING;
            cancelButton.setLocation( x, y );

            if( image != null )
            {
                int imageWidth = size.width - GuiUtils.PADDING * 2;
                int imageHeight = size.height - GuiUtils.PADDING * 4 - buttonSize.height - comboBoxSize.height;
                Dimension imageSize = new Dimension( imageWidth, imageHeight );

                x = GuiUtils.PADDING;
                y = GuiUtils.PADDING * 2 + comboBoxSize.height;

                image.setSize( imageSize );
                image.setLocation( x, y );
            }
        }
    }
}
