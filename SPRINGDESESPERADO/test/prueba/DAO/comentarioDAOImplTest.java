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
import prueba.modelo.Usuario;

/**
 *
 * @author Antonio
 */
public class comentarioDAOImplTest {
    
    private static comentarioDAOImpl objetoComentario = new comentarioDAOImpl();
    private static Comentario comment;
    
    public comentarioDAOImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        comment = objetoComentario.instancia();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }

    /**
     * Test de prueba donde se verifica la correcta insercion de un objeto de tipo comentario en la base de datos.
     */
    @Test
    public void testInsertComentario() {
        System.out.println("----------insertComentario probando el test");
        
        comment.setId("11");
        comment.setAdjunto("adjunto");
        comment.setFecha_creacion("31/12/2000");
        comment.setReply("0");
        comment.setmensaje("Comentario realizado desde el testInsertComentario");
        comment.setnickName("NicknamePrueba");
        
            Boolean testComment = objetoComentario.insertComentario(comment);
        
////////        Assert.assertNotNull("El usuario generado es nulo", user.getNickname());
            assertEquals("Comentario insertado correctamente",true, testComment);
    }
    
    /**
     * Test donde se verifica que no se realice la insercion de un objeto de tipo comentario vacio.
     */
    @Test(expected = BusinessException.class)
    public void testInsertComentarioVacio() {
        System.out.println("----------insertComentario probando el test con comentario vacio");
        
        comment.setId("");
        comment.setAdjunto("");
        comment.setFecha_creacion("");
        comment.setReply("");
        comment.setmensaje("");
        comment.setnickName("");
        
        Boolean testComment = objetoComentario.insertComentario(comment);
        
        assertEquals("El comentario no fue insertado",false, testComment);
        
    }
    
    /**
     * Test donde se verifica que un usuario no podra realizar un comentario sin tener un
     * token asignado que se encuentre vigente, en este caso, el usuario creado en esta
     * prueba no tiene ningun token asignado, por lo que se espera que genere una excepcion.
     */
    @Test(expected = BusinessException.class)
    public void realizarComentarioSinToken() {
        
        System.out.println("----------realizarComentarioSinToken probando el test");
        
        Usuario user = new Usuario();
        usuarioDAOImpl inserta = new usuarioDAOImpl();
        
        user.setApellido("PruebaComentarioSinToken");
        user.setBiografia("PruebaComentarioSinToken");
        user.setClave("PruebaComentarioSinToken");
        user.setCorreo("PruebaComentarioSinToken");
        user.setFecha_nac("PruebaComentarioSinToken");
        user.setFoto("PruebaComentarioSinToken");
        user.setNickname("PruebaComentarioSinToken");
        user.setNombre("PruebaComentarioSinToken");
        user.setPais("PruebaComentarioSinToken");
        
            Boolean testUser = inserta.insertUsuario(user);
            Boolean vigencia = inserta.comprobarVigenciaToken(user);
            
        assertEquals("El usuario no tiene token asignado",false, vigencia);
        
        comment.setId("13");
        comment.setAdjunto("adjunto");
        comment.setFecha_creacion("31/12/2000");
        comment.setReply("0");
        comment.setmensaje("Comentario realizado desde el realizarComentarioSinToken");
        comment.setnickName("PruebaComentarioSinToken");
        
        Boolean testComment = objetoComentario.insertComentario(comment);
        
        assertEquals("El comentario fue insertado correctamente",true, testComment);
        
        
    }
    
    /**
     * Test donde se determina que un usuario no tiene ningun token asignado, por lo
     * que no podra realizar ninguna operacion de insercion, eliminacion o modificacion.
     */
    @Test(expected = BusinessException.class)
    public void verificarUsuarioSinToken() {
        
        System.out.println("----------verificarUsuarioSinToken probando el test");
        
        Usuario user = new Usuario();
        usuarioDAOImpl inserta = new usuarioDAOImpl();
        
        user.setNickname("PruebaComentarioSinToken");
        
            //Boolean testUser = inserta.insertUsuario(user);
            Boolean vigencia = inserta.comprobarVigenciaToken(user);
            
        assertEquals("El usuario no tiene token asignado",false, vigencia);        
        
    }
    
    /**
     * Test de prueba donde se intenta insertar un reply a un comentario que no existe.
     */
    @Test(expected = BusinessException.class)
    public void testInsertReply() {
        System.out.println("----------testInsertReply probando el test");
        
        comment.setId("1333");
        comment.setAdjunto("adjunto");
        comment.setFecha_creacion("31/12/2000");
        comment.setReply("100000");
        comment.setmensaje("Intento de comentario reply realizado desde el testInsertComentario");
        comment.setnickName("NicknamePrueba");
        
            Comentario testComment = objetoComentario.buscarComentario(comment.getReply());
            
            Boolean testCommentInsert = objetoComentario.insertComentario(comment);
        
            Assert.assertNotNull("El Comentario generado es nulo", testComment.getId());
//            assertEquals("Comentario insertado correctamente",true, testComment);
    }
    
     /**
     * Test de prueba donde se busca realizar la eliminacion de un comentario que no a sido
     * realizado por el usuario especificado, arrojando una excepcion.
     */
    @Test(expected = BusinessException.class)
    public void borrarComentarioAjeno() {
        System.out.println("----------borrarComentarioAjeno probando el test");
        
        Usuario user = new Usuario();
        usuarioDAOImpl inserta = new usuarioDAOImpl();
        
        user.setNickname("PruebaComentarioSinToken");
        
        comment.setId("11");
        
        Comentario testComment = objetoComentario.buscarComentario(comment.getId());

        Boolean comparar = objetoComentario.validarComentarioUsuario(user.getNickname(), testComment);
        
//////////////        objetoComentario.deleteComentario(testComment.getId());

//        Assert.assertNotNull("El Comentario generado es nulo", testComment.getId());
        assertEquals("Comentario fue borrado correctamente",true, comparar);
    }
    
    /**
     * Test de prueba donde se busca realizar la eliminacion de un comentario que a sido
     * realizado por el usuario especificado, pero que para este caso dicho usuario no posee
     * un token vigente.
     */
    @Test(expected = BusinessException.class)
    public void borrarComentarioPropioSinToken() {
        System.out.println("----------borrarComentarioPropioSinToken probando el test");
        
        Usuario user = new Usuario();
        usuarioDAOImpl validarToken = new usuarioDAOImpl();
        
        user.setNickname("PruebaComentarioSinToken");
        
        comment.setId("12");
        
        Comentario testComment = objetoComentario.buscarComentario(comment.getId());

        Boolean comparar = objetoComentario.validarComentarioUsuario(user.getNickname(), testComment);
        
        Boolean testToken = validarToken.comprobarVigenciaToken(user);
        
////////////        objetoComentario.deleteComentario(testComment.getId());

//        Assert.assertNotNull("El Comentario generado es nulo", testComment.getId());
        assertEquals("Comentario fue borrado correctamente",true, testToken);
    }
    
    /**
     * Test de prueba donde se realiza la busqueda de un comentario que no existe en el sistema,
     * por lo que se espera que genere una excepcion.
     */
    @Test(expected = BusinessException.class)
    public void testExisteComentario() {
        System.out.println("----------testExisteComentario probando el test");
        
        comment.setId("4353466546");
        
            Comentario testComment = objetoComentario.existeComentario(comment.getId());
        
        Assert.assertNull("El usuario generado no es nulo", comment.getId());
//            assertEquals("Comentario insertado correctamente",true, testComment);
    }

    /**
     * Test of deleteComentario method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testDeleteComentario() {
//        System.out.println("deleteComentario");
//        String id = "";
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        boolean expResult = false;
//        boolean result = instance.deleteComentario(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of findComentario method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testFindComentario() {
//        System.out.println("findComentario");
//        String id = "";
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        Comentario expResult = null;
//        Comentario result = instance.findComentario(id);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of updateComentario method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testUpdateComentario() {
//        System.out.println("updateComentario");
//        Comentario comment = null;
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        boolean expResult = false;
//        boolean result = instance.updateComentario(comment);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of listComentario method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testListComentario() {
//        System.out.println("listComentario");
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        Collection expResult = null;
//        Collection result = instance.listComentario();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of getID method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testGetID() {
//        System.out.println("getID");
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        String expResult = "";
//        String result = instance.getID();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of countComentario method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testCountComentario() {
//        System.out.println("countComentario");
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        int expResult = 0;
//        int result = instance.countComentario();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of insertarComentarioSuperColumn method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testInsertarComentarioSuperColumn() {
//        System.out.println("insertarComentarioSuperColumn");
//        Comentario comment = null;
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.insertarComentarioSuperColumn(comment);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of borrarComentarioSuperColumn method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testBorrarComentarioSuperColumn() {
//        System.out.println("borrarComentarioSuperColumn");
//        Comentario comment = null;
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.borrarComentarioSuperColumn(comment);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of comentariosReply method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testComentariosReply() {
//        System.out.println("comentariosReply");
//        String ruta = "";
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        Collection expResult = null;
//        Collection result = instance.comentariosReply(ruta);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of insertarReplySuperColumn method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testInsertarReplySuperColumn() {
//        System.out.println("insertarReplySuperColumn");
//        Comentario comment = null;
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.insertarReplySuperColumn(comment);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of borrarReplyRaizSuperColumn method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testBorrarReplyRaizSuperColumn() {
//        System.out.println("borrarReplyRaizSuperColumn");
//        String name = "";
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.borrarReplyRaizSuperColumn(name);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of borrarReplySegundoSuperColumn method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testBorrarReplySegundoSuperColumn() {
//        System.out.println("borrarReplySegundoSuperColumn");
//        Comentario comment = null;
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.borrarReplySegundoSuperColumn(comment);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of deleteComentarioEtiqueta method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testDeleteComentarioEtiqueta() {
//        System.out.println("deleteComentarioEtiqueta");
//        String ruta = "";
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.deleteComentarioEtiqueta(ruta);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of insertarComentarioEtiquetaSuperColumn method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testInsertarComentarioEtiquetaSuperColumn() {
//        System.out.println("insertarComentarioEtiquetaSuperColumn");
//        Etiqueta tag = null;
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.insertarComentarioEtiquetaSuperColumn(tag);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of comentarioEtiquetas method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testComentarioEtiquetas() {
//        System.out.println("comentarioEtiquetas");
//        String ruta = "";
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        Collection expResult = null;
//        Collection result = instance.comentarioEtiquetas(ruta);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of insertarComentarioPuntuacionSuperColumn method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testInsertarComentarioPuntuacionSuperColumn() {
//        System.out.println("insertarComentarioPuntuacionSuperColumn");
//        Comentario comment = null;
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.insertarComentarioPuntuacionSuperColumn(comment);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of deleteComentarioPuntuacion method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testDeleteComentarioPuntuacion() {
//        System.out.println("deleteComentarioPuntuacion");
//        String ruta = "";
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.deleteComentarioPuntuacion(ruta);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of comentarioPuntuacionMeGusta method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testComentarioPuntuacionMeGusta() {
//        System.out.println("comentarioPuntuacionMeGusta");
//        String ruta = "";
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        Collection expResult = null;
//        Collection result = instance.comentarioPuntuacionMeGusta(ruta);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of comentarioPuntuacionNoMeGusta method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testComentarioPuntuacionNoMeGusta() {
//        System.out.println("comentarioPuntuacionNoMeGusta");
//        String ruta = "";
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        Collection expResult = null;
//        Collection result = instance.comentarioPuntuacionNoMeGusta(ruta);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of insertarEtiquetaComentariosSuperColumn method, of class comentarioDAOImpl.
     */
//    @Test
//    public void testInsertarEtiquetaComentariosSuperColumn() {
//        System.out.println("insertarEtiquetaComentariosSuperColumn");
//        Etiqueta tag = null;
//        Comentario comment = null;
//        comentarioDAOImpl instance = new comentarioDAOImpl();
//        instance.insertarEtiquetaComentariosSuperColumn(tag, comment);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
