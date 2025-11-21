package com.digi01.CMonroyProgramacionNCapasSpring.Controller;

import com.digi01.CMonroyProgramacionNCapasSpring.ML.Direccion;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.ErrorCarga;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Pais;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Rol;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Usuario;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("usuario")
public class UsuarioController {

//    @GetMapping("estado/{idPais}")
//    @ResponseBody //Retorna dato Estucturado
//    public Result GetEstadosByIdPais(@PathVariable("idPais") int idPais) {
//        return estadoDAOImplementation.GetByIdPais(idPais);
//    }
//
//    @GetMapping("municipio/{idEstado}")
//    @ResponseBody
//    public Result GetMunicipiosByIdEstado(@PathVariable("idEstado") int idEstado) {
//        return municipioDAOImplementation.GetByIdEstado(idEstado);
//    }
//
//    @GetMapping("colonia/{idMunicipio}")
//    @ResponseBody
//    public Result GetColoniasByIdMunicipio(@PathVariable("idMunicipio") int idMunicipio) {
//        return coloniaDAOImplementation.GetByIdMunicipio(idMunicipio);
//    }
    private static final String urlBase = "http://localhost:8080";

    @GetMapping()
    public String Index(Model model) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(urlBase + "/api/usuario",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Usuario>>>() {
        });

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(urlBase + "/api/usuario/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(urlBase + "/api/pais",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Pais>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            Result result = responseEntity.getBody();
            model.addAttribute("usuarios", result.object);
            model.addAttribute("usuariosBusqueda", new Usuario());

            Result resultRol = responseEntityRol.getBody();
            model.addAttribute("roles", resultRol.object);

            Result resultPais = responseEntityPais.getBody();
            model.addAttribute("paises", resultPais.object);

        }

        return "UsuarioIndex";
    }

//    @GetMapping()
//    public String Index(Model model) {
//        //Result result = usuarioDAOImplementation.GetAll();
//        List<Usuario> usuarios = usuarioService.GetAll();
//        //Result resultJPA = usuarioJPADAOImplementation.GetAll();
//        model.addAttribute("usuarios", usuarios);
//        model.addAttribute("paises", paisDAOImplementation);
//        model.addAttribute("roles", rolDAOImplementation.GetAll().objects);
//
//        model.addAttribute("usuariosBusqueda", new Usuario());
//        return "UsuarioIndex";
//    }
    @PostMapping()
    public String GetAllDinamico(@ModelAttribute("usuariosBusqueda") Usuario usuario, Model model) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Usuario> entity = new HttpEntity<>(usuario);

        ResponseEntity<Result<List<Usuario>>> responseEntity = restTemplate.exchange(urlBase + "/api/usuario/busqueda",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Result<List<Usuario>>>() {
        });

        ResponseEntity<Result<List<Rol>>> responseEntityrol = restTemplate.exchange(urlBase + "/api/usuario/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        if (responseEntity.getStatusCode().value() == 200) {
            Result result = (Result) responseEntity.getBody();
            Result resultRol = responseEntityrol.getBody();
            model.addAttribute("roles", resultRol.object);
            model.addAttribute("usuarios", result.object);
            model.addAttribute("usuariosBusqueda", new Usuario());

        }
        return "UsuarioIndex";
    }

    //@PostMapping()
//    public String GetAllDinamico(@ModelAttribute("usuariosBusqueda") Usuario usuario, Model model) {
//        List<Usuario> result = usuarioService.GetAllDinamico(usuario);
//        //Result result = usuarioDAOImplementation.GetAllDinamico(usuario);
//
//        model.addAttribute("usuarios", result);
//        model.addAttribute("roles", rolDAOImplementation.GetAll().objects);
//        model.addAttribute("usuariosBusqueda", usuario);
//
//        return "UsuarioIndex";
//    }
    @GetMapping("add")
    public String Add(Model model) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(urlBase + "/api/usuario/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(urlBase + "/api/pais",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Pais>>>() {
        });

        if (responseEntityRol.getStatusCode().value() == 200) {

            Result resultRol = responseEntityRol.getBody();
            model.addAttribute("roles", resultRol.object);

        }

        if (responseEntityPais.getStatusCode().value() == 200) {
            Result resultPais = responseEntityPais.getBody();
            model.addAttribute("paises", resultPais.object);
        }

        model.addAttribute("Usuario", new Usuario());

        return "UsuarioForm";

    }

