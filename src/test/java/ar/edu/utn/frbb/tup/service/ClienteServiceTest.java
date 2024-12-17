package ar.edu.utn.frbb.tup.service;

import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteMenorDeEdadException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.ClienteNoEncontradoException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.model.*;
import ar.edu.utn.frbb.tup.model.enumModels.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.ClienteDao;
import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    @InjectMocks
    private ClienteService clienteService;

    @Mock
    private ClienteDao clienteDao;

    @Mock
    private CuentaService cuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    // -------------------------------
    // Test: CrearCliente
    // -------------------------------

    @Test
    void testCrearCliente_Success() throws ClienteAlreadyExistsException, ClienteMenorDeEdadException {
        
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("2000-01-01");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Test");

        Cliente cliente = new Cliente(clienteDto);

        when(clienteDao.find(cliente.getDni(), false)).thenReturn(null); // Cliente no existe
        doNothing().when(clienteDao).save(any(Cliente.class));

       
        Cliente result = clienteService.CrearCliente(clienteDto);

        
        assertNotNull(result);
        assertEquals(12345678L, result.getDni());
        verify(clienteDao, times(1)).save(any(Cliente.class));
    }


    @Test
    void testCrearCliente_ClienteAlreadyExistsException() {
    
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento("2000-01-01");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Test");

        Cliente existingCliente = new Cliente(clienteDto); // Cliente ya existente

        when(clienteDao.find(clienteDto.getDni(), false)).thenReturn(existingCliente);


        ClienteAlreadyExistsException exception = assertThrows(ClienteAlreadyExistsException.class,
                () -> clienteService.CrearCliente(clienteDto));
        assertEquals("Error: Ya existe un cliente con DNI 12345678", exception.getMessage());

        // Verificar que save no se ejecutó
        verify(clienteDao, never()).save(any(Cliente.class));
    }


    @Test
    void testCrearCliente_ClienteMenorDeEdadException() {
        
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDni(12345678L);
        clienteDto.setNombre("Juan");
        clienteDto.setApellido("Perez");
        clienteDto.setFechaNacimiento(LocalDate.now().minusYears(17).toString()); // Menor de edad
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Test");

        when(clienteDao.find(clienteDto.getDni(), false)).thenReturn(null);

        
        ClienteMenorDeEdadException exception = assertThrows(ClienteMenorDeEdadException.class,
                () -> clienteService.CrearCliente(clienteDto));
        assertEquals("Error: El cliente debe ser mayor a 18 años", exception.getMessage());

        // Verificar que save no se ejecutó
        verify(clienteDao, never()).save(any(Cliente.class));
    }



    // -------------------------------
    // Test: agregarCuenta
    // -------------------------------

    @Test
    void testAgregarCuenta_Success() throws ClienteNoEncontradoException, TipoCuentaAlreadyExistsException {
        
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        when(clienteDao.find(dni, true)).thenReturn(cliente);
        doNothing().when(clienteDao).save(any(Cliente.class));

       
        clienteService.agregarCuenta(cuenta, dni);

        
        verify(clienteDao, times(1)).save(cliente);
        assertTrue(cliente.tieneCuenta(cuenta.getTipoCuenta(), cuenta.getMoneda()));
    }

    @Test
    void testAgregarCuenta_ClienteNoEncontradoException() {
        
        long dni = 12345678L;
        Cuenta cuenta = new Cuenta();

        when(clienteDao.find(dni, true)).thenReturn(null);

        
        ClienteNoEncontradoException exception = assertThrows(ClienteNoEncontradoException.class,
                () -> clienteService.agregarCuenta(cuenta, dni));
        assertEquals("Error: El cliente no existe", exception.getMessage());
    }

    @Test
    void testAgregarCuenta_TipoCuentaAlreadyExistsException() throws ClienteNoEncontradoException {
        
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);

        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CAJA_AHORRO);
        cuenta.setMoneda(TipoMoneda.PESOS);

        cliente.addCuenta(cuenta);

        when(clienteDao.find(dni, true)).thenReturn(cliente);

        
        TipoCuentaAlreadyExistsException exception = assertThrows(TipoCuentaAlreadyExistsException.class,
                () -> clienteService.agregarCuenta(cuenta, dni));
        assertEquals("Error: El cliente ya posee una cuenta de ese tipo y moneda.", exception.getMessage());
    }



    // -------------------------------
    // Test: buscarClientePorDni
    // -------------------------------

    @Test
    void testBuscarClientePorDni_Success() throws ClienteNoEncontradoException {
        
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);

        when(clienteDao.find(dni, true)).thenReturn(cliente);

       
        Cliente result = clienteService.buscarClientePorDni(dni);

        
        assertNotNull(result);
        assertEquals(dni, result.getDni());
    }

    @Test
    void testBuscarClientePorDni_ClienteNoEncontradoException() {
        
        long dni = 12345678L;

        when(clienteDao.find(dni, true)).thenReturn(null);

        
        ClienteNoEncontradoException exception = assertThrows(ClienteNoEncontradoException.class,
                () -> clienteService.buscarClientePorDni(dni));
        assertEquals("Error: El cliente no existe", exception.getMessage());
    }



    // -------------------------------
    // Test: agregarPrestamo
    // -------------------------------

    @Test
    void testAgregarPrestamo_Success() throws ClienteNoEncontradoException, CuentaNoEncontradaException {
        
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);

        Prestamo prestamo = new Prestamo();
        prestamo.setMoneda(TipoMoneda.PESOS);

        Cuenta cuenta = new Cuenta();
        cuenta.setMoneda(TipoMoneda.PESOS);
        cliente.addCuenta(cuenta);

        when(clienteDao.find(dni, true)).thenReturn(cliente);
        doNothing().when(clienteDao).save(any());

       
        clienteService.agregarPrestamo(prestamo, dni);

        
        verify(clienteDao, times(1)).save(cliente);
        assertTrue(cliente.tieneCuentaMoneda(TipoMoneda.PESOS));
    }

    @Test
    void testAgregarPrestamo_CuentaNoEncontradaException() throws ClienteNoEncontradoException {
        
        long dni = 12345678L;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);

        Prestamo prestamo = new Prestamo();
        prestamo.setMoneda(TipoMoneda.DOLARES);

        when(clienteDao.find(dni, true)).thenReturn(cliente);

        
        CuentaNoEncontradaException exception = assertThrows(CuentaNoEncontradaException.class,
                () -> clienteService.agregarPrestamo(prestamo, dni));
        assertEquals("Error: El cliente no posee una cuenta de esa moneda.", exception.getMessage());
    }



    // -------------------------------
    // Test: TipoCuentaEstaSoportada
    // -------------------------------

    @Test
    void testTipoCuentaEstaSoportada_CuentaCorrienteDolares() {
        
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(TipoCuenta.CUENTA_CORRIENTE);
        cuenta.setMoneda(TipoMoneda.DOLARES);

        boolean result = cuentaService.tipoCuentaEstaSoportada(cuenta);

        assertFalse(result, "Cuenta Corriente con moneda DOLARES no debería ser soportada");
    }


    @Test
    void testTipoCuentaEstaSoportada_TipoInvalido() {
        
        Cuenta cuenta = new Cuenta();
        cuenta.setTipoCuenta(null); // Tipo inválido
        cuenta.setMoneda(TipoMoneda.PESOS);

        boolean result = cuentaService.tipoCuentaEstaSoportada(cuenta);

        assertFalse(result, "Un tipo de cuenta inválido no debería ser soportado");
    }
}