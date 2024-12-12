package ar.edu.utn.frbb.tup.model;

import ar.edu.utn.frbb.tup.controller.dto.ClienteDto;
import ar.edu.utn.frbb.tup.model.enumModels.TipoCuenta;
import ar.edu.utn.frbb.tup.model.enumModels.TipoMoneda;
import ar.edu.utn.frbb.tup.model.enumModels.TipoPersona;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Cliente extends Persona {

    
    private TipoPersona tipoPersona; 
    private String banco; 
    private LocalDate fechaAlta; 
    private Set<Cuenta> cuentas = new HashSet<>(); // Colección de cuentas asociadas al cliente
    private Set<Prestamo> prestamos = new HashSet<>(); // Colección de préstamos asociados al cliente

    public Cliente() {
        super();
    }
    // Constructor: inicializa un Cliente a partir del DTO
    public Cliente(ClienteDto clienteDto) {
        super(clienteDto.getDni(), clienteDto.getApellido(), clienteDto.getNombre(), clienteDto.getFechaNacimiento());
        tipoPersona = TipoPersona.fromString(clienteDto.getTipoPersona());// tipo de persona a string
        fechaAlta = LocalDate.now(); 
        banco = clienteDto.getBanco(); 
    }


    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Set<Cuenta> getCuentas() {
        return cuentas;
    }

    public Set<Prestamo> getPrestamos() {
        return prestamos;
    }

    // AGREGAR UNA CUENTA AL CLIENTE
    public void addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta); // Añade la cuenta a la lista de cuentas
        cuenta.setTitular(cuenta.getTitular()); // Actualiza el titular de la cuenta
    }

    // VERIFICA SI EL CLIENTE TIENE UNA CUENTA (en base al tipo y moneda)
    public boolean tieneCuenta(TipoCuenta tipoCuenta, TipoMoneda moneda) {
        for (Cuenta cuenta : cuentas) {
            if (tipoCuenta.equals(cuenta.getTipoCuenta()) && moneda.equals(cuenta.getMoneda())) {// Si si true
                return true;
            }
        }
        return false; // si no false
    }

    // VERIFICA SI EL CLIENTE TIENE UNA CUENTA CON UNA MONEDA ESPECIFICA
    public boolean tieneCuentaMoneda(TipoMoneda moneda) {
        for (Cuenta cuenta : cuentas) {
            if (moneda.equals(cuenta.getMoneda())) {// Si si true
                return true;
            }
        }
        return false; // si no false
    }


    // AGREGAR UN PRESTAMO AL CLIENTE
    public void addPrestamo(Prestamo prestamo) {
        this.prestamos.add(prestamo); // Añade el préstamo a la lista de préstamos.
        prestamo.setNumeroCliente(prestamo.getNumeroCliente()); // Actualiza el número de cliente en el préstamo.
    }

    // REPRESENTACION EN TEXTO DEL CLIENTE
    @Override
    public String toString() {
        return "Cliente{" +
                "tipoPersona=" + tipoPersona +
                ", banco='" + banco + '\'' +
                ", fechaAlta=" + fechaAlta +
                ", cuentas=" + cuentas +
                '}';
    }
}
