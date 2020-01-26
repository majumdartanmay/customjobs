package com.customjobs.networking.helpers;

import com.customjobs.networking.entity.Scripts;
import com.customjobs.networking.repositories.ScriptsRepository;
import com.customjobs.networking.utils.Constants;
import com.customjobs.networking.utils.ScriptStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScriptDbHelpers {
    @Autowired
    ScriptsRepository scriptsRepository;

    @Bean(name = Constants.scriptDBHelpersIdentifier)
    public ScriptDbHelpers getBean(){
        return this;
    }

    public Scripts createOrUpdate(String userName, String fileName, String absolutePath, ScriptStatus scriptStatus) {
        Scripts data = new Scripts(fileName, userName, absolutePath, scriptStatus);
        scriptsRepository.save(new Scripts(fileName, userName, absolutePath, scriptStatus));
        return data;
    }

    public void updateScriptStatus(String scriptId, ScriptStatus scriptStatus) {
        scriptsRepository.updateScriptStatus(scriptId, scriptStatus);
    }

    public List<Scripts> getUserScripts(String userName,
                                        LocalDateTime startDate,
                                        LocalDateTime endDate,
                                        int limit,
                                        int offset) {
        return scriptsRepository.getUserScripts(userName, startDate, endDate, limit);
    }
}
