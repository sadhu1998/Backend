package com.epsilon.donornearme.operations;

import com.epsilon.donornearme.Common;
import com.epsilon.donornearme.utilities.JSONUtility;
import com.epsilon.donornearme.utilities.KafkaUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatBotOperator extends CommonOperator {
    KafkaUtility kafkaUtility = new KafkaUtility();

    public String sendMessageToBot(String json) throws JSONException, JsonProcessingException {
        HashMap<String, Object> bot_input_msg_map = JSONUtility.jsonToMap(json);
        HashMap<String, Object> kafka_config = new HashMap<>();
        HashMap<String, Object> status_map = new HashMap<>();

        kafka_config.put(Common.KAFKA_BOOTSTRAP_SERVERS, "localhost:9092");
        try {
            KafkaProducer kafkaProducer = kafkaUtility.createKafkaProducer(kafka_config);

            kafkaProducer.send(new ProducerRecord("test", json));
            kafkaProducer.close();

            status_map.put(Common.STATUS, "Status sent to Bot");
            return getResponseOfBot();

        } catch (Exception e) {
            status_map.put(Common.STATUS, "Sending Message Failed");
            e.printStackTrace();
        }
        return "Bot under progress";
    }

    public String getResponseOfBot() throws Exception {
        HashMap<String, Object> kafka_config_map_consumer = new HashMap<String, Object>();
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("response");
        kafka_config_map_consumer.put(Common.KAFKA_BOOTSTRAP_SERVERS, "localhost:9092");
        kafka_config_map_consumer.put(Common.GROUP_ID, "donor_bot_response");
        kafka_config_map_consumer.put(Common.TOPIC_LIST, topics);
        kafka_config_map_consumer.put(Common.TOPIC_CONTAINS_PATTERN, false);
        KafkaConsumer kafkaConsumer = getKafkaConsumer();
        String response;
        while (true) {
            logger.info("Waiting for Response");
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                response = record.value();
                logger.info("Chat Bot Response :" + response);
                kafkaConsumer.commitSync();
                kafkaConsumer.close();
                return response;
            }
        }
    }

    public KafkaConsumer getKafkaConsumer() throws Exception {
        HashMap<String, Object> kafka_config_map_consumer = new HashMap<String, Object>();
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("response");
        kafka_config_map_consumer.put(Common.KAFKA_BOOTSTRAP_SERVERS, "localhost:9092");
        kafka_config_map_consumer.put(Common.GROUP_ID, "donor_bot_response");
        kafka_config_map_consumer.put(Common.TOPIC_LIST, topics);
        kafka_config_map_consumer.put(Common.TOPIC_CONTAINS_PATTERN, false);

        return kafkaUtility.createKafkaConsumer(kafka_config_map_consumer);
    }

}
