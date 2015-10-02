package com.galvin.markdown.swing.editor;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.preferences.EditorPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.swabunga.spell.engine.SpellDictionary;
import galvin.swing.editor.Editor;
import galvin.swing.spell.SpellUtils;
import galvin.swing.spell.SpellingHighlighter;
import galvin.swing.spell.SpellingPopupMenu;
import galvin.swing.text.KeyBindings;
import java.awt.Cursor;
import javax.swing.JScrollPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Keymap;

public class MarkdownEditor
    extends Editor
    implements CaretListener
{

    private JScrollPane scrollPane;
    private SpellingPopupMenu spellingPopupMenu;

    public MarkdownEditor()
    {
        this( new MarkdownDocument() );
        init();
    }

    public MarkdownEditor( MarkdownDocument document )
    {
        super( document );
        init();
    }

    private void init()
    {
        try
        {
            setSpellingHighlighter( new SpellingHighlighter( this ) );
            spellingPopupMenu = new SpellingPopupMenu( this, SpellUtils.getCustomDictionary(), getProjectDictionary() );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
        
        buildKeyMap();
        
        Preferences preferences = MarkdownServer.getPreferences();
        configure( preferences );

        scrollPane = new JScrollPane( this );
        
        MarkdownDocument document = getMarkdownDocument();
        int start = Math.min( document.getSelectionStart(), document.getSelectionEnd() );
        start = Math.min( start, document.getLength() - 1 );
        start = Math.max( start, 0 );

        int end = Math.max( document.getSelectionStart(), document.getSelectionEnd() );
        end = Math.min( end, document.getLength() - 1 );
        end = Math.max( end, 0 );
        
        setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
        
        setCaretPosition( start );
        moveCaretPosition( end );
        addCaretListener( this );
    }

    private SpellDictionary getProjectDictionary()
    {
        try
        {
            MarkdownDocument document = getMarkdownDocument();
            if( document != null )
            {
                Node node = document.getNode();
                if( node != null )
                {
                    Project project = node.getProject();
                    if( project != null )
                    {
                        return project.getProjectDictionary();
                    }
                }
            }
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }

        return null;
    }

    public void configure( Preferences preferences )
    {
        EditorPreferences editorPreferences = preferences.getEditorPreferences();
//        setEOLMarkersVisible( editorPreferences.showInvisibles() );
//        setWhitespaceVisible( editorPreferences.showInvisibles() );
        
        setBackground( editorPreferences.getBackgroundColor() );
        setForeground( editorPreferences.getTextColor() );
        setCaretColor( editorPreferences.getTextColor() );

//        setLineWrap( editorPreferences.lineWrap() );
//        setHighlightCurrentLine( editorPreferences.highlightLines() );
//        scrollPane.setLineNumbersEnabled( editorPreferences.showLineNumbers() );

//        setTabsEmulated( editorPreferences.softTabs() );
//        setTabSize( editorPreferences.getSpacesPerTab() );

        setSoftTabs( editorPreferences.softTabs() );
        setSpacesPerTab( editorPreferences.getSpacesPerTab() );

//        setMarkOccurrences( false );
//        setWrapStyleWord( true );

//        setMarginLineEnabled( true );
//        setMarginLinePosition( 80 );
    }

    private void buildKeyMap()
    {
        Keymap keymap = getKeymap();

        keymap.removeKeyStrokeBinding( KeyBindings.toggleBoldKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleBoldKeyStroke, new TextActions.ToggleBoldAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.toggleItalicKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleItalicKeyStroke, new TextActions.ToggleItalicAction() );

        keymap.removeKeyStrokeBinding( KeyBindings.toggleUnderlineKeyStroke );
        keymap.addActionForKeyStroke( KeyBindings.toggleUnderlineKeyStroke, new TextActions.ToggleUnderlineAction() );
    }

    public MarkdownDocument getMarkdownDocument()
    {
        return (MarkdownDocument) super.getDocument();
    }

//    public ErrorStrip getErrorStrip()
//    {
//        return errorStrip;
//    }
//
    public JScrollPane getScrollPane()
    {
        return scrollPane;
    }

    public void caretUpdate( CaretEvent e )
    {
        writeCaretPositions( e.getDot(), e.getMark() );
    }

    private void writeCaretPositions( int start, int end )
    {
        getMarkdownDocument().setSelectionStart( start );
        getMarkdownDocument().setSelectionEnd( end );
    }
    
    @Override
    public int getDefaultFontSize()
    {
        return 14;
    }
}
