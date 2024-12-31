package me.bergenfly.nations.impl.command.company;

import me.bergenfly.nations.api.command.CommandFlower;
import me.bergenfly.nations.api.command.CommandRoot;
import me.bergenfly.nations.api.command.TranslatableString;
import me.bergenfly.nations.impl.model.CompanyImpl;
import me.bergenfly.nations.impl.model.NationImpl;

public class CompanyCommand extends CommandRoot {
    @Override
    public void loadSubcommands() {
        addBranch("create", new CommandFlower()
                .companyDoesNotExist(0)
                .cleanName(0)
                .nameLength(0, 3, 24)
                .player()
                .command((a) -> CompanyImpl.tryCreate(a.args()[0], a.invokerUser()) != null)
                .successBroadcast((a) -> TranslatableString.translate("nations.broadcast.created.company", a.invoker().getName(), a.args()[0]))
                .failureMessage((a) -> TranslatableString.translate("nations.general.failure"))
                .make());

        addBranch("info", new CommandFlower()
                .addCompany(0)
                .player()
                .commandAlwaysSuccess((a) -> a.communities()[0].sendInfo(a.invoker()))
                .make());
    }

    public CompanyCommand() {
        super("company");
    }
}
