package org.example.GUI;

import org.example.GUI.PopUps.JfrErrorPopUp;
import org.example.Interfaces.AccionBusqueda;
import org.example.Modelo.Cliente;

public class AccionNombre implements AccionBusqueda {

    @Override
    public void ejecutar(String busqueda, JfrAgregarNuevoCliente nuevoCliente, Cliente cliente) {
        if(!busqueda.isEmpty()) {
            if(!nuevoCliente.verificarSiContieneNumero(busqueda)) {
                cliente.modificarNombreCliente(busqueda);
                
            }
            else {
                mostrarError("El nombre no puede tener digitos", nuevoCliente);
            }
        }
        else{
            mostrarError("No ingreso ningun nombre", nuevoCliente);
            }
    }

    private void mostrarError(String mensaje,java.awt.Frame parent) {
        new JfrErrorPopUp(parent, true, mensaje);
    }
    
}
