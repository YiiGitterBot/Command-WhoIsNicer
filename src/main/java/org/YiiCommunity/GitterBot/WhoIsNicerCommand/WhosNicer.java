package org.YiiCommunity.GitterBot.WhoIsNicerCommand;

import com.amatkivskiy.gitter.sdk.model.response.message.MessageResponse;
import com.amatkivskiy.gitter.sdk.model.response.room.RoomResponse;
import com.avaje.ebean.Ebean;
import org.YiiCommunity.GitterBot.GitterBot;
import org.YiiCommunity.GitterBot.api.Command;
import org.YiiCommunity.GitterBot.containers.Gitter;
import org.YiiCommunity.GitterBot.models.database.User;

import java.util.ArrayList;
import java.util.List;

public class WhosNicer extends Command {

    private List<String> commands = new ArrayList<>();

    public WhosNicer() {
        commands = getConfig().getStringList("commands");
    }

    @Override
    public void onMessage(RoomResponse room, MessageResponse message) {
        for (String item : commands) {
            if (message.text.trim().equalsIgnoreCase("@" + GitterBot.getInstance().getConfiguration().getBotUsername() + " " + item)) {
                User obj = Ebean.find(User.class).where().ne("username", GitterBot.getInstance().getConfiguration().getBotUsername()).order().desc("carma").setMaxRows(1).findUnique();
                try {
                    Gitter.sendMessage(room,
                            getConfig()
                                    .getString("response", "Today's the best is @{username} with **{carma}** carma!")
                                    .replace("{username}", obj.getUsername())
                                    .replace("{carma}", (obj.getCarma() >= 0 ? "+" : "-") + obj.getCarma())
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
        }
    }
}
