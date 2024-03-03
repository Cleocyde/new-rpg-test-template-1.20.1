package net.cleocyde.newrpgtest;

import net.cleocyde.newrpgtest.stats.Status;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public class EntityData {

    public PlayerEntity entity;
    public MinecraftServer server;
    public Status status;

    public EntityData(MinecraftServer server, PlayerEntity entity){
        this.entity = entity;
        this.server = server;
        status = new Status(this);

    }

}