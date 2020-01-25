package com.customjobs.networking.entity;

import com.customjobs.networking.utils.ScriptStatus;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
public class Scripts implements Serializable {
    private static final long serialVersionUID = -2343243243242432341L;

    public Scripts(){super();}

    public Scripts(String id, String userName, String absolutePathOnMemory, ScriptStatus scriptStatus) {
        this.id = id;
        this.userName = userName;
        this.absolutePathOnMemory = absolutePathOnMemory;
        this.status = scriptStatus;
    }

    @Id
    private String id;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @UpdateTimestamp
    private LocalDateTime updatedOn;

    private boolean isArchived;

    private String userName;

    private String absolutePathOnMemory;

    private ScriptStatus status;
}
