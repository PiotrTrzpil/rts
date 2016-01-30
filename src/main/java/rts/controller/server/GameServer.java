package rts.controller.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import rts.controller.Command;
import rts.controller.connection.ServerSideConnection;
import rts.controller.connection.ServerSideConnectionOnline;
import rts.misc.P;

// TODO: Auto-generated Javadoc
/**
 * Serwer uzywany w grze
 */
public class GameServer extends Server implements Runnable
{
    /** Numer portu uzywanego do polaczen. */
    public static final int portNumber = 6113;
    /** The server socket. */
    private final ServerSocket serverSocket;
    /** The server queue. */
    private final ServerNetBuffer serverBuffer;

    /**
     * Instantiates a new initial server.
     * 
     * @param controller the controller
     * @param serverQueue the server queue
     * @throws Exception the exception
     */
    public GameServer(final ServerController controller, final ServerNetBuffer serverQueue)
            throws Exception
    {
        super(controller, controller.getQueue(), new ArrayList<ServerSideConnection>());
        //        this.controller = controller;
        serverBuffer = serverQueue;
        serverSocket = new ServerSocket(portNumber);
    }
    public void stop()
    {
        stopAcceptingConnections();
        removeAll();
    }
    /**
     * Stop accepting connections.
     */
    public void stopAcceptingConnections()
    {
        try
        {
            serverSocket.close();
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
    }
    /**
     * Akceptowanie nowych polaczen. Po zaakceptowaniu tworzony jest nowy watek
     * obslugujacy klienta.
     */
    @Override
    public void run()
    {
        Socket client;
        while(!serverSocket.isClosed())
        {
            try
            {
                client = serverSocket.accept();
                P.pr("Accepted from: " + client.getInetAddress());
                final ServerSideConnectionOnline connection = new ServerSideConnectionOnline(
                        client, serverBuffer);
                gameQueue.add(new Command()
                {
                    @Override
                    public void execute()
                    {
                        addConnection(connection);
                        new Thread(connection).start();
                        //                        connection.send(new ToClientInit());
                    }
                });
            }
            catch(final SocketException e)
            {
                P.pr("STOPPED ACCEPTING CONNECTIONS");
                return;
            }
            catch(final IOException e)
            {
                e.printStackTrace();
                continue;
            }
        }
    }
}
