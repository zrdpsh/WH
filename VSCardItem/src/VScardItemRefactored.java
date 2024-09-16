import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Первоначальный метод - getType внутри VcardItem
 * ЦС первоначального метода - 3
 *
 * Значения выбираются при помощи разветвлённой if-else
 *
 * Способы снижения ЦС:
 * 1. Избавление от if-else с помощью ad-hoc полиморфизма
 * 2. Избавление от switch
 *      добавил интерфейс TypeHandler и 11 классов-наследников -
 *      добавил класс ContactTypeHandler,
 *      внутри вызов getType и статический дата-класс
 *      "значение на вход - нужный TypeHandler на выход"
 *
 * 3. Избавление от проверок на Null
 *
 *
 * ЦС нового кода - 1
 */

public class VScardItem {

    public ArrayList<String> vcardData = new ArrayList<>();
    public String fullData = "";
    public int type;
    public boolean checked = true;

    public String getType() {
        return ContactTypeHandler.getType(type, fullData);
    }
}

public class ContactTypeHandler {
    private static final Map<Integer, TypeHandler> handlers = new HashMap<>();
    private static final Map<String, TypeHandler> specialCaseHandlers = new HashMap<>();

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

    public static String getType(int type, String fullData) {
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
            return  args[0].substring(0, 1).toUpperCase()
                    + value.substring(1).toLowerCase();
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

            TypeHandler specialCaseHandler = specialCaseHandlers.get(value.toUpperCase());
            String result = Optional
                    .ofNullable(specialCaseHandler.handle(fullData))
                    .orElse("there were no applicable String");

            result = result.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
            return result;

        }
    }

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
