/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.controlador;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XStream11XmlFriendlyReplacer;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import prueba.DAO.comentarioDAOImpl;
import prueba.modelo.Comentario;
import prueba.modelo.Usuario;

/**
 *
 * @author Antonio
 */

@Controller
public class homeControlador {
    
    Comparator<String> comparator;
    
    @RequestMapping(value = "/home")
    public ModelAndView home() {
        
        System.out.println("HomeController: Passing through...");     
        
        ModelAndView mav = new ModelAndView();
                
        comentarioDAOImpl com = new comentarioDAOImpl();

        Collection<Comentario> commentsRaiz = com.listComentariosDeRaiz();

        mav.setViewName("home");
        mav.addObject("comentariosEnListaRaiz", commentsRaiz);
        
        return mav;
    }
    
    @RequestMapping(value = "/compare")
	public String compare(@RequestParam("input1") String input1, @RequestParam("input2") String input2, Model model) {
        System.out.println("HomeController: Passing through COMPAREEEE");
		int result = comparator.compare(input1, input2);
                System.out.println("Daniel dice mensaje: "+input1);
                System.out.println("Daniel dice otro mensaje: "+input2);
        String inEnglish = (result < 0) ? "less than" : (result > 0 ? "greater than" : "equal to");
 
        String output = "According to our Comparator, '" + input1 + "' is " + inEnglish + "'" + input2 + "'";
        System.out.println("output="+output);
        model.addAttribute("output", output);
        
        return "compareResult";
	}
    
}
