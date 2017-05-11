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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fairenough
 */
public class TCPConnection 
{
    Socket socket;
    DataOutputStream outToClient;
    BufferedReader inFromClient;
    
    public TCPConnection(ServerSocket welcomeSocket) throws IOException
    {
        this.socket = welcomeSocket.accept();
    }
    
    public void open() throws IOException
    {
        
        this.outToClient  = new DataOutputStream(this.socket.getOutputStream());
        this.inFromClient = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }
    
    public void close() throws IOException
    {
        this.outToClient.close();
        this.inFromClient.close();
        this.socket.close();
    }
    
    public String readLine()
    {
        String lineReadFromClient = null;
        
        try 
        {
            lineReadFromClient = this.inFromClient.readLine();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(TCPConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return lineReadFromClient;
    }
    
    public void writeBytes(String serverToClientMessage) throws IOException
    {
        this.outToClient.writeBytes(serverToClientMessage);
        this.outToClient.flush();
    }
}
