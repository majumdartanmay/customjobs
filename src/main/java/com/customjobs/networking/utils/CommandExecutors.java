package com.customjobs.networking.utils;

import lombok.Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Data
public class CommandExecutors {
    String workingDirectory;
    public String executeCommand(String... command) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    command);
            if (workingDirectory != null && !workingDirectory.isEmpty())
                builder.directory(new File(workingDirectory));
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break;
                }else{
                    stringBuilder.append(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
