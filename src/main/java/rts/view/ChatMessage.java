package rts.view;

/**
 * Wiadomosc czatu.
 */
public class ChatMessage
{
    /** The message. */
    private final String message;

    /**
     * Instantiates a new chat message.
     * 
     * @param string the string
     */
    public ChatMessage(final String string)
    {
        message = string;
    }
    /**
     * Gets the message.
     * 
     * @return the message
     */
    public String getMessage()
    {
        return message;
    }
}
