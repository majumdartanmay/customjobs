package com.customjobs.networking.helpers;

import com.customjobs.networking.entity.Scripts;
import com.customjobs.networking.repositories.ScriptsRepository;
import com.customjobs.networking.utils.ScriptStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class ScriptDbHelpers {
    @Autowired
    ScriptsRepository scriptsRepository;

    public Scripts createRecord(String userName, String fileName, String absolutePath, ScriptStatus scriptStatus) {
        Scripts data = new Scripts(fileName, userName, absolutePath, scriptStatus);
        scriptsRepository.save(new Scripts(fileName, userName, absolutePath, scriptStatus));
        return data;
    }
}
