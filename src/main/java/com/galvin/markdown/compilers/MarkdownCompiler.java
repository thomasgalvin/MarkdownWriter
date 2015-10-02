/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.compilers;

import com.galvin.markdown.model.Node;
import com.galvin.markdown.model.NodeSection;
import java.io.File;
import java.util.List;

public interface MarkdownCompiler
{
    public List<ExportFormat> getSupportedFormats();
    public List<CompileResults> compile( CompileOptions compileOptions ) throws Exception;
    public File getPreview( CompileOptions compileOptions, Node node, NodeSection nodeSection) throws Exception;
    public File getPreview( CompileOptions compileOptions, Node node, NodeSection nodeSection, boolean includeChildren ) throws Exception;
    
    public void addListener( CompilerProgressListener listenener );
    public void removeListener( CompilerProgressListener listenener );
}
