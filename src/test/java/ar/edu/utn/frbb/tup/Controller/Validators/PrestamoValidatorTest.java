package ar.edu.utn.frbb.tup.Controller.Validators;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoMonedaNoSoportadaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PrestamoValidatorTest {

    private PrestamoValidator prestamoValidator;

    @BeforeEach
    void setUp() {
        prestamoValidator = new PrestamoValidator();
    }


   
    // -------------------------------
    // Test: VALIDACION GENERAL SUCCESS
    // -------------------------------

    @Test
    void testValidate_Success() {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 50000L, 24, "P");

        assertDoesNotThrow(() -> prestamoValidator.validate(prestamoDto));
    }

    // -------------------------------
    // Test: VALIDACION NUMERO CLIENTE INVALIDO
    // -------------------------------

    @Test
    void testValidateDatosCompletos_NumeroClienteInvalido() {
        PrestamoDto prestamoDto = new PrestamoDto(0, 50000L, 24, "P");

        CampoVacioException exception = assertThrows(CampoVacioException.class,
                () -> prestamoValidator.validate(prestamoDto));

        assertEquals("Error: Ingrese un número de cliente válido", exception.getMessage());
    }

    // -------------------------------
    // Test: VALIDACION PLAZO INVALIDO
    // -------------------------------

    @Test
    void testValidateDatosCompletos_PlazoMesesInvalido() {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 50000L, 0, "P");

        CampoVacioException exception = assertThrows(CampoVacioException.class,
                () -> prestamoValidator.validate(prestamoDto));

        assertEquals("Error: Ingrese un plazo en meses válido", exception.getMessage());
    }

    // -------------------------------
    // Test: VALIDACION MONTO INVALIDO
    // -------------------------------
    
    @Test
    void testValidateDatosCompletos_MontoInvalido() {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 0, 24, "P");

        CampoVacioException exception = assertThrows(CampoVacioException.class,
                () -> prestamoValidator.validate(prestamoDto));

        assertEquals("Error: Ingrese un monto de préstamo válido", exception.getMessage());
    }

    // -------------------------------
    // Test: VALIDACION MONEDA
    // -------------------------------

    // TEST: Moneda nula
    @Test
    void testValidateDatosCompletos_MonedaNula() {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 50000L, 24, null);

        CampoVacioException exception = assertThrows(CampoVacioException.class,
                () -> prestamoValidator.validate(prestamoDto));

        assertEquals("Error: Ingrese una moneda válida", exception.getMessage());
    }

    // TEST: Moneda vacía
    @Test
    void testValidateDatosCompletos_MonedaVacia() {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 50000L, 24, "");

        CampoVacioException exception = assertThrows(CampoVacioException.class,
                () -> prestamoValidator.validate(prestamoDto));

        assertEquals("Error: Ingrese una moneda válida", exception.getMessage());
    }

    // TEST: Tipo de moneda no soportada
    @Test
    void testValidateMoneda_TipoMonedaNoSoportada() {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 50000L, 24, "E"); // "E" no es válido

        TipoMonedaNoSoportadaException exception = assertThrows(TipoMonedaNoSoportadaException.class,
                () -> prestamoValidator.validate(prestamoDto));

        assertEquals("Error: El tipo de moneda no es correcto", exception.getMessage());
    }

    // TEST: Tipo de moneda soportada (Pesos - "P")
    @Test
    void testValidateMoneda_TipoMonedaPesos() {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 50000L, 24, "P");

        assertDoesNotThrow(() -> prestamoValidator.validate(prestamoDto));
    }

    // TEST: Tipo de moneda soportada (Dólares - "D")
    @Test
    void testValidateMoneda_TipoMonedaDolares() {
        PrestamoDto prestamoDto = new PrestamoDto(12345678L, 50000L, 24, "D");

        assertDoesNotThrow(() -> prestamoValidator.validate(prestamoDto));
    }
}
