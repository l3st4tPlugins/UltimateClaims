package com.songoda.ultimateclaims.listeners;

import com.songoda.ultimateclaims.UltimateClaims;
import com.songoda.ultimateclaims.claim.Claim;
import com.songoda.ultimateclaims.claim.ClaimManager;
import com.songoda.ultimateclaims.claim.PowerCell;
import com.songoda.ultimateclaims.member.ClaimMember;
import com.songoda.ultimateclaims.member.ClaimRole;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LoginListeners implements Listener {

    private final UltimateClaims plugin;

    public LoginListeners(UltimateClaims plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerLoginEvent event) {
        ClaimManager claimManager = plugin.getClaimManager();

        Chunk chunk = event.getPlayer().getLocation().getChunk();

        if (!claimManager.hasClaim(chunk)) return;

        Claim claim = claimManager.getClaim(chunk);

        ClaimMember member = claim.getMember(event.getPlayer());
        if (member == null) {
            claim.addMember(event.getPlayer(), ClaimRole.VISITOR);
            member = claim.getMember(event.getPlayer());
        }

        member.setPresent(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerQuitEvent event) {
        ClaimManager claimManager = plugin.getClaimManager();

        Chunk chunk = event.getPlayer().getLocation().getChunk();

        if (!claimManager.hasClaim(chunk)) return;

        Claim claim = claimManager.getClaim(chunk);

        ClaimMember member = claim.getMember(event.getPlayer());
        if (member != null) {
            if (member.getRole() == ClaimRole.VISITOR)
                claim.removeMember(member);
            else
                member.setPresent(false);
        }
    }
}
