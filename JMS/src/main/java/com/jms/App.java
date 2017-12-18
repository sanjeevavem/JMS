package com.jms;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

/**
 * Hello world!
 *
 */
public class App 
{
	 public static void main( String[] args ) throws JMSException
	    { @SuppressWarnings("resource")
		AnnotationConfigApplicationContext context =
	    new AnnotationConfigApplicationContext(AppConfig.class);
	    
	     QueueConnectionFactory qconFactory =(QueueConnectionFactory) context.getBean("connectionFactory");
	     QueueConnection qcon = null;
		try {
			qcon = qconFactory.createQueueConnection();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     System.out.println("qconFactory"+qconFactory);
	     try {
			QueueSession qsession = qcon.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue)context.getBean("jmsQueueName");
		     QueueSender qsender;
		     TextMessage msg;
		     qsender = qsession.createSender(queue);
		     msg = qsession.createTextMessage();
		   
		     for(int i=0;i<15;i++)
		     {
		    	  qcon.start();
		    	 msg.setText("Hello World new message "+i);
		    	 qsender.send(msg);
		    	 qcon.stop();
		     }
		    
		    
		    
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	    
	    }
}
