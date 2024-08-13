package me.bergenfly.nations.api;

import me.bergenfly.nations.api.model.User;
import me.bergenfly.nations.api.model.organization.Nation;
import me.bergenfly.nations.api.model.organization.Settlement;
import me.bergenfly.nations.api.registry.Registry;

import java.util.UUID;

public interface NationsAPI {
    Registry<Nation, String> nationsRegistry();
    Registry<Settlement, String> settlementsRegistry();
    Registry<User, UUID> usersRegistry();
}