import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class FixedLengthStringIO {
	public static String readFixedLengthString(int size, DataInput in) throws IOException {
		char[] chars = new char[size];
		for (int i = 0; i < size; i++)
			chars[i] = in.readChar();
		return new String(chars);
	}

	public static void writeFixedLengthString(String s, int size, DataOutput out) throws IOException {
		char[] chars = new char[size];
		s.getChars(0, Math.min(s.length(), size), chars, 0);
		for (int i = s.length(); i < size; i++)
			chars[i] = ' ';
		out.writeChars(new String(chars));
	}
}
