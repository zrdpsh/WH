interface PermissionState {
    boolean handlePermission(int requestCode, String[] permissions, int[] grantResults);
}
