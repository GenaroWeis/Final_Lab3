package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;

public class Prestamo {

    private long id;
    private long numeroCliente;
    private int plazoMeses;
    private long montoPedido; // Monto solicitado por el cliente
    private long montoConIntereses; // Monto total con intereses
    private long saldoRestante; // Saldo a pagar
    private TipoMoneda moneda;
    private int cuotasPagas;

    // Constructor: vacio
    public Prestamo() {
    }

    // Constructor
    public Prestamo(long numeroCliente, int plazoMeses, long montoPedido, TipoMoneda moneda) {
        this.numeroCliente = numeroCliente;
        this.plazoMeses = plazoMeses;
        this.montoPedido = montoPedido;
        this.moneda = moneda;
        this.cuotasPagas = 0; // Inicialmente no se ha pagado
    }


    // marcar cuotas como pagadas
    public void pagarCuota() {
        this.cuotasPagas++;
    }
    
    // si el numero de cuotas pagas alcanzo el plazo en meses
    public boolean estaPagado() {
        return cuotasPagas >= plazoMeses;
    }


    public long getMontoConIntereses() {
        return montoConIntereses;
    }

    public void setMontoConIntereses(long montoConIntereses) {
        this.montoConIntereses = montoConIntereses;
    }
    public long getSaldoRestante() {
        return saldoRestante;
    }
    public void setSaldoRestante(long saldoRestante) {
        this.saldoRestante = saldoRestante;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNumeroCliente() {
        return numeroCliente;
    }

    public void setNumeroCliente(long numeroCliente) {
        this.numeroCliente = numeroCliente;
    }

    public int getPlazoMeses() {
        return plazoMeses;
    }

    public void setPlazoMeses(int plazoMeses) {
        this.plazoMeses = plazoMeses;
    }

    public long getMontoPedido() {
        return montoPedido;
    }

    public void setMontoPedido(long montoPedido) {
        this.montoPedido = montoPedido;
    }

    public TipoMoneda getMoneda() {
        return moneda;
    }

    public void setMoneda(TipoMoneda moneda) {
        this.moneda = moneda;
    }

    public int getCuotasPagas() {
        return cuotasPagas;
    }

    public void setCuotasPagas(int cuotasPagas) {
        this.cuotasPagas = cuotasPagas;
    }
}
    