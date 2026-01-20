package rts.model.serverUnits;

import rts.view.clientModel.PlayerObjectSprite;

public class Archer extends Unit
{
    private final int minRange = 0;
    private final int maxRange = 100;
    private final int enemySpotRange = 50;
    private final boolean autoAttack = true;
    private final int damage = 20;

    @Override
    public void activate()
    {
    }
    private PlayerObjectSprite searchForEnemy()
    {
        return null;
    }
    private boolean isValidAttackDist(final double distance)
    {
        return (distance >= minRange && distance <= maxRange);
    }
    @Override
    public int getAttackDistance()
    {
        return maxRange;
    }
    @Override
    public int getDamage()
    {
        return damage;
    }
}
