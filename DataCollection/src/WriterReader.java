import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class WriterReader {
	
	private String path;
	
	public WriterReader(String filePath) {
		path = filePath;
	}
	
	/**
	 * Checks if file is empty.
	 * @return false if the file isn't or if it doesn't exist. True otherwise.
	 */
	public boolean isEmpty() {
		try (BufferedReader br = new BufferedReader(new FileReader(path));) {
			if (br.readLine() == null) {
			    return true;
			} else {
				return false;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Precondition: the file associated with this object is empty
	 */
	public <E> void writeList(ArrayList<E> list) {
		try (ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(path))) {
			objectOutput.writeObject(list);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <E> ArrayList readList() {
		ArrayList<E> list = new ArrayList<E>();
		
		try (ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(path))) {
			list = (ArrayList<E>) objectInput.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
