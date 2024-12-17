package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.enumModels.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    ClienteDao clienteDao;

    // INICIALIZA EL SERVICIO DE CLIENTE CON SU DAO
    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    // CREA UN NUEVO CLIENTE Y LO GUARDA EN LA BASE DE DATOS (validaciones de negocio)
    public Cliente CrearCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {
        Cliente cliente = new Cliente(clienteDto);
        if (clienteDao.find(cliente.getDni(), false) != null) {
            throw new ClienteAlreadyExistsException("Error: Ya existe un cliente con DNI " + cliente.getDni());
        }
        if (cliente.getEdad() < 18) {
            throw new ClienteMenorDeEdadException("Error: El cliente debe ser mayor a 18 años");
        }
        clienteDao.save(cliente);//Guarda el cliente en la base de datos
        return cliente;
    }
    
    // AGREGA UNA CUENTA A UN CLIENTE EXISTENTE
    public void agregarCuenta(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistsException, ClienteNoEncontradoException {
        Cliente titular = buscarClientePorDni(dniTitular);
        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("Error: El cliente ya posee una cuenta de ese tipo y moneda.");
        }
        cuenta.setTitular(titular.getDni());
        titular.addCuenta(cuenta); // Añade la cuenta al cliente.
        clienteDao.save(titular);
    }

    // BUSCA UN CLIENTE POR SU DNI
    public Cliente buscarClientePorDni(long dni) throws ClienteNoEncontradoException {
        Cliente cliente = clienteDao.find(dni, true);
        if(cliente == null) {
            throw new ClienteNoEncontradoException("Error: El cliente no existe");
        }
        return cliente;
    }
       public List<Cliente> buscarTodosLosClientes() {
        return clienteDao.findAll();
    }

    // AGREGA UN PRESTAMO A UN CLIENTE EXISTENTE
    public void agregarPrestamo(Prestamo prestamo, long dniTitular) throws ClienteNoEncontradoException, CuentaNoEncontradaException {
        Cliente titular = buscarClientePorDni(dniTitular);
        if (!titular.tieneCuentaMoneda(prestamo.getMoneda())) {
            throw new CuentaNoEncontradaException("Error: El cliente no posee una cuenta de esa moneda.");
        }
        titular.addPrestamo(prestamo); // Se añade el préstamo al cliente.
        clienteDao.save(titular); // Guarda el cliente actualizado 
    }
    
    //VALIDACIONES PARA CUENTA SERVICE (trae la validacion de si tiene cuentas del mismo tipo y moneda)
    public boolean tieneCuentaDelMismoTipo(long dniTitular, TipoCuenta tipoCuenta, TipoMoneda moneda) throws ClienteNoEncontradoException {
        Cliente cliente = buscarClientePorDni(dniTitular);
        return cliente.tieneCuenta(tipoCuenta, moneda);
    }

}


