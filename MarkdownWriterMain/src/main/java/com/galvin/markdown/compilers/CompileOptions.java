package com.galvin.markdown.compilers;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import galvin.SystemUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "CompileOptions" )
@XmlAccessorType( XmlAccessType.FIELD )
public class CompileOptions
{

    private transient Project project;
    private transient List<Node> manuscript = new ArrayList();
    private transient List<Node> resources = new ArrayList();
    private transient File outputDirectory;
    private ImportFormat importFormat = ImportFormat.MARKDOWN;
    private List<ExportFormat> exportFormats = new ArrayList();
    private NodeSection nodeSection = NodeSection.MANUSCRIPT;
    private boolean includeTOC = true;
    private int tocDepth = 3;
    private int epubChapterLevel = 1;
    private boolean includeContributors = true;
    private boolean includeContributorRoles = true;
    private boolean includeTitlesOfFolders = true;
    private boolean includeSubtitlesOfFolders = true;
    private boolean includeTitlesOfFiles = false;
    private boolean includeSubtitlesOfFiles = false;
    private boolean generateTitleBlock = true;
    private String separatorFolderFolder = Markup.PAGE_BREAK;
    private String separatorFolderFile = Markup.PARAGRAPH_BREAK;
    private String separatorFileFolder = Markup.PAGE_BREAK;
    private String separatorFileFile = Markup.PARAGRAPH_BREAK;
    private String separatorTitleFile = Markup.PARAGRAPH_BREAK;
    private String separatorTitleFolder = Markup.PARAGRAPH_BREAK;
    private String unusedCustomSeparatorFolderFolder = "";
    private String unusedCustomSeparatorFolderFile = "";
    private String unusedCustomSeparatorFileFolder = "";
    private String unusedCustomSeparatorFileFile = "";
    private String unusedCustomSeparatorTitleFile = "";
    private String unusedCustomSeparatorTitleFolder = "";
    private String endOfDocumentMarker = "";
    private String projectContributorMarkup = "*";
    private String nodeContributorMarkup = "*";

    public CompileOptions()
    {
    }

    public CompileOptions( Project project )
    {
        this();

        this.project = project;

        File projectDirectory = project.getProjectDirectory();
        if( projectDirectory == null )
        {
            projectDirectory = SystemUtils.getRandomTempDir();
        }

        setOutputDirectory( new File( projectDirectory, ProjectIo.EXPORT_DIRECTORY ) );
        setManuscript( project.getManuscript().getChildNodes() );
        setResources( project.getResources().getChildNodes() );

        exportFormats.add( ExportFormat.XHTML );
        exportFormats.add( ExportFormat.PDF );
        exportFormats.add( ExportFormat.EPUB );
    }

    public void refreshNodes()
    {
        setManuscript( project.getManuscript().getChildNodes() );
        setResources( project.getResources().getChildNodes() );
    }

    public String getEndOfDocumentMarker()
    {
        return endOfDocumentMarker;
    }

    public void setEndOfDocumentMarker( String endOfDocumentMarker )
    {
        this.endOfDocumentMarker = endOfDocumentMarker;
    }

    public List<ExportFormat> getExportFormats()
    {
        return exportFormats;
    }

    public void setExportFormats( List<ExportFormat> exportFormats )
    {
        this.exportFormats = exportFormats;
    }

    public boolean includeContributors()
    {
        return includeContributors;
    }

    public void setIncludeContributors( boolean includeContributors )
    {
        this.includeContributors = includeContributors;
    }

    public boolean includeContributorRoles()
    {
        return includeContributorRoles;
    }

    public void setIncludeContributorRoles( boolean includeContributorRoles )
    {
        this.includeContributorRoles = includeContributorRoles;
    }

    public boolean includeTitlesOfFiles()
    {
        return includeTitlesOfFiles;
    }

    public void setIncludeTitlesOfFiles( boolean includeTitlesOfFiles )
    {
        this.includeTitlesOfFiles = includeTitlesOfFiles;
    }

    public boolean includeTitlesOfFolders()
    {
        return includeTitlesOfFolders;
    }

    public void setIncludeTitlesOfFolders( boolean includeTitlesOfFolders )
    {
        this.includeTitlesOfFolders = includeTitlesOfFolders;
    }

    public boolean includeSubtitlesOfFiles()
    {
        return includeSubtitlesOfFiles;
    }

    public void setIncludeSubtitlesOfFiles( boolean includeSubtitlesOfFiles )
    {
        this.includeSubtitlesOfFiles = includeSubtitlesOfFiles;
    }

    public boolean includeSubtitlesOfFolders()
    {
        return includeSubtitlesOfFolders;
    }

    public void setIncludeSubtitlesOfFolders( boolean includeSubtitlesOfFolders )
    {
        this.includeSubtitlesOfFolders = includeSubtitlesOfFolders;
    }

    public List<Node> getManuscript()
    {
        return manuscript;
    }

    public void setManuscript( List<Node> manuscript )
    {
        this.manuscript = manuscript;
    }

    public NodeSection getNodeSection()
    {
        return nodeSection;
    }

    public void setNodeSection( NodeSection nodeSection )
    {
        this.nodeSection = nodeSection;
    }

    public File getOutputDirectory()
    {
        return outputDirectory;
    }

    public void setOutputDirectory( File outputDirectory )
    {
        this.outputDirectory = outputDirectory;
    }

