package measure;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

public class CsvBeanWriter {
	
	private static final String DIRECTORY_DIR = "./messdaten";

	private static void writeCsvFromAccessTime(Path path, List<AccessTime> zugriffszeiten) throws Exception {
		Writer writer = new FileWriter(path.toString());

		StatefulBeanToCsv<CsvBean> sbc = new StatefulBeanToCsvBuilder<CsvBean>(writer)
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withEscapechar(CSVWriter.NO_ESCAPE_CHARACTER)
				.withSeparator(';').build();

		List<CsvBean> list = new ArrayList<>();
		list.addAll(zugriffszeiten);

		sbc.write(list);
		writer.close();
	}
	
	private static void createDirectory() {
		File directory = new File(DIRECTORY_DIR);
	    if (! directory.exists()){
	        directory.mkdir();
	    }
	}

	public static void writeCsvFromAccessTimeExample(List<AccessTime> list, String fileName) throws Exception {
		createDirectory();
		LocalTime time=LocalTime.now();
		Path path = Paths.get(DIRECTORY_DIR +"./" + fileName + "_" +time.getHour()+"_"+time.getMinute()+"_"+time.getSecond()+".csv");
		writeCsvFromAccessTime(path, list);
	}
}
