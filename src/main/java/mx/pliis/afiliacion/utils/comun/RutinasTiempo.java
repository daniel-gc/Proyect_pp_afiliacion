package mx.pliis.afiliacion.utils.comun;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.stereotype.Component;

@Component
public class RutinasTiempo {

    /**
     * @author genunt
     * @param format String
     * @param fecha Date
     * @return String
     */
    public String getStringDateFromFormta(String format, Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(fecha);
    }

    /**
     * @author genunt
     * @param stringDate String
     * @return Date dd/MM/yyyy
     */
    public Date convertirStringDate(String stringDate, String formato) {
        SimpleDateFormat format = new SimpleDateFormat(formato);
        Date fecha = new Date();

        try {
            fecha = format.parse(stringDate);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return fecha;
    }

    /**
     * @author genunt
     * @param fecha
     * @param dias
     * @return Date
     */
    public Date incrementarNumeroDias(Date fecha, Integer dias) {
        List<Integer> listaAno = new ArrayList<Integer>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        listaAno.add(calendar.get(Calendar.YEAR));

        for (int i = 1; i <= dias; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, i);
        }

        return calendar.getTime();
    }

    /**
     * @author genunt
     * @param fecha Date
     * @return Map<String, String>
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Map<String, String> obtenerFechaDesglosada(Date fecha) {
        Map listaFecha = new HashMap<String, String>();
        String result = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        switch (month) {
            case 0: {
                result = "Enero";
                break;
            }
            case 1: {
                result = "Febrero";
                break;
            }
            case 2: {
                result = "Marzo";
                break;
            }
            case 3: {
                result = "Abril";
                break;
            }
            case 4: {
                result = "Mayo";
                break;
            }
            case 5: {
                result = "Junio";
                break;
            }
            case 6: {
                result = "Julio";
                break;
            }
            case 7: {
                result = "Agosto";
                break;
            }
            case 8: {
                result = "Septiembre";
                break;
            }
            case 9: {
                result = "Octubre";
                break;
            }
            case 10: {
                result = "Noviembre";
                break;
            }
            case 11: {
                result = "Diciembre";
                break;
            }
            default: {
                result = "Error";
                break;
            }
        }

        listaFecha.put("day", day);
        listaFecha.put("month", result);
        listaFecha.put("year", year);

        return listaFecha;
    }

    /**
     * @author genunt
     * @param fecha String yyyyMMdd
     * @return Date dd/MM/yyyy
     */
    public Date getFechaStringDate(String fecha) {
        String ano = fecha.substring(0, 4);
        String mes = fecha.substring(4, 6);
        String dia = fecha.substring(6, 8);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();

        try {
            date = format.parse(dia + "/" + mes + "/" + ano);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return date;
    }

    /**
     * @author genunt
     * @return String
     */
    public String getStringFechaDDMMYYYHHMMSS() {
        Calendar fecha = Calendar.getInstance();
        int ano = fecha.get(Calendar.YEAR);
        int mes = fecha.get(Calendar.MONTH) + 1;
        int dia = fecha.get(Calendar.DAY_OF_MONTH);
        int hora = fecha.get(Calendar.HOUR_OF_DAY);
        int minuto = fecha.get(Calendar.MINUTE);
        int segundo = fecha.get(Calendar.SECOND);

        return dia + "-" + mes + "-" + ano + "-" + hora + "-" + minuto + "-" + segundo;
    }

    /**
     * @author UnitisDes0416
     * @param fecha Date
     * @return String anio
     */
    public String obtenerAnioFecha(Date fecha) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);

        Integer year = calendar.get(Calendar.YEAR);

        return year.toString();
    }

    /**
     * Regresa la fecha actual en formato dd/MM/yyyy
     *
     * @author Fjmb
     * @param fecha Date
     * @return String anio
     */
    public Date getFechaActual() {
        Date fechaActual = new Date();
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = formateador.format(fechaActual);
        try {
            fechaActual = formateador.parse(fecha);
        } catch (ParseException e) {
        }
        return fechaActual;
    }

    /**
     * @author javier07
     * @param file File
     * @return Date
     */
    public Date obtenrFechaModificacionArchivo(File file) {
        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("America/Mexico_City"));
        calendar.setTimeInMillis(file.lastModified());

        Date fecha = calendar.getTime();
        String fechaArchivo = formateador.format(fecha);
        try {
            fecha = formateador.parse(fechaArchivo);
        } catch (ParseException e) {
        }
        return fecha;
    }

    /**
     * @author genunt
     * @param stringDate String
     * @return Date dd/MM/yyyy
     */
    public Date convertirStringDate(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();

        try {
            fecha = format.parse(stringDate);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return fecha;
    }

    /**
     *
     * @param date
     * @return
     */
    public String getFecha_ddMMYYYY_hhmmss(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String dateString = sdf.format(date);
        return dateString;
    }

    /**
     * Método que recibe una fecha tipo Date y retorna el nombre del dia
     * correspondiente
     *
     * @author Javier Flores
     * @param fecha
     * @return String
     */
    public String obtenerNombreDia(Date fecha) {
        String[] dias = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        int numeroDia = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        numeroDia = cal.get(Calendar.DAY_OF_WEEK);
        return dias[numeroDia - 1];
    }

    /**
     * Regresa una fecha con los dias sumados o restados
     *
     * @author Javier Flores
     * @param fecha
     * @param dias
     * @return Date
     */
    public Date sumarRestarDiasFecha(Date fecha, int dias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        calendar.add(Calendar.DAY_OF_YEAR, dias);
        return calendar.getTime();
    }

    /**
     *
     * @param fecha Date
     * @param pais String
     * @return Regresa un Integer que representa el numero de dia de la semana
     * si se coloca el pasi Mexico, regresara el numero de dia Lunes=1,Martes=2
     * ... si se coloca el pais EUA, regresara el numero de dia
     * Domingo=1,Lunes=2...
     */
    public Integer obtenerNumeroDia(Date fecha, String pais) {

        String[] dias = {"Domingo", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado"};
        //pais=eua DOMINGO=1, LUNES=2, MARTES=3, MIERCOLES=4, JUEVES=5, VIERNES=6,SABADO=7 
        //Mexico= LUENES=1,MARTES=2,MIERCOLES=3,JUEVES=4,VIERNES=5,SABADO=6,DOMINGO=7

        int numeroDiaEUA = 0;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        numeroDiaEUA = cal.get(Calendar.DAY_OF_WEEK);
        Integer numDiaSemanaMexico = 0;

        switch (dias[numeroDiaEUA - 1]) {
            case "Domingo":
                numDiaSemanaMexico = new Integer(7);
                break;
            case "Lunes":
                numDiaSemanaMexico = new Integer(1);
                break;
            case "Martes":
                numDiaSemanaMexico = new Integer(2);
                break;
            case "Miércoles":
                numDiaSemanaMexico = new Integer(3);
                break;
            case "Jueves":
                numDiaSemanaMexico = new Integer(4);
                break;
            case "Viernes":
                numDiaSemanaMexico = new Integer(5);
                break;
            case "Sábado":
                numDiaSemanaMexico = new Integer(6);
                break;
        }
        if (pais.equals("Mexico")) {
            return numDiaSemanaMexico;
        } else {
            return numeroDiaEUA;
        }
    }

}
