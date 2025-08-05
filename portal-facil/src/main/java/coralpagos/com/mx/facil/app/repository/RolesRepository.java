package coralpagos.com.mx.facil.app.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import coralpagos.com.mx.facil.app.dto.Roles;
import coralpagos.com.mx.facil.app.dto.UsuariosRoles;
import coralpagos.com.mx.facil.app.dto.Modulos;
import coralpagos.com.mx.facil.app.dto.ModulosRoles;


public interface RolesRepository extends CrudRepository<coralpagos.com.mx.orm.facil.Roles, Long> {
	 Logger log4j = LogManager.getLogger(RolesRepository.class);
	List<Roles> findByRolContainingIgnoreCase(String rol);
	Roles findByRol(String rol);
	
	/*@Procedure(procedureName = "spObtieneRoles")
	List<coralpagos.com.mx.facil.app.dto.Roles> findAllRoles();*/
	
	 /*@Query(value = "EXEC spObtieneRoles", nativeQuery = true)
	    List<Object[]> executeSpObtieneRoles();
	    
	    default List<coralpagos.com.mx.facil.app.dto.Roles> findAllRoles() {
	        return executeSpObtieneRoles().stream()
	            .map(row -> new coralpagos.com.mx.facil.app.dto.Roles() {
	                public String getRol_Id() { return row[2].toString(); }
	                public String getRol_Descripcion() { return row[3].toString(); }
	            })
	            .collect(Collectors.toList());
	    }*/
	
	@Query(value = "EXEC spObtieneRolesV2", nativeQuery = true)
	List<Object[]> executeSpObtieneRoles();

