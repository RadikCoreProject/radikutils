package org.radikutils.net;

public interface IpAdress {
    boolean isEmpty();

    void clear();

    IpAdress getMask(IpAdress mask);

    void setIpAddress(String ipAddress);
}
