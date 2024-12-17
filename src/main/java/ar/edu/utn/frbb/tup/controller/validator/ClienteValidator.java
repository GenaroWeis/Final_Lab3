package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.TipoPersonaNoAceptadoException;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class ClienteValidator {

    //acordate que no usas runtimeexceptions, asi que tenes que declara con throws cuando son verificadas

    //VALIDACION GENERAL (valida todos los campos)
    public void validate(ClienteDto clienteDto) throws CampoVacioException, TipoPersonaNoAceptadoException {
        validateDatosCompletos(clienteDto); //no vacios
        validateTipoPersona(clienteDto);//tipo persona
        validateFechaNacimiento(clienteDto);//fecha de nacimiento
        validateDni(clienteDto); //dni
    }

    //VALIDACIONES CAMPOS NO VACIOS
    private void validateDatosCompletos(ClienteDto clienteDto) throws CampoVacioException {
        if (clienteDto.getNombre() == null || clienteDto.getNombre().isEmpty()) {
            throw new CampoVacioException("Error: Ingrese un nombre");
        }
        if (clienteDto.getApellido() == null || clienteDto.getApellido().isEmpty()) {
            throw new CampoVacioException("Error: Ingrese un apellido");
        }
        if (clienteDto.getFechaNacimiento() == null || clienteDto.getFechaNacimiento().isEmpty()) {
            throw new CampoVacioException("Error: Ingrese una fecha de nacimiento");
        }
        if (clienteDto.getBanco() == null || clienteDto.getBanco().isEmpty()) {
            throw new CampoVacioException("Error: Ingrese un banco");
        }
        if (clienteDto.getTipoPersona() == null || clienteDto.getTipoPersona().isEmpty()) {
            throw new CampoVacioException("Error: Ingrese un tipo de persona");
        }
    }

    //VALIDACION TIPO PERSONA
    private void validateTipoPersona(ClienteDto clienteDto)  throws TipoPersonaNoAceptadoException  {
        if (!"F".equals(clienteDto.getTipoPersona()) && !"J".equals(clienteDto.getTipoPersona())) {
            throw new TipoPersonaNoAceptadoException("Error: El tipo de persona no es correcto");
        }
    }

    //VALIDACION FECHA DE NACIMIENTO
    private void validateFechaNacimiento(ClienteDto clienteDto) {
        try {
            LocalDate.parse(clienteDto.getFechaNacimiento());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error: el formato de fecha es erroneo");
        }
    }
    
   // VALIDACION DNI CORRECTO
    private void validateDni(ClienteDto clienteDto) {
        long dni = clienteDto.getDni();
        if (dni < 10_000_000 || dni > 99_999_999) { // Validar rango
            throw new IllegalArgumentException("Error: El DNI debe tener 8 dígitos y estar entre 10.000.000 y 99.999.999.");
        }
    }
}
