package xyz.tirtagt.matthew_kampus.utsmobileapps;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;

import xyz.tirtagt.matthew_kampus.objects.ProfileStruct;
import xyz.tirtagt.matthew_kampus.utility.Logging;
import xyz.tirtagt.matthew_kampus.utsmobileapps.databinding.FragmentProfileBinding;
import xyz.tirtagt.matthew_kampus.utsmobileapps.databinding.FragmentSettingsBinding;
import xyz.tirtagt.matthew_kampus.utsmobileapps.utility.StorageUtility;

public class SettingsFragment extends Fragment {
	private FragmentSettingsBinding binding;
	private StorageUtility StorageFile;
	private Context context;
	private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
	private byte[] TemporaryPictureArray = new byte[0];

	public SettingsFragment() {}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.StorageFile = new StorageUtility(StorageUtility.GetRelativePath("/data/settings.txt"));
		this.context = requireContext();

		pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), this::onProfilePhotoPicked);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		this.binding = FragmentSettingsBinding.inflate(inflater, container, false);
		return this.binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		binding.button.setOnClickListener(v -> this.SaveCurrentInfo());
		binding.profilePicture.setOnClickListener(v -> {
			PickProfilePhoto();
		});

		LoadSavedInfo();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
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
			Logging.warn("SettingsFragment", "Couldn't decode profile data to the structure");
			return;
		}

		binding.profileName.setText(ProfileData.Name);
		binding.profileNim.setText(ProfileData.NIM);

		StorageUtility outputImage = new StorageUtility(StorageUtility.GetRelativePath("/data/profile.raw"));
		if (!outputImage.exists()) { return; }

		binding.profilePicture.setImageBitmap(BitmapFactory.decodeFile(StorageUtility.GetRelativePath("/data/profile.raw")));
	}

	private void SaveCurrentInfo() {
		if (!this.StorageFile.exists()) { this.StorageFile.CreateFile(); }

		if (!this.StorageFile.Open(StorageUtility.FileOpModeEnum.Write)) {
			Logging.error("SettingsFragment", "Couldn't open file for writing !");
			return;
		}

		ProfileStruct ProfileData = new ProfileStruct();
		ProfileData.Name = binding.profileName.getText().toString();
		ProfileData.NIM = binding.profileNim.getText().toString();

		String encoded = ProfileStruct.Encode(ProfileData);
		this.StorageFile.WriteString(0, encoded, Charset.defaultCharset());
		this.StorageFile.Close();
		
		// Save the profile image if there is any
		if (this.TemporaryPictureArray.length == 0) { return; }
		
		StorageUtility outputImage = new StorageUtility(StorageUtility.GetRelativePath("/data/profile.raw"));
		if (!outputImage.exists()) { outputImage.CreateFile(); }
		
		outputImage.Open(StorageUtility.FileOpModeEnum.Write);
		outputImage.WriteBytes(0, this.TemporaryPictureArray);
		outputImage.Close();
	}

	private void PickProfilePhoto() {
		PickVisualMediaRequest.Builder req = new PickVisualMediaRequest.Builder();
		req.setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE);
		pickMedia.launch(req.build());
	}

	private void onProfilePhotoPicked(@Nullable Uri mediaUri) {
		if (mediaUri == null) { return; }

		ContentResolver cr = this.context.getContentResolver();

		if (cr == null) {
			Logging.error("SettingsFragment", "Unable to get ContentResolver for this fragment!");
			return;
		}

		String mimeType = cr.getType(mediaUri);

		if (mimeType == null || !mimeType.startsWith("image/")) {
			Logging.error("SettingsFragment", "Unsupported file type: " + mimeType);
			return;
		}

		try (InputStream inputStream = cr.openInputStream(mediaUri)) {

			if (inputStream == null) {
				throw new IOException("Couldn't open Input Stream");
			}

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int readBytes;
			byte[] buffer = new byte[1024];

			while ((readBytes = inputStream.read(buffer)) > 0) {
				outputStream.write(buffer, 0, readBytes);
			}

			this.TemporaryPictureArray = outputStream.toByteArray();
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}

		binding.profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(this.TemporaryPictureArray, 0, this.TemporaryPictureArray.length));
	}
}