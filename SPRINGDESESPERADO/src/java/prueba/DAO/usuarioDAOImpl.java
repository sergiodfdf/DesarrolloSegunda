/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.DAO;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import me.prettyprint.cassandra.dao.SimpleCassandraDao;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.CountQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import org.springframework.util.DigestUtils;
import prueba.modelo.Usuario;


import me.prettyprint.hector.api.beans.HSuperColumn;
import me.prettyprint.hector.api.beans.OrderedSuperRows;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.mutation.MutationResult;
import me.prettyprint.hector.api.query.SuperColumnQuery;
import me.prettyprint.hector.api.query.RangeSuperSlicesQuery;
import prueba.modelo.Comentario;

import static me.prettyprint.hector.api.factory.HFactory.createMutator;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import prueba.modelo.BusinessException;
import prueba.modelo.Tokken;
import prueba.modelo.usuarioLogin;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio
 */
public class usuarioDAOImpl implements usuarioDAO {
    
    	//private static Logger logger = Logger.getLogger(authorDAOImpl.class);	
    
    
	private static final String CF_NAME = "Usuario";
	private static final String KS_NAME = "Proyecto";
	private static final String HOST_PORT = "localhost:9160";
	
        private static final String CN_NAME = "nombre";
	private static final String CN_LASTNAME = "apellido";
        private static final String CN_PASSWORD = "clave";
	private static final String CN_MAIL = "correo";
        private static final String CN_NICKNAME = "nickname";
        private static final String CN_DATE_NAC = "fecha_nac";
        private static final String CN_COUNTRY = "pais";
        private static final String CN_BIOGRAFY = "biografia";
        private static final String CN_PHOTO = "foto";
        
        private SimpleCassandraDao cassandraDao;
	private Keyspace keyspace;
        
        private StringSerializer serializer;
        
        private String [] comandos;
        private String [] comandos2;
        private String [] definitivo;
        private String [] arregloFinal = new String [100];
        private String linea; 
        private String res;
        private String res2;
        private int e = 0;
        private String ultimoSplit;
        private int cuenta;
        
        //Variables para solicitar token
        private usuarioDAO dao;
        private tokkenDAO daoTok;
        private Usuario obj;
        private String arreglo[];
        private String diasss[];
        private String horasss[];
        private Calendar calendario;
        String horas, minutos;
        int month;
        String dia, mes, anno, fechaCon;
        
        private Logger logUtility = Logger.getLogger(getClass());
	
        public Comentario instancia(){
            Comentario obj = new Comentario();
            return obj;
        }
        
        public Usuario instanciaUsuario(){
            Usuario obj = new Usuario();
            return obj;
        }        
        
        public Usuario existeUsuario(String username) throws BusinessException {
		//check if row key exist, then query
		CountQuery<String, String> countQuery = HFactory.createCountQuery(keyspace, StringSerializer.get(), StringSerializer.get());
		countQuery.setColumnFamily(CF_NAME);
                System.out.println("Variable: "+username);
		countQuery.setKey(username);
		countQuery.setRange(null, null, 100);
		QueryResult<Integer> countResult = countQuery.execute();
		int count = countResult.get();
		if(count==0) 
		{
                    logUtility.error("El usuario: "+username+" no existe en el sistema.");
                    System.out.println("El usuario: "+username+" no existe en el sistema.");
                    throw new BusinessException("009", "El usuario: "+username+" no existe en el sistema");
		} 
		else
		{
                    System.out.println("ENTRO EN EL FINDUSUARIO");
                    logUtility.error("El usuario: "+username+" si existe en el sistema.");
			Usuario user = new Usuario();
			user.setNickname(username);
                        
			String name = cassandraDao.get(username, CN_NAME);
			user.setNombre(name);
			String lastName = cassandraDao.get(username, CN_LASTNAME);
			user.setApellido(lastName);                        
			String password = cassandraDao.get(username, CN_PASSWORD);
			user.setClave(password);
                        String mail = cassandraDao.get(username, CN_MAIL);
			user.setCorreo(mail);
                        String nickName = cassandraDao.get(username, CN_NICKNAME);
			user.setNickname(nickName);
                        String date_nac = cassandraDao.get(username, CN_DATE_NAC);
			user.setFecha_nac(date_nac);
                        String country = cassandraDao.get(username, CN_COUNTRY);
			user.setPais(country);
                        String biografy = cassandraDao.get(username, CN_BIOGRAFY);
			user.setBiografia(biografy);
                        String photo = cassandraDao.get(username, CN_PHOTO);
			user.setFoto(photo);                       
			
			return user;
		}
	}
        
