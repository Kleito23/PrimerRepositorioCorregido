package org.example.Modelo;



import org.example.Enum.EDiasSemana;
import org.example.Enum.EObjetivo;
import org.example.JavaUtiles.JsonUtiles;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.IOException;

import java.io.Serializable;
import java.util.*;


public class Rutina implements Serializable
{
    //atributos
    private LinkedHashSet<Ejercicio> rutina;//nos importa el orden de realizacion de ejercicios
    private EObjetivo objetivo; //Perder peso|Ganar fuerza|Ganar musculo
    private EDiasSemana diaAsignado;
    private LinkedHashMap<String, Rutina> rutinaSemanal;
    //constructores

    public Rutina(EObjetivo objetivo,EDiasSemana diaAsignado) {
        this.objetivo = objetivo;
        rutina = new LinkedHashSet<>();
        this.diaAsignado= diaAsignado;
        setRutinaSemanal();
    }

    public Rutina(EDiasSemana diaAsignado) {
        objetivo = null;
        this.diaAsignado = diaAsignado;
        rutina = new LinkedHashSet<>();
        setRutinaSemanal();
    }

    public Rutina() {
        objetivo = null;
        rutina = new LinkedHashSet<>();
        diaAsignado= null;
        setRutinaSemanal();
    }

    //getters y setters

    public LinkedHashSet<Ejercicio> getRutina() {
        return rutina;
    }

    public EObjetivo getObjetivo()
    {
        return objetivo;
    }

    public void setObjetivo(String objetivo)
    {
        EObjetivo objetivoEnum = EObjetivo.valueOf(objetivo);//no necesita validarse ya que viene de un boton del GUI
        this.objetivo = objetivoEnum;
    }

    public EDiasSemana getDiaAsignado() {
        return diaAsignado;
    }

    //metodos
    public Rutina getUnaRutinaEspecifica(Enum diaRequerido)
    {
        return rutinaSemanal.get(diaRequerido.toString());
    }

    public boolean agregarUnEjercicioARutina(Ejercicio nuevoEjercicio)
    {
        //al ser un LinkedHashSet, el ejercicio no se repetiria y tiene orden
        return rutina.add(nuevoEjercicio);
    }

    public boolean sacarUnEjercicioDeRutinaXObjeto(Ejercicio ejercicioAEliminar)
    {
        return rutina.remove(ejercicioAEliminar);
    }
    public void setRutinaSemanal() {
        //metodo para inicializar todos los dias con una rutina vacia, y q no haya nullpointers
        rutinaSemanal = new LinkedHashMap<>();
        rutinaSemanal.put(EDiasSemana.LUNES.toString(),new Rutina(EDiasSemana.LUNES));
        rutinaSemanal.put(EDiasSemana.MARTES.toString(),new Rutina(EDiasSemana.MARTES));
        rutinaSemanal.put(EDiasSemana.MIERCOLES.toString(),new Rutina(EDiasSemana.MIERCOLES));
        rutinaSemanal.put(EDiasSemana.JUEVES.toString(),new Rutina(EDiasSemana.JUEVES));
        rutinaSemanal.put(EDiasSemana.VIERNES.toString(),new Rutina(EDiasSemana.VIERNES));
        rutinaSemanal.put(EDiasSemana.SABADO.toString(),new Rutina(EDiasSemana.SABADO));
        rutinaSemanal.put(EDiasSemana.DOMINGO.toString(),new Rutina(EDiasSemana.DOMINGO));

    }

//    public boolean agregarUnEjercicioARutinaXId(int idEjercicioAAgregar, int repeticiones, int series){
//        boolean flag=false;
//        Iterator<Ejercicio> iterator = rutina.iterator();
//        while (iterator.hasNext() && flag==false)
//        {
//            Ejercicio ejercicioAux=iterator.next();
//            if (ejercicioAux.getIdEjercicio() == idEjercicioAAgregar)
//            {
//                ejercicioAux.setRepeticiones(repeticiones);
//                ejercicioAux.setSeries(series);
//                rutina.add(ejercicioAux);
//                flag=true;
//            }
//
//        }
//        return flag;
//    }


