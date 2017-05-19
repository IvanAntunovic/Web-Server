/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.server;

/**
 *
 * @author A661517
 */
public class RequestHttpMessage extends HttpMessage
{
    String methodField;
    String urlField;
    String versionField;

    public RequestHttpMessage() 
    {
        super();
        this.methodField    = "";
        this.urlField       = "";
        this.versionField   = "";
    }
    
    public void clear()
    {
        this.methodField    = "";
        this.urlField       = "";
        this.versionField   = "";
        this.headers.clear();
        this.body.clear();
    }
        
    public boolean isGet()
    {
        return this.methodField.equals("GET");
    }

    public String getMethodField() 
    {
        return methodField;
    }

    public void setMethodField(String methodField)
    {
        this.methodField = methodField;
    }

    public String getUrlField() 
    {
        return urlField;
    }

    public void setUrlField(String urlField)
    {
        this.urlField = urlField;
    }

    public String getVersionField()
    {
        return versionField;
    }

    public void setVersionField(String versionField)
    {
        this.versionField = versionField;
    }
   
}
