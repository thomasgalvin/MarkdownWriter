/**
Copyright &copy 2012 Thomas Galvin - All Rights Reserved.
 */
package com.galvin.markdown.model;

import javax.swing.ImageIcon;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement( name = "ImageResource" )
@XmlAccessorType( XmlAccessType.FIELD )
public class ImageResource
{
    private String mimeType;
    private byte[] bytes;
    private transient boolean needsSaving = false;
    private transient ImageIcon imageIcon;

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType( String mimeType )
    {
        this.mimeType = mimeType;
    }

    public boolean needsSaving()
    {
        return needsSaving;
    }

    public void setNeedsSaving( boolean needsSaving )
    {
        this.needsSaving = needsSaving;
    }

    public byte[] getBytes()
    {
        return bytes;
    }

    public void setBytes( byte[] bytes )
    {
        this.bytes = bytes;
    }

    public ImageIcon getImageIcon()
    {
        return imageIcon;
    }

    public void setImageIcon( ImageIcon imageIcon )
    {
        this.imageIcon = imageIcon;
    }
}
