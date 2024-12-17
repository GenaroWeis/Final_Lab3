package ar.edu.utn.frbb.tup.Controller;
import ar.edu.utn.frbb.tup.controller.ClienteController;
import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.controller.validator.ClienteValidator;
import ar.edu.utn.frbb.tup.exception.CampoVacioException;
import ar.edu.utn.frbb.tup.exception.clienteExceptions.*;
import ar.edu.utn.frbb.tup.model.Cliente;
import ar.edu.utn.frbb.tup.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidator clienteValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    // -------------------------------
    // Test:  CLIENTE SUCCESS
    // -------------------------------

    @Test
    void testCrearCliente_Success() throws Exception {
        // datos de entrada
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNombre("Genaro");
        clienteDto.setApellido("Weis");
        clienteDto.setDni(46613030);
        clienteDto.setFechaNacimiento("2005-06-07");
        clienteDto.setTipoPersona("F");
        clienteDto.setBanco("Banco Prueba");

        Cliente cliente = new Cliente();
        cliente.setDni(46613030);

        // Simular es y creación
        doNothing().when(clienteValidator).validate(clienteDto);
        when(clienteService.CrearCliente(clienteDto)).thenReturn(cliente);

        // invocar el método
        Cliente result = clienteController.crearCliente(clienteDto);

        // verificar resultados
        assertNotNull(result);
        assertEquals(46613030, result.getDni());
        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, times(1)).CrearCliente(clienteDto);
    }

    // -------------------------------
    // Test:  GENERAL CAMPOS VACIOS
    // -------------------------------

    @Test
    void testCrearCliente_CampoVacioException() throws Exception {
        // datos de entrada inválidos
        ClienteDto clienteDto = new ClienteDto();

        // Simular la excepción en el validador
        doThrow(new CampoVacioException("Error: Ingrese un nombre"))
                .when(clienteValidator).validate(clienteDto);

        // verificar que lanza la excepción
        CampoVacioException exception = assertThrows(CampoVacioException.class, () -> clienteController.crearCliente(clienteDto));
        assertEquals("Error: Ingrese un nombre", exception.getMessage());
        verify(clienteValidator, times(1)).validate(clienteDto);
        verify(clienteService, never()).CrearCliente(clienteDto);
    }

    // -------------------------------
    // Test:  BUSCAR CLIENTE POR DNI
    // -------------------------------

    @Test
    void testBuscarClientePorDni_Success() throws Exception {
        // simular un cliente existente
        long dni = 46613030;
        Cliente cliente = new Cliente();
        cliente.setDni(dni);

        when(clienteService.buscarClientePorDni(dni)).thenReturn(cliente);

        // invocar el método
        Cliente result = clienteController.buscarClientePorDni(dni);

        //  verificar resultados
        assertNotNull(result);
        assertEquals(dni, result.getDni());
        verify(clienteService, times(1)).buscarClientePorDni(dni);
    }

    // -------------------------------
    // Test:  NO ENCONTRADO BUSCAR CLIENTE POR DNI
    // -------------------------------

    @Test
    void testBuscarClientePorDni_ClienteNoEncontrado() throws Exception {
        // simular la excepción
        long dni = 46613030;
        when(clienteService.buscarClientePorDni(dni))
                .thenThrow(new ClienteNoEncontradoException("Error: El cliente no existe"));

        // verificar que lanza la excepción
        ClienteNoEncontradoException exception = assertThrows(ClienteNoEncontradoException.class, () -> clienteController.buscarClientePorDni(dni));
        assertEquals("Error: El cliente no existe", exception.getMessage());
        verify(clienteService, times(1)).buscarClientePorDni(dni);
    }

    // -------------------------------
    // Test:  GENERAL BUSCAR TODOS LOS CLIENTES
    // -------------------------------

    @Test
    void testBuscarTodosLosClientes_Success() {
        // simular una lista de clientes
        Cliente cliente = new Cliente();
        cliente.setDni(46613030);
        List<Cliente> clientes = Collections.singletonList(cliente);

        when(clienteService.buscarTodosLosClientes()).thenReturn(clientes);

        // invocar el método
        List<Cliente> result = clienteController.buscarTodosLosClientes();

        // verificar resultados
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(46613030, result.get(0).getDni());
        verify(clienteService, times(1)).buscarTodosLosClientes();
    }
}
