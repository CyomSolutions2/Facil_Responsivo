package coralpagos.com.mx.facil.app.service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import coralpagos.com.mx.facil.app.dto.Modulos;
import coralpagos.com.mx.facil.app.dto.ModulosRoles;
import coralpagos.com.mx.facil.app.dto.Roles;
import coralpagos.com.mx.facil.app.dto.TiposReportes;
import coralpagos.com.mx.facil.app.dto.UsuariosRoles;
import coralpagos.com.mx.orm.facil.UserSystem;
import coralpagos.com.mx.util.facil.dto.CredencialesDTO;
import coralpagos.com.mx.util.facil.dto.SocioDTO;

public interface UsuarioService {

	Optional<UserSystem> findByUsuario(String usuario);

	UserSystem guardar(UserSystem entity);

	String authenticacion(String error, String logout, RedirectAttributes flash, Principal principal, HttpSession session);

	String olvido(Model model, Principal principal);

	String registro(Model model);

	String datosEmpleado(Model model, Principal principal, HttpSession session);

	String reset(String email, String usuario, Model model, HttpSession session) throws MessagingException;

	String otp(String otp, Model model, HttpSession session);

	String password(CredencialesDTO credencial, BindingResult result, Model model, HttpSession session);

	String registro(SocioDTO socio, BindingResult result, RedirectAttributes flash, Model model);
	
	String adminRoles(Model model, Principal principal, HttpSession session);
	
	String searchRoles(Model model, Roles roles, HttpSession session);
	
	String saveRoles(Model model, Roles roles, HttpSession session, String username);
	
	String deleteRole(Model model, Long id, String username);
	
	List<Roles> loadRoles();
	
	void saveInheritance(Long rolId, Long rolPadreId, List<Integer> modulosSeleccionados);
	
	void enableDisableRole(int  rolId,  Boolean activo);
	
	List<UsuariosRoles> getUsersByRole(int  rolId);
	
	List<ModulosRoles> findModulesByRole(int  rolId);
	
	List<Modulos> getModules();
	
	void editModulesRoles(Long rolId, List<Integer> modulosSeleccionados);
	
	public boolean hasAccess(List<ModulosRoles> modulos, String claveBuscada);
	
	String adminReportes(Model model, Principal principal, HttpSession session);
	
	String searchRolesReportes(Model model, Roles roles, HttpSession session);
	
	List<TiposReportes> obtieneTiposReportes();
	
	List<TiposReportes> getReportesRol(int  rolId);
	
	void updateReportesRol(int idRol, String idReportes, String usuario);
	
	public ModulosRoles buscarModuloActivado(List<ModulosRoles> modulos, String claveBuscada);
	
	String registerActiveUser(String otp, String userName,  Model model, HttpSession session);
	
}
