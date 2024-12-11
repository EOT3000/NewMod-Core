package me.bergenfly.nations.api.model.organization;

import me.bergenfly.nations.api.permission.NationPermission;

public interface Rank extends Organization, NationComponent, Deletable {
    void setPermission(NationPermission permission);

    void unsetPermission(NationPermission permission);

    boolean hasPermission(NationPermission permission);
}
