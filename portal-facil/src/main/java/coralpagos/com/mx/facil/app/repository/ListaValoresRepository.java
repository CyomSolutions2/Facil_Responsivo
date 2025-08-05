package coralpagos.com.mx.facil.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import coralpagos.com.mx.orm.facil.ListaValores;
import coralpagos.com.mx.orm.facil.sp.SpDatosListasValoresXNombre;

public interface ListaValoresRepository extends CrudRepository<ListaValores, Long> {
	
	@Query(value="{CALL spDatosListasValoresXNombre(:tabla,:descripcion)}", nativeQuery = true)
    List<SpDatosListasValoresXNombre> spDatosListasValoresXNombre( @Param("tabla") String tabla, @Param("descripcion") String descripcion );
	
	Optional<ListaValores> findByTablaAndClave( String tabla, String clave );
	
}
