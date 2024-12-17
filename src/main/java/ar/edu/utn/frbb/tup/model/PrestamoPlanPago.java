package ar.edu.utn.frbb.tup.model;

import java.time.LocalDate;

/*Cada instancia indica una cuota individual con su monto, número y una fecha de pago*/

public class PrestamoPlanPago {

    private int numeroCuota;
    private double montoCuota;
    private LocalDate fechaPago;


    public PrestamoPlanPago(int numeroCuota, double montoCuota, LocalDate localDate) {
        this.numeroCuota = numeroCuota;
        this.montoCuota = montoCuota;
        this.fechaPago = localDate; 
    }
    
    
    public int getNumeroCuota() {
        return numeroCuota;
    }

    public void setNumeroCuota(int numeroCuota) {
        this.numeroCuota = numeroCuota;
    }

    public double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(double montoCuota) {
        this.montoCuota = montoCuota;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }
}


