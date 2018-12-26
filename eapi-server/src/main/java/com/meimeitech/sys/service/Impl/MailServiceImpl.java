package com.meimeitech.sys.service.Impl;

import java.io.IOException;
import java.util.Locale;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;

import com.meimeitech.sys.service.MailService;
import com.meimeitech.sys.util.FreemarkerUtils;
import freemarker.template.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

	private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Configuration freemarkerConfiguration;
	@Autowired
	private MailProperties mailProperties;

	@Async
	@Override
	public void send(String[] toRecipients, String subject, String templateName, Locale locale, Object context) {
		try {
			String text = FreemarkerUtils.renderTemplate(
					freemarkerConfiguration.getTemplate(
							new StringBuffer().append("mail/").append(templateName).append(".ftl").toString(), locale),
					context);
			send(toRecipients, subject, text);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Async
	@Override
	public void send(final String[] toRecipients, final String subject, final Object text) {
		try {
			mailSender.send(mimeMessage -> {
				Address[] recipients = new Address[toRecipients.length];
				for (int i = 0; i < toRecipients.length; i++) {
					recipients[i] = new InternetAddress(toRecipients[i]);
				}
				mimeMessage.setRecipients(Message.RecipientType.TO, recipients);
				mimeMessage.setFrom(new InternetAddress(mailProperties.getUsername()));
				mimeMessage.setText(String.valueOf(text), mailProperties.getDefaultEncoding().name(),"html");
				mimeMessage.setSubject(subject);
			});
		} catch (MailException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}
	}

}
