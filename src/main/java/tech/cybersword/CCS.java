package tech.cybersword;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.port;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CCS {
    private static final Logger logger = LogManager.getLogger(CCS.class);
    private static final String CSV_FILE = "./logs/tech.cybersword.java.command.control.csv";

    public static void main(String[] args) {
        port(1337);

        logger.info("start 1337 server");

        MqttPublisher mqttPublisher = new MqttPublisher();

        before((request, response) -> {
            String clientIP = request.ip();
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);

            String ipDateStr = formattedDateTime.concat("_" + clientIP);

            mqttPublisher.sendMQTT(ipDateStr);

            try {
                appendToCsv(formattedDateTime, clientIP);
            } catch (IOException e) {
                logger.error("error on log in csv file: ", e);
            }

            String message1 = request.requestMethod() + "_" + request.pathInfo();

            mqttPublisher.sendMQTT(message1);

            logger.info("Request method: {}, path: {}", request.requestMethod(), request.pathInfo());

            request.queryMap().toMap()
                    .forEach((key, value) -> logger.info("Query-parameter: {} = {}", key, String.join(", ", value)));

            request.headers().forEach(header -> logger.info("Header: {} = {}", header, request.headers(header)));

            if (request.contentLength() > 0) {
                logger.info("Body: {}", request.body());
            }
        });

        get("/1", (req, res) -> {
            String clientIP = req.ip();
            logger.info(String.format("request from %s on http endpoint 1 %n %s", clientIP, req.toString()));
            return clientIP;
        });

        get("/2", (req, res) -> {
            String clientIP = req.ip();
            logger.info(String.format("request from %s on http endpoint 2 %n %s", clientIP, req.toString()));
            return clientIP;
        });
    }

    private static void appendToCsv(String dateTime, String ip) throws IOException {
        try (FileWriter writer = new FileWriter(CSV_FILE, true)) {
            writer.append(dateTime);
            writer.append(',');
            writer.append(ip);
            writer.append('\n');
            writer.flush();
        }
    }
}
