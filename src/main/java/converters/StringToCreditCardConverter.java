/*
 * StringToCreditCardConverter.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package converters;

import java.net.URLDecoder;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.CreditCard;

@Component
@Transactional
public class StringToCreditCardConverter implements Converter<String, CreditCard> {

	@Override
	public CreditCard convert(final String text) {
		final CreditCard res;
		final String parts[];

		if (text == null || text == "")
			try {
				res = new CreditCard();
				res.setCVV(null);
				res.setExpirationMonth(null);
				res.setExpirationYear(null);
				res.setHolderName("");
				res.setNum(null);
			} catch (final Throwable oops) {
				throw new RuntimeException();
			}

		else
			try {
				parts = text.split("\\|");
				res = new CreditCard();
				String num;
				Integer cvv;
				Integer expMonth;
				Integer expYear;
				String holderName;

				if (URLDecoder.decode(parts[0], "UTF-8").equals("")) {
					num = null;
				} else {
					num = URLDecoder.decode(parts[0], "UTF-8");
				}

				if (URLDecoder.decode(parts[1], "UTF-8").equals("")) {
					cvv = null;
				} else {
					cvv = Integer.valueOf(URLDecoder.decode(parts[1], "UTF-8"));
				}

				if (URLDecoder.decode(parts[3], "UTF-8").equals("")) {
					expMonth = null;
				} else {
					expMonth = Integer.valueOf(URLDecoder.decode(parts[3], "UTF-8"));
				}
				if (URLDecoder.decode(parts[4], "UTF-8").equals("")) {
					expYear = null;
				} else {
					expYear = Integer.valueOf(URLDecoder.decode(parts[4], "UTF-8"));
				}
				if (URLDecoder.decode(parts[5], "UTF-8").equals("")) {
					holderName = null;
				} else {
					holderName = URLDecoder.decode(parts[5], "UTF-8");
				}

				res.setNum(num);
				res.setCVV(cvv);
				res.setExpirationMonth(expMonth);
				res.setExpirationYear(expYear);
				res.setHolderName(holderName);

			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			}

		return res;
	}
}
