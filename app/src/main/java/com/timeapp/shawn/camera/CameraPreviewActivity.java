package com.timeapp.shawn.camera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.timeapp.shawn.camera.preview.ChangbaRecordingPreviewScheduler;
import com.timeapp.shawn.camera.preview.ChangbaRecordingPreviewView;
import com.timeapp.shawn.camera.preview.ChangbaVideoCamera;
import com.timeapp.shawn.camera.utils.LogUtils;

public class CameraPreviewActivity extends Activity {

	private RelativeLayout recordScreen;
	private ChangbaRecordingPreviewView surfaceView;
	private ChangbaVideoCamera videoCamera;
	private ChangbaRecordingPreviewScheduler previewScheduler;

	private ImageView switchCameraBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_preview);
		permissionCheck();
	}

	private void bindListener() {
		switchCameraBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				previewScheduler.switchCameraFacing();
			}
		});
	}

	private void findView() {
		recordScreen = (RelativeLayout) findViewById(R.id.recordscreen);
		switchCameraBtn = (ImageView) findViewById(R.id.img_switch_camera);
		surfaceView = new ChangbaRecordingPreviewView(this);
		recordScreen.addView(surfaceView, 0);
		surfaceView.getLayoutParams().width = getWindowManager().getDefaultDisplay().getWidth();
		surfaceView.getLayoutParams().height = getWindowManager().getDefaultDisplay().getWidth();
	}

	private void initCameraResource() {
		videoCamera = new ChangbaVideoCamera(this);
		previewScheduler = new ChangbaRecordingPreviewScheduler(surfaceView, videoCamera) {
			public void onPermissionDismiss(final String tip) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(CameraPreviewActivity.this, tip, Toast.LENGTH_SHORT).show();
					}
				});
			}
		};
	}

	private void beginRecording() {
		findView();
		bindListener();
		initCameraResource();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
		}
	};

	private final int PERMISSION_REQUEST_CODE = 0x101;
	private static final String[] permissionManifest = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA,
			Manifest.permission.RECORD_AUDIO
	};

	private void permissionCheck() {
		LogUtils.e("Permission", "permissionCheck begin");
		if (Build.VERSION.SDK_INT >= 23) {

			LogUtils.e("Permission", "SDK_INT >= 23");
			boolean permissionState = true;
			for (String permission : permissionManifest) {
				if (ContextCompat.checkSelfPermission(this, permission)
						!= PackageManager.PERMISSION_GRANTED) {
					permissionState = false;
				}
			}
			if (!permissionState) {
				LogUtils.e("Permission", "permissionState false");
				ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
			} else {
				LogUtils.e("Permission", "permissionState true");
				beginRecording();
			}
		} else {
			LogUtils.e("Permission", "SDK_INT < 23");

			beginRecording();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_REQUEST_CODE) {
			boolean isGrant = true;

			LogUtils.e("Permission", "onRequestPermissionsResult");

			for (int i = 0; i < permissions.length; i++) {
				LogUtils.e("Video", "permission: " + permissions[i] + " = " + grantResults[i]);
				if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
					isGrant = false;
				}
			}
			if (isGrant) {
				beginRecording();
			}
		}
	}
}
