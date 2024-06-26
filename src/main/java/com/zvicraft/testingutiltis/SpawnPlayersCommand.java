package com.zvicraft.testingutiltis;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.game.PacketPlayOutNamedEntitySpawn;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import org.apache.commons.codec.binary.Base64;

public class SpawnPlayersCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only for players.");
            return false;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("Usage: /spawnplayers <amount>");
            return false;
        }

        try {
            int playerAmount = Integer.parseInt(args[0]);
            for (int i = 0; i < playerAmount; i++) {
                spawnFakePlayer(player, Generator.generateRandomName(4).toString());
            }
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid number format.");
            return false;
        }

        return true;
    }

    public void spawnFakePlayer(Player player, String displayName) {
        MinecraftServer mcserver = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldserver = ((CraftWorld) Bukkit.getWorld(player.getWorld().getName())).getHandle();
        ProfilePublicKey key = new ProfilePublicKey(null);
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), displayName);
        EntityPlayer npc = new EntityPlayer(mcserver, worldserver, gameProfile, key);
        EntityPlayer ep = ((CraftPlayer)player).getHandle();


        GameProfile gp = ep.getGameProfile();
        PropertyMap pm = gp.getProperties();

        Collection<Property> properties = pm.get("ewogICJ0aW1lc3RhbXAiIDogMTU5NDAzMDE0NDI2MiwKICAicHJvZmlsZUlkIiA6ICI4YTZmNTlmYjY1Mjg0NTE1OTliZjAwNjY4YzZhYjI4YSIsCiAgInByb2ZpbGVOYW1lIiA6ICJTdGVhdmUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Q4ZTczZTM3MGVlMmEzNzY3NDg0ZmNmNTY2OThmZWU2YmM3ODViZjcxZTVkZmQ2YWJhNjdiZjU5Mjk0YjQxOCIKICAgIH0KICB9Cn0=");
        Property property = pm.get("U7rqASWskO//4VGkcsTknzsFpHvQmQggul4JB7fjLwmCidKRelq1OD7eGCE1HOXcDqO7FLEWGuAA4O+zJD1x+34L/83ih4rN5Zy11b5j+yrlONTec6HdCbr03wyDtnnTGSiM+mpYLyksNv+XQBiB1E6kbzQx7nVcG9sq8hG9B1Kbr7Cjs7HMpg9UhKagvyz85JF5/gM0nA6FSFRmbEbX/Y7d9tn4+YCwUqLh13utxrCBXAVF1pnJe3pSgeYhQOqzANhdJDq0spHnHqqDIHurekkuTmAnBDficGL5NaCeh9NENyfYekatyETNH4Qo+dix9E7jPAGLB2hswQkXFnGgA4GgheZqh4oF6remMFayen+jfpiEvlf1f3E73FHImhlFA026yT1et60MVw01aW+Uv5wJA5fIhRDvTesj+TbAZ+PTSaijX85ClNYzl6iGuh5S0VIAPRm3UuUyoiL2SX7RjrjKHLl9EY6j7Wn4CcHMR4ZhcLEkTCei1kYX0droWb2vZvhX5Ab2VEl1vsnRpZxzOMb1DgIZJFreQ5xLu1w5TW33yHf1hn3pxlCU1LLVB+6mD+fD/I+m42NX2BQNfD+0gexlbQLLFsYucGVyWMjGqTO4i8FP61ftDu8ogj1otGxsP7rRlb7/vcbS1EZFnflhSciZsn+6MezCXm6t7VeXVKI=").iterator().next();

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
        String base64 = property.getValue();
        String decoded = new String(Base64.decodeBase64(base64));
       GameProfile gameProfile = new GameProfile(UUID.randomUUID(), displayName);
        ProfilePublicKey profilePublicKey = new ProfilePublicKey(null); // Use the player's UUID
       EntityPlayer npc = new EntityPlayer(server, world, gameProfile, profilePublicKey);

        Location loc = player.getLocation();

        npc.setPosRaw(loc.getX(), loc.getY(), loc.getZ());

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                ((CraftPlayer) onlinePlayer).getHandle().connection.send(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
                ((CraftPlayer) onlinePlayer).getHandle().connection.send(new PacketPlayOutNamedEntitySpawn(npc));
            }
        }
    }
}
