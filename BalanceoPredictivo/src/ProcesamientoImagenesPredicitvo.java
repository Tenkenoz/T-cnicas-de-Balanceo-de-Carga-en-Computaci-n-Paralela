/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Alexander
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ProcesamientoImagenesPredicitvo {
    public static void main(String[] args) {
        // Ruta de la carpeta que contiene las imágenes
        String inputDirPath = "imagenesSinFiltro";
        // Ruta de la carpeta donde se guardarán las imágenes modificadas
        String outputDirPath = "imagenesConFiltro";

        File inputDir = new File(inputDirPath);
        File outputDir = new File(outputDirPath);

        // Crear la carpeta de salida si no existe
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        // Obtener todas las imágenes de la carpeta
        File[] files = inputDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png"));

        if (files != null) {
            // Obtener el número de núcleos disponibles en el sistema
            int numCores = 1;

            // Crear un pool de hilos con un número de hilos igual a los núcleos del sistema
            ExecutorService executorService = Executors.newFixedThreadPool(numCores);

            // Usamos una lista de tareas
            List<Callable<Void>> tasks = new ArrayList<>();

            long startTime = System.nanoTime(); // Empieza a medir el tiempo de ejecución

            for (File file : files) {
                tasks.add(() -> {
                    try {
                        // Cargar la imagen
                        BufferedImage img = ImageIO.read(file);

                        // Modificar los colores de la imagen
                        BufferedImage modifiedImg = cambiarColores(img);

                        // Guardar la imagen modificada en la carpeta de salida
                        String outputFileName = outputDirPath + "/" + file.getName();
                        ImageIO.write(modifiedImg, "PNG", new File(outputFileName));

                        System.out.println("Imagen procesada: " + file.getName());
                    } catch (IOException e) {
                        System.err.println("Error al procesar la imagen: " + file.getName());
                        e.printStackTrace();
                    }
                    return null;
                });
            }

            try {
                // Ejecutar todas las tareas en paralelo utilizando el pool de hilos
                executorService.invokeAll(tasks);
            } catch (InterruptedException e) {
                System.err.println("Error al ejecutar las tareas en paralelo.");
                e.printStackTrace();
            } finally {
                // Cerrar el pool de hilos después de que todas las tareas han sido ejecutadas
                executorService.shutdown();
            }

            long endTime = System.nanoTime(); // Finaliza la medición
            long duration = (endTime - startTime); // Calcula el tiempo transcurrido

            System.out.println("Tiempo total de procesamiento: " + duration / 1_000_000.0 + " ms"); // Convierte a milisegundos
        }
    }

    // Método para cambiar los colores de la imagen
    public static BufferedImage cambiarColores(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        // Crear una nueva imagen con el mismo tamaño
        BufferedImage newImage = new BufferedImage(width, height, img.getType());

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Obtener el color de cada píxel
                int pixelColor = img.getRGB(x, y);

                // Extraer los componentes RGB
                int red = (pixelColor >> 16) & 0xff;
                int green = (pixelColor >> 8) & 0xff;
                int blue = pixelColor & 0xff;

                // Aplicar un cambio en los colores (ejemplo: invertir colores)
                red = 255 - red;    // Invertir rojo
                green = 255 - green;  // Invertir verde
                blue = 255 - blue;   // Invertir azul

                // Crear el nuevo color y establecerlo en la imagen nueva
                int newPixelColor = (red << 16) | (green << 8) | blue;
                newImage.setRGB(x, y, newPixelColor);
            }
        }

        return newImage;
    }
}