    public boolean agregarUnEjercicioARutinaXID(int idEjercicioAAgregar, int repeticiones, int series){
        boolean flag = false;
            ArrayList<Ejercicio> ejercicios;

            try {
                ejercicios = leerJSONEjercicio();

                for (Ejercicio ejercicio : ejercicios) {
                    if (ejercicio.getIdEjercicio() == idEjercicioAAgregar) {
                        ejercicio.setSeries(series);
                        ejercicio.setRepeticiones(repeticiones);
                        rutina.addLast(ejercicio);
                        flag = true;
                    }
                }
            }
            catch (IOException e){
                e.getMessage();
                e.printStackTrace();
            }

        return flag;
    }

    public Ejercicio buscarUnEjercicioXId(int idEjercicioAbuscar)
    {
        Ejercicio ejercicioAuxBuscado=null;
        ArrayList<Ejercicio> ejercicios;

        try {
            ejercicios = leerJSONEjercicio();

            for (Ejercicio ejercicio : ejercicios) {
                if (ejercicio.getIdEjercicio() == idEjercicioAbuscar) {
                    ejercicioAuxBuscado= ejercicio;
                }
            }
        }
        catch (IOException e){
            e.getMessage();
            e.printStackTrace();
        }

        return ejercicioAuxBuscado;
    }

    public Ejercicio buscarUnEjercicioXNombre(String nombreEjercicioBuscar)
    {
        Ejercicio ejercicioAuxBuscado=null;
        ArrayList<Ejercicio> ejercicios;

        try {
            ejercicios = leerJSONEjercicio();

            for (Ejercicio ejercicio : ejercicios) {
                if (ejercicio.getNombreEjercicio().equals(nombreEjercicioBuscar)) {
                    ejercicioAuxBuscado= ejercicio;
                }
            }
        }
        catch (IOException e){
            e.getMessage();
            e.printStackTrace();
        }

        return ejercicioAuxBuscado;
    }

    public boolean sacarUnEjercicioDeRutinaXId(int idEjercicioAEliminar)
    {
        boolean flag=false;
        Iterator<Ejercicio> iterator = rutina.iterator();
        while (iterator.hasNext() && flag==false)
        {
            Ejercicio ejercicioAux=iterator.next();
            if (ejercicioAux.getIdEjercicio() == idEjercicioAEliminar)
            {
                rutina.remove(ejercicioAux);
                flag=true;
            }

        }
        return flag;
    }






    //funcion para leer el JSON de un ejerciciod
    public ArrayList<Ejercicio> leerJSONEjercicio() throws IOException {

        ArrayList<Ejercicio> ejercicioArrayList = new ArrayList<>();

        String contenido = JsonUtiles.leer("ejercicios");

        try {
            JSONArray ja = new JSONArray(contenido);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                Ejercicio ejercicio = new Ejercicio(jo.getString("tipoDeEjercicio"),jo.getString("nombreEjercicio"),jo.getString("grupoMuscular"),jo.getString("complejidad"),jo.getString("materialDeTrabajo"),jo.getInt("idEjercicio"));
                ejercicioArrayList.add(ejercicio);
            }
        }catch(JSONException e){
            System.out.println("Archivo no encontrado o datos invalidos");
        }


        /*
        ObjectMapper objectMapper = new ObjectMapper(); // Clase para serializar y deserializar datos de JSON
        ArrayList<Ejercicio> ejercicioArrayList;

        //Intentar leer un archivo desde el ClassPath
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(archivo)) {
            if (inputStream == null) {
                throw new IOException("Archivo no encontrado: " + archivo);
            }

            //Carga el ArrayList con los datos del JSON
            ejercicioArrayList = objectMapper.readValue(inputStream, new TypeReference<ArrayList<Ejercicio>>() {
            });
        } catch (IOException e){
            throw e;
        }
*/
        return  ejercicioArrayList;
    }



    @Override
    public boolean equals(Object o) {
        boolean flag=false;
        if (o != null)
        {
            if (o instanceof Rutina rutinaAComparar)
            {
                if (this.rutina.equals(rutinaAComparar.rutina) && this.objetivo == rutinaAComparar.objetivo )
                {
                    flag=true;
                }
            }
        }
        return flag;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return "Rutina{" +
                "rutina=" + rutina +
                ", objetivo=" + objetivo +
                ", diaAsignado=" + diaAsignado +
                '}';
    }
}
