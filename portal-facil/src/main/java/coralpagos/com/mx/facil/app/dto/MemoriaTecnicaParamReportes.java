package coralpagos.com.mx.facil.app.dto;

public class MemoriaTecnicaParamReportes {
	
	String idMemoriaTecnica;
	String usuario;
	String idReporte;
	String idParametroReporte;
	String idFiltroParametro;
	String activo;
	public String getIdMemoriaTecnica() {
		return idMemoriaTecnica;
	}
	public void setIdMemoriaTecnica(String idMemoriaTecnica) {
		this.idMemoriaTecnica = idMemoriaTecnica;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIdReporte() {
		return idReporte;
	}
	public void setIdReporte(String idReporte) {
		this.idReporte = idReporte;
	}
	public String getIdParametroReporte() {
		return idParametroReporte;
	}
	public void setIdParametroReporte(String idParametroReporte) {
		this.idParametroReporte = idParametroReporte;
	}
	public String getIdFiltroParametro() {
		return idFiltroParametro;
	}
	public void setIdFiltroParametro(String idFiltroParametro) {
		this.idFiltroParametro = idFiltroParametro;
	}
	public String getActivo() {
		return activo;
	}
	public void setActivo(String activo) {
		this.activo = activo;
	}
	@Override
	public String toString() {
		return "MemoriaTecnicaParamReportes [idMemoriaTecnica=" + idMemoriaTecnica + ", usuario=" + usuario
				+ ", idReporte=" + idReporte + ", idParametroReporte=" + idParametroReporte + ", idFiltroParametro="
				+ idFiltroParametro + ", activo=" + activo + "]";
	}
	
	

}
