package org.example.Modelo;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public abstract class Persona {

//Atributos
    private String nombre;
    private String apellido;
    private String  DNI;
    private String sexo;
    private Double peso;
    private Double altura;
    private LocalDate fechaDeNacimiento;
    private int edad;

    //Constructor

    public Persona(String nombre, String apellido, String DNI, String sexo, Double peso, Double altura, String fechaDeNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.DNI = DNI;
        this.sexo = sexo;
        this.peso = peso;
        this.altura = altura;
        this.fechaDeNacimiento = formatearFechaALocalDate(fechaDeNacimiento); //formateo string DD-MM-YYYY
        edad = calcularEdad();
    }

    //Getters Y Setters

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDNI() {
        return DNI;
    }

    public String getSexo() {
        return sexo;
    }

    public Double getPeso() {
        return peso;
    }

    public Double getAltura() {
        return altura;
    }

    public LocalDate getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public int getEdad() {
        return edad;
    }

    //Metodos

    private LocalDate formatearFechaALocalDate(String fecha)
    {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return LocalDate.parse(fecha,formato);
    }


     private int calcularEdad()
     {
         LocalDate fechaActual = LocalDate.now();
         int edad = Period.between(fechaDeNacimiento, fechaActual).getYears();

         return edad;
     }
}
