package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    ClienteDao clienteDao;

    public ClienteService(ClienteDao clienteDao) {
        this.clienteDao = clienteDao;
    }

    public Cliente darDeAltaCliente(ClienteDto clienteDto) throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {
        Cliente cliente = new Cliente(clienteDto);

        if (clienteDao.find(cliente.getDni(), false) != null) {
            throw new ClienteAlreadyExistsException("Ya existe un cliente con DNI " + cliente.getDni());
        }

        if (cliente.getEdad() < 18) {
            throw new ClienteMenorDeEdadException("El cliente debe ser mayor a 18 años");
        }

        clienteDao.save(cliente);
        return cliente;
    }

    public void agregarCuenta(Cuenta cuenta, long dniTitular) throws TipoCuentaAlreadyExistsException, ClienteNoEncontradoException {
        Cliente titular = buscarClientePorDni(dniTitular);
        cuenta.setTitular(titular.getDni());// pasar el dni
        if (titular.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de ese tipo y moneda");
        }
        if (titular.tieneCuentaMoneda(cuenta.getMoneda())) {
            throw new TipoCuentaAlreadyExistsException("El cliente ya posee una cuenta de esa moneda");
        }
        titular.addCuenta(cuenta);
        clienteDao.save(titular);
    }

  
    public Cliente buscarClientePorDni(long dni) throws ClienteNoEncontradoException {
        Cliente cliente = clienteDao.find(dni, true);
        if(cliente == null) {
            throw new ClienteNoEncontradoException("El cliente no existe");
        }
        return cliente;
    }

       public List<Cliente> buscarTodosLosClientes() {
        return clienteDao.findAll();
    }

}