        public usuarioLogin solicitarToken(usuarioLogin datos) throws BusinessException {
            
            //System.out.println("hola SolicitarToken");
                
                dao = new usuarioDAOImpl();
                
                obj = new Usuario();
               
               System.out.println("nickName: "+datos.getNickname()); 
               obj = dao.findUsuario(datos.getNickname());
               //System.out.println("nickName de obj: "+obj.getNickname());
//               System.out.println(obj.getNickname().toString());
               if (obj.getNickname().isEmpty()) {
                   
                   logUtility.error("El nickName introducido no existe en el sistema.");
                   System.out.println("El nickName introducido no existe");
                   throw new BusinessException("002", "El nickName del usuario no existe en el sistema");
                   
               }
               else
                   logUtility.info("El nickName si existe: "+obj.getNickname());
                   System.out.println("El nickName si existe: "+obj.getNickname());
               
               if (!datos.getClave().equals(obj.getClave())){
                   
                   logUtility.error("La clave introducida es incorrecta.");
                   System.out.println("La clave introducida es incorrecta");
                   throw new BusinessException("003", "La clave: "+datos.getClave()+" introducida por el usuario: "+
                           datos.getNickname()+" es incorrecta");
                   
               }
               else
                   logUtility.info("La clave es correcta: "+obj.getClave());
                   System.out.println("La clave es correcta: "+obj.getClave());
               
               Tokken findTokenViejo = new Tokken();
               
               daoTok = new tokkenDAOImpl();
               
               findTokenViejo = daoTok.findTokken(datos.getNickname());
               
               if (!findTokenViejo.getTokken().isEmpty()) {
                   
                    calendario = Calendar.getInstance();
                
                    horas = Integer.toString(calendario.get(Calendar.HOUR_OF_DAY));
                    minutos = Integer.toString(calendario.get(Calendar.MINUTE));
                    dia = Integer.toString(calendario.get(Calendar.DATE));
                    month = calendario.get(Calendar.MONTH);
                    month = month + 1;
                    mes = Integer.toString(month);
                    anno = Integer.toString(calendario.get(Calendar.YEAR));
                
                    fechaCon = dia+"/"+mes+"/"+anno+","+horas+":"+minutos;
                    
                    String fechaVieja = findTokenViejo.getFecha_creacion();
                    
                    arreglo = fechaVieja.split(",");
                    String days = arreglo[0];
                    String hours = arreglo[1];
                    
                    diasss = days.split("/");
                    String diaViejo = diasss[0];
                    String mesViejo = diasss[1];
                    String annoViejo = diasss[2];
                    
                    horasss = hours.split(":");
                    String horaViejo = horasss[0];
                    String minutoViejo = horasss[1];
                    
                    if ((dia.equals(diaViejo)) && (mes.equals(mesViejo)) && (anno.equals(annoViejo))) {
                        
                        int newHora = Integer.parseInt(horas);
                        int oldHora = Integer.parseInt(horaViejo);
                        
                        int oldMinutos = Integer.parseInt(minutoViejo);
                        
                        newHora = newHora*60;
                        oldHora = oldHora*60;
                        
                        newHora = newHora + calendario.get(Calendar.MINUTE);
                        oldHora = oldHora + oldMinutos;
                        
                        int total = newHora - oldHora;
                        
                        if (total >= 3){   //AQUI CAMBIAR PARA LA DURACION DE LOS MINUTOS
                            
                            Tokken tok = new Tokken();
                
                            daoTok = new tokkenDAOImpl();
               
                            Random ramdom = new Random();
               
                            int numero = ramdom.nextInt(999999999);
                            String num = String.valueOf(numero);
               
                            System.out.println("Numero aleatorio: "+numero);
               
                            datos.setToken(num);
               
                            tok.setTokken(num);

//                            String remoteAddress = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
                //                System.out.println("IP: "+remoteAddress);
                                tok.setIp("127.0.0.1");

                                calendario = Calendar.getInstance();

                                horas = Integer.toString(calendario.get(Calendar.HOUR_OF_DAY));
                                minutos = Integer.toString(calendario.get(Calendar.MINUTE));
                                dia = Integer.toString(calendario.get(Calendar.DATE));
                                month = calendario.get(Calendar.MONTH);
                                month = month + 1;
                                mes = Integer.toString(month);
                                anno = Integer.toString(calendario.get(Calendar.YEAR));

                                fechaCon = dia+"/"+mes+"/"+anno+","+horas+":"+minutos;
                                tok.setFecha_creacion(fechaCon);
                                tok.setUsuario(datos.getNickname());

//                                System.out.println("IP: "+remoteAddress);
                                System.out.println("Fecha: "+fechaCon);

                                daoTok.insertTokken(tok);

                                logUtility.info("Al NickName: " + obj.getNickname() + " se le ha asignado el Token= " + tok.getTokken());

                                return datos;
                            
                        }
                        else {
                            
                            System.out.println("Token vigente");
                            logUtility.error("El usuario: " + obj.getNickname() + " aun tiene vigente el token= " + findTokenViejo.getTokken());
                            throw new BusinessException("004", "El usuario: "+datos.getNickname()
                                    +" aun tiene vigente el token: "+findTokenViejo.getTokken()+" vigente");
                            
                        }
                        
                    }
                   
               }
               
               Tokken tok = new Tokken();
                
               daoTok = new tokkenDAOImpl();
               
               Random ramdom = new Random();
               
               int numero = ramdom.nextInt(999999999);
               String num = String.valueOf(numero);
               
               System.out.println("Numero aleatorio: "+numero);
               
               datos.setToken(num);
               
               tok.setTokken(num);
               
//////               String remoteAddress = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
//                System.out.println("IP: "+remoteAddress);
                
                tok.setIp("127.0.0.1");
                
                calendario = Calendar.getInstance();
                
                horas = Integer.toString(calendario.get(Calendar.HOUR_OF_DAY));
                minutos = Integer.toString(calendario.get(Calendar.MINUTE));
                dia = Integer.toString(calendario.get(Calendar.DATE));
                month = calendario.get(Calendar.MONTH);
                month = month + 1;
                mes = Integer.toString(month);
                anno = Integer.toString(calendario.get(Calendar.YEAR));
                
                fechaCon = dia+"/"+mes+"/"+anno+","+horas+":"+minutos;
                tok.setFecha_creacion(fechaCon);
                tok.setUsuario(datos.getNickname());
                
                System.out.println("IP: "+tok.getIp());
                System.out.println("Fecha: "+fechaCon);
                
                logUtility.info("Al NickName: " + obj.getNickname() + " se le ha asignado el Token= " + tok.getTokken());
                
                daoTok.insertTokken(tok);               
                return datos;                
    
        }
        
