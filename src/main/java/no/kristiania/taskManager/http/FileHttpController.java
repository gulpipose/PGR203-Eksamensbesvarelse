package no.kristiania.taskManager.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class FileHttpController implements HttpController {
    private HttpServer httpServer;

    public FileHttpController(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    @Override
    public void handle(String requestPath, OutputStream outputStream, Map<String, String> query) throws IOException {
        File file = new File(httpServer.getAssetRoot() + requestPath);
        if(file.isDirectory()){
            file = new File(file, "index.html");
        }
        if(file.exists()){
            long length = file.length();
            outputStream.write(("HTTP:/1.1 200 OK\r\n" +
                    "Content-length: " + length + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n").getBytes());
            try (FileInputStream fileInputStream = new FileInputStream(file)){
                fileInputStream.transferTo(outputStream);
            }
        } else {
            outputStream.write(("HTTP/1.1 404 Not found\r\n" +
                    "Connection: close\r\n" +
                    "\r\n").getBytes());
        }
    }
}