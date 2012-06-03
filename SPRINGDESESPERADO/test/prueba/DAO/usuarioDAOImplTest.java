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
import prueba.modelo.Usuario;
import prueba.modelo.usuarioLogin;

/**
 *
 * @author Antonio
 */
public class usuarioDAOImplTest {
    
    private static usuarioDAOImpl objeto = new usuarioDAOImpl();
    private static Usuario user;
    
    public usuarioDAOImplTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        user = objeto.instanciaUsuario();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        user = null;
    }
    
//    @Before
//    public void setUp() {
//    }
//    
//    @After
//    public void tearDown() {
//    }

    /**
     * Test de prueba donde se verifica la correcta insercion de un objeto de tipo usuario en la base de datos.
     */
    @Test
    public void testInsertUsuario() {
        System.out.println("----------insertUsuario probando el test");
        
        user.setApellido("ApellidoPrueba");
        user.setBiografia("BiografiaPrueba");
        user.setClave("ClavePrueba");
        user.setCorreo("CorreoPrueba");
        user.setFecha_nac("FechaNacPrueba");
        user.setFoto("FotoPrueba");
        user.setNickname("NicknamePrueba");
        user.setNombre("NombrePrueba");
        user.setPais("PaisPrueba");
        
            Boolean testUser = objeto.insertUsuario(user);
        
////////        Assert.assertNotNull("El usuario generado es nulo", user.getNickname());
            assertEquals("Usuario insertado correctamente",true, testUser);
    }
    
    /**
     * Test donde se verifica que no se realice la insercion de un objeto de tipo usuario vacio.
     */
    @Test(expected = BusinessException.class)
    public void testInsertUsuarioVacio() {
        System.out.println("----------insertUsuario probando el test con usuario vacio");
        
        user.setApellido("");
        user.setBiografia("");
        user.setClave("");
        user.setCorreo("");
        user.setFecha_nac("");
        user.setFoto("");
        user.setNickname("");
        user.setNombre("");
        user.setPais("");
        
        boolean testUser = objeto.insertUsuario(user);
        
        assertEquals("Usuario no fue insertado",false, testUser);
        
    }
    
    /**
     * Test de prueba donde se realiza el login de un usuario de forma fallida, introduciendo un
     * usuario que no exista en el sistema, o donde el usuario si existe pero la clave no concuerda
     * con el usuario
     */
    @Test(expected = BusinessException.class)
    public void testLoginFallido() {
        System.out.println("----------testLoginFallido, probando un login incorrecto");
        
        usuarioLogin login = new usuarioLogin();
        
        login.setNickname("sd");
        login.setClave("1234hiuh");
        
        usuarioLogin probandoLogin = objeto.solicitarToken(login);
        
//        assertEquals("Usuario no fue insertado",false, testUser);
        //System.out.println(probandoLogin.getNickname());
        //Assert.assertNull("La prueba de login realizada fue exitosa", probandoLogin.getNickname());
        Assert.assertNotNull("La prueba de login realizada no fue exitosa", probandoLogin.getNickname());
        
    }
    
    /**
     * Test de prueba donde inicialmente se realiza una peticion para obtener un token, luego se procede
     * a realizar un comentario y seguidamente se vuelve a solicitar otro token, momento en el cual se
     * genera una excepcion y se comprueba el buen funcionamiento de la prueba.
     */
    @Test(expected = BusinessException.class)
    public void solicitarTokenComentarToken() {
        System.out.println("----------Probando el test donde se solicita solicita un token, se comenta y luego "
                + "se solicita otro token");
        
        usuarioLogin login = new usuarioLogin();
        Comentario comment = new Comentario();
        
        login.setNickname("NicknamePrueba");
        login.setClave("ClavePrueba");
        
        comment.setnickName("NicknamePrueba");
        comment.setAdjunto("adjunto");
        comment.setFecha_creacion("31/12/2011");
        comment.setId("10");
        comment.setReply("0");
        comment.setmensaje("Comentario realizado desde el test solicitarTokenComentarToken");
        
        usuarioLogin probandoLogin = objeto.solicitarToken(login);
        System.out.println("Al usuario: "+login.getNickname()+" se le asigno el token: "+login.getToken()+".");
        
        comentarioDAOImpl comentarioDAO = new comentarioDAOImpl();
        
        boolean testComment = comentarioDAO.insertComentario(comment);
        System.out.println("El usuario: "+login.getNickname()+" realizo el comentario: "+comment.getId()+".");
        
        probandoLogin = objeto.solicitarToken(login);
        
        assertEquals("El comentario no fue insertado",true, testComment);
        //System.out.println(probandoLogin.getNickname());
        //Assert.assertNull("La prueba de login realizada fue exitosa", probandoLogin.getNickname());
        Assert.assertNotNull("La prueba de login realizada no fue exitosa", probandoLogin.getNickname());
        
    }
    
    /**
     * Test donde se verifica que un usuario tiene un token vencido, por lo que no podra
     * realizar ningun tipo de modificacion mayor, obteniendo una excepcion.
     */
    @Test(expected = BusinessException.class)
    public void comprobarTokenVencido() {
        System.out.println("----------Probando test donde se comprueba que un token a vencido");
        Usuario user = new Usuario();
        //usuarioDAOImpl inserta = new usuarioDAOImpl();
                
        user.setNickname("sd");
        
            //Boolean testUser = inserta.insertUsuario(user);
            Boolean vigencia = objeto.comprobarVigenciaToken(user);
            
        assertEquals("El usuario no tiene token asignado",false, vigencia);
        
    }
    
    /**
     * Test de prueba donde se realiza la busqueda de un usuario que no existe en el sistema,
     * por lo que se espera que genere una excepcion.
     */
    @Test(expected = BusinessException.class)
    public void testExisteUsuario() {
        System.out.println("----------testExisteUsuario probando el test");
        
        user.setNickname("UsuarioQueNoExiste");
        
            Usuario testUser = objeto.existeUsuario(user.getNickname());
        
        Assert.assertNull("El usuario generado no es nulo", user.getNickname());
//            assertEquals("Usuario insertado correctamente",true, testUser);
    }

    /**
     * Test of deleteUsuario method, of class usuarioDAOImpl.
     */