        public boolean comprobarVigenciaToken(Usuario usuario) throws BusinessException {
            
            //System.out.println("hola SolicitarToken");
                
                dao = new usuarioDAOImpl();
                
                obj = new Usuario();
               
               System.out.println("nickName: "+usuario.getNickname()); 
               obj = dao.findUsuario(usuario.getNickname());
               //System.out.println("nickName de obj: "+obj.getNickname());
//               System.out.println(obj.getNickname().toString());
               if (obj.getNickname().isEmpty()) {
                   
                   logUtility.info("El nickName introducido no existe");
                   System.out.println("El nickName introducido no existe");
                   throw new BusinessException("002", "El nickName del usuario no existe en el sistema");
                   
               }
               
               Tokken findTokenViejo = new Tokken();
               
               daoTok = new tokkenDAOImpl();
               
               findTokenViejo = daoTok.findTokken(usuario.getNickname());
               
               if (!findTokenViejo.getTokken().isEmpty()) {
                   
                    calendario = Calendar.getInstance();
                
                    horas = Integer.toString(calendario.get(Calendar.HOUR_OF_DAY));
                    minutos = Integer.toString(calendario.get(Calendar.MINUTE));
                    dia = Integer.toString(calendario.get(Calendar.DATE));
                    month = calendario.get(Calendar.MONTH);
                    month = month + 1;
                    mes = Integer.toString(month);
                    anno = Integer.toString(calendario.get(Calendar.YEAR));
                
                    fechaCon = dia+"/"+mes+"/"+anno+","+horas+":"+minutos;
                    
                    String fechaVieja = findTokenViejo.getFecha_creacion();
                    
                    arreglo = fechaVieja.split(",");
                    String days = arreglo[0];
                    String hours = arreglo[1];
                    
                    diasss = days.split("/");
                    String diaViejo = diasss[0];
                    String mesViejo = diasss[1];
                    String annoViejo = diasss[2];
                    
                    horasss = hours.split(":");
                    String horaViejo = horasss[0];
                    String minutoViejo = horasss[1];
                    
                    if ((dia.equals(diaViejo)) && (mes.equals(mesViejo)) && (anno.equals(annoViejo))) {
                        
                        int newHora = Integer.parseInt(horas);
                        int oldHora = Integer.parseInt(horaViejo);
                        
                        int oldMinutos = Integer.parseInt(minutoViejo);
                        
                        newHora = newHora*60;
                        oldHora = oldHora*60;
                        
                        newHora = newHora + calendario.get(Calendar.MINUTE);
                        oldHora = oldHora + oldMinutos;
                        
                        int total = newHora - oldHora;
                        
                        if (total >= 3){   //AQUI CAMBIAR PARA LA DURACION DE LOS MINUTOS
                            
                           logUtility.info("El usuario: "+usuario.getNickname()
                                    +" tiene el token: "+findTokenViejo.getTokken()+" vencido"); 
                           System.out.println("El usuario: "+usuario.getNickname()
                                    +" tiene el token: "+findTokenViejo.getTokken()+" vencido");
                           throw new BusinessException("006", "El usuario: "+usuario.getNickname()
                                    +" tiene el token: "+findTokenViejo.getTokken()+" vencido");
                            
                        }
                        else {
                            
                            logUtility.info("El token esta vigente.");
                            System.out.println("Token vigente");
                            return true;
                            
                        }
                        
                    }
                    else {
                        
                        logUtility.info("El usuario: "+usuario.getNickname()
                                    +" tiene el token: "+findTokenViejo.getTokken()+" vencido");
               System.out.println("El usuario: "+usuario.getNickname()
                                    +" tiene el token: "+findTokenViejo.getTokken()+" vencido");
               throw new BusinessException("006", "El usuario: "+usuario.getNickname()
                                    +" tiene el token: "+findTokenViejo.getTokken()+" vencido");
                        
                    }
                   
               }
               
               logUtility.info("El usuario: "+usuario.getNickname()
                                    +" no tiene un token asignado.");
               System.out.println("El usuario: "+usuario.getNickname()
                                    +" no tiene un token asignado.");
               throw new BusinessException("005", "El usuario: "+usuario.getNickname()
                                    +" no tiene un token asignado.");                
    
        }
        
