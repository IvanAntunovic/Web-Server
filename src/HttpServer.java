/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fairenough
 */
public class HttpServer 
{
    final String SERVER_NAME = "TestServer/1.0.0";
    final String CRLF        = "\r\n";
    TCPConnection tcpConnection;
    Header       header;
    ServerSocket welcomeSocket;
    
    public HttpServer(String socketNumber) throws IOException
    {
        header = new Header();
        
        try 
        {
            welcomeSocket = new ServerSocket(Integer.parseInt(socketNumber));
        }
        catch (IOException ex) 
        {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.tcpConnection = new TCPConnection(welcomeSocket);
    }
    
    public void connect()
    {
        try
        {
            this.tcpConnection.open();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void disconnect() 
    {
        try 
        {
            this.welcomeSocket.close();
        } 
        catch (IOException ex)
        {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try 
        {
            this.tcpConnection.close();
        }
        catch (IOException ex) 
        {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void run() throws Exception 
    {     
        String filePath                             = "";
        String statusLine                           = "";
        final int START_OF_MESSAGE_INDEX            = 0;
        ArrayList<String> dataReceivedFromClient    = new ArrayList();
        
        while (true)
        {
            //Socket           connectionSocket   = welcomeSocket.accept();
            //DataOutputStream outToClient        = new DataOutputStream(connectionSocket.getOutputStream());
            
            dataReceivedFromClient.clear();
            this.receiveData(dataReceivedFromClient);
            
            System.out.println("Received: " + dataReceivedFromClient + "\n");
            
            Path path = null;
            if (dataReceivedFromClient.get(START_OF_MESSAGE_INDEX).contains("GET") && 
                dataReceivedFromClient.get(START_OF_MESSAGE_INDEX).contains("HTTP/1.1")
               ) 
            {
                final int START_OF_FILE_PATH_INDEX  = 4;
                final int END_OF_FILE_PATH_INDEX    = 1;
                int endIndex;
                int startIndex;
                
                startIndex = dataReceivedFromClient.get(START_OF_MESSAGE_INDEX).indexOf("GET")    + START_OF_FILE_PATH_INDEX;
                endIndex = dataReceivedFromClient.get(START_OF_MESSAGE_INDEX).indexOf("HTTP/1.1") - END_OF_FILE_PATH_INDEX;
                filePath = dataReceivedFromClient.get(START_OF_MESSAGE_INDEX).substring(startIndex + 1, endIndex);
                
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
            else if (!dataReceivedFromClient.get(START_OF_MESSAGE_INDEX).contains("HTTP/1.1"))
            {
                statusLine = "HTTP/1.1 505 HTTP Version Not Supported" + CRLF;
            }
            else
            {
                statusLine = "HTTP/1.1 400 Bad Request" + CRLF;
            }
            
            if (statusLine.equals("HTTP/1.1 200 OK" + CRLF)) 
            {   
                //
                String data                     = "";
                //String headerMessage            = "";
                
                for (String line : Files.readAllLines(path))
                {
                    data += line;
                }
                
                this.transmitData(statusLine, data, filePath);
                
                //System.out.println("\nData written to the client:\n\n" + serverToClientMessage);
                
            }
        }
    }

    private void receiveData(ArrayList<String> dataReceived) throws IOException
    {
            String tempDataReceived = null;
            
            do
            {
                tempDataReceived = this.tcpConnection.readLine();
                dataReceived.add(tempDataReceived);
            }
            while (!tempDataReceived.isEmpty() && tempDataReceived != null);
    }
    
    private void transmitData(String statusLine, String data, String filePath) throws IOException
    {
        String headerMessage            = "";
        String serverToClientMessage    = "";
        
        headerMessage = this.header.addHTTPHeaders(SERVER_NAME, Integer.toString(data.length()), filePath) + CRLF; 
        serverToClientMessage = statusLine + headerMessage + data;
        this.tcpConnection.writeBytes(serverToClientMessage);
    }
}
