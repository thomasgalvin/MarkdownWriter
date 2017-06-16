package com.galvin.markdown.model;

import com.galvin.markdown.swing.MarkdownServer;
import java.io.File;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "RecentProject" )
@XmlAccessorType( XmlAccessType.FIELD )
public class RecentProject {
    private String title;
    private File projectFile;

    public RecentProject( File projectFile, String title ) {
        this.projectFile = projectFile;
        this.title = title;
    }

    public RecentProject() {
    }

    public File getProjectFile() {
        return projectFile;
    }

    public void setProjectFile( File projectFile ) {
        this.projectFile = projectFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    @Override
    public String toString() {
        if( projectFile == null ) {
            return MarkdownServer.getMessages().projectFileMissing();
        }

        return projectFile.getName() + "     " + projectFile.getParentFile().getAbsolutePath();
    }

    @Override
    public boolean equals( Object obj ) {
        if( obj == null ) {
            return false;
        }
        if( getClass() != obj.getClass() ) {
            return false;
        }
        final RecentProject other = (RecentProject)obj;
        if( this.projectFile != other.projectFile && ( this.projectFile == null || !this.projectFile.getAbsolutePath().equals( other.projectFile.getAbsolutePath() ) ) ) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + ( this.projectFile != null ? this.projectFile.hashCode() : 0 );
        return hash;
    }

}
