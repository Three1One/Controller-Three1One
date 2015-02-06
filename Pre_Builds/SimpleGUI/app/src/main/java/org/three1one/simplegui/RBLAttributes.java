package org.three1one.simplegui;

/**
 * Created by sam on 05/12/2014.
 */

import java.util.HashMap;

public class RBLAttributes {
    private static HashMap<String, String> attributes = new HashMap<String, String>();
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String BLE_SHIELD_TX = "713d0003-503e-4c75-ba94-3148f18d941e";
    public static String BLE_SHIELD_RX = "713d0002-503e-4c75-ba94-3148f18d941e";
    public static String BLE_SHIELD_SERVICE = "713d0000-503e-4c75-ba94-3148f18d941e";
    public static int DEFAULT_BYTES_VIA_BLE = 20;
    public static byte INITIAL_MESSAGE_PACKET = 0x03;
    static {
        // RBL Services.
        attributes.put("713d0000-503e-4c75-ba94-3148f18d941e",
                "BLE Shield Service");
        // RBL Characteristics.
        attributes.put(BLE_SHIELD_TX, "BLE Shield TX");
        attributes.put(BLE_SHIELD_RX, "BLE Shield RX");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}

