/*
 * CreditCardToStringConverter.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import java.net.URLEncoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;

@Component
@Transactional
public class CreditCardToStringConverter implements Converter<CreditCard, String> {

	@Override
	public String convert(final CreditCard creditCard) {
		String res;
		StringBuilder builder;

		if (creditCard == null)
			res = null;
		else
			try {
				builder = new StringBuilder();

				String cvv;
				String expMonth;
				String expYear;
				cvv = Integer.toString(creditCard.getCVV());
				expMonth = Integer.toString(creditCard.getExpirationMonth());
				expYear = Integer.toString(creditCard.getExpirationYear());

				builder.append(URLEncoder.encode(creditCard.getNum(), "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(cvv, "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(expMonth, "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(expYear, "UTF-8"));
				builder.append("|");
				builder.append(URLEncoder.encode(creditCard.getHolderName(), "UTF-8"));
				res = builder.toString();
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}
		return res;
	}
}
