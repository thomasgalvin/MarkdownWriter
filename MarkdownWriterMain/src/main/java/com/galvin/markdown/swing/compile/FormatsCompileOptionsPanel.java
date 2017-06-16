/**
 * Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.swing.compile;

import com.galvin.markdown.compilers.CompileOptions;
import com.galvin.markdown.compilers.ExportFormat;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.StringUtils;
import galvin.swing.GuiUtils;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FormatsCompileOptionsPanel
    extends AbstractCompileOptionsPanel {

    private MarkdownMessages messages = MarkdownServer.getMessages();

    private JLabel importFormatLabel = new JLabel( messages.labelInputFormat() );
    private ImportFormatComboBox importFormatComboBox = new ImportFormatComboBox();

    private JLabel outputDirectoryLabel = new JLabel( messages.labelOutputDir() );
    private JTextField outputDirectoryField = new JTextField();
    private JButton chooseOutputDirectoryButton = new JButton( messages.ellipsis() );

    private List<ExportFormatCheckBox> checkboxes = new ArrayList();
    private JFileChooser fileChooser = new JFileChooser();

    private Dimension comboSize = GuiUtils.preferredSize( importFormatComboBox );

    private Dimension labelSize = GuiUtils.sameSize( new JComponent[]{
        importFormatLabel, outputDirectoryLabel
    } );

    private Dimension fieldSize = GuiUtils.sameSize( new JComponent[]{
        outputDirectoryField
    } );

    private Dimension buttonSize = GuiUtils.preferredSize( chooseOutputDirectoryButton );
    private Dimension checkBoxSize;

    public FormatsCompileOptionsPanel( CompileDialog compileDialog ) {
        super( compileDialog );
        setLayout( null );

        add( importFormatLabel );
        add( importFormatComboBox );

        add( outputDirectoryLabel );
        add( outputDirectoryField );
        add( chooseOutputDirectoryButton );

        List<JComponent> components = new ArrayList();
        for( ExportFormat format : ExportFormat.FORMATS ) {
            ExportFormatCheckBox checkBox = new ExportFormatCheckBox( format );
            checkboxes.add( checkBox );
            add( checkBox );
            components.add( checkBox );
        }
        checkBoxSize = GuiUtils.sameSize( components );

        fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );

        doLayout();
        new Listener();
    }

    @Override
    public void doLayout() {
        Dimension size = getSize();
        int x = GuiUtils.PADDING;
        int y = GuiUtils.PADDING;

        importFormatLabel.setLocation( x, y );
        y += labelSize.height + GuiUtils.PADDING;

        comboSize.width = size.width - GuiUtils.PADDING * 2;
        importFormatComboBox.setSize( comboSize );
        importFormatComboBox.setLocation( x, y );
        y += comboSize.height + GuiUtils.PADDING;

        outputDirectoryLabel.setLocation( x, y );
        y += labelSize.height + GuiUtils.PADDING;

        fieldSize.width = size.width - GuiUtils.PADDING * 2;
        fieldSize.width -= buttonSize.width;
        outputDirectoryField.setSize( fieldSize );

        int buttonX = size.width - GuiUtils.PADDING;
        buttonX -= buttonSize.width;

        outputDirectoryField.setLocation( x, y );
        chooseOutputDirectoryButton.setLocation( buttonX, y );
        y += Math.max( fieldSize.height, buttonSize.height );

        for( ExportFormatCheckBox checkBox : checkboxes ) {
            checkBox.setLocation( x, y );
            y += checkBoxSize.height + GuiUtils.PADDING;
        }

        int width = labelSize.width + buttonSize.width;
        width = Math.max( width, 500 );

        setPreferredSize( new Dimension( width, y ) );
    }

    @Override
    public void updatePreferences() {
        super.updatePreferences();
        List<ExportFormat> supportedFormats = MarkdownServer.getCompiler().getSupportedFormats();
        for( ExportFormatCheckBox checkBox : checkboxes ) {
            checkBox.setEnabled( supportedFormats );
        }
    }

    public void clearAllFormats() {
        for( ExportFormatCheckBox checkBox : checkboxes ) {
            checkBox.setSelected( false );
        }
    }

    public void loadPreferences( CompileOptions compileOptions ) {
        importFormatComboBox.setSelectedFormat( compileOptions.getImportFormat() );

        File outputDirectory = compileOptions.getOutputDirectory();
        if( outputDirectory != null ) {
            outputDirectoryField.setText( outputDirectory.getAbsolutePath() );
            fileChooser.setSelectedFile( outputDirectory );
        }
        else {
            outputDirectoryField.setText( System.getProperty( "user.home" ) );
            fileChooser.setSelectedFile( new File( System.getProperty( "user.home" ) ) );
        }

        clearAllFormats();
        for( ExportFormatCheckBox checkBox : checkboxes ) {
            checkBox.setSelected( compileOptions.getExportFormats() );
        }
    }

    public void writePreferences( CompileOptions compileOptions ) {
        compileOptions.setImportFormat( importFormatComboBox.getSelectedFormat() );

        String outputDirectory = outputDirectoryField.getText();
        if( StringUtils.empty( outputDirectory ) ) {
            compileOptions.setOutputDirectory( new File( System.getProperty( "user.home" ) ) );
        }
        else {
            compileOptions.setOutputDirectory( new File( outputDirectory ) );
        }

        compileOptions.getExportFormats().clear();
        for( ExportFormatCheckBox checkBox : checkboxes ) {
            if( checkBox.isSelected() ) {
                compileOptions.getExportFormats().add( checkBox.getExportFormat() );
            }
        }
    }

    public void chooseOutputDirectory() {
        int option = fileChooser.showSaveDialog( this );
        if( option == JFileChooser.APPROVE_OPTION ) {
            outputDirectoryField.setText( fileChooser.getSelectedFile().getAbsolutePath() );
        }
    }

    private class Listener
        implements ActionListener {

        public Listener() {
            chooseOutputDirectoryButton.addActionListener( this );
        }

        public void actionPerformed( ActionEvent e ) {
            Object source = e.getSource();
            if( source == chooseOutputDirectoryButton ) {
                chooseOutputDirectory();
            }
        }

    }

}
