import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public interface PermissionHandler {
    boolean handle(int requestCode, String[] permissions, int[] grantResults);
}



public class CameraPermissionHandler implements PermissionHandler {
    @Override
    public boolean handle(int requestCode, String[] permissions, int[] grantResults) {
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (granted && GroupCallActivity.groupCallInstance != null) {
                GroupCallActivity.groupCallInstance.enableCamera();
                return true;
        }
        showPermissionErrorAlert(R.raw.permission_request_camera, LocaleController.getString(R.string.VoipNeedCameraPermission));
        return true;
    }
}

public class StoragePermissionHandler implements PermissionHandler {
    @Override
    public boolean handle(int requestCode, String[] permissions, int[] grantResults) {
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (!granted) {
            showPermissionErrorAlert(R.raw.permission_request_folder, requestCode == REQUEST_CODE_EXTERNAL_STORAGE_FOR_AVATAR ?
                    LocaleController.getString(R.string.PermissionNoStorageAvatar) :
                    LocaleController.getString(R.string.PermissionStorageWithHint));
            return true;
        }
        ImageLoader.getInstance().checkMediaPaths();
        return true;
    }
}

public class ContactPermissionHandler implements PermissionHandler {
    @Override
    public boolean handle(int requestCode, String[] permissions, int[] grantResults) {
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        if (!granted) {
            showPermissionErrorAlert(R.raw.permission_request_contacts, LocaleController.getString(R.string.PermissionNoContactsSharing));
            return false;
        }
        ContactsController.getInstance(currentAccount).forceImportContacts();
        return true;
    }
}


public class VideoMessagePermissionHandler implements PermissionHandler {
    @Override
    public boolean handle(int requestCode, String[] permissions, int[] grantResults) {
        AtomicBoolean audioGranted = new AtomicBoolean(true);
        AtomicBoolean cameraGranted = new AtomicBoolean(true);

        IntStream.range(0, Math.min(permissions.length, grantResults.length))
                .forEach(i -> {
                    if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
                        audioGranted.set(grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    }
                    if (Manifest.permission.CAMERA.equals(permissions[i])) {
                        cameraGranted.set(grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    }
                });

        if (requestCode == REQUEST_CODE_VIDEO_MESSAGE && (!audioGranted.get() || !cameraGranted.get())) {
            showPermissionErrorAlert(R.raw.permission_request_camera, LocaleController.getString(R.string.PermissionNoCameraMicVideo));
        }
        if (!audioGranted.get()) {
            showPermissionErrorAlert(R.raw.permission_request_microphone, LocaleController.getString(R.string.PermissionNoAudioWithHint));
        }
        if (!cameraGranted.get()) {
            showPermissionErrorAlert(R.raw.permission_request_camera, LocaleController.getString(R.string.PermissionNoCameraWithHint));
        }
        if (SharedConfig.inappCamera) {
            CameraController.getInstance().initCamera(null);
            return false;
        }
        return true;
    }
}
