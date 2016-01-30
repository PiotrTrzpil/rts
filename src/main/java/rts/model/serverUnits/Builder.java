package rts.model.serverUnits;

public class Builder extends Unit
{
    //    public class CmdGoAndBuild extends Cmd
    //    {
    //
    //        private final UnfinishedBuilding building;
    //
    //        public CmdGoAndBuild(final UnfinishedBuilding building)
    //        {
    //            this.building = building;
    //
    //        }
    //
    //        @Override
    //        public void startTask()
    //        {
    //            if(building.isComplete())
    //            {
    //                return;
    //            }
    //            // addCmdLast(new CmdMove(randomBuildPos()));
    //            // addCmdLast(new CmdHitWithHammer(building));
    //            // addCmdLast(new CmdGoAndBuild(building));
    //        }
    //
    //        @Override
    //        public boolean finishCond()
    //        {
    //            return true;
    //        }
    //
    //        private Coords randomBuildPos()
    //        {
    ////          final ConstRect rect = building.getBounds();
    //            final Random rand = new Random(System.currentTimeMillis());
    //            final int x = rand.nextInt(rect.getWidth());
    //            final int y = rand.nextInt(rect.getHeight());
    //
    //            return rect.pointNearRect(new Coords(rect.getX() + x, rect.getY() + y), 10, 10);
    //        }
    //
    //    }
    //    public class CmdHitWithHammer extends Cmd
    //    {
    //
    //        private final UnfinishedBuilding building;
    //
    //        public CmdHitWithHammer(final UnfinishedBuilding building)
    //        {
    //            this.building = building;
    //        }
    //
    //        @Override
    //        public void startTask()
    //        {
    //            if(building.canBeHitWithHammer())
    //            {
    //                System.out.println("HIT");
    //                building.hitWithHammer();
    //                // addCmdLast(new CmdWait(1000));
    //                // addCmdLast(new CmdHitWithHammer(building));
    //
    //            }
    //            else
    //            {
    //                getOwner().getWorkerOvermind().getBuilders().add(Builder.this);
    //            }
    //
    //            // addCmd(new CmdMove(building.getDoorPosX(),
    //            // building.getDoorPosY()));
    //
    //        }
    //
    //        @Override
    //        public boolean finishCond()
    //        {
    //            return true;
    //        }
    //
    //    }
    //    public Builder(final int x, final int y)
    //    {
    //        super(x, y);
    //
    //    }
    //    public void goAndBuild(final UnfinishedBuilding building)
    //    {
    //
    //        getOwner().getWorkerOvermind().getBuilders().remove(this);
    //        // addCmdLast(new CmdGoAndBuild(building));
    //
    //    }
    @Override
    public void activate()
    {
        //        getOwner().getWorkerOvermind().getBuilders().add(this);
    }
    //    @Override
    //    public void deathCleaning()
    //    {
    //        getOwner().getWorkerOvermind().getBuilders().remove(this);
    //    }
}