	public usuarioDAOImpl() {
            System.out.println("hola usuarioDAOImpl");
		cassandraDao = new SimpleCassandraDao();
		Cluster cluster = HFactory.getOrCreateCluster("Test-Cluster", HOST_PORT);
		keyspace = HFactory.createKeyspace(KS_NAME, cluster);
		
		cassandraDao.setKeyspace(keyspace);
		cassandraDao.setColumnFamilyName(CF_NAME);
            System.out.println("hola2 usuarioDAOImpl");
	}
        
        @Override
	public boolean insertUsuario(Usuario user) throws BusinessException {
		//String key = user.getName();  //AQUI VA EL VALOR DEL INDEX
                String name = user.getNombre();
		String lastName = user.getApellido();
                String password = user.getClave();
		String mail = user.getCorreo();
                String nickName = user.getNickname();
                String date_nac = user.getFecha_nac();
                String country = user.getPais();
                String biografy = user.getBiografia();
                String photo = user.getFoto();
                
                if ((nickName.equals(""))||(name.equals(""))||(lastName.equals(""))||(password.equals(""))||(mail.equals(""))||(date_nac.equals(""))||(country.equals(""))||(biografy.equals(""))||(photo.equals(""))){
                    System.out.println("Uno de los campos del objeto usuario se encuentra vacio");
                    throw new BusinessException("001", "Uno de los campos del objeto usuario se encuentra vacio");
                }
		
//		Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
		
//		mutator.addInsertion(key, CF_NAME, HFactory.createColumn(CN_PASSWORD, password, StringSerializer.get(), StringSerializer.get()));
//		mutator.addInsertion(key, CF_NAME, HFactory.createColumn(CN_CORREO, correo, StringSerializer.get(), StringSerializer.get()));
//		mutator.addInsertion(key, CF_NAME, HFactory.createColumn(CN_CORREO, twitter, StringSerializer.get(), StringSerializer.get()));
//                mutator.execute();
		
		cassandraDao.insert(nickName, CN_NAME, name);
		cassandraDao.insert(nickName, CN_LASTNAME, lastName);
                cassandraDao.insert(nickName, CN_PASSWORD, password);
                cassandraDao.insert(nickName, CN_MAIL, mail);
                cassandraDao.insert(nickName, CN_NICKNAME, nickName);
                cassandraDao.insert(nickName, CN_DATE_NAC, date_nac);
                cassandraDao.insert(nickName, CN_COUNTRY, country);
                cassandraDao.insert(nickName, CN_BIOGRAFY, biografy);
                cassandraDao.insert(nickName, CN_PHOTO, photo);
		return true;
	}
        
