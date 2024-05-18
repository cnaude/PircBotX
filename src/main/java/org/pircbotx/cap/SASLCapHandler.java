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
package org.pircbotx.cap;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.CAPException;

/**
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
@RequiredArgsConstructor
@ToString(exclude = "password")
public class SASLCapHandler implements CapHandler {
	protected final String username;
	protected final String password;
	protected final boolean ignoreFail;
	@Getter
	protected boolean done = false;

	/**
	 * Create SASLCapHandler not ignoring failed authentication and throwing
	 * a CapException
	 * @param username
	 * @param password
	 */
	public SASLCapHandler(String username, String password) {
		this.username = username;
		this.password = password;
		this.ignoreFail = false;
	}

        @Override
	public boolean handleLS(PircBotX bot, ImmutableList<String> capabilities) throws CAPException {
		if (capabilities.contains("sasl"))
			//Server supports sasl, send request to use it
			bot.sendCAP().request("sasl");
		else
			throw new CAPException(CAPException.Reason.UnsupportedCapability, "SASL");
		return false;
	}

        @Override
	public boolean handleACK(PircBotX bot, ImmutableList<String> capabilities) {
		if (capabilities.contains("sasl"))
			//Server acknowledges our request to use sasl 
			bot.sendRaw().rawLineNow("AUTHENTICATE PLAIN");
		//Still not finished
		return false;
	}

        @Override
	public boolean handleUnknown(PircBotX bot, String rawLine) throws CAPException {              
                if (rawLine.replace("AUTHENTICATE :+", "AUTHENTICATE +").equals("AUTHENTICATE +")) {
			//Server ackowledges our request to use plain authentication
			String encodedAuth = Base64.encodeBase64String((username + '\0' + username + '\0' + password).getBytes(Charsets.UTF_8));
			bot.sendRaw().rawLineNow("AUTHENTICATE " + encodedAuth);
		} 

		//Check for 904 and 905 
		String[] parsedLine = rawLine.split(" ", 4);
		if (parsedLine.length >= 1)
			if (parsedLine[1].equals("904") || parsedLine[1].equals("905")) {
				//Remove sasl as an enabled capability
				bot.getEnabledCapabilities().remove("sasl");

				if (!ignoreFail)
					throw new CAPException(CAPException.Reason.SASLFailed, "SASL Authentication failed with message: " + parsedLine[3].substring(1));

				//Pretend like nothing happened
				return true;
			} else if (parsedLine[1].equals("900") || parsedLine[1].equals("903"))
				//Success!
				return true;
		return false;
	}

        @Override
	public boolean handleNAK(PircBotX bot, ImmutableList<String> capabilities) throws CAPException {
		if (!ignoreFail && capabilities.contains("sasl")) {
			//Make sure the bot didn't register this capability
			bot.getEnabledCapabilities().remove("sasl");
			throw new CAPException(CAPException.Reason.UnsupportedCapability, "SASL");
		}
		return false;
	}
}
