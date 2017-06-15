package com.galvin.markdown.swing;

import com.galvin.markdown.swing.menu.MarkdownMenuBar;
import com.galvin.markdown.swing.dialogs.MacroEditorDialog;
import com.galvin.markdown.swing.dialogs.InsertImageDialog;
import com.galvin.markdown.swing.dialogs.FindAndReplaceDialog;
import com.galvin.markdown.swing.tree.MarkdownTreeNode;
import com.galvin.markdown.swing.tree.MarkdownTree;
import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.swing.compile.CompileDialog;
import com.galvin.markdown.swing.editor.MarkdownEditor;
import com.galvin.markdown.swing.editor.MarkdownEditorPanel;
import com.galvin.markdown.swing.split.MarkdownEditorSplitPane;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.lang3.StringUtils;

public class ProjectFrame
    implements TreeSelectionListener {

    private Controller controller;
    private Project project;
    private ProjectWindow window;
    private MarkdownMenuBar menuBar;
    private JSplitPane splitPane;
    private MarkdownTree tree;
    private JScrollPane treeScroll;
    private MarkdownEditorSplitPane editorSplitPane;
    private JFileChooser fileChooser;
    private FileFilter projectFileFilter = new ProjectXmlFileFilter();
    private JFileChooser imageChooser;
    private FileFilter imageFileFilter = new ImageFileFilter();
    private CompileDialog compileDialog;
    private FindAndReplaceDialog findAndReplaceDialog;
    private InsertImageDialog insertImageDialog;
    private MacroEditorDialog macroEditorDialog;

    public ProjectFrame( Project project ) {
        this.project = project;
        controller = new Controller( this );

        tree = new MarkdownTree( controller, project );
        tree.addTreeSelectionListener( this );
        treeScroll = new JScrollPane( tree );
        editorSplitPane = new MarkdownEditorSplitPane( controller );

        splitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, treeScroll, editorSplitPane );
        window = new ProjectWindow( this );
        splitPane.setBorder( null );
        treeScroll.setBorder( null );
        treeScroll.setHorizontalScrollBarPolicy( ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

        controller.nullSelection();
        controller.projectRenamed();

        menuBar = new MarkdownMenuBar( controller );
        window.setJMenuBar( menuBar );

        fileChooser = new JFileChooser( System.getProperty( "user.home" ) );
        fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
        fileChooser.setFileFilter( projectFileFilter );
        fileChooser.setMultiSelectionEnabled( false );

        imageChooser = new JFileChooser( System.getProperty( "user.home" ) );
        imageChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
        imageChooser.setFileFilter( imageFileFilter );
        imageChooser.setDialogType( JFileChooser.OPEN_DIALOG );
        imageChooser.setMultiSelectionEnabled( true );

        File projectFile = project.getProjectDirectory();
        if( projectFile != null ) {
            fileChooser.setCurrentDirectory( projectFile );
            imageChooser.setCurrentDirectory( projectFile );
        }

        compileDialog = new CompileDialog( controller );
        compileDialog.setCompileOptions( project.getCompileOptions() );

        findAndReplaceDialog = new FindAndReplaceDialog( controller );
        insertImageDialog = new InsertImageDialog( controller );
        macroEditorDialog = new MacroEditorDialog( controller );

        registerWithMarkdownServer();

        String selectedNode = project.getSelectedNode();
        if( !StringUtils.isBlank( selectedNode ) ) {
            tree.selectNode( selectedNode );
        }
    }

    public void registerWithMarkdownServer() {
        MarkdownServer.registerProjectFrame( this );
    }

    public void unregisterWithMarkdownServer() {
        MarkdownServer.unregisterProjectFrame( this );
    }

    public void unregisterWithMarkdownServerWithoutSaving() {
        MarkdownServer.unregisterWithMarkdownServerWithoutSaving( this );
    }

    public void unregisterIfHidden() {
        if( !getWindow().isVisible() ) {
            unregisterWithMarkdownServerWithoutSaving();
        }
    }

    public void requestFocus() {
        getWindow().requestFocus();
    }

    public void editNode( Node node ) {
        System.out.println("###");
        System.out.println("###");
        System.out.println("###");
        System.out.println("ProjectFrame: editNode()");
        if( node != null ) {
            System.out.println("    node: " + node.getTitle() );
            project.setSelectedNode( node.getUuid() );

            boolean metadata = false;
            boolean cover = false;
            boolean styleSheet = false;

            if( NodeTypes.CONFIG.equals( node.getNodeType() ) ) {
                System.out.println("    config; returning");
                controller.nullSelection();
                return;
            }
            else if( NodeTypes.METADATA.equals( node.getNodeType() ) ) {
                metadata = true;
            }
            else if( NodeTypes.COVER.equals( node.getNodeType() ) ) {
                cover = true;
            }
            else if( NodeTypes.STYLESHEET.equals( node.getNodeType() ) ) {
                styleSheet = true;
            }

            if( project.synchronizeEditors() ) {
                MarkdownEditorPanel currentPanel = getEditorPanel();

                List<MarkdownEditorPanel> editorPanels = getEditorSplitPane().getAllMarkdownEditorPanels();
                for( MarkdownEditorPanel editorPanel : editorPanels ) {
                    if( metadata ) {
                        editorPanel.editMetadata( project );
                    }
                    else if( cover ) {
                        editorPanel.editCover( project );
                    }
                    else if( styleSheet ) {
                        editorPanel.editStylesheet( project );
                    }
                    else {
                        editorPanel.edit( node );
                    }
                }

                if( currentPanel != null ) {
                    currentPanel.requestFocus();
                }
            }
            else {
                System.out.println("    not synchronized");
                MarkdownEditorPanel editorPanel = editorSplitPane.getCurrentComponent();
                if( editorPanel != null ) {
                    System.out.println("    got editor panel");
                    
                    if( metadata ) {
                        System.out.println("    metadata");
                        editorPanel.editMetadata( project );
                    }
                    else if( cover ) {
                        System.out.println("    cover");
                        editorPanel.editCover( project );
                    }
                    else if( styleSheet ) {
                        System.out.println("    style sheet");
                        editorPanel.editStylesheet( project );
                    }
                    else {
                        System.out.println("    editing node");
                        editorPanel.edit( node );
                    }

                    if( editorPanel.getEditor() != null ) {
                        System.out.println("    requesting focus");
                        editorPanel.getEditor().requestFocus();
                    }
                }
            }
        }
        else {
            controller.nullSelection();
        }
    }

    public void editProject( Project project ) {
        if( project != null ) {
            if( project.synchronizeEditors() ) {
                List<MarkdownEditorPanel> editorPanels = getEditorSplitPane().getAllMarkdownEditorPanels();
                for( MarkdownEditorPanel editorPanel : editorPanels ) {
                    editorPanel.edit( project );
                }
            }
            else {
                MarkdownEditorPanel editorPanel = editorSplitPane.getCurrentComponent();
                if( editorPanel != null ) {
                    editorPanel.edit( project );
                }
            }
        }
        else {
            controller.nullSelection();
        }
    }

    @Override
    public void valueChanged( TreeSelectionEvent e ) {
        if( tree.getSelectionCount() == 1 ) {
            Object value = tree.getLastSelectedPathComponent();
            if( value instanceof MarkdownTreeNode ) {
                MarkdownTreeNode treeNode = (MarkdownTreeNode)value;
                Node node = treeNode.getNode();
                Project project = treeNode.getProject();

                if( node != null ) {
                    editNode( node );
                }
                else if( project != null ) {
                    editProject( project );
                }
                else {
                    controller.nullSelection();
                }
            }
            else {
                controller.nullSelection();
            }
        }
    }

    public MarkdownEditorSplitPane getEditorSplitPane() {
        return editorSplitPane;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public MarkdownTree getTree() {
        return tree;
    }

    public JScrollPane getTreeScroll() {
        return treeScroll;
    }

    public ProjectWindow getWindow() {
        return window;
    }

    public Controller getController() {
        return controller;
    }

    public CompileDialog getCompileDialog() {
        return compileDialog;
    }

    public JFileChooser getSaveFileChooser() {
        fileChooser.setDialogType( JFileChooser.SAVE_DIALOG );
        return fileChooser;
    }

    public JFileChooser getOpenFileChooser() {
        fileChooser.setDialogType( JFileChooser.OPEN_DIALOG );
        return fileChooser;
    }

    public JFileChooser getImageFileChooser() {
        return imageChooser;
    }

    public MarkdownMenuBar getMenuBar() {
        return menuBar;
    }

    public MarkdownEditor getEditor() {
        return getEditorSplitPane().getCurrentComponent().getEditor();
    }

    public MarkdownEditorPanel getEditorPanel() {
        return getEditorSplitPane().getCurrentComponent();
    }

    public Node getCurrentNode() {
        return getEditorSplitPane().getCurrentComponent().getNode();
    }

    public Project getProject() {
        project = ProjectIo.toProject( tree );
        return project;
    }

    public FindAndReplaceDialog getFindAndReplaceDialog() {
        return findAndReplaceDialog;
    }

    public InsertImageDialog getInsertImageDialog() {
        return insertImageDialog;
    }

    public MacroEditorDialog getMacroEditorDialog() {
        return macroEditorDialog;
    }

}
