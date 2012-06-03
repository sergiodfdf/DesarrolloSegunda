/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.modelo;

/**
 *
 * @author Antonio
 */
public class registerVista {
    
    private Usuario user;
    private String nombre1;
    private String apellido1;
    private String clave1;
    private String correo1;
    private String nickname1;
    private String fecha_nac1;
    private String pais1;
    private String biografia1;
    private String foto1;
  
  public Usuario getUser(){  
      System.out.println("Del resgisterVista getUser: "+user);
      return user;  
  }  
    
  public void setUser (Usuario user){
      System.out.println("Del resgisterVista setUser: "+user);
      this.user = user;
  }
    
  public String getNombre() {
//      System.out.println("Del resgisterVista getNombre: "+nombre);
    return nombre1;
  }

  public void setNombre(String nombre) {
//      System.out.println("Del resgisterVista setNombre: "+nombre);
      this.nombre1 = nombre;
  }
  
  public String getApellido() {
    return apellido1;
  }

  public void setApellido(String apellido) {
    this.apellido1 = apellido;
  }
  
  public String getClave() {
    return clave1;
  }

  public void setClave(String clave) {
    this.clave1 = clave;
  }
  
  public String getCorreo() {
    return correo1;
  }

  public void setCorreo(String correo) {
    this.correo1 = correo;
  }
  
  public String getNickname() {
    return nickname1;
  }

  public void setNickname(String nickname) {
    this.nickname1 = nickname;
  }
  
  public String getFecha_nac() {
    return fecha_nac1;
  }

  public void setFecha_nac(String fecha_nac) {
    this.fecha_nac1 = fecha_nac;
  }
  
  public String getPais() {
    return pais1;
  }

  public void setPais(String pais) {
    this.pais1 = pais;
  }
  
  public String getBiografia() {
    return biografia1;
  }

  public void setBiografia(String biografia) {
    this.biografia1 = biografia;
  }
  
  public String getFoto() {
    return foto1;
  }

  public void setFoto(String foto) {
    this.foto1 = foto;
  }
  
  @Override
	public String toString() {
		return "RegisterFormBean [clave=" + clave1
				+ ", nombre=" + nombre1
				+", nickname=" + nickname1
				+"]";
	}
    
}
