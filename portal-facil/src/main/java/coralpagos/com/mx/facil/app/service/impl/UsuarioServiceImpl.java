package coralpagos.com.mx.facil.app.service.impl;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import coralpagos.com.mx.facil.app.dto.BuscaUsuarios;
import coralpagos.com.mx.facil.app.dto.Modulos;
import coralpagos.com.mx.facil.app.dto.ModulosRoles;
import coralpagos.com.mx.facil.app.dto.ModulosUrl;
import coralpagos.com.mx.facil.app.dto.Roles;
import coralpagos.com.mx.facil.app.dto.TiposReportes;
import coralpagos.com.mx.facil.app.dto.UsuariosRoles;
import coralpagos.com.mx.facil.app.repository.AdministracionReportesRepository;
import coralpagos.com.mx.facil.app.repository.CodigoSeguridadRepository;
import coralpagos.com.mx.facil.app.repository.DatosAuditoriaRepository;
import coralpagos.com.mx.facil.app.repository.EmpleadoRepository;
import coralpagos.com.mx.facil.app.repository.ListaValoresRepository;
import coralpagos.com.mx.facil.app.repository.ParametroSistemaRepository;
import coralpagos.com.mx.facil.app.repository.PlantillaCorreoRepository;
import coralpagos.com.mx.facil.app.repository.RolesRepository;
import coralpagos.com.mx.facil.app.repository.SistemaRolRepository;
import coralpagos.com.mx.facil.app.repository.SistemaUsuarioRepository;
import coralpagos.com.mx.facil.app.repository.UserSystemRepository;
import coralpagos.com.mx.facil.app.repository.UserSystemSistemaRolRepository;
import coralpagos.com.mx.facil.app.repository.UsuarioRepository;
import coralpagos.com.mx.facil.app.service.UsuarioService;
import coralpagos.com.mx.facil.app.util.AppConstanst;
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
import coralpagos.com.mx.util.facil.Utils;
import coralpagos.com.mx.util.facil.dto.BusquedaDTO;
import coralpagos.com.mx.util.facil.dto.CredencialesDTO;
import coralpagos.com.mx.util.facil.dto.EnviarCodigoDTO;
import coralpagos.com.mx.util.facil.dto.SocioDTO;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {
	
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
	private AdministracionReportesRepository administracionReportesRepository;
	
	@Autowired
	private DatosAuditoriaRepository datosAuditoriaRepository;
	
	@Value("${spring.mail.username}")
	private String username;
	
    private JavaMailSender mailSender;
    
    private Logger log4j = LogManager.getLogger(UsuarioServiceImpl.class);
	
	public UsuarioServiceImpl(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}
	
	@Override
	public Optional<UserSystem> findByUsuario(String usuario) {
		return this.userSystemRepository.findByUsuario(usuario);
	}
	
	@Override
	public UserSystem guardar(UserSystem entity) {
		return this.userSystemRepository.save(entity);
	}

	@Override
	public String authenticacion(String error, String logout, RedirectAttributes flash, Principal principal,
			HttpSession session) {
		if (principal != null) {
			flash.addFlashAttribute("clase", "success");
			flash.addFlashAttribute("mensaje", "Ya ha iniciado sesión anteriormente ");
			session.removeAttribute("mensaje");
			session.removeAttribute("userSystem");
			return "redirect:/home";
		}
		
		if(error != null && !error.isEmpty()) {
			session.removeAttribute("userSystem");
			session.removeAttribute("mensaje");
			flash.addFlashAttribute("clase", "danger");
			flash.addFlashAttribute("mensaje", error);
			
			return "redirect:/authenticate/login";
		}
		
		if (error != null) {
			
			String mensaje = "La contraseña es incorrecta. Por favor pasa a la sección olvide contraseña.";
			
			if( session.getAttribute("mensaje") != null ) {
				int code = (int) session.getAttribute("mensaje");
				if ( code == 1 ) {
					 mensaje = "El usuario no existe, por favor debes de darte de alta.";
				} else if ( code == 2 ) {
					 mensaje = "Llevas más de 3 intentos, y has sido bloqueado. Dirígete a olvide contraseña.";
				} else if ( code == 3 ) {
					 mensaje = "Tú cuenta expiró.";
				} else if ( code == 4 ) {
					 mensaje = "Tú credencial expiró.";
				} else if ( code == 5 ) {
					 mensaje = "Tú cuenta ha sido bloqueada.";
				} else if ( code == 6 ) {
					 mensaje = "Intentos no permitidos.";
				} else if ( code == 7 ) {
					 mensaje = "Tú cuenta se encuentra en mantenimiento.";
				} else if ( code == 8 ) {
					 mensaje = "Tú cuenta no cuenta con un rol.";
				} else if ( code == 9 ) {
					 mensaje = "Tú membresía no esta activa.";
				}
			}
			
			if (  session.getAttribute("userSystem") != null ) {
			UserSystem userSystem = (UserSystem) session.getAttribute("userSystem");
			int intentos = userSystem.getIntentos();
			if ( intentos < 0) {
				intentos = 0;
			}
			intentos++;
			userSystem.setIntentos(intentos);
			userSystemRepository.save(userSystem);
			}
			
			session.removeAttribute("userSystem");
			session.removeAttribute("mensaje");
			flash.addFlashAttribute("clase", "danger");
			flash.addFlashAttribute("mensaje", mensaje);
			
			return "redirect:/authenticate/login";
		}else {
			if (session != null && session.getAttribute("userSystem") != null) {
			 UserSystem userSystem = (UserSystem) session.getAttribute("userSystem");
			    int activo = userSystem.getActivo();
				
				if(activo == 0) {
					session.removeAttribute("userSystem");
					session.removeAttribute("mensaje");
					flash.addFlashAttribute("clase", "danger");
					flash.addFlashAttribute("mensaje", "Tu cuenta se encuentra inactiva.");
					return "redirect:/authenticate/login";
				}
			}
			    
		}
		
		if (logout != null) {
			flash.addFlashAttribute("clase", "success");
			flash.addFlashAttribute("mensaje", "Ha cerrado sesión exitosamente.");
			session.removeAttribute("mensaje");
			session.removeAttribute("userSystem");
			return "redirect:/authenticate/login";
		}
		
		session.removeAttribute("userSystem");
		session.removeAttribute("mensaje");
		return "acceso/login";
	}
	
	@Override
	public String olvido(Model model, Principal principal) {
		return "acceso/olvido";
	}

	@Override
	public String registro(Model model) {
		model.addAttribute("socio", new SocioDTO());
		return "acceso/registro";
	}

	@Override
	public String datosEmpleado(Model model, Principal principal, HttpSession session) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	    model.addAttribute("socio", new BusquedaDTO());
	    
	    List<BuscaUsuarios> usuarioList = usuarioRepository.findUsers(username, 0);
	    if(usuarioList == null || usuarioList.isEmpty()) {
	        throw new AuthenticationCredentialsNotFoundException("Usuario no encontrado");
	    }
	    BuscaUsuarios usuario = usuarioList.get(0);
	    if(!"ACTIVO".equals(usuario.getEstatus())){
	        throw new DisabledException("Usuario no activo");
	    }
	    
	    Optional<UserSystem> userSystemOpt =  userSystemRepository.findByUsuario(username);
	    UserSystem userSystem = userSystemOpt.get();
	    int activo = userSystem.getActivo();
		
		if(activo == 0) {
			session.setAttribute("userSystem", userSystem);
			  throw new DisabledException("El usuario se encuentra inactiva.");
		}
	    
	    List<ModulosRoles> modulosUsuario = rolesRepository.findModulesByRole(Integer.valueOf(usuario.getUsr_rol_id()));
	    
	    // Mapa para almacenar las rutas de los módulos
	    Map<Integer, String> rutasModulos = new HashMap<>();
	    
	    // Obtener las rutas para cada módulo
	    for(ModulosRoles modulo : modulosUsuario) {
	        int idModulo = Integer.valueOf(modulo.getRom_mdl_id());
	        List<ModulosUrl> modulosPathList = usuarioRepository.findModulePath(idModulo);
	        
	        if(modulosPathList != null && !modulosPathList.isEmpty()) {
	            rutasModulos.put(idModulo, modulosPathList.get(0).getMu_path());
	        } else {
	           // rutasModulos.put(idModulo, "/home/" + UUID.randomUUID().toString());
	        	rutasModulos.put(idModulo, "/home#");
	        }
	    }
	    
	    // Organizar módulos en jerarquía completa
	    Map<String, List<ModulosRoles>> jerarquiaModulos = modulosUsuario.stream()
	        .filter(mod -> mod.getMdl_padre_id() != null && !mod.getMdl_padre_id().isEmpty())
	        .collect(Collectors.groupingBy(ModulosRoles::getMdl_padre_id));
	    
	    // Asignar rutas y construir jerarquía completa
	    modulosUsuario.forEach(mod -> {
	        int idModulo = Integer.valueOf(mod.getRom_mdl_id());
	        mod.setMu_path(rutasModulos.getOrDefault(idModulo, "/home"));
	        
	        // Asignar hijos directos
	        if(jerarquiaModulos.containsKey(mod.getRom_mdl_id())) {
	            List<ModulosRoles> hijos = jerarquiaModulos.get(mod.getRom_mdl_id());
	            mod.setHijos(hijos);
	            
	            // Para cada hijo, asignar sus nietos (si los tiene)
	            hijos.forEach(hijo -> {
	                if(jerarquiaModulos.containsKey(hijo.getRom_mdl_id())) {
	                    hijo.setHijos(jerarquiaModulos.get(hijo.getRom_mdl_id()));
	                }
	            });
	        }
	    });
	    
	    // Filtrar módulos de nivel superior (los que tienen MDL_PADRE_ID = "10")
	    List<ModulosRoles> modulosNivelSuperior = modulosUsuario.stream()
	        .filter(mod -> "10".equals(mod.getMdl_padre_id()))
	        .collect(Collectors.toList());
	    
	    model.addAttribute("usuario", usuario);
	    model.addAttribute("modulos", modulosNivelSuperior);
	    return "administracion/console";
	}
	
	
	/*@Override
	public String reset(String email, String usuario, Model model, HttpSession session) throws MessagingException {
        model.addAttribute("dto", new EnviarCodigoDTO());

		String mensajeModel = "";
		String claseModel = "";
		
		Optional<SistemaUsuario> s = sistemaUsuarioRepository.findById(2l);
		
		Optional<UserSystem> validacionMultiple = userSystemRepository.findByCorreoAndUsuarioAndActivo( email, usuario, 1);
		Optional<UserSystem> user = Optional.empty();
		
		if ( validacionMultiple.isPresent() ) {
			user = validacionMultiple;
		} else {
			validacionMultiple = userSystemRepository.findByCorreoAndUsuario( email, usuario );
			
			if ( validacionMultiple.isPresent() ) {
				user = validacionMultiple;
			}
			
		}

		if (user.isPresent()) {
			
			UserSystem us = user.get();
			
			
			Optional<PlantillaCorreo> p = plantillaCorreoRepository.findById(1l);

			if (p.isPresent()) {
				Optional<ParametroSistema> pa = parametroSistemaRepository.findById(30l);
				Optional<ParametroSistema> subject = parametroSistemaRepository.findById(31l);
				if (pa.isPresent()) {
					ParametroSistema mensaje = pa.get();
					String otp = new String(Utils.generaOTP(5));
					PlantillaCorreo param = p.get();
					String plantilla = param.getPlantilla().replace("{nombre}", us.getUsuario());
					plantilla = plantilla.replace("{texto}", mensaje.getVariable().trim());
					
					log4j.info(otp);
					
					plantilla = plantilla.replace("{codigo}", otp);
					          	
					//email = "cristopher.o.m@gmail.com";
					this.sendHtml(username, email , subject.get().getVariable().trim(), plantilla);

					Optional<CodigoSeguridad> code = codigoSeguridadRepository.findByClienteAndIdActivo(user.get(), 1);
					CodigoSeguridad c = new CodigoSeguridad();

					String clave = bCryptPasswordEncoder.encode(otp.trim());
					if (!code.isPresent()) {
						c = CodigoSeguridad.builder().codigo(clave).intento(0).cliente(user.get()).usuarioAlta(s.get())
								.usuarioUltimaActualizacion(s.get()).build();
						c = codigoSeguridadRepository.save(c);
					} else {
						c = code.get();
						c.setCodigo(clave);
						c.setIntento(0);
						c.setUsuarioUltimaActualizacion(s.get());
						c = codigoSeguridadRepository.save(c);
					}

					if (c == null) {
						model.addAttribute("mensaje", "Error al generar tu código de seguridad.");
						model.addAttribute("clase", "danger");
						return "acceso/olvido";
					}
					session.setAttribute("username", us);
					return "acceso/otp";
				} else {
					model.addAttribute("mensaje", "Contacta al personal autorizado.");
					model.addAttribute("clase", "danger");
				}

			} else {
				model.addAttribute("mensaje", "Contacta al administrador.");
				model.addAttribute("clase", "danger");
			}

		} else { 
			
			mensajeModel = "Verifica tu correo o usuario.";
			claseModel = "danger";
			
			Optional<UserSystem> validaUsuario = userSystemRepository.findByUsuarioAndActivo( usuario, 1);
			
			if ( !validaUsuario.isPresent() ) {
				mensajeModel = "Verifica tu usuario.";
			}
			
			List<UserSystem> emails = userSystemRepository.findByCorreoAndActivo(email, 1);
			
			if ( emails.size() == 0 ) {
				mensajeModel = "Verifica tu correo.";
			}
			
		}

		model.addAttribute("mensaje", mensajeModel);
		model.addAttribute("clase", claseModel);
		
		return "acceso/olvido";
	}*/
	
	@Override
	public String reset(String email, String usuario, Model model, HttpSession session) throws MessagingException {
		try {
        model.addAttribute("dto", new EnviarCodigoDTO());

		String mensajeModel = "";
		String claseModel = "";
		
		Optional<SistemaUsuario> s = sistemaUsuarioRepository.findById(2l);
		
		Optional<UserSystem> validacionMultiple = userSystemRepository.findByCorreoAndUsuarioAndActivo( email, usuario, 1);
		Optional<UserSystem> user = Optional.empty();
		
		if ( validacionMultiple.isPresent() ) {
			user = validacionMultiple;
		} else {
			validacionMultiple = userSystemRepository.findByCorreoAndUsuario( email, usuario );
			
			if ( validacionMultiple.isPresent() ) {
				user = validacionMultiple;
			}
			
		}

		if (user.isPresent()) {
			
			UserSystem us = user.get();
			
			
			Optional<PlantillaCorreo> p = plantillaCorreoRepository.findById(14l);

			if (p.isPresent()) {
				Optional<ParametroSistema> pa = parametroSistemaRepository.findById(109l);
				Optional<ParametroSistema> subject = parametroSistemaRepository.findById(110l);
				if (pa.isPresent()) {
					ParametroSistema mensaje = pa.get();
					String otp = new String(Utils.generaOTP(5));
					PlantillaCorreo param = p.get();
					String plantilla = param.getPlantilla().replace("{nombre}", us.getUsuario());
					plantilla = plantilla.replace("{texto}", mensaje.getVariable().trim());
					
					log4j.info(otp);
					
					String urlBase = ServletUriComponentsBuilder
					        .fromCurrentContextPath()  // Obtiene el contexto actual
					        .build()
					        .toUriString();
					    
					    log4j.info("URL base: " + urlBase);
					
					
			        String url = urlBase + "/authenticate/code?otp=" + otp;
					
					plantilla = plantilla.replace("{url}", url);
					          	
					//email = "cristopher.o.m@gmail.com";
					this.sendHtml(username, email , subject.get().getVariable().trim(), plantilla);
					 log4j.info("Correo por reset de password enviado a " + email + " con el asunto: " + subject.get().getVariable().trim() + " y mensaje: " + plantilla);
					Optional<CodigoSeguridad> code = codigoSeguridadRepository.findByClienteAndIdActivo(user.get(), 1);
					CodigoSeguridad c = new CodigoSeguridad();

					String clave = bCryptPasswordEncoder.encode(otp.trim());
					if (!code.isPresent()) {
						c = CodigoSeguridad.builder().codigo(clave).intento(0).cliente(user.get()).usuarioAlta(s.get())
								.usuarioUltimaActualizacion(s.get()).build();
						c = codigoSeguridadRepository.save(c);
					} else {
						c = code.get();
						c.setCodigo(clave);
						c.setIntento(0);
						c.setUsuarioUltimaActualizacion(s.get());
						c = codigoSeguridadRepository.save(c);
					}

					if (c == null) {
						 log4j.info("Error al generar el código de seguridad de " + username);
						model.addAttribute("mensaje", "Error al generar tu código de seguridad.");
						model.addAttribute("clase", "danger");
						return "acceso/olvido";
					}
					session.setAttribute("username", us);
					 return "redirect:/authenticate/login?resetEmailSent=true";
				} else {
					 log4j.info("No se encontó el cuerpo en parametro de sistema para envío de correo por reset de password");
					model.addAttribute("mensaje", "Contacta al personal autorizado.");
					model.addAttribute("clase", "danger");
				}

			} else {
				 log4j.info("No se encontó la plantilla para envío de correo por reset de password");
				model.addAttribute("mensaje", "Contacta al administrador.");
				model.addAttribute("clase", "danger");
			}

		} else { 
			
			mensajeModel = "Verifica tu correo o usuario.";
			claseModel = "danger";
			
			Optional<UserSystem> validaUsuario = userSystemRepository.findByUsuarioAndActivo( usuario, 1);
			
			if ( !validaUsuario.isPresent() ) {
				 log4j.info("No se encontó en UserSystem el usuario activo: " + usuario);
				mensajeModel = "Verifica tu usuario.";
			}
			
			List<UserSystem> emails = userSystemRepository.findByCorreoAndActivo(email, 1);
			
			if ( emails.size() == 0 ) {
				 log4j.info("El correo " + email + " no coincide con el registrado del usuario " + usuario);
				mensajeModel = "Verifica tu correo.";
			}
			
		}

		model.addAttribute("mensaje", mensajeModel);
		model.addAttribute("clase", claseModel);
		
		return "acceso/olvido";
		}catch(Exception ex) {
			ex.printStackTrace();
			log4j.error("Exception ", ex);
			model.addAttribute("mensaje", ex.toString());
			model.addAttribute("clase", "danger");
			
			return "acceso/olvido";
		}
	}
	
	@Override
	public String otp(String otp, Model model, HttpSession session) {
		UserSystem us = (UserSystem) session.getAttribute("username");
		Optional<CodigoSeguridad> code = codigoSeguridadRepository.findByClienteAndIdActivo(us, 1);
		if ( code.isPresent() ) {
			CodigoSeguridad x = code.get();
			String codeOtpBD = x.getCodigo();
			BCryptPasswordEncoder b = new BCryptPasswordEncoder();
			if ( b.matches(otp, codeOtpBD) ) {
				x.setIdActivo(2);
				model.addAttribute("credencial", new CredencialesDTO());
				codigoSeguridadRepository.save(x);
				return "acceso/password";
			} else {
				 model.addAttribute("mensaje", "El codigo de seguridad es incorrecto.");
				 model.addAttribute("clase", "danger");
			}
			
		}  else {
			 model.addAttribute("mensaje", "El codigo de seguridad es incorrecto.");
			 model.addAttribute("clase", "danger");
		}

		return "acceso/otp";
	}
	
	private void sendHtml(String from, String to, String subject, String text) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text, true);
		mailSender.send(message);
		 log4j.info("Correo enviado a " + to + " con el asunto: " + subject + " y mensaje: " + text + " desde: " + from);
	}

	@Override
	public String password(CredencialesDTO crendenciales, BindingResult result, Model model, HttpSession session) {
		String body = "Cambiaste tu contrase\u00f1a correctamente.";
		model.addAttribute("credencial", crendenciales);
		
		if (result.hasErrors()) {
			model.addAttribute("errores", Utils.validaciones(result));
		} else {
			if (!crendenciales.getPassword().trim().equals(crendenciales.getConfirmacion().trim())) {
				body = "Las contrase\u00f1as no coinciden.";
			} else if (crendenciales.getPassword().trim().length() < 7 ) {
				body = "Las contraseñas debe tener mínimo 7 caracteres.";
			} else if (!Utils.validaPassword(crendenciales.getPassword().trim())) {
				body =  "Contrase\u00f1a de 8 a 32 caracteres que requiere al menos 3 de 4 (may\u00fasculas y letras min\u00fasculas, n\u00fameros y caracteres especiales) y como m\u00e1ximo 2 caracteres consecutivos iguales.";;
			} else {
				//System.out.println("Entro aqui");
			/*	UserSystem us = (UserSystem) session.getAttribute("username");
				BCryptPasswordEncoder b = new BCryptPasswordEncoder();
				String password = b.encode(crendenciales.getPassword().trim());
				us.setUserPassword(password);
				us = userSystemRepository.save(us);
				SistemaRol sr = new  SistemaRol();
				
				if ( us != null ) {
				   Optional<UserSystemSistemaRol> v = userSystemSistemaRolRepository.findByUsuarioAndIdActivo(us, 1);
				   
				   Optional<SistemaRol> r = sistemaRolRepository.findById(5l);
				   
				   if ( r.isPresent() ) {
					   sr = r.get();
				   }
				   
				   if ( v.isPresent() ) {
					   UserSystemSistemaRol rol = v.get();
					   rol.setRol(sr);
					   userSystemSistemaRolRepository.save(rol);   
				   } else {
					   UserSystemSistemaRol rolNuevo = new  UserSystemSistemaRol();
					   Optional<SistemaUsuario> sistemaUsuario = sistemaUsuarioRepository.findById(1l);
					   SistemaUsuario creador = sistemaUsuario.get();
					   SistemaUsuario actualizador = sistemaUsuario.get();
					   rolNuevo.setActualizador(actualizador);
					   rolNuevo.setCreador(creador);
					   rolNuevo.setRol(sr);
					   rolNuevo.setUsuario(us);
					   userSystemSistemaRolRepository.save(rolNuevo);
				   }
				   
				} */
				UserSystem us = (UserSystem) session.getAttribute("username");
				BCryptPasswordEncoder b = new BCryptPasswordEncoder();
				String password = b.encode(crendenciales.getPassword().trim());
				
				usuarioRepository.cambiarPassUsuario(us.getUsuario(), password);
				return "redirect:/authenticate/login";
			}
		}
		model.addAttribute("mensaje", body);
		return "acceso/password";
	}

	@Override
	public String registro(SocioDTO socio, BindingResult result, RedirectAttributes flash, Model model) {
		Map<String, String> errores = new HashMap<>();
	    List<String> error = new ArrayList<String>();
		String mensaje = "Te has registrado exitosamente";
		
		if (result.hasErrors()) {
			
			result.getFieldErrors().forEach(err -> {
				errores.put(err.getField(),
						"El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
				error.add("El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
				log4j.info("El campo ".concat(err.getField()).concat(" ").concat(err.getDefaultMessage()));
			});

		/*	model.addAttribute("errores", errores);
			model.addAttribute("socio", new SocioDTO());
			return "acceso/registro";*/
			
			Utils.mensajes(model, new SocioDTO(), String.join(". ", error), "danger");
			return "acceso/registro";
		}
		
			Optional<Usuario> u = usuarioRepository.findByUsrUsuarioAndUsrLsvEstatusUsuarios(socio.getMembresia().toUpperCase().trim(), 7);
			
			if ( u.isPresent() ) {
				
				Optional<UserSystem> alta = userSystemRepository.findByUsuario(socio.getMembresia().toUpperCase().trim());
				
				if ( !alta.isPresent() ) {
				
				Usuario us = u.get();
				Optional<ListaValores> lv = listaValoresRepository.findByTablaAndClave("CORREOS AUTORIZADOS SALIDA INTERNET", socio.getMembresia().toUpperCase().trim());
				
				if ( lv.isPresent() ) {
					ListaValores v = lv.get();
					if ( !socio.getCorreo().toUpperCase().trim().equals(v.getDescripcion().toUpperCase().trim()) ) {
						Utils.mensajes(model, new SocioDTO(), "Por favor coloca tu correo correctamente.", "danger");
						log4j.info("Correo no encontrado en Lista de Valores - CORREOS AUTORIZADOS SALIDA INTERNET");
						return "acceso/registro";
					}
					
					Optional<Empleado> k = empleadoRepository.findByEmpIdAndEmpLsvUbicacionUsuarios(us.getUsrEmpId(), us.getUsrLsvUbicacionUsuarios());
					
					if ( k.isPresent() ) {
						Empleado emp = k.get();
						log4j.info("Existe: "+k.isPresent());
						Date birth = emp.getEmpFecNacimiento();
						String pattern = "dd/MM/yyyy";
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
						String nacimiento = simpleDateFormat.format(birth);
						if ( !nacimiento.trim().equals(socio.getFechaNacimiento()) ) {
							Utils.mensajes(model, new SocioDTO(), "Por favor coloca tu fecha de nacimiento correctamente.", "danger");
							log4j.info("Fecha de nacimiento incorrecta");
							return "acceso/registro";
						}
						
						if (!socio.getPassword().trim().equals(socio.getComfirmacion().trim())) {
							mensaje = "Las contrase\u00f1as no coinciden.";
							log4j.info(mensaje);
						} else if (socio.getPassword().trim().length() < 7 ) {
							mensaje = "Las contraseñas debe tener mínimo 7 caracteres.";
							log4j.info(mensaje);
						} else if (!Utils.validaPassword(socio.getPassword().trim())) {
							mensaje =  "Contrase\u00f1a de 8 a 32 caracteres que requiere al menos 3 de 4 (may\u00fasculas y letras min\u00fasculas, n\u00fameros y caracteres especiales) y como m\u00e1ximo 2 caracteres consecutivos iguales.";;
							log4j.info(mensaje);
						} else {
							
							UserSystem sys = new UserSystem();
							sys.setId(socio.getMembresia().toUpperCase().trim());
							sys.setUsuario(socio.getMembresia().toUpperCase().trim());
							BCryptPasswordEncoder b = new BCryptPasswordEncoder();
							String password = b.encode(socio.getPassword().trim());
							sys.setUserPassword(password);
							sys.setAnotacion("A");
							sys.setTipo(1316212);
							sys.setPerfil("0");
							sys.setCorreo(socio.getCorreo().toLowerCase().trim());
							sys.setIntentos(0);
							sys.setPasswordTemporal(password);
							sys.setActivo(0);
							sys = userSystemRepository.save(sys);
							
							sys.setActivo(0);
							sys = userSystemRepository.save(sys);
							SistemaRol sr = new  SistemaRol();
							
							if ( sys != null ) {
								   Optional<UserSystemSistemaRol> ussr = userSystemSistemaRolRepository.findByUsuarioAndIdActivo(sys, 1);
								   Optional<SistemaRol> r = sistemaRolRepository.findById(5l);
								   
								   if ( r.isPresent() ) {
									   sr = r.get();
								   }
								   
								   if ( ussr.isPresent() ) {
									   UserSystemSistemaRol rol = ussr.get();
									   rol.setRol(sr);
									   userSystemSistemaRolRepository.save(rol);   
									   return "redirect:/authenticate/login";
								   } else {
									   UserSystemSistemaRol rolNuevo = new  UserSystemSistemaRol();
									   Optional<SistemaUsuario> sistemaUsuario = sistemaUsuarioRepository.findById(1l);
									   SistemaUsuario creador = sistemaUsuario.get();
									   SistemaUsuario actualizador = sistemaUsuario.get();
									   rolNuevo.setActualizador(actualizador);
									   rolNuevo.setCreador(creador);
									   rolNuevo.setRol(sr);
									   rolNuevo.setUsuario(sys);
									   userSystemSistemaRolRepository.save(rolNuevo);
									  
									   
									   //Envío correo de registro
									   
									   Optional<PlantillaCorreo> p = plantillaCorreoRepository.findById(21l);

										if (p.isPresent()) {
											Optional<ParametroSistema> titulo = parametroSistemaRepository.findById(111l);
											Optional<ParametroSistema> cuerpo = parametroSistemaRepository.findById(112l);
											Optional<ParametroSistema> subject = parametroSistemaRepository.findById(113l);
											
											
											if(!titulo.isPresent()) {
												Utils.mensajes(model, new SocioDTO(), "No se encontró el titulo utilizado en la plantilla de correo para registro de usuario.", "danger");
												log4j.info("No se encontró el titulo utilizado en la plantilla de correo para registro de usuario.");
												return "acceso/registro";	
											}
											
											if(!cuerpo.isPresent()) {
												Utils.mensajes(model, new SocioDTO(), "No se encontró el cuerpo utilizado en la plantilla de correo para registro de usuario.", "danger");
												log4j.info("No se encontró el cuerpo utilizado en la plantilla de correo para registro de usuario.");
												return "acceso/registro";	
											}
											
											if(!subject.isPresent()) {
												Utils.mensajes(model, new SocioDTO(), "No se encontró el asunto utilizado en la plantilla de correo para registro de usuario.", "danger");
												log4j.info("No se encontró el asunto utilizado en la plantilla de correo para registro de usuario.");
												return "acceso/registro";	
											}
											
										
												ParametroSistema cuerpoPlantilla = cuerpo.get();
												ParametroSistema titutloPlantilla = titulo.get();
												
									
												PlantillaCorreo plantillaCorreo = p.get();
												String plantilla = plantillaCorreo.getPlantilla().replace("{titulo}", titutloPlantilla.getVariable());
												plantilla = plantilla.replace("{cuerpo}", cuerpoPlantilla.getVariable());
												
												
												String urlBase = ServletUriComponentsBuilder
												        .fromCurrentContextPath()  // Obtiene el contexto actual
												        .build()
												        .toUriString();
												    
												    log4j.info("URL base: " + urlBase);
												
												    String otp = new String(Utils.generaOTP(5));
										        String url = urlBase + "/register/activeUser?otp=" + otp + "&userName=" + socio.getMembresia().toUpperCase().trim();
												
												plantilla = plantilla.replace("{url}", url);

												
									   try {
										this.sendHtml(username, socio.getCorreo().toLowerCase().trim() , subject.get().getVariable().trim(), plantilla);
										log4j.info("Correo por registro enviado a " + socio.getCorreo().toLowerCase().trim() + " con el asunto: " + subject.get().getVariable().trim() + " y mensaje: " + plantilla);
										Optional<CodigoSeguridad> code = codigoSeguridadRepository.findByClienteAndIdActivo(sys, 1);
										CodigoSeguridad c = new CodigoSeguridad();

										String clave = bCryptPasswordEncoder.encode(otp.trim());
										if (!code.isPresent()) {
											c = CodigoSeguridad.builder().codigo(clave).intento(0).cliente(sys).usuarioAlta(creador)
													.usuarioUltimaActualizacion(actualizador).build();
											c = codigoSeguridadRepository.save(c);
										} else {
											c = code.get();
											c.setCodigo(clave);
											c.setIntento(0);
											c.setUsuarioUltimaActualizacion(actualizador);
											c = codigoSeguridadRepository.save(c);
										}

										if (c == null) {
											 log4j.info("Error al generar el código de seguridad de " + username);
											model.addAttribute("mensaje", "Error al generar tu código de seguridad.");
											model.addAttribute("clase", "danger");
											return "acceso/olvido";
										}

										
										 return "redirect:/authenticate/login?registerEmailSent=true";
									//		return "authenticate/login?registerEmailSent=true";	
										 
									} catch (MessagingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
										 
										}else {
											 log4j.info("No se encontró la plantilla para envío de correo por registro");
										}
										
									   
								   }
								   
								 //  return "redirect:/authenticate/login";
								   
							} else {
								Utils.mensajes(model, new SocioDTO(), "Error al dar de alta al usuario.", "danger");
								log4j.info("Error al dar de alta al usuario.");
								return "acceso/registro";	
							}
							
						}
						
					}else {
						Utils.mensajes(model, new SocioDTO(), "Usuario no encontrado en Empleados. Favor de comunicarse con el administrador del sistema.", "danger");
						log4j.info("Usuario no encontrado en Empleados. Favor de comunicarse con el administrador del sistema..");
						return "acceso/registro";
					}
					
				} else {
					Utils.mensajes(model, new SocioDTO(), "Por favor coloca tu correo correctamente.", "danger");
					log4j.info("Por favor coloca tu correo correctamente.");
					return "acceso/registro";
				}
				
			 }	else {
				 Utils.mensajes(model, new SocioDTO(), "Ya estas registrado, por favor pasa a la sección de recuperación de contraseña.", "danger");
				 log4j.info("Ya estas registrado, por favor pasa a la sección de recuperación de contraseña.");
					return "acceso/registro";
			 }
				
			}else {
				Utils.mensajes(model, new SocioDTO(), "Usuario no encontrado. Favor de comunicarse con el administrador del sistema.", "danger");
				log4j.info("Usuario no encontrado. Favor de comunicarse con el administrador del sistema.");
				return "acceso/registro";	
			}
			
			Utils.mensajes(model, new SocioDTO(), mensaje, "danger");
			return "acceso/registro";
		
	}
	
	@Override
	public String adminRoles(Model model, Principal principal, HttpSession session) {
		model.addAttribute("roles", new coralpagos.com.mx.facil.app.dto.Roles());
		return "usuarios/adminRoles";
	}
	
	@Override
	public String searchRoles(Model model, Roles roles, HttpSession session) {
		log4j.info("*** Search Roles");
		List<coralpagos.com.mx.facil.app.dto.Roles> sistemaRolList  = null;
		String rolBuscado = roles.getRol_Descripcion();
		
		if(rolBuscado == null || rolBuscado.trim().isEmpty()) {
			//sistemaRolList = (List<Roles>) rolesRepository.findAll();
			sistemaRolList =  rolesRepository.findAllRoles();
			for(Roles rol : sistemaRolList) {
				rol.setModulosRoles(findModulesByRole(Integer.valueOf(rol.getRol_Id())));
			}
			
		}else {
			//sistemaRolList = (List<Roles>) rolesRepository.findByRolContainingIgnoreCase(rolBuscado);
			sistemaRolList =  rolesRepository.findRolesByNameContaining(rolBuscado);
			for(Roles rol : sistemaRolList) {
				rol.setModulosRoles(findModulesByRole(Integer.valueOf(rol.getRol_Id())));
			}
		}
		model.addAttribute("sistemaRol", new SistemaRol());
		model.addAttribute("listaRoles", sistemaRolList);
		
		return "usuarios/adminRoles";
	}
	
	@SuppressWarnings("null")
	@Override
	public String saveRoles(Model model, Roles roles, HttpSession session, String usuario) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		List<Roles> sistemaRolList = null;
		String rol = roles.getRol_Descripcion();

		if(rol == null || rol.trim().isEmpty()) {
			//sistemaRolList = (List<Roles>) rolesRepository.findByRolContainingIgnoreCase(rol);
			sistemaRolList =  rolesRepository.findAllRoles();
			for(Roles r : sistemaRolList) {
				r.setModulosRoles(findModulesByRole(Integer.valueOf(r.getRol_Id())));
			}
			
		}else {
			rol = rol.trim().toUpperCase();
			Roles rolExists =rolesRepository.findRoleByName(rol);
			if(rolExists == null) {			
			rolesRepository.saveRole(rol, 0);
			Roles rolBd =  rolesRepository.findRoleByName(rol);
			sistemaRolList = new ArrayList<Roles>();
			sistemaRolList.add(rolBd);
			for(Roles r : sistemaRolList) {
				r.setModulosRoles(findModulesByRole(Integer.valueOf(r.getRol_Id())));
			}
			//sistemaRolList =  rolesRepository.findAllRoles();
			model.addAttribute("sistemaRol", new SistemaRol());
			model.addAttribute("listaRoles", sistemaRolList);
			model.addAttribute("success", true);
			model.addAttribute("msg", "Se ha guardado el rol " +  rol + " correctamente");
			
			datosAuditoriaRepository.saveDataAudit("Usuarios", "saveRoles", "Guardar Rol", null, usuario, request.getRemoteAddr(), "Se ha guardado el rol " +  rol + " correctamente", null, AppConstanst.STATUS_SUCCESSFUL, null, "spInsNuevosRoles");
			
			return "usuarios/adminRoles";
			}else {
				sistemaRolList =  rolesRepository.findAllRoles();
				for(Roles r : sistemaRolList) {
					r.setModulosRoles(findModulesByRole(Integer.valueOf(r.getRol_Id())));
				}
				model.addAttribute("sistemaRol", new SistemaRol());
				model.addAttribute("listaRoles", sistemaRolList);
				model.addAttribute("success", false);
				model.addAttribute("msg", "Error de duplicidad. El rol " + rol + " ya existe en el portal");
				return "usuarios/adminRoles";
			}
		}
		model.addAttribute("sistemaRol", new SistemaRol());
		model.addAttribute("listaRoles", sistemaRolList);
		
		return "usuarios/adminRoles";
	}
	
	public String deleteRole(Model model,  Long id, String usuario) {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
		List<Roles> sistemaRolList = null;
		Roles rolEncontrado = null;
		if(id == null || id==0) {
			sistemaRolList = rolesRepository.findAllRoles();
			for(Roles rol : sistemaRolList) {
				rol.setModulosRoles(findModulesByRole(Integer.valueOf(rol.getRol_Id())));
			}
			model.addAttribute("success", false);
			model.addAttribute("msg", "Error al eliminar rol. No se encontró el rol en el portal");
			
		}else {
			List<Roles>  rolesAllList = rolesRepository.findAllRoles();
			
			Optional<Roles> rolEncontradoBd = rolesAllList.stream()
				    .filter(rol -> rol.getRol_Id().equals(String.valueOf(id)))
				    .findFirst();
			
			if(rolEncontradoBd.isPresent()) {
				rolEncontrado = rolEncontradoBd.get();
			}

			if(rolEncontrado == null) {
			sistemaRolList =  rolesRepository.findAllRoles();
			for(Roles rol : sistemaRolList) {
				rol.setModulosRoles(findModulesByRole(Integer.valueOf(rol.getRol_Id())));
			}
			model.addAttribute("success", false);
			model.addAttribute("msg", "Error al eliminar rol. No se encontró el rol (ID) en el portal");
			model.addAttribute("sistemaRol", new SistemaRol());
			model.addAttribute("listaRoles", sistemaRolList);
			model.addAttribute("roles", rolEncontrado);
			return "usuarios/adminRoles";
			}else {
			rolesRepository.deleteRole(id,rolEncontrado.getRol_Descripcion());
			sistemaRolList = rolesRepository.findAllRoles();
			for(Roles rol : sistemaRolList) {
				rol.setModulosRoles(findModulesByRole(Integer.valueOf(rol.getRol_Id())));
			}
			datosAuditoriaRepository.saveDataAudit("Usuarios", "deleteRole", "Eliminar Rol", null, usuario, request.getRemoteAddr(), "Se ha eliminado el rol " +  rolEncontrado.getRol_Descripcion() + " correctamente", null, AppConstanst.STATUS_SUCCESSFUL, null, "spDelRoles");
			
			model.addAttribute("success", true);
			model.addAttribute("msg", "Se ha eliminado el rol " + rolEncontrado.getRol_Descripcion() + " correctamente");
			}
		}
		model.addAttribute("sistemaRol", new SistemaRol());
		model.addAttribute("listaRoles", sistemaRolList);
		model.addAttribute("roles", rolEncontrado);
		return "usuarios/adminRoles";

	}
	
	@Override
	public List<Roles> loadRoles() {
		 List<Roles> sistemaRolList = rolesRepository.findAllRoles();
		 List<Roles> sistemaRolListFinal = new ArrayList<Roles>();
		for(Roles rol : sistemaRolList) {
			rol.setModulosRoles(findModulesByRole(Integer.valueOf(rol.getRol_Id())));
			if(rol.rol_Activo && !rol.modulosRoles.isEmpty()) {
				sistemaRolListFinal.add(rol);
			}
		}
		
		return sistemaRolListFinal;
	}
	
	public void saveInheritance(Long rolId,  Long rolPadreId,List<Integer> modulosSeleccionados) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	 	String usuario = auth.getName();
		List<Roles>  rolesAllList = rolesRepository.findAllRoles();
			
		Optional<SistemaRol> rolHijo =  sistemaRolRepository.findById(rolId);
		Optional<SistemaRol> rolPadre =  sistemaRolRepository.findById(rolPadreId);
		
		Roles rolHijoEncontrado = null;
		Roles rolPadreEncontrado = null;
		Optional<Roles> rolHijoEncontradoBd = rolesAllList.stream()
			    .filter(rol -> rol.getRol_Id().equals(String.valueOf(rolId)))
			    .findFirst();
		
		rolHijoEncontrado = rolHijoEncontradoBd.get();
		
		Optional<Roles> rolPadreEncontradoBd = rolesAllList.stream()
			    .filter(rol -> rol.getRol_Id().equals(String.valueOf(rolPadreId)))
			    .findFirst();
		
		
		rolPadreEncontrado = rolPadreEncontradoBd.get();
		
		if(rolHijoEncontrado==null) {
			throw new EntityNotFoundException("Rol no encontrado");
		}
		
		if(rolPadreEncontrado==null) {
			throw new EntityNotFoundException("Rol padre no encontrado");
		}
		
		 List<Modulos> modulosList = getModules();
		 		 
		 String nombresSeparadosPorComas = modulosList.stream()
				    .filter(modulo -> modulosSeleccionados.contains(Integer.parseInt(modulo.getMdl_id())))
				    .map(Modulos::getMdl_nombre)
				    .collect(Collectors.joining(", "));
		 
		 String idsSeparadosPorComas = modulosSeleccionados.stream()
			        .map(String::valueOf) // Convertir cada Integer a String
			        .collect(Collectors.joining(", "));
		 		
		rolesRepository.saveInheritance(Integer.valueOf(rolHijoEncontrado.getRol_Id()),Integer.valueOf(rolPadreEncontrado.getRol_Id()),rolHijoEncontrado.getRol_Descripcion(),rolPadreEncontrado.getRol_Descripcion(),idsSeparadosPorComas);
		
		datosAuditoriaRepository.saveDataAudit("Usuarios", "saveInheritance", "Heredar permisos Rol", null, usuario, request.getRemoteAddr(), "El rol " + rolHijoEncontrado.getRol_Descripcion() +
				" hereda de " + rolPadreEncontrado.getRol_Descripcion() + " correctamente", idsSeparadosPorComas, AppConstanst.STATUS_SUCCESSFUL, null, "spInsRolesModulosV2");
 
	}
	
	public void enableDisableRole(int  rolId,  Boolean activo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	 	String usuario = auth.getName();

		String actDescStr = activo ? "activar" : "desactivar";
		List<Roles> sistemaRolList = rolesRepository.findAllRoles();
		Roles rolEncontrado = null;
		Optional<Roles> rolEncontradoBd = sistemaRolList.stream()
			    .filter(rol -> rol.getRol_Id().equals(String.valueOf(rolId)))
			    .findFirst();
		
			rolEncontrado = rolEncontradoBd.get();
			rolesRepository.enableDisableRole(activo.booleanValue(), rolId, rolEncontrado.getRol_Descripcion());
			datosAuditoriaRepository.saveDataAudit("Usuarios", "enableDisableRole", actDescStr.substring(0, 1).toUpperCase() + actDescStr.substring(1)  + " Rol", null, usuario, request.getRemoteAddr(), "Se " + actDescStr.substring(0, actDescStr.length() - 1) + " el rol " + rolEncontrado.getRol_Descripcion() + " correctamente", null, AppConstanst.STATUS_SUCCESSFUL, null, "spActivaDesactivaRoles");
			
 
	}

	public 	List<UsuariosRoles> getUsersByRole(int  rolId) {
		List<Roles> sistemaRolList = rolesRepository.findAllRoles();
		Roles rolEncontrado = null;
		Optional<Roles> rolEncontradoBd = sistemaRolList.stream()
			    .filter(rol -> rol.getRol_Id().equals(String.valueOf(rolId)))
			    .findFirst();
		
			rolEncontrado = rolEncontradoBd.get();
		return	rolesRepository.getUsersByRole(rolId);
 
	}
	
	public 	List<ModulosRoles> findModulesByRole(int  rolId) {
		return	rolesRepository.findModulesByRole(rolId);
 
	}
	
	public List<Modulos> getModules() {
		return rolesRepository.getModules();
	}
	
	public void editModulesRoles(Long rolId, List<Integer> modulosSeleccionados) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	 	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	 	String usuario = auth.getName();
		List<Roles>  rolesAllList = rolesRepository.findAllRoles();
			
		Optional<SistemaRol> rolHijo =  sistemaRolRepository.findById(rolId);
		
		Roles rolHijoEncontrado = null;
		Optional<Roles> rolHijoEncontradoBd = rolesAllList.stream()
			    .filter(rol -> rol.getRol_Id().equals(String.valueOf(rolId)))
			    .findFirst();
		
		rolHijoEncontrado = rolHijoEncontradoBd.get();
				
		
		if(rolHijoEncontrado==null) {
			throw new EntityNotFoundException("Rol no encontrado");
		}
				
		 List<Modulos> modulosList = getModules();
		 		 
		 String nombresSeparadosPorComas = modulosList.stream()
				    .filter(modulo -> modulosSeleccionados.contains(Integer.parseInt(modulo.getMdl_id())))
				    .map(Modulos::getMdl_nombre)
				    .collect(Collectors.joining(", "));
		 
		 String idsSeparadosPorComas = modulosSeleccionados.stream()
			        .map(String::valueOf) // Convertir cada Integer a String
			        .collect(Collectors.joining(", "));
		 		
		rolesRepository.editModulesRoles(Integer.valueOf(rolHijoEncontrado.getRol_Id()),rolHijoEncontrado.getRol_Descripcion(),idsSeparadosPorComas);
		
		datosAuditoriaRepository.saveDataAudit("Usuarios", "editModulesRoles", "Editar permisos Rol", null, usuario, request.getRemoteAddr(), "Se modifica el rol " + rolHijoEncontrado.getRol_Descripcion() 
				 + " correctamente", idsSeparadosPorComas, AppConstanst.STATUS_SUCCESSFUL, null, "spInsRolesModulosV2");
 
	}


	public boolean hasAccess(List<ModulosRoles> modulos, String claveBuscada) {
	    for (ModulosRoles modulo : modulos) {
	        if (claveBuscada.equals(modulo.getMdl_clave())) {
	            return true;
	        }
	        if (modulo.getHijos() != null && hasAccess(modulo.getHijos(), claveBuscada)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public ModulosRoles buscarModuloActivado(List<ModulosRoles> modulos, String claveBuscada) {
		
		if (modulos == null) {
	        return null;
	    }
	    for (ModulosRoles modulo : modulos) {
	    	
	        if (claveBuscada.equals(modulo.getRom_mdl_id())) {
	            return modulo;
	        }
	        ModulosRoles moduloEncontrado = buscarModuloActivado(modulo.getHijos(), claveBuscada);
	        if (moduloEncontrado != null) {
	            return moduloEncontrado;
	        }
	    }
	    return null;
	}
	
	@Override
	public String adminReportes(Model model, Principal principal, HttpSession session) {
		model.addAttribute("roles", new coralpagos.com.mx.facil.app.dto.Roles());
		return "usuarios/adminReportes";
	}
	
	@Override
	public String searchRolesReportes(Model model, Roles roles, HttpSession session) {
		log4j.info("*** Search Roles");
		List<coralpagos.com.mx.facil.app.dto.Roles> sistemaRolList  = null;
		String rolBuscado = roles.getRol_Descripcion();
		
		if(rolBuscado == null || rolBuscado.trim().isEmpty()) {
			//sistemaRolList = (List<Roles>) rolesRepository.findAll();
			sistemaRolList =  rolesRepository.findAllRoles();
			for(Roles rol : sistemaRolList) {
				rol.setModulosRoles(findModulesByRole(Integer.valueOf(rol.getRol_Id())));
			}
			
		}else {
			//sistemaRolList = (List<Roles>) rolesRepository.findByRolContainingIgnoreCase(rolBuscado);
			sistemaRolList =  rolesRepository.findRolesByNameContaining(rolBuscado);
			for(Roles rol : sistemaRolList) {
				rol.setModulosRoles(findModulesByRole(Integer.valueOf(rol.getRol_Id())));
			}
		}
		model.addAttribute("sistemaRol", new SistemaRol());
		model.addAttribute("listaRoles", sistemaRolList);
		
		return "usuarios/adminReportes";
	}
	
	@Override
	public List<TiposReportes> obtieneTiposReportes() {
		return administracionReportesRepository.obtieneTiposReportes();
	}

	@Override
	public List<TiposReportes> getReportesRol(int  rolId) {
		return administracionReportesRepository.getReportesRol(rolId);
	}
	
	@Override
	public void updateReportesRol(int idRol, String idReportes, String usuario) {
		administracionReportesRepository.guardarReportesRoles(idRol, idReportes, usuario);
	}
	
	@Override
	public String registerActiveUser(String otp, String userName, Model model, HttpSession session) {
		log4j.info("Inicio de activacion del usuario " + userName + " con el codigo: " + otp);
		try {
		Optional<UserSystem> usOpt =  userSystemRepository.findByUsuario(userName);
		if(!usOpt.isPresent()) {
			log4j.info("El usuario " + userName + " no existe para la activacion de nuevo usuario");
			model.addAttribute("mensaje", "El usuario no existe");
			model.addAttribute("clase", "danger");
			return "redirect:/authenticate/login?error=El%20usuario%20no%20existe";
		}
		UserSystem us = usOpt.get();
		Optional<CodigoSeguridad> code = codigoSeguridadRepository.findByClienteAndIdActivo(us, 1);
		BCryptPasswordEncoder b = new BCryptPasswordEncoder();
		
		if ( code.isPresent() ) {
			CodigoSeguridad x = code.get();
			String codeOtpBD = x.getCodigo();
		
			if ( b.matches(otp, codeOtpBD) ) {
				x.setIdActivo(2);
				us.setActivo(1);
				model.addAttribute("credencial", new CredencialesDTO());
				codigoSeguridadRepository.save(x);
				userSystemRepository.save(us);
				log4j.info("La activacion de usuario nuevo " + userName + " con el codigo de seguridad " + otp + " fue existosa");
				return "redirect:authenticate/login?activeUserSuccess=true";
			} else {
				log4j.info("El codigo de seguridad " + otp + " para el usuario " + userName + " no coincide con el registrado en el sistema");
				 model.addAttribute("mensaje", "El%200codigo%20de%20seguridad%20no%20coincide%20con%20el%20registrado%20en%20el%20sistema");
				 model.addAttribute("clase", "danger");
				 return "redirect:authenticate/login?error=El%20codigo%20de%20seguridad%20no%20coincide%20con%20el%20registrado%20en%20el%20sistema";
			}
			
		}  else {
			log4j.info("El codigo de seguridad " + otp + " para el usuario " + userName + " es incorrecto. No se encuentra en la tabla");
			 model.addAttribute("mensaje", "El%20codigo%20de%20seguridad%20es%20incorrecto");
			 model.addAttribute("clase", "danger");
			 return "redirect:authenticate/login?error=El%20codigo%20de%20seguridad%20es%20incorrecto";
		}
	  }catch(Exception ex) {
		  ex.printStackTrace();
		  log4j.error("Exception",ex);
		  model.addAttribute("mensaje", "Excepción%20Favor%20de%comunicarse%con%soporte");
		  model.addAttribute("clase", "danger");
		 return "redirect:authenticate/login?error=Excepción.%20Favor%20de%comunicarse%con%soporte";
		}
	  

//		return "redirect:authenticate/login";
	}
}
