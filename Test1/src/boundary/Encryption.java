package boundary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import entity.Defect;
import entity.Effort;

public class Encryption {
	
	private static ArrayList<Effort> effortList = new ArrayList<Effort>();
	private static ArrayList<String> effortFilesList = new ArrayList<String>();
	private static ArrayList<Defect> defectList = new ArrayList<Defect>();
	private static ArrayList<String> defectFilesList = new ArrayList<String>();
	private static SecretKey currentKey = null;

	/**
	 * Take a path to an effort object and decrypt it.
	 * Note that if you run the decrypt function twice on the same filename,
	 * it will return a reference to the exact same object both times.
	 * Note the encrypter needs to know the password prior.
	 * @param filename The path to the effort to decrypt.
	 * @return The effort associated to the filename.
	 * @see #usePassword(String)
	 */
	public static Effort decryptEffort(String filename)
	{
		int i = -1;
		for (int j = 0; j < effortFilesList.size(); j++)
		{
			if (filename.equals(effortFilesList.get(j)))
			{
				i = j;
				break;
			}
		}
		
		if (i != -1) return effortList.get(i);

		Effort readresult = (Effort) read(filename, currentKey);
		System.out.println("read from " + filename);
		if (null != readresult)
		{
			effortList.add(readresult);
			effortFilesList.add(filename);
		}
		return readresult;
	}
	
