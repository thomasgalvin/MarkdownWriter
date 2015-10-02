package com.galvin.markdown.swing;

import com.apple.eawt.AppEvent;
import com.apple.eawt.OpenFilesHandler;
import com.galvin.markdown.compilers.MarkdownCompiler;
import com.galvin.markdown.compilers.PandocCompiler;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.model.RecentProject;
import com.galvin.markdown.preferences.MarkdownPreferences;
import com.galvin.markdown.preferences.Preferences;
import com.galvin.markdown.swing.dialogs.OpenProgressDialog;
import com.galvin.markdown.swing.preferences.PreferencesDialog;
import com.galvin.markdown.templates.EmptyProject;
import com.galvin.markdown.templates.FileTemplate;
import com.galvin.markdown.templates.NovelProject;
import com.galvin.markdown.templates.ProjectTemplate;
import galvin.SystemUtils;
import galvin.swing.mac.MacUtils;
import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MarkdownServer
{

    public static final String PROGRAM_NAME = "MarkdownWriter";
    public static final String PROGRAM_VERSION = "1.0";
    public static final String PREFERENCES_FILE = "preferences.xml";
    public static final String GROOVY_SCRIPTS_DIR = "Scripts";
    public static final String TEMPLATES_DIR = "Templates";
    public static final int RECENT_PROJECTS_SIZE = 25;
    private static Preferences preferences;
    private static MarkdownMessages messages;
    private static List<ProjectFrame> projectFrames = new ArrayList();
    private static JFileChooser fileChooser;
    private static JFileChooser imageChooser;
    private static PreferencesDialog preferencesDialog;
    private static WelcomeScreen welcomeScreen = new WelcomeScreen();
    private static MarkdownSocket markdownSocket;
    private static ProjectWindow currentWindow;

    public static Preferences getPreferences()
    {
        if( preferences == null )
        {
            File preferencesFile = getPreferencesFile();
            if( preferencesFile.exists() && preferencesFile.canRead() )
            {
                try
                {
                    preferences = ProjectIo.readPreferences( preferencesFile );
                }
                catch( Throwable t )
                {
                    t.printStackTrace();
                }
            }
        }

        if( preferences == null )
        {
            preferences = new Preferences();
            writePreferences();
        }

        return preferences;
    }

    public static ProjectFrame getProjectFrame( File projectStructureDocument )
    {
        for( ProjectFrame frame : projectFrames )
        {
            if( projectStructureDocument.equals( frame.getProject().getProjectFile()) )
            {
                return frame;
            }
        }

        return null;
    }

    public static void setCurrentWindow( ProjectWindow projectWindow )
    {
        currentWindow = projectWindow;
    }

    public static ProjectWindow getCurrentWindow()
    {
        return currentWindow;
    }

    public static void focusCurrentWindow()
    {
        if( currentWindow != null )
        {
            currentWindow.toFront();
            currentWindow.requestFocus();
        }
    }

    public static void registerProjectFrame( ProjectFrame projectFrame )
    {
        if( !projectFrames.contains( projectFrame ) )
        {
            projectFrames.add( projectFrame );
        }
    }

    public static void unregisterProjectFrame( ProjectFrame projectFrame )
    {
        if( !projectFrame.getController().fileNeedsSaving() )
        {
            if( projectFrames.contains( projectFrame ) )
            {
                projectFrames.remove( projectFrame );
                if( projectFrames.isEmpty() )
                {
                    noOpenProjects();
                }
            }
        }
        else
        {
            projectFrame.getWindow().setVisible( true );
        }
    }

    public static void unregisterWithMarkdownServerWithoutSaving( ProjectFrame projectFrame )
    {
        if( projectFrames.contains( projectFrame ) )
        {
            projectFrames.remove( projectFrame );
            if( projectFrames.isEmpty() )
            {
                noOpenProjects();
            }
        }
    }

    public static void checkForOpenProjects()
    {
        if( projectFrames.isEmpty() )
        {
            noOpenProjects();
        }
    }

    private static void noOpenProjects()
    {
        System.exit( 0 );
    }

    public static void writePreferences()
    {
        File preferencesFile = getPreferencesFile();
        preferencesFile.getParentFile().mkdirs();

        try
        {
            ProjectIo.write( preferences, preferencesFile );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }

    public static File getPreferencesDir()
    {
        return SystemUtils.getPreferencesDirectory( PROGRAM_NAME, PROGRAM_VERSION );
    }

    public static File getPreferencesFile()
    {
        File preferencesDir = getPreferencesDir();
        File preferencesFile = new File( preferencesDir, PREFERENCES_FILE );
        return preferencesFile;
    }

    public static File getGroovyScriptsDir()
    {
        File result = new File( getPreferencesDir(), GROOVY_SCRIPTS_DIR );
        result.mkdirs();
        return result;
    }

    public static File getTemplatesDir()
    {
        File result = new File( getPreferencesDir(), TEMPLATES_DIR );
        result.mkdirs();
        return result;
    }

    public static MarkdownMessages getMessages()
    {
        if( messages == null )
        {
            messages = new MarkdownMessages();
        }

        return messages;
    }

    public static JFileChooser getOpenFileChooser()
    {
        if( fileChooser == null )
        {
            fileChooser = new JFileChooser( System.getProperty( "user.home" ) );
            fileChooser.setFileFilter( new ProjectXmlFileFilter() );
            fileChooser.setMultiSelectionEnabled( false );
        }

        fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
        fileChooser.setDialogType( JFileChooser.OPEN_DIALOG );
        return fileChooser;
    }

    public static JFileChooser getSaveFileChooser()
    {
        if( fileChooser == null )
        {
            fileChooser = new JFileChooser( System.getProperty( "user.home" ) );
            fileChooser.setFileFilter( new ProjectXmlFileFilter() );
            fileChooser.setMultiSelectionEnabled( false );
        }

        fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        fileChooser.setDialogType( JFileChooser.SAVE_DIALOG );
        return fileChooser;
    }

    public static JFileChooser getImageFileChooser()
    {
        if( imageChooser == null )
        {
            imageChooser = new JFileChooser( System.getProperty( "user.home" ) );
            imageChooser.setFileFilter( new ImageFileFilter() );
            imageChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
            imageChooser.setDialogType( JFileChooser.OPEN_DIALOG );
            imageChooser.setMultiSelectionEnabled( true );
        }

        return imageChooser;
    }

    public static PreferencesDialog getPreferencesDialog()
    {
        if( preferencesDialog == null )
        {
            preferencesDialog = new PreferencesDialog();
        }

        return preferencesDialog;
    }

    public static MarkdownCompiler getCompiler()
    {
        return getCompiler( getPreferences().getMarkdownPreferences() );
    }

    public static MarkdownCompiler getCompiler( MarkdownPreferences markdownPreferences )
    {
        //return new BaseCompiler();
        return new PandocCompiler();
    }

    public static void welcomeNewProject()
    {
        welcomeScreen.setVisible( false );

        Project project = ProjectIo.createDefaultProject();
        ProjectFrame projectFrame = new ProjectFrame( project );
        projectFrame.getController().fileSaveAs();
    }

    public static void welcomeNewProject( ProjectTemplate template )
    {
        welcomeScreen.setVisible( false );

        Project project = template.getProject();
        project.setNeedsSaving( true );
        
        ProjectIo.setCreationDate( project );
        ProjectFrame projectFrame = new ProjectFrame( project );
        projectFrame.getController().fileSaveAs();
        projectFrame.getWindow().setVisible( true );
    }

    public static void welcomeOpenProject()
    {
        welcomeScreen.setVisible( false );

        JFileChooser fileChooser = getOpenFileChooser();
        int option = fileChooser.showDialog( null, messages.fileChooserOpenProjectButton() );
        if( option == JFileChooser.APPROVE_OPTION )
        {
            try
            {
                File projectStructureDocument = fileChooser.getSelectedFile();
                ProjectFrame existingFrame = MarkdownServer.getProjectFrame( projectStructureDocument );
                if( existingFrame != null )
                {
                    existingFrame.requestFocus();
                }
                else
                {
                    OpenProgressDialog dialog = new OpenProgressDialog();
                    dialog.setVisible( true );

                    Project project = ProjectIo.readProject( projectStructureDocument );
                    ProjectFrame newFrame = new ProjectFrame( project );

                    RecentProject recentProject = new RecentProject( projectStructureDocument, project.getTitle() );
                    addRecentProject( recentProject );

                    dialog.setVisible( false );
                    newFrame.getWindow().setVisible( true );
                }
            }
            catch( Throwable t )
            {
                t.printStackTrace();
                JOptionPane.showMessageDialog( null,
                                               messages.errorDuringOpen(),
                                               messages.errorDuringOpenTitle(),
                                               JOptionPane.ERROR_MESSAGE );
            }
        }

        checkForOpenProjects();
    }

    public static void fileOpen( File projectStructureDocument )
    {
        try
        {
            ProjectFrame existingFrame = MarkdownServer.getProjectFrame( projectStructureDocument );
            if( existingFrame != null )
            {
                existingFrame.getWindow().toFront();
                existingFrame.requestFocus();
            }
            else
            {
                OpenProgressDialog dialog = new OpenProgressDialog();
                dialog.setVisible( true );

                Project project = ProjectIo.readProject( projectStructureDocument );
                ProjectFrame newFrame = new ProjectFrame( project );

                RecentProject recentProject = new RecentProject( projectStructureDocument, project.getTitle() );
                MarkdownServer.addRecentProject( recentProject );

                dialog.setVisible( false );
                newFrame.getWindow().setVisible( true );
            }
        }
        catch( Throwable t )
        {
            t.printStackTrace();
            JOptionPane.showMessageDialog( null,
                                           messages.errorDuringOpen(),
                                           messages.errorDuringOpenTitle(),
                                           JOptionPane.ERROR_MESSAGE );
        }

        MarkdownServer.checkForOpenProjects();
    }

    public static void welcomeOpenRecentProject( File projectStructureDocument )
    {
        welcomeScreen.setVisible( false );

        try
        {
            OpenProgressDialog dialog = new OpenProgressDialog();
            dialog.setVisible( true );

            Project project = ProjectIo.readProject( projectStructureDocument );
            ProjectFrame newFrame = new ProjectFrame( project );

            RecentProject recentProject = new RecentProject( projectStructureDocument, project.getTitle() );
            addRecentProject( recentProject );

            dialog.setVisible( false );
            newFrame.getWindow().setVisible( true );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
            JOptionPane.showMessageDialog( null,
                                           messages.errorDuringOpen(),
                                           messages.errorDuringOpenTitle(),
                                           JOptionPane.ERROR_MESSAGE );
        }

        checkForOpenProjects();
    }

    public static void reloadRecentProjects()
    {
        for( ProjectFrame projectFrame : projectFrames )
        {
            projectFrame.getMenuBar().reloadRecentProjects();
        }
    }

    public static RecentProject[] getRecentProjects()
    {
        List<RecentProject> recentProjects = getPreferences().getRecentProjects();
        Iterator<RecentProject> iterator = recentProjects.iterator();
        while( iterator.hasNext() )
        {
            RecentProject project = iterator.next();
            if( !project.getProjectFile().exists() )
            {
                iterator.remove();
            }
        }

        RecentProject[] result = new RecentProject[ recentProjects.size() ];
        recentProjects.toArray( result );
        return result;
    }

    public static ProjectTemplate[] getProjectTemplates()
    {
        List<ProjectTemplate> result = new ArrayList();
        result.add( new EmptyProject() );
        result.add( new NovelProject() );

        File templatesDir = getTemplatesDir();
        for( File file : templatesDir.listFiles() )
        {
            if( file.getName().endsWith( ProjectIo.PROJECT_STRUCTURE_DOCUMENT_EXTENSION ) )
            {
                if( file.exists() && file.canRead() )
                {
                    try
                    {
                        FileTemplate template = new FileTemplate( file );
                        result.add( template );
                    }
                    catch( Throwable t )
                    {
                        t.printStackTrace();
                    }
                }
            }
        }

        ProjectTemplate[] array = new ProjectTemplate[ result.size() ];
        array = result.toArray( array );
        return array;
    }

    public static void addRecentProject( RecentProject recentProject )
    {
        List<RecentProject> recentProjects = getPreferences().getRecentProjects();
        recentProjects.remove( recentProject );
        recentProjects.add( 0, recentProject );

        if( recentProjects.size() > RECENT_PROJECTS_SIZE )
        {
            recentProjects = recentProjects.subList( 0, RECENT_PROJECTS_SIZE );
            getPreferences().setRecentProjects( recentProjects );
        }

        writePreferences();
        reloadRecentProjects();
    }

    public static void clearRecentProjects()
    {
        getPreferences().getRecentProjects().clear();
        writePreferences();
        reloadRecentProjects();
    }

    public static void alreadyRunning()
    {
        System.out.println( "Already running!" );
        System.exit( 0 );
    }

    public static void main( String[] args )
    {
        
        //show args
//        System.out.println( "args:" );
//        StringBuilder builder = new StringBuilder();
//        for( String arg : args )
//        {
//            builder.append( arg );
//            builder.append( "\n" );
//            System.out.println( "    " + arg );
//        }
//        JTextArea text = new JTextArea( builder.toString() );
//        ApplicationWindow textWindow = new ApplicationWindow( "Args" );
//        textWindow.getContentPane().add( new JScrollPane( text ) );
//        textWindow.setSize( 500, 500 );
//        textWindow.center();
//        textWindow.setVisible( true );
//        textWindow.setExitOnClose( false );
        
        if( MarkdownSocket.alreadyRunning( args ) )
        {
            alreadyRunning();
        }
        else
        {
            markdownSocket = new MarkdownSocket();
            markdownSocket.acceptConnections();
        }

        if( SystemUtils.IS_MAC )
        {
            try
            {
                MacUtils.registerApplicationListener( "com.galvin.markdown.swing.MarkdownApplicationAdapter" );
                System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Markdown Writer");
                
                System.out.println( "Setting Mac's Open File Handler..." );
                
                OpenFilesHandler handler = new OpenFilesHandler()
                {
                    public void openFiles( AppEvent.OpenFilesEvent ofe )
                    {
                        List<File> files = ofe.getFiles();
                        System.out.println( "OpenFilesHandler: Opening " + files.size() + " double-clicked files" );
                        for( File file : files )
                        {
                            System.out.println( "    opening file: " + file );
                            fileOpen( file );
                        }
                    }
                };
                com.apple.eawt.Application macApp = com.apple.eawt.Application.getApplication();
                macApp.setOpenFileHandler( handler );
                
//                MRJOpenDocumentHandler mrjHandler = new MRJOpenDocumentHandler()
//                {
//                    public void handleOpenFile( File file )
//                    {
//                        System.out.println( "MRJOpenDocumentHandler: opening file: " + file );
//                        fileOpen( file );
//                    }
//                };
//                com.apple.mrj.MRJApplicationUtils.registerOpenDocumentHandler( mrjHandler );
                
            }
            catch( Throwable t )
            {
                t.printStackTrace();
            }
        }

        try
        {
            welcomeScreen.setVisible( true );
        }
        catch( Throwable t )
        {
            t.printStackTrace();
        }
    }
    
    public static void exit()
    {
        List<ProjectFrame> tmp = new ArrayList();
        tmp.addAll( projectFrames );
        
        for( ProjectFrame projectFrame : tmp )
        {
            projectFrame.getController().fileClose();
        }

        checkForOpenProjects();
    }

    public static void helpAbout()
    {
        try
        {
            Desktop.getDesktop().browse( HelpFiles.getAbout().toURI() );
        }
        catch( Throwable t )
        {
            JOptionPane.showMessageDialog( null,
                                           messages.errorLoadingHelpFile(),
                                           messages.errorLoadingHelpFileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }

    public static void helpShowHelp()
    {
        try
        {
            Desktop.getDesktop().browse( HelpFiles.getApplicationHelp().toURI() );
        }
        catch( Throwable t )
        {
            JOptionPane.showMessageDialog( null,
                                           messages.errorLoadingHelpFile(),
                                           messages.errorLoadingHelpFileTitle(),
                                           JOptionPane.ERROR_MESSAGE );
            t.printStackTrace();
        }
    }
    
    public static void startSpellCheck()
    {
        for( ProjectFrame projectFrame : projectFrames )
        {
            Preferences preferences = getPreferences();
            projectFrame.getProject().startSpellCheck();
            projectFrame.getController().configureMenusAndEditors( preferences );
        }
    }
    
    public static void stopSpellCheck()
    {
        for( ProjectFrame projectFrame : projectFrames )
        {
            Preferences preferences = getPreferences();
            projectFrame.getProject().stopSpellCheck();
            projectFrame.getController().configureMenusAndEditors( preferences );
        }
    }
}