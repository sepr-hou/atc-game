package seprhou.network;

public class Packet {
	public static class PacketType1Request {}
	public static class PacketType2Answer {boolean accepted = false;}
	public static class PacketType3Message {String message;}
}
