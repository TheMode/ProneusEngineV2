package fr.proneus.engine.motable.listener;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

public class PingListener extends Listener {

    private Client client;

    public PingListener(Client client){
        this.client = client;
    }

    @Override
    public void connected(Connection connection) {
        client.updateReturnTripTime();
    }

    public void received (Connection connection, Object object) {
        if (object instanceof FrameworkMessage.Ping) {
            FrameworkMessage.Ping ping = (FrameworkMessage.Ping)object;
            // if (ping.isReply) System.out.println("Ping: " + connection.getReturnTripTime());
            client.updateReturnTripTime();
        }
    }
}
