package coralpagos.com.mx.facil.app.dto;

public class Estados {
	String idEstado;
	String estado;
	String tabla;
	String clave;
	public String getIdEstado() {
		return idEstado;
	}
	public void setIdEstado(String idEstado) {
		this.idEstado = idEstado;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
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
		return "Estados [idEstado=" + idEstado + ", estado=" + estado + ", tabla=" + tabla + ", clave=" + clave + "]";
	}
	
	
	
}
