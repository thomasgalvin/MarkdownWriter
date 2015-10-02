package com.galvin.markdown.swing.split;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.editor.MarkdownEditorPanel;
import galvin.swing.splitpane.ComponentFactory;
import java.awt.Component;

public class MarkdownEditorPanelFactory
        implements ComponentFactory
{

    private Controller controller;

    public MarkdownEditorPanelFactory( Controller controller )
    {
        this.controller = controller;
    }

    public Component createComponent( Component currentSelection )
    {
        MarkdownEditorPanel result = new MarkdownEditorPanel( controller );
        if( currentSelection != null )
        {
            if( currentSelection instanceof MarkdownEditorPanel )
            {
                MarkdownEditorPanel panel = ( MarkdownEditorPanel ) currentSelection;
                
                Node node = panel.getNode();
                if( node != null )
                {
                    result.edit( node );
                }
                else
                {
                    Project project = panel.getProject();
                    result.edit( project );
                }
            }
        }
        return result;
    }

    public void removeComponent( Component currentSelection )
    {
    }
}
