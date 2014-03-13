package seprhou.network;

import seprhou.network.Packet.*;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class MPCNetworkListener extends Listener{
	private Client client;
	
	public void init(Client client) {
		this.client = client;
	}
	
	//examples
	public void connected(Connection arg0) {
		Log.info("[Client] You have connected");
		client.sendTCP(new PacketType1());
	}
	
	public void disconnected(Connection arg0) {
		Log.info("[Client] You have disconnected");
	}
	
	public void received(Connection c, Object o){
		if (o instanceof PacketType1) {
			//do something
		}
		if (o instanceof PacketType2) {
			boolean answer = ((PacketType2) o).accepted;
			
			if (answer){
				Log.info("Please enter you message for the server:");
				
				while(true){
					if(MPClient.scanner.hasNext()){
						PacketType3 mpacket = new PacketType3();
						mpacket.message = MPClient.scanner.nextLine();
						client.sendTCP(mpacket);
						Log.info("Please enter another message:");
					}
				}
			} else {
				c.close();
			}
		}
		if (o instanceof PacketType3) {
			//do something
		}
	}
	
}
