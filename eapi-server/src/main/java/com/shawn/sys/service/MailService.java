package com.shawn.sys.service;

import java.util.Locale;

public interface MailService {

	void send(String[] toRecipients, String subject, String templateName, Locale locale, Object context);

	void send(String[] toRecipients, String subject, Object text);

}
