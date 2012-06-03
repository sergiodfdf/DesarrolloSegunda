/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.DAO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import me.prettyprint.cassandra.dao.SimpleCassandraDao;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.*;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.*;
import org.springframework.util.DigestUtils;
import prueba.modelo.BusinessException;
import prueba.modelo.Comentario;
import prueba.modelo.Etiqueta;
import org.apache.log4j.Logger;

/**
 *
 * @author Antonio
 */
public class etiquetaDAOImpl implements etiquetaDAO {
    
    private static final String CF_NAME = "Etiqueta";
    private static final String KS_NAME = "Proyecto";
    private static final String HOST_PORT = "localhost:9160";
    
    private static final String CN_NAME = "nombre";
    
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
	
        private Logger logUtility = Logger.getLogger(getClass());
        
        public Comentario instancia(){
            Comentario obj = new Comentario();
            return obj;
        }
        
        public Etiqueta existeEtiqueta(String name) {
		
		CountQuery<String, String> countQuery = HFactory.createCountQuery(keyspace, StringSerializer.get(), StringSerializer.get());
		countQuery.setColumnFamily(CF_NAME);
                System.out.println("Variable: "+name);
		countQuery.setKey(name);
		countQuery.setRange(null, null, 100);
		QueryResult<Integer> countResult = countQuery.execute();
		int count = countResult.get();
		if(count==0) 
		{
                    System.out.println("La etiqueta: "+name+" no existe en el sistema.");
                    logUtility.error("La etiqueta: "+name+" no existe en el sistema.");
                    throw new BusinessException("011", "La etiqueta: "+name+" no existe en el sistema");
		} 
		else
		{
                    System.out.println("ENTRO EN EL FINDETIQUETA");
			Etiqueta tag = new Etiqueta();
			tag.setNombre(name);
                        
			String name1 = cassandraDao.get(name, CN_NAME);
			tag.setNombre(name1);
                        logUtility.info("La etiqueta: "+name+" si existe en el sistema.");
			
			return tag;
		}
	}
    
    public etiquetaDAOImpl() {
            System.out.println("hola etiquetaDAOImpl");
		cassandraDao = new SimpleCassandraDao();
		Cluster cluster = HFactory.getOrCreateCluster("Test-Cluster", HOST_PORT);
		keyspace = HFactory.createKeyspace(KS_NAME, cluster);
		
		cassandraDao.setKeyspace(keyspace);
		cassandraDao.setColumnFamilyName(CF_NAME);
            System.out.println("hola2etiquetaDAOImpl");
    }
    
        @Override
	public boolean insertEtiqueta(Etiqueta tag) throws BusinessException {
		//String key = user.getName();  //AQUI VA EL VALOR DEL INDEX
                String name = tag.getNombre();
		
                if (name.equals("")){
                    
                    System.out.println("El campo nombre del objeto etiqueta se encuentra vacio");
                    throw new BusinessException("007", "El campo nombre del objeto etiqueta se encuentra vacio");
                    
                }
                
//		Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
		
//		mutator.addInsertion(key, CF_NAME, HFactory.createColumn(CN_PASSWORD, password, StringSerializer.get(), StringSerializer.get()));
//		mutator.addInsertion(key, CF_NAME, HFactory.createColumn(CN_CORREO, correo, StringSerializer.get(), StringSerializer.get()));
//		mutator.addInsertion(key, CF_NAME, HFactory.createColumn(CN_CORREO, twitter, StringSerializer.get(), StringSerializer.get()));
//                mutator.execute();
		
		cassandraDao.insert(name, CN_NAME, name);

		return true;
	}
        
        @Override
        public boolean deleteEtiqueta(String name) {
		//seems this is not an easy API to delete one single row
		//cassandraDao.delete(columnName, keys)
		//
		//try another way
		ColumnFamilyTemplate<String, String> template =
                new ThriftColumnFamilyTemplate<String, String>(keyspace, CF_NAME, StringSerializer.get(), StringSerializer.get());
		template.deleteRow(name);
                
		return true;
	}
    
