package company.balanceoreactivo;

import java.util.concurrent.*;

public class ColaTareas {
    private final int filasTotales;
    private final int numeroHilos;
    private final BlockingQueue<int[]> tareas;

    public ColaTareas(int filasTotales, int numeroHilos) {
        this.filasTotales = filasTotales;
        this.numeroHilos = numeroHilos;
        this.tareas = new LinkedBlockingQueue<>();

        // Dividir las tareas entre los hilos
        int filasPorHilo = filasTotales / numeroHilos;
        for (int i = 0; i < numeroHilos; i++) {
            int inicioFila = i * filasPorHilo;
            int finFila = (i == numeroHilos - 1) ? filasTotales : inicioFila + filasPorHilo;
            tareas.add(new int[]{inicioFila, finFila});
        }
    }

    public int[] obtenerTarea() {
        try {
            return tareas.poll(10, TimeUnit.MILLISECONDS);  // Hacer una espera corta si no hay tareas
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
