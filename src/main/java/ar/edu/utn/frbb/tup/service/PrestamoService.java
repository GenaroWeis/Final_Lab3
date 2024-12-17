package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.controller.dto.PrestamoDto;
import ar.edu.utn.frbb.tup.exception.NoAlcanzaException;
import ar.edu.utn.frbb.tup.exception.PrestamoNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.model.PrestamoPlanPago;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.PrestamoResultado;
import ar.edu.utn.frbb.tup.model.enumModels.PrestamoEstados;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.PrestamoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

//aclaracion: a veces cuando llamas a metodos en otros servicios es para traer los datos con las validaciones hechas
@Service
public class PrestamoService {

    //Inyectar dependencias
    @Autowired
    private ClienteService clienteService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private PrestamoDao prestamoDao;

    @Autowired
    private CreditCheckService creditCheckService;

    @Autowired
    private PrestamoCalculator prestamoCalculator;

    //SOLICITAR UN PRÉSTAMO
    public PrestamoResultado solicitarPrestamo(PrestamoDto prestamoDto) throws CuentaNoEncontradaException, ClienteNoEncontradoException {
    validarEntrada(prestamoDto);//validacion (abajo)
    if (!creditCheckService.verificarEstadoCrediticio(prestamoDto.getNumeroCliente())) {// Verificar la calificación crediticia del cliente.
        return new PrestamoResultado(
            PrestamoEstados.RECHAZADO, "El cliente no tiene una calificación crediticia suficiente para un préstamo.",Collections.emptyList());// No hay plan de pagos en caso de rechazo.
    }
    // Crear préstamo y calcular valores
    Prestamo prestamo = new Prestamo(prestamoDto.getNumeroCliente(), prestamoDto.getPlazoMeses(), prestamoDto.getMonto(), TipoMoneda.fromString(prestamoDto.getMoneda()));
    prestamoCalculator.calcularValor(prestamo);
    clienteService.agregarPrestamo(prestamo, prestamoDto.getNumeroCliente());
    cuentaService.actualizarCuenta(prestamo); 
    prestamoDao.save(prestamo);
    // Generar plan de pagos
    List<PrestamoPlanPago> planPagos = prestamoCalculator.generarPlanPagos(prestamo);
    return new PrestamoResultado(PrestamoEstados.APROBADO, "Préstamo aprobado y acreditado en su cuenta.", planPagos);
    }

    //VALIDAR ENTRADA
    private void validarEntrada(PrestamoDto prestamoDto) {
        if (prestamoDto.getMonto() <= 0 || prestamoDto.getPlazoMeses() <= 0) {
            throw new IllegalArgumentException("Error: El monto y el plazo deben ser mayores a cero.");
        }
    }

    //MÉTODO PARA PAGAR UNA CUOTA DE UN PRÉSTAMO
    public Prestamo pagarCuota(long id) throws PrestamoNoEncontradoException, NoAlcanzaException, CuentaNoEncontradaException {
    Prestamo prestamo = prestamoDao.find(id);//buscas el prestamo por id
    //validaciones
    if (prestamo == null) {
        throw new PrestamoNoEncontradoException("Error: El préstamo no existe");
    }
    if (prestamo.estaPagado()) {
        throw new IllegalStateException("Error: El préstamo ya está completamente pagado");
    }
    long valorCuota = prestamoCalculator.calcularValorCuota(prestamo);
    cuentaService.pagarCuotaPrestamo(prestamo); 
    prestamo.setSaldoRestante(prestamo.getSaldoRestante() - valorCuota);
    prestamo.pagarCuota();
    prestamoDao.save(prestamo);//guardas los cambios
    return prestamo;
    }

    //MÉTODO PARA OBTENER LOS PRÉSTAMOS DE UN CLIENTE
    public List<Prestamo> getPrestamosByCliente(long dni) throws ClienteNoEncontradoException {
        clienteService.buscarClientePorDni(dni);//usa las validaciones de clienteService
        return prestamoDao.getPrestamosByCliente(dni); 
    }  

}
