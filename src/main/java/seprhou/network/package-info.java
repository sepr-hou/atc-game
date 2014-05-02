/**
 * The network and multiplayer handling code of the game.
 *
 * <p>Note that some of the design is very prone to network latency issues, so don't try and run the game on anything
 * other than a LAN!
 *
 * <h4>Public Interface</h4>
 * <p>The {@link seprhou.network.GameEndpoint} interface abstracts over the entire multiplayer system, allowing the
 * {@link seprhou.gui.GameScreen} to be oblivious to whether it's running as the client, server or even just as a single
 * player. The core multiplayer code is in {@link seprhou.network.MultiServer} and {@link seprhou.network.MultiClient}
 * with code common to both network endpoints in {@link seprhou.network.NetworkCommon}.
 *
 * <h4>Internals</h4>
 * <p>The network code uses the KryoNet as its network libray and the Kryo libray for serializing game messages. We've
 * chosen to use the single threaded method of using KryoNet, which makes it wasier in a game with a game loop which
 * runs all the time.
 *
 * <p>The design is based on the server being "god" of the airspace and sends the client updates every frame when
 * anything happens.  All messages are prefixed with "CMsg" for messages SENT by the client, or
 * "SMsg" for messages SENT by the server.
 *
 * @see <a href="https://github.com/EsotericSoftware/kryo">Kryo</a>
 * @see <a href="https://github.com/EsotericSoftware/kryonet">KryoNet</a>
 *
 */
package seprhou.network;