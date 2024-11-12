package org.example.Interfaces;

import org.example.Enum.ESexo;


public interface IContable
{
   int  contarTotalClientes();
   int contarClientesActivos();
   int contarClientesInactivos();
   int contarClientesXGenero(ESexo sexoBuscado);
   int contarClientesXActividad(String actividad);
   
}
