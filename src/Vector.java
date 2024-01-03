import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;


public class Vector extends JFrame implements KeyListener {

    private BufferedImage buffer;
    private Graphics2D graPixel;

    private int puntoDePerspectiva = 500; // Declarar la variable puntoDePerspectiva

    private int xCabeza = 0;
    private int yCabeza = 0;
    private int zCabeza = 0;

    private double anguloX = 0; // Angulo de rotación en el eje X
    private double anguloY = 0; // Angulo de rotación en el eje Y
    private double anguloZ = 0; // Angulo de rotación en el eje Z

    private boolean rotarX = false;
    private boolean rotarY = false;
    private boolean rotarZ = false;

    private boolean mostrarVertices = true;
    private boolean mostrarVerticesUnidos = false;
    private boolean rellenarCreeper = false;

    // Definir los vértices de la cara en 3D
    private int[][] verticesCabeza = {
            {150, -300, 150},
            {150, -300, -150},
            {150, 0, 150},
            {150, 0, -150},
            {-150, -300, 150},
            {-150, -300, -150},
            {-150, 0, 150},
            {-150, 0, -150}
    };

    private int[][] verticesPata1 = {
            {130, -10, -90},
            {130, -10, -130},
            {130, 200, -40},
            {130, 200, -80},
            {90, -10, -90},
            {90, -10, -130},
            {90, 200, -40},
            {90, 200, -80}
    };

    // Estructura de las patas
    private int[][] verticesPata2;
    private int[][] verticesPata3;
    private int[][] verticesPata4;
    private int[][] verticesPata5;
    private int[][] verticesPata6;
    private int[][] verticesPata7;
    private int[][] verticesPata8;
    private int[][] verticesPata9;

    // Definir los vértices del ojo 1 en 3D
    private int[][] verticesOjo1 = {
            {-20, -210, -150},
            {-20, -210, -160},
            {-20, -195, -150},
            {-20, -195, -160},
            {-100, -210, -150},
            {-100, -210, -160},
            {-100, -195, -150},
            {-100, -195, -160}
    };

    // Definir los vértices del ojo 2 en 3D
    private int[][] verticesOjo2 = {
            {20, -210, -150},
            {20, -210, -160},
            {20, -195, -150},
            {20, -195, -160},
            {100, -210, -150},
            {100, -210, -160},
            {100, -195, -150},
            {100, -195, -160}
    };

    // Definir los vértices de la boca en 3D
    private int[][] verticesBoca = {
            {40, -115, -150},
            {40, -115, -160},
            {40, -100, -150},
            {40, -100, -160},
            {-40, -115, -150},
            {-40, -115, -160},
            {-40, -100, -150},
            {-40, -100, -160}
    };

    private int[][] caras = {
            {0, 1, 2, 3},  // Cara frontal
            {4, 6, 7, 5},  // Cara trasera
            {0, 4, 5, 1},  // Cara superior
            {2, 6, 7, 3},  // Cara inferior
            {0, 2, 6, 4},  // Cara izquierda
            {1, 3, 7, 5}   // Cara derecha
    };


