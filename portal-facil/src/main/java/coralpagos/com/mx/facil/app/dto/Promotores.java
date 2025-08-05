package coralpagos.com.mx.facil.app.dto;

public class Promotores {
	
	String idPromotor;
	String promotor;
	public String getIdPromotor() {
		return idPromotor;
	}
	public void setIdPromotor(String idPromotor) {
		this.idPromotor = idPromotor;
	}
	public String getPromotor() {
		return promotor;
	}
	public void setPromotor(String promotor) {
		this.promotor = promotor;
	}
	@Override
	public String toString() {
		return "Promotores [idPromotor=" + idPromotor + ", promotor=" + promotor + "]";
	}
	
	

}
