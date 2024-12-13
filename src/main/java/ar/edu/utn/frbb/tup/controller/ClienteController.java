package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.TipoPersonaNoAceptadoException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.service.ClienteService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//no usas runtimeexceptions, asi que tenes que declara con throws acordate

@RestController//te transforma la clase en una api rest (get, post)
@RequestMapping("/cliente")//rutas
public class ClienteController {

    //inyecta dependencias
    @Autowired
    private ClienteService clienteService;
    @Autowired
    private ClienteValidator clienteValidator;

    @PostMapping//maneja solicitudes post
    public Cliente crearCliente(@RequestBody ClienteDto clienteDto) throws ClienteMenorDeEdadException, ClienteAlreadyExistsException, CampoVacioException, TipoPersonaNoAceptadoException {
        clienteValidator.validate(clienteDto);
        return clienteService.darDeAltaCliente(clienteDto);
    }

    @GetMapping("/{dni}")//maneja solicitudes get
    public Cliente buscarClientePorDni(@PathVariable long dni) throws ClienteNoEncontradoException {
        return clienteService.buscarClientePorDni(dni);
    }

    @GetMapping("/all")//maneja solicitudes get
    public List<Cliente> buscarTodosLosClientes() {
        return clienteService.buscarTodosLosClientes();
    }
}