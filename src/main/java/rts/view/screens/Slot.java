package rts.view.screens;

public enum Slot
{
    PLAYER("Player"),
    OPEN("Open"),
    CLOSED("Closed");
    private String name;

    private Slot(final String name)
    {
        this.name = name;
    }
    public void setName(final String name)
    {
        this.name = name;
    }
    @Override
    public String toString()
    {
        return name;
    }
}
