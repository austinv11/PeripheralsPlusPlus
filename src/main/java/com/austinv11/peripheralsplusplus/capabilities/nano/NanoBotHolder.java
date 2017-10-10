package com.austinv11.peripheralsplusplus.capabilities.nano;

import net.minecraft.entity.Entity;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Capability interface for entities that will hold nano bots
 */
public interface NanoBotHolder {

    /**
     * Get the bots the entity is infested with
     * @return number of bots on entity
     */
    int getBots();

    /**
     * Set the bots on an entity
     * @param bots number of bots to set on the entity
     */
    void setBots(int bots);

    /**
     * Set the antenna UUID the item is registered to
     * @param antennaIdentifier antenna UUID
     */
    void setAntenna(UUID antennaIdentifier);

    /**
     * Get the registered antenna id
     * @return antenna UUID
     */
    @Nullable
    UUID getAntenna();

    /**
     * Get the entity that this capability is handling
     * @return the entity that the bots are attached to
     */
    @Nullable
    Entity getEntity();

    /**
     * Sets the entity that the bots are attached to
     * If there is no entity set the entity will not be registered with the antenna registry on (re)load.
     * This should be set on the {@link net.minecraftforge.event.AttachCapabilitiesEvent<Entity>} event
     * @param entity entity the bots are on
     */
    void setEntity(Entity entity);
}
