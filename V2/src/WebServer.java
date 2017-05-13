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
public class WebServer
{
    public static void main(String[] args) throws Exception
    {
       HttpServer server = new HttpServer("9009");
       server.connect();
       server.run();
       server.disconnect();
    }
}