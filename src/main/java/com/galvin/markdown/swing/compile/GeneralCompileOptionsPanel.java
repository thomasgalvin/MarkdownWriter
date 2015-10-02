/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.swing.GuiUtils;
import java.awt.Dimension;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;

public class GeneralCompileOptionsPanel
extends AbstractCompileOptionsPanel
{
    private MarkdownMessages messages = MarkdownServer.getMessages();
    private JRadioButton includeManuscript = new JRadioButton( messages.includeManuscript() );
    private JRadioButton includeDescription = new JRadioButton( messages.includeDescription() );
    private JRadioButton includeSummary = new JRadioButton( messages.includeSummary() );
    private JRadioButton includeNotes = new JRadioButton( messages.includeNotes() );
    private ButtonGroup buttonGroup = new ButtonGroup();
    private JCheckBox includeContributors = new JCheckBox( messages.includeContributors() );
    private JCheckBox includeContributorRoles = new JCheckBox( messages.includeContributorRoles() );
    private JCheckBox includeTitlesOfFolders = new JCheckBox( messages.includeTitlesOfFolders() );
    private JCheckBox includeSubtitlesOfFolders = new JCheckBox( messages.includeSubtitlesOfFolders() );
    private JCheckBox includeTitlesOfFiles = new JCheckBox( messages.includeTitlesOfFiles() );
    private JCheckBox includeSubtitlesOfFiles = new JCheckBox( messages.includeSubtitlesOfFiles() );
    private JFileChooser fileChooser = new JFileChooser();
    private Dimension radioSize = GuiUtils.sameSize( new JComponent[] { includeManuscript,
                                                                        includeDescription,
                                                                        includeSummary,
                                                                        includeNotes,
    } );
    private Dimension checkBoxSize = GuiUtils.sameSize( new JComponent[] { includeContributors,
                                                                           includeContributorRoles,
                                                                           includeTitlesOfFolders,
                                                                           includeSubtitlesOfFolders,
                                                                           includeTitlesOfFiles,
                                                                           includeSubtitlesOfFiles,
    } );
    
    public GeneralCompileOptionsPanel( CompileDialog compileDialog )
    {
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
        
        add( includeContributors );
        add( includeContributorRoles );
        add( includeTitlesOfFolders );
        add( includeSubtitlesOfFolders );
        add( includeTitlesOfFiles );
        add( includeSubtitlesOfFiles );
        doLayout();
        
        fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
    }
    
    public void loadPreferences( CompileOptions compileOptions )
    {
        NodeSection nodeSection = compileOptions.getNodeSection();
        includeManuscript.setSelected( nodeSection.equals( NodeSection.MANUSCRIPT ) );
        includeDescription.setSelected( nodeSection.equals( NodeSection.DESCRIPTION ) );
        includeSummary.setSelected( nodeSection.equals( NodeSection.SUMMARY ) );
        includeNotes.setSelected( nodeSection.equals( NodeSection.NOTES ) );
        
        includeContributors.setSelected( compileOptions.includeContributors() );
        includeContributorRoles.setSelected( compileOptions.includeContributorRoles() );
        includeTitlesOfFolders.setSelected( compileOptions.includeTitlesOfFolders() );
        includeSubtitlesOfFolders.setSelected( compileOptions.includeSubtitlesOfFolders() );
        includeTitlesOfFiles.setSelected( compileOptions.includeTitlesOfFiles() );
        includeSubtitlesOfFiles.setSelected( compileOptions.includeSubtitlesOfFiles() );
    }
    
    public void writePreferences( CompileOptions compileOptions )
    {
        if( includeManuscript.isSelected() )
        {
            compileOptions.setNodeSection( NodeSection.MANUSCRIPT );
        }
        else if( includeDescription.isSelected() )
        {
            compileOptions.setNodeSection( NodeSection.DESCRIPTION );
        }
        
        else if( includeSummary.isSelected() )
        {
            compileOptions.setNodeSection( NodeSection.SUMMARY );
        }
        else if( includeNotes.isSelected() )
        {
            compileOptions.setNodeSection( NodeSection.NOTES );
        }
        
        compileOptions.setIncludeContributors( includeContributors.isSelected() );
        compileOptions.setIncludeContributorRoles( includeContributorRoles.isSelected() );
        compileOptions.setIncludeTitlesOfFolders( includeTitlesOfFolders.isSelected() );
        compileOptions.setIncludeSubtitlesOfFolders( includeSubtitlesOfFolders.isSelected() );
        compileOptions.setIncludeTitlesOfFiles( includeTitlesOfFiles.isSelected() );
        compileOptions.setIncludeSubtitlesOfFiles( includeSubtitlesOfFiles.isSelected() );
    }
    
    @Override
    public void doLayout()
    {
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
        
        includeContributors.setLocation( x, y );
        y += checkBoxSize.height + GuiUtils.PADDING;
        
        includeContributorRoles.setLocation( x, y );
        y += checkBoxSize.height + GuiUtils.PADDING;
        
        includeTitlesOfFolders.setLocation( x, y );
        y += checkBoxSize.height + GuiUtils.PADDING;
        
        includeSubtitlesOfFolders.setLocation( x, y );
        y += checkBoxSize.height + GuiUtils.PADDING;
        
        includeTitlesOfFiles.setLocation( x, y );
        y += checkBoxSize.height + GuiUtils.PADDING;
        
        includeSubtitlesOfFiles.setLocation( x, y );
        y += checkBoxSize.height + GuiUtils.PADDING;
        
        int width = 500;
        setPreferredSize( new Dimension( width, y ) );
    }
}
