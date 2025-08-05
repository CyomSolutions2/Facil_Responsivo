package coralpagos.com.mx.facil.app.controller;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import coralpagos.com.mx.facil.app.dto.Promotores;
import coralpagos.com.mx.facil.app.dto.ReportesEspeciales;
import coralpagos.com.mx.facil.app.dto.Roles;
import coralpagos.com.mx.facil.app.dto.SeriesRecibos;
import coralpagos.com.mx.facil.app.dto.TipoCuponesDescuentoPaq;
import coralpagos.com.mx.facil.app.dto.TiposMovimientos;
import coralpagos.com.mx.facil.app.dto.TiposProductos;
import coralpagos.com.mx.facil.app.dto.Usuarios;
import coralpagos.com.mx.facil.app.dto.UsuariosRoles;
import coralpagos.com.mx.facil.app.repository.UsuarioRepository;
import coralpagos.com.mx.facil.app.service.ExcelService;
import coralpagos.com.mx.facil.app.service.ReportesEspecialesService;
import coralpagos.com.mx.facil.app.service.UsuarioService;
import coralpagos.com.mx.facil.app.util.AppConstanst;
import coralpagos.com.mx.orm.facil.SistemaRol;
import coralpagos.com.mx.util.facil.dto.CredencialesDTO;
import coralpagos.com.mx.util.facil.dto.SocioDTO;

@Controller
public class ReportesEspecialesController {
	
	@Autowired
	private ReportesEspecialesService reportesEspecialesService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private ExcelService excelService;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	private Logger log4j = LogManager.getLogger(ReportesEspecialesController.class);
	
	
	/*@GetMapping("/repEspeciales/cobranza/repIntegralCobranza_BKP")
	public String repIntegralCobranza_BKP( Model model, Principal principal, HttpSession session ) {
		try {
	    usuarioService.datosEmpleado(model, principal, session);
	    model.addAttribute("menuActivo", AppConstanst.MENU_ACTIVO_REP_INTEGRAL_COBRANZA);
	   // Object modulos = model.getAttribute("modulos");
	   List<ModulosRoles> modulosRoles = (List<ModulosRoles>) model.getAttribute("modulos");
	    
	    if(modulosRoles == null || modulosRoles.isEmpty()) {
	    	return "redirect:/authenticate/login";
	    }else {
	    
	    	 
	    	boolean isModuleValid =usuarioService.hasAccess(modulosRoles,AppConstanst.MENU_ACTIVO_REP_INTEGRAL_COBRANZA);
	    	 if(!isModuleValid) {
	    		 return "redirect:/error/error403";
	    	 }
	    	 
	    }
		} catch (AuthenticationCredentialsNotFoundException e) {
	        return "redirect:/authenticate/login";
	    } catch (DisabledException e) {
	        return "redirect:/authenticate/login";
	    }
	    
	    return reportesEspecialesService.repIntegralCobranza(model, principal, session);

	}*/
	
	@SuppressWarnings("unchecked")
	@GetMapping("/repEspeciales/reportes")
	public String repIntegralCobranza( Model model, Principal principal, HttpSession session ,
			@RequestParam(value = "menuId", required = false) String menuId) {
		try {
	    usuarioService.datosEmpleado(model, principal, session);
	    
	   // Object modulos = model.getAttribute("modulos");
	   List<ModulosRoles> modulosRoles = (List<ModulosRoles>) model.getAttribute("modulos");
	   
	   modulosRoles.forEach(System.out::println);
	    
	    if(modulosRoles == null || modulosRoles.isEmpty()) {
	    	return "redirect:/authenticate/login";
	    }else {
	    	
	    	ModulosRoles moduloActivado = usuarioService.buscarModuloActivado(modulosRoles, menuId);
	    	if(moduloActivado == null) {
	    		 return "redirect:/error/error403";
	    	}
	    	
	    	 
	    	boolean isModuleValid =usuarioService.hasAccess(modulosRoles, moduloActivado.getMdl_clave());
	    	 if(!isModuleValid) {
	    		 return "redirect:/error/error403";
	    	 }
	    	 
	    	 model.addAttribute("menuActivo", moduloActivado.getMdl_clave());
	    	 model.addAttribute("menuReporteId", menuId);
	    	 model.addAttribute("menuReporteNombre", moduloActivado.getMdl_nombre());
	    	 
	       }
		} catch (AuthenticationCredentialsNotFoundException e) {
	        return "redirect:/authenticate/login";
	    } catch (DisabledException e) {
	        return "redirect:/authenticate/login";
	    }
	    
	    return reportesEspecialesService.repIntegralCobranza(model, principal, session);

	}
	
