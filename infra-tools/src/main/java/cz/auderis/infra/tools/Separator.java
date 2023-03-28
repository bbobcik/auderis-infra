package cz.auderis.infra.tools;

/**
 * Supplies separator strings needed for joining multiple items. The usage
 * pattern is as follows:
 * <ol>
 *     <li>Append this separator to the constructed buffer</li>
 *     <li>Append an item</li>
 * </ol>
 * This helper class ensures that the first occurrence of the separator is
 * a blank string. This is useful when constructing a comma-separated list,
 * when the caller doesn't have to keep track of the first item.
 *
 * @author Boleslav Bobcik
 */
@org.jetbrains.annotations.Debug.Renderer(text = "\"sep='\" + this.separator + \"', first=\" + this.firstItem")
public class Separator {

    private final String separator;
    private boolean firstItem;

    /**
     * Creates a new join helper with a default separator of ", ".
     */
    public Separator() {
        this(null);
    }

    /**
     * Creates a new join helper with a separator of a single character.
     *
     * @param separator the separator character
     */
    public Separator(char separator) {
        this(String.valueOf(separator));
    }

    /**
     * Creates a new join helper that separates items with the given character
     * sequence (typically a string)
     *
     * @param separator the separator
     */
    public Separator(CharSequence separator) {
        if (null == separator) {
            this.separator = ", ";
        } else if (separator instanceof String s) {
            this.separator = s;
        } else {
            this.separator = separator.toString();
        }
        firstItem = true;
    }

    /**
     * Allows reuse of this join helper instance. The next separator value
     * will be a blank string.
     */
    public void reset() {
        firstItem = true;
    }

    public void to(StringBuilder sb) {
        if (firstItem) {
            firstItem = false;
        } else {
            sb.append(separator);
        }
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
