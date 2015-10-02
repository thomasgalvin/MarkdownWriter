/**
Copyright &copy 2014 Thomas Galvin - All Rights Reserved.
*/

package com.galvin.markdown.compilers;

import com.galvin.markdown.model.Node;
import java.util.ArrayList;
import java.util.List;

public class EpubCompiler
{
    public static List<CompileResults> compile( CompileOptions compileOptions, MarkdownCompiler compiler )
        throws Exception
    {
        List<CompileResults> results = new ArrayList();
        
        Section currentSection = new Section();
        List<Section> sections = new ArrayList();
        
        List<Node> manuscript = compileOptions.getManuscript();
        for( Node node : manuscript )
        {
            currentSection = partition( node, currentSection, sections );
        }
        
        return results;
    }
    
    private static Section partition( Node node, Section currentSection, List<Section> sections )
    {
        String nodeType = node.getNodeType();
        
        return currentSection;
    }
    
    private static class Section
    {
        List<Node> nodes = new ArrayList();
    }
}
