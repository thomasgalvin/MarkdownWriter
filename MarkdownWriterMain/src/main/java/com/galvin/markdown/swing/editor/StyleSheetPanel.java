package com.galvin.markdown.swing.editor;

import com.galvin.markdown.swing.Controller;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

public class StyleSheetPanel
extends RSyntaxTextArea
{
    private Controller controller;
    private RTextScrollPane scrollPane;
    
    public StyleSheetPanel( Controller controller )
    {
        setDocument( controller.getProjectFrame().getProject().getStyleSheetDocument() );
        setSyntaxEditingStyle( SYNTAX_STYLE_CSS );
    }

    public RTextScrollPane getScrollPane()
    {
        if( scrollPane == null )
        {
            scrollPane = new RTextScrollPane(this );
        }
        return scrollPane;
    }
    
}
