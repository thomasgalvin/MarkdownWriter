package com.galvin.markdown.templates;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.model.Project;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.swing.MarkdownMessages;
import com.galvin.markdown.swing.MarkdownServer;
import java.util.GregorianCalendar;

public class EmptyProject
extends ProjectTemplate
{
    private MarkdownMessages messages = MarkdownServer.getMessages();
    
    public EmptyProject()
    {
        setTitle( messages.projectTemplateEmptyTitle()  );
        setDescription( messages.projectTemplateEmpty()  );
    }
    
    public Project getProject()
    {
        GregorianCalendar date = new GregorianCalendar();

        Project project = new Project();
        project.setCreatedDate( date );
        project.setModifiedDate( date );
        project.setTitle( messages.titleUntitledProject() );
        project.setProjectModelVersion( ProjectIo.PROJECT_MODEL_VERSION);

        Node root = new Node();
        root.setTitle( messages.titleUntitled() );
        root.setNodeType( NodeTypes.PROJECT );
        root.setCreatedDate( date );
        root.setModifiedDate( date );

        Node manuscript = new Node();
        manuscript.setTitle( messages.titleManuscript() );
        manuscript.setNodeType( NodeTypes.MANUSCRIPT );
        manuscript.setCreatedDate( date );
        manuscript.setModifiedDate( date );
        project.getChildNodes().add( manuscript );

        Node blankText = new Node();
        blankText.setTitle( messages.titleUntitledDocument() );
        blankText.setNodeType( NodeTypes.MARKDOWN );
        blankText.setCreatedDate( date );
        blankText.setModifiedDate( date );
        manuscript.getChildNodes().add( blankText );


        Node research = new Node();
        research.setTitle( messages.titleResearch() );
        research.setNodeType( NodeTypes.FOLDER );
        research.setCreatedDate( date );
        research.setModifiedDate( date );
        project.getChildNodes().add( research );

        Node resources = new Node();
        resources.setTitle( messages.titleResources() );
        resources.setNodeType( NodeTypes.RESOURCES );
        resources.setCreatedDate( date );
        resources.setModifiedDate( date );
        project.getChildNodes().add( resources );

        Node trash = new Node();
        trash.setTitle( messages.titleTrash() );
        trash.setNodeType( NodeTypes.TRASH );
        trash.setCreatedDate( date );
        trash.setModifiedDate( date );
        project.getChildNodes().add( trash );

        project.setManuscriptUuid( manuscript.getUuid() );
        project.setResourcesUuid( resources.getUuid() );
        project.setTrashUuid( trash.getUuid() );
        
        String css = messages.defaultCss();
        project.setStyleSheet( css );

        return project;
    }
}
