package company.balanceodistribuido;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;

public class BalanceoDistribuido {

    private static final String INPUT_FOLDER = "imagenesSinFiltro";
    private static final String OUTPUT_FOLDER = "imagenesConFiltro";
    private static final int NUMERO_TRABAJADORES = 8; // EXACTAMENTE 4 HILOS

    public static void main(String[] args) {
        long startTime = System.nanoTime(); // Comienza a medir el tiempo

        File inputFolder = new File(INPUT_FOLDER);
        File[] imageFiles = inputFolder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No hay imágenes en la carpeta " + INPUT_FOLDER);
            return;
        }

        for (File imageFile : imageFiles) {
            try {
                BufferedImage imagen = ImageIO.read(imageFile);
                int altura = imagen.getHeight();

                System.out.println("\nProcesando imagen: " + imageFile.getName());

                ColaTareas colaTareas = new ColaTareas(altura, NUMERO_TRABAJADORES);
                ExecutorService ejecutor = Executors.newFixedThreadPool(NUMERO_TRABAJADORES); // SOLO 4 HILOS

                for (int i = 0; i < NUMERO_TRABAJADORES; i++) {
                    ejecutor.execute(new TrabajadorInvertirColores(imagen, colaTareas));
                }

                // Esperar a que todos los hilos terminen
                ejecutor.shutdown();
                ejecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

                String outputFilePath = OUTPUT_FOLDER + File.separator + imageFile.getName();
                ImageIO.write(imagen, "png", new File(outputFilePath));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long endTime = System.nanoTime(); // Finaliza la medición
        long duration = (endTime - startTime); // Calcula el tiempo transcurrido

        System.out.println("Tiempo total de procesamiento: " + duration / 1_000_000.0 + " ms"); // Convierte a milisegundos
    }
}