        @Override
        public boolean deleteUsuario(String username) {
		//seems this is not an easy API to delete one single row
		//cassandraDao.delete(columnName, keys)
		//
		//try another way
		ColumnFamilyTemplate<String, String> template =
                new ThriftColumnFamilyTemplate<String, String>(keyspace, CF_NAME, StringSerializer.get(), StringSerializer.get());
		template.deleteRow(username);
                
		return true;
	}
                
        @Override
	public Usuario findUsuario(String username) {
		//check if row key exist, then query
		CountQuery<String, String> countQuery = HFactory.createCountQuery(keyspace, StringSerializer.get(), StringSerializer.get());
		countQuery.setColumnFamily(CF_NAME);
                System.out.println("Variable: "+username);
		countQuery.setKey(username);
		countQuery.setRange(null, null, 100);
		QueryResult<Integer> countResult = countQuery.execute();
		int count = countResult.get();
		if(count==0) 
		{
                    System.out.println("ENTRO EN EL IF FINDUSUARIO");
                    Usuario user = new Usuario();
                    user.setNickname("");
			return user;
		} 
		else
		{
                    System.out.println("ENTRO EN EL FINDUSUARIO");
			Usuario user = new Usuario();
			user.setNickname(username);
                        
//                        user.setUsername(username);
//			
//			SliceQuery<String, String, String>  sliceQuery = HFactory.createSliceQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
//			sliceQuery.setColumnFamily(CF_NAME);
//			sliceQuery.setRange(null, null, false, 1000);
//			sliceQuery.setKey(username);
//			sliceQuery.setColumnNames(CN_PASSWORD,CN_CORREO,CN_TWITTER);
//			
//			QueryResult<ColumnSlice<String, String>> result = sliceQuery.execute();
//			ColumnSlice<String, String> columnSlice= result.get();
			
////			logger.debug("columnSlice isNull?="+(columnSlice==null));
////			logger.debug("columnSlice.getColumnByName(CN_PASSWORD) isNull?="+(columnSlice.getColumnByName(CN_PASSWORD)==null)); 
////			HColumn<String, String> columnPassword = columnSlice.getColumnByName(CN_PASSWORD);
////			String md5Password = (columnPassword != null ? columnPassword.getValue() : null);
//			
////			HColumn<String, String> columnDate = columnSlice.getColumnByName(CN_CREATED_AT);
////			String dateStr = (columnDate != null ? columnDate.getValue() : null);
////			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////			try {
////				Date date = formatter.parse(dateStr);
////				user.setCreated_at(date);
////			} catch (ParseException e) {
////				logger.debug(e.getMessage());
////			}
////			user.setPassword(md5Password);
                        
			String name = cassandraDao.get(username, CN_NAME);
			user.setNombre(name);
			String lastName = cassandraDao.get(username, CN_LASTNAME);
			user.setApellido(lastName);                        
			String password = cassandraDao.get(username, CN_PASSWORD);
			user.setClave(password);
                        String mail = cassandraDao.get(username, CN_MAIL);
			user.setCorreo(mail);
                        String nickName = cassandraDao.get(username, CN_NICKNAME);
			user.setNickname(nickName);
                        String date_nac = cassandraDao.get(username, CN_DATE_NAC);
			user.setFecha_nac(date_nac);
                        String country = cassandraDao.get(username, CN_COUNTRY);
			user.setPais(country);
                        String biografy = cassandraDao.get(username, CN_BIOGRAFY);
			user.setBiografia(biografy);
                        String photo = cassandraDao.get(username, CN_PHOTO);
			user.setFoto(photo);                       
			
//			String dateStr = cassandraDao.get(username, CN_CREATED_AT);
//			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			try {
//				Date date = formatter.parse(dateStr);
//				user.setCreated_at(date);
//			} catch (ParseException e) {
//				logger.debug(e.getMessage());
//			}
			
			return user;
		}
	}
        