	@PostMapping("/repEspeciales/reportes/cargarReportes")
	@ResponseBody
	public List<Map<String, String>> loadCollectReports(HttpSession session,
			@RequestParam(required = true) String menuReporteId) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }
	    
	    
	    List<BuscaUsuarios> usuarioList = usuarioRepository.findUsers(username, 0);
	    if(usuarioList == null || usuarioList.isEmpty()) {
	        throw new AuthenticationCredentialsNotFoundException("Usuario no encontrado");
	    }
	    BuscaUsuarios usuario = usuarioList.get(0);
	    List<ReportesEspeciales> reportesCobranza = reportesEspecialesService.getReportsType(Integer.valueOf(menuReporteId).intValue(), Integer.valueOf(usuario.getUsr_rol_id()).intValue());
	    
	    return reportesCobranza.stream()
	        .map(rep -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", rep.getIdReporte());
	            rolMap.put("reporte", rep.getReporte());
	            rolMap.put("tabla", rep.getTabla());
	            rolMap.put("clave", rep.getClave());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/obtenerParametrosReporte")
	@ResponseBody
	public Map<String, Object> obtenerParametrosReporte(@RequestParam String reporteId) {
	    // Validar autenticación
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }
	    
	    // Lógica para determinar qué parámetros necesita el reporte
	    
	     Map<String, Object> parametros = reportesEspecialesService.obtieneParametrosReportes(Integer.valueOf(reporteId).intValue());
	     List<MemoriaTecnicaParamReportes> memoriaTecnica = reportesEspecialesService.obtieneMemoriaTecnicaParamReportes(Integer.valueOf(reporteId).intValue(), username);
	     
	     parametros.put("memoriaTecnica", memoriaTecnica);
	     
	    return parametros;
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroCartera")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroCartera(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<Cartera> carteras = reportesEspecialesService.obtenerParametroCartera();
	    
	    return carteras.stream()
	        .map(car -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", car.getCcbId());
	            rolMap.put("desc", car.getCcbCartera());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroClasificacionMembresia")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroClasificacionMembresia(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<ClasificacionMembresia> clasificacionMembresia = reportesEspecialesService.obtenerParametroClasificacionMembresia();
	    
	    return clasificacionMembresia.stream()
	        .map(clas -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", clas.getIdClasificacion());
	            rolMap.put("desc", clas.getClasificacion());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroEmpleados")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroEmpleados(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<Empleados> empleados = reportesEspecialesService.obtenerParametroEmpleados();
	    
	    return empleados.stream()
	        .map(emp -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", emp.getEmpId());
	            rolMap.put("desc", emp.getNombreEmpleado());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroEstados")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroEstados(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<Estados> estados = reportesEspecialesService.obtenerParametroEstados();
	    
	    return estados.stream()
	        .map(est -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", est.getIdEstado());
	            rolMap.put("desc", est.getEstado());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroEstatusRecibos")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroEstatusRecibos(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<EstatusRecibos> estatusRecibos = reportesEspecialesService.obtenerParametroEstatusRecibos();
	    
	    return estatusRecibos.stream()
	        .map(est -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", est.getIdEstatusRecibo());
	            rolMap.put("desc", est.getEstatusRecibo());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroEstatusUnidades")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroEstatusUnidades(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<EstatusUnidades> estatusUnidades = reportesEspecialesService.obtenerParametroEstatusUnidades();
	    
	    return estatusUnidades.stream()
	        .map(est -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", est.getIdEstatusUnidad());
	            rolMap.put("desc", est.getEstatusUnidad());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroLocaciones")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroLocaciones(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<Locaciones> locaciones = reportesEspecialesService.obtenerParametroLocaciones();
	    
	    return locaciones.stream()
	        .map(loc -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", loc.getIdLocacion());
	            rolMap.put("desc", loc.getLocacion());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroPromotores")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroPromotores(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<Promotores> promotores = reportesEspecialesService.obtenerParametroPromotores();
	    
	    return promotores.stream()
	        .map(promotor -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", promotor.getIdPromotor());
	            rolMap.put("desc", promotor.getPromotor());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroSeriesRecibos")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroSeriesRecibos(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<SeriesRecibos> seriesRecibos = reportesEspecialesService.obtenerParametroSeriesRecibos();
	    
	    return seriesRecibos.stream()
	        .map(serie -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", serie.getIdSerieRecibo());
	            rolMap.put("desc", serie.getSerieRecibo());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroTipoCuponesDescuentoPaq")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroTipoCuponesDescuentoPaq(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<TipoCuponesDescuentoPaq> tipoCuponesDescuentoPaq = reportesEspecialesService.obtenerParametroTipoCuponesDescuentoPaq();
	    
	    return tipoCuponesDescuentoPaq.stream()
	        .map(cupon -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", cupon.getIdTipoCuponDescuentoPaq());
	            rolMap.put("desc", cupon.getTipoCuponDescuentoPaq());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroTiposProductos")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroTiposProductos(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<TiposProductos> tiposProductos = reportesEspecialesService.obtenerParametroTiposProductos();
	    
	    return tiposProductos.stream()
	    		.map(cupon -> {
		            Map<String, String> rolMap = new HashMap<>();
		            rolMap.put("id", cupon.getIdTipoProducto());
		            rolMap.put("desc", cupon.getTipoProducto());
		            return rolMap;
		        })
		        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroUsuarios")
	@ResponseBody
	public List<Map<String, String>> obtenerParametroUsuarios(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<Usuarios> usuarios = reportesEspecialesService.obtenerParametroUsuarios();
	    
	    return usuarios.stream()
	    		.map(usr -> {
		            Map<String, String> rolMap = new HashMap<>();
		            rolMap.put("id", usr.getUsrEmpId());
		            rolMap.put("desc", usr.getUsrUsuario() + " - " + usr.getNombre());
		            return rolMap;
		        })
		        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroDesarrollo")
	@ResponseBody
	public List<Map<String, String>> loadRealState(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<Desarrollos> desarrollos = reportesEspecialesService.obtenerParametroDesarrollo();
	    
	    return desarrollos.stream()
	        .map(desarollo -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", desarollo.getIdDesarrollo());
	            rolMap.put("desarrollo", desarollo.getDesarrollo());
	            rolMap.put("tabla", desarollo.getTabla());
	            rolMap.put("clave", desarollo.getClave());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	@PostMapping("/repEspeciales/reportes/params/obtenerParametroTiposMovimiento")
	@ResponseBody
	public List<Map<String, String>> loadTypesMov(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<TiposMovimientos> tiposMovimientos = reportesEspecialesService.obtenerParametroTiposMovimiento();
	    
	    return tiposMovimientos.stream()
	        .map(mov -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", mov.getTmv_tipomovimiento());
	            rolMap.put("desarrollo", mov.getTmv_descripcion());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
	
	
	/*@PostMapping("/repEspeciales/cobranza/searchReport")
	@ResponseBody
	public ResponseEntity<?> searchReport(
	    @RequestParam(required = false) String fechaInicio,
	    @RequestParam(required = false) String fechaFin,
	    @RequestParam String reporteId,
	    @RequestParam(required = false) String desarrollos,
	    @RequestParam(required = false) String tiposMovimiento,
	    HttpSession session) {
	    
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName();
	        
	        if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }
	        
	        // Convertir JSON strings a listas si es necesario
	        List<Integer> desarrollosList = desarrollos != null && !desarrollos.isEmpty() ? 
	            new ObjectMapper().readValue(desarrollos, new TypeReference<List<Integer>>() {}) : 
	            Collections.emptyList();
	            
	        List<Integer> tiposMovList = tiposMovimiento != null && !tiposMovimiento.isEmpty() ? 
	            new ObjectMapper().readValue(tiposMovimiento, new TypeReference<List<Integer>>() {}) : 
	            Collections.emptyList();
	        
	        
	        byte[] reporteBytes =  reportesEspecialesService.buscarReporte(Integer.valueOf(reporteId).intValue(),fechaInicio,fechaFin,desarrollosList,tiposMovList);
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDisposition(ContentDisposition.attachment()
	                .filename("reporte.xlsx")
	                .build());
	        
	        return new ResponseEntity<>(reporteBytes, headers, HttpStatus.OK);
	        
	    } catch (JsonProcessingException e) {
	        log4j.error("Error al procesar parámetros JSON", e);
	        return ResponseEntity.badRequest().body("Error en el formato de los parámetros");
	    } catch (Exception e) {
	        log4j.error("Error al generar el reporte", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Ocurrió un error al generar el reporte");
	    }
	}*/
	
	@PostMapping("/repEspeciales/reportes/searchReport")
	@ResponseBody
	public ResponseEntity<?> searchReport(
	    @RequestParam(required = false) String fechaInicio,
	    @RequestParam(required = false) String fechaFin,
	    @RequestParam String reporteId,
	    @RequestParam String nombreReporte,
	    @RequestParam(required = false) String parametrosReporte,
	    HttpSession session) throws JsonProcessingException {
	    
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName();
	        
	        if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }
	        	        
	        
	       
	        reportesEspecialesService.eliminarMemoriaTecnicaParametrosReportes(Integer.valueOf(reporteId).intValue(), username);
	        reportesEspecialesService.guardarMemoriaTecnicaParametrosReportes(Integer.valueOf(reporteId).intValue(), parametrosReporte, username);
	        Map<String, Object> inforReport=  reportesEspecialesService.obtenercolumnasProcedimientoReporte(Integer.valueOf(reporteId).intValue(),fechaInicio,fechaFin,nombreReporte,parametrosReporte,username);
	        List<Map<String, Object>> datos = (List<Map<String, Object>>) inforReport.get("data");
	        List<String> columns = (List<String>) inforReport.get("columns");
	        UUID uuid = UUID.randomUUID();
	        inforReport.remove("data");
	        inforReport.put("uuid", uuid.toString());
	        session.setAttribute(uuid.toString() + "_datos", datos);
	        session.setAttribute(uuid.toString() + "_columnas", columns);
	        return ResponseEntity.ok(inforReport);
	        
	     /*   byte[] reporteBytes  =  reportesEspecialesService.buscarReporte(Integer.valueOf(reporteId).intValue(),fechaInicio,fechaFin,nombreReporte,parametrosReporte,username);
	        //byte[] reporteBytes = null;
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDisposition(ContentDisposition.attachment()
	                .filename("reporte.xlsx")
	                .build());
	        
	        return new ResponseEntity<>(reporteBytes, headers, HttpStatus.OK);*/
	        
	    } catch (Exception e) {
	        log4j.error("Error al generar el reporte", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Ocurrió un error al generar el reporte");
	    }
	}
	
	@PostMapping("/repEspeciales/reportes/generarReporte")
	@ResponseBody
	public ResponseEntity<?> generateReport(
	    @RequestParam(required = false) String fechaInicio,
	    @RequestParam(required = false) String fechaFin,
	    @RequestParam String reporteId,
	    @RequestParam String nombreReporte,
	    @RequestParam(required = false) String parametrosReporte,
	    @RequestParam String columnasSeleccionadas,
	    @RequestParam String uuid,
	    HttpSession session) throws JsonProcessingException {
	    
	    try {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        String username = authentication.getName();
	        
	        if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }
	        	        
	        List<Map<String, Object>> datos = (List<Map<String, Object>>) session.getAttribute(uuid + "_datos");
	        List<String> columnas = (List<String>) session.getAttribute(uuid + "_columnas");
	      // byte[] reporteBytes  =  reportesEspecialesService.buscarReporte(datos,columnas,columnasSeleccionadas,nombreReporte);
	        session.removeAttribute(uuid + "_datos");
	        session.removeAttribute(uuid + "_columnas");
	       byte[]  reporteBytes = excelService.generarReporteExcel(datos,columnas,columnasSeleccionadas,nombreReporte);
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDisposition(ContentDisposition.attachment()
	                .filename("reporte.xlsx")
	                .build());
	        
	        return new ResponseEntity<>(reporteBytes, headers, HttpStatus.OK);
	        
	    } catch (Exception e) {
	        log4j.error("Error al generar el reporte", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Ocurrió un error al generar el reporte");
	    }
	}
	
	
	
}