package coralpagos.com.mx.facil.app.dto;

public class TiposReportes {
	String idTipoReporte;
	String tipoReporte;
	String tabla;
	String clave;
	public String getIdTipoReporte() {
		return idTipoReporte;
	}
	public void setIdTipoReporte(String idTipoReporte) {
		this.idTipoReporte = idTipoReporte;
	}
	public String getTipoReporte() {
		return tipoReporte;
	}
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
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
		return "TiposReportes [idTipoReporte=" + idTipoReporte + ", tipoReporte=" + tipoReporte + ", tabla=" + tabla
				+ ", clave=" + clave + "]";
	}
	
	
	
	
}
