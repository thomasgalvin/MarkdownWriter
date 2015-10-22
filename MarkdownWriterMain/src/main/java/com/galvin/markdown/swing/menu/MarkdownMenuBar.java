package com.galvin.markdown.swing.menu;

import com.galvin.markdown.model.Project;
import com.galvin.markdown.preferences.EditorPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.SystemUtils;
import galvin.swing.ThrowableDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MarkdownMenuBar
    extends JMenuBar
{
    private static final Logger logger = LoggerFactory.getLogger( MarkdownMenuBar.class );
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private Listener listener = new Listener();
    private Controller controller;
    private JMenu fileMenu = new JMenu( messages.menuBarFile() );
    //private JMenuItem fileNew = createMenuItem( messages.menuBarFileNew(), listener );
    private JMenu fileNew;
    private JMenuItem fileOpen = createMenuItem( messages.menuBarFileOpen(), listener );
    private JMenuItem fileSave = createMenuItem( messages.menuBarFileSave(), listener );
    private JMenuItem fileSaveAs = createMenuItem( messages.menuBarFileSaveAs(), listener );
    private JMenuItem fileSaveCopyAs = createMenuItem( messages.menuBarFileSaveCopyAs(), listener );
    private JMenuItem fileOpenTemplatesDir = createMenuItem( messages.menuBarOpenTemplatesDir(), listener );
    private JMenu fileExportMenu = new JMenu( messages.menuBarFileExport() );
    private JMenuItem fileExportProject = createMenuItem( messages.menuBarFileExportProject(), listener );
    private JMenuItem fileExportProjectUsingCurrentOptions = createMenuItem( messages.menuBarFileExportProjectUsingCurrentOptions(), listener );
    private JMenuItem fileExportCurrentDocument = createMenuItem( messages.menuBarFileExportCurrentDocument(), listener );
    private JMenuItem fileExportCurrentDocumentUsingCurrentOptions = createMenuItem( messages.menuBarFileExportCurrentDocumentUsingCurrentOptions(), listener );
    private JMenuItem fileExportCurrentDocumentAndChildren = createMenuItem( messages.menuBarFileExportCurrentDocumentAndChildren(), listener );
    private JMenuItem fileExportCurrentDocumentAndChildrenUsingCurrentOptions = createMenuItem( messages.menuBarFileExportCurrentDocumentAndChildrenUsingCurrentOptions(), listener );
    private JMenuItem fileShowExportDirectory = createMenuItem( messages.menuBarFileShowExportDirectory(), listener );
    private JMenu filePreviewMenu = new JMenu( messages.menuBarFilePreview() );
    private JMenuItem filePreviewCurrentDocument = createMenuItem( messages.menuBarFilePreviewCurrentDocument(), listener );
    private JMenuItem filePreviewCurrentDocumentAndChildren = createMenuItem( messages.menuBarFilePreviewCurrentDocumentAndChildren(), listener );
    private JMenuItem fileClose = createMenuItem( messages.menuBarFileClose(), listener );
    private JMenuItem fileCloseWithoutSaving = createMenuItem( messages.menuBarFileCloseWithoutSaving(), listener );
    private JMenuItem fileExit = createMenuItem( messages.menuBarFileExit(), listener );
    private JMenu editMenu = new JMenu( messages.menuBarEdit() );
    private JMenuItem editCut = createMenuItem( messages.menuBarEditCut(), listener );
    private JMenuItem editCopy = createMenuItem( messages.menuBarEditCopy(), listener );
    private JMenuItem editPaste = createMenuItem( messages.menuBarEditPaste(), listener );
    private JMenuItem editUndo = createMenuItem( messages.menuBarEditUndo(), listener );
    private JMenuItem editRedo = createMenuItem( messages.menuBarEditRedo(), listener );
    private JMenuItem editSelectAll = createMenuItem( messages.menuBarEditSelectAll(), listener );
    private JMenuItem editGoToLine = createMenuItem( messages.menuBarEditGoToLine(), listener );
    private JMenuItem editPrependToSelectedLines = createMenuItem( messages.menuBarEditPrependToSelectedLines(), listener );
    private JMenuItem editAppendToSelectedLines = createMenuItem( messages.menuBarEditAppendToSelectedLines(), listener );
    private JMenuItem editFindInProject = createMenuItem( messages.menuBarEditFindInProject(), listener );
    private JMenuItem editFindNext = createMenuItem( messages.menuBarEditFindNext(), listener );
    private JMenuItem editToggleLiveSpellcheck = createMenuItem( messages.menuBarEditTurnLiveSpellcheckOff(), listener );
    private JMenuItem editShowUserDictionary = createMenuItem( messages.menuBarEditShowUserDictionary(), listener );
    private JMenu formatMenu = new JMenu( messages.menuBarFormat() );
    private JMenuItem formatBold = createMenuItem( messages.menuBarFormatBold(), listener );
    private JMenuItem formatItalic = createMenuItem( messages.menuBarFormatItalic(), listener );
    private JMenuItem formatUnderline = createMenuItem( messages.menuBarFormatUnderline(), listener );
    private JMenuItem formatStrikethrough = createMenuItem( messages.menuBarFormatStrikethrough(), listener );
    private JMenuItem formatSmall = createMenuItem( messages.menuBarFormatSmall(), listener );
    private JMenuItem formatCenter = createMenuItem( messages.menuBarFormatCenter(), listener );
    private JMenuItem formatSuperscript = createMenuItem( messages.menuBarFormatSuperscript(), listener );
    private JMenuItem formatSubscript = createMenuItem( messages.menuBarFormatSubscript(), listener );
    private JMenuItem formatBlockquote = createMenuItem( messages.menuBarFormatBlockquote(), listener );
    private JMenuItem formatCode = createMenuItem( messages.menuBarFormatCode(), listener );
    private JMenuItem formatBulletedList = createMenuItem( messages.menuBarBulletedList(), listener );
    private JMenuItem formatH1 = createMenuItem( messages.menuBarFormatH1(), listener );
    private JMenuItem formatH2 = createMenuItem( messages.menuBarFormatH2(), listener );
    private JMenuItem formatH3 = createMenuItem( messages.menuBarFormatH3(), listener );
    private JMenuItem formatH4 = createMenuItem( messages.menuBarFormatH4(), listener );
    private JMenuItem formatH5 = createMenuItem( messages.menuBarFormatH5(), listener );
    private JMenuItem formatH6 = createMenuItem( messages.menuBarFormatH6(), listener );
    private JMenu insertMenu = new JMenu( messages.menuBarFormatInsert() );
    private JMenuItem formatInsertLink = createMenuItem( messages.menuBarFormatInsertLink(), listener );
    private JMenuItem formatInsertImage = createMenuItem( messages.menuBarFormatInsertImage(), listener );
    private JMenuItem formatInsertFootnote = createMenuItem( messages.menuBarFormatInsertFootnote(), listener );
    private JMenuItem formatInsertPageBreak = createMenuItem( messages.menuBarFormatInsertPageBreak(), listener );
    private JMenu viewMenu = new JMenu( messages.menuBarView() );
    private JMenuItem viewSplitHorizontal = createMenuItem( messages.menuBarViewSplitHorizontal(), listener );
    private JMenuItem viewSplitVertical = createMenuItem( messages.menuBarViewSplitVertical(), listener );
    private JMenuItem viewSplitUnsplit = createMenuItem( messages.menuBarViewUnsplit(), listener );
    private JMenuItem viewSynchronizeEditors = createMenuItem( messages.menuBarViewSynchronizeEditors(), listener );
    private JMenuItem viewLineNumbers = createMenuItem( messages.menuBarViewHideLineNumbers(), listener );
    private JMenuItem viewLineHighlighting = createMenuItem( messages.menuBarViewHideLineHighlitghts(), listener );
    private JMenuItem viewToggleWordWrap = createMenuItem( messages.menuBarViewTurnWordWrapOff(), listener );
    private JMenuItem viewToggleInvisibles = createMenuItem( messages.menuBarViewShowInvisibles(), listener );
    private JMenu documentsMenu = new JMenu( messages.menuBarDocuments() );
    private JMenuItem menuBarDocumentsNewFile = createMenuItem( messages.menuBarDocumentsNewFile(), listener );
    private JMenuItem menuBarDocumentsNewChildText = createMenuItem( messages.menuBarDocumentsNewChildText(), listener );
    private JMenuItem menuBarDocumentsNewFolder = createMenuItem( messages.menuBarDocumentsNewFolder(), listener );
    private JMenu addChild = new JMenu( messages.treeMenuAddChild() );
    private JMenuItem addChildText = createMenuItem( messages.treeMenuAddText(), listener );
    private JMenuItem addChildFolder = createMenuItem( messages.treeMenuAddFolder(), listener );
    private JMenu addSibling = new JMenu( messages.treeMenuAddSibling() );
    private JMenuItem addSiblingText = createMenuItem( messages.treeMenuAddText(), listener );
    private JMenuItem addSiblingFolder = createMenuItem( messages.treeMenuAddFolder(), listener );
    private JMenuItem documentsConvertToFile = createMenuItem( messages.menuBarDocumentsConvertToFile(), listener );
    private JMenuItem documentsConvertToFolder = createMenuItem( messages.menuBarDocumentsConvertToFolder(), listener );
    private JMenuItem documentsRename = createMenuItem( messages.menuBarDocumentsRename(), listener );
    private JMenuItem documentsImportImages = createMenuItem( messages.menuBarDocumentsImportImages(), listener );
    private JMenuItem documentsDelete = createMenuItem( messages.menuBarDocumentsDelete(), listener );
    private JMenuItem documentsDuplicate = createMenuItem( messages.menuBarDocumentsDuplicate(), listener );
    private JMenuItem documentsSplitAtCursor = createMenuItem( messages.menuBarDocumentsSplitAtCurson(), listener );
    private JMenuItem documentsSplitAtCursorMakeSelectionTitle = createMenuItem( messages.menuBarDocumentsSplitMakeSelectionTitle(), listener );
    private JMenuItem documentsJoin = createMenuItem( messages.menuBarDocumentsJoin(), listener );
    private JMenuItem documentsProjectStatistics = createMenuItem( messages.menuBarDocumentsStatistics(), listener );
    private JMenuItem documentsEmptyTrash = createMenuItem( messages.menuBarDocumentsEmptyTrash(), listener );
    private JMenuItem documentsSelectInTree = createMenuItem( messages.menuBarDocumentsSelectInTree(), listener );
    private JMenu textToolsMenu = new JMenu( messages.menuBarTextTools() );
    private JMenuItem toolsExpandMacro = createMenuItem( messages.menuBarExpandMacro(), listener );
    private JMenuItem toolsEditMacros = createMenuItem( messages.menuBarEditMacros(), listener );
    private JMenuItem toolsTextSelectionToUpperCase = createMenuItem( messages.menuBarTextSelectionToUpperCase(), listener );
    private JMenuItem toolsTextSelectionToLowerCase = createMenuItem( messages.menuBarTextSelectionToLowerCase(), listener );
    private JMenuItem toolsTextSelectionToCamelCase = createMenuItem( messages.menuBarTextSelectionToCamelCase(), listener );
    private JMenuItem toolsTextSelectionTabsToSpaces = createMenuItem( messages.menuBarTextSelectionTabsToSpaces(), listener );
    private JMenuItem toolsTextSelectionSpacesToTabs = createMenuItem( messages.menuBarTextSelectionSpacesToTabs(), listener );
    private JMenuItem toolsTextCondenseMultipleSpaces = createMenuItem( messages.menuBarTextSelectionCondenseMultipleSpaces(), listener );
    private JMenuItem toolsTextShiftIndentLeft = createMenuItem( messages.menuBarTextShiftIndentLeft(), listener );
    private JMenuItem toolsTextShiftIndentRight = createMenuItem( messages.menuBarTextShiftIndentRight(), listener );
    private JMenuItem toolsTextJoinLines = createMenuItem( messages.menuBarTextJoinLines(), listener );
    private JMenuItem toolsTextDeleteLines = createMenuItem( messages.menuBarTextDeleteLines(), listener );
    private JMenuItem toolsTextDeleteToStartOfLine = createMenuItem( messages.menuBarTextDeleteToStartOfLine(), listener );
    private JMenuItem toolsTextDeleteToEndOfLine = createMenuItem( messages.menuBarTextDeleteToEndOfLine(), listener );
    private JMenuItem toolsTextDoubleNewlines = createMenuItem( messages.menuBarTextDoubleNewlines(), listener );
    private JMenuItem toolsTextSingleNewlines = createMenuItem( messages.menuBarTextSingleNewlines(), listener );
    private JMenuItem toolsTextInsertCurrentDate = createMenuItem( messages.menuBarTextInsertCurrentDate(), listener );
    private JMenuItem toolsTextInsertCurrentTime = createMenuItem( messages.menuBarTextInsertCurrentTime(), listener );
    private JMenuItem toolsTextInsertCurrentDateAndTime = createMenuItem( messages.menuBarTextInsertCurrentDateAndTime(), listener );
    private JMenu helpMenu = new JMenu( messages.menuBarHelp() );
    private JMenuItem helpShowHelp = createMenuItem( messages.menuBarHelpShow(), listener );
    private JMenuItem helpAbout = createMenuItem( messages.menuBarHelpAbout(), listener );
    private JMenuItem helpPreferences = createMenuItem( messages.menuBarHelpPreferences(), listener );
    private GroovyMenu groovyMenu;
    private RecentProjectsMenu recentProjectsMenu;

    public MarkdownMenuBar( Controller controller )
    {
        this.controller = controller;

        fileNew = new NewProjectMenu( controller );
        recentProjectsMenu = new RecentProjectsMenu( controller );

        fileMenu.add( fileNew );
        fileMenu.add( fileOpen );
        fileMenu.add( recentProjectsMenu );
        fileMenu.addSeparator();
        fileMenu.add( fileSave );
        fileMenu.add( fileSaveAs );
        fileMenu.add( fileSaveCopyAs );
        fileMenu.addSeparator();
        
        fileMenu.add( fileExportMenu );
        fileExportMenu.add( fileExportProject );
        fileExportMenu.add( fileExportProjectUsingCurrentOptions );
        fileExportMenu.addSeparator();
        fileExportMenu.add( fileExportCurrentDocument );
        fileExportMenu.add( fileExportCurrentDocumentUsingCurrentOptions );
        fileExportMenu.addSeparator();
        fileExportMenu.add( fileExportCurrentDocumentAndChildren );
        fileExportMenu.add( fileExportCurrentDocumentAndChildrenUsingCurrentOptions );
        fileExportMenu.addSeparator();
        fileExportMenu.add( fileShowExportDirectory );
        
        fileMenu.add( filePreviewMenu );
        filePreviewMenu.add( filePreviewCurrentDocument );
        filePreviewMenu.add( filePreviewCurrentDocumentAndChildren );

        fileMenu.addSeparator();
        fileMenu.add( fileOpenTemplatesDir );
        fileMenu.addSeparator();
        fileMenu.add( fileClose );
        fileMenu.add( fileCloseWithoutSaving );

        if( !SystemUtils.IS_MAC )
        {
            fileMenu.addSeparator();
            fileMenu.add( fileExit );
        }

        editMenu.add( editCut );
        editMenu.add( editCopy );
        editMenu.add( editPaste );
        editMenu.addSeparator();
        editMenu.add( editUndo );
        editMenu.add( editRedo );
        editMenu.addSeparator();
        editMenu.add( editSelectAll );
        editMenu.add( editGoToLine );
        editMenu.addSeparator();
        editMenu.add( editFindInProject );
        editMenu.add( editFindNext );
        editMenu.addSeparator();
        editMenu.add( formatMenu );
        editMenu.add( insertMenu );
        editMenu.add( textToolsMenu );
        editMenu.addSeparator();
        editMenu.add( toolsExpandMacro );
        editMenu.add( toolsEditMacros );
        editMenu.addSeparator();
        editMenu.add( editToggleLiveSpellcheck );
        editMenu.add( editShowUserDictionary );

        formatMenu.add( formatBold );
        formatMenu.add( formatItalic );
        formatMenu.add( formatUnderline );
        formatMenu.add( formatStrikethrough );
        formatMenu.add( formatCode );
        formatMenu.add( formatSmall );
        formatMenu.add( formatCenter );
        formatMenu.add( formatSuperscript );
        formatMenu.add( formatSubscript );
        formatMenu.addSeparator();
        formatMenu.add( formatBulletedList );
        formatMenu.addSeparator();
        formatMenu.add( formatBlockquote );
        formatMenu.addSeparator();
        formatMenu.add( formatH1 );
        formatMenu.add( formatH2 );
        formatMenu.add( formatH3 );
        formatMenu.add( formatH4 );
        formatMenu.add( formatH5 );
        formatMenu.add( formatH6 );

        insertMenu.add( formatInsertLink );
        insertMenu.add( formatInsertImage );
        insertMenu.add( formatInsertFootnote );
        insertMenu.addSeparator();
        insertMenu.add( formatInsertPageBreak );
        insertMenu.addSeparator();
        insertMenu.add( toolsTextInsertCurrentDate );
        insertMenu.add( toolsTextInsertCurrentTime );
        insertMenu.add( toolsTextInsertCurrentDateAndTime );

        textToolsMenu.add( toolsTextSelectionToUpperCase );
        textToolsMenu.add( toolsTextSelectionToLowerCase );
        textToolsMenu.add( toolsTextSelectionToCamelCase );
        textToolsMenu.addSeparator();
        textToolsMenu.add( toolsTextSelectionTabsToSpaces );
        textToolsMenu.add( toolsTextSelectionSpacesToTabs );
        textToolsMenu.add( toolsTextCondenseMultipleSpaces );
        textToolsMenu.addSeparator();
        textToolsMenu.add( toolsTextDoubleNewlines );
        textToolsMenu.add( toolsTextSingleNewlines );
        textToolsMenu.addSeparator();
        textToolsMenu.add( toolsTextShiftIndentLeft );
        textToolsMenu.add( toolsTextShiftIndentRight );
        textToolsMenu.addSeparator();
        textToolsMenu.add( toolsTextJoinLines );
        textToolsMenu.add( toolsTextDeleteLines );
        textToolsMenu.addSeparator();
        textToolsMenu.add( toolsTextDeleteToStartOfLine );
        textToolsMenu.add( toolsTextDeleteToEndOfLine );
        textToolsMenu.addSeparator();
        textToolsMenu.add( editPrependToSelectedLines );
        textToolsMenu.add( editAppendToSelectedLines );

        viewMenu.add( viewSplitVertical );
        viewMenu.add( viewSplitHorizontal );
        viewMenu.add( viewSplitUnsplit );
        viewMenu.addSeparator();
        viewMenu.add( viewSynchronizeEditors );
        viewMenu.addSeparator();
        viewMenu.add( viewLineHighlighting );
        viewMenu.add( viewLineNumbers );
        viewMenu.addSeparator();
        viewMenu.add( viewToggleWordWrap );
        viewMenu.addSeparator();
        viewMenu.add( viewToggleInvisibles );

        documentsMenu.add( menuBarDocumentsNewFile );
        documentsMenu.add( menuBarDocumentsNewChildText );
        documentsMenu.add( menuBarDocumentsNewFolder );
        documentsMenu.addSeparator();
        documentsMenu.add( addSibling );
        addSibling.add( addSiblingText );
        addSibling.add( addSiblingFolder );
        documentsMenu.add( addChild );
        addChild.add( addChildText );
        addChild.add( addChildFolder );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsConvertToFile );
        documentsMenu.add( documentsConvertToFolder );
        documentsMenu.add( documentsRename );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsDuplicate );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsSelectInTree );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsSplitAtCursor );
        documentsMenu.add( documentsSplitAtCursorMakeSelectionTitle );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsJoin );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsImportImages );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsProjectStatistics );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsDelete );
        documentsMenu.add( documentsEmptyTrash );

        helpMenu.add( helpShowHelp );

        if( !SystemUtils.IS_MAC )
        {
            helpMenu.addSeparator();
            helpMenu.add( helpAbout );
        }


        helpMenu.addSeparator();
        helpMenu.add( helpPreferences );

        groovyMenu = new GroovyMenu( controller );

        fileOpen.setMnemonic( 'O' );
        fileOpen.setAccelerator( KeyStroke.getKeyStroke( 'O', SystemUtils.PREFERED_MODIFIER_KEY ) );

        fileSave.setMnemonic( 'S' );
        fileSave.setAccelerator( KeyStroke.getKeyStroke( 'S', SystemUtils.PREFERED_MODIFIER_KEY ) );

        fileClose.setMnemonic( 'W' );
        fileClose.setAccelerator( KeyStroke.getKeyStroke( 'W', SystemUtils.PREFERED_MODIFIER_KEY ) );

        fileExit.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_F4, SystemUtils.SECONDARY_MODIFIER_KEY ) );

        fileExportProject.setAccelerator( KeyStroke.getKeyStroke( 'E', SystemUtils.PREFERED_MODIFIER_KEY ) );
        fileExportProjectUsingCurrentOptions.setAccelerator( KeyStroke.getKeyStroke( 'E', SystemUtils.PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );

        filePreviewCurrentDocument.setAccelerator( KeyStroke.getKeyStroke( 'P', SystemUtils.PREFERED_MODIFIER_KEY ) );
        filePreviewCurrentDocumentAndChildren.setAccelerator( KeyStroke.getKeyStroke( 'P', SystemUtils.PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );

        editCut.setAccelerator( KeyStroke.getKeyStroke( 'X', SystemUtils.PREFERED_MODIFIER_KEY ) );
        editCopy.setAccelerator( KeyStroke.getKeyStroke( 'C', SystemUtils.PREFERED_MODIFIER_KEY ) );
        editPaste.setAccelerator( KeyStroke.getKeyStroke( 'V', SystemUtils.PREFERED_MODIFIER_KEY ) );

        editUndo.setAccelerator( KeyStroke.getKeyStroke( 'Z', SystemUtils.PREFERED_MODIFIER_KEY ) );
        editRedo.setAccelerator( KeyStroke.getKeyStroke( 'Y', SystemUtils.PREFERED_MODIFIER_KEY ) );

        editSelectAll.setAccelerator( KeyStroke.getKeyStroke( 'A', SystemUtils.PREFERED_MODIFIER_KEY ) );
        editGoToLine.setAccelerator( KeyStroke.getKeyStroke( 'L', SystemUtils.PREFERED_MODIFIER_KEY ) );

        editFindInProject.setAccelerator( KeyStroke.getKeyStroke( 'F', SystemUtils.PREFERED_MODIFIER_KEY ) );
        editFindNext.setAccelerator( KeyStroke.getKeyStroke( 'G', SystemUtils.PREFERED_MODIFIER_KEY ) );

        formatBold.setAccelerator( KeyStroke.getKeyStroke( 'B', SystemUtils.PREFERED_MODIFIER_KEY ) );
        formatItalic.setAccelerator( KeyStroke.getKeyStroke( 'I', SystemUtils.PREFERED_MODIFIER_KEY ) );
        formatUnderline.setAccelerator( KeyStroke.getKeyStroke( 'U', SystemUtils.PREFERED_MODIFIER_KEY ) );

        viewSplitHorizontal.setAccelerator( KeyStroke.getKeyStroke( '3', SystemUtils.PREFERED_MODIFIER_KEY ) );
        viewSplitVertical.setAccelerator( KeyStroke.getKeyStroke( '2', SystemUtils.PREFERED_MODIFIER_KEY ) );
        viewSplitUnsplit.setAccelerator( KeyStroke.getKeyStroke( '0', SystemUtils.PREFERED_MODIFIER_KEY ) );

        documentsRename.setAccelerator( KeyStroke.getKeyStroke( 'R', SystemUtils.PREFERED_MODIFIER_KEY ) );

        menuBarDocumentsNewFile.setAccelerator( KeyStroke.getKeyStroke( 'N', SystemUtils.PREFERED_MODIFIER_KEY ) );
        menuBarDocumentsNewChildText.setAccelerator( KeyStroke.getKeyStroke( 'N', SystemUtils.PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );
        menuBarDocumentsNewFolder.setAccelerator( KeyStroke.getKeyStroke( 'N', SystemUtils.SECONDARY_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );

        toolsExpandMacro.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_SEMICOLON, SystemUtils.PREFERED_MODIFIER_KEY ) );

        toolsTextSelectionToUpperCase.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_U, SystemUtils.PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );
        toolsTextSelectionToLowerCase.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_L, SystemUtils.PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );

        toolsTextShiftIndentLeft.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_LEFT, SystemUtils.SECONDARY_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );
        toolsTextShiftIndentRight.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT, SystemUtils.SECONDARY_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );

        configure( MarkdownServer.getPreferences() );

        add( fileMenu );
        add( editMenu );
        add( groovyMenu );
        add( viewMenu );
        add( documentsMenu );
        add( helpMenu );
        
        Preferences preferences = MarkdownServer.getPreferences();
        EditorPreferences editorPreferences = preferences.getEditorPreferences();
        setBackground( editorPreferences.getBackgroundColor() );
        setForeground( editorPreferences.getTextColor() );
        
        fileMenu.setForeground( editorPreferences.getTextColor() );
        editMenu.setForeground( editorPreferences.getTextColor() );
        viewMenu.setForeground( editorPreferences.getTextColor() );
        documentsMenu.setForeground( editorPreferences.getTextColor() );
        helpMenu.setForeground( editorPreferences.getTextColor() );
        groovyMenu.setForeground( editorPreferences.getTextColor() );
    }

    private static JMenuItem createMenuItem( String label, ActionListener listener )
    {
        JMenuItem item = new JMenuItem( label );
        item.addActionListener( listener );
        return item;
    }

    public void reloadGroovyScripts()
    {
        groovyMenu.reloadGroovyScripts();
    }

    public void reloadRecentProjects()
    {
        recentProjectsMenu.reloadRecentProjects();
    }

    private class Listener
        implements ActionListener
    {
        @Override
        public void actionPerformed( ActionEvent e )
        {
            final Object source = e.getSource();

            Thread thread = new Thread()
            {
                @Override
                public void run()
                {
                    if( source == fileNew )
                    {
                        controller.fileNew();
                    }
                    else if( source == fileOpen )
                    {
                        controller.fileOpen();
                    }
                    else if( source == fileSave )
                    {
                        controller.fileSave();
                    }
                    else if( source == fileSaveAs )
                    {
                        controller.fileSaveAs();
                    }
                    else if( source == fileSaveCopyAs )
                    {
                        controller.fileSaveCopyAs();
                    }
                    else if( source == fileOpenTemplatesDir )
                    {
                        controller.fileOpenTemplatesDir();
                    }
                    else if( source == fileExportProject )
                    {
                        controller.fileExportProject();
                    }
                    else if( source == fileExportProjectUsingCurrentOptions )
                    {
                        controller.fileExportProjectUsingCurrentOptions();
                    }
                    else if( source == fileExportCurrentDocument )
                    {
                        controller.fileExportCurrentDocument();
                    }
                    else if( source == fileExportCurrentDocumentUsingCurrentOptions )
                    {
                        controller.fileExportCurrentDocumentUsingCurrentOptions();
                    }
                    else if( source == fileExportCurrentDocumentAndChildren )
                    {
                        controller.fileExportCurrentDocumentAndChildren();
                    }
                    else if( source == fileExportCurrentDocumentAndChildrenUsingCurrentOptions )
                    {
                        controller.fileExportCurrentDocumentAndChildrenUsingCurrentOptionst();
                    }
                    else if( source == fileShowExportDirectory )
                    {
                        controller.fileShowExportDirectory();
                    }
                    else if( source == fileClose )
                    {
                        try
                        {
                            controller.fileClose();
                        }
                        catch( Throwable t )
                        {
                            logger.error( "Error in close event handler", t );
                            ThrowableDialog dialog = new ThrowableDialog( t );
                            dialog.setVisible( true );
                        }
                    }
                    else if( source == fileCloseWithoutSaving )
                    {
                        controller.fileCloseWithoutSaving();
                    }
                    else if( source == fileExit )
                    {
                        controller.fileExit();
                    }
                    else if( source == editCut )
                    {
                        controller.editCut();
                    }
                    else if( source == editCopy )
                    {
                        controller.editCopy();
                    }
                    else if( source == editPaste )
                    {
                        controller.editPaste();
                    }
                    else if( source == editUndo )
                    {
                        controller.editUndo();
                    }
                    else if( source == editRedo )
                    {
                        controller.editRedo();
                    }
                    else if( source == editSelectAll )
                    {
                        controller.editSelectAll();
                    }
                    else if( source == editGoToLine )
                    {
                        controller.editGoToLine();
                    }
                    else if( source == editPrependToSelectedLines )
                    {
                        controller.editPrependToSelectedLines();
                    }
                    else if( source == editAppendToSelectedLines )
                    {
                        controller.editAppendToSelectedLines();
                    }
                    else if( source == editFindInProject )
                    {
                        controller.editFindInProject();
                    }
                    else if( source == editFindNext )
                    {
                        controller.editFindNext();
                    }
                    else if( source == editToggleLiveSpellcheck )
                    {
                        controller.editToggleLiveSpellcheck();
                    }
                    else if( source == editShowUserDictionary )
                    {
                        controller.editShowUserDictionary();
                    }
                    else if( source == formatBold )
                    {
                        controller.formatBold();
                    }
                    else if( source == formatItalic )
                    {
                        controller.formatItalic();
                    }
                    else if( source == formatUnderline )
                    {
                        controller.formatUnderline();
                    }
                    else if( source == formatStrikethrough )
                    {
                        controller.formatStrikethrough();
                    }
                    else if( source == formatSmall )
                    {
                        controller.formatSmall();
                    }
                    else if( source == formatCenter )
                    {
                        controller.formatCenter();
                    }
                    else if( source == formatSuperscript )
                    {
                        controller.formatSuperscript();
                    }
                    else if( source == formatSubscript )
                    {
                        controller.formatSubcript();
                    }
                    else if( source == formatBlockquote )
                    {
                        controller.formatBlockquote();
                    }
                    else if( source == formatBulletedList )
                    {
                        controller.formatBulletedList();
                    }
                    else if( source == formatCode )
                    {
                        controller.formatCode();
                    }
                    else if( source == formatH1 )
                    {
                        controller.formatH1();
                    }
                    else if( source == formatH2 )
                    {
                        controller.formatH2();
                    }
                    else if( source == formatH3 )
                    {
                        controller.formatH3();
                    }
                    else if( source == formatH4 )
                    {
                        controller.formatH4();
                    }
                    else if( source == formatH5 )
                    {
                        controller.formatH5();
                    }
                    else if( source == formatH6 )
                    {
                        controller.formatH6();
                    }
                    else if( source == formatInsertLink )
                    {
                        controller.formatInsertLink();
                    }
                    else if( source == formatInsertImage )
                    {
                        controller.formatInsertImage();
                    }
                    else if( source == formatInsertFootnote )
                    {
                        controller.formatInsertFootnote();
                    }
                    else if( source == formatInsertPageBreak ){
                        controller.formatInsertPageBreak();
                    }
                    else if( source == filePreviewCurrentDocument )
                    {
                        controller.filePreview();
                    }
                    else if( source == filePreviewCurrentDocumentAndChildren )
                    {
                        controller.filePreviewWithChildren();
                    }
                    else if( source == viewSplitHorizontal )
                    {
                        controller.viewSplitHorizontal();
                    }
                    else if( source == viewSplitVertical )
                    {
                        controller.viewSplitVertical();
                    }
                    else if( source == viewSplitUnsplit )
                    {
                        controller.viewSplitUnsplit();
                    }
                    else if( source == viewSynchronizeEditors )
                    {
                        controller.viewSynchronizeEditors();
                    }
                    else if( source == viewLineNumbers )
                    {
                        controller.viewToggleLineNumbers();
                    }
                    else if( source == viewLineHighlighting )
                    {
                        controller.viewLineHighlighting();
                    }
                    else if( source == viewToggleWordWrap )
                    {
                        controller.viewToggleWordWrap();
                    }
                    else if( source == viewToggleInvisibles )
                    {
                        controller.viewToggleInvisibles();
                    }
                    else if( source == menuBarDocumentsNewFile )
                    {
                        controller.documentsNewFile();
                    }
                    else if( source == menuBarDocumentsNewFolder )
                    {
                        controller.documentsNewFolder();
                    }
                    else if( source == addChildText )
                    {
                        controller.documentsNewChildFile();
                    } 
                    else if( source == menuBarDocumentsNewChildText ){
                        controller.documentsNewChildFile();
                    }
                    else if( source == addChildFolder )
                    {
                        controller.documentsNewChildFolder();
                    }
                    else if( source == addSiblingText )
                    {
                        controller.documentsNewSiblingFile();
                    }
                    else if( source == addSiblingFolder )
                    {
                        controller.documentsNewSiblingFolder();
                    }
                    else if( source == documentsConvertToFile )
                    {
                        controller.documentsConvertToFile();
                    }
                    else if( source == documentsConvertToFolder )
                    {
                        controller.documentsConvertToFolder();
                    }
                    else if( source == documentsRename )
                    {
                        controller.documentsRename();
                    }
                    else if( source == documentsImportImages )
                    {
                        controller.documentsImportImages();
                    }
                    else if( source == documentsDelete )
                    {
                        controller.documentsDelete();
                    }
                    else if( source == documentsSelectInTree )
                    {
                        controller.documentsSelectInTree();
                    }
                    else if( source == documentsDuplicate )
                    {
                        controller.documentsDuplicate();
                    }
                    else if( source == documentsSplitAtCursor )
                    {
                        controller.documentsSplitAtCursor();
                    }
                    else if( source == documentsSplitAtCursorMakeSelectionTitle )
                    {
                        controller.documentsSplitAtCursorMakeSelectionTitle();
                    }
                    else if( source == documentsJoin )
                    {
                        controller.documentsJoin();
                    }
                    else if( source == documentsProjectStatistics )
                    {
                        controller.documentsProjectStatistics();
                    }
                    else if( source == documentsEmptyTrash )
                    {
                        controller.documentsEmptyTrash();
                    }
                    else if( source == toolsExpandMacro )
                    {
                        controller.toolsExpandMacro();
                    }
                    else if( source == toolsEditMacros )
                    {
                        controller.toolsEditMacros();
                    }
                    else if( source == toolsTextSelectionToUpperCase )
                    {
                        controller.toolsTextSelectionToUpperCase();
                    }
                    else if( source == toolsTextSelectionToLowerCase )
                    {
                        controller.toolsTextSelectionToLowerCase();
                    }
                    else if( source == toolsTextSelectionToCamelCase )
                    {
                        controller.toolsTextSelectionToCamelCase();
                    }
                    else if( source == toolsTextSelectionTabsToSpaces )
                    {
                        controller.toolsTextSelectionTabsToSpaces();
                    }
                    else if( source == toolsTextSelectionSpacesToTabs )
                    {
                        controller.toolsTextSelectionSpacesToTabs();
                    }
                    else if( source == toolsTextCondenseMultipleSpaces )
                    {
                        controller.toolsTextCondenseMultipleSpaces();
                    }
                    else if( source == toolsTextShiftIndentLeft )
                    {
                        controller.toolsTextShiftIndentLeft();
                    }
                    else if( source == toolsTextShiftIndentRight )
                    {
                        controller.toolsTextShiftIndentRight();
                    }
                    else if( source == toolsTextJoinLines )
                    {
                        controller.toolsTextJoinLines();
                    }
                    else if( source == toolsTextDeleteLines )
                    {
                        controller.toolsTextDeleteLines();
                    }
                    else if( source == toolsTextDoubleNewlines )
                    {
                        controller.toolsTextDoubleNewlines();
                    }
                    else if( source == toolsTextSingleNewlines )
                    {
                        controller.toolsTextSingleNewlines();
                    }
                    else if( source == toolsTextDeleteToStartOfLine )
                    {
                        controller.toolsTextDeleteToStartOfLine();
                    }
                    else if( source == toolsTextDeleteToEndOfLine )
                    {
                        controller.toolsTextDeleteToEndOfLine();
                    }
                    else if( source == toolsTextInsertCurrentDate )
                    {
                        controller.toolsTextInsertCurrentDate();
                    }
                    else if( source == toolsTextInsertCurrentTime )
                    {
                        controller.toolsTextInsertCurrentTime();
                    }
                    else if( source == toolsTextInsertCurrentDateAndTime )
                    {
                        controller.toolsTextInsertCurrentDateAndTime();
                    }
                    else if( source == helpShowHelp )
                    {
                        MarkdownServer.helpShowHelp();
                    }
                    else if( source == helpAbout )
                    {
                        MarkdownServer.helpAbout();
                    }
                    else if( source == helpPreferences )
                    {
                        controller.helpPreferences();
                    }
                }
            };
            SwingUtilities.invokeLater( thread );
        }
    }

    public void configure( Preferences preferences )
    {
        EditorPreferences editorPreferences = preferences.getEditorPreferences();

        if( editorPreferences.showLineNumbers() )
        {
            viewLineNumbers.setText( messages.menuBarViewHideLineNumbers() );
        }
        else
        {
            viewLineNumbers.setText( messages.menuBarViewShowLineNumbers() );
        }

        if( editorPreferences.showInvisibles() )
        {
            viewToggleInvisibles.setText( messages.menuBarViewHideInvisibles() );
        }
        else
        {
            viewToggleInvisibles.setText( messages.menuBarViewShowInvisibles() );
        }

        if( editorPreferences.liveSpellCheck() )
        {
            editToggleLiveSpellcheck.setText( messages.menuBarEditTurnLiveSpellcheckOff() );
        }
        else
        {
            editToggleLiveSpellcheck.setText( messages.menuBarEditTurnLiveSpellcheckOn() );
        }

        if( editorPreferences.lineWrap() )
        {
            viewToggleWordWrap.setText( messages.menuBarViewTurnWordWrapOff() );
        }
        else
        {
            viewToggleWordWrap.setText( messages.menuBarViewTurnWordWrapOn() );
        }

        if( editorPreferences.highlightLines() )
        {
            viewLineHighlighting.setText( messages.menuBarViewHideLineHighlitghts() );
        }
        else
        {
            viewLineHighlighting.setText( messages.menuBarViewShowLineHighlitghts() );
        }

        Project project = controller.getProject();
        if( project.synchronizeEditors() )
        {
            viewSynchronizeEditors.setText( messages.menuBarViewStopSynchronizingEditors() );
        }
        else
        {
            viewSynchronizeEditors.setText( messages.menuBarViewSynchronizeEditors() );
        }
    }
}
