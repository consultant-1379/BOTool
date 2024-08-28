package BOMain;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {

	public static BufferedWriter createFileAdnWriter(String queryFilePath) throws IOException {
		queryFilePath += new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".csv";
		File file = new File(queryFilePath);
		return new BufferedWriter(new FileWriter(file));
	}

	public static void write(BufferedWriter writer, String formattedString) throws IOException {
		writer.append(formattedString);
		writer.write(System.lineSeparator());
		writer.flush();
	}

	public static void close(BufferedWriter queryWriter) throws IOException {
		queryWriter.close();
	}
}
