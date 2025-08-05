package coralpagos.com.mx.facil.app.dto;

public class SeriesRecibos {
	String idSerieRecibo;
	String serieRecibo;
	String tabla;
	String clave;
	public String getIdSerieRecibo() {
		return idSerieRecibo;
	}
	public void setIdSerieRecibo(String idSerieRecibo) {
		this.idSerieRecibo = idSerieRecibo;
	}
	public String getSerieRecibo() {
		return serieRecibo;
	}
	public void setSerieRecibo(String serieRecibo) {
		this.serieRecibo = serieRecibo;
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
		return "SeriesRecibos [idSerieRecibo=" + idSerieRecibo + ", serieRecibo=" + serieRecibo + ", tabla=" + tabla
				+ ", clave=" + clave + "]";
	}
	
	
	
}
