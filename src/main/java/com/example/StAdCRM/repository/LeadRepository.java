package com.example.StAdCRM.repository;

import com.example.StAdCRM.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    
    Optional<Lead> findByPhone(String phone);
    
    List<Lead> findByAssignedCounselor(String counselor);
}
