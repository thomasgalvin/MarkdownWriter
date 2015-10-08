package com.galvin.markdown.swing.editor;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.model.Project;
import galvin.swing.editor.EditorDocument;
import galvin.swing.spell.DocumentSpellChecker;
import galvin.swing.spell.SpellDictionaryUser;
import galvin.swing.spell.SpellUtils;
import java.awt.Toolkit;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

public class MarkdownDocument
    extends EditorDocument
    implements Document
{

    private int selectionStart = -1;
    private int selectionEnd = -1;
    private Node node;
    private NodeSection nodeSection;
    private UndoManager undoManager = new UndoManager();
    private DocumentSpellChecker documentSpellChecker;
    private SpellDictionaryUser userDictionary;
    private SpellDictionaryUser projectDictionary;

    public MarkdownDocument()
    {
        addDocumentListener( new NeedsSavingListener() );
        addUndoableEditListener( undoManager );

        try
        {
            userDictionary = SpellUtils.getCustomDictionary();
            documentSpellChecker = new DocumentSpellChecker( this, SpellUtils.getAmericanDictionary(), userDictionary, projectDictionary );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    public MarkdownDocument( Node parent )
    {
        this.node = parent;
        addDocumentListener( new NeedsSavingListener() );
        addUndoableEditListener( undoManager );

        try
        {
            userDictionary = SpellUtils.getCustomDictionary();

            if( parent != null )
            {
                Project project = parent.getProject();
                if( project != null )
                {
                    projectDictionary = project.getProjectDictionary();
                }
            }

            documentSpellChecker = new DocumentSpellChecker( this, SpellUtils.getAmericanDictionary(), userDictionary, projectDictionary );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    @Override
    public void undo()
    {
        if( undoManager.canUndo() )
        {
            undoManager.undo();
        }
        else
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void redo()
    {
        if( undoManager.canRedo() )
        {
            undoManager.redo();
        }
        else
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void stopSpellCheck()
    {
        documentSpellChecker.stopSpellCheck();
    }

    public void startSpellCheck()
    {
        documentSpellChecker.startSpellCheck();
    }

    public int getSelectionEnd()
    {
        return selectionEnd;
    }

    public void setSelectionEnd( int selectionEnd )
    {
        this.selectionEnd = selectionEnd;
    }

    public int getSelectionStart()
    {
        return selectionStart;
    }

    public void setSelectionStart( int selectionStart )
    {
        this.selectionStart = selectionStart;
    }

    public Node getNode()
    {
        return node;
    }

    public void setNode( Node node )
    {
        this.node = node;
    }

    public NodeSection getNodeSection()
    {
        return nodeSection;
    }

    public void setNodeSection( NodeSection nodeSection )
    {
        this.nodeSection = nodeSection;
    }

    private class NeedsSavingListener
        implements DocumentListener
    {

        public void changedUpdate( DocumentEvent evt )
        {
            //for some reason, gaining focus is considered a change event.
            //since there really is no change, this little hack will
            //get around it.
            if( evt.toString().compareTo( "[]" ) != 0 )
            {
                recordChange( evt );
            }
        }

        public void insertUpdate( DocumentEvent evt )
        {
            recordChange( evt );
        }

        public void removeUpdate( DocumentEvent evt )
        {
            recordChange( evt );
        }

        public void recordChange( DocumentEvent evt )
        {
            if( getRecordChanges() )
            {
                setNeedsSaving( true );
            }
        }
    }
}
