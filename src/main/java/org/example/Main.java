package org.example;

import com.opencsv.CSVReader;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.opencsv.exceptions.CsvException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.producer.internals.FutureRecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

// This class implements the Callback interface to handle asynchronous responses from Kafka.
class DemoProducerCallback implements Callback {
    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
        if (e != null) {
            e.printStackTrace();
        } else {
            System.out.println("The topic " + recordMetadata.topic() + " The partition " + recordMetadata.partition());
        }
    }
}

// Main class for the Kafka producer
public class Main {

    public static void main(String[] args) throws InterruptedException, JsonProcessingException, ParseException {
        List<String[]> the_rows; // List to store rows from CSV
        ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper to convert objects to JSON
        SimpleDateFormat formatter = new SimpleDateFormat("yy-MM-dd HH:mm"); // Date formatter

        // Reading data from CSV file
        {
            try {
                // Specify the path to the CSV file
                CSVReader reader = new CSVReader(new FileReader("C:\\Users\\aaboheiba\\Desktop\\mass-data-generator-main\\mass-data-generator-main\\data\\transaction.csv"));
                reader.skip(1); // Skip header row

                try {
                    // Read all rows from the CSV file
                    the_rows = reader.readAll();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (CsvException e) {
                throw new RuntimeException(e);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Kafka producer configuration
        Properties prop = new Properties();
        prop.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        prop.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.put(ProducerConfig.ACKS_CONFIG, "1");

        KafkaProducer producer1 = new KafkaProducer(prop); // Creating a Kafka producer instance
        int i = 0;

        // Iterating through each row from the CSV and sending records to Kafka topic
        for (String[] record1 : the_rows) {
            // Parsing the date from the first column of the CSV
            Date date = formatter.parse(record1[0]);
            long timestamp = date.getTime(); // Converting date to timestamp in milliseconds

            // Creating a ProducerRecord to be sent to Kafka
            ProducerRecord<String, String> record = new ProducerRecord<>("transaction_Card_2", record1[1], objectMapper.writeValueAsString(record1));
            try {
                // Sending the record asynchronously to Kafka with a callback
                producer1.send(record, new DemoProducerCallback());

            } catch (Exception e) {
                e.printStackTrace();
                producer1.close();
            }
            if (i % 20 == 0) {
                Thread.sleep(5000); // Introducing a delay every 20 records
            }
            i = i + 1;

            System.out.println(i); // Printing record count
        }
        producer1.close(); // Closing the Kafka producer
    }
}
