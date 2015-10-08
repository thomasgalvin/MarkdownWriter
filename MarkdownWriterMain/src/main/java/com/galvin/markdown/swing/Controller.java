package com.galvin.markdown.swing;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.compilers.MarkdownCompiler;
import com.galvin.markdown.compilers.Markup;
import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.model.RecentProject;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.compile.CompileDialog;
import com.galvin.markdown.swing.compile.CompileProgressDialog;
import com.galvin.markdown.swing.dialogs.FootnoteDialog;
import com.galvin.markdown.swing.dialogs.HyperlinkEditorDialog;
import com.galvin.markdown.swing.dialogs.OpenProgressDialog;
import com.galvin.markdown.swing.dialogs.ProjectStatisticsDialog;
import com.galvin.markdown.swing.dialogs.RenameDocumentDialog;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import com.galvin.markdown.swing.editor.MarkdownEditor;
import com.galvin.markdown.swing.editor.MarkdownEditorPanel;
import com.galvin.markdown.swing.preferences.PreferencesDialog;
import com.galvin.markdown.swing.tree.MarkdownTree;
import com.galvin.markdown.swing.tree.MarkdownTreeNode;
import com.galvin.markdown.util.Utils;
import galvin.StringUtils;
import galvin.SystemUtils;
import galvin.swing.GuiUtils;
import galvin.swing.spell.SpellUtils;
import galvin.swing.text.DocumentUtils;
import galvin.swing.text.TextControlUtils;
import galvin.swing.text.macros.MacroList;
import galvin.swing.text.macros.MacroUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class Controller {

    private ProjectFrame projectFrame;
    private MarkdownMessages messages = MarkdownServer.getMessages();

    public Controller( ProjectFrame projectFrame ) {
        this.projectFrame = projectFrame;
    }

    ///////////////
    // File actions
    ///////////////
    public void fileNew() {
        MarkdownServer.welcomeNewProject();
    }

    public void fileOpen() {
        JFileChooser fileChooser = getOpenFileChooser();
        int option = fileChooser.showDialog( getPopupWindowOwner(), messages.fileChooserOpenProjectButton() );
        if( option == JFileChooser.APPROVE_OPTION ) {
            File projectStructureDocument = fileChooser.getSelectedFile();
            fileOpen( projectStructureDocument );
        }

        MarkdownServer.checkForOpenProjects();
    }

    public void fileOpen( File projectStructureDocument ) {
        new OpenThread( projectStructureDocument ).start();
    }

    private class OpenThread
        extends Thread {

        private File projectStructureDocument;

        public OpenThread( File projectStructureDocument ) {
            this.projectStructureDocument = projectStructureDocument;
        }

        @Override
        public void run() {
            OpenProgressDialog dialog = new OpenProgressDialog();
            dialog.setVisible( true );

            MarkdownServer.fileOpen( projectStructureDocument );

            dialog.setVisible( false );
        }

    }

    public void reloadRecentProjects() {
        MarkdownServer.reloadRecentProjects();
    }

    public boolean fileNeedsSaving() {
        return ProjectIo.needsSaving( projectFrame.getTree() );
    }

    public void fileSave() {
        Project project = projectFrame.getProject();

        if( project != null ) {
            if( project.getProjectFile() != null ) {
                try {
                    project = ProjectIo.toProject( projectFrame.getTree() );
                    ProjectIo.write( project, project.getProjectFile() );
                    projectFrame.getTree().setNeedsSaving( false );
                }
                catch( Throwable t ) {
                    t.printStackTrace();
                    JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                                   messages.errorDuringSave(),
                                                   messages.errorDuringSaveTitle(),
                                                   JOptionPane.ERROR_MESSAGE );
                }

                return;
            }

            fileSaveAs();
        }

        MarkdownServer.checkForOpenProjects();
    }

    public void fileSaveLegacyVersionAs() {
        JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                       messages.saveLegacyVersion(),
                                       messages.saveLegacyVersionTitle(),
                                       JOptionPane.INFORMATION_MESSAGE );
        File newFile = fileSaveCopyAs();
        if( newFile != null ) {
            Project project = projectFrame.getProject();
            project.setProjectFile( newFile );
            project.setProjectDictionaryFile( newFile.getParentFile() );
            project.setProjectModelVersion( ProjectIo.PROJECT_MODEL_VERSION );

            //TODO: project dictionary file?
        }
    }

    public void fileSaveAs() {
        JFileChooser fileChooser = projectFrame.getSaveFileChooser();
        int option = fileChooser.showDialog( getPopupWindowOwner(), messages.fileChooserSaveProjectButton() );
        if( option == JFileChooser.APPROVE_OPTION ) {
            File file = fileChooser.getSelectedFile();
            if( file != null ) {
                if( !file.getName().endsWith( ProjectIo.PROJECT_STRUCTURE_DOCUMENT_EXTENSION ) ) {
                    file = new File( file.getParentFile(), file.getName() + ProjectIo.PROJECT_STRUCTURE_DOCUMENT_EXTENSION );
                }

                fileSaveAs( file );
            }
        }
    }

    public void fileSaveAs( File projectFile ) {
        try {
            Project project = ProjectIo.toProject( projectFrame.getTree() );
            project.setProjectFile( projectFile );

            File projectDirectory = projectFile.getParentFile();
            project.setProjectDirectory( projectDirectory );

            if( StringUtils.isBlank( project.getTitle() ) ) {
                String title = projectFile.getName();
                title = title.replace( ProjectIo.PROJECT_STRUCTURE_DOCUMENT_EXTENSION, "" );
                project.setTitle( title );
            }

            File outputDirectory = new File( projectDirectory, ProjectIo.EXPORT_DIRECTORY );
            project.getCompileOptions().setOutputDirectory( outputDirectory );
            getProjectFrame().getCompileDialog().setCompileOptions( project.getCompileOptions() );

            ProjectIo.write( project, projectFile );
            projectFrame.getTree().setNeedsSaving( false );
            projectRenamed();

            RecentProject recentProject = new RecentProject( projectFile, project.getTitle() );
            MarkdownServer.addRecentProject( recentProject );
        }
        catch( Throwable t ) {
            t.printStackTrace();
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringSave(),
                                           messages.errorDuringSaveTitle(),
                                           JOptionPane.ERROR_MESSAGE );
        }

        MarkdownServer.checkForOpenProjects();
    }

    public File fileSaveCopyAs() {
        JFileChooser fileChooser = projectFrame.getSaveFileChooser();
        int option = fileChooser.showDialog( getPopupWindowOwner(), messages.fileChooserSaveProjectButton() );
        if( option == JFileChooser.APPROVE_OPTION ) {
            File file = fileChooser.getSelectedFile();
            if( file != null ) {
                if( !file.getName().endsWith( ProjectIo.PROJECT_STRUCTURE_DOCUMENT_EXTENSION ) ) {
                    file = new File( file.getParentFile(), file.getName() + ProjectIo.PROJECT_STRUCTURE_DOCUMENT_EXTENSION );
                }

                fileSaveCopyAs( file );
                return file;
            }
        }

        return null;
    }

    public void fileOpenTemplatesDir() {
        try {
            File templatesDir = MarkdownServer.getTemplatesDir();
            templatesDir.mkdirs();
            Desktop.getDesktop().open( templatesDir );
        }
        catch( Throwable t ) {
            t.printStackTrace();
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void fileSaveCopyAs( File projectFile ) {
        try {
            Project project = ProjectIo.toProject( projectFrame.getTree() );
            project = ProjectIo.clone( project, false, true );
            project.setProjectFile( projectFile );
            project.setProjectDirectory( projectFile.getParentFile() );
            ProjectIo.write( project, projectFile );

            RecentProject recentProject = new RecentProject( projectFile, project.getTitle() );
            MarkdownServer.addRecentProject( recentProject );
        }
        catch( Throwable t ) {
            t.printStackTrace();
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringSave(),
                                           messages.errorDuringSaveTitle(),
                                           JOptionPane.ERROR_MESSAGE );
        }

        MarkdownServer.checkForOpenProjects();
    }

    public void fileExportProject() {
        fileSave();
        projectFrame.getCompileDialog().setVisible( true );
    }

    public void fileExportProject( CompileOptions compileOptions ) {
        try {
            if( compileOptions != null && !compileOptions.getExportFormats().isEmpty() ) {
                MarkdownCompiler compiler = MarkdownServer.getCompiler();
                CompileThread compileThread = new CompileThread( this, compiler, compileOptions );

                CompileProgressDialog progressDialog = new CompileProgressDialog( compileThread, compiler, compileOptions );
                progressDialog.setVisible( true );

                compileThread.start();
            }
            else {
                fileExportProject();
            }
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportProjectUsingCurrentOptions() {
        Project project = projectFrame.getProject();
        CompileOptions compileOptions = project.getCompileOptions();
        compileOptions.refreshNodes();

        fileExportProject( compileOptions );
    }

    public void fileExportCurrentDocument() {
        try {
            Node currentNode = getNodeForCurrentDocument();
            fileExportCurrentDocument( currentNode );
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportCurrentDocument( MarkdownTreeNode node ) {
        try {
            if( node != null ) {
                fileExportCurrentDocument( node.getNode() );
            }
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportCurrentDocument( Node node ) {
        try {
            if( node != null ) {
                Project project = projectFrame.getProject();
                if( project != null ) {
                    node = ProjectIo.clone( node, project.getProjectDirectory(), true, true, false );
                    project = ProjectIo.clone( project, true, true );
                    project.setTitle( node.getTitle() );
                    project.setSubtitle( node.getSubtitle() );
                    project.getManuscript().getChildNodes().clear();
                    project.getManuscript().getChildNodes().add( node );

                    CompileOptions options = ProjectIo.clone( project.getCompileOptions() );
                    options.setProject( project );

                    CompileDialog dialog = new CompileDialog( this, project );
                    dialog.setCompileOptions( options );
                    dialog.setVisible( true );
                }
            }
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportCurrentDocumentUsingCurrentOptions() {
        try {
            Node currentNode = getNodeForCurrentDocument();
            fileExportCurrentDocumentUsingCurrentOptions( currentNode );
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportCurrentDocumentUsingCurrentOptions( MarkdownTreeNode node ) {
        try {
            if( node != null ) {
                fileExportCurrentDocumentUsingCurrentOptions( node.getNode() );
            }
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportCurrentDocumentUsingCurrentOptions( Node node ) {
        try {
            if( node != null ) {
                Project project = projectFrame.getProject();
                if( project != null ) {
                    node = ProjectIo.clone( node, project.getProjectDirectory(), true, true, false );
                    project = ProjectIo.clone( project, true, true );
                    project.setTitle( node.getTitle() );
                    project.setSubtitle( node.getSubtitle() );
                    project.getManuscript().getChildNodes().clear();
                    project.getManuscript().getChildNodes().add( node );

                    CompileOptions options = project.getCompileOptions();
                    options.refreshNodes();

                    fileExportProject( options );
                }
            }
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportCurrentDocumentAndChildren() {
        try {
            Node currentNode = getNodeForCurrentDocument();
            fileExportCurrentDocumentAndChildren( currentNode );
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportCurrentDocumentAndChildren( Node node ) {
        try {
            if( node != null ) {
                Project project = projectFrame.getProject();
                if( project != null ) {
                    project = ProjectIo.clone( project, true, true );
                    project.setTitle( node.getTitle() );
                    project.setSubtitle( node.getSubtitle() );
                    project.getManuscript().getChildNodes().clear();
                    project.getManuscript().getChildNodes().add( node );

                    CompileOptions options = ProjectIo.clone( project.getCompileOptions() );
                    options.setProject( project );

                    CompileDialog dialog = new CompileDialog( this, project );
                    dialog.setCompileOptions( options );
                    dialog.setVisible( true );
                }
            }
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportCurrentDocumentAndChildrenUsingCurrentOptionst() {
        try {
            Node currentNode = getNodeForCurrentDocument();
            fileExportCurrentDocumentAndChildrenUsingCurrentOptionst( currentNode );
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void fileExportCurrentDocumentAndChildrenUsingCurrentOptionst( Node node ) {
        try {
            if( node != null ) {
                Project project = projectFrame.getProject();
                if( project != null ) {
                    node = ProjectIo.clone( node, project.getProjectDirectory(), true, true, true );
                    project = ProjectIo.clone( project, true, true );
                    project.setTitle( node.getTitle() );
                    project.setSubtitle( node.getSubtitle() );
                    project.getManuscript().getChildNodes().clear();
                    project.getManuscript().getChildNodes().add( node );

                    CompileOptions options = project.getCompileOptions();
                    options.refreshNodes();

                    fileExportProject( options );
                }
            }
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringCompile(),
                                           messages.errorDuringCompileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public File getExportDirectory() {
        File result = getProject().getCompileOptions().getOutputDirectory();
        if( result == null ) {
            result = new File( getProject().getProjectDirectory(), ProjectIo.EXPORT_DIRECTORY );
        }
        result.mkdirs();
        return result;
    }

    public void fileShowExportDirectory() {
        try {
            File exportDir = getExportDirectory();
            exportDir.mkdirs();
            Desktop.getDesktop().open( exportDir );
        }
        catch( Throwable t ) {
            t.printStackTrace();
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void fileClose() {
        if( fileNeedsSaving() ) {
            fileSave();
        }

        projectFrame.getWindow().closeApplicationWindow();
    }

    public void fileCloseWithoutSaving() {
        if( !fileNeedsSaving() ) {
            fileClose();
        }
        else {
            int result = JOptionPane.showConfirmDialog( getPopupWindowOwner(),
                                                        messages.dialogCloseWithoutSaving(),
                                                        messages.dialogCloseWithoutSavingTitle(),
                                                        JOptionPane.YES_NO_OPTION );
            if( result == JOptionPane.YES_OPTION ) {
                doFileCloseWithoutSaving();
            }
        }
    }

    public void doFileCloseWithoutSaving() {
        projectFrame.getWindow().closeApplicationWindowWithoutSaving();
    }

    public void fileExit() {
        MarkdownServer.exit();
    }

    ///////////////
    // Edit actions
    ///////////////
    public void editCut() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            editor.cut();
        }
    }

    public void editCopy() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            editor.copy();
        }
    }

    public void editPaste() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            editor.paste();
        }
    }

    public void editUndo() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            MarkdownDocument document = editor.getMarkdownDocument();
            if( document != null ) {
                document.undo();
            }
        }
    }

    public void editRedo() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            MarkdownDocument document = editor.getMarkdownDocument();
            if( document != null ) {
                document.redo();
            }
        }
    }

    public void editSelectAll() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            editor.selectAll();
        }
    }

    public void editGoToLine() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            Object result = JOptionPane.showInputDialog( getPopupWindowOwner(),
                                                         messages.dialogMessageGoToLine() );
            if( result != null ) {
                String value = result.toString();
                try {
                    int line = Integer.parseInt( value );
                    line--;
                    if( line > 0 ) {
                        editor.setCaretPosition( editor.getLineStartOffset( line ) );
                    }

                }
                catch( Throwable t ) {
                    t.printStackTrace();
                }
            }
        }
    }

    public void editFindInProject() {
        String searchTerm = null;

        MarkdownEditor editor = projectFrame.getEditor();
        if( editor != null ) {
            searchTerm = editor.getSelectedText();
        }

        if( !StringUtils.empty( searchTerm ) ) {
            projectFrame.getFindAndReplaceDialog().setSearchTerm( searchTerm );
        }

        projectFrame.getFindAndReplaceDialog().setVisible( true );
    }

    public void editFindNext() {

        String searchTerm = projectFrame.getFindAndReplaceDialog().getSearchTerm();
        if( !StringUtils.empty( searchTerm ) ) {
            MarkdownEditor editor = projectFrame.getEditor();
            if( editor != null ) {
                MarkdownDocument document = editor.getMarkdownDocument();
                String text = DocumentUtils.getText( document );

                if( projectFrame.getFindAndReplaceDialog().getIgnoreCase() ) {
                    text = text.toLowerCase();
                    searchTerm = searchTerm.toLowerCase();
                }

                int index = editor.getCaretPosition();
                int result = text.indexOf( searchTerm, index );
                if( result != -1 ) {
                    int resultEnd = result + searchTerm.length();
                    editor.setCaretPosition( result );
                    editor.moveCaretPosition( resultEnd );
                }
                else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
            else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void editToggleLiveSpellcheck() {
        Preferences preferences = MarkdownServer.getPreferences();

        boolean spellCheck = !preferences.getEditorPreferences().liveSpellCheck();
        preferences.getEditorPreferences().setLiveSpellCheck( spellCheck );
        MarkdownServer.writePreferences();

        if( spellCheck ) {
            MarkdownServer.startSpellCheck();
        }
        else {
            MarkdownServer.stopSpellCheck();
        }

        configureMenusAndEditors( preferences );

        if( spellCheck ) {
            getProject().startSpellCheck();
        }
        else {
            getProject().stopSpellCheck();
        }
    }

    public void editShowUserDictionary() {
        try {
            SystemUtils.selectFileInBrowser( SpellUtils.getCustomDictionaryFile() );
        }
        catch( Throwable t ) {
            t.printStackTrace();
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void formatBold() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.markup( editor, Markup.BOLD );
        }
    }

    public void formatItalic() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.markup( editor, Markup.ITALIC );
        }
    }

    public void formatUnderline() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.markup( editor, Markup.UNDERLINE );
        }
    }

    public void formatStrikethrough() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.markup( editor, Markup.STRIKETHROUGH, Markup.END_STRIKETHROUGH );
        }
    }

    public void formatSmall() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.markup( editor, Markup.SMALL, Markup.END_SMALL );
        }
    }

    public void formatCenter() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.markup( editor, Markup.CENTER_START, Markup.CENTER_END );
        }
    }

    public void formatSuperscript() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.markup( editor, Markup.SUPERSCRIPT_START, Markup.SUPERSCRIPT_END );
        }
    }

    public void formatSubcript() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.markup( editor, Markup.SUBSCRIPT_START, Markup.SUBSCRIPT_END );
        }
    }

    public void editPrependToSelectedLines() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            Object result = JOptionPane.showInputDialog( getPopupWindowOwner(),
                                                         messages.dialogMessagePrependToLines() );
            if( result != null ) {
                bookendSelectedLines( result.toString(), null );
            }
        }
    }

    public void editAppendToSelectedLines() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            Object result = JOptionPane.showInputDialog( getPopupWindowOwner(),
                                                         messages.dialogMessageAppendToLines() );
            if( result != null ) {
                bookendSelectedLines( null, result.toString() );
            }
        }
    }

    public void bookendSelectedLines( String prepend, String append ) {
        if( StringUtils.isBlank( append ) && StringUtils.isBlank( prepend ) ) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            try {
                int selectionStart = editor.getSelectionStart();
                int selectionEnd = editor.getSelectionEnd();
                boolean selection = selectionStart != selectionEnd;

                if( prepend != null ) {
                    selectionStart += prepend.length();
                }

                int prependLength = prepend == null ? 0 : prepend.length();
                int appendLength = append == null ? 0 : append.length();

                selectLines();
                String text = editor.getSelectedText();
                if( !StringUtils.empty( text ) ) {
                    StringBuilder result = new StringBuilder( text.length() * 2 );
                    String[] lines = StringUtils.tokenize( text, "\n" );
                    if( lines.length == 1 ) {
                        String line = lines[0];
                        if( prepend != null ) {
                            result.append( prepend );
                            selectionEnd += prepend.length();
                        }

                        result.append( line );

                        if( append != null ) {
                            result.append( append );
                        }

                        result.append( "\n" );
                    }
                    else {
                        for( String line : lines ) {
                            if( prepend != null ) {
                                result.append( prepend );
                                selectionEnd += prepend.length();
                            }

                            result.append( line );

                            if( append != null ) {
                                result.append( append );
                                selectionEnd += append.length();
                            }

                            result.append( "\n" );
                        }

                        if( append != null ) {
                            selectionEnd -= append.length();
                        }
                    }

                    editor.replaceSelection( result.toString() );
                    editor.setCaretPosition( selectionStart );
                    if( selection ) {
                        editor.moveCaretPosition( selectionEnd );
                    }
                }
            }
            catch( Throwable t ) {
                Toolkit.getDefaultToolkit().beep();
                t.printStackTrace();
            }
        }
        else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    ////////////
    //Formatting
    ////////////
    public void formatBlockquote() {
        bookendSelectedLines( Markup.BLOCKQUOTE, null );
    }

    public void formatBulletedList() {
        bookendSelectedLines( Markup.BULLET, null );
    }

    public void formatCode() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.markup( editor, Markup.CODE );
        }
    }

    public void formatH1() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.bookendLine( editor, Markup.H1, Markup.SPACE );
        }
    }

    public void formatH2() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.bookendLine( editor, Markup.H2, Markup.SPACE );
        }
    }

    public void formatH3() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.bookendLine( editor, Markup.H3, Markup.SPACE );
        }
    }

    public void formatH4() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.bookendLine( editor, Markup.H4, Markup.SPACE );
        }
    }

    public void formatH5() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.bookendLine( editor, Markup.H5, Markup.SPACE );
        }
    }

    public void formatH6() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            TextControlUtils.bookendLine( editor, Markup.H6, Markup.SPACE );
        }
    }

    public void formatInsertLink() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            HyperlinkEditorDialog dialog = new HyperlinkEditorDialog( this );

            String text = editor.getSelectedText();
            if( !StringUtils.empty( text ) ) {
                dialog.setText( text );
            }

            dialog.setVisible( true );
        }
    }

    public void formatInsertLink( String text, String url ) {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            StringBuilder link = null;

            if( StringUtils.empty( text ) || text.equals( url ) ) {
                link = new StringBuilder( url.length() + 2 );
                link.append( "<" );
                link.append( url );
                link.append( ">" );
            }
            else {
                link = new StringBuilder( text.length() + url.length() + 4 );

                link.append( "[" );
                link.append( text );
                link.append( "]" );
                link.append( "(" );
                link.append( url );
                link.append( ")" );
            }

            editor.replaceSelection( link.toString() );
        }
    }

    public void formatInsertImage() {
        projectFrame.getInsertImageDialog().setVisible( true );
    }

    public void formatInsertImage( Node node ) {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            String imageName = Utils.getImageName( node );
            if( imageName != null ) {
                String text = StringUtils.neverNull( editor.getSelectedText() );
                StringBuilder imageMarkup = new StringBuilder();
                imageMarkup.append( "![" );
                imageMarkup.append( text );
                imageMarkup.append( "](Resources/" );
                imageMarkup.append( imageName );
                imageMarkup.append( ")" );

                editor.replaceSelection( imageMarkup.toString() );
            }
        }
    }

    public void formatInsertFootnote() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            FootnoteDialog dialog = new FootnoteDialog( this );

            String text = editor.getSelectedText();
            if( !StringUtils.empty( text ) ) {
                dialog.setText( text );
            }

            dialog.setVisible( true );
        }
    }

    public void formatInsertFootnote( String noteName, String noteText ) {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            StringBuilder footnoteLink = new StringBuilder( noteName.length() + 3 );
            StringBuilder footnoteBody = new StringBuilder( noteName.length() + noteText.length() + 7 );

            footnoteLink.append( "[^" );
            footnoteLink.append( noteName );
            footnoteLink.append( "]" );

            int caretPosition = Math.max( editor.getSelectionStart(), editor.getSelectionEnd() );
            editor.setCaretPosition( caretPosition );
            editor.replaceSelection( footnoteLink.toString() );

            footnoteBody.append( "\n\n[^" );
            footnoteBody.append( noteName );
            footnoteBody.append( "]: " );
            footnoteBody.append( noteText );

            DocumentUtils.appendText( editor.getDocument(), footnoteBody.toString() );
        }
    }

    public void filePreview() {
        filePreview( false );
    }

    public void filePreviewWithChildren() {
        filePreview( true );
    }

    public void filePreview( boolean includeChildren ) {
        try {
            Node node = getNodeForCurrentDocument();
            CompileOptions compileOptions = getProject().getCompileOptions();
            File previewFile = MarkdownServer.getCompiler().getPreview( compileOptions, node, NodeSection.MANUSCRIPT, includeChildren );
            Desktop.getDesktop().browse( previewFile.toURI() );
        }
        catch( Throwable t ) {

            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorPreview(),
                                           messages.errorPreviewTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void viewSplitHorizontal() {
        projectFrame.getEditorSplitPane().splitHorizontal();
    }

    public void viewSplitVertical() {
        projectFrame.getEditorSplitPane().splitVertical();
    }

    public void viewSplitUnsplit() {
        projectFrame.getEditorSplitPane().noSplit();
    }

    public void configureMenusAndEditors( Preferences preferences ) {
        projectFrame.getMenuBar().configure( preferences );

        List<MarkdownEditorPanel> editorPanels = projectFrame.getEditorSplitPane().getAllMarkdownEditorPanels();
        for( MarkdownEditorPanel editorPanel : editorPanels ) {
            MarkdownEditor editor = editorPanel.getEditor();
            if( editor != null ) {
                editor.configure( preferences );
            }
        }
    }

    public void viewSynchronizeEditors() {
        Project project = getProject();
        project.setSynchronizeEditors( !project.synchronizeEditors() );

        Preferences preferences = MarkdownServer.getPreferences();
        configureMenusAndEditors( preferences );
    }

    public void viewToggleLineNumbers() {
        Preferences preferences = MarkdownServer.getPreferences();
        preferences.getEditorPreferences().setShowLineNumbers( !preferences.getEditorPreferences().showLineNumbers() );
        MarkdownServer.writePreferences();

        configureMenusAndEditors( preferences );
    }

    public void viewLineHighlighting() {
        Preferences preferences = MarkdownServer.getPreferences();
        preferences.getEditorPreferences().setHighlightLines( !preferences.getEditorPreferences().highlightLines() );
        MarkdownServer.writePreferences();

        configureMenusAndEditors( preferences );
    }

    public void viewToggleWordWrap() {
        Preferences preferences = MarkdownServer.getPreferences();
        preferences.getEditorPreferences().setLineWrap( !preferences.getEditorPreferences().lineWrap() );
        MarkdownServer.writePreferences();

        configureMenusAndEditors( preferences );
    }

    public void viewToggleInvisibles() {
        Preferences preferences = MarkdownServer.getPreferences();
        preferences.getEditorPreferences().setShowInvisibles( !preferences.getEditorPreferences().showInvisibles() );
        MarkdownServer.writePreferences();

        configureMenusAndEditors( preferences );
    }

    ////////////
    // Documents
    ////////////
    private int getDepth( MarkdownTreeNode treeNode ) {
        int depth = 0;
        TreeNode[] path = projectFrame.getTree().getPath( treeNode );
        for( TreeNode tmp : path ) {
            if( tmp instanceof MarkdownTreeNode ) {
                MarkdownTreeNode mdtn = (MarkdownTreeNode)tmp;
                if( mdtn.getNode() != null ) {
                    if( NodeTypes.countsTowardDepth( mdtn.getNode().getNodeType() ) ) {
                        depth++;
                    }
                }
            }
        }
        return depth;
    }

    private void newNode( String nodeType, MarkdownTreeNode treeNode, boolean child ) {
        String name = showRenameDialog();
        if( !StringUtils.empty( name ) ) {
            int depth = getDepth( treeNode );
            
            if( child ) {
                depth++;
            }
            
            if( depth > 6 ){
                depth = 6;
            }

            StringBuilder headingMarkup = new StringBuilder();
            for( int i = 0; i < depth; i++ ) {
                headingMarkup.append( "#" );
            }

            StringBuilder heading = new StringBuilder();
            heading.append( headingMarkup );
            heading.append( " " );
            heading.append( name );
            heading.append( " " );
            heading.append( headingMarkup );
            heading.append( "\n\n" );

            Node node = new Node();
            node.setProject( getProject() );
            node.setTitle( name );
            node.setNodeType( nodeType );
            node.getManuscript().setText( heading.toString() );
            node.getManuscript().setSelectionStart( heading.length() );

            MarkdownTreeNode newTreeNode = new MarkdownTreeNode( node );
            if( child ) {
                projectFrame.getTree().addChild( treeNode, newTreeNode );
            }
            else {
                projectFrame.getTree().addSiblingAfter( treeNode, newTreeNode );
            }

            projectFrame.getTree().select( newTreeNode );
            
        }
    }

    public void documentsNewChildFile() {
        MarkdownTreeNode treeNode = getTreeNodeForCurrentDocument();
        documentsNewChildFile( treeNode );
    }

    public void documentsNewChildFile( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            Node currentNode = treeNode.getNode();
            String nodeType = currentNode.getNodeType();
            if( NodeTypes.RESOURCE.equals( nodeType ) || NodeTypes.RESOURCES.equals( nodeType ) ) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            newNode( NodeTypes.MARKDOWN, treeNode, true );
        }
    }

    public void documentsNewFile() {
        MarkdownTreeNode treeNode = getTreeNodeForCurrentDocument();
        documentsNewFile( treeNode );
    }

    public void documentsNewFile( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            Node currentNode = treeNode.getNode();
            String nodeType = currentNode.getNodeType();
            if( NodeTypes.MARKDOWN.equals( nodeType ) ) {
                documentsNewSiblingFile( treeNode );
            }
            else if( NodeTypes.RESOURCES.equals( nodeType ) ) {
                Toolkit.getDefaultToolkit().beep();
            }
            else if( NodeTypes.RESOURCE.equals( nodeType ) ) {
                Toolkit.getDefaultToolkit().beep();
            }
            else {
                documentsNewChildFile( treeNode );
            }
        }
    }

    public void documentsNewFolder() {
        MarkdownTreeNode treeNode = getTreeNodeForCurrentDocument();
        documentsNewFolder( treeNode );
    }

    public void documentsNewFolder( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            Node currentNode = treeNode.getNode();
            String nodeType = currentNode.getNodeType();
            if( NodeTypes.RESOURCE.equals( nodeType ) || NodeTypes.RESOURCES.equals( nodeType ) ) {
                Toolkit.getDefaultToolkit().beep();
            }
            else {
                documentsNewSiblingFolder( treeNode );
            }
        }
    }

    public void documentsNewSiblingFile() {
        MarkdownTreeNode treeNode = getTreeNodeForCurrentDocument();
        documentsNewSiblingFile( treeNode );
    }

    public void documentsNewSiblingFile( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            Node currentNode = treeNode.getNode();
            String nodeType = currentNode.getNodeType();
            if( NodeTypes.RESOURCE.equals( nodeType ) ) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            newNode( NodeTypes.MARKDOWN, treeNode, false );
        }
    }

    public void documentsNewChildFolder() {
        MarkdownTreeNode treeNode = getTreeNodeForCurrentDocument();
        documentsNewChildFolder( treeNode );
    }

    public void documentsNewChildFolder( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            Node currentNode = treeNode.getNode();
            String nodeType = currentNode.getNodeType();
            if( NodeTypes.RESOURCE.equals( nodeType ) || NodeTypes.RESOURCES.equals( nodeType ) ) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            newNode( NodeTypes.FOLDER, treeNode, true );
        }
    }

    public void documentsNewSiblingFolder() {
        MarkdownTreeNode treeNode = getTreeNodeForCurrentDocument();
        documentsNewSiblingFolder( treeNode );
    }

    public void documentsNewSiblingFolder( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            Node currentNode = treeNode.getNode();
            String nodeType = currentNode.getNodeType();
            if( NodeTypes.RESOURCE.equals( nodeType ) ) {
                Toolkit.getDefaultToolkit().beep();
                return;
            }

            newNode( NodeTypes.FOLDER, treeNode, false );
        }
    }

    public void documentsConvertToFile( Node node ) {
        if( node != null ) {
            if( NodeTypes.FOLDER.equals( node.getNodeType() ) ) {
                node.setNodeType( NodeTypes.MARKDOWN );
                repaintTree();
            }
            else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public void documentsConvertToFile() {
        Node markdownNode = getNodeForCurrentDocument();
        if( markdownNode != null ) {
            documentsConvertToFile( markdownNode );
        }
    }

    public void documentsConvertToFile( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            Node markdownNode = treeNode.getNode();
            documentsConvertToFile( markdownNode );
        }
    }

    public void documentsConvertToFolder() {
        Node markdownNode = getNodeForCurrentDocument();
        if( markdownNode != null ) {
            documentsConvertToFolder( markdownNode );
        }
    }

    public void documentsConvertToFolder( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            Node markdownNode = treeNode.getNode();
            documentsConvertToFolder( markdownNode );
        }
    }

    public void documentsConvertToFolder( Node node ) {
        if( node != null ) {
            if( NodeTypes.MARKDOWN.equals( node.getNodeType() ) ) {
                node.setNodeType( NodeTypes.FOLDER );
                repaintTree();
            }
            else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public void documentsImportImages() {
        JFileChooser fileChooser = getImageFileChooser();
        int option = fileChooser.showDialog( getPopupWindowOwner(), messages.fileChooserOpenProjectButton() );
        if( option == JFileChooser.APPROVE_OPTION ) {
            File[] files = fileChooser.getSelectedFiles();
            documentsImportImages( files );
        }
    }

    public void documentsImportImages( File[] files ) {
        try {
            for( File file : files ) {
                projectFrame.getTree().addImageResource( file );
            }
        }
        catch( Throwable t ) {
            t.printStackTrace();
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorImportingImages(),
                                           messages.errorImportingImagesTitle(),
                                           JOptionPane.ERROR_MESSAGE );
        }
    }

    public void documentsRename() {
        Node markdownNode = getNodeForCurrentDocument();
        if( markdownNode != null ) {
            documentsRename( markdownNode );
        }
    }

    public void documentsRename( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            Node markdownNode = treeNode.getNode();
            documentsRename( markdownNode );
        }
    }

    public void documentsRename( Node markdownNode ) {
        if( markdownNode != null ) {
            new RenameDocumentDialog( this, markdownNode ).setVisible( true );
        }
    }

    public void documentsDelete() {
        MarkdownTreeNode currentNode = getTreeNodeForCurrentDocument();
        documentsDelete( currentNode );
    }

    public void documentsDelete( MarkdownTreeNode treeNode ) {
        if( treeNode != null ) {
            projectFrame.getTree().moveToTrash( treeNode );
        }
    }

    public void documentsDelete( List<DefaultMutableTreeNode> nodes ) {
        projectFrame.getTree().moveToTrash( nodes );
    }

    public void documentsDuplicate() {
        MarkdownTreeNode currentNode = getTreeNodeForCurrentDocument();
        documentsDuplicate( currentNode );
    }

    public void documentsDuplicate( MarkdownTreeNode treeNode ) {
        try {
            if( treeNode != null ) {
                Node node = treeNode.getNode();
                if( node != null ) {
                    String name = showRenameDialog( node.getTitle() );
                    if( !StringUtils.empty( name ) ) {

                        if( NodeTypes.TRASH.equals( node.getNodeType() )
                            || NodeTypes.RESOURCES.equals( node.getNodeType() )
                            || NodeTypes.RESOURCE.equals( node.getNodeType() )
                            || NodeTypes.PROJECT.equals( node.getNodeType() ) ) {
                            return;
                        }

                        Node duplicate = ProjectIo.clone( node, projectFrame.getProject().getProjectDirectory(), false, false );
                        duplicate.setTitle( name );
                        if( NodeTypes.MANUSCRIPT.equals( duplicate.getNodeType() ) ) {
                            duplicate.setNodeType( NodeTypes.FOLDER );
                        }

                        MarkdownTreeNode newTreeNode = projectFrame.getTree().addNodes( treeNode, duplicate );
                        projectFrame.getTree().addSiblingAfter( treeNode, newTreeNode );
                        projectFrame.getTree().select( newTreeNode );
                    }
                }
            }
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringDuplicate(),
                                           messages.errorDuringDuplicateTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void documentsSplitAtCursor() {
        String name = showRenameDialog();
        if( !StringUtils.empty( name ) ) {
            documentsSplitAtCursor( name, false );
        }
    }

    public void documentsSplitAtCursorMakeSelectionTitle() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            String name = editor.getSelectedText();
            if( !StringUtils.empty( name ) ) {
                documentsSplitAtCursor( name, true );
            }
        }
    }

    public void documentsSplitAtCursor( String title, boolean highlightSelection ) {
        try {
            MarkdownTreeNode treeNode = getTreeNodeForCurrentDocument();
            Node node = getNodeForCurrentDocument();
            MarkdownEditor editor = getCurrentEditor();

            if( treeNode != null && node != null && editor != null ) {
                MarkdownEditorPanel editorPanel = projectFrame.getEditorSplitPane().getCurrentComponent();
                NodeSection section = editorPanel.getNodeSection();

                int selectionStart = editor.getSelectionStart();
                int selectionEnd = editor.getSelectionEnd();
                int index = Math.min( selectionStart, selectionEnd );

                String text = editor.getText();
                String firstDocument = text.substring( 0, index );
                String secondDocument = text.substring( index, text.length() );

                Node duplicate = ProjectIo.clone( node, projectFrame.getProject().getProjectDirectory(), false, false );
                duplicate.setTitle( title );

                if( NodeSection.MANUSCRIPT.equals( section ) ) {
                    DocumentUtils.setText( node.getManuscript(), firstDocument );
                    DocumentUtils.setText( duplicate.getManuscript(), secondDocument );
                }
                else if( NodeSection.DESCRIPTION.equals( section ) ) {
                    DocumentUtils.setText( node.getDescription(), firstDocument );
                    DocumentUtils.setText( duplicate.getDescription(), secondDocument );
                }
                else if( NodeSection.NOTES.equals( section ) ) {
                    DocumentUtils.setText( node.getNotes(), firstDocument );
                    DocumentUtils.setText( duplicate.getNotes(), secondDocument );
                }
                else if( NodeSection.SUMMARY.equals( section ) ) {
                    DocumentUtils.setText( node.getSummary(), firstDocument );
                    DocumentUtils.setText( duplicate.getSummary(), secondDocument );
                }
                else {
                    return;
                }

                MarkdownTreeNode newTreeNode = new MarkdownTreeNode( duplicate );
                projectFrame.getTree().addSiblingAfter( treeNode, newTreeNode );
                projectFrame.getTree().select( newTreeNode );

                if( highlightSelection ) {
                    selectionEnd = selectionEnd - selectionStart;
                    selectionStart = 0;
                    editor = getCurrentEditor();
                    editor.setCaretPosition( selectionStart );
                    editor.moveCaretPosition( selectionEnd );
                }
            }
        }
        catch( Throwable t ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorDuringSplit(),
                                           messages.errorDuringSplitTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public void documentsJoin() {
        List<DefaultMutableTreeNode> selectedNodes = projectFrame.getTree().getSelectedNodes();
        if( selectedNodes != null && selectedNodes.size() > 1 ) {

            List<MarkdownTreeNode> markdownTreeNodes = new ArrayList();
            List<Node> nodes = new ArrayList();
            for( DefaultMutableTreeNode node : selectedNodes ) {
                MarkdownTreeNode treeNode = (MarkdownTreeNode)node;
                markdownTreeNodes.add( treeNode );
                nodes.add( treeNode.getNode() );
            }

            ProjectIo.join( nodes );
            selectedNodes.remove( 0 );
            projectFrame.getTree().deleteFromTree( selectedNodes );
        }
    }

    public void documentsSelectInTree() {
        Node node = getNodeForCurrentDocument();
        projectFrame.getTree().selectNode( node.getUuid() );
    }

    public void documentsProjectStatistics() {
        ProjectStatisticsDialog projectStatisticsDialog = new ProjectStatisticsDialog( this );
        projectStatisticsDialog.loadPreferences();
        projectStatisticsDialog.refresh();
        projectStatisticsDialog.setVisible( true );
    }

    public void documentsEmptyTrash() {
        int result = JOptionPane.showConfirmDialog( getPopupWindowOwner(),
                                                    messages.dialogEmptyTrash(),
                                                    messages.dialogEmptyTrashTitle(),
                                                    JOptionPane.YES_NO_OPTION );
        if( result == JOptionPane.YES_OPTION ) {
            doDocumentsEmptyTrash();
        }
    }

    public void doDocumentsEmptyTrash() {
        Project project = getProject();
        projectFrame.getTree().emptyTrash();

        boolean error = false;

        Node trashNode = project.getTrash();
        for( Node node : trashNode.getChildNodes() ) {
            try {
                ProjectIo.stopSpellCheck( node );
            }
            catch( Throwable t ) {
                t.printStackTrace();
                error = true;
            }
        }
        trashNode.getChildNodes().clear();

        if( error ) {
            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           messages.errorEmptyingTrash(),
                                           messages.errorEmptyingTrashTitle(),
                                           JOptionPane.ERROR_MESSAGE );
        }
    }

    /////////////
    // Text tools
    /////////////
    public void toolsTextSelectionToUpperCase() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            int selectionStart = editor.getSelectionStart();
            int selectionEnd = editor.getSelectionEnd();
            String text = editor.getSelectedText();
            text = text.toUpperCase();
            editor.replaceSelection( text );
            editor.setCaretPosition( selectionStart );
            editor.moveCaretPosition( selectionEnd );
        }
    }

    public void toolsTextSelectionToLowerCase() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            int selectionStart = editor.getSelectionStart();
            int selectionEnd = editor.getSelectionEnd();
            String text = editor.getSelectedText();
            text = text.toLowerCase();
            editor.replaceSelection( text );
            editor.setCaretPosition( selectionStart );
            editor.moveCaretPosition( selectionEnd );
        }
    }

    public void toolsTextSelectionToCamelCase() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            int selectionStart = editor.getSelectionStart();
            int selectionEnd = editor.getSelectionEnd();
            String text = editor.getSelectedText();
            text = StringUtils.camelCase( text );
            editor.replaceSelection( text );
            editor.setCaretPosition( selectionStart );
            editor.moveCaretPosition( selectionEnd );
        }
    }

    public String getSpacesForTabs() {
        int spacesPerTab = MarkdownServer.getPreferences().getEditorPreferences().getSpacesPerTab();
        StringBuilder indent = new StringBuilder( spacesPerTab );
        for( int i = 0; i < spacesPerTab; i++ ) {
            indent.append( " " );
        }
        return indent.toString();
    }

    public void toolsTextSelectionTabsToSpaces() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            boolean softTabs = editor.getSoftTabs();
            int selectionStart = editor.getSelectionStart();
            int selectionEnd = editor.getSelectionEnd();
            String text = editor.getSelectedText();
            int initialLength = text.length();

            String indent = getSpacesForTabs();
            text = StringUtils.replaceAll( text, "\t", indent );

            int newLength = text.length();
            selectionEnd += newLength - initialLength;

            editor.setSoftTabs( false );
            editor.replaceSelection( text );
            editor.setSoftTabs( softTabs );
            editor.setCaretPosition( selectionStart );
            editor.moveCaretPosition( selectionEnd );
        }
    }

    public void toolsTextSelectionSpacesToTabs() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            boolean softTabs = editor.getSoftTabs();
            int selectionStart = editor.getSelectionStart();
            int selectionEnd = editor.getSelectionEnd();
            String text = editor.getSelectedText();
            int initialLength = text.length();

            String indent = getSpacesForTabs();
            text = StringUtils.replaceAll( text, indent, "\t" );

            int newLength = text.length();
            selectionEnd -= initialLength - newLength;

            editor.setSoftTabs( false );
            editor.replaceSelection( text );
            editor.setSoftTabs( softTabs );
            editor.setCaretPosition( selectionStart );
            editor.moveCaretPosition( selectionEnd );
        }
    }

    public void toolsTextCondenseMultipleSpaces() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            boolean softTabs = editor.getSoftTabs();
            int selectionStart = editor.getSelectionStart();
            int selectionEnd = editor.getSelectionEnd();
            String text = editor.getSelectedText();
            int initialLength = text.length();

            StringBuilder buffer = new StringBuilder( text );
            while( buffer.indexOf( "  " ) != -1 ) {
                StringUtils.replaceAll( buffer, "  ", " " );
            }
            text = buffer.toString();

            int newLength = text.length();
            selectionEnd -= initialLength - newLength;

            editor.setSoftTabs( false );
            editor.replaceSelection( text );
            editor.setSoftTabs( softTabs );
            editor.setCaretPosition( selectionStart );
            editor.moveCaretPosition( selectionEnd );
        }
    }

    public void toolsTextShiftIndentLeft() {
        try {
            MarkdownEditor editor = getCurrentEditor();
            if( editor != null ) {
                String indent = getIndent();
                int length = indent.length();

                int selectionStart = editor.getSelectionStart() - length;
                int selectionEnd = editor.getSelectionEnd();

                selectLines();
                String text = editor.getSelectedText();
                StringBuilder newText = new StringBuilder( text.length() );

                String[] lines = StringUtils.tokenize( text, "\n" );
                for( String line : lines ) {
                    if( line.startsWith( indent ) ) {
                        line = line.substring( length );
                        newText.append( line );
                        newText.append( "\n" );
                    }

                    selectionEnd -= length;
                }

                editor.replaceSelection( newText.toString() );
                editor.setCaretPosition( selectionStart );
                editor.moveCaretPosition( selectionEnd );
            }
        }
        catch( Throwable t ) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void toolsTextShiftIndentRight() {
        bookendSelectedLines( getIndent(), null );
    }

    public void toolsTextJoinLines() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            try {
                selectLines();
                String text = editor.getSelectedText();
                boolean newLine = text.endsWith( "\n" );

                String[] lines = StringUtils.tokenize( text, "\n" );
                StringBuilder joinedLines = new StringBuilder( StringUtils.cat( lines, " " ) );
                if( newLine ) {
                    joinedLines.append( "\n" );
                }

                editor.replaceSelection( joinedLines.toString() );
            }
            catch( Throwable t ) {
                Toolkit.getDefaultToolkit().beep();
                t.printStackTrace();
            }
        }
    }

    public void toolsTextDoubleNewlines() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            try {
                int selectionStart = Math.min( editor.getSelectionStart(), editor.getSelectionEnd() );
                int selectionEnd = Math.max( editor.getSelectionStart(), editor.getSelectionEnd() );

                int startLine = editor.getLineOfOffset( selectionStart );
                int endLine = editor.getLineOfOffset( selectionEnd );

                selectionStart = editor.getLineStartOffset( startLine );
                selectionEnd = editor.getLineEndOffset( endLine );

                editor.setCaretPosition( selectionStart );
                editor.moveCaretPosition( selectionEnd );

                String text = editor.getSelectedText();
                text = StringUtils.replaceAll( text, "\n", "\n\n" );
                editor.replaceSelection( text );

                editor.setCaretPosition( selectionStart );
                editor.moveCaretPosition( selectionStart + text.length() );
            }
            catch( Throwable t ) {
                Toolkit.getDefaultToolkit().beep();
                t.printStackTrace();
            }
        }
    }

    public void toolsTextSingleNewlines() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            try {
                int selectionStart = Math.min( editor.getSelectionStart(), editor.getSelectionEnd() );
                int selectionEnd = Math.max( editor.getSelectionStart(), editor.getSelectionEnd() );

                int startLine = editor.getLineOfOffset( selectionStart );
                int endLine = editor.getLineOfOffset( selectionEnd );

                selectionStart = editor.getLineStartOffset( startLine );
                selectionEnd = editor.getLineEndOffset( endLine );

                editor.setCaretPosition( selectionStart );
                editor.moveCaretPosition( selectionEnd );

                String text = editor.getSelectedText();
                text = StringUtils.replaceAll( text, "\n\n", "\n" );
                editor.replaceSelection( text );

                editor.setCaretPosition( selectionStart );
                editor.moveCaretPosition( selectionStart + text.length() );
            }
            catch( Throwable t ) {
                Toolkit.getDefaultToolkit().beep();
                t.printStackTrace();
            }
        }
    }

    public void toolsTextDeleteLines() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            try {
                int selectionStart = Math.min( editor.getSelectionStart(), editor.getSelectionEnd() );
                int selectionEnd = Math.max( editor.getSelectionStart(), editor.getSelectionEnd() );

                int startLine = editor.getLineOfOffset( selectionStart );
                int endLine = editor.getLineOfOffset( selectionEnd );

                selectionStart = editor.getLineStartOffset( startLine );
                selectionEnd = editor.getLineEndOffset( endLine );

                editor.setCaretPosition( selectionStart );
                editor.moveCaretPosition( selectionEnd );

                editor.replaceSelection( "" );
            }
            catch( Throwable t ) {
                Toolkit.getDefaultToolkit().beep();
                t.printStackTrace();
            }
        }
    }

    public void toolsTextDeleteToStartOfLine() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            try {
                int line = editor.getCaretLineNumber();
                int lineStartOffset = editor.getLineStartOffset( line );
                editor.moveCaretPosition( lineStartOffset );
                editor.replaceSelection( "" );
            }
            catch( Throwable t ) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public void toolsTextDeleteToEndOfLine() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            try {
                int line = editor.getCaretLineNumber();
                int lineEndOffset = editor.getLineEndOffset( line ) - 1;
                editor.moveCaretPosition( lineEndOffset );
                editor.replaceSelection( "" );
            }
            catch( Throwable t ) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }

    public void toolsTextInsertCurrentDate() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            String pattern = MarkdownServer.getPreferences().getGeneralPreferences().getDateFormat();
            SimpleDateFormat format = new SimpleDateFormat( pattern );
            editor.replaceSelection( format.format( new Date() ) );
        }
    }

    public void toolsTextInsertCurrentTime() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            String pattern = MarkdownServer.getPreferences().getGeneralPreferences().getTimeFormat();
            SimpleDateFormat format = new SimpleDateFormat( pattern );
            editor.replaceSelection( format.format( new Date() ) );
        }
    }

    public void toolsTextInsertCurrentDateAndTime() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            String pattern = MarkdownServer.getPreferences().getGeneralPreferences().getDatetimeFormat();
            SimpleDateFormat format = new SimpleDateFormat( pattern );
            editor.replaceSelection( format.format( new Date() ) );
        }
    }

    public void toolsExpandMacro() {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            List<MacroList> list = new ArrayList();
            list.add( projectFrame.getProject().getProjectMacros() );
            list.add( MarkdownServer.getPreferences().getGlobalMacros() );

            boolean expanded = MacroUtils.expandMacro( editor, list );
            if( !expanded ) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
        else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void toolsEditMacros() {
        projectFrame.getMacroEditorDialog().refresh();
        projectFrame.getMacroEditorDialog().setVisible( true );
    }

    public void helpPreferences() {
        PreferencesDialog preferencesDialog = MarkdownServer.getPreferencesDialog();
        preferencesDialog.setVisible( true );
    }

    ////////////////////////////////////
    // Document selection and navigation
    ////////////////////////////////////
    public void nullSelection() {
        if( getProject().synchronizeEditors() ) {
            List<MarkdownEditorPanel> editorPanels = projectFrame.getEditorSplitPane().getAllMarkdownEditorPanels();
            for( MarkdownEditorPanel editorPanel : editorPanels ) {
                editorPanel.edit( (Node)null );
            }
        }
        else {

            MarkdownEditorPanel editorPanel = projectFrame.getEditorSplitPane().getCurrentComponent();
            if( editorPanel != null ) {
                editorPanel.edit( (Node)null );
            }
        }
    }

    ///////////
    // Renaming
    ///////////
    public void projectRenamed() {
        String title = messages.titleUntitled();
        Project project = projectFrame.getProject();
        if( project != null ) {
            title = project.getTitle();
        }

        projectFrame.getWindow().setTitle( title + " - " + messages.applicationTitle() );
        projectFrame.getTree().projectRenamed();
    }

    public String showRenameDialog() {
        return showRenameDialog( null );
    }

    public String showRenameDialog( String originalName ) {
        if( StringUtils.empty( originalName ) ) {
            originalName = messages.titleUntitled();
        }

        Object result = JOptionPane.showInputDialog( getPopupWindowOwner(), messages.dialogMessageRename(), originalName );
        if( result != null ) {
            return result.toString();
        }

        return null;
    }

    /////////
    // Groovy
    /////////
    public GroovyShell getGroovyShell() {
        Binding binding = new Binding();
        binding.setVariable( "controller", this );
        binding.setVariable( "project", getProject() );
        binding.setVariable( "projectFrame", getProjectFrame() );
        binding.setVariable( "currentEditor", getCurrentEditor() );
        binding.setVariable( "currentNode", getNodeForCurrentDocument() );

        GroovyShell groovyShell = new GroovyShell( binding );
        return groovyShell;
    }

    public void executeGroovyScript( File file ) {
        try {
            getGroovyShell().evaluate( file );
        }
        catch( Throwable t ) {
            t.printStackTrace();

            StringBuilder message = new StringBuilder();
            message.append( t.getMessage() );
            StringUtils.replaceAll( message, "\n", "<br>\n" );

            JOptionPane.showMessageDialog( getPopupWindowOwner(),
                                           message.toString(),
                                           messages.errorExecutingGroovyTitle(),
                                           JOptionPane.ERROR_MESSAGE );
        }
    }

    public void reloadGroovyScripts() {
        projectFrame.getMenuBar().reloadGroovyScripts();
    }

    public void openGroovyScriptsDir() {
        try {
            File groovyScriptsDir = MarkdownServer.getGroovyScriptsDir();
            groovyScriptsDir.mkdirs();
            Desktop.getDesktop().open( groovyScriptsDir );
        }
        catch( Throwable t ) {
            t.printStackTrace();
            Toolkit.getDefaultToolkit().beep();
        }
    }

    ////////
    // Utils
    ////////
    public ProjectFrame getProjectFrame() {
        return projectFrame;
    }

    public Project getProject() {
        return getProjectFrame().getProject();
    }

    public MarkdownEditor getCurrentEditor() {
        MarkdownEditorPanel panel = projectFrame.getEditorSplitPane().getCurrentComponent();
        if( panel != null ) {
            return panel.getEditor();
        }

        return null;
    }

    public Node getNodeForCurrentDocument() {
        return projectFrame.getCurrentNode();
    }

    public MarkdownTreeNode getTreeNodeForCurrentDocument() {
        Node node = getNodeForCurrentDocument();
        if( node != null ) {
            return getTreeNodeForNode( node );
        }

        return null;
    }

    public MarkdownTreeNode getTreeNodeForNode( Node node ) {
        MarkdownTree tree = projectFrame.getTree();
        MarkdownTreeNode root = (MarkdownTreeNode)tree.getRootNode();
        return getTreeNodeForNode( node, root );
    }

    private MarkdownTreeNode getTreeNodeForNode( Node node, MarkdownTreeNode treeNode ) {
        Node testNode = treeNode.getNode();
        if( testNode != null ) {
            if( node.getUuid().equals( testNode.getUuid() ) ) {
                return treeNode;
            }
        }

        int childCount = treeNode.getChildCount();
        for( int i = 0; i < childCount; i++ ) {
            MarkdownTreeNode childNode = (MarkdownTreeNode)treeNode.getChildAt( i );
            MarkdownTreeNode result = getTreeNodeForNode( node, childNode );
            if( result != null ) {
                return result;
            }
        }

        return null;
    }

//    private SaveProjectDialog getSaveProjectDialog()
//    {
//        if( projectFrame != null )
//        {
//            if( projectFrame.getSaveFileChooser() != null )
//            {
//                return projectFrame.getSaveProjectDialog();
//            }
//        }
//
//        return MarkdownServer.getSaveProjectDialog();
//    }
    private JFileChooser getOpenFileChooser() {
        if( projectFrame != null ) {
            if( projectFrame.getOpenFileChooser() != null ) {
                return projectFrame.getOpenFileChooser();
            }
        }

        return MarkdownServer.getOpenFileChooser();
    }

    private JFileChooser getImageFileChooser() {
        if( projectFrame != null ) {
            if( projectFrame.getImageFileChooser() != null ) {
                return projectFrame.getImageFileChooser();
            }
        }

        return MarkdownServer.getImageFileChooser();
    }

    public Component getPopupWindowOwner() {
        if( projectFrame != null ) {
            return projectFrame.getWindow();
        }

        return null;
    }

    public void repaintTree() {
        GuiUtils.forceRepaint( projectFrame.getTree() );
    }

    public void selectLine()
        throws BadLocationException {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            int caretLine = editor.getCaretLineNumber();
            int lineStart = editor.getLineStartOffset( caretLine );
            int lineEnd = editor.getLineEndOffset( caretLine );
            editor.setCaretPosition( lineStart );
            editor.moveCaretPosition( lineEnd );
        }
        else {
            throw new IllegalArgumentException( "No editor set." );
        }
    }

    public void selectLines()
        throws BadLocationException {
        MarkdownEditor editor = getCurrentEditor();
        if( editor != null ) {
            int selectionStart = Math.min( editor.getSelectionStart(), editor.getSelectionEnd() );
            int selectionEnd = Math.max( editor.getSelectionStart(), editor.getSelectionEnd() );

            int startLine = editor.getLineOfOffset( selectionStart );
            int endLine = editor.getLineOfOffset( selectionEnd );

            selectionStart = editor.getLineStartOffset( startLine );
            selectionEnd = editor.getLineEndOffset( endLine );

            editor.setCaretPosition( selectionStart );
            editor.moveCaretPosition( selectionEnd );
        }
        else {
            throw new IllegalArgumentException( "No editor set." );
        }
    }

    public String getIndent() {
        int spacesPerTab = MarkdownServer.getPreferences().getEditorPreferences().getSpacesPerTab();
        StringBuilder indent = new StringBuilder( spacesPerTab );

        if( MarkdownServer.getPreferences().getEditorPreferences().softTabs() ) {
            for( int i = 0; i < spacesPerTab; i++ ) {
                indent.append( " " );
            }
        }
        else {
            indent.append( "\t" );
        }
        return indent.toString();
    }

    public MarkdownMessages getMessages() {
        return messages;
    }

    private static void stubCode() {
        System.out.println( "=========================" );
        System.out.println( "=========================" );
        System.out.println( "=== STUB CODE: FIX ME ===" );
        System.out.println( "=========================" );
        System.out.println( "=========================" );
    }

}
