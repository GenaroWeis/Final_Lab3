package ar.edu.utn.frbb.tup.persistence.entity;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;

public class PrestamoEntity extends BaseEntity {
    
    private long numeroCliente;
    private int plazoMeses;
    private long montoPedido;
    private long montoConIntereses;
    private long saldoRestante;
    private int cuotasPagas;
    private String moneda;

    // Constructor para convertir desde Prestamo
    public PrestamoEntity(Prestamo prestamo) {
        super(prestamo.getId());
        this.numeroCliente = prestamo.getNumeroCliente();
        this.plazoMeses = prestamo.getPlazoMeses();
        this.montoPedido = prestamo.getMontoPedido();
        this.montoConIntereses = prestamo.getMontoConIntereses();
        this.saldoRestante = prestamo.getSaldoRestante();
        this.cuotasPagas = prestamo.getCuotasPagas();
        this.moneda = prestamo.getMoneda().toString();
    }

    // Método para convertir de PrestamoEntity a Prestamo
    public Prestamo toPrestamo() {
        Prestamo prestamo = new Prestamo();
        prestamo.setId(this.getId());
        prestamo.setNumeroCliente(this.getNumeroCliente());
        prestamo.setPlazoMeses(this.getPlazoMeses());
        prestamo.setMontoPedido(this.getMontoPedido());
        prestamo.setMoneda(TipoMoneda.valueOf(this.getMoneda()));
        prestamo.setMontoConIntereses(this.getMontoConIntereses());
        prestamo.setSaldoRestante(this.getSaldoRestante());
        prestamo.setCuotasPagas(this.getCuotasPagas());
        return prestamo;
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public long getMontoPedido() {
        return montoPedido;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public long getMontoConIntereses() {
        return montoConIntereses;
    }


    public long getSaldoRestante() {
        return saldoRestante;
    }

    public int getCuotasPagas() {
        return cuotasPagas;
    }
}

    
