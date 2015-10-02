/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing;

import com.galvin.markdown.model.Contributor;
import com.galvin.markdown.model.ContributorRole;
import galvin.swing.GuiUtils;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;

public class ContributorWidget
    extends JPanel
{
    private MarkdownMessages messages = MarkdownServer.getMessages();

    private JLabel nameLabel = new JLabel( messages.contributorWidgetName() );
    private JTextField nameField = new JTextField();
    private JLabel sortByLabel = new JLabel( messages.contributorWidgetSortByName() );
    private JTextField sortByField = new JTextField();
    private JLabel roleLabel = new JLabel( messages.contributorWidgetRole() );
    private JComboBox roleComboBox = new JComboBox( ContributorRole.ALL_ROLES );
    private JComponent[] labels = new JComponent[] { nameLabel, sortByLabel, roleLabel };
    private JComponent[] fields = new JComponent[] { nameField, sortByField };
    private Dimension labelSize = GuiUtils.sameSize( labels );
    private Dimension fieldSize = GuiUtils.sameSize( fields );
    private Dimension comboSize = GuiUtils.preferredSize( roleComboBox );

    public ContributorWidget()
    {
        setLayout( null );
        add( nameLabel );
        add( nameField );
        add( sortByLabel );
        add( sortByField );
        add( roleLabel );
        add( roleComboBox );
        
        doLayout();
    }
    
    public void addRoleListener( ActionListener listener )
    {
        roleComboBox.addActionListener( listener );
    }
    
    public void addNameListener( DocumentListener listener )
    {
        nameField.getDocument().addDocumentListener( listener );
        sortByField.getDocument().addDocumentListener( listener );
    }
    
    @Override
    public void doLayout()
    {
        Dimension size = getSize();
        
        int labelX = 0;
        int fieldX = labelSize.width + GuiUtils.PADDING;
        int y = 0;
        int rowHeight = Math.max( labelSize.height, fieldSize.height );
        
        comboSize.width = size.width - labelSize.width - GuiUtils.PADDING * 3;
        roleComboBox.setSize( comboSize );
        
        
        nameLabel.setLocation( labelX, y );
        nameField.setLocation( fieldX, y );
        y += rowHeight;
        y += GuiUtils.PADDING;
        
        sortByLabel.setLocation( labelX, y );
        sortByField.setLocation( fieldX, y );
        y += rowHeight;
        y += GuiUtils.PADDING;
        
        roleLabel.setLocation( labelX, y );
        roleComboBox.setLocation( fieldX, y );
        y += rowHeight;
        y += GuiUtils.PADDING;
        
        int width = labelSize.width * 2 + GuiUtils.PADDING;
        setPreferredSize( new Dimension( width, y ) );
    }
    
    public Dimension setLabelSize( Dimension newLabelSize )
    {
        int width = Math.max( labelSize.width, newLabelSize.width - GuiUtils.PADDING );
        int height = Math.max( labelSize.height, newLabelSize.height );
        labelSize = new Dimension( width, height );
        GuiUtils.setSize( labelSize, labels );
        return labelSize;
    }
    
    public Dimension setFieldSize( Dimension newFieldSize )
    {
        int width = Math.max( fieldSize.width, newFieldSize.width - GuiUtils.PADDING );
        int height = Math.max( fieldSize.height, newFieldSize.height );
        fieldSize = new Dimension( width, height );
        GuiUtils.setSize( fieldSize, fields );
        return fieldSize;
    }
    
    public Contributor getContributor()
    {
        Contributor contributor = new Contributor( nameField.getText(),
                                                   sortByField.getText(),
                                                   (ContributorRole)roleComboBox.getSelectedItem() );
        return contributor;
    }
    
    public void setContributor( Contributor contributor )
    {
        if( contributor != null )
        {
            nameField.setText( contributor.getName() );
            sortByField.setText( contributor.getSortByName() );
            roleComboBox.setSelectedItem( contributor.getRole() );
        }
        else
        {
            nameField.setText( "" );
            sortByField.setText( "" );
            roleComboBox.setSelectedIndex( 0 );
        }
    }
}
