
import java.io.*;
import java.net.*;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class TCPServer {

    public static void main(String argv[]) throws Exception {
        
        String filePath = null;
        String clientSentence;
        String capitalizedSentence;
        ServerSocket welcomeSocket = new ServerSocket(9009);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

            clientSentence = inFromClient.readLine();
            System.out.println("Received: " + clientSentence);

            if (clientSentence.contains("GET")) {
                final int START_OF_FILE_PATH_INDEX = 4;
                final int END_OF_FILE_PATH_INDEX = 1;
                int endIndex;
                int startIndex;

                startIndex = clientSentence.indexOf("GET") + START_OF_FILE_PATH_INDEX;
                endIndex = clientSentence.indexOf("HTTP/1.1") - END_OF_FILE_PATH_INDEX;

                filePath = clientSentence.substring(startIndex + 1, endIndex);
                System.out.println("File path:" + filePath);

            }

            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                for (String line : Files.readAllLines(path)) {
                    System.out.println(line);
                    capitalizedSentence = line.toUpperCase() + '\n';
                    outToClient.writeBytes(capitalizedSentence);
                }
                outToClient.flush();
            }
        }
    }
}
