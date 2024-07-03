package test;

import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import org.code_house.bacnet4j.wrapper.api.Device;
import org.code_house.bacnet4j.wrapper.device.ip.IpDevice;
import org.code_house.bacnet4j.wrapper.ip.BacNetIpClient;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MyBacnet {

    private BacNetIpClient client;
    private Set<Device> devices;
    private int deviceId = 1441;
    private long timeout = 30L;
    public MyBacnet () {
        client = new BacNetIpClient("255.255.255.255", deviceId);
        discoverDevices();
    }

    private void discoverDevices() {
        System.out.println("Device id " + deviceId);
        System.out.println("Fetching devices for "  + " address with " + timeout + " second timeout");
        client.start();
        devices = client.discoverDevices(TimeUnit.SECONDS.toMillis(timeout));
        System.out.println("Discovery complete, found device num:"+devices.size());
    }

    private Device findDevice(String ip) {
        if (devices == null) return null;
        Optional<Device> optional = devices.stream().filter(s ->((IpDevice) s).getHostAddress().equals(ip)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        System.out.println("Cannot find Device by ip:"+ip);
        return null;
    }

    private Device findDevice(String ip, int deviceInstanceNum) {
        if (devices == null) return null;
        Optional<Device> optional = devices.stream().filter(s ->
                s.getInstanceNumber() == deviceInstanceNum && ((IpDevice) s).getHostAddress().equals(ip)
        ).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        System.out.println("Cannot find Device by ip:"+ip+"+  id:"+deviceInstanceNum);
        return null;
    }

    public void control(String msg){
        System.out.println("control: " + msg);
        try{
            if(msg.contentEquals("test")){
                //sendTest();
                return;
            }else if(msg.contentEquals("search")){
                discoverDevices();
                return;
            }
            String[] strArr = msg.split("\\:");
            if(strArr.length>1){
                if(strArr[0].contentEquals("test")){

                }else if(strArr[0].contentEquals("system_status")){
                    String ip = strArr[1];

                }else if(strArr[0].contentEquals("object_name")){
                    String ip = strArr[1];

                }else if(strArr[0].contentEquals("service")){
                    String ip = strArr[1];

                }else if(strArr[0].contentEquals("object_list")){
                    String ip = strArr[1];

                }else if(strArr[0].contentEquals("property_list")){
                    if(strArr.length==5) {
                        String ip = strArr[1];
                        String deviceInstanceNum = strArr[2];
                        String objectInstanceNumber = strArr[3];
                        String objectType = strArr[4];
                    }
                }else if(strArr[0].contentEquals("polling")){
                    if(strArr.length==5) {
                        String ip = strArr[1];
                        String objectInstanceNumber = strArr[2];
                        String objectType = strArr[3];
                        String propertyType = strArr[4];

                        Device device = findDevice(ip);
                        if (device != null) {
                            ThreadPoolUtil.execute(() -> {
                                ObjectIdentifier id = new ObjectIdentifier(BACnetHandler.convertObjectType(objectType), Integer.parseInt(objectInstanceNumber));
                                String propertyValue = client.readProperty(device, id, BACnetHandler.convertPropertyIdentifier(propertyType));
                                String r = objectInstanceNumber + ":" + objectType + ":" + propertyType + ":" + propertyValue;
                                System.out.println( "response: " + r);
                            });
                        }
                    }
                    if(strArr.length==6) {
                        String ip = strArr[1];
                        String deviceInstanceNum = strArr[2];
                        String objectInstanceNumber = strArr[3];
                        String objectType = strArr[4];
                        String propertyType = strArr[5];

                        Device device = findDevice(ip, Integer.parseInt(deviceInstanceNum));
                        if (device != null) {
                            ThreadPoolUtil.execute(() -> {
                                ObjectIdentifier id = new ObjectIdentifier(BACnetHandler.convertObjectType(objectType), Integer.parseInt(objectInstanceNumber));
                                String propertyValue = client.readProperty(device, id, BACnetHandler.convertPropertyIdentifier(propertyType));
                                String r = deviceInstanceNum + ":" + objectInstanceNumber + ":" + objectType + ":" + propertyType + ":" + propertyValue;
                                System.out.println("response: " + r);
                            });
                        }
                    }
                }else if(strArr[0].contentEquals("multi_polling")){
                    if(strArr.length==4) {
                        String ip = strArr[1];
                        String type = strArr[2];
                        String num_list = strArr[3];
//                        sendReadPropertyMultiValue(strArr[1], strArr[2], strArr[3]);
                    }
                }else if(strArr[0].contentEquals("multi_object_name")){
                    if(strArr.length==4) {
                        String ip = strArr[1];
                        String type = strArr[2];
                        String num_list = strArr[3];
//                        sendReadPropertyMultiObjName(strArr[1], strArr[2], strArr[3]);
                    }
                }else if(strArr[0].contentEquals("multi_object_info")){
                    // multi_object_info:[ip]:[did]:[gw_id]
                    if(strArr.length==4){
                        String ip = strArr[1];
                        int deviceInstanceNum = Integer.parseInt(strArr[2]);
                    }

                    // multi_object_info:[ip]:[gw_id]
                    if(strArr.length==3) {
                        String ip = strArr[1];
                        String gw_id = strArr[2];
                    }
                }else if(strArr[0].contentEquals("multi_object_prop")){
                    if(strArr.length==3) {
                        String ip = strArr[1];
                        String objectAndProps = strArr[2];
                        /**
                         * multi_object_prop:192.168.10.191:life_safety_point,20000|present_value|status_flags;life_safety_point,20001|present_value|status_flags;
                         */
                    }
                }else if(strArr[0].contentEquals("init_gw_id")){
                    if(strArr.length==2) {
                        String gatewayId = strArr[1];
                    }
                }else{
                    if(strArr.length==4) {
                    }
                    if(strArr.length==5) {
                    }
                }
            }
        }catch(Exception e){e.printStackTrace();}
    }

    public void end() {
        if (client != null) {
            client.stop();
        }
    }
}
