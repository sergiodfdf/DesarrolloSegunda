/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.modelo;

/**
 *
 * @author Antonio
 */
public class loginFormularioVista {
    
    private Usuario user;
    private String nickName;
    private String clave;
    
    public Usuario getUser(){  
      System.out.println("Del loginFOrmularioVista getUser: "+user);
      return user;  
  }  
    
  public void setUser (Usuario user){
      System.out.println("Del loginFOrmularioVista setUser: "+user);
      this.user = user;
  }
  
  public String getnickName() {
    return nickName;
  }

  public void setnickName(String nickname) {
    this.nickName = nickname;
  }
  
  public String getClave() {
    return clave;
  }

  public void setClave(String clavee) {
    this.clave = clavee;
  }
  
  @Override
	public String toString() {
		return "loginFormularioVista [clave=" + clave
				+ ", nickname=" + nickName
				+"]";
	}
  
}
