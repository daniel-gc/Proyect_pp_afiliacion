package mx.pliis.afiliacion.persistencia.hibernate.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "certificado")
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
    private LocalDate fhNacimiento;
	private String direccionDomicilio;
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
	@Size(min = 1, max = 100)
    @Column(name = "cd_curp")
    private String cdCurp;
	@NotNull
	@Size(min = 1, max = 100)
    @Column(name = "cd_rfc")
    private String cdRfc;
	@NotNull
	@Size(min = 1, max = 100)
    @Column(name = "cd_correo")
	private String cdCorreo;

}
