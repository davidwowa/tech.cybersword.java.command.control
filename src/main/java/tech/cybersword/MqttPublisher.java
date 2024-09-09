package tech.cybersword;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublisher {

    private static final Logger logger = LogManager.getLogger(MqttPublisher.class);

    public void sendMQTT(String content) {
        String broker = "tcp://server:1883";
        String clientId = "JavaClient";
        String topic = "home/p";
        int qos = 2; // Quality of Service Level (0, 1, oder 2)

        try {
            // Speicher für Nachrichten wird im Speicher (RAM) gehalten
            MemoryPersistence persistence = new MemoryPersistence();

            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);

            mqttClient.connect();

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);

            mqttClient.publish(topic, message);
            mqttClient.disconnect();
            if (logger.isInfoEnabled()) {
                logger.info("MQTT-connection closed");
            }
        } catch (MqttException e) {
            logger.error("Fehler bei der MQTT-Verbindung oder Veröffentlichung", e.getMessage());
            e.printStackTrace();
        }
    }
}
