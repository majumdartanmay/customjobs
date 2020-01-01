package com.customjobs.networking.utils.executors;


import com.customjobs.networking.utils.CommandExecutors;
import com.customjobs.networking.utils.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class ScriptExecutors {
    private String absolutePath;
    private String user;
    private String scriptName;
    private int timeout;
    private CommandExecutors commandExecutors;
    private static final String virtualEnvPrefix = "venv";

    @Autowired
    FileStorageService fileStorageService;

    public ScriptExecutors(String absolutePath, String user, String scriptName) {
        this.absolutePath = absolutePath;
        this.user = user;
        this.scriptName = scriptName;
        commandExecutors = new CommandExecutors();
        commandExecutors.setWorkingDirectory(absolutePath);
        if (!isDirectoryExists(virtualEnvPrefix)) {
            setVirtualEnvironment(virtualEnvPrefix);
        }
       // activateVirtualEnvironment(user);
    }

    private void activateVirtualEnvironment(String virtualEnvironment) {
        commandExecutors.executeCommand(String.format("cd", absolutePath,"\\%s\\Scripts\\activate.bat", virtualEnvironment));
    }

    private void setVirtualEnvironment(String virtualEnvironment){
        commandExecutors.executeCommand("python", "-m", "venv", virtualEnvironment);
    }

    private boolean isDirectoryExists(String directory) {
        Path path = Paths.get(absolutePath, directory);
        return Files.exists(path);
    }

    public void executeScript() {

        String virtualPath = fileStorageService.getVirtualEnvironmentOfUser(user, virtualEnvPrefix);

        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(() -> {
            commandExecutors.executeCommand(String.format("%s/scripts/python %s", virtualPath, scriptName));
        });

        ExecutorService timer = Executors.newCachedThreadPool();
        Callable timeoutCallback = () -> {
            service.shutdownNow();
            return null;
        };

        Future timeoutFuture = timer.submit(timeoutCallback);
        try{
            timeoutFuture.get(timeout, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
