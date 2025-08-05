package coralpagos.com.mx.facil.app.dto;

public class ListaValores {
	
	String lsvId;
	String lsvDescripcion;
	String lsvTabla;
	String lsvClave;
	public String getLsvId() {
		return lsvId;
	}
	public void setLsvId(String lsvId) {
		this.lsvId = lsvId;
	}
	public String getLsvDescripcion() {
		return lsvDescripcion;
	}
	public void setLsvDescripcion(String lsvDescripcion) {
		this.lsvDescripcion = lsvDescripcion;
	}
	public String getLsvTabla() {
		return lsvTabla;
	}
	public void setLsvTabla(String lsvTabla) {
		this.lsvTabla = lsvTabla;
	}
	public String getLsvClave() {
		return lsvClave;
	}
	public void setLsvClave(String lsvClave) {
		this.lsvClave = lsvClave;
	}
	@Override
	public String toString() {
		return "ListaValores [lsvId=" + lsvId + ", lsvDescripcion=" + lsvDescripcion + ", lsvTabla=" + lsvTabla
				+ ", lsvClave=" + lsvClave + "]";
	}
	
	

}
