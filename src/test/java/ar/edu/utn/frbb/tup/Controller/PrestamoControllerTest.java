package ar.edu.utn.frbb.tup.Controller;

import ar.edu.utn.frbb.tup.controller.PrestamoController;
import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.PrestamoNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.service.PrestamoService;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrestamoControllerTest {

    @InjectMocks
    private PrestamoController prestamoController;

    @Mock
    private PrestamoValidator prestamoValidator;

    @Mock
    private PrestamoService prestamoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    // -------------------------------
    // Test:  GENERAL SUCCESS
    // -------------------------------

    @Test
    void testCrearPrestamo_Success() throws Exception {
        // Datos de entrada
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 50000L, 24, "P");
        PrestamoResultado resultadoEsperado = new PrestamoResultado();

        // Simular validación y lógica de negocio
        doNothing().when(prestamoValidator).validate(prestamoDto);
        when(prestamoService.solicitarPrestamo(prestamoDto)).thenReturn(resultadoEsperado);

        // Ejecutar
        PrestamoResultado resultado = prestamoController.crearPrestamo(prestamoDto);

        // Verificar
        assertNotNull(resultado);
        assertEquals(resultadoEsperado, resultado);
        verify(prestamoValidator, times(1)).validate(prestamoDto);
        verify(prestamoService, times(1)).solicitarPrestamo(prestamoDto);
    }

    // -------------------------------
    // Test:  GENERAL CAMPOS NO VACIOS
    // -------------------------------

    @Test
    void testCrearPrestamo_CampoVacioException() throws Exception {
        // Datos inválidos
        PrestamoDto prestamoDto = new PrestamoDto();

        // Simular validación fallida
        doThrow(new CampoVacioException("Error: Ingrese un número de cliente válido"))
                .when(prestamoValidator).validate(prestamoDto);

        // Verificar excepción
        CampoVacioException exception = assertThrows(CampoVacioException.class,
                () -> prestamoController.crearPrestamo(prestamoDto));

        assertEquals("Error: Ingrese un número de cliente válido", exception.getMessage());
        verify(prestamoValidator, times(1)).validate(prestamoDto);
        verify(prestamoService, never()).solicitarPrestamo(prestamoDto);
    }

    // -------------------------------
    // Test:  GENERAL MONEDA NO SOPORTADA
    // -------------------------------

    @Test
    void testCrearPrestamo_TipoMonedaNoSoportadaException() throws Exception {
        // Datos inválidos
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 50000L, 24, "X");

        // Simular validación fallida
        doThrow(new TipoMonedaNoSoportadaException("Error: El tipo de moneda no es correcto"))
                .when(prestamoValidator).validate(prestamoDto);

        // Verificar excepción
        TipoMonedaNoSoportadaException exception = assertThrows(TipoMonedaNoSoportadaException.class,
                () -> prestamoController.crearPrestamo(prestamoDto));

        assertEquals("Error: El tipo de moneda no es correcto", exception.getMessage());
        verify(prestamoValidator, times(1)).validate(prestamoDto);
        verify(prestamoService, never()).solicitarPrestamo(prestamoDto);
    }

    // -------------------------------
    // Test:  GENERAL BUSCAR PRÉSTAMOS POR DNI SUCCESS
    // -------------------------------

    @Test
    void testBuscarPrestamoPorDni_Success() throws Exception {
        long dni = 12345678L;
        List<Prestamo> prestamos = Collections.singletonList(new Prestamo());

        when(prestamoService.getPrestamosByCliente(dni)).thenReturn(prestamos);

        // Ejecutar
        List<Prestamo> resultado = prestamoController.buscarPrestamoPorDni(dni);

        // Verificar
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(prestamoService, times(1)).getPrestamosByCliente(dni);
    }

    // -------------------------------
    // Test:  GENERAL BUSCAR PRÉSTAMOS POR DNI CLIENTE NO ENCONTRADO
    // -------------------------------

    @Test
    void testBuscarPrestamoPorDni_ClienteNoEncontrado() throws Exception {
        long dni = 12345678L;

        when(prestamoService.getPrestamosByCliente(dni))
                .thenThrow(new ClienteNoEncontradoException("Error: Cliente no encontrado"));

        ClienteNoEncontradoException exception = assertThrows(ClienteNoEncontradoException.class,
                () -> prestamoController.buscarPrestamoPorDni(dni));

        assertEquals("Error: Cliente no encontrado", exception.getMessage());
        verify(prestamoService, times(1)).getPrestamosByCliente(dni);
    }

    // -------------------------------
    // Test:  GENERAL PAGAR CUOTA SUCCESS
    // -------------------------------
    @Test
    void testPagarCuota_Success() throws Exception {
        long id = 1L;
        Prestamo prestamo = new Prestamo();

        when(prestamoService.pagarCuota(id)).thenReturn(prestamo);

        // Ejecutar
        Prestamo resultado = prestamoController.pagarCuota(id);

        // Verificar
        assertNotNull(resultado);
        verify(prestamoService, times(1)).pagarCuota(id);
    }

    // -------------------------------
    // Test:  GENERAL PAGAR CUOTA PRESTAMO NO ENCONTRADO
    // -------------------------------
    @Test
    void testPagarCuota_PrestamoNoEncontrado() throws Exception {
        long id = 1L;

        when(prestamoService.pagarCuota(id))
                .thenThrow(new PrestamoNoEncontradoException("Error: Préstamo no encontrado"));

        PrestamoNoEncontradoException exception = assertThrows(PrestamoNoEncontradoException.class,
                () -> prestamoController.pagarCuota(id));

        assertEquals("Error: Préstamo no encontrado", exception.getMessage());
        verify(prestamoService, times(1)).pagarCuota(id);
    }
}
