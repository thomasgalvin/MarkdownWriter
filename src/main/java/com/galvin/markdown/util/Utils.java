/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.util;

import com.galvin.markdown.model.ImageResource;
import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import com.galvin.markdown.model.NodeTypes;
import com.galvin.markdown.model.ProjectIo;
import com.galvin.markdown.swing.editor.MarkdownDocument;
import galvin.StringUtils;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils
{
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss a" );
    public static final DecimalFormat NUMBER_FORMATTER = new DecimalFormat("#,###");
    
    public static MarkdownDocument getDocument( Node node, NodeSection nodeSection )
    {
        if( NodeSection.MANUSCRIPT.equals(  nodeSection ) )
        {
            return node.getManuscript();
        }
        else if( NodeSection.DESCRIPTION.equals(  nodeSection ) )
        {
            return node.getDescription();
        }
        else if( NodeSection.SUMMARY.equals(  nodeSection ) )
        {
            return node.getSummary();
        }
        else if( NodeSection.NOTES.equals(  nodeSection ) )
        {
            return node.getNotes();
        }

        return null;
    }
    
    public static String getMimeType( Node node )
    {
        if( NodeTypes.RESOURCE.equals( node.getNodeType() ) )
        {
            ImageResource resource = node.getImageResource();
            return resource.getMimeType();
        }
        
        return MimeTypes.MIME_TYPE_OTHER;
    }
    
    public static List<Node> flatten( List<Node> nodes, File projectDirectory ) throws IOException
    {
        return flatten( nodes, projectDirectory, 0 );
    }

    private static List<Node> flatten( List<Node> nodes, File projectDirectory, int level ) throws IOException
    {
        List<Node> result = new ArrayList();

        for( Node node : nodes )
        {
            Node clone = ProjectIo.clone( node, projectDirectory, true, true, false );
            clone.setLevel( level );
            result.add( clone );
            result.addAll( flatten( node.getChildNodes(), projectDirectory, level + 1 ) );
        }

        return result;
    }
    
    public static String getImageID( Node node )
    {
        return getImageName( node );
    }
    
    public static String getImageName( Node node )
    {
        ImageResource image = node.getImageResource();
        if( image != null )
        {
            String mimeType = image.getMimeType();
            String title = node.getTitle();
            
            if( MimeTypes.MIME_TYPE_JPEG.equals( mimeType ) )
            {
                return StringUtils.appendIfNecessary( title, ".jpg" );
            }
            else if( MimeTypes.MIME_TYPE_PNG.equals( mimeType ) )
            {
                return StringUtils.appendIfNecessary( title, ".png" );
            }
            else if( MimeTypes.MIME_TYPE_GIF.equals( mimeType ) )
            {
                return StringUtils.appendIfNecessary( title, ".gif" );
            }
        }
        
        return null;
    }
}
