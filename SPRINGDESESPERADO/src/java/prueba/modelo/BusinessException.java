/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prueba.modelo;

/**
 *
 * @author Antonio
 */
public class BusinessException extends RuntimeException {
    
    private String codigoError;

    private String descripcion;

    public BusinessException(String error, String descrip) {
        this.codigoError = error;
        this.descripcion = descrip;
    }

    public String getCodigoError() {
        return codigoError;
    }

    public void setCodigoError(String error) {
        this.codigoError = error;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descrip) {
        this.descripcion = descrip;
    }
    
}
