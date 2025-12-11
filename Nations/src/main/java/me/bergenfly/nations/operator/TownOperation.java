package me.bergenfly.nations.operator;

import me.bergenfly.nations.model.User;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Function;

public class TownOperation {
    public List<User> approvers;
    public List<User> disapprovers;

    private final boolean needTrusted;
    private final int numberApprovers;
    private final int netApprovers;

    private final Runnable action;
    private final User promulgator;

    //TODO instead approval *policy* also is promulgator the right word?
    public TownOperation(Function<Player, String> proposalMessage, Runnable action, User promulgator, boolean needTrusted, int numberApprovers, int netApprovers) {
        this.action = action;
        this.promulgator = promulgator;

        this.needTrusted = needTrusted;
        this.numberApprovers = numberApprovers;
        this.netApprovers = netApprovers;
    }

    public void addApprover(User user) {
        if(promulgator == user) {
            return;
        }

        approvers.add(user);

        if(approvers.size() > numberApprovers && (approvers.size()-disapprovers.size() > netApprovers)) {
            action.run();
        }
    }
}
