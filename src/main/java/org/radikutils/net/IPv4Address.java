package org.radikutils.net;

import java.util.ArrayList;

// TODO: not released
public class IPv4Address implements IpAdress{
    public IPv4Address(String ipAddress) {
        if(ipAddress == null || ipAddress.isEmpty()) throw new IllegalArgumentException("IP address cannot be null or empty");
        if(!ipAddress.contains(".")) throw new WrongIPIndicateException("Invalid IP indicator");
        String[] bytes = ipAddress.split("\\.");
        if (bytes.length != 4) throw new WrongIPIndicateException("Invalid IP length");
        for (String b : bytes) {
            if(Integer.parseInt(b) > 255) throw new IPAdressByteException("Byte out of range");
        }
        b1 = Byte.parseByte(bytes[0]);
        b2 = Byte.parseByte(bytes[1]);
        b3 = Byte.parseByte(bytes[2]);
        b4 = Byte.parseByte(bytes[3]);
    }

    byte b1;
    byte b2;
    byte b3;
    byte b4;

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public IpAdress getMask(IpAdress mask) {
        return null;
    }

    @Override
    public void setIpAddress(String ipAddress) {

    }
}
