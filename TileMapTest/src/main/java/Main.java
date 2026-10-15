import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starte Map-Generierung...");

        try {
            BufferedImage tileLight = ImageIO.read(new File("land_dark.png"));
            BufferedImage tileDark = ImageIO.read(new File("land_light.png"));
            BufferedImage tileCoast = ImageIO.read(new File("water_coast_sw.png"));
            BufferedImage tileSea = ImageIO.read(new File("water.png"));

            int canvasWidth = 800;
            int canvasHeight = 600;
            int zoom = 3;

            BufferedImage canvas = new BufferedImage(canvasWidth * zoom, canvasHeight * zoom, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = canvas.createGraphics();

            g2d.setRenderingHint(
                    java.awt.RenderingHints.KEY_INTERPOLATION,
                    java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
            );

            g2d.scale(zoom, zoom);

            int startX = canvasWidth / 2 - 32;
            int startY = 50;

            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {

                    BufferedImage currentTile;

                    // Logik für die NO-Kante
                    if (row == 0) {
                        currentTile = tileSea;
                    } else if (row == 1) {
                        currentTile = tileCoast;
                    } else {
                        // Schachbrett-Logik für das restliche Land
                        currentTile = ((row + col) % 2 == 0) ? tileLight : tileDark;
                    }

                    int pixelX = startX + (col * 32) - (row * 32);
                    int pixelY = startY + (col * 16) + (row * 16);

                    if (row == 4 && col == 4) {
                        continue;
                    }

                    g2d.drawImage(currentTile, pixelX, pixelY, null);
                }
            }

            g2d.dispose();

            File outputFile = new File("map_test.png");
            ImageIO.write(canvas, "png", outputFile);
            System.out.println("Erfolgreich gespeichert unter: " + outputFile.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Fehler beim Laden oder Speichern: " + e.getMessage());
            e.printStackTrace();
        }
    }
}