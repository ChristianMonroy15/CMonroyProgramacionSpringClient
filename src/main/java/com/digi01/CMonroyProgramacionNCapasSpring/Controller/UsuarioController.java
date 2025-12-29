package com.digi01.CMonroyProgramacionNCapasSpring.Controller;

import com.digi01.CMonroyProgramacionNCapasSpring.DTO.ResultLog;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Colonia;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Direccion;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Estado;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Municipio;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Pais;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Result;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Rol;
import com.digi01.CMonroyProgramacionNCapasSpring.ML.Usuario;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.Base64;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
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

    private static final String urlBase = "http://localhost:8080";

    @GetMapping()
    public String Index(Model model, HttpSession session) {

        String token = (String) session.getAttribute("token");

        System.out.println("🔥 TOKEN RECUPERADO EN INDEX = " + token);

        if (token == null) {
            System.out.println("❌ NO HAY TOKEN, REDIRIGIENDO A LOGIN");
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        System.out.println("➡ Enviando token al servidor: Bearer " + token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Usuario>>> responseEntity
                = restTemplate.exchange(
                        urlBase + "/api/usuario",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Result<List<Usuario>>>() {
                }
                );

        ResponseEntity<Result<List<Rol>>> responseEntityRol
                = restTemplate.exchange(
                        urlBase + "/api/usuario/rol",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Result<List<Rol>>>() {
                }
                );

        ResponseEntity<Result<List<Pais>>> responseEntityPais
                = restTemplate.exchange(
                        urlBase + "/api/pais",
                        HttpMethod.GET,
                        entity,
                        new ParameterizedTypeReference<Result<List<Pais>>>() {
                }
                );

        Result result = responseEntity.getBody();
        model.addAttribute("usuarios", result.object);
        model.addAttribute("usuariosBusqueda", new Usuario());

        model.addAttribute("roles", responseEntityRol.getBody().object);
        model.addAttribute("paises", responseEntityPais.getBody().object);

        return "UsuarioIndex";
    }

    @PostMapping()
    public String GetAllDinamico(@ModelAttribute("usuariosBusqueda") Usuario usuario,
            Model model,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<Usuario> entity = new HttpEntity<>(usuario, headers);

        RestTemplate restTemplate = new RestTemplate();

        //Enviar petición protegida
        ResponseEntity<Result<List<Usuario>>> responseEntity
                = restTemplate.exchange(
                        urlBase + "/api/usuario/busqueda",
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<Result<List<Usuario>>>() {
                }
                );

        HttpEntity<?> emptyWithToken = new HttpEntity<>(headers);

        ResponseEntity<Result<List<Rol>>> responseEntityRol
                = restTemplate.exchange(
                        urlBase + "/api/usuario/rol",
                        HttpMethod.GET,
                        emptyWithToken,
                        new ParameterizedTypeReference<Result<List<Rol>>>() {
                }
                );

        //Poblar modelo
        Result result = responseEntity.getBody();
        model.addAttribute("usuarios", result.object);
        model.addAttribute("usuariosBusqueda", new Usuario());
        model.addAttribute("roles", responseEntityRol.getBody().object);

        return "UsuarioIndex";
    }

    @GetMapping("add")
    public String Add(Model model,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(urlBase + "/api/usuario/rol",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(urlBase + "/api/pais",
                HttpMethod.GET,
                entity,
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

    @GetMapping("{detail}")
    public String Detail(@PathVariable("detail") int idUsuario,
            Model model,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(urlBase + "/api/usuario/" + idUsuario,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });

        ResponseEntity<Result<List<Rol>>> responseEntityRol = restTemplate.exchange(urlBase + "/api/usuario/rol",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<List<Rol>>>() {
        });

        ResponseEntity<Result<List<Pais>>> responseEntityPais = restTemplate.exchange(urlBase + "/api/pais",
                HttpMethod.GET,
                entity,
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
        model.addAttribute("Direccion", new Direccion());

        return "UsuarioDetail";
    }

    @GetMapping("deleteUsuario/{idUsuario}")
    public String DeleteUsuario(@PathVariable("idUsuario") int idUsuario,
            Model model,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(urlBase + "/api/usuario/" + idUsuario,
                HttpMethod.DELETE,
                entity,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });

        redirectAttributes.addFlashAttribute("resultDelete", responseEntity.getBody());

        return "redirect:/usuario";
    }

    @GetMapping("direccion/{idDirecion}")
    @ResponseBody
    public Direccion getDireccion(@PathVariable int idDireccion,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            Direccion direccion = new Direccion();
            direccion.setCalle("UNAUTHORIZED");
            return direccion;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Result<Direccion>> responseEntity = restTemplate.exchange(urlBase + "api/direccion/" + idDireccion,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Result<Direccion>>() {
        });

        return responseEntity.getBody().object;
    }

    @GetMapping("/cargamasiva")
    public String CargaMasiva() {
        return "CargaMasiva";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordView() {
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordView() {
        return "reset-password";
    }

    @PostMapping("addDireccion/{idUsuario}")
    public String AddDireccion(@ModelAttribute("Direccion") Direccion direccion,
            @PathVariable("idUsuario") int idUsuario,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        RestTemplate restTemplate = new RestTemplate();
        Result<Direccion> result;

        if (direccion.getIdDireccion() > 0) {

            HttpEntity<Direccion> request = new HttpEntity<>(direccion, headers);

            ResponseEntity<Result<Direccion>> response = restTemplate.exchange(
                    urlBase + "/api/direccion/usuario/" + idUsuario,
                    HttpMethod.PUT,
                    request,
                    new ParameterizedTypeReference<Result<Direccion>>() {
            }
            );

            result = response.getBody();

            if (result.correct) {
                redirectAttributes.addFlashAttribute("successMessage", "Se actualizó la dirección correctamente");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo actualizar la dirección");
            }

        } else {

            HttpEntity<Direccion> request = new HttpEntity<>(direccion, headers);

            ResponseEntity<Result<Direccion>> response = restTemplate.exchange(
                    urlBase + "/api/direccion/" + idUsuario,
                    HttpMethod.POST,
                    request,
                    new ParameterizedTypeReference<Result<Direccion>>() {
            }
            );

            result = response.getBody();

            if (result.correct) {
                redirectAttributes.addFlashAttribute("successMessage", "Se agregó la dirección correctamente");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "No se pudo agregar la dirección");
            }
        }

        return "redirect:/usuario/" + idUsuario;
    }

    @PostMapping("/cargamasiva")
    public String CargaMasiva(@RequestParam("archivo") MultipartFile archivo,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        if (archivo == null || archivo.isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Debes seleccionar un archivo antes de subirlo."
            );
            return "redirect:/usuario/cargamasiva";
        }

        try {
            // 🔐 Token de sesión
            String tokenSesion = (String) session.getAttribute("token");
            if (tokenSesion == null) {
                return "redirect:/login";
            }

            RestTemplate restTemplate = new RestTemplate();

            // Multipart
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            ByteArrayResource recurso = new ByteArrayResource(archivo.getBytes()) {
                @Override
                public String getFilename() {
                    return archivo.getOriginalFilename();
                }
            };

            body.add("archivo", recurso);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("Authorization", "Bearer " + tokenSesion);

            HttpEntity<MultiValueMap<String, Object>> requestEntity
                    = new HttpEntity<>(body, headers);

            ResponseEntity<ResultLog> response = restTemplate.exchange(
                    urlBase + "/api/cargamasiva/subir",
                    HttpMethod.POST,
                    requestEntity,
                    ResultLog.class
            );

            ResultLog result = response.getBody();

            // RESPUESTA DEL SERVIDOR
            if (result != null && result.isCorrect()) {

                session.setAttribute("tokenCarga", result.getToken());
                session.setAttribute("idLog", (Integer) result.getIdLog());

                // ✅ Archivo válido
                redirectAttributes.addFlashAttribute("successMessage", result.getMensaje());
                redirectAttributes.addFlashAttribute("error", false);

                redirectAttributes.addFlashAttribute("tokenCarga", result.getToken());
                redirectAttributes.addFlashAttribute("idLog", result.getIdLog());

            } else {

                // ❌ Archivo con errores
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        result != null ? result.getMensaje() : "Error en validación"
                );
                redirectAttributes.addFlashAttribute("error", true);

                if (result != null && result.getErrores() != null) {
                    redirectAttributes.addFlashAttribute("errores", result.getErrores());
                }
            }

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error al comunicar con el servidor: " + ex.getMessage()
            );
        }

        return "redirect:/usuario/cargamasiva";
    }

    @PostMapping("/cargamasiva/procesar")
    public String procesarCargaMasiva(
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String tokenSesion = (String) session.getAttribute("token");
        String tokenCarga = (String) session.getAttribute("tokenCarga");
        Integer idLog = (Integer) session.getAttribute("idLog");

        if (tokenSesion == null || tokenCarga == null || idLog == null) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "No hay información válida para procesar.");
            return "redirect:/usuario/cargamasiva";
        }

        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + tokenSesion);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<Result> response = restTemplate.exchange(
                    urlBase + "/api/cargamasiva/procesar/" + idLog + "/" + tokenCarga,
                    HttpMethod.POST,
                    entity,
                    Result.class
            );

            Result result = response.getBody();

            if (result != null && result.correct) {
                redirectAttributes.addFlashAttribute(
                        "successMessage",
                        "Carga masiva procesada correctamente."
                );

                session.removeAttribute("tokenCarga");
                session.removeAttribute("idLog");

            } else {
                redirectAttributes.addFlashAttribute(
                        "errorMessage",
                        "Error al procesar la carga."
                );
            }

        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Error al procesar dentro de la Base de Datos"
            );
        }

        return "redirect:/usuario/cargamasiva";
    }

    @PostMapping("/detail")
    public String UpdateUsuario(@ModelAttribute("usuario") Usuario usuario,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Usuario> request = new HttpEntity<>(usuario, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(urlBase + "/api/usuario/" + usuario.getIdUsuario(),
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });

        if (responseEntity.getBody().correct == true) {
            redirectAttributes.addFlashAttribute("successMessage", "Se actualizo la informacion del usuario " + usuario.getUserName());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "No actualizo la informacion del usuario " + usuario.getUserName());
        }

        return "redirect:/usuario/" + usuario.getIdUsuario();
    }

    @PostMapping("add")
    public String Add(@Valid @ModelAttribute("Usuario") Usuario usuario,
            BindingResult bindingResult,
            Model model,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @RequestParam("imagenFile") MultipartFile imagenFile) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        RestTemplate restTemplate = new RestTemplate();

        if (bindingResult.hasErrors()) {

            ResponseEntity<Result<List<Rol>>> responseRol = restTemplate.exchange(
                    urlBase + "/api/usuario/rol",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Rol>>>() {
            }
            );
            model.addAttribute("roles", responseRol.getBody().object);

            ResponseEntity<Result<List<Pais>>> responsePais = restTemplate.exchange(
                    urlBase + "/api/pais",
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Result<List<Pais>>>() {
            }
            );
            model.addAttribute("paises", responsePais.getBody().object);

            if (usuario.getDirecciones().get(0).getColonia().getMunicipio().getEstado().getPais().getIdPais() > 0) {

                ResponseEntity<Result<List<Estado>>> responseEstado = restTemplate.exchange(
                        urlBase + "/api/estado/pais/"
                        + usuario.getDirecciones().get(0).getColonia().getMunicipio().getEstado().getPais().getIdPais(),
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Result<List<Estado>>>() {
                }
                );
                model.addAttribute("estados", responseEstado.getBody().object);

                if (usuario.getDirecciones().get(0).getColonia().getMunicipio().getEstado().getIdEstado() > 0) {

                    ResponseEntity<Result<List<Municipio>>> responseMun = restTemplate.exchange(
                            urlBase + "/api/municipio/estado/"
                            + usuario.getDirecciones().get(0).getColonia().getMunicipio().getEstado().getIdEstado(),
                            HttpMethod.GET,
                            HttpEntity.EMPTY,
                            new ParameterizedTypeReference<Result<List<Municipio>>>() {
                    }
                    );
                    model.addAttribute("municipios", responseMun.getBody().object);

                    if (usuario.getDirecciones().get(0).getColonia().getMunicipio().getIdMunicipio() > 0) {

                        ResponseEntity<Result<List<Colonia>>> responseCol = restTemplate.exchange(
                                urlBase + "/api/colonia/municipio/"
                                + usuario.getDirecciones().get(0).getColonia().getMunicipio().getIdMunicipio(),
                                HttpMethod.GET,
                                HttpEntity.EMPTY,
                                new ParameterizedTypeReference<Result<List<Colonia>>>() {
                        }
                        );
                        model.addAttribute("colonias", responseCol.getBody().object);
                    }
                }
            }

            redirectAttributes.addFlashAttribute("errorMessageAdd",
                    "Revisa que los campos sean válidos y estén completos");

            model.addAttribute("Usuario", usuario);
            return "UsuarioForm";
        }

        if (imagenFile != null && !imagenFile.isEmpty()) {
            try {
                String ext = imagenFile.getOriginalFilename()
                        .substring(imagenFile.getOriginalFilename().lastIndexOf(".") + 1);

                if (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("jpg")) {
                    usuario.setImagen(Base64.getEncoder().encodeToString(imagenFile.getBytes()));
                }
            } catch (Exception e) {
            }
        }

        HttpEntity<Usuario> requestEntity = new HttpEntity<>(usuario, headers);

        ResponseEntity<Result<Usuario>> response = restTemplate.exchange(
                urlBase + "/api/usuario",
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Result<Usuario>>() {
        }
        );

        Result<Usuario> result = response.getBody();

        if (result.correct) {
            redirectAttributes.addFlashAttribute("successMessageAdd",
                    "El usuario " + usuario.getUserName() + " se creó con éxito.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessageAdd",
                    "Ocurrió un error al crear el usuario.");
        }

        return "redirect:/usuario";
    }

    @PostMapping("deleteDireccion")
    public String DeleteDireccion(@RequestParam("IdDireccion") int idDireccion,
            @RequestParam("IdUsuario") int idUsuario,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Direccion>> responseEntity = restTemplate.exchange(
                urlBase + "/api/direccion/" + idDireccion,
                HttpMethod.DELETE,
                entity,
                new ParameterizedTypeReference<Result<Direccion>>() {
        }
        );

        Result<Direccion> result = responseEntity.getBody();

        if (result.correct) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "La dirección se eliminó correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "No se pudo eliminar la dirección.");
        }

        return "redirect:/usuario/" + idUsuario;
    }

    @PostMapping("/changePassword")
    public String ChangePassword(@ModelAttribute("usuario") Usuario usuario,
            RedirectAttributes redirectAttributes,
            HttpSession session) {

        String token = (String) session.getAttribute("token");
        if (token == null) {
            return "redirect:/login";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Usuario> request = new HttpEntity<>(usuario, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result<Usuario>> responseEntity = restTemplate.exchange(urlBase + "/api/usuario/changePassword/" + usuario.getIdUsuario(),
                HttpMethod.PATCH,
                request,
                new ParameterizedTypeReference<Result<Usuario>>() {
        });

        if (responseEntity.getBody().correct == true) {
            redirectAttributes.addFlashAttribute("successMessage", "Se actualizo la contraseña del usuario " + usuario.getUserName());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "No actualizo la contraseña del usuario " + usuario.getUserName());
        }

        return "redirect:/usuario/" + usuario.getIdUsuario();

    }

}
