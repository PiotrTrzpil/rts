package rts.misc;

import java.io.File;
import java.io.IOException;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

public class GameMidiPlayer implements MetaEventListener
{
    public static final int END_OF_TRACK_MESSAGE = 47;
    private Sequencer sequencer;
    private boolean loop;
    private boolean paused;

    public GameMidiPlayer()
    {
        try
        {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequencer.addMetaEventListener(this);
        }
        catch(final MidiUnavailableException ex)
        {
            sequencer = null;
            ex.printStackTrace();
        }
    }
    private Sequence getSequence(final String filename)
    {
        try
        {
            return MidiSystem.getSequence(new File(filename));
        }
        catch(final InvalidMidiDataException ex)
        {
            ex.printStackTrace();
            return null;
        }
        catch(final IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }
    public void play(final String path, final boolean loop)
    {
        final Sequence sequence = getSequence(path);
        if(sequencer != null && sequence != null)
        {
            try
            {
                sequencer.setSequence(sequence);
                sequencer.start();
                this.loop = loop;
            }
            catch(final InvalidMidiDataException ex)
            {
                ex.printStackTrace();
            }
        }
    }
    public void meta(final MetaMessage event)
    {
        if(event.getType() == END_OF_TRACK_MESSAGE && sequencer != null && sequencer.isOpen()
            && loop)
        {
            sequencer.start();
        }
    }
    public void stop()
    {
        if(sequencer != null && sequencer.isOpen())
        {
            sequencer.stop();
            sequencer.setMicrosecondPosition(0);
        }
    }
    public void close()
    {
        if(sequencer != null && sequencer.isOpen())
        {
            sequencer.close();
        }
    }
    public void setPaused(final boolean paused)
    {
        if(this.paused != paused && sequencer != null)
        {
            this.paused = paused;
            if(paused)
            {
                sequencer.stop();
            }
            else
            {
                sequencer.start();
            }
        }
    }
}
