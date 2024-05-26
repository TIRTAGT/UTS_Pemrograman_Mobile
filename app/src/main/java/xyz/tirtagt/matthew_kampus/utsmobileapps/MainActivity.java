package xyz.tirtagt.matthew_kampus.utsmobileapps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import xyz.tirtagt.matthew_kampus.utsmobileapps.databinding.ActivityMainBinding;
import xyz.tirtagt.matthew_kampus.utsmobileapps.utility.StorageUtility;

public class MainActivity extends AppCompatActivity {

	private AppBarConfiguration mAppBarConfiguration;
	private ActivityMainBinding binding;
	private FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		StorageUtility.AppRelativeStorage = getFilesDir().getPath();
		StorageUtility a = new StorageUtility(StorageUtility.GetRelativePath("/data/"));
		if (!a.exists()) { a.CreateDirectory(); }

		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		mAppBarConfiguration = new AppBarConfiguration.Builder().setOpenableLayout(binding.drawerLayout).build();
		fragmentManager = getSupportFragmentManager();
		
		
		OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
		onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				onBackButtonPressed();
			}
		});
		
		initializeLogic();
	}

	public void initializeLogic() {
		binding.drawerLayout.findViewById(R.id.drawerOpenButton).setOnClickListener(v -> binding.drawerLayout.open());
		binding.drawerLayout.findViewById(R.id.drawerCloseButton).setOnClickListener(v -> binding.drawerLayout.close());
		binding.drawerLayout.findViewById(R.id.drawer_goto_explore).setOnClickListener(v -> {
			binding.drawerLayout.close();
			
			// Switch current fragment to explore fragment
			navigateToPrimaryMenus(new ExploreFragment());
		});
		binding.drawerLayout.findViewById(R.id.drawer_goto_profile).setOnClickListener(v -> {
			binding.drawerLayout.close();
			
			// Switch current fragment to profile fragment
			navigateToPrimaryMenus(new ProfileFragment());
		});
		binding.drawerLayout.findViewById(R.id.drawer_goto_settings).setOnClickListener(v -> {
			binding.drawerLayout.close();
			
			// Switch current fragment to settings fragment
			navigateToPrimaryMenus(new SettingsFragment());
		});
		binding.mainProfileIcon.setOnClickListener(v -> {
			// Switch current fragment to profile fragment
			navigateToPrimaryMenus(new ProfileFragment());
		});

		this.fragmentManager.beginTransaction()
			.replace(R.id.MainContainer, new ExploreFragment())
			.setReorderingAllowed(true)
			.addToBackStack("explore")
			.commit();
	}

	@Override
	public boolean onSupportNavigateUp() {
		return super.onSupportNavigateUp();
	}

	private void onBackButtonPressed() {
		DrawerLayout drawer = binding.drawerLayout;

		if (drawer.isOpen()) {
			drawer.close();
			return;
		}

		Fragment currentFragment = this.fragmentManager.findFragmentById(R.id.MainContainer);

		if (currentFragment == null || currentFragment.getClass() == ExploreFragment.class) {
			finish();
			return;
		}

		this.fragmentManager.popBackStack();
	}

	private void navigateToPrimaryMenus(Fragment targetClass) {
		Fragment currentFragment = this.fragmentManager.findFragmentById(R.id.MainContainer);

		if (currentFragment == null) {
			return;
		}

		if (currentFragment.getClass() == targetClass.getClass()) {
			return;
		}

		// If the previous fragment is not ExploreFragment, pop it
		if (currentFragment.getClass() != ExploreFragment.class) {
			this.fragmentManager.popBackStack();
		}

		this.fragmentManager.beginTransaction()
			.replace(R.id.MainContainer, targetClass)
			.setReorderingAllowed(true)
			.addToBackStack(targetClass.getClass().getName())
			.commit();
		
	}

	public void navigateToExtraMenus(Fragment targetClass) {
		Fragment currentFragment = this.fragmentManager.findFragmentById(R.id.MainContainer);

		if (currentFragment == null) { return; }

		if (currentFragment.getClass() == targetClass.getClass()) { return; }

		// Only allow extra fragments if it's the Explore or ItemDetails
		if (currentFragment.getClass() != ExploreFragment.class && currentFragment.getClass() != CampusDetailsFragment.class) {
			return;
		}

		this.fragmentManager.beginTransaction()
			.replace(R.id.MainContainer, targetClass)
			.setReorderingAllowed(true)
			.addToBackStack(targetClass.getClass().getName())
			.commit();
	}
}