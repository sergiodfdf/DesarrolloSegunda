/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.controlador;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.binary.Token;
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
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import prueba.DAO.*;
import prueba.modelo.*;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio
 */
@Controller
public class comentarioControlador {
    
    private Logger logUtility = Logger.getLogger(getClass());
    
    @RequestMapping(value = "/comentarioVista", method = RequestMethod.GET)
	public String comentarioVista(Model model) {
		System.out.println("comentarioControladorGet: Passing through ...");
//		Collection<Tweet> tweets = tweetService.listTweets();
//		model.addAttribute("tweetList", tweets);
		return "comentarioVista";
	}
    
    @RequestMapping(value = "/comentarioVista", method = RequestMethod.POST)
	public String comentarioVista(@RequestParam String message, String etiquetas, Model model, RedirectAttributes redirectAttrs, HttpSession session) {
        
		System.out.println("comentarioControladorPost: Passing through ...");
		System.out.println("message = " + message);
                System.out.println("etiquetas = "+etiquetas);
                
                XStream xstream = new XStream();
                int error = 1;
                String mensajeVista = null;
                Comentario respuesta = new Comentario();

                Comentario comment = new Comentario();

		comment.setmensaje(message);
		Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
		comment.setnickName(user.getNickname());
                
                System.out.println("Usuario: "+user.getNickname());
		
                Tokken tokken = new Tokken();
                tokkenDAOImpl buscar = new tokkenDAOImpl();
                
                tokken = buscar.findTokken(user.getNickname());
                comment.setId(tokken.getTokken());
                comment.setFecha_creacion(tokken.getFecha_creacion());
                
                comment.setAdjunto("no");
                
                if(message.contains("http://www.youtube.com")){
                    comment.setmensaje("Video de Youtube");
                    comment.setAdjunto(message);
                }
                
                XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("__","_");
                
//                XStream xstream = new XStream();
                xstream = new XStream(new DomDriver("UTF-8",replacer));

                xstream.alias("Comentario", Comentario.class);

                String input = xstream.toXML(comment);
                System.out.println("pasando por la interfaz");
                try {
 
		URL url = new URL("http://localhost:8084/SPRINGDESESPERADO/rest/insertarComentario");
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

                XStream xstream2 = new XStream();
                
                xstream2 = new XStream(new DomDriver());

                xstream2.alias("Comentario", Comentario.class);
            
                respuesta = (Comentario) xstream2.fromXML(xmlRespuesta);
                
                    System.out.println("Objeto respuesta IDcomentario: "+respuesta.getId());
                
                if (respuesta.getId().startsWith("ERROR")) {
                    
                    if (respuesta.getId().startsWith("ERROR 100")){
                        
                        System.out.println("El usuario tiene el token vencido");
                        error = 0;
                        logUtility.error("El usuario= " + user.getNickname() + " tiene el token vencido.");
                        mensajeVista = "El usuario tiene el token vencido";
                        
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
            
            if (error == 1){
                
                Etiqueta tag = new Etiqueta();
                etiquetaDAO dao;
                dao = new etiquetaDAOImpl();
                
                if (!etiquetas.equals("")) {
                    
                    String [] arregloEtiquetas;
                    int cantidad;
                    
                    arregloEtiquetas = etiquetas.split(";");
                    
                    cantidad = arregloEtiquetas.length;
                    System.out.println("La cantidad del split es: "+cantidad);
                    for (int i=0; i<cantidad;++i){
                    
                        tag.setNombre(arregloEtiquetas[i]);
                        tag.setCommentario(respuesta.getId());

                        dao.insertEtiqueta(tag);

                        comentarioDAOImpl daoComentarioEtiqueta = new comentarioDAOImpl();

                        daoComentarioEtiqueta.insertarComentarioEtiquetaSuperColumn(tag);
                        daoComentarioEtiqueta.insertarEtiquetaComentariosSuperColumn(tag, respuesta);
                    
                    }
                    
                    logUtility.info("El usuario= " + user.getNickname() + " realizo el comentario: "+respuesta.getId()+" correctamente con etiquetas.");
                    redirectAttrs.addFlashAttribute("flash_message", "Comentario realizado de manera exitosa con etiquetas!");
                    return "redirect:/comentariosDeUsuario";
                    
                } else {
                    
                    logUtility.info("El usuario= " + user.getNickname() + " realizo el comentario: "+respuesta.getId()+" correctamente sin etiquetas.");
                    redirectAttrs.addFlashAttribute("flash_message", "Comentario realizado de manera exitosa sin etiquetas!");
                    return "redirect:/comentariosDeUsuario";
                    
                }
            } else {
                redirectAttrs.addFlashAttribute("error", mensajeVista);
                return "redirect:/comentarioVista";
            }
            
    
	}
    
    @RequestMapping(value="comentariosDeUsuario", method=RequestMethod.GET)
    public ModelAndView getComentariosDeUsuario(HttpSession session, String message) {
	
        System.out.println("ComentarioController: Passing through ...");
        ModelAndView mav = new ModelAndView();
        System.out.println("valor de la variable message: "+message);
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String usuario = user.getNickname();
        String dirURL = "http://localhost:8084/SPRINGDESESPERADO/rest/buscarComentarios/"+usuario;
        
        usuarioDAOImpl buscar = new usuarioDAOImpl();
        
        try {
 
		URL url = new URL(dirURL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("GET");
//		conn.setRequestProperty("Content-Type", "application/xml");
                
                conn.connect();
 
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
                
                listaComentarios respuesta = new listaComentarios();

                XStream xstream2 = new XStream();
                
                xstream2 = new XStream(new DomDriver());

                xstream2.alias("comentarios", listaComentarios.class);
                xstream2.alias("comentario", Comentario.class);
            
//                respuesta = (listaComentarios) xstream2.fromXML(xmlRespuesta);
                
//                    System.out.println("Objeto respuesta IDcomentario: "+respuesta.getId());
                
//                if (respuesta.getId().startsWith("ERROR")) {
//                    
//                    if (respuesta.getId().equalsIgnoreCase("ERROR 100")){
//                        
//                        System.out.println("El usuario ya posee un token vigente");
//                        error = 0;
//                        mensajeVista = "El usuario ya posee un token vigente";
//                        
//                    } else{
//                    
//                        System.out.println("Error en los datos introducidos");
//                        error = 0;
//                        mensajeVista = "Error en los datos introducidos";
//                    }
//                }
                comentarioDAOImpl com = new comentarioDAOImpl();
                
                Collection<Comentario> comments = com.listComentariosDeUsuario(user.getNickname());
                
                mav.setViewName("comentariosDeUsuario");
                mav.addObject("comentariosEnLista", comments);
        
	  }  catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	 }
        
//        Collection<Comentario> comments = buscar.comentariosUsuario("ad");
//        mav.setViewName("comentariosDeUsuario");
//        mav.addObject("comentariosEnLista", comments);
		
        return mav;
    }
    
    @RequestMapping(value = "/replyDeUnComentario", method = RequestMethod.GET)
	public String replyDeUnComentario(Model model, String messageee) {
		System.out.println("comentarioControladorGet: Passing through ...");
                System.out.println("Contenido de message: "+messageee);
//		Collection<Tweet> tweets = tweetService.listTweets();
//		model.addAttribute("tweetList", tweets);
                String variable = "replyDeUnComentario?message="+messageee;
                System.out.println("VALOR DE LA VARIABLE VARIABLE: "+variable);
		return "replyDeUnComentario";
	}
    
    @RequestMapping(value = "/replyDeUnComentario", method = RequestMethod.POST)
	public String replyDeUnComentario(@RequestParam String message, String messageee, String etiquetas, Model model, RedirectAttributes redirectAttrs, HttpSession session, HttpServletRequest request) {
                
                //System.out.println("El request contiene: "+request);
                String req = request.toString();
                String [] comandos;
                String [] comandos1;
                String [] comandosFinal;
                
                System.out.println("El request contiene: "+req);
                System.out.println("Contenido de requestURL: "+request.getRequestURL());
		System.out.println("comentarioControladorPost: Passing through ...");
		System.out.println("message = " + message);
                System.out.println("etiquetas = "+etiquetas);
                System.out.println("MESAGEEE = "+messageee);
                
                
                comandos = req.split("Headers:");
                System.out.println("Posicion 0: "+comandos[0]);
                System.out.println("Posicion 1: "+comandos[1]);
                
                req = comandos[1];
                
                comandos1 = req.split("Name: referer	Value: http");
                System.out.println("Posicion 0 en comandos1: "+comandos1[0]);
                System.out.println("Posicion 1 en comandos1: "+comandos1[1]);
                
                req = comandos1[1];
                
                comandosFinal = req.split("://");
                System.out.println("Posicion 0: "+comandosFinal[0]);
                System.out.println("Posicion 1: "+comandosFinal[1]);
                
                System.out.println("Posicion 0 en comandos1: "+comandosFinal[0]);
                System.out.println("Posicion 1 en comandos1: "+comandosFinal[1]);
                
                req = comandosFinal[1];
                
                comandosFinal = req.split("localhost:8084/");
                System.out.println("Posicion 0: "+comandosFinal[0]);
                System.out.println("Posicion 1: "+comandosFinal[1]);
                
                System.out.println("Posicion 0 en comandos1: "+comandosFinal[0]);
                System.out.println("Posicion 1 en comandos1: "+comandosFinal[1]);
                
                req = comandosFinal[1];
                
                comandosFinal = req.split("SPRINGDESESPERADO/");
                System.out.println("Posicion 0: "+comandosFinal[0]);
                System.out.println("Posicion 1: "+comandosFinal[1]);
                
                System.out.println("Posicion 0 en comandos1: "+comandosFinal[0]);
                System.out.println("Posicion 1 en comandos1: "+comandosFinal[1]);
                
                req = comandosFinal[1];
                
                comandosFinal = req.split("replyDeUnComentario");
                System.out.println("Posicion 0: "+comandosFinal[0]);
                System.out.println("Posicion 1: "+comandosFinal[1]);
                
                System.out.println("Posicion 0 en comandos1: "+comandosFinal[0]);
                System.out.println("Posicion 1 en comandos1: "+comandosFinal[1]);
                
                req = comandosFinal[1];
                
                String cadenaNueva = req.substring(1, req.length());
                System.out.println("String cadenaNUeva: "+cadenaNueva);
                
                comandosFinal = req.split("messageee=");
                System.out.println("Posicion 0: "+comandosFinal[0]);
                System.out.println("Posicion 1: "+comandosFinal[1]); //aqui ya tenemos el ID del comentario a hacer reply
                
                System.out.println("Posicion 0 en comandos1: "+comandosFinal[0]);
                System.out.println("Posicion 1 en comandos1: "+comandosFinal[1]);
                
                req = comandosFinal[1];                
                
                comandosFinal = req.split("	Name:");
                System.out.println("Posicion 0: "+comandosFinal[0]);
                System.out.println("Posicion 1: "+comandosFinal[1]); //aqui ya tenemos el ID del comentario a hacer reply
                
                System.out.println("Posicion 0 en comandos1: "+comandosFinal[0]);
                System.out.println("Posicion 1 en comandos1: "+comandosFinal[1]);
                
                String substring = comandosFinal[0];
                String cadenaNuevaaa = substring.substring(0, substring.length()-1);
                System.out.println("Longitud del substring: "+substring.length());
                System.out.println("String cadenaNUeva: "+cadenaNueva);
                
                String hacerReply = cadenaNuevaaa;
                
                XStream xstream = new XStream();
                int error = 1;
                String mensajeVista = null;
                Comentario respuesta = new Comentario();

                Comentario buscarComentario = new Comentario();
                
                comentarioDAOImpl looking = new comentarioDAOImpl(); 
                
                Comentario comment = new Comentario();
                
                buscarComentario = looking.findComentario(hacerReply);
                
		comment.setmensaje(message);
		Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
		comment.setnickName(user.getNickname());
                
                System.out.println("Usuario: "+user.getNickname());
		
                Tokken tokken = new Tokken();
                tokkenDAOImpl buscar = new tokkenDAOImpl();
                
                tokken = buscar.findTokken(user.getNickname());
                comment.setId(tokken.getTokken());
                comment.setFecha_creacion(tokken.getFecha_creacion());
                
                comment.setAdjunto("no");
                comment.setReply(hacerReply);
                
                XmlFriendlyNameCoder replacer = new XmlFriendlyNameCoder("__","_");
                
//                XStream xstream = new XStream();
                xstream = new XStream(new DomDriver("UTF-8",replacer));

                xstream.alias("Comentario", Comentario.class);

                String input = xstream.toXML(comment);
                System.out.println("pasando por la interfaz");
                try {
 
		URL url = new URL("http://localhost:8084/SPRINGDESESPERADO/rest/insertarReply");
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

                XStream xstream2 = new XStream();
                
                xstream2 = new XStream(new DomDriver());

                xstream2.alias("Comentario", Comentario.class);
            
                respuesta = (Comentario) xstream2.fromXML(xmlRespuesta);
                
                    System.out.println("Objeto respuesta IDcomentario: "+respuesta.getId());
                
                if (respuesta.getId().startsWith("ERROR")) {
                    
                    if (respuesta.getId().startsWith("ERROR 100")){
                        
                        System.out.println("El usuario tiene el token vencido");
                        logUtility.error("El usuario= " + user.getNickname() + " tiene el token vencido.");
                        error = 0;
                        mensajeVista = "El usuario tiene el token vencido";
                        
                    } else{
                    
                        System.out.println("Error en los datos introducidos");
                        logUtility.error("El usuario= " + user.getNickname() + " introdujo algun campo vacio en el reply.");
                        error = 0;
                        mensajeVista = "Error en los datos introducidos";
                    }
                }
 
	  }  catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	 }
            
            if (error == 1){
                
                Etiqueta tag = new Etiqueta();
                etiquetaDAO dao;
                dao = new etiquetaDAOImpl();
                
                tag.setNombre(etiquetas);
                tag.setCommentario(respuesta.getId());
                
                dao.insertEtiqueta(tag);
                
                comentarioDAOImpl daoComentarioEtiqueta = new comentarioDAOImpl();
                
                daoComentarioEtiqueta.insertarComentarioEtiquetaSuperColumn(tag);
                daoComentarioEtiqueta.insertarEtiquetaComentariosSuperColumn(tag, respuesta);
                
                logUtility.info("El usuario= " + user.getNickname() + " realizo el comentario reply: "+respuesta.getId()+" correctamente.");
                redirectAttrs.addFlashAttribute("flash_message", "Comentario realizado de manera exitosa!");
                return "redirect:/home";
            } else {
                redirectAttrs.addFlashAttribute("error", mensajeVista);
                return "redirect:/home";
            }
//                redirectAttrs.addFlashAttribute("flash_message", "Comentario realizado de manera exitosa!");
//                return "redirect:/comentarioVista";            
    
}
    
    @RequestMapping(value = "/borrandoComentario",method = RequestMethod.GET)
    public String borrandoUsuario(registerVista form, String messageee, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
            System.out.println("entro en controlador borrarComentario");
        int error = 1;
        String mensajeVista = null;
        
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String comentarioID = messageee;
        System.out.println("ID de ComentarioID: "+comentarioID);
        
        String urlBorrar = "http://localhost:8084/SPRINGDESESPERADO/rest/borrar/comentario/"+comentarioID;
        
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
                
                Comentario respuesta = new Comentario();
                
                XStream xstream2 = new XStream();
                
                xstream2 = new XStream(new DomDriver());

                xstream2.alias("Comentario", Comentario.class);
            
                respuesta = (Comentario) xstream2.fromXML(xmlRespuesta);
                
                    System.out.println("Objeto respuesta nickName: "+respuesta.getId());
                
                if (respuesta.getId().startsWith("ERROR")) {
                    if (respuesta.getId().startsWith("ERROR 100")){
                        
                        System.out.println("El usuario tiene el token vencido");
                        logUtility.error("El usuario= " + user.getNickname() + " tiene el token vencido.");
                        error = 0;
                        mensajeVista = "El usuario tiene el token vencido";
                        
                    } else{
                    
                        System.out.println("Error en los datos introducidos");
                        error = 0;
                        mensajeVista = "Error en los datos introducidos";
                    }
                }
 
	  }  catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	 }
            
            
            if (error == 1){
                redirectAttrs.addFlashAttribute("flash_message", "borrado exitoso!");
                logUtility.info("El usuario= " + user.getNickname() + " borro el comentario: "+comentarioID+" correctamente.");
                return "redirect:/comentariosDeUsuario";
            } else {
                redirectAttrs.addFlashAttribute("error", mensajeVista);
                return "redirect:/comentariosDeUsuario";
            }
    }
    
