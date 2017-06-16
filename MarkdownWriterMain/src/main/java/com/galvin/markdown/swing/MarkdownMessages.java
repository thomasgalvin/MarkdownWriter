package com.galvin.markdown.swing;

import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;

public class MarkdownMessages
//extends BaseMessages
{
//    private static final String MESSAGE_BUNDLE_NAME = "MarkdownMessageBundle";
    private static final String DEFAULT_CSS_TEMPLATE = "/com/galvin/markdown/default-styles.css";
    
//    public MarkdownMessages()
//    {
//        this( new Locale( "en", "US" ) );
//    }
//    
//    public MarkdownMessages( Locale locale )
//    {
//        super( MESSAGE_BUNDLE_NAME, locale );
//    }
    
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
    
    //////////
    /// Common 
    //////////

    public String application(){ return "Markdown Writer"; };    
    public String errorDialogTitle(){ return "Error"; };
    
    public String cancel(){ return "Cancel"; };
    public String create(){ return "Create"; };
    public String ellipsis(){ return "..."; };
    public String global(){ return "Global"; };
    public String next(){ return "Next"; };
    public String okay(){ return "Okay"; };
    public String open(){ return "Open"; };
    public String progress(){ return "Progress"; };
    public String project(){ return "Project"; };
    public String refresh(){ return "Refresh"; };
    public String save(){ return "Save"; };
    public String success(){ return "Success"; };
    public String successLabel(){ return "Success:"; };
    public String failureLabel(){ return "Failure:"; };
    
    public String renameDialogTitle(){ return "Rename"; };
    
    ///////////////////////////
    /// Document/Section Titles
    ///////////////////////////
    
    public String untitled(){ return "Untitled"; };
    public String config(){ return "Config"; };
    public String coverImage(){ return "Cover Image"; };
    public String description(){ return "Description"; };
    public String imageResource(){ return "Image Resource"; };
    public String manuscript(){ return "Manuscript"; };
    public String metadata(){ return "Metadata"; };
    public String notes(){ return "Notes"; };
    public String research(){ return "Research"; };
    public String resources(){ return "Resources"; };
    public String styleSheet(){ return "Style Sheet"; };
    public String summary(){ return "Summary"; };
    public String trash(){ return "Trash"; };
    
    ////////////
    /// Metadata
    ////////////
    
    
    public String labelContributors(){ return "Contributors:"; };
    public String labelCreated(){ return "Created:"; };
    public String labelGenres(){ return "Genres:"; };
    public String labelGlobal(){ return "Global:"; };
    public String labelIdentifier(){ return "Identifier:"; };
    public String labelKeywords(){ return "Keywords:"; };
    public String labelLanguage(){ return "Language:"; };
    public String labelMetadata(){ return "Metadata:"; };
    public String labelModified(){ return "Modified:"; };
    public String labelName(){ return "Name:"; };
    public String labelProgress(){ return "Progress:"; };
    public String labelProject(){ return "Project:"; };
    public String labelRole(){ return "Role:"; };
    public String labelSortByName(){ return "Sort By Name:"; };
    public String labelSubtitle(){ return "Subtitle:"; };
    public String labelTitle(){ return "Title:"; };
    public String labelTopics(){ return "Topics:"; };
    
    ////////////////////////////
    /// AppendPrependDialog.java
    ////////////////////////////
    
    public String appendToLinesLabel(){ return "Append to lines:"; };
    public String prependToLinesLabel(){ return "Prepend to lines:"; };
    public String appendPrependDialogTitle(){ return "Append/Prepend"; };
    
    //////////////////////
    /// CompileDialog.java
    //////////////////////
    
    public String exportFormatMacOnly(){ return "Sorry, export to ${format} is only supported on Mac."; };
    
    public String output(){ return "Output"; };
    public String include(){ return "Include"; };
    public String separators(){ return "Separators"; };
    
    public String compile(){ return "Compile"; };
    
    public String labelInputFormat(){ return "Input format:"; };
    public String labelOutputDir(){ return "Output folder:"; };
    
    public String separatorSame(){ return "Same level:"; };
    public String separatorHighterToLower(){ return "Higher to lower:"; };
    public String separatorLowerToHighter(){ return "Lower to higher:"; };
    public String endOfDocument(){ return "End of document:"; };
    
    //////////////////////
    /// CompileThread.java
    //////////////////////
    
    public String noSupportedFormats(){ return "No supported formats were selected for export."; };
    public String noSupportedFormatsDialogTitle(){ return "Nothing to export"; };
    
    ///////////////////
    /// Controller.java
    ///////////////////
    
    public String goToLineDialogTitle(){ return "Go to line"; };
    
    public String errorOpen(){ return "Sorry, an error occurred while opening the project."; };
    public String errorSave(){ return "Sorry, an error occurred while saving the project."; };
    public String errorAbout(){ return "Sorry, an error occurred loading the about file."; };
    public String errorHelp(){ return "Sorry, an error occurred loading the help file."; };
    public String errorCompile(){ return "Sorry, an error occurred while compiling the project."; };
    public String errorPreview(){ return "Sorry, an error occurred while generating the preview."; };
    
    public String saveLegacyVersion(){ return "This project is in a legacy format; you will have to save it as a new file."; };
    public String saveLegacyVersionDialogTitle(){ return "Conversion required"; };
    
    public String closeWithoutSave(){ return "Are you sure you want to close without saving? UNSAVED CHANGES WILL BE LOST."; };
    public String closeWithoutSaveDialogTitle(){ return "Close without saving?"; };
    
    public String emptyTrash(){ return "Are you sure you want to empty the trash. THIS CANNOT BE UNDONE."; };
    public String emptyTrashDialogTitle(){ return "Empty trash?"; };
    
    public String errorImport(){ return "Sorry, an error occurred while importing the file."; };
    public String errorDuplicateNode(){ return "Sorry, an error occurred while duplicating the selected node."; };
    public String errorSplit(){ return "Sorry, an error occurred while splitting the current document."; };
    public String errorEmptyTrash(){ return "Sorry, an error occurred while emptying the trash."; };
    public String errorScript(){ return "Sorry, an error occurred while executing this script."; };
    
    /////////////////////
    /// EmptyProject.java
    /////////////////////
    
    public String emptyProjectTitle(){ return "Empty project"; };
    public String emptyProjectDesc(){ return "An empty project"; };
    
    /////////////////////////////
    /// ExportCompleteDialog.java
    /////////////////////////////
    
    public String openExportFolder(){ return "Open export folder ..."; };
    public String exportCompleteLabel(){ return "Export complete:"; };
    
    /////////////////////
    /// FileTemplate.java
    /////////////////////
    
    public String customeTemplateDesc(){ return "A custome project template file"; };
    
    /////////////////////////////
    /// FindAndReplaceDialog.java
    /////////////////////////////
    
    public String findReplaceDialogTitle(){ return "Find / Replace"; };
    public String searchLabel(){ return "Search:"; };
    public String replaceLabel(){ return "Replace:"; };
    public String findAll(){ return "Find all ..."; };
    public String replaceAll(){ return "Replace all..."; };
    
    public String currentDocument(){ return "Replace all..."; };
    public String allDocuments(){ return "Replace all..."; };
    public String onlyManuscript(){ return "Only documents in the Manuscript (exclude research, etc)"; };
    public String ignoreCase(){ return "Ignore case"; };
    
    ///////////////////////
    /// FootnoteDialog.java
    ///////////////////////
    
    public String footnoteDialogTitle(){ return "Add a footnote"; };
    public String footnoteNameLabel(){ return "Footnote name:"; };
    public String footnoteBodyLabel(){ return "Footnote body:"; };
    
    ///////////////////
    /// GroovyMenu.java
    ///////////////////

    public String groovy(){ return "Groovy"; };
    public String groovyReload(){ return "Reload scripts"; };
    public String groovyOpenDir(){ return "Open script folder"; };
    public String groovyNoScripts(){ return "No scripts available"; };
    
    //////////////////////////////
    /// HyperlinkEditorDialog.java
    //////////////////////////////
    
    public String hyperlinkDialogTitle(){ return "Add a hyperlink"; };
    public String hyperlinkTextLabel(){ return "Text:"; };
    public String hyperlinkUrlLabel(){ return "URL:"; };
    
    ////////////////////////
    /// ImageFileFilter.java
    ////////////////////////
    
    public String imageFilterDesc(){ return "Images (JPEGs, PNGs, GIFs)"; };
    
    //////////////////////////
    /// InsertImageDialog.java
    //////////////////////////
    
    public String imageDialogTitle(){ return "Insert an image"; };
    
    ////////////////////////
    /// MarkdownMenuBar.java
    ////////////////////////
    
    public String menuNew(){ return "New ..."; };
    public String menuChild(){ return "Add a child ..."; };
    public String menuDelete(){ return "Delete ..."; };
    public String menuDuplicate(){ return "Delete ..."; };
    public String menuEmptyTrash(){ return "Empty trash ..."; };
    public String menuExport(){ return "Export"; };
    public String menuExportProject(){ return "Export project ..."; };
    public String menuExportProjectCurrentOptions(){ return "Export project with current options"; };
    public String menuExportDocument(){ return "Export document ..."; };
    public String menuExportDocumentCurrentOptions(){ return "Export current document with current options"; };
    public String menuExportDocumentAndChildren(){ return "Export current document and children"; };
    public String menuExportDocumentAndChildrenCurrentOptions(){ return "Export current document and children with current options"; };
    public String menuJoin(){ return "Join selected documents"; };
    public String menuPreview(){ return "Preview"; };
    public String menuPreviewDocument(){ return "Preview current document..."; };
    public String menuPreviewDocumentAndChildren(){ return "Preview current document and children..."; };
    public String menuRename(){ return "Rename ..."; };
    public String menuSibling(){ return "Add a sibling ..."; };
    
    //////////////////////////
    /// MacroEditorDialog.java
    //////////////////////////
    
    public String macrosDialogTitle(){ return "Macros"; };
    
    ////////////////////////////////
    /// MarkdownPreferencePanel.java
    ////////////////////////////////
    
    public String pandocPathLabel(){ return "`pandoc` path:"; };
    public String pdfLatexPathLabel(){ return "`pdflatex` path:"; };
    public String kindleGenPathLabel(){ return "`kindlegen` path:"; };
    
    /////////////////////////
    /// MarkdownTreeMenu.java
    /////////////////////////
    
    ///////////////////////
    /// MetadataWidget.java
    ///////////////////////
    
    public String addContributor(){ return "Add contributor"; };
    public String removeContributor(){ return "Remove contributor"; };
    
    /////////////////////
    /// NovelProject.java
    /////////////////////
    
    public String novelTitle(){ return "Novel"; };
    public String novelDesc(){ return "A novel template with beat sheets"; };
    
    //////////////////////////
    /// ProjectCoverPanel.java
    //////////////////////////
    
    public String noCover(){ return "No cover"; };
    public String errorSettingCoverImage(){ return "Sorry, an error occurred while setting the cover image."; };
    
    //////////////////////////////
    /// ProjectMetadataWidget.java
    //////////////////////////////
    
    ////////////////////////////////
    /// ProjectStatisticsDialog.java
    ////////////////////////////////
    
    public String statisticsDialogTitle(){ return "Project Statistics"; };
    public String characterLabel(){ return "Characters:"; };
    public String wordsLabel(){ return "Words:"; };
    public String pagesLabel(){ return "Pages:"; };
    public String cpwLabel(){ return "Chars/Word:"; };
    public String wppLabel(){ return "Words/Page:"; };
    
    /////////////////////////////
    /// ProjectXmlFileFilter.java
    /////////////////////////////
    
    public String mdpFilterDesc(){ return "Markdown Project XML (*.mdp.xml)"; };
    
    //////////////////////
    /// RecentProject.java
    //////////////////////
    
    public String projectFileMissing(){ return "Project file not found"; };
    
    ///////////////////////////
    /// RecentProjectsMenu.java
    ///////////////////////////
    
    public String recentProjects(){ return "Recent projects"; };
    public String noRecentProjects(){ return "No recent projects"; };
    public String clearRecentProjects(){ return "Clear recent projects"; };
    
    //////////////////////
    /// WelcomeScreen.java
    //////////////////////
    
    public String labelProjectTemplates(){ return "Project templates:"; };
    public String labelRecentProjects(){ return "Recent project:"; };
    
    public String buttonCreateNew(){ return "Create a new project ..."; };
    public String buttonOpenRecent(){ return "Open a recent project ..."; };
    public String buttonOpenExisting(){ return "Open an existing project ..."; };
    
    
}
