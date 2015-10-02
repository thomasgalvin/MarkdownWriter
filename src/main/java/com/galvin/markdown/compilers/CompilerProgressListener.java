/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.compilers;

import com.galvin.markdown.model.Project;

public interface CompilerProgressListener
{
    public void progress( Project project, int complete, int total, MarkdownCompiler source );
}
