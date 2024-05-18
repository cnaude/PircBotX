/**
 * Copyright (C) 2010-2013 Leon Blakey <lord.quackstar at gmail.com>
 *
 * This file is part of PircBotX.
 *
 * PircBotX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * PircBotX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PircBotX. If not, see <http://www.gnu.org/licenses/>.
 */
package org.pircbotx.hooks.events;

import javax.annotation.Nullable;
import lombok.AccessLevel;
import org.pircbotx.Channel;
import org.pircbotx.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.pircbotx.hooks.Event;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericUserModeEvent;

/**
 * Called when a user (possibly us) gets voice status granted in a channel.
 * <p>
 * This is a type of mode change and therefor is also dispatched in a
 * {@link org.pircbotx.hooks.events.ModeEvent}
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VoiceEvent<T extends PircBotX> extends Event<T> implements GenericUserModeEvent<T> {
	@Getter(onMethod_={@Override})
	protected final Channel channel;
	@Getter(onMethod_={@Override})
	protected final User user;
	@Getter(onMethod_={@Override})
	protected final User recipient;
	@Getter(AccessLevel.NONE)
	protected final boolean hasVoice;

	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param channel The channel in which the mode change took place.
	 * @param user The user that performed the mode change.
	 * @param recipient The nick of the user that got 'voiced'.
	 */
	public VoiceEvent(T bot, @NonNull Channel channel, @NonNull User user, @NonNull User recipient, boolean isVoice) {
		super(bot);
		this.channel = channel;
		this.user = user;
		this.recipient = recipient;
		this.hasVoice = isVoice;
	}

	/**
	 * Checks if this is a set or remove voice operation
	 * @return True if this was set, false if removed
	 * @deprecated Use the better named hasVoice method. Will be removed in future versions
	 * @see #hasVoice()
	 */
	@Deprecated
	public boolean isVoice() {
		return hasVoice;
	}

	public boolean hasVoice() {
		return hasVoice;
	}

	@Override
	public void respond(@Nullable String response) {
		getChannel().send().message(response);
	}
}
