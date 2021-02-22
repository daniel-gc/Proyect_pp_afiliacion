package mx.pliis.afiliacion.api_rest_controller;

import javax.validation.Valid;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/certificadoFunerario")
@CrossOrigin(origins = "*")
@Log4j2
public class CertificadoFunerarioRestController {

    @Autowired
    private CertificadoFunerarioService certificadoFunerarioService;
    
    @PostMapping("/nuevo")
    public ResponseEntity<?> createCertificadoFunerario(@RequestBody @Valid CertificadoFunerarioDTO certificadoFunerarioDTO) {
        Integer ret = Integer.MIN_VALUE;

		try {
			ret = certificadoFunerarioService.nuevoCertificadoFunerario(certificadoFunerarioDTO);
		} catch (Exception e) {
			MensajeDTO msg = new MensajeDTO("Ocurrió un error salvando la información: " + e.getLocalizedMessage());
			log.error(e.getLocalizedMessage());
			return new ResponseEntity<>(msg, HttpStatus.PRECONDITION_FAILED);
		}

		return new ResponseEntity<>(ret, HttpStatus.CREATED);

    }

}
