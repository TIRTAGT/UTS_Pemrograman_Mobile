package xyz.tirtagt.matthew_kampus.utsmobileapps;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.MessageFormat;

import xyz.tirtagt.matthew_kampus.objects.ExploreItem;
import xyz.tirtagt.matthew_kampus.utsmobileapps.databinding.FragmentCampusTourSummaryBinding;

public class CampusTourSummaryFragment extends Fragment {
	private FragmentCampusTourSummaryBinding binding;
	private ExploreItem data;

	public CampusTourSummaryFragment() {}
	public CampusTourSummaryFragment(ExploreItem campusData) {
		this.data = campusData;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		this.binding = FragmentCampusTourSummaryBinding.inflate(inflater, container, false);
		return this.binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.binding.ExploreItemImage.setImageBitmap(this.data.Image);
		this.binding.ExploreItemTitle.setText("Summary of your tour to " + this.data.Title);
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
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
