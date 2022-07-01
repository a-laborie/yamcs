package org.yamcs.tctm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.yamcs.ConfigurationException;
import org.yamcs.YConfiguration;
import org.yamcs.commanding.PreparedCommand;

/**
 * Sends raw packets on UDP socket.
 * 
 * @author nm
 *
 */
public class UdpTcDataLink extends AbstractThreadedTcDataLink {

    protected DatagramSocket socket;
    protected String host;
    protected int port;
    InetAddress address;

    @Override
    public void init(String yamcsInstance, String name, YConfiguration config) throws ConfigurationException {
        super.init(yamcsInstance, name, config);
        host = config.getString("host");
        port = config.getInt("port");
    }

    @Override
    protected void startUp() throws SocketException, UnknownHostException {
        address = InetAddress.getByName(host);
        socket = new DatagramSocket();
    }

    @Override
    public void doDisable() {
        if (socket != null) {
            socket.close();
            socket = null;
        }
        if (thread != null) {
            thread.interrupt();
        }
    }

    @Override
    public void doEnable() {
        thread = new Thread(this);
        thread.setName(this.getClass().getSimpleName() + "-" + linkName);
        thread.start();
    }

    @Override
    public String getDetailedStatus() {
        if (isDisabled()) {
            return String.format("DISABLED (should connect to %s:%d", host, port);
        } else {
        return String.format("OK, connected to %s:%d", host, port);
        }
    }

    @Override
    public void shutDown() {
        socket.close();
    }

    @Override
    public void uplinkCommand(PreparedCommand pc) throws IOException {
        byte[] binary = postprocess(pc);
        if (binary == null) {
            return;
        }

        DatagramPacket packet = new DatagramPacket(binary, binary.length, address, port);
        socket.send(packet);
        dataCount.getAndIncrement();
        ackCommand(pc.getCommandId());
    }

    @Override
    protected Status connectionStatus() {
        return Status.OK;
    }
}
