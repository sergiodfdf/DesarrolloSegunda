/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.controlador;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
public class perfilControlador {
    
    private Logger logUtility = Logger.getLogger(getClass());
    
    @RequestMapping(value = "/perfil",method = RequestMethod.GET )
    public String loginVista() {
        System.out.println("PerfilController: Passing through GET login ...");
        return "perfil";
    }
    
    @RequestMapping(value = "/perfil",method = RequestMethod.POST )
    public String login(registerVista form, BindingResult result, RedirectAttributes redirectAttrs,HttpSession session) {
        
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
                }
 
	  }  catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	 }
            }
            
            if (error == 1){
                logUtility.info("El usuario= " + user.getNickname() + " actualizo correctamente su perfil.");
                redirectAttrs.addFlashAttribute("flash_message", "Actualizacion exitosa!");
                session.removeAttribute("SESSION_CURRENT_USER");
                session.setAttribute("SESSION_CURRENT_USER", user);
                return "redirect:/home";
            } else {
                redirectAttrs.addFlashAttribute("error", "Actualizacion fallido! Intente de nuevo!");
                logUtility.error("El usuario= " + user.getNickname() + " fallo actualizando su perfil.");
                return "redirect:/perfil";
            }
            
    }
    
}
