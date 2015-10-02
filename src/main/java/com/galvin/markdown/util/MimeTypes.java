/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.util;

import java.io.File;
import java.util.HashMap;

public class MimeTypes
{

    public static final String MIME_TYPE_XHTML = "application/xhtml+xml";
    public static final String MIME_TYPE_JPEG = "image/jpeg";
    public static final String MIME_TYPE_PNG = "image/png";
    public static final String MIME_TYPE_GIF = "image/gif";
    public static final String MIME_TYPE_OTHER = "application/octet-stream";
    private static final HashMap<String, String> extensionHash = new HashMap();

    static
    {
        extensionHash.put( ".jpg", MIME_TYPE_JPEG );
        extensionHash.put( ".jpeg", MIME_TYPE_JPEG );
        extensionHash.put( ".png", MIME_TYPE_PNG );
        extensionHash.put( ".gif", MIME_TYPE_GIF );
    }

    public static String getMimeType( File file )
    {
        String fileName = file.getName();
        int pos = fileName.lastIndexOf( '.' );
        if( pos != -1 )
        {
            String extension = fileName.substring( pos );
            String result = extensionHash.get( extension );
            if( result != null )
            {
                return result;
            }
        }
        
        return MIME_TYPE_OTHER;
    }
    
    public static boolean isImage( String mimeType )
    {
        return MIME_TYPE_JPEG.equals( mimeType )
            || MIME_TYPE_PNG.equals( mimeType )
            || MIME_TYPE_GIF.equals( mimeType );
    }
    
    public static boolean isImage( File file )
    {
        String mimeType = getMimeType( file );
        return isImage( mimeType );
    }
}
