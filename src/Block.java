
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;


/** этот интерфейс предназначен для копирования
 * @author Vlad
 */
 interface Copyable{
    Object copy();
}
/** этот класс предназначен для копирует изображение
 */
class Image implements Copyable{
    BufferedImage imageCopy;
    public  Image(BufferedImage imageCopy){
        this.imageCopy = imageCopy;
    }

    @Override
    public Object copy() {
        Image copy = new Image(imageCopy);
        return copy;
    }
}
/** этот класс предназначен выводить на экран картинку изменять её и сохранять
 */
public class Block extends JFrame {

    JSplitPane main, leftSplit;
    JPanel rightInstrument, downImage, upImage;
    JMenuBar menuBar;
    JMenuItem menuExit, menuSave, menuOpen;
    static JLabel sourceImage,copyImage;
    static JScrollPane upPanel, downPanel;
    String fileName;
    BufferedImage imag = null, rezult = null, copied;

    JComboBox comboBox = new JComboBox();


    /** Этот главный метод который производит открытие картинки ее сохранение
     */
    private Block() throws IOException {
        super("Effects");
        setSize(1500, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createLayout();

        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        menuOpen = new JMenuItem("Открыть");
        fileMenu.add(menuOpen);
        menuSave = new JMenuItem("Cохранить");
        fileMenu.add(menuSave);
        menuExit = new JMenuItem("Выход");
        fileMenu.add(menuExit);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        setVisible(true);



        menuExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(rezult != null) {
                    try {
                        JFileChooser jf = new JFileChooser();
                        // Создаем фильтры для файлов
                        TextFileFilter pngFilter = new TextFileFilter(".png");
                        TextFileFilter jpgFilter = new TextFileFilter(".jpg");
                        // Добавляем фильтры
                        jf.addChoosableFileFilter(pngFilter);
                        jf.addChoosableFileFilter(jpgFilter);
                        int result = jf.showSaveDialog(null);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            fileName = jf.getSelectedFile().getAbsolutePath();
                        }
                        // Смотрим какой фильтр выбран
                        if (jf.getFileFilter() == pngFilter) {
                            ImageIO.write(rezult, "png", new File(fileName + ".png"));
                            JOptionPane.showMessageDialog(null, "Картинка сохранена!");
                        } else {
                            ImageIO.write(rezult, "jpeg", new File(fileName + ".jpg"));
                            JOptionPane.showMessageDialog(null, "Картинка сохранена!");
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                else JOptionPane.showMessageDialog(null, "Нечего сохранять!");
            }
        });
        menuOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = new  JFileChooser();
                int  result = jf.showOpenDialog(null);
                if(result==JFileChooser.APPROVE_OPTION) {
                    fileName = jf.getSelectedFile().getAbsolutePath();
                    File iF = new File(fileName);
                    jf.addChoosableFileFilter(new TextFileFilter(".png"));
                    jf.addChoosableFileFilter(new TextFileFilter(".jpg"));
                    try {
                        imag = ImageIO.read(iF);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Image original = new Image(imag);

                    // создание сплит
                    sourceImage = new JLabel(new ImageIcon(imag));
                    upPanel = new JScrollPane(sourceImage);
                    upPanel.setSize(760, 500);
                    // копирование картинки

                    Image copy = (Image)original.copy();
                    copied = copy.imageCopy;

                    copyImage = new JLabel(new ImageIcon(copied));
                    downPanel = new JScrollPane(copyImage);

                    leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upPanel, downPanel);
                    leftSplit.setDividerLocation(400);
                    getContentPane().removeAll();
                    validate();
                    main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, rightInstrument);
                    main.setDividerLocation(800);
                    add(main);
                    rezult=null;
                    setVisible(true);
                }
            }
        });

    }

    /** Этот метод заполняет панели приложения
     */
    private void createLayout() throws IOException {
        createPanels();

        leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upPanel, null);
        leftSplit.setDividerLocation(500);
        leftSplit.setContinuousLayout(true);
        leftSplit.setOneTouchExpandable(true);
        main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, rightInstrument);
        main.setDividerLocation(1075);
        main.setContinuousLayout(true);
        main.setOneTouchExpandable(true);
        getContentPane().add(main);
    }
    /** Этот метод создает главные панели приложения
     */
    private void createPanels() throws IOException {
        createUP();
        createDown();
        createInstruments();

    }
    /** Этот метод создает верхнюю левую панель
     */
    private void createUP() throws IOException {
        upImage = new JPanel(new GridLayout(1, 1));
        upImage.setMinimumSize(new Dimension(600, 100));
        BufferedImage source = ImageIO.read(new File("maxresdefault.jpg"));
        sourceImage = new JLabel(new ImageIcon(source));
        upPanel = new JScrollPane(sourceImage);
    }
    /** Этот метод создает нижнюю левую панель
     */
    private void createDown() {
        downImage = new JPanel(new GridLayout(1, 1));
        downImage.setMinimumSize(new Dimension(600, 100));
    }
    /** Этот метод создает панель инструментов
     */
    private void createInstruments() {
        rightInstrument = new JPanel(new GridLayout(20, 7));
        comboBox.setEditable(true);
        comboBox.addItem("Чёрно-белое");
        comboBox.addItem("Сепия");
        comboBox.addItem("Штамп");
        comboBox.addItem("Негатив");


        JButton button= new JButton("Выполнить");
        Font two = new Font("Serif", Font.BOLD, 18);
        button.setFont(two);
        comboBox.setFont(two);
        rightInstrument.add(comboBox);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Object selectedItem = comboBox.getSelectedItem();

                if (imag != null) {

                    switch (Objects.requireNonNull(selectedItem).toString()){
                        case "Чёрно-белое": {
                            Effects ef = new Effects(copied, 1);
                            rezult = ef.res;
                        };break;
                        case "Штамп": {
                            Effects ef = new Effects(copied, 3);
                            rezult = ef.res;
                        };break;
                        case "Негатив": {
                            Effects ef = new Effects(copied, 2);
                            rezult = ef.res;
                        };break;
                        case "Сепия": {
                            Effects ef = new Effects(copied, 4);
                            rezult = ef.res;
                        };break;
                    }
                    Settings();
                }
                else JOptionPane.showMessageDialog(null, "Картинка не открыта");
            }
        });
        rightInstrument.add(button);
    }
    /** Этот метод производит настройку экрана перед перерисовкой
     */
    private void Settings() {
        JLabel right = new JLabel(new ImageIcon(rezult));
        downPanel = new JScrollPane(right);
        leftSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upPanel, downPanel);
        leftSplit.setDividerLocation(400);
        getContentPane().removeAll();
        validate();
        main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, rightInstrument);
        main.setDividerLocation(800);
        add(main);
        setLocationRelativeTo(null);
        setVisible(true);
    }

static class TextFileFilter extends FileFilter
{
    private String ext;
    TextFileFilter(String ext)
    {
        this.ext=ext;
    }
    public boolean accept(java.io.File file)
    {
        if (file.isDirectory()) return true;
        return (file.getName().endsWith(ext));
    }
    public String getDescription()
    {
        return "*"+ext;
    }
}

    public static void main(String[] args) throws IOException {
        new Block();
    }
}