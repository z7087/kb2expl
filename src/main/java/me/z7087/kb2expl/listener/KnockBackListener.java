package me.z7087.kb2expl.listener;

import me.z7087.kb2expl.nms.INMS;
import me.z7087.kb2expl.nms.NMSManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityKnockbackEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KnockBackListener implements Listener {
    private static final Set<EntityKnockbackEvent.KnockbackCause> KNOCKBACK_CAUSES;
    static {
        KNOCKBACK_CAUSES = Set.of(
                EntityKnockbackEvent.KnockbackCause.ENTITY_ATTACK,
                EntityKnockbackEvent.KnockbackCause.SWEEP_ATTACK
        );
    }

    private final Map<Player, Vector> PlayerGotKBMap = new HashMap<>();
    private final NMSManager nmsManager;

    public KnockBackListener(NMSManager nmsManager) {
        this.nmsManager = nmsManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityKnockBack(EntityKnockbackEvent event) {
        if (!state)
            return;
        if (event.isAsynchronous())
            return;
        if (event.getEntity() instanceof Player player) {
            // when a player on the ground is knocked back, it's vertical motion increases
            // and makes unfair advantages if we change this knockback to an explosion
            if (KNOCKBACK_CAUSES.contains(event.getCause()) && !isPlayerOnGround(player)) {
                PlayerGotKBMap.put(player, player.getVelocity());
            } else {
                PlayerGotKBMap.remove(player);
            }
        }
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        if (!state)
            return;
        if (event.isAsynchronous())
            return;
        final Player player = event.getPlayer();
        final Vector playerVelocityOld = PlayerGotKBMap.remove(player);
        if (playerVelocityOld != null && !event.isCancelled()) {
            //System.out.println("originkb: " + event.getVelocity() + ", originmotion: " + playerVelocityOld + ", to: " + event.getVelocity().clone().subtract(playerVelocityOld));
            INMS nms = nmsManager.getNMS();
            if (!player.getVelocity().equals(event.getVelocity())) {
                player.setVelocity(event.getVelocity());
            }
            event.setCancelled(true);
            nms.setHurtMarked(player, false); // do this after setVelocity
            nms.broadcast(player);
            nms.sendExplosionPacket(player, event.getVelocity().clone().subtract(playerVelocityOld));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!state)
            return;
        assert !event.isAsynchronous();
        PlayerGotKBMap.remove(event.getPlayer());
    }

    @SuppressWarnings("deprecation")
    private static boolean isPlayerOnGround(Player player) {
        // we have to check its onGround state even it's controlled only by the client
        return player.isOnGround();
    }

    private boolean state;
    public void setState(boolean state) {
        this.state = state;
        PlayerGotKBMap.clear();
    }
}
