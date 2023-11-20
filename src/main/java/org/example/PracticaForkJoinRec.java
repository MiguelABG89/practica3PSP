package org.example;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**********************************************************************************************************************************************
 *   APLICACIÓN: "MaximoForkJoin"                                                                                                             *
 **********************************************************************************************************************************************
 *   PROGRAMACIÓN DE SERVICIOS Y PROCESOS 2DAM                                                                                                *
 **********************************************************************************************************************************************
 *   @author  Miguel Ángel Brand Gaviria                                                                                                      *
 *   @version 1.0 Version final                                                                                                               *
 *   @since   13/11/2023                                                                                                                      *
 **********************************************************************************************************************************************
 *   COMENTARIOS:                                                                                                                             *
 *      -Genera un Array de numeros que avarca del 0 hasta el 1000, desde ahi se divide el array en dos tareas para poder imprimir la media   *
 *      -de cada parte como corresponde                                                                                                       *
 **********************************************************************************************************************************************/
public class PracticaForkJoinRec {
    //Aqui definimos la costantes para la longitud del array y los atributos
    private static final int LONGITUDARRAY = 1_000;
    private static final double UMBRAL = 750;
    class ForkJoin extends RecursiveTask<Double> {

        private int[] a_ArrayDatos;
        private double a_Inicio = 0, a_Fin = 0;

        //constructor para darles los valores a los atributos
        public ForkJoin(int[] p_Array, double p_Inicio, double p_Fin) {
            this.a_ArrayDatos = p_Array;
            this.a_Inicio = p_Inicio;
            this.a_Fin = p_Fin;
        }
        private double MediaRec(){
            double l_Medio = ( (a_Inicio + a_Fin) / 2 ) ;
            ForkJoin l_Tarea1 = new ForkJoin(a_ArrayDatos, a_Inicio, l_Medio);;
            ForkJoin l_Tarea2 = new ForkJoin(a_ArrayDatos, l_Medio, a_Fin);

            // No tratamos el caso trivial pues cortaremos la recursividad en el UMBRAL.
            l_Tarea1.fork();
            l_Tarea2.fork();

            return l_Medio;
        }
        private double MediaIter(){
            double l_Media = 0;
            int l_Contador = 0;
            int l_Contador2 = 0;

            //aqui es donde se realiza el calculo de la media de cada una de las mitades y posteriormente devuelve la media al main
            for (l_Contador = (int) a_Inicio; l_Contador < a_Fin; l_Contador++) {
                l_Media = l_Media + a_ArrayDatos[l_Contador];
                l_Contador2++;
            }
            l_Media = l_Media / l_Contador2;
            return l_Media;
        }

        //metodo para darle valores al array
        private int[] crearArray() {
            int[] l_ArrayDatos = new int[LONGITUDARRAY];
            int l_Contador = 0;

            //Aqui es donde se asignan los valores a cada posicion del array con el contador
            for (l_Contador = 0; l_Contador < LONGITUDARRAY; l_Contador++) {
                l_ArrayDatos[l_Contador] = l_Contador;
            }

            return (l_ArrayDatos);
        }

        //metodo compute para llamar a la tarea
        protected Double compute() {
            Double l_Retorno;

            if( (a_Fin - a_Inicio) <= UMBRAL ) l_Retorno = MediaIter();   // Ejecución secuencial/iterativa.
            else l_Retorno = MediaRec();   // Ejecución recursiva.

            return (l_Retorno);
        }
    }
    public static void main(String[] args) {
        //Definimos las variables en este orden para poder realizar las llamadas de forma correcta

        int[] l_Data = null;
        ForkJoin l_Tarea = new PracticaForkJoinRec().new ForkJoin(l_Data,0,0);
        ForkJoinPool l_Pool = new ForkJoinPool();

        l_Data = l_Tarea.crearArray();
        double l_Inicio = 0;
        double l_Fin = l_Data.length;

        double l_Media1 = 0;

        // Crea la tarea, la lanza, y obtiene el resultado "invoke".
        ForkJoin l_Tarea1 = new PracticaForkJoinRec().new ForkJoin(l_Data, l_Inicio, l_Fin);
        l_Media1 = l_Pool.invoke(l_Tarea1);

        //Imprimimos los resultados optenidos con el invoke
        System.out.println("la primera media es: " + l_Media1);
    }

}