package coralpagos.com.mx.facil.app.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import coralpagos.com.mx.facil.app.dto.Cartera;
import coralpagos.com.mx.facil.app.dto.ClasificacionMembresia;
import coralpagos.com.mx.facil.app.dto.Desarrollos;
import coralpagos.com.mx.facil.app.dto.Empleados;
import coralpagos.com.mx.facil.app.dto.Estados;
import coralpagos.com.mx.facil.app.dto.EstatusRecibos;
import coralpagos.com.mx.facil.app.dto.EstatusUnidades;
import coralpagos.com.mx.facil.app.dto.Locaciones;
import coralpagos.com.mx.facil.app.dto.MemoriaTecnicaParamReportes;
import coralpagos.com.mx.facil.app.dto.ParametrosReportes;
import coralpagos.com.mx.facil.app.dto.ProcedimientosReportes;
import coralpagos.com.mx.facil.app.dto.Promotores;
import coralpagos.com.mx.facil.app.dto.ReportesEspeciales;
import coralpagos.com.mx.facil.app.dto.SeriesRecibos;
import coralpagos.com.mx.facil.app.dto.TipoCuponesDescuentoPaq;
import coralpagos.com.mx.facil.app.dto.TiposMovimientos;
import coralpagos.com.mx.facil.app.dto.TiposProductos;
import coralpagos.com.mx.facil.app.dto.Usuarios;

public interface ReportesEspecialesService {
	
	String repIntegralCobranza(Model model, Principal principal, HttpSession session);
	
	List<ReportesEspeciales> getReportsType(int lsvClave, int idRol);

	Map<String, Object> obtieneParametrosReportes(int idTipoReporte);
	
	List<Cartera> obtenerParametroCartera();
	
	List<ClasificacionMembresia> obtenerParametroClasificacionMembresia();
	
	List<Desarrollos> obtenerParametroDesarrollo();
	
	List<Empleados> obtenerParametroEmpleados();
	
	List<Estados> obtenerParametroEstados();
	
	List<EstatusRecibos> obtenerParametroEstatusRecibos();	
	
	List<EstatusUnidades> obtenerParametroEstatusUnidades();	
	
	List<Locaciones> obtenerParametroLocaciones();
	
	List<Promotores> obtenerParametroPromotores();
	
	List<SeriesRecibos> obtenerParametroSeriesRecibos();
	
	List<TipoCuponesDescuentoPaq> obtenerParametroTipoCuponesDescuentoPaq();
	
	List<TiposMovimientos> obtenerParametroTiposMovimiento();
	
	List<TiposProductos> obtenerParametroTiposProductos();
	
	List<Usuarios> obtenerParametroUsuarios();
	
	List<ProcedimientosReportes> obtieneProcedimientoReporte(int idTipoReporte);
	
	List<MemoriaTecnicaParamReportes> obtieneMemoriaTecnicaParamReportes(int idTipoReporte, String usuario);
	
	byte[] buscarReporte_BKP(int idTipoReporte, String fechaInicio, String fechaFin, List<Integer> desarrollosList, List<Integer> tiposMovList);
	
	byte[] buscarReporte(int idTipoReporte, String fechaInicio, String fechaFin, String nombreReporte, String parametrosReporte,String usuarioGenerador);
	
	void eliminarMemoriaTecnicaParametrosReportes(int idTipoReporte, String usuarioGenerador);
	
	void guardarMemoriaTecnicaParametrosReportes(int idTipoReporte, String parametrosReporte, String usuarioGenerador);
	
	Map<String, Object> obtenercolumnasProcedimientoReporte(int idTipoReporte, String fechaInicio, String fechaFin, String nombreReporte, String parametrosReporte, String usuarioGenerador);
	
}
