package cz.auderis.infra.tools;

public class JoinHelper implements CharSequence {

    private final String separator;
    private boolean firstItem;

    public JoinHelper() {
        this(null);
    }

    public JoinHelper(char separator) {
        this(String.valueOf(separator));
    }

    public JoinHelper(CharSequence separator) {
        if (null == separator) {
            this.separator = ", ";
        } else if (separator instanceof String s) {
            this.separator = s;
        } else {
            this.separator = separator.toString();
        }
    }

    public void reset() {
        firstItem = true;
    }

    @Override
    public int length() {
        return firstItem ? 0 : separator.length();
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        final CharSequence result;
        if (firstItem) {
            firstItem = false;
            result = "";
        } else {
            result = separator.subSequence(start, end);
        }
        return result;
    }

    @Override
    public String toString() {
        if (firstItem) {
            firstItem = false;
            return "";
        }
        return separator;
    }

}
