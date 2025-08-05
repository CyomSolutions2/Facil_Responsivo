package coralpagos.com.mx.facil.app.service.impl;

import java.io.IOException;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import coralpagos.com.mx.facil.app.dto.BuscaUsuarios;
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
import coralpagos.com.mx.facil.app.dto.ModulosUrl;
import coralpagos.com.mx.facil.app.dto.ParametrosActivosReporte;
import coralpagos.com.mx.facil.app.dto.ParametrosReportes;
import coralpagos.com.mx.facil.app.dto.ProcedimientosReportes;
import coralpagos.com.mx.facil.app.dto.Promotores;
import coralpagos.com.mx.facil.app.dto.ReportesEspeciales;
import coralpagos.com.mx.facil.app.dto.Roles;
import coralpagos.com.mx.facil.app.dto.SeriesRecibos;
import coralpagos.com.mx.facil.app.dto.TipoCuponesDescuentoPaq;
import coralpagos.com.mx.facil.app.dto.TiposMovimientos;
import coralpagos.com.mx.facil.app.dto.TiposProductos;
import coralpagos.com.mx.facil.app.dto.Usuarios;
import coralpagos.com.mx.facil.app.dto.UsuariosRoles;
import coralpagos.com.mx.facil.app.repository.CodigoSeguridadRepository;
import coralpagos.com.mx.facil.app.repository.DatosAuditoriaRepository;
import coralpagos.com.mx.facil.app.repository.EjecutarReportesEspecialesRepository;
import coralpagos.com.mx.facil.app.repository.EmpleadoRepository;
import coralpagos.com.mx.facil.app.repository.ListaValoresRepository;
import coralpagos.com.mx.facil.app.repository.ParametroSistemaRepository;
import coralpagos.com.mx.facil.app.repository.PlantillaCorreoRepository;
import coralpagos.com.mx.facil.app.repository.ReportesEspecialesRepository;
import coralpagos.com.mx.facil.app.repository.RolesRepository;
import coralpagos.com.mx.facil.app.repository.SistemaRolRepository;
import coralpagos.com.mx.facil.app.repository.SistemaUsuarioRepository;
import coralpagos.com.mx.facil.app.repository.UserSystemRepository;
import coralpagos.com.mx.facil.app.repository.UserSystemSistemaRolRepository;
import coralpagos.com.mx.facil.app.repository.UsuarioRepository;
import coralpagos.com.mx.facil.app.service.ExcelService;
import coralpagos.com.mx.facil.app.service.ReportesEspecialesService;
import coralpagos.com.mx.facil.app.util.AppConstanst;
import coralpagos.com.mx.facil.app.util.Utils;
import coralpagos.com.mx.orm.facil.CodigoSeguridad;
import coralpagos.com.mx.orm.facil.Empleado;
import coralpagos.com.mx.orm.facil.ListaValores;
import coralpagos.com.mx.orm.facil.ParametroSistema;
import coralpagos.com.mx.orm.facil.PlantillaCorreo;
import coralpagos.com.mx.orm.facil.SistemaRol;
import coralpagos.com.mx.orm.facil.SistemaUsuario;
import coralpagos.com.mx.orm.facil.UserSystem;
import coralpagos.com.mx.orm.facil.UserSystemSistemaRol;
import coralpagos.com.mx.orm.facil.Usuario;
import coralpagos.com.mx.util.facil.dto.BusquedaDTO;
import coralpagos.com.mx.util.facil.dto.CredencialesDTO;
import coralpagos.com.mx.util.facil.dto.EnviarCodigoDTO;
import coralpagos.com.mx.util.facil.dto.SocioDTO;

@Service
@Transactional
public class ReportesEspecialesServiceImpl implements ReportesEspecialesService {
	
	@Autowired
	private UserSystemRepository userSystemRepository;
	
	@Autowired
	private SistemaUsuarioRepository sistemaUsuarioRepository;
	
	@Autowired
	private PlantillaCorreoRepository plantillaCorreoRepository;
	
	@Autowired
	private ParametroSistemaRepository parametroSistemaRepository;
	
	@Autowired
	private CodigoSeguridadRepository codigoSeguridadRepository;
	
	@Autowired
	private UserSystemSistemaRolRepository userSystemSistemaRolRepository;
	
	@Autowired
	private SistemaRolRepository sistemaRolRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ListaValoresRepository listaValoresRepository;
	
	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private DatosAuditoriaRepository datosAuditoriaRepository;
	
	@Autowired
	private ReportesEspecialesRepository reportesEspecialesRepository;
	
	@Autowired
	private EjecutarReportesEspecialesRepository ejecutarReportesEspecialesRepository;
	
	@Autowired
	private ExcelService excelService;
	
	@Value("${spring.mail.username}")
	private String username;
	
    private JavaMailSender mailSender;
    
    private Logger log4j = LogManager.getLogger(ReportesEspecialesServiceImpl.class);
	
	public ReportesEspecialesServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
		
