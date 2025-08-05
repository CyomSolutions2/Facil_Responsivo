package coralpagos.com.mx.facil.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Seguridad {

	@Autowired
	private LoginPersonalizado loginPersonalizado;

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(requests -> requests

				// las vistas que públicas no requieren autenticación
				.antMatchers("/", "/registro", "/authenticate/login", "/socios/**", "/authenticate/olvido", "/authenticate/reset", "/authenticate/code", "/authenticate/password","/register/activeUser").permitAll()
				// Asignar permisos a URLs por ROLES
				.antMatchers("/panel/**").hasAnyAuthority("ROLE_FACIL")
				.antMatchers("/usuarios/**").permitAll()
				.antMatchers("/repEspeciales/**").permitAll()
				.antMatchers("/usuarios/roles/**").permitAll()
				
				.antMatchers("/pago/**").hasAnyAuthority("ROLE_FACIL", "ROLE_FACIL_ADMIN")
				// .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

				// se hacen las configuraciones generales
				.anyRequest().authenticated())
				.formLogin(
						login -> login.successHandler(loginPersonalizado).loginPage("/authenticate/login").permitAll())
				.logout(logout -> logout.permitAll())
				.sessionManagement(session -> session
		                .invalidSessionUrl("/session-expired")
		                .maximumSessions(1)
		            );
				
				

		return http.build();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().antMatchers("/images/**", "/js/**", "/css/**", "/publico/**", "/login/**",
				"/portal/**", "/registro/**", "/interno/**", "/negocio/**");
	}

}
