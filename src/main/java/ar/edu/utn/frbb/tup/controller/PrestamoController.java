package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.controller.validator.PrestamoValidator;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.exception.PrestamoNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoMonedaNoSoportadaException;
import ar.edu.utn.frbb.tup.service.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/prestamo")
public class PrestamoController {

    //injectar dependencias
    @Autowired
    private PrestamoValidator prestamoValidator;

    @Autowired
    private PrestamoService prestamoService;

    // POST: CREAR PRESTAMO
    @PostMapping
    public PrestamoResultado crearPrestamo(@RequestBody PrestamoDto prestamoDto) throws CuentaNoEncontradaException, ClienteNoEncontradoException, TipoMonedaNoSoportadaException, CampoVacioException {
        prestamoValidator.validate(prestamoDto); 
        PrestamoResultado prestamoResultado = prestamoService.solicitarPrestamo(prestamoDto);
        return prestamoResultado;
    }

    // GET: BUSCAR PRESTAMO POR DNI
    @GetMapping("/{dni}")
    public List<Prestamo> buscarPrestamoPorDni(@PathVariable long dni) throws ClienteNoEncontradoException {
        return prestamoService.getPrestamosByCliente(dni);
    }

    // POST: PAGAR CUOTA POR ID
    @PostMapping("/{id}")
    public Prestamo pagarCuota(@PathVariable long id) throws NoAlcanzaException, PrestamoNoEncontradoException, CuentaNoEncontradaException {
        return prestamoService.pagarCuota(id);
    }
}
