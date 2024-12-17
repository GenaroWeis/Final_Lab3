package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enumModels.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;
import java.time.LocalDateTime;

public class CuentaEntity extends BaseEntity{

    String nombre;
    LocalDateTime fechaCreacion;
    long balance;
    String tipoCuenta;
    Long titular;
    long numeroCuenta;
    private String moneda;


    // convertir Cuenta a CuentaEntity
    public CuentaEntity(Cuenta cuenta) {
        super(cuenta.getNumeroCuenta()); 
        this.numeroCuenta = cuenta.getNumeroCuenta(); 
        this.balance = cuenta.getBalance();
        this.tipoCuenta = cuenta.getTipoCuenta().toString();
        this.titular = cuenta.getTitular();
        this.fechaCreacion = cuenta.getFechaCreacion();
        this.moneda = cuenta.getMoneda().toString();
    }


    /// convertir CuentaEntity a Cuenta
    public Cuenta toCuenta() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(this.numeroCuenta); // Ahora se recupera correctamente.
        cuenta.setBalance(this.balance);
        cuenta.setTipoCuenta(TipoCuenta.valueOf(this.tipoCuenta));
        cuenta.setFechaCreacion(this.fechaCreacion);
        cuenta.setTitular(this.titular);
        cuenta.setMoneda(TipoMoneda.valueOf(this.moneda));
        return cuenta;
    }


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Long getTitular() {
        return titular;
    }

    public void setTitular(Long titular) {
        this.titular = titular;
    }

    public long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getMoneda() {
        return moneda;
    }
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }
}
