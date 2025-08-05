package coralpagos.com.mx.facil.app.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ExcelService {
	
	byte[] generarReporteExcel(Map<String, Object> resultadoSP, String sheetName) throws IOException; 
	
	byte[] generarReporteExcel(List<Map<String, Object>> datos, List<String> columnas, String columnasSeleccionadas, String sheetName) throws IOException;

}
