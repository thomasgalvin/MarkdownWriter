/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.SystemUtils;
import galvin.swing.CloseableDialog;
import galvin.swing.GuiUtils;
import galvin.swing.text.macros.Macro;
import galvin.swing.text.macros.MacroComparator;
import galvin.swing.text.macros.MacroEditor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;

public class MacroEditorDialog
extends CloseableDialog
implements MacroEditor.MacroListListener
{
    private static final KeyStroke NEW_KEYSTROKE = KeyStroke.getKeyStroke( KeyEvent.VK_N, SystemUtils.PREFERED_MODIFIER_KEY );
    public static final String NEW_MACRO_ACTION_MAP_KEY = "com.galvin.util.swing.text.macros.NewMacro";
    
    private static final KeyStroke SAVE_KEYSTROKE = KeyStroke.getKeyStroke( KeyEvent.VK_S, SystemUtils.PREFERED_MODIFIER_KEY );
    public static final String SAVE_MACRO_ACTION_MAP_KEY = "com.galvin.util.swing.text.macros.SaveMacro";
    
    MarkdownMessages messages = MarkdownServer.getMessages();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private MacroEditor globalEditor = new MacroEditor();
    private MacroEditor projectEditor = new MacroEditor();
    private Controller controller;
    private boolean listening = true;
    
    public MacroEditorDialog( Controller controller )
    {
        this.controller = controller;
        
        setTitle( messages.dialogMacrosTitle() );
        
        tabbedPane.add( messages.dialogMacrosProject(), projectEditor );
        tabbedPane.add( messages.dialogMacrosGlobal(), globalEditor );
        
        globalEditor.addMacroListListener( this );
        projectEditor.addMacroListListener( this );
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( NEW_KEYSTROKE, NEW_MACRO_ACTION_MAP_KEY ); 
        getRootPane().getActionMap().put( NEW_MACRO_ACTION_MAP_KEY, new NewMacroAction() ); 
        
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put( SAVE_KEYSTROKE, SAVE_MACRO_ACTION_MAP_KEY ); 
        getRootPane().getActionMap().put( SAVE_MACRO_ACTION_MAP_KEY, new SaveMacroAction() ); 
        
        add( tabbedPane );
        
        pack();
        GuiUtils.center( this );
        
        GuiUtils.closeOnEscape( this );
    }
    
    @Override
    public void closeDialog()
    {
        setVisible( false );
        saveMacro();
    }
    
    public void refresh()
    {
        listening = false;
        
        MacroComparator comparator = new MacroComparator();
        
        List<Macro> global = MarkdownServer.getPreferences().getGlobalMacros().getMacros();
        Collections.sort( global, comparator );
        globalEditor.setMacros( global );
        
        List<Macro> project = controller.getProjectFrame().getProject().getProjectMacros().getMacros();
        Collections.sort( project, comparator );
        projectEditor.setMacros( project );
        
        listening = true;
    }
    
    private void write( Object source )
    {
        if( listening )
        {
            if( source == globalEditor )
            {
                writeGlobal();
            }
            else if( source == projectEditor )
            {
                writeProject();
            }
        }
    }
    
    public void writeGlobal()
    {
        List<Macro> macros = globalEditor.getMacros();
        MarkdownServer.getPreferences().getGlobalMacros().getMacros().clear();
        MarkdownServer.getPreferences().getGlobalMacros().getMacros().addAll( macros );
        MarkdownServer.writePreferences();
    }
    
    public void writeProject()
    {
        List<Macro> macros = projectEditor.getMacros();
        controller.getProjectFrame().getProject().getProjectMacros().getMacros().clear();
        controller.getProjectFrame().getProject().getProjectMacros().getMacros().addAll( macros );
    }

    public void macroAdded( Macro macro, Object source )
    {
        write( source );
    }

    public void macroRemoved( Macro macro, Object source )
    {
        write( source );
    }

    public void macroUpdated( Macro macro, Object source )
    {
        write( source );
    }

    public void macroEditingComplete( Object source )
    {
        setVisible( false );
    }
    
    public void newMacro()
    {
        if( tabbedPane.getSelectedIndex() == 0 )
        {
            projectEditor.newMacro();
        }
        else
        {
            globalEditor.newMacro();
        }
    }
    
    public void saveMacro()
    {
        if( tabbedPane.getSelectedIndex() == 0 )
        {
            projectEditor.saveMacro();
        }
        else
        {
            globalEditor.saveMacro();
        }
    }
    
    private class NewMacroAction
        extends AbstractAction
    {
        public void actionPerformed( ActionEvent event )
        {
            newMacro();
        }
    }
    
    private class SaveMacroAction
        extends AbstractAction
    {
        public void actionPerformed( ActionEvent event )
        {
            saveMacro();
        }
    }
}
