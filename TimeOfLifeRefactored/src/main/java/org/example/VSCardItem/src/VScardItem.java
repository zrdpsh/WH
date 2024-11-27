//package org.example.VSCardItem.src;
//
//import java.util.ArrayList;
//
//public class VScardItem {
//
//    public ArrayList<String> vcardData = new ArrayList<>();
//    public String fullData = "";
//    public int type;
//    public boolean checked = true;
//
//    public String getType() {
//        if (type == 4) {
//            return getString(R.string.ContactNote);
//        } else if (type == 3) {
//            return getString(R.string.ContactUrl);
//        } else if (type == 5) {
//            return getString(R.string.ContactBirthday);
//        } else if (type == 6) {
//            if ("ORG".equalsIgnoreCase(getRawType(true))) {
//                return getString(R.string.ContactJob);
//            } else {
//                return getString(R.string.ContactJobTitle);
//            }
//        }
//        int idx = fullData.indexOf(':');
//        if (idx < 0) {
//            return "";
//        }
//        String value = fullData.substring(0, idx);
//        if (type == 20) {
//            value = value.substring(2);
//            String[] args = value.split(";");
//            value = args[0];
//        } else {
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
//            switch (value) {
//                case "PREF":
//                    value = getString(R.string.PhoneMain);
//                    break;
//                case "HOME":
//                    value = getString(R.string.PhoneHome);
//                    break;
//                case "MOBILE":
//                case "CELL":
//                    value = getString(R.string.PhoneMobile);
//                    break;
//                case "OTHER":
//                    value = getString(R.string.PhoneOther);
//                    break;
//                case "WORK":
//                    value = getString(R.string.PhoneWork);
//                    break;
//            }
//        }
//        value = value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
//        return value;
//    }
//}
//}
