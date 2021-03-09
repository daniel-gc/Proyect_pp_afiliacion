package mx.pliis.afiliacion.service.certificadoFunerario;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import mx.pliis.afiliacion.dto.CertificadoFunerarioDTO;

/**
 *
 * @author FerchoCV
 */
public interface CertificadoFunerarioService {

    Integer nuevoCertificadoFunerario(CertificadoFunerarioDTO certificadoFunerarioDTO);

    ByteArrayOutputStream generarPDFCertificadoFn(String cdCertificado,
            String rutaTotalArchivo, String [] rutaTotalImagen) throws FileNotFoundException, IOException;
    
}
