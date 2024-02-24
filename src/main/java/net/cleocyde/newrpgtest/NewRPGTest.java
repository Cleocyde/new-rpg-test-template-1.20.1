package net.cleocyde.newrpgtest;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.Unpooled;
import net.cleocyde.newrpgtest.menu.MenuSystem;
import net.cleocyde.newrpgtest.stats.Resource;
import net.cleocyde.newrpgtest.stats.Status;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.core.jmx.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class NewRPGTest implements ModInitializer {
	public static final String MOD_ID = "rpgmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static Map<ServerPlayerEntity, EntityData> entityDatas;

	public static Map<ServerPlayerEntity, EntityData> getEntityDatas(){return entityDatas;}
	@Override
	public void onInitialize() {
		LOGGER.info("Fuck minecraft modding");

		entityDatas = new HashMap<>();

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			entityDatas.put(player, new EntityData(server, player));
		});


		ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
			EntityData entityData = getEntityDatas().get(newPlayer);
			if (entityData != null) {
				entityData.status.HP.Add(entityData.status.MaximumHealth());
			}
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				EntityData entityData = entityDatas.get(player);
				entityData.status.updateActionBar(player);
				entityData.status.RefreshHealthBar(player);

			}
		});

		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("rpg")
					.then(CommandManager.literal("givexp")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.then(CommandManager.argument("xp", IntegerArgumentType.integer())
											.executes((this::executeSetLevel))))));
		}));

		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("rpg")
					.then(CommandManager.literal("sethealth")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.then(CommandManager.argument("health", IntegerArgumentType.integer())
											.executes((this::executeSetHealth))))));
		}));



	}
	private int executeSetLevel(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		int xp = IntegerArgumentType.getInteger(context, "xp");

		EntityData entityData = entityDatas.get(player);
		if(entityData != null){
			entityData.status.EXP.Add(xp);
		}
		return 1;
	}

	private int executeSetHealth(CommandContext<ServerCommandSource> context) throws CommandSyntaxException{
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		int health = IntegerArgumentType.getInteger(context, "health");

		EntityData entityData = entityDatas.get(player);
		if(entityData !=null){
			entityData.status.HP.Set(health);
		}
		return 1;
	}

}