package ar.edu.utn.frbb.tup.model;

import java.time.LocalDateTime;
import java.util.Random;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.model.enumModels.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;

public class Cuenta {

    private long numeroCuenta;
    private LocalDateTime fechaCreacion;
    private long balance;
    private TipoCuenta tipoCuenta;
    private long dniTitular;
    private TipoMoneda moneda;


    // Constructor
    public Cuenta() {
        this.numeroCuenta = new Random().nextLong();
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now();
    }
    
    //Constructor: DTO
    public Cuenta(CuentaDto cuentaDto){
        this.numeroCuenta = Math.abs(new Random().nextLong() % 1_000_000_000L) + 1_000_000_000L;
        this.balance = 0;
        this.fechaCreacion = LocalDateTime.now(); 
        this.tipoCuenta = TipoCuenta.fromString(cuentaDto.getTipoCuenta());
        this.moneda = TipoMoneda.fromString(cuentaDto.getMoneda());
        this.dniTitular = cuentaDto.getDniTitular();
    }


    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public Cuenta setTipoCuenta(TipoCuenta tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
        return this;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public Cuenta setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
        return this;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Cuenta setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public long getBalance() {
        return balance;
    }

    public Cuenta setBalance(long balance) {
        this.balance = balance;
        return this;
    }

    public long getTitular() {
        return dniTitular;
    }

    public Cuenta setTitular(long dniTitular) {
        this.dniTitular = dniTitular;
        return this;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

}