import java.util.HashMap;
import java.util.Map;

/**
 * Первоначальный метод - checkPermissionsResult внутри BasePermissionActivity
 * ЦС первоначального метода - 21
 *
 * Способы снижения ЦС:
 * 1. Избавление от if-else с помощью ad-hoc полиморфизма,
 * вынесение логики в отдельные классы, наследуемые от общего интерфейса
 * 2. Избавление от вложенных if и циклов for
 *
 * переписанный метод - checkPermissionsResult внутри BasePermissionActivityRefactored
 * дополнительный файл - PermissionHandler
 * ЦС нового кода - 4
 */

public class BasePermissionActivityRefactored {
    public final static int REQUEST_CODE_GEOLOCATION = 2,
            REQUEST_CODE_EXTERNAL_STORAGE = 4,
            REQUEST_CODE_ATTACH_CONTACT = 5,
            REQUEST_CODE_CALLS = 7,
            REQUEST_CODE_OPEN_CAMERA = 20,
            REQUEST_CODE_VIDEO_MESSAGE = 150,
            REQUEST_CODE_EXTERNAL_STORAGE_FOR_AVATAR = 151,
            REQUEST_CODE_SIGN_IN_WITH_GOOGLE = 200,
            REQUEST_CODE_PAYMENT_FORM = 210,
            REQUEST_CODE_MEDIA_GEO = 211;

    protected int currentAccount = -1;


    protected boolean checkPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults == null) {
            grantResults = new int[0];
        }
        if (permissions == null) {
            permissions = new String[0];
        }

        PermissionHandler handler = PermissionHandlerFactory.getHandler(requestCode);
        if (handler != null) {
            return handler.handle(requestCode, permissions, grantResults);
        }

        return true;
    }

    public class PermissionHandlerFactory {

        private static final Map<Integer, PermissionHandler> handlers = new HashMap<>();

        static {
            handlers.put(104, new CameraPermissionHandler());
            handlers.put(REQUEST_CODE_EXTERNAL_STORAGE, new StoragePermissionHandler());
            handlers.put(REQUEST_CODE_EXTERNAL_STORAGE_FOR_AVATAR, new StoragePermissionHandler());
            handlers.put(REQUEST_CODE_ATTACH_CONTACT, new ContactPermissionHandler());
            handlers.put(REQUEST_CODE_VIDEO_MESSAGE, new VideoMessagePermissionHandler());
        }

        public static PermissionHandler getHandler(int requestCode) {
            return handlers.get(requestCode);
        }
    }
}