//    @GetMapping("add")
//    public String Add(Model model) {
//        model.addAttribute("Usuario", new Usuario());
//        model.addAttribute("roles", rolDAOImplementation.GetAll().objects);
//        model.addAttribute("paises", paisDAOImplementation.GetAll().objects);
//        return "UsuarioForm";
//    }
//
    @GetMapping("{detail}")
    public String Detail(@PathVariable("detail") int idUsuario,
            Model model) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(urlBase + "/api/usuario/" + idUsuario,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(urlBase + "/api/usuario/rol",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(urlBase + "/api/pais",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<List<Pais>>>() {
        });

        if (responseEntityRol.getStatusCode().value() == 200) {

            Result resultRol = responseEntityRol.getBody();
            model.addAttribute("roles", resultRol.object);

        }

        if (responseEntityPais.getStatusCode().value() == 200) {
            Result resultPais = responseEntityPais.getBody();
            model.addAttribute("paises", resultPais.object);
        }

        model.addAttribute("usuario", responseEntity.getBody().object);
//        model.addAttribute("Direccion", new Direccion());

        return "UsuarioDetail";
    }

//    @GetMapping("{detail}")
//    public String Detail(@PathVariable("detail") int detail, Model model) {
//
//        //Result result = usuarioDAOImplementation.GetById(detail);
//        Usuario result = usuarioService.GetById(detail);
//
//        model.addAttribute("roles", rolDAOImplementation.GetAll().objects);
//        model.addAttribute("usuario", result);
//        model.addAttribute("Direccion", new Direccion());
//        model.addAttribute("paises", paisDAOImplementation.GetAll().objects);
//
//        return "UsuarioDetail";
//    }
    
    @GetMapping("deleteUsuario/{idUsuario}")
    public String DeleteUsuario(@PathVariable("idUsuario") int idUsuario,
                Model model,
                RedirectAttributes redirectAttributes){
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(urlBase + "/api/usuario/" + idUsuario,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });
        
        redirectAttributes.addFlashAttribute("resultDelete", responseEntity.getBody());
        
        return "redirect:/usuario";
    }
    
    //    @GetMapping("deleteUsuario/{idUsuario}")
//    public String DeleteUsuario(@PathVariable("idUsuario") int idUsuario,
//            Model model,
//            RedirectAttributes redirectAttributes) {
//
//        //Result result = usuarioDAOImplementation.Delete(idUsuario);
//        Result result = usuarioService.Delete(idUsuario);
//
//        redirectAttributes.addFlashAttribute("resultDelete", result);
//        return "redirect:/usuario";
//    }
//
    
