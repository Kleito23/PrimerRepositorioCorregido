package org.example.API;


import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;

import java.io.*;
import java.util.Date;


public class DropBoxAPI {
    private String ACCESS_TOKEN = "sl.B1lXUD8Fm1QUv9tfZG0TIlSxruZSp2tkLY6puZqtu-wjPjLUkhyYsLAAu0dCev_kJGyMytIxyFgQr9dLQji7HWhEJTnaRLPrEmYXPL0NrLDEp3gvuqOrW8HmybliM7wnDYMNTnQju1Ur8Da3xD6e5BM";
    private String CARPETA = "Aplicaciones/fitAdmin"; // Ruta de la carpeta en Dropbox
    private DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/fitAdmin").build();
    private DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

    //todo: verificar si el TOKEN se vence

    /**
        DropBoxAPI dropBoxAPI= new DropBoxAPI();
        QrAPI qrAPI= new QrAPI();

        gimnasio.crearPDFParaQR(cliente);

        dropBoxAPI.subirPDF("pdfDatosCliente/QRaGenerar.pdf"); // subirlo a dropbox
        String url= dropBoxAPI.obtenerURL("QRaGenerar"); //recordar subir solo el nombre del archivo que esta en dropbox
        qrAPI.generarQr(url);
    */


   public void subirPDF(String rutaArchivo){

       File pdf = new File(rutaArchivo);// obtiene el pdf de la carpeta
       InputStream inputStream; // crea una entrada del archivo PDF
       try {
           inputStream = new FileInputStream(pdf);
       } catch (FileNotFoundException e) {
           throw new RuntimeException(e);
       }
        String nombreArchivoMasBarrita="/";
       nombreArchivoMasBarrita= nombreArchivoMasBarrita.concat(pdf.getName());
       try {
           if (existeArchivoEnDropbox(nombreArchivoMasBarrita)){
               eliminarArchivoEnDropbox(nombreArchivoMasBarrita);
           }
       } catch (GetMetadataErrorException e) {
           throw new RuntimeException(e);
       }


       UploadBuilder uploadBuilder = client.files().uploadBuilder("/"+pdf.getName());//guardar el archivo en la carpeta DropBox
        uploadBuilder.withClientModified(new Date(pdf.lastModified()));//carga la fecha de la ultima modificacion
        uploadBuilder.withMode(WriteMode.ADD);// elige el modo de acceso en que se va a utilizar el archivo
        uploadBuilder.withAutorename(false);// Si hay un archivo del mismo nombre, crea otro con un (x) por ejemplo PDF(1)



       try {
           uploadBuilder.uploadAndFinish(inputStream); //se sube el archivo
       } catch (DbxException e) {
           throw new RuntimeException(e);
       } catch (IOException e) {
           throw new RuntimeException(e);
       }

       try {
           inputStream.close(); //cierro la entrada del archivo
       } catch (IOException e) {
           throw new RuntimeException(e);
       }
   }

    public String obtenerURL(String nombreArchivoDeDropbox){
        //obtengo la url de Dropbox del archivo requerido

        //recordar subir solo el nombre del archivo que esta en dropbox

        //hay que especificar el tipo de archivo que quiero. EJ: rutina.pdf
        //se construye dentro de este metodo
        String urlObtenida;
        String s="/"; //agrego la barra para el directorio de dropbox
        s= s.concat(nombreArchivoDeDropbox); //le concateno el nombre del archivo; como se llama en dropbox
        s= s.concat(".pdf"); // le agrego el tipo de archivo
        try {
            urlObtenida= client.files().getTemporaryLink(s).getLink();
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
        return urlObtenida;
    }

    private boolean existeArchivoEnDropbox(String nombreArchivo) throws GetMetadataErrorException {
       boolean flag=false;
       try {
            client.files().getMetadata(nombreArchivo); //obtengo la informacion del archivo y basicamente si la obtengo es porque existe
            flag=true;
        } catch (GetMetadataErrorException e) {
            if (e.errorValue.isPath() && e.errorValue.getPathValue().isNotFound()) {
                //si me tira una excepcion significa que no existe por lo tanto retorno false
                //flag=false;
            } else {
                throw e; //sino tiro error
            }
        } catch (Exception e) {
           e.printStackTrace(); //si me tira otro tipo de excepcion, tambien retorno false ya que no existe
       }
       return flag;
    }

    private void eliminarArchivoEnDropbox(String nombreArchivo){
        try {
            //si el archivo existe, lo elimino
            client.files().deleteV2(nombreArchivo).getMetadata();
        }catch (DbxException e)
        {
            // sino retorno una excepcion
            e.printStackTrace();
        }
    }

}



