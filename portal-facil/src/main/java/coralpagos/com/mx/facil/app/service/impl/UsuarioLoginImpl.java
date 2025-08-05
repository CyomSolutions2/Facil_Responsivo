package coralpagos.com.mx.facil.app.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import coralpagos.com.mx.facil.app.repository.MembresiaRepository;
import coralpagos.com.mx.facil.app.service.UsuarioService;
import coralpagos.com.mx.orm.facil.Membresia;
import coralpagos.com.mx.orm.facil.SistemaRol;
import coralpagos.com.mx.orm.facil.UserSystem;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Component
public class UsuarioLoginImpl implements UserDetailsService {
	
	@Autowired
	private UsuarioService usuariosService;
	
	@Autowired
	private MembresiaRepository membresiaRepository;
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		Optional<UserSystem> user = this.usuariosService.findByUsuario(username);
		if( !user.isPresent() ) {
			session.setAttribute("mensaje", 1);
			throw new UsernameNotFoundException("El E-Mail: " + username + " no existe en el sistema!");
		} else {
			
			UserSystem userSystem = user.get();
			session.setAttribute("userSystem", userSystem);
			int intento = userSystem.getIntentos();
			if ( intento > 3 ) {
				session.setAttribute("mensaje", 2);
				throw new UsernameNotFoundException("El E-Mail: " + username + " no existe en el sistema!");
			} else if ( userSystem.getActivo() == null ) {
				session.setAttribute("mensaje", 7);
				throw new UsernameNotFoundException("El E-Mail: " + username + " no existe en el sistema!");
			} else if ( userSystem.getActivo().equals(2) ) {
				session.setAttribute("mensaje", 3);
				throw new UsernameNotFoundException("El E-Mail: " + username + " no existe en el sistema!");
			} else if ( userSystem.getActivo().equals(3) ) {
				session.setAttribute("mensaje", 4);
				throw new UsernameNotFoundException("El E-Mail: " + username + " no existe en el sistema!");
			} else if ( userSystem.getActivo().equals(4) ) {
				session.setAttribute("mensaje", 5);
				throw new UsernameNotFoundException("El E-Mail: " + username + " no existe en el sistema!");
	        } else if ( userSystem.getActivo().equals(5) ) {
	        	session.setAttribute("mensaje", 6);
	        	throw new UsernameNotFoundException("El E-Mail: " + username + " no existe en el sistema!");
			}
			
			Optional<Membresia> obj = membresiaRepository.findByMemMembresia(username);
			
			if ( obj.isPresent() ) {
				Membresia m = obj.get();
				
				if ( !m.getMemLsvEstatusMembresia().equals(269) ) {
					session.setAttribute("mensaje", 9);
		        	throw new UsernameNotFoundException("El E-Mail: " + username + " no existe en el sistema!");
				}
				
			}
			
			userSystem.setIntentos(0);
			usuariosService.guardar(userSystem);
			
		}
		
		//if( !user.isPresent() ) {
			//throw new UsernameNotFoundException("El E-Mail: " + username + " no existe en el sistema!");
		//}		
		UserSystem usuario = user.get();
		//configuramos los autorities
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(SistemaRol role: usuario.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getRol()));
		}
		
		//System.out.println(authorities);
		if(authorities.isEmpty()) {
			session.setAttribute("mensaje", 8);
			throw new UsernameNotFoundException("Error en el Login: E-Mail '" + username + "' no tiene roles asignados!");
		}
		
		//retornamos el userDetail con los datos del usuario logueado
		return new User(usuario.getUsuario(), usuario.getUserPassword(), true, true, true, true, authorities);
	}
	
}
