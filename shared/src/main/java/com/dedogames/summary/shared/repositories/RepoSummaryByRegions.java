package com.dedogames.summary.shared.repositories;


import com.dedogames.summary.shared.entities.SummaryByRegions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoSummaryByRegions extends JpaRepository<SummaryByRegions,String> {
}

