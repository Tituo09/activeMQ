import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Tituo
 * @create 2019-12-25 18:54
 *      签收模式：
 *          签收分为 自动签收：Session.AUTO_ACKNOWLEDGE（默认为自动签收）
 *                  手动签收：Session.CLIENT_ACKNOWLEDGE
 *                  当为手动签收时，客户端需要调用acknowledge方法手动签收
 *                      acknowledge方法手动签收
 *      一般消费者默认不开启事务
 */
public class JMSConsumer {
    public static final String MQ_URL = "tcp://192.168.138.128:61616";
    public static final String QUEUE_NAME ="queue0805";
    public static void main(String[] args) throws JMSException {
        //1.获得ActiveMQConnectionFactory连接工厂
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(MQ_URL);
        //2.由activeMQConnectionFactory获取Connection连接
        Connection connection = activeMQConnectionFactory.createConnection();
        //3.启动连接准备会话
        connection.start();
        //4.获得session会话
            //4.1是否开启事务
            //4.2签收模式
        //自动签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //手动签收
        //Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        //5.获取目的地，此例是队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6.获得消息消费者，消费什么内容？从哪里消费？
        MessageConsumer messageConsumer = session.createConsumer(queue);
        /**
         *异步非阻塞方式（监听器onMessage（））
         * 订阅者或接收者通过MessageConsumer的setMessageListener（MessageListener listener）
         * 注册一个消息监听器，当消息到达之后，系统自动调用监听器MessageListener的onMessage(Message
         * message)方法。
         */
        messageConsumer.setMessageListener(message ->  {
           if (message != null && message instanceof TextMessage){
               TextMessage textMessage = (TextMessage) message;
               try {
                   System.out.println("********messageConsumer:"+textMessage.getText());
               } catch (JMSException e) {
                   e.printStackTrace();
               }
           }
        });
        //暂停毫秒
        try {
            TimeUnit.MICROSECONDS.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //提交后能直接获取队列的值
        session.commit();

        /**
         * 同步阻塞方式receive()，订阅者或接收者调用MessageConsumer的receive()方法来接收消息
         * receive()将一直阻塞
         * receive(long timeout)按照给定的时间阻塞，到时间自动退出
         */
       /* while (true){
            //要与创造者类型一致需要进行强转
            //一直等不用释放资源
            //TextMessage textMessage = (TextMessage) messageConsumer.receive();
            //close需要
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000);
            if (null != textMessage){
                System.out.println("*******messageConsumer:"+textMessage.getText());
            }else {
                break;
            }
        }
        //8.释放各种连接和资源
        messageConsumer.close();
        session.close();
        connection.close();
        System.out.println("*******msg Consumer ok,o(*￣︶￣*)o");*/

    }
}
