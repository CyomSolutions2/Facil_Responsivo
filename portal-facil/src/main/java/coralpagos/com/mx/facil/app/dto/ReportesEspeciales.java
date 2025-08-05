package coralpagos.com.mx.facil.app.dto;

public class ReportesEspeciales {
	
	String idReporte;
	String reporte;
	String tabla;
	String clave;
	
	public String getIdReporte() {
		return idReporte;
	}
	public void setIdReporte(String idReporte) {
		this.idReporte = idReporte;
	}
	public String getReporte() {
		return reporte;
	}
	public void setReporte(String reporte) {
		this.reporte = reporte;
	}
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	@Override
	public String toString() {
		return "ReportesEspeciales [idReporte=" + idReporte + ", reporte=" + reporte + ", tabla=" + tabla + ", clave="
				+ clave + "]";
	}
	
	

}
