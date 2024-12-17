package ar.edu.utn.frbb.tup.service;

import org.springframework.stereotype.Service;
import java.util.Random;

/*
VERIFICAR EL ESTADO CREDITICIO DE UN CLIENTE (si se le va a dar el prestamo o no)
    -te da un 20% de probabilidad de false (rechazo)
    -usa una verificacion similar a la de un dni comun
*/

@Service
public class CreditCheckService {

    private static final double PROBABILIDAD_RECHAZO = 0.20; // 20% de probabilidad de rechazo
    private static final int MIN_DNI = 10_000_000; // DNI más bajo aceptado
    private static final int MAX_DNI = 99_999_999; // DNI más alto aceptado
    private final Random random = new Random();

    //VERIFICA EL ESTADO CREDITICIO DEL CLIENTE (en base a la probabilidad de rechazo)
    public boolean verificarEstadoCrediticio(long dni) {
        if (!esDniValido(dni)) {
            throw new IllegalArgumentException("Error: DNI inválido. Debe estar entre " + MIN_DNI + " y " + MAX_DNI + " y tener 8 dígitos.");
        }
        return random.nextDouble() >= PROBABILIDAD_RECHAZO;
    }

    //VERIFICA SI EL DNI ES VALIDO
    private boolean esDniValido(long dni) {
        return dni >= MIN_DNI && dni <= MAX_DNI;
    }
}

