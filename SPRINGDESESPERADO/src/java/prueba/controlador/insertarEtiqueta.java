/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.controlador;

/**
 *
 * @author Antonio
 */

import java.io.StringReader;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.Marshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import prueba.DAO.*;
import prueba.modelo.Etiqueta;

@Controller
public class insertarEtiqueta {
    
    private etiquetaDAO dao;
    private Logger logUtility = Logger.getLogger(getClass());
    
    @RequestMapping(value = "/insertarEtiqueta", method = RequestMethod.POST, consumes = "application/xml", produces="application/xml")
        
    	public @ResponseBody Etiqueta insertarUsuarioInXML(@RequestBody Etiqueta entrada) {
            
                System.out.println("hola InsertarEtiqueta");

//                StreamSource source = new StreamSource(new StringReader(entrada));
//                Jaxb2Marshaller prueba = new Jaxb2Marshaller();
                System.out.println(entrada.toString());
//                Usuario e = (Usuario) prueba.unmarshal(source);
//                System.out.println(entrada.toString());
                //System.out.println(jaxb2Marshaller.unmarshal(source));
//                Usuario e = (Usuario) jaxb2Marshaller.unmarshal(source);

		Etiqueta tag = new Etiqueta();
                
                dao = new etiquetaDAOImpl();
                System.out.println("Nickname: "+ entrada.getNombre());
                
                if (entrada.getNombre().isEmpty()){
                   entrada.setNombre("ERROR 012: Nombre Nulo");
                   logUtility.error("El nombre de la etiqueta es nulo.");
                                                 
                   return entrada;
               } 
                
                dao.insertEtiqueta(entrada);
                logUtility.info("La etiqueta: "+entrada.getNombre()+" se inserto correctamente en el sistema.");
                //user = dao.findUsuario(e.getNickname());                
		
		return entrada;

	}
    
}
