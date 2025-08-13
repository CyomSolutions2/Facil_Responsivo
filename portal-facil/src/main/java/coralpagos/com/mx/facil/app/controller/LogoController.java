package coralpagos.com.mx.facil.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import coralpagos.com.mx.facil.app.util.AppConstanst;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@RestController
public class LogoController {

    // Ruta absoluta de la imagen en Windows
    private final Path logoPath = Paths.get(
        AppConstanst.LOGO_SRC
    );

    @GetMapping("/logo-path")
    public String getLogoAsBase64() {
        try {
            if (Files.exists(logoPath)) {
                byte[] fileContent = Files.readAllBytes(logoPath);
                String base64Image = Base64.getEncoder().encodeToString(fileContent);
                return "data:image/png;base64," + base64Image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ""; // Vacío si no existe
    }
}