/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.server;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author A661517
 */
public class TcpDeserializatior
{
    final int START_OF_MESSAGE_INDEX = 0;
    final String HOST_HEADER = "Host: ";
    final String CONNECTION_HEADER = "Connection: ";
    final String CACHE_CONTROL_HEADER = "Cache-Control: ";
    
    public RequestHttpMessage getRequestMessage(List<String> bytes)
    {
        RequestHttpMessage requestMessage = new RequestHttpMessage();
        StringBuilder statusLine = new StringBuilder (bytes.get(START_OF_MESSAGE_INDEX));
        
        // Set Status Line Method Field
        requestMessage.setMethodField(statusLine.substring(START_OF_MESSAGE_INDEX, statusLine.indexOf(" ")));
        statusLine.delete(START_OF_MESSAGE_INDEX, statusLine.indexOf(" ") + 1);
        // Set Status Line URL Field
        requestMessage.setUrlField(statusLine.substring(START_OF_MESSAGE_INDEX + 1, statusLine.indexOf(" ")));
        statusLine.delete(START_OF_MESSAGE_INDEX, statusLine.indexOf(" ") + 1);
        // Set Status Line Version Field
        requestMessage.setVersionField(statusLine.toString());
        bytes.subList(START_OF_MESSAGE_INDEX, START_OF_MESSAGE_INDEX + 1).clear();
        
        for (int index = 0; index < bytes.size(); ++index)
        {
            if (bytes.get(index).contains(CACHE_CONTROL_HEADER))
            {
                requestMessage.setHeaders(bytes.subList(START_OF_MESSAGE_INDEX, index + 1));
                bytes.subList(START_OF_MESSAGE_INDEX, index + 1).clear();
                break;
            }
        }
        requestMessage.setBody(bytes);
        
        return requestMessage;
    }
}
