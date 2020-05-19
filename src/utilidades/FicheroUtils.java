package utilidades;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import virusexpansion.Comunidad;
import virusexpansion.Simulacion;

/**
 *
 * @author Santiago Pérez Gómez
 */
public class FicheroUtils {

    /**
     * Método que a partir de un objeto de tipo Simulacion, rellena una tabla
     * con la información de dicho objeto.
     *
     * @param resultado : se reciibe por parámetro la información calculada en
     * la ejecución de la simulación.
     * @return File: Se recibe el fichero con la tabla generada con los
     * resultados de la simulación.
     */
    public static String generarTabla(Simulacion resultado, String path) {
        String file = generarTablaExcel(resultado, path);

        generarTablaConsola(resultado);
        return file;
    }

    /**
     * Método que a partir de un objeto de tipo Simulacion, rellena una tabla
     * con la información de dicho objeto por consola
     *
     * @param resultado : se reciibe por parámetro la información calculada en
     * la ejecución de la simulación.
     * @return File: Se recibe el fichero con la tabla generada con los
     * resultados de la simulación.
     */
    public static File generarTablaConsola(Simulacion resultado) {
        DecimalFormat formato = new DecimalFormat("#");
        DecimalFormat format = new DecimalFormat("#.##");
        System.out.println("Num Dias: " + resultado.getNumDias());
        System.out.println("V: " + resultado.getPorcetajeViajeros());
        System.out.println("P: " + resultado.getProbContagiosP());
        System.out.println("E: " + resultado.getPromContactosE());
        System.out.println("Población total " + formato.format(resultado.getPoblacionTotal()));
        System.out.println("Numero de comunidades: " + resultado.getComunidades().length);
        for (Comunidad comunidad : resultado.getComunidades()) {
            System.out.println(" Comunidad" + comunidad.getNumComunidad() + ": " + comunidad.getIdComunidad());
            System.out.println(" Población total: " + formato.format(comunidad.getPoblacionTotal()));
            System.out.println(" Numero de viajeros: " + formato.format(comunidad.getNumeroViajerosV()));
            for (int i = 0; i < comunidad.getVisitantesComunidad().length; i++) {
                double visitantes = comunidad.getVisitantesComunidad()[i];
                if (visitantes != -1) {
                    System.out.println("Visitantes comunidad" + (i + 1) + ": " + formato.format(visitantes));
                }
            }
            for (int i = 0; i < resultado.getNumDias(); i++) {
                System.out.println("------Dia " + (i + 1) + "----------");
                System.out.println("Infectados totales: " + formato.format(comunidad.getNumInfectadosDia()[i]));
                System.out.println("Porcentaje: " + format.format(comunidad.getPorcentajeInfectados(i) * 100));
                System.out.println("InfectadosInternos: " + formato.format(comunidad.getNumInfectadosInternos()[i]));
                for (int j = 0; j < comunidad.getNumInfectadosExternosDiasComunidad()[i].length; j++) {
                    double externos = comunidad.getNumInfectadosExternosDiasComunidad()[i][j];
                    System.out.println("     ----Comunidad " + j + "-------");
                    System.out.println("     Visitantes: " + formato.format(externos));
                }

            }

        }
        return null;
    }

