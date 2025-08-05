package coralpagos.com.mx.facil.app.dto;

public class ClasificacionMembresia {
	
	String idClasificacion;
	String clasificacion;
	String tabla;
	String clave;
	public String getIdClasificacion() {
		return idClasificacion;
	}
	public void setIdClasificacion(String idClasificacion) {
		this.idClasificacion = idClasificacion;
	}
	public String getClasificacion() {
		return clasificacion;
	}
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
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
		return "ClasificacionMembresia [idClasificacion=" + idClasificacion + ", clasificacion=" + clasificacion
				+ ", tabla=" + tabla + ", clave=" + clave + "]";
	}
	
	

}
