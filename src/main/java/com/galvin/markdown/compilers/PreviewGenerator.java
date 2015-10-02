/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.compilers;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import galvin.SystemUtils;
import java.io.File;
import java.util.List;

public class PreviewGenerator
{

    public File getPreview( MarkdownCompiler compiler, CompileOptions compileOptions, Node node, NodeSection nodeSection )
        throws Exception
    {
        return getPreview( compiler, compileOptions, node, nodeSection, false );
    }
    
    public File getPreview( MarkdownCompiler compiler, CompileOptions compileOptions, Node node, NodeSection nodeSection, boolean includeChildren )
        throws Exception
    {
        Project project = compileOptions.getProject();
        if( project != null )
        {
            node = ProjectIo.clone( node, project.getProjectDirectory(), true, true, includeChildren );
            
            if( !includeChildren )
            {
                node.getChildNodes().clear();
            }
            
            project = ProjectIo.clone( project, true, true );
            project.setTitle( node.getTitle() );
            project.setSubtitle( node.getSubtitle() );
            project.getManuscript().getChildNodes().clear();
            project.getManuscript().getChildNodes().add( node );
            project.setProjectDirectory( SystemUtils.getRandomTempDir() );
            System.out.println( "Writing preview to: " + project.getProjectDirectory() );

            CompileOptions options = ProjectIo.clone( project.getCompileOptions() );
            options.setProject( project );
            options.setOutputDirectory( new File( project.getProjectDirectory(), "export" ) );
            options.refreshNodes();
            options.setNodeSection( nodeSection );
            options.getExportFormats().clear();
            options.getExportFormats().add( ExportFormat.XHTML );

            List<CompileResults> results = compiler.compile( options );
            if( !results.isEmpty() )
            {
                return results.get( 0 ).getFile();
            }
        }

        return null;
    }
}
