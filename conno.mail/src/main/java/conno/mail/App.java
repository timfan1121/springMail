package conno.mail;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.velocity.VelocityEngineUtils;

public class App {
	public App() throws FileNotFoundException {
		
	}

	public void sendOrderEmail() {
		/* 1.Spring IOC 獲得 Bean */
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "spring.xml" });
		JavaMailSenderImpl sender = (JavaMailSenderImpl) context
				.getBean("mailSender"); // 可以發送帶有附件的郵件
		VelocityEngine velocityEngine = (VelocityEngine) context
				.getBean("velocityEngine");

		try {

			/* 2.郵件內容(VelocityEngine) */
			String templateLocation = "mail/VM_global_library.vm";

			Map<String, Object> model = new HashMap<String, Object>();
			model.put("ORDER_ID", "this is table title");

			StringBuilder ds = new StringBuilder();
			ds.append("this is email content");
			model.put("ORDER_DETAIL", ds.toString());

			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templateLocation, "UTF-8", model);

			MimeMessage message = sender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");// 處理中文編碼
			helper.setSubject("email title"); // 主題
			helper.setFrom(sender.getUsername()); // 寄件者
			String[] users={"xxx@qq.com"};
			helper.setTo(users); // 收件人
			helper.setText(text, true); // 內容(HTML)
			sender.send(message);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			new App().sendOrderEmail();
			System.out.println("send Finish");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
