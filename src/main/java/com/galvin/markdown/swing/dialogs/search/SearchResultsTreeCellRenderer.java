/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs.search;

import com.galvin.markdown.model.Node;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class SearchResultsTreeCellRenderer
    extends DefaultTreeCellRenderer
{

    @Override
    public Component getTreeCellRendererComponent( JTree tree,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean isExpanded,
                                                   boolean isLeaf,
                                                   int row,
                                                   boolean hasFocus )
    {

        Component result = super.getTreeCellRendererComponent( tree, value, isSelected, isExpanded, isLeaf, row, hasFocus );

        if( result instanceof JLabel )
        {
            JLabel label = (JLabel) result;

            if( value instanceof DefaultMutableTreeNode )
            {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
                value = node.getUserObject();
            }
            
            if( value instanceof NodeSearchResults )
            {
                NodeSearchResults nodeSearchResults = (NodeSearchResults) value;
                Node node = nodeSearchResults.getNode();
                label.setText( node.getTitle() );
            }
            else if( value instanceof SearchResult )
            {
                SearchResult searchResult = (SearchResult) value;
                label.setText( searchResult.getSnippet() );
            }
        }

        return result;
    }
}
