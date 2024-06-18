
import view.TermView;

public class TrackerApp {
    private static boolean isAppRunning;

    public static void main(String[] args) {
        isAppRunning = true;
        TermView termView = new TermView();

        while (isAppRunning) {
            isAppRunning = termView.open();
        }
    }
}