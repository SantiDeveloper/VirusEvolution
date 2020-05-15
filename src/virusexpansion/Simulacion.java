package virusexpansion;

import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Santiago Pérez Gómez
 */
public class Simulacion {

    String idSimulacion;
    Comunidad[] comunidades;
    int numDias;
    long porcentajeViajeros;
    int promContactosE;
    long probContagiosP;
    long poblacionTotal;
   
    public Simulacion(){
        
    }

    /**
     * Método con el que se ejecutarán los procedimientos correspondientes para
     * calcular las cifras de los contagios a lo largo de los días.
     */
   public void iniciarSimulacion(){
       
       
   }

   /**
    * Método por el que se pedirán los parámetros de cada comunidad por pantalla.
    */
   public void cargarParametros(){ 
       this.poblacionTotal = 0;
       boolean error = false;
       Scanner in = new Scanner(System.in);
       System.out.println("Por favor, introduzca los datos que se vaya solicitando por pantalla.");
       System.out.println("- Introduzca el número de días a simular:");
       this.numDias = in.nextInt();
       if(numDias < 1){
           System.err.println("Número incorrecto de días a tratar.");
           error = true;
       }
       System.out.println("- Introduzca el porcentaje de viajeros para cada comunidad:");
       this.porcentajeViajeros = in.nextLong();
       if(porcentajeViajeros < 0){
           System.err.println("Porcentaje de viajeros a tratar incorrecto.");
           error = true;
       }
       System.out.println("- Introduzca el coeficiente \"E\" (número de contactos"
               + "\nque en promedio tenga cada infectado con personas no infectadas): ");
       this.promContactosE= in.nextInt();
       if(promContactosE < 1){
           System.err.println("Coeficiente \"E\" a tratar, incorrecto.");
           error = true;
       }
       System.out.println("- Introduca el coeficiente \"p\" (probabilidad de infectarse con un contacto):");
       this.probContagiosP = in.nextLong();
       if(probContagiosP < 1){
           System.err.println("Coeficiente \"P\" a tratar, incorrecto.");
           error = true;
       }
       System.out.println("- Introduzca el número de comunidades que se van a tratar en la simulación:");
       int numComunidades =in.nextInt();
       if(numComunidades < 1){
           System.err.println("Número incorrecto de comunidades a tratar.");
           error = true;
       }
       
       this.comunidades = new Comunidad[numComunidades];
       Comunidad aux = null;
       String identificadorComunidad = null;
       long poblacionComunidad = -1l;
       for (int i = 0; (i < numComunidades && !error); i++) { 
           System.out.println("****************** Comunidad "+(i+1)+" ***************");
           System.out.println("**  Introduzca el identificador de la Comunidad:    **");
           identificadorComunidad = in.nextLine();
           System.out.println("**  Introduzca la población total de la comunidad:  **");
           poblacionComunidad= (long) in.nextLong();
           if(poblacionComunidad < 1){
               System.err.println("Población incorrecta");
               error = true;
           }
           aux = new Comunidad(poblacionComunidad,porcentajeViajeros,numDias,numComunidades,identificadorComunidad,i);
           poblacionTotal += aux.getPoblacionTotal();
           comunidades[i]= aux;
       }
       in.close();
       this.calcularVisitantesComunidades();
   }
    
    private void calcularVisitantesComunidades(){
        for (int i = 0; i < comunidades.length; i++) {
            Comunidad comunidad = comunidades[i];
            long[] visitantes = comunidad.getVisitantes();
            for (int j = 0; j < visitantes.length; j++) {
                if(i==j){
                    visitantes[i] = -1;
                }else{
                    Comunidad comunidadVecina = comunidades[j];
                    long numV = comunidadVecina.getNumeroViajerosV();
                    long pobTotalVecina = comunidadVecina.getPoblacionTotal();
                    long pobTotal = comunidad.getPoblacionTotal();
                    visitantes[j] = ((numV*pobTotal)/(this.poblacionTotal-pobTotalVecina));
                }
            }
            comunidad.setVisitantes(visitantes);
        }
    }
    public String getIdSimulacion() {
        return idSimulacion;
    }

    public void setIdSimulacion(String idSimulacion) {
        this.idSimulacion = idSimulacion;
    }

    public Comunidad [] getComunidades() {
        return comunidades;
    }

    public void setComunidades(Comunidad [] comunidades) {
        this.comunidades = comunidades;
    }

    public int getNumDias() {
        return numDias;
    }

    public void setNumDias(int numDias) {
        this.numDias = numDias;
    }

    public long getPorcetajeViajeros() {
        return porcentajeViajeros;
    }

    public void setPorcetajeViajeros(long porcetajeViajeros) {
        this.porcentajeViajeros = porcetajeViajeros;
    }

    public int getPromContactosE() {
        return promContactosE;
    }

    public void setPromContactosE(int promContactosE) {
        this.promContactosE = promContactosE;
    }

    public long getProbContagiosP() {
        return probContagiosP;
    }

    public void setProbContagiosP(long probContagiosP) {
        this.probContagiosP = probContagiosP;
    }

    public long getPorcentajeViajeros() {
        return porcentajeViajeros;
    }

    public void setPorcentajeViajeros(long porcentajeViajeros) {
        this.porcentajeViajeros = porcentajeViajeros;
    }

    public long getPoblacionTotal() {
        return poblacionTotal;
    }

    public void setPoblacionTotal(long poblacionTotal) {
        this.poblacionTotal = poblacionTotal;
    }
    
    
}
