package com.example.application.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;

@Entity
public class Jadwal extends AbstractEntity {

    private String namapembooking;
    private String nohp;
    private LocalDate bookinguntuktanggal;
    private LocalDateTime jam;
    private String jumlahdp;

    public String getNamapembooking() {
        return namapembooking;
    }
    public void setNamapembooking(String namapembooking) {
        this.namapembooking = namapembooking;
    }
    public String getNohp() {
        return nohp;
    }
    public void setNohp(String nohp) {
        this.nohp = nohp;
    }
    public LocalDate getBookinguntuktanggal() {
        return bookinguntuktanggal;
    }
    public void setBookinguntuktanggal(LocalDate bookinguntuktanggal) {
        this.bookinguntuktanggal = bookinguntuktanggal;
    }
    public LocalDateTime getJam() {
        return jam;
    }
    public void setJam(LocalDateTime jam) {
        this.jam = jam;
    }
    public String getJumlahdp() {
        return jumlahdp;
    }
    public void setJumlahdp(String jumlahdp) {
        this.jumlahdp = jumlahdp;
    }

}
