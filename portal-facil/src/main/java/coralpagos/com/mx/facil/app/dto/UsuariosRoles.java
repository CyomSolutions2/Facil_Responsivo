package coralpagos.com.mx.facil.app.dto;

public class UsuariosRoles {
	
	public String uro_rol_id;
	public String uro_usr_usuario;
	public String getUro_rol_id() {
		return uro_rol_id;
	}
	public void setUro_rol_id(String uro_rol_id) {
		this.uro_rol_id = uro_rol_id;
	}
	public String getUro_usr_usuario() {
		return uro_usr_usuario;
	}
	public void setUro_usr_usuario(String uro_usr_usuario) {
		this.uro_usr_usuario = uro_usr_usuario;
	}
	@Override
	public String toString() {
		return "UsuariosRoles [uro_rol_id=" + uro_rol_id + ", uro_usr_usuario=" + uro_usr_usuario + "]";
	}
	



}
