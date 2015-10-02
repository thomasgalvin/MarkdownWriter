package com.galvin.markdown.templates;

import com.galvin.markdown.model.Project;

public abstract class ProjectTemplate
{
    private String title;
    private String description;

    public ProjectTemplate()
    {
    }

    public ProjectTemplate( String title, String description )
    {
        this.title = title;
        this.description = description;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }
    
    public String toString()
    {
        return getTitle() + " - " + getDescription();
    }
    
    public abstract Project getProject();
}
