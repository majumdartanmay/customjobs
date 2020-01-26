package com.customjobs.networking.repositories;

import com.customjobs.networking.entity.Scripts;
import com.customjobs.networking.utils.ScriptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScriptsRepository extends JpaRepository<Scripts, String> {
    List<Scripts> findByuserName(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE Scripts s SET s.status = :status WHERE id = :scriptId")
    void updateScriptStatus(@Param("scriptId") String scriptId, @Param("status")ScriptStatus status);

    @Query (value = "SELECT * from Scripts s WHERE " +
                    "s.user_name = :userName and " +
                    "s.created_on > :startDate and "+
                    "s.created_on < :endDate "+
                    "ORDER BY s.created_on DESC "+
                    "LIMIT :limit",
                    nativeQuery = true)
    List<Scripts> getUserScripts(
                                @Param("userName") String userName,
                                @Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate,
                                @Param("limit") int limit
                                );

}
