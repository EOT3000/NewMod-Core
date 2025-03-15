package me.bergenfly.nations.api.manager;

import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.ints.IntObjectPair;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.*;
import me.bergenfly.nations.api.registry.Registry;
import me.bergenfly.nations.impl.NationsPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class NationsPermissionManager {
    static {
        VALID = 0;
        INVALID_TYPE = 1;
        INVALID_NATION = 2;
        NONE_MATCH_SPECIFIC = 3;
        NONE_MATCH_GENERAL = 4;
        MULTIPLE_MATCH = 5;
        TOO_MANY_PARAMETERS = 6;

    }

    private Registry<Nation, String> NATIONS = NationsPlugin.getInstance().nationsRegistry();

    public static final int VALID;
    public static final int INVALID_TYPE;
    public static final int INVALID_NATION;
    public static final int NONE_MATCH_SPECIFIC;
    public static final int NONE_MATCH_GENERAL;
    public static final int MULTIPLE_MATCH;
    public static final int TOO_MANY_PARAMETERS;

    private final Map<String, Set<LandPermissionHolder>> holders = new HashMap<>();

    /**
     * Register a permission holder, for example when a holder is created, or when a holder's name is changed.
     *
     * @param holder the holder to register
     * @param oldName the old name of the holder to deregister
     */
    public void registerHolder(LandPermissionHolder holder, @Nullable String oldName) {
        if(oldName != null && holders.get(oldName) != null) {
            holders.get(oldName).remove(holder);
        }

        holders.putIfAbsent(holder.getName(), new HashSet<>());

        holders.get(holder.getName()).add(holder);

        if(holder instanceof Deletable d) {
            d.subscribeToDeletion((a) -> holders.get(a.getName()).remove(d));
        }
    }

    public IntObjectPair<LandPermissionHolder> get(String params) {
        String[] spl = params.split(":");

        switch (spl.length) {
            case 1: {
                return get(null, null, params);
            }
            case 2: {
                Nation nation = NATIONS.get(spl[0]);

                if(nation != null) {
                    return get(null, nation, spl[1]);
                } else {
                    return get(spl[0], null, spl[1]);
                }
            }
            case 3: {
                Nation nation = NATIONS.get(spl[1]);

                if(nation == null) {
                    return new IntObjectImmutablePair<>(INVALID_NATION, null);
                }

                return get(spl[0], nation, spl[2]);
            }
            default: {
                return new IntObjectImmutablePair<>(TOO_MANY_PARAMETERS, null);
            }
        }
    }

    public IntObjectPair<LandPermissionHolder> get(String type, Nation nation, String name) {
        Set<LandPermissionHolder> byName = holders.get(name);

        if(byName == null) {
            return new IntObjectImmutablePair<>(NONE_MATCH_GENERAL, null);
        }

        if(type != null) {
            boolean invalid = switch (type) {
                case "rank", "community", "tribe", "settlement", "nation", "company", "user" -> false;
                default -> true;
            };

            if(invalid) {
                return new IntObjectImmutablePair<>(INVALID_TYPE, null);
            }
        }

        List<LandPermissionHolder> valid = new ArrayList<>();

        for(LandPermissionHolder holder : byName) {
            boolean typeValid = true;
            boolean nationValid = true;

            if(type != null) {
                typeValid = switch (type) {
                    case "rank" -> holder instanceof Rank;
                    case "community" -> holder instanceof Community;
                    case "tribe" -> holder instanceof Tribe;
                    case "settlement" -> holder instanceof Settlement;
                    case "nation" -> holder instanceof Nation;
                    case "company" -> holder instanceof Company;
                    case "user" -> holder instanceof User;
                    default -> false; //Will never happen
                };
            }

            if(nation != null) {
                if(holder instanceof NationComponent c) {
                    nationValid = nation.equals(c.getNation());
                }
            }

            if(nationValid && typeValid) valid.add(holder);
        }

        return switch (valid.size()) {
            case 0 -> new IntObjectImmutablePair<>(NONE_MATCH_SPECIFIC, null);
            case 1 -> new IntObjectImmutablePair<>(VALID, valid.getFirst());
            default -> new IntObjectImmutablePair<>(MULTIPLE_MATCH, null);
        };
    }
}
