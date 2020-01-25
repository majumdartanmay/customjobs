package com.customjobs.networking.repositories;

import com.customjobs.networking.entity.Scripts;
import com.customjobs.networking.utils.ScriptStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ScriptsRepository extends JpaRepository<Scripts, String> {
    List<Scripts> findByuserName(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE Scripts s SET s.status = :status WHERE id = :scriptId")
    void updateScriptStatus(@Param("scriptId") String scriptId, @Param("status")ScriptStatus status);

}
