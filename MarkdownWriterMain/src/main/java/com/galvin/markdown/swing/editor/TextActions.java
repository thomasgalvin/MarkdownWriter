package com.galvin.markdown.swing.editor;

import com.galvin.markdown.compilers.Markup;
import galvin.swing.text.TextControlUtils;
import java.awt.event.ActionEvent;
import javax.swing.text.TextAction;

public class TextActions
{
    /////////////////
    // MarkdownEditorAction
    /////////////////

    /**
    All of the actions in MarkdownEditor have to poll the event passed to ActionPreformed()
    to find out what MarkdownEditor is responsible for the event; this class
    implements that code, provides a convenience getMarkdownEditor() method
    to find the result, and has an abstract doAction() method that is called
    from ActionPreformed().  All child classes have to do is implement doAction() and
    call getMarkdownEditor().
     */
    public static abstract class MarkdownEditorAction
            extends TextAction
    {

        private MarkdownEditor editor;

        public MarkdownEditorAction( String name )
        {
            super( name );
        }

        public void actionPerformed( ActionEvent evt )
        {
            Object target = evt.getSource();
            if( target instanceof MarkdownEditor )
            {
                editor = ( MarkdownEditor ) target;
                doAction();
                editor.requestFocus();
            }
        }

        public MarkdownEditor getMarkdownEditor()
        {
            return editor;
        }

        /**
        Child classes should implement this with a call to getMarkdownEditor()
        to find the component responsible for the event.
         */
        protected abstract void doAction();
    }

    //////////////////////
    // Uppercase/Lowercase
    //////////////////////
    /**
    A text action that allows keyboard bindings for the toggleSelectionUpperCase command.
     */
    public static class ToggleUpperCaseAction
            extends MarkdownEditorAction
    {

        public static final String actionName = "toggle-upper-case-action";

        public ToggleUpperCaseAction()
        {
            super( actionName );
        }

        protected void doAction()
        {
            TextControlUtils.toggleSelectionUpperCase( getMarkdownEditor() );
        }
    }

    
    
    //////////
    // Styles
    //////////
    /**
    A text action that allows keyboard bindings for the toggleBold command.
     */
    public static class ToggleBoldAction
            extends MarkdownEditorAction
    {

        public static final String actionName = "toggle-bold-action";

        public ToggleBoldAction()
        {
            super( actionName );
        }

        protected void doAction()
        {
            MarkdownEditor editor = getMarkdownEditor();
            TextControlUtils.markup( editor, Markup.BOLD );
        }
    }

    /**
    A text action that allows keyboard bindings for the toggleItalic command.
     */
    public static class ToggleItalicAction
            extends MarkdownEditorAction
    {

        public static final String actionName = "toggle-italic-action";

        public ToggleItalicAction()
        {
            super( actionName );
        }

        protected void doAction()
        {
            MarkdownEditor editor = getMarkdownEditor();
            TextControlUtils.markup( editor, Markup.ITALIC );
        }
    }

    /**
    A text action that allows keyboard bindings for the toggleUnderline command.
     */
    public static class ToggleUnderlineAction
            extends MarkdownEditorAction
    {

        public static final String actionName = "toggle-underline-action";

        public ToggleUnderlineAction()
        {
            super( actionName );
        }

        protected void doAction()
        {
            MarkdownEditor editor = getMarkdownEditor();
            TextControlUtils.markup( editor, Markup.UNDERLINE );
        }
    }
}
