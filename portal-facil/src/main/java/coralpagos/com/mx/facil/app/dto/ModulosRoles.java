package coralpagos.com.mx.facil.app.dto;

import java.util.ArrayList;
import java.util.List;

public class ModulosRoles {
	
	public String rom_rol_id;
	public String rom_mdl_id;
	public String mdl_padre_id;
	public String mdl_clave;
	public String mdl_nombre;
	public String mu_path = "/home";
	public List<ModulosRoles> hijos = new ArrayList<>();
	
	public String getRom_rol_id() {
		return rom_rol_id;
	}
	public void setRom_rol_id(String rom_rol_id) {
		this.rom_rol_id = rom_rol_id;
	}
	public String getRom_mdl_id() {
		return rom_mdl_id;
	}
	public void setRom_mdl_id(String rom_mdl_id) {
		this.rom_mdl_id = rom_mdl_id;
	}
	public String getMdl_padre_id() {
		return mdl_padre_id;
	}
	public void setMdl_padre_id(String mdl_padre_id) {
		this.mdl_padre_id = mdl_padre_id;
	}
	public String getMdl_clave() {
		return mdl_clave;
	}
	public void setMdl_clave(String mdl_clave) {
		this.mdl_clave = mdl_clave;
	}
	public String getMdl_nombre() {
		return mdl_nombre;
	}
	public void setMdl_nombre(String mdl_nombre) {
		this.mdl_nombre = mdl_nombre;
	}
	public String getMu_path() {
		return mu_path;
	}
	public void setMu_path(String mu_path) {
		this.mu_path = mu_path;
	}
	public List<ModulosRoles> getHijos() {
		return hijos;
	}
	public void setHijos(List<ModulosRoles> hijos) {
		this.hijos = hijos;
	}
	@Override
	public String toString() {
		return "ModulosRoles [rom_rol_id=" + rom_rol_id + ", rom_mdl_id=" + rom_mdl_id + ", mdl_padre_id="
				+ mdl_padre_id + ", mdl_clave=" + mdl_clave + ", mdl_nombre=" + mdl_nombre + ", mu_path=" + mu_path
				+ ", hijos=" + hijos + "]";
	}
	




}
