package coralpagos.com.mx.facil.app.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginPersonalizado extends SimpleUrlAuthenticationSuccessHandler{
	
 	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
 		
 		response.sendRedirect("redirect:/authenticate/socios");
 		super.onAuthenticationSuccess(request, response, chain, authentication);
 		
 	}
}