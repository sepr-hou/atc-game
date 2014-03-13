package seprhou.network;

import java.io.IOException;
import java.util.Scanner;

import seprhou.network.Packet.*;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class MPClient {
	public Client client;
	public static Scanner scanner;
	
	public MPClient() {
		client = new Client();
		scanner = new Scanner(System.in);
		register();
		
		MPCNetworkListener nl = new MPCNetworkListener();
		nl.init(client);
		client.addListener(nl);
		
		client.start();
		
		try {
			// TODO Change these values!!
			// timeout, host IP, tcpPort
			Log.info("Please enter the IP address");
			client.connect(60000, scanner.nextLine(), 54558);
		} catch (IOException e) {
			e.printStackTrace();
			client.stop();
		}
	}
	
	private void register() {
		Kryo kryo = client.getKryo();
		kryo.register(PacketType1Request.class);
		kryo.register(PacketType2Answer.class);
		kryo.register(PacketType3Message.class);
	}

	public static void main(String[] args) {
		new MPClient();
		Log.set(Log.LEVEL_DEBUG);

	}

}
