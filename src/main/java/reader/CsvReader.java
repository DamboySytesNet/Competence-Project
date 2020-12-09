package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class CsvReader {
    private static final String path = "data";

    public static List<POIDataContainer> readPOIDataFromCSV(String fileName) {
        List<POIDataContainer> dataList = new LinkedList<>();
        Path pathToFile = Paths.get(path + File.separator + fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8)) {
            String line = br.readLine();
            while(line != null) {
                String[] attributes = line.split(",");
                POIDataContainer data = new POIDataContainer(attributes);
                dataList.add(data);
                line = br.readLine();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return dataList;
    }
}

