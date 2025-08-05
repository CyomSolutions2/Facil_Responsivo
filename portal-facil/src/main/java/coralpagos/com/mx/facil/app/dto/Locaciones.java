package coralpagos.com.mx.facil.app.dto;

public class Locaciones {
	String idLocacion;
	String locacion;
	String tabla;
	String clave;
	public String getIdLocacion() {
		return idLocacion;
	}
	public void setIdLocacion(String idLocacion) {
		this.idLocacion = idLocacion;
	}
	public String getLocacion() {
		return locacion;
	}
	public void setLocacion(String locacion) {
		this.locacion = locacion;
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
		return "Locaciones [idLocacion=" + idLocacion + ", locacion=" + locacion + ", tabla=" + tabla + ", clave="
				+ clave + "]";
	}

		
}
