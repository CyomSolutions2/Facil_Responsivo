package coralpagos.com.mx.facil.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import coralpagos.com.mx.orm.facil.UserSystem;
import coralpagos.com.mx.orm.facil.UserSystemSistemaRol;

public interface UserSystemSistemaRolRepository extends CrudRepository<UserSystemSistemaRol, Long>{
	
	Optional<UserSystemSistemaRol> findByUsuarioAndIdActivo( UserSystem usuario, Integer idActivo );

}
