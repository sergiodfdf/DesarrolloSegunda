/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.controlador;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prueba.modelo.Usuario;
import prueba.modelo.registerVista;

/**
 *
 * @author Antonio
 */
@Controller
public class registerControlador {
    
//    IUserService userService;
    private Logger logUtility = Logger.getLogger(getClass());
    String urlArchivo="";
    String fileName="";
	
	@RequestMapping(value = "/register",method = RequestMethod.GET )
    public String register() {
            System.out.println("entro en controlador REGISTER");
        return "register";
    }

	
	@RequestMapping(value = "/register",method = RequestMethod.POST )
    public String login(registerVista form, BindingResult result, RedirectAttributes redirectAttrs) {
        
            XStream xstream = new XStream();
            int error = 1;
            Usuario user = new Usuario();
            
            if (result.hasErrors()){
                System.out.println( "Object has validation errors" );
		} else {
              
                System.out.println("entro en controlador REGISTER por el post");
                System.out.println("Datos del form: "+form.toString());
                System.out.println("Nickname: "+form.getNickname());
                
                user.setApellido(form.getApellido());
                user.setBiografia(form.getBiografia());
                user.setClave(form.getClave());
                user.setCorreo(form.getCorreo());
                user.setFecha_nac(form.getFecha_nac());
                user.setFoto(form.getFoto());
                user.setNickname(form.getNickname());
                user.setNombre(form.getNombre());
                user.setPais(form.getPais());
                
                System.out.println("Contenido de foto antes del controlador: "+user.getFoto());
                
                XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("__","_");
                
//                XStream xstream = new XStream();
                xstream = new XStream(new DomDriver("UTF-8",replacer));

                xstream.alias("Usuario", Usuario.class);

                String input = xstream.toXML(user);
                System.out.println("pasando por la interfaz");
                try {
 
		URL url = new URL("http://localhost:8084/SPRINGDESESPERADO/rest/insertarUsuario");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/xml");
 
		OutputStream os = conn.getOutputStream();
		os.write(input.getBytes());
		os.flush();
                
                    System.out.println(conn.getOutputStream());
                    System.out.println(input.getBytes());
                
//		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//			throw new RuntimeException("Failed : HTTP error code : "
//				+ conn.getResponseCode());
//		}
 
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
 
		String output;
                String xmlRespuesta = null;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
                        xmlRespuesta = output;
		}                
 
		conn.disconnect();
                
                System.out.println("Output fuera del while: "+xmlRespuesta);
                
                Usuario respuesta = new Usuario();
                
                XStream xstream2 = new XStream();
                
                xstream2 = new XStream(new DomDriver());

                xstream2.alias("Usuario", Usuario.class);
            
                respuesta = (Usuario) xstream2.fromXML(xmlRespuesta);
                
                    System.out.println("Objeto respuesta nickName: "+respuesta.getNickname());
                
                if (respuesta.getNickname().startsWith("ERROR")) {
                    System.out.println("Uno de los campos del formulario usuario se dejo vacio");
                    logUtility.error("El usuario= " + user.getNickname() + " introdujo algun campo vacio.");
                    error = 0;
                }else ///////////////////////////////////////////////////////////
               
                {
              String foto = respuesta.getFoto(); 
              System.out.println("valor de foto: "+foto);
              if (foto.equalsIgnoreCase("no")){
              urlArchivo = "C:/Users/Antonio/Dropbox/Desarrollo/Adjuntar/Default.jpg";
              fileName = "Default.jpg";
              System.out.println("El nombre de la ruta:" + urlArchivo);
              }else
              {
              
              urlArchivo = "C:/Users/Antonio/Dropbox/Desarrollo/Adjuntar/" + foto;
              fileName = foto;
              System.out.println("El nombre de la ruta:" + urlArchivo);

              }
        
        String otroURL = "http://localhost:8084/SPRINGDESESPERADO/rest/Adjunto2";
           String userIdParameter = respuesta.getNickname();
       System.out.println("Nombre del usuario userIdParameter" + userIdParameter);
        HttpURLConnection conn2 = null;
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
       String upLoadServerUri = otroURL;
        File sourceFile = new File(urlArchivo);

        int serverResponseCode = 0;
        try { // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);

          String data = URLEncoder.encode("contenido", "UTF-8") + "=" + URLEncoder.encode("epa", "UTF-8");

            URL url2 = new URL(upLoadServerUri);
            conn2 = (HttpURLConnection) url2.openConnection(); // Open a HTTP
                                     // connection to
                                     // the URL
            conn2.setDoInput(true); // Allow Inputs
            conn2.setDoOutput(true); // Allow Outputs
            conn2.setUseCaches(false); // Don't use a Cached Copy
            conn2.setRequestMethod("POST");
            conn2.setRequestProperty("Connection", "Keep-Alive");
            conn2.setRequestProperty("Accept-Charset", charset);
            conn2.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
            conn2.setRequestProperty("file_name", userIdParameter + ".jpg");
            conn2.setRequestProperty("user_id", userIdParameter);
                    dos = new DataOutputStream(conn2.getOutputStream());
            dos.write(data.getBytes(charset));
              dos.writeBytes(twoHyphens + boundary + lineEnd);

              dos.writeBytes("Content-Disposition: form-data; name=\"file_name\";filename=\""
               + userIdParameter+".jpg" + "\"" + lineEnd);
              
              System.out.println("Content-Disposition: form-data; name=\"file_name\";filename=\""
               + userIdParameter + "\"" + lineEnd); 

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
            serverResponseCode = conn2.getResponseCode();
            String serverResponseMessage = conn2.getResponseMessage();

            System.out.println("Upload file to serverHTTP Response is : "
                + serverResponseMessage + ": " + serverResponseCode);

            fileInputStream.close();
            dos.flush();
            dos.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                conn2.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
            System.out.println("RESULT Message: " + line);
            }
            rd.close();

            
        } catch (IOException ioex) {
        }
          
                    
                }    //////////////////////////////////////////////////////////
 
	  }  catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	 }
            }
            
            if (error == 1){
                logUtility.info("El usuario= " + user.getNickname() + " fue registrado correctamente.");
                redirectAttrs.addFlashAttribute("flash_message", "registro exitoso! Continuar a iniciar sesion!");
                return "redirect:/loginVista";
            } else {
                logUtility.error("El usuario= " + user.getNickname() + " no fue registrado correctamente.");
                redirectAttrs.addFlashAttribute("error", "resgitro fallido! Intente de nuevo!");
                return "redirect:/register";
            }
            
    }
        
    @RequestMapping(value = "/borrandoUsuario",method = RequestMethod.GET)
    public String borrandoUsuario(registerVista form, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
            System.out.println("entro en controlador REGISTER");
        int error = 1;
        
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        
        String urlBorrar = "http://localhost:8084/SPRINGDESESPERADO/rest/borrar/usuario/"+user.getNickname();
        
        try {
 
		URL url = new URL(urlBorrar);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("DELETE");
		//conn.setRequestProperty("Content-Type", "application/xml");
 
//		OutputStream os = conn.getOutputStream();
//		os.write(input.getBytes());
//		os.flush();
//                
//                    System.out.println(conn.getOutputStream());
//                    System.out.println(input.getBytes());
                
//		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
//			throw new RuntimeException("Failed : HTTP error code : "
//				+ conn.getResponseCode());
//		}
 
		BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
 
		String output;
                String xmlRespuesta = null;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
                        xmlRespuesta = output;
		}                
 
		conn.disconnect();
                
                System.out.println("Output fuera del while: "+xmlRespuesta);
                
                Usuario respuesta = new Usuario();
                
                XStream xstream2 = new XStream();
                
                xstream2 = new XStream(new DomDriver());

                xstream2.alias("Usuario", Usuario.class);
            
                respuesta = (Usuario) xstream2.fromXML(xmlRespuesta);
                
                    System.out.println("Objeto respuesta nickName: "+respuesta.getNickname());
                
                if (respuesta.getNickname().startsWith("ERROR")) {
                    System.out.println("Uno de los campos del formulario usuario se dejo vacio");
                    logUtility.error("El usuario= " + user.getNickname() + " introdujo algun campo vacio.");
                    error = 0;
                }
 
	  }  catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	 }
            
            
            if (error == 1){
                logUtility.info("El usuario= " + user.getNickname() + " fue borrado correctamente.");
                redirectAttrs.addFlashAttribute("flash_message", "borrado exitoso!");
                session.removeAttribute("SESSION_CURRENT_USER");
                return "redirect:/home";
            } else {
                logUtility.info("El usuario= " + user.getNickname() + " no fue eliminado correctamente.");
                redirectAttrs.addFlashAttribute("error", "borrado fallido! Intente de nuevo!");
                return "redirect:/perfil";
            }
    }
}
        
