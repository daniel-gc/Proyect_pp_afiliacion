package mx.pliis.afiliacion.dto;

import java.time.LocalDate;
import javax.validation.constraints.Email;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.Data;

@Data
public class CertificadoFunerarioDTO {
	private Integer idCertificado;
	private String cdCertificado;
	@NotNull(message = "El nombre de la persona es obligatorio")
	private String nbPersona;
	@NotEmpty(message = "El apellido paterno es obligatorio")
	private String apPaterno;
	@NotEmpty(message = "El apellido materno es obligatorio")
	private String apMaterno;
	@NotEmpty(message = "La edad es obligatorio")
	private int nuEdad;
	@NotNull(message = "Falta la fecha de nacimiento")
	@Past(message = "La fecha de nacimiento debe ser anterior")
	private LocalDate fhNacimiento;
	private String direccionDomicilio;
	@NotEmpty(message = "El sexo es obligatorio")
	private SexoDTO sexo;
	@NotEmpty(message = "La calle y numero es obligatorio")
	private String txCalleNumero;
	@NotEmpty(message = "El número interior es obligatorio")
	private int nuInterior;
	@NotEmpty(message = "La colonia es obligatorio")
	private String nbColonia;
	@NotEmpty(message = "El municipio es obligatorio")
	private String nbMunicipio;
	@NotEmpty(message = "El estado es obligatorio")
	private String nbEstado;
	@NotEmpty(message = "La ciudad es obligatorio")
	private String nbCiudad;
	@NotEmpty(message = "La curp es obligatorio")
	private String cdCurp;
	@NotEmpty(message = "El RFC es obligatorio")
	private String cdRfc;
	@NotEmpty(message = "El correo es obligatorio")
	@Email
	private String cdCorreo;

}
