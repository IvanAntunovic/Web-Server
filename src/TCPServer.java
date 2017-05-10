
import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.scenario.effect.Color4f;
import java.io.*;
import java.net.*;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

class TCPServer 
{
    final String SERVER_NAME = "TestServer/1.0.0";
    Header       header;
    ServerSocket welcomeSocket;
    
    public TCPServer()
    {
        header = new Header();
    }
    
    public void connect(String socketNumber)
    {
        final int SOCKET_NUMBER = 9009;
        int socketNum;
        
        socketNum = Integer.parseInt(socketNumber);
        
        try 
        {
            welcomeSocket = new ServerSocket(SOCKET_NUMBER);
        }
        catch (IOException ex) 
        {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disconnect() 
    {
        try 
        {
            welcomeSocket.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run() throws Exception 
    {     
        String clientSentence           = "";
        String filePath                 = "";
        String statusLine               = "";
        final String CRLF               = "\r\n";
        
        while (true)
        {
            Socket           connectionSocket   = welcomeSocket.accept();
            BufferedReader   inFromClient       = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient        = new DataOutputStream(connectionSocket.getOutputStream());

            //clientSentence = inFromClient.readLine();
            
            do
            {
                clientSentence += inFromClient.readLine();
               
            }while (!clientSentence.contains("Accept-Language"));
            
            System.out.println("Received: " + clientSentence + "\n");
            
            Path path = null;
            if (clientSentence.contains("GET") && clientSentence.contains("HTTP/1.1")) 
            {
                final int START_OF_FILE_PATH_INDEX  = 4;
                final int END_OF_FILE_PATH_INDEX    = 1;
                int endIndex;
                int startIndex;
                
                startIndex = clientSentence.indexOf("GET")    + START_OF_FILE_PATH_INDEX;
                endIndex = clientSentence.indexOf("HTTP/1.1") - END_OF_FILE_PATH_INDEX;
                filePath = clientSentence.substring(startIndex + 1, endIndex);
                
                path = Paths.get(filePath);
                if (Files.exists(path))
                {
                    statusLine = "HTTP/1.1 200 OK" + CRLF;
                }
                else
                {
                    statusLine = "HTTP/1.1 404 Not Found" + CRLF;
                }
            }
            else if (!clientSentence.contains("HTTP/1.1"))
            {
                statusLine = "HTTP/1.1 505 HTTP Version Not Supported" + CRLF;
            }
            else
            {
                statusLine = "HTTP/1.1 400 Bad Request" + CRLF;
            }
            
            if (statusLine.equals("HTTP/1.1 200 OK" + CRLF)) 
            {   
                String serverToClientMessage    = "";
                String data                     = "";
                String headerMessage            = "";
                
                for (String line : Files.readAllLines(path))
                {
                    data += line;
                }
                
                headerMessage = this.header.addHTTPHeaders(headerMessage, SERVER_NAME, Integer.toString(data.length()), filePath) + CRLF; 
                serverToClientMessage = statusLine + headerMessage + data;
                outToClient.writeBytes(serverToClientMessage);
                outToClient.flush();
                
                System.out.println("\nData written to the client:\n\n" + serverToClientMessage);
            }
        }
    }
}
