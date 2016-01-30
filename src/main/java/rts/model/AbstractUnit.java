package rts.model;

import rts.model.ingameEvents.ChangeMoveEvent;

public interface AbstractUnit extends AbstractOwnableObject
{
    public UnitType getType();
    public float getMaxVelocity();
    public void setCurrentMove(ChangeMoveEvent changeMoveEvent);
}
