package com.example.application.data.service;

import com.example.application.data.entity.Jadwal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JadwalRepository extends JpaRepository<Jadwal, Long>, JpaSpecificationExecutor<Jadwal> {

}
