package coralpagos.com.mx.facil.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import coralpagos.com.mx.orm.facil.CodigoSeguridad;
import coralpagos.com.mx.orm.facil.UserSystem;


public interface CodigoSeguridadRepository extends CrudRepository<CodigoSeguridad, Long>{

	Optional<CodigoSeguridad> findByClienteAndIdActivo( UserSystem cliente, int idActivo );
	
}
