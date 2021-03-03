package mx.pliis.afiliacion.persistencia.hibernate.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "certificado")
@NamedQueries({
    @NamedQuery(name = "CertificadoFunerarioEntity.findLastCertificado",
            query = "SELECT max(a.cdCertificado) FROM CertificadoFunerarioEntity a")})
public class CertificadoFunerarioEntity implements Serializable {

    private static final long serialVersionUID = -8379119867049893452L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_certificado")
    private Integer idCertificado;
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "cd_certificado")
    private String cdCertificado;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nb_persona")
    private String nbPersona;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ap_paterno")
    private String apPaterno;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ap_materno")
    private String apMaterno;
    @NotNull
    @Column(name = "nu_edad")
    private int nuEdad;
    @NotNull
    @Column(name = "fh_nacimiento")
    private Date fhNacimiento;
//    private String direccionDomicilio;
    @NotNull
    @JoinColumn(name = "id_sexo", referencedColumnName = "id_sexo")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SexoEntity sexo;
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "tx_calle_num")
    private String txCalleNumero;
    @NotNull
    @Column(name = "nu_interior")
    private int nuInterior;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nb_colonia")
    private String nbColonia;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nb_municipio")
    private String nbMunicipio;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nb_estado")
    private String nbEstado;
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nb_ciudad")
    private String nbCiudad;
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "cd_curp")
    private String cdCurp;
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "cd_rfc")
    private String cdRfc;
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "cd_correo")
    private String cdCorreo;
    @NotNull
    @Column(name = "id_usr_creacion")
    private int idUsrCreacion;
    @NotNull
    @Column(name = "fh_creacion")
    private Date fhCreacion;
    @NotNull
    @Column(name = "id_usr_modificacion")
    private int idUsrModificacion;
    @NotNull
    @Column(name = "fh_modificacion")
    private Date fhModificacion;

}
