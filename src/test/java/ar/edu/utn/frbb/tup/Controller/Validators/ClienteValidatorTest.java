package ar.edu.utn.frbb.tup.Controller.Validators;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.TipoPersonaNoAceptadoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteValidatorTest {

    private ClienteValidator clienteValidator;

    @BeforeEach
    void setUp() {
        clienteValidator = new ClienteValidator();
    }



    // -------------------------------
    // Test: VALIDACION GENERAL SUCCESS
    // -------------------------------
    
    @Test
    void testValidate_Success() throws CampoVacioException, TipoPersonaNoAceptadoException {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");
        clienteDto.setDni(46613030);

        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }



    // -------------------------------
    // Test: VALIDACION GENERAL CAMPOS NO VACIOS
    // -------------------------------

    @Test
    void testValidateDatosCompletos_NombreVacio() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validate(clienteDto));//guardas en exception la excepcion esperada para despues manejarlo

        assertEquals("Error: Ingrese un nombre", exception.getMessage());//por ejemplo con el mensaje
    }

    @Test
    void testValidateDatosCompletos_ApellidoVacio() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: Ingrese un apellido", exception.getMessage());
    }

    
    @Test
    void testValidateDatosCompletos_ApellidoNull() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido(null);
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: Ingrese un apellido", exception.getMessage());
    }

    @Test
    void testValidateDatosCompletos_FechaNacimientoVacia() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: Ingrese una fecha de nacimiento", exception.getMessage());
    }

    @Test
    void testValidateDatosCompletos_FechaNacimientoNull() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento(null);
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: Ingrese una fecha de nacimiento", exception.getMessage());
    }

    @Test
    void testValidateDatosCompletos_BancoVacio() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("");
        clienteDto.setTipoPersona("F");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: Ingrese un banco", exception.getMessage());
    }

    @Test
    void testValidateDatosCompletos_BancoNull() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco(null);
        clienteDto.setTipoPersona("F");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: Ingrese un banco", exception.getMessage());
    }

    @Test
    void testValidateDatosCompletos_TipoPersonaVacio() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("");

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: Ingrese un tipo de persona", exception.getMessage());
    }

    @Test
    void testValidateDatosCompletos_TipoPersonaNull() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona(null);

        CampoVacioException exception = assertThrows(CampoVacioException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: Ingrese un tipo de persona", exception.getMessage());
    }



    // -------------------------------
    // Test: VALIDACION GENERAL TIPO PERSONA
    // -------------------------------

    @Test
    void testValidateTipoPersona_TipoInvalido() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("X");

        TipoPersonaNoAceptadoException exception = assertThrows(TipoPersonaNoAceptadoException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: El tipo de persona no es correcto", exception.getMessage());
    }

    @Test
    void testValidateTipoPersona_TipoValidoFisica() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");
        clienteDto.setDni(46613030);

        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    @Test
    void testValidateTipoPersona_TipoValidoJuridica() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Empresa");
        clienteDto.setApellido("Test");
        clienteDto.setFechaNacimiento("2000-01-01");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("J");
        clienteDto.setDni(46613030);

        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }

    

    // -------------------------------
    // Test: VALIDACION FECHA NACIMIENTO
    // -------------------------------

    @Test
    void testValidateFechaNacimiento_FormatoErroneo() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("1990-15-30"); // Formato inválido
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");
        clienteDto.setDni(46613030);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: el formato de fecha es erroneo", exception.getMessage());
    }

   

    // -------------------------------
    // Test: VALIDACION DNI
    // -------------------------------

    @Test
    void testValidateDni_RangoInvalido_Bajo() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");
        clienteDto.setDni(9999999L); // Menor que el mínimo permitido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: El DNI debe tener 8 dígitos y estar entre 10.000.000 y 99.999.999.", exception.getMessage());
    }

    @Test
    void testValidateDni_RangoInvalido_Alto() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");
        clienteDto.setDni(100000000L); // Mayor que el máximo permitido

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> clienteValidator.validate(clienteDto));

        assertEquals("Error: El DNI debe tener 8 dígitos y estar entre 10.000.000 y 99.999.999.", exception.getMessage());
    }

    @Test
    void testValidateDni_RangoValido() {
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setBanco("Banco Test");
        clienteDto.setTipoPersona("F");
        clienteDto.setDni(46613030); // Dentro del rango permitido

        assertDoesNotThrow(() -> clienteValidator.validate(clienteDto));
    }
}
