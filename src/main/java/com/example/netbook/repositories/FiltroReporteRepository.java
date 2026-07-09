package com.example.netbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.netbook.models.entities.FiltroReporte;

@Repository
public interface  FiltroReporteRepository extends JpaRepository<FiltroReporte, Integer> {
    
    
}
