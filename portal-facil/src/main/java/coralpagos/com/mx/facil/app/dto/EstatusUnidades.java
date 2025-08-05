package coralpagos.com.mx.facil.app.dto;

public class EstatusUnidades {
	String idEstatusUnidad;
	String estatusUnidad;
	String tabla;
	String clave;
	public String getIdEstatusUnidad() {
		return idEstatusUnidad;
	}
	public void setIdEstatusUnidad(String idEstatusUnidad) {
		this.idEstatusUnidad = idEstatusUnidad;
	}
	public String getEstatusUnidad() {
		return estatusUnidad;
	}
	public void setEstatusUnidad(String estatusUnidad) {
		this.estatusUnidad = estatusUnidad;
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
		return "EstatusUnidades [idEstatusUnidad=" + idEstatusUnidad + ", estatusUnidad=" + estatusUnidad + ", tabla="
				+ tabla + ", clave=" + clave + "]";
	}
	
		
}
