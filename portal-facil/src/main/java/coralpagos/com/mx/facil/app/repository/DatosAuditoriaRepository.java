package coralpagos.com.mx.facil.app.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import coralpagos.com.mx.orm.facil.DatosAuditoria;

public interface DatosAuditoriaRepository extends CrudRepository<DatosAuditoria, Long> {
	
	Logger log4j = LogManager.getLogger(DatosAuditoriaRepository.class);

	
	@Modifying
	@Query(value = "EXEC [spInsDatosAuditoria] :Modulo, :Metodo, :Accion, :Membresia, :Usuario, :Ip, :Mensaje, :Notas, :Estatus, :Paso, :Sp", nativeQuery = true)
	void executeSpInsDatosAuditoria(@Param("Modulo") String modulo, @Param("Metodo") String metodo, @Param("Accion") String accion, @Param("Membresia") String membresia, @Param("Usuario") String usuario,
			                    @Param("Ip") String ip, @Param("Mensaje") String mensaje, @Param("Notas") String notas, @Param("Estatus") String estatus, @Param("Paso") String paso,
			                    @Param("Sp") String sp);

	default void saveDataAudit(String modulo, String metodo, String accion, String membresia, String usuario, String ip, String mensaje, String notas, String estatus, String paso, String sp) {
		try {
			executeSpInsDatosAuditoria(modulo, metodo, accion, membresia, usuario, ip, mensaje, notas, estatus, paso, sp);
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spInsDatosAuditoria] :Modulo, :Metodo, ;Accion, :Membresia, :Usuario, :Ip, :Mensaje, :Notas, :Estatus, :Paso, :Sp "
					+ "con los valores respectivos: " + modulo + " , " + metodo + " , " + accion + " , " + membresia + " , " + usuario + " , " + ip + " , " + mensaje + " , " + notas + " , " + estatus
					+ " , " + paso + " , " + sp );
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al guardar datos de auditoria",ex);
		}
	}
}
