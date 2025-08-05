package coralpagos.com.mx.facil.app.dto;

public class ProcedimientosReportes {
	
	String idProcedimiento;
	String idReporte;
	String procedimiento;
	String numeroParametros;
	public String getIdProcedimiento() {
		return idProcedimiento;
	}
	public void setIdProcedimiento(String idProcedimiento) {
		this.idProcedimiento = idProcedimiento;
	}
	public String getIdReporte() {
		return idReporte;
	}
	public void setIdReporte(String idReporte) {
		this.idReporte = idReporte;
	}
	public String getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(String procedimiento) {
		this.procedimiento = procedimiento;
	}
	public String getNumeroParametros() {
		return numeroParametros;
	}
	public void setNumeroParametros(String numeroParametros) {
		this.numeroParametros = numeroParametros;
	}
	@Override
	public String toString() {
		return "ProcedimientoReporte [idProcedimiento=" + idProcedimiento + ", idReporte=" + idReporte
				+ ", procedimiento=" + procedimiento + ", numeroParametros=" + numeroParametros + "]";
	}
	
	

}
