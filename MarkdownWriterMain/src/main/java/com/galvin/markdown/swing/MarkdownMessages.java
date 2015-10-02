package com.galvin.markdown.swing;

import galvin.BaseMessages;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import org.apache.commons.io.IOUtils;

public class MarkdownMessages
extends BaseMessages
{
    private static final String MESSAGE_BUNDLE_NAME = "MarkdownMessageBundle";
    private static final String DEFAULT_CSS_TEMPLATE = "/com/galvin/markdown/default-styles.css";
    
    public MarkdownMessages()
    {
        this( new Locale( "en", "US" ) );
    }
    
    public MarkdownMessages( Locale locale )
    {
        super( MESSAGE_BUNDLE_NAME, locale );
    }
    
    /////////////////
    // Default CSS //
    /////////////////
    
    public String defaultCss()
    {
        try
        {
            URL url = getClass().getResource( DEFAULT_CSS_TEMPLATE );
            InputStream stream = url.openStream();
            String result = IOUtils.toString( stream );
            return result;
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
        
        return "";
    }
    
    ////////////////
    // Window titles
    ////////////////
    
    public String applicationTitle(){ return getString( "applicationTitle" ); }
    public String previewTitle(){ return getString( "previewTitle" ); }
    public String helpApplicationGuide(){ return getString( "helpApplicationGuide" ); }
    public String helpMarkdownCompleteGuide(){ return getString( "helpMarkdownCompleteGuide" ); }
    public String helpMarkdownCheetSheet(){ return getString( "helpMarkdownCheetSheet" ); }

    /////////////////
    // Welcome screen
    /////////////////
    
    public String welcomeScreenTitle(){ return getString( "welcomeScreenTitle" ); }
    public String welcomeScreenNewProjectsLabel(){ return getString( "welcomeScreenNewProjectsLabel" ); }
    public String welcomeScreenNewProject(){ return getString( "welcomeScreenNewProject" ); }
    public String welcomeScreenOpenProject(){ return getString( "welcomeScreenOpenProject" ); }
    public String welcomeScreenRecentProjectsLabel(){ return getString( "welcomeScreenRecentProjectsLabel" ); }
    public String welcomeScreenRecentProjectsButton(){ return getString( "welcomeScreenRecentProjectsButton" ); }
    
    //////////////////
    // Document titles
    //////////////////
    
    public String titleManuscript(){ return getString( "titleManuscript" ); }
    public String titleResearch(){ return getString( "titleResearch" ); }
    public String titleResources(){ return getString( "titleResources" ); }
    public String titleTrash(){ return getString( "titleTrash" ); }
    public String titleUntitled(){ return getString( "titleUntitled" ); }
    public String titleUntitledProject(){ return getString( "titleUntitledProject" ); }
    public String titleUntitledDocument(){ return getString( "titleUntitledDocument" ); }
    public String titleUntitledFolder(){ return getString( "titleUntitledFolder" ); }
    public String titleTableOfContents(){ return getString( "titleTableOfContents" ); }
    public String titleCover(){ return getString( "titleCover" ); }
    
    ////////////////
    // Node sections
    ////////////////
    
    public String nodeSectionMauscript(){ return getString( "nodeSectionMauscript" ); }
    public String nodeSectionDescription(){ return getString( "nodeSectionDescription" ); }
    public String nodeSectionSummary(){ return getString( "nodeSectionSummary" ); }
    public String nodeSectionNotes(){ return getString( "nodeSectionNotes" ); }
    public String nodeSectionMetadata(){ return getString( "nodeSectionMetadata" ); }
    public String nodeSectionImageResource(){ return getString( "nodeSectionImageResource" ); }
    
    public String fileChooserProjectSave(){ return getString( "fileChooserProjectSave" ); }
    public String fileChooserProjectCancel(){ return getString( "fileChooserProjectCancel" ); }
    
    public String fileChooserTitleSave(){ return getString( "fileChooserTitleSave" ); }
    public String fileChooserTitleSaveAs(){ return getString( "fileChooserTitleSaveAs" ); }
    
    
    public String fileChooserProjectFilterDescription(){ return getString( "fileChooserProjectFilterDescription" ); }
    public String fileChooserImageFilterDescription(){ return getString( "fileChooserImageFilterDescription" ); }
    
    ///////////
    // Menu bar
    ///////////
    
    public String menuBarFile(){ return getString( "menuBarFile" ); }
    public String menuBarFileNew(){ return getString( "menuBarFileNew" ); }
    public String menuBarFileOpen(){ return getString( "menuBarFileOpen" ); }
    public String menuBarFileSave(){ return getString( "menuBarFileSave" ); }
    public String menuBarFileSaveAs(){ return getString( "menuBarFileSaveAs" ); }
    public String menuBarFileSaveCopyAs(){ return getString( "menuBarFileCopySaveAs" ); }
    public String menuBarFileTakeSnapshot(){ return getString( "menuBarFileTakeSnapshot" ); }
    public String menuBarFileOpenSnapshotsDir(){ return getString( "menuBarFileOpenSnapshotsDir" ); }
    
    
    public String menuBarFileExport(){ return getString( "menuBarFileExport" ); }
    public String menuBarFileExportProject(){ return getString( "menuBarFileExportProject" ); }
    public String menuBarFileExportProjectUsingCurrentOptions(){ return getString( "menuBarFileExportProjectUsingCurrentOptions" ); }
    public String menuBarFileExportCurrentDocument(){ return getString( "menuBarFileExportCurrentDocument" ); }
    public String menuBarFileExportCurrentDocumentUsingCurrentOptions(){ return getString( "menuBarFileExportCurrentDocumentUsingCurrentOptions" ); }
    public String menuBarFileExportCurrentDocumentAndChildren(){ return getString( "menuBarFileExportCurrentDocumentAndChildren" ); }
    public String menuBarFileExportCurrentDocumentAndChildrenUsingCurrentOptions(){ return getString( "menuBarFileExportCurrentDocumentAndChildrenUsingCurrentOptions" ); }
    public String menuBarFileShowExportDirectory(){ return getString( "menuBarFileShowExportDirectory" ); }
    public String menuBarFileClearExportDirectory(){ return getString( "menuBarFileClearExportDirectory" ); }
    
    public String menuBarFilePreview(){ return getString( "menuBarFilePreview" ); }
    public String menuBarFilePreviewCurrentDocument(){ return getString( "menuBarFilePreviewCurrentDocument" ); }
    public String menuBarFilePreviewCurrentDocumentAndChildren(){ return getString( "menuBarFilePreviewCurrentDocumentAndChildren" ); }
    
    public String menuBarFileClose(){ return getString( "menuBarFileClose" ); }
    public String menuBarFileCloseWithoutSaving(){ return getString( "menuBarFileCloseWithoutSaving" ); }
    public String menuBarFileExit(){ return getString( "menuBarFileExit" ); }
    public String menuBarFileRecentProjects(){ return getString( "menuBarFileRecentProjects" ); }
    public String menuBarFileNoRecentProjects(){ return getString( "menuBarFileNoRecentProjects" ); }
    public String menuBarFileClearRecentProjects(){ return getString( "menuBarFileClearRecentProjects" ); }
    
    public String menuBarEdit(){ return getString( "menuBarEdit" ); }
    public String menuBarEditCut(){ return getString( "menuBarEditCut" ); }
    public String menuBarEditCopy(){ return getString( "menuBarEditCopy" ); }
    public String menuBarEditPaste(){ return getString( "menuBarEditPaste" ); }
    public String menuBarEditUndo(){ return getString( "menuBarEditUndo" ); }
    public String menuBarEditRedo(){ return getString( "menuBarEditRedo" ); }
    public String menuBarEditSelectAll(){ return getString( "menuBarEditSelectAll" ); }
    public String menuBarEditGoToLine(){ return getString( "menuBarEditGoToLine" ); }
    public String menuBarEditPrependToSelectedLines(){ return getString( "menuBarEditPrependToSelectedLines" ); }
    public String menuBarEditAppendToSelectedLines(){ return getString( "menuBarEditAppendToSelectedLines" ); }
    public String menuBarEditFindInProject(){ return getString( "menuBarEditFindInProject" ); }
    public String menuBarEditFindNext(){ return getString( "menuBarEditFindNext" ); }
    public String menuBarEditTurnLiveSpellcheckOn(){ return getString( "menuBarEditTurnLiveSpellcheckOn" ); }
    public String menuBarEditTurnLiveSpellcheckOff(){ return getString( "menuBarEditTurnLiveSpellcheckOff" ); }
    public String menuBarEditShowUserDictionary(){ return getString( "menuBarEditShowUserDictionary" ); }
    
    public String menuBarGroovy(){ return getString( "menuBarGroovy" ); }
    public String menuBarGroovyReload(){ return getString( "menuBarGroovyReload" ); }
    public String menuBarGroovyNoScripts(){ return getString( "menuBarGroovyNoScripts" ); }
    public String menuBarGroovyOpenDir(){ return getString( "menuBarGroovyOpenDir" ); }
    
    public String menuBarFormat(){ return getString( "menuBarFormat" ); }
    public String menuBarFormatBold(){ return getString( "menuBarFormatBold" ); }
    public String menuBarFormatItalic(){ return getString( "menuBarFormatItalic" ); }
    public String menuBarFormatUnderline(){ return getString( "menuBarFormatUnderline" ); }
    public String menuBarFormatStrikethrough(){ return getString( "menuBarFormatStrikethrough" ); }
    public String menuBarFormatSmall(){ return getString( "menuBarFormatSmall" ); }
    public String menuBarFormatCenter(){ return getString( "menuBarFormatCenter" ); }
    public String menuBarFormatSuperscript(){ return getString( "menuBarFormatSuperscript" ); }
    public String menuBarFormatSubscript(){ return getString( "menuBarFormatSubscript" ); }
    public String menuBarFormatBlockquote(){ return getString( "menuBarFormatBlockquote" ); }
    public String menuBarFormatCode(){ return getString( "menuBarFormatCode" ); }
    public String menuBarBulletedList(){ return getString( "menuBarBulletedList" ); }
    public String menuBarFormatH1(){ return getString( "menuBarFormatH1" ); }
    public String menuBarFormatH2(){ return getString( "menuBarFormatH2" ); }
    public String menuBarFormatH3(){ return getString( "menuBarFormatH3" ); }
    public String menuBarFormatH4(){ return getString( "menuBarFormatH4" ); }
    public String menuBarFormatH5(){ return getString( "menuBarFormatH5" ); }
    public String menuBarFormatH6(){ return getString( "menuBarFormatH6" ); }
    public String menuBarFormatInsert(){ return getString( "menuBarFormatInsert" ); }
    public String menuBarFormatInsertLink(){ return getString( "menuBarFormatInsertLink" ); }
    public String menuBarFormatInsertImage(){ return getString( "menuBarFormatInsertImage" ); }
    public String menuBarFormatInsertFootnote(){ return getString( "menuBarFormatInsertFootnote" ); }
    
    public String menuBarView(){ return getString( "menuBarView" ); }
    public String menuBarViewSplitHorizontal(){ return getString( "menuBarViewSplitHorizontal" ); }
    public String menuBarViewSplitVertical(){ return getString( "menuBarViewSplitVertical" ); }
    public String menuBarViewUnsplit(){ return getString( "menuBarViewUnsplit" ); }
    public String menuBarViewSynchronizeEditors(){ return getString( "menuBarViewSynchronizeEditors" ); }
    public String menuBarViewStopSynchronizingEditors(){ return getString( "menuBarViewStopSynchronizingEditors" ); }
    public String menuBarViewShowLineNumbers(){ return getString( "menuBarViewShowLineNumbers" ); }
    public String menuBarViewHideLineNumbers(){ return getString( "menuBarViewHideLineNumbers" ); }
    public String menuBarViewShowLineHighlitghts(){ return getString( "menuBarViewShowLineHighlitghts" ); }
    public String menuBarViewHideLineHighlitghts(){ return getString( "menuBarViewHideLineHighlitghts" ); }
    public String menuBarViewTurnWordWrapOn(){ return getString( "menuBarViewTurnWordWrapOn" ); }
    public String menuBarViewTurnWordWrapOff(){ return getString( "menuBarViewTurnWordWrapOff" ); }
    public String menuBarViewShowInvisibles(){ return getString( "menuBarViewShowInvisibles" ); }
    public String menuBarViewHideInvisibles(){ return getString( "menuBarViewHideInvisibles" ); }
    
    public String menuBarDocuments(){ return getString( "menuBarDocuments" ); }
    public String menuBarDocumentsNewFile(){ return getString( "menuBarDocumentsNewFile" ); }
    public String menuBarDocumentsNewFolder(){ return getString( "menuBarDocumentsNewFolder" ); }
    public String menuBarDocumentsConvertToFile(){ return getString( "menuBarDocumentsConvertToFile" ); }
    public String menuBarDocumentsConvertToFolder(){ return getString( "menuBarDocumentsConvertToFolder" ); }
    public String menuBarDocumentsRename(){ return getString( "menuBarDocumentsRename" ); }
    public String menuBarDocumentsImportImages(){ return getString( "menuBarDocumentsImportImages" ); }
    public String menuBarDocumentsDelete(){ return getString( "menuBarDocumentsDelete" ); }
    public String menuBarDocumentsDuplicate(){ return getString( "menuBarDocumentsDuplicate" ); }
    public String menuBarDocumentsSplitAtCurson(){ return getString( "menuBarDocumentsSplitAtCurson" ); }
    public String menuBarDocumentsSplitMakeSelectionTitle(){ return getString( "menuBarDocumentsSplitMakeSelectionTitle" ); }
    public String menuBarDocumentsJoin(){ return getString( "menuBarDocumentsJoin" ); }
    public String menuBarDocumentsStatistics(){ return getString( "menuBarDocumentsStatistics" ); }
    public String menuBarDocumentsEmptyTrash(){ return getString( "menuBarDocumentsEmptyTrash" ); }
    public String menuBarDocumentsSelectInTree(){ return getString( "menuBarDocumentsSelectInTree" ); }
    
    public String menuBarTextTools(){ return getString( "menuBarTextTools" ); }
    public String menuBarExpandMacro(){ return getString( "menuBarExpandMacro" ); }
    public String menuBarEditMacros(){ return getString( "menuBarEditMacros" ); }
    
    public String menuBarTextSelectionToUpperCase(){ return getString( "menuBarTextSelectionToUpperCase" ); }
    public String menuBarTextSelectionToLowerCase(){ return getString( "menuBarTextSelectionToLowerCase" ); }
    public String menuBarTextSelectionToCamelCase(){ return getString( "menuBarTextSelectionToCamelCase" ); }
    public String menuBarTextSelectionTabsToSpaces(){ return getString( "menuBarTextSelectionTabsToSpaces" ); }
    public String menuBarTextSelectionSpacesToTabs(){ return getString( "menuBarTextSelectionSpacesToTabs" ); }
    public String menuBarTextSelectionCondenseMultipleSpaces(){ return getString( "menuBarTextSelectionCondenseMultipleSpaces" ); }
    public String menuBarTextShiftIndentLeft(){ return getString( "menuBarTextShiftIndentLeft" ); }
    public String menuBarTextShiftIndentRight(){ return getString( "menuBarTextShiftIndentRight" ); }
    public String menuBarTextJoinLines(){ return getString( "menuBarTextJoinLines" ); }
    public String menuBarTextDeleteLines(){ return getString( "menuBarTextDeleteLines" ); }
    public String menuBarTextDeleteToStartOfLine(){ return getString( "menuBarTextDeleteToStartOfLine" ); }
    public String menuBarTextDeleteToEndOfLine(){ return getString( "menuBarTextDeleteToEndOfLine" ); }
    
    public String menuBarTextDoubleNewlines(){ return getString( "menuBarTextDoubleNewlines" ); }
    public String menuBarTextSingleNewlines(){ return getString( "menuBarTextSingleNewlines" ); }
    
    public String menuBarTextInsertCurrentDate(){ return getString( "menuBarTextInsertCurrentDate" ); }
    public String menuBarTextInsertCurrentTime(){ return getString( "menuBarTextInsertCurrentTime" ); }
    public String menuBarTextInsertCurrentDateAndTime(){ return getString( "menuBarTextInsertCurrentDateAndTime" ); }
    
    public String menuBarHelp(){ return getString( "menuBarHelp" ); }
    public String menuBarHelpShow(){ return getString( "menuBarHelpShow" ); }
    public String menuBarHelpAbout(){ return getString( "menuBarHelpAbout" ); }
    public String menuBarHelpPreferences(){ return getString( "menuBarHelpPreferences" ); }
    
    public String menuBarOpenTemplatesDir(){ return getString( "menuBarOpenTemplatesDir" ); }
    
    //////////////////
    // Tree popup menu
    //////////////////
    
    public String treeMenuAddChild(){ return getString( "treeMenuAddChild" ); }
    public String treeMenuAddSibling(){ return getString( "treeMenuAddSibling" ); }
    public String treeMenuAddText(){ return getString( "treeMenuAddText" ); }
    public String treeMenuAddFolder(){ return getString( "treeMenuAddFolder" ); }
    
    public String treeMenuRename(){ return getString( "treeMenuRename" ); }
    public String treeMenuDelete(){ return getString( "treeMenuDelete" ); }
    public String treeMenuDuplicate(){ return getString( "treeMenuDuplicate" ); }
    public String treeMenuJoin(){ return getString( "treeMenuJoin" ); }
    public String treeMenuConvertToFile(){ return getString( "treeMenuConvertToFile" ); }
    public String treeMenuConvertToFolder(){ return getString( "treeMenuConvertToFolder" ); }
    public String treeMenuEmptyTrash(){ return getString( "treeMenuEmptyTrash" ); }
    
    
    //////////////////////
    // Save project dialog
    //////////////////////
    
    public String fileChooserSaveProjectTitle(){ return getString( "fileChooserSaveProjectTitle" ); }
    public String fileChooserSaveProjectButton(){ return getString( "fileChooserSaveProjectButton" ); }
    
    public String fileChooserOpenProjectTitle(){ return getString( "fileChooserOpenProjectTitle" ); }
    public String fileChooserOpenProjectButton(){ return getString( "fileChooserOpenProjectButton" ); }
    
    public String fileChooserProjectName(){ return getString( "fileChooserProjectName" ); }
    public String fileChooserProjectLocation(){ return getString( "fileChooserProjectLocation" ); }
    public String fileChooserProjectLocationButton(){ return getString( "fileChooserProjectLocationButton" ); }
    public String fileChooserProjectWillBeSavedAs(){ return getString( "fileChooserProjectWillBeSavedAs" ); }
    
    //////////
    // Dialogs
    //////////
    
    public String dialogMessageRename(){ return getString( "dialogMessageRename" ); }
    public String dialogMessageGoToLine(){ return getString( "dialogMessageGoToLine" ); }
    public String dialogMessagePrependToLines(){ return getString( "dialogMessagePrependToLines" ); }
    public String dialogMessageAppendToLines(){ return getString( "dialogMessageAppendToLines" ); }
    public String dialogMessageSnapshotName(){ return getString( "dialogMessageSnapshotName" ); }
    
    public String dialogCloseWithoutSaving(){ return getString( "dialogCloseWithoutSaving" ); }
    public String dialogCloseWithoutSavingTitle(){ return getString( "dialogCloseWithoutSavingTitle" ); }
    
    public String dialogEmptyTrash(){ return getString( "dialogEmptyTrash" ); }
    public String dialogEmptyTrashTitle(){ return getString( "dialogEmptyTrashTitle" ); }
    
    public String dialogClearExportDir(){ return getString( "dialogClearExportDir" ); }
    public String dialogClearExportDirTitle(){ return getString( "dialogClearExportDirTitle" ); }
    
    public String dialogHyperlinkTitle(){ return getString( "dialogHyperlinkTitle" ); }
    public String dialogHyperlinkText(){ return getString( "dialogHyperlinkText" ); }
    public String dialogHyperlinkUrl(){ return getString( "dialogHyperlinkUrl" ); }
    public String dialogHyperlinkCancel(){ return getString( "dialogHyperlinkCancel" ); }
    public String dialogHyperlinkOk(){ return getString( "dialogHyperlinkOk" ); }
    
    public String dialogFootnoteTitle(){ return getString( "dialogFootnoteTitle" ); }
    public String dialogFootnoteText(){ return getString( "dialogFootnoteText" ); }
    public String dialogFootnoteBody(){ return getString( "dialogFootnoteBody" ); }
    public String dialogFootnoteCancel(){ return getString( "dialogFootnoteCancel" ); }
    public String dialogFootnoteOk(){ return getString( "dialogFootnoteOk" ); }
    
    public String dialogImageInsertCancel(){ return getString( "dialogImageInsertCancel" ); }
    public String dialogImageInsertOkay(){ return getString( "dialogImageInsertOkay" ); }
    
    public String dialogMacrosTitle(){ return getString( "dialogMacrosTitle" ); }
    public String dialogMacrosGlobal(){ return getString( "dialogMacrosGlobal" ); }
    public String dialogMacrosProject(){ return getString( "dialogMacrosProject" ); }
    
    public String dialogCompileProgressTitle(){ return getString( "dialogCompileProgressTitle" ); }
    public String dialogCompileProgressLabel(){ return getString( "dialogCompileProgressLabel" ); }
    
    public String dialogOpenProgressTitle(){ return getString( "dialogOpenProgressTitle" ); }
    public String dialogOpenProgressLabel(){ return getString( "dialogOpenProgressLabel" ); }
    
    public String dialogStatisticsTitle(){ return getString( "dialogStatisticsTitle" ); }
    public String dialogStatisticsCharacters(){ return getString( "dialogStatisticsCharacters" ); }
    public String dialogStatisticsWords(){ return getString( "dialogStatisticsWords" ); }
    public String dialogStatisticsPages(){ return getString( "dialogStatisticsPages" ); }
    public String dialogStatisticsCharactersPerWord(){ return getString( "dialogStatisticsCharactersPerWord" ); }
    public String dialogStatisticsWordsPerPage(){ return getString( "dialogStatisticsWordsPerPage" ); }
    public String dialogStatisticsDone(){ return getString( "dialogStatisticsDone" ); }
    public String dialogStatisticsRefresh(){ return getString( "dialogStatisticsRefresh" ); }
    
    public String dialogRenameWindowTitle(){ return getString( "dialogRenameWindowTitle" ); }
    public String dialogRenameTitle(){ return getString( "dialogRenameTitle" ); }
    public String dialogRenameSubTitle(){ return getString( "dialogRenameSubTitle" ); }
    public String dialogRenameOkay(){ return getString( "dialogRenameOkay" ); }
    public String dialogRenameCancel(){ return getString( "dialogRenameCancel" ); }
    
    ///////////////////
    // Find and Replace
    ///////////////////
    
    public String findAndReplaceSearchForLabel(){ return getString( "findAndReplaceSearchForLabel" ); }
    public String findAndReplaceReplaceWithLabel(){ return getString( "findAndReplaceReplaceWithLabel" ); }
    public String findAndReplaceFindNextButton(){ return getString( "findAndReplaceFindNextButton" ); }
    public String findAndReplaceFindAllButton(){ return getString( "findAndReplaceFindAllButton" ); }
    public String findAndReplaceReplaceAllButton(){ return getString( "findAndReplaceReplaceAllButton" ); }
    public String findAndReplaceCancelButton(){ return getString( "findAndReplaceCancelButton" ); }
    public String findAndReplaceCurrentDocumentRadioButton(){ return getString( "findAndReplaceCurrentDocumentRadioButton" ); }
    public String findAndReplaceAllDocumentsRadioButton(){ return getString( "findAndReplaceAllDocumentsRadioButton" ); }
    public String findAndReplaceOnlyDocumentsInManuscriptCheckbox(){ return getString( "findAndReplaceOnlyDocumentsInManuscriptCheckbox" ); }
    public String findAndReplaceManuscriptCheckbox(){ return getString( "findAndReplaceManuscriptCheckbox" ); }
    public String findAndReplaceSummaryCheckbox(){ return getString( "findAndReplaceSummaryCheckbox" ); }
    public String findAndReplaceDescriptionCheckbox(){ return getString( "findAndReplaceDescriptionCheckbox" ); }
    public String findAndReplaceNotesCheckbox(){ return getString( "findAndReplaceNotesCheckbox" ); }
    public String findAndReplaceIgnoreCaseCheckbox(){ return getString( "findAndReplaceIgnoreCaseCheckbox" ); }
    
    //////////////
    // Preferences
    //////////////
    
    public String preferences(){ return getString( "preferences" ); }
    public String preferencesGeneral(){ return getString( "preferencesGeneral" ); }
    public String preferencesMarkdown(){ return getString( "preferencesMarkdown" ); }
    
    ///////////////////////
    // Markdown Preferences
    ///////////////////////
    
    public String markdownEngine(){ return getString( "markdownEngine" ); }
    public String pdfLatexPath(){ return getString( "pdfLatexPath" ); }
    public String multiMarkdownSmartQuotes(){ return getString( "multiMarkdownSmartQuotes" ); }
    public String multiMarkdownNotes(){ return getString( "multiMarkdownNotes" ); }
    public String multiMarkdownProcessHtml(){ return getString( "multiMarkdownProcessHtml" ); }
    public String multiMarkdownGenerateHeaderIDs(){ return getString( "multiMarkdownGenerateHeaderIDs" ); }
    public String setGlobalMakdownPreferences(){ return getString( "setGlobalMakdownPreferences" ); }
    public String pandocPath(){ return getString( "pandocPath" ); }
    public String kindleGenPath(){ return getString( "kindleGenPath" ); }
    
    ////////////////////////////////
    //Document separator preferences
    ////////////////////////////////
    
    public String compileOptionsFolderFolder(){ return getString( "compileOptionsFolderFolder" ); }
    public String compileOptionsFolderFile(){ return getString( "compileOptionsFolderFile" ); }
    public String compileOptionsFileFolder(){ return getString( "compileOptionsFileFolder" ); }
    public String compileOptionsFileFile(){ return getString( "compileOptionsFileFile" ); }
    public String compileOptionsTitleFolder(){ return getString( "compileOptionsTitleFolder" ); }
    public String compileOptionsTitleFile(){ return getString( "compileOptionsTitleFile" ); }
    
    public String compileOptionsLineBreak(){ return getString( "compileOptionsLineBreak" ); }
    public String compileOptionsParagraphBreak(){ return getString( "compileOptionsParagraphBreak" ); }
    public String compileOptionsPageBreak(){ return getString( "compileOptionsPageBreak" ); }
    public String compileOptionsHR(){ return getString( "compileOptionsHR" ); }
    public String compileOptionsCustom(){ return getString( "compileOptionsCustom" ); }
    
    /////////////////
    //Compile Options
    /////////////////
    
    public String compileDialogTitle(){ return getString( "compileDialogTitle" ); }
    public String outputCompileOptions(){ return getString( "outputCompileOptions" ); }
    public String includeCompileOptions(){ return getString( "includeCompileOptions" ); }
    public String separatorCompileOptions(){ return getString( "separatorCompileOptions" ); }
    public String doCompile(){ return getString( "doCompile" ); }
    public String cancelCompile(){ return getString( "cancelCompile" ); }
    
    public String dialogMessageCompileSuccess(){ return getString( "dialogMessageCompileSuccess" ); }
    public String dialogMessageCompileSuccessTitle(){ return getString( "dialogMessageCompileSuccessTitle" ); }
    public String dialogMessageCompileSuccessOpenFolder(){ return getString( "dialogMessageCompileSuccessOpenFolder" ); }
    public String dialogMessageCompileSuccessOkay(){ return getString( "dialogMessageCompileSuccessOkay" ); }
    
    public String dialogMessageCompileResultSuccess(){ return getString( "dialogMessageCompileResultSuccess" ); }
    public String dialogMessageCompileResultFailure(){ return getString( "dialogMessageCompileResultFailure" ); }
    
    public String dialogMessageCompileNoResults(){ return getString( "dialogMessageCompileNoResults" ); }
    public String dialogMessageCompileNoResultsTitle(){ return getString( "dialogMessageCompileNoResultsTitle" ); }
    
    /////////////////////////
    //General Compile Options
    /////////////////////////
    
    public String importFormat(){ return getString( "importFormat" ); }
    public String outputDirectory(){ return getString( "outputDirectory" ); }
    public String chooseOutputDirectory(){ return getString( "chooseOutputDirectory" ); }
    public String includeManuscript(){ return getString( "includeManuscript" ); }
    public String includeDescription(){ return getString( "includeDescription" ); }
    public String includeNotes(){ return getString( "includeNotes" ); }
    public String includeSummary(){ return getString( "includeSummary" ); }
    public String includeContributors(){ return getString( "includeContributors" ); }
    public String includeContributorRoles(){ return getString( "includeContributorRoles" ); }
    public String includeTitlesOfFolders(){ return getString( "includeTitlesOfFolders" ); }
    public String includeSubtitlesOfFolders(){ return getString( "includeSubtitlesOfFolders" ); }
    public String includeTitlesOfFiles(){ return getString( "includeTitlesOfFiles" ); }
    public String includeSubtitlesOfFiles(){ return getString( "includeSubtitlesOfFiles" ); }
    public String includeTableOfContents(){ return getString( "includeTableOfContents" ); }

    /////////////////////
    // Contributor widget
    /////////////////////
    
    public String contributorWidgetName(){ return getString( "contributorWidgetName" ); }
    public String contributorWidgetSortByName(){ return getString( "contributorWidgetSortByName" ); }
    public String contributorWidgetRole(){ return getString( "contributorWidgetRole" ); }
    
    //////////////////
    // Metadata widget
    //////////////////
    
    public String metadataWidgetTitle(){ return getString( "metadataWidgetTitle" ); }
    public String metadataWidgetSubtitle(){ return getString( "metadataWidgetSubtitle" ); }
    public String metadataWidgetCreatedDate(){ return getString( "metadataWidgetCreatedDate" ); }
    public String metadataWidgetModifiedDate(){ return getString( "metadataWidgetModifiedDate" ); }
    public String metadataWidgetLanguage(){ return getString( "metadataWidgetLanguage" ); }
    public String metadataWidgetIdentifierScheme(){ return getString( "metadataWidgetIdentifierScheme" ); }
    public String metadataWidgetIdentifier(){ return getString( "metadataWidgetIdentifier" ); }
    public String metadataWidgetGenres(){ return getString( "metadataWidgetGenres" ); }
    public String metadataWidgetTopics(){ return getString( "metadataWidgetTopics" ); }
    public String metadataWidgetKeywords(){ return getString( "metadataWidgetKeywords" ); }
    public String metadataWidgetContributors(){ return getString( "metadataWidgetContributors" ); }
    public String metadataWidgetAddContributor(){ return getString( "metadataWidgetAddContributor" ); }
    public String metadataWidgetRemoveContributor(){ return getString( "metadataWidgetRemoveContributor" ); }
    
    public String projectMetadataWidgetConfig(){ return getString( "projectMetadataWidgetConfig" ); }
    public String projectMetadataWidgetMetadata(){ return getString( "projectMetadataWidgetMetadata" ); }
    public String projectMetadataWidgetCover(){ return getString( "projectMetadataWidgetCover" ); }
    public String projectMetadataWidgetStyleSheet(){ return getString( "projectMetadataWidgetStyleSheet" ); }
    public String projectMetadataNoCover(){ return getString( "projectMetadataNoCover" ); }
    
    ////////////////////
    // Project templates
    ////////////////////
    
    public String projectTemplateEmptyTitle(){ return getString( "projectTemplateEmptyTitle" ); }
    public String projectTemplateEmpty(){ return getString( "projectTemplateEmpty" ); }
    
    public String projectTemplateNovelTitle(){ return getString( "projectTemplateNovelTitle" ); }
    public String projectTemplateNovel(){ return getString( "projectTemplateNovel" ); }
    
    public String projectTemplateFromFile(){ return getString( "projectTemplateFromFile" ); }
    
    /////////////////
    // Error messages
    /////////////////
    
    public String errorMissingProjectFile(){ return getString( "errorMissingProjectFile" ); }
    
    public String errorDuringCompile(){ return getString( "errorDuringCompile" ); }
    public String errorDuringCompileTitle(){ return getString( "errorDuringCompileTitle" ); }
    
    public String errorDuringSave(){ return getString( "errorDuringSave" ); }
    public String errorDuringSaveTitle(){ return getString( "errorDuringSaveTitle" ); }
    
    public String saveLegacyVersion(){ return getString( "saveLegacyVersion" ); }
    public String saveLegacyVersionTitle(){ return getString( "saveLegacyVersionTitle" ); }
    
    public String errorDuringOpen(){ return getString( "errorDuringOpen" ); }
    public String errorDuringOpenTitle(){ return getString( "errorDuringOpenTitle" ); }
    
    public String errorDuringNew(){ return getString( "errorDuringNew" ); }
    public String errorDuringNewTitle(){ return getString( "errorDuringNewTitle" ); }
    
    public String errorDuringDuplicate(){ return getString( "errorDuringDuplicate" ); }
    public String errorDuringDuplicateTitle(){ return getString( "errorDuringDuplicateTitle" ); }
    
    public String errorDuringSplit(){ return getString( "errorDuringSplit" ); }
    public String errorDuringSplitTitle(){ return getString( "errorDuringSplitTitle" ); }
    
    public String errorPreview(){ return getString( "errorPreview" ); }
    public String errorPreviewTitle(){ return getString( "errorPreviewTitle" ); }
    
    public String errorExportFormatOnlySupportedOnMac(){ return getString( "errorExportFormatOnlySupportedOnMac" ); }
    public String errorExportFormatOnlySupportedOnMacPlaceholder(){ return getString( "errorExportFormatOnlySupportedOnMacPlaceholder" ); }
    public String errorExportFormatOnlySupportedOnMacTitle(){ return getString( "errorExportFormatOnlySupportedOnMacTitle" ); }
    
    public String errorLoadingHelpFile(){ return getString( "errorLoadingHelpFile" ); }
    public String errorLoadingHelpFileTitle(){ return getString( "errorLoadingHelpFileTitle" ); }
    
    public String errorImportingImages(){ return getString( "errorImportingImages" ); }
    public String errorImportingImagesTitle(){ return getString( "errorImportingImagesTitle" ); }
    
    public String errorBlockquote(){ return getString( "errorBlockquote" ); }
    public String errorBlockquoteTitle(){ return getString( "errorBlockquoteTitle" ); }
    
    public String errorSettingCoverImage(){ return getString( "errorSettingCoverImage" ); }
    public String errorSettingCoverImageTitle(){ return getString( "errorSettingCoverImageTitle" ); }
    
    public String errorExecutingGroovy(){ return getString( "errorExecutingGroovy" ); }
    public String errorExecutingGroovyTitle(){ return getString( "errorExecutingGroovyTitle" ); }
    
    public String errorWontDeleteExportDir(){ return getString( "errorWontDeleteExportDir" ); }
    public String errorWontDeleteExportDirTitle(){ return getString( "errorWontDeleteExportDirTitle" ); }
    
    public String errorEmptyingTrash(){ return getString( "errorEmptyingTrash" ); }
    public String errorEmptyingTrashTitle(){ return getString( "errorEmptyingTrashTitle" ); }
    
    public String errorTakingSnapshot(){ return getString( "errorTakingSnapshot" ); }
    public String errorTakingSnapshotTitle(){ return getString( "errorTakingSnapshotTitle" ); }
}
