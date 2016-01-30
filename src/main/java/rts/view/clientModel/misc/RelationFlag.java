package rts.view.clientModel.misc;

import java.util.EnumSet;
import rts.controller.UserPlayer;
import rts.view.clientModel.PlayerObjectSprite;

// TODO: Auto-generated Javadoc
/**
 * Oznaczenie relacji obiektu do innego
 */
public enum RelationFlag
{
    /** The HOSTILE. */
    HOSTILE,
    /** The ALLIED. */
    ALLIED,
    /** The FRIENDLY. */
    FRIENDLY,
    /** The ALL. */
    ALL
    {
        @Override
        public boolean equalsRelation(final PlayerObjectSprite obj1, final UserPlayer player)
        {
            return true;
        }
    };
    /**
     * Equals relation.
     * 
     * @param obj1 the obj1
     * @param player the player
     * @return true, if successful
     */
    public boolean equalsRelation(final PlayerObjectSprite obj1, final UserPlayer player)
    {
        return equals(obj1.getRelationTo(player));
    }

    /**
     * The Class Set.
     */
    public static class Set
    {
        /** The set. */
        private final EnumSet<RelationFlag> set;

        /**
         * Instantiates a new sets the.
         * 
         * @param set the set
         */
        public Set(final EnumSet<RelationFlag> set)
        {
            this.set = set;
        }
        /**
         * Equals relation.
         * 
         * @param obj1 the obj1
         * @param player the player
         * @return true, if successful
         */
        public boolean equalsRelation(final PlayerObjectSprite obj1, final UserPlayer player)
        {
            for(final RelationFlag rel : set)
            {
                if(rel.equalsRelation(obj1, player))
                {
                    return true;
                }
            }
            return false;
        }
    }
}