        @Override
        public boolean updateUsuario(Usuario user) {
////		String key = user.getName();
////
////		String password = user.getPassword();
////		String correo = user.getCorreo();
////                String twitter = user.getTwitter();
		
//		Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
//		
//		mutator.addInsertion(key, CF_NAME, HFactory.createColumn(CN_PASSWORD, md5Password, StringSerializer.get(), StringSerializer.get()));
//		mutator.addInsertion(key, CF_NAME, HFactory.createColumn(CN_CREATED_AT, dateFormatted, StringSerializer.get(), StringSerializer.get()));
//		mutator.execute();
		
////		cassandraDao.insert(key, CN_PASSWORD, password);
////		cassandraDao.insert(key, CN_CORREO, correo);
////                cassandraDao.insert(key, CN_TWITTER, twitter);
		return true;
	}
        
        @Override
	public Collection<Usuario> listUsuario() {
		List<Usuario> userList = new ArrayList<Usuario>();
		RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
		rangeSlicesQuery.setColumnFamily(CF_NAME);
		rangeSlicesQuery.setColumnNames(CN_NAME,CN_LASTNAME,CN_PASSWORD,CN_MAIL,CN_NICKNAME,CN_DATE_NAC,CN_COUNTRY,CN_BIOGRAFY, CN_PHOTO);
		rangeSlicesQuery.setRowCount(1000);
//		rangeSlicesQuery.setRange(start, finish, reversed, count);
		QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
		OrderedRows<String, String, String> rows = result.get();
		Iterator<Row<String, String, String>> itr = rows.iterator();
		while(itr.hasNext()) {
			Row<String, String, String> row = itr.next();
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			
			Usuario user = new Usuario();
			
			String username = row.getKey();
			user.setNickname(username);
                        System.out.println(row.getColumnSlice().toString());
                        if (!columnSlice.toString().equalsIgnoreCase("ColumnSlice([])")) {
                        
                        String name = columnSlice.getColumnByName(CN_NAME).getValue();
			user.setNombre(name);
                        
                        String apellido = columnSlice.getColumnByName(CN_LASTNAME).getValue();
			user.setApellido(apellido);
			
			String Password = columnSlice.getColumnByName(CN_PASSWORD).getValue();
			user.setClave(Password);
                        
                        String correo = columnSlice.getColumnByName(CN_MAIL).getValue();
			user.setCorreo(correo);
                        
                        String fecha = columnSlice.getColumnByName(CN_DATE_NAC).getValue();
			user.setFecha_nac(fecha);
                        
                        String pais = columnSlice.getColumnByName(CN_COUNTRY).getValue();
			user.setPais(pais);
                        
                        String biografia = columnSlice.getColumnByName(CN_BIOGRAFY).getValue();
			user.setBiografia(biografia);
                        
                        String foto = columnSlice.getColumnByName(CN_PHOTO).getValue();
			user.setFoto(foto);
			
//			String dateStr = columnSlice.getColumnByName(CN_CREATED_AT).getValue();
//			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			try {
//				Date date = formatter.parse(dateStr);
//				user.setCreated_at(date);
//			} catch (ParseException e) {
//				logger.debug(e.getMessage());
//			}
			
			userList.add(user);
                        }
                        else
                            System.out.println("Variable null");
                        
		}
		
		return userList;
	}
        
        @Override
	public int countUsuario() {
		CountQuery<String, String> countQuery = HFactory.createCountQuery(keyspace, StringSerializer.get(), StringSerializer.get());
		countQuery.setColumnFamily(CF_NAME);
		QueryResult<Integer> result = countQuery.execute();
		return result.get();
	}
        
