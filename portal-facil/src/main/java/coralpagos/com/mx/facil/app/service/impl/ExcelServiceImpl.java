package coralpagos.com.mx.facil.app.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.poi.ss.usermodel.Font;

import coralpagos.com.mx.facil.app.service.ExcelService;
import coralpagos.com.mx.facil.app.util.AppConstanst;

@Service
@Transactional
public class ExcelServiceImpl implements ExcelService{
	
	private Logger log4j = LogManager.getLogger(ExcelServiceImpl.class);
	
	public byte[] generarReporteExcel(Map<String, Object> resultadoSP, String sheetName) throws IOException {
	    if (resultadoSP == null || resultadoSP.isEmpty()) {
	        throw new IllegalArgumentException("El resultado del stored procedure está vacío");
	    }

	    @SuppressWarnings("unchecked")
	    List<String> columnas = (List<String>) resultadoSP.get("columns");
	    @SuppressWarnings("unchecked")
	    List<Map<String, Object>> datos = (List<Map<String, Object>>) resultadoSP.get("data");

	    if (columnas == null || datos == null) {
	        throw new IllegalArgumentException("El resultado no contiene la estructura esperada");
	    }

	    try (Workbook workbook = new XSSFWorkbook();
	         ByteArrayOutputStream out = new ByteArrayOutputStream()) {
	        
	        Sheet hoja = workbook.createSheet(validateSheetName(sheetName));

	        // Crear estilo para la cabecera
	        CellStyle estiloCabecera = workbook.createCellStyle();
	        Font fuenteCabecera = workbook.createFont();
	        fuenteCabecera.setBold(true);
	        estiloCabecera.setFont(fuenteCabecera);

	        // Crear fila de cabecera
	        Row filaCabecera = hoja.createRow(0);
	        for (int i = 0; i < columnas.size(); i++) {
	            Cell celda = filaCabecera.createCell(i);
	            celda.setCellValue(columnas.get(i));
	            celda.setCellStyle(estiloCabecera);
	        }

	        // Llenar datos
	        int numFila = 1;
	        for (Map<String, Object> fila : datos) {
	            Row filaDatos = hoja.createRow(numFila++);
	            for (int i = 0; i < columnas.size(); i++) {
	                String columna = columnas.get(i);
	                Object valor = fila.get(columna);
	                Cell celda = filaDatos.createCell(i);
	                
	                if (valor != null) {
	                    if (valor instanceof Number) {
	                        celda.setCellValue(((Number) valor).doubleValue());
	                    } else if (valor instanceof Boolean) {
	                        celda.setCellValue((Boolean) valor);
	                    } else if (valor instanceof Date) {
	                        celda.setCellValue((Date) valor);
	                    } else {
	                        celda.setCellValue(valor.toString());
	                    }
	                }
	            }
	        }

	        // Autoajustar el ancho de las columnas
	        for (int i = 0; i < columnas.size(); i++) {
	            hoja.autoSizeColumn(i);
	        }

	        workbook.write(out);
	        return out.toByteArray();
	    }
	}
	
