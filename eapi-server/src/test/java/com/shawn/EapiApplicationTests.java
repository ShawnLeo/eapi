package com.shawn;

import java.io.IOException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class EapiApplicationTests {

	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private MailProperties mailProperties;

	@Test
	public void fileTree() throws IOException {



	}
	public void contextLoads() {

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailProperties.getUsername());
		message.setTo("122525686@qq.com");
		message.setSubject("主题：简单邮件");
		message.setText("测试邮件内容");

		mailSender.send(message);
	}

}
