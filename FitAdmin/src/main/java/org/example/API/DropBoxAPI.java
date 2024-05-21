package org.example.API;


import java.io.*;
import java.util.Locale;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;


/**
 DropBoxAPI dropBoxAPI= new DropBoxAPI();
 QrAPI qrAPI= new QrAPI();

 gimnasio.crearPDFParaQR(cliente);

 dropBoxAPI.subirPDF("pdfDatosCliente/QRaGenerar.pdf"); // subirlo a dropbox
 String url= dropBoxAPI.obtenerURL("QRaGenerar"); //recordar subir solo el nombre del archivo que esta en dropbox
 qrAPI.generarQr(url);
 */
//todo: verificar si el TOKEN se vence

import com.dropbox.core.v2.users.FullAccount;

public class DropBoxAPI {

    private static final String APP_KEY = "txclgtve4z6nla2";
    private static final String APP_SECRET = "4l7sllt3ezkyhuf";
    private static final String ACCESS_TOKEN_FILE = "access_token.txt";

    private DbxClientV2 client;

    public DropBoxAPI() throws IOException {
        String accessToken = loadAccessToken();
        if (accessToken == null) {
            accessToken = authenticate();
            saveAccessToken(accessToken);
        }
        initializeClient(accessToken);
    }

    private void initializeClient(String accessToken) {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token cannot be null or empty.");
        }
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial")
                .withUserLocale(Locale.getDefault().toString())
                .build();
        client = new DbxClientV2(config, accessToken);
        System.out.println("Dropbox client initialized successfully.");
    }

    private String authenticate() throws IOException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial")
                .withUserLocale(Locale.getDefault().toString())
                .build();

        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        String authorizeUrl = webAuth.start();

        System.out.println("1. Ve a: " + authorizeUrl);
        System.out.println("2. Haz clic en \"Permitir\" (puede que necesites iniciar sesión primero)");
        System.out.println("3. Copia el código de autorización.");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String code = br.readLine().trim();

        DbxAuthFinish authFinish = null;
        try {
            authFinish = webAuth.finish(code);
        } catch (DbxException e) {
            throw new RuntimeException(e);
        }
        return authFinish.getAccessToken();
    }

    private void saveAccessToken(String accessToken) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCESS_TOKEN_FILE))) {
            writer.write(accessToken);
        }
    }

    private String loadAccessToken() throws IOException {
        File file = new File(ACCESS_TOKEN_FILE);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return reader.readLine().trim();
        }
    }

    public FullAccount getCurrentAccount() throws Exception
    {
        if (client == null) {
            throw new IllegalStateException("Dropbox client is not initialized.");
        }
        return client.users().getCurrentAccount();
    }
  /*  package org.example.API;


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


        /*public void subirPDF(String rutaArchivo){

            File pdf = new File(rutaArchivo);// obtiene el pdf de la carpeta
            InputStream inputStream; // crea una entrada del archivo PDF
            String nombreArchivoMasBarrita="/";
            nombreArchivoMasBarrita= nombreArchivoMasBarrita.concat(pdf.getName());
            try {
                inputStream = new FileInputStream(pdf);

                if (existeArchivoEnDropbox(nombreArchivoMasBarrita)){
                    eliminarArchivoEnDropbox(nombreArchivoMasBarrita);
                }


                UploadBuilder uploadBuilder = client.files().uploadBuilder("/"+pdf.getName());//guardar el archivo en la carpeta DropBox
                uploadBuilder.withClientModified(new Date(pdf.lastModified()));//carga la fecha de la ultima modificacion
                uploadBuilder.withMode(WriteMode.ADD);// elige el modo de acceso en que se va a utilizar el archivo
                uploadBuilder.withAutorename(false);// Si hay un archivo del mismo nombre, crea otro con un (x) por ejemplo PDF(1)





                uploadBuilder.uploadAndFinish(inputStream); //se sube el archivo


                inputStream.close(); //cierro la entrada del archivo

            } catch (FileNotFoundException e) {
                //archivo no encontrado
                e.getMessage();
                e.printStackTrace();
            } catch (GetMetadataErrorException e) {
                //error con obtener info de un archivo dropbox
                e.getMessage();
                e.printStackTrace();
            }
            catch (DbxException e) {
                //error con obtener info cuenta dropbox
                e.getMessage();
                e.printStackTrace();
            } catch (IOException e) {
                //error con el input/output (Archivo)
                e.getMessage();
                e.printStackTrace();
            }

        }

        public String obtenerURL(String nombreArchivoDeDropbox){
            //obtengo la url de Dropbox del archivo requerido

            //recordar subir solo el nombre del archivo que esta en dropbox

            //hay que especificar el tipo de archivo que quiero. EJ: /rutina.pdf
            //se construye dentro de este metodo
            String urlObtenida = "";
            String s="/"; //agrego la barra para el directorio de dropbox
            s= s.concat(nombreArchivoDeDropbox); //le concateno el nombre del archivo; como se llama en dropbox
            s= s.concat(".pdf"); // le agrego el tipo de archivo


            try {
                urlObtenida= client.files().getTemporaryLink(s).getLink();
            } catch (DbxException e) {
                e.getMessage();
                e.printStackTrace();
            }
            return urlObtenida;
        }

        private boolean existeArchivoEnDropbox(String nombreArchivo) throws DbxException {
            boolean flag=false;

            try {
                client.files().getMetadata(nombreArchivo); //obtengo la informacion del archivo y basicamente si la obtengo es porque existe
                flag=true;
            } catch (GetMetadataErrorException e) {
                if (e.errorValue.isPath() && e.errorValue.getPathValue().isNotFound()) {
                    //si me tira una excepcion significa que no existe por lo tanto retorno false
                    //flag=false;
                } else {
                    throw e; //sino tiro el error
                }
            } catch (Exception e) {
                throw e; //si me tira otro tipo de excepcion, tambien retorno false ya que no existe
            }
            return flag;
        }

        private void eliminarArchivoEnDropbox(String nombreArchivo) throws DbxException {
            try {
                //si el archivo existe, lo elimino
                client.files().deleteV2(nombreArchivo).getMetadata();
            }catch (DbxException e)
            {
                // sino retorno una excepcion
                throw e;
            }
        }

    }*/

}








