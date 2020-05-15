package virusexpansion;

/**
 *
 * @author Santiago Pérez Gómez
 */
public class Comunidad {

    String idComunidad;
    long poblacionTotal;
    long numeroViajerosV;
    long [] visitantes;
    int [] numInfectadosDia;
    int [] numInfectadosInternos;
    int [] numInfectadosExternos;

    public Comunidad(long poblacionTotal, long porcentajeViajeros, int numDias, int numComunidades,String identificador,int numComunidad) {
        this.idComunidad = identificador;
        this.poblacionTotal = poblacionTotal;
        this.calcularNumViajeros(porcentajeViajeros);
        this.visitantes = new long[numComunidades];
        visitantes[numComunidad] = -1;
        this.numInfectadosDia = new int [numDias];
    }

    /**
     * calcula el número de viajeros de la comunidad
     * @param porcentajeViajeros 
     */
    private void calcularNumViajeros(long porcentajeViajeros){
        this.numeroViajerosV = (porcentajeViajeros * poblacionTotal);
    }
    
    /**
     * Calcula los infectados internos de la comunidad
     * N(d)=N(d-1)*(1+E*P)
     * @param dia: dia en el que se encuentra la pandemia.
     * @param p: coeficiente p (probabilidad de contagio por viajero)
     * @param e: coeficiente e (número de contactos que en promedio tenga cada infectado con personas no infectadas)
     * @return 
     */
    private int calculaInfectadosDiaComunidad(int dia, long p, long e){
        //TODO:
        return -1;
    }
    
    /**
     * Calcula los infectdos externos de la comunidad.
     * Nv= E*p*V*nOri/pOri
     * @param dia: dia en el que se encuentra la pandemia.
     * @param p: coeficiente p (probabilidad de contagio por viajero).
     * @param e: coeficiente e (número de contactos que en promedio tenga cada infectado con personas no infectadas).
     * @param nOri: número de infectados de la población origen.
     * @param pOri: población total de la comunidad origen.
     * @return 
     */
    private int calcularInfectadosPorViajeros(int dia, long p,long e, int nOri, int pOri){
        //TODO:
        return -1;
    }
    
    /**
     * Calcula los infectados totales de la comunidad en un día.
     * @param dia: dia en el que se encuentra la pandemia.
     * @param p: coeficiente p (probabilidad de contagio por viajero).
     * @param e: coeficiente e (número de contactos que en promedio tenga cada infectado con personas no infectadas).
     * @param nOri: número de infectados de la población origen.
     * @param pOri: población de la comunidad origen.
     * @return 
     */
    public int calcularInfectadosDiaTotal(int dia, long p, int e, int nOri, int pOri){
        numInfectadosInternos[dia] = this.calculaInfectadosDiaComunidad(dia, p, calcularEdia(dia,e));
        numInfectadosExternos[dia] = this.calcularInfectadosPorViajeros(dia, p, calcularEdia(dia,e), nOri, pOri);
        numInfectadosDia[dia] = numInfectadosInternos[dia] + numInfectadosExternos[dia];
        return numInfectadosDia[dia];
    }
    
    /**
     * Calcula el coeficiente e en relación al día y al número de infectados
     * @param dia
     * @param e
     * @return 
     */
    private long calcularEdia(int dia,int e){
        return numInfectadosDia[dia] == this.poblacionTotal ? 0 : e*(1-(numInfectadosDia[dia]/this.poblacionTotal));
    }
    public long getPorcentajeInfectados(int dia){
        return numInfectadosDia[dia]/poblacionTotal;
    }
    public String getIdComunidad() {
        return idComunidad;
    }

    public void setIdComunidad(String idComunidad) {
        this.idComunidad = idComunidad;
    }

    public long getPoblacionTotal() {
        return poblacionTotal;
    }

    public void setPoblacionTotal(long poblacionTotal) {
        this.poblacionTotal = poblacionTotal;
    }

    public long getNumeroViajerosV() {
        return numeroViajerosV;
    }

    public void setNumeroViajerosV(long numeroViajerosV) {
        this.numeroViajerosV = numeroViajerosV;
    }

    public long[] getVisitantes() {
        return visitantes;
    }

    public void setVisitantes(long[] visitantes) {
        this.visitantes = visitantes;
    }

    public int[] getNumInfectadosDia() {
        return numInfectadosDia;
    }

    public void setNumInfectadosDia(int[] numInfectadosDia) {
        this.numInfectadosDia = numInfectadosDia;
    }

    public int[] getNumInfectadosInternos() {
        return numInfectadosInternos;
    }

    public void setNumInfectadosInternos(int[] numInfectadosInternos) {
        this.numInfectadosInternos = numInfectadosInternos;
    }

    public int[] getNumInfectadosExternos() {
        return numInfectadosExternos;
    }

    public void setNumInfectadosExternos(int[] numInfectadosExternos) {
        this.numInfectadosExternos = numInfectadosExternos;
    }
    
    
    
}
