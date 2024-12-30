package me.bergenfly.nations.api.manager;

import me.bergenfly.nations.api.model.organization.LandAdministrator;
import me.bergenfly.nations.api.model.organization.LandPermissionHolder;
import me.bergenfly.nations.api.model.organization.Nation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NationsPermissionManager {

    private final Map<String, Set<LandPermissionHolder>> holders = new HashMap<>();

    public void register() {}

    public LandPermissionHolder get(String type, LandAdministrator authority, String name) {
        return null;
    }
}
