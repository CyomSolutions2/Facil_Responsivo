package coralpagos.com.mx.facil.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import coralpagos.com.mx.orm.facil.Membresia;

public interface MembresiaRepository extends CrudRepository<Membresia, String> {
	
	Optional<Membresia> findByMemMembresia( String memMembresia );

}
