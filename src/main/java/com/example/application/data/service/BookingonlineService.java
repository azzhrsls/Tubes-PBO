package com.example.application.data.service;

import com.example.application.data.entity.Bookingonline;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class BookingonlineService {

    private final BookingonlineRepository repository;

    public BookingonlineService(BookingonlineRepository repository) {
        this.repository = repository;
    }

    public Optional<Bookingonline> get(Long id) {
        return repository.findById(id);
    }

    public Bookingonline update(Bookingonline entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Bookingonline> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Bookingonline> list(Pageable pageable, Specification<Bookingonline> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
