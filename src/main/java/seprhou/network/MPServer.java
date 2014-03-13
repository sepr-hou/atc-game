package seprhou.network;

import java.io.IOException;

import seprhou.network.Packet.*;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;


public class MPServer {
	private Server server;
	
	public MPServer() throws IOException {
		server = new Server();
		register();
		server.addListener(new MPSNetworkListener());
		server.bind(54558);
		server.start();
	}
	
	private void register() {
		Kryo kryo = server.getKryo();
		kryo.register(PacketType1Request.class);
		kryo.register(PacketType2Answer.class);
		kryo.register(PacketType3Message.class);
	}
	
	public static void main(String[] args) {
		try {
			new MPServer();
			Log.set(Log.LEVEL_DEBUG);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
