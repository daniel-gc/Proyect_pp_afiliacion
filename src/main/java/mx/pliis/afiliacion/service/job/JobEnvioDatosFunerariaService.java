package mx.pliis.empresas_sindicatos.negocio.servicios.job;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import mx.pliis.empresas_sindicatos.dto.FamiliarBeneficiarioCsvDTO;
import mx.pliis.empresas_sindicatos.negocio.utils.EnumSexoBdAfiliado;
import mx.pliis.empresas_sindicatos.persistencia.hibernate.entity.EmpleadoEntity;
import mx.pliis.empresas_sindicatos.persistencia.hibernate.entity.FamiliarBeneficiarioEntity;
import mx.pliis.empresas_sindicatos.persistencia.hibernate.repository.CatalogoEntityRepository;
import mx.pliis.empresas_sindicatos.persistencia.hibernate.repository.EmpleadoEntityRepository;
import mx.pliis.empresas_sindicatos.persistencia.hibernate.repository.FamiliarBeneficiarioRepository;


@Component
public class JobEnvioDatosFunerariaService {
	
	private final static String TMP_FILE = "tmp"+File.separator+"csvDataFuneraria.csv";
	private final static String CODIGO_CAT_FUNERARIAS = "FUNERARIAS";

	@Autowired
	private FamiliarBeneficiarioRepository familiarBeneficiarioRepository;

	@Autowired
	private CatalogoEntityRepository catalogoEntityRepository;
	
	@Autowired
	private EmpleadoEntityRepository empleadoEntityRepository;
	
//	@Value("${username.email.addr}")
	private String username = "asd";
//	@Value("${username.email.passw}")
	private String password = "qwe";

	// Cron cada dia a las 00:00:01 AM
	@Scheduled(cron = "1 0 0 * * ?", zone="America/Mexico_City")
	public void enviaDatosFuneraria() throws IOException, MessagingException {
		// OBTENEMOS TODOS LOS AFILIADOS DE LA BD
		var list = familiarBeneficiarioRepository.findAll();
		

		// VALIDAMOS LA LISTA DE Familiares de beneficiarios
		if (!list.isEmpty()) {
			
			Set<EmpleadoEntity> empEntityList = list.stream().map(FamiliarBeneficiarioEntity::getIdEmpleado)
					.collect(Collectors.toSet());
			
			Set<Integer> idsEmp = empEntityList.stream().map(EmpleadoEntity::getIdEmpleado)
					.collect(Collectors.toSet());
			
			List<EmpleadoEntity> listEmpleado = empleadoEntityRepository.findAllById(idsEmp);
			
			var listDtoCsv = new ArrayList<FamiliarBeneficiarioCsvDTO>();
			
			//agregamos los elementos de familiar beneficiarios a la lista para generar el csv
			 
			list.stream().forEach(e ->{
				FamiliarBeneficiarioCsvDTO famCsv = new FamiliarBeneficiarioCsvDTO();
				famCsv.setIdUnico(e.getIdFamiliarBeneficiario());
				famCsv.setNombre(e.getNombres());
				famCsv.setApPaterno(e.getApPaterno());
				famCsv.setApMaterno(e.getApMaterno());
				famCsv.setFhNacimiento(e.getFechaNacimiento() == null ? "" : getDateString("dd/MM/yyyy", e.getFechaNacimiento()));
				famCsv.setEdad(obtenerAnios(e.getFechaNacimiento(), new Date())+"");
				famCsv.setGenero(e.getSexo() == null ? "" : getNameSexo(e.getSexo()));
				listDtoCsv.add(famCsv);
			});
			
			if(!listEmpleado.isEmpty()) {
				listEmpleado.stream().forEach(e ->{
					FamiliarBeneficiarioCsvDTO famCsv = new FamiliarBeneficiarioCsvDTO();
					famCsv.setIdUnico(e.getIdEmpleado());
					
					String fullNameArray[] = e.getNombre().split(" ");
					String nombre = "";
					String apPaterno = "";
					String apMaterno = "";
					if(fullNameArray.length == 1) {
						nombre = fullNameArray[0];
					}else if(fullNameArray.length < 3) {
						nombre = fullNameArray[0];
						apPaterno = fullNameArray[1];
					}else {
						apMaterno = fullNameArray[fullNameArray.length-1];
						apPaterno = fullNameArray[fullNameArray.length-2];
						for(int i=0; i<fullNameArray.length-2; i++) {
							if(nombre.isEmpty()) {
								nombre=fullNameArray[i];
							}else {
								nombre+=" "+fullNameArray[i];
							}
							
						}
					}
					
					famCsv.setNombre(nombre);
					famCsv.setApPaterno(apPaterno);
					famCsv.setApMaterno(apMaterno);
					famCsv.setFhNacimiento(e.getFechaNacimiento() == null ? "" : getAnioBtewenDatLocale(e.getFechaNacimiento(), LocalDate.now()));
					famCsv.setGenero(e.getIdSexo() == null ? "" : e.getIdSexo().getNombre());
					listDtoCsv.add(famCsv);
				});
				
			}

			List<String[]> dataCsv = construyeCsv(listDtoCsv);
			
			//CREA EN EL CARPETA TMP UN ARCHIVO TEMPORAL PARA DE AHI TOMARLO Y MANDARLO POR EMAIL
			givenDataArray_whenConvertToCSV_thenOutputCreated(dataCsv);
			
			//Se consultan los correos de las funerarias mediante el cofigo
			var listCatalogoFunerarias = catalogoEntityRepository.findByCode(CODIGO_CAT_FUNERARIAS);
			
			if(!listCatalogoFunerarias.isEmpty()) {
				
				listCatalogoFunerarias.stream().forEach(e ->{
					sendEmailWithAttachment(
							"Se adjuntan datos de afiliados y familiares funerarios", 
							"Archivo de afiliados y fmiliares funerarios", e.getValue()
							);
				});
			}
			
		}
	}
	
