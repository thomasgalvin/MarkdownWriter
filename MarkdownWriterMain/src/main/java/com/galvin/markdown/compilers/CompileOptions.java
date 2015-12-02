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
    private boolean generateTitleBlock = true;
    
    private NodeSeparators separators = new NodeSeparators();
    
    @Deprecated private boolean includeContributors = true;
    @Deprecated private boolean includeContributorRoles = true;
    @Deprecated private boolean includeTitlesOfFolders = true;
    @Deprecated private boolean includeSubtitlesOfFolders = true;
    @Deprecated private boolean includeTitlesOfFiles = false;
    @Deprecated private boolean includeSubtitlesOfFiles = false;
    
    @Deprecated private String separatorFolderFolder = Markup.PAGE_BREAK;
    @Deprecated private String separatorFolderFile = Markup.PARAGRAPH_BREAK;
    @Deprecated private String separatorFileFolder = Markup.PAGE_BREAK;
    @Deprecated private String separatorFileFile = Markup.PARAGRAPH_BREAK;
    @Deprecated private String separatorTitleFile = Markup.PARAGRAPH_BREAK;
    @Deprecated private String separatorTitleFolder = Markup.PARAGRAPH_BREAK;
    
    @Deprecated private String unusedCustomSeparatorFolderFolder = "";
    @Deprecated private String unusedCustomSeparatorFolderFile = "";
    @Deprecated private String unusedCustomSeparatorFileFolder = "";
    @Deprecated private String unusedCustomSeparatorFileFile = "";
    @Deprecated private String unusedCustomSeparatorTitleFile = "";
    @Deprecated private String unusedCustomSeparatorTitleFolder = "";
    
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

    public List<ExportFormat> getExportFormats()
    {
        return exportFormats;
    }

    public void setExportFormats( List<ExportFormat> exportFormats )
    {
        this.exportFormats = exportFormats;
    }

    @Deprecated 
    public boolean includeContributors()
    {
        return includeContributors;
    }

    @Deprecated 
    public void setIncludeContributors( boolean includeContributors )
    {
        this.includeContributors = includeContributors;
    }

    @Deprecated 
    public boolean includeContributorRoles()
    {
        return includeContributorRoles;
    }

    @Deprecated 
    public void setIncludeContributorRoles( boolean includeContributorRoles )
    {
        this.includeContributorRoles = includeContributorRoles;
    }

    @Deprecated 
    public boolean includeTitlesOfFiles()
    {
        return includeTitlesOfFiles;
    }

    @Deprecated 
    public void setIncludeTitlesOfFiles( boolean includeTitlesOfFiles )
    {
        this.includeTitlesOfFiles = includeTitlesOfFiles;
    }

    @Deprecated 
    public boolean includeTitlesOfFolders()
    {
        return includeTitlesOfFolders;
    }

    @Deprecated 
    public void setIncludeTitlesOfFolders( boolean includeTitlesOfFolders )
    {
        this.includeTitlesOfFolders = includeTitlesOfFolders;
    }

    @Deprecated 
    public boolean includeSubtitlesOfFiles()
    {
        return includeSubtitlesOfFiles;
    }

    @Deprecated 
    public void setIncludeSubtitlesOfFiles( boolean includeSubtitlesOfFiles )
    {
        this.includeSubtitlesOfFiles = includeSubtitlesOfFiles;
    }

    @Deprecated 
    public boolean includeSubtitlesOfFolders()
    {
        return includeSubtitlesOfFolders;
    }

    @Deprecated 
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

    @Deprecated 
    public String getSeparatorFileFile()
    {
        return separatorFileFile;
    }

    @Deprecated 
    public void setSeparatorFileFile( String separatorFileFile )
    {
        this.separatorFileFile = separatorFileFile;
    }

    @Deprecated 
    public String getSeparatorFileFolder()
    {
        return separatorFileFolder;
    }

    @Deprecated 
    public void setSeparatorFileFolder( String separatorFileFolder )
    {
        this.separatorFileFolder = separatorFileFolder;
    }

    @Deprecated 
    public String getSeparatorFolderFile()
    {
        return separatorFolderFile;
    }

    @Deprecated 
    public void setSeparatorFolderFile( String separatorFolderFile )
    {
        this.separatorFolderFile = separatorFolderFile;
    }

    @Deprecated 
    public String getSeparatorFolderFolder()
    {
        return separatorFolderFolder;
    }

    @Deprecated 
    public void setSeparatorFolderFolder( String separatorFolderFolder )
    {
        this.separatorFolderFolder = separatorFolderFolder;
    }

    @Deprecated 
    public String getSeparatorTitleFile()
    {
        return separatorTitleFile;
    }

    @Deprecated 
    public void setSeparatorTitleFile( String separatorTitleFile )
    {
        this.separatorTitleFile = separatorTitleFile;
    }

    @Deprecated 
    public String getSeparatorTitleFolder()
    {
        return separatorTitleFolder;
    }

    @Deprecated 
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

    @Deprecated 
    public String getUnusedCustomSeparatorFileFile()
    {
        return unusedCustomSeparatorFileFile;
    }

    @Deprecated 
    public void setUnusedCustomSeparatorFileFile( String unusedCustomSeparatorFileFile )
    {
        this.unusedCustomSeparatorFileFile = unusedCustomSeparatorFileFile;
    }

    @Deprecated 
    public String getUnusedCustomSeparatorFileFolder()
    {
        return unusedCustomSeparatorFileFolder;
    }

    @Deprecated 
    public void setUnusedCustomSeparatorFileFolder( String unusedCustomSeparatorFileFolder )
    {
        this.unusedCustomSeparatorFileFolder = unusedCustomSeparatorFileFolder;
    }

    @Deprecated 
    public String getUnusedCustomSeparatorFolderFile()
    {
        return unusedCustomSeparatorFolderFile;
    }

    @Deprecated 
    public void setUnusedCustomSeparatorFolderFile( String unusedCustomSeparatorFolderFile )
    {
        this.unusedCustomSeparatorFolderFile = unusedCustomSeparatorFolderFile;
    }

    @Deprecated 
    public String getUnusedCustomSeparatorFolderFolder()
    {
        return unusedCustomSeparatorFolderFolder;
    }

    @Deprecated 
    public void setUnusedCustomSeparatorFolderFolder( String unusedCustomSeparatorFolderFolder )
    {
        this.unusedCustomSeparatorFolderFolder = unusedCustomSeparatorFolderFolder;
    }

    @Deprecated 
    public String getUnusedCustomSeparatorTitleFile()
    {
        return unusedCustomSeparatorTitleFile;
    }

    @Deprecated 
    public void setUnusedCustomSeparatorTitleFile( String unusedCustomSeparatorTitleFile )
    {
        this.unusedCustomSeparatorTitleFile = unusedCustomSeparatorTitleFile;
    }

    @Deprecated 
    public String getUnusedCustomSeparatorTitleFolder()
    {
        return unusedCustomSeparatorTitleFolder;
    }

    @Deprecated 
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

    public NodeSeparators getSeparators() {
        return separators;
    }

    public void setSeparators( NodeSeparators separators ) {
        this.separators = separators;
    }
}
