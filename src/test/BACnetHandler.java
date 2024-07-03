package test;


import com.serotonin.bacnet4j.type.enumerated.ObjectType;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;

import java.util.HashMap;
import java.util.Map;

public class BACnetHandler {
    private static int invokeId = 0;
    public final static String TAG = "BACnetHandler";

    /*
        beverly test device
        pump 3000508
        1 min
        chiller 3000503
        v 3000506
        5 mins
        close

        192.168.6.174:binary_output:3000508:1
     */


    private static byte[] getLen(byte[] bArr){
        int len = bArr.length;
        if(len>4){
            bArr[2] = (byte)((len >> 8) & 0xff);
            bArr[3] = (byte)(len & 0xff);
        }
        return bArr;
    }


    /**
     *     public static final ObjectType analogInput = new ObjectType(0);
     *     public static final ObjectType analogOutput = new ObjectType(1);
     *     public static final ObjectType analogValue = new ObjectType(2);
     *     public static final ObjectType binaryInput = new ObjectType(3);
     *     public static final ObjectType binaryOutput = new ObjectType(4);
     *     public static final ObjectType binaryValue = new ObjectType(5);
     *     public static final ObjectType calendar = new ObjectType(6);
     *     public static final ObjectType command = new ObjectType(7);
     *     public static final ObjectType device = new ObjectType(8);
     *     public static final ObjectType eventEnrollment = new ObjectType(9);
     *     public static final ObjectType file = new ObjectType(10);
     *     public static final ObjectType group = new ObjectType(11);
     *     public static final ObjectType loop = new ObjectType(12);
     *     public static final ObjectType multiStateInput = new ObjectType(13);
     *     public static final ObjectType multiStateOutput = new ObjectType(14);
     *     public static final ObjectType notificationClass = new ObjectType(15);
     *     public static final ObjectType program = new ObjectType(16);
     *     public static final ObjectType schedule = new ObjectType(17);
     *     public static final ObjectType averaging = new ObjectType(18);
     *     public static final ObjectType multiStateValue = new ObjectType(19);
     *     public static final ObjectType trendLog = new ObjectType(20);
     *     public static final ObjectType lifeSafetyPoint = new ObjectType(21);
     *     public static final ObjectType lifeSafetyZone = new ObjectType(22);
     */
    private final static String[] objectTypeArr = {"analog_input", "analog_output", "analog_value", "binary_input", "binary_output", "binary_value",
            "calendar", "command", "device", "event_enrollment", "file", "group", "loop",
            "multi_state_input", "multi_state_output", "notification_class", "program", "schedule",
            "averaging", "multi_state_value","trend_log","life_safety_point","life_safety_zone"
    };

    private static Map<String, ObjectType> objectTypeMap;
    private static Map<String, PropertyIdentifier> propertyIdentifierMap;
    private static Map<String, Byte> propertyByteMap;
    public final static String DESC_PRESENT_VALUE = "present_value";
    public final static String DESC_STATUS_FLAGS = "status_flags";
    public final static String DESC_OUT_OF_SERVICE = "out_of_service";

    public final static String DESC_VENDOR_PROPRIETARY_VALUE = "vendor_proprietary_value";
    public final static byte BYTE_PRESENT_VALUE = (byte) 0x55;
    public final static byte BYTE_STATUS_FLAGS = (byte) 0x6F;
    public final static byte BYTE_OUT_OF_SERVICE = (byte) 0x51;



    static {
        propertyByteMap = new HashMap<>();
        propertyByteMap.put(DESC_PRESENT_VALUE, BYTE_PRESENT_VALUE);
        propertyByteMap.put(DESC_STATUS_FLAGS, BYTE_STATUS_FLAGS);
        propertyByteMap.put(DESC_OUT_OF_SERVICE, BYTE_OUT_OF_SERVICE);

        objectTypeMap = new HashMap<>();
        objectTypeMap.put("analog_input", ObjectType.analogInput);
        objectTypeMap.put("analog_output", ObjectType.analogOutput);
        objectTypeMap.put("analog_value", ObjectType.analogValue);
        objectTypeMap.put("binary_input", ObjectType.binaryInput);
        objectTypeMap.put("binary_value", ObjectType.binaryOutput);
        objectTypeMap.put("calendar", ObjectType.calendar);
        objectTypeMap.put("command", ObjectType.command);
        objectTypeMap.put("device", ObjectType.device);
        objectTypeMap.put("event_enrollment", ObjectType.eventEnrollment);
        objectTypeMap.put("file", ObjectType.file);
        objectTypeMap.put("group", ObjectType.group);
        objectTypeMap.put("loop", ObjectType.loop);
        objectTypeMap.put("multi_state_input", ObjectType.multiStateInput);
        objectTypeMap.put("multi_state_output", ObjectType.multiStateOutput);
        objectTypeMap.put("notification_class", ObjectType.notificationClass);
        objectTypeMap.put("program", ObjectType.program);
        objectTypeMap.put("schedule", ObjectType.schedule);
        objectTypeMap.put("averaging", ObjectType.averaging);
        objectTypeMap.put("multi_state_value", ObjectType.multiStateValue);
        objectTypeMap.put("trend_log", ObjectType.trendLog);
        objectTypeMap.put("life_safety_point", ObjectType.lifeSafetyPoint);
        objectTypeMap.put("life_safety_zone", ObjectType.lifeSafetyZone);

        propertyIdentifierMap = new HashMap<>();
        propertyIdentifierMap.put("present_value", PropertyIdentifier.presentValue);
        propertyIdentifierMap.put("status_flags", PropertyIdentifier.statusFlags);
        propertyIdentifierMap.put("out_of_service", PropertyIdentifier.outOfService);
        propertyIdentifierMap.put("vendor_proprietary_value", PropertyIdentifier.vendorProprietaryValue);// todo check
    }

    private static byte getByteByProType(String propertyType) {
        if (!propertyByteMap.containsKey(propertyType)) {
            return (byte) 0x55;
        } else {
           return propertyByteMap.get(propertyType);
        }
    }

    public static ObjectType convertObjectType(String s) {
        return objectTypeMap.get(s);
    }

    public static PropertyIdentifier convertPropertyIdentifier(String s) {
        return propertyIdentifierMap.get(s);
    }

    public static int objectStringToInt(String s){
        for(int i = 0; i< objectTypeArr.length; i++){
            if(s.contentEquals(objectTypeArr[i])){
                return (i & 0xffff);
            }
        }
        return 0;
    }
    public static String objectIntToString(int num){
        if(num< objectTypeArr.length){
            return objectTypeArr[num];
        }
        return "";
    }
    public static String getObjectName(int index){
        if(index>=0 && index< objectTypeArr.length){
            return objectTypeArr[index];
        }
        return "";
    }


    private final static String[] apduPropertyArr = {
            "system_status", "object_name", "service", "object_list"
    };
    private final static int[] apduPropertyIdArr = {
            112, 77, 97, 76
    };
    public static int apduPropertyStringToInt(String s){
        for(int i=0;i<apduPropertyArr.length;i++){
            if(s.contentEquals(apduPropertyArr[i])){
                return (apduPropertyIdArr[i]);
            }
        }
        return 0;
    }
    public static String apduPropertyIntToString(int num){
        for(int i=0;i<apduPropertyIdArr.length;i++){
            if(apduPropertyIdArr[i]==num){
                return apduPropertyArr[i];
            }
        }
        return "";
    }

}
