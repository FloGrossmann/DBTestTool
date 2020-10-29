package measure;

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

	public static void writeCsvFromAccessTimeExample(List<AccessTime> list) throws Exception {
		LocalTime time=LocalTime.now();
		Path path = Paths.get("./messdaten_"+time.getHour()+"_"+time.getMinute()+"_"+time.getSecond()+".csv");
		writeCsvFromAccessTime(path, list);
	}
}
