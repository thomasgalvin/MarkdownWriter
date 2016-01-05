package com.galvin.markdown.swing.menu;

import com.galvin.markdown.swing.Controller;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.StringUtils;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class GroovyMenu
        extends JMenu
{

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private Listener listener = new Listener();
    private Controller controller;
    private JMenuItem reloadScriptsItem = createMenuItem( messages.menuBarGroovyReload(), listener );
    private JMenuItem openScriptsDirItem = createMenuItem( messages.menuBarGroovyOpenDir(), listener );
    private MenuComparator menuComparator = new MenuComparator();

    public GroovyMenu( Controller controller )
    {
        setText( messages.menuBarGroovy() );
        this.controller = controller;

        reloadGroovyScripts();
    }

    private static JMenuItem createMenuItem( String label, ActionListener listener )
    {
        JMenuItem item = new JMenuItem( label );
        item.addActionListener( listener );
        return item;
    }

    public void reloadGroovyScripts()
    {
        removeAll();
        File scriptsDir = MarkdownServer.getGroovyScriptsDir();
        loadScripts( this, scriptsDir );

        addSeparator();
        add( openScriptsDirItem );
        addSeparator();
        add( reloadScriptsItem );
    }
    
    private void loadScripts( JMenu menu, File scriptsDir )
    {
        int count = 0;
        File[] scripts = scriptsDir.listFiles();
        if( scripts != null )
        {
            List<JMenu> dirs = new ArrayList();
            List<ScriptMenuItem> files = new ArrayList();
            
            for( File script : scripts )
            {
                if( script.isDirectory() )
                {
                    JMenu dirMenu = loadGroovyScripts( script );
                    dirs.add( dirMenu );
                    count++;
                }
                else if( script.getName().endsWith( ".groovy" ) )
                {
                    ScriptMenuItem item = new ScriptMenuItem( script );
                    files.add( item );
                    count++;
                }
            }
            
            Collections.sort( dirs, menuComparator );
            Collections.sort( files, menuComparator );
            
            for( JMenu item : dirs ){
                menu.add( item );
            }
            
            if( !dirs.isEmpty() && !files.isEmpty() ) {
                menu.addSeparator();
            }
            
            for( ScriptMenuItem item : files ){
                menu.add( item );
            }
        }

        if( count == 0 )
        {
            menu.add( new NoScriptsMenuItem() );
        }
    }
    
    private JMenu loadGroovyScripts( File scriptsDir )
    {
        JMenu result = new JMenu( getDisplayName( scriptsDir ) );
        loadScripts( result, scriptsDir );
        return result;
    }

    public static String getDisplayName( File file )
    {
        StringBuilder result = new StringBuilder( file.getName() );
        StringUtils.replaceAll( result, ".groovy", "" );
        StringUtils.replaceAll( result, "_", " " );
        return result.toString();
    }

    private class Listener
            implements ActionListener
    {

        public void actionPerformed( ActionEvent e )
        {
            Object source = e.getSource();
            if( source == reloadScriptsItem )
            {
                reloadGroovyScripts();
            }
            else if( source == openScriptsDirItem )
            {
                controller.openGroovyScriptsDir();
            }
        }
    }

    private class ScriptMenuItem
            extends JMenuItem
            implements ActionListener
    {

        private File scriptFile;

        public ScriptMenuItem( File scriptFile )
        {
            super( getDisplayName( scriptFile ) );
            this.scriptFile = scriptFile;
            addActionListener( this );
        }

        public void actionPerformed( ActionEvent e )
        {
            controller.executeGroovyScript( scriptFile );
        }
    }
    
    private class NoScriptsMenuItem
            extends JMenuItem
    {
        public NoScriptsMenuItem()
        {
            super( messages.menuBarGroovyNoScripts() );
            setEnabled( false );
        }
    }
    
    private class MenuComparator implements Comparator {
        @Override
        public int compare( Object o1, Object o2 ) {
            JMenuItem menu1 = (JMenuItem)o1;
            JMenuItem menu2 = (JMenuItem)o2;
            
            return menu1.getText().compareTo( menu2.getText() );
        }
        
    }
}
