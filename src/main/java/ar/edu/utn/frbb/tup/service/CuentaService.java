package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaNoSoportadaException;

import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CuentaService {
   
    //inyectar dependencias
    @Autowired
    private CuentaDao cuentaDao;

    @Autowired
    ClienteService clienteService;

    @Autowired
    private PrestamoCalculator prestamoCalculator;

    //CREAR CUENTA (dardealtacueta) (verificaciones modulares para expandirlo por las dudas)
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
            throw new CuentaAlreadyExistsException("Error: La cuenta " + cuenta.getNumeroCuenta() + " ya existe.");
        }
    }
    private void verificarTipoCuentaSoportada(Cuenta cuenta) throws TipoCuentaNoSoportadaException {
        if (!tipoCuentaEstaSoportada(cuenta)) {//la creo abajo
            throw new TipoCuentaNoSoportadaException("Error: El tipo de cuenta " + cuenta.getTipoCuenta() + " en " + cuenta.getMoneda() + " no está soportado.");
        }
    }
    private void verificarClienteYaTieneTipoCuenta(long dniTitular, Cuenta cuenta) throws TipoCuentaAlreadyExistsException, ClienteNoEncontradoException{
        if (clienteService.tieneCuentaDelMismoTipo(dniTitular, cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("Error: El cliente ya tiene una cuenta del tipo " + cuenta.getTipoCuenta() + " en " + cuenta.getMoneda());
        }
    }
    //acá. TIPO DE CUENTA ESTA SOPORTADO CON EL TIPO DE MONEDA
    public boolean tipoCuentaEstaSoportada(Cuenta cuenta) {
        switch (cuenta.getTipoCuenta()) {
            case CUENTA_CORRIENTE:
                return cuenta.getMoneda() == TipoMoneda.PESOS;//cc: solo pesos
            case CAJA_AHORRO:
                return cuenta.getMoneda() == TipoMoneda.PESOS || cuenta.getMoneda() == TipoMoneda.DOLARES;//ca: pesos o dolares
            default:
                return false;
        }
    } 

    //BUSCAR CUENTA POR ID
    public Cuenta find(long id) throws CuentaNoEncontradaException {
        Cuenta cuenta = cuentaDao.find(id);
        if (cuenta == null) {
            throw new CuentaNoEncontradaException("Error: La cuenta no existe");
        }
        return cuenta; 
    }

    // SUMA EL MONTO DEL PRESTAMO AL BALANCE DE LA CUENTA
    public void actualizarCuenta(Prestamo prestamo) throws CuentaNoEncontradaException {
        Cuenta cuenta = buscarPorMoneda(prestamo.getMoneda());
        long nuevoBalance = cuenta.getBalance() + prestamo.getMontoPedido(); // Refleja solo el monto solicitado.
        cuenta.setBalance(nuevoBalance);
        cuentaDao.save(cuenta);
    }

    // PAGA 1 CUOTA DEL PRESTAMO Y ACTUALIZA EL BALANCE DE LA CUENTA
    public void pagarCuotaPrestamo(Prestamo prestamo) throws NoAlcanzaException, CuentaNoEncontradaException {
        Cuenta cuenta = buscarPorMoneda(prestamo.getMoneda());
        long valorCuota = prestamoCalculator.calcularValorCuota(prestamo);
        if (cuenta.getBalance() < valorCuota) {
            throw new NoAlcanzaException("Error: No hay suficiente saldo en la cuenta");
        }
        cuenta.setBalance(cuenta.getBalance() - valorCuota);
        cuentaDao.save(cuenta);
    }

    // BUSCA UNA CUENTA POR MONEDA
    public Cuenta buscarPorMoneda(TipoMoneda moneda) throws CuentaNoEncontradaException {
    if (cuentaDao.buscarPorMoneda(moneda) == null) {
        throw new CuentaNoEncontradaException("Error: La cuenta no existe");
    }
    return cuentaDao.buscarPorMoneda(moneda);
    }

}

