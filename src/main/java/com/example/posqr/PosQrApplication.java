package com.example.posqr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@SpringBootApplication
public class PosQrApplication {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${spring.application.name:POS QR App}")
    private String applicationName;

    private final Environment environment;
    private Instant startupTime;

    public PosQrApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(PosQrApplication.class, args);
    }

    @PostConstruct
    public void logStartupDetails() {
        this.startupTime = Instant.now();
        String timestamp = getCurrentFormattedTimestamp();
        String profile = getActiveProfile();
        String hostAddress = getHostAddress();

        logBanner("Started", timestamp, profile, hostAddress);
    }

    @EventListener
    public void onApplicationShutdown(ContextClosedEvent event) {
        Instant shutdownTime = Instant.now();
        Duration uptime = Duration.between(startupTime, shutdownTime);
        long minutes = uptime.toMinutes();
        long seconds = uptime.minusMinutes(minutes).getSeconds();

        System.out.println("\n========================================");
        System.out.println("Application       : " + applicationName);
        System.out.println("POS QR Code System Shutting Down");
        System.out.println("Uptime Duration   : " + minutes + " min, " + seconds + " sec");
        System.out.println("Shutdown Time     : " + getCurrentFormattedTimestamp());
        System.out.println("========================================\n");
    }

    private String getActiveProfile() {
        String[] profiles = environment.getActiveProfiles();
        return profiles.length > 0 ? String.join(", ", profiles) : "default";
    }

    private String getCurrentFormattedTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH));
    }

    private String getHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }

    private void logBanner(String status, String timestamp, String profile, String hostAddress) {
        String osName = System.getProperty("os.name");
        String javaVersion = System.getProperty("java.version");
        String userName = System.getProperty("user.name");
        String userDir = System.getProperty("user.dir");

        System.out.println("\n========================================");
        System.out.println("Application       : " + applicationName);
        System.out.println("POS QR Code System " + status);
        System.out.println("Timestamp         : " + timestamp);
        System.out.println("Active Profiles   : " + profile);
        System.out.println("Server Port       : " + serverPort);
        System.out.println("Host Address      : " + hostAddress);
        System.out.println("OS Name           : " + osName);
        System.out.println("Java Version      : " + javaVersion);
        System.out.println("System User       : " + userName);
        System.out.println("Working Directory : " + userDir);
        System.out.println("========================================\n");
    }
}
