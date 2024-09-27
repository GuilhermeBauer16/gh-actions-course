package com.example.testCI.CD.controller;


import com.example.testCI.CD.service.ShellCommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dependency-update")
public class DependencyUpdateController {

    @Autowired
    private ShellCommandService shellCommandService;

    @PostMapping("/check")
    public String checkForUpdates(@RequestParam String baseBranch,
                                  @RequestParam String targetBranch,
                                  @RequestParam String workingDirectory,
                                  @RequestParam(required = false) boolean debug) {
        try {
            // Validate branch names
            if (!isValidBranchName(baseBranch) || !isValidBranchName(targetBranch)) {
                return "Invalid branch name provided.";
            }

            // Validate working directory
            if (!isValidDirectory(workingDirectory)) {
                return "Invalid working directory provided.";
            }

            // Execute Maven and Git commands
            shellCommandService.checkForDependencyUpdates(workingDirectory);

            return "Dependency update check completed successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to check for updates: " + e.getMessage();
        }
    }

    private boolean isValidBranchName(String branch) {
        return branch.matches("[a-zA-Z0-9._/-]+");
    }

    private boolean isValidDirectory(String dir) {
        return dir.matches("[a-zA-Z0-9._/-]+");
    }
}
