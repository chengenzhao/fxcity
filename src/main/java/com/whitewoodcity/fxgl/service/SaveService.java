package com.whitewoodcity.fxgl.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public interface SaveService {
  default void autoSave(final Map parameters) {
    parameters.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    saveFile("saves", "autosave.json", parameters);
  }

  default void saveFile(String directory, String fileName, final ObjectNode config) {
    saveFile(directory, fileName, config.toString());
  }

  default void saveFile(String directory, String fileName, final Map parameters) {
    try {
      saveFile(directory, fileName, new ObjectMapper().writeValueAsString(parameters));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  default void saveFile(String directory, String fileName, final String content) {
    var thread = new Thread(() -> {
      var file = new File(directory, fileName);
      try {
        if (file.exists()) file.delete();
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        if (file.createNewFile()) {
          var writer = new FileWriter(file);
          writer.write(content);
          writer.flush();
          writer.close();
        }
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    });
    thread.setDaemon(true);
    thread.start();
  }
}
