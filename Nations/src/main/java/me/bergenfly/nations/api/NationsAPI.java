package me.bergenfly.nations.api;

import me.bergenfly.nations.api.manager.NationsLandManager;
import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Community;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.model.plot.ClaimedChunk;
import me.bergenfly.nations.api.registry.Registry;

import java.util.Map;
import java.util.UUID;

public interface NationsAPI {
    Registry<Nation, String> nationsRegistry();
    Registry<Community, String> communitiesRegistry();
    Registry<User, UUID> usersRegistry();
    Registry<Map<Class<?>, LandPermissionHolder>, String> permissionHoldersByNameRegistry();
    Registry<LandPermissionHolder, String> permissionHoldersByIdRegistry();
    //Registry<ClaimedChunk, Long> plotsRegistry(); //TODO: primitive instead of boxed?

    NationsLandManager landManager();
}
