package com.epsilon.donornearme.utilities;

import com.epsilon.donornearme.Common;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.regex.Pattern;

public class KafkaUtility {
    private static final Logger logger = LogManager.getLogger(KafkaUtility.class);

    public KafkaConsumer createKafkaConsumer(HashMap<String, Object> props) throws Exception {
        KafkaConsumer kafkaConsumer;
        try {
            String bootstrap_servers = HashMapUtility.getString(props, Common.KAFKA_BOOTSTRAP_SERVERS);
//            String trustStoreFilePath =HashMapUtility.getString(props,Utils.Common.TRUSTORE_FILE_PATH);
            String consumerGroup = HashMapUtility.getString(props, Common.GROUP_ID);
            String clientId = HashMapUtility.getString(props, Common.CONSUMER_CLIENT_ID);
            clientId = StringUtils.defaultIfEmpty(clientId, UUID.randomUUID().toString());
            Properties kafkaConsumerProps = new Properties();
            kafkaConsumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_servers);
            kafkaConsumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
            kafkaConsumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Common.ENABLE_AUTO_COMMIT_CONFIG);
            kafkaConsumerProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, Common.SESSION_TIMEOUT_MS_CONFIG);
            kafkaConsumerProps.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, Common.MAX_POLL_RECORDS_CONFIG);
            kafkaConsumerProps.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
            kafkaConsumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            kafkaConsumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            kafkaConsumerProps.put(
                    ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, Common.MAX_POLL_INTERVAL_MS_CONFIG);
            kafkaConsumerProps.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, Common.REQUEST_TIMEOUT_MS_CONFIG);

            ArrayList<String> topics = HashMapUtility.getArrayList(props, Common.TOPIC_LIST);
            kafkaConsumer = new KafkaConsumer(kafkaConsumerProps);
            Boolean kafkaTopicContainsPattern = HashMapUtility.getBoolean(props, Common.TOPIC_CONTAINS_PATTERN);
            if (BooleanUtils.isFalse(kafkaTopicContainsPattern)) {
                kafkaConsumer.subscribe(topics);
                System.out.println("Subscribed to topics " + topics.get(0));
            } else {
                String topic = topics.get(0);
                System.out.println("Subscribed to topic " + topic);
                kafkaConsumer.subscribe(Pattern.compile(topic), new ConsumerRebalanceListener() {
                    public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                    }

                    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                    }
                });
                logger.info("kafka consumer subscribed to topic : {}", topic);
            }
        } catch (Exception e) {
            throw new Exception("Failed to construct kafka consumer", e);
        }
        return kafkaConsumer;
    }


    public KafkaProducer createKafkaProducer(HashMap<String, Object> props) throws Exception {
        KafkaProducer kafkaProducer;
        try {
            String bootstrap_servers = HashMapUtility.getString(props, Common.KAFKA_BOOTSTRAP_SERVERS);
//            String trustStoreFilePath = HashMapUtility.getString(props, Common.TRUSTORE_FILE_PATH);
            Properties kafkaProducerProps = new Properties();
            kafkaProducerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap_servers);
            kafkaProducerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            kafkaProducerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            kafkaProducerProps.put(ProducerConfig.ACKS_CONFIG, Common.ACKS_CONFIG);
            kafkaProducer = new KafkaProducer(kafkaProducerProps);
        } catch (Exception e) {
            throw new Exception("Failed to construct kafka producer", e);
        }
        return kafkaProducer;
    }
}



