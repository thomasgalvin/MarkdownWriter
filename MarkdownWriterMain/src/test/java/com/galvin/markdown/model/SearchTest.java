package com.galvin.markdown.model;

import com.galvin.markdown.search.Hit;
import com.galvin.markdown.search.ProjectSearch;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SearchTest {
    private static final Logger logger = LoggerFactory.getLogger( SearchTest.class );

    public static final String LOREM = "Lorem ipsum dolor sit amet, consectetur "
                                       + "adipisicing elit, sed do eiusmod tempor "
                                       + "incididunt ut labore et dolore magna aliqua. "
                                       + " "
                                       + "Ut enim ad minim veniam, quis nostrud "
                                       + "exercitation ullamco laboris nisi ut "
                                       + "aliquip ex ea commodo consequat."
                                       + " "
                                       + "Duis aute irure dolor in reprehenderit "
                                       + "in voluptate velit esse cillum dolore "
                                       + "eu fugiat nulla pariatur. "
                                       + " "
                                       + "Excepteur sint occaecat cupidatat non "
                                       + "proident, sunt in culpa qui officia "
                                       + "deserunt mollit anim id est laborum."
                                       + " ";

    @Test
    public void testSearch()
        throws Exception {
        Project project = getTestProject();
        doSearchTerm( "chapter one", project );
        doSearchTerm( "chapter two", project );
        doSearchTerm( "chapter three", project );
        doSearchTerm( "section a", project );
        doSearchTerm( "section b", project );
        doSearchTerm( "section c", project );
    }
    
    private static void doSearchTerm( String target, Project project ) throws Exception {
        List<Hit> hits = ProjectSearch.search( project, target, false, true, true, true, true, true );
        Assert.assertEquals( "Wrong number of hits for: " + target, 4, hits.size() );
        
        hits = ProjectSearch.search( project, target, true, true, true, true, true, true );
        Assert.assertEquals( "Wrong number of hits for: " + target, 5, hits.size() );
        
        hits = ProjectSearch.search( project, target, true, false, false, false, false, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 0, hits.size() );
        
        hits = ProjectSearch.search( project, target, true, true, false, false, false, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 1, hits.size() );
        
        hits = ProjectSearch.search( project, target, true, false, true, false, false, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 1, hits.size() );
        
        hits = ProjectSearch.search( project, target, true, false, false, true, false, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 1, hits.size() );
        
        hits = ProjectSearch.search( project, target, true, false, false, false, true, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 1, hits.size() );
        
        hits = ProjectSearch.search( project, target, true, false, false, false, false, true );
        Assert.assertEquals( "Wrong number of hits for: " + target, 1, hits.size() );
        
        
        
        hits = ProjectSearch.search( project, target, false, false, false, false, false, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 0, hits.size() );
        
        hits = ProjectSearch.search( project, target, false, true, false, false, false, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 0, hits.size() );
        
        hits = ProjectSearch.search( project, target, false, false, true, false, false, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 1, hits.size() );
        
        hits = ProjectSearch.search( project, target, false, false, false, true, false, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 1, hits.size() );
        
        hits = ProjectSearch.search( project, target, false, false, false, false, true, false );
        Assert.assertEquals( "Wrong number of hits for: " + target, 1, hits.size() );
        
        hits = ProjectSearch.search( project, target, false, false, false, false, false, true );
        Assert.assertEquals( "Wrong number of hits for: " + target, 1, hits.size() );
    }
        

    private static Project getTestProject() {
        Project project = new Project();

        Node chapterOne = new Node();
        project.getChildNodes().add( chapterOne );
        chapterOne.setTitle( "Chapter One" );
        chapterOne.getManuscript().setText( LOREM + " chapter one " + LOREM );
        chapterOne.getDescription().setText( LOREM + " chapter one " + LOREM );
        chapterOne.getSummary().setText( LOREM + " chapter one " + LOREM );
        chapterOne.getNotes().setText( LOREM + " chapter one " + LOREM );

        Node sectionA = new Node();
        chapterOne.getChildNodes().add( sectionA );
        sectionA.setTitle( "Section A" );
        sectionA.getManuscript().setText( LOREM + " section a " + LOREM );
        sectionA.getDescription().setText( LOREM + " section a " + LOREM );
        sectionA.getSummary().setText( LOREM + " section a " + LOREM );
        sectionA.getNotes().setText( LOREM + " section a " + LOREM );

        Node chapterTwo = new Node();
        project.getChildNodes().add( chapterTwo );
        chapterTwo.setTitle( "Chapter Two" );
        chapterTwo.getManuscript().setText( LOREM + " chapter two " + LOREM );
        chapterTwo.getDescription().setText( LOREM + " chapter two " + LOREM );
        chapterTwo.getSummary().setText( LOREM + " chapter two " + LOREM );
        chapterTwo.getNotes().setText( LOREM + " chapter two " + LOREM );

        Node sectionB = new Node();
        chapterTwo.getChildNodes().add( sectionB );
        sectionB.setTitle( "Section B" );
        sectionB.getManuscript().setText( LOREM + " section b " + LOREM );
        sectionB.getDescription().setText( LOREM + " section b " + LOREM );
        sectionB.getSummary().setText( LOREM + " section b " + LOREM );
        sectionB.getNotes().setText( LOREM + " section b " + LOREM );

        Node chapterThree = new Node();
        project.getChildNodes().add( chapterThree );
        chapterThree.setTitle( "Chapter Three" );
        chapterThree.getManuscript().setText( LOREM + " chapter three " + LOREM );
        chapterThree.getDescription().setText( LOREM + " chapter three " + LOREM );
        chapterThree.getSummary().setText( LOREM + " chapter three " + LOREM );
        chapterThree.getNotes().setText( LOREM + " chapter three " + LOREM );

        Node sectionC = new Node();
        chapterThree.getChildNodes().add( sectionC );
        sectionC.setTitle( "Section C" );
        sectionC.getManuscript().setText( LOREM + " section c " + LOREM );
        sectionC.getDescription().setText( LOREM + " section c " + LOREM );
        sectionC.getSummary().setText( LOREM + " section c " + LOREM );
        sectionC.getNotes().setText( LOREM + " section c " + LOREM );

        return project;
    }

}
