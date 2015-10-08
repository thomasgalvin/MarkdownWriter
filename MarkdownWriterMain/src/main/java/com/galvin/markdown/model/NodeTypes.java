/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.model;

public final class NodeTypes
{
    private NodeTypes(){}
    
    public static final String PROJECT = "project/project";
    public static final String FOLDER = "file/folder";
    public static final String MARKDOWN = "file/markdown";
    public static final String RESOURCES = "project/resources";
    public static final String RESOURCE = "file/resource";
    public static final String TRASH = "project/trash";
    public static final String MANUSCRIPT = "project/manuscript";
    public static final String CONFIG = "project/config";
    public static final String METADATA = "project/metadata";
    public static final String STYLESHEET = "project/stylesheet";
    public static final String COVER = "project/cover";
    
    public static boolean countsTowardDepth( String nodeType ){
        return FOLDER.equals( nodeType ) ||
               MARKDOWN.equals( nodeType );
               
    }
}
