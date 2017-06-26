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
    extends JMenuBar {
    private static final Logger logger = LoggerFactory.getLogger( MarkdownMenuBar.class );
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private Listener listener = new Listener();
    private Controller controller;
    private JMenu fileMenu = new JMenu( messages.menuFile() );
    private JMenu fileNew;
    private JMenuItem fileOpen = createMenuItem( messages.menuOpen(), listener );
    private JMenuItem fileSave = createMenuItem( messages.menuSave(), listener );
    private JMenuItem fileSaveAs = createMenuItem( messages.menuSaveAs(), listener );
    private JMenuItem fileSaveCopyAs = createMenuItem( messages.menuSaveCopyAs(), listener );
    private JMenuItem fileOpenTemplatesDir = createMenuItem( messages.menuOpenTemplatesDir(), listener );
    private JMenu fileExportMenu = new JMenu( messages.menuExport() );
    private JMenuItem fileExportProject = createMenuItem( messages.menuExportProject(), listener );
    private JMenuItem fileExportProjectUsingCurrentOptions = createMenuItem( messages.menuExportProjectCurrentOptions(), listener );
    private JMenuItem fileExportCurrentDocument = createMenuItem( messages.menuExportDocument(), listener );
    private JMenuItem fileExportCurrentDocumentUsingCurrentOptions = createMenuItem( messages.menuExportDocumentCurrentOptions(), listener );
    private JMenuItem fileExportCurrentDocumentAndChildren = createMenuItem( messages.menuExportDocumentAndChildren(), listener );
    private JMenuItem fileExportCurrentDocumentAndChildrenUsingCurrentOptions = createMenuItem( messages.menuExportDocumentAndChildrenCurrentOptions(), listener );
    private JMenuItem fileShowExportDirectory = createMenuItem( messages.menuOpenExportFolder(), listener );
    private JMenu filePreviewMenu = new JMenu( messages.menuPreview() );
    private JMenuItem filePreviewCurrentDocument = createMenuItem( messages.menuPreviewDocument(), listener );
    private JMenuItem filePreviewCurrentDocumentAndChildren = createMenuItem( messages.menuPreviewDocumentAndChildren(), listener );
    private JMenuItem fileClose = createMenuItem( messages.menuClose(), listener );
    private JMenuItem fileCloseWithoutSaving = createMenuItem( messages.menuCloseWithoutSave(), listener );
    private JMenuItem fileExit = createMenuItem( messages.menuExit(), listener );
    private JMenu editMenu = new JMenu( messages.menuEdit() );
    private JMenuItem editCut = createMenuItem( messages.menuCut(), listener );
    private JMenuItem editCopy = createMenuItem( messages.menuCopy(), listener );
    private JMenuItem editPaste = createMenuItem( messages.menuPaste(), listener );
    private JMenuItem editUndo = createMenuItem( messages.menuUndo(), listener );
    private JMenuItem editRedo = createMenuItem( messages.menuRedo(), listener );
    private JMenuItem editSelectAll = createMenuItem( messages.menuSelectAll(), listener );
    private JMenuItem editGoToLine = createMenuItem( messages.menuGoToLine(), listener );
    private JMenuItem editPrependAppendToSelectedLines = createMenuItem( messages.menuPrependAppendToSelectedLines(), listener );
    private JMenuItem editFindReplace = createMenuItem( messages.menuFindReplace(), listener );
    private JMenuItem editFindNext = createMenuItem( messages.menuFindNext(), listener );
    private JMenuItem editShowUserDictionary = createMenuItem( messages.menuShowUserDictionary(), listener );
    private JMenu formatMenu = new JMenu( messages.menuFormat() );
    private JMenuItem formatBold = createMenuItem( messages.menuFormatBold(), listener );
    private JMenuItem formatItalic = createMenuItem( messages.menuFormatItalic(), listener );
    private JMenuItem formatUnderline = createMenuItem( messages.menuFormatUnderline(), listener );
    private JMenuItem formatStrikethrough = createMenuItem( messages.menuFormatStrikethrough(), listener );
    private JMenuItem formatSmall = createMenuItem( messages.menuFormatSmall(), listener );
    private JMenuItem formatCenter = createMenuItem( messages.menuFormatCenter(), listener );
    private JMenuItem formatSuperscript = createMenuItem( messages.menuFormatSuperscript(), listener );
    private JMenuItem formatSubscript = createMenuItem( messages.menuFormatSubscript(), listener );
    private JMenuItem formatBlockquote = createMenuItem( messages.menuFormatBlockquote(), listener );
    private JMenuItem formatCode = createMenuItem( messages.menuFormatCode(), listener );
    private JMenuItem formatBulletedList = createMenuItem( messages.menuFormatBulletedList(), listener );
    private JMenuItem formatH1 = createMenuItem( messages.menuFormatH1(), listener );
    private JMenuItem formatH2 = createMenuItem( messages.menuFormatH2(), listener );
    private JMenuItem formatH3 = createMenuItem( messages.menuFormatH3(), listener );
    private JMenuItem formatH4 = createMenuItem( messages.menuFormatH4(), listener );
    private JMenuItem formatH5 = createMenuItem( messages.menuFormatH5(), listener );
    private JMenuItem formatH6 = createMenuItem( messages.menuFormatH6(), listener );
    private JMenu cssMenu = new JMenu( messages.menuCSS() );
    private JMenuItem cssPagebreakBefore = createMenuItem( messages.cssPagebreakBefore(), listener );
    private JMenuItem cssPagebreakAfter = createMenuItem( messages.cssPagebreakAfter(), listener );
    private JMenuItem cssPagebreakBoth = createMenuItem( messages.cssPagebreakBoth(), listener );
    private JMenuItem cssChapter = createMenuItem( messages.cssChapter(), listener );
    private JMenuItem cssChapterPagebreakBefore = createMenuItem( messages.cssChapterPagebreakBefore(), listener );
    private JMenuItem cssChapterPagebreakAfter = createMenuItem( messages.cssChapterPagebreakAfter(), listener );
    private JMenuItem cssChapterPagebreakBoth = createMenuItem( messages.cssChapterPagebreakBoth(), listener );
    private JMenu insertMenu = new JMenu( messages.menuInsert() );
    private JMenuItem formatInsertLink = createMenuItem( messages.menuInsertLink(), listener );
    private JMenuItem formatInsertImage = createMenuItem( messages.menuInsertImage(), listener );
    private JMenuItem formatInsertFootnote = createMenuItem( messages.menuInsertFootnote(), listener );
    private JMenuItem formatInsertPageBreak = createMenuItem( messages.menuInsertPageBreak(), listener );
    private JMenu viewMenu = new JMenu( messages.menuView() );
    private JMenuItem viewSplitHorizontal = createMenuItem( messages.menuViewSplitHorizontal(), listener );
    private JMenuItem viewSplitVertical = createMenuItem( messages.menuViewSplitVertical(), listener );
    private JMenuItem viewSplitUnsplit = createMenuItem( messages.menuViewUnsplit(), listener );
    private JMenuItem viewSynchronizeEditors = createMenuItem( messages.menuViewSynchronizeEditors(), listener );
    private JMenu documentsMenu = new JMenu( messages.menuDocuments() );
    private JMenuItem menuDocumentsNewFile = createMenuItem( messages.menuSibling(), listener );
    private JMenuItem menuDocumentsNewChildText = createMenuItem( messages.menuChild(), listener );
    private JMenuItem documentsRename = createMenuItem( messages.menuRename(), listener );
    private JMenuItem documentsImportImages = createMenuItem( messages.menuImportImages(), listener );
    private JMenuItem documentsDelete = createMenuItem( messages.menuMoveToTrash(), listener );
    private JMenuItem documentsDuplicate = createMenuItem( messages.menuDuplicate(), listener );
    private JMenuItem documentsJoin = createMenuItem( messages.menuJoinDocuments(), listener );
    private JMenuItem documentsSplitAtCursor = createMenuItem( messages.menuSplitAtCursor(), listener );
    private JMenuItem documentsSplitAtCursorMakeSelectionTitle = createMenuItem( messages.menuSplitAtCursorTitle(), listener );
    private JMenuItem documentsProjectStatistics = createMenuItem( messages.statisticsDialogTitle(), listener );
    private JMenuItem documentsEmptyTrash = createMenuItem( messages.menuEmptyTrash(), listener );
    private JMenu textToolsMenu = new JMenu( messages.menuTextTools() );
    private JMenuItem toolsExpandMacro = createMenuItem( messages.menuExpandMacro(), listener );
    private JMenuItem toolsEditMacros = createMenuItem( messages.menuEditMacros(), listener );
    private JMenuItem toolsNewProjectMacroFromSelection = createMenuItem( messages.menuNewProjectMacro(), listener );
    private JMenuItem toolsNewGlobalMacroFromSelection = createMenuItem( messages.menuNewGlobalMacro(), listener );
    
    private JMenuItem toolsUpperCase = createMenuItem( messages.menuUpperCase(), listener );
    private JMenuItem toolsLowerCase = createMenuItem( messages.menuLowerCase(), listener );
    private JMenuItem toolsCamelCase = createMenuItem( messages.menuCamelCase(), listener );
    private JMenuItem toolsTabsToSpaces = createMenuItem( messages.menuTabsToSpaces(), listener );
    private JMenuItem toolsSpacesToTabs = createMenuItem( messages.menuSpacesToTabs(), listener );
    private JMenuItem toolsTextCondenseMultipleSpaces = createMenuItem( messages.menuCondenseMultipleSpaces(), listener );
    private JMenuItem toolsTextShiftIndentLeft = createMenuItem( messages.menuShiftIndentLeft(), listener );
    private JMenuItem toolsTextShiftIndentRight = createMenuItem( messages.menuShiftIndentRight(), listener );
    private JMenuItem toolsTextJoinLines = createMenuItem( messages.menuJoinLines(), listener );
    private JMenuItem toolsTextDeleteLines = createMenuItem( messages.menuDeleteLines(), listener );
    private JMenuItem toolsTextDeleteToStartOfLine = createMenuItem( messages.menuDeleteToStartOfLine(), listener );
    private JMenuItem toolsTextDeleteToEndOfLine = createMenuItem( messages.menuDeleteToEndOfLine(), listener );
    private JMenuItem toolsTextDoubleNewlines = createMenuItem( messages.menuDoubleNewlines(), listener );
    private JMenuItem toolsTextSingleNewlines = createMenuItem( messages.menuSingleNewlines(), listener );
    private JMenuItem toolsTextInsertCurrentDate = createMenuItem( messages.menuInsertCurrentDate(), listener );
    private JMenuItem toolsTextInsertCurrentTime = createMenuItem( messages.menuInsertCurrentTime(), listener );
    private JMenuItem toolsTextInsertCurrentDateAndTime = createMenuItem( messages.menuInsertCurrentDateAndTime(), listener );
    private JMenuItem toolsTextInsertLoremSentance = createMenuItem( messages.menuGreekingLoremSentence(), listener );
    private JMenuItem toolsTextInsertLoremParagraph = createMenuItem( messages.menuGreekingLoremParagraph(), listener );
    private JMenuItem toolsTextInsertSedUt = createMenuItem( messages.menuGreekingSedUt(), listener );
    private JMenuItem toolsTextInsertAtVero = createMenuItem( messages.menuGreekingAtVero(), listener );
    private JMenuItem toolsTextInsertGreekingParagraphs = createMenuItem( messages.menuGreekingParagraphs(), listener );
    private JMenuItem toolsTextInsertUUID = createMenuItem( messages.menuUUID(), listener );

    private JMenu helpMenu = new JMenu( messages.menuHelp() );
    private JMenuItem helpShowHelp = createMenuItem( messages.menuHelpShow(), listener );
    private JMenuItem helpAbout = createMenuItem( messages.menuHelpAbout(), listener );
    private JMenuItem helpPreferences = createMenuItem( messages.menuHelpPreferences(), listener );
    private JMenuItem helpMemory = createMenuItem( messages.menuHelpMemory(), listener );
    private GroovyMenu groovyMenu;
    private RecentProjectsMenu recentProjectsMenu;

    public MarkdownMenuBar( Controller controller ) {
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

        if( !SystemUtils.IS_MAC ) {
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
        editMenu.add( editFindReplace );
        editMenu.add( editFindNext );
        editMenu.addSeparator();
        editMenu.add( formatMenu );
        editMenu.add( cssMenu );
        editMenu.add( insertMenu );
        editMenu.add( textToolsMenu );
        editMenu.addSeparator();
        editMenu.add( toolsExpandMacro );
        editMenu.add( toolsEditMacros );
        editMenu.add( toolsNewGlobalMacroFromSelection );
        editMenu.add( toolsNewProjectMacroFromSelection );
        editMenu.addSeparator();
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

        cssMenu.add( cssPagebreakBefore );
        cssMenu.add( cssPagebreakAfter );
        cssMenu.add( cssPagebreakBoth );
        cssMenu.addSeparator();
        cssMenu.add( cssChapter );
        cssMenu.add( cssChapterPagebreakBefore );
        cssMenu.add( cssChapterPagebreakAfter );
        cssMenu.add( cssChapterPagebreakBoth );

        insertMenu.add( formatInsertLink );
        insertMenu.add( formatInsertImage );
        insertMenu.add( formatInsertFootnote );
        insertMenu.addSeparator();
        insertMenu.add( formatInsertPageBreak );
        insertMenu.addSeparator();
        insertMenu.add( toolsTextInsertCurrentDate );
        insertMenu.add( toolsTextInsertCurrentTime );
        insertMenu.add( toolsTextInsertCurrentDateAndTime );
        insertMenu.addSeparator();
        insertMenu.add( toolsTextInsertLoremSentance );
        insertMenu.add( toolsTextInsertLoremParagraph );
        insertMenu.add( toolsTextInsertSedUt );
        insertMenu.add( toolsTextInsertAtVero );
        insertMenu.add( toolsTextInsertGreekingParagraphs );
        insertMenu.addSeparator();
        insertMenu.add( toolsTextInsertUUID );

        textToolsMenu.add( toolsUpperCase );
        textToolsMenu.add( toolsLowerCase );
        textToolsMenu.add( toolsCamelCase );
        textToolsMenu.addSeparator();
        textToolsMenu.add( toolsTabsToSpaces );
        textToolsMenu.add( toolsSpacesToTabs );
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
        textToolsMenu.add( editPrependAppendToSelectedLines );

        viewMenu.add( viewSplitVertical );
        viewMenu.add( viewSplitHorizontal );
        viewMenu.add( viewSplitUnsplit );
        viewMenu.addSeparator();
        viewMenu.add( viewSynchronizeEditors );
        viewMenu.addSeparator();

        documentsMenu.add( menuDocumentsNewFile );
        documentsMenu.add( menuDocumentsNewChildText );
        documentsMenu.addSeparator();
        documentsMenu.addSeparator();
        documentsMenu.add( documentsRename );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsDuplicate );
        documentsMenu.add( documentsJoin );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsSplitAtCursor );
        documentsMenu.add( documentsSplitAtCursorMakeSelectionTitle );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsImportImages );
        documentsMenu.addSeparator();
        documentsMenu.addSeparator();
        documentsMenu.add( documentsProjectStatistics );
        documentsMenu.addSeparator();
        documentsMenu.add( documentsDelete );
        documentsMenu.add( documentsEmptyTrash );

        helpMenu.add( helpShowHelp );

        if( !SystemUtils.IS_MAC ) {
            helpMenu.addSeparator();
            helpMenu.add( helpAbout );
        }

        helpMenu.addSeparator();
        helpMenu.add( helpPreferences );
        helpMenu.add( helpMemory );

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

        editFindReplace.setAccelerator( KeyStroke.getKeyStroke( 'F', SystemUtils.PREFERED_MODIFIER_KEY ) );
        editFindNext.setAccelerator( KeyStroke.getKeyStroke( 'G', SystemUtils.PREFERED_MODIFIER_KEY ) );

        formatBold.setAccelerator( KeyStroke.getKeyStroke( 'B', SystemUtils.PREFERED_MODIFIER_KEY ) );
        formatItalic.setAccelerator( KeyStroke.getKeyStroke( 'I', SystemUtils.PREFERED_MODIFIER_KEY ) );
        formatUnderline.setAccelerator( KeyStroke.getKeyStroke( 'U', SystemUtils.PREFERED_MODIFIER_KEY ) );

        viewSplitHorizontal.setAccelerator( KeyStroke.getKeyStroke( '3', SystemUtils.PREFERED_MODIFIER_KEY ) );
        viewSplitVertical.setAccelerator( KeyStroke.getKeyStroke( '2', SystemUtils.PREFERED_MODIFIER_KEY ) );
        viewSplitUnsplit.setAccelerator( KeyStroke.getKeyStroke( '0', SystemUtils.PREFERED_MODIFIER_KEY ) );

        documentsRename.setAccelerator( KeyStroke.getKeyStroke( 'R', SystemUtils.PREFERED_MODIFIER_KEY ) );

        menuDocumentsNewFile.setAccelerator( KeyStroke.getKeyStroke( 'N', SystemUtils.PREFERED_MODIFIER_KEY ) );
        menuDocumentsNewChildText.setAccelerator( KeyStroke.getKeyStroke( 'N', SystemUtils.PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );

        toolsExpandMacro.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_SEMICOLON, SystemUtils.PREFERED_MODIFIER_KEY ) );

        toolsUpperCase.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_U, SystemUtils.PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );
        toolsLowerCase.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_L, SystemUtils.PREFERED_MODIFIER_KEY | ActionEvent.SHIFT_MASK ) );

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

    private static JMenuItem createMenuItem( String label, ActionListener listener ) {
        JMenuItem item = new JMenuItem( label );
        item.addActionListener( listener );
        return item;
    }

    public void reloadGroovyScripts() {
        groovyMenu.reloadGroovyScripts();
    }

    public void reloadRecentProjects() {
        recentProjectsMenu.reloadRecentProjects();
    }

    private class Listener
        implements ActionListener {
        @Override
        public void actionPerformed( ActionEvent e ) {
            final Object source = e.getSource();

            Thread thread = new Thread() {
                @Override
                public void run() {
                    if( source == fileNew ) {
                        controller.fileNew();
                    }
                    else if( source == fileOpen ) {
                        controller.fileOpen();
                    }
                    else if( source == fileSave ) {
                        controller.fileSave();
                    }
                    else if( source == fileSaveAs ) {
                        controller.fileSaveAs();
                    }
                    else if( source == fileSaveCopyAs ) {
                        controller.fileSaveCopyAs();
                    }
                    else if( source == fileOpenTemplatesDir ) {
                        controller.fileOpenTemplatesDir();
                    }
                    else if( source == fileExportProject ) {
                        controller.fileExportProject();
                    }
                    else if( source == fileExportProjectUsingCurrentOptions ) {
                        controller.fileExportProjectUsingCurrentOptions();
                    }
                    else if( source == fileExportCurrentDocument ) {
                        controller.fileExportCurrentDocument();
                    }
                    else if( source == fileExportCurrentDocumentUsingCurrentOptions ) {
                        controller.fileExportCurrentDocumentUsingCurrentOptions();
                    }
                    else if( source == fileExportCurrentDocumentAndChildren ) {
                        controller.fileExportCurrentDocumentAndChildren();
                    }
                    else if( source == fileExportCurrentDocumentAndChildrenUsingCurrentOptions ) {
                        controller.fileExportCurrentDocumentAndChildrenUsingCurrentOptionst();
                    }
                    else if( source == fileShowExportDirectory ) {
                        controller.fileShowExportDirectory();
                    }
                    else if( source == fileClose ) {
                        try {
                            controller.fileClose();
                        }
                        catch( Throwable t ) {
                            logger.error( "Error in close event handler", t );
                            ThrowableDialog dialog = new ThrowableDialog( t );
                            dialog.setVisible( true );
                        }
                    }
                    else if( source == fileCloseWithoutSaving ) {
                        controller.fileCloseWithoutSaving();
                    }
                    else if( source == fileExit ) {
                        controller.fileExit();
                    }
                    else if( source == editCut ) {
                        controller.editCut();
                    }
                    else if( source == editCopy ) {
                        controller.editCopy();
                    }
                    else if( source == editPaste ) {
                        controller.editPaste();
                    }
                    else if( source == editUndo ) {
                        controller.editUndo();
                    }
                    else if( source == editRedo ) {
                        controller.editRedo();
                    }
                    else if( source == editSelectAll ) {
                        controller.editSelectAll();
                    }
                    else if( source == editGoToLine ) {
                        controller.editGoToLine();
                    }
                    else if( source == editPrependAppendToSelectedLines ) {
                        controller.editPrependAppendToSelectedLines();
                    }
                    else if( source == editFindReplace ) {
                        controller.editFindInProject();
                    }
                    else if( source == editFindNext ) {
                        controller.editFindNext();
                    }
                    else if( source == editShowUserDictionary ) {
                        controller.editShowUserDictionary();
                    }
                    else if( source == formatBold ) {
                        controller.formatBold();
                    }
                    else if( source == formatItalic ) {
                        controller.formatItalic();
                    }
                    else if( source == formatUnderline ) {
                        controller.formatUnderline();
                    }
                    else if( source == formatStrikethrough ) {
                        controller.formatStrikethrough();
                    }
                    else if( source == formatSmall ) {
                        controller.formatSmall();
                    }
                    else if( source == formatCenter ) {
                        controller.formatCenter();
                    }
                    else if( source == formatSuperscript ) {
                        controller.formatSuperscript();
                    }
                    else if( source == formatSubscript ) {
                        controller.formatSubcript();
                    }
                    else if( source == formatBlockquote ) {
                        controller.formatBlockquote();
                    }
                    else if( source == formatBulletedList ) {
                        controller.formatBulletedList();
                    }
                    else if( source == formatCode ) {
                        controller.formatCode();
                    }
                    else if( source == formatH1 ) {
                        controller.formatH1();
                    }
                    else if( source == formatH2 ) {
                        controller.formatH2();
                    }
                    else if( source == formatH3 ) {
                        controller.formatH3();
                    }
                    else if( source == formatH4 ) {
                        controller.formatH4();
                    }
                    else if( source == formatH5 ) {
                        controller.formatH5();
                    }
                    else if( source == formatH6 ) {
                        controller.formatH6();
                    }
                    else if( source == cssPagebreakBefore ) {
                        controller.cssPagebreakBefore();
                    }
                    else if( source == cssPagebreakAfter ) {
                        controller.cssPagebreakAfter();
                    }
                    else if( source == cssPagebreakBoth ) {
                        controller.cssPagebreakBoth();
                    }
                    else if( source == cssChapter ) {
                        controller.cssChapter();
                    }
                    else if( source == cssChapterPagebreakBefore ) {
                        controller.cssChapterPagebreakBefore();
                    }
                    else if( source == cssChapterPagebreakAfter ) {
                        controller.cssChapterPagebreakAfter();
                    }
                    else if( source == cssChapterPagebreakBoth ) {
                        controller.cssChapterPagebreakBoth();
                    }

                    else if( source == formatInsertLink ) {
                        controller.formatInsertLink();
                    }
                    else if( source == formatInsertImage ) {
                        controller.formatInsertImage();
                    }
                    else if( source == formatInsertFootnote ) {
                        controller.formatInsertFootnote();
                    }
                    else if( source == formatInsertPageBreak ) {
                        controller.formatInsertPageBreak();
                    }
                    else if( source == filePreviewCurrentDocument ) {
                        controller.filePreview();
                    }
                    else if( source == filePreviewCurrentDocumentAndChildren ) {
                        controller.filePreviewWithChildren();
                    }
                    else if( source == viewSplitHorizontal ) {
                        controller.viewSplitHorizontal();
                    }
                    else if( source == viewSplitVertical ) {
                        controller.viewSplitVertical();
                    }
                    else if( source == viewSplitUnsplit ) {
                        controller.viewSplitUnsplit();
                    }
                    else if( source == viewSynchronizeEditors ) {
                        controller.viewSynchronizeEditors();
                    }
                    else if( source == menuDocumentsNewFile ) {
                        controller.documentsNewFile();
                    }
                    else if( source == menuDocumentsNewChildText ) {
                        controller.documentsNewChildFile();
                    }
                    else if( source == documentsRename ) {
                        controller.documentsRename();
                    }
                    else if( source == documentsImportImages ) {
                        controller.documentsImportImages();
                    }
                    else if( source == documentsDelete ) {
                        controller.documentsDelete();
                    }
                    else if( source == documentsDuplicate ) {
                        controller.documentsDuplicate();
                    }
                    else if( source == documentsSplitAtCursor ) {
                        controller.documentsSplitAtCursor();
                    }
                    else if( source == documentsSplitAtCursorMakeSelectionTitle ) {
                        controller.documentsSplitAtCursorMakeSelectionTitle();
                    }
                    else if( source == documentsJoin ) {
                        controller.documentsJoin();
                    }
                    else if( source == documentsProjectStatistics ) {
                        controller.documentsProjectStatistics();
                    }
                    else if( source == documentsEmptyTrash ) {
                        controller.documentsEmptyTrash();
                    }
                    else if( source == toolsExpandMacro ) {
                        controller.toolsExpandMacro();
                    }
                    else if( source == toolsEditMacros ) {
                        controller.toolsEditMacros();
                    }
                    else if(source == toolsNewProjectMacroFromSelection){
                        controller.toolsNewProjectMacroFromSelection();
                    }
                    else if(source == toolsNewGlobalMacroFromSelection){
                        controller.toolsNewGlobalMacroFromSelection();
                    }
                    else if( source == toolsUpperCase ) {
                        controller.toolsTextSelectionToUpperCase();
                    }
                    else if( source == toolsLowerCase ) {
                        controller.toolsTextSelectionToLowerCase();
                    }
                    else if( source == toolsCamelCase ) {
                        controller.toolsTextSelectionToCamelCase();
                    }
                    else if( source == toolsTabsToSpaces ) {
                        controller.toolsTextSelectionTabsToSpaces();
                    }
                    else if( source == toolsSpacesToTabs ) {
                        controller.toolsTextSelectionSpacesToTabs();
                    }
                    else if( source == toolsTextCondenseMultipleSpaces ) {
                        controller.toolsTextCondenseMultipleSpaces();
                    }
                    else if( source == toolsTextShiftIndentLeft ) {
                        controller.toolsTextShiftIndentLeft();
                    }
                    else if( source == toolsTextShiftIndentRight ) {
                        controller.toolsTextShiftIndentRight();
                    }
                    else if( source == toolsTextJoinLines ) {
                        controller.toolsTextJoinLines();
                    }
                    else if( source == toolsTextDeleteLines ) {
                        controller.toolsTextDeleteLines();
                    }
                    else if( source == toolsTextDoubleNewlines ) {
                        controller.toolsTextDoubleNewlines();
                    }
                    else if( source == toolsTextSingleNewlines ) {
                        controller.toolsTextSingleNewlines();
                    }
                    else if( source == toolsTextDeleteToStartOfLine ) {
                        controller.toolsTextDeleteToStartOfLine();
                    }
                    else if( source == toolsTextDeleteToEndOfLine ) {
                        controller.toolsTextDeleteToEndOfLine();
                    }
                    else if( source == toolsTextInsertCurrentDate ) {
                        controller.toolsTextInsertCurrentDate();
                    }
                    else if( source == toolsTextInsertCurrentTime ) {
                        controller.toolsTextInsertCurrentTime();
                    }
                    else if( source == toolsTextInsertCurrentDateAndTime ) {
                        controller.toolsTextInsertCurrentDateAndTime();
                    }
                    else if( source == toolsTextInsertLoremSentance ) {
                        controller.toolsTextInsertLoremSentance();
                    }
                    else if( source == toolsTextInsertLoremParagraph ) {
                        controller.toolsTextInsertLoremParagraph();
                    }
                    else if( source == toolsTextInsertSedUt ) {
                        controller.toolsTextInsertSedUt();
                    }
                    else if( source == toolsTextInsertAtVero ) {
                        controller.toolsTextInsertAtVero();
                    }
                    else if( source == toolsTextInsertGreekingParagraphs ) {
                        controller.toolsTextInsertGreekingParagraphs();
                    }
                    else if( source == toolsTextInsertUUID ) {
                        controller.textToolsInsertUUID();
                    }
                    else if( source == helpShowHelp ) {
                        MarkdownServer.helpShowHelp();
                    }
                    else if( source == helpAbout ) {
                        MarkdownServer.helpAbout();
                    }
                    else if( source == helpPreferences ) {
                        controller.helpPreferences();
                    }
                    else if( source == helpMemory ) {
                        controller.showSystemMemory();
                    }
                }
            };
            SwingUtilities.invokeLater( thread );
        }

    }

    public void configure( Preferences preferences ) {
        EditorPreferences editorPreferences = preferences.getEditorPreferences();

        Project project = controller.getProject();
        if( project.synchronizeEditors() ) {
            viewSynchronizeEditors.setText( messages.menuViewUnsynchronizeEditors() );
        }
        else {
            viewSynchronizeEditors.setText( messages.menuViewSynchronizeEditors() );
        }
    }

}
