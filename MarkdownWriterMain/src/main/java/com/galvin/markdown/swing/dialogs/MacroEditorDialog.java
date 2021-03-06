/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.SystemUtils;
import galvin.swing.CloseableDialog;
import galvin.swing.GuiUtils;
import galvin.swing.spell.SpellDictionaryUser;
import galvin.swing.text.macros.Macro;
import galvin.swing.text.macros.MacroComparator;
import galvin.swing.text.macros.MacroEditor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MacroEditorDialog
    extends CloseableDialog
    implements MacroEditor.MacroListListener {
    private static final Logger logger = LoggerFactory.getLogger( MacroEditorDialog.class );

    private static final KeyStroke NEW_KEYSTROKE = KeyStroke.getKeyStroke( KeyEvent.VK_N, SystemUtils.PREFERED_MODIFIER_KEY );
    public static final String NEW_MACRO_ACTION_MAP_KEY = "com.galvin.util.swing.text.macros.NewMacro";

    private static final KeyStroke SAVE_KEYSTROKE = KeyStroke.getKeyStroke( KeyEvent.VK_S, SystemUtils.PREFERED_MODIFIER_KEY );
    public static final String SAVE_MACRO_ACTION_MAP_KEY = "com.galvin.util.swing.text.macros.SaveMacro";

    MarkdownMessages messages = MarkdownServer.getMessages();
    private JTabbedPane tabbedPane = new JTabbedPane();
    private MacroEditor globalMacrosEditor;
    private MacroEditor projectMacrosEditor;
    private Controller controller;
    private boolean listening = true;

    public MacroEditorDialog( Controller controller ) {
        this.controller = controller;
        setTitle( messages.macrosDialogTitle() );

        SpellDictionaryUser userDict = null;
        try {
            System.out.println( "dict file: " + controller.getProject().getProjectDictionaryFile().getAbsolutePath() );
            userDict = controller.getProject().getProjectDictionary();
        }
        catch( IOException ioe ) {
            logger.error( "Error loading user dict", ioe );
        }
        globalMacrosEditor = new MacroEditor( userDict );
        projectMacrosEditor = new MacroEditor( userDict );

        tabbedPane.add( messages.labelProject(), projectMacrosEditor );
        tabbedPane.add( messages.labelGlobal(), globalMacrosEditor );

        globalMacrosEditor.addMacroListListener( this );
        projectMacrosEditor.addMacroListListener( this );

        getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( NEW_KEYSTROKE, NEW_MACRO_ACTION_MAP_KEY );
        getRootPane().getActionMap().put( NEW_MACRO_ACTION_MAP_KEY, new NewMacroAction() );

        getRootPane().getInputMap( JComponent.WHEN_IN_FOCUSED_WINDOW ).put( SAVE_KEYSTROKE, SAVE_MACRO_ACTION_MAP_KEY );
        getRootPane().getActionMap().put( SAVE_MACRO_ACTION_MAP_KEY, new SaveMacroAction() );

        add( tabbedPane );

        pack();
        GuiUtils.center( this );

        GuiUtils.closeOnEscape( this );
    }

    @Override
    public void closeDialog() {
        setVisible( false );
        saveMacro();
    }

    public void refresh() {
        listening = false;

        MacroComparator comparator = new MacroComparator();

        List<Macro> global = MarkdownServer.getPreferences().getGlobalMacros().getMacros();
        Collections.sort( global, comparator );
        globalMacrosEditor.setMacros( global );

        List<Macro> project = controller.getProjectFrame().getProject().getProjectMacros().getMacros();
        Collections.sort( project, comparator );
        projectMacrosEditor.setMacros( project );

        listening = true;
    }

    private void write( Object source ) {
        if( listening ) {
            if( source == globalMacrosEditor ) {
                writeGlobal();
            }
            else if( source == projectMacrosEditor ) {
                writeProject();
            }
        }
    }

    public void writeGlobal() {
        List<Macro> macros = globalMacrosEditor.getMacros();
        MarkdownServer.getPreferences().getGlobalMacros().getMacros().clear();
        MarkdownServer.getPreferences().getGlobalMacros().getMacros().addAll( macros );
        MarkdownServer.writePreferences();
    }

    public void writeProject() {
        List<Macro> macros = projectMacrosEditor.getMacros();
        controller.getProjectFrame().getProject().getProjectMacros().getMacros().clear();
        controller.getProjectFrame().getProject().getProjectMacros().getMacros().addAll( macros );
    }

    public void macroAdded( Macro macro, Object source ) {
        write( source );
    }

    public void macroRemoved( Macro macro, Object source ) {
        write( source );
    }

    public void macroUpdated( Macro macro, Object source ) {
        write( source );
    }

    public void macroEditingComplete( Object source ) {
        setVisible( false );
    }

    public void newMacro() {
        if( tabbedPane.getSelectedIndex() == 0 ) {
            projectMacrosEditor.newMacro();
        }
        else {
            globalMacrosEditor.newMacro();
        }
    }
    
    public void newProjectMacro( String abb, String beforeCursor, String afterCursor ){
        newMacro(0, projectMacrosEditor, abb, beforeCursor, afterCursor);
    }
    
    public void newGlobalMacro( String abb, String beforeCursor, String afterCursor ){
        newMacro(1, globalMacrosEditor, abb, beforeCursor, afterCursor);
    }
    
    private void newMacro( int tabIndex, MacroEditor editor, String abb, String beforeCursor, String afterCursor ){
        tabbedPane.setSelectedIndex(tabIndex);
        editor.newMacro(abb, beforeCursor, afterCursor);
    }

    public void saveMacro() {
        if( tabbedPane.getSelectedIndex() == 0 ) {
            projectMacrosEditor.saveMacro();
        }
        else {
            globalMacrosEditor.saveMacro();
        }
    }

    private class NewMacroAction
        extends AbstractAction {
        public void actionPerformed( ActionEvent event ) {
            newMacro();
        }

    }

    private class SaveMacroAction
        extends AbstractAction {
        public void actionPerformed( ActionEvent event ) {
            saveMacro();
        }

    }

}
