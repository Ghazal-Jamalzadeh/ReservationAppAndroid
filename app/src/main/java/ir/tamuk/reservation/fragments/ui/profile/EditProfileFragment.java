package ir.tamuk.reservation.fragments.ui.profile;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.List;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.tamuk.reservation.Interfaces.ApiCommunicationInterface;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.activities.ImagePickerActivity;
import ir.tamuk.reservation.api.UploadHelper;
import ir.tamuk.reservation.databinding.FragmentEditProfileBinding;
import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.Photo;
import ir.tamuk.reservation.models.User;
import ir.tamuk.reservation.utils.Connectivity;
import ir.tamuk.reservation.utils.Constants;
import ir.tamuk.reservation.utils.DateTime;
import ir.tamuk.reservation.utils.JsonConverter;
import ir.tamuk.reservation.utils.NullEmptyCheck;
import ir.tamuk.reservation.utils.Tools;

public class EditProfileFragment extends Fragment implements ApiCommunicationInterface {

    private static final String TAG = "EditProfileFragment";
    //binding
    private FragmentEditProfileBinding binding;
    //viewModel
    private ProfileViewModel profileViewModel;
    //other variables
    private User user = new User();
    private String birthdate = "";
    private int REQUEST_IMAGE = 0;
    private boolean isFromOut = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        profileViewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Handle the back button event
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //titles
        binding.firstNameField.title.setText("نام");
        binding.lastNameField.title.setText("نام خانوادگی");
        binding.birthDateField.title.setText("تاریخ تولد");

