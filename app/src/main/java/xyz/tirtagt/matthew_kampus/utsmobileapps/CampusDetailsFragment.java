package xyz.tirtagt.matthew_kampus.utsmobileapps;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xyz.tirtagt.matthew_kampus.objects.ExploreItem;
import xyz.tirtagt.matthew_kampus.utility.Logging;
import xyz.tirtagt.matthew_kampus.utsmobileapps.databinding.FragmentCampusDetailsBinding;

public class CampusDetailsFragment extends Fragment {
	private FragmentCampusDetailsBinding binding;
	private ExploreItem data;

	public CampusDetailsFragment() {}
	public CampusDetailsFragment(ExploreItem campusData) {
		this.data = campusData;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		this.binding = FragmentCampusDetailsBinding.inflate(inflater, container, false);
		return this.binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		this.binding.ExploreItemImage.setImageBitmap(this.data.Image);
		this.binding.ExploreItemTitle.setText(this.data.Title);
		this.binding.ExploreItemDescription.setText(this.data.Description);
		this.binding.goTourThisCampus.setOnClickListener(v -> LaunchSummary());
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

	private void LaunchSummary() {
		MainActivity ParentActivity = (MainActivity) getActivity();

		if (ParentActivity == null) {
			Logging.error("CampusDetailsFragment", "Unable to get ParentActivity !");
			return;
		}

		CampusTourSummaryFragment a = new CampusTourSummaryFragment(this.data);
		ParentActivity.navigateToExtraMenus(a);
	}
}
