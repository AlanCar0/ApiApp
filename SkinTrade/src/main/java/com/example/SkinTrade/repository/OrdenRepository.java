package com.example.SkinTrade.repository;

import com.example.SkinTrade.model.Orden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
}