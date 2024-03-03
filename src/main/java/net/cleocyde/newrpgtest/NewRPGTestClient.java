package net.cleocyde.newrpgtest;

import net.cleocyde.newrpgtest.menu.MenuSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.text.Text;

import static net.cleocyde.newrpgtest.NewRPGTest.OPEN_GUI_PACKET;
import static net.cleocyde.newrpgtest.NewRPGTest.VITALITY;


public class NewRPGTestClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {


        ClientPlayNetworking.registerGlobalReceiver(OPEN_GUI_PACKET, (client, handler, buf, responseSender) -> {
            int guiId = buf.readInt(); // read the data sent from the server
            client.execute(() -> {
                // Open the GUI based on the id, for example:
                if (guiId == 1) {
                    client.setScreen(new MenuSystem());
                }
            });
        });

        ClientPlayNetworking.registerGlobalReceiver(NewRPGTest.VITALITY, (client, handler, buf, responseSender) -> {
            int totalDirtBlocksBroken = buf.readInt();
            int playerSpecificDirtBlocksBroken = buf.readInt();

            client.execute(() -> {
                client.player.sendMessage(Text.literal("Total Vitality: " + VITALITY));
                client.player.sendMessage(Text.literal("Player specific Vitality: " + VITALITY));
            });
        });

    }
}
