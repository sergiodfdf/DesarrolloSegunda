/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.controlador;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import prueba.DAO.etiquetaDAO;
import prueba.DAO.etiquetaDAOImpl;
import prueba.DAO.usuarioDAO;
import prueba.DAO.usuarioDAOImpl;
import prueba.modelo.Comentario;
import prueba.modelo.Usuario;
import prueba.modelo.listaComentarios;

/**
 *
 * @author Antonio
 */

@Controller
@RequestMapping("/etiquetaComentarios")
public class etiquetaComentarios {
    
    private etiquetaDAO dao;
    String[] arreglo = new String [100];
    
    	@RequestMapping(value="{name}", method = RequestMethod.GET)
	public @ResponseBody listaComentarios getEtiquetaComentariosInXML(@PathVariable String name) {
            
                dao = new etiquetaDAOImpl();
                List<Comentario> comentarios = (List<Comentario>) dao.etiquetaComentarios(name);
                listaComentarios list = new listaComentarios(comentarios);
                
                return list;
            
        }
    
}
