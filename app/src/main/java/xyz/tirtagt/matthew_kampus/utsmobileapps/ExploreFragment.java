package xyz.tirtagt.matthew_kampus.utsmobileapps;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import xyz.tirtagt.matthew_kampus.objects.ExploreItem;
import xyz.tirtagt.matthew_kampus.utsmobileapps.databinding.FragmentExploreBinding;

public class ExploreFragment extends Fragment {
	private FragmentExploreBinding binding;
	private ExploreItemListAdapter ExploreItemAdapter;
	private Context context;
	private MainActivity ParentActivity;
	private ArrayList<ExploreItem> OriginalDataset;
	private ArrayList<ExploreItem> FilteredDataset;

	public ExploreFragment() {}

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);

		this.context = context;
		this.ParentActivity = (MainActivity) getActivity();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.OriginalDataset = ExploreItem.initializeDummyData(this.context);
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		this.binding = FragmentExploreBinding.inflate(inflater, container, false);
		return this.binding.getRoot();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// TODO: Because this is just for UTS, I will just use placeholder data here
		this.ExploreItemAdapter = new ExploreItemListAdapter(this.OriginalDataset);
		this.ExploreItemAdapter.setOnItemSelectedListener(this::LaunchItemDetails);
		this.binding.exploreItemsRecyclerview.setLayoutManager(new LinearLayoutManager(this.context));
		this.binding.exploreItemsRecyclerview.setAdapter(ExploreItemAdapter);

		this.binding.editTextText2.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				FilterItemsByUserInput(s.toString());
			}
		});
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

	private void LaunchItemDetails(int position, ExploreItem item) {
		CampusDetailsFragment a = new CampusDetailsFragment(item);

		this.ParentActivity.navigateToExtraMenus(a);
	}

	private void FilterItemsByUserInput(String UserInput) {
		if (UserInput.isEmpty()) {
			// This is not efficient, but good enough for UTS
			this.ExploreItemAdapter.setDataset(this.OriginalDataset);
			this.ExploreItemAdapter.notifyDataSetChanged();
			return;
		}

		UserInput = UserInput.toLowerCase();

		ArrayList<ExploreItem> a = new ArrayList<>();

		for (ExploreItem item: this.OriginalDataset) {
			if (item.Title.toLowerCase().contains(UserInput)) {
				a.add(item);
			}
		}

		this.FilteredDataset = a;

		// This is not efficient, but good enough for UTS.
		this.ExploreItemAdapter.setDataset(this.FilteredDataset);
		this.ExploreItemAdapter.notifyDataSetChanged();
	}
}