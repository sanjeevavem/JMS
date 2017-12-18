package com.jms;

import java.util.Properties;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.NamingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;



@Configuration
@ComponentScan

public class AppConfig 
{
  @Bean(name="JndiTemplate")
  public JndiTemplate jndiTemplate()
  {
	  JndiTemplate jndiJndiTemplate = new JndiTemplate();
	  Properties properties = new Properties();
	  properties.put("java.naming.factory.initial","weblogic.jndi.WLInitialContextFactory");
	  properties.put("java.naming.provider.url", "t3://localhost:7001");
	  properties.put("java.naming.security.principal", "weblogic");
	  properties.put("java.naming.security.credentials", "weblogic@123");
	  jndiJndiTemplate.setEnvironment(properties);
	  return jndiJndiTemplate;
  }
  
  @Bean(name = "jmsJndiConnectionFactory")
  public JndiObjectFactoryBean jndiObjectFactoryBean() 
  {

      final JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
      jndiObjectFactoryBean.setJndiTemplate(jndiTemplate());   
      jndiObjectFactoryBean.setJndiName("jms/TestConnectionFactory");
      return jndiObjectFactoryBean;

  }
  
 @Bean(name = "connectionFactory")
  public ConnectionFactory connectionFactory()
  {
      final ConnectionFactory connectionFactory = (ConnectionFactory) jndiObjectFactoryBean().getObject();
      System.out.print("ConnectoinFactory is null? {}"+ connectionFactory == null);
      return connectionFactory;
  }
  
  @Bean(name = "jmsConnectionFactory")
  @Primary
  public ConnectionFactory connectionFactoryProxy() {
      final CachingConnectionFactory jmsConnectionFactory = new CachingConnectionFactory(connectionFactory());
      jmsConnectionFactory.setCacheProducers(true);
      jmsConnectionFactory.setSessionCacheSize(20);
      return jmsConnectionFactory;
  }
  
  @Bean(name="jmsQueueName")
  public Destination jmsQueueName() {

      final JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
      jndiObjectFactoryBean.setJndiTemplate(jndiTemplate());
      jndiObjectFactoryBean.setJndiName("jms/TestJMSQueue"); // queue name
      try {
		jndiObjectFactoryBean.afterPropertiesSet();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NamingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     // jndiObjectFactoryBean.set
      return (Destination) jndiObjectFactoryBean.getObject();
  }
  
  
  @Bean(name="jmsQueueName")
  public Destination jmsDestinationQueueName() {

      final JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
      jndiObjectFactoryBean.setJndiTemplate(jndiTemplate());
      jndiObjectFactoryBean.setJndiName("jms/jmsDestinationQueue"); // queue name
      try {
		jndiObjectFactoryBean.afterPropertiesSet();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NamingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
     // jndiObjectFactoryBean.set
      return (Destination) jndiObjectFactoryBean.getObject();
  }
  
  /*@Bean
  public DefaultMessageListenerContainer responder() {
  	DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
  	container.setConnectionFactory(connectionFactory());
  	container.setDestination(jmsDestinationQueueName());
   	MessageListenerAdapter adapter = new MessageListenerAdapter(new Object() {

  		@SuppressWarnings("unused")
  		public String handleMessage(String in) {
  			return in.toUpperCase();
  		}

  	});
  	container.setMessageListener(adapter);
  	return container;
  }*/
  
  
   
  
  	@Bean
  public MessageListenerContainer listenerContainer() {
      DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
      container.setConnectionFactory(connectionFactory());
      container.setDestinationName("test.out");
      //container.setDestination(jmsDestinationQueueName()); jmsQueueName() 
      container.setDestination(jmsQueueName());  
      container.setMessageListener(new MyJmsListener());
      container.start(); 
      return container;
  }
  	 
  
}