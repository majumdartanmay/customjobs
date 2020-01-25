package com.customjobs.networking.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScriptExecutionModel implements Serializable {

    private static final long serialVersionUID = -2343243243242432341L;

    private String absolutePathOnMemory;
    private String scriptId;
    private String userName;

    public ScriptExecutionModel(){super();}

    public ScriptExecutionModel(String absolutePathOnMemory, String scriptId, String userName) {
        this.absolutePathOnMemory = absolutePathOnMemory;
        this.scriptId = scriptId;
        this.userName = userName;
    }

}
