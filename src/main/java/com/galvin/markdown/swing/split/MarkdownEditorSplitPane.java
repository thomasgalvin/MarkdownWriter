package com.galvin.markdown.swing.split;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.editor.MarkdownEditorPanel;
import galvin.swing.splitpane.MultiSplitPane;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

public class MarkdownEditorSplitPane
extends MultiSplitPane
{
    public MarkdownEditorSplitPane( Controller controller )
    {
        super( new MarkdownEditorPanelFactory( controller ) );
    }
    
    @Override
    public MarkdownEditorPanel getCurrentComponent()
    {
        return (MarkdownEditorPanel)super.getCurrentComponent();
    }
    
    public List<MarkdownEditorPanel> getAllMarkdownEditorPanels()
    {
        List<Component> components = getAllComponents();
        List<MarkdownEditorPanel> result = new ArrayList( components.size() );
        for( Component component : components )
        {
            result.add( (MarkdownEditorPanel)component );
        }
        return result;
    }
}
