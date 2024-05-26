package xyz.tirtagt.matthew_kampus.utsmobileapps.utility;

import android.util.NoSuchPropertyException;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

public class StorageUtility {
	public static String AppRelativeStorage = "";
	private File FileInstance = null;
	private FileInputStream FReaderInstance = null;
	private FileChannel FChannelInstance = null;
	private FileOutputStream FWriterInstance = null;
	public enum FileOpModeEnum {
		Read, Write
	}

	public enum InternalOperationStatus {
		Read, Write, Unspecified
	}

	public class IllegalDuplexOperation extends Exception {}

	/**
	 * Buat instance baru untuk StorageUtility
	 * @param Path lokasi penyimpanan
	 */
	public StorageUtility(@Nullable String Path) {
		if (Path != null) {
			this.FileInstance = new File(Path);
		}
	}

	/**
	 * Dapatkan tempat penyimpanan aplikasi dari global AppRelativeStorage
	 * @param Path lokasi tambahan
	 * @return Nilai AppRelativeStorage + lokasi tambahan
	 */
	public static String GetRelativePath(@Nullable String Path) {
		if (AppRelativeStorage.length() == 0) {
			throw new NoSuchPropertyException("AppRelativeStorage belum diatur oleh activity !");
		}

		if (Path == null) {
			Path = "/";
		}

		if (!Path.startsWith("/")) {
			Path = "/".concat(Path);
		}

		return AppRelativeStorage.concat(Path);
	}

	/**
	 * Cek apakah lokasi yang saat ini digunakan ada atau tidak
	 * @return True jika ada, false jika tidak ada
	 */
	public boolean exists() {
		if (this.FileInstance == null) {
			return false;
		}

		return this.FileInstance.exists();
	}

	/**
	 * Cek ukuran file
	 * @return Ukuran file atau -1 jika tidak dapat diperiksa
	 */
	public long GetFileSize() {
		boolean IsTemporarilyOpened = false;

		// Check if we should open a new reader instance
		if (this.FReaderInstance == null) {
			try {
				if (!this.OpenRead()) {
					// Failed to open
					return -1;
				}

				IsTemporarilyOpened = true;
			}
			catch (IllegalDuplexOperation ignored) { return -1; }
		}

		long FileSize = 0;

		try {
			FileSize = this.FChannelInstance.size();
		}
		catch (IOException e) {
			FileSize = -1;
		}

		if (IsTemporarilyOpened) {
			// Close again since we're done.
			this.CloseReading();
		}

		return FileSize;
	}

	private boolean OpenRead() throws IllegalDuplexOperation {
		if (!this.FileInstance.exists() || !this.FileInstance.isFile()) {
			return false;
		}

		if (!this.FileInstance.canRead()) {
			return false;
		}

		// If we are already writing, throw Error
		if (this.FWriterInstance != null) {
			throw new IllegalDuplexOperation();
		}

		try {
			this.FReaderInstance = new FileInputStream(this.FileInstance);
			this.FChannelInstance = this.FReaderInstance.getChannel();
			return true;
		}
		catch (FileNotFoundException e) {
			this.FReaderInstance = null;
			return false;
		}
	}

	private boolean OpenWrite() throws IllegalDuplexOperation {
		if (!this.FileInstance.exists()) {
			if (!this.CreateFile()) {
				return false;
			}
		}

		if (!this.FileInstance.isFile()) {
			return false;
		}

		if (!this.FileInstance.canWrite()) {
			return false;
		}

		// If we are already reading, throw Error
		if (this.FReaderInstance != null) {
			throw new IllegalDuplexOperation();
		}

		try {
			this.FWriterInstance = new FileOutputStream(this.FileInstance);
			this.FChannelInstance = this.FWriterInstance.getChannel();
			return true;
		}
		catch (FileNotFoundException e) {
			this.FWriterInstance = null;
			return false;
		}
	}

	public boolean CreateFile() {
		if (this.FileInstance == null || this.FileInstance.exists()) {
			return false;
		}

		try {
			return this.FileInstance.createNewFile();
		}
		catch (IOException e) {
			return false;
		}
	}

	public boolean CreateDirectory() {
		if (this.FileInstance == null || this.FileInstance.exists()) {
			return false;
		}

		try {
			return this.FileInstance.mkdir();
		}
		catch (SecurityException e) {
			return false;
		}
	}

	public boolean DeleteFile() {
		if (this.FileInstance == null || !this.FileInstance.exists()) {
			return false;
		}

		if (!this.FileInstance.isFile()) {
			return false;
		}

		try {
			return this.FileInstance.delete();
		}
		catch (SecurityException e) {
			return false;
		}
	}

