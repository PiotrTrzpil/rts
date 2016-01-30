package rts.view.clientModel.misc;

import java.util.EnumSet;
import rts.model.UnitType;
import rts.view.clientModel.BuildingSprite;
import rts.view.clientModel.PlayerObjectSprite;
import rts.view.clientModel.UnitSprite;

// TODO: Auto-generated Javadoc
/**
 * Oznaczenie typu obiektu
 */
public enum ObjectFlag
{
    /** The BUILDING. */
    BUILDING
    {
        @Override
        public boolean matchesObject(final PlayerObjectSprite object)
        {
            return object instanceof BuildingSprite;
        }
    },
    /** The UNIT. */
    UNIT
    {
        @Override
        public boolean matchesObject(final PlayerObjectSprite object)
        {
            return object instanceof UnitSprite;
        }
    },
    /** The SOLDIER. */
    SOLDIER
    {
        @Override
        public boolean matchesObject(final PlayerObjectSprite object)
        {
            if(object instanceof UnitSprite)
            {
                final UnitSprite unit = (UnitSprite) object;
                if(unit.getType().equals(UnitType.WARRIOR)
                    || unit.getType().equals(UnitType.ARCHER))
                {
                    return true;
                }
            }
            return false;
        }
    },
    /** The CIVIL. */
    CIVIL
    {
        @Override
        public boolean matchesObject(final PlayerObjectSprite object)
        {
            if(object instanceof UnitSprite)
            {
                final UnitSprite unit = (UnitSprite) object;
                if(!unit.getType().equals(UnitType.WARRIOR))
                {
                    return true;
                }
            }
            return false;
        }
    },
    /** The ALL. */
    ALL
    {
        @Override
        public boolean matchesObject(final PlayerObjectSprite object)
        {
            return true;
        }
    };
    /**
     * Matches object.
     * 
     * @param object the object
     * @return true, if successful
     */
    public abstract boolean matchesObject(PlayerObjectSprite object);

    /**
     * The Class Set.
     */
    public static class Set
    {
        /** The set. */
        private final EnumSet<ObjectFlag> set;

        /**
         * Instantiates a new sets the.
         * 
         * @param set the set
         */
        public Set(final EnumSet<ObjectFlag> set)
        {
            this.set = set;
        }
        /**
         * Matches object.
         * 
         * @param obj1 the obj1
         * @return true, if successful
         */
        public boolean matchesObject(final PlayerObjectSprite obj1)
        {
            for(final ObjectFlag rel : set)
            {
                if(rel.matchesObject(obj1))
                {
                    return true;
                }
            }
            return false;
        }
    }
}
