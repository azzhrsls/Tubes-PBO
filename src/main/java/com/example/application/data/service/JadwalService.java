package com.example.application.data.service;

import com.example.application.data.entity.Jadwal;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class JadwalService {

    private final JadwalRepository repository;

    public JadwalService(JadwalRepository repository) {
        this.repository = repository;
    }

    public Optional<Jadwal> get(Long id) {
        return repository.findById(id);
    }

    public Jadwal update(Jadwal entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Jadwal> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Jadwal> list(Pageable pageable, Specification<Jadwal> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
