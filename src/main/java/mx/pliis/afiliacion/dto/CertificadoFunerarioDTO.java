package mx.pliis.afiliacion.dto;


import java.time.LocalDate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class CertificadoFunerarioDTO {
	private Integer idCertificado;
	private String cdCertificado;
	@NotNull(message = "El nombre de la persona es obligatorio")
	@NotEmpty(message = "El nombre de la persona es obligatorio")
	private String nbPersona;
	@NotNull(message = "El apellido paterno es obligatorio")
	@NotEmpty(message = "El apellido paterno es obligatorio")
	private String apPaterno;
	@NotNull(message = "El apellido materno es obligatorio")
	@NotEmpty(message = "El apellido materno es obligatorio")
	private String apMaterno;
	@NotNull(message = "La edad es obligatorio")
	private Integer nuEdad;
	@NotNull(message = "Falta la fecha de nacimiento")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")	
	private LocalDate fhNacimiento;
	private String direccionDomicilio;
	@NotNull(message = "El sexo es obligatorio")
	private SexoDTO sexo;
	@NotNull(message = "La calle y numero es obligatorio")
	@NotEmpty(message = "La calle y numero es obligatorio")
	private String txCalleNumero;
	@NotNull(message = "El número interior es obligatorio")
	private Integer nuInterior;
	@NotNull(message = "La colonia es obligatorio")
	@NotEmpty(message = "La colonia es obligatorio")
	private String nbColonia;
	@NotNull(message = "El municipio es obligatorio")
	@NotEmpty(message = "El municipio es obligatorio")
	private String nbMunicipio;
	@NotNull(message = "El estado es obligatorio")
	@NotEmpty(message = "El estado es obligatorio")
	private String nbEstado;
	@NotNull(message = "La ciudad es obligatorio")
	@NotEmpty(message = "La ciudad es obligatorio")
	private String nbCiudad;
	@NotNull(message = "El CURP es obligatorio")
	@NotEmpty(message = "El CURP es obligatorio")
	private String cdCurp;
	@NotNull(message = "El RFC es obligatorio")
	@NotEmpty(message = "El RFC es obligatorio")
	private String cdRfc;
	@NotNull(message = "El correo es obligatorio")
	@NotEmpty(message = "El correo es obligatorio")
	@Email
	private String cdCorreo;

}
