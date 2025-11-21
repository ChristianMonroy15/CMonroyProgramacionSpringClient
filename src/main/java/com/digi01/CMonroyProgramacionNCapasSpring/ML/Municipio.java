package com.digi01.CMonroyProgramacionNCapasSpring.ML;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Municipio {
    private int IdMunicipio;
    private String Nombre;
    
    @JsonProperty("EstadoJPA")
    public Estado Estado;
    
    public void setIdMunicipio(int IdMunicipio){
        this.IdMunicipio=IdMunicipio;
    }
    
    public int getIdMunicipio(){
        return IdMunicipio;
    }
    
    public void setNombre(String Nombre){
        this.Nombre=Nombre;
    }
    
    public String getNombre(){
        return Nombre;
    }

    public Estado getEstado() {
        return Estado;
    }

    public void setEstado(Estado Estado) {
        this.Estado = Estado;
    }
    
    
    
    
    
}
