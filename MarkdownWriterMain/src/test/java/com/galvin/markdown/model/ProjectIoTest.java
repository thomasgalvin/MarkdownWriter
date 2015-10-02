package com.galvin.markdown.model;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.tree.MarkdownTree;
import galvin.SystemUtils;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class ProjectIoTest
{
    @Test
    public void testMarshalling() throws Exception
    {
        File projectDir = SystemUtils.getRandomTempDir( "test-project.mdp" );
        File projectFile = new File( projectDir, "test.mdp.xml" );
        System.out.println( "writing project to: " + projectDir );
        
        Project project = ProjectIo.createDefaultProject();
        ProjectIo.write( project, projectFile );
        Project loaded = ProjectIo.readProject( projectFile );
        
        Assert.assertEquals( ProjectIo.toString( project, true ), 
                             ProjectIo.toString( loaded, true ) );
        
        project.stopSpellCheck();
        loaded.stopSpellCheck();
        
        try
        {
            FileUtils.deleteDirectory( projectDir );
        }
        catch( Throwable t )
        {
            //eat it
        }
    }
    
    @Test
    public void testSerializeTree() throws Exception
    {
        Project project = ProjectIo.createDummyProject();
        MarkdownTree tree = new MarkdownTree( new Controller( null ), project );
        Project loaded = ProjectIo.toProject( tree );
        Assert.assertEquals( ProjectIo.toString( project ), 
                             ProjectIo.toString( loaded ) );
    }
}
