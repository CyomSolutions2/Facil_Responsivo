package coralpagos.com.mx.facil.app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import coralpagos.com.mx.orm.facil.SistemaUsuario;

public interface SistemaUsuarioRepository extends CrudRepository<SistemaUsuario, Long> {
	 SistemaUsuario findOneByUsuario(String usuario);
}
