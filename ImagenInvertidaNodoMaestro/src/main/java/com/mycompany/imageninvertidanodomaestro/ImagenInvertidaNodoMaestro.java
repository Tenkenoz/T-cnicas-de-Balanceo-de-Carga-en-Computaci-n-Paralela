package com.mycompany.imageninvertidanodomaestro;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class ImagenInvertidaNodoMaestro {

    private static final String INPUT_FOLDER = "imagenesSinFiltro";
    private static final String OUTPUT_FOLDER = "imagenesConFiltro";

    public static void main(String[] args) {
        File inputFolder = new File(INPUT_FOLDER);
        File[] imageFiles = inputFolder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png"));

        if (imageFiles == null || imageFiles.length == 0) {
            System.out.println("No hay imágenes en la carpeta " + INPUT_FOLDER);
            return;
        }

        // Número de trabajadores (hilos)
        int numeroTrabajadores = 8; // Puedes ajustar este valor según el número de núcleos de la CPU

        // Procesar cada imagen
        for (File imageFile : imageFiles) {
            try {
                // Iniciar medición del tiempo
                long inicioTiempo = System.nanoTime();

                BufferedImage imagen = ImageIO.read(imageFile);
                int altura = imagen.getHeight();
                int ancho = imagen.getWidth();

                System.out.println("Procesando imagen: " + imageFile.getName() + " (" + ancho + "x" + altura + ")");

                // Dividir la imagen en secciones para cada trabajador
                int filasPorTrabajador = altura / numeroTrabajadores;
                List<Thread> trabajadores = new ArrayList<>();

                // Crear y ejecutar los trabajadores
                for (int i = 0; i < numeroTrabajadores; i++) {
                    int inicioFila = i * filasPorTrabajador;
                    int finFila = (i == numeroTrabajadores - 1) ? altura : inicioFila + filasPorTrabajador;

                    Thread trabajador = new Thread(new TrabajadorInvertirColores(imagen, inicioFila, finFila));
                    trabajadores.add(trabajador);
                    trabajador.start();
                }

                // Esperar a que todos los trabajadores terminen
                for (Thread trabajador : trabajadores) {
                    trabajador.join();
                }

                // Guardar la imagen procesada
                String outputFilePath = OUTPUT_FOLDER + File.separator + imageFile.getName();
                File outputFile = new File(outputFilePath);
                ImageIO.write(imagen, "png", outputFile);

                // Finalizar medición del tiempo
                long finTiempo = System.nanoTime();
                long tiempoTotal = finTiempo - inicioTiempo;

                // Mostrar el tiempo de procesamiento
                System.out.println("Imagen procesada y guardada: " + outputFilePath);
                System.out.println("Tiempo de procesamiento: " + (tiempoTotal / 1e9) + " segundos");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