    public Project getProject()
    {
        return project;
    }

    public void setProject( Project project )
    {
        this.project = project;
        if( project != null )
        {
            File projectDirectory = project.getProjectDirectory();
            if( projectDirectory != null )
            {
                outputDirectory = new File( projectDirectory, ProjectIo.EXPORT_DIRECTORY );
            }
        }
    }

    public List<Node> getResources()
    {
        return resources;
    }

    public void setResources( List<Node> resources )
    {
        this.resources = resources;
    }

    public String getSeparatorFileFile()
    {
        return separatorFileFile;
    }

    public void setSeparatorFileFile( String separatorFileFile )
    {
        this.separatorFileFile = separatorFileFile;
    }

    public String getSeparatorFileFolder()
    {
        return separatorFileFolder;
    }

    public void setSeparatorFileFolder( String separatorFileFolder )
    {
        this.separatorFileFolder = separatorFileFolder;
    }

    public String getSeparatorFolderFile()
    {
        return separatorFolderFile;
    }

    public void setSeparatorFolderFile( String separatorFolderFile )
    {
        this.separatorFolderFile = separatorFolderFile;
    }

    public String getSeparatorFolderFolder()
    {
        return separatorFolderFolder;
    }

    public void setSeparatorFolderFolder( String separatorFolderFolder )
    {
        this.separatorFolderFolder = separatorFolderFolder;
    }

    public String getSeparatorTitleFile()
    {
        return separatorTitleFile;
    }

    public void setSeparatorTitleFile( String separatorTitleFile )
    {
        this.separatorTitleFile = separatorTitleFile;
    }

    public String getSeparatorTitleFolder()
    {
        return separatorTitleFolder;
    }

    public void setSeparatorTitleFolder( String separatorTitleFolder )
    {
        this.separatorTitleFolder = separatorTitleFolder;
    }

    public String getNodeContributorMarkup()
    {
        return nodeContributorMarkup;
    }

    public void setNodeContributorMarkup( String nodeContributorMarkup )
    {
        this.nodeContributorMarkup = nodeContributorMarkup;
    }

    public String getProjectContributorMarkup()
    {
        return projectContributorMarkup;
    }

    public void setProjectContributorMarkup( String projectContributorMarkup )
    {
        this.projectContributorMarkup = projectContributorMarkup;
    }

    public String getUnusedCustomSeparatorFileFile()
    {
        return unusedCustomSeparatorFileFile;
    }

    public void setUnusedCustomSeparatorFileFile( String unusedCustomSeparatorFileFile )
    {
        this.unusedCustomSeparatorFileFile = unusedCustomSeparatorFileFile;
    }

    public String getUnusedCustomSeparatorFileFolder()
    {
        return unusedCustomSeparatorFileFolder;
    }

    public void setUnusedCustomSeparatorFileFolder( String unusedCustomSeparatorFileFolder )
    {
        this.unusedCustomSeparatorFileFolder = unusedCustomSeparatorFileFolder;
    }

    public String getUnusedCustomSeparatorFolderFile()
    {
        return unusedCustomSeparatorFolderFile;
    }

    public void setUnusedCustomSeparatorFolderFile( String unusedCustomSeparatorFolderFile )
    {
        this.unusedCustomSeparatorFolderFile = unusedCustomSeparatorFolderFile;
    }

    public String getUnusedCustomSeparatorFolderFolder()
    {
        return unusedCustomSeparatorFolderFolder;
    }

    public void setUnusedCustomSeparatorFolderFolder( String unusedCustomSeparatorFolderFolder )
    {
        this.unusedCustomSeparatorFolderFolder = unusedCustomSeparatorFolderFolder;
    }

    public String getUnusedCustomSeparatorTitleFile()
    {
        return unusedCustomSeparatorTitleFile;
    }

    public void setUnusedCustomSeparatorTitleFile( String unusedCustomSeparatorTitleFile )
    {
        this.unusedCustomSeparatorTitleFile = unusedCustomSeparatorTitleFile;
    }

    public String getUnusedCustomSeparatorTitleFolder()
    {
        return unusedCustomSeparatorTitleFolder;
    }

    public void setUnusedCustomSeparatorTitleFolder( String unusedCustomSeparatorTitleFolder )
    {
        this.unusedCustomSeparatorTitleFolder = unusedCustomSeparatorTitleFolder;
    }

    public ImportFormat getImportFormat()
    {
        return importFormat;
    }

    public void setImportFormat( ImportFormat importFormat )
    {
        this.importFormat = importFormat;
    }

    public boolean includeTOC()
    {
        return includeTOC;
    }

    public void setIncludeTOC( boolean includeTOC )
    {
        this.includeTOC = includeTOC;
    }

    public int getTocDepth()
    {
        return tocDepth;
    }

    public void setTocDepth( int tocDepth )
    {
        this.tocDepth = tocDepth;
    }

    public int getEpubChapterLevel()
    {
        return epubChapterLevel;
    }

    public void setEpubChapterLevel( int epubChapterLevel )
    {
        this.epubChapterLevel = epubChapterLevel;
    }

    public boolean generateTitleBlock()
    {
        return generateTitleBlock;
    }

    public void setGenerateTitleBlock( boolean generateTitleBlock )
    {
        this.generateTitleBlock = generateTitleBlock;
    }
}
