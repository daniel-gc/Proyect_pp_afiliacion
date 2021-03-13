package mx.pliis.afiliacion.dto;

import java.io.ByteArrayOutputStream;
import lombok.Data;

@Data
public class FileReporteDTO {
    private ByteArrayOutputStream reporte;
    private String cdCertificado;
    
}
