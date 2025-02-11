package company.balanceodistribuido;

import java.awt.image.BufferedImage;

class TrabajadorInvertirColores implements Runnable {
    private final BufferedImage imagen;
    private final ColaTareas colaTareas;

    public TrabajadorInvertirColores(BufferedImage imagen, ColaTareas colaTareas) {
        this.imagen = imagen;
        this.colaTareas = colaTareas;
    }

    @Override
    public void run() {
        boolean primerTrabajo = true; // Indica si es la primera tarea del hilo

        while (true) {
            int[] tarea = colaTareas.obtenerTarea();
            if (tarea == null) {
                break; // No hay más tareas, el hilo puede terminar
            }

            int inicioFila = tarea[0];
            int finFila = tarea[1];

            synchronized (System.out) { // Asegura que el mensaje salga ordenado
                if (!primerTrabajo) {
                    System.out.println(Thread.currentThread().getName() + " robó una tarea.");
                }
                System.out.println(Thread.currentThread().getName() + " procesando filas " + inicioFila + " a " + finFila);
            }

            // Procesar las filas de la imagen
            for (int y = inicioFila; y < finFila; y++) {
                for (int x = 0; x < imagen.getWidth(); x++) {
                    int rgba = imagen.getRGB(x, y);
                    int alpha = (rgba >> 24) & 0xFF;
                    int rojo = (rgba >> 16) & 0xFF;
                    int verde = (rgba >> 8) & 0xFF;
                    int azul = rgba & 0xFF;

                    // Invertir los colores
                    int rojoInvertido = 255 - rojo;
                    int verdeInvertido = 255 - verde;
                    int azulInvertido = 255 - azul;

                    // Combinar los componentes invertidos
                    int rgbaInvertido = (alpha << 24) | (rojoInvertido << 16) | (verdeInvertido << 8) | azulInvertido;
                    imagen.setRGB(x, y, rgbaInvertido);
                }
            }

            primerTrabajo = false; // Después del primer trabajo, el hilo puede robar tareas
        }
    }
}

