package me.googol.fisch.otherglide.util;

import net.minecraft.util.math.Vec3d;

public interface PlayerDataSaver {
    boolean startGlideMode();
    boolean finishGlideMode();

    boolean startPlayerStuck();
    boolean stopPlayerStuck();

    void storePlayerGoto(Vec3d pos);
    boolean playerGotoStore();

    void playerBoostMax(double vel);
    void playerThermalHeight(double height);
}
