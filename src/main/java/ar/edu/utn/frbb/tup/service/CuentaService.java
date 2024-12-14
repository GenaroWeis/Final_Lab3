package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaNoSoportadaException;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CuentaService {
   
    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    ClienteService clienteService;

    //CREAR CUENTA (dardealtacueta)
    public Cuenta CrearCuenta(CuentaDto cuentaDto) throws CuentaAlreadyExistsException, TipoCuentaNoSoportadaException, TipoCuentaAlreadyExistsException, ClienteNoEncontradoException {
        Cuenta cuenta = new Cuenta(cuentaDto);
        verificarCuentaExistente(cuenta);
        verificarTipoCuentaSoportada(cuenta);
        verificarClienteYaTieneTipoCuenta(cuentaDto.getDniTitular(), cuenta);
        clienteService.agregarCuenta(cuenta, cuentaDto.getDniTitular());
        cuentaDao.save(cuenta);
        return cuenta;
    }

    //VERIFICACIONES PARA CREAR CUENTA
    private void verificarCuentaExistente(Cuenta cuenta) throws CuentaAlreadyExistsException {
        if (cuentaDao.find(cuenta.getNumeroCuenta()) != null) {
            throw new CuentaAlreadyExistsException("La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }
    }
    
    private void verificarTipoCuentaSoportada(Cuenta cuenta) throws TipoCuentaNoSoportadaException {
        if (!tipoCuentaEstaSoportada(cuenta)) {//la creo abajo
            throw new TipoCuentaNoSoportadaException(
                "El tipo de cuenta " + cuenta.getTipoCuenta() + " en " + cuenta.getMoneda() + " no está soportado."
            );
        }
    }

    private void verificarClienteYaTieneTipoCuenta(long dniTitular, Cuenta cuenta) throws TipoCuentaAlreadyExistsException, ClienteNoEncontradoException{
        if (clienteService.tieneCuentaDelMismoTipo(dniTitular, cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya tiene una cuenta del tipo " + cuenta.getTipoCuenta() + " en " + cuenta.getMoneda());
        }
    }
    //acá. TIPO DE CUENTA ESTA SOPORTADO CON EL TIPO DE MONEDA
    public boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        switch (cuenta.getTipoCuenta()) {
            case CUENTA_CORRIENTE:
                return cuenta.getMoneda() == TipoMoneda.PESOS;
            case CAJA_AHORRO:
                return cuenta.getMoneda() == TipoMoneda.PESOS || cuenta.getMoneda() == TipoMoneda.DOLARES;
            default:
                return false;
        }
    } 

    //BUSCAR CUENTA POR ID
    public Cuenta find(long id) throws CuentaNoEncontradaException {
        Cuenta cuenta = cuentaDao.find(id);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("La cuenta no existe");
        }
        return cuenta; 
    }
}

