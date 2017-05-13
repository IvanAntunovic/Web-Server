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
import java.util.ArrayList;

/**
 *
 * @author fairenough
 */
public class TcpConnection 
{
    Socket socket;
    DataOutputStream outToClient;
    BufferedReader inFromClient;
    
    public TcpConnection(ServerSocket welcomeSocket) throws IOException
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
    
    public void readBytes(ArrayList<String> dataReceived) throws IOException
    {
        String tempDataReceived;

        for (;;)
        {
            tempDataReceived = this.inFromClient.readLine();
            if (!tempDataReceived.isEmpty())
            {
               dataReceived.add(tempDataReceived); 
            }
            else
            {
                break;
            }
        }   
    }
    
    public void writeBytes(String serverToClientMessage) throws IOException
    {
        System.out.println(serverToClientMessage);
        this.outToClient.writeBytes(serverToClientMessage);
        this.outToClient.flush();
    }
}