package com.example.netbook.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.netbook.models.entities.Reporte;

@Repository
public interface  ReporteRepository extends JpaRepository<Reporte, Integer> {

}
