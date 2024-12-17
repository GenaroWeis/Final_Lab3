package ar.edu.utn.frbb.tup.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoPlanPago;

/*calculos para la logica de prestamos*/

@Component
public class PrestamoCalculator {

    private static final double INTERES_ANUAL = 0.10; // 10% anual


    //CALCULAMOS EL VALOR TOTAL DEL MONTO CON INTERESES
    public void calcularValor(Prestamo prestamo) {
    // Validar datos de préstamo
    if (prestamo == null || prestamo.getMontoPedido() <= 0 || prestamo.getPlazoMeses() <= 0) {
        throw new IllegalArgumentException("Error: Datos de préstamo inválidos para el cálculo.");
    }
    long montoConIntereses = (long) (prestamo.getMontoPedido() * (1 + INTERES_ANUAL));
    prestamo.setMontoConIntereses(montoConIntereses);
    prestamo.setSaldoRestante(montoConIntereses);
    }

    //CALCULAR EL VALOR DE CADA CUOTA (en base al valor del montototal con intereses)
    public long calcularValorCuota(Prestamo prestamo) {
        return prestamo.getMontoConIntereses() / prestamo.getPlazoMeses();
    }


    // GENERA UN PLAN DE PAGOS PARA UN PRÉSTAMO BASADO EN CUOTAS IGUALES
    public List<PrestamoPlanPago> generarPlanPagos(Prestamo prestamo) {
    // Validar datos de préstamo
    if (prestamo == null || prestamo.getPlazoMeses() <= 0 || prestamo.getMontoConIntereses() <= 0) {
        throw new IllegalArgumentException("Datos de préstamo inválidos para generar el plan de pagos.");
    }
    List<PrestamoPlanPago> plan = new ArrayList<>();//lista para almacenar las cuotas
    double montoCuota = calcularValorCuota(prestamo);//calcular el valor de la cuota
    LocalDate fechaInicio = LocalDate.now();//fecha actual como referencia para las fechas de las cuotas
    //itera y agrega las cuotas a la lista de plan de pagos
    for (int i = 1; i <= prestamo.getPlazoMeses(); i++) {
        plan.add(new PrestamoPlanPago(i, montoCuota, fechaInicio.plusMonths(i)));
    }
    return plan;
}
}

    