	@Override
	public String repIntegralCobranza(Model model, Principal principal, HttpSession session) {
		model.addAttribute("roles", new coralpagos.com.mx.facil.app.dto.Roles());
		return "/repEspeciales/reportes";
	}
	
	@Override
	public List<ReportesEspeciales> getReportsType(int lsvClave, int idRol) {
		return reportesEspecialesRepository.getReportsType(lsvClave,idRol);
	}
	
	public Map<String, Object>  obtieneParametrosReportes(int idTipoReporte) {
		List<ParametrosReportes> parametrosReportes = reportesEspecialesRepository.obtieneParametrosReportes(idTipoReporte);
		ParametrosActivosReporte parametrosActivosReporte = new ParametrosActivosReporte();
		 Map<String, Object> parametros = new HashMap<>();
		for(ParametrosReportes param : parametrosReportes) {
			
			switch(param.getLsvParametroReporte()) {
			 case "914":
				 parametrosActivosReporte.setDesarrollos(true);
				 parametros.put("requiereDesarrollos", true);
				 parametros.put("paramIDDesarrollos", param.getLsvParametroReporte());
				 break;
			 case "915":
				 parametrosActivosReporte.setClasificacionMembresia(true);
				 parametros.put("requiereClasificacionMembresia", true);
				 parametros.put("paramIDClasificacionMembresia", param.getLsvParametroReporte());
				 break;
			 case "916":
				 parametrosActivosReporte.setTiposProductos(true);
				 parametros.put("requiereTiposProductos", true);
				 parametros.put("paramIDTiposProductos", param.getLsvParametroReporte());
				 break;
			 case "917":
				 parametrosActivosReporte.setCarteras(true);
				 parametros.put("requiereCarteras", true);
				 parametros.put("paramIDCarteras", param.getLsvParametroReporte());
				 break;
			 case "918":
				 parametrosActivosReporte.setTiposMovimientos(true);
				 parametros.put("requiereTiposMovimientos", true);
				 parametros.put("paramIDTiposMovimientos", param.getLsvParametroReporte());
				 break;
			 case "919":
				 parametrosActivosReporte.setSeriesRecibos(true);
				 parametros.put("requiereSeriesRecibos", true);
				 parametros.put("paramIDSeriesRecibos", param.getLsvParametroReporte());
				 break;
			 case "991":
				 parametrosActivosReporte.setUsuarios(true);
				 parametros.put("requiereUsuarios", true);
				 parametros.put("paramIDUsuarios", param.getLsvParametroReporte());
				 break;
			 case "1169":
				 parametrosActivosReporte.setEmpleados(true);
				 parametros.put("requiereEmpleados", true);
				 parametros.put("paramIDEmpleados", param.getLsvParametroReporte());
				 break;
			 case "1178":
				 parametrosActivosReporte.setEstados(true);
				 parametros.put("requiereEstados", true);
				 parametros.put("paramIDEstados", param.getLsvParametroReporte());
				 break;
			 case "1280":
				 parametrosActivosReporte.setLocaciones(true);
				 parametros.put("requiereLocaciones", true);
				 parametros.put("paramIDLocaciones", param.getLsvParametroReporte());
				 break;
			 case "1303":
				 parametrosActivosReporte.setTiposCuponesDescuentoPqa(true);
				 parametros.put("requiereTiposCuponesDescuentoPqa", true);
				 parametros.put("paramIDTiposCuponesDescuentoPqa", param.getLsvParametroReporte());
				 break;
			 case "1528":
				 parametrosActivosReporte.setEstatusUnidades(true);
				 parametros.put("requiereEstatusUnidades", true);
				 parametros.put("paramIDEstatusUnidades", param.getLsvParametroReporte());
				 break;
			 case "3349":
				 parametrosActivosReporte.setPromotores(true);
				 parametros.put("requierePromotores", true);
				 parametros.put("paramIDPromotores", param.getLsvParametroReporte());
				 break;
			 case "3356":
				 parametrosActivosReporte.setEstatusRecibos(true);
				 parametros.put("requiereEstatusRecibos", true);
				 parametros.put("paramIDEstatusRecibos", param.getLsvParametroReporte());
				 break;
			}
			 
		}
		
		
		return parametros;
	}
		
	@Override
	public List<Cartera> obtenerParametroCartera() {
		return reportesEspecialesRepository.obtenerParametroCartera();
	}
	
	@Override
	public List<ClasificacionMembresia> obtenerParametroClasificacionMembresia() {
		return reportesEspecialesRepository.obtenerParametroClasificacionMembresia();
	}
	
	@Override
	public List<Desarrollos> obtenerParametroDesarrollo() {
		return reportesEspecialesRepository.obtenerParametroDesarrollo();
	}
	
	@Override
	public List<Empleados> obtenerParametroEmpleados() {
		return reportesEspecialesRepository.obtenerParametroEmpleados();
	}
	
	@Override
	public List<Estados> obtenerParametroEstados() {
		return reportesEspecialesRepository.obtenerParametroEstados();
	}
	
	@Override
	public List<EstatusRecibos> obtenerParametroEstatusRecibos() {
		return reportesEspecialesRepository.obtenerParametroEstatusRecibos();
	}
	
