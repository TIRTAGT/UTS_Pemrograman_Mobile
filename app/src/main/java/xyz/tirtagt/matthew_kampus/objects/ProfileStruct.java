package xyz.tirtagt.matthew_kampus.objects;

public class ProfileStruct {
	public String Name = "";
	public String NIM = "";

	public static ProfileStruct Decode(String data) {
		String[] a = data.split(",");

		if (a.length != 2) { return null; }

		ProfileStruct obj = new ProfileStruct();
		obj.Name = a[0];
		obj.NIM = a[1];

		return obj;
	}

	public static String Encode(ProfileStruct data) {
		return data.Name + "," + data.NIM;
	}
}
