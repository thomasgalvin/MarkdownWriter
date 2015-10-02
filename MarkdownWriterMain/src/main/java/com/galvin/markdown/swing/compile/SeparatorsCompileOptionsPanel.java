package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.compilers.Markup;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.StringUtils;
import galvin.swing.GuiUtils;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SeparatorsCompileOptionsPanel
        extends AbstractCompileOptionsPanel
{

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private final String[] OPTIONS = new String[]
    {
        messages.compileOptionsLineBreak(),
        messages.compileOptionsParagraphBreak(),
        messages.compileOptionsPageBreak(),
        messages.compileOptionsHR(),
        messages.compileOptionsCustom(),
    };
    private SeparatorPanel folderFolderWidget = new SeparatorPanel( messages.compileOptionsFolderFolder() );
    private SeparatorPanel folderFileWidget = new SeparatorPanel( messages.compileOptionsFolderFile() );
    private SeparatorPanel fileFolderWidget = new SeparatorPanel( messages.compileOptionsFileFolder() );
    private SeparatorPanel fileFileWidget = new SeparatorPanel( messages.compileOptionsFileFile() );
    private SeparatorPanel titleFolderWidget = new SeparatorPanel( messages.compileOptionsTitleFolder() );
    private SeparatorPanel titleFileWidget = new SeparatorPanel( messages.compileOptionsTitleFile() );
    private SeparatorPanel[] panels = new SeparatorPanel[]
    {
        folderFolderWidget,
        folderFileWidget,
        fileFolderWidget,
        fileFileWidget,
        titleFolderWidget,
        titleFileWidget,
    };

    public SeparatorsCompileOptionsPanel( CompileDialog compileDialog )
    {
        super( compileDialog );

        setLayout( null );
        add( folderFolderWidget );
        add( folderFileWidget );
        add( fileFolderWidget );
        add( fileFileWidget );
        add( titleFolderWidget );
        add( titleFileWidget );
    }

    public void loadPreferences( CompileOptions compileOptions )
    {
        folderFolderWidget.loadSeparator( compileOptions.getSeparatorFolderFolder(), compileOptions.getUnusedCustomSeparatorFolderFolder() );
        folderFileWidget.loadSeparator( compileOptions.getSeparatorFolderFile(), compileOptions.getUnusedCustomSeparatorFolderFile() );
        fileFolderWidget.loadSeparator( compileOptions.getSeparatorFileFolder(), compileOptions.getUnusedCustomSeparatorFileFolder() );
        fileFileWidget.loadSeparator( compileOptions.getSeparatorFileFile(), compileOptions.getUnusedCustomSeparatorFileFile() );
        titleFolderWidget.loadSeparator( compileOptions.getSeparatorTitleFolder(), compileOptions.getUnusedCustomSeparatorTitleFolder() );
        titleFileWidget.loadSeparator( compileOptions.getSeparatorTitleFile(), compileOptions.getUnusedCustomSeparatorTitleFile() );
    }
    
    public void writePreferences( CompileOptions compileOptions )
    {
        compileOptions.setSeparatorFolderFolder( folderFolderWidget.getSeparator() );
        compileOptions.setSeparatorFolderFile( folderFileWidget.getSeparator() );
        compileOptions.setSeparatorFileFolder( fileFolderWidget.getSeparator() );
        compileOptions.setSeparatorFileFile( fileFileWidget.getSeparator() );
        compileOptions.setSeparatorTitleFolder( titleFolderWidget.getSeparator() );
        compileOptions.setSeparatorTitleFile( titleFileWidget.getSeparator() );
        
        compileOptions.setUnusedCustomSeparatorFolderFolder( folderFolderWidget.getCustomSeparator() );
        compileOptions.setUnusedCustomSeparatorFolderFile( folderFileWidget.getCustomSeparator() );
        compileOptions.setUnusedCustomSeparatorFileFolder( fileFolderWidget.getCustomSeparator() );
        compileOptions.setUnusedCustomSeparatorFileFile( fileFileWidget.getCustomSeparator() );
        compileOptions.setUnusedCustomSeparatorTitleFolder( titleFolderWidget.getCustomSeparator() );
        compileOptions.setUnusedCustomSeparatorTitleFile( titleFileWidget.getCustomSeparator() );
    }
    
    @Override
    public void doLayout()
    {
        Dimension size = getSize();
        int width = size.width - GuiUtils.PADDING * 2;
        int height = size.height;
        height -= GuiUtils.PADDING * 7;
        height /= 6;
        
        int x = GuiUtils.PADDING;
        int y = GuiUtils.PADDING;
        
        Dimension panelSize = new Dimension( width, height );
        for( SeparatorPanel panel : panels )
        {
            panel.setSize( panelSize );
            panel.setLocation( x, y );
            y += panelSize.height + GuiUtils.PADDING;
        }
    }

    private class SeparatorPanel
            extends JPanel
    {

        private JLabel label;
        private JComboBox comboBox;
        private JTextArea textArea;
        private Dimension labelSize;
        private Dimension comboBoxSize;
        private Dimension textSize;

        public SeparatorPanel( String name )
        {
            label = new JLabel( name );
            labelSize = GuiUtils.preferredSize( label );

            comboBox = new JComboBox( OPTIONS );
            comboBoxSize = GuiUtils.preferredSize( comboBox );

            textArea = new JTextArea();
            textSize = GuiUtils.preferredSize( textArea );
            
            new CustomActionListener( comboBox, textArea );

            setLayout( null );
            add( label );
            add( comboBox );
            add( textArea );
        }
        
        public void loadSeparator( String separator, String unusedSeparator )
        {
            separator = StringUtils.neverNull( separator );
            unusedSeparator = StringUtils.neverNull( unusedSeparator );
            
            if( Markup.LINE_BREAK.equals( separator ) )
            {
                comboBox.setSelectedItem( messages.compileOptionsLineBreak() );
                textArea.setText( unusedSeparator );
            }
            else if( Markup.PARAGRAPH_BREAK.equals( separator ) )
            {
                comboBox.setSelectedItem( messages.compileOptionsParagraphBreak() );
                textArea.setText( unusedSeparator );
            }
            else if( Markup.PAGE_BREAK.equals( separator ) )
            {
                comboBox.setSelectedItem( messages.compileOptionsPageBreak() );
                textArea.setText( unusedSeparator );
            }
            else
            {
                comboBox.setSelectedItem( messages.compileOptionsCustom() );
                textArea.setText( separator );
            }
        }
        
        public String getSeparator()
        {
            Object selectedItem = comboBox.getSelectedItem();
            if( messages.compileOptionsLineBreak().equals( selectedItem ) )
            {
                return Markup.LINE_BREAK;
            }
            else if( messages.compileOptionsParagraphBreak().equals( selectedItem ) )
            {
                return Markup.PARAGRAPH_BREAK;
            }
            else if( messages.compileOptionsPageBreak().equals( selectedItem ) )
            {
                return Markup.PAGE_BREAK;
            }
            else if( messages.compileOptionsHR().equals( selectedItem ) )
            {
                return Markup.HR;
            }
            else
            {
                String text = textArea.getText().trim();
                StringBuilder result = new StringBuilder( Markup.LINE_BREAK.length() * 2 + text.length() );
                
                result.append( Markup.LINE_BREAK );
                result.append( text );
                result.append( Markup.LINE_BREAK );
                return result.toString();
            }
        }
        
        public String getCustomSeparator()
        {
            return textArea.getText();
        }

        @Override
        public void doLayout()
        {
            Dimension size = getSize();

            comboBoxSize.width = size.width;
            comboBox.setSize( comboBoxSize );

            textSize.width = size.width;
            textSize.height = size.height - labelSize.height - comboBoxSize.height - GuiUtils.PADDING * 2;
            textArea.setSize( textSize );

            int x = 0;
            int y = 0;

            label.setLocation( x, y );
            y += labelSize.height + GuiUtils.PADDING;

            comboBox.setLocation( x, y );
            y += comboBoxSize.height + GuiUtils.PADDING;

            textArea.setLocation( x, y );
        }
    }
    
    private class CustomActionListener
    implements ActionListener
    {
        private JComboBox comboBox;
        private JTextArea textArea;
        
        public CustomActionListener( JComboBox comboBox, JTextArea textArea )
        {
            comboBox.addActionListener( this );
            this.comboBox = comboBox;
            this.textArea = textArea;
        }

        public void actionPerformed( ActionEvent e )
        {
            Object selection = comboBox.getSelectedItem();
            boolean customEnabled = messages.compileOptionsCustom().equals( selection );
            
            textArea.setEditable( customEnabled );
            if( customEnabled )
            {
                textArea.requestFocus();
            }
        }
    }
}
