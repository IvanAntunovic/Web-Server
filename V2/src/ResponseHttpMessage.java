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
public class ResponseHttpMessage extends HttpMessage
{
    String statusLine;

    public ResponseHttpMessage()
    {
        this.statusLine = "";
    }
    
    public void clear()
    {
        this.statusLine = "";
        this.headers.clear();
        this.body.clear();
    }

    public String getStatusLine()
    {
        return statusLine;
    }

    public void setStatusLine(String statusLine) 
    {
        this.statusLine = statusLine;
    }
    
}
