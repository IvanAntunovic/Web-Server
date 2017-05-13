/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
/**
 *
 * @author A661517
 */
public class StaticFileHandler
{
    ResponseBuilder responseBuilder;
    
    public StaticFileHandler()
    {
        this.responseBuilder = new ResponseBuilder();
    }
    
    public ResponseHttpMessage serveFiles(RequestHttpMessage requestMessage, HttpServer httpServer) throws IOException
    {     
        Path path;

        if (requestMessage.isGet())
        {
            path = Paths.get(requestMessage.getUrlField());
            if (!Files.exists(path))
            {   
                return this.responseBuilder.notFound().addHeaders(requestMessage, null).build();
            }
            return this.responseBuilder
                .ok()
                .addBody(Files.readAllLines(path))
                .addHeaders(requestMessage, httpServer.getServerName())
                .build();
        }
        return null;
    }
}
