
package com.digi01.CMonroyProgramacionNCapasSpring.ML;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Colonia {
    
    private int IdColonia;
    private String Nombre;
    private String CodigoPostal;
    
    @JsonProperty("MunicipioJPA")
    public Municipio Municipio;
    
    public void setIdColonia(int IdColonia){
        this.IdColonia=IdColonia;
    }
    
    public int getIdColonia(){
        return IdColonia;
    }
    
    public void setNombre(String Nombre){
        this.Nombre=Nombre;
    }
    
    public String getNombre(){
        return Nombre;
    }
    
    public void setCodigoPostal(String CodigoPostal){
        this.CodigoPostal=CodigoPostal;
    }
    
    public String getCodigoPostal(){
        return CodigoPostal;
    }

    public Municipio getMunicipio() {
        return Municipio;
    }

    public void setMunicipio(Municipio Municipio) {
        this.Municipio = Municipio;
    }
    
        
}
