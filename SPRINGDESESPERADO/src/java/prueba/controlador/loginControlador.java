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
import java.util.Collection;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prueba.DAO.comentarioDAOImpl;
import prueba.DAO.usuarioDAOImpl;
import prueba.modelo.Comentario;
import prueba.modelo.Usuario;
import prueba.modelo.loginFormularioVista;
import prueba.modelo.usuarioLogin;

/**
 *
 * @author Antonio
 */

@Controller
public class loginControlador {
    
    private Logger logUtility = Logger.getLogger(getClass());
    
    @RequestMapping(value = "/loginVista",method = RequestMethod.GET )
    public String loginVista() {
        System.out.println("LoginController: Passing through GET login ...");
        return "loginVista";
    }
    
    @RequestMapping(value = "/loginVista",method = RequestMethod.POST )
    public ModelAndView loginVista(loginFormularioVista form, Model model, BindingResult result,RedirectAttributes redirectAttrs,HttpSession session) {
        System.out.println("LoginController: Passing through POST login ...");

        XStream xstream = new XStream();
            int error = 1;
            String mensajeVista = null;
            
            if (result.hasErrors()){
                System.out.println( "Object has validation errors" );
		} else {

                usuarioLogin user = new usuarioLogin();
                System.out.println("entro en controlador LOGIN por el post");
                System.out.println("Datos del formLoginController: "+form.getnickName());
                System.out.println("Datos del formLoginController: "+form.getClave());
                
                user.setNickname(form.getnickName());
                user.setClave(form.getClave());
                
             ///   XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("__","_");
                
//                XStream xstream = new XStream();
           //     xstream = new XStream(new DomDriver("UTF-8",replacer));
               xstream = new XStream(new DomDriver());
                xstream.alias("Login", usuarioLogin.class);

                String input = xstream.toXML(user);
                System.out.println("pasando por la interfaz");
                try {
 
		URL url = new URL("http://localhost:8084/SPRINGDESESPERADO/rest/login");
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
		System.out.println("Output from ServerLoginControlador .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
                        xmlRespuesta = output;
		}                
 
		conn.disconnect();
                
                System.out.println("Output fuera del while: "+xmlRespuesta);
                
                usuarioLogin respuesta = new usuarioLogin();
                
                XStream xstream2 = new XStream();
                
                xstream2 = new XStream(new DomDriver());

                xstream2.alias("Login", usuarioLogin.class);
            
                respuesta = (usuarioLogin) xstream2.fromXML(xmlRespuesta);
                
                    System.out.println("Objeto respuesta nickName: "+respuesta.getNickname());
                
                if (respuesta.getNickname().startsWith("ERROR")) {
                    
                    if (respuesta.getNickname().startsWith("ERROR 100")){
                        
                        System.out.println("El usuario tiene el token vencido.");
                        logUtility.error("El usuario= " + user.getNickname() + " tiene el token vencido.");
                        error = 0;
                        mensajeVista = "El usuario ya posee un token vigente";
                        
                    } else{
                    
                        System.out.println("Error en los datos introducidos");
                        error = 0;
                        logUtility.error("El usuario= " + user.getNickname() + " introdujo algun campo vacio.");
                        mensajeVista = "Error en los datos introducidos";
                    }
                }
 
	  }  catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	 }
            }
            
            if (error == 1){
                Usuario user = new Usuario();
                usuarioDAOImpl buscar = new usuarioDAOImpl();
        
                user = buscar.findUsuario(form.getnickName());
	        model.addAttribute("result", "succeed");
	        session.setAttribute("SESSION_CURRENT_USER", user);
	        redirectAttrs.addFlashAttribute("flash_message", "Login realizado con exito! Bienvenido!");
                logUtility.info("El usuario= " + user.getNickname() + " se logeo correctamente.");
                
                ModelAndView mav = new ModelAndView();
                
                comentarioDAOImpl com = new comentarioDAOImpl();
                
                Collection<Comentario> commentsRaiz = com.listComentariosDeRaiz();
                
                mav.setViewName("home");
                mav.addObject("comentariosEnListaRaiz", commentsRaiz);
                
	        return mav;
            } else {
                
                logUtility.error("El login del usuario no fue realizado con exito.");
                ModelAndView mav = new ModelAndView();
                mav.setViewName("loginVista");
                mav.addObject("error", mensajeVista);
                
//                model.addAttribute("result", "failed");
//        	redirectAttrs.addFlashAttribute("error", mensajeVista);
        	return mav;
            }
            
    }
    
    @RequestMapping(value = "/logout",method = RequestMethod.GET )
    public String logout(RedirectAttributes redirectAttrs, HttpSession session) {
        System.out.println("LoginController: Passing through GET login ...");
        logUtility.info("El usuario cerro sesion correctamente.");
        session.removeAttribute("SESSION_CURRENT_USER");
        redirectAttrs.addFlashAttribute("flash_message", "Sesion cerrada con exito!");
        return "redirect:/home";
    }
  
    
}