	public static String validateSheetName(String sheetName) {		
		String sheetNameOk = null;
		String invalidCharsRegex = "[:\\\\/?*\\[\\]]";
		
		if(sheetName == null) {
			sheetNameOk = AppConstanst.SHEET_NAME_DEFAULT_REPORT;
			return sheetNameOk;
		}
		
		 
	     sheetNameOk = sheetName.replaceAll(invalidCharsRegex, "");
	     
	     if (sheetNameOk.length() > 31) {
	    	 sheetNameOk = sheetNameOk.substring(0, 31);
	     }
	     
	     if (sheetNameOk.trim().isEmpty()) {
	    	 sheetNameOk = AppConstanst.SHEET_NAME_DEFAULT_REPORT;
				return sheetNameOk;
	     }
		
		
		return sheetNameOk;
		
	}
	
	
	@SuppressWarnings("resource")
	public byte[] generarReporteExcel(List<Map<String, Object>> datos, List<String> todasLasColumnas,
			String columnasSeleccionadas, String sheetName) throws IOException {
		
		columnasSeleccionadas = columnasSeleccionadas.replaceAll("[\\[\\]\"]", "");

		List<String> columnasSeleccionadasLista = Arrays.asList(columnasSeleccionadas.split(","));
		
		  ObjectMapper mapper = new ObjectMapper();
          String jsonColumnasSeleccionadasLista = null;;
			try {
				jsonColumnasSeleccionadasLista = mapper.writeValueAsString(columnasSeleccionadasLista);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				log4j.error("Exception: ", e);
				e.printStackTrace();
			}
			
			log4j.info("sheetName - " + sheetName + " - jsonColumnasSeleccionadasLista: " + jsonColumnasSeleccionadasLista);
			
			   mapper = new ObjectMapper();
	          String jsonTodasLasColumnas = null;;
				try {
					jsonTodasLasColumnas = mapper.writeValueAsString(todasLasColumnas);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					log4j.error("Exception: ", e);
					
				}
					
			
			log4j.info("sheetName - " + sheetName + " - jsonTodasLasColumnas: " + jsonTodasLasColumnas);
			
			
			

// 3. Verificar coincidencias exactas
		List<String> columnasAIncluir = new ArrayList<>();
		for (String colSeleccionada : columnasSeleccionadasLista) {
			if (todasLasColumnas.contains(colSeleccionada)) {
// Recuperar el nombre original de la columna (conservando mayúsculas y espacios)
				int index = todasLasColumnas.indexOf(colSeleccionada);
				columnasAIncluir.add(todasLasColumnas.get(index));
			}
		}

// 4. Si no hay coincidencias, usar todas las columnas
		if (columnasAIncluir.isEmpty()) {
			columnasAIncluir = new ArrayList<>(todasLasColumnas);
			log4j.info(
					"Advertencia: Ninguna columna seleccionada coincide con las columnas disponibles. Mostrando todas.");
		}
		

		   mapper = new ObjectMapper();
       String jsonColumnasAIncluir = null;;
			try {
				jsonColumnasAIncluir = mapper.writeValueAsString(columnasAIncluir);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				log4j.error("Exception: ", e);
				
			}
				
		
		log4j.info("sheetName - " + sheetName + " - jsonColumnasAIncluir: " + jsonColumnasAIncluir);

// 5. Crear el libro de Excel
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

			Sheet sheet = workbook.createSheet(sheetName != null ? validateSheetName(sheetName) : "Reporte");

// 6. Crear estilo para encabezados
			CellStyle headerStyle = workbook.createCellStyle();
			Font font = workbook.createFont();
			font.setBold(true);
			headerStyle.setFont(font);

// 7. Crear fila de encabezados
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < columnasAIncluir.size(); i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columnasAIncluir.get(i));
				cell.setCellStyle(headerStyle);
			}

// 8. Llenar los datos
			int filaNum = 1;
			for (Map<String, Object> filaDatos : datos) {
				Row fila = sheet.createRow(filaNum++);

				for (int i = 0; i < columnasAIncluir.size(); i++) {
					String columna = columnasAIncluir.get(i);
					Object valor = filaDatos.get(columna);

					if (valor != null) {
						Cell celda = fila.createCell(i);
// Manejo de diferentes tipos de datos
						if (valor instanceof Number) {
							celda.setCellValue(((Number) valor).doubleValue());
						} else if (valor instanceof Date) {
							celda.setCellValue((Date) valor);
							CellStyle dateStyle = workbook.createCellStyle();
							CreationHelper createHelper = workbook.getCreationHelper();
							dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
							celda.setCellStyle(dateStyle);
						} else if (valor instanceof Boolean) {
							celda.setCellValue((Boolean) valor);
						} else {
							celda.setCellValue(valor.toString());
						}
					}
				}
			}

// 9. Autoajustar columnas
			for (int i = 0; i < columnasAIncluir.size(); i++) {
				sheet.autoSizeColumn(i);
			}

// 10. Escribir a ByteArrayOutputStream
			workbook.write(out);
			return out.toByteArray();
		}
	}

}