    @RequestMapping(value="comentariosDeRaiz", method=RequestMethod.GET)
    public ModelAndView getComentariosDeRaiz(HttpSession session) {
	
        System.out.println("ComentarioController: Passing through ...");
        ModelAndView mav = new ModelAndView();
        //System.out.println("valor de la variable message: "+messageee);
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String usuario = user.getNickname();
        
                comentarioDAOImpl com = new comentarioDAOImpl();
                
                Collection<Comentario> commentsRaiz = com.listComentariosDeRaiz();
                
                mav.setViewName("comentariosDeRaiz");
                mav.addObject("comentariosEnListaRaiz", commentsRaiz);       
		
        return mav;
    }
    
    @RequestMapping(value="replysDeComentario", method=RequestMethod.GET)
    public ModelAndView getReplysDeComentario(HttpSession session, String messageee) {
	
        System.out.println("replysDeComentarioController: Passing through ...");
        ModelAndView mav = new ModelAndView();
        System.out.println("valor de la variable message: "+messageee);
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String usuario = user.getNickname();
                        comentarioDAOImpl com = new comentarioDAOImpl();
                
                Collection<Comentario> comments = com.listComentariosDeReply(messageee);
                
                mav.setViewName("replysDeComentario");
                mav.addObject("replysEnLista", comments);        
		
        return mav;
    }
    
