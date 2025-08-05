package coralpagos.com.mx.facil.app.repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import coralpagos.com.mx.facil.app.dto.BuscaUsuarios;
import coralpagos.com.mx.facil.app.dto.ModulosUrl;
import coralpagos.com.mx.orm.facil.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
	 Logger log4j = LogManager.getLogger(UsuarioRepository.class);

	Optional<Usuario> findByUsrUsuarioAndUsrLsvEstatusUsuarios( String usrUsuario, Integer usrLsvEstatusUsuarios );
	
	@Query(value = "EXEC [spBuscaUsuariosV2] :NombreUsuario, :EstatusUsuario", nativeQuery = true)
	List<Object[]> executeSpBuscaUsuariosV2(@Param("NombreUsuario") String nombreUsuario, @Param("EstatusUsuario") int estatusUsuario);
	SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
	default List<BuscaUsuarios> findUsers(String nombreUsuario, int estatusUsuario) {
		log4j.info("Ejecución de spBuscaUsuariosV2 con parámetros: " + nombreUsuario + " - " + estatusUsuario );
	    return executeSpBuscaUsuariosV2(nombreUsuario, estatusUsuario).stream()
	        .map(row -> {
	        	BuscaUsuarios buscaUsuarios = new BuscaUsuarios();
	        	buscaUsuarios.setUsuario(row[0] != null ? row[0].toString() : null);
	        	buscaUsuarios.setNombreUsuario(row[1] != null ? row[1].toString() : null);
	        	buscaUsuarios.setUsr_emp_id(row[2] != null ? row[2].toString() : null);
	        	buscaUsuarios.setUsr_lsv_desarrollos(row[3] != null ? row[3].toString() : null);
	        	buscaUsuarios.setDescDesarrollo(row[4] != null ? row[4].toString() : null);
	        	try {
					buscaUsuarios.setUsr_fec_actualizacion(row[5] != null ? formato.parse(row[5].toString()) : null);
				} catch (ParseException e) {
					log4j.error(e);
					e.printStackTrace();
				}
	            buscaUsuarios.setUsr_diasVigencia(row[6] != null ? row[6].toString() : null);
	        	buscaUsuarios.setUsr_lsv_estatusUsuarios(row[7] != null ? row[7].toString() : null);
	        	buscaUsuarios.setEstatus(row[8] != null ? row[8].toString() : null);
	        	buscaUsuarios.setUsr_ejecutivoCobranza(row[9] != null ? row[9].toString() : null);
	        	buscaUsuarios.setUsr_lsv_ubicacionUsuarios(row[10] != null ? row[10].toString() : null);
	        	buscaUsuarios.setUbicacionUsuarios(row[11] != null ? row[11].toString() : null);
	        	buscaUsuarios.setUsr_emisor_recibos(row[12] != null ? row[12].toString() : null);
	        	buscaUsuarios.setUsr_rol_id(row[13] != null ? row[13].toString() : null);
	        	buscaUsuarios.setRolDescripcion(row[14] != null ? row[14].toString() : null);
	        	buscaUsuarios.setInicialesUsuarioEmpleado(row[15] != null ? row[15].toString() : null);
	        	
	            return buscaUsuarios;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spBuscaModuloPath] :idModulo", nativeQuery = true)
	List<Object[]> executeSpBuscaModuloPath(@Param("idModulo") int idModulo);
	default List<ModulosUrl> findModulePath(int idModulo) {
		log4j.info("Ejecución de spBuscaModuloPath con parámetro: " + idModulo );
	    return executeSpBuscaModuloPath(idModulo).stream()
	        .map(row -> {
	        	ModulosUrl moduloUrl = new ModulosUrl();
	        	moduloUrl.setMu_mdl_id(row[0] != null ? row[0].toString() : null);
	        	moduloUrl.setMu_path(row[1] != null ? row[1].toString() : null);
	            return moduloUrl;
	        })
	        .collect(Collectors.toList());
	}
	
	@Modifying
	@Query(value = "EXEC [spCambiarPasswordUsuario] :Usuario, :Password", nativeQuery = true)
	void executeSpCambiarPasswordUsuario(@Param("Usuario") String usuario, @Param("Password") String password);
	default void cambiarPassUsuario(String usuario, String password) {
		try {
			log4j.info("Ejecución de spCambiarPasswordUsuario con parámetros: " + usuario + " - " + password );
			executeSpCambiarPasswordUsuario(usuario, password);
			log4j.info("Se cambió password del usuario " + usuario);
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spCambiarPasswordUsuario] :Usuario, :Password con los valores respectivos: " + usuario + " - " +  password);
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al cambiar password del usuario: " + usuario,ex);
		}
	}
	
	
}
