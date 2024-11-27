package org.example.FileSize.src;

import java.util.List;

/**
 * ЦС первоначального метода - 9
 *
 * Значения выбираются при помощи разветвлённой if-else
 *
 * Способы снижения ЦС:
 * 1. Избавление от if-else при помощи вынесения логики в отдельный класс
 * 2. Полиморфные методы - formatByCondition(args)
 * Условия собраны в одном месте, логика форматирования собрана под одним названием.
 *
 * ЦС нового кода - 1
 */

public class FileSizeHandlerRefactored {

    public String formatFileSize(long size, boolean removeZero, boolean makeShort) {

        FormatterFactory f = new FormatterFactory();
        List<FormatterFactory.FileSizeFormatRule> rules = f.getRules();

        return rules.stream()
                .filter(rule -> rule.matches(size))
                .findFirst()
                .map(rule -> rule.apply(size, removeZero, makeShort))
                .orElseThrow();
    }


    public class FormatterFactory {

        private static final long SIZE_ZERO = 0;
        private static final long SIZE_ONE_KB = 1023;
        private static final long SIZE_ONE_MB = 1024 * 1024 - 1;
        private static final long SIZE_ONE_GB = 1000 * 1024 * 1024 - 1;

        @FunctionalInterface
        public interface TriFunction<T, U, V, R> {
            R apply(T t, U u, V v);
        }

        public static class FileSizeFormatRule {
            final long maxSize;
            final TriFunction<Long, Boolean, Boolean, String> formatFunction;

            public FileSizeFormatRule(long maxSize, TriFunction<Long, Boolean, Boolean, String> formatFunction) {
                this.maxSize = maxSize;
                this.formatFunction = formatFunction;
            }

            public boolean matches(long size) {
                return size <= maxSize;
            }

            public String apply(long size, boolean removeZero, boolean makeShort) {
                return formatFunction.apply(size, removeZero, makeShort);
            }
        }

        private static String formatByCondition(long value, String formatString) {
            formatString = "%d " + formatString;
            return String.format(formatString, value);
        }

        private static String formatByCondition(float value, String formatString, boolean removeZero) {
            if (removeZero && (value - (int) value) * 10 == 0) {
                return String.format(("%d " + formatString), (int) value);
            }
            return String.format(("%.1f" + formatString), value);
        }

        private static String formatByCondition(float value, String formatString, boolean removeZero, boolean makeShort) {
            if (removeZero && (value - (int) value) * 10 == 0) {
                return String.format(("%d " + formatString), (int) value);
            }
            if (makeShort) {
                return String.format(("%.1f " + formatString), value);
            }
            return String.format(("%.2f " + formatString), value);
        }

        private static final List<FileSizeFormatRule> rules = List.of(
                new FileSizeFormatRule(SIZE_ZERO, (size, removeZero, makeShort) -> formatByCondition(0, "KB")),
                new FileSizeFormatRule(SIZE_ONE_KB, (size, removeZero, makeShort) -> formatByCondition(size, "B")),
                new FileSizeFormatRule(SIZE_ONE_MB, (size, removeZero, makeShort) -> formatByCondition(size / 1024.0f, "KB", removeZero)),
                new FileSizeFormatRule(SIZE_ONE_GB, (size, removeZero, makeShort) -> formatByCondition(size / 1024.0f / 1024.0f, "MB", removeZero)),
                new FileSizeFormatRule(Long.MAX_VALUE, (size, removeZero, makeShort) -> formatByCondition((int) (size / 1024L / 1024L) / 1000.0f, "GB", removeZero, makeShort))
        );

        public static List<FileSizeFormatRule> getRules() {
            return rules;
        }

    }

}
