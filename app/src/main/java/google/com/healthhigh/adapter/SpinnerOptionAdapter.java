package google.com.healthhigh.adapter;

public class SpinnerOptionAdapter {
    public String string;
    public Object tag;

    public SpinnerOptionAdapter(String stringPart, Object tagPart) {
        string = stringPart;
        tag = tagPart;
    }

    @Override
    public String toString() {
        return string;
    }
}
