package mx.pliis.afiliacion.service.certificadoFunerario;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.pliis.afiliacion.dto.CertificadoFunerarioDTO;
import mx.pliis.afiliacion.persistencia.hibernate.entity.CertificadoFunerarioEntity;
import mx.pliis.afiliacion.persistencia.hibernate.repository.CertificadoFunerarioEntityRepository;
import mx.pliis.afiliacion.utils.comun.ResponseConverter;
import mx.pliis.afiliacion.utils.comun.RutinasTiempo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

@RequiredArgsConstructor
@Log4j2
@Service
public class CertificadoFunerarioServiceImpl implements CertificadoFunerarioService {

    @Autowired
    private CertificadoFunerarioEntityRepository certificadoFunerarioEntityRepository;

    @Autowired
    private RutinasTiempo rutinasTiempo;

    @Override
    @Transactional
    public Integer nuevoCertificadoFunerario(CertificadoFunerarioDTO certificadoFunerarioDTO) {
//        var certificadoFunerarioEntity = new CertificadoFunerarioEntity();
//        copyProperties.copyProperties(certificadoFunerarioDTO, certificadoFunerarioEntity);
        var certificadoFunerarioEntity = ResponseConverter.copiarPropiedadesFull(certificadoFunerarioDTO, CertificadoFunerarioEntity.class);
        String cdCertificado = certificadoFunerarioEntityRepository.findLastCertificado();
        Date hoy = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(hoy);
        c.add(Calendar.MONTH, certificadoFunerarioDTO.getVigenciaFinCertificado());
        certificadoFunerarioEntity.setIdUsrCreacion(certificadoFunerarioDTO.getIdUsuario());
        certificadoFunerarioEntity.setFhCreacion(hoy);
        certificadoFunerarioEntity.setIdUsrModificacion(certificadoFunerarioDTO.getIdUsuario());
        certificadoFunerarioEntity.setFhModificacion(hoy);
        certificadoFunerarioEntity.setVigenciaFinCertificado(c.getTime());

        if (cdCertificado == null) {
            certificadoFunerarioEntity.setCdCertificado("00001");
        } else {
            int numeroAcuerdo = Integer.parseInt(cdCertificado) + 1;
            String dig = String.valueOf(numeroAcuerdo);
            String folioAcuerdo = cdCertificado.substring(0, cdCertificado.length() - dig.length());
            certificadoFunerarioEntity.setCdCertificado(folioAcuerdo + numeroAcuerdo);
        }
        certificadoFunerarioEntityRepository.save(certificadoFunerarioEntity);

        return certificadoFunerarioEntity.getIdCertificado();
    }

    @Override
    @Transactional
    public ByteArrayOutputStream generarPDFCertificadoFn(String cdCertificado,
            String rutaTotalArchivo, String [] rutaTotalImagen) throws FileNotFoundException, IOException {
        var certOptional = certificadoFunerarioEntityRepository.findByCdCertificado(cdCertificado);
        if (certOptional.isPresent()) {

            var certificadoFunerarioEntity = certOptional.get();

            ByteArrayOutputStream reporte = new ByteArrayOutputStream();
            HashMap parametros = new HashMap();
//            InputStream imagen = new FileInputStream(rutaTotalImagen);

            parametros.put("imagen1", rutaTotalImagen[0]);
            parametros.put("imagen2", rutaTotalImagen[1]);
            parametros.put("imagen3", rutaTotalImagen[2]);
            parametros.put("folio", certificadoFunerarioEntity.getCdCertificado());
            parametros.put("nbPersona", certificadoFunerarioEntity.getNbPersona());
            parametros.put("apPaterno", certificadoFunerarioEntity.getApPaterno());
            parametros.put("apMaterno", certificadoFunerarioEntity.getApMaterno());
            parametros.put("nuEdad", String.valueOf(certificadoFunerarioEntity.getNuEdad()));
            parametros.put("fhNacimiento", rutinasTiempo.getStringDateFromFormta("dd/MM/yyyy",certificadoFunerarioEntity.getFhNacimiento()));
            parametros.put("sexo", certificadoFunerarioEntity.getSexo().getNombre());
            parametros.put("txCalleNumero", certificadoFunerarioEntity.getTxCalleNumero());
            parametros.put("nuInterior", String.valueOf(certificadoFunerarioEntity.getNuInterior()));
            parametros.put("nbColonia", certificadoFunerarioEntity.getNbColonia());
            parametros.put("nbMunicipio", certificadoFunerarioEntity.getNbMunicipio());
            parametros.put("nbEstado", certificadoFunerarioEntity.getNbEstado());
            parametros.put("nbCiudad", certificadoFunerarioEntity.getNbCiudad());
            parametros.put("cdCurp", certificadoFunerarioEntity.getCdCurp());
            parametros.put("cdRfc", certificadoFunerarioEntity.getCdRfc());
            parametros.put("cdCorreo", certificadoFunerarioEntity.getCdCorreo());
            try {
                reporte.write(JasperRunManager.runReportToPdf(rutaTotalArchivo, parametros, new JREmptyDataSource()));
                return reporte;
            } catch (JRException ex) {
                Logger.getLogger(CertificadoFunerarioServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        } else {
            return null;
        }
    }

}
