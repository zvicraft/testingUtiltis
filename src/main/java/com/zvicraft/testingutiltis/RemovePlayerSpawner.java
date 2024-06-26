package com.zvicraft.testingutiltis;


import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class RemovePlayerSpawner implements CommandExecutor{
    private List<EntityPlayer> fakePlayers = new ArrayList<>();

    @Override
        public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
            if (label.equalsIgnoreCase("removefakeplayers")) {
                removeFakePlayers();
                sender.sendMessage("Removed all fake players.");
                return true;
            }
            return false;
        }
            public void removeFakePlayers() {
                for (EntityPlayer fakePlayer : fakePlayers) {
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        ((CraftPlayer) onlinePlayer).getHandle().connection.send(new PacketPlayOutEntityDestroy(fakePlayer.getId()));
                    }
                }
                fakePlayers.clear();
            }

}