	public boolean DeleteDirectory() {
		if (this.FileInstance == null || !this.FileInstance.exists()) {
			return false;
		}

		if (!this.FileInstance.isDirectory()) {
			return false;
		}

		try {
			return this.FileInstance.delete();
		}
		catch (SecurityException e) {
			return false;
		}
	}

	public boolean Delete() {
		if (this.FileInstance == null || !this.FileInstance.exists()) {
			return false;
		}

		try {
			return this.FileInstance.delete();
		}
		catch (SecurityException e) {
			return false;
		}
	}

	private boolean SeekFile(int position) {
		if (this.FReaderInstance == null || this.FChannelInstance == null) { return false; }

		// Make sure the position is not larger than the file size
		long FileSize = -1;

		try {
			FileSize = this.FChannelInstance.size();
		}
		catch (IOException e) {
			return false;
		}

		if (position > FileSize) {
			return false;
		}

		try {
			this.FChannelInstance.position(position);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	public boolean Open(FileOpModeEnum Mode) {
		if (Mode == FileOpModeEnum.Read) {
			try {
				return this.OpenRead();
			}
			catch (IllegalDuplexOperation ignored) { return false; }
		}
		else if (Mode == FileOpModeEnum.Write) {
			try {
				return this.OpenWrite();
			}
			catch (IllegalDuplexOperation ignored) { return false; }
		}

		return false;
	}

	public byte[] ReadBytes(int startpos, int length) {
		// Make sure the reader instance exists
		if (this.FReaderInstance == null) {
			try {
				if (!this.OpenRead()) {
					// Failed to open, return empty.
					return new byte[length];
				}
			}
			catch (IllegalDuplexOperation ignored) { return new byte[length]; }
		}

		byte[] buffer = new byte[length];

		try {
			this.SeekFile(startpos);
			this.FReaderInstance.read(buffer, 0, length);
		}
		catch (IOException e) {
			return new byte[length];
		}

		return buffer;
	}

	public String ReadString(int startpos, int length, Charset charset) {
		byte[] FileByte = this.ReadBytes(startpos, length);

		return new String(FileByte, charset);
	}

	public void CloseReading() {
		// Make sure the reader instance exists
		if (this.FReaderInstance == null) {
			return;
		}

		try {
			this.FReaderInstance.close();
		}
		catch (IOException ignored) { }
		finally {
			this.FReaderInstance = null;
		}
	}

	public int WriteBytes(int StartPosition, byte[] data) {
		// Make sure the writer instance exists
		if (this.FWriterInstance == null) {
			try {
				if (!this.OpenWrite()) {
					// Failed to open, return -1.
					return -1;
				}
			}
			catch (IllegalDuplexOperation ignored) { return -1; }
		}

		try {
			this.SeekFile(StartPosition);
			this.FWriterInstance.write(data, 0, data.length);
		}
		catch (IOException e) {
			return -1;
		}

		return data.length;
	}

	public int WriteString(int StartPosition, String data, Charset charset) {
		byte[] StringByte = data.getBytes(charset);

		return this.WriteBytes(StartPosition, StringByte);
	}

	public void CloseWriting() {
		// Make sure the writer instance exists
		if (this.FWriterInstance == null) {
			return;
		}

		try {
			this.FWriterInstance.flush();
			this.FWriterInstance.close();
		}
		catch (IOException ignored) { }
		finally {
			this.FWriterInstance = null;
		}
	}

	public void Close() {
		if (this.FWriterInstance != null) {
			this.CloseWriting();
		}

		if (this.FReaderInstance != null) {
			this.CloseReading();
		}
	}

	public InternalOperationStatus GetCurrentMode() {
		if (this.FWriterInstance != null) {
			return InternalOperationStatus.Write;
		}

		if (this.FReaderInstance != null) {
			return InternalOperationStatus.Read;
		}

		return InternalOperationStatus.Unspecified;
	}

	/**
	 * Tampilkan item pada lokasi saat ini (direktori)
	 * @return Kumpulan nama-nama item pada lokasi saat ini
	 */
	public String[] ListItems() {
		return this.FileInstance.list();
	}

	public void Dispose() {
		this.Close();

		this.FileInstance = null;
		this.FChannelInstance = null;
		this.FWriterInstance = null;
		this.FReaderInstance = null;
	}

	/**
	 * Paksa tutup lokasi saat ini, kemudian ganti ke lokasi penyimpanan baru
	 * @param Path lokasi penyimpanan baru
	 */
	public void SetPath(@Nullable String Path) {
		this.Dispose();

		if (Path != null) {
			this.FileInstance = new File(Path);
		}
	}
}
