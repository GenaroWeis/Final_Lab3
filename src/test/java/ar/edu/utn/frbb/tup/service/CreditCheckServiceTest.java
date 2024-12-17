package ar.edu.utn.frbb.tup.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreditCheckServiceTest {

    private CreditCheckService creditCheckService;
    private Random randomMock;

    @BeforeEach
    void setUp() {
        randomMock = mock(Random.class);
        creditCheckService = new CreditCheckService() {
            @Override
            protected double getRandomDouble() {
                return randomMock.nextDouble();
            }
        };
    }

    // -------------------------------
    // Test: DNI
    // -------------------------------

    // TEST: DNI válido dentro del rango
    @Test
    void testEsDniValido_Success() {
        long dniValido = 30_000_000L;
        boolean result = creditCheckService.esDniValido(dniValido);
        assertTrue(result, "El DNI debería ser válido.");
    }

    // TEST: DNI muy bajo (fuera del rango)
    @Test
    void testEsDniValido_DniBajo() {
        long dniInvalido = 9_999_999L;
        boolean result = creditCheckService.esDniValido(dniInvalido);
        assertFalse(result, "El DNI debería ser inválido por ser demasiado bajo.");
    }

    // TEST: DNI muy alto (fuera del rango)
    @Test
    void testEsDniValido_DniAlto() {
        long dniInvalido = 100_000_000L;
        boolean result = creditCheckService.esDniValido(dniInvalido);
        assertFalse(result, "El DNI debería ser inválido por ser demasiado alto.");
    }

    // TEST: verificarEstadoCrediticio - DNI válido, probabilidad de rechazo satisfecha (APROBADO)
    @Test
    void testVerificarEstadoCrediticio_Aprobado() {
        long dniValido = 30_000_000L;

        when(randomMock.nextDouble()).thenReturn(0.5); // Mayor que PROBABILIDAD_RECHAZO (0.2)
        
        boolean result = creditCheckService.verificarEstadoCrediticio(dniValido);

        assertTrue(result, "El estado crediticio debería ser aprobado.");
        verify(randomMock, times(1)).nextDouble();
    }

    // TEST: verificarEstadoCrediticio - DNI válido, probabilidad de rechazo (RECHAZADO)
    @Test
    void testVerificarEstadoCrediticio_Rechazado() {
        long dniValido = 30_000_000L;

        when(randomMock.nextDouble()).thenReturn(0.1); // Menor que PROBABILIDAD_RECHAZO (0.2)
        
        boolean result = creditCheckService.verificarEstadoCrediticio(dniValido);

        assertFalse(result, "El estado crediticio debería ser rechazado.");
        verify(randomMock, times(1)).nextDouble();
    }

    // TEST: verificarEstadoCrediticio - DNI inválido (muy bajo)
    @Test
    void testVerificarEstadoCrediticio_DniInvalido_Bajo() {
        long dniInvalido = 9_999_999L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> creditCheckService.verificarEstadoCrediticio(dniInvalido));

        assertEquals("Error: DNI inválido. Debe estar entre 10000000 y 99999999 y tener 8 dígitos.", exception.getMessage());
    }

    // TEST: verificarEstadoCrediticio - DNI inválido (muy alto)
    @Test
    void testVerificarEstadoCrediticio_DniInvalido_Alto() {
        long dniInvalido = 100_000_000L;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> creditCheckService.verificarEstadoCrediticio(dniInvalido));

        assertEquals("Error: DNI inválido. Debe estar entre 10000000 y 99999999 y tener 8 dígitos.", exception.getMessage());
    }

}
