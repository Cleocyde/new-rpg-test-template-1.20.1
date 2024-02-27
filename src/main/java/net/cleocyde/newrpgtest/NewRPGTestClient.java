package net.cleocyde.newrpgtest;

import net.cleocyde.newrpgtest.menu.MenuSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import static net.cleocyde.newrpgtest.NewRPGTest.OPEN_GUI_PACKET;


public class NewRPGTestClient implements ClientModInitializer {
    @Override
    public void onInitializeClient(){


        ClientPlayNetworking.registerGlobalReceiver(OPEN_GUI_PACKET, (client, handler, buf, responseSender) -> {
            int guiId = buf.readInt(); // read the data sent from the server
            client.execute(() -> {
                // Open the GUI based on the id, for example:
                if (guiId == 1) {
                    client.setScreen(new MenuSystem());
                }
            });
        });

    }
}
