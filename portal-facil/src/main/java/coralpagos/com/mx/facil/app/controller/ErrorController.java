package coralpagos.com.mx.facil.app.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
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
import coralpagos.com.mx.facil.app.dto.UsuariosRoles;
import coralpagos.com.mx.facil.app.service.UsuarioService;
import coralpagos.com.mx.facil.app.util.AppConstanst;
import coralpagos.com.mx.orm.facil.SistemaRol;
import coralpagos.com.mx.util.facil.dto.CredencialesDTO;
import coralpagos.com.mx.util.facil.dto.SocioDTO;

@Controller
public class ErrorController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	private Logger log4j = LogManager.getLogger(ErrorController.class);
	
	
	@GetMapping("error/error403")
	public String adminRoles( Model model, Principal principal, HttpSession session ) {
		
	    
	    return "/error/error403";
	}
	    
	
}