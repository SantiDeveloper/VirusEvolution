package virusexpansion;

/**
 *
 * @author Santiago Pérez Gómez
 */
public class Comunidad {

    
    String idComunidad;
    int numComunidad;
    double poblacionTotal;
    double numeroViajerosV;
    double[] visitantesComunidad;
    double[] numInfectadosDia;
    double[] numInfectadosInternos;
    double[][] numInfectadosExternosDiasComunidad;

    public Comunidad(double poblacionTotal, double porcentajeViajeros, int numDias, int numComunidades, String identificador, int numComunidad) {
        this.idComunidad = identificador;
        this.poblacionTotal = poblacionTotal;
        this.calcularNumViajeros(porcentajeViajeros);
        this.visitantesComunidad = new double[numComunidades];
        visitantesComunidad[numComunidad] = -1;
        this.numInfectadosDia = new double [numDias];
        this.numInfectadosInternos = new double [numDias];
        this.numInfectadosExternosDiasComunidad = new double [numDias][numComunidades];
        this.numComunidad = numComunidad;
    }

    /**
     * calcula el número de viajeros de la comunidad
     *
     * @param porcentajeViajeros
     */
    private void calcularNumViajeros(double porcentajeViajeros) {
        this.numeroViajerosV = (int)((porcentajeViajeros/100) * poblacionTotal);
    }

    /**
     * Calcula los infectados internos de la comunidad N(d)=N(d-1)*(1+E*P)
     *
     * @param dia: dia en el que se encuentra la pandemia.
     * @param p: coeficiente p (probabilidad de contagio por viajero)
     * @param e: coeficiente e (número de contactos que en promedio tenga cada
     * infectado con personas no infectadas)
     * @return
     */
    private double calculaInfectadosDiaComunidad(int dia, double p, double e) {
        return (this.numInfectadosDia[dia - 1] * (e * p)) ;
    }

    /**
     * Calcula los infectdos externos de la comunidad. Sumatorio de los
     * visitantes de todas las comunidades Nv= E*p*V*nOri/pOri
     *
     * @param dia: dia en el que se encuentra la pandemia.
     * @param p: coeficiente p (probabilidad de contagio por viajero).
     * @param e: coeficiente e (número de contactos que en promedio tenga cada
     * infectado con personas no infectadas).
     * @param nOri: número de infectados de la población origen.
     * @param pOri: población total de la comunidad origen.
     * @return
     */
    private double calcularInfectadosPorViajeros(int dia, double p, double e, double v, double nOri, double pOri) {
        if (dia == 0) {
            return 0;
        } else {
            return e * p * v * nOri / pOri;
        }
    }

    /**
     * Calcula los infectados totales de la comunidad en un día.
     *
     * @param dia: dia en el que se encuentra la pandemia.
     * @param p: coeficiente p (probabilidad de contagio por viajero).
     * @param e: coeficiente e (número de contactos que en promedio tenga cada
     * infectado con personas no infectadas).
     * @param comunidades: comunidades vecinas
     * @param primerInfectado: Indica si el primer infectado se produce en esta comunidad
     * @return
     */
    public double calcularInfectadosDiaTotal(int dia, double p, double e, Comunidad[] comunidades, boolean primerInfectado) {

        if (dia == 0) {
            numInfectadosInternos[dia] = primerInfectado ? 1 : 0;
            for (int nComunidad = 0; nComunidad < comunidades.length; nComunidad++) {
                if (nComunidad != numComunidad) {
                    numInfectadosExternosDiasComunidad[dia][nComunidad] = 0;
                } else {
                    numInfectadosExternosDiasComunidad[dia][nComunidad] = -1;
                }
            }
            numInfectadosDia[dia] = numInfectadosInternos[dia] + calcularInfectadosExternosTotales(dia);
        } else {
            numInfectadosInternos[dia] = this.calculaInfectadosDiaComunidad(dia, p, calcularEdia(dia, e));
            for (int nComunidad = 0; nComunidad < comunidades.length; nComunidad++) {
                if (nComunidad != numComunidad) {
                    Comunidad comunidad = comunidades[nComunidad];
                    numInfectadosExternosDiasComunidad[dia][nComunidad] = this.calcularInfectadosPorViajeros(dia, p, calcularEdia(dia, e), visitantesComunidad[nComunidad], comunidad.getNumInfectadosDia()[dia - 1], comunidad.getPoblacionTotal());
                } else {
                    numInfectadosExternosDiasComunidad[dia][nComunidad] = -1;
                }
            }
            double infectadosTotales = this.numInfectadosDia[dia - 1] + numInfectadosInternos[dia] + calcularInfectadosExternosTotales(dia);
            numInfectadosDia[dia] = infectadosTotales >= this.poblacionTotal ? this.poblacionTotal : infectadosTotales;
        }
        return numInfectadosDia[dia];
    }

    /**
     * calcula los infectados externos totales al día
     * @param dia
     * @return 
     */
    public double calcularInfectadosExternosTotales(int dia) {
        double infectadosExternosDia = 0;
        for (int i = 0; i < numInfectadosExternosDiasComunidad[dia].length; i++) {
            if (numInfectadosExternosDiasComunidad[dia][i] != -1) {
                infectadosExternosDia += numInfectadosExternosDiasComunidad[dia][i];
            }
        }
        return infectadosExternosDia;
    }

    /**
     * Calcula el coeficiente e en relación al día y al número de infectados
     *
     * @param dia
     * @param e
     * @return
     */
    public double calcularEdia(int dia, double e) {
        if(dia == 0){
            return e;
        }else{
            return numInfectadosDia[dia - 1] == this.poblacionTotal ? 0 : e * (1 - (numInfectadosDia[dia - 1] / this.poblacionTotal));
        }
    }
    
    public double getResidentes(){
        return this.poblacionTotal - this.numeroViajerosV;
    }

    public double getPorcentajeInfectados(int dia) {
        return numInfectadosDia[dia] / poblacionTotal;
    }

    public String getIdComunidad() {
        return idComunidad;
    }

    public void setIdComunidad(String idComunidad) {
        this.idComunidad = idComunidad;
    }

    public double getPoblacionTotal() {
        return poblacionTotal;
    }

    public void setPoblacionTotal(double poblacionTotal) {
        this.poblacionTotal = poblacionTotal;
    }

    public double getNumeroViajerosV() {
        return numeroViajerosV;
    }

    public void setNumeroViajerosV(double numeroViajerosV) {
        this.numeroViajerosV = numeroViajerosV;
    }

    public double[] getNumInfectadosDia() {
        return numInfectadosDia;
    }

    public void setNumInfectadosDia(double[] numInfectadosDia) {
        this.numInfectadosDia = numInfectadosDia;
    }

    public double[] getNumInfectadosInternos() {
        return numInfectadosInternos;
    }

    public void setNumInfectadosInternos(double[] numInfectadosInternos) {
        this.numInfectadosInternos = numInfectadosInternos;
    }

    public int getNumComunidad() {
        return numComunidad;
    }

    public void setNumComunidad(int numComunidad) {
        this.numComunidad = numComunidad;
    }

    public double[] getVisitantesComunidad() {
        return visitantesComunidad;
    }

    public void setVisitantesComunidad(double[] visitantesComunidad) {
        this.visitantesComunidad = visitantesComunidad;
    }

    public double[][] getNumInfectadosExternosDiasComunidad() {
        return numInfectadosExternosDiasComunidad;
    }

    public void setNumInfectadosExternosDiasComunidad(double[][] numInfectadosExternosDiasComunidad) {
        this.numInfectadosExternosDiasComunidad = numInfectadosExternosDiasComunidad;
    }

}
