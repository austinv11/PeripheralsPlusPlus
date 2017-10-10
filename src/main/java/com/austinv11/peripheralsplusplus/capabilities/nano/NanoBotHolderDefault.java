package com.austinv11.peripheralsplusplus.capabilities.nano;

import net.minecraft.entity.Entity;

import java.util.UUID;

public class NanoBotHolderDefault implements NanoBotHolder {
    private int bots;
    private UUID antenna;
    private Entity entity;

    @Override
    public int getBots() {
        return bots;
    }

    @Override
    public void setBots(int bots) {
        this.bots = bots;
    }

    @Override
    public void setAntenna(UUID antennaIdentifier) {
        this.antenna = antennaIdentifier;
    }

    @Override
    public UUID getAntenna() {
        return antenna;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
