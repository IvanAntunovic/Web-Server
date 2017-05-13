/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.server;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author A661517
 */
public class HttpMessage 
{
    List<String> headers;
    List<String> body;

    public HttpMessage() 
    {
        this.headers        = new ArrayList<>();
        this.body           = new ArrayList<>();
    }

    public List<String> getHeaders()
    {
        return headers;
    }

    public void setHeaders(List<String> headers) 
    {
        this.headers = new ArrayList<>(headers);
    }

    public List<String> getBody() 
    {
        return body;
    }

    public void setBody(List<String> body) 
    {
        this.body = new ArrayList<>(body);
    }
    
    public int bodyLength()
    {
        int bodyLength = 0;
        for (int index = 0; index < this.body.size(); ++index)
        {
            bodyLength += this.body.get(index).length();
        }
        
        return bodyLength;
    }

}
    
   