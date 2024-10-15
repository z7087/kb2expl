package me.z7087.kb2expl;

import me.z7087.kb2expl.listener.KnockBackListener;
import me.z7087.kb2expl.nms.NMSManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Kb2expl extends JavaPlugin {

    public final NMSManager nmsManager;
    public final KnockBackListener knockBackListener;

    public Kb2expl() {
        NMSManager nmsManager = new NMSManager();
        this.nmsManager = nmsManager;
        this.knockBackListener = new KnockBackListener(nmsManager);
    }

    @Override
    public void onLoad() {
        nmsManager.load(this.getServer().getClass().getName());
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(knockBackListener, this);
        // Plugin startup logic
        knockBackListener.setState(true);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        knockBackListener.setState(false);
    }
}
