import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author Tituo
 * @create 2019-12-25 19:57
 * 消息中间件
 *      面向主题——消费者
 */
public class JMSConsumer_Topic {
    public static final String MQ_URL = "tcp://192.168.138.128:61616";
    public static final String TOPIC_NAME = "topic0805";

    public static void main(String[] args) throws JMSException {
        //1.先通过ActiveMQConnectionFactory获得mq工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(MQ_URL);
        //2.获得连接connection
        Connection connection = activeMQConnectionFactory.createConnection();
        //3.通过connection获得Session
            //3.1 第一个参数为事务。默认为false
            //3.2 第二个参数叫签收，默认自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //4.通过session创建目的地
        Topic topic = session.createTopic(TOPIC_NAME);
        //5.通过session创建消息消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);

        /*
        异步非阻塞方式（监听器onMessage（））
         */
        messageConsumer.setMessageListener(message -> {
            if (message != null && message instanceof TextMessage){
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("*******收到topic："+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