	/**
	 * Take a path to an effort or defect and decrypt it.
	 * Note that if you run the decrypt function twice on the same filename,
	 * it will return a reference to the exact same object both times.
	 * Note the encrypter needs to know the password prior.
	 * @param filename The path to the effort to decrypt.
	 * @return True if the effort or defect from the file exists in its respective list now.
	 * @see #usePassword(String)
	 */
	public static boolean decrypt(String filename)
	{
		int i = -1;
		for (int j = 0; j < effortFilesList.size(); j++)
		{
			if (filename.equals(effortFilesList.get(j)))
			{
				i = j;
				break;
			}
		}
		if (i != -1) return true;
		for (int j = 0; j < defectFilesList.size(); j++)
		{
			if (filename.equals(defectFilesList.get(j)))
			{
				i = j;
				break;
			}
		}
		if (i != -1) return true;

		Object readresult = read(filename, currentKey);
		if (null != readresult)
		{
			if (readresult.getClass() == Effort.class)
			{
				effortList.add((Effort) readresult);
				effortFilesList.add(filename);
				System.out.println("read from effort " + filename);
				return true;
			}
			else if (readresult.getClass() == Defect.class)
			{
				defectList.add((Defect) readresult);
				defectFilesList.add(filename);
				System.out.println("read from defect " + filename);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a copied list of all effort that has been decrypted
	 * but also not forgotten or deleted.
	 * @return A copied list of all effort that have been decrypted.
	 * @see #decryptEffort
	 * @see #forgetEffort
	 * @see #deleteEffort
	 */
	public static List<Effort> getEffort()
	{
		return (List<Effort>) effortList.clone();
	}

	/**
	 * Returns a copied list of all files that has been decrypted
	 * but also not forgotten or deleted.
	 * @return A copied list of all effort files that have been decrypted.
	 * @see #decryptEffort
	 * @see #forgetEffort
	 * @see #deleteEffort
	 */
	public static List<String> getEffortFiles()
	{
		return (List<String>) effortFilesList.clone();
	}

	/**
	 * Makes a specific effort not be returned by getEffort on further calls.
	 * @param e Effort to forget
	 * @return True if an effort was genuinely forgotten.
	 */
	public static boolean forgetEffort(Effort e)
	{
		int i = effortList.indexOf(e);
		if (i == -1) return false;
		effortList.remove(i);
		effortFilesList.remove(i);
		return true;
	}

	/**
	 * Deletes the effort file for a specific effort.
	 * @param e The effort who's file will be deleted.
	 * @return True if a file was deleted.
	 */
	public static boolean deleteEffort(Effort e)
	{
		int i = effortList.indexOf(e);
		String filename = null;
		if (i == -1) return false;

		effortList.remove(i);
		filename = effortFilesList.remove(i);

		File f = new File(filename);
		return f.delete();
	}

	/**
	 * Writes the updated version of the effort to the given file.
	 * If the effort does not have an file, a new file will be written.
	 * Note the encrypter needs to know the password prior.
	 * @param effortToWrite The effort who's file will be written.
	 * @see #usePassword(String)
	 */
	// TODO Make it so the encrypter does NOT need to be externally told the password
	public static void writeEffort(Effort effortToWrite)
	{
		int i = effortList.indexOf(effortToWrite);
		String filename;
		if (i != -1)
		{
			// filename exists, get it
			filename = effortFilesList.get(i);
		} else
		{
			filename = getPseudoRandom(16) + ".effort";

			effortList.add(effortToWrite);
			effortFilesList.add(filename);
			// TODO if a write fails, the new file should be removed
		}

		write(filename, currentKey, effortToWrite);
		System.out.println("write to effort " + filename);
	}

	/**
	 * Returns a copied list of all effort that has been decrypted
	 * but also not forgotten or deleted.
	 * @return A copied list of all effort that have been decrypted.
	 */
	public static List<Defect> getDefect()
	{
		return (List<Defect>) defectList.clone();
	}

	/**
	 * Returns a copied list of all defect files that has been decrypted
	 * but also not forgotten or deleted.
	 * @return A copied list of all defect files that have been decrypted.
	 */
	public static List<String> getDefectFiles()
	{
		return (List<String>) defectFilesList.clone();
	}

	/**
	 * Makes a specific effort not be returned by getDefect on further calls.
	 * @param d Defect to forget
	 * @return True if an defect was genuinely forgotten.
	 */
	public static boolean forgetDefect(Defect d)
	{
		int i = defectList.indexOf(d);
		if (i == -1) return false;
		defectList.remove(i);
		defectFilesList.remove(i);
		return true;
	}

	/**
	 * Deletes the defect file for a specific defect.
	 * @param d The defect who's file will be deleted.
	 * @return True if a file was deleted.
	 */
	public static boolean deleteDefect(Defect d)
	{
		int i = defectList.indexOf(d);
		String filename = null;
		if (i == -1) return false;

		defectList.remove(i);
		filename = defectFilesList.remove(i);

		File f = new File(filename);
		return f.delete();
	}

	/**
	 * Writes the updated version of the defect to the given file.
	 * If the defect does not have an file, a new file will be written.
	 * Note the encrypter needs to know the password prior.
	 * @param defectToWrite The defect who's file will be written.
	 * @see #usePassword(String)
	 */
	// TODO Make it so the encrypter does NOT need to be externally told the password
	public static void writeDefect(Defect defectToWrite)
	{
		int i = defectList.indexOf(defectToWrite);
		String filename;
		if (i != -1)
		{
			// filename exists, get it
			filename = defectFilesList.get(i);
		} else
		{
			filename = getPseudoRandom(16) + ".defect";

			defectList.add(defectToWrite);
			defectFilesList.add(filename);
			// TODO if a write fails, the new file should be removed
		}

		write(filename, currentKey, defectToWrite);
		System.out.println("write to defect " + filename);
	}
	
	/**
	 * 
	 */
	public static void usePassword(String password)
	{
		currentKey = passwordToKey(password);
	}
	
	/**
	 * 
	 */
	public static void unusePassword()
	{
		currentKey = null;
	}
	

	
	/**
	 * Generate a PseudoRandom string.
	 * @param i length of string
	 * 
	 */
	private static String getPseudoRandom(int i)
	{
		Random rand = new Random();
		String output = "";
		String allowed = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String nonnum = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		if (i<0) i=0;
		if (i>0)
		{
			output+= nonnum.charAt(rand.nextInt(nonnum.length()));
			i--;
		}
		for (; i>0; i--) output+= allowed.charAt(rand.nextInt(allowed.length()));
		return output;
	}

	/**
	 * Convert a string password into a AES key.
	 * @param password some string
	 * @return usable AES key
	 * 
	 */
	private static SecretKey passwordToKey(String password)
	{
		String salt = "uNEB,pr^V#,9$K2{"; // the salt can be the same since we are not storing the password
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
	    SecretKey originalKey;
		try {
			originalKey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			return null;
		}
	    return originalKey;
	}
	
	/**
	 * Write a single object to an encrypted file
	 * @param fileName name of the encrypted file to read
	 * @param key usable AES key
	 * @param content object to be serialized
	 * @return true if the file was successfully written
	 * 
	 */
	private static boolean write(String fileName, SecretKey key, Object content)
	{
		boolean return_status = true;
		// get file
		OutputStream ostream;
		try {
			ostream = new FileOutputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		// build cipher
		Cipher cipher;
		byte[] iv;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			// TODO take a better look at http://stackoverflow.com/a/3452620/1188357
			cipher.init(Cipher.ENCRYPT_MODE, key);

			iv = cipher.getIV();
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();

			try {
				ostream.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			return false;
		}

		// generate streams and write data
		CipherOutputStream costream = new CipherOutputStream(ostream, cipher);
		ObjectOutputStream oostream = null;
		try {
			 oostream = new ObjectOutputStream(costream);

			// make sure to write initialization vector first
			ostream.write(iv);
			// then write data
			oostream.writeObject(content);

		} catch (IOException e) {
			e.printStackTrace();
			return_status = false;
		}
		
		// close all streams
		try {
			if (null != oostream) oostream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// NOTE it is very important for the stream to close so the padding can be added
			// if the stream is not closed, the padding will not be added
			costream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ostream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return return_status;
	}

	/**
	 * Read a single object to an encrypted file
	 * @param fileName name of the encrypted file to read
	 * @param key usable AES key
	 * @return the object which was deserialized
	 */
	private static Object read(String fileName, SecretKey key)
	{
		Object o = null;
		// get file
		InputStream istream;
		try {
			istream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		// retrieve initialization vector
		byte[] iv = new byte[16];
		try {
			// retrieve initialization vector before setting up cipher
			istream.read(iv);
		} catch (IOException e1) {
			e1.printStackTrace();

			try {
				istream.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			return null;
		}

		// reconstruct cipher
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();

			try {
				istream.close();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			return null;
		}

		// setup reader
		CipherInputStream cistream = new CipherInputStream(istream, cipher);
		ObjectInputStream oistream = null;
		try {
			oistream = new ObjectInputStream(cistream);
			
			o = oistream.readObject();
		} catch (IOException | ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// close all streams
		try {
			if (null != oistream) oistream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			cistream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			istream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return o;
	}
	

	@Test
	void testWriteRead()
	{
		String file = "test.enc";
		SecretKey key = passwordToKey("generic password");
		Integer content = 12;
		
		write(file, key, content);
		Integer readcontent = (Integer) read(file, key);

		assertEquals(content.intValue(), readcontent.intValue());
	}
	
	@Test
	void testBadPassword()
	{
		String file = "test.enc";
		SecretKey key = passwordToKey("generic password");
		Integer content = 12;
		
		write(file, key, content);
		SecretKey key2 = passwordToKey("different password");
		Integer readcontent = (Integer) read(file, key2);

		assertEquals(null, readcontent);
	}
	
	@Test
	void testBadReadContent()
	{
		String file = "test.enc";
		SecretKey key = passwordToKey("generic password");
		Integer content = 12;
		
		try (FileOutputStream ostream = new FileOutputStream(file))
		{
			ostream.write(content.intValue()); // write some content to the file
		} catch (IOException e)
		{
			fail(e.getMessage());
		}
		
		Integer readcontent = (Integer) read(file, key);

		assertEquals(null, readcontent);
	}

	@Test
	void testBadReadFile()
	{
		String file = "test.enc";
		SecretKey key = passwordToKey("generic password");
		Integer content = 12;
		
		File f = new File(file);
		f.delete(); // make sure there is no file
		
		Integer readcontent = (Integer) read(file, key);

		assertEquals(null, readcontent);
	}
	
	@Test
	void testWriteReadEffort()
	{
		// make some effort
		Effort e = new Effort(12, null, null, null, null, 0, null, null, null);
		Effort e2;

		usePassword("generic password");
		writeEffort(e);
		
		e2 = decryptEffort(effortFilesList.get(effortList.indexOf(e)));
		
		assertEquals(e.toString(), e2.toString());
	}

	@Test
	void testWriteWriteReadEffort()
	{
		// make some effort
		Effort e = new Effort(12, null, null, null, null, 0, null, null, null);
		Effort e2;

		usePassword("generic password");
		writeEffort(e);
		
		e2 = decryptEffort(effortFilesList.get(effortList.indexOf(e)));
		
		assertEquals(e.toString(), e2.toString());
		

		e.setNum(11); // change the effort

		writeEffort(e); // update the change to the effort
		
		e2 = decryptEffort(effortFilesList.get(effortList.indexOf(e)));

		assertEquals(e.toString(), e2.toString());
	}

	@Test
	void testWriteReadEfforts()
	{
		// make some effort
		ArrayList<Effort> el = new ArrayList<Effort>();
		Effort e2;

		usePassword("generic password");

		for (int i = 0; i < 20; i++)
		{
			el.add(new Effort(i, null, null, null, null, i, null, null, null));
			writeEffort(el.get(i));
		}

		for (int i = 0; i < 20; i++)
		{
			e2 = decryptEffort(effortFilesList.get(effortList.indexOf(el.get(i))));
			assertEquals(el.get(i).toString(), e2.toString());
		}
	}

	@Test
	void testForgetEffort()
	{
		// make some effort
		Effort e = new Effort(12, null, null, null, null, 0, null, null, null);
		Effort e2;
		String password = "generic password";

		usePassword(password);
		writeEffort(e);
		unusePassword();
		
		String file = effortFilesList.get(effortList.indexOf(e));
		
		forgetEffort(e); // forgetting the file should not take the password
		assertEquals(-1, effortList.indexOf(e));
		assertEquals(-1, effortFilesList.indexOf(file));

		usePassword(password);
		e2 = decryptEffort(file); // the file should still be present after forgetting
		unusePassword();
		
		assertEquals(e.toString(), e2.toString());
	}

	@Test
	void testDeleteEffort()
	{
		// make some effort
		Effort e = new Effort(12, null, null, null, null, 0, null, null, null);
		Effort e2;
		String password = "generic password";

		usePassword(password);
		writeEffort(e);
		unusePassword();
		
		String file = effortFilesList.get(effortList.indexOf(e));
		
		deleteEffort(e); // deleting the file should not take the password

		usePassword(password);
		e2 = decryptEffort(file);
		unusePassword();
		
		assertEquals(null, e2);
		assertEquals(-1, effortList.indexOf(e));
		assertEquals(-1, effortFilesList.indexOf(file));
	}
	
	@Test
	void testListProtection()
	{
		// make some effort
		Effort e = new Effort(12, null, null, null, null, 0, null, null, null);
		Effort e2;

		usePassword("generic password");
		writeEffort(e);
		unusePassword();
		assertEquals(false, effortList.isEmpty());
		assertEquals(false, effortFilesList.isEmpty());

		getEffort().clear(); // do something to an external copy
		getEffortFiles().clear(); // do something to an external copy

		// changes to external copy should not affect original
		assertEquals(effortList, getEffort());
		assertNotSame(effortList, getEffort());
		assertEquals(false, effortList.isEmpty());

		assertEquals(effortFilesList, getEffortFiles());
		assertNotSame(effortFilesList, getEffortFiles());
		assertEquals(false, effortFilesList.isEmpty());
	}
	
	@Test
	void testWriteReadReadEffort()
	{
		// make some effort
		Effort e = new Effort(12, null, null, null, null, 0, null, null, null);
		Effort e2;
		Effort e3;

		usePassword("generic password");
		writeEffort(e);
		
		e2 = decryptEffort(effortFilesList.get(effortList.indexOf(e)));
		List<Effort> list1 = getEffort();
		
		assertEquals(e.toString(), e2.toString());
		
		e3 = decryptEffort(effortFilesList.get(effortList.indexOf(e)));
		List<Effort> list2 = getEffort();
		
		assertSame(e2, e3);
		assertNotSame(list1, list2);
		assertEquals(list1, list2);
	}
}
