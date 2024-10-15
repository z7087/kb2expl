package me.z7087.kb2expl.nms;

public class NMSManager {
    private INMS nms;

    public void load(String serverClassName) {
        if (nms == null) {
            switch (serverClassName) {
                case "org.bukkit.craftbukkit.CraftServer" -> nms = new me.z7087.kb2expl.nms.V_1_21_R1.NMS_1_21_R1();
                default -> throw new IllegalStateException("Unknown server version");
            }
        }
    }

    public INMS getNMS() {
        return nms;
    }
}
