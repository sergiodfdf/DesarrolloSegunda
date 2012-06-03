/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.DAO;

import java.util.Collection;
import org.junit.*;
import static org.junit.Assert.*;
import prueba.modelo.BusinessException;
import prueba.modelo.Comentario;
import prueba.modelo.Etiqueta;

/**
 *
 * @author Antonio
 */
public class etiquetaDAOImplTest {
    
    private static etiquetaDAOImpl objetoEtiqueta = new etiquetaDAOImpl();
    private static comentarioDAOImpl objetoComentario = new comentarioDAOImpl();
    private static Etiqueta tag;
    
    public etiquetaDAOImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        tag = objetoComentario.instanciaEtiqueta();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        tag = null;
    }
    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }

    /**
     * Test de prueba donde se verifica la correcta insercion de un objeto de tipo etiqueta en la base de datos.
     */
    @Test
    public void testInsertEtiqueta() {
        
        System.out.println("insertEtiqueta probando el test");
        
        tag.setNombre("PruebaInsertEtiqueta");
        
        Boolean testTag = objetoEtiqueta.insertEtiqueta(tag);
        
        assertEquals("Etiqueta insertada correctamente",true, testTag);
        
    }
    
     /**
     * Test donde se verifica que no se realice la insercion de un objeto de tipo comentario vacio.
     */
    @Test(expected = BusinessException.class)
    public void testInsertEtiquetaVacia() {
        System.out.println("insertEtiqueta probando el test con una etiqueta vacia");
        
        tag.setNombre("");
        
        Boolean testTag = objetoEtiqueta.insertEtiqueta(tag);
        
        assertEquals("La etiqueta no fue insertada",false, testTag);
        
    }
    
    /**
     * Test de prueba donde se realiza la busqueda de una etiqueta que no existe en el sistema,
     * por lo que se espera que genere una excepcion.
     */
    @Test(expected = BusinessException.class)
    public void testExisteEtiqueta() {
        
        System.out.println("----------testExisteEtiqueta probando el test");
        
        tag.setNombre("EtiquetaQueNoExiste");
        
        Etiqueta testTag = objetoEtiqueta.existeEtiqueta(tag.getNombre());
        
        Assert.assertNull("La etiqueta generada no es nula", testTag.getNombre());
//        assertEquals("Etiqueta insertada correctamente",true, testTag);
        
    }

    /**
     * Test of deleteEtiqueta method, of class etiquetaDAOImpl.
     */
//    @Test
//    public void testDeleteEtiqueta() {
//        System.out.println("deleteEtiqueta");
//        String name = "";
//        etiquetaDAOImpl instance = new etiquetaDAOImpl();
//        boolean expResult = false;
//        boolean result = instance.deleteEtiqueta(name);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of findEtiqueta method, of class etiquetaDAOImpl.
     */
//    @Test
//    public void testFindEtiqueta() {
//        System.out.println("findEtiqueta");
//        String name = "";
//        etiquetaDAOImpl instance = new etiquetaDAOImpl();
//        Etiqueta expResult = null;
//        Etiqueta result = instance.findEtiqueta(name);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of listEtiqueta method, of class etiquetaDAOImpl.
     */
//    @Test
//    public void testListEtiqueta() {
//        System.out.println("listEtiqueta");
//        etiquetaDAOImpl instance = new etiquetaDAOImpl();
//        Collection expResult = null;
//        Collection result = instance.listEtiqueta();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of countEtiqueta method, of class etiquetaDAOImpl.
     */
//    @Test
//    public void testCountEtiqueta() {
//        System.out.println("countEtiqueta");
//        etiquetaDAOImpl instance = new etiquetaDAOImpl();
//        int expResult = 0;
//        int result = instance.countEtiqueta();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of etiquetaComentarios method, of class etiquetaDAOImpl.
     */
//    @Test
//    public void testEtiquetaComentarios() {
//        System.out.println("etiquetaComentarios");
//        String ruta = "";
//        etiquetaDAOImpl instance = new etiquetaDAOImpl();
//        Collection expResult = null;
//        Collection result = instance.etiquetaComentarios(ruta);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
