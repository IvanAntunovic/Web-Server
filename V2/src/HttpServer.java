package web.server;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class HttpServer 
{
    final String    SERVER_NAME     = "TestServer/1.0.0";
    final String    CRLF            = "\r\n";

    TcpConnection           tcpConnection;
    ResponseBuilder         header;
    ServerSocket            welcomeSocket;
    TcpDeserializatior      tcpDeserializatior;
    TcpSerializator         tcpSerializator;
    StaticFileHandler       staticFileHandler;
    
    public HttpServer(String socketNumber) throws IOException
    {
        this.header              = new ResponseBuilder();
        this.tcpDeserializatior  = new TcpDeserializatior();
        this.tcpSerializator     = new TcpSerializator();
        this.staticFileHandler   = new StaticFileHandler();
        
        try 
        {
            welcomeSocket = new ServerSocket(Integer.parseInt(socketNumber));
        }
        catch (IOException ex) 
        {
            Logger.getLogger(HttpServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.tcpConnection = new TcpConnection(welcomeSocket);
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
        while (true)
        {   
            RequestHttpMessage requestMessage;
            ResponseHttpMessage responseMessage;
            ArrayList<String> requestMessageBytes = new ArrayList();
            String resposeMessageBytes;
            
            this.tcpConnection.readBytes(requestMessageBytes);     
            requestMessage      = this.tcpDeserializatior.getRequestMessage(requestMessageBytes);
            responseMessage     = this.staticFileHandler.serveFiles(requestMessage, this);
            resposeMessageBytes = this.tcpSerializator.getRespondMessageBytes(responseMessage);
            this.tcpConnection.writeBytes(resposeMessageBytes);
        }
    }
    
    public String getServerName()
    {
        return this.SERVER_NAME;
    }
}