	@Override
	public List<EstatusUnidades> obtenerParametroEstatusUnidades() {
		return reportesEspecialesRepository.obtenerParametroEstatusUnidades();
	}
	
	@Override
	public List<Locaciones> obtenerParametroLocaciones() {
		return reportesEspecialesRepository.obtenerParametroLocaciones();
	}
	
	@Override
	public List<Promotores> obtenerParametroPromotores() {
		return reportesEspecialesRepository.obtenerParametroPromotores();
	}
	
	@Override
	public List<SeriesRecibos> obtenerParametroSeriesRecibos()  {
		return reportesEspecialesRepository.obtenerParametroSeriesRecibos();
	}
	
	@Override
	public List<TipoCuponesDescuentoPaq> obtenerParametroTipoCuponesDescuentoPaq()  {
		return reportesEspecialesRepository.obtenerParametroTipoCuponesDescuentoPaq();
	}
	
	@Override
	public List<TiposMovimientos> obtenerParametroTiposMovimiento() {
		return reportesEspecialesRepository.obtenerParametroTiposMovimiento();
	}
	
	@Override
	public List<TiposProductos> obtenerParametroTiposProductos() {
		return reportesEspecialesRepository.obtenerParametroTiposProductos();
	}
	
	@Override
	public List<Usuarios> obtenerParametroUsuarios() {
		return reportesEspecialesRepository.obtenerParametroUsuarios();
	}
	
	@Override
	public List<ProcedimientosReportes> obtieneProcedimientoReporte(int idTipoReporte) {
		return reportesEspecialesRepository.obtieneProcedimientoReporte(idTipoReporte);
	}
	
	@Override
	public List<MemoriaTecnicaParamReportes> obtieneMemoriaTecnicaParamReportes(int idTipoReporte, String usuario) {
		return reportesEspecialesRepository.obtieneMemoriaTecnicaParamReportes(idTipoReporte, usuario);
	}
	
