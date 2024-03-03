package net.cleocyde.newrpgtest.utils;

import net.cleocyde.newrpgtest.NewRPGTest;
import net.cleocyde.newrpgtest.stats.Attribute;
import net.cleocyde.newrpgtest.stats.Resource;
import net.cleocyde.newrpgtest.stats.Status;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

import static net.cleocyde.newrpgtest.stats.Status.entityData;

public class StateSaverAndLoader extends PersistentState {


    public Attribute Vitality = Status.Vitality;
    public Attribute Luck = Status.Vitality;
    public Attribute Strength = Status.Vitality;
    public Attribute Intelligence = Status.Vitality;
    public Attribute Agility = Status.Vitality;
    public Resource HP = Status.HP;
    public Resource EXP = Status.EXP;
    public Integer level = Status.level;

    public HashMap<UUID, Status> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putString("Vitality", Vitality.toString());
        nbt.putString("Luck", Luck.toString());
        nbt.putString("Strength", Strength.toString());
        nbt.putString("Intelligence", Intelligence.toString());
        nbt.putString("Agility", Agility.toString());
        nbt.putString("HP", HP.toString());
        nbt.putString("EXP", EXP.toString());
        nbt.putInt("level", level);

        NbtCompound playersNbt = new NbtCompound();
        players.forEach((uuid, playerData) -> {
            NbtCompound playerNbt = new NbtCompound();

            playerNbt.putString("Vitality", playerData.Vitality.toString());

            playersNbt.put(uuid.toString(), playerNbt);

            nbt.put("players", playersNbt);
        });


        return nbt;

    }

    public static StateSaverAndLoader createFromNbt(NbtCompound nbt) {
        StateSaverAndLoader state = new StateSaverAndLoader();
        state.Vitality = (Attribute) nbt.get("Vitality");
        state.Luck = (Attribute) nbt.get("Luck");
        state.Strength = (Attribute) nbt.get("Strength");
        state.Intelligence = (Attribute) nbt.get("Intelligence");
        state.Agility = (Attribute) nbt.get("Agility");
        state.HP = (Resource) nbt.get("HP");
        state.EXP = (Resource) nbt.get("EXP");
        state.level = nbt.getInt("level");

        NbtCompound playersNbt = nbt.getCompound("players");
        playersNbt.getKeys().forEach(key -> {
            Status playerData = new Status(entityData);

            playerData.Vitality = (Attribute) playersNbt.getCompound(key).get("Vitality");

            UUID uuid = UUID.fromString(key);
            state.players.put(uuid, playerData);
        });

        return state;
    }


    @SuppressWarnings("DataFlowIssue")
    public static StateSaverAndLoader getServerState(MinecraftServer server) {

        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        StateSaverAndLoader state = persistentStateManager.getOrCreate(StateSaverAndLoader::createFromNbt, StateSaverAndLoader::new, NewRPGTest.MOD_ID);

        state.markDirty();

        return state;
    }

    public static Status getPlayerState(LivingEntity player) {
        StateSaverAndLoader serverState = getServerState(player.getWorld().getServer());
        Status playerState = serverState.players.computeIfAbsent(player.getUuid(), uuid -> new Status(entityData));

        return playerState;
    }
}
