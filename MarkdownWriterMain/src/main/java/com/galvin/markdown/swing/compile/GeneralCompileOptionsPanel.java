/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import java.awt.Dimension;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;

public class GeneralCompileOptionsPanel
    extends AbstractCompileOptionsPanel {
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JRadioButton includeManuscript = new JRadioButton( messages.manuscript() );
    private JRadioButton includeDescription = new JRadioButton( messages.description() );
    private JRadioButton includeSummary = new JRadioButton( messages.summary() );
    private JRadioButton includeNotes = new JRadioButton( messages.notes() );
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JFileChooser fileChooser = new JFileChooser();
    private Dimension radioSize = GuiUtils.sameSize( new JComponent[]{ includeManuscript,
                                                                       includeDescription,
                                                                       includeSummary,
                                                                       includeNotes, } );

    public GeneralCompileOptionsPanel( CompileDialog compileDialog ) {
        super( compileDialog );
        setLayout( null );

        buttonGroup.add( includeManuscript );
        buttonGroup.add( includeDescription );
        buttonGroup.add( includeSummary );
        buttonGroup.add( includeNotes );

        add( includeManuscript );
        add( includeDescription );
        add( includeSummary );
        add( includeNotes );

        doLayout();

        fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
    }

    public void loadPreferences( CompileOptions compileOptions ) {
        NodeSection nodeSection = compileOptions.getNodeSection();
        includeManuscript.setSelected( nodeSection.equals( NodeSection.MANUSCRIPT ) );
        includeDescription.setSelected( nodeSection.equals( NodeSection.DESCRIPTION ) );
        includeSummary.setSelected( nodeSection.equals( NodeSection.SUMMARY ) );
        includeNotes.setSelected( nodeSection.equals( NodeSection.NOTES ) );
    }

    public void writePreferences( CompileOptions compileOptions ) {
        if( includeManuscript.isSelected() ) {
            compileOptions.setNodeSection( NodeSection.MANUSCRIPT );
        }
        else if( includeDescription.isSelected() ) {
            compileOptions.setNodeSection( NodeSection.DESCRIPTION );
        }

        else if( includeSummary.isSelected() ) {
            compileOptions.setNodeSection( NodeSection.SUMMARY );
        }
        else if( includeNotes.isSelected() ) {
            compileOptions.setNodeSection( NodeSection.NOTES );
        }
    }

    @Override
    public void doLayout() {
        int x = GuiUtils.PADDING;
        int y = GuiUtils.PADDING;

        includeManuscript.setLocation( x, y );
        y += radioSize.height + GuiUtils.PADDING;

        includeDescription.setLocation( x, y );
        y += radioSize.height + GuiUtils.PADDING;

        includeSummary.setLocation( x, y );
        y += radioSize.height + GuiUtils.PADDING;

        includeNotes.setLocation( x, y );
        y += radioSize.height + GuiUtils.PADDING;

        y += GuiUtils.PADDING;

        int width = 500;
        setPreferredSize( new Dimension( width, y ) );
    }

}