	private String getNameSexo(Integer idSexo) {
		if(EnumSexoBdAfiliado.MASCULINO.getIdSexo().intValue() == idSexo.intValue())
			return EnumSexoBdAfiliado.MASCULINO.getNombre();
		
		if(EnumSexoBdAfiliado.FEMENINO.getIdSexo().intValue() == idSexo.intValue())
			return EnumSexoBdAfiliado.FEMENINO.getNombre();
		
		if(EnumSexoBdAfiliado.OTRO.getIdSexo().intValue() == idSexo.intValue())
			return EnumSexoBdAfiliado.OTRO.getNombre();
		
		if(EnumSexoBdAfiliado.PREFIERO_NO_DECIR.getIdSexo().intValue() == idSexo.intValue())
			return EnumSexoBdAfiliado.PREFIERO_NO_DECIR.getNombre();
		
		return EnumSexoBdAfiliado.OTRO.getNombre(); 
	}
	
	public String getAnioBtewenDatLocale(LocalDate firtsDate, LocalDate lastDate) {
		Period period = Period.between(lastDate, firtsDate);
		return  period.getYears() + "";
	}
	
	public void sendEmailWithAttachment(String text, String subject, String to) {
		
		try {
			
			Properties props = new Properties();
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "587");
			props.setProperty("mail.smtp.auth", "true");
			
			props.put("from", username);
			props.put("username", username);
			props.put("password", password);
			
			Session session = Session.getDefaultInstance(props, null);
			
			BodyPart texto = new MimeBodyPart();
			texto.setText(text);
			
			
			BodyPart adjunto = new MimeBodyPart();
			adjunto.setDataHandler(new DataHandler(new FileDataSource(TMP_FILE)));
			adjunto.setFileName("DatosDeFamiliaresFunerarios.csv");

			// Una MultiParte para agrupar texto e imagen.
			MimeMultipart multiParte = new MimeMultipart();
			multiParte.addBodyPart(texto);
			multiParte.addBodyPart(adjunto);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(props.getProperty("from")));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			message.setSubject(subject);
			message.setContent(multiParte);

			// Se envia el correo.
			Transport t = session.getTransport("smtp");
			t.connect(props.getProperty("from"), props.getProperty("password"));
			t.sendMessage(message, message.getAllRecipients());
			t.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			File f = new File(TMP_FILE);
			if(f.exists() && !f.isDirectory()) { 
				f.delete();
			}
		}
	}
	
	public List<String[]> construyeCsv(List<FamiliarBeneficiarioCsvDTO> list) {
		List<String[]> lisGen = new ArrayList<>();
		String[] headers = { "IDENTIFICADOR UNICO", "NOMBRE", "APELLIDO PATERNO", "APELLIDO MATERNO",
				"FECHA DE NACIMIENTO", "EDAD", "GENERO" };
		lisGen.add(headers);
		String[] data = null;
		for (FamiliarBeneficiarioCsvDTO f : list) {
			data = new String[headers.length];
			data[0] = f.getIdUnico()+"";
			data[1] = f.getNombre();
			data[2] = f.getApPaterno();
			data[3] = f.getApMaterno();
			data[4] = f.getFhNacimiento();
			data[5] = f.getEdad();
			data[6] = f.getGenero();

			lisGen.add(data);
		}
		return lisGen;
	}
	
	
	private int obtenerAnios(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
        if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH) ||
            (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) &&   
            a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
            diff--;
        }
        return diff;
    }

	private Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        return cal;
    }
	
	private String getDateString(String format, Date value) {
        try{
        	SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
            return formatter.format(value);
        }catch (Exception e1) {
            e1.printStackTrace();
            return null;
        }
    }
	
	public File givenDataArray_whenConvertToCSV_thenOutputCreated(List<String[]> dataLines) throws IOException {
	    File csvOutputFile = new File(TMP_FILE);
	    try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
	        dataLines.stream()
	          .map(this::convertToCSV)
	          .forEach(pw::println);
	    }
	    
	    return csvOutputFile;
	}
	
	
	public String convertToCSV(String[] data) {
	    return Stream.of(data)
	      .collect(Collectors.joining(","));
	}
	

}
