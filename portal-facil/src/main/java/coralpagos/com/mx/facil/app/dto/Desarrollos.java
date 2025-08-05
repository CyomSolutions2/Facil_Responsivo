package coralpagos.com.mx.facil.app.dto;

public class Desarrollos {
	String idDesarrollo;
	String desarrollo;
	String tabla;
	String clave;
	public String getIdDesarrollo() {
		return idDesarrollo;
	}
	public void setIdDesarrollo(String idDesarrollo) {
		this.idDesarrollo = idDesarrollo;
	}
	public String getDesarrollo() {
		return desarrollo;
	}
	public void setDesarrollo(String desarrollo) {
		this.desarrollo = desarrollo;
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
		return "Desarrollos [idDesarrollo=" + idDesarrollo + ", desarrollo=" + desarrollo + ", tabla=" + tabla
				+ ", clave=" + clave + "]";
	}
	
}
