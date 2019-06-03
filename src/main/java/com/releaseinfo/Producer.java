package com.releaseinfo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.BasicConfigurator;

public class Producer {
	private final static String templatePath = System.getProperty("user.dir") + "/templates/";
	private static KafkaProducer<String, String> producer = null;
	
    @PostConstruct
    public void initPostConstruct() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
            	producer.close();
            }
        });
    }

	public static void main(String ... args) {
		String topic = "test";
		BasicConfigurator.configure();
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
		
		while (true) {
			String content = getXmlTemplate("1.xml");
			System.out.println("sent");
			ProducerRecord<String, String> message = new ProducerRecord<String, String>(topic, content.length() + " size");
			producer.send(message);
			
			pause(5);
		}
	}
	
	private static void pause (int secs) {
		try {
			TimeUnit.SECONDS.sleep(secs);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static String getXmlTemplate(String templateName) {
		String line = "";
		try {
			StringBuilder sb = new StringBuilder("");
			List<String> lines = Files.readAllLines(Paths.get(templatePath + templateName), StandardCharsets.UTF_8);
			for (int i = 0; i < lines.size(); i++) {
				sb.append(lines.get(i));
			}
			line = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}
}
