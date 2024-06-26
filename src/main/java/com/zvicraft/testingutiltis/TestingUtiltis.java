package com.zvicraft.testingutiltis;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.PacketPlayOutEntityDestroy;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.PlayerInteractManager;
import net.minecraft.server.level.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class TestingUtiltis extends JavaPlugin{
    private List<EntityPlayer> fakePlayers = new ArrayList<>();


    private double lastX;
    private double locX;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("spawnplayers").setExecutor(new SpawnPlayersCommand());
        getCommand("removefakeplayers").setExecutor(new RemovePlayerSpawner());



    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        removeFakePlayers();
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
