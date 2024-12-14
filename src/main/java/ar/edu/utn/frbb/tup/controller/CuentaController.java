package ar.edu.utn.frbb.tup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import ar.edu.utn.frbb.tup.controller.validator.CuentaValidator;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaNoSoportadaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.service.CuentaService;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {// logica basada en cliente controller

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private CuentaValidator cuentaValidator;

    @PostMapping
    public Cuenta crearCuenta(@RequestBody CuentaDto cuentaDto) throws TipoCuentaNoSoportadaException, TipoMonedaNoSoportadaException, TipoCuentaAlreadyExistsException, ClienteNoEncontradoException, CuentaAlreadyExistsException, CampoVacioException {
        cuentaValidator.validate(cuentaDto);
        return cuentaService.CrearCuenta(cuentaDto);
    }

    @GetMapping("/{numeroCuenta}")
    public Cuenta buscarCuenta(@PathVariable long numeroCuenta) throws CuentaNoEncontradaException {
        return cuentaService.find(numeroCuenta);
    }

}
