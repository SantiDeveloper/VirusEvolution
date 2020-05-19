package virusexpansion;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javafx.util.Pair;
import utilidades.FicheroUtils;

/**
 *
 * @author Santiago Pérez Gómez
 */
public class Simulacion {

    String idSimulacion;
    Comunidad[] comunidades;
    int numDias;
    double porcentajeViajeros;
    double promContactosE;
    double probContagiosP;
    double poblacionTotal;

    /**
     * Método con el que se ejecutarán los procedimientos correspondientes para
     * calcular las cifras de los contagios a lo largo de los días.
     * @param path directorio de salida donde se almacenará el fichero
     */
    public void iniciarSimulacion(String path) {

        System.out.println("Se inicia simulación");
        boolean primerInfectado;
        Random aleatorio = new Random(System.currentTimeMillis());
        // Producir nuevo int aleatorio entre 0 y numComunidades
        int intAletorio = aleatorio.nextInt(comunidades.length-1);
        for (int dia = 0; dia < numDias; dia++) {
            for (int i = 0; i < comunidades.length; i++) {
                primerInfectado = i == intAletorio;
                Comunidad comunidad = comunidades[i];
                comunidad.calcularInfectadosDiaTotal(dia, probContagiosP, promContactosE, comunidades, primerInfectado);
            }
        }
        System.out.println("Fin de la simulación");
        System.out.println("Resultado");
        FicheroUtils.generarTabla(this, path);
        System.out.println("Fin Resultado");
    }

    /**
     * Se encarga de almacenar los parámetros generales necesarios para la simulación.
     * @param numDias
     * @param porcentajeViajeros
     * @param promContactosE
     * @param probContagiosP
     * @param numComunidades 
     */
    public void cargarParametrosFrontGenerales(int numDias, double porcentajeViajeros, double promContactosE, double probContagiosP, int numComunidades) {
        this.numDias = numDias;
        this.porcentajeViajeros = porcentajeViajeros;
        this.promContactosE = promContactosE;
        this.probContagiosP = probContagiosP;
        this.comunidades = new Comunidad[numComunidades];
    }

    /**
     * Se encarga de almacenar los paráemtros de cada comunidad necesarios para la simulación
     * 
     * @param pairComunidades 
     */
    public void cargarParametrosFrontComunidad(List<Pair<String, Double>> pairComunidades) {
        this.poblacionTotal = 0;
        int numComunidades = pairComunidades.size();
        Comunidad aux;
        String identificadorComunidad;
        double poblacionComunidad;
        for (int i = 0; (i < pairComunidades.size()); i++) {
            Pair<String, Double> pair = pairComunidades.get(i);
            identificadorComunidad = pair.getKey();
            poblacionComunidad = pair.getValue();
            aux = new Comunidad(poblacionComunidad, porcentajeViajeros, numDias, numComunidades, identificadorComunidad, i);
            poblacionTotal += aux.getPoblacionTotal();
            comunidades[i] = aux;
        }
        this.calcularVisitantesComunidades();
    }

