package net.cleocyde.newrpgtest;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.netty.buffer.Unpooled;
import net.cleocyde.newrpgtest.utils.ItemUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


public class NewRPGTest implements ModInitializer {

	public static final String MOD_ID = "newrpgtest";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static Map<ServerPlayerEntity, EntityData> entityDatas;

	public static final Identifier OPEN_GUI_PACKET = new Identifier(MOD_ID, "open_gui");

	public static Map<ServerPlayerEntity, EntityData> getEntityDatas(){return entityDatas;}
	@Override
	public void onInitialize() {
		LOGGER.info("Fuck minecraft modding");

		entityDatas = new HashMap<>();


		ItemUtils.registerModItems();


		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			entityDatas.get(player);
			if(entityDatas.get(player) == null) {
				entityDatas.put(player, new EntityData(server, player));
				EntityData entityData = entityDatas.get(player);
				player.experienceLevel = entityData.status.level;
			}
		});

		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			newPlayer.experienceLevel = oldPlayer.experienceLevel;
			newPlayer.experienceProgress = oldPlayer.experienceProgress;
			newPlayer.totalExperience = oldPlayer.totalExperience;
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
		//Give Vitality
		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("rpg")
					.then(CommandManager.literal("givevita")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.then(CommandManager.argument("vitality", IntegerArgumentType.integer())
											.executes((this::executeAddVitality))))));
		}));
		//Give Agility
		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("rpg")
					.then(CommandManager.literal("giveagi")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.then(CommandManager.argument("agility", IntegerArgumentType.integer())
											.executes((this::executeAddAgility))))));
		}));
		//Give Luck
		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("rpg")
					.then(CommandManager.literal("giveluck")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.then(CommandManager.argument("luck", IntegerArgumentType.integer())
											.executes((this::executeAddLuck))))));
		}));
		//Give Strength
		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("rpg")
					.then(CommandManager.literal("givestr")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.then(CommandManager.argument("strength", IntegerArgumentType.integer())
											.executes((this::executeAddStrength))))));
		}));

		//Give Intelligence
		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("rpg")
					.then(CommandManager.literal("giveint")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.then(CommandManager.argument("intelligence", IntegerArgumentType.integer())
											.executes((this::executeAddIntelligence))))));
		}));

		CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("rpg")
					.then(CommandManager.literal("sethealth")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.then(CommandManager.argument("health", IntegerArgumentType.integer())
											.executes((this::executeSetHealth))))));
		}));

		/*CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated, environment) -> {
			dispatcher.register(CommandManager.literal("rpg")
					.then(CommandManager.literal("getattributes")
							.then(CommandManager.argument("player", EntityArgumentType.player()),
											client.setScreen)));
		}));*/


		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {

			LiteralArgumentBuilder<ServerCommandSource> command = CommandManager.literal("rpg")
					.then(CommandManager.literal("getattributes")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.executes(this::openGui)
									.executes(this::executeGetAttributes)));

			dispatcher.register(command);
		});






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

	private int executeAddVitality(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		int vitality = IntegerArgumentType.getInteger(context, "vitality");

		EntityData entityData = entityDatas.get(player);
		if(entityData != null){
			entityData.status.Vitality.Add(vitality);

		}
		return 1;
	}
	private int executeAddAgility(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		int agility = IntegerArgumentType.getInteger(context, "agility");

		EntityData entityData = entityDatas.get(player);
		if(entityData != null){
			entityData.status.Agility.Add(agility);

		}
		return 1;
	}
	private int executeAddStrength(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		int strength = IntegerArgumentType.getInteger(context, "strength");

		EntityData entityData = entityDatas.get(player);
		if(entityData != null){
			entityData.status.Strength.Add(strength);

		}
		return 1;
	}
	private int executeAddLuck(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		int luck = IntegerArgumentType.getInteger(context, "luck");

		EntityData entityData = entityDatas.get(player);
		if(entityData != null){
			entityData.status.Luck.Add(luck);

		}
		return 1;
	}
	private int executeAddIntelligence(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
		int intelligence = IntegerArgumentType.getInteger(context, "intelligence");

		EntityData entityData = entityDatas.get(player);
		if(entityData != null){
			entityData.status.Intelligence.Add(intelligence);

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

	private int executeGetAttributes(CommandContext<ServerCommandSource> context) throws CommandSyntaxException{
		ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");


		EntityData entityData = entityDatas.get(player);
		if(entityData !=null){
			entityData.status.Luck.GetValue();
			player.sendMessage(Text.literal("you have " + (int)entityData.status.Vitality.GetValue() + " Vitality."));
			player.sendMessage(Text.literal("you have " + (int)entityData.status.Agility.GetValue() + " Agility."));
			player.sendMessage(Text.literal("you have " + (int)entityData.status.Strength.GetValue() + " Strength."));
			player.sendMessage(Text.literal("you have " + (int)entityData.status.Intelligence.GetValue() + " Intelligence."));
			player.sendMessage(Text.literal("you have " + (int)entityData.status.Luck.GetValue() + " Luck."));
		}
		return 1;
	}

	private int openGui(CommandContext<ServerCommandSource> context) {
		ServerPlayerEntity player = context.getSource().getPlayer();
		PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
		// Here you can write any data you want to send to the client, for example:
		passedData.writeInt(1); // let's say this is the id of the GUI to open
		ServerPlayNetworking.send(player, OPEN_GUI_PACKET, passedData);
		return 1;
	}


}