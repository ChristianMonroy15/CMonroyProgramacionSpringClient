
package com.digi01.CMonroyProgramacionNCapasSpring.ML;

import jakarta.validation.constraints.Min;

public class Rol {
    
    @Min(value=1,message = "Selecciona una opcion")
    private int IdRol;
    
    private String Nombre;
   
    public Rol(){}

    public Rol(Integer IdRol) {
        this.IdRol = IdRol;
    }    
    
    public Rol(int IdRol,String Nombre){
        this.IdRol=IdRol;
        this.Nombre=Nombre;
    }
    
    public void setIdRol(int IdRol){
        this.IdRol=IdRol;
    }
    
    public int getIdRol(){
        return IdRol;
    }
    
    public void setNombre(String Nombre){
        this.Nombre=Nombre;
    }
    
    public String getNombre(){
    return Nombre;
    }
    
    
    
    
}
