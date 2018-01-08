package com.ka.cert.transparency.loader.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Description: File utility class
 * Project: cert-transparency-service
 * Package: org.cert.transparency.service.util
 * Author: kakyurek
 * Date: 2018.01.07
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() {
    }

    /**
     * Create directory unless it exists into specified path
     *
     * @param directoryPath Target directory path
     */
    public static void createDirectoryIfNotExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            if (!created) {
                logger.error("Could not create directory with path: " + directoryPath);
            }
        }
    }

    /**
     * Deletes directory if it exists into specified path
     *
     * @param directoryPath Target directory path
     */
    public static void deleteDirectoryIfExists(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists()) {
            boolean deleted = directory.delete();
            if (!deleted) {
                logger.error("Could not delete directory with path: " + directoryPath);
            }
        }
    }

    /**
     * Deletes directory with all content in it
     *
     * @param directoryPath Target directory patbh
     * @throws IOException Thrown while deleting files recursively if something occurs outside of scope
     */
    public static void deleteDirectoryRecursively(String directoryPath) throws IOException {
        Path directory = Paths.get(directoryPath);
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * Append string line to existing file
     *
     * @param filePath Target file path
     * @param line     New string line to append
     */
    public static void appendLineToFile(String filePath, String line) {
        File file = new File(filePath);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))) {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw.write(line);
            bw.close();
        } catch (IOException e) {
            logger.error("Error thrown while trying to write a file\n" + e.getMessage());
        }
    }

    /**
     * Creates file like Linux touch command manner
     *
     * @param filePath Target file path
     * @return Created file
     * @throws IOException Thrown if error occurs while trying to create file
     */
    private static File touchFile(String filePath) throws IOException {
        File file = new File(filePath);
        boolean created = file.createNewFile();
        logger.debug("File created: " + filePath);
        return file;
    }

    /**
     * Writes byte array to file
     *
     * @param bytes    File content as byte array
     * @param filePath Target file path
     */
    static void byteArrayToFile(byte[] bytes, String filePath) {
        try (FileOutputStream fileOutStream = new FileOutputStream(touchFile(filePath))) {
            fileOutStream.write(bytes);
        } catch (IOException e) {
            logger.error("Error thrown while trying to save byte array into file.\n" + e.getMessage());
        }
    }

}
