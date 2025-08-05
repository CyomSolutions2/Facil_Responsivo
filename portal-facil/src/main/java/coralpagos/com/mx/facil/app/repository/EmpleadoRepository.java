package coralpagos.com.mx.facil.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import coralpagos.com.mx.orm.facil.Empleado;

public interface EmpleadoRepository extends CrudRepository<Empleado, Long> {

	Optional<Empleado> findByEmpIdAndEmpLsvUbicacionUsuarios( Long empId, Integer empLsvUbicacionUsuarios );
	
}
