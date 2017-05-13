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
public class TcpSerializator 
{
    public String getRespondMessageBytes(ResponseHttpMessage responseMessage)
    {
        String headerBytes  = "";
        String bodyBytes    = "";
        
        for (int index = 0; index < responseMessage.getHeaders().size(); ++index)
        {
            headerBytes += responseMessage.getHeaders().get(index);
        }
        
        for (int index = 0; index < responseMessage.getBody().size(); ++index)
        {
            bodyBytes += responseMessage.getBody().get(index);
        }
        
        return responseMessage.getStatusLine() + headerBytes + bodyBytes;
    }
}
