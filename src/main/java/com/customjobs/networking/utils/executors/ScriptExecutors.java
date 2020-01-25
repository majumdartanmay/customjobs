package com.customjobs.networking.utils.executors;

import com.customjobs.networking.helpers.ScriptDbHelpers;
import com.customjobs.networking.utils.CommandExecutors;
import com.customjobs.networking.utils.Constants;
import com.customjobs.networking.utils.FileStorageService;
import com.customjobs.networking.utils.queue.QueueCommunicationUtils;
import com.customjobs.networking.utils.ScriptStatus;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;
@Component
public class ScriptExecutors implements ApplicationContextAware {

    private static ApplicationContext ctx;

    private String absolutePath;
    private String userName;
    private String scriptName;
    private int timeout = 10;
    private CommandExecutors commandExecutors;
    private static final String virtualEnvPrefix = "venv";
    private QueueCommunicationUtils queueCommunicationUtils;

    private ScriptExecutors(){}

    public ScriptExecutors(String absolutePath, String userName, String scriptName) {

        this.absolutePath = absolutePath;
        this.userName = userName;
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

    private void setVirtualEnvironment(String virtualEnvironment){
        commandExecutors.executeCommand("python", "-m", "venv", virtualEnvironment);
    }

    private boolean isDirectoryExists(String directory) {
        Path path = Paths.get(absolutePath, directory);
        return Files.exists(path);
    }


    public void executeScript(FileStorageService fileStorageService) {

        String virtualPath = fileStorageService.getVirtualEnvironmentOfUser(userName, virtualEnvPrefix);

        try {

        ExecutorService service = Executors.newSingleThreadExecutor();

        service.execute(() -> {
                String output = commandExecutors.executeCommand (
                        String.format("%s\\Scripts\\python.exe", virtualPath) , scriptName);
                System.out.println(output);
                sendMessage(output);
                updateScriptExecutionStatus(ScriptStatus.COMPLETED);
            }
        );



        ExecutorService timer = Executors.newCachedThreadPool();
        Callable<String> timeoutCallback = () -> {
            service.shutdownNow();
            return "Script time limit exceeded";
        };

        updateScriptExecutionStatus(ScriptStatus.PROCESSING);

        Future<String> timeoutFuture = timer.submit(timeoutCallback);
        String output = timeoutFuture.get(timeout, TimeUnit.SECONDS);

        sendMessage(output);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void updateScriptExecutionStatus(ScriptStatus scriptStatus) {
        ScriptDbHelpers scriptDbHelpers = (ScriptDbHelpers)ctx.getBean(Constants.scriptDBHelpersIdentifier);
        scriptDbHelpers.updateScriptStatus(scriptName, scriptStatus);
    }

    private void sendMessage(String output) {
        if (queueCommunicationUtils != null) {
            queueCommunicationUtils.sendMessageForScriptExecutionStatus(output);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

}
