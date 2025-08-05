package coralpagos.com.mx.facil.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({
	"coralpagos.com.mx.util.facil",
	"coralpagos.com.mx.orm.facil",
	"coralpagos.com.mx.facil.app.dto"
})
public class PortalFacilApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalFacilApplication.class, args);
	}

}
