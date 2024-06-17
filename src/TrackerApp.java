import java.io.IOException;

import view.TermView;

public class TrackerApp {
    private static boolean isAppRunning;

    public static void main(String[] args) throws IOException {
        isAppRunning = true;
        TermView termView = new TermView();

        while (isAppRunning) {
            isAppRunning = termView.open();
        }
    }
}