        @Override
        public Etiqueta findEtiqueta(String name) {
		//check if row key exist, then query
		CountQuery<String, String> countQuery = HFactory.createCountQuery(keyspace, StringSerializer.get(), StringSerializer.get());
		countQuery.setColumnFamily(CF_NAME);
                System.out.println("Variable: "+name);
		countQuery.setKey(name);
		countQuery.setRange(null, null, 100);
		QueryResult<Integer> countResult = countQuery.execute();
		int count = countResult.get();
		if(count==0) 
		{
                    System.out.println("ENTRO EN EL IF FINDETIQUETA");
                    Etiqueta tag = new Etiqueta();
                    tag.setNombre("");
                    
			return tag;
		} 
		else
		{
                    System.out.println("ENTRO EN EL FINDETIQUETA");
			Etiqueta tag = new Etiqueta();
			tag.setNombre(name);
                        
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
                        
			String name1 = cassandraDao.get(name, CN_NAME);
			tag.setNombre(name1);                      
			
//			String dateStr = cassandraDao.get(username, CN_CREATED_AT);
//			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			try {
//				Date date = formatter.parse(dateStr);
//				user.setCreated_at(date);
//			} catch (ParseException e) {
//				logger.debug(e.getMessage());
//			}
			
			return tag;
		}
	}
    
    @Override
	public Collection<Etiqueta> listEtiqueta() {
		List<Etiqueta> etiquetaList = new ArrayList<Etiqueta>();
		RangeSlicesQuery<String, String, String> rangeSlicesQuery = HFactory.createRangeSlicesQuery(keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
		rangeSlicesQuery.setColumnFamily(CF_NAME);
		rangeSlicesQuery.setColumnNames(CN_NAME);
		rangeSlicesQuery.setRowCount(1000);
//		rangeSlicesQuery.setRange(start, finish, reversed, count);
		QueryResult<OrderedRows<String, String, String>> result = rangeSlicesQuery.execute();
		OrderedRows<String, String, String> rows = result.get();
		Iterator<Row<String, String, String>> itr = rows.iterator();
		while(itr.hasNext()) {
			Row<String, String, String> row = itr.next();
			ColumnSlice<String, String> columnSlice = row.getColumnSlice();
			
			Etiqueta tag = new Etiqueta();
			
			String name = row.getKey();
			
                        
                        System.out.println(row.getColumnSlice().toString());
                        if (!columnSlice.toString().equalsIgnoreCase("ColumnSlice([])")) {
			
//			String dateStr = columnSlice.getColumnByName(CN_CREATED_AT).getValue();
//			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			try {
//				Date date = formatter.parse(dateStr);
//				user.setCreated_at(date);
//			} catch (ParseException e) {
//				logger.debug(e.getMessage());
//			}
                            tag.setNombre(name);
                            System.out.println(tag.getNombre());
                            etiquetaList.add(tag);
                        }
                        
		}
		
		return etiquetaList;
	}
        
        @Override
	public int countEtiqueta() {
		CountQuery<String, String> countQuery = HFactory.createCountQuery(keyspace, StringSerializer.get(), StringSerializer.get());
		countQuery.setColumnFamily(CF_NAME);
		QueryResult<Integer> result = countQuery.execute();
		return result.get();
	}
        
        @Override
        public Collection<Comentario> etiquetaComentarios(String ruta){
       
            StringSerializer serializer = StringSerializer.get();

            List<Comentario> commentList = new ArrayList<Comentario>();
            
            System.out.println("nombre de la variable "+ruta);
            RangeSuperSlicesQuery<String, String, String,String> rangeSuperSlicesQuery =
            HFactory.createRangeSuperSlicesQuery(keyspace, serializer, serializer,serializer,serializer);
            rangeSuperSlicesQuery.setColumnFamily("Etiqueta_Comentario");
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
    
}
