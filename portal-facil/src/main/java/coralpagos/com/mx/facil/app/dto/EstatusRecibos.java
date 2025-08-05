package coralpagos.com.mx.facil.app.dto;

public class EstatusRecibos {
	String idEstatusRecibo;
	String estatusRecibo;
	String tabla;
	String clave;
	public String getIdEstatusRecibo() {
		return idEstatusRecibo;
	}
	public void setIdEstatusRecibo(String idEstatusRecibo) {
		this.idEstatusRecibo = idEstatusRecibo;
	}
	public String getEstatusRecibo() {
		return estatusRecibo;
	}
	public void setEstatusRecibo(String estatusRecibo) {
		this.estatusRecibo = estatusRecibo;
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
		return "EstatusRecibos [idEstatusRecibo=" + idEstatusRecibo + ", estatusRecibo=" + estatusRecibo + ", tabla=" + tabla
				+ ", clave=" + clave + "]";
	}
	
	
		
}