//    @Test
//    public void testDeleteUsuario() {
//        System.out.println("deleteUsuario");
//        String username = "";
//        usuarioDAOImpl instance = new usuarioDAOImpl();
//        boolean expResult = false;
//        boolean result = instance.deleteUsuario(username);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of findUsuario method, of class usuarioDAOImpl.
     */
//    @Test
//    public void testFindUsuario() {
//        System.out.println("findUsuario");
//        String username = "";
//        usuarioDAOImpl instance = new usuarioDAOImpl();
//        Usuario expResult = null;
//        Usuario result = instance.findUsuario(username);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of updateUsuario method, of class usuarioDAOImpl.
     */
//    @Test
//    public void testUpdateUsuario() {
//        System.out.println("updateUsuario");
//        Usuario user = null;
//        usuarioDAOImpl instance = new usuarioDAOImpl();
//        boolean expResult = false;
//        boolean result = instance.updateUsuario(user);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of listUsuario method, of class usuarioDAOImpl.
     */
//    @Test
//    public void testListUsuario() {
//        System.out.println("listUsuario");
//        usuarioDAOImpl instance = new usuarioDAOImpl();
//        Collection expResult = null;
//        Collection result = instance.listUsuario();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of countUsuario method, of class usuarioDAOImpl.
     */
//    @Test
//    public void testCountUsuario() {
//        System.out.println("countUsuario");
//        usuarioDAOImpl instance = new usuarioDAOImpl();
//        int expResult = 0;
//        int result = instance.countUsuario();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of comentariosUsuario method, of class usuarioDAOImpl.
     */
//    @Test
//    public void testComentariosUsuario() {
//        System.out.println("comentariosUsuario");
//        String ruta = "";
//        usuarioDAOImpl instance = new usuarioDAOImpl();
//        Collection expResult = null;
//        Collection result = instance.comentariosUsuario(ruta);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of borrarUsuarioSuperColumn method, of class usuarioDAOImpl.
     */
//    @Test
//    public void testBorrarUsuarioSuperColumn() {
//        System.out.println("borrarUsuarioSuperColumn");
//        String user = "";
//        usuarioDAOImpl instance = new usuarioDAOImpl();
//        instance.borrarUsuarioSuperColumn(user);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
