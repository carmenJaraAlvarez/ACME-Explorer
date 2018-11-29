/*
 * AdministratorToStringConverter.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Agent;

@Component
@Transactional
public class AgentToStringConverter implements Converter<Agent, String> {

	@Override
	public String convert(final Agent agent) {

		String result;
		if (agent == null)
			result = null;
		else
			result = String.valueOf(agent.getId());

		return result;
	}

}
