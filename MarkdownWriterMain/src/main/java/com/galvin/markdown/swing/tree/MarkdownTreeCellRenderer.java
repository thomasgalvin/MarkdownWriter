package com.galvin.markdown.swing.tree;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.preferences.EditorPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.Icons;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.StringUtils;
import galvin.swing.tree.DraggableTreeCellRenderer;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.net.URL;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTree;

public class MarkdownTreeCellRenderer
        extends DraggableTreeCellRenderer
{
    private Color backgroundColor;
    private Color textColor;
    
    public MarkdownTreeCellRenderer()
    {
        Preferences preferences = MarkdownServer.getPreferences();
        EditorPreferences editorPreferences = preferences.getEditorPreferences();
        backgroundColor = editorPreferences.getBackgroundColor();
        textColor = editorPreferences.getTextColor();
    }
    
    @Override
    public Color getBackground() {
        return backgroundColor;
    }
    
    @Override
    public Color getBackgroundNonSelectionColor() {
        return backgroundColor;
    }
    
    @Override
    public Color getBackgroundSelectionColor() {
        return super.getBackgroundSelectionColor().darker();
    }
    
    @Override
    public Color getTextNonSelectionColor()
    {
        return textColor;
    }
    
    @Override
    public Color getTextSelectionColor()
    {
        return textColor;
    }
    
    

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

        if( value instanceof MarkdownTreeNode && result instanceof JLabel )
        {
            MarkdownTreeNode markdownTreeNode = ( MarkdownTreeNode ) value;
            Node node = markdownTreeNode.getNode();
            Project project = markdownTreeNode.getProject();

            JLabel label = ( JLabel ) result;
            String icon = null;
            String nodeType = null;

            if( node != null )
            {
                icon = node.getIcon();
                nodeType = node.getNodeType();
                label.setText( "<html>" + node.getTitle() + "</html>" );
            }
            else if( project != null )
            {
                nodeType = NodeTypes.PROJECT;
                label.setText( "<html>" + project.getTitle() + "</html>" );
            }

            if( !StringUtils.empty( icon ) )
            {
                setImageIcon( label, icon );
            }
            else if( !StringUtils.empty( nodeType ) )
            {
                if( NodeTypes.PROJECT.equals( nodeType ) )
                {
                    setImageIcon( label, Icons.PROJECT );
                }
                else if( NodeTypes.MANUSCRIPT.equals( nodeType ) )
                {
                    setImageIcon( label, Icons.MANUSCRIPT );
                }
//                else if( NodeTypes.FOLDER.equals( nodeType ) )
//                {
//                    if( markdownTreeNode.isLeaf() )
//                    {
//                        setImageIcon( label, Icons.FOLDER );
//                    }
//                    else
//                    {
//                        setImageIcon( label, Icons.FOLDER_DOCUMENTS );
//                    }
//                }
                else if( NodeTypes.TRASH.equals( nodeType ) )
                {
                    if( markdownTreeNode.isLeaf() )
                    {
                        setImageIcon( label, Icons.TRASH );
                    }
                    else
                    {
                        setImageIcon( label, Icons.TRASH_FULL );
                    }
                }
                else if( NodeTypes.RESOURCES.equals( nodeType ) )
                {
                    if( markdownTreeNode.isLeaf() )
                    {
                        setImageIcon( label, Icons.RESOURCES );
                    }
                    else
                    {
                        setImageIcon( label, Icons.RESOURCES );
                    }
                }
                else if( NodeTypes.RESOURCE.equals( nodeType ) )
                {
                    setImageIcon( label, Icons.IMAGE );
                }
                else if( NodeTypes.CONFIG.equals( nodeType ) )
                {
                    setImageIcon( label, Icons.CONFIG );
                }
                else if( NodeTypes.METADATA.equals( nodeType ) )
                {
                    setImageIcon( label, Icons.METADATA );
                }
                else if( NodeTypes.STYLESHEET.equals( nodeType ) )
                {
                    setImageIcon( label, Icons.STYLESHEET );
                }
                else if( NodeTypes.COVER.equals( nodeType ) )
                {
                    setImageIcon( label, Icons.COVER );
                }
                else
                {
                    if( markdownTreeNode.isLeaf() )
                    {
                        setImageIcon( label, Icons.DOCUMENT );
                    }
                    else
                    {
                        setImageIcon( label, Icons.DOCUMENT_MULTIPLE );
                    }
                }
            }
        }

        Dimension size = result.getPreferredSize();
        result.setPreferredSize( size );
//        result.setMinimumSize( size );
//        result.setMaximumSize( size );
        
        return result;
    }
    
    private HashMap<String, ImageIcon> cachedImageIcons = new HashMap();

    private void setImageIcon( JLabel label, String imageName )
    {
        ImageIcon icon = loadImageIcon( imageName );
        label.setIcon( icon );
    }
    
    private ImageIcon loadImageIcon( String imageName )
    {
        ImageIcon icon = cachedImageIcons.get( imageName );
        if( icon != null )
        {
            return icon;
        }
        else
        {
            URL url = ClassLoader.getSystemResource( imageName );
            if( url == null )
            {
                url = MarkdownTreeCellRenderer.class.getClassLoader().getSystemResource( imageName );
            }

            if( url != null )
            {
                icon = new ImageIcon( url );
                cachedImageIcons.put( imageName, icon );
                return icon;
            }
        }
        
        return null;
    }
}
