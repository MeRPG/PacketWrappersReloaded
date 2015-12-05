package com.parapvp.packetWrappersReloaded;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @Author Jaxon A Brown
 */
public abstract class AbstractPacket {
	protected PacketContainer handle;

	protected AbstractPacket(PacketContainer handle, PacketType type) {
		if(handle == null) {
			throw new IllegalArgumentException("Handle cannot be null.");
		} else if(handle.getType() != type) {
			throw new IllegalArgumentException("You've used a wrapper not valid for that packet.");
		}

		this.handle = handle;
	}

	/**
	 * Get the raw packet.
	 * @return the PacketContainer for this packet.
	 */
	public PacketContainer getHandle() {
		return handle;
	}

	/**
	 * Sends the packet to a player. This WILL trigger packet events.
	 * @param player Player to send the packet to.
	 * @return The exception thrown when trying to send the packet. Null if none was thrown.
	 */
	public RuntimeException sendPacket(Player player) {
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(player, getHandle());
			return null;
		} catch(InvocationTargetException ex) {
			return new RuntimeException("Failed to send packet to player.", ex);
		}
	}

	/**
	 * Sends the packet to players. This WILL trigger packet events.
	 * @param players List of players to send the packet to.
	 * @return The exceptions thrown when trying to send the packet. Null at indices of players when no exception was thrown.
	 */
	public Exception[] sendPacket(List<Player> players) {
		Exception[] results = new Exception[players.size()];
		for(int i = 0; i < players.size(); i++) {
			results[i] = sendPacket(players.get(i));
		}
		return results;
	}

	/**
	 * Silently sends the packet to a player. This will NOT trigger packet events.
	 * @param player Player to send the packet to.
	 * @return The exception thrown when trying to send the packet. Null if none was thrown.
	 */
	public Exception sendPacketSilent(Player player) {
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(player, getHandle(), false);
			return null;
		} catch(InvocationTargetException ex) {
			return new RuntimeException("Failed to send packet to player.", ex);
		}
	}

	/**
	 * Silently sends the packet to players. This will NOT trigger packet events.
	 * @param players List of players to send the packet to.
	 * @return The exceptions thrown when trying to send the packet. Null at indices of players when no exception was thrown.
	 */
	public Exception[] sendPacketSilent(List<Player> players) {
		Exception[] results = new Exception[players.size()];
		for(int i = 0; i < players.size(); i++) {
			results[i] = sendPacketSilent(players.get(i));
		}
		return results;
	}

	/**
	 * Sends the packet to a player. This WILL trigger packet events.
	 * @param sender Player who 'sent' the packet.
	 * @return The exception thrown when trying to receive the packet. Null if none was thrown.
	 */
	public Exception receivePacket(Player sender) {
		try {
			ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, getHandle());
			return null;
		} catch(Exception ex) {
			return new RuntimeException("Failed to receive packet through player.", ex);
		}
	}

	/**
	 * Silently sends the packet to a player. This will NOT trigger packet events.
	 * @param sender Player who 'sent' the packet.
	 * @return The exception thrown when trying to receive the packet. Null if none was thrown.
	 */
	public Exception receivePacketSilent(Player sender) {
		try {
			ProtocolLibrary.getProtocolManager().recieveClientPacket(sender, getHandle(), false);
			return null;
		} catch(Exception ex) {
			return new RuntimeException("Failed to receive packet through player.", ex);
		}
	}
}