/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs.search;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import galvin.StringUtils;
import static galvin.StringUtils.neverNull;
import java.util.List;

public class SearchTool {
    public static SearchResults findAll( String searchText, boolean ignoreCase,
                                         List<Node> nodes,
                                         boolean manuscript, boolean description, boolean summary, boolean notes ) {
        SearchResults results = new SearchResults( searchText, ignoreCase );

        if( ignoreCase ) {
            searchText = searchText.toLowerCase();
        }

        for( Node node : nodes ) {
            NodeSearchResults nodeHits = new NodeSearchResults(node);

            if( manuscript ) {
                addHits( node, nodeHits, NodeSection.MANUSCRIPT, searchText, ignoreCase );
            }

            if( description ) {
                addHits( node, nodeHits, NodeSection.DESCRIPTION, searchText, ignoreCase );
            }

            if( summary ) {
                addHits( node, nodeHits, NodeSection.SUMMARY, searchText, ignoreCase );
            }

            if( notes ) {
                addHits( node, nodeHits, NodeSection.NOTES, searchText, ignoreCase );
            }

            if( nodeHits.hasHits() ) {
                results.getResults().add( nodeHits );
            }
        }

        return results;
    }

    private static void addHits( Node node,
                                 NodeSearchResults nodeHits,
                                 NodeSection section,
                                 String searchText,
                                 boolean ignoreCase ) {
        MarkdownDocument document = null;
        String _body = neverNull( node.getManuscriptText() );
        String body = _body;
        if( ignoreCase ) {
            body = body.toLowerCase();
        }

        int index = 0;
        while( index != -1 ) {
            index = body.indexOf( searchText, index );
            if( index != -1 ) {
                if( document == null ) {
                    if( section == NodeSection.MANUSCRIPT ) {
                        document = node.getManuscript();
                    }
                    else if( section == NodeSection.DESCRIPTION ) {
                        document = node.getDescription();
                    }
                    else if( section == NodeSection.SUMMARY ) {
                        document = node.getSummary();
                    }
                    else if( section == NodeSection.NOTES ) {
                        document = node.getNotes();
                    }
                }

                SearchResult hit = new SearchResult( document,
                                                     searchText,
                                                     getSearchSnippet( _body, index ),
                                                     index );
                nodeHits.getManuscriptResults().add( hit );
                index += searchText.length();
            }
        }
    }

    public static String getSearchSnippet( String text, int index ) {
        return getSearchSnippet( text, index, 50 );
    }

    public static String getSearchSnippet( String text, int index, int padding ) {
        int startIndex = index;
        int endIndex = index;

        startIndex -= padding;
        if( startIndex < 0 ) {
            startIndex = 0;
        }
        else {
            while( startIndex > 0 && !Character.isWhitespace( text.charAt( startIndex ) ) ) {
                startIndex--;
            }
        }

        endIndex += padding;
        if( endIndex >= text.length() ) {
            endIndex = text.length();
        }
        else {
            while( endIndex <= text.length() && !Character.isWhitespace( text.charAt( endIndex ) ) ) {
                endIndex--;
            }
        }

        String result = text.substring( startIndex, endIndex );
        result = StringUtils.replaceAll( result, "\n", " " );
        return result;
    }

}
