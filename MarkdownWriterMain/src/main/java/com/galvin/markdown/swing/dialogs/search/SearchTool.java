/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs.search;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import galvin.StringUtils;
import galvin.swing.text.DocumentUtils;
import java.util.HashMap;
import java.util.List;
import javax.swing.text.Document;

public class SearchTool
{

    public static SearchResults findAll( String searchText, boolean ignoreCase, List<Document> documents )
    {
        SearchResults result = new SearchResults( searchText, ignoreCase );

        HashMap<Node, NodeSearchResults> nodeResultsMap = new HashMap();

        for(Document document : documents)
        {
            System.out.println( "Searching document: " + document );
            
            if( document instanceof MarkdownDocument )
            {
                MarkdownDocument markdownDocument = (MarkdownDocument) document;
                Node node = markdownDocument.getNode();

                NodeSearchResults nodeResults = getNodeResults( result, nodeResultsMap, node );

                String text = DocumentUtils.getText( document );
                String actualText = text;
                String actualSearchText = searchText;
                
                System.out.println( "text:\n" + text );

                if( ignoreCase )
                {
                    actualText = text.toLowerCase();
                    actualSearchText = searchText.toLowerCase();
                }

                List<SearchResult> documentResults = getSearchResults( nodeResults, markdownDocument.getNodeSection() );
                if( documentResults != null )
                {
                    int index = 0;
                    while( index != -1 )
                    {
                        index = actualText.indexOf( actualSearchText, index );
                        if( index != -1 )
                        {
                            String snippet = getSearchSnippet( text, index );
                            SearchResult searchResult = new SearchResult( markdownDocument, searchText, snippet, index );
                            documentResults.add( searchResult );

                            index += searchText.length();
                        }
                    }
                }
            }
        }

        return result;
    }

    public static List<SearchResult> getSearchResults( NodeSearchResults results, NodeSection section )
    {
        if( NodeSection.MANUSCRIPT.equals( section ) )
        {
            return results.getManuscriptResults();
        }
        else if( NodeSection.DESCRIPTION.equals( section ) )
        {
            return results.getDescriptionResults();
        }
        else if( NodeSection.SUMMARY.equals( section ) )
        {
            return results.getSummaryResults();
        }
        else if( NodeSection.NOTES.equals( section ) )
        {
            return results.getNotesResults();
        }

        return null;
    }

    public static String getSearchSnippet( String text, int index )
    {
        return getSearchSnippet( text, index, 50 );
    }

    public static String getSearchSnippet( String text, int index, int padding )
    {
        int startIndex = index;
        int endIndex = index;

        startIndex -= padding;
        if( startIndex < 0 )
        {
            startIndex = 0;
        }
        else
        {
            while( startIndex > 0 && !Character.isWhitespace( text.charAt( startIndex ) ) )
            {
                startIndex--;
            }
        }

        endIndex += padding;
        if( endIndex >= text.length() )
        {
            endIndex = text.length();
        }
        else
        {
            while( endIndex <= text.length() && !Character.isWhitespace( text.charAt( endIndex ) ) )
            {
                endIndex--;
            }
        }

        String result = text.substring( startIndex, endIndex );
        result = StringUtils.replaceAll( result, "\n", " " );
        return result;
    }

    public static NodeSearchResults getNodeResults( SearchResults result, HashMap<Node, NodeSearchResults> nodeResults, Node node )
    {
        NodeSearchResults results = nodeResults.get( node );
        if( results == null )
        {
            results = new NodeSearchResults( node );
            nodeResults.put( node, results );
            result.getResults().add( results );
        }
        return results;
    }
}
