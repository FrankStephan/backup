package com.frozen_foo.shuffle_my_music_app.ui;

import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.frozen_foo.shuffle_my_music_app.R;
import com.frozen_foo.shuffle_my_music_app.durations.DurationsAccess;
import com.frozen_foo.shuffle_my_music_app.list_player.Player;
import com.frozen_foo.shuffle_my_music_app.mediaplayer.ListPlayerListener;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest;
import com.frozen_foo.shuffle_my_music_app.permission.PermissionsAccess;
import com.frozen_foo.shuffle_my_music_app.settings.SettingsActivity;
import com.frozen_foo.shuffle_my_music_app.shuffle.ShuffleAccess;
import com.frozen_foo.shuffle_my_music_app.shuffle.progress.ShuffleProgressAccess;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.CreateListController;
import com.frozen_foo.shuffle_my_music_app.ui.create_list.ListCreationListener;
import com.frozen_foo.shuffle_my_music_app.ui.select_favorites.SelectFavoritesController;
import com.frozen_foo.shuffle_my_music_app.ui.show_list.ShowListController;
import com.frozen_foo.shuffle_my_music_app.ui.type_dialog.TypeDialogController;
import com.frozen_foo.shuffle_my_music_app.volume.VolumeMaxController;

import static com.frozen_foo.shuffle_my_music_app.permission.PermissionRequest.READ_EXTERNAL_STORAGE_REQUEST;

public class ShuffleListActivity extends AppCompatActivity {

	//private ListPlayerController listPlayerController;
	private Player listPlayerController;
	private VolumeMaxController volumeMaxController;
	private Mode mode = Mode.NORMAL_MODE_SHOW_LIST;
	private BroadcastReceiver progressUpdater;

	private ListView list() {
		return (ListView) findViewById(R.id.shuffleList);
	}

	private Button button1() {
		return (Button) findViewById(R.id.createShuffleListButton);
	}

	private ToggleButton button2() {
		return (ToggleButton) findViewById(R.id.selectAddFavoritesButton);
	}

