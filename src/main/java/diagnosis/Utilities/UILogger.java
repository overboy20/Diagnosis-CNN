package diagnosis.Utilities;

import javafx.scene.control.TextArea;

public class UILogger {
    private TextArea log;

    public UILogger(TextArea log) {
        this.log = log;
    }

    public void log(String line) {
        if (this.log.getLength() == 0) {
            this.log.setText(line);
        } else {
            this.log.setText(this.log.getText() + "\n" + line);
        }
    }
}
