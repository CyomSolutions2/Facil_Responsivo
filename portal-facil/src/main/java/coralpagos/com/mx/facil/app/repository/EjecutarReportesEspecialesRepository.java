package coralpagos.com.mx.facil.app.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class EjecutarReportesEspecialesRepository {
	
	 @Autowired
	 private JdbcTemplate jdbcTemplate;
	 
	 private Logger log4j = LogManager.getLogger(EjecutarReportesEspecialesRepository.class);
	
	 public Map<String, Object> ejecutarStoredProcedure(String spName) {
	        return jdbcTemplate.execute((Connection con) -> {
	            Map<String, Object> result = new HashMap<>();

	            try (CallableStatement cs = con.prepareCall("{call " + spName + "}")) {
	                boolean hasResult = cs.execute();

	                if (hasResult) {
	                    try (ResultSet rs = cs.getResultSet()) {
	                        ResultSetMetaData metaData = rs.getMetaData();
	                        int columnCount = metaData.getColumnCount();

	                        // Obtener nombres de columnas
	                        List<String> columnNames = new ArrayList<>();
	                        for (int i = 1; i <= columnCount; i++) {
	                            columnNames.add(metaData.getColumnLabel(i));
	                        }
	                        result.put("columns", columnNames);

	                        // Obtener los datos
	                        List<Map<String, Object>> rows = new ArrayList<>();
	                        while (rs.next()) {
	                            Map<String, Object> row = new LinkedHashMap<>();
	                            for (int i = 1; i <= columnCount; i++) {
	                                row.put(metaData.getColumnLabel(i), rs.getObject(i));
	                            }
	                            rows.add(row);
	                        }
	                        result.put("data", rows);
	                    }
	                }

	                return result;
	            }
	        });
	    }
	 
	 public Map<String, Object> ejecutarStoredProcedureConParametros(String spName, Map<String, Object> parametros) {
		    return jdbcTemplate.execute((Connection con) -> {
		        Map<String, Object> result = new HashMap<>();

		        // Construir string de llamada: {call spNombre(?, ?, ?, ...)}
		        StringBuilder callBuilder = new StringBuilder("{call ");
		        callBuilder.append(spName);
		        callBuilder.append("(");
		        for (int i = 0; i < parametros.size(); i++) {
		            callBuilder.append("?,");
		        }
		        if (!parametros.isEmpty()) {
		            callBuilder.setLength(callBuilder.length() - 1); // Quitar la última coma
		        }
		        
		        callBuilder.append(")}");

		        try (CallableStatement cs = con.prepareCall(callBuilder.toString())) {
		            int index = 1;
		            for (Map.Entry<String, Object> entry : parametros.entrySet()) {
		                cs.setObject(index++, entry.getValue());
		            }
		            
		            ObjectMapper mapper = new ObjectMapper();
		            String jsonParametros = null;;
					try {
						jsonParametros = mapper.writeValueAsString(parametros);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						log4j.error("Exception: ", e);
						e.printStackTrace();
					}
		            log4j.info("Ejecución de " + spName + " con parámetros: " + jsonParametros);
		 

		            boolean hasResult = cs.execute();

		            if (hasResult) {
		                try (ResultSet rs = cs.getResultSet()) {
		                    ResultSetMetaData metaData = rs.getMetaData();
		                    int columnCount = metaData.getColumnCount();

		                    List<String> columnNames = new ArrayList<>();
		                    for (int i = 1; i <= columnCount; i++) {
		                        columnNames.add(metaData.getColumnLabel(i));
		                    }
		                    result.put("columns", columnNames);

		                    List<Map<String, Object>> rows = new ArrayList<>();
		                    while (rs.next()) {
		                        Map<String, Object> row = new LinkedHashMap<>();
		                        for (int i = 1; i <= columnCount; i++) {
		                            row.put(metaData.getColumnLabel(i), rs.getObject(i));
		                        }
		                        rows.add(row);
		                    }
		                    result.put("data", rows);
		                }
		            }

		            return result;
		        }
		    });
		}



}
