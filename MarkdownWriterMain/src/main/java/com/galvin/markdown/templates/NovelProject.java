package com.galvin.markdown.templates;

import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import galvin.SystemUtils;
import java.io.File;
import java.io.InputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class NovelProject
    extends ProjectTemplate {

    private static final String NOVEL_ZIP_FILE = "/com/galvin/markdown/templates/Novel.mdp.xml";
    private static MarkdownMessages messages = MarkdownServer.getMessages();

    public NovelProject() {
        super( messages.novelTitle(), messages.novelDesc() );
    }

    public Project getProject() {
        try {
            InputStream stream = NovelProject.class.getResourceAsStream( NOVEL_ZIP_FILE );
            String source = IOUtils.toString( stream );

            File projectFile = SystemUtils.getTempFileWithExtension( ProjectIo.PROJECT_STRUCTURE_DOCUMENT_EXTENSION );
            FileUtils.writeStringToFile( projectFile, source );

            Project result = ProjectIo.readProject( projectFile );
            result.setProjectDirectory( null );
            result.resetUuid();
            return result;
        }
        catch( Throwable t ) {
            t.printStackTrace();
        }

        return null;
    }

}
