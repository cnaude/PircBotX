/**
 * Copyright (C) 2010 Leon Blakey <lord.quackstar at gmail.com>
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with PircBotX.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.pircbotx.events;

import org.pircbotx.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.pircbotx.hooks.helpers.Event;
import org.pircbotx.PircBotX;

/**
 * This method is called whenever someone (possibly us) changes nick on any
 * of the channels that we are on.
 *  <p>
 * The implementation of this method in the PircBotX abstract class
 * performs no actions and may be overridden as required.
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NickChangeEvent extends Event {
	protected final String oldNick;
	protected final String newNick;
	protected final User source;

	/**
	 * Default constructor to setup object. Timestamp is automatically set
	 * to current time as reported by {@link System#currentTimeMillis() }
	 * @param oldNick The old nick.
	 * @param newNick The new nick.
	 * @param source The user that changed their nick
	 */
	public <T extends PircBotX> NickChangeEvent(T bot, String oldNick, String newNick, User source) {
		super(bot);
		this.oldNick = oldNick;
		this.newNick = newNick;
		this.source = source;
	}
}