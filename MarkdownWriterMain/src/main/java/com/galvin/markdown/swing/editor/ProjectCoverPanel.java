package com.galvin.markdown.swing.editor;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import galvin.swing.ScaledImage;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ProjectCoverPanel
    extends JPanel {

    private static MarkdownMessages messages = MarkdownServer.getMessages();
    private ScaledImage image;
    private JComboBox imageComboBox;
    private Controller controller;

    public ProjectCoverPanel( Controller controller ) {
        this.controller = controller;
        setLayout( new BorderLayout() );

        Node cover = controller.getProjectFrame().getProject().getCover();

        refresh();

        if( cover != null ) {
            imageComboBox.setSelectedItem( cover );
        }
        else {
            imageComboBox.setSelectedItem( messages.noCover() );
        }
    }

    public void refresh() {
        removeAll();

        Vector<Object> values = new Vector();
        values.add( messages.noCover() );

        Project project = controller.getProjectFrame().getProject();
        Node resourceNode = project.getResources();
        if( resourceNode != null ) {
            values.addAll( resourceNode.getChildNodes() );
        }

        Object selection = messages.noCover();
        if( imageComboBox != null ) {
            selection = imageComboBox.getSelectedItem();
        }

        imageComboBox = new JComboBox( values );
        imageComboBox.setSelectedItem( selection );
        imageComboBox.addActionListener( new Listener() );

        add( imageComboBox, BorderLayout.SOUTH );
        refreshImage();
    }

    public void refreshImage() {
        if( image != null ) {
            remove( image );
        }

        image = null;

        Object selection = imageComboBox.getSelectedItem();
        if( selection instanceof Node ) {
            Node node = (Node)selection;
            image = new ScaledImage( node.getImageResource().getImageIcon() );
        }

        if( image != null ) {
            add( image, BorderLayout.CENTER );
        }
        else {
            add( new JLabel( "" ), BorderLayout.CENTER );
        }
        GuiUtils.forceRepaint( this );
    }

    public void updateCover() {
        Object selection = imageComboBox.getSelectedItem();
        if( selection instanceof Node ) {
            try {
                Node node = (Node)selection;
                Project project = controller.getProjectFrame().getProject();
                project.setCover( node );
            }
            catch( Throwable t ) {
                JOptionPane.showMessageDialog( controller.getPopupWindowOwner(),
                                               messages.errorSettingCoverImage(),
                                               messages.errorDialogTitle(),
                                               JOptionPane.ERROR_MESSAGE );
                t.printStackTrace();
            }
        }
        else {
            controller.getProjectFrame().getProject().setCover( null );
        }
        refreshImage();
    }

    private class Listener
        implements ActionListener {

        @Override
        public void actionPerformed( ActionEvent e ) {
            updateCover();
        }

    }

}
