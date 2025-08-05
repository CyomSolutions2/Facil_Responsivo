package coralpagos.com.mx.facil.app.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import coralpagos.com.mx.facil.app.dto.Modulos;
import coralpagos.com.mx.facil.app.dto.TiposReportes;
import coralpagos.com.mx.orm.facil.Usuario;

public interface AdministracionReportesRepository extends CrudRepository<Usuario, Long> {
	
	Logger log4j = LogManager.getLogger(AdministracionReportesRepository.class);
	
	@Query(value = "EXEC [spObtieneTiposReportes]", nativeQuery = true)
	List<Object[]> executeSpObtieneTiposReportes();
	
	default List<TiposReportes> obtieneTiposReportes() {
		log4j.info("Ejecución de spObtieneTiposReportes");
	    return executeSpObtieneTiposReportes().stream()
	        .map(row -> {
	        	TiposReportes tipoReporte = new TiposReportes();
	        	tipoReporte.setIdTipoReporte(row[0] != null ? row[0].toString() : null);
	        	tipoReporte.setTipoReporte(row[1] != null ? row[1].toString() : null);
	        	tipoReporte.setTabla(row[2] != null ? row[2].toString() : null);
	        	tipoReporte.setClave(row[3] != null ? row[3].toString() : null);
	            return tipoReporte;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneTiposReportesPorRol] :IdRol", nativeQuery = true)
	List<Object[]> executeSpObtieneTiposReportesPorRol(@Param("IdRol") int idRol);
	
	default List<TiposReportes> getReportesRol(int idRol) {
		 log4j.info("Ejecución de spObtieneTiposReportesPorRol con parámetro: " + idRol );
	    return executeSpObtieneTiposReportesPorRol(idRol).stream()
	        .map(row -> {
	        	TiposReportes tipoReporte = new TiposReportes();
	        	tipoReporte.setIdTipoReporte(row[0] != null ? row[0].toString() : null);
	        	tipoReporte.setTipoReporte(row[1] != null ? row[1].toString() : null);
	        	tipoReporte.setTabla(row[2] != null ? row[2].toString() : null);
	        	tipoReporte.setClave(row[3] != null ? row[3].toString() : null);
	            return tipoReporte;
	        })
	        .collect(Collectors.toList());
	}
	

	@Modifying
	@Query(value = "EXEC [spInsReportesRoles] :IdRol, :IdReportes, :Usuario", nativeQuery = true)
	void executeSpInsReportesRoles(@Param("IdRol") int idRol, @Param("IdReportes") String idReportes, @Param("Usuario") String usuario);

	default void guardarReportesRoles(int idRol, String idReportes, String usuario) {
		try {
			log4j.info("Ejecución de spInsReportesRoles con parámetros: " + idRol + " - " +  idReportes 
					+ " con usuario: " + usuario);
			executeSpInsReportesRoles(idRol, idReportes,  usuario);
			log4j.info("Se insertó reportes roles con id rol " + idRol + " correctamente por " + usuario + " con idReportes: " + idReportes);
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spInsReportesRoles] :IdRol, :IdReportes, :Usuario con los valores respectivos: " + idRol + " - " +  idReportes 
					+ " con usuario: " + usuario);
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al insertar reportes roles del id rol" + idRol + " correctamente por " + usuario + " con idReportes: " 
			+ idReportes ,ex);
		}
	}

}
