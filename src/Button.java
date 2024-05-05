import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Button {
    public static JButton createButton(MainWindow mainWindow, String iconPath, int width, int height) {
        JButton button = new JButton();

        java.net.URL imgUrl = mainWindow.getClass().getResource(iconPath);
        Icon icon = new ImageIcon(imgUrl);
        Image img = ((ImageIcon) icon).getImage();
        Image newImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        Icon newIcon = new ImageIcon(newImg);

        button.setIcon(newIcon);
        button.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // Configuração do botão Abrir

        return button;
    }

    
}
