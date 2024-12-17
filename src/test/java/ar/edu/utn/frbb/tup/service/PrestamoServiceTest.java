package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import ar.edu.utn.frbb.tup.exception.PrestamoNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.enumModels.PrestamoEstados;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrestamoServiceTest {

    @InjectMocks
    private PrestamoService prestamoService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private PrestamoDao prestamoDao;

    @Mock
    private CreditCheckService creditCheckService;

    @Mock
    private PrestamoCalculator prestamoCalculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    // -------------------------------
    // Test: solicitarPrestamo
    // -------------------------------

    @Test
    void testSolicitarPrestamo_Success() throws ClienteNoEncontradoException, CuentaNoEncontradaException {
        
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 100000L, 12, "P");
        List<PrestamoPlanPago> planPagos = Collections.singletonList(new PrestamoPlanPago(1, 10000L, null));

        when(creditCheckService.verificarEstadoCrediticio(prestamoDto.getNumeroCliente())).thenReturn(true);
        doNothing().when(clienteService).agregarPrestamo(any(), anyLong());
        doNothing().when(cuentaService).actualizarCuenta(any());
        when(prestamoCalculator.generarPlanPagos(any())).thenReturn(planPagos);

        PrestamoResultado resultado = prestamoService.solicitarPrestamo(prestamoDto);

        assertNotNull(resultado);
        assertEquals(PrestamoEstados.APROBADO, resultado.getEstado());
        assertEquals("Préstamo aprobado y acreditado en su cuenta.", resultado.getMensaje());
        assertEquals(1, resultado.getPlanPagos().size());

        verify(creditCheckService, times(1)).verificarEstadoCrediticio(prestamoDto.getNumeroCliente());
        verify(prestamoDao, times(1)).save(any());
        verify(clienteService, times(1)).agregarPrestamo(any(), eq(prestamoDto.getNumeroCliente()));
    }
    

    @Test
    void testSolicitarPrestamo_CreditCheckFail() throws ClienteNoEncontradoException, CuentaNoEncontradaException {
        
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 100000L, 12, "P");

        when(creditCheckService.verificarEstadoCrediticio(prestamoDto.getNumeroCliente())).thenReturn(false);

        PrestamoResultado resultado = prestamoService.solicitarPrestamo(prestamoDto);

        assertNotNull(resultado);
        assertEquals(PrestamoEstados.RECHAZADO, resultado.getEstado());
        assertEquals("El cliente no tiene una calificación crediticia suficiente para un préstamo.", resultado.getMensaje());
        assertTrue(resultado.getPlanPagos().isEmpty());

        verify(creditCheckService, times(1)).verificarEstadoCrediticio(prestamoDto.getNumeroCliente());
        verify(prestamoDao, never()).save(any());
    }


    @Test
    void testSolicitarPrestamo_CuentaNoEncontradaException() throws ClienteNoEncontradoException, CuentaNoEncontradaException {
        
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 100000L, 12, "P");

        when(creditCheckService.verificarEstadoCrediticio(prestamoDto.getNumeroCliente())).thenReturn(true);
        doThrow(new CuentaNoEncontradaException("Cuenta no encontrada")).when(cuentaService).actualizarCuenta(any());

        assertThrows(CuentaNoEncontradaException.class, () -> prestamoService.solicitarPrestamo(prestamoDto));

        verify(prestamoDao, never()).save(any());
    }


    
    // -------------------------------
    // Test: pagarCuota
    // -------------------------------

    @Test
    void testPagarCuota_Success() throws PrestamoNoEncontradoException, NoAlcanzaException, CuentaNoEncontradaException {
        
        long prestamoId = 1L;
        Prestamo prestamo = new Prestamo(12345678L, 12, 100000L, TipoMoneda.PESOS);
        prestamo.setId(prestamoId);
        prestamo.setMontoConIntereses(120000L);
        prestamo.setSaldoRestante(120000L);

        when(prestamoDao.find(prestamoId)).thenReturn(prestamo);
        when(prestamoCalculator.calcularValorCuota(prestamo)).thenReturn(10000L);

        Prestamo result = prestamoService.pagarCuota(prestamoId);
        
        assertNotNull(result);
        assertEquals(110000L, result.getSaldoRestante()); // 120000 - 10000

        verify(prestamoDao, times(1)).find(prestamoId);
        verify(cuentaService, times(1)).pagarCuotaPrestamo(prestamo);
        verify(prestamoDao, times(1)).save(prestamo);
    }


    @Test
    void testPagarCuota_PrestamoNoEncontrado() {
        
        long prestamoId = 1L;
        when(prestamoDao.find(prestamoId)).thenReturn(null);

        PrestamoNoEncontradoException exception = assertThrows(PrestamoNoEncontradoException.class,
                () -> prestamoService.pagarCuota(prestamoId));

        assertEquals("Error: El préstamo no existe", exception.getMessage());
    }



    @Test
    void testPagarCuota_PrestamoYaPagado() {
        
        long prestamoId = 1L;
        Prestamo prestamo = mock(Prestamo.class);
        when(prestamoDao.find(prestamoId)).thenReturn(prestamo);
        when(prestamo.estaPagado()).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> prestamoService.pagarCuota(prestamoId));

        assertEquals("Error: El préstamo ya está completamente pagado", exception.getMessage());
    }



    // -------------------------------
    // Test: getPrestamosByCliente
    // -------------------------------
    
    @Test
    void testGetPrestamosByCliente_Success() throws ClienteNoEncontradoException {
        long dni = 12345678L;
        List<Prestamo> prestamos = List.of(new Prestamo());

        when(prestamoDao.getPrestamosByCliente(dni)).thenReturn(prestamos);

        List<Prestamo> result = prestamoService.getPrestamosByCliente(dni);

        assertEquals(1, result.size());
        verify(prestamoDao, times(1)).getPrestamosByCliente(dni);
    }
}
