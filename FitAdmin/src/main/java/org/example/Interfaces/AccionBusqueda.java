package org.example.Interfaces;

import org.example.GUI.JfrAgregarNuevoCliente;
import org.example.Modelo.Cliente;

public interface AccionBusqueda {
    void ejecutar(String busqueda,JfrAgregarNuevoCliente nuevoCliente, Cliente cliente);
}
