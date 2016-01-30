package rts.controller.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toServerEvents.ToServerEvent;
import rts.controller.netEvents.toServerEvents.ToServerPlayerDisconnected;
import rts.controller.server.ServerNetBuffer;
import rts.misc.P;
import rts.view.ViewModel;

// TODO: Auto-generated Javadoc
/**
 * Sieciowe polaczenie od strony serwera
 */
public class ServerSideConnectionOnline extends ServerSideConnection implements Runnable
{
    /** socket. */
    private final Socket socket;
    /** The input. */
    private ObjectInputStream input;
    /** The output. */
    private ObjectOutputStream output;

    /**
     * Instantiates a new server side connection online.
     * 
     * @param clientSocket the client socket
     * @param queue the queue
     */
    public ServerSideConnectionOnline(final Socket clientSocket, final ServerNetBuffer queue)
    {
        super(queue);
        socket = clientSocket;
        try
        {
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        }
        catch(final Exception e1)
        {
            try
            {
                socket.close();
            }
            catch(final Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    /**
     * Odbieranie zdarzen od odpowiadajacego polaczenia po stronie klienta.
     */
    @Override
    public void run()
    {
        ToServerEvent message;
        while(true)
        {
            try
            {
                message = (ToServerEvent) input.readObject();
                receive(message);
            }
            catch(final Exception e1)
            {
                P.pr("CLOSING PLAYER SOCKET");
                try
                {
                    input.close();
                    output.close();
                    socket.close();
                }
                catch(final IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                message = new ToServerPlayerDisconnected();
                receive(message);
                return;
            }
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * rts.controller.connection.ServerSideConnection#send(rts.controller.netEvents
     * .NetEvent)
     */
    @Override
    public void send(final NetEvent object)
    {
        try
        {
            output.writeObject(object);
            output.flush();
            if(ViewModel.console != null)
            {
                ViewModel.console.print("SERVER SIDE [Online] Sent: " + object);
            }
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see rts.controller.connection.ServerSideConnection#close()
     */
    @Override
    public void close()
    {
        try
        {
            input.close();
            output.close();
            socket.close();
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
    }
}
