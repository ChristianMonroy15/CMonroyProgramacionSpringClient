
package com.digi01.CMonroyProgramacionNCapasSpring.ML;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Estado {
    
    private int IdEstado;
    private String Nombre;
    
    @JsonProperty("PaisJPA")
    public Pais Pais;
    
    public void setIdEstado(int IdEstado){
        this.IdEstado=IdEstado;
    }
    
    public int getIdEstado(){
        return IdEstado;
    }
    
    public void setNombre(String Nombre){
        this.Nombre=Nombre;
    }
    
    public String getNombre(){
        return Nombre;
    }

    public Pais getPais() {
        return Pais;
    }

    public void setPais(Pais Pais) {
        this.Pais = Pais;
    }
    
}
