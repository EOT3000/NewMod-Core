package me.bergenfly.nations.impl.command.community;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.impl.model.TribeImpl;

public class TribeCommand extends CommunityCommand {
    public TribeCommand() {
        super("tribe");

        addBranch("create", new CommandFlower()
                .communityDoesNotExist(CommandFlower.INVOKER_MEMBER)
                .communityDoesNotExist(0)
                .cleanName(0)
                .player()
                .command((a) -> TribeImpl.tryCreate(a.args()[0], a.invokerUser()) != null)
                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.community", a.invoker().getName(), a.args()[0], "tribe"))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .make());
    }

    @Override
    public void loadSubcommands() {
        super.loadSubcommands();
    }
}
