package mx.pliis.afiliacion.api_rest_controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
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
import mx.pliis.afiliacion.service.job.JobEnvioDatosFunerariaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import mx.pliis.afiliacion.dto.FileReporteDTO;

@RestController
@RequestMapping("/certificadoFunerario")
@CrossOrigin(origins = "*")
@Log4j2
public class CertificadoFunerarioRestController {

    @Autowired
    ServletContext context;
    @Autowired
    private JobEnvioDatosFunerariaService jobEnvioDatosFunerariaService;
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
    public ResponseEntity<?> generarPDFCertificadoFn(@RequestParam(value = "cdCertificado") String cdCertificado, @RequestParam(value = "cdCorreoFn") String cdCorreoFn)
            throws FileNotFoundException, IOException, MessagingException {

        String filename = "CertificadoFunerario.pdf";
        String rutaArchivo = context.getRealPath("/WEB-INF/jasper/certificadoFunerario/certificadoFunerario.jasper");
        String rutaImagen[] = {context.getRealPath("/WEB-INF/jasper/certificadoFunerario/asistencia.PNG"),
            context.getRealPath("/WEB-INF/jasper/certificadoFunerario/snac.PNG"),
            context.getRealPath("/WEB-INF/jasper/certificadoFunerario/piePag.PNG")};

        ByteArrayOutputStream reporte = certificadoFunerarioService.generarPDFCertificadoFn(cdCertificado, rutaArchivo, rutaImagen);
        if (reporte == null) {
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
        jobEnvioDatosFunerariaService.sendEmailWithAttachment(
                "Se adjuntan los certificados funerarios",
                "Archivo de certificado funerario", cdCorreoFn, reporte, cdCertificado
        );
        return new ResponseEntity<byte[]>(bytes, headers, HttpStatus.OK);

    }

    @Scheduled(cron = "0 0 1 * * ?")
    public void envioCertificadoFunerario() throws FileNotFoundException, IOException, MessagingException {
        List<CertificadoFunerarioDTO> listCertificadoFunerarioDTO = certificadoFunerarioService.findAll();
        String rutaArchivo = context.getRealPath("/WEB-INF/jasper/certificadoFunerario/certificadoFunerario.jasper");
        String rutaImagen[] = {context.getRealPath("/WEB-INF/jasper/certificadoFunerario/asistencia.PNG"),
            context.getRealPath("/WEB-INF/jasper/certificadoFunerario/snac.PNG"),
            context.getRealPath("/WEB-INF/jasper/certificadoFunerario/piePag.PNG")};
        List<FileReporteDTO> listFileReporteDTO = new ArrayList<>();
        listCertificadoFunerarioDTO.forEach(certificadoFunerarioDTO -> {
            ByteArrayOutputStream reporte;
            FileReporteDTO fileReporteDTO = new FileReporteDTO();
            try {
                reporte = certificadoFunerarioService.generarPDFCertificadoFn(certificadoFunerarioDTO.getCdCertificado(), rutaArchivo, rutaImagen);
                if (reporte == null) {
                    MensajeDTO msg = new MensajeDTO("Ocurrió un error: No se encotró el certificado" + certificadoFunerarioDTO.getCdCertificado());
                } else {
                    fileReporteDTO.setReporte(reporte);
                    fileReporteDTO.setCdCertificado(certificadoFunerarioDTO.getCdCertificado());
                    listFileReporteDTO.add(fileReporteDTO);
                }
            } catch (IOException ex) {
                Logger.getLogger(CertificadoFunerarioRestController.class.getName()).log(Level.SEVERE, null, ex);
            }
//            final byte[] bytes = reporte.toByteArray();
        });
        if (!listFileReporteDTO.isEmpty()) {
            jobEnvioDatosFunerariaService.enviaDatosFuneraria(listFileReporteDTO);
        }
    }

}
