package ar.edu.utn.frbb.tup.controller.validator;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoMonedaNoSoportadaException;

public class CuentaValidator {// en base al validate de cliente
    
    //VALIDACION GENERAL (valida todos los campos)
    public void validate(CuentaDto cuentaDto) throws TipoCuentaNoSoportadaException, TipoMonedaNoSoportadaException, CampoVacioException {
        validateDatosCompletos(cuentaDto);
        validateTipoCuenta(cuentaDto);
        validateMoneda(cuentaDto);
    }

    //VALIDACIONES CAMPOS NO VACIOS
    public void validateDatosCompletos(CuentaDto cuentaDto) throws CampoVacioException {
        if (cuentaDto.getTipoCuenta() == null || cuentaDto.getTipoCuenta().isEmpty()) { 
            throw new CampoVacioException("Error: Ingrese un tipo de cuenta ");
        }
        if (cuentaDto.getMoneda() == null || cuentaDto.getMoneda().isEmpty()) { 
            throw new CampoVacioException("Error: Ingrese una moneda");
        }
        if (cuentaDto.getDniTitular() == 0) { 
            throw new CampoVacioException("Error: Ingrese un dni");
        }
    }

    //VALIDACIONES TIPO DE CUENTA
    public void validateTipoCuenta(CuentaDto cuentaDto) throws TipoCuentaNoSoportadaException {
        if (!"C".equals(cuentaDto.getTipoCuenta()) && !"A".equals(cuentaDto.getTipoCuenta())) {
            throw new TipoCuentaNoSoportadaException("Error: El tipo de cuenta no es correcto");
        }
    }
    
    //VALIDACIONES TIPO DE MONEDA
    public void validateMoneda(CuentaDto cuentaDto) {
        if (!"P".equals(cuentaDto.getMoneda()) && !"D".equals(cuentaDto.getMoneda())) {
            throw new IllegalArgumentException("Error: El tipo de moneda no es correcto");
        }
    }
  
}
