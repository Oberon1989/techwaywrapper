package ru.webdevpet.server.wrapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StartMinecraftServer {

    private final WebSockClient client;

    public StartMinecraftServer(WebSockClient client) {

        this.client = client;
    }
    public void start() {
        try {
            String wrapperConfigPath = "wrapper.conf";
            String commandLine = readFile(wrapperConfigPath);
            client.send(commandLine);

            String[] commandParts = commandLine.split(" ", 2);
            String program = commandParts[0];
            String args = commandParts.length > 1 ? commandParts[1] : "";


            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder processBuilder;

            if (os.contains("win")) {

                processBuilder = new ProcessBuilder("cmd.exe", "/c", program + " " + args);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {

                processBuilder = new ProcessBuilder(program, args);
            } else {
                throw new UnsupportedOperationException("Unsupported OS: " + os);
            }


          //  processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();


//            Thread stdoutThread = new Thread(() -> {
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        client.send(line);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    client.send("Error reading stdout: " + e.getMessage());
//                }
//            });
//
//
//            stdoutThread.start();



            process.waitFor();


           // stdoutThread.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            client.send("Error starting the server: " + e.getMessage());
        }
    }

    private String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString().trim();
    }
}
