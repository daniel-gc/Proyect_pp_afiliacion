package mx.pliis.afiliacion.service.certificadoFunerario;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import mx.pliis.afiliacion.dto.CertificadoFunerarioDTO;
import mx.pliis.afiliacion.persistencia.hibernate.entity.CertificadoFunerarioEntity;
import mx.pliis.afiliacion.persistencia.hibernate.repository.CertificadoFunerarioEntityRepository;
import mx.pliis.afiliacion.utils.comun.ResponseConverter;
import mx.pliis.afiliacion.utils.copyproperties.CopyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author FerchoCV
 */
@RequiredArgsConstructor
@Log4j2
@Service
public class CertificadoFunerarioServiceImpl implements CertificadoFunerarioService {

    @Autowired
    private CertificadoFunerarioEntityRepository certificadoFunerarioEntityRepository;

    @Autowired
    private CopyProperties copyProperties;

    @Override
    @Transactional(readOnly = false)
    public Integer nuevoCertificadoFunerario(CertificadoFunerarioDTO certificadoFunerarioDTO) {
//        var certificadoFunerarioEntity = new CertificadoFunerarioEntity();
//        copyProperties.copyProperties(certificadoFunerarioDTO, certificadoFunerarioEntity);
        var certificadoFunerarioEntity = ResponseConverter.copiarPropiedadesFull(certificadoFunerarioDTO, CertificadoFunerarioEntity.class);
        String cdCertificado = certificadoFunerarioEntityRepository.findLastCertificado();
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

}
