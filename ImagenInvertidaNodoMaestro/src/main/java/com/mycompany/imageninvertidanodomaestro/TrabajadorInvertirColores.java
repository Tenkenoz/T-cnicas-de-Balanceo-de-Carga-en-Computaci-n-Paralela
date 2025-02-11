/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.imageninvertidanodomaestro;

import java.awt.image.BufferedImage;

/**
 *
 * @author pc
 */
class TrabajadorInvertirColores implements Runnable {
    private final BufferedImage imagen;
    private final int inicioFila;
    private final int finFila;

    public TrabajadorInvertirColores(BufferedImage imagen, int inicioFila, int finFila) {
        this.imagen = imagen;
        this.inicioFila = inicioFila;
        this.finFila = finFila;
    }

    @Override
    public void run() {
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