    @RequestMapping(value = "/MeGusta",method = RequestMethod.GET)
    public String puntuarMeGusta(registerVista form, String messageee, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
            System.out.println("entro en controlador puntuarMeGusta");
        int error = 1;
        String mensajeVista = null;
        
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String comentarioID = messageee;
        System.out.println("ID de ComentarioID: "+comentarioID);
        
        Comentario meGusta = new Comentario();
        meGusta.setAdjunto("1");
        meGusta.setId(messageee);
        meGusta.setnickName(user.getNickname());
                
                XStream xstream = new XStream();
                xstream = new XStream(new DomDriver());
                xstream.alias("Comentario", Comentario.class);

                String input = xstream.toXML(meGusta);
                System.out.println("pasando por la interfaz");
                try {
 
		URL url = new URL("http://localhost:8084/SPRINGDESESPERADO/rest/comentario/puntuar");
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
                
                Comentario respuesta = new Comentario();
                
                System.out.println("Output fuera del while: "+xmlRespuesta);                

                XStream xstream2 = new XStream();
                
                xstream2 = new XStream(new DomDriver());

                xstream2.alias("Comentario", Comentario.class);
            
                respuesta = (Comentario) xstream2.fromXML(xmlRespuesta);
                
                    System.out.println("Objeto respuesta IDcomentario: "+respuesta.getId());
                
//                if (respuesta.getId().startsWith("ERROR")) {
//                    if (respuesta.getId().startsWith("ERROR 100")){
//                        
//                        System.out.println("El usuario tiene el token vencido");
//                        logUtility.error("El usuario= " + user.getNickname() + " tiene el token vencido.");
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
 
	  }  catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	 }
            
            
            if (error == 1){
                redirectAttrs.addFlashAttribute("flash_message", "Voto MeGusta exitoso!");
                logUtility.info("El usuario= " + user.getNickname() + " voto me gusta en el comentario: "+comentarioID+" correctamente.");
                return "redirect:/home";
            } 
            else {
                redirectAttrs.addFlashAttribute("error", mensajeVista);
                return "redirect:/comentariosDeUsuario";
            }
    }
    
    @RequestMapping(value = "/NoMeGusta",method = RequestMethod.GET)
    public String puntuarNoMeGusta(registerVista form, String messageee, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
            System.out.println("entro en controlador puntuarMeGusta");
        int error = 1;
        String mensajeVista = null;
        
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String comentarioID = messageee;
        System.out.println("ID de ComentarioID: "+comentarioID);
        
        Comentario meGusta = new Comentario();
        meGusta.setAdjunto("0");
        meGusta.setId(messageee);
        meGusta.setnickName(user.getNickname());
                
                XStream xstream = new XStream();
                xstream = new XStream(new DomDriver());
                xstream.alias("Comentario", Comentario.class);

                String input = xstream.toXML(meGusta);
                System.out.println("pasando por la interfaz");
                try {
 
		URL url = new URL("http://localhost:8084/SPRINGDESESPERADO/rest/comentario/puntuar");
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
                
                Comentario respuesta = new Comentario();
                
                System.out.println("Output fuera del while: "+xmlRespuesta);                

                XStream xstream2 = new XStream();
                
                xstream2 = new XStream(new DomDriver());

                xstream2.alias("Comentario", Comentario.class);
            
                respuesta = (Comentario) xstream2.fromXML(xmlRespuesta);
                
                    System.out.println("Objeto respuesta IDcomentario: "+respuesta.getId());
                
//                if (respuesta.getId().startsWith("ERROR")) {
//                    if (respuesta.getId().startsWith("ERROR 100")){
//                        
//                        System.out.println("El usuario tiene el token vencido");
//                        logUtility.error("El usuario= " + user.getNickname() + " tiene el token vencido.");
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
 
	  }  catch (MalformedURLException e) {
 
		e.printStackTrace();
 
	  } catch (IOException e) {
 
		e.printStackTrace();
 
	 }
            
            
            if (error == 1){
                redirectAttrs.addFlashAttribute("flash_message", "Voto NoMeGusta exitoso!");
                logUtility.info("El usuario= " + user.getNickname() + " voto no me gusta en el comentario: "+comentarioID+" correctamente.");
                return "redirect:/home";
            } 
            else {
                redirectAttrs.addFlashAttribute("error", mensajeVista);
                return "redirect:/comentariosDeUsuario";
            }
    }
    
    @RequestMapping(value = "/verPuntuacion",method = RequestMethod.GET)
    public String getVerPuntuacion(registerVista form, String messageee, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
            System.out.println("entro en controlador puntuarMeGusta");

        String mensajeVista = null;
        
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String comentarioID = messageee;
        System.out.println("ID de ComentarioID: "+comentarioID);
        
                comentarioDAO daoMeGusta = new comentarioDAOImpl();
                List<Etiqueta> tag = (List<Etiqueta>) daoMeGusta.comentarioPuntuacionMeGusta(messageee);
                listaEtiquetas listMeGusta = new listaEtiquetas(tag); 
                
                int Gustar = listMeGusta.getCount();
                System.out.println("Cantidad de MeGusta: "+Gustar);
                
                comentarioDAO daoNoMeGusta = new comentarioDAOImpl();
                List<Etiqueta> tag1 = (List<Etiqueta>) daoNoMeGusta.comentarioPuntuacionNoMeGusta(messageee);
                listaEtiquetas listMeGusta1 = new listaEtiquetas(tag1); 
                
                int NoGustar = listMeGusta1.getCount();
                System.out.println("Cantidad de NoMeGusta: "+NoGustar);
                
                String mensajeMeGusta = "Cantidad de MeGusta: "+Gustar+", Cantidad de NoMeGusta: "+NoGustar;
            
                redirectAttrs.addFlashAttribute("flash_message", mensajeMeGusta);
//                logUtility.info("El usuario= " + user.getNickname() + " voto no me gusta en el comentario: "+comentarioID+" correctamente.");
                return "redirect:/home";

    }
    
    @RequestMapping(value = "/buscandoEtiqueta",method = RequestMethod.GET)
    public ModelAndView getBuscandoEtiqueta(registerVista form, String messageee, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
            System.out.println("entro en controlador buscandoEtiqueta");
        
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String comentarioID = messageee;
        //System.out.println("ID de ComentarioID: "+comentarioID);
        
                etiquetaDAO dao = new etiquetaDAOImpl();
                Collection<Etiqueta> etiquetas = dao.listEtiqueta();
                Collection<Etiqueta> comments = etiquetas;
                
                String mensaje = "Chevere que chevere.";
            
                redirectAttrs.addFlashAttribute("flash_message", mensaje);
//                logUtility.info("El usuario= " + user.getNickname() + " voto no me gusta en el comentario: "+comentarioID+" correctamente.");
                
                ModelAndView mav = new ModelAndView();
                
                mav.setViewName("busquedaEtiqueta");
                mav.addObject("listaDeEtiquetas", comments);
                
                return mav;
                

    }
    
    @RequestMapping(value = "/buscandoComentario",method = RequestMethod.GET)
    public ModelAndView getBuscandoComentario(registerVista form, String messageee, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
            System.out.println("entro en controlador buscandoCOmentario");
        
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String comentarioID = messageee;
        //System.out.println("ID de ComentarioID: "+comentarioID);
        
                comentarioDAO dao = new comentarioDAOImpl();
                Collection<Comentario> comentarios = dao.listComentario();
                Collection<Comentario> comments = comentarios;
                
                String mensaje = "Chevere que chevere.";
            
                redirectAttrs.addFlashAttribute("flash_message", mensaje);
//                logUtility.info("El usuario= " + user.getNickname() + " voto no me gusta en el comentario: "+comentarioID+" correctamente.");
                
                ModelAndView mav = new ModelAndView();
                
                mav.setViewName("busquedaComentario");
                mav.addObject("listaDeComentarios", comments);
                
                return mav;
                

    }
    
    @RequestMapping(value = "/buscandoUsuario",method = RequestMethod.GET)
    public ModelAndView getBuscandoUsuario(registerVista form, String messageee, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
            System.out.println("entro en controlador buscandoUsuario");
        
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String comentarioID = messageee;
        //System.out.println("ID de ComentarioID: "+comentarioID);
        
                usuarioDAO dao = new usuarioDAOImpl();
                Collection<Usuario> usuarios = dao.listUsuario();
                Collection<Usuario> comments = usuarios;
                
                String mensaje = "Chevere que chevere.";
            
                redirectAttrs.addFlashAttribute("flash_message", mensaje);
//                logUtility.info("El usuario= " + user.getNickname() + " voto no me gusta en el comentario: "+comentarioID+" correctamente.");
                
                ModelAndView mav = new ModelAndView();
                
                mav.setViewName("busquedaUsuario");
                mav.addObject("listaDeUsuarios", comments);
                
                return mav;
                

    }
    
    @RequestMapping(value = "/verVideos",method = RequestMethod.GET)
    public ModelAndView getVerVideos(registerVista form, String messageee, BindingResult result, RedirectAttributes redirectAttrs, HttpSession session) {
            System.out.println("entro en controlador buscandoVideo");
        
        Usuario user = (Usuario)session.getAttribute("SESSION_CURRENT_USER");
        String comentarioID = messageee;
        //System.out.println("ID de ComentarioID: "+comentarioID);
        
                comentarioDAOImpl dao = new comentarioDAOImpl();
                Collection<Comentario> comentarios = dao.listComentarioVideos();
                Collection<Comentario> comments = comentarios;
                
                String mensaje = "Chevere que chevere.";
            
                redirectAttrs.addFlashAttribute("flash_message", mensaje);
//                logUtility.info("El usuario= " + user.getNickname() + " voto no me gusta en el comentario: "+comentarioID+" correctamente.");
                
                ModelAndView mav = new ModelAndView();
                
                mav.setViewName("busquedaVideo");
                mav.addObject("listaDeVideos", comments);
                
                return mav;
                

    }
    
}
