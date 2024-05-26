package xyz.tirtagt.matthew_kampus.utsmobileapps;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import xyz.tirtagt.matthew_kampus.objects.ExploreItem;
import xyz.tirtagt.matthew_kampus.utsmobileapps.databinding.ListExploreItemBinding;

public class ExploreItemListAdapter extends RecyclerView.Adapter<ExploreItemListAdapter.ExploreItemViewHolder> {
	private ArrayList<ExploreItem> dataset;
	private ExploreItemListAdapter.OnItemSelected onItemSelectedListener;

	public interface OnItemSelected {
		void run(int position, ExploreItem data);
	}

	public static class ExploreItemViewHolder extends RecyclerView.ViewHolder {
		private final ListExploreItemBinding binding;
		private ExploreItemViewHolder.OnClicked onClickListener;

		public interface OnClicked {
			void run();
		}

		public ExploreItemViewHolder(ListExploreItemBinding binding) {
			super(binding.getRoot());
			this.binding = binding;

			this.binding.ExploreItemImage.setOnClickListener(this::onClick);
//			this.binding.ExploreItemTitle.setOnClickListener(this::onClick);
//			this.binding.ExploreItemDescription.setOnClickListener(this::onClick);
		}

		public void setContent(String title, String description, @Nullable Bitmap image) {
			this.binding.ExploreItemTitle.setText(title);

			String text = description.replaceAll("\n\n", " ");
			text = text.replaceAll("\n", "  ");
			int textLength = text.length();

			// Limit how much description is displayed (but with strategic after-word breaking)
			if (textLength > 140) {
				int limit = 140;

				while (limit < textLength && text.charAt(limit) != ' ') {
					limit += 1;
				}

				text = description.substring(0, limit);
				text += "...";
			}

			this.binding.ExploreItemDescription.setText(text);

			this.binding.ExploreItemImage.setImageBitmap(image);
		}

		private void onClick(View view) {
			if (this.onClickListener == null) { return; }

			this.onClickListener.run();
		}

		public void setOnClickListener(ExploreItemViewHolder.OnClicked listener) {
			this.onClickListener = listener;
		}
	}

	public ExploreItemListAdapter(ArrayList<ExploreItem> dataset) {
		this.dataset = dataset;
	}

	@NonNull
	@Override
	public ExploreItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());

		ListExploreItemBinding binding = ListExploreItemBinding.inflate(inflater, parent, false);
		return new ExploreItemViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(@NonNull ExploreItemViewHolder holder, int position) {
		ExploreItem data = this.dataset.get(position);

		holder.setContent(data.Title, data.Description, data.Image);
		holder.setOnClickListener(() -> this.triggerOnItemSelectedListener(position));
	}

	@Override
	public int getItemCount() {
		return this.dataset.size();
	}

	public void setOnItemSelectedListener(OnItemSelected listener) {
		this.onItemSelectedListener = listener;
	}

	private void triggerOnItemSelectedListener(int position) {
		if (this.onItemSelectedListener == null) { return; }

		this.onItemSelectedListener.run(position, dataset.get(position));
	}

	public void setDataset(ArrayList<ExploreItem> NewDataset) {
		this.dataset = NewDataset;
	}
}
