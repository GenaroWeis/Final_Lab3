package ar.edu.utn.frbb.tup.Controller.Validators;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaNoSoportadaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CuentaValidatorTest {

    private CuentaValidator cuentaValidator;

    @BeforeEach
    void setUp() {
        cuentaValidator = new CuentaValidator();
    }



    // -------------------------------
    // Test: VALIDACION GENERAL SUCCESS
    // -------------------------------
    @Test
    void testValidate_Success() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");  // Cuenta Corriente
        cuentaDto.setMoneda("P");      // Pesos
        cuentaDto.setDniTitular(46613030);

        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto));
    }



    // -------------------------------
    // Test: VALIDACION GENERAL CAMPOS VACIOS
    // -------------------------------

    @Test
    void testValidateDatosCompletos_TipoCuentaVacio() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("");   
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(46613030);

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("Error: Ingrese un tipo de cuenta ", exception.getMessage());
    }

    @Test
    void testValidateDatosCompletos_MonedaVacia() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("");   // Moneda vacía
        cuentaDto.setDniTitular(46613030);

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("Error: Ingrese una moneda", exception.getMessage());
    }

    @Test
    void testValidateDatosCompletos_MonedaNull() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda(null);   
        cuentaDto.setDniTitular(46613030);

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("Error: Ingrese una moneda", exception.getMessage());
    }

    @Test
    void testValidateDatosCompletos_DniTitularVacio() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(0);   // DNI vacío (0)

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("Error: Ingrese un dni", exception.getMessage());
    }



    // -------------------------------
    // Test: VALIDACION GENERAL TIPO CUENTA
    // -------------------------------

    @Test
    void testValidateTipoCuenta_TipoCuentaInvalido() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("Z");  // Tipo de cuenta no soportado
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(46613030);

        TipoCuentaNoSoportadaException exception = assertThrows(TipoCuentaNoSoportadaException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("Error: El tipo de cuenta no es correcto", exception.getMessage());
    }

    @Test
    void testValidateTipoCuenta_TipoCuentaValido_Success() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");  
        cuentaDto.setMoneda("P");      
        cuentaDto.setDniTitular(46613030);
    
        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto), 
                "No debería lanzarse ninguna excepción para un tipo de cuenta y moneda válidos");
    }
    


    // -------------------------------
    // Test: VALIDACION GENERAL MONEDA
    // -------------------------------

    @Test
    void testValidateMoneda_TipoMonedaInvalida() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("X");  
        cuentaDto.setDniTitular(46613030);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("Error: El tipo de moneda no es correcto", exception.getMessage());
    }

    @Test
    void testValidateTipoMoneda_TipoMonedaValido_Success() {
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");  
        cuentaDto.setMoneda("P");      
        cuentaDto.setDniTitular(46613030);
    
        assertDoesNotThrow(() -> cuentaValidator.validate(cuentaDto), 
                "No debería lanzarse ninguna excepción para un tipo de cuenta y moneda válidos");
    }
    


    // -------------------------------
    // Test: VALIDACION GENERAL EMPTY (ningun dato)
    // -------------------------------

    @Test
    void testValidateDatosCompletos_TodoVacio() {
        CuentaDto cuentaDto = new CuentaDto(); 

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> cuentaValidator.validate(cuentaDto));

        assertEquals("Error: Ingrese un tipo de cuenta ", exception.getMessage());
    }
}