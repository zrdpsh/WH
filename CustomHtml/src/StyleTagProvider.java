//import java.util.Arrays;
//import java.util.List;
//
//import android.text.Spanned;
//import android.text.TextUtils;
//
//import org.telegram.messenger.CodeHighlighting;
//import org.telegram.ui.Components.AnimatedEmojiSpan;
//import org.telegram.ui.Components.QuoteSpan;
//import org.telegram.ui.Components.TextStyleSpan;
//import org.telegram.ui.Components.URLSpanMono;
//import org.telegram.ui.Components.URLSpanReplacement;
//
//public class StyleTagProvider {
//
//    private static final List<StyleTag> STYLE_TAGS;
//
//    static {
//        STYLE_TAGS = Arrays.asList(
//                new StyleTag("<spoiler>", "</spoiler>", TextStyleSpan.FLAG_STYLE_SPOILER),
//                new StyleTag("<b>", "</b>", TextStyleSpan.FLAG_STYLE_BOLD),
//                new StyleTag("<i>", "</i>", TextStyleSpan.FLAG_STYLE_ITALIC),
//                new StyleTag("<u>", "</u>", TextStyleSpan.FLAG_STYLE_UNDERLINE),
//                new StyleTag("<s>", "</s>", TextStyleSpan.FLAG_STYLE_STRIKE)
//        );
//    }
//
//    private StyleTagProvider() {
//        // Prevent instantiation
//    }
//
//    public static List<StyleTag> getStyleTags() {
//        return STYLE_TAGS;
//    }
//}
