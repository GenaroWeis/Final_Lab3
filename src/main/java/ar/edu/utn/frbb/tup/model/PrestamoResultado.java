package ar.edu.utn.frbb.tup.model;

import java.util.List;
import ar.edu.utn.frbb.tup.model.enumModels.PrestamoEstados;

/*la respuesta que le es enviada al cliente*/

public class PrestamoResultado {

    private PrestamoEstados estado;
    private String mensaje;
    private List<PrestamoPlanPago> planPagos; 
    public PrestamoResultado() {
    }


    public PrestamoResultado(PrestamoEstados estado, String mensaje, List<PrestamoPlanPago> planPagos) {
        this.estado = estado;
        this.mensaje = mensaje;
        this.planPagos = planPagos;
    }


    public PrestamoEstados getEstado() {
        return estado;
    }

    public void setEstado(PrestamoEstados estado) {
        this.estado = estado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public List<PrestamoPlanPago> getPlanPagos() {
        return planPagos;
    }

    public void setPlanPagos(List<PrestamoPlanPago> planPagos) {
        this.planPagos = planPagos;
    }
}