//    @RequestMapping(value = "/borrandoUsuario",method = RequestMethod.DELETE )
//    public String borrandoUsuario(registerVista form, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
//        
//        int error = 0;
//        
//        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
//        
//        String urlBorrar = "http://localhost:8084/SPRINGDESESPERADO/rest/borrar/usuario/"+user.getNickname();
//        
//        try {
// 
//		URL url = new URL(urlBorrar);
//		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//		conn.setDoOutput(true);
//		conn.setRequestMethod("DELETE");
//		//conn.setRequestProperty("Content-Type", "application/xml");
// 
////		OutputStream os = conn.getOutputStream();
////		os.write(input.getBytes());
////		os.flush();
////                
////                    System.out.println(conn.getOutputStream());
////                    System.out.println(input.getBytes());
//                
////		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
////			throw new RuntimeException("Failed : HTTP error code : "
////				+ conn.getResponseCode());
////		}
// 
//		BufferedReader br = new BufferedReader(new InputStreamReader(
//				(conn.getInputStream())));
// 
//		String output;
//                String xmlRespuesta = null;
//		System.out.println("Output from Server .... \n");
//		while ((output = br.readLine()) != null) {
//			System.out.println(output);
//                        xmlRespuesta = output;
//		}                
// 
//		conn.disconnect();
//                
//                System.out.println("Output fuera del while: "+xmlRespuesta);
//                
//                Usuario respuesta = new Usuario();
//                
//                XStream xstream2 = new XStream();
//                
//                xstream2 = new XStream(new DomDriver());
//
//                xstream2.alias("Usuario", Usuario.class);
//            
//                respuesta = (Usuario) xstream2.fromXML(xmlRespuesta);
//                
//                    System.out.println("Objeto respuesta nickName: "+respuesta.getNickname());
//                
//                if (respuesta.getNickname().startsWith("ERROR")) {
//                    System.out.println("Uno de los campos del formulario usuario se dejo vacio");
//                    error = 0;
//                }
// 
//	  }  catch (MalformedURLException e) {
// 
//		e.printStackTrace();
// 
//	  } catch (IOException e) {
// 
//		e.printStackTrace();
// 
//	 }
//            
//            
//            if (error == 1){
//                redirectAttrs.addFlashAttribute("flash_message", "borrado exitoso!");
//                return "redirect:/home";
//            } else {
//                redirectAttrs.addFlashAttribute("error", "borrado fallido! Intente de nuevo!");
//                return "redirect:/perfil";
//            }
//        
//    }
//    
//}
