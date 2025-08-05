package coralpagos.com.mx.facil.app.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.fasterxml.jackson.databind.ObjectMapper;

import coralpagos.com.mx.facil.app.dto.Modulos;
import coralpagos.com.mx.facil.app.dto.ModulosRoles;
import coralpagos.com.mx.facil.app.dto.Roles;
import coralpagos.com.mx.facil.app.dto.TiposReportes;
import coralpagos.com.mx.facil.app.dto.UsuariosRoles;
import coralpagos.com.mx.facil.app.service.UsuarioService;
import coralpagos.com.mx.facil.app.util.AppConstanst;
import coralpagos.com.mx.orm.facil.SistemaRol;
import coralpagos.com.mx.util.facil.dto.CredencialesDTO;
import coralpagos.com.mx.util.facil.dto.SocioDTO;

@Controller
public class UserController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	private Logger log4j = LogManager.getLogger(UserController.class);
	
	
	@GetMapping("/usuarios/adminRoles")
	public String adminRoles( Model model, Principal principal, HttpSession session ) {
		try {
	    usuarioService.datosEmpleado(model, principal, session);
	    model.addAttribute("menuActivo", AppConstanst.MENU_ACTIVO_ADMIN_ROLES);
	   // Object modulos = model.getAttribute("modulos");
	   List<ModulosRoles> modulosRoles = (List<ModulosRoles>) model.getAttribute("modulos");
	    
	    if(modulosRoles == null || modulosRoles.isEmpty()) {
	    	return "redirect:/authenticate/login";
	    }else {
	    	/* boolean isModuleValid = modulosRoles.stream()
	    		        .anyMatch(modulo -> AppConstanst.MENU_ACTIVO_ADMIN_ROLES.equals(modulo.getMdl_clave()));*/
	    	 
	    	boolean isModuleValid =usuarioService.hasAccess(modulosRoles,AppConstanst.MENU_ACTIVO_ADMIN_ROLES);
	    	 if(!isModuleValid) {
	    		 return "redirect:/error/error403";
	    	 }
	    	 
	    }
		} catch (AuthenticationCredentialsNotFoundException e) {
	        return "redirect:/authenticate/login";
	    } catch (DisabledException e) {
	        return "redirect:/authenticate/login";
	    }
	    return usuarioService.adminRoles(model, principal, session);
	}
	
	@GetMapping("/usuarios/searchRoles")
	public String searchRoles( SistemaRol sistemaRol,
            BindingResult result,
            Model model,
            HttpSession session ) {
		//return usuarioService.searchRoles(model, sistemaRol, session);
		return "";
	}
	
	@PostMapping("/usuarios/saveRole")
	public String saveRole( SistemaRol sistemaRol,
            BindingResult result,
            Model model,
            HttpSession session ) {
		log4j.info("saveRole");
		//return usuarioService.searchRoles(model, sistemaRol, session);
		return "";
	}
	
	@PostMapping("/usuarios/rol")
	public String manejarRol(@ModelAttribute Roles roles,
	                         @RequestParam("accion") String accion,
	                         Model model,
	                         HttpSession session) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		usuarioService.datosEmpleado(model, null, session);
		model.addAttribute("menuActivo", AppConstanst.MENU_ACTIVO_ADMIN_ROLES);
		
		if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
			return "/authenticate/login";
		}

	    if ("searchRoles".equals(accion)) {
	    	log4j.info("searchRoles");
	        return usuarioService.searchRoles(model, roles, session);
	    } else if ("saveRoles".equals(accion)) {
	    	log4j.info("saveRoles");
	       return usuarioService.saveRoles(model, roles, session,username);
	    } else {
	        return "redirect:/error"; // por si viene algo inesperado
	    }
	}
	
	@GetMapping("/usuarios/auth/roles")
	public String authRoles( Model model, Principal principal, HttpSession session ) {
		return usuarioService.datosEmpleado(model, principal, session);
	}
		
	@PostMapping("/usuarios/deleterole22")
	public String deleterole22(@RequestParam Long id,
	                         Model model,
	                         HttpSession session,
	                         RedirectAttributes redirectAttributes) {
	    
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	        return "/authenticate/login";
	    }

	    try {
	        String resultado = usuarioService.deleteRole(model, id, username);
	        if(resultado.contains("éxito")) {
	            redirectAttributes.addFlashAttribute("success", true);
	            redirectAttributes.addFlashAttribute("msg", "Rol eliminado con éxito");
	        } else {
	            redirectAttributes.addFlashAttribute("success", false);
	            redirectAttributes.addFlashAttribute("msg", resultado);
	        }
	        return "redirect:/usuarios/adminRoles";
	    } catch (Exception e) {
	    	log4j.error(e);
	    	e.printStackTrace();
	        redirectAttributes.addFlashAttribute("success", false);
	        redirectAttributes.addFlashAttribute("msg", "Error al eliminar el rol: " + e.getMessage());
	        return "redirect:/usuarios/adminRoles";
	    }
	}
	
	@PostMapping("/usuarios/deleterole")
	public String deleterole(@RequestParam Long id,
	                         Model model,
	                         HttpSession session) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		
		if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
			return "/authenticate/login";
		}

	        return usuarioService.deleteRole(model, id,username);
	}
	
	
	@PostMapping("/usuarios/loadRolesJson")
	@ResponseBody
	public List<Map<String, String>> loadRolesJson(HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    
	    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
	    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
	    }

	    List<Roles> roles = usuarioService.loadRoles();
	    
	    return roles.stream()
	        .map(rol -> {
	            Map<String, String> rolMap = new HashMap<>();
	            rolMap.put("id", rol.getRol_Id());
	            rolMap.put("nombre", rol.getRol_Descripcion());
	            return rolMap;
	        })
	        .collect(Collectors.toList());
	}
		
	    @PostMapping("/usuarios/roles/saveInheritance")
	    public ResponseEntity<Map<String, Object>> saveInheritance(
	            @RequestBody Map<String, Object> request,
	            @RequestHeader("X-CSRF-TOKEN") String csrfToken) {
	        
	        Map<String, Object> response = new HashMap<>();
	        
	        try {
	            Long rolId = ((Number) request.get("rolId")).longValue();
	            Long rolPadreId = ((Number) request.get("rolPadreId")).longValue();
	            List<Integer> modulosSeleccionados = (List<Integer>) request.get("modulosSeleccionados");
	            
	            // Validación básica
	            if(rolId == null || rolPadreId == null) {
	                response.put("success", false);
	                response.put("message", "Datos incompletos");
	                return ResponseEntity.badRequest().body(response);
	            }
	            

	            usuarioService.saveInheritance(rolId,rolPadreId,modulosSeleccionados);
	            
	            response.put("success", true);
	            response.put("message", "Herencia guardada exitosamente");
	            return ResponseEntity.ok(response);
	            
	        } catch (Exception e) {
	        	log4j.error(e);
		    	e.printStackTrace();
	            response.put("success", false);
	            response.put("message", "Error al guardar herencia: " + e.getMessage());
	            return ResponseEntity.internalServerError().body(response);
	        }
	    }
	    
	    @PostMapping("/usuarios/role/cambiarEstadoRol")
	    @ResponseBody
	    public ResponseEntity<Map<String, Object>> cambiarEstadoRol(
	        @RequestBody Map<String, Object> request,
	        Principal principal) {
	        
	        Map<String, Object> response = new HashMap<>();
	        try {
	            Integer rolId = (Integer) request.get("rolId");
	            Boolean nuevoEstado = (Boolean) request.get("nuevoEstado");
	            
	            
	            List<UsuariosRoles> usuariosRolesList = usuarioService.getUsersByRole(rolId);
	            // Aquí va tu lógica para cambiar el estado del rol
	            if(!nuevoEstado) {
	            	if(usuariosRolesList.isEmpty()) {
	            usuarioService.enableDisableRole(rolId, nuevoEstado);	            
	            response.put("success", true);
	            response.put("message", "Estado del rol actualizado correctamente");
	            return ResponseEntity.ok(response);
	            	}else {
	            		response.put("success", false);
	            		response.put("message", "Hay usuarios asignados a este rol. No es posible deshabilitarlo.");
	            		return ResponseEntity.ok(response);
	            	}
	            }else {
	            	usuarioService.enableDisableRole(rolId, nuevoEstado);	            
		            response.put("success", true);
		            response.put("message", "Estado del rol actualizado correctamente");
		            return ResponseEntity.ok(response);
	            }
	        } catch (Exception e) {
	        	log4j.error(e);
		    	e.printStackTrace();
	            response.put("success", false);
	            response.put("message", "Error al cambiar el estado: " + e.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	        }
	    }

		@PostMapping("/usuarios/loadRolesModulosJson")
		@ResponseBody
		public List<Map<String, String>> loadRolesModulosJson(HttpSession session) {
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = authentication.getName();
		    
		    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
		    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		    }

		    List<Roles> roles = usuarioService.loadRoles();
		    
		    return roles.stream()
		        .map(rol -> {
		            Map<String, String> rolMap = new HashMap<>();
		            rolMap.put("id", rol.getRol_Id());
		            rolMap.put("nombre", rol.getRol_Descripcion());
		            return rolMap;
		        })
		        .collect(Collectors.toList());
		}
		
		@PostMapping("/usuarios/loadModulosJson")
		@ResponseBody
		public List<Map<String, String>> loadModulosJson(HttpSession session) {
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = authentication.getName();
		    
		    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
		    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		    }

		    List<Modulos> modulos = usuarioService.getModules();
		    log4j.info(modulos.toString());
		    ObjectMapper mapper = new ObjectMapper();
	        String jsonString = null;
			try {
				jsonString = mapper.writeValueAsString(modulos);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log4j.info(jsonString);
		    
		    return modulos.stream()
		        .map(modulo -> {
		            Map<String, String> rolMap = new HashMap<>();
		            rolMap.put("mdl_id", modulo.getMdl_id());
		            rolMap.put("mdl_padre_id", modulo.getMdl_padre_id());
		            rolMap.put("mdl_clave", modulo.getMdl_clave());
		            rolMap.put("mdl_nombre", modulo.getMdl_nombre());
		            rolMap.put("mdl_orden", modulo.getMdl_orden());
		            
		            return rolMap;
		        })
		        .collect(Collectors.toList());
		}
		
		@PostMapping("/usuarios/getModulosRol")
		@ResponseBody
		public List<ModulosRoles> getModulosRol(@RequestParam String rolId, HttpSession session) {
		    // Validar sesión y permisos
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = authentication.getName();
		    List<ModulosRoles> modulosrolesist = null;
		    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
		        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		    }
		    
		    ObjectMapper mapper = new ObjectMapper();
	        String jsonString = null;
			try {
				modulosrolesist = usuarioService.findModulesByRole(Integer.valueOf(rolId).intValue());
				jsonString = mapper.writeValueAsString(modulosrolesist);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log4j.info(jsonString);

		    return modulosrolesist;
		}
		
		@PostMapping("/usuarios/roles/editModulesRoles")
	    public ResponseEntity<Map<String, Object>> editModulesRoles(
	            @RequestBody Map<String, Object> request,
	            @RequestHeader("X-CSRF-TOKEN") String csrfToken) {
	        
	        Map<String, Object> response = new HashMap<>();
	        
	        try {
	            String rolId =  request.get("rolId").toString();
	            List<Integer> modulosSeleccionados = (List<Integer>) request.get("modulosSeleccionados");
	            
	            // Validación básica
	            if(rolId == null || rolId.isEmpty()) {
	                response.put("success", false);
	                response.put("message", "Datos incompletos");
	                return ResponseEntity.badRequest().body(response);
	            }
	            

	            usuarioService.editModulesRoles(Long.valueOf(rolId),modulosSeleccionados);
	            
	            response.put("success", true);
	            response.put("message", "Herencia guardada exitosamente");
	            return ResponseEntity.ok(response);
	            
	        } catch (Exception e) {
	        	log4j.error(e);
		    	e.printStackTrace();
	            response.put("success", false);
	            response.put("message", "Error al guardar herencia: " + e.getMessage());
	            return ResponseEntity.internalServerError().body(response);
	        }
	    }

		@GetMapping("/usuarios/adminReportes")
		public String adminReportes( Model model, Principal principal, HttpSession session ) {
			try {
		    usuarioService.datosEmpleado(model, principal, session);
		    model.addAttribute("menuActivo", AppConstanst.MENU_ACTIVO_ADMIN_REPORTES);
		   // Object modulos = model.getAttribute("modulos");
		   List<ModulosRoles> modulosRoles = (List<ModulosRoles>) model.getAttribute("modulos");
		    
		   modulosRoles.forEach(System.out::println);
		   
		    if(modulosRoles == null || modulosRoles.isEmpty()) {
		    	return "redirect:/authenticate/login";
		    }else {
		    	/* boolean isModuleValid = modulosRoles.stream()
		    		        .anyMatch(modulo -> AppConstanst.MENU_ACTIVO_ADMIN_ROLES.equals(modulo.getMdl_clave()));*/
		    	 
		    	boolean isModuleValid =usuarioService.hasAccess(modulosRoles,AppConstanst.MENU_ACTIVO_ADMIN_REPORTES);
		    	 if(!isModuleValid) {
		    		 return "redirect:/error/error403";
		    	 }
		    	 
		    }
			} catch (AuthenticationCredentialsNotFoundException e) {
		        return "redirect:/authenticate/login";
		    } catch (DisabledException e) {
		        return "redirect:/authenticate/login";
		    }
		    return usuarioService.adminReportes(model, principal, session);
		}
		
		@PostMapping("/usuarios/AdmonReportes")
		public String AdmonReportes(@ModelAttribute Roles roles,
		                         @RequestParam("accion") String accion,
		                         Model model,
		                         HttpSession session) {
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			usuarioService.datosEmpleado(model, null, session);
			model.addAttribute("menuActivo", AppConstanst.MENU_ACTIVO_ADMIN_REPORTES);
			
			if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
				return "/authenticate/login";
			}

		    if ("searchRoles".equals(accion)) {
		    	log4j.info("searchRoles");
		        return usuarioService.searchRolesReportes(model, roles, session);
		    } else if ("saveRoles".equals(accion)) {
		    	log4j.info("saveRoles");
		       return usuarioService.saveRoles(model, roles, session,username);
		    } else {
		        return "redirect:/error"; // por si viene algo inesperado
		    }
		}
		
		@PostMapping("/usuarios/loadTiposReportesJson")
		@ResponseBody
		public List<Map<String, String>> loadTiposReportesJson(HttpSession session) {
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = authentication.getName();
		    
		    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
		    	throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		    }

		    List<TiposReportes> reportes = usuarioService.obtieneTiposReportes();
		    log4j.info(reportes.toString());
		    ObjectMapper mapper = new ObjectMapper();
	        String jsonString = null;
			try {
				jsonString = mapper.writeValueAsString(reportes);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log4j.info(jsonString);
		    
		    return reportes.stream()
		        .map(reporte -> {
		            Map<String, String> rolMap = new HashMap<>();
		            rolMap.put("mdl_tipo_id", reporte.getIdTipoReporte());
		            rolMap.put("mdl_tipo_reporte", reporte.getTipoReporte());
		            rolMap.put("mdl_tabla", reporte.getTabla());
		            rolMap.put("mdl_clave", reporte.getClave());		            
		            return rolMap;
		        })
		        .collect(Collectors.toList());
		}
		
		@PostMapping("/usuarios/getReportesRol")
		@ResponseBody
		public List<TiposReportes> getReportesRol(@RequestParam String rolId, HttpSession session) {
		    // Validar sesión y permisos
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String username = authentication.getName();
		    List<TiposReportes> reportesRolList = null;
		    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
		        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
		    }
		    
		    ObjectMapper mapper = new ObjectMapper();
	        String jsonString = null;
			try {
				reportesRolList = usuarioService.getReportesRol(Integer.valueOf(rolId).intValue());
				jsonString = mapper.writeValueAsString(reportesRolList);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			log4j.info(jsonString);

		    return reportesRolList;
		}
		
		@PostMapping("/usuarios/updateAssignReports")
		@ResponseBody
		public ResponseEntity<Map<String, Object>> updateReportesRol(
		        @RequestBody Map<String, Object> request,
		        @RequestHeader("X-CSRF-TOKEN") String csrfToken) {
			
			 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			    String username = authentication.getName();
			    if(username.trim().isEmpty() || "anonymousUser".equals(username)) {
			        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
			    }
		    
		    Map<String, Object> response = new HashMap<>();
		    
		    try {
		        Integer rolId = (Integer) request.get("rolId");
		        List<Integer> reportesSeleccionados = (List<Integer>) request.get("reportesSeleccionados");
		        
		        // Validación básica
		        if(rolId == null || reportesSeleccionados == null) {
		            response.put("success", false);
		            response.put("message", "Datos incompletos");
		            return ResponseEntity.badRequest().body(response);
		        }
		        
		        String reportesComoCadena = reportesSeleccionados.stream()
		        	    .map(String::valueOf) 
		        	    .collect(Collectors.joining(","));
		        
		         usuarioService.updateReportesRol(rolId.intValue(), reportesComoCadena, username);
		        
		        response.put("success", true);
		        response.put("message", "Reportes actualizados exitosamente");
		        return ResponseEntity.ok(response);
		        
		    } catch (Exception e) {
		        log4j.error(e);
		        e.printStackTrace();
		        response.put("success", false);
		        response.put("message", "Error al actualizar reportes: " + e.getMessage());
		        return ResponseEntity.internalServerError().body(response);
		    }
		}
		
		@GetMapping("/register/activeUser")
		public String registerActiveUser( @RequestParam(required = true) String otp, @RequestParam(required = true) String userName,Model model, HttpSession session) {
			return usuarioService.registerActiveUser(otp, userName, model, session);
		}
		
	
}