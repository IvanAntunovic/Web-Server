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
import java.util.List;
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
        final String NO_FILE_FOUND = "No File Found";
        final boolean FILE_NOT_FOUND = false;
        final boolean FILE_FOUND = true;
        List<String> noFileList = new ArrayList<String>();
        noFileList.add(NO_FILE_FOUND);

        if (requestMessage.isGet())
        {
            path = Paths.get(requestMessage.getUrlField());
            if (!Files.exists(path))
            {   
                return this.responseBuilder.notFound().addBody(noFileList).addHeaders(requestMessage, httpServer.getServerName(), FILE_NOT_FOUND).build();
            }
            return this.responseBuilder
                .ok()
                .addBody(Files.readAllLines(path))
                .addHeaders(requestMessage, httpServer.getServerName(), FILE_FOUND)
                .build();
        }
        return null;
    }
}
