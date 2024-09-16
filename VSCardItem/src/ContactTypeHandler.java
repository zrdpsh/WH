import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ContactTypeHandler {
    private static final Map<Integer, TypeHandler> handlers = new HashMap<>();
    private static final Map<String, TypeHandler> specialCaseHandlers = new HashMap<>();

    // Static block to initialize handlers
    static {
        handlers.put(3, new UrlHandler());
        handlers.put(4, new NoteHandler());
        handlers.put(5, new BirthdayHandler());
        handlers.put(6, new JobHandler());
        handlers.put(20, new Type20Handler());

        specialCaseHandlers.put("PREF", new PrefHandler());
        specialCaseHandlers.put("HOME", new HomeHandler());
        specialCaseHandlers.put("MOBILE", new MobileHandler());
        specialCaseHandlers.put("CELL", new MobileHandler());
        specialCaseHandlers.put("OTHER", new OtherHandler());
        specialCaseHandlers.put("WORK", new WorkHandler());
    }

    public String getType(int type, String fullData) {
        TypeHandler handler = handlers.get(type);
        return Optional
                .ofNullable(handler.handle(fullData))
                .orElse(new StringValueHandler().handle(fullData));
    }

    interface TypeHandler {
        String handle(String fullData);
    }

    static class NoteHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            return getString(R.string.ContactNote);
        }
    }

    static class UrlHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            return getString(R.string.ContactUrl);
        }
    }

    static class BirthdayHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            return getString(R.string.ContactBirthday);
        }
    }

    static class JobHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            if ("ORG".equalsIgnoreCase(getRawType(true))) {
                return getString(R.string.ContactJob);
            }
            return getString(R.string.ContactJobTitle);
        }
    }

    static class Type20Handler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            int idx = fullData.indexOf(':');
            if (idx < 0) {
                return "";
            }
            String value = fullData.substring(0, idx);
            value = value.substring(2);
            String[] args = value.split(";");
            return args[0];
        }
    }

    static class StringValueHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            int idx = fullData.indexOf(':');
            if (idx < 0) {
                return "";
            }
            String value = fullData.substring(0, idx);
            String[] args = value.split(";");

            for (String arg : args) {
                if (arg.indexOf('=') >= 0) {
                    continue;
                }
                value = arg;
            }

            if (value.startsWith("X-")) {
                value = value.substring(2);
            }

            // Delegate special cases to appropriate handlers
            TypeHandler specialCaseHandler = specialCaseHandlers.get(value.toUpperCase());
            if (specialCaseHandler != null) {
                return specialCaseHandler.handle(fullData);
            }

            value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
            return value;
        }
    }

//    static class AHandler implements TypeHandler {
//
//        @Override
//        public String handle(String fullData) {
//            int idx = fullData.indexOf(':');
//            if (idx < 0) {
//                return "";
//            }
//            String value = fullData.substring(0, idx);
//            value = value.substring(2);
//            String[] args = value.split(";");
//
//            value = handleAdditionalCases(value, idx);
//            value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
//            return value;
//        }
//
//        public String handleAdditionalCases(String value) {
//            return "";
//        }
//    }
//
//
//    static class Type20Handler extends AHandler {
//        @Override
//        public String handleAdditionalCases(String value) {
//            value = value.substring(2);
//            String[] args = value.split(";");
//            return args[0];
//        }
//    }

//    static class TypeRestHandler extends AHandler {
//        @Override
//        public String handleAdditionalCases(String value) {
//            String[] args = value.split(";");
//            for (int a = 0; a < args.length; a++) {
//                if (args[a].indexOf('=') >= 0) {
//                    continue;
//                }
//                value = args[a];
//            }
//            if (value.startsWith("X-")) {
//                value = value.substring(2);
//            }
//            value = getResultingPhone(value);
//            return value;
//        }
//
//        public String getResultingPhone(String value) {
//            return "default phone value";
//        }
//    }


    static class PrefHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            return getString(R.string.PhoneMain);
        }
    }

    static class HomeHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            return getString(R.string.PhoneHome);
        }
    }

    static class MobileHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            return getString(R.string.PhoneMobile);
        }
    }

    static class OtherHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            return getString(R.string.PhoneOther);
        }
    }

    static class WorkHandler implements TypeHandler {
        @Override
        public String handle(String fullData) {
            return getString(R.string.PhoneWork);
        }
    }
}
