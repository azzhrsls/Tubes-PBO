package com.example.application.data.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.validation.constraints.Email;

@Entity
public class Bookingonline extends AbstractEntity {

    private String namapenyewa;
    private String nohp;
    @Email
    private String email;
    private LocalDate bookinguntuktanggal;
    private LocalDateTime jam;
    private String paket;
    private String buktidp;

    public String getNamapenyewa() {
        return namapenyewa;
    }
    public void setNamapenyewa(String namapenyewa) {
        this.namapenyewa = namapenyewa;
    }
    public String getNohp() {
        return nohp;
    }
    public void setNohp(String nohp) {
        this.nohp = nohp;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
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
    public String getPaket() {
        return paket;
    }
    public void setPaket(String paket) {
        this.paket = paket;
    }
    public String getBuktidp() {
        return buktidp;
    }
    public void setBuktidp(String buktidp) {
        this.buktidp = buktidp;
    }

}
