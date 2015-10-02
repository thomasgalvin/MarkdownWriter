/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.preferences;

import galvin.StringUtils;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MarkdownPreferences")
@XmlAccessorType(XmlAccessType.FIELD)
public class MarkdownPreferences
{

    private String pathToPandoc = "";
    private String pathToKindleGen = "";
    private String pathToPdfLatex = "";

    @Override
    public MarkdownPreferences clone()
    {
        MarkdownPreferences result = new MarkdownPreferences();

        result.setPathToPandoc( pathToPandoc );
        result.setPathToPdfLatex( pathToPdfLatex );

        return result;
    }

    public String getPathToPandoc()
    {
        return pathToPandoc;
    }

    public String getPathToKindleGen()
    {
        return pathToKindleGen;
    }

    public String getPathToPdfLatex()
    {
        return pathToPdfLatex;
    }

    public void setPathToPdfLatex( String pathToPdfLatex )
    {
        this.pathToPdfLatex = pathToPdfLatex;
    }

    public void setPathToKindleGen( String pathToKindleGen )
    {
        this.pathToKindleGen = pathToKindleGen;
    }

    public void setPathToPandoc( String pathToPandoc )
    {
        this.pathToPandoc = pathToPandoc;
    }

    public boolean pandocSupported()
    {
        return !StringUtils.empty( pathToPandoc );
    }
    
    public boolean pdfLatexSupported()
    {
        return !StringUtils.empty( pathToPdfLatex );
    }

    public boolean kindleGenSupported()
    {
        return !StringUtils.empty( pathToKindleGen );
    }
}
