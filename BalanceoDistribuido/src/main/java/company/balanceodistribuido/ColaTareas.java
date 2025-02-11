package company.balanceodistribuido;

import java.util.LinkedList;
import java.util.Queue;

public class ColaTareas {
    private final Queue<int[]> tareas;

    public ColaTareas(int filasTotales, int numeroTrabajadores) {
        this.tareas = new LinkedList<>();

        int filasPorTarea = 10; // Dividir en bloques de 10 filas (ajustable)
        for (int i = 0; i < filasTotales; i += filasPorTarea) {
            int finFila = Math.min(i + filasPorTarea, filasTotales);
            tareas.add(new int[]{i, finFila});
        }
    }

    public synchronized int[] obtenerTarea() {
        return tareas.poll(); // Retorna una tarea o null si no hay más tareas
    }

    public synchronized boolean estaVacia() {
        return tareas.isEmpty();
    }
}
