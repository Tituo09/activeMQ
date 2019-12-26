import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author Tituo
 * @create 2019-12-25 18:37
 * 消息中间件
 *     面向队列——消息创造者
 *     队列默认持久化
 *        持久化：当服务器宕机，消息依然存在
 *        非持久：当服务器宕机，消息不存在
 *      事务：事务大于一切
 *          消息创建者开启事务的目的是防止数据不一致，进行回滚，事务开启后只有commit后才能将全部信息变为
 *          已消费
 *          在事务性会话中，当一个事务被成功提交则消息被自动签收
 *          如果事务回滚，则消息会被再次传送
 *          非事务性会话中，消息何时被确认取决于创建会话时的应答模式（acknowledgement mode）
 */
public class JMSProduce {
    public static final String MQ_URL = "tcp://192.168.138.128:61616";
    public static final String QUEUE_NAME = "queue0805";
    public static void main(String[] args) throws JMSException {
        //1.获得ActiveMQConnectionFactory
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(MQ_URL);
        //2.由ActiveMQConnectionFactory获得Connection
        Connection connection = activeMQConnectionFactory.createConnection();
        //3.启动连接准备建立会话
        connection.start();
        //4.获得Session，两个参数先用默认
            //4.1是否开启事务
            //4.2签收模式
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //开启事务
        //Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        //5.获得目的地，此例是队列
        Queue queue = session.createQueue(QUEUE_NAME);
        //6.获得消息生产者，生产什么内容？生产出来放在哪里？
        MessageProducer messageProducer = session.createProducer(queue);
        //设置队列为非持久化
        //messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        //7.生产message内容
        for (int i = 0; i < 3 ; i++) {
            TextMessage textMessage = session.createTextMessage("msg-------" + i);
            messageProducer.send(textMessage);
        }
        //8.释放各种连接和资源
        messageProducer.close();
        //提交事务
        //session.commit();
        session.close();
        connection.close();

        System.out.println("************msg send ok,^_^");

    }
}
