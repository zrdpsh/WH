import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VScardItem {

    public ArrayList<String> vcardData = new ArrayList<>();
    public String fullData = "";
    public int type;
    public boolean checked = true;

    public String getType() {

    }
}

public class ContactTypeHandler {
    private static final Map<Integer, TypeHandler> handlers = new HashMap<>();

    // Static block to initialize handlers
    static {
        handlers.put(3, new UrlHandler());
        handlers.put(4, new NoteHandler());
        handlers.put(5, new BirthdayHandler());
        handlers.put(6, new JobHandler());
        handlers.put(20, new Type20Handler());
    }

    public String getType(int type, String fullData) {
        TypeHandler handler = handlers.get(type);
        if (handler != null) {
            return handler.handle(fullData);
        } else {
            return new DefaultHandler().handle(fullData); // default handler for non-registered types
        }
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
            } else {
                return getString(R.string.ContactJobTitle);
            }
        }
    }

    static class AHandler implements TypeHandler {
        
        @Override
        public String handle(String fullData) {
            int idx = fullData.indexOf(':');
            if (idx < 0) {
                return "";
            }
            String value = fullData.substring(0, idx);
            value = value.substring(2);
            String[] args = value.split(";");

            value = handleAdditionalCases(value, idx);
            value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
            return value;
        }

        public String handleAdditionalCases(String value) {
            return "";
        }
    }


    static class Type20Handler extends AHandler {
        @Override
        public String handleAdditionalCases(String value) {
            value = value.substring(2);
            String[] args = value.split(";");
            return args[0];
        }
    }
    
    static class TypeRestHandler extends AHandler {
        @Override
        public String handleAdditionalCases(String value) {
            String[] args = value.split(";");
            for (int a = 0; a < args.length; a++) {
                if (args[a].indexOf('=') >= 0) {
                    continue;
                }
                value = args[a];
            }
            if (value.startsWith("X-")) {
                value = value.substring(2);
            }
            value = getResultingPhone(value);
            return value;
        }
        
        public String getResultingPhone(String value) {
            return "default phone value";
        }
    }
    
    static class PrefHandler extends TypeRestHandler {
        @Override
        public String getResultingPhone(String value) {
            return getString(R.string.PhoneMain);
        }
    }

    static class HomePhoneHandler extends TypeRestHandler {
        @Override
        public String getResultingPhone(String value) {
            return getString(R.string.PhoneMain);
        }
    }

    static class DefaultHandler extends AHandler {

        @Override
        public String handleAdditionalCases(String fullData) {

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

            switch (value) {
                case "PREF":
                    return getString(R.string.PhoneMain);
                case "HOME":
                    return getString(R.string.PhoneHome);
                case "MOBILE":
                case "CELL":
                    return getString(R.string.PhoneMobile);
                case "OTHER":
                    return getString(R.string.PhoneOther);
                case "WORK":
                    return getString(R.string.PhoneWork);
                default:
                    value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
                    return value;
            }
        }
    }
}
