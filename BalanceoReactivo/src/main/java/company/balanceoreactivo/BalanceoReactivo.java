package company.balanceoreactivo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.*;
import javax.imageio.ImageIO;

public class BalanceoReactivo {

    private static final String INPUT_FOLDER = "imagenesSinFiltro";
    private static final String OUTPUT_FOLDER = "imagenesConFiltro";

    public static void main(String[] args) {
        long startTime = System.nanoTime(); // Comienza a medir el tiempo

        File inputFolder = new File(INPUT_FOLDER);
        File[] imageFiles = inputFolder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No hay imágenes en la carpeta " + INPUT_FOLDER);
            return;
        }

        // Número de hilos en el pool (ajustar según la carga)
        int numeroHilos = 1;

        // Crear un pool de hilos
        ExecutorService threadPool = Executors.newFixedThreadPool(numeroHilos);

        // Procesar cada imagen
        for (File imageFile : imageFiles) {
            try {
                BufferedImage imagen = ImageIO.read(imageFile);
                int altura = imagen.getHeight();
                int ancho = imagen.getWidth();

                // Crear la cola de tareas
                ColaTareas colaTareas = new ColaTareas(altura, numeroHilos);

                // Enviar las tareas al pool de hilos
                for (int i = 0; i < numeroHilos; i++) {
                    threadPool.submit(new TrabajadorReactivo(imagen, colaTareas)); // Reutiliza los hilos
                }

                // Esperar a que todas las tareas se completen antes de continuar
                // Pero solo cerrar el pool después de que todas las imágenes se hayan procesado
                // Y NO llamar a shutdown() aquí, sino al final del ciclo de procesamiento de imágenes

                // Guardar la imagen procesada
                String outputFilePath = OUTPUT_FOLDER + File.separator + imageFile.getName();
                File outputFile = new File(outputFilePath);
                ImageIO.write(imagen, "png", outputFile);

                System.out.println("Imagen procesada y guardada: " + outputFilePath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow(); 
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow(); 
        }

        long endTime = System.nanoTime(); // Finaliza la medición
        long duration = (endTime - startTime); // Calcula el tiempo transcurrido

        System.out.println("Tiempo total de procesamiento: " + duration / 1_000_000.0 + " ms"); // Convierte a milisegundos
    }
}
