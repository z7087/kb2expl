package me.z7087.kb2expl.nms;

public class NMSManager {
    private INMS nms;

    public void load(String serverClassName) {
        if (this.nms == null) {
            switch (serverClassName) {
                case "org.bukkit.craftbukkit.CraftServer" -> this.nms = new me.z7087.kb2expl.nms.V_1_21_R1.NMS_1_21_R1();
                default -> throw new IllegalStateException("Unknown server version");
            }
        }
    }

    public INMS getNMS() {
        INMS nms = this.nms;
        if (nms == null)
            throw new NullPointerException("NMS instance not found");
        return nms;
    }
}
