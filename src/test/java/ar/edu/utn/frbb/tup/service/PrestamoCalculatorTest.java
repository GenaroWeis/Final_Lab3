package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoPlanPago;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrestamoCalculatorTest {

    private PrestamoCalculator prestamoCalculator;

    @BeforeEach
    void setUp() {
        prestamoCalculator = new PrestamoCalculator();
    }



    // -------------------------------
    // Test calcularValor(Prestamo)
    // -------------------------------
    @Test
    void testCalcularValor_Exito() {
        Prestamo prestamo = new Prestamo(12345678L, 12, 100_000L, null);

        prestamoCalculator.calcularValor(prestamo);

        assertEquals(110_000L, prestamo.getMontoConIntereses()); // 100,000 + 10% interés
        assertEquals(110_000L, prestamo.getSaldoRestante());
    }

    @Test
    void testCalcularValor_PrestamoNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> prestamoCalculator.calcularValor(null));

        assertEquals("Error: Datos de préstamo inválidos para el cálculo.", exception.getMessage());
    }

    @Test
    void testCalcularValor_MontoNegativo() {
        Prestamo prestamo = new Prestamo(12345678L, 12, -50_000L, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> prestamoCalculator.calcularValor(prestamo));

        assertEquals("Error: Datos de préstamo inválidos para el cálculo.", exception.getMessage());
    }

    @Test
    void testCalcularValor_PlazoCero() {
        Prestamo prestamo = new Prestamo(12345678L, 0, 100_000L, null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> prestamoCalculator.calcularValor(prestamo));

        assertEquals("Error: Datos de préstamo inválidos para el cálculo.", exception.getMessage());
    }



    // -------------------------------
    // Test calcularValorCuota(Prestamo)
    // -------------------------------

    @Test
    void testCalcularValorCuota_Exito() {
        Prestamo prestamo = new Prestamo(12345678L, 10, 100_000L, null);
        prestamo.setMontoConIntereses(110_000L); // 10% de interés ya calculado

        long valorCuota = prestamoCalculator.calcularValorCuota(prestamo);

        assertEquals(11_000L, valorCuota); // 110,000 / 10 cuotas
    }

    @Test
    void testCalcularValorCuota_DivisionPorCero() {
        Prestamo prestamo = new Prestamo(12345678L, 0, 100_000L, null);
        prestamo.setMontoConIntereses(110_000L);

        ArithmeticException exception = assertThrows(ArithmeticException.class,
                () -> prestamoCalculator.calcularValorCuota(prestamo));

        assertEquals("/ by zero", exception.getMessage());
    }



    // -------------------------------
    // Test generarPlanPagos(Prestamo)
    // -------------------------------
    
    @Test
    void testGenerarPlanPagos_Exito() {
        Prestamo prestamo = new Prestamo(12345678L, 3, 100_000L, null);
        prestamo.setMontoConIntereses(105_000L); // Valor con intereses calculado previamente

        List<PrestamoPlanPago> planPagos = prestamoCalculator.generarPlanPagos(prestamo);

        assertEquals(3, planPagos.size());

        assertEquals(1, planPagos.get(0).getNumeroCuota());
        assertEquals(35_000, planPagos.get(0).getMontoCuota());
        assertEquals(LocalDate.now().plusMonths(1), planPagos.get(0).getFechaPago());

        assertEquals(2, planPagos.get(1).getNumeroCuota());
        assertEquals(35_000, planPagos.get(1).getMontoCuota());
        assertEquals(LocalDate.now().plusMonths(2), planPagos.get(1).getFechaPago());

        assertEquals(3, planPagos.get(2).getNumeroCuota());
        assertEquals(35_000, planPagos.get(2).getMontoCuota());
        assertEquals(LocalDate.now().plusMonths(3), planPagos.get(2).getFechaPago());
    }

    @Test
    void testGenerarPlanPagos_PrestamoNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> prestamoCalculator.generarPlanPagos(null));

        assertEquals("Datos de préstamo inválidos para generar el plan de pagos.", exception.getMessage());
    }

    @Test
    void testGenerarPlanPagos_PlazoCero() {
        Prestamo prestamo = new Prestamo(12345678L, 0, 100_000L, null);
        prestamo.setMontoConIntereses(110_000L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> prestamoCalculator.generarPlanPagos(prestamo));

        assertEquals("Datos de préstamo inválidos para generar el plan de pagos.", exception.getMessage());
    }

    @Test
    void testGenerarPlanPagos_MontoConInteresesCero() {
        Prestamo prestamo = new Prestamo(12345678L, 5, 100_000L, null);
        prestamo.setMontoConIntereses(0L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> prestamoCalculator.generarPlanPagos(prestamo));

        assertEquals("Datos de préstamo inválidos para generar el plan de pagos.", exception.getMessage());
    }
}