    /**
     * Método por el que se pedirán los parámetros de cada comunidad por
     * pantalla.
     */
    public void cargarParametros() {
        this.poblacionTotal = 0;
        boolean error = false;
        Scanner in = new Scanner(System.in);
        System.out.println("Por favor, introduzca los datos que se vaya solicitando por pantalla.");
        System.out.println("- Introduzca el número de días a simular:");
        this.numDias = in.nextInt();
        if (numDias < 1) {
            System.err.println("Número incorrecto de días a tratar.");
            error = true;
        }
        System.out.println("- Introduzca el porcentaje de viajeros para cada comunidad:");
        this.porcentajeViajeros = in.nextDouble();
        if (porcentajeViajeros < 0) {
            System.err.println("Porcentaje de viajeros a tratar incorrecto.");
            error = true;
        }
        System.out.println("- Introduzca el coeficiente \"E\" (número de contactos"
                + "\nque en promedio tenga cada infectado con personas no infectadas): ");
        this.promContactosE = in.nextDouble();
        if (promContactosE < 1) {
            System.err.println("Coeficiente \"E\" a tratar, incorrecto.");
            error = true;
        }
        System.out.println("- Introduca el coeficiente \"p\" (probabilidad de infectarse con un contacto):");
        this.probContagiosP = in.nextDouble();
        if (probContagiosP < 0) {
            System.err.println("Coeficiente \"P\" a tratar, incorrecto.");
            error = true;
        }
        System.out.println("- Introduzca el número de comunidades que se van a tratar en la simulación:");
        int numComunidades = in.nextInt();
        if (numComunidades < 1) {
            System.err.println("Número incorrecto de comunidades a tratar.");
            error = true;
        }

        this.comunidades = new Comunidad[numComunidades];
        Comunidad aux;
        String identificadorComunidad;
        double poblacionComunidad;
        for (int i = 0; (i < numComunidades && !error); i++) {
            in.nextLine();
            System.out.println("****************** Comunidad " + (i + 1) + " ***************");
            System.out.println("**  Introduzca el identificador de la Comunidad:    **");
            identificadorComunidad = in.nextLine();
            System.out.println("**  Introduzca la población total de la comunidad:  **");
            poblacionComunidad = (double) in.nextDouble();
            if (poblacionComunidad < 1) {
                System.err.println("Población incorrecta");
                error = true;
            }
            aux = new Comunidad(poblacionComunidad, porcentajeViajeros, numDias, numComunidades, identificadorComunidad, i);
            poblacionTotal += aux.getPoblacionTotal();
            comunidades[i] = aux;
        }
        in.close();
        this.calcularVisitantesComunidades();
    }

    /**
     * Este método se encarga de calcular los visitantes que hay en cada comunidad
     * procedentes del resto de comunidades
     */
    private void calcularVisitantesComunidades() {
        for (int i = 0; i < comunidades.length; i++) {
            Comunidad comunidad = comunidades[i];
            double[] visitantes = comunidad.getVisitantesComunidad();
            for (int j = 0; j < visitantes.length; j++) {
                if (i == j) {
                    visitantes[j] = -1;
                } else {
                    Comunidad comunidadVecina = comunidades[j];
                    double numV = comunidadVecina.getNumeroViajerosV();
                    double pobTotalVecina = comunidadVecina.getPoblacionTotal();
                    double pobTotal = comunidad.getPoblacionTotal();
                    visitantes[j] = ((numV * pobTotal) / (this.poblacionTotal - pobTotalVecina));
                }
            }
            comunidad.setVisitantesComunidad(visitantes);
        }
    }

    /**
     * Calcula los infectados totales en un día
     * @param dia
     * @return 
     */
    public double getTotalInfectados(int dia) {
        double total = 0;

        for (Comunidad comunidad : comunidades) {
            total += comunidad.getNumInfectadosDia()[dia];
        }
        return total;
    }

    public double getPorcentajeInfectados(int dia) {
        return getTotalInfectados(dia) / poblacionTotal * 100;
    }

    public String getIdSimulacion() {
        return idSimulacion;
    }

    public void setIdSimulacion(String idSimulacion) {
        this.idSimulacion = idSimulacion;
    }

    public Comunidad[] getComunidades() {
        return comunidades;
    }

    public void setComunidades(Comunidad[] comunidades) {
        this.comunidades = comunidades;
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }

    public double getPorcetajeViajeros() {
        return porcentajeViajeros;
    }

    public void setPorcetajeViajeros(long porcetajeViajeros) {
        this.porcentajeViajeros = porcetajeViajeros;
    }

    public double getPromContactosE() {
        return promContactosE;
    }

    public void setPromContactosE(int promContactosE) {
        this.promContactosE = promContactosE;
    }

    public double getProbContagiosP() {
        return probContagiosP;
    }

    public void setProbContagiosP(long probContagiosP) {
        this.probContagiosP = probContagiosP;
    }

    public double getPorcentajeViajeros() {
        return porcentajeViajeros;
    }

    public void setPorcentajeViajeros(long porcentajeViajeros) {
        this.porcentajeViajeros = porcentajeViajeros;
    }

    public double getPoblacionTotal() {
        return poblacionTotal;
    }

    public void setPoblacionTotal(long poblacionTotal) {
        this.poblacionTotal = poblacionTotal;
    }

}
