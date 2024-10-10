//public class FileSizeHandler {
//
//    public static String formatFileSize(long size, boolean removeZero, boolean makeShort) {
//        if (size !== null) {
//
//
//
//            if (size == 0) {
//                return String.format("%d KB", 0);
//            } else if (size < 1024) {
//                return String.format("%d B", size);
//            } else if (size < 1024 * 1024) {
//                float value = size / 1024.0f;
//                if (removeZero && (value - (int) value) * 10 == 0) {
//                    return String.format("%d KB", (int) value);
//                } else {
//                    return String.format("%.1f KB", value);
//                }
//            } else if (size < 1000 * 1024 * 1024) {
//                float value = size / 1024.0f / 1024.0f;
//                if (removeZero && (value - (int) value) * 10 == 0) {
//                    return String.format("%d MB", (int) value);
//                } else {
//                    return String.format("%.1f MB", value);
//                }
//            } else {
//                float value = (int) (size / 1024L / 1024L) / 1000.0f;
//                if (removeZero && (value - (int) value) * 10 == 0) {
//                    return String.format("%d GB", (int) value);
//                } else if (makeShort) {
//                    return String.format("%.1f GB", value);
//                } else {
//                    return String.format("%.2f GB", value);
//                }
//            }
//        }
//    }
//
//}
