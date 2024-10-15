package me.z7087.kb2expl.nms;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface INMS {
    boolean isHurtMarked(Entity entity);

    void setHurtMarked(Entity entity, boolean hurtMarked);

    void broadcast(Entity entity);

    void sendExplosionPacket(Player player, Vector vec);
}
