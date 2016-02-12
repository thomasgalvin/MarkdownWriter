package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.compilers.Markup;
import com.galvin.markdown.compilers.NodeSeparators;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.editor.MarkdownEditor;
import galvin.StringUtils;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class SeparatorsCompileOptionsPanel extends AbstractCompileOptionsPanel
{
    private MarkdownMessages messages = MarkdownServer.getMessages();
    
    private JLabel separatorSameLevelLabel = new JLabel( messages.separatorSameLevel() );
    private SeparatorComboBox separatorSameLevel = new SeparatorComboBox();
    private MarkdownEditor separatorSameLevelCustom = new MarkdownEditor();
    private JScrollPane separatorSameLevelCustomScrollPane = new JScrollPane( separatorSameLevelCustom );
    
    private JLabel separatorHigherToLowerLabel = new JLabel( messages.separatorHigherToLower() );
    private SeparatorComboBox separatorHigherToLower = new SeparatorComboBox();
    private MarkdownEditor separatorHigherToLowerCustom = new MarkdownEditor();
    private JScrollPane separatorHigherToLowerCustomScrollPane = new JScrollPane( separatorHigherToLowerCustom );
    
    private JLabel separatorLowerToHigherLabel = new JLabel( messages.separatorLowerToHigher() );
    private SeparatorComboBox separatorLowerToHigher = new SeparatorComboBox();
    private MarkdownEditor separatorLowerToHigherCustom = new MarkdownEditor();
    private JScrollPane separatorLowerToHigherCustomScrollPane = new JScrollPane( separatorLowerToHigherCustom );
    
    private JLabel endOfDocumentMarkerLabel = new JLabel( messages.endOfDocumentMarker() );
    private SeparatorComboBox endOfDocumentMarker = new SeparatorComboBox();
    private MarkdownEditor endOfDocumentMarkerCustom = new MarkdownEditor();
    private JScrollPane endOfDocumentMarkerCustomScrollPane = new JScrollPane( endOfDocumentMarkerCustom );
    
    public SeparatorsCompileOptionsPanel( CompileDialog compileDialog ) {
        super( compileDialog );
        
        setLayout( new GridBagLayout() );
        
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = 0;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.FIRST_LINE_START;
        labelConstraints.weightx = 1.0;
        labelConstraints.weighty = 1.0;
        labelConstraints.gridwidth = 1;
        labelConstraints.gridheight = 1;
        
        GridBagConstraints comboConstraints = new GridBagConstraints();
        comboConstraints.gridx = 0;
        comboConstraints.gridy = 1;
        comboConstraints.fill = GridBagConstraints.HORIZONTAL;
        comboConstraints.anchor = GridBagConstraints.LINE_START;
        comboConstraints.weightx = 1.0;
        comboConstraints.weighty = 1.0;
        comboConstraints.gridwidth = 1;
        comboConstraints.gridheight = 1;
        
        GridBagConstraints textConstraints = new GridBagConstraints();
        textConstraints.gridx = 0;
        textConstraints.gridy = 2;
        textConstraints.fill = GridBagConstraints.HORIZONTAL;
        textConstraints.anchor = GridBagConstraints.LINE_START;
        textConstraints.weightx = 1.0;
        textConstraints.weighty = 1.0;
        textConstraints.gridwidth = 1;
        textConstraints.gridheight = GridBagConstraints.REMAINDER;
        
        GridBagConstraints spacerConstraints = new GridBagConstraints();
        spacerConstraints.gridx = 0;
        spacerConstraints.gridy = 3;
        spacerConstraints.fill = GridBagConstraints.HORIZONTAL;
        spacerConstraints.anchor = GridBagConstraints.LINE_START;
        spacerConstraints.weightx = 1.0;
        spacerConstraints.weighty = 1.0;
        spacerConstraints.gridwidth = 1;
        spacerConstraints.gridheight = 1;
        
        add( separatorSameLevelLabel, labelConstraints );
        add( separatorSameLevel, comboConstraints );
        add( separatorSameLevelCustomScrollPane, textConstraints );
        add( new JLabel( " " ), spacerConstraints );
        
//        labelConstraints.gridy++;
//        comboConstraints.gridy++;
//        textConstraints.gridy++;
//        spacerConstraints.gridy++;
//        labelConstraints.anchor = GridBagConstraints.LINE_START;
//        
//        add( separatorHigherToLowerLabel, labelConstraints );
//        add( separatorHigherToLower, comboConstraints );
//        add( separatorHigherToLowerCustomScrollPane );
//        add( new JLabel( " " ), spacerConstraints );
//        
//        labelConstraints.gridy++;
//        comboConstraints.gridy++;
//        textConstraints.gridy++;
//        spacerConstraints.gridy++;
//        
//        add( separatorLowerToHigherLabel, labelConstraints );
//        add( separatorLowerToHigher, comboConstraints );
//        add( separatorLowerToHigherCustomScrollPane, textConstraints );
//        add( new JLabel( " " ), spacerConstraints );
//        
//        labelConstraints.gridy++;
//        comboConstraints.gridy++;
//        textConstraints.gridy++;
//        spacerConstraints.gridy++;
//        
//        add( endOfDocumentMarkerLabel, labelConstraints );
//        add( endOfDocumentMarker, comboConstraints );
//        add( endOfDocumentMarkerCustomScrollPane, textConstraints );
//        add( new JLabel( " " ), spacerConstraints );
    }
    
    @Override
    public void updatePreferences(){
        super.updatePreferences();
    }
    
    public void loadPreferences( CompileOptions compileOptions ){
        NodeSeparators prefs = getNodeSeparators( compileOptions );
        set( separatorSameLevel, separatorSameLevelCustom, prefs.getSeparatorSameLevel(), prefs.getCustomSameLevel() );
        set( separatorHigherToLower, separatorHigherToLowerCustom, prefs.getSeparatorHigherToLower(), prefs.getCustomHigherToLower() );
        set( separatorLowerToHigher, separatorLowerToHigherCustom, prefs.getSeparatorLowerToHigher(), prefs.getCustomLowerToHigher() );
        set( endOfDocumentMarker, endOfDocumentMarkerCustom, prefs.getEndOfDocumentMarker(), prefs.getCustomEndOfDocument());
    }
    
    private void set( SeparatorComboBox comboBox, MarkdownEditor editor, String value, String custom ){
        editor.setText( StringUtils.neverNull( custom ) );
        
        for( SeparatorOption option : SeparatorOption.values() ){
            if( value.equals( option.markup ) ){
                comboBox.setSelectedItem( option );
                return;
            }
        }
        
        comboBox.setSelectedItem( SeparatorOption.CUSTOM );
    }
    
    public void writePreferences( CompileOptions compileOptions ){
        NodeSeparators prefs = getNodeSeparators( compileOptions );
        
        SeparatorOption option = (SeparatorOption)separatorSameLevel.getSelectedItem();
        prefs.setSeparatorSameLevel( getSeparator( option, separatorSameLevelCustom ) );
        
        option = (SeparatorOption)separatorHigherToLower.getSelectedItem();
        prefs.setSeparatorHigherToLower( getSeparator( option, separatorHigherToLowerCustom ) );
        
        option = (SeparatorOption)separatorLowerToHigher.getSelectedItem();
        prefs.setSeparatorLowerToHigher( getSeparator( option, separatorLowerToHigherCustom ) );
        
        option = (SeparatorOption)endOfDocumentMarker.getSelectedItem();
        prefs.setEndOfDocumentMarker( getSeparator( option, endOfDocumentMarkerCustom ) );
        
        prefs.setCustomSameLevel( separatorSameLevelCustom.getText() );
        prefs.setCustomHigherToLower( separatorHigherToLowerCustom.getText() );
        prefs.setCustomLowerToHigher( separatorLowerToHigherCustom.getText() );
        prefs.setCustomEndOfDocument( endOfDocumentMarkerCustom.getText() );
    }
    
    private NodeSeparators getNodeSeparators( CompileOptions compileOptions ){
        NodeSeparators prefs = compileOptions.getSeparators();
        if( prefs == null ){
            prefs = compileOptions.getSeparators();
            compileOptions.setSeparators( prefs );
        }
        return prefs;
    }
    
    private String getSeparator( SeparatorOption option, MarkdownEditor editor ){
        if( option == SeparatorOption.CUSTOM ){
            return editor.getText();
        }
        return option.markup;
    }
    
    private static class SeparatorComboBox extends JComboBox<SeparatorOption>{
        public SeparatorComboBox(){
            super( SeparatorOption.values() );
        }
    }
    
    private static enum SeparatorOption{
        LINE_BREAK( "Line Break", Markup.LINE_BREAK ),
        PARAGRAPH_BREAK( "Paragraph Break", Markup.PARAGRAPH_BREAK ),
        PAGE_BREAK( "Page Break", Markup.PAGE_BREAK ),
        STARS_HR( "***", Markup.SECTION_BREAK ),
        NORMAL_HR( "<hr/>", Markup.HR ),
        SMALL_HR( "<hr width='25%'/>", Markup.SMALL_CENTERED_HR ),
        NONE( "None", "" ),
        CUSTOM( "Custom:", null );
        
        private String title;
        private String markup;
        
        private SeparatorOption( String title, String markup ){
            this.title = title;
            this.markup = markup;
        }
        
        public String toString(){
            return title;
        }
    }
}
