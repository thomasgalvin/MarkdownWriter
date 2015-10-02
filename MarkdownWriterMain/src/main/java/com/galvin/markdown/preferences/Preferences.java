package com.galvin.markdown.preferences;

import com.galvin.markdown.model.RecentProject;
import galvin.swing.text.macros.MacroList;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "Preferences" )
@XmlAccessorType( XmlAccessType.FIELD )
public class Preferences
{
    private GeneralPreferences generalPreferences = new GeneralPreferences();
    private EditorPreferences editorPreferences = new EditorPreferences();
    private MarkdownPreferences markdownPreferences = new MarkdownPreferences();
    private List<RecentProject> recentProjects = new ArrayList();
    private MacroList globalMacros = new MacroList();

    public EditorPreferences getEditorPreferences()
    {
        return editorPreferences;
    }

    public void setEditorPreferences( EditorPreferences editorPreferences )
    {
        this.editorPreferences = editorPreferences;
    }

    public GeneralPreferences getGeneralPreferences()
    {
        return generalPreferences;
    }

    public void setGeneralPreferences( GeneralPreferences generalPreferences )
    {
        this.generalPreferences = generalPreferences;
    }

    public MarkdownPreferences getMarkdownPreferences()
    {
        return markdownPreferences;
    }

    public void setMarkdownPreferences( MarkdownPreferences markdownPreferences )
    {
        this.markdownPreferences = markdownPreferences;
    }

    public List<RecentProject> getRecentProjects()
    {
        return recentProjects;
    }

    public void setRecentProjects( List<RecentProject> recentProjects )
    {
        this.recentProjects = recentProjects;
    }

    public MacroList getGlobalMacros()
    {
        return globalMacros;
    }

    public void setGlobalMacros( MacroList globalMacros )
    {
        this.globalMacros = globalMacros;
    }
}
