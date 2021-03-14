package mx.pliis.afiliacion.service.job;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import mx.pliis.afiliacion.dto.FileReporteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobEnvioDatosFunerariaService {

    private final static Date f = new Date();
    private final static String TMP_FILE = "tmp" + File.separator;
    private final static String TMP_FILE_ZIP = "Certificados_Funerarios" + File.separator;
    
    @Value("${username.email.addr}")
    private String username;
    @Value("${username.email.passw}")
    private String password;

    // Cron cada dia a las 00:00:01 AM
//    @Scheduled(cron = "1 0 0 * * ?", zone = "America/Mexico_City")
    public void enviaDatosFuneraria(List<FileReporteDTO> listFileReporteDTO) throws FileNotFoundException, IOException, MessagingException {
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(new File(TMP_FILE_ZIP))));
        try (zos) {
            listFileReporteDTO.forEach(reporte -> {
                try {
                    givenDataArray_whenConvertToCSV_thenOutputCreated(reporte.getReporte());
                    InputStream resource = new ByteArrayInputStream(reporte.getReporte().toByteArray());
                    zos.putNextEntry(new ZipEntry(TMP_FILE_ZIP+"Certificado_Funerario_" + reporte.getCdCertificado() + ".pdf"));
                    zos.write(resource.readAllBytes());
                    zos.closeEntry();
                } catch (IOException ex) {
                    Logger.getLogger(JobEnvioDatosFunerariaService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(JobEnvioDatosFunerariaService.class.getName()).log(Level.SEVERE, null, ex);
        }

        sendEmailZip(
                "Se adjuntan los certificados funerarios",
                "Archivos de certificado funerario", "daniel.wolf2.gc@gmail.com");
    }

    public void sendEmailWithAttachment(String text, String subject, String to, ByteArrayOutputStream reporte, String codigo) throws MessagingException, IOException {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("from", username);
        props.put("username", username);
        props.put("password", password);

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        BodyPart texto = new MimeBodyPart();
        texto.setText(text);
        givenDataArray_whenConvertToCSV_thenOutputCreated(reporte);
        BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(new DataHandler(new FileDataSource(TMP_FILE)));
        adjunto.setFileName("Certificado_Funerario_" + codigo + ".pdf");
        // Una MultiParte para agrupar texto e imagen.
        MimeMultipart multiParte = new MimeMultipart();
        multiParte.addBodyPart(texto);
        multiParte.addBodyPart(adjunto);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("from")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(multiParte);
//            message.setText("Hola");

            // Se envia el correo.
            Transport.send(message);
//            t = session.getTransport("smtp");
//            t.connect(props.getProperty("from"), props.getProperty("password"));
//            t.sendMessage(message, message.getAllRecipients());
//            t.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File givenDataArray_whenConvertToCSV_thenOutputCreated(ByteArrayOutputStream data) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(TMP_FILE));
            // Put data in your baos
            data.writeTo(fos);
        } catch (IOException ioe) {
            // Handle exception here
            ioe.printStackTrace();
        } finally {
            fos.close();
        }
        return null;
    }

    public void sendEmailZip(String text, String subject, String to) throws MessagingException, IOException {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("from", username);
        props.put("username", username);
        props.put("password", password);

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        BodyPart texto = new MimeBodyPart();
        texto.setText(text);
//        givenDataArray_whenConvertToCSV_thenOutputCreated(reporte);
        BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(new DataHandler(new FileDataSource(TMP_FILE_ZIP)));
        adjunto.setFileName("Certificados_Funerarios.zip");
        MimeMultipart multiParte = new MimeMultipart();
        multiParte.addBodyPart(texto);
        multiParte.addBodyPart(adjunto);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("from")));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(multiParte);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
