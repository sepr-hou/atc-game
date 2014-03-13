package seprhou.network;

import seprhou.network.Packet.*;

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
		if (o instanceof PacketType1Request) {
			PacketType2Answer answer = new PacketType2Answer();
			answer.accepted = true;
			c.sendTCP(answer);
		}
		if (o instanceof PacketType2Answer) {
			//do something
		}
		if (o instanceof PacketType3Message) {
			String message = ((PacketType3Message) o).message;
			Log.info(message);
		}
	}
}
