package coralpagos.com.mx.facil.app.dto;

import java.util.List;

public class Roles {
	
	public String rol_Id;
	public String rol_Descripcion;
	public boolean rol_Activo;
	public List<ModulosRoles> modulosRoles;
	public String getRol_Id() {
		return rol_Id;
	}
	public void setRol_Id(String rol_Id) {
		this.rol_Id = rol_Id;
	}
	public String getRol_Descripcion() {
		return rol_Descripcion;
	}
	public void setRol_Descripcion(String rol_Descripcion) {
		this.rol_Descripcion = rol_Descripcion;
	}
	public boolean isRol_Activo() {
		return rol_Activo;
	}
	public void setRol_Activo(boolean rol_Activo) {
		this.rol_Activo = rol_Activo;
	}
	public List<ModulosRoles> getModulosRoles() {
		return modulosRoles;
	}
	public void setModulosRoles(List<ModulosRoles> modulosRoles) {
		this.modulosRoles = modulosRoles;
	}
	@Override
	public String toString() {
		return "Roles [rol_Id=" + rol_Id + ", rol_Descripcion=" + rol_Descripcion + ", rol_Activo=" + rol_Activo + "]";
	}

	
	
	
	


}