    /**
     * Método que genera una fichero de tipo .xslx con la información de la simulación ejecutada
     * @param resultado
     * @return 
     */
    private static String generarTablaExcel(Simulacion resultado, String path) {
        DecimalFormat formatoEntero = new DecimalFormat("#");
        DecimalFormat formatDecimal = new DecimalFormat("#.##");

        //Hojas
        String hojaParam = "Parámetros";
        String hojaDatos = "Información Comunidades";
        String hojaSimulacion = "Simulacion";

        //Creando objeto libro de Excel
        XSSFWorkbook book = new XSSFWorkbook();

        //Hoja Parametros
        XSSFSheet parametros = book.createSheet(hojaParam);
        //Hoja Comunidades
        XSSFSheet datos = book.createSheet(hojaDatos);
        //Hoja Simulacion
        XSSFSheet simulacion = book.createSheet(hojaSimulacion);

        //Cabecera 
        //Parametros
        String[] headerParametros = new String[]{"Parámetros de la simulación", "Valor"};
        String[] headerDatos = new String[resultado.getComunidades().length + 2];
        for (int i = 1; i < headerDatos.length - 1; i++) {
            Comunidad comunidad = resultado.getComunidades()[i - 1];
            headerDatos[i] = "Comunidad " + (comunidad.getNumComunidad() + 1) + " - " + comunidad.getIdComunidad();
        }
        //Datos
        headerDatos[0] = "";
        headerDatos[headerDatos.length - 1] = "Total";
        //Simulacion
        int modulo = 5 + resultado.getComunidades().length - 1;
        String[] headerSimulacion = new String[1 + ((modulo) * resultado.getComunidades().length) + 2];
        headerSimulacion[0] = "";
        int contComunidad = 0;
        int visitantesCont = 0;
        for (int j = 1; j < headerSimulacion.length - 2; j++) {
            int aux = j % (modulo);
            Comunidad comunidad = resultado.getComunidades()[contComunidad];
            switch (aux) {
                case 0:
                    contComunidad++;
                    visitantesCont = comunidad.getNumComunidad() == visitantesCont ? visitantesCont + 1 : visitantesCont;
                    if (comunidad.getNumComunidad() != visitantesCont) {
                        headerSimulacion[j] = "Contagiados Vis. Com." + (++visitantesCont);
                    }
                    visitantesCont = 0;
                    break;
                case 1://Contagiados
                    headerSimulacion[j] = "Contagiados";
                    break;
                case 2:// %
                    headerSimulacion[j] = "Porcentaje";
                    break;
                case 3:// E
                    headerSimulacion[j] = "Prom. encuentros no contagiados";
                    break;
                case 4: // Contagios internos
                    headerSimulacion[j] = "Contagiados Internos";
                    break;
                case 5: // Contagios Externos
                    headerSimulacion[j] = "Contagiados Externos";
                    break;
                default:// Contagios comunidad n
                    visitantesCont = comunidad.getNumComunidad() == visitantesCont ? visitantesCont + 1 : visitantesCont;
                    headerSimulacion[j] = "Contagiados Vis. Com." + (++visitantesCont);
                    break;
            }
        }
        headerSimulacion[headerSimulacion.length - 2] = "Infectados Totales";
        headerSimulacion[headerSimulacion.length - 1] = "Porcentaje Total";
        //Contenido
        //Parametros
        String[][] contenidoParametros = new String[][]{
            {"Número de días a simular", "" + formatoEntero.format(resultado.getNumDias())},
            {"Porcentaje de Viajeros 'V'", "" + formatDecimal.format(resultado.getPorcentajeViajeros()) + " %"},
            {"Coeficiente 'P'", "" + formatDecimal.format(resultado.getProbContagiosP()) + ""},
            {"Coeficiente 'E'", "" + formatoEntero.format(resultado.getPromContactosE())}
        };
        //Datos
        String[][] contenidoDatos = new String[resultado.getComunidades().length + 4][headerDatos.length];
        contenidoDatos[0][0] = "Población";
        contenidoDatos[1][0] = "Viajeros";
        contenidoDatos[2][0] = "Población Residente";
        for (int i = 3; i < (3 + resultado.getComunidades().length); i++) {
            Comunidad comunidad = resultado.getComunidades()[i - 3];
            contenidoDatos[i][0] = "Visitantes de la comunidad " + (comunidad.getNumComunidad() + 1);
        }

        for (int i = 0; i < resultado.getComunidades().length; i++) {
            int cont = 0;
            Comunidad comunidad = resultado.getComunidades()[i];
            contenidoDatos[cont++][i + 1] = formatoEntero.format(comunidad.getPoblacionTotal());
            contenidoDatos[cont++][i + 1] = formatoEntero.format(comunidad.getNumeroViajerosV());
            contenidoDatos[cont++][i + 1] = formatoEntero.format(comunidad.getResidentes());

            for (int j = 0; j < comunidad.getVisitantesComunidad().length; j++) {
                double visitantes = comunidad.getVisitantesComunidad()[j];
                contenidoDatos[cont++][i + 1] = visitantes == -1 ? "" : formatoEntero.format(visitantes);
            }
        }
        contenidoDatos[0][headerDatos.length - 1] = formatoEntero.format(resultado.getPoblacionTotal());

        //Simulacion
        String[][] contenidoSimulacion = new String[resultado.getNumDias() + 1][headerSimulacion.length];
        for (int i = 0; i < resultado.getNumDias(); i++) {
            visitantesCont = 0;
            contComunidad = 0;
            contenidoSimulacion[i+1][0] = "Día " + (i + 1);
            for (int j = 1; j < headerSimulacion.length - 2; j++) {
                int aux = j % (modulo);
                Comunidad comunidad = resultado.getComunidades()[contComunidad];
                switch (aux) {
                    case 0:
                        contenidoSimulacion[0][j-(modulo-1)] = comunidad.getIdComunidad();
                        contComunidad++;
                        visitantesCont = comunidad.getNumComunidad() == visitantesCont ? visitantesCont + 1 : visitantesCont;
                        if (comunidad.getNumComunidad() != visitantesCont) {
                            contenidoSimulacion[i+1][j] = formatoEntero.format(comunidad.getNumInfectadosExternosDiasComunidad()[i][visitantesCont++]);
                        }
                        visitantesCont = 0;
                        break;
                    case 1://Contagiados
                        contenidoSimulacion[i+1][j] = formatoEntero.format(comunidad.getNumInfectadosDia()[i]);
                        break;
                    case 2:// %
                        contenidoSimulacion[i+1][j] = formatDecimal.format(comunidad.getPorcentajeInfectados(i) * 100) + "%";
                        break;
                    case 3:// E
                        contenidoSimulacion[i+1][j] = formatDecimal.format(comunidad.calcularEdia(i, resultado.getPromContactosE()));
                        break;
                    case 4: // Contagios internos
                        contenidoSimulacion[i+1][j] = formatoEntero.format(comunidad.getNumInfectadosInternos()[i]);
                        break;
                    case 5: // Contagios Externos
                        contenidoSimulacion[i+1][j] = formatoEntero.format(comunidad.calcularInfectadosExternosTotales(i));
                        break;
                    default:// Contagios comunidad n
                        visitantesCont = comunidad.getNumComunidad() == visitantesCont ? visitantesCont + 1 : visitantesCont;
                        contenidoSimulacion[i+1][j] = formatoEntero.format(comunidad.getNumInfectadosExternosDiasComunidad()[i][visitantesCont++]);
                        break;
                }
            }
            contenidoSimulacion[i+1][headerSimulacion.length - 2] = formatoEntero.format(resultado.getTotalInfectados(i));
            contenidoSimulacion[i+1][headerSimulacion.length - 1] = formatDecimal.format(resultado.getPorcentajeInfectados(i)) + " %";
        }

        //Aplicando estilo color negrita a los encabezados
        CellStyle style = book.createCellStyle();
        Font font = book.createFont();

        font.setBold(
                true);//Seteando fuente negrita al encabezado del archivo excel
        style.setFont(font);
        

        //Generando el contenido de la hoja "Parámetros"
        generarContenido(contenidoParametros, parametros, headerParametros, style, -1);

        //Generando el contenido de la hoja "Datos"
        generarContenido(contenidoDatos, datos, headerDatos, style, -1);

        //Generando el contenido de la hoja "Simulacion"
        generarContenido(contenidoSimulacion, simulacion, headerSimulacion, style, modulo);
        File excelFile;
        excelFile = new File(path); // Referenciando a la ruta y el archivo Excel a crear
        try (FileOutputStream fileOuS = new FileOutputStream(excelFile)) {
            /**
             * if (excelFile.exists()) { // Si el archivo existe lo eliminaremos
             * excelFile.delete(); System.out.println("Archivo eliminado.!"); }*
             */
            book.write(fileOuS);
            fileOuS.flush();
            fileOuS.close();
            System.out.println("Archivo Creado.!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(excelFile.getAbsolutePath());

        return excelFile.getAbsolutePath();
    }

    /**
     * Método que inserta el contenido de cada hoja de la excel en su
     * @param contenido
     * @param sheet
     * @param header
     * @param style
     * @param modulo 
     */
    private static void generarContenido(String[][] contenido, XSSFSheet sheet, String[] header, CellStyle style, int modulo) {
        //Generando el contenido de la hoja "Parámetros"
        for (int i = 0; i <= contenido.length; i++) {
            XSSFRow row = sheet.createRow(i);//se crea las filas
            for (int j = 0; j < header.length; j++) {
                sheet.autoSizeColumn(j);
                if (i == 0) {//Recorriendo cabecera
                    XSSFCell cell = row.createCell(j);//Creando la celda de la cabecera en conjunto con la posición
                    cell.setCellStyle(style); //Añadiendo estilo a la celda creada anteriormente
                    cell.setCellValue(header[j]);//Añadiendo el contenido de nuestra lista
                } else if(i == 1 && modulo != -1){
                    XSSFCell cell = row.createCell(j);//Creando celda para el contenido
                    CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
                    cell.setCellValue(contenido[i - 1][j]); //Añadiendo el contenido
                }else{//para el contenido                    
                    XSSFCell cell = row.createCell(j);//Creando celda para el contenido
                    cell.setCellValue(contenido[i - 1][j]); //Añadiendo el contenido
                }
            }
            if (i == 1 && modulo != -1) {
                int aux = header.length / modulo;
                for (int k = 0; k < aux; k++) {
                    sheet.addMergedRegion(new CellRangeAddress(1, 1, k * (modulo) + 1, (k * (modulo) + modulo)));
                }
            }
        }
    }
}