	@Override
	public byte[] buscarReporte_BKP(int idTipoReporte, String fechaInicio, String fechaFin, List<Integer> desarrollosList, List<Integer> tiposMovList) {
		
		
		List<ProcedimientosReportes> procedimientoReporteList = reportesEspecialesRepository.obtieneProcedimientoReporte(idTipoReporte);
		ProcedimientosReportes procedimientosReporte = new ProcedimientosReportes();
		if(procedimientoReporteList.isEmpty()) {
			 throw new NoSuchElementException("No se encontró el procedimiento para el tipo de reporte con ID: " + idTipoReporte);
		}else {
			procedimientosReporte = procedimientoReporteList.get(0);
		}
		
		 String desarrollosListStr = desarrollosList.stream().map(String::valueOf).collect(Collectors.joining(", "));
		 String tiposMovListStr = tiposMovList.stream().map(String::valueOf).collect(Collectors.joining(", "));
		
		/*  Map<String, Object> params = new LinkedHashMap<>();
params.put("FechaInicialPago", Timestamp.valueOf("2025-01-01 00:00:00"));
params.put("FechaFinalPago", Timestamp.valueOf("2025-01-31 23:59:59"));
params.put("Desarrollo", "1");
params.put("Serie", "939,1450,2126,935,936,937,938,941,1018,1974,2120,2121,2122,2123,2125,2127,2128,2129,870,939,1124,1125,1950,1951,1952,1953,1954,1955,1956,1957,1958,1965,1966,1967,1968,1969,1970,1971,1972,1973,1289,1291,1292,1293");
params.put("TipoMovimiento", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,365,366,367,368,369,370,371,372,380,381,382,383,384,385,386,387,388,389,390,391,392,397,398,399,400,401,403,404,405,406,407,408,409,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,430,431,432,433,435,436,439,440,442,443,444,450,451,460,468,499,500,501,502,503,504,505,506,507,508,509,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,544,545,546,547,548,549,550,551,552,553,554,555,556,557,558,559,560,561,562,563,564,565,566,567,568,569,570,571,572,573,574,575,576,577,578,579,580,581,582,583,584,585,586,587,588,589,590,591,592,593,594,595,596,597,598,599,600,601,602,603,606,607,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,630,665,666,699,700,702,710,720,721,722,723,724,725,726,727,728,729,730,731,732,750,760,800,801,802,803,804,805,806,807,808,810,811,812,813,814,815,816,817,818,819,850,851,852,1000,1001,1002,1003,1004,1005,1006,1007,1010,1015,1016,1017,1018,1019,1020,1021,1022,1023,1024,1025,1026,1027,1028,1030,1031,1032,1033,1034,1035,1036,1037,1038,1039,1040,1041,1042,1043,1044,1900,1901,1910,1920,1921,1922,2000,3000,3001,3002,3003,4000,4001,4002,4003,4004,4005,4006,4007,4008,4009,4010,4011,4012,4013,4014,4015,4016,4017,4020,4021,4022,4025,4026,4027,4050,4051,4052,4053,4054,4055,4056,4057,4058,4059,4060,4061,4062,4063,4064,4065,4066,4067,4068,4069,4070,4071,4072,4073,4074,4075,4076,4077,4078,4079,4080,4081,4082,4084,4085,4086,4087,4088,4089,4100,4101,4102,4500,4501,5000,5001,5002,5003,5004,5005,5006,5007,5008,5100,5101,5102,5103,5104,5105,5106,5107,5108,5109,5110");
params.put("Usuario", "222222222,304141,201382,305213,4080991,839,20839,201435,1153,4080823,10839,405000,405153,80244,99999989,4080321,4080944,4080757,1232,40999001,10999001,10999001,4080368,201450,4080603,4081017,101331,101426,30661749,40999002,101400,10999002,10252,20656,9920656,1066138,10973,40999006,10999006,4080726,10605,101351,40999003,10999003,80966,101301,80140,444444444,101443,1483,10527,81050,4080774,101430,1452,10022,80948,1439,101317,4080516,105212,1492,66389,1448,1458,10992022,1293,101305,306650,101399,40999005,10999005,1204,4080902,105013,1446,1445,101425,101324,101440,101433,304057,10139,4080913,4080821,201414,99201414,101391,987654321,88888888,80925,10005,201474,201314,992,101312,5201,306640,81041,80013,4080013,201464,5061,4081038,101427,40999004,10999004,306664,40999666,201183,3066100,4081024,60002,69993,66391");
params.put("Usuario_Generador", "RMANILLA");*/
		
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("FechaInicialPago", fechaInicio);
		params.put("FechaFinalPago", fechaFin);
		params.put("Desarrollo", desarrollosListStr);
		params.put("Serie", "939,1450,2126,935,936,937,938,941,1018,1974,2120,2121,2122,2123,2125,2127,2128,2129,870,939,1124,1125,1950,1951,1952,1953,1954,1955,1956,1957,1958,1965,1966,1967,1968,1969,1970,1971,1972,1973,1289,1291,1292,1293");
		//params.put("TipoMovimiento", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,365,366,367,368,369,370,371,372,380,381,382,383,384,385,386,387,388,389,390,391,392,397,398,399,400,401,403,404,405,406,407,408,409,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,430,431,432,433,435,436,439,440,442,443,444,450,451,460,468,499,500,501,502,503,504,505,506,507,508,509,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,544,545,546,547,548,549,550,551,552,553,554,555,556,557,558,559,560,561,562,563,564,565,566,567,568,569,570,571,572,573,574,575,576,577,578,579,580,581,582,583,584,585,586,587,588,589,590,591,592,593,594,595,596,597,598,599,600,601,602,603,606,607,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,630,665,666,699,700,702,710,720,721,722,723,724,725,726,727,728,729,730,731,732,750,760,800,801,802,803,804,805,806,807,808,810,811,812,813,814,815,816,817,818,819,850,851,852,1000,1001,1002,1003,1004,1005,1006,1007,1010,1015,1016,1017,1018,1019,1020,1021,1022,1023,1024,1025,1026,1027,1028,1030,1031,1032,1033,1034,1035,1036,1037,1038,1039,1040,1041,1042,1043,1044,1900,1901,1910,1920,1921,1922,2000,3000,3001,3002,3003,4000,4001,4002,4003,4004,4005,4006,4007,4008,4009,4010,4011,4012,4013,4014,4015,4016,4017,4020,4021,4022,4025,4026,4027,4050,4051,4052,4053,4054,4055,4056,4057,4058,4059,4060,4061,4062,4063,4064,4065,4066,4067,4068,4069,4070,4071,4072,4073,4074,4075,4076,4077,4078,4079,4080,4081,4082,4084,4085,4086,4087,4088,4089,4100,4101,4102,4500,4501,5000,5001,5002,5003,5004,5005,5006,5007,5008,5100,5101,5102,5103,5104,5105,5106,5107,5108,5109,5110");
		params.put("TipoMovimiento", tiposMovListStr);
		params.put("Usuario", "222222222,304141,201382,305213,4080991,839,20839,201435,1153,4080823,10839,405000,405153,80244,99999989,4080321,4080944,4080757,1232,40999001,10999001,10999001,4080368,201450,4080603,4081017,101331,101426,30661749,40999002,101400,10999002,10252,20656,9920656,1066138,10973,40999006,10999006,4080726,10605,101351,40999003,10999003,80966,101301,80140,444444444,101443,1483,10527,81050,4080774,101430,1452,10022,80948,1439,101317,4080516,105212,1492,66389,1448,1458,10992022,1293,101305,306650,101399,40999005,10999005,1204,4080902,105013,1446,1445,101425,101324,101440,101433,304057,10139,4080913,4080821,201414,99201414,101391,987654321,88888888,80925,10005,201474,201314,992,101312,5201,306640,81041,80013,4080013,201464,5061,4081038,101427,40999004,10999004,306664,40999666,201183,3066100,4081024,60002,69993,66391");
		params.put("Usuario_Generador", "RMANILLA");


		
	
		
		//Map<String, Object> valoresReporte = ejecutarReportesEspecialesRepository.ejecutarStoredProcedure(procedimientosReporte.getProcedimiento());
		Map<String, Object> valoresReporte = ejecutarReportesEspecialesRepository.ejecutarStoredProcedureConParametros(procedimientosReporte.getProcedimiento(),params);
		byte[] fileXls = null;
		try {
			fileXls = excelService.generarReporteExcel(valoresReporte,"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileXls;

	}
	
	public byte[] buscarReporte(int idTipoReporte, String fechaInicio, String fechaFin, String nombreReporte, String parametrosReporte, String usuarioGenerador) {
			
		List<ProcedimientosReportes> procedimientoReporteList = reportesEspecialesRepository.obtieneProcedimientoReporte(idTipoReporte);
		ProcedimientosReportes procedimientosReporte = new ProcedimientosReportes();
		JSONObject jsonObject = null;
		int numParametros = 0;
		if(procedimientoReporteList.isEmpty()) {
			 throw new NoSuchElementException("No se encontró el procedimiento para el tipo de reporte con ID: " + idTipoReporte);
		}else {
			procedimientosReporte = procedimientoReporteList.get(0);
		}
		
		if(procedimientosReporte != null && !procedimientosReporte.getNumeroParametros().isEmpty()) {
		 numParametros = Integer.valueOf(procedimientosReporte.getNumeroParametros()).intValue();
		}
		
		 List<ParametrosReportes> parametrosReportes = reportesEspecialesRepository.obtieneParametrosReportes(idTipoReporte);
		 
		 if(!parametrosReporte.isEmpty()) {
			 jsonObject = new JSONObject(parametrosReporte);
		 }
			 
			
		 
		// String desarrollosListStr = desarrollosList.stream().map(String::valueOf).collect(Collectors.joining(", "));
	//	 String tiposMovListStr = tiposMovList.stream().map(String::valueOf).collect(Collectors.joining(", "));
		
		/*  Map<String, Object> params = new LinkedHashMap<>();
params.put("FechaInicialPago", Timestamp.valueOf("2025-01-01 00:00:00"));
params.put("FechaFinalPago", Timestamp.valueOf("2025-01-31 23:59:59"));
params.put("Desarrollo", "1");
params.put("Serie", "939,1450,2126,935,936,937,938,941,1018,1974,2120,2121,2122,2123,2125,2127,2128,2129,870,939,1124,1125,1950,1951,1952,1953,1954,1955,1956,1957,1958,1965,1966,1967,1968,1969,1970,1971,1972,1973,1289,1291,1292,1293");
params.put("TipoMovimiento", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,365,366,367,368,369,370,371,372,380,381,382,383,384,385,386,387,388,389,390,391,392,397,398,399,400,401,403,404,405,406,407,408,409,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,430,431,432,433,435,436,439,440,442,443,444,450,451,460,468,499,500,501,502,503,504,505,506,507,508,509,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,544,545,546,547,548,549,550,551,552,553,554,555,556,557,558,559,560,561,562,563,564,565,566,567,568,569,570,571,572,573,574,575,576,577,578,579,580,581,582,583,584,585,586,587,588,589,590,591,592,593,594,595,596,597,598,599,600,601,602,603,606,607,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,630,665,666,699,700,702,710,720,721,722,723,724,725,726,727,728,729,730,731,732,750,760,800,801,802,803,804,805,806,807,808,810,811,812,813,814,815,816,817,818,819,850,851,852,1000,1001,1002,1003,1004,1005,1006,1007,1010,1015,1016,1017,1018,1019,1020,1021,1022,1023,1024,1025,1026,1027,1028,1030,1031,1032,1033,1034,1035,1036,1037,1038,1039,1040,1041,1042,1043,1044,1900,1901,1910,1920,1921,1922,2000,3000,3001,3002,3003,4000,4001,4002,4003,4004,4005,4006,4007,4008,4009,4010,4011,4012,4013,4014,4015,4016,4017,4020,4021,4022,4025,4026,4027,4050,4051,4052,4053,4054,4055,4056,4057,4058,4059,4060,4061,4062,4063,4064,4065,4066,4067,4068,4069,4070,4071,4072,4073,4074,4075,4076,4077,4078,4079,4080,4081,4082,4084,4085,4086,4087,4088,4089,4100,4101,4102,4500,4501,5000,5001,5002,5003,5004,5005,5006,5007,5008,5100,5101,5102,5103,5104,5105,5106,5107,5108,5109,5110");
params.put("Usuario", "222222222,304141,201382,305213,4080991,839,20839,201435,1153,4080823,10839,405000,405153,80244,99999989,4080321,4080944,4080757,1232,40999001,10999001,10999001,4080368,201450,4080603,4081017,101331,101426,30661749,40999002,101400,10999002,10252,20656,9920656,1066138,10973,40999006,10999006,4080726,10605,101351,40999003,10999003,80966,101301,80140,444444444,101443,1483,10527,81050,4080774,101430,1452,10022,80948,1439,101317,4080516,105212,1492,66389,1448,1458,10992022,1293,101305,306650,101399,40999005,10999005,1204,4080902,105013,1446,1445,101425,101324,101440,101433,304057,10139,4080913,4080821,201414,99201414,101391,987654321,88888888,80925,10005,201474,201314,992,101312,5201,306640,81041,80013,4080013,201464,5061,4081038,101427,40999004,10999004,306664,40999666,201183,3066100,4081024,60002,69993,66391");
params.put("Usuario_Generador", "RMANILLA");*/
		
		
		
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("FechaInicio", fechaInicio);
		params.put("FechaFinal", fechaFin);
		
		for(int x=0; x<numParametros; x++) {
			
			if(x<(numParametros-1)) {
				params.put(parametrosReportes.get(x).getParametroReporte(), Utils.getValoresByDataOrder(jsonObject, Integer.valueOf(parametrosReportes.get(x).getLsvParametroReporte()).intValue()));
			}
			
			if( x == (numParametros-1)) {
				params.put("UsuarioGenerador", usuarioGenerador);
			}
			
		}
		
	/*	params.put("Desarrollo", "1");
		params.put("Serie", "939,1450,2126,935,936,937,938,941,1018,1974,2120,2121,2122,2123,2125,2127,2128,2129,870,939,1124,1125,1950,1951,1952,1953,1954,1955,1956,1957,1958,1965,1966,1967,1968,1969,1970,1971,1972,1973,1289,1291,1292,1293");
		//params.put("TipoMovimiento", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,365,366,367,368,369,370,371,372,380,381,382,383,384,385,386,387,388,389,390,391,392,397,398,399,400,401,403,404,405,406,407,408,409,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,430,431,432,433,435,436,439,440,442,443,444,450,451,460,468,499,500,501,502,503,504,505,506,507,508,509,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,544,545,546,547,548,549,550,551,552,553,554,555,556,557,558,559,560,561,562,563,564,565,566,567,568,569,570,571,572,573,574,575,576,577,578,579,580,581,582,583,584,585,586,587,588,589,590,591,592,593,594,595,596,597,598,599,600,601,602,603,606,607,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,630,665,666,699,700,702,710,720,721,722,723,724,725,726,727,728,729,730,731,732,750,760,800,801,802,803,804,805,806,807,808,810,811,812,813,814,815,816,817,818,819,850,851,852,1000,1001,1002,1003,1004,1005,1006,1007,1010,1015,1016,1017,1018,1019,1020,1021,1022,1023,1024,1025,1026,1027,1028,1030,1031,1032,1033,1034,1035,1036,1037,1038,1039,1040,1041,1042,1043,1044,1900,1901,1910,1920,1921,1922,2000,3000,3001,3002,3003,4000,4001,4002,4003,4004,4005,4006,4007,4008,4009,4010,4011,4012,4013,4014,4015,4016,4017,4020,4021,4022,4025,4026,4027,4050,4051,4052,4053,4054,4055,4056,4057,4058,4059,4060,4061,4062,4063,4064,4065,4066,4067,4068,4069,4070,4071,4072,4073,4074,4075,4076,4077,4078,4079,4080,4081,4082,4084,4085,4086,4087,4088,4089,4100,4101,4102,4500,4501,5000,5001,5002,5003,5004,5005,5006,5007,5008,5100,5101,5102,5103,5104,5105,5106,5107,5108,5109,5110");
		params.put("TipoMovimiento", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,365,366,367,368,369,370,371,372,380,381,382,383,384,385,386,387,388,389,390,391,392,397,398,399,400,401,403,404,405,406,407,408,409,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,430,431,432,433,435,436,439,440,442,443,444,450,451,460,468,499,500,501,502,503,504,505,506,507,508,509,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,544,545,546,547,548,549,550,551,552,553,554,555,556,557,558,559,560,561,562,563,564,565,566,567,568,569,570,571,572,573,574,575,576,577,578,579,580,581,582,583,584,585,586,587,588,589,590,591,592,593,594,595,596,597,598,599,600,601,602,603,606,607,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,630,665,666,699,700,702,710,720,721,722,723,724,725,726,727,728,729,730,731,732,750,760,800,801,802,803,804,805,806,807,808,810,811,812,813,814,815,816,817,818,819,850,851,852,1000,1001,1002,1003,1004,1005,1006,1007,1010,1015,1016,1017,1018,1019,1020,1021,1022,1023,1024,1025,1026,1027,1028,1030,1031,1032,1033,1034,1035,1036,1037,1038,1039,1040,1041,1042,1043,1044,1900,1901,1910,1920,1921,1922,2000,3000,3001,3002,3003,4000,4001,4002,4003,4004,4005,4006,4007,4008,4009,4010,4011,4012,4013,4014,4015,4016,4017,4020,4021,4022,4025,4026,4027,4050,4051,4052,4053,4054,4055,4056,4057,4058,4059,4060,4061,4062,4063,4064,4065,4066,4067,4068,4069,4070,4071,4072,4073,4074,4075,4076,4077,4078,4079,4080,4081,4082,4084,4085,4086,4087,4088,4089,4100,4101,4102,4500,4501,5000,5001,5002,5003,5004,5005,5006,5007,5008,5100,5101,5102,5103,5104,5105,5106,5107,5108,5109,5110");
		params.put("Usuario", "222222222,304141,201382,305213,4080991,839,20839,201435,1153,4080823,10839,405000,405153,80244,99999989,4080321,4080944,4080757,1232,40999001,10999001,10999001,4080368,201450,4080603,4081017,101331,101426,30661749,40999002,101400,10999002,10252,20656,9920656,1066138,10973,40999006,10999006,4080726,10605,101351,40999003,10999003,80966,101301,80140,444444444,101443,1483,10527,81050,4080774,101430,1452,10022,80948,1439,101317,4080516,105212,1492,66389,1448,1458,10992022,1293,101305,306650,101399,40999005,10999005,1204,4080902,105013,1446,1445,101425,101324,101440,101433,304057,10139,4080913,4080821,201414,99201414,101391,987654321,88888888,80925,10005,201474,201314,992,101312,5201,306640,81041,80013,4080013,201464,5061,4081038,101427,40999004,10999004,306664,40999666,201183,3066100,4081024,60002,69993,66391");
		params.put("Usuario_Generador", "RMANILLA");


		*/
	
		
		//Map<String, Object> valoresReporte = ejecutarReportesEspecialesRepository.ejecutarStoredProcedure(procedimientosReporte.getProcedimiento());
		Map<String, Object> valoresReporte = ejecutarReportesEspecialesRepository.ejecutarStoredProcedureConParametros(procedimientosReporte.getProcedimiento(),params);
		byte[] fileXls = null;
		try {
			fileXls = excelService.generarReporteExcel(valoresReporte, nombreReporte);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fileXls;

	}
	
	public Map<String, Object> obtenercolumnasProcedimientoReporte(int idTipoReporte, String fechaInicio, String fechaFin, String nombreReporte, String parametrosReporte, String usuarioGenerador) {
		
		List<ProcedimientosReportes> procedimientoReporteList = reportesEspecialesRepository.obtieneProcedimientoReporte(idTipoReporte);
		ProcedimientosReportes procedimientosReporte = new ProcedimientosReportes();
		JSONObject jsonObject = null;
		int numParametros = 0;
		if(procedimientoReporteList.isEmpty()) {
			 throw new NoSuchElementException("No se encontró el procedimiento para el tipo de reporte con ID: " + idTipoReporte);
		}else {
			procedimientosReporte = procedimientoReporteList.get(0);
		}
		
		if(procedimientosReporte != null && !procedimientosReporte.getNumeroParametros().isEmpty()) {
		 numParametros = Integer.valueOf(procedimientosReporte.getNumeroParametros()).intValue();
		}
		
		 List<ParametrosReportes> parametrosReportes = reportesEspecialesRepository.obtieneParametrosReportes(idTipoReporte);
		 
		 if(!parametrosReporte.isEmpty()) {
			 jsonObject = new JSONObject(parametrosReporte);
		 }
			 
			
		 
		// String desarrollosListStr = desarrollosList.stream().map(String::valueOf).collect(Collectors.joining(", "));
	//	 String tiposMovListStr = tiposMovList.stream().map(String::valueOf).collect(Collectors.joining(", "));
		
		/*  Map<String, Object> params = new LinkedHashMap<>();
params.put("FechaInicialPago", Timestamp.valueOf("2025-01-01 00:00:00"));
params.put("FechaFinalPago", Timestamp.valueOf("2025-01-31 23:59:59"));
params.put("Desarrollo", "1");
params.put("Serie", "939,1450,2126,935,936,937,938,941,1018,1974,2120,2121,2122,2123,2125,2127,2128,2129,870,939,1124,1125,1950,1951,1952,1953,1954,1955,1956,1957,1958,1965,1966,1967,1968,1969,1970,1971,1972,1973,1289,1291,1292,1293");
params.put("TipoMovimiento", "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255,256,257,258,259,260,261,262,263,264,265,266,267,268,269,270,271,272,273,274,275,276,277,278,279,280,281,282,283,284,285,286,287,288,289,290,291,292,293,294,295,296,297,298,299,300,301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322,323,324,325,326,327,328,329,330,331,332,333,334,335,336,337,338,339,340,341,342,343,344,345,347,348,349,350,351,352,353,354,355,356,357,358,359,360,361,362,363,365,366,367,368,369,370,371,372,380,381,382,383,384,385,386,387,388,389,390,391,392,397,398,399,400,401,403,404,405,406,407,408,409,410,411,412,413,414,415,416,417,418,419,420,421,422,423,424,425,426,427,428,429,430,431,432,433,435,436,439,440,442,443,444,450,451,460,468,499,500,501,502,503,504,505,506,507,508,509,510,511,512,513,514,515,516,517,518,519,520,521,522,523,524,525,526,527,528,529,530,531,532,533,534,535,536,537,538,539,540,541,542,543,544,545,546,547,548,549,550,551,552,553,554,555,556,557,558,559,560,561,562,563,564,565,566,567,568,569,570,571,572,573,574,575,576,577,578,579,580,581,582,583,584,585,586,587,588,589,590,591,592,593,594,595,596,597,598,599,600,601,602,603,606,607,610,611,612,613,614,615,616,617,618,619,620,621,622,623,624,625,626,627,628,629,630,665,666,699,700,702,710,720,721,722,723,724,725,726,727,728,729,730,731,732,750,760,800,801,802,803,804,805,806,807,808,810,811,812,813,814,815,816,817,818,819,850,851,852,1000,1001,1002,1003,1004,1005,1006,1007,1010,1015,1016,1017,1018,1019,1020,1021,1022,1023,1024,1025,1026,1027,1028,1030,1031,1032,1033,1034,1035,1036,1037,1038,1039,1040,1041,1042,1043,1044,1900,1901,1910,1920,1921,1922,2000,3000,3001,3002,3003,4000,4001,4002,4003,4004,4005,4006,4007,4008,4009,4010,4011,4012,4013,4014,4015,4016,4017,4020,4021,4022,4025,4026,4027,4050,4051,4052,4053,4054,4055,4056,4057,4058,4059,4060,4061,4062,4063,4064,4065,4066,4067,4068,4069,4070,4071,4072,4073,4074,4075,4076,4077,4078,4079,4080,4081,4082,4084,4085,4086,4087,4088,4089,4100,4101,4102,4500,4501,5000,5001,5002,5003,5004,5005,5006,5007,5008,5100,5101,5102,5103,5104,5105,5106,5107,5108,5109,5110");
params.put("Usuario", "222222222,304141,201382,305213,4080991,839,20839,201435,1153,4080823,10839,405000,405153,80244,99999989,4080321,4080944,4080757,1232,40999001,10999001,10999001,4080368,201450,4080603,4081017,101331,101426,30661749,40999002,101400,10999002,10252,20656,9920656,1066138,10973,40999006,10999006,4080726,10605,101351,40999003,10999003,80966,101301,80140,444444444,101443,1483,10527,81050,4080774,101430,1452,10022,80948,1439,101317,4080516,105212,1492,66389,1448,1458,10992022,1293,101305,306650,101399,40999005,10999005,1204,4080902,105013,1446,1445,101425,101324,101440,101433,304057,10139,4080913,4080821,201414,99201414,101391,987654321,88888888,80925,10005,201474,201314,992,101312,5201,306640,81041,80013,4080013,201464,5061,4081038,101427,40999004,10999004,306664,40999666,201183,3066100,4081024,60002,69993,66391");
params.put("Usuario_Generador", "RMANILLA");*/
		
		
		
		Map<String, Object> params = new LinkedHashMap<>();
		params.put("FechaInicio", fechaInicio);
		params.put("FechaFinal", fechaFin);
		
		for(int x=0; x<numParametros; x++) {
			
			if(x<(numParametros-1)) {
				params.put(parametrosReportes.get(x).getParametroReporte(), Utils.getValoresByDataOrder(jsonObject, Integer.valueOf(parametrosReportes.get(x).getLsvParametroReporte()).intValue()));
			}
			
			if( x == (numParametros-1)) {
				params.put("UsuarioGenerador", usuarioGenerador);
			}
			
		}
		
		Map<String, Object> valoresReporte = ejecutarReportesEspecialesRepository.ejecutarStoredProcedureConParametros(procedimientosReporte.getProcedimiento(),params);
		
		
		return valoresReporte;

	}
	
	public void eliminarMemoriaTecnicaParametrosReportes(int idTipoReporte, String usuarioGenerador) {
		reportesEspecialesRepository.eliminarMemoriaTecnicaParametrosReportes(idTipoReporte, usuarioGenerador);
	}
	
	public void guardarMemoriaTecnicaParametrosReportes(int idTipoReporte, String parametrosReporte, String usuarioGenerador) {
		
		//eliminarMemoriaTecnicaParametrosReportes(idTipoReporte, usuarioGenerador);
		JSONObject jsonObject =  new JSONObject(parametrosReporte);
		
		for(String key : jsonObject.keySet()) {
			JSONObject objPrincipal = jsonObject.getJSONObject(key);
			JSONArray seleccionados = objPrincipal.getJSONArray("seleccionados");
			boolean selectedAll = objPrincipal.getBoolean("selectedAll");
			String idParametroReporte = key;			
			for(int x = 0;x < seleccionados.length(); x++) {
				JSONObject item =seleccionados.getJSONObject(x);
				String idFiltroParametro = item.getString("id");			
				if(!selectedAll) {
				reportesEspecialesRepository.guardarMemoriaTecnicaParametrosReportes(idTipoReporte, usuarioGenerador, Integer.valueOf(idParametroReporte).intValue(), Integer.valueOf(idFiltroParametro).intValue(), 1);
				}
				
				//int idTipoReporte, String usuario, int idParametroReporte, int idFiltroParametro, int activo
			}
		}
		
	}
	
}
