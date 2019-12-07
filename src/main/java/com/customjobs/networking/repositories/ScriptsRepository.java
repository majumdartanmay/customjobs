package com.customjobs.networking.repositories;

import com.customjobs.networking.entity.Scripts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScriptsRepository extends JpaRepository<Scripts, String> {
    List<Scripts> findByuserName(String userName);
}
