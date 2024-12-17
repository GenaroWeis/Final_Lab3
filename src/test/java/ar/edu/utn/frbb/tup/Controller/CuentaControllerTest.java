package ar.edu.utn.frbb.tup.Controller;

import ar.edu.utn.frbb.tup.controller.CuentaController;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CuentaControllerTest {

    @InjectMocks
    private CuentaController cuentaController;

    @Mock
    private CuentaService cuentaService;

    @Mock
    private CuentaValidator cuentaValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    // -------------------------------
    // Test:  GENERAL CUENTA SUCCESS
    // -------------------------------

    @Test
    void testCrearCuenta_Success() throws Exception {
        // Datos de entrada
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(46613030);

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(1001L);

        // Simular es y creación
        doNothing().when(cuentaValidator).validate(cuentaDto);
        when(cuentaService.CrearCuenta(cuentaDto)).thenReturn(cuenta);

        // Invocar el método
        Cuenta result = cuentaController.crearCuenta(cuentaDto);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(1001L, result.getNumeroCuenta());
        verify(cuentaValidator, times(1)).validate(cuentaDto);
        verify(cuentaService, times(1)).CrearCuenta(cuentaDto);
    }

    // -------------------------------
    // Test:  GENERAL CAMPOS VACIOS
    // -------------------------------

    @Test
    void testCrearCuenta_CampoVacioException() throws Exception {
        // Datos de entrada inválidos
        CuentaDto cuentaDto = new CuentaDto();

        // Simular la excepción del validador
        doThrow(new CampoVacioException("Error: Ingrese un tipo de cuenta"))
                .when(cuentaValidator).validate(cuentaDto);

        // Verificar que lanza la excepción
        CampoVacioException exception = assertThrows(CampoVacioException.class, () -> cuentaController.crearCuenta(cuentaDto));
        assertEquals("Error: Ingrese un tipo de cuenta", exception.getMessage());
        verify(cuentaValidator, times(1)).validate(cuentaDto);
        verify(cuentaService, never()).CrearCuenta(cuentaDto);
    }

    // -------------------------------
    // Test:  GENERAL TIPO CUENTA NO SOPORTADA
    // -------------------------------

    @Test
    void testCrearCuenta_TipoCuentaNoSoportadaException() throws Exception {
        // Datos de entrada
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("X"); 
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(46613030);

        // Simular excepción del validador
        doThrow(new TipoCuentaNoSoportadaException("Error: El tipo de cuenta no es correcto"))
                .when(cuentaValidator).validate(cuentaDto);

        // Verificar que lanza la excepción
        TipoCuentaNoSoportadaException exception = assertThrows(TipoCuentaNoSoportadaException.class, () -> cuentaController.crearCuenta(cuentaDto));
        assertEquals("Error: El tipo de cuenta no es correcto", exception.getMessage());
        verify(cuentaValidator, times(1)).validate(cuentaDto);
        verify(cuentaService, never()).CrearCuenta(cuentaDto);
    }

    // -------------------------------
    // Test:  BUSQUEDA CUENTA POR NUMERO
    // -------------------------------
    
    @Test
    void testBuscarCuentaPorNumero_Success() throws Exception {
        // Simular cuenta existente
        long numeroCuenta = 1001L;
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(numeroCuenta);

        when(cuentaService.find(numeroCuenta)).thenReturn(cuenta);

        // Invocar el método
        Cuenta result = cuentaController.buscarCuenta(numeroCuenta);

        // Verificar resultados
        assertNotNull(result);
        assertEquals(numeroCuenta, result.getNumeroCuenta());
        verify(cuentaService, times(1)).find(numeroCuenta);
    }

    // -------------------------------
    // Test:  CUENTA NO ENCONTRADA
    // -------------------------------

    @Test
    void testBuscarCuentaPorNumero_CuentaNoEncontradaException() throws Exception {
        // Simular excepción de cuenta no encontrada
        long numeroCuenta = 1001L;

        when(cuentaService.find(numeroCuenta)).thenThrow(new CuentaNoEncontradaException("Error: La cuenta no existe"));

        // Verificar que lanza la excepción
        CuentaNoEncontradaException exception = assertThrows(CuentaNoEncontradaException.class, () -> cuentaController.buscarCuenta(numeroCuenta));
        assertEquals("Error: La cuenta no existe", exception.getMessage());
        verify(cuentaService, times(1)).find(numeroCuenta);
    }
}
