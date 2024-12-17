package ar.edu.utn.frbb.tup.service;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.CuentaNoEncontradaException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaAlreadyExistsException;
import ar.edu.utn.frbb.tup.exception.cuentaExceptions.TipoCuentaNoSoportadaException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import ar.edu.utn.frbb.tup.model.Cuenta;
import ar.edu.utn.frbb.tup.model.Prestamo;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;
import ar.edu.utn.frbb.tup.persistence.CuentaDao;
import ar.edu.utn.frbb.tup.controller.dto.CuentaDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CuentaServiceTest {

    @InjectMocks
    private CuentaService cuentaService;

    @Mock
    private CuentaDao cuentaDao;

    @Mock
    private ClienteService clienteService;

    @Mock
    private PrestamoCalculator prestamoCalculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    // --------------------------------------------
    // TEST: Crear cuenta con éxito
    // --------------------------------------------

    @Test
    void testCrearCuenta_Success() throws Exception {
        
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(12345678L);

        Cuenta nuevaCuenta = new Cuenta(cuentaDto);

        when(cuentaDao.find(anyLong())).thenReturn(null);
        when(clienteService.tieneCuentaDelMismoTipo(anyLong(), any(), any())).thenReturn(false);

        Cuenta result = cuentaService.CrearCuenta(cuentaDto);

        assertNotNull(result);
        assertEquals(nuevaCuenta.getTipoCuenta(), result.getTipoCuenta());
        assertEquals(nuevaCuenta.getMoneda(), result.getMoneda());
        verify(cuentaDao, times(1)).save(any(Cuenta.class));
    }

    // --------------------------------------------
    // TEST: Tipo de cuenta no soportada (TipoCuentaNoSoportadaException)
    // --------------------------------------------

    @Test
    void testCrearCuenta_TipoCuentaNoSoportadaException() {
        
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("C");
        cuentaDto.setMoneda("D"); // Cuenta Corriente no soporta Dólares
        cuentaDto.setDniTitular(12345678L);

        when(cuentaDao.find(anyLong())).thenReturn(null);

        TipoCuentaNoSoportadaException exception = assertThrows(TipoCuentaNoSoportadaException.class,
                () -> cuentaService.CrearCuenta(cuentaDto));
        assertEquals("Error: El tipo de cuenta CUENTA_CORRIENTE en DOLARES no está soportado.",
                exception.getMessage());

        verify(cuentaDao, never()).save(any(Cuenta.class));
    }

    // --------------------------------------------
    // TEST: Cliente ya tiene una cuenta del mismo tipo (TipoCuentaAlreadyExistsException)
    // --------------------------------------------

    @Test
    void testCrearCuenta_TipoCuentaAlreadyExistsException() throws Exception {
        
        CuentaDto cuentaDto = new CuentaDto();
        cuentaDto.setTipoCuenta("A");
        cuentaDto.setMoneda("P");
        cuentaDto.setDniTitular(12345678L);

        when(cuentaDao.find(anyLong())).thenReturn(null);
        when(clienteService.tieneCuentaDelMismoTipo(anyLong(), any(), any())).thenReturn(true);

        TipoCuentaAlreadyExistsException exception = assertThrows(TipoCuentaAlreadyExistsException.class,
                () -> cuentaService.CrearCuenta(cuentaDto));
        assertEquals("Error: El cliente ya tiene una cuenta del tipo CAJA_AHORRO en PESOS", exception.getMessage());

        verify(cuentaDao, never()).save(any(Cuenta.class));
    }

    // --------------------------------------------
    // TEST: Buscar cuenta con éxito
    // --------------------------------------------

    @Test
    void testBuscarCuenta_Success() throws Exception {
        
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(123L);
        when(cuentaDao.find(123L)).thenReturn(cuenta);

        Cuenta result = cuentaService.find(123L);

        assertNotNull(result);
        assertEquals(123L, result.getNumeroCuenta());
        verify(cuentaDao, times(1)).find(123L);
    }

    // --------------------------------------------
    // TEST: Buscar cuenta - No encontrada (CuentaNoEncontradaException)
    // --------------------------------------------

    @Test
    void testBuscarCuenta_CuentaNoEncontradaException() {
        
        when(cuentaDao.find(123L)).thenReturn(null);

        CuentaNoEncontradaException exception = assertThrows(CuentaNoEncontradaException.class,
                () -> cuentaService.find(123L));
        assertEquals("Error: La cuenta no existe", exception.getMessage());
    }

    // --------------------------------------------
    // TEST: Actualizar cuenta con éxito
    // --------------------------------------------

    @Test
    void testActualizarCuenta_Success() throws Exception {
        
        Prestamo prestamo = new Prestamo(12345678L, 12, 5000, TipoMoneda.PESOS);
        Cuenta cuenta = new Cuenta();
        cuenta.setBalance(1000);
        when(cuentaDao.buscarPorMoneda(TipoMoneda.PESOS)).thenReturn(cuenta);

        cuentaService.actualizarCuenta(prestamo);

        assertEquals(6000, cuenta.getBalance()); // 1000 + 5000
        verify(cuentaDao, times(1)).save(cuenta);
    }

    // --------------------------------------------
    // TEST: Actualizar cuenta - Cuenta no encontrada
    // --------------------------------------------

    @Test
    void testActualizarCuenta_CuentaNoEncontradaException() {
        
        Prestamo prestamo = new Prestamo(12345678L, 12, 5000, TipoMoneda.PESOS);
        when(cuentaDao.buscarPorMoneda(TipoMoneda.PESOS)).thenReturn(null);

        CuentaNoEncontradaException exception = assertThrows(CuentaNoEncontradaException.class,
                () -> cuentaService.actualizarCuenta(prestamo));
        assertEquals("Error: La cuenta no existe", exception.getMessage());
    }
}
