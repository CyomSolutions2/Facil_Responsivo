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
import coralpagos.com.mx.facil.app.dto.SeriesRecibos;
import coralpagos.com.mx.facil.app.dto.TipoCuponesDescuentoPaq;
import coralpagos.com.mx.facil.app.dto.TiposMovimientos;
import coralpagos.com.mx.facil.app.dto.TiposProductos;
import coralpagos.com.mx.facil.app.dto.Usuarios;
import coralpagos.com.mx.facil.app.dto.UsuariosRoles;
import coralpagos.com.mx.facil.app.dto.Cartera;
import coralpagos.com.mx.facil.app.dto.ClasificacionMembresia;
import coralpagos.com.mx.facil.app.dto.Desarrollos;
import coralpagos.com.mx.facil.app.dto.Empleados;
import coralpagos.com.mx.facil.app.dto.Estados;
import coralpagos.com.mx.facil.app.dto.EstatusRecibos;
import coralpagos.com.mx.facil.app.dto.EstatusUnidades;
import coralpagos.com.mx.facil.app.dto.Locaciones;
import coralpagos.com.mx.facil.app.dto.MemoriaTecnicaParamReportes;
import coralpagos.com.mx.facil.app.dto.Modulos;
import coralpagos.com.mx.facil.app.dto.ModulosRoles;
import coralpagos.com.mx.facil.app.dto.ParametrosReportes;
import coralpagos.com.mx.facil.app.dto.ProcedimientosReportes;
import coralpagos.com.mx.facil.app.dto.Promotores;
import coralpagos.com.mx.facil.app.dto.ReportesEspeciales;


public interface ReportesEspecialesRepository extends CrudRepository<coralpagos.com.mx.orm.facil.Roles, Long> {
	 Logger log4j = LogManager.getLogger(ReportesEspecialesRepository.class);
		

	
	@Query(value = "EXEC [spObtieneTiposdeReportesPorModulo] :IdModulo, :IdRol", nativeQuery = true)
	List<Object[]> executeSpObtieneTiposdeReportes(@Param("IdModulo") int idModulo, @Param("IdRol") int idRol);

