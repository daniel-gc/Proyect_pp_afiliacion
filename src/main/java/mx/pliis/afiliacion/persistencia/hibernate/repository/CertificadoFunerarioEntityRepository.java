package mx.pliis.afiliacion.persistencia.hibernate.repository;

import java.util.Optional;
import mx.pliis.afiliacion.persistencia.hibernate.entity.CertificadoFunerarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author FerchoCV
 */
@Repository
public interface CertificadoFunerarioEntityRepository extends JpaRepository<CertificadoFunerarioEntity, Integer> {

    String findLastCertificado();
    
    Optional<CertificadoFunerarioEntity> findByCdCertificado(String cdCertificado);
    
}
