import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TableKeyListener implements KeyListener {

    private static final int[] SEQUENCE = { KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN };
    private int currentIndex = 0;
    private MainWindow mainWindow;

    public TableKeyListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == SEQUENCE[currentIndex]) {
            currentIndex++;
            if (currentIndex == SEQUENCE.length) {
                Button.changeTable();
                currentIndex = 0; 
                mainWindow.setTitle("IDE do Professor - Easter egg");
                new Thread(() -> {
                    try {
                        Thread.sleep(100); 
                        mainWindow.setTitle("IDE do Professor");
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();


            }
        } else {
            currentIndex = 0; 
        }
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
