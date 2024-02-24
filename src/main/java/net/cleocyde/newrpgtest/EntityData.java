package net.cleocyde.newrpgtest;

import net.cleocyde.newrpgtest.stats.Status;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

public class EntityData {
    PlayerEntity player;
    MinecraftServer server;

    Status status;
    public EntityData(MinecraftServer server, PlayerEntity player){
        this.player = player;
        this.server = server;
        this.status = new Status(this);

    }

}
