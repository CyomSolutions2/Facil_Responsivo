package coralpagos.com.mx.facil.app.dto;

public class ParametrosReportes {
	String idParametro;
	String idReporte;
	String tipoReporte;
	String lsvParametroReporte;
	String parametroReporte;
	public String getIdParametro() {
		return idParametro;
	}
	public void setIdParametro(String idParametro) {
		this.idParametro = idParametro;
	}
	public String getIdReporte() {
		return idReporte;
	}
	public void setIdReporte(String idReporte) {
		this.idReporte = idReporte;
	}
	public String getTipoReporte() {
		return tipoReporte;
	}
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
	public String getLsvParametroReporte() {
		return lsvParametroReporte;
	}
	public void setLsvParametroReporte(String lsvParametroReporte) {
		this.lsvParametroReporte = lsvParametroReporte;
	}
	public String getParametroReporte() {
		return parametroReporte;
	}
	public void setParametroReporte(String parametroReporte) {
		this.parametroReporte = parametroReporte;
	}
	@Override
	public String toString() {
		return "ParametrosReportes [idParametro=" + idParametro + ", idReporte=" + idReporte + ", tipoReporte="
				+ tipoReporte + ", lsvParametroReporte=" + lsvParametroReporte + ", parametroReporte="
				+ parametroReporte + "]";
	}
	
	

}