	private ProgressBar progressBar() {
		return (ProgressBar) findViewById(R.id.progressBar);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_shuffle_list);
		progressUpdater = new CreateListController()
				.createShuffleProgressReceiver(this, list(), progressBar(), shuffleCompletedListener());
		switch (getMode()) {
			case NORMAL_MODE_SHOW_LIST:
			case NORMAL_MODE_CREATE_LIST:
			case NORMAL_MODE_SELECT_FAVORITES:
				list().setBackgroundResource(R.color.holo_blue_dark);
				break;
			case FAVORITES_MODE_SHOW_LIST:
				list().setBackgroundResource(R.color.holo_purple);
				break;
		}
	}

	@NonNull
	private ListCreationListener shuffleCompletedListener() {
		return new ListCreationListener() {
			@Override
			public void onComplete() {
				synchronized (ShuffleListActivity.this) {
					changeMode(Mode.NORMAL_MODE_SHOW_LIST);
					listPlayerController.createNewListFinished(ShuffleListActivity.this);
					button1().setEnabled(true);
				}
			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menubar, menu);
		Object o = menu.findItem(R.id.play_pause);

		listPlayerController = new Player(markPlayingSongListener(menu));
		loadList();
		// TODO: Music
		//listPlayerController.initPlayer(this, list(), menu.findItem(R.id.play_pause), markPlayingSongListener());
		listPlayerController.appStart(this, list());
		volumeMaxController = new VolumeMaxController();
		volumeMaxController.init(this, menu.findItem(R.id.volume_max), progressBar());


		new CreateListController().registerShuffleProgressReceiver(this, progressUpdater);
		return true;
	}

	private ListPlayerListener markPlayingSongListener(Menu menu) {
		final MenuItem playItem = menu.findItem(R.id.play_pause);

		return new ListPlayerListener() {

			@Override
			public void onStart() {
				playItem.setIcon(R.drawable.ic_pause_black_24dp);
			}

			@Override
			public void onPause() {
				playItem.setIcon(R.drawable.ic_play_arrow_black_24dp);
			}

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(ShuffleListActivity.this, "MediaPlayer Error " + what + " " + extra, Toast.LENGTH_LONG)
						.show();
				return false;
			}

			@Override
			public void playingSongChanged(final int index) {
				new ShowListController().markAsPlayingSong(ShuffleListActivity.this, list(), index);
			}
		};

	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				openSettings();
				return true;
			case R.id.play_pause:
				playPause();
				return true;
			case R.id.reload:
				reload();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void openSettings() {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	private void playPause() {
		listPlayerController.playPause();
	}

	public void volumeMax(View view) {
		boolean selected = ((ToggleButton) view).isChecked();
		listPlayerController.setVolumeMax(selected);
	}

	private void loadList() {
		if (new PermissionsAccess().hasPermission(this, READ_EXTERNAL_STORAGE_REQUEST)) {
			_loadList();
		} else {
			new PermissionsAccess().requestPermission(this, READ_EXTERNAL_STORAGE_REQUEST);
		}
	}

	private void _loadList() {
		updateListAttributesWhenSettingsChanged();
		new ShowListController().loadAndInflateList(this, list(), progressBar(), shuffleCompletedListener());
	}

	private void updateListAttributesWhenSettingsChanged() {
		if (getIntent().getBooleanExtra(SettingsActivity.SETTINGS_CHANGED_FLAG, false)) {
			getIntent().removeExtra(SettingsActivity.SETTINGS_CHANGED_FLAG);
			listPlayerController.userChangedSettings(this);
			new ShuffleProgressAccess(this).updateProgress(null, new ShuffleAccess().getLocalIndex(this).size());
			new DurationsAccess(this).updateForAllSongs();

		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		PermissionRequest permissionRequest = new PermissionsAccess().forRequestCode(requestCode);
		if (new PermissionsAccess().hasPermission(this, permissionRequest)) {
			switch (permissionRequest) {
				case READ_EXTERNAL_STORAGE_REQUEST:
					_loadList();
					break;
				case INTERNET_REQUEST:
					confirmCreateShuffleList();
					break;
				default:
					break;
			}
		}
	}

	private void confirmCreateShuffleList() {
		final TypeDialogController typeDialogController = new TypeDialogController();
		typeDialogController.showDialog(this, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(final DialogInterface dialog, final int which) {
				switch (typeDialogController.getTypeResult()) {
					case NEW_SHUFFLE_LIST:
						createNewShuffleList(false, typeDialogController.getNumberOfNewSongs());
						break;
					case ALL_FAVORITES_LIST:
						createAllFavoritesList();
						break;
				}
			}
		});
	}

	private void createNewShuffleList(boolean useExistingList, final int numberOfSongs) {
		button1().setEnabled(false);
		listPlayerController.createNewListStarted(this);
		ProgressBar progressBar = progressBar();
		changeMode(Mode.NORMAL_MODE_CREATE_LIST);
		new CreateListController().createShuffleList(this, progressBar, numberOfSongs, useExistingList);
	}

	private void createAllFavoritesList() {
		button1().setEnabled(false);
		listPlayerController.createNewListStarted(this);
		ProgressBar progressBar = progressBar();
		changeMode(Mode.FAVORITES_MODE_CREATE_LIST);
		new CreateListController().createAllFavoritesList(this, progressBar);
	}

	@Override
	public boolean onKeyDown(final int keyCode, final KeyEvent event) {
		boolean isConsumed = listPlayerController.onKeyDown(keyCode, event);
		if (!isConsumed) {
			return super.onKeyDown(keyCode, event);
		} else {
			return true;
		}
	}

	@Override
	public boolean onKeyUp(final int keyCode, final KeyEvent event) {
		boolean isConsumed = listPlayerController.onKeyUp(keyCode, event);
		if (!isConsumed) {
			return super.onKeyUp(keyCode, event);
		} else {
			return true;
		}
	}

	public void pressButton1(View view) {
		switch (mode) {
			case NORMAL_MODE_CREATE_LIST:
				// Should not happen, cause button is disabled
				// So do nothing - new list is being created already
				break;
			case NORMAL_MODE_SHOW_LIST:
				if (new PermissionsAccess().hasPermission(this, PermissionRequest.INTERNET_REQUEST,
						PermissionRequest.WRITE_EXTERNAL_STORAGE_REQUEST)) {
					confirmCreateShuffleList();
				} else {
					new PermissionsAccess().requestPermission(this, PermissionRequest.INTERNET_REQUEST,
							PermissionRequest.WRITE_EXTERNAL_STORAGE_REQUEST);
				}
				break;
			case NORMAL_MODE_SELECT_FAVORITES:
				button1().setText(R.string.createShuffledList);
				button1().setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
				button2().setChecked(false);
				button2().setEnabled(true);
				cancelFavoritesSelection();
				break;
		}
	}

	public void pressButton2(View view) {
		final ListView     shuffleList = list();
		final ToggleButton button2     = button2();
		switch (mode) {
			case NORMAL_MODE_SHOW_LIST:
				button1().setText(R.string.cancel);
				button1().setBackgroundTintList(ColorStateList.valueOf(Color.RED));
				selectFavorites(shuffleList, button2);
				break;
			case NORMAL_MODE_SELECT_FAVORITES:
				button1().setText(R.string.createShuffledList);
				button1().setBackgroundTintList(ColorStateList.valueOf(Color.LTGRAY));
				markSelectedFavorites(shuffleList);
				break;
		}
	}

	private void reload() {
		int numberOfSongs = list().getAdapter().getCount();
		createNewShuffleList(true, numberOfSongs);
	}

	private void selectFavorites(final ListView shuffleList, final ToggleButton button2) {
		final SelectFavoritesController selectFavoritesController = new SelectFavoritesController();
		selectFavoritesController.selectFavorites(this, shuffleList);
		changeMode(Mode.NORMAL_MODE_SELECT_FAVORITES);
	}

	private void markSelectedFavorites(final ListView shuffleList) {
		new SelectFavoritesController().markSelectedFavorites(this, shuffleList);
		changeMode(Mode.NORMAL_MODE_SHOW_LIST);
	}

	private void cancelFavoritesSelection() {
		new SelectFavoritesController().cancelFavoritesSelection(this, list());
		changeMode(Mode.NORMAL_MODE_SHOW_LIST);
	}

	private void changeMode(Mode mode) {
		this.mode = mode;
	}

	private Mode getMode() {
		return mode;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	protected void onDestroy() {
		new CreateListController().unregisterShuffleProgressReceiver(this, progressUpdater);
		listPlayerController.appShutDown(this);
		volumeMaxController.release(this);

		super.onDestroy();
	}
}
