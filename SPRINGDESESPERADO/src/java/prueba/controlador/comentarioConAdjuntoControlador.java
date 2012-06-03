/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.controlador;

import com.sun.xml.fastinfoset.stax.events.Util;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prueba.DAO.tokkenDAOImpl;
import prueba.modelo.Comentario;
import prueba.modelo.Tokken;
import prueba.modelo.Usuario;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio
 */

@Controller
public class comentarioConAdjuntoControlador {
    
    private Logger logUtility = Logger.getLogger(getClass());
    
    @RequestMapping(value = "/comentarioConAdjunto",method = RequestMethod.GET )
    public String comentarioConAdjuntoVista() {
        System.out.println("comentarioConAdjuntoController: Passing through GET login ...");
        return "comentarioConAdjunto";
    }
    
    @RequestMapping(value = "/comentarioConAdjunto",method = RequestMethod.POST )
    public String comentarioConAdjuntoVista(String message,String datafile,String Directory, HttpSession session, RedirectAttributes redirectAttrs) {
        System.out.println("comentarioConAdjuntoController: Passing through POST login ...");
       		Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        int error = 0;
        String mensajeVista = null;
        
        String urlArchivo = "C:/Users/Antonio/Dropbox/Desarrollo/Adjuntar/"+ datafile;
        System.out.println("URL Archivo: " + urlArchivo + " Mensaje: " + message + " Usuario: " + user.getNickname() );
       System.out.println("directorio: "+datafile);
        
        if (!(message.equals("")) && !Util.isEmptyString(datafile)) {        
                
//        String urlArchivo = "C:/Users/Antonio/Dropbox/Desarrollo/Adjuntar/"+ datafile;
        System.out.println("URL Archivo: " + urlArchivo + " Mensaje: " + message + " Usuario: " + user.getNickname() );
       System.out.println("directorio: "+Directory);
        tokkenDAOImpl busca = new tokkenDAOImpl();
        Tokken tokken = new Tokken();
        
        tokken = busca.findTokken(user.getNickname());
        
        String otroURL = "http://localhost:8084/SPRINGDESESPERADO/rest/insertarAdjunto";
        
     
        
    String userIdParameter = user.getNickname();
        String fileName = datafile;
        error = 1;
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;

        String charset = "UTF-8";
        String responseFromServer = "";


        String stringFieldName = "user_id";


        //String sourceFileUri = urlArchivo;
        String upLoadServerUri = otroURL;

        File sourceFile = new File(urlArchivo);
        if (!sourceFile.isFile()) {
         //   Log.e("Huzza", "Source File Does not exist");
           // return;
        }
        int serverResponseCode = 0;
        try { // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);

          String data = URLEncoder.encode("contenido", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");
    data += "&" + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(tokken.getTokken(), "UTF-8");
    data += "&" + URLEncoder.encode("nickName", "UTF-8") + "=" + URLEncoder.encode(user.getNickname(), "UTF-8");



            URL url = new URL(upLoadServerUri +"?"+ data);
            conn = (HttpURLConnection) url.openConnection(); // Open a HTTP
                                     // connection to
                                     // the URL
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", charset);

     //       conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type",
                "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("file_name", fileName);
            conn.setRequestProperty("user_id", userIdParameter);
                    dos = new DataOutputStream(conn.getOutputStream());
            dos.write(data.getBytes(charset));
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"file_name\";filename=\""
               + fileName + "\"" + lineEnd);

          //  dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + urlArchivo +"\"" + lineEnd); 
         //   dos.writeBytes("Content-Disposition: form-data; name=\"" + stringFieldName + "\""+ lineEnd);
            dos.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available(); // create a buffer of
                                  // maximum size
                bufferSize = (int) sourceFile.length();

            System.out.println("BytesAvail" + bytesAvailable);
            System.out.println("maxBufferSize" + maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
            dos.write(buffer, 0, bufferSize);

            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            System.out.println("Upload file to serverHTTP Response is : "
                + serverResponseMessage + ": " + serverResponseCode);
            // close streams
           // System.out.println("Upload file to server"+ sourceFileUri + " File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
          //  Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // this block will give the response of upload link
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
            System.out.println("RESULT Message: " + line);
            }
            rd.close();
            
//            Comentario respuesta = new Comentario();
//            //respuesta.setReply("0");
//                
//                XStream xstream2 = new XStream();
//                
//                xstream2 = new XStream(new DomDriver());
//
//                xstream2.alias("Comentario", Comentario.class);
            
//                respuesta = (Comentario) xstream2.fromXML(line);
//                
//                    System.out.println("Objeto respuesta nickName: "+respuesta.getId());
//                
//                if (respuesta.getId().startsWith("ERROR")) {
//                    if (respuesta.getId().startsWith("ERROR 100")){
//                        
//                        System.out.println("El usuario tiene el token vencido");
//                        error = 0;
//                        mensajeVista = "El usuario tiene el token vencido";
//                        
//                    } else{
//                    
//                        System.out.println("Error en los datos introducidos");
//                        error = 0;
//                        mensajeVista = "Error en los datos introducidos";
//                    }
//                }
            
        } catch (IOException ioex) {
           // Log.e("Huzza", "error: " + ioex.getMessage(), ioex);
        }
      //  return; // like 200 (Ok)

        } else
            mensajeVista = "Error en los datos introducidos";
        
        if (error == 1){
                redirectAttrs.addFlashAttribute("flash_message", "Comentario con adjunto exitoso!");
                logUtility.info("El comentario con adjunto realizado por: "+user.getNickname()+" fue insertado exitosamente");
                return "redirect:/comentariosDeUsuario";
            } else {
                logUtility.error("El comentario con adjunto realizado por: "+user.getNickname()+" no fue insertado exitosamente");
                redirectAttrs.addFlashAttribute("error", mensajeVista);
                return "redirect:/comentarioConAdjunto";
            }
    }
}