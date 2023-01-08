package com.example.application.data.service;

import com.example.application.data.entity.Bookingonline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingonlineRepository
        extends
            JpaRepository<Bookingonline, Long>,
            JpaSpecificationExecutor<Bookingonline> {

}
