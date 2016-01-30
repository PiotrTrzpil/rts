package rts.controller.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import rts.controller.netEvents.NetEvent;
import rts.controller.netEvents.toClientIngame.ToClientServerLost;
import rts.controller.netEvents.toServerEvents.ToServerEvent;
import rts.controller.server.GameServer;
import rts.misc.P;
import rts.view.ViewModel;

// TODO: Auto-generated Javadoc
/**
 * Sieciowe polaczenie od strony klienta.
 */
public class ClientSideConnectionOnline extends ClientSideConnection implements Runnable
{
    /** The socket. */
    private Socket socket;
    /** The output. */
    private ObjectOutputStream output;
    /** The input. */
    private ObjectInputStream input;

    public void connect(final String hostName) throws UnknownHostException, IOException
    {
        try
        {
            socket = new Socket(hostName, GameServer.portNumber);
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            new Thread(this).start();
        }
        catch(final IOException e1)
        {
            try
            {
                if(socket != null)
                {
                    socket.close();
                }
            }
            catch(final Exception e)
            {
                P.er("Exception while closing socket.");
            }
            throw e1;
        }
    }
    /*
     * (non-Javadoc)
     *
     * @see
     * rts.controller.connection.ClientSideConnection#send(rts.controller.netEvents
     * .NetEvent)
     */
    @Override
    public void send(final ToServerEvent obj)
    {
        try
        {
            if(ViewModel.console != null)
            {
                ViewModel.console.print("CLIENT SIDE [Online] Sent: " + obj);
            }
            output.writeObject(obj);
            output.flush();
        }
        catch(final Exception e1)
        {
            e1.printStackTrace();
            try
            {
                socket.close();
            }
            catch(final Exception e)
            {
                e.printStackTrace();
            }
            return;
        }
    }
    /**
     * Zamkniecie polaczenia
     */
    @Override
    public void close()
    {
        try
        {
            P.pr("CLIENT CLOSING SOCKET!");
            if(socket != null)
            {
                socket.close();
            }
            if(input != null)
            {
                input.close();
            }
            if(output != null)
            {
                output.close();
            }
        }
        catch(final Exception e)
        {
            e.printStackTrace();
        }
    }
    // public class EventReceiver extends Thread
    // {
    /**
     * Odbieranie zdarzen od polaczenia serwera.
     */
    @Override
    public void run()
    {
        while(socket.isConnected())
        {
            NetEvent response = null;
            try
            {
                response = (NetEvent) input.readObject();
                receive(response);
            }
            catch(final SocketException e)
            {
                P.pr("SocketException. Closing connection.");
                close();
                receive(new ToClientServerLost());
                return;
            }
            catch(final IOException e)
            {
                // e.printStackTrace();
                close();
                receive(new ToClientServerLost());
                return;
            }
            catch(final Exception e)
            {
                e.printStackTrace();
                close();
                receive(new ToClientServerLost());
                return;
            }
        }
        // }
    }
}