        @Override
        public Collection<Comentario> comentariosUsuario(String ruta){
       
            StringSerializer serializer = StringSerializer.get();

            List<Comentario> commentList = new ArrayList<Comentario>();
            
            System.out.println("nombre de la variable "+ruta);
            RangeSuperSlicesQuery<String, String, String,String> rangeSuperSlicesQuery =
            HFactory.createRangeSuperSlicesQuery(keyspace, serializer, serializer,serializer,serializer);
            rangeSuperSlicesQuery.setColumnFamily("Usuario_Comentario");
            //rangeSuperSlicesQuery.setKeys("", "");
            rangeSuperSlicesQuery.setKeys(ruta, ruta);
            rangeSuperSlicesQuery.setRange("", "", false, 100);
            rangeSuperSlicesQuery.setRowCount(11);
            //rangeSuperSlicesQuery.setColumnNames("");
            QueryResult<OrderedSuperRows<String,String,String,String>> queryResult = rangeSuperSlicesQuery.execute();
            OrderedSuperRows<String,String,String,String> orderedRows = queryResult.get();
            // System.out.print(orderedRows.getList());
            linea = orderedRows.getList().toString();
              // comandos = linea.split(",");
            
            
            
               comandos = linea.split("HColumn\\(");

                cuenta = comandos.length;
                Comentario com = new Comentario();
                for (int i=0; i<cuenta;++i){
                    if (comandos[i].startsWith("adjunto=")) //|| (comandos[i].startsWith("contenido=")) || (comandos[i].startsWith("fecha_creacion=")))
                        {

                            res = comandos[i];
                            // System.out.println("res "+res); 
                            comandos2 = res.split("adjunto=");
                            res2 = comandos2[1];
                            definitivo = res2.split("\\),");
                            //System.out.println("adjunto "+res2);

                //          System.out.println("adjunto final: "+definitivo[e]);
                            ultimoSplit = definitivo[0];
                            arregloFinal[e] = ultimoSplit;
                            com.setAdjunto(ultimoSplit);
                            com.setId("1");
                            System.out.println("adjunto final en pos "+e+": "+ arregloFinal[e]);
                            e++;
           
                        }
               
              if (comandos[i].startsWith("contenido=")) 
              {
                
                 res = comandos[i];
                 // System.out.println("res "+res); 
                 comandos2 = res.split("contenido=");
                 res2 = comandos2[1];
                 definitivo = res2.split("\\),");
                //System.out.println("adjunto "+res2);
                
      //          System.out.println("adjunto final: "+definitivo[e]);
                ultimoSplit = definitivo[0];
                arregloFinal[e] = ultimoSplit;
                com.setmensaje(ultimoSplit);
                System.out.println("contenedor final en pos "+e+": "+ arregloFinal[e]);
                e++;
           
              }  
              
             if (comandos[i].startsWith("fecha_creacion="))
              {
                
                 res = comandos[i];
                 // System.out.println("res "+res); 
                 comandos2 = res.split("fecha_creacion=");
                 res2 = comandos2[1];
                 definitivo = res2.split("\\)]");
                 //System.out.println("adjunto "+res2);
                
      //          System.out.println("adjunto final: "+definitivo[e]);
                ultimoSplit = definitivo[0];
                arregloFinal[e] = ultimoSplit; 
                System.out.println("fecha_creacio final en pos "+e+": "+ arregloFinal[e]);
                e++;
                
                com.setFecha_creacion(ultimoSplit);
                  System.out.println(com.getmensaje());
                commentList.add(com);
                com = instancia();
              }  
             
             }
        //   System.out.println("prueba "+linea.replaceAll(" HColumn","<%"));  
            //  comandos[1].                 
       //  System.out.println("la cuenta de , es:"+cuenta);
//        System.out.print(linea);

        return commentList; 
     
        }
        
        @Override
        public void borrarUsuarioSuperColumn(String user){
            
           serializer = StringSerializer.get(); 
           Mutator<String> m = createMutator(keyspace, serializer);
           //Mutator m = HFactory.createMutator("Proyecto", serializer);
            System.out.println(user);
            //System.out.println(comment.getId());
	   MutationResult mr2 = m.superDelete(user, "Usuario_Comentario", null, serializer);
            
        }
}
