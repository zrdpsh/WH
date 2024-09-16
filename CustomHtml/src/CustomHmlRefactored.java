import android.text.Spanned;
import android.text.TextUtils;

import org.telegram.messenger.CodeHighlighting;
import org.telegram.ui.Components.AnimatedEmojiSpan;
import org.telegram.ui.Components.QuoteSpan;
import org.telegram.ui.Components.TextStyleSpan;
import org.telegram.ui.Components.URLSpanMono;
import org.telegram.ui.Components.URLSpanReplacement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CustomHtmlRefactored {

    private CustomHtml() {
    }

    public static String toHtml(Spanned text) {
        StringBuilder out = new StringBuilder();
        toHTML_0_wrapQuote(out, text, 0, text.length());
        return out.toString();
    }

    private static final List<StyleTag> STYLE_TAGS;

    static {
        STYLE_TAGS = new ArrayList<>(
                new StyleTag("<spoiler>", "</spoiler>", TextStyleSpan.FLAG_STYLE_SPOILER),
                new StyleTag("<b>", "</b>", TextStyleSpan.FLAG_STYLE_BOLD),
                new StyleTag("<i>", "</i>", TextStyleSpan.FLAG_STYLE_ITALIC),
                new StyleTag("<u>", "</u>", TextStyleSpan.FLAG_STYLE_UNDERLINE),
                new StyleTag("<s>", "</s>", TextStyleSpan.FLAG_STYLE_STRIKE)
        );
    }

//    private static void toHTML_0_wrapQuote(StringBuilder out, Spanned text, int start, int end) {
//
//        int next;
//        for (int i = start; i < end; i = next) {
//            next = text.nextSpanTransition(i, end, QuoteSpan.class);
//            if (next < 0) {
//                next = end;
//            }
//            QuoteSpan[] spans = text.getSpans(i, next, QuoteSpan.class);
//
//            if (spans != null) {
//                for (int j = 0; j < spans.length; ++j) {
//                    out.append(spans[j].isCollapsing ? "<details>" : "<blockquote>");
//                }
//            }
//
//            toHTML_1_wrapTextStyle(out, text, i, next);
//
//            if (spans != null) {
//                for (int j = spans.length - 1; j >= 0; --j) {
//                    out.append(spans[j].isCollapsing ? "</details>" : "</blockquote>");
//                }
//            }
//        }
//    }


    private static void toHTML_1_wrapTextStyle(StringBuilder out, Spanned text, int start, int end) {
        int next;
        for (int i = start; i < end; i = next) {
            next = text.nextSpanTransition(i, end, TextStyleSpan.class);
            if (next < 0) {
                break;
            }
            TextStyleSpan[] spans = text.getSpans(i, next, TextStyleSpan.class);
            processSpans(out, spans);
        }
    }


    public static void processSpans(StringBuilder out, TextStyleSpan[] spans) {
        Arrays.stream(Optional.ofNullable(spans).orElse(new TextStyleSpan[0]))
                .filter(spanObject -> spanObject != null)
                .forEach(spanObject -> {
                    TextStyleSpan span = (TextStyleSpan) spanObject;
                    int flags = span.getStyleFlags();
                    // Append opening tags based on flags
                    if ((flags & (TextStyleSpan.FLAG_STYLE_SPOILER | TextStyleSpan.FLAG_STYLE_SPOILER_REVEALED)) > 0) {
                        out.append("<spoiler>");
                    }
                    if ((flags & TextStyleSpan.FLAG_STYLE_BOLD) > 0) {
                        out.append("<b>");
                    }
                    if ((flags & TextStyleSpan.FLAG_STYLE_ITALIC) > 0) {
                        out.append("<i>");
                    }
                    if ((flags & TextStyleSpan.FLAG_STYLE_UNDERLINE) > 0) {
                        out.append("<u>");
                    }
                    if ((flags & TextStyleSpan.FLAG_STYLE_STRIKE) > 0) {
                        out.append("<s>");
                    }
                    if ((flags & TextStyleSpan.FLAG_STYLE_URL) > 0) {
                        Optional.ofNullable(span.getTextStyleRun())
                                .map(run -> run.urlEntity)
                                .map(urlEntity -> urlEntity.url)
                                .ifPresent(url -> out.append("<a href=\"").append(url).append("\">"));
                    }
                });
    }

    public static void appendOpeningTags(int flags, StringBuilder out) {
        STYLE_TAGS.forEach( styleTag -> {
                    (flags & styleTag.getFlag()) ? out.append(styleTag.getOpenTag()) : out
                });
    }
}