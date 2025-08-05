package coralpagos.com.mx.facil.app.dto;

public class Empleados {
	
	String empId;
	String nombreEmpleado;
	String empSuperiorId;
	String usrLsvDesarrollos;
	String desarrollo;
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getNombreEmpleado() {
		return nombreEmpleado;
	}
	public void setNombreEmpleado(String nombreEmpleado) {
		this.nombreEmpleado = nombreEmpleado;
	}
	public String getEmpSuperiorId() {
		return empSuperiorId;
	}
	public void setEmpSuperiorId(String empSuperiorId) {
		this.empSuperiorId = empSuperiorId;
	}
	public String getUsrLsvDesarrollos() {
		return usrLsvDesarrollos;
	}
	public void setUsrLsvDesarrollos(String usrLsvDesarrollos) {
		this.usrLsvDesarrollos = usrLsvDesarrollos;
	}
	public String getDesarrollo() {
		return desarrollo;
	}
	public void setDesarrollo(String desarrollo) {
		this.desarrollo = desarrollo;
	}
	@Override
	public String toString() {
		return "Empleados [empId=" + empId + ", nombreEmpleado=" + nombreEmpleado + ", empSuperiorId=" + empSuperiorId
				+ ", usrLsvDesarrollos=" + usrLsvDesarrollos + ", desarrollo=" + desarrollo + "]";
	}
	
	

}
