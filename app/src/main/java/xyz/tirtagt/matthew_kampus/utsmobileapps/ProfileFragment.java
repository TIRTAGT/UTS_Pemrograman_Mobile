package xyz.tirtagt.matthew_kampus.utsmobileapps;

import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.nio.charset.Charset;

import xyz.tirtagt.matthew_kampus.objects.ProfileStruct;
import xyz.tirtagt.matthew_kampus.utility.Logging;
import xyz.tirtagt.matthew_kampus.utsmobileapps.databinding.FragmentProfileBinding;
import xyz.tirtagt.matthew_kampus.utsmobileapps.utility.StorageUtility;

public class ProfileFragment extends Fragment {
	private FragmentProfileBinding binding;
	private StorageUtility StorageFile;

	public ProfileFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.StorageFile = new StorageUtility(StorageUtility.GetRelativePath("/data/settings.txt"));
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		this.binding = FragmentProfileBinding.inflate(inflater, container, false);
		return this.binding.getRoot();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		LoadSavedInfo();
	}

	@Override
	public void onStop() {
		super.onStop();

		if (this.StorageFile.GetCurrentMode() != StorageUtility.InternalOperationStatus.Unspecified) {
			this.StorageFile.Close();
		}
	}

	@Override
	public void onDestroy() {
		this.StorageFile.Dispose();
		super.onDestroy();
	}

	private void LoadSavedInfo() {
		if (!this.StorageFile.exists()) { return; }

		this.StorageFile.Open(StorageUtility.FileOpModeEnum.Read);

		int FileSize = (int) this.StorageFile.GetFileSize();

		if (FileSize < 1) { return; }

		String a = this.StorageFile.ReadString(0, FileSize, Charset.defaultCharset());
		this.StorageFile.Close();

		ProfileStruct ProfileData = ProfileStruct.Decode(a);

		if (ProfileData == null) {
			Logging.warn("ProfileFragment", "Couldn't decode profile data to the structure");
			return;
		}

		binding.profileName.setText(ProfileData.Name);
		binding.profileNim.setText(ProfileData.NIM);

		StorageUtility outputImage = new StorageUtility(StorageUtility.GetRelativePath("/data/profile.raw"));
		if (!outputImage.exists()) { return; }

		binding.profilePicture.setImageBitmap(BitmapFactory.decodeFile(StorageUtility.GetRelativePath("/data/profile.raw")));
	}
}