package seprhou.network;

import seprhou.network.Packet.PacketType1;
import seprhou.network.Packet.PacketType2;
import seprhou.network.Packet.PacketType3;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class MPSNetworkListener extends Listener{
	
	//examples
	public void connected(Connection arg0) {
		Log.info("[Server] Someone had connected");
	}
	
	public void disconnected(Connection arg0) {
		Log.info("[Server] Someone has disconnected");
	}
	
	public void received(Connection c, Object o){
		if (o instanceof PacketType1) {
			PacketType2 answer = new PacketType2();
			answer.accepted = true;
			c.sendTCP(answer);
		}
		if (o instanceof PacketType2) {
			//do something
		}
		if (o instanceof PacketType3) {
			String message = ((PacketType3) o).message;
			Log.info(message);
		}
	}
}
