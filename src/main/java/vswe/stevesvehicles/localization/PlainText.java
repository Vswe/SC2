package vswe.stevesvehicles.localization;


public class PlainText implements ILocalizedText {
    private String text;

    public PlainText(String text) {
        this.text = text;
    }

    @Override
    public String translate(String... params) {
        return text;
    }
}
