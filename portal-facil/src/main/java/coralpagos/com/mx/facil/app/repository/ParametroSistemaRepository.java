package coralpagos.com.mx.facil.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import coralpagos.com.mx.orm.facil.ParametroSistema;

public interface ParametroSistemaRepository extends CrudRepository<ParametroSistema, Long> {
	
	Optional<ParametroSistema> findByIdAndActivo(Long id, Integer activo);

}
