package com.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;

public class Log {
  private String innerPath = "citadela_audit.log";

  Log() {
    super();
  }

  Log(String path) {
    super();
    innerPath = path;
  }

  public void logAudit(String args, String stdout) throws IOException {

    Path LOG_FILE = Path.of(innerPath);
    DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
    String line = "[" + timestamp + "] "
        + "<command-name>/census</command-name>"
        + "<command-message>census successful</command-message>"
        + "<command-args>" + args + "</command-args>"
        + "<local-command-stdout>" + stdout + "</local-command-stdout>"
        + System.lineSeparator();
    Files.writeString(LOG_FILE, line, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
  }
}
