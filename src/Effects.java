import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
/** этот класс может накладывать эффект на изображение
 */
public class Effects {

    public BufferedImage res;
    /** это конструктор который принимает изображение и номер эффекта
     * @param bm изображение
     * @param ef номер эффекта
     */
    public Effects(BufferedImage bm, int ef) {
        if (ef == 1) {
            this.res = blackWhite(bm);
        } else if (ef == 2) {
            this.res = negative(bm);
        } else if (ef == 3) {
            this.res = shtamp(bm);
        } else if (ef == 4) {
            this.res = sepia(bm);
        }
    }

    /** этот метод  принимает изображение и накладывает эффект черно-белое
     * @param bm изображение
     */
    public BufferedImage blackWhite(BufferedImage bm) {
        BufferedImage result = new BufferedImage(bm.getWidth(), bm.getHeight(), bm.getType());

        for (int x = 0; x < bm.getWidth(); x++) {
            for (int y = 0; y < bm.getHeight(); y++) {

                Color color = new Color(bm.getRGB(x, y));

                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();

                // Применяем стандартный алгоритм для получения черно-белого изображения
                int grey = (int) (red + green + blue) / 3;

                int newRed = grey;
                int newGreen = grey;
                int newBlue = grey;

                // создаём новый цвет
                Color newColor = new Color(newRed, newGreen, newBlue);

                // И устанавливаем этот цвет в текущий пиксель результирующего изображения
                result.setRGB(x, y, newColor.getRGB());
            }
        }
        return result;
    }
    /** этот метод  принимает изображение и накладывает эффект черно-белое
     * @param img изображение
     */
    public BufferedImage negative(BufferedImage img) {
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int width = img.getWidth();
        int height = img.getHeight();
        // Преобразовать в отрицательный
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;
                // вычитаем RGB из 255
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                // установить новое значение RGB
                p = (a << 24) | (r << 16) | (g << 8) | b;
                result.setRGB(x, y, p);
            }
        }
        return result;
    }
    /** этот метод  принимает изображение и накладывает эффект черно-белое
     * @param img изображение
     */
    public BufferedImage shtamp(BufferedImage img) {
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int x = 0; x < img.getWidth(); x++)
            for (int y = 0; y < img.getHeight(); y++)
                if (rgbToGray(img.getRGB(x, y)) > 50)
                    result.setRGB(x, y, 0);
                else
                    result.setRGB(x, y, 0xffffff);

        return result;
    }
    /** этот метод обесвечивет пиксель
     * @param rgb пиксель
     */
    private static int rgbToGray(int rgb) {
        // split rgb integer into R, G and B components
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        int gray;
        gray = Math.round((Math.max(r, Math.max(g, b)) + Math.min(r, Math.min(g, b))) / 2);
        return gray;
    }


    public BufferedImage sepia(BufferedImage img) {
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        int width = img.getWidth();
        int height = img.getHeight();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int p = img.getRGB(x, y);
                int a = (p >> 24) & 0xff;
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p & 0xff;

                int tr = (int)(0.393*r + 0.769*g + 0.189*b);
                int tg = (int)(0.349*r + 0.686*g + 0.168*b);
                int tb = (int)(0.272*r + 0.534*g + 0.131*b);

                //check condition
                if(tr > 255){
                    r = 255;
                }else{
                    r = tr;
                }

                if(tg > 255){
                    g = 255;
                }else{
                    g = tg;
                }

                if(tb > 255){
                    b = 255;
                }else{
                    b = tb;
                }

                //set new RGB value
                p = (a<<24) | (r<<16) | (g<<8) | b;

                result.setRGB(x, y, p);
            }
        }
        return result;
    }
}
