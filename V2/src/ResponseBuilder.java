/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.server;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class ResponseBuilder 
{
    ResponseHttpMessage responseMessage;
    
    final String CONNECTION_HEADER     = "Connection: ";
    final String DATE_HEADER           = "Date: ";
    final String SERVER_HEADER         = "Server: ";
    final String LAST_MODIFIED_HEADER  = "Last-Modified: ";
    final String CONTENT_LENGTH_HEADER = "Content-Length: ";
    final String CONTENT_TYPE_HEADER   = "Content-Type: ";
    final String CRLF                  = "\r\n";
    
    public ResponseBuilder()
    {
        this.responseMessage = new ResponseHttpMessage();
    }
    
    public ResponseBuilder ok()
    {
        this.responseMessage.setStatusLine("HTTP/1.1 200 OK" + CRLF);
        return this;
    }
    
    public ResponseBuilder notFound()
    {
        this.responseMessage.setStatusLine("HTTP/1.1 404 Not Found" + CRLF);
        return this;
    }
    
    public ResponseBuilder versionNotSupported()
    {
        this.responseMessage.setStatusLine("HTTP/1.1 505 HTTP Version Not Supported" + CRLF);
        return this;
    }
    
    public ResponseBuilder badRequest()
    {
         this.responseMessage.setStatusLine("HTTP/1.1 400 Bad Request" + CRLF);
         return this;
    }
    
    public ResponseBuilder addHeaders( RequestHttpMessage requestMessage,
                                       final String SERVER_NAME)
                                     {
        ArrayList<String> responseHeaders = new ArrayList<>();
        
        responseHeaders.add(this.addConnectionTypeHeader(requestMessage));
        responseHeaders.add(this.addDateHeader());
        responseHeaders.add(this.addServerHeader(SERVER_NAME));
        responseHeaders.add(this.addLastModifiedHeader(requestMessage.getUrlField()));   
        responseHeaders.add(this.addContentLengthHeader());
        responseHeaders.add(this.addContentTypeHeader());
        this.responseMessage.setHeaders(responseHeaders);
        
        return this;
    }
    
    
    
    public ResponseBuilder addBody(List<String> fileData) throws IOException 
    {
        final String NO_FILE_FOUND = "No File Found";
        
        if (fileData.get(0).equals(NO_FILE_FOUND))
        {
            
           this.responseMessage.setBody( Arrays.asList(CRLF + "We are sorry, the page you requested cannot be found.\n"
                                                           + "The URL may be misspelled or the page you're looking for is no longer available.")
                                       );
        }
        else
        {
           final int START_OF_FILE = 0;
           
           String tempBody = CRLF + fileData.get(START_OF_FILE);
           fileData.clear();
           fileData.add(tempBody);
           this.responseMessage.setBody(fileData);
        }
        return this;
    }

    public ResponseHttpMessage build() 
    {   
        return this.responseMessage;
    }
    
    private String addConnectionTypeHeader(HttpMessage requestMessage)
    {
        String connectionHeader = null;
        
        for (int index= 0; index < requestMessage.getHeaders().size(); ++index)
        {
            if (requestMessage.getHeaders().get(index).contains(CONNECTION_HEADER))
            {
                connectionHeader = new String(requestMessage.getHeaders().get(index));
                break;
            }
        }
        
        if (connectionHeader.contains("close"))
        {
            return CONNECTION_HEADER + "close" + CRLF;
        }
        else if (connectionHeader.contains("keep-alive"))
        {
            return CONNECTION_HEADER + "keep-alive" + CRLF;
        }
        else
        {
            return null;
        }
       
    }   

    private String addDateHeader() 
    {   
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        return DATE_HEADER + dateFormat.format(calendar.getTime()) + CRLF;
    }

    private String addServerHeader( final String SERVER_NAME)
    {
        return SERVER_HEADER + SERVER_NAME + CRLF;
    }

    private String addLastModifiedHeader(final String filePath) 
    {
       Path path = Paths.get(filePath);
       if (Files.exists(path)) 
       {
            File file = new File(filePath);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
            return LAST_MODIFIED_HEADER + sdf.format(file.lastModified()) + CRLF;
       }
       return null;
    }

    private String addContentLengthHeader() 
    {
        return CONTENT_LENGTH_HEADER + this.responseMessage.bodyLength() + CRLF;
    }
    
    private String addContentTypeHeader()
    {
       return CONTENT_TYPE_HEADER + "text/plain" + CRLF;
    }

}