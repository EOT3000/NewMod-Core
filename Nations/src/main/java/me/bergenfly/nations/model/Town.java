package me.bergenfly.nations.model;

import it.unimi.dsi.fastutil.objects.ObjectIntImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import me.bergenfly.nations.NationsPlugin;
import me.bergenfly.nations.manager.NationsLandManager;
import me.bergenfly.nations.model.check.Check;
import me.bergenfly.nations.model.plot.ClaimedChunk;
import me.bergenfly.nations.model.plot.Lot;
import me.bergenfly.nations.operator.TownOperation;
import me.bergenfly.nations.registry.Registry;
import me.bergenfly.nations.serializer.IdList;
import me.bergenfly.nations.serializer.Serializable;
import me.bergenfly.nations.serializer.type.TownDeserialized;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public class Town implements LandAdministrator, Serializable {
    private static Registry<Town, String> COMMUNITIES;
    private static Registry<User, UUID> USERS;
    private static NationsLandManager LAND;

    private final Set<User> residents = new HashSet<>();
    private final Set<ClaimedChunk> land = new HashSet<>();
    private final Set<Lot> lots = new HashSet<>();

    private final Set<TownOperation> openProposals = new HashSet<>();

    private final Set<BoardMessage> boardMessages = new HashSet<>();

    private final Set<User> outlaws = new HashSet<>();

    private User leader;
    private String name;

    private Nation nation;

    private ClaimedChunk homePlot;

    private final long creationTime;
    private final String initialName;
    private final String founder;

    private TownDeserialized deserializedTown;

    public String getId() {
        return "$T_"+initialName+"_"+creationTime;
    }

    public Town(TownDeserialized deserializedTown) {
        this(deserializedTown.name(), deserializedTown.getLeader(), deserializedTown.creationTime(), deserializedTown.initialName(), deserializedTown.founder());

        this.deserializedTown = deserializedTown;
    }

    public Town(String name, User leader) {
        this(name, leader, System.currentTimeMillis(), name, leader.getId()); //TODO: too long name causes file creation error?
    }

    public Town(String name, User leader, long creationTime, String initialName, String founder) {
        this.name = name;
        this.leader = leader;

        this.creationTime = creationTime;
        this.initialName = initialName;
        this.founder = founder;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getLeader() {
        return leader;
    }

    public boolean setLeader(User newLeader, boolean silent) {
        if(isResident(newLeader)) {
            this.leader = newLeader;

            return true;
        }

        return false;
    }

    public ClaimedChunk getHomePlot() {
        return homePlot;
    }

    public void setHomePlot(ClaimedChunk homePlot) {
        this.homePlot = homePlot;
    }

    public boolean isResident(User user) {
        return residents.contains(user);
    }

    public Set<User> getResidents() {
        return new HashSet<>(residents);
    }

    public void sendInfo(CommandSender sender) {

    }

    public boolean addOutlaw(User toAdd) {
        if(residents.contains(toAdd)) {
            return false;
        }

        outlaws.add(toAdd);
        return true;
    }

    public boolean isOutlaw(User toCheck) {
        return outlaws.contains(toCheck);
    }

    public boolean addResident(User resident, boolean silent) {
        if(Check.checkResidentCanJoinTown(resident, this)) {
            if(resident.hasCommunity()) {
                if (resident.getCommunity() != this) {
                    Town oldCommunity = resident.getCommunity();

                    if (!oldCommunity.removeResident(resident, silent)) {
                        throw new RuntimeException("Unexpected error trying to add resident {1} to town {2}. Unable to kick from old town {3}".replace("{1}", resident.getOfflinePlayer().getName()).replace("{2}", this.getName()).replace("{3}", oldCommunity.getName()));
                    }
                }
            }

            residents.add(resident);

            if(resident.getCommunity() != this) {
                if (!resident.setCommunity(this, silent) || resident.getCommunity() != this) {
                    residents.remove(resident);

                    throw new RuntimeException("Unexpected error trying to add resident {1} to town {2}. Unable to set user's town. User's town is currently {3}".replace("{1}", resident.getOfflinePlayer().getName()).replace("{2}", this.getName()).replace("{3}", String.valueOf(resident.getCommunity())));
                }
            }

            return true;
        }

        return false;
    }

    public boolean canRemoveResident(User resident) {
        return leader != resident;
    }

    public boolean removeResident(User resident, boolean silent) {
        if(canRemoveResident(resident)) {
            residents.remove(resident);

            if(resident.hasCommunity()) {
                resident.setCommunity(null, silent);
            }

            return true;
        }

        return false;
    }

    public boolean removeFromNation() {
        if(!Check.checkTownCanLeaveNation(this)) {
            return false;
        }

        if(nation == null) {
            return true;
        }

        this.nation = null;

        if(nation.getTowns().contains(this)) {
            nation.removeTown(this);
        }

        return true;
    }

    public boolean setNation(@NotNull Nation nation) {
        if(nation == null) {
            return false;
        }

        this.nation = nation;

        if(!nation.getTowns().contains(this)) {
            nation.addTown(this);
        }

        return true;
    }

    public @Nullable Nation getNation() {
        return nation;
    }

    @Override
    public Set<ClaimedChunk> getLand() {
        return new HashSet<>(land);
    }

    @Override
    public void addLandToList(ClaimedChunk claimedChunk) {
        land.add(claimedChunk);
    }

    @Override
    public void removeLandFromList(ClaimedChunk claimedChunk) {
        land.remove(claimedChunk);
    }

    public int getMaxChunks() {
        return residents.size()*20;
    }

    public boolean isSettlement() {
        return true;
    }



    // Messages

    public void broadcast(String string) {
        for(User user : residents) {
            if(user.getOfflinePlayer().isOnline()) {
                user.getPlayer().sendMessage(string);
            }
        }
    }

    public void broadcast(Function<Player, String> stringGenerator) {
        for(User user : residents) {
            if(user.getOfflinePlayer().isOnline()) {
                user.getPlayer().sendMessage(stringGenerator.apply(user.getPlayer()));
            }
        }
    }

    //TODO make this work. Can't use a functional interface as those reset after server restarts
    public void addToBoard(String string) {

    }



    public static ObjectIntPair<Town> tryCreate(String name, User leader, Player player, boolean silent) {
        if(COMMUNITIES == null) {
            COMMUNITIES = NationsPlugin.getInstance().communitiesRegistry();
        }

        if(COMMUNITIES.get(name) != null) {
            return new ObjectIntImmutablePair<>(null, -1);
        }

        if(leader.getCommunity() != null) {
            return new ObjectIntImmutablePair<>(null, -2);
        }

        if(LAND.getClaimedChunkAtLocation(leader.getPlayer().getLocation()) != null) {
            return new ObjectIntImmutablePair<>(null, -3);
        }

        Town s = new Town(name, leader);

        ClaimedChunk homePlot = LAND.tryClaimFullChunkOtherwiseFail(player.getChunk(), s);

        COMMUNITIES.set(name, s);

        leader.setCommunity(s, silent);
        //NationsPlugin.getInstance().permissionManager().registerHolder(s, null);

        return new ObjectIntImmutablePair<>(s, 1);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> ret = new HashMap<>();

        ret.put("name", name);
        ret.put("leader", leader);

        ret.put("residents", new IdList(residents));
        ret.put("outlaws", new IdList(outlaws));

        ret.put("initialName", initialName);
        ret.put("creationTime", creationTime);
        ret.put("founder", founder);

        ret.put("lots", lots);

        return ret;
    }
}
