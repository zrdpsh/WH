import android.Manifest;
import android.content.pm.PackageManager;
import androidx.fragment.app.FragmentActivity;

import org.telegram.messenger.GroupCallActivity;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.camera.CameraController;
import org.telegram.ui.Components.AlertsCreator;

import java.util.*;

public class BasePermissionsActivity extends FragmentActivity {

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

    private final Map<Set<Integer>, PermissionHandler> permissionHandlers = new HashMap<>();

    public BasePermissionsActivity() {
        initializeHandlers();
    }

    private void initializeHandlers() {
        permissionHandlers.put(Set.of(104), this::handleCameraPermission);
        permissionHandlers.put(Set.of(REQUEST_CODE_EXTERNAL_STORAGE, REQUEST_CODE_EXTERNAL_STORAGE_FOR_AVATAR), this::handleStoragePermission);
        permissionHandlers.put(Set.of(REQUEST_CODE_ATTACH_CONTACT), this::handleContactPermission);
        permissionHandlers.put(Set.of(3, REQUEST_CODE_VIDEO_MESSAGE), this::handleAudioVideoPermission);
        permissionHandlers.put(Set.of(18, 19, 22, REQUEST_CODE_OPEN_CAMERA), this::handleGenericCameraPermission);
        permissionHandlers.put(Set.of(REQUEST_CODE_GEOLOCATION), this::handleLocationPermission);
        permissionHandlers.put(Set.of(REQUEST_CODE_MEDIA_GEO), this::handleMediaGeoPermission);
    }

    protected boolean checkPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        grantResults = grantResults == null ? new int[0] : grantResults;
        permissions = permissions == null ? new String[0] : permissions;


        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;


        for (Map.Entry<Set<Integer>, PermissionHandler> entry : permissionHandlers.entrySet()) {
            if (entry.getKey().contains(requestCode)) {
                return entry.getValue().handlePermission(permissions, grantResults, granted);
            }
        }


        return handleDefaultPermission(permissions, grantResults, granted);
    }

    private boolean handleDefaultPermission(String[] permissions, int[] grantResults, boolean granted) {
        return true;
    }

    private boolean handleCameraPermission(String[] permissions, int[] grantResults, boolean granted) {
        if (granted) {
            if (GroupCallActivity.groupCallInstance != null) {
                GroupCallActivity.groupCallInstance.enableCamera();
            }
        }
        if (!granted){
            showPermissionErrorAlert(R.raw.permission_request_camera, LocaleController.getString(R.string.VoipNeedCameraPermission));
        }
        return true;
    }

    private boolean handleStoragePermission(String[] permissions, int[] grantResults, boolean granted) {
        if (granted) {
            ImageLoader.getInstance().checkMediaPaths();
        }

        if (!granted){
            String message = (Arrays.asList(permissions).contains(REQUEST_CODE_EXTERNAL_STORAGE_FOR_AVATAR))
                    ? LocaleController.getString(R.string.PermissionNoStorageAvatar)
                    : LocaleController.getString(R.string.PermissionStorageWithHint);
            showPermissionErrorAlert(R.raw.permission_request_folder, message);
        }
        return true;
    }

    private boolean handleContactPermission(String[] permissions, int[] grantResults, boolean granted) {
        if (granted) {
            ContactsController.getInstance(currentAccount).forceImportContacts();
        }

        if (!granted){
            showPermissionErrorAlert(R.raw.permission_request_contacts, LocaleController.getString(R.string.PermissionNoContactsSharing));
        }
        return granted;
    }

    private boolean handleAudioVideoPermission(String[] permissions, int[] grantResults, boolean granted) {
        boolean audioGranted = true, cameraGranted = true;

        for (int i = 0; i < Math.min(permissions.length, grantResults.length); i++) {
            if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
                audioGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                break;
            }
            if (Manifest.permission.CAMERA.equals(permissions[i])) {
                cameraGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }

        if (!audioGranted || !cameraGranted) {
            showPermissionErrorAlert(R.raw.permission_request_camera,
                    LocaleController.getString(R.string.PermissionNoCameraMicVideo));
        }
        if (!audioGranted) {
            showPermissionErrorAlert(R.raw.permission_request_microphone,
                    LocaleController.getString(R.string.PermissionNoAudioWithHint));
        }
        if (!cameraGranted) {
            showPermissionErrorAlert(R.raw.permission_request_camera,
                    LocaleController.getString(R.string.PermissionNoCameraWithHint));
        }
        if (SharedConfig.inappCamera) {
            CameraController.getInstance().initCamera(null);
        }
        return audioGranted && cameraGranted;
    }

    private boolean handleGenericCameraPermission(String[] permissions, int[] grantResults, boolean granted) {
        if (!granted) {
            showPermissionErrorAlert(R.raw.permission_request_camera, LocaleController.getString(R.string.PermissionNoCameraWithHint));
        }
        return true;
    }

    private boolean handleLocationPermission(String[] permissions, int[] grantResults, boolean granted) {
        NotificationCenter.getGlobalInstance().postNotificationName(
                granted ? NotificationCenter.locationPermissionGranted : NotificationCenter.locationPermissionDenied
        );
        return true;
    }

    private boolean handleMediaGeoPermission(String[] permissions, int[] grantResults, boolean granted) {
        NotificationCenter.getGlobalInstance().postNotificationName(
                granted ? NotificationCenter.locationPermissionGranted : NotificationCenter.locationPermissionDenied, 1
        );
        return true;
    }

    private void showPermissionErrorAlert(@RawRes int iconRes, String message) {
        AlertsCreator.showSimpleAlert(this, LocaleController.getString("AppName"), message, Theme.getColor(Theme.key_dialogTextBlack), iconRes);
    }

    @FunctionalInterface
    private interface PermissionHandler {
        boolean handlePermission(String[] permissions, int[] grantResults, boolean granted);
    }
}
