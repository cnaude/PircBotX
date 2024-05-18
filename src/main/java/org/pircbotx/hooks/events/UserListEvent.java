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

import com.google.common.collect.ImmutableSortedSet;
import java.util.Set;
import javax.annotation.Nullable;
import org.pircbotx.Channel;
import org.pircbotx.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.pircbotx.hooks.Event;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericChannelEvent;

/**
 * This event is dispatched when we receive a user list from the server
 * after joining a channel.
 * <p>
 * Shortly after joining a channel, the IRC server sends a list of all
 * users in that channel. The PircBotX collects this information and
 * dispatched this event as soon as it has the full list.
 * <p>
 * To obtain the nick of each user in the channel, call the {@link User#getNick()}
 * method on each User object in the {@link Set}.
 * <p>
 * At a later time, you may call {@link PircBotX#getUsers(org.pircbotx.Channel) }
 * to obtain an up to date list of the users in the channel.
 * @author Leon Blakey <lord.quackstar at gmail.com>
 * @see User
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserListEvent<B extends PircBotX> extends Event<B> implements GenericChannelEvent<B> {
	@Getter(onMethod_={@Override})
	protected final Channel channel;
	protected final ImmutableSortedSet<User> users;

	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param channel The channel that the user list is from.
	 * @param users An <b>immutable</b> Set of Users belonging to this channel.
	 */
	public UserListEvent(@NonNull B bot, @NonNull Channel channel, @NonNull ImmutableSortedSet<User> users) {
		super(bot);
		this.channel = channel;
		this.users = users;
	}

	/**
	 * Respond with a message to the channel that the userlist was from
	 * @param response The response to send
	 */
	@Override
	public void respond(@Nullable String response) {
		getChannel().send().message(response);
	}
}
