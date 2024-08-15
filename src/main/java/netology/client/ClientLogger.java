package netology.client;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

class ClientLogger {
    private final String logFilePath;

    public ClientLogger(String logFileName) {
        this.logFilePath = Paths.get("src/main/java/netology/client", logFileName).toString();  // Путь к файлу логов в ресурсах
        createLogFileIfNotExists();  // Создаем лог-файл, если его нет
    }

    // Метод для создания файла логов, если он не существует
    private void createLogFileIfNotExists() {
        File logFile = new File(logFilePath);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();  // Создаем файл
                System.out.println("Лог-файл создан: " + logFilePath);
            } catch (IOException e) {
                System.out.println("Ошибка при создании лог-файла: " + e.getMessage());
            }
        }
    }

    // Метод для записи сообщения в лог-файл с добавлением временной метки
    public void logMessage(String message) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = new Date();
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            writer.write("[" + formater.format(currentTime) + "]" + " " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
