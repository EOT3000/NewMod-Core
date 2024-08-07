package me.bergenfly.newmod.flyfun.horn;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.GenericGameEvent;
import org.bukkit.inventory.meta.MusicInstrumentMeta;

import static org.bukkit.MusicInstrument.*;
import static org.bukkit.MusicInstrument.DREAM_GOAT_HORN;
import static org.bukkit.Sound.*;

public class HornListener implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEvent(GenericGameEvent event) {
        if(event.getEvent().equals(GameEvent.HIT_GROUND) ||
                event.getEvent().equals(GameEvent.STEP) ||
                event.getEvent().equals(GameEvent.FLAP) ||
                event.getEvent().equals(GameEvent.SWIM)) {
            return;
        }

        if(event.getEvent().equals(GameEvent.INSTRUMENT_PLAY)) {
            //System.out.println("insturment played");

            if(event.getEntity() instanceof Player) {
                playAt(event.getLocation(), getHorn((Player) event.getEntity()));

                event.setCancelled(true);
            }
        }
    }

    public static void playAt(Location location, Sound horn) {
        for(Player p : location.getNearbyPlayers(1800)) {
            /*if(location.distance(p.getLocation()) != 0) {
                Vector vector = location.clone().subtract(p.getLocation()).toVector().normalize();

                p.playSound(p.getLocation().add(vector), horn, volumeAtDistance((int) location.distance(p.getLocation())), 1);
            } else {
                p.playSound(p.getLocation(), horn, volumeAtDistance(1), 1);
            }*/

            p.playSound(location, horn, 112, 1);
        }
    }

    private static float volumeAtDistance(int d) {
        return (float) (5000.0/(d+200.0)-2.5)/15;
    }

    private static Sound getHorn(Player player) {
        MusicInstrument instrument;

        if(player.getInventory().getItemInMainHand().getType().equals(Material.GOAT_HORN)) {
            instrument = ((MusicInstrumentMeta) player.getInventory().getItemInMainHand().getItemMeta()).getInstrument();
        } else if(player.getInventory().getItemInOffHand().getType().equals(Material.GOAT_HORN)) {
            instrument = ((MusicInstrumentMeta) player.getInventory().getItemInOffHand().getItemMeta()).getInstrument();
        } else {
            instrument = null;
        }

        if(PONDER_GOAT_HORN.equals(instrument)) {
            return ITEM_GOAT_HORN_SOUND_0;
        } else if(SING_GOAT_HORN.equals(instrument)) {
            return ITEM_GOAT_HORN_SOUND_1;
        } else if(SEEK_GOAT_HORN.equals(instrument)) {
            return ITEM_GOAT_HORN_SOUND_2;
        } else if(FEEL_GOAT_HORN.equals(instrument)) {
            return ITEM_GOAT_HORN_SOUND_3;
        } else if(ADMIRE_GOAT_HORN.equals(instrument)) {
            return ITEM_GOAT_HORN_SOUND_4;
        } else if(CALL_GOAT_HORN.equals(instrument)) {
            return ITEM_GOAT_HORN_SOUND_5;
        } else if(YEARN_GOAT_HORN.equals(instrument)) {
            return ITEM_GOAT_HORN_SOUND_6;
        } else if(DREAM_GOAT_HORN.equals(instrument)) {
            return ITEM_GOAT_HORN_SOUND_7;
        }

        //In case something goes wrong
        return BLOCK_NOTE_BLOCK_BANJO;
    }
}