    public Vector() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 1000);
        setLocationRelativeTo(null);

        buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        graPixel = buffer.createGraphics();

        Graphics2D g2d = (Graphics2D) graPixel;
        g2d.setBackground(Color.decode("#8A2429"));
        g2d.clearRect(0, 0, getWidth(), getHeight());

        addKeyListener(this);  // Agregar el escuchador de teclas

        // Declaramos un valor para todas las coordenadas de las patas
        verticesPata2 = cloneAndTranslate(verticesPata1, -110, 0, 0);
        verticesPata3 = cloneAndTranslate(verticesPata1, -220, 0, 0);

        verticesPata4 = cloneAndTranslate(verticesPata1, 0, 0, 110);
        verticesPata5 = cloneAndTranslate(verticesPata1, -110, 0, 110);
        verticesPata6 = cloneAndTranslate(verticesPata1, -220, 0, 110);

        verticesPata7 = cloneAndTranslate(verticesPata1, 0, 0, 220);
        verticesPata8 = cloneAndTranslate(verticesPata1, -110, 0, 220);
        verticesPata9 = cloneAndTranslate(verticesPata1, -220, 0, 220);

        // Configurar temporizador para la rotación continua
        Timer timer = new Timer(20, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rotarX) {
                    anguloX += 0.01; // Ajusta la velocidad de rotación según sea necesario
                }
                if (rotarY) {
                    anguloY += 0.01;
                }
                if (rotarZ) {
                    anguloZ += 0.01;
                }
                drawPoligon3D();
            }
        });
        timer.start();
    }

    // Método para clonar y trasladar las coordenadas
    private int[][] cloneAndTranslate(int[][] original, int translateX, int translateY, int translateZ) {
        int[][] result = new int[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            for (int j = 0; j < original[i].length; j++) {
                result[i][j] = original[i][j];
            }
            // Aplicar traslación
            result[i][0] += translateX;
            result[i][1] += translateY;
            result[i][2] += translateZ;
        }
        return result;
    }

    public void putPixel(int x, int y, Color c) {
        if (x >= 0 && x < getWidth() && y >= 0 && y < getHeight()) {
            buffer.setRGB(x, y, c.getRGB());
        }
    }

    public void clearMyScreen() {
        graPixel.clearRect(0, 0, getWidth(), getHeight());
    }

    // Algoritmo de Bresenham para dibujar una línea
    public void drawLineBresenham(int x1, int y1, int x2, int y2, Color c) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            putPixel(x1, y1, c);

            if (x1 == x2 && y1 == y2) {
                break;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err = err - dy;
                x1 = x1 + sx;
            }
            if (e2 < dx) {
                err = err + dx;
                y1 = y1 + sy;
            }
        }
    }

    private int[][] aplicarTransformacion(int[][] vertices, double[][] matrizTransformacion) {
        int[][] verticesTransformados = new int[vertices.length][3];
    
        for (int i = 0; i < vertices.length; i++) {
            int[] resultadoRotacion = vertices[i];
    
            // Aplicar la traslación antes de la rotación
            resultadoRotacion[0] += xCabeza;
            resultadoRotacion[1] += yCabeza;
            resultadoRotacion[2] += zCabeza;
    
            // Aplicar la transformación utilizando la matriz
            for (int j = 0; j < 3; j++) {
                double suma = 0;
                for (int k = 0; k < 3; k++) {
                    suma += resultadoRotacion[k] * matrizTransformacion[k][j];
                }
                verticesTransformados[i][j] = (int) suma;
            }
        }
    
        return verticesTransformados;
    }
    

    // Función para dibujar un conjunto de vértices proyectados en 2D
    private void dibujarVertices(int[][] vertices, int puntoDePerspectiva) {
        for (int i = 0; i < vertices.length; i++) {
            int x = (vertices[i][0] * puntoDePerspectiva) / (vertices[i][2] + puntoDePerspectiva) + getWidth() / 2;
            int y = (vertices[i][1] * puntoDePerspectiva) / (vertices[i][2] + puntoDePerspectiva) + getHeight() / 2;

            // Dibujar los vértices
            graPixel.setColor(Color.WHITE);
            graPixel.fillRect(x, y, 3, 3);
        }
    }

    // Función para dibujar un conjunto de líneas entre vértices
    private void dibujarLineas(int[][] vertices, Color fill) {
        for (int i = 0; i < vertices.length; i++) {
            int x1 = (vertices[i][0] * puntoDePerspectiva) / (vertices[i][2] + puntoDePerspectiva) + getWidth() / 2;
            int y1 = (vertices[i][1] * puntoDePerspectiva) / (vertices[i][2] + puntoDePerspectiva) + getHeight() / 2;

            // Conectar los vértices para formar el objeto
            for (int j = i + 1; j < vertices.length; j++) {
                int x2 = (vertices[j][0] * puntoDePerspectiva) / (vertices[j][2] + puntoDePerspectiva) + getWidth() / 2;
                int y2 = (vertices[j][1] * puntoDePerspectiva) / (vertices[j][2] + puntoDePerspectiva) + getHeight() / 2;

                drawLineBresenham(x1, y1, x2, y2, fill);
            }
        }
    }

    // Función para dibujar el cubo y la pirámide
    private void drawPoligon3D() {
        // Crear la matriz de rotación
        double[][] matrizRotacion = {
                {Math.cos(anguloY) * Math.cos(anguloZ), -Math.cos(anguloY) * Math.sin(anguloZ), Math.sin(anguloY)},
                {Math.cos(anguloX) * Math.sin(anguloZ) + Math.sin(anguloX) * Math.sin(anguloY) * Math.cos(anguloZ), Math.cos(anguloX) * Math.cos(anguloZ) - Math.sin(anguloX) * Math.sin(anguloY) * Math.sin(anguloZ), -Math.sin(anguloX) * Math.cos(anguloY)},
                {Math.sin(anguloX) * Math.sin(anguloZ) - Math.cos(anguloX) * Math.sin(anguloY) * Math.cos(anguloZ), Math.sin(anguloX) * Math.cos(anguloZ) + Math.cos(anguloX) * Math.sin(anguloY) * Math.sin(anguloZ), Math.cos(anguloX) * Math.cos(anguloY)}
        };

        int[][] verticesPata1Rotados = aplicarTransformacion(verticesPata1, matrizRotacion);        // Aplicar la rotación a los vértices de la pata numero 1
        int[][] verticesPata2Rotados = aplicarTransformacion(verticesPata2, matrizRotacion);        // Aplicar la rotación a los vértices de la pata numero 2
        int[][] verticesPata3Rotados = aplicarTransformacion(verticesPata3, matrizRotacion);        // Aplicar la rotación a los vértices de la pata numero 3

        int[][] verticesPata4Rotados = aplicarTransformacion(verticesPata4, matrizRotacion);        // Aplicar la rotación a los vértices de la pata numero 4
        int[][] verticesPata5Rotados = aplicarTransformacion(verticesPata5, matrizRotacion);        // Aplicar la rotación a los vértices de la pata numero 5
        int[][] verticesPata6Rotados = aplicarTransformacion(verticesPata6, matrizRotacion);        // Aplicar la rotación a los vértices de la pata numero 6

        int[][] verticesPata7Rotados = aplicarTransformacion(verticesPata7, matrizRotacion);        // Aplicar la rotación a los vértices de la pata numero 7
        int[][] verticesPata8Rotados = aplicarTransformacion(verticesPata8, matrizRotacion);        // Aplicar la rotación a los vértices de la pata numero 8
        int[][] verticesPata9Rotados = aplicarTransformacion(verticesPata9, matrizRotacion);        // Aplicar la rotación a los vértices de la pata numero 9

        int[][] verticesCabezaRotados = aplicarTransformacion(verticesCabeza, matrizRotacion);      // Aplicar la rotación a los vértices de la cabesa

        int[][] verticesOjo1Rotados = aplicarTransformacion(verticesOjo1, matrizRotacion);          // Aplicar la rotación a los vértices del ojo numero 1
        int[][] verticesOjo2Rotados = aplicarTransformacion(verticesOjo2, matrizRotacion);          // Aplicar la rotación a los vértices del ojo numero 2

        int[][] verticesBocaRotados = aplicarTransformacion(verticesBoca, matrizRotacion);          // Aplicar la rotación a los vértices de la boca

        clearMyScreen();

        if(rellenarCreeper) {
            fillPoligon3D(caras, verticesPata1Rotados, Color.decode("#F5FAFC"));
            fillPoligon3D(caras, verticesPata2Rotados, Color.decode("#F5FAFC"));
            fillPoligon3D(caras, verticesPata3Rotados, Color.decode("#F5FAFC"));

            fillPoligon3D(caras, verticesPata4Rotados, Color.decode("#F5FAFC"));
            fillPoligon3D(caras, verticesPata5Rotados, Color.decode("#F5FAFC"));
            fillPoligon3D(caras, verticesPata6Rotados, Color.decode("#F5FAFC"));

            fillPoligon3D(caras, verticesPata7Rotados, Color.decode("#F5FAFC"));
            fillPoligon3D(caras, verticesPata8Rotados, Color.decode("#F5FAFC"));
            fillPoligon3D(caras, verticesPata9Rotados, Color.decode("#F5FAFC"));

            fillPoligon3D(caras, verticesCabezaRotados, Color.decode("#F5FAFC"));

            fillPoligon3D(caras, verticesOjo1Rotados, Color.decode("#575F6E"));
            fillPoligon3D(caras, verticesOjo2Rotados, Color.decode("#575F6E"));

            fillPoligon3D(caras, verticesBocaRotados, Color.decode("#575F6E"));
        }


        // Dibujar los vértices
        else if(mostrarVertices) {
            dibujarVertices(verticesPata1Rotados, puntoDePerspectiva);
            dibujarVertices(verticesPata2Rotados, puntoDePerspectiva);
            dibujarVertices(verticesPata3Rotados, puntoDePerspectiva);

            dibujarVertices(verticesPata4Rotados, puntoDePerspectiva);
            dibujarVertices(verticesPata5Rotados, puntoDePerspectiva);
            dibujarVertices(verticesPata6Rotados, puntoDePerspectiva);

            dibujarVertices(verticesPata7Rotados, puntoDePerspectiva);
            dibujarVertices(verticesPata8Rotados, puntoDePerspectiva);
            dibujarVertices(verticesPata9Rotados, puntoDePerspectiva);

            dibujarVertices(verticesCabezaRotados, puntoDePerspectiva);

            dibujarVertices(verticesOjo1Rotados, puntoDePerspectiva);
            dibujarVertices(verticesOjo2Rotados, puntoDePerspectiva);

            dibujarVertices(verticesBocaRotados, puntoDePerspectiva);
        }
        
        else if(mostrarVerticesUnidos) {
            dibujarLineas(verticesPata1Rotados, Color.decode("#F5FAFC"));           // Dibujar las líneas de la pata 1
            dibujarLineas(verticesPata2Rotados, Color.decode("#F5FAFC"));           // Dibujar las líneas de la pata 2
            dibujarLineas(verticesPata3Rotados, Color.decode("#F5FAFC"));           // Dibujar las líneas de la pata 3

            dibujarLineas(verticesPata4Rotados, Color.decode("#F5FAFC"));           // Dibujar las líneas de la pata 4
            dibujarLineas(verticesPata5Rotados, Color.decode("#F5FAFC"));           // Dibujar las líneas de la pata 5
            dibujarLineas(verticesPata6Rotados, Color.decode("#F5FAFC"));           // Dibujar las líneas de la pata 6

            dibujarLineas(verticesPata7Rotados, Color.decode("#F5FAFC"));           // Dibujar las líneas de la pata 7
            dibujarLineas(verticesPata8Rotados, Color.decode("#F5FAFC"));           // Dibujar las líneas de la pata 8
            dibujarLineas(verticesPata9Rotados, Color.decode("#F5FAFC"));           // Dibujar las líneas de la pata 9

            dibujarLineas(verticesCabezaRotados, Color.decode("#F5FAFC"));          // Dibujar las líneas de la cabeza

            dibujarLineas(verticesOjo1Rotados, Color.decode("#575F6E"));            // Dibujar las líneas del ojo 1
            dibujarLineas(verticesOjo2Rotados, Color.decode("#575F6E"));            // Dibujar las líneas del ojo 1

            dibujarLineas(verticesBocaRotados, Color.decode("#575F6E"));            // Dibujar las líneas de la boca
        }

        // Repintar el JFrame para mostrar el cubo y la pirámide
        repaint();
    }

    public void fillPoligon3D(int[][] caras, int[][] vertices, Color fill) {
        // Crear un array para almacenar las distancias de las caras al espectador
        double[] distancias = new double[caras.length];

        // Calcular la distancia al espectador para cada cara
        for (int i = 0; i < caras.length; i++) {
            int[] cara = caras[i];
            int[] centro = {0, 0, 0};  // Puedes usar el centroide u otro punto representativo de la cara

            // Calcular la distancia euclidiana
            distancias[i] = Math.sqrt(Math.pow(centro[0] - xCabeza, 2) + Math.pow(centro[1] - yCabeza, 2) + Math.pow(centro[2] - zCabeza, 2));
        }

        // Ordenar las caras por distancia (de cercano a lejano)
        int[] ordenCaras = IntStream.range(0, caras.length)
                .boxed()
                .sorted(Comparator.comparingDouble(i -> -distancias[i]))
                .mapToInt(ele -> ele)
                .toArray();

        // Dibujar las caras en el orden correcto
        for (int i = 0; i < ordenCaras.length; i++) {
            int[] cara = caras[ordenCaras[i]];
            int[] puntosX = new int[cara.length];
            int[] puntosY = new int[cara.length];

            for (int j = 0; j < cara.length; j++) {
                int x = (vertices[cara[j]][0] * puntoDePerspectiva) / (vertices[cara[j]][2] + puntoDePerspectiva)
                        + getWidth() / 2;
                int y = (vertices[cara[j]][1] * puntoDePerspectiva) / (vertices[cara[j]][2] + puntoDePerspectiva)
                        + getHeight() / 2;
                puntosX[j] = x;
                puntosY[j] = y;
            }

            // Rellenar la cara con un color
            fillPolygonScanLine(puntosX, puntosY, fill);
        }

        // Repintar el JFrame para mostrar el cubo
        repaint();
    }


    public void fillPolygonScanLine(int[] xPoints, int[] yPoints, Color c) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;

        // Encontrar el rango horizontal del polígono
        for (int x : xPoints) {
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }

        List<Integer> intersections = new ArrayList<>();

        // Escanear cada línea vertical dentro del rango horizontal
        for (int x = minX; x <= maxX; x++) {
            intersections.clear();

            for (int i = 0; i < xPoints.length; i++) {
                int x1 = xPoints[i];
                int y1 = yPoints[i];
                int x2 = xPoints[(i + 1) % xPoints.length];
                int y2 = yPoints[(i + 1) % xPoints.length];

                if ((x1 <= x && x2 > x) || (x2 <= x && x1 > x)) {
                    // Calcula la intersección vertical con la línea
                    double y = y1 + (double) (x - x1) * (y2 - y1) / (x2 - x1);
                    intersections.add((int) y);
                }
            }

            // Ordena las intersecciones de arriba a abajo
            intersections.sort(Integer::compareTo);

            // Rellena el espacio entre las intersecciones
            for (int i = 0; i < intersections.size(); i += 2) {
                int startY = intersections.get(i);
                int endY = intersections.get(i + 1);
                for (int y = startY; y < endY; y++) {
                    putPixel(x, y, c);
                }

                repaint();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(buffer, 0, 0, this);
    }

    // Métodos de la interfaz KeyListener
    @Override
    public void keyTyped(KeyEvent e) {
        // No necesitamos implementar este método en este caso
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Capturar teclas presionadas y ajustar los estados de rotación y traslación
        if (e.getKeyChar() == 'x' && !rotarX) {
            rotarX = true;
        } else if (e.getKeyChar() == 'y' && !rotarY) {
            rotarY = true;
        } else if (e.getKeyChar() == 'z' && !rotarZ) {
            rotarZ = true;
        } else if (e.getKeyChar() == 'q') {
            mostrarVerticesUnidos = true;
            mostrarVertices = false;
        } else if (e.getKeyChar() == 'w') {
            rellenarCreeper = true;
            mostrarVerticesUnidos = false;
        } else if (e.getKeyChar() == 'e') {
            rellenarCreeper = false;
            mostrarVerticesUnidos = false;
            mostrarVertices = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            xCabeza -= 10; // Ajusta la velocidad de traslación según sea necesario
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            xCabeza += 10;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            zCabeza -= 10;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            zCabeza += 10;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // Restaurar el cubo y la pirámide a su posición estática
            anguloX = 0;
            anguloY = 0;
            anguloZ = 0;
            xCabeza = 0;
            yCabeza = 0;
            zCabeza = 0;
            rotarX = false;
            rotarY = false;
            rotarZ = false;
            drawPoligon3D();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // No necesitamos implementar este método en este caso
    }

    public static void main(String[] args) throws Exception {
        Vector vector = new Vector();
        vector.setVisible(true);
    }
}
