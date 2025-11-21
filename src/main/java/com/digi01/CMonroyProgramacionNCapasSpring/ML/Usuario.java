package com.digi01.CMonroyProgramacionNCapasSpring.ML;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

public class Usuario {

    private int IdUsuario;

    @NotNull(message = "El campo nombre no puede estar vacío")
    @NotBlank(message = "El campo nombre debe contener almenos 2 caracteres")
    @Size(min = 2, max = 17, message = "El nombre debe contener entre 2 y 17 caracteres")
    private String Nombre;

    @NotNull(message = "El campo Apellido Paterno no puede estar vacío")
    @NotBlank(message = "El campo Apellido Paterno debe contener almenos 2 caracteres")
    @Size(min = 2, max = 17, message = "El nombre debe contener entre 2 y 17 caracteres")
    private String ApellidoPaterno;

    
    @Size(min = 0, max = 17, message = "El nombre debe contener entre 2 y 17 caracteres")
    private String ApellidoMaterno;

    @Email(message = "El formato del correo no es válido")
    @NotBlank(message = "El campo Email no puede estar vacío")
    private String Email;

    @NotBlank(message = "El campo Password no puede estar vacío")
    private String Password;

    @Past(message = "La fecha no puede ser posterior a hoy")
    @NotNull(message = "El campo Fecha no puede estar vacío")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date FechaNacimiento;

    @NotBlank(message = "Selecciona una opcion")
    private String Sexo;

    @NotBlank(message = "El campo Telefono no puede estar vacío")
    private String Telefono;

    private String Celular;

    @NotBlank(message = "El campo Username no puede estar vacío")
    private String Username;

    @Pattern(regexp = "^(([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d))*$", message = "Ingresa un CURP valido")
    private String Curp;
    
    private String Imagen;

    @Valid
    public Rol Rol;

    @JsonProperty("DireccionesJPA")
    public List<Direccion> Direcciones;

    public Usuario() {
    }

    public Usuario(int IdUsuario, String Username, String Nombre, String ApellidoPaterno, String ApellidoMaterno, String Email, String Password, Date FechaNacimiento, String Sexo, String Telefono, String Celular, String Curp) {
        this.IdUsuario = IdUsuario;
        this.Username = Username;
        this.Nombre = Nombre;
        this.ApellidoPaterno = ApellidoPaterno;
        this.ApellidoMaterno = ApellidoMaterno;
        this.Email = Email;
        this.Password = Password;
        this.FechaNacimiento = FechaNacimiento;
        this.Sexo = Sexo;
        this.Telefono = Telefono;
        this.Celular = Celular;
        this.Curp = Curp;
    }

    public void setIdUsuario(int IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public int getIdUsuario() {
        return IdUsuario;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getUsername() {
        return Username;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setApellidoPaterno(String ApellidoPaterno) {
        this.ApellidoPaterno = ApellidoPaterno;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoMaterno(String ApellidoMaterno) {
        this.ApellidoMaterno = ApellidoMaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getEmail() {
        return Email;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getPassword() {
        return Password;
    }

    public void setFechaNacimiento(Date FechaNacimiento) {
        this.FechaNacimiento = FechaNacimiento;
    }

    public Date getFechaNacimiento() {
        return FechaNacimiento;
    }

    public void setSexo(String Sexo) {
        this.Sexo = Sexo;
    }

    public String getSexo() {
        return Sexo;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setCelular(String Celular) {
        this.Celular = Celular;
    }

    public String getCelular() {
        return Celular;
    }

    public void setCurp(String Curp) {
        this.Curp = Curp;
    }

    public String getCurp() {
        return Curp;
    }

    public Rol getRol() {
        return Rol;
    }

    public void setRol(Rol Rol) {
        this.Rol = Rol;
    }

    public List<Direccion> getDirecciones() {
        return Direcciones;
    }

    public void setDirecciones(List<Direccion> Direcciones) {
        this.Direcciones = Direcciones;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String Imagen) {
        this.Imagen = Imagen;
    }
    
     

}
