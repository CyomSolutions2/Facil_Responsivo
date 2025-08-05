package coralpagos.com.mx.facil.app.controller;

import java.security.Principal;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import coralpagos.com.mx.facil.app.service.UsuarioService;
import coralpagos.com.mx.facil.app.service.impl.UsuarioServiceImpl;
import coralpagos.com.mx.util.facil.dto.CredencialesDTO;
import coralpagos.com.mx.util.facil.dto.SocioDTO;

@Controller
public class HomeController {
	
	@Autowired
	private UsuarioService usuarioService;
	
    private Logger log4j = LogManager.getLogger(HomeController.class);
	
	@GetMapping("/")
	public String home() {
		return "redirect:/authenticate/login";
	}
	
	@GetMapping("/registro")
	public String registro( Model model ) {
		return usuarioService.registro( model );
	}
	
	@PostMapping("/registro")
	public String registroPost(@Valid SocioDTO socio, BindingResult result, RedirectAttributes flash, Model model) {	
		return usuarioService.registro(socio, result, flash, model);
	}
	
	@GetMapping("/authenticate/login")
	public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
			RedirectAttributes flash, Principal principal, HttpSession session ) {
		return usuarioService.authenticacion(error, logout, flash, principal, session);
	}
	
	@GetMapping("/authenticate/olvido")
	public String olvido( Model model, Principal principal) {
		return usuarioService.olvido(model, principal);
	}
	
	@GetMapping("/authenticate/empleado")
	public String socio( Model model, Principal principal, HttpSession session ) {
		return usuarioService.datosEmpleado(model, principal, session);
	}
	
	@GetMapping("/authenticate/reset")
	public String reset( @RequestParam(required = true) String correo,  @RequestParam(required = true) String membresia, Model model, HttpSession session) throws MessagingException {
		return usuarioService.reset(correo, membresia, model, session);
	}
	
	@GetMapping("/authenticate/code")
	public String otp( @RequestParam(required = true) String otp, Model model, HttpSession session) {
		return usuarioService.otp(otp, model, session);
	}
	
	@PostMapping("/authenticate/password")
	public String password( @Valid CredencialesDTO credencial, BindingResult result, Model model, HttpSession session) throws MessagingException {
		return usuarioService.password(credencial, result, model, session);
	}
	
	@GetMapping("/home")
	public String home( Model model, Principal principal, HttpSession session ) {
		return usuarioService.datosEmpleado(model, principal, session);
	}	
	
    @GetMapping("/authenticate/keep-alive")
    @ResponseBody
    public String keepSessionAlive(HttpSession session) {
        session.setMaxInactiveInterval(10 * 60); // Reinicia tiempo (opcional)
        return "OK";
    }	
	
}