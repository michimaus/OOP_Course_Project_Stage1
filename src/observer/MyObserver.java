package observer;

import angels.StandardAngel;
import players.StandardPlayer;

/**
 * Signatures for the methods that are implemented by the observer.
 * Each type of event is defined by a specific method.
 */

public interface MyObserver {
    void updatePlayerKillingOther(StandardPlayer killer, StandardPlayer player);
    void updatePlayerLevel(StandardPlayer player, int level);
    void updateAngelKillingPlayer(StandardPlayer player);
    void updatePlayerRespawned(StandardPlayer player);
    void updateAngelSpawn(StandardAngel angel);
    void updatePlayerInteraction(StandardAngel angel, StandardPlayer player);
}
