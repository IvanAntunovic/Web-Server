
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author A661517
 */
public class Header 
{
    final String CONNECTION_HEADER = "Connection: ";
    final String DATE_HEADER = "Date: ";
    final String SERVER_HEADER = "Server: ";
    final String LAST_MODIFIED_HEADER = "Last-Modified: ";
    final String CONTENT_LENGTH_HEADER = "Content-Length: ";
    final String CONTENT_TYPE_HEADER = "Content-Type: ";
    final String CRLF = "\r\n";
    
    public String addHTTPHeaders( String clientSentence, 
                                final String SERVER_NAME, 
                                final String dataContentLength,
                                final String filePath)
    {
        clientSentence = this.setConnectionType(clientSentence);
        clientSentence = this.setDateHeader(clientSentence);
        clientSentence = this.setServerHeader(clientSentence, SERVER_NAME);
        clientSentence = this.setLastModifiedHeader(clientSentence, filePath);
        clientSentence = this.setContentLengthHeader(clientSentence, dataContentLength);
        clientSentence = this.setContentTypeHeader(clientSentence);

        return clientSentence;
    }
    
    private String setConnectionType(String clientSentence)
    {
        clientSentence += CONNECTION_HEADER + "keep-alive" + CRLF;
        return clientSentence;
    }   

    private String setDateHeader(String clientSentence) 
    {
      
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        
        clientSentence += DATE_HEADER + dateFormat.format(calendar.getTime()) + CRLF;
        return clientSentence;
    
    }

    private String setServerHeader(String clientSentence, final String SERVER_NAME)
    {
        clientSentence += SERVER_HEADER + SERVER_NAME + CRLF;
        return clientSentence;
    }

    private String setLastModifiedHeader(String clientSentence, final String filePath) 
    {
       Path path = Paths.get(filePath);
       if (Files.exists(path)) 
       {
            File file = new File(filePath);
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.UK);
            clientSentence += LAST_MODIFIED_HEADER + sdf.format(file.lastModified()) + CRLF;
            return clientSentence;
       }
       return null;
    }

    private String setContentLengthHeader(String clientSentence, final String dataContentLength) 
    {
        clientSentence += CONTENT_LENGTH_HEADER + dataContentLength + CRLF;
        return clientSentence;
    }
    
    private String setContentTypeHeader(String clientSentence)
    {
       clientSentence += CONTENT_TYPE_HEADER + "text/plain" + CRLF;
       return clientSentence;
    }
    
}
