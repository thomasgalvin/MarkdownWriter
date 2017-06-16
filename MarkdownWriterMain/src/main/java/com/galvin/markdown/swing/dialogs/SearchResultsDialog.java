/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.dialogs;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.dialogs.search.SearchResults;
import com.galvin.markdown.swing.dialogs.search.SearchResultsTree;
import galvin.swing.GuiUtils;
import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JScrollPane;

public class SearchResultsDialog
    extends JDialog {
    private SearchResultsTree searchResultsTree;
    private JScrollPane scrollPane;

    public SearchResultsDialog( Controller controller, SearchResults results ) {
        MarkdownMessages messages = MarkdownServer.getMessages();
        String message = messages.searchResultsDialogTitle() + results.getSearchTerm();
        if( results.ignoreCase() ) {
            message += messages.ignoringCase();
        }

        setTitle( message );
        searchResultsTree = new SearchResultsTree( controller, results );
        scrollPane = new JScrollPane( searchResultsTree );

        getContentPane().setLayout( new BorderLayout() );
        getContentPane().add( scrollPane, BorderLayout.CENTER );
        setSize( 800, 500 );
        GuiUtils.center( this );
    }

}
