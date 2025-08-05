package coralpagos.com.mx.facil.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import coralpagos.com.mx.orm.facil.UserSystem;

public interface UserSystemRepository extends CrudRepository<UserSystem, String>{
	
    Optional<UserSystem> findByCorreoOrUsuarioAndActivo(String correo, String usuario, Integer activo);
    
    Optional<UserSystem> findByUsuarioAndActivo(String usuario, Integer activo);
    
    Optional<UserSystem> findByUsuario(String usuario);
    
    Optional<UserSystem> findByCorreoAndUsuarioAndActivo(String correo, String usuario, Integer activo);
    
    Optional<UserSystem> findByCorreoAndUsuario(String correo, String usuario);
    
    Optional<UserSystem> findByCorreo(String correo);
    
    List<UserSystem> findByCorreoAndActivo(String correo, Integer activo);
    
    @Query(value="select top 1 * from USERSYSTEM where correo = :correo order by FECHA_REGISTRO desc", nativeQuery = true)
	public UserSystem recuperaUltmimoUsuario(@Param("correo") String correo);
    
}