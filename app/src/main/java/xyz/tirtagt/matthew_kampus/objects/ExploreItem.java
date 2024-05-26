package xyz.tirtagt.matthew_kampus.objects;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

import xyz.tirtagt.matthew_kampus.utsmobileapps.R;

public class ExploreItem {
	public Bitmap Image;
	public String Title;
	public String Description;

	public static ArrayList<ExploreItem> initializeDummyData(Context context) {
		ArrayList<ExploreItem> a = new ArrayList<>();

		Resources AppRes = context.getResources();

		ExploreItem item1 = new ExploreItem();
		item1.Image = BitmapFactory.decodeResource(AppRes, R.drawable.iti_image);
		item1.Title = "ITI";
		item1.Description = "Institut Teknologi Indonesia (ITI) didirikan atas prakarsa Almarhum Prof. B.J Habibie untuk melengkapi Kawasan Puspiptek dengan fasilitas pendidikan.\n\nPendirian ITI yang dilatarbelakangi oleh kebutuhan insinyur di Indonesia, telah mendorong Persatuan Insinyur Indonesia (PII) pada tahun 1983 untuk mendirikan perguruan tinggi teknik.";
		a.add(item1);

		ExploreItem item2 = new ExploreItem();
		item2.Image = BitmapFactory.decodeResource(AppRes, R.drawable.unpam_image);
		item2.Title = "UNPAM";
		item2.Description = "Universitas Pamulang (UNPAM) didirikan pada tahun 2000 oleh Yayasan Prima Jaya yang dipimpin oleh Drs. Wayan. Namun, karena Yayasan Prima Jaya mengalami kesulitan dalam mengelola sebuah universitas, kepemilikan dan pengelolaannya dialihkan kepada Yayasan Sasmita Jaya pada awal tahun 2005. Dengan pergantian manajemen tersebut, juga berubahlah tujuan Universitas Pamulang.";
		a.add(item2);

		ExploreItem item3 = new ExploreItem();
		item3.Image = BitmapFactory.decodeResource(AppRes, R.drawable.gunadarma_image);
		item3.Title = "Universitas Gunadarma";
		item3.Description = "Pada 7 Agustus 1981 berdiri Program Pendidikan Ilmu Komputer (PPIK) di Jakarta yang tiga tahun kemudian berubah menjadi Sekolah Tinggi Manajemen Informatika dan Komputer (STMIK) Gunadarma. Enam tahun kemudian, tepatnya pada 13 Januari 1990, berdiri Sekolah Tinggi Ilmu Ekonomi Gunadarma (STIE Gunadarma).";
		a.add(item3);

		ExploreItem item4 = new ExploreItem();
		item4.Image = BitmapFactory.decodeResource(AppRes, R.drawable.bsi_image);
		item4.Title = "Bina Sarana Informatika";
		item4.Description = "Universitas Bina Sarana Informatika berawal dari Lembaga Pendidikan Komputer yang bernama LPK BSI yang didirikan sejak 3 Maret 1988. Sejalan dengan perkembangannya, BSI mendirikan Program Pendidikan Siap Kerja yang dinamakan Politeknik BSI pada tahun 1990 dengan membuka program studi Akuntansi Komputer sebagai program studi pertamanya.";
		a.add(item4);

		ExploreItem item5 = new ExploreItem();
		item5.Image = BitmapFactory.decodeResource(AppRes, R.drawable.ui_image);
		item5.Title = "Universitas Indonesia";
		item5.Description = "Cikal-bakal terbentuknya Universitas Indonesia adalah ketika pemerintah kolonial Belanda mendirikan sebuah sekolah yang bertujuan untuk menghasilkan asisten dokter tambahan";
		a.add(item5);

		ExploreItem item6 = new ExploreItem();
		item6.Image = BitmapFactory.decodeResource(AppRes, R.drawable.stmik_stik_image);
		item6.Title = "STMIK STI&K";
		item6.Description = "Sekolah Tinggi Manajemen Informatika dan Komputer Jakarta STI&K berawal dari sebuah sekolah yang bernama Institut Ilmu Komputer (IIK) , yang didirikan pada tahun 1978, lahir Perguruan Tinggi Komputer pertama di Indonesia ini, sebagai gagasan seorang Praktisi Komputer bernama Drs. F. Ameln, SH";
		a.add(item6);

		return a;
	}
}
