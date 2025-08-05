package coralpagos.com.mx.facil.app.dto;

public class TiposProductos {
	String idTipoProducto;
	String tipoProducto;
	String tabla;
	String clave;
	public String getIdTipoProducto() {
		return idTipoProducto;
	}
	public void setIdTipoProducto(String idTipoProducto) {
		this.idTipoProducto = idTipoProducto;
	}
	public String getTipoProducto() {
		return tipoProducto;
	}
	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
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
		return "TiposProductos [idTipoProducto=" + idTipoProducto + ", tipoProducto=" + tipoProducto + ", tabla="
				+ tabla + ", clave=" + clave + "]";
	}
	
	
	
}