//    @GetMapping("/direccion/{idDireccion}")
//    @ResponseBody
//    public Direccion getDireccion(@PathVariable int idDireccion) {
//        
//        return direccionService.GetById(idDireccion);
//        //return (Direccion) direccionDAOImplementation.GetById(idDireccion).object;
//    }
//
//
//    @PostMapping("addDireccion/{idUsuario}")
//    public String AddDireccion(@ModelAttribute("Direccion") Direccion direccion,
//            Model model,
//            @PathVariable("idUsuario") int idUsuario,
//            RedirectAttributes redirectAttributes) {
//
//        if (direccion.getIdDireccion() > 0) {
//            
//            //Result result = direccionDAOImplementation.Update(direccion);
//            boolean result = direccionService.Update(direccion, idUsuario);
//            if (result == true) {
//                redirectAttributes.addFlashAttribute("successMessage", "Se Actualizo la direccion correctamente");
//            } else {
//                redirectAttributes.addFlashAttribute("errorMessage", "No se Actualizo la direccion");
//            }
//        }
//        if (direccion.getIdDireccion() == 0) {
//            
//            //Result result = direccionDAOImplementation.Add(direccion, idUsuario);
//            boolean result = direccionService.Add(direccion, idUsuario);
//            if (result == true) {
//                redirectAttributes.addFlashAttribute("successMessage", "Se agrego la direccion correctamente");
//            } else {
//                redirectAttributes.addFlashAttribute("errorMessage", "No se agrego la direccion");
//            }
//            redirectAttributes.addFlashAttribute("resultAdd", result);
//        }
//
//        return "redirect:/usuario/" + idUsuario;
//    }
//
//    @GetMapping("/cargamasiva")
//    public String CargaMasiva() {
//        return "CargaMasiva";
//    }
//
//    @GetMapping("/cargamasiva/procesar")
//    public String CargaMasiva(HttpSession session, RedirectAttributes redirectAttributes) {
//        String path = session.getAttribute("archivoCargaMasiva").toString();
//        session.removeAttribute("archivoCargaMasiva");
//
//        List<Usuario> usuarios;
//        if (path.endsWith(".txt")) {
//            usuarios = LecturaArchivoTXT(new File(path));
//        } else if (path.endsWith(".xlsx")) {
//            usuarios = LecturaArchivoXLSX(new File(path));
//        } else {
//            redirectAttributes.addFlashAttribute("errorMessage", "Extensión de archivo no soportada.");
//            return "redirect:/usuario/cargamasiva";
//        }
//        //inserción con carga masiva
//        // 
//        Result result = usuarioDAOImplementation.AddAll(usuarios);
//        if (result.correct == true) {
//            redirectAttributes.addFlashAttribute("successMessage", "Carga masiva completada exitosamente. Los usuarios fueron insertados en la base de datos.");
//        } else {
//            redirectAttributes.addFlashAttribute("errorMessage", "Ocurrió un error al insertar los usuarios: " + result.ex.getLocalizedMessage());
//        }
//
//        return "redirect:/usuario/cargamasiva";
//    }
//
//    @PostMapping("/cargamasiva")
//    public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo,
//            HttpSession session,
//            Model model,
//            RedirectAttributes redirectAttributes) {
//
//        if (archivo == null || archivo.isEmpty()) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Debes seleccionar un archivo antes de subirlo.");
//        }
//
//        String extension = archivo.getOriginalFilename().split("\\.")[1];
//
//        String path = System.getProperty("user.dir");
//        String pathArchivo = "src/main/resources/archivosCarga";
//        String fecha = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmSS"));
//        String pathDefinitvo = path + "/" + pathArchivo + "/" + fecha + archivo.getOriginalFilename();
//
//        try {
//            archivo.transferTo(new File(pathDefinitvo));
//
//        } catch (Exception ex) {
//            String errortransferencia = ex.getLocalizedMessage();
//            redirectAttributes.addFlashAttribute("errorMessage", "Error al subir el archivo: " + ex.getMessage());
//            return "redirect:usuario/cargamasiva";
//        }
//
//        List<Usuario> usuarios;
//        if (extension.equalsIgnoreCase("txt")) {
//            usuarios = LecturaArchivoTXT(new File(pathDefinitvo));
//
//        } else if (extension.equalsIgnoreCase("xlsx")) {
//            usuarios = LecturaArchivoXLSX(new File(pathDefinitvo));
//
//        } else {
//            // error de archivo
//            redirectAttributes.addFlashAttribute("errorMessage", "Formato de archivo no soportado.");
//            return "redirect:/usuario/cargamasiva";
//        }
//        List<ErrorCarga> errores = ValidarDatosArchivo(usuarios);
//        if (errores.isEmpty()) {
//            //Boton de procesar
//            model.addAttribute("error", false);
//            session.setAttribute("archivoCargaMasiva", pathDefinitvo);
//            redirectAttributes.addFlashAttribute("successMessage", "Archivo validado correctamente. Puedes procesarlo.");
//
//        } else {
//            //Lista Errores
//            model.addAttribute("error", true);
//            model.addAttribute("errores", errores);
//            redirectAttributes.addFlashAttribute("errorMessage", "Se encontraron errores en el archivo.");
//
//        }
//        return "CargaMasiva";
//    }
//
//    @PostMapping("/imagen/update")
//    public String actualizarImagen(@RequestParam int idUsuario,
//            @RequestParam String imagen) {
//        if (imagen.contains(",")) {
//            imagen = imagen.split(",")[1];
//        }
//        usuarioDAOImplementation.UpdateImagen(idUsuario, imagen);
//        return "redirect:/usuario/" + idUsuario;
//    }
//
//    @PostMapping("/detail")
//    public String UpdateUsuario(@ModelAttribute("usuario") Usuario usuario,
//            RedirectAttributes redirectAttributes) {
//        
//        Result result = usuarioService.Update(usuario);
//
//        //Result result = usuarioDAOImplementation.Update(usuario);
//
//        if (result.correct == true) {
//            redirectAttributes.addFlashAttribute("successMessage", "Se actualizo la informacion del usuario " + usuario.getUsername());
//        } else {
//            redirectAttributes.addFlashAttribute("errorMessage", "Se actualizo la informacion del usuario " + usuario.getUsername());
//        }
//        return "redirect:/usuario/" + usuario.getIdUsuario();
//    }
//
//    @PostMapping("add")
//    public String Add(@Valid @ModelAttribute("Usuario") Usuario usuario,
//            BindingResult bindingResult,
//            Model model,
//            RedirectAttributes redirectAttributes,
//            @RequestParam("imagenFile") MultipartFile imagenFile) {
//
//        if (bindingResult.hasErrors()) {
//
//            model.addAttribute("Usuario", usuario); // colonia tiene, municipio, estado y que pais
//            model.addAttribute("roles", rolDAOImplementation.GetAll().objects);
//            model.addAttribute("paises", paisDAOImplementation.GetAll().objects);
//
//            if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais() > 0) {
//                model.addAttribute("estados", estadoDAOImplementation.GetByIdPais(usuario.Direcciones.get(0).Colonia.Municipio.Estado.Pais.getIdPais()).objects);
//                if (usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado() > 0) {
//                    model.addAttribute("municipios", municipioDAOImplementation.GetByIdEstado(usuario.Direcciones.get(0).Colonia.Municipio.Estado.getIdEstado()).objects);
//                    if (usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio() > 0) {
//                        model.addAttribute("colonias", coloniaDAOImplementation.GetByIdMunicipio(usuario.Direcciones.get(0).Colonia.Municipio.getIdMunicipio()).objects);
//                    }
//                }
//            }
//            redirectAttributes.addFlashAttribute("errorMessageAdd", "Revisa que los sean campos validos y esten completos");
//
//            return "UsuarioForm";
//
//        }
//
//        if (imagenFile != null && !imagenFile.isEmpty()) {
//            try {
//
//                String nombreArchivo = imagenFile.getOriginalFilename();
//
//                // Validamos que no venga null y que contenga punto
//                if (nombreArchivo != null && nombreArchivo.contains(".")) {
//
//                    String[] partes = nombreArchivo.split("\\.");
//                    String extension = partes[partes.length - 1]; // último segmento
//
//                    if (extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png")) {
//                        byte[] byteImagen = imagenFile.getBytes();
//                        String imagenBase64 = Base64.getEncoder().encodeToString(byteImagen);
//                        usuario.setImagen(imagenBase64);
//                    }
//                }
//
//            } catch (IOException ex) {
//                Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        //alumnoDAOImplementation
//        boolean result = usuarioService.Add(usuario);
//        //Result result = usuarioDAOImplementation.Add(usuario);
//        redirectAttributes.addFlashAttribute("successMessageAdd", "El usuario " + usuario.getUsername() + "se creo con exito.");
//        return "redirect:/usuario";
//    }
//
//    @PostMapping("deleteDireccion")
//    public String DeleteDireccion(int IdDireccion,
//            @RequestParam("IdUsuario") int IdUsuario,
//            RedirectAttributes redirectAttributes) {
//        
//        Boolean result = direccionService.Delete(IdDireccion);
//        //Result result = direccionDAOImplementation.Delete(IdDireccion);
//
//        return "redirect:/usuario/" + IdUsuario;
//    }
//
//    
//
//    public List<Usuario> LecturaArchivoTXT(File archivo) {
//
//        List<Usuario> usuarios = new ArrayList<>();
//
//        try (InputStream inputStream = new FileInputStream(archivo); BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));) {
//
//            String linea = "";
//
//            while ((linea = bufferedReader.readLine()) != null) {
//
//                String[] campos = linea.split("\\|");
//                Usuario usuario = new Usuario();
//
//                usuario.setNombre(campos[0].trim());
//                usuario.setApellidoPaterno(campos[1].trim());
//                usuario.setApellidoMaterno(campos[2].trim());
//                usuario.setUsername(campos[3].trim());
//                usuario.setEmail(campos[4].trim());
//                usuario.setPassword(campos[5].trim());
//
//                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
//                String fecha = campos[6].trim();
//                Date fecha2 = formato.parse(fecha);
//
//                usuario.setFechaNacimiento(fecha2);
//
//                usuario.setSexo(campos[7].trim());
//                usuario.setCelular(campos[8].trim());
//                usuario.setTelefono(campos[9].trim());
//                usuario.setCurp(campos[10].trim());
//                usuario.setRol(new Rol());
//                usuario.Rol.setIdRol(Integer.parseInt(campos[11].trim()));
//
//                usuarios.add(usuario);
//            }
//
//        } catch (Exception ex) {
//            return null;
//        }
//        return usuarios;
//    }
//
//    public List<Usuario> LecturaArchivoXLSX(File archivo) {
//
//        List<Usuario> usuarios = new ArrayList<>();
//
//        try (InputStream fileInputStream = new FileInputStream(archivo); XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream)) {
//            XSSFSheet workSheet = workbook.getSheetAt(0);
//            DataFormatter formatter = new DataFormatter();
//
//            for (Row row : workSheet) {
//
//                Usuario usuario = new Usuario();
//
//                usuario.setNombre(formatter.formatCellValue(
//                        row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//                usuario.setApellidoPaterno(formatter.formatCellValue(
//                        row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//                usuario.setApellidoMaterno(formatter.formatCellValue(
//                        row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//
//                usuario.setUsername(formatter.formatCellValue(
//                        row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//                usuario.setEmail(formatter.formatCellValue(
//                        row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//                usuario.setPassword(formatter.formatCellValue(
//                        row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//
//                Cell fechaCell = row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//                if (fechaCell.getCellType() == CellType.NUMERIC) {
//                    usuario.setFechaNacimiento(fechaCell.getDateCellValue());
//                }
//
//                usuario.setSexo(formatter.formatCellValue(
//                        row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//                usuario.setCelular(formatter.formatCellValue(
//                        row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//                usuario.setTelefono(formatter.formatCellValue(
//                        row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//                usuario.setCurp(formatter.formatCellValue(
//                        row.getCell(10, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))
//                );
//
//                usuario.Rol = new Rol();
//                usuario.Rol.setIdRol(
//                        (int) row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)
//                                .getNumericCellValue()
//                );
//
//                usuarios.add(usuario);
//
//            }
//
//        } catch (Exception ex) {
//            return null;
//
//        }
//        return usuarios;
//    }
//
//    public List<ErrorCarga> ValidarDatosArchivo(List<Usuario> usuarios) {
//        List<ErrorCarga> erroresCarga = new ArrayList();
//
//        int lineaError = 0;
//
//        for (Usuario usuario : usuarios) {
//            lineaError++;
//            BindingResult bindingResult = validationService.validateObject(usuario);
//            List<ObjectError> errors = bindingResult.getAllErrors();
//
//            for (ObjectError error : errors) {
//                FieldError fieldError = (FieldError) error;
//                ErrorCarga errorCarga = new ErrorCarga();
//                errorCarga.campo = fieldError.getField();
//                errorCarga.descripcion = fieldError.getDefaultMessage();
//                errorCarga.linea = lineaError;
//                erroresCarga.add(errorCarga);
//            }
//        }
//        return erroresCarga;
//    }
}
