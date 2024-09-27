package com.example.testCI.CD.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class ShellCommandService {

    private static final String SHELL_COMMAND = "mvn versions:display-dependency-updates";

    public void checkForDependencyUpdates(String workingDirectory) throws Exception {
        // Run Maven dependency update command
        runShellCommand(SHELL_COMMAND, workingDirectory);

        // Run Git status to check for updates in the pom.xml
        String gitStatusOutput = runShellCommandWithOutput("git status -s pom.xml", workingDirectory);

        if (!gitStatusOutput.isEmpty()) {
            System.out.println("Updates available for Maven dependencies.");
        } else {
            System.out.println("No updates available.");
        }
    }

    private void runShellCommand(String command, String workingDirectory) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.directory(new java.io.File(workingDirectory));
        Process process = processBuilder.start();
        process.waitFor();
    }

    private String runShellCommandWithOutput(String command, String workingDirectory) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
        processBuilder.directory(new java.io.File(workingDirectory));
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        process.waitFor();
        return output.toString();
    }
}
