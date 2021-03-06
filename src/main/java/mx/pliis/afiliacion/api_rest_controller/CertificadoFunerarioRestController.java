package mx.pliis.afiliacion.api_rest_controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.log4j.Log4j2;
import mx.pliis.afiliacion.dto.CertificadoFunerarioDTO;
import mx.pliis.afiliacion.dto.MensajeDTO;
import mx.pliis.afiliacion.service.certificadoFunerario.CertificadoFunerarioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/certificadoFunerario")
@CrossOrigin(origins = "*")
@Log4j2
public class CertificadoFunerarioRestController {

    @Autowired
    ServletContext context;

    @Autowired
    private CertificadoFunerarioService certificadoFunerarioService;

    @PostMapping("/nuevo")
    public ResponseEntity<?> createCertificadoFunerario(@RequestBody @Valid CertificadoFunerarioDTO certificadoFunerarioDTO) {
        Integer ret = Integer.MIN_VALUE;

        try {
            ret = certificadoFunerarioService.nuevoCertificadoFunerario(certificadoFunerarioDTO);
        } catch (Exception e) {
            MensajeDTO msg = new MensajeDTO("Ocurrió un error salvando la información: " + e.getMessage());
            log.error(e.getLocalizedMessage());
            return new ResponseEntity<>(msg, HttpStatus.PRECONDITION_FAILED);
        }

        return new ResponseEntity<>(ret, HttpStatus.CREATED);

    }

    @GetMapping(value = "/generarReporteCertificadoFunerario")
    // @PreAuthorize("hasAnyAuthority('GENERAR_GARANTIAS_ENTREGA_PDF')")
    public ResponseEntity<?> generarPDFCorteSupervisor(@RequestParam(value = "cdCertificado") String cdCertificado)
            throws FileNotFoundException, IOException {

        String filename = "reporteCorteSupervisor.pdf";
        String rutaArchivo = context.getRealPath("/WEB-INF/jasper/certificadoFunerario/certificadoFunerario.jasper");
        String rutaImagen = context.getRealPath("/WEB-INF/jasper/certificadoFunerario/asistencia.PNG");

        ByteArrayOutputStream reporte = certificadoFunerarioService.generaReporteCorteSupervisor(cdCertificado, rutaArchivo, rutaImagen);
        if(reporte==null){
            MensajeDTO msg = new MensajeDTO("Ocurrió un error salvando la información: No se encotró el certificado");
            return new ResponseEntity<>(msg, HttpStatus.PRECONDITION_FAILED);
        }
        final byte[] bytes = reporte.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        headers.add("Content-Disposition", "attachmnt; filename =" + filename);
        headers.add("filename", filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        headers.setContentLength(bytes.length);
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);

    }

}
