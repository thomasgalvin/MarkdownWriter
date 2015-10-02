package com.galvin.markdown.search;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.model.Project;
import java.util.ArrayList;
import java.util.List;

public class ProjectSearch {
    private static final int window = 50;
    
    public static List<Hit> search(Project project,
                                   String searchTerm,
                                   boolean ignoreCase,
                                   boolean title,
                                   boolean manuscript,
                                   boolean description,
                                   boolean summary,
                                   boolean notes) {
        List<Hit> result = new ArrayList();

        List<Node> nodes = project.getChildNodes();
        result.addAll( search( nodes, searchTerm, ignoreCase, title, manuscript, description, summary, notes ) );

        return result;
    }

    private static List<Hit> search(List<Node> nodes,
                                    String searchTerm,
                                    boolean ignoreCase,
                                    boolean title,
                                    boolean manuscript,
                                    boolean description,
                                    boolean summary,
                                    boolean notes) {
        List<Hit> result = new ArrayList();
        for( Node node : nodes ) {
            result.addAll( search( node, searchTerm, ignoreCase, title, manuscript, description, summary, notes ) );
        }
        return result;
    }

    private static List<Hit> search(Node node,
                                    String searchTerm,
                                    boolean ignoreCase,
                                    boolean title,
                                    boolean manuscript,
                                    boolean description,
                                    boolean summary,
                                    boolean notes) {
        List<Hit> result = new ArrayList();

        if( title ) {
            result.addAll( search( node, NodeSection.TITLE, searchTerm, node.getTitle(), ignoreCase ) );
        }

        if( manuscript ) {
            if( node.getManuscriptIfNotNull() != null ) {
                result.addAll( search( node, NodeSection.MANUSCRIPT, searchTerm, node.getManuscript().getText(), ignoreCase ) );
            }
        }

        if( description ) {
            if( node.getDescriptionIfNotNull() != null ) {
                result.addAll( search( node, NodeSection.DESCRIPTION, searchTerm, node.getDescription().getText(), ignoreCase ) );
            }
        }

        if( summary ) {
            if( node.getSummaryIfNotNull() != null ) {
                result.addAll( search( node, NodeSection.SUMMARY, searchTerm, node.getSummary().getText(), ignoreCase ) );
            }
        }

        if( notes ) {
            if( node.getNotesIfNotNull() != null ) {
                result.addAll( search( node, NodeSection.NOTES, searchTerm, node.getNotes().getText(), ignoreCase ) );
            }
        }

        for( Node child : node.getChildNodes() ) {
            result.addAll( search( child, searchTerm, ignoreCase, title, manuscript, description, summary, notes ) );
        }

        return result;
    }

    private static List<Hit> search(Node node,
                                    NodeSection section,
                                    String searchTerm,
                                    String body,
                                    boolean ignoreCase) {
        List<Hit> result = new ArrayList();

        String originalSearchTerm = searchTerm;
        String originalBodyTerm = body;
        
        if( ignoreCase ) {
            searchTerm = searchTerm.toLowerCase();
            body = body.toLowerCase();
        }

        int hitIndex = body.indexOf( searchTerm );
        while( hitIndex != -1 ) {
            int endIndex = hitIndex + searchTerm.length();
            String snippet = getSnippet( originalBodyTerm, hitIndex, endIndex );
            
            Hit hit = new Hit( originalSearchTerm, snippet, node, section, hitIndex, endIndex );
            result.add( hit );

            hitIndex++;
            if( hitIndex < body.length() ) {
                hitIndex = body.indexOf( searchTerm, hitIndex );
            }
        }

        return result;
    }

    private static String getSnippet( String body, int startIndex, int endIndex )
    {
        startIndex = Math.max( 0, startIndex - window );
        endIndex = Math.min( body.length(), endIndex + window );
        return body.substring( startIndex, endIndex );
    }
}
