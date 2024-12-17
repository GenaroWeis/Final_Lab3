package ar.edu.utn.frbb.tup.controller.validator;

import org.springframework.stereotype.Component;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoMonedaNoSoportadaException;

@Component
public class PrestamoValidator {
  
    //VALIDACION GENERAL (valida todos los campos)
    public void validate(PrestamoDto prestamoDto) throws TipoMonedaNoSoportadaException, CampoVacioException {
        validateDatosCompletos(prestamoDto);
        validateMoneda(prestamoDto);
    }

    //VALIDACIONES CAMPOS NO VACIOS
    public void validateDatosCompletos(PrestamoDto prestamoDto) throws CampoVacioException {
        if (prestamoDto.getNumeroCliente() == 0) {
            throw new CampoVacioException("Error: Ingrese un número de cliente válido");
        }
        if (prestamoDto.getPlazoMeses() <= 0) {
            throw new CampoVacioException("Error: Ingrese un plazo en meses válido");
        }
        if (prestamoDto.getMonto() <= 0) {
            throw new CampoVacioException("Error: Ingrese un monto de préstamo válido");
        }
        if (prestamoDto.getMoneda() == null || prestamoDto.getMoneda().isEmpty()) {
            throw new CampoVacioException("Error: Ingrese una moneda válida");
        }
    }

    //VALIDACIONES TIPO DE MONEDA
    public void validateMoneda(PrestamoDto prestamoDto) throws TipoMonedaNoSoportadaException {
        if ((!"P".equals(prestamoDto.getMoneda()) && !"D".equals(prestamoDto.getMoneda()))) {
            throw new TipoMonedaNoSoportadaException("Error: El tipo de moneda no es correcto");
        }
    }

}