	default List<ReportesEspeciales> getReportsType(int idModulo, int idRol) {
		log4j.info("Ejecución de spObtieneTiposdeReportesPorModulo con parámetros: " + idModulo + " - " + idRol );
	    return executeSpObtieneTiposdeReportes(idModulo,idRol).stream()
	        .map(row -> {
	        	ReportesEspeciales reporte = new ReportesEspeciales();
	        	reporte.setIdReporte(row[0] != null ? row[0].toString() : null); // LSV_ID
	        	reporte.setReporte(row[1] != null ? row[1].toString() : null); // LSV_DESCRIPCION
	        	reporte.setTabla(row[2] != null ? row[2].toString() : null); //LSV_TABLA
	        	reporte.setClave(row[3] != null ? row[3].toString() : null); //LSV_CLAVE
	            return reporte;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametrosReportes] :IdTipoReporte", nativeQuery = true)
	List<Object[]> executeSpObtieneParametrosReportes(@Param("IdTipoReporte") int idTipoReporte);

	default List<ParametrosReportes> obtieneParametrosReportes(int idTipoReporte) {
		log4j.info("Ejecución de spObtieneParametrosReportes con parámetro: " + idTipoReporte);
	    return executeSpObtieneParametrosReportes(idTipoReporte).stream()
	        .map(row -> {
	        	ParametrosReportes parametroRep = new ParametrosReportes();
	        	parametroRep.setIdParametro(row[0] != null ? row[0].toString() : null); // PRPT_ID
	        	parametroRep.setIdReporte(row[1] != null ? row[1].toString() : null); // PRPT_LSV_TIPOSREPORTES
	        	parametroRep.setTipoReporte(row[2] != null ? row[2].toString() : null); //TIPOREPORTE
	        	parametroRep.setLsvParametroReporte(row[3] != null ? row[3].toString() : null); //PRPT_LSV_PARAMETROSREPORTES
	        	parametroRep.setParametroReporte(row[4] != null ? row[4].toString() : null);    //PARAMETROREPORTE
	            return parametroRep;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroCartera]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroCartera();

	default List<Cartera> obtenerParametroCartera() {
		log4j.info("Ejecución de spObtieneParametroCartera");
	    return executeSpObtieneParametroCartera().stream()
	        .map(row -> {
	        	Cartera cartera = new Cartera();
	        	cartera.setCcbId(row[0] != null ? row[0].toString() : null); 
	        	cartera.setCcbCartera(row[1] != null ? row[1].toString() : null); 
	        	cartera.setCcbRangoDiasInicio(row[2] != null ? row[2].toString() : null); 
	        	cartera.setCcbRangoDiasFinal(row[3] != null ? row[3].toString() : null);
	            return cartera;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroClasificacionMembresia]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroClasificacionMembresia();

	default List<ClasificacionMembresia> obtenerParametroClasificacionMembresia() {
		log4j.info("Ejecución de spObtieneParametroClasificacionMembresia");
	    return executeSpObtieneParametroClasificacionMembresia().stream()
	        .map(row -> {
	        	ClasificacionMembresia clasificacionCartera = new ClasificacionMembresia();
	        	clasificacionCartera.setIdClasificacion(row[0] != null ? row[0].toString() : null); // LSV_ID
	        	clasificacionCartera.setClasificacion(row[1] != null ? row[1].toString() : null); // LSV_DESCRIPCION
	        	clasificacionCartera.setTabla(row[2] != null ? row[2].toString() : null); //LSV_TABLA
	        	clasificacionCartera.setClave(row[3] != null ? row[3].toString() : null); //LSV_CLAVE
	            return clasificacionCartera;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroDesarrollos]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroDesarrollos();

	default List<Desarrollos> obtenerParametroDesarrollo() {
		log4j.info("Ejecución de spObtieneParametroDesarrollos");
	    return executeSpObtieneParametroDesarrollos().stream()
	        .map(row -> {
	        	Desarrollos desarrollo = new Desarrollos();
	        	desarrollo.setIdDesarrollo(row[0] != null ? row[0].toString() : null); // LSV_ID
	        	desarrollo.setDesarrollo(row[1] != null ? row[1].toString() : null); // LSV_DESCRIPCION
	        	desarrollo.setTabla(row[2] != null ? row[2].toString() : null); //LSV_TABLA
	        	desarrollo.setClave(row[3] != null ? row[3].toString() : null); //LSV_CLAVE
	            return desarrollo;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroEmpleados]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroEmpleados();
	
	default List<Empleados> obtenerParametroEmpleados() {
		log4j.info("Ejecución de spObtieneParametroEmpleados");
	    return executeSpObtieneParametroEmpleados().stream()
	        .map(row -> {
	        	Empleados empleados = new Empleados();
	        	empleados.setEmpId(row[0] != null ? row[0].toString() : null); 
	        	empleados.setNombreEmpleado(row[1] != null ? row[1].toString() : null);
	        	empleados.setEmpSuperiorId(row[2] != null ? row[2].toString() : null); 
	        	empleados.setUsrLsvDesarrollos(row[3] != null ? row[3].toString() : null); 
	        	empleados.setDesarrollo(row[4] != null ? row[4].toString() : null); 
	            return empleados;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroEstados]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroEstados();
	
	default List<Estados> obtenerParametroEstados() {
		log4j.info("Ejecución de spObtieneParametroEstados");
	    return executeSpObtieneParametroEstados().stream()
	        .map(row -> {
	        	Estados estados = new Estados();
	        	estados.setIdEstado(row[0] != null ? row[0].toString() : null); 
	        	estados.setEstado(row[1] != null ? row[1].toString() : null);
	        	estados.setTabla(row[2] != null ? row[2].toString() : null); 
	        	estados.setClave(row[3] != null ? row[3].toString() : null); 
	            return estados;
	        })
	        .collect(Collectors.toList());
	}
	
	
	@Query(value = "EXEC [spObtieneParametroEstatusRecibos]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroEstatusRecibos();
	default List<EstatusRecibos> obtenerParametroEstatusRecibos() {
		log4j.info("Ejecución de spObtieneParametroEstatusRecibos");
	    return executeSpObtieneParametroEstatusRecibos().stream()
	        .map(row -> {
	        	EstatusRecibos estatusRecibos = new EstatusRecibos();
	        	estatusRecibos.setIdEstatusRecibo(row[0] != null ? row[0].toString() : null); 
	        	estatusRecibos.setEstatusRecibo(row[1] != null ? row[1].toString() : null);
	        	estatusRecibos.setTabla(row[2] != null ? row[2].toString() : null); 
	        	estatusRecibos.setClave(row[3] != null ? row[3].toString() : null); 
	            return estatusRecibos;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroEstatusUnidades]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroEstatusUnidades();

	default List<EstatusUnidades> obtenerParametroEstatusUnidades() {
		log4j.info("Ejecución de spObtieneParametroEstatusUnidades");
	    return executeSpObtieneParametroEstatusUnidades().stream()
	        .map(row -> {
	        	EstatusUnidades estatusUnidades = new EstatusUnidades();
	        	estatusUnidades.setIdEstatusUnidad(row[0] != null ? row[0].toString() : null); 
	        	estatusUnidades.setEstatusUnidad(row[1] != null ? row[1].toString() : null);
	        	estatusUnidades.setTabla(row[2] != null ? row[2].toString() : null); 
	        	estatusUnidades.setClave(row[3] != null ? row[3].toString() : null); 
	            return estatusUnidades;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroLocaciones]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroLocaciones();

	default List<Locaciones> obtenerParametroLocaciones() {
		log4j.info("Ejecución de spObtieneParametroLocaciones");
	    return executeSpObtieneParametroLocaciones().stream()
	        .map(row -> {
	        	Locaciones locaciones = new Locaciones();
	        	locaciones.setIdLocacion(row[0] != null ? row[0].toString() : null); 
	        	locaciones.setLocacion(row[1] != null ? row[1].toString() : null);
	        	locaciones.setTabla(row[2] != null ? row[2].toString() : null); 
	        	locaciones.setClave(row[3] != null ? row[3].toString() : null); 
	            return locaciones;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroPromotores]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroPromotores();

	default List<Promotores> obtenerParametroPromotores() {
		log4j.info("Ejecución de spObtieneParametroPromotores");
	    return executeSpObtieneParametroPromotores().stream()
	        .map(row -> {
	        	Promotores promotores = new Promotores();
	        	promotores.setIdPromotor(row[0] != null ? row[0].toString() : null); 
	        	promotores.setPromotor(row[1] != null ? row[1].toString() : null);
	            return promotores;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroSeriesRecibos]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroSeriesRecibos();

	default List<SeriesRecibos> obtenerParametroSeriesRecibos() {
		log4j.info("Ejecución de spObtieneParametroSeriesRecibos");
	    return executeSpObtieneParametroSeriesRecibos().stream()
	        .map(row -> {
	        	SeriesRecibos seriesRecibos = new SeriesRecibos();
	        	seriesRecibos.setIdSerieRecibo(row[0] != null ? row[0].toString() : null); 
	        	seriesRecibos.setSerieRecibo(row[1] != null ? row[1].toString() : null);
	        	seriesRecibos.setTabla(row[2] != null ? row[2].toString() : null); 
	        	seriesRecibos.setClave(row[3] != null ? row[3].toString() : null); 
	            return seriesRecibos;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroTipoCuponesDescuentoPaq]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroTipoCuponesDescuentoPaq();

	default List<TipoCuponesDescuentoPaq> obtenerParametroTipoCuponesDescuentoPaq() {
		log4j.info("Ejecución de spObtieneParametroTipoCuponesDescuentoPaq");
	    return executeSpObtieneParametroTipoCuponesDescuentoPaq().stream()
	        .map(row -> {
	        	TipoCuponesDescuentoPaq tipoCuponesDescuentoPaq = new TipoCuponesDescuentoPaq();
	        	tipoCuponesDescuentoPaq.setIdTipoCuponDescuentoPaq(row[0] != null ? row[0].toString() : null); 
	        	tipoCuponesDescuentoPaq.setTipoCuponDescuentoPaq(row[1] != null ? row[1].toString() : null);
	        	tipoCuponesDescuentoPaq.setTabla(row[2] != null ? row[2].toString() : null); 
	        	tipoCuponesDescuentoPaq.setClave(row[3] != null ? row[3].toString() : null); 
	            return tipoCuponesDescuentoPaq;
	        })
	        .collect(Collectors.toList());
	}
	
	
	@Query(value = "EXEC [spObtieneParametroTiposMovimientos]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroTiposMovimientos();

	default List<TiposMovimientos> obtenerParametroTiposMovimiento() {
		log4j.info("Ejecución de spObtieneParametroTiposMovimientos");
	    return executeSpObtieneParametroTiposMovimientos().stream()
	        .map(row -> {
	        	TiposMovimientos tipoMov = new TiposMovimientos();
	        	tipoMov.setTmv_tipomovimiento(row[0] != null ? row[0].toString() : null);
	        	tipoMov.setTmv_descripcion(row[1] != null ? row[1].toString() : null); 
	        	tipoMov.setTmv_lsv_periodicidad(row[2] != null ? row[2].toString() : null); 
	        	tipoMov.setTmv_lsv_clasificacionmovimientos(row[3] != null ? row[3].toString() : null); 
	        	tipoMov.setTmv_lsv_categoriamovimientos(row[4] != null ? row[4].toString() : null); // LSV_ID
	        	tipoMov.setTmv_lsv_tipoasignacion_movimientos(row[5] != null ? row[5].toString() : null); // LSV_ID
	        	tipoMov.setTmv_lsv_basedecobro(row[6] != null ? row[6].toString() : null); // LSV_ID
	        	tipoMov.setTmv_tipoimporte_basedecobro(row[7] != null ? row[7].toString() : null); // LSV_ID
	        	tipoMov.setTmv_establece_cuota(row[8] != null ? row[8].toString() : null); // LSV_ID
	        	tipoMov.setTmv_generacion_venta(row[9] != null ? row[9].toString() : null); // LSV_ID
	        	tipoMov.setTmv_movimiento_paquete_anual(row[10] != null ? row[10].toString() : null);
	        	tipoMov.setTmv_genera_cargo_uso_tdc(row[11] != null ? row[11].toString() : null);
	        	tipoMov.setTmv_solicita_datos_contacto(row[12] != null ? row[12].toString() : null);
	            return tipoMov;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroTiposProductos]", nativeQuery = true)
	List<Object[]> executeSpObtieneParametroTiposProductos();

	default List<TiposProductos> obtenerParametroTiposProductos() {
		log4j.info("Ejecución de spObtieneParametroTiposProductos");
	    return executeSpObtieneParametroTiposProductos().stream()
	        .map(row -> {
	        	TiposProductos tiposProductos = new TiposProductos();
	        	tiposProductos.setIdTipoProducto(row[0] != null ? row[0].toString() : null); 
	        	tiposProductos.setTipoProducto(row[1] != null ? row[1].toString() : null);
	        	tiposProductos.setTabla(row[2] != null ? row[2].toString() : null); 
	        	tiposProductos.setClave(row[3] != null ? row[3].toString() : null); 
	            return tiposProductos;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneParametroUsuarios]", nativeQuery = true)// PENDIENTE
	List<Object[]> executeSpObtieneParametroUsuarios();

	default List<Usuarios> obtenerParametroUsuarios() {
		log4j.info("Ejecución de spObtieneParametroUsuarios");
	    return executeSpObtieneParametroUsuarios().stream()
	        .map(row -> {
	        	Usuarios usuarios = new Usuarios();
	        	usuarios.setUsrUsuario(row[0] != null ? row[0].toString() : null);
	        	usuarios.setNombre(row[1] != null ? row[1].toString() : null); 
	        	usuarios.setUsrEmpId(row[2] != null ? row[2].toString() : null); 
	        	usuarios.setUsrLsvDesarrollos(row[3] != null ? row[3].toString() : null); 
	        	usuarios.setDescDesarrollo(row[4] != null ? row[4].toString() : null); // LSV_ID
	        	usuarios.setUsrLsvEstatusUsuarios(row[5] != null ? row[5].toString() : null); // LSV_ID
	        	usuarios.setEstatus(row[6] != null ? row[6].toString() : null); // LSV_ID
	        	usuarios.setUsrEjecutivoCobranza(row[7] != null ? row[7].toString() : null); // LSV_ID
	        	usuarios.setUsrLsvUbicacionUsuarios(row[8] != null ? row[8].toString() : null); // LSV_ID
	        	usuarios.setUbicacionUsuarios(row[9] != null ? row[9].toString() : null); // LSV_ID
	        	usuarios.setUsrEmisorDeRecibos(row[10] != null ? row[10].toString() : null);
	        	usuarios.setUsrRolId(row[11] != null ? row[11].toString() : null);
	            return usuarios;
	        })
	        .collect(Collectors.toList());
	}
	
	
	@Query(value = "EXEC [spObtieneProcedimientoDeReporte] :IdTipoReporte", nativeQuery = true)
	List<Object[]> executeSpObtieneProcedimientoDeReporte(@Param("IdTipoReporte") int idTipoReporte);

	default List<ProcedimientosReportes> obtieneProcedimientoReporte(int idTipoReporte) {
		log4j.info("Ejecución de spObtieneProcedimientoDeReporte con parámetro: " + idTipoReporte );;
	    return executeSpObtieneProcedimientoDeReporte(idTipoReporte).stream()
	        .map(row -> {
	        	ProcedimientosReportes procedimientoReporte = new ProcedimientosReportes();
	        	procedimientoReporte.setIdProcedimiento(row[0] != null ? row[0].toString() : null); // PDBRPT_ID
	        	procedimientoReporte.setIdReporte(row[1] != null ? row[1].toString() : null); // PDBRPT_LSV_TIPOSREPORTES
	        	procedimientoReporte.setProcedimiento(row[2] != null ? row[2].toString() : null); //PDBRPT_PROCEDIMIENTO_DB
	        	procedimientoReporte.setNumeroParametros(row[3] != null ? row[3].toString() : null); //PDBRPT_NUMERO_PARAMETROS
	            return procedimientoReporte;
	        })
	        .collect(Collectors.toList());
	}
	
	@Query(value = "EXEC [spObtieneMemoriaTecnicaParametrosReportes] :IdTipoReporte, :Usuario", nativeQuery = true)
	List<Object[]> executeSpObtieneMemoriaTecnicaParametrosReportes(@Param("IdTipoReporte") int idTipoReporte, @Param("Usuario") String usuario);

	default List<MemoriaTecnicaParamReportes> obtieneMemoriaTecnicaParamReportes(int idTipoReporte, String usuario) {
		log4j.info("Ejecución de spObtieneMemoriaTecnicaParametrosReportes con parámetros: " + idTipoReporte + " - " + usuario );
	    return executeSpObtieneMemoriaTecnicaParametrosReportes(idTipoReporte, usuario).stream()
	        .map(row -> {
	        	MemoriaTecnicaParamReportes memoriaTecnica = new MemoriaTecnicaParamReportes();
	        	memoriaTecnica.setIdMemoriaTecnica(row[0] != null ? row[0].toString() : null); // MTPR_ID
	        	memoriaTecnica.setUsuario(row[1] != null ? row[1].toString() : null); // MTPR_USR_USUARIO
	        	memoriaTecnica.setIdReporte(row[2] != null ? row[2].toString() : null); //MTPR_LSV_TIPO_REPORTE
	        	memoriaTecnica.setIdParametroReporte(row[3] != null ? row[3].toString() : null); //MTPR_LSV_PARAMETRO_REPORTE
	        	memoriaTecnica.setIdFiltroParametro(row[4] != null ? row[4].toString() : null); //MTPR_ID_FILTRO_PARAMETRO
	        	memoriaTecnica.setActivo(row[5] != null ? row[5].toString() : null); //MTPR_ACTIVO
	            return memoriaTecnica;
	        })
	        .collect(Collectors.toList());
	    
	}
	
	@Modifying
	@Query(value = "EXEC [spDelMemoriaTecnicaParametrosReportes] :IdTipoReporte, :Usuario", nativeQuery = true)
	void executeSpDelMemoriaTecnicaParametrosReportes(@Param("IdTipoReporte") int idTipoReporte, @Param("Usuario") String usuario);

	default void eliminarMemoriaTecnicaParametrosReportes(int idTipoReporte, String usuario) {
		try {
			log4j.info("Ejecución de spDelMemoriaTecnicaParametrosReportes con parámetros: " + idTipoReporte + " - " + usuario );
			executeSpDelMemoriaTecnicaParametrosReportes(idTipoReporte, usuario);			
			log4j.info("Se eliminó memoria técnica del id reporte " + idTipoReporte + " correctamente por " + usuario);
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spDelMemoriaTecnicaParametrosReportes] :IdTipoReporte, :Usuario con los valores respectivos: " + idTipoReporte + " - " +  usuario);
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al eliminar memoria técnica del id reporte" + idTipoReporte + " correctamente por " + usuario,ex);
		}
	}
	
	@Modifying
	@Query(value = "EXEC [spInsMemoriaTecnicaParametrosReportes] :IdTipoReporte, :Usuario, :IdParametroReporte, :IdFiltroParametro, :Activo", nativeQuery = true)
	void executeSpInsMemoriaTecnicaParametrosReportes(@Param("IdTipoReporte") int idTipoReporte, @Param("Usuario") String usuario, @Param("IdParametroReporte") int idParametroReporte,
													  @Param("IdFiltroParametro") int idFiltroParametro, @Param("Activo") int activo);

	default void guardarMemoriaTecnicaParametrosReportes(int idTipoReporte, String usuario, int idParametroReporte, int idFiltroParametro, int activo) {
		try {
			log4j.info("Ejecución de spInsMemoriaTecnicaParametrosReportes con parámetros: " + idTipoReporte + " correctamente por " + usuario + " con idParametroReporte: " + idParametroReporte + ", idFiltroParametro: " + idFiltroParametro
					+ ", Activo: " + activo);
			executeSpInsMemoriaTecnicaParametrosReportes(idTipoReporte, usuario, idParametroReporte, idFiltroParametro, activo);
			log4j.info("Se insertó memoria técnica del id reporte " + idTipoReporte + " correctamente por " + usuario + " con idParametroReporte: " + idParametroReporte + ", idFiltroParametro: " + idFiltroParametro
					+ ", Activo: " + activo);
		}catch(Exception ex) {
			log4j.error(ex.toString() + "  - Error en la ejecución del SP [spDelMemoriaTecnicaParametrosReportes] :IdTipoReporte, :Usuario con los valores respectivos: " + idTipoReporte + " - " +  usuario 
					+ " con idParametroReporte: " + idParametroReporte + ", idFiltroParametro: " + idFiltroParametro + ", Activo: " + activo);
			ex.printStackTrace();		
			throw new RuntimeException("Hubo un error al eliminar memoria técnica del id reporte" + idTipoReporte + " correctamente por " + usuario + " con idParametroReporte: " 
			+ idParametroReporte + ", idFiltroParametro: " + idFiltroParametro + ", Activo: " + activo,ex);
		}
	}
	

}
