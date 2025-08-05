package coralpagos.com.mx.facil.app.dto;

public class Modulos {
		
	public String mdl_id;
	public String mdl_padre_id;
	public String mdl_clave;
	public String mdl_nombre;
	public String mdl_orden;
	public String getMdl_id() {
		return mdl_id;
	}
	public void setMdl_id(String mdl_id) {
		this.mdl_id = mdl_id;
	}
	public String getMdl_padre_id() {
		return mdl_padre_id;
	}
	public void setMdl_padre_id(String mdl_padre_id) {
		this.mdl_padre_id = mdl_padre_id;
	}
	public String getMdl_clave() {
		return mdl_clave;
	}
	public void setMdl_clave(String mdl_clave) {
		this.mdl_clave = mdl_clave;
	}
	public String getMdl_nombre() {
		return mdl_nombre;
	}
	public void setMdl_nombre(String mdl_nombre) {
		this.mdl_nombre = mdl_nombre;
	}
	public String getMdl_orden() {
		return mdl_orden;
	}
	public void setMdl_orden(String mdl_orden) {
		this.mdl_orden = mdl_orden;
	}
	@Override
	public String toString() {
		return "Modulos [mdl_id=" + mdl_id + ", mdl_padre_id=" + mdl_padre_id + ", mdl_clave=" + mdl_clave
				+ ", mdl_nombre=" + mdl_nombre + ", mdl_orden=" + mdl_orden + "]";
	}
	
	
	

}
