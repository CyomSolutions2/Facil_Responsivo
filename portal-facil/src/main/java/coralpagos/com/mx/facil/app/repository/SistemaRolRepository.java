package coralpagos.com.mx.facil.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import coralpagos.com.mx.orm.facil.SistemaRol;

public interface SistemaRolRepository extends CrudRepository<SistemaRol, Long> {
	List<SistemaRol> findByRolContainingIgnoreCase(String rol);
	SistemaRol findByRol(String rol);

}