	default List<Roles> findAllRoles() {
		log4j.info("Ejecución de spObtieneRolesV2");
	    return executeSpObtieneRoles().stream()
	        .map(row -> {
	        	Roles rol = new Roles();
	            rol.setRol_Id(row[0] != null ? row[0].toString() : null); // ROL_ID
	            rol.setRol_Descripcion(row[1] != null ? row[1].toString() : null); // ROL_DESCRIPCION
	            rol.setRol_Activo(row[2] != null ? (boolean) row[2] : false); //ROL_ACTIVO
	            return rol;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneRolesPorCoincidenciaNombre] :nombreRol", nativeQuery = true)
	List<Object[]> executeSpObtieneRolesPorCoincidenciaNombre(@Param("nombreRol") String nombreRol);

	default List<Roles> findRolesByNameContaining(String nombreRol) {
		log4j.info("Ejecución de spObtieneRolesPorCoincidenciaNombre con parámetro: " + nombreRol );
	    return executeSpObtieneRolesPorCoincidenciaNombre(nombreRol).stream()
	        .map(row -> {
	        	Roles rol = new Roles();
	            rol.setRol_Id(row[0] != null ? row[0].toString() : null); // ROL_ID
	            rol.setRol_Descripcion(row[1] != null ? row[1].toString() : null); // ROL_DESCRIPCION
	            rol.setRol_Activo(row[2] != null ? (boolean) row[2] : false); //ROL_ACTIVO
	            return rol;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneRolesPorNombre] :nombreRol", nativeQuery = true)
	List<Object[]> executeSpObtieneRolesPorNombre(@Param("nombreRol") String nombreRol);

	default Roles findRoleByName(String nombreRol) {
	    try {
	    	log4j.info("Ejecución de spObtieneRolesPorNombre con parámetro: " + nombreRol );
	        List<Object[]> resultados = executeSpObtieneRolesPorNombre(nombreRol);
	        if (resultados == null || resultados.isEmpty()) {
	            return null;
	        }

	        // Obtener el primer registro
	        Object[] primerRegistro = resultados.get(0);
	        
	        Roles rol = new Roles();
	        rol.setRol_Id(primerRegistro[0] != null ? primerRegistro[0].toString() : null);
	        rol.setRol_Descripcion(primerRegistro[1] != null ? primerRegistro[1].toString() : null);
	        rol.setRol_Activo(primerRegistro[2] != null ? (boolean) primerRegistro[2] : false); //ROL_ACTIVO
	        return rol;
	    } catch (Exception e) {
			log4j.error(e.toString() + "  - Error en la ejecución del SP [spObtieneRolesPorNombre] :nombreRol con los valores respectivos: " + nombreRol);
			e.printStackTrace();
	        throw new RuntimeException("Error al buscar rol por nombre", e);
	    }
	}
	
	@Modifying
	@Query(value = "EXEC [spInsNuevosRoles] :nombreRol , :idRolHereda", nativeQuery = true)
	void executeSpInsNuevosRoles(@Param("nombreRol") String nombreRol, @Param("idRolHereda") int idRolHereda);

	default void saveRole(String nombreRol, int idRolHereda) {
		try {
			log4j.info("Ejecución de spInsNuevosRoles con parámetros: " + nombreRol + " - " + idRolHereda);
			executeSpInsNuevosRoles(nombreRol, idRolHereda);
			log4j.info("Se creo rol " + nombreRol + " correctamente");
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spInsNuevosRoles] :nombreRol , :idRolHereda con los valores respectivos: " + nombreRol + " , " + idRolHereda);
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al crear el rol " + nombreRol,ex);
		}
	}
	
	@Modifying
	@Query(value = "EXEC [spDelRoles] :claveRol", nativeQuery = true)
	void executeSpDelRoles(@Param("claveRol") Long claveRol);

	default void deleteRole(Long claveRol, String nombreRol) {
		try {
			log4j.info("Ejecución de spDelRoles con parámetros: " + claveRol);
			executeSpDelRoles(claveRol);		
			log4j.info("Se eliminó rol " + nombreRol + " correctamente");
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spDelRoles] :claveRol con los valores respectivos: " + claveRol );
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al crear el rol " + nombreRol,ex);
		}
	}
	
	@Modifying
	@Query(value = "EXEC [spActivaDesactivaRoles] :Activo, :RolId", nativeQuery = true)
	void executeSpActivaDesactivaRoles(@Param("Activo") boolean activo, @Param("RolId") int rolId);

	default void enableDisableRole(boolean activo, int rolId, String nombreRol) {
		String actDescStr = null;
		try {
			 actDescStr = activo ? "activar" : "desactivar";
			 log4j.info("Ejecución de spActivaDesactivaRoles con parámetros: " + activo + " - " + rolId);
			executeSpActivaDesactivaRoles(activo,rolId);
			log4j.info("Se " + actDescStr + " el rol " + nombreRol + " correctamente");
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spActivaDesactivaRoles] :Activo, :RolId con los valores respectivos: " + activo + " , " + rolId);
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al " + actDescStr + " el rol " + nombreRol,ex);
		}	
	}
	
	
	@Query(value = "EXEC [spObtieneUsuariosXRol] :ClaveRol", nativeQuery = true)
	List<Object[]> executeSpObtieneUsuariosXRol(@Param("ClaveRol") int rolId);

	default List<UsuariosRoles> getUsersByRole(int rolId) {
		log4j.info("Ejecución de spObtieneUsuariosXRol con parámetro: " + rolId);
	    return executeSpObtieneUsuariosXRol(rolId).stream()
	        .map(row -> {
	        	UsuariosRoles usuarioRol = new UsuariosRoles();
	        	usuarioRol.setUro_rol_id(row[0] != null ? row[0].toString() : null);
	        	usuarioRol.setUro_usr_usuario(row[1] != null ? row[1].toString() : null);
	            return usuarioRol;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spModulosAutorizadosXRolV2] :ClaveRol", nativeQuery = true)
	List<Object[]> executeSpModulosAutorizadosXRol(@Param("ClaveRol") int rolId);
	
	default List<ModulosRoles> findModulesByRole(int rolId) {
		log4j.info("Ejecución de spModulosAutorizadosXRolV2 con parámetro: " + rolId);
	    return executeSpModulosAutorizadosXRol(rolId).stream()
	        .map(row -> {
	        	ModulosRoles moduloRol = new ModulosRoles();
	        	moduloRol.setRom_rol_id(row[0] != null ? row[0].toString() : null);
	        	moduloRol.setRom_mdl_id(row[1] != null ? row[1].toString() : null);
	        	moduloRol.setMdl_padre_id(row[2] != null ? row[2].toString() : null);
	        	moduloRol.setMdl_clave(row[3] != null ? row[3].toString() : null);
	        	moduloRol.setMdl_nombre(row[4] != null ? row[4].toString() : null);
	            return moduloRol;
	        })
	        .collect(Collectors.toList());
	}
	
	@Modifying
	@Query(value = "EXEC [spInsRolesModulosV2] :idRol , :idsModulos", nativeQuery = true)
	void executeSpInsHerenciaRoles(@Param("idRol") int idRol, @Param("idsModulos") String idsModulos);

	default void saveInheritance(int idRol, int idRolHereda, String rolNombre, String rolNombreHereda, String idsModulos) {
		try {
			log4j.info("Ejecución de spInsRolesModulosV2 con parámetro: " + idRol + " - " + idsModulos);
			executeSpInsHerenciaRoles(idRol, idsModulos);
			log4j.info("El rol " + rolNombre + " hereda de " + rolNombreHereda + " correctamente");
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spInsRolesModulosV2] :idRol , :idsModulos con los valores respectivos: " + idRol + " , " + idsModulos);
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al heredar el rol " + rolNombre + " del rol " + rolNombreHereda + "con los modulos " + idsModulos ,ex);
		}
	}
	
		
	@Query(value = "EXEC [spObtieneModulosV2]", nativeQuery = true)
	List<Object[]> executeSpObtieneModulos();
	
	default List<Modulos> getModules() {
		log4j.info("Ejecución de spObtieneModulosV2");
	    return executeSpObtieneModulos().stream()
	        .map(row -> {
	        	Modulos modulo = new Modulos();
	        	modulo.setMdl_id(row[0] != null ? row[0].toString() : null);
	        	modulo.setMdl_padre_id(row[1] != null ? row[1].toString() : null);
	        	modulo.setMdl_clave(row[2] != null ? row[2].toString() : null);
	        	modulo.setMdl_nombre(row[3] != null ? row[3].toString() : null);
	        	modulo.setMdl_orden(row[4] != null ? row[4].toString() : null);
	            return modulo;
	        })
	        .collect(Collectors.toList());
	}
	
	@Modifying
	@Query(value = "EXEC [spInsRolesModulosV2] :idRol , :idsModulos", nativeQuery = true)
	void executeSpRolesModulesEdit(@Param("idRol") int idRol, @Param("idsModulos") String idsModulos);

	default void editModulesRoles(int idRol, String rolNombre,  String idsModulos) {
		try {
			log4j.info("Ejecución de spInsRolesModulosV2 con parámetro: " + idRol + " - " + idsModulos);
			executeSpRolesModulesEdit(idRol, idsModulos);
			log4j.info("Se modifica el rol " + rolNombre +  " correctamente");
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spInsRolesModulosV2] :idRol , :idsModulos con los valores respectivos: " + idRol + " , " + idsModulos);
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al modificar el rol " + rolNombre + "con los modulos " + idsModulos ,ex);
		}
	}
	
}
