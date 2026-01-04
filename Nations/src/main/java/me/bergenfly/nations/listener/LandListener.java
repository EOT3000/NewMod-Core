/*package me.bergenfly.nations.listener;

public class LandListener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        checkBuild(event.getBlock(), event, event.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        checkBuild(event.getBlock(), event, event.getPlayer());
    }

    public void getPermissionType(PlayerInteractEvent event) {
        Block cb = event.getClickedBlock();

        //Block clicks
        if(cb != null) {
            Material type = cb.getType();

            PlotSection section = landManager.getPlotSectionAtLocation(cb.getLocation());

            if (section instanceof PermissiblePlotSection permissiblePlotSection) {
                User user = api.usersRegistry().get(event.getPlayer().getUniqueId());

                if (Tag.FENCE_GATES.isTagged(cb.getType())
                        || Tag.DOORS.isTagged(cb.getType())
                        || Tag.TRAPDOORS.isTagged(cb.getType())) {
                    return DefaultPlotPermission.DOOR;
                }

                if (Tag.BUTTONS.isTagged(cb.getType())
                        || cb.getType().equals(Material.LEVER)) {
                    return DefaultPlotPermission.LEVER;
                }

                if(type == Material.CHEST
                        || type == Material.TRAPPED_CHEST
                        || type == Material.BARREL
                        || type == Material.HOPPER
                        || type == Material.DISPENSER
                        || type == Material.DROPPER
                        || type == Material.BREWING_STAND
                        || type == Material.SMOKER
                        || type == Material.FURNACE
                        || type == Material.BLAST_FURNACE
                        || type == Material.CRAFTER
                        || Tag.SHULKER_BOXES.isTagged(type)) {
                    return DefaultPlotPermission.CONTAINER;
                }

                if(Tag.ALL_SIGNS.isTagged(type)) {
                    return DefaultPlotPermission.SIGN;
                }
            }
        }
    }

    private boolean canAccess(User user, PlotPermission permission, Lot lot) {
        LandOwner owner = lot.getOwner();

        if(owner.canAccess(user)) {
            return true;
        }

        if(lot.isTrusted(user)) {
            return true;
        }

        Level level = RESIDENT;

        if(lot.isAllowed()) {
            return true;
        }
    }

    private Plot getPlot(Location location) {

    }
}
*/