        //observers
        profileViewModel.getMyProfile().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User userProfile) {

                user = userProfile;

                binding.firstNameField.edt.setText(user.firstName);
                binding.lastNameField.edt.setText(user.lastName);
                if (!NullEmptyCheck.isNullOrEmpty(user.birthday)) {
                    birthdate = user.birthday;
                    binding.birthDateField.txt.setText(DateTime.getPersianDate(birthdate));
                }
                if (!user.photo.filename.equals("")) {
                    Glide.with(getContext()).load(Constants.DOWNLOAD_PHOTO_URL + user.photo.filename)
                            .into(binding.imgProfile);
                }


            }
        });

        profileViewModel.editIsSuccess.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    profileViewModel.getMyProfile();

                    if (profileViewModel.ignoreSuccessMessage) {
                        profileViewModel.ignoreSuccessMessage = false;
                    } else {
                        Tools.showToast(getContext(), "تغییرات با موفقیت ذخیره شد");
                    }
                }
                binding.progressCircular.setVisibility(View.INVISIBLE);
            }
        });

        profileViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {

                binding.progressCircular.setVisibility(View.INVISIBLE);

                if (profileViewModel.ignoreMessage) {
                    profileViewModel.ignoreMessage = false;
                } else {
                    Tools.showToast(getContext(), errorMessage);
                }
            }
        });

        //buttons:
        //save btn
        binding.btnSave.setOnClickListener(view1 -> {
            binding.progressCircular.setVisibility(View.VISIBLE);
            collectUserData();
        });

        //gallery
        binding.profileImageLay.setOnClickListener(view1 -> {
            REQUEST_IMAGE = 100;
            Dexter.withActivity(getActivity())
                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {

                                Log.d(TAG, "onPermissionsChecked: ");
                                Log.d(TAG, "onPermissionsChecked: sdk : " + Build.VERSION.SDK_INT);
                                Log.d(TAG, "onPermissionsChecked: code : " + Build.VERSION_CODES.R);

                                try {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        if (Environment.isExternalStorageManager()) {

                                            Log.d(TAG, "we need extra permission and we have : ");
                                            showImagePickerOptions();

                                        } else {

                                            Log.d(TAG, "we need extra permission but we don't have -> getting permission  ");
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                            Uri uri = Uri.fromParts("package", getActivity().getApplicationContext().getPackageName(), null);
                                            intent.setData(uri);
                                            startActivity(intent);
                                        }
                                    } else {
                                        Log.d(TAG, "we don't need extra permission and we have all we want");
                                        showImagePickerOptions();

                                    }
                                } catch (Exception e) {
                                    Log.d(TAG, "error " + e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                            if (report.isAnyPermissionPermanentlyDenied()) {
                                Log.d(TAG, "denied ");
                                showSettingsDialog();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                            Log.d(TAG, "onPermissionRationaleShouldBeShown: ");
                        }
                    }).check();
        });

        //date picker
        binding.birthDateField.getRoot().setOnClickListener(view1 -> {

            //https://github.com/aliab/Persian-Date-Picker-Dialog
            PersianDatePickerDialog picker = new PersianDatePickerDialog(getContext())
                    .setPositiveButtonString("تایید")
                    .setNegativeButton("انصراف")
                    .setTodayButton("امروز")
                    .setTodayButtonVisible(true)
                    .setTypeFace(getResources().getFont(R.font.iran_sans_mob))
                    .setMinYear(1300)
                    .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                    .setMaxMonth(12)
                    .setInitDate(PersianDatePickerDialog.THIS_YEAR, PersianDatePickerDialog.THIS_MONTH, PersianDatePickerDialog.THIS_DAY)
                    .setActionTextColor(getResources().getColor(R.color.show_more_text))
                    .setTitleColor(getResources().getColor(R.color.login_blue))
                    .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                    .setShowInBottomSheet(true)
                    .setListener(new PersianPickerListener() {
                        @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                        @Override
                        public void onDateSelected(@NotNull PersianPickerDate persianPickerDate) {

                            binding.birthDateField.txt.setText(persianPickerDate.getPersianYear() + "/" + persianPickerDate.getPersianMonth() + "/" + persianPickerDate.getPersianDay());
                            birthdate = DateTime.datePickerGeorgianDateConverter(persianPickerDate.getGregorianYear(), persianPickerDate.getGregorianMonth(), persianPickerDate.getGregorianDay());

                            Log.d(TAG, "onDateSelected: " + birthdate);

                        }

                        @Override
                        public void onDismissed() {

                        }
                    });

            picker.show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        profileViewModel.ignoreMessage = false;
        profileViewModel.ignoreSuccessMessage = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        profileViewModel.ignoreMessage = true;
        profileViewModel.ignoreSuccessMessage = true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_IMAGE == 100) {
            if (requestCode == REQUEST_IMAGE) {
                if (resultCode == RESULT_OK) {

                    Uri uri = data.getParcelableExtra("path");
                    callUploadFile(uri);

                }
            }
        }
    }

    private void collectUserData() {

        BodySubmitCustomer body = new BodySubmitCustomer();
        String firstName = binding.firstNameField.edt.getText().toString().trim();
        String lastName = binding.lastNameField.edt.getText().toString().trim();
        if (NullEmptyCheck.isEmpty(firstName)) {

            Tools.showToast(getContext(), "نام نمی تواند خالی باشد.");
            binding.firstNameField.edt.requestFocus();

        } else if (NullEmptyCheck.isEmpty(lastName)) {

            Tools.showToast(getContext(), "نام خانوادگی نمی تواند خالی باشد.");
            binding.lastNameField.edt.requestFocus();

        } else {
            body.firstName = firstName;
            body.lastName = lastName;

            if (!NullEmptyCheck.isNullOrEmpty(birthdate)) {
                body.birthday = birthdate;
            }

            if (user.photo != null) {
                if (!NullEmptyCheck.isEmpty(user.photo.filename)) {
                    body.photo = user.photo.id;
                }
            }

            //call api
            profileViewModel.editProfile(body);
        }

    }


    //-------------------------Image Picker & Cropper---------------------------------------------->
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getContext(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    private void launchCameraIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        isFromOut = true;
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(getContext(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        isFromOut = true;
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        isFromOut = true;
        startActivityForResult(intent, 101);
    }
    //-------------------------Image Picker & Cropper---------------------------------------------//

    //-----------------------------Upload photo Api------------------------------------------------>
    //error
    public void handleApiErrors(JSONObject response) {

        binding.progressCircularUploadPhoto.setVisibility(View.INVISIBLE);

        try {
            if (response.getInt("code") == 0 && response.getInt("code") >= 500) {
                Tools.showToast(getContext(), getContext().getString(R.string.api_response_error));

            } else {
                Tools.showToast(getContext(), response.getString("result"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //response
    @Override
    public void onResponse(JSONObject response) {

        try {
            switch (response.getString("request")) {

                case Constants.UploadPhoto_URL:
                    if (response.getBoolean("isSuccess")) {

                        binding.progressCircularUploadPhoto.setVisibility(View.INVISIBLE);

                        Photo photo = JsonConverter.convertPhotoJsonToObject(response.getJSONObject("result").getJSONObject("data"));

                        user.photo = photo;

                        if (!user.photo.filename.equals("")) {
                            Glide.with(getContext()).load(Constants.DOWNLOAD_PHOTO_URL + user.photo.filename)
                                    .into(binding.imgProfile);
                        }


                    } else {
                        handleApiErrors(response);
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //call api
    public void callUploadFile(Uri uri) {

        binding.progressCircularUploadPhoto.setVisibility(View.VISIBLE);

        File file = new File(uri.getPath());

        if (Connectivity.isConnected(getContext())) {
            new UploadHelper(getContext(), this).uploadPhoto(file);
        } else {
            Toast.makeText(getContext(), R.string.internet_not_connected_error, Toast.LENGTH_SHORT).show();
            binding.progressCircularUploadPhoto.setVisibility(View.INVISIBLE);
        }
    }
    //-----------------------------Upload photo Api------------------------------------------------>

}
