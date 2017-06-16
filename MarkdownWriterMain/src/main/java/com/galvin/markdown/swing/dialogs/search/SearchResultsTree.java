/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs.search;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import java.util.List;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class SearchResultsTree
    extends JTree
    implements TreeSelectionListener {

    private Controller controller;
    private SearchResults results;
    private MarkdownMessages messages = MarkdownServer.getMessages();

    public SearchResultsTree( Controller controller, SearchResults results ) {
        this.controller = controller;
        this.results = results;
        setCellRenderer( new SearchResultsTreeCellRenderer() );

        String message = "Search results for: " + results.getSearchTerm();
        if( results.ignoreCase() ) {
            message += " (ignoring case)";
        }

        DefaultMutableTreeNode root = new DefaultMutableTreeNode( message );
        DefaultTreeModel model = new DefaultTreeModel( root );
        setModel( model );
        setRootVisible( true );

        for( NodeSearchResults nodeSearchResults : results.getResults() ) {
            addNodes( root, model, nodeSearchResults );
        }

        for( int i = 0; i < getRowCount(); i++ ) {
            expandRow( i );
        }

        addTreeSelectionListener( this );
    }

    public void addNodes( DefaultMutableTreeNode root, DefaultTreeModel model, NodeSearchResults nodeSearchResults ) {
        List<SearchResult> manuscriptResults = nodeSearchResults.getManuscriptResults();
        List<SearchResult> descriptionResults = nodeSearchResults.getDescriptionResults();
        List<SearchResult> summaryResults = nodeSearchResults.getSummaryResults();
        List<SearchResult> notesResults = nodeSearchResults.getNotesResults();

        if(    !manuscriptResults.isEmpty()
            || !descriptionResults.isEmpty()
            || !summaryResults.isEmpty()
            || !notesResults.isEmpty() ) {

            DefaultMutableTreeNode nodeSearchResultsNode = new DefaultMutableTreeNode( nodeSearchResults );
            root.add( nodeSearchResultsNode );

            if( !manuscriptResults.isEmpty() ) {
                DefaultMutableTreeNode manuscriptResultsNode = new DefaultMutableTreeNode( messages.manuscript() );
                nodeSearchResultsNode.add( manuscriptResultsNode );
                for( SearchResult searchResult : manuscriptResults ) {
                    DefaultMutableTreeNode searchResultNode = new DefaultMutableTreeNode( searchResult );
                    manuscriptResultsNode.add( searchResultNode );
                }
            }

            if( !descriptionResults.isEmpty() ) {
                DefaultMutableTreeNode descriptionResultsNode = new DefaultMutableTreeNode( messages.description() );
                nodeSearchResultsNode.add( descriptionResultsNode );
                for( SearchResult searchResult : descriptionResults ) {
                    DefaultMutableTreeNode searchResultNode = new DefaultMutableTreeNode( searchResult );
                    descriptionResultsNode.add( searchResultNode );
                }
            }

            if( !summaryResults.isEmpty() ) {
                DefaultMutableTreeNode summaryResultsNode = new DefaultMutableTreeNode( messages.summary() );
                nodeSearchResultsNode.add( summaryResultsNode );
                for( SearchResult searchResult : summaryResults ) {
                    DefaultMutableTreeNode searchResultNode = new DefaultMutableTreeNode( searchResult );
                    summaryResultsNode.add( searchResultNode );
                }
            }

            if( !notesResults.isEmpty() ) {
                DefaultMutableTreeNode notesResultsNode = new DefaultMutableTreeNode( messages.notes() );
                nodeSearchResultsNode.add( notesResultsNode );
                for( SearchResult searchResult : notesResults ) {
                    DefaultMutableTreeNode searchResultNode = new DefaultMutableTreeNode( searchResult );
                    notesResultsNode.add( searchResultNode );
                }
            }
        }
    }

    @Override
    public void valueChanged( TreeSelectionEvent tse ) {
        if( getSelectionCount() == 1 ) {
            Object value = getLastSelectedPathComponent();
            if( value instanceof DefaultMutableTreeNode ) {
                DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode)value;
                Object userObject = defaultMutableTreeNode.getUserObject();

                if( userObject instanceof NodeSearchResults ) {
                    NodeSearchResults nodeSearchResults = (NodeSearchResults)userObject;
                    Node node = nodeSearchResults.getNode();
                    controller.getProjectFrame().editNode( node );

                    SwingUtilities.invokeLater( new DocumentSelectionThread( node, null, -1, -1) );
                }
                else if( userObject instanceof SearchResult ) {
                    SearchResult searchResult = (SearchResult)userObject;
                    MarkdownDocument document = searchResult.getDocument();
                    Node node = document.getNode();

                    int selectionStart = searchResult.getLocation();
                    int selectionEnd = selectionStart + searchResult.getSearchTerm().length();

                    SwingUtilities.invokeLater( new DocumentSelectionThread( node, document.getNodeSection(), selectionStart, selectionEnd ) );
                }
            }
        }
    }

    private class DocumentSelectionThread
        extends Thread {
        
        private Node node;
        private NodeSection section;
        private int start;
        private int end;

        public DocumentSelectionThread( Node node, NodeSection section, int start, int end ) {
            this.node = node;
            this.section = section;
            this.start = start;
            this.end = end;
        }

        @Override
        public void run() {
            controller.getProjectFrame().editNode( node );
            
            if( section != null ){
                controller.getProjectFrame().getEditorPanel().changeSection( section );
            }
                    
            controller.getProjectFrame().getWindow().toFront();
            controller.getProjectFrame().getWindow().requestFocus();
            controller.getProjectFrame().getEditor().requestFocus();
            
            if( start != -1 && end != -1 ){
                controller.getProjectFrame().getEditor().setCaretPosition( start );
                controller.getProjectFrame().getEditor().moveCaretPosition( end );
            }
        }

    }

}
