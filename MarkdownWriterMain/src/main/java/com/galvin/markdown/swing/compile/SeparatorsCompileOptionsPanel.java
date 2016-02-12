package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.compilers.Markup;
import com.galvin.markdown.compilers.NodeSeparators;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import com.galvin.markdown.swing.editor.MarkdownEditor;
import galvin.StringUtils;
import galvin.swing.GuiUtils;
import java.awt.Dimension;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
    
    private JComponent[] labels = new JComponent[]{
        separatorSameLevelLabel, separatorHigherToLowerLabel, separatorLowerToHigherLabel, endOfDocumentMarkerLabel
    };
    
    private JComponent[] combos = new JComponent[]{
        separatorSameLevel, separatorHigherToLower, separatorLowerToHigher, endOfDocumentMarker
    };
    
    private JComponent[] text = new JComponent[]{
        separatorSameLevelCustomScrollPane, separatorHigherToLowerCustomScrollPane, separatorLowerToHigherCustomScrollPane, endOfDocumentMarkerCustomScrollPane
    };
    
    private JComponent[] allItems = new JComponent[]{
        separatorSameLevelLabel, separatorSameLevel, separatorSameLevelCustomScrollPane,
        separatorHigherToLowerLabel, separatorHigherToLower, separatorHigherToLowerCustomScrollPane,
        separatorLowerToHigherLabel, separatorLowerToHigher, separatorLowerToHigherCustomScrollPane,
        endOfDocumentMarkerLabel, endOfDocumentMarker, endOfDocumentMarkerCustomScrollPane,
    };
    
    public SeparatorsCompileOptionsPanel( CompileDialog compileDialog ) {
        super( compileDialog );
        
        setLayout( null );
        
        add( separatorSameLevelLabel );
        add( separatorSameLevel );
        add( separatorSameLevelCustomScrollPane );
        
        add( separatorHigherToLowerLabel );
        add( separatorHigherToLower );
        add( separatorHigherToLowerCustomScrollPane );
        
        add( separatorLowerToHigherLabel );
        add( separatorLowerToHigher );
        add( separatorLowerToHigherCustomScrollPane );
        
        add( endOfDocumentMarkerLabel );
        add( endOfDocumentMarker );
        add( endOfDocumentMarkerCustomScrollPane );
    }
    
    @Override public void doLayout(){
        Dimension size = getSize();
        int x = GuiUtils.PADDING;
        int y = GuiUtils.PADDING;
        int width = size.width - GuiUtils.PADDING * 2;
        
        Dimension labelSize = GuiUtils.sameSize( labels );
        Dimension comboSize = GuiUtils.sameSize( combos );
        GuiUtils.sameWidth( allItems, width );
        labelSize.width = width;
        comboSize.width = width;
        
        int height = size.height;
        height -= labelSize.height * 4;
        height -= comboSize.height * 4;
        height -= GuiUtils.PADDING * 10;
        height /= 4;
        
        Dimension textSize = new Dimension( width, height );
        GuiUtils.setSize( textSize, text );
        
        for( int i = 0; i < 4; i++ ){
            JComponent label = labels[i];
            label.setLocation( x, y );
            y += labelSize.height + GuiUtils.PADDING;
            
            JComponent combo = combos[i];
            combo.setLocation( x, y );
            y += comboSize.height + GuiUtils.PADDING;
            
            JComponent editor = text[i];
            editor.setLocation( x, y );
            y += textSize.height + GuiUtils.PADDING;
        }
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
