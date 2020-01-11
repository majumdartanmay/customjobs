package com.customjobs.networking.utils.executors;

import com.customjobs.networking.utils.CommandExecutors;
import com.customjobs.networking.utils.FileStorageService;
import com.customjobs.networking.utils.Queue.QueueCommunicationUtils;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class ScriptExecutors {

    private String absolutePath;
    private String user;
    private String scriptName;
    private int timeout = 10;
    private CommandExecutors commandExecutors;
    private static final String virtualEnvPrefix = "venv";
    private QueueCommunicationUtils queueCommunicationUtils;

    public ScriptExecutors(String absolutePath, String user, String scriptName) {

        this.absolutePath = absolutePath;
        this.user = user;
        this.scriptName = scriptName;
        commandExecutors = new CommandExecutors();
        commandExecutors.setWorkingDirectory(absolutePath);

        if (!isDirectoryExists(virtualEnvPrefix)) {
            setVirtualEnvironment(virtualEnvPrefix);
        }
    }

    public void setQueueCommunicationUtils(QueueCommunicationUtils queueCommunicationUtils) {
        this.queueCommunicationUtils = queueCommunicationUtils;
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


    public void executeScript(FileStorageService fileStorageService) {

        String virtualPath = fileStorageService.getVirtualEnvironmentOfUser(user, virtualEnvPrefix);

        try {

        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(() -> {
                String output = commandExecutors.executeCommand (
                        String.format("%s\\Scripts\\python.exe", virtualPath) , scriptName);
                System.out.println(output);
                sendMessage(output);
            }
        );

        ExecutorService timer = Executors.newCachedThreadPool();
        Callable<String> timeoutCallback = () -> {
            service.shutdownNow();
            return "Script time limit exceeded";
        };

        Future<String> timeoutFuture = timer.submit(timeoutCallback);
        String output = timeoutFuture.get(timeout, TimeUnit.SECONDS);

        sendMessage(output);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void sendMessage(String output) {

        if (queueCommunicationUtils != null) {
            queueCommunicationUtils.sendMessageForScriptExecutionStatus(output);
        }
    }
}
