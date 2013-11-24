package liebenberg.closure_utilities.stylesheets;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageUrlProcessor extends AbstractGssProcessor
        implements GssProcessor {

    private static final Pattern IMAGE_URL_PATTERN =
            Pattern.compile("image-url\\(([^\\)]+)\\)");

    @Nonnull
    @Override
    public String processString(
            @Nonnull final String inputString,
            @Nullable final String imagePath) {
        final Matcher matcher = IMAGE_URL_PATTERN.matcher(inputString);
        final StringBuffer sb = new StringBuffer();
        String urlSegment, stripped, path;
        while (matcher.find()) {
            if (matcher.groupCount() == 1) {
                urlSegment = matcher.group(1);
                stripped = stripQuotes(urlSegment);
                path = getStringPath(stripped, imagePath);
                matcher.appendReplacement(sb, "url(" + path + ")");
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }


}
