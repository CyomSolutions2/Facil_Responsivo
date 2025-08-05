package coralpagos.com.mx.facil.app.dto;

public class TipoCuponesDescuentoPaq {
	String idTipoCuponDescuentoPaq;
	String tipoCuponDescuentoPaq;
	String tabla;
	String clave;
	public String getIdTipoCuponDescuentoPaq() {
		return idTipoCuponDescuentoPaq;
	}
	public void setIdTipoCuponDescuentoPaq(String idTipoCuponDescuentoPaq) {
		this.idTipoCuponDescuentoPaq = idTipoCuponDescuentoPaq;
	}
	public String getTipoCuponDescuentoPaq() {
		return tipoCuponDescuentoPaq;
	}
	public void setTipoCuponDescuentoPaq(String tipoCuponDescuentoPaq) {
		this.tipoCuponDescuentoPaq = tipoCuponDescuentoPaq;
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
		return "TipoCuponesDescuentoPaq [idTipoCuponDescuentoPaq=" + idTipoCuponDescuentoPaq
				+ ", tipoCuponDescuentoPaq=" + tipoCuponDescuentoPaq + ", tabla=" + tabla + ", clave=" + clave + "]";
	}
	
	
	
}
