package coralpagos.com.mx.facil.app.dto;

public class Cartera {

	String ccbId;
	String ccbCartera;
	String ccbRangoDiasInicio;
	String ccbRangoDiasFinal;
	
	public String getCcbId() {
		return ccbId;
	}
	public void setCcbId(String ccbId) {
		this.ccbId = ccbId;
	}
	public String getCcbCartera() {
		return ccbCartera;
	}
	public void setCcbCartera(String ccbCartera) {
		this.ccbCartera = ccbCartera;
	}
	public String getCcbRangoDiasInicio() {
		return ccbRangoDiasInicio;
	}
	public void setCcbRangoDiasInicio(String ccbRangoDiasInicio) {
		this.ccbRangoDiasInicio = ccbRangoDiasInicio;
	}
	public String getCcbRangoDiasFinal() {
		return ccbRangoDiasFinal;
	}
	public void setCcbRangoDiasFinal(String ccbRangoDiasFinal) {
		this.ccbRangoDiasFinal = ccbRangoDiasFinal;
	}
	@Override
	public String toString() {
		return "Cartera [ccbId=" + ccbId + ", ccbCartera=" + ccbCartera + ", ccbRangoDiasInicio=" + ccbRangoDiasInicio
				+ ", ccbRangoDiasFinal=" + ccbRangoDiasFinal + "]";
	}
	
	
}
