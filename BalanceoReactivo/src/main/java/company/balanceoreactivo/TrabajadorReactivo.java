package company.balanceoreactivo;

import java.awt.image.BufferedImage;

public class TrabajadorReactivo implements Runnable {
    private final BufferedImage imagen;
    private final ColaTareas colaTareas;

    public TrabajadorReactivo(BufferedImage imagen, ColaTareas colaTareas) {
        this.imagen = imagen;
        this.colaTareas = colaTareas;
    }

    @Override
    public void run() {
        // Los hilos toman tareas dinámicamente
        while (true) {
            int[] tarea = colaTareas.obtenerTarea();  // Obtener una tarea de la cola
            if (tarea == null) {
                break;  // Si no hay más tareas, salir del bucle
            }

            int inicioFila = tarea[0];
            int finFila = tarea[1];

            // Procesar las filas de la imagen
            for (int y = inicioFila; y < finFila; y++) {
                for (int x = 0; x < imagen.getWidth(); x++) {
                    int rgba = imagen.getRGB(x, y);
                    int alpha = (rgba >> 24) & 0xFF; // Componente alfa
                    int rojo = (rgba >> 16) & 0xFF; // Componente rojo
                    int verde = (rgba >> 8) & 0xFF;  // Componente verde
                    int azul = rgba & 0xFF;        // Componente azul

                    // Invertir los colores
                    int rojoInvertido = 255 - rojo;
                    int verdeInvertido = 255 - verde;
                    int azulInvertido = 255 - azul;

                    // Combinar los componentes invertidos
                    int rgbaInvertido = (alpha << 24) | (rojoInvertido << 16) | (verdeInvertido << 8) | azulInvertido;
                    imagen.setRGB(x, y, rgbaInvertido);
                }
            }
        }
    }
}

