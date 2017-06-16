package com.galvin.markdown.templates;

import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import java.io.File;
import java.io.IOException;
import java.util.GregorianCalendar;

public class FileTemplate
    extends ProjectTemplate {

    private MarkdownMessages messages = MarkdownServer.getMessages();
    private File projectStructureDocument;
    private Project project;

    public FileTemplate( File projectStructureDocument ) throws IOException {
        this.projectStructureDocument = projectStructureDocument;
        project = ProjectIo.readProject( projectStructureDocument, true );

        setTitle( project.getTitle() );
        setDescription( messages.customeTemplateDesc() );
    }

    @Override
    public Project getProject() {
        GregorianCalendar date = new GregorianCalendar();
        project.setCreatedDate( date );
        project.setModifiedDate( date );

        return project;
    }

}
