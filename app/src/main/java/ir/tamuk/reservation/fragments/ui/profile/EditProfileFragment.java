package ir.tamuk.reservation.fragments.ui.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
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

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;
import ir.tamuk.reservation.R;
import ir.tamuk.reservation.databinding.FragmentEditProfileBinding;
import ir.tamuk.reservation.fragments.ui.profile.ProfileViewModel;
import ir.tamuk.reservation.models.BodySubmitCustomer;
import ir.tamuk.reservation.models.ResponseUploadFile;
import ir.tamuk.reservation.models.User;
import ir.tamuk.reservation.repository.ProfileRepository;
import ir.tamuk.reservation.utils.DateTime;
import ir.tamuk.reservation.utils.NullEmptyCheck;
import ir.tamuk.reservation.utils.RealPathUtil;
import ir.tamuk.reservation.utils.TokenManager;
import ir.tamuk.reservation.utils.Tools;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
//import retrofit2.Response;
import okhttp3.Response;


public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";
    private FragmentEditProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private String path = "";
    private String birthdate = "";

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

        Log.d(TAG, "access token : " + TokenManager.getAccessToken(getContext()));

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                Navigation.findNavController(view).popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        //titles
        binding.firstNameField.title.setText("نام");
        binding.lastNameField.title.setText("نام خانوادگی");
        binding.birthDateField.title.setText("تاریخ تولد");

        profileViewModel.getMyProfile().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.firstNameField.edt.setText(user.firstName);
                binding.lastNameField.edt.setText(user.lastName);
                if (!NullEmptyCheck.isNullOrEmpty(user.birthday)) {
                    birthdate = user.birthday;
                    binding.birthDateField.txt.setText(getPersianDate(birthdate));
                }
//        if (!user.photo.filename.equals("")){
//            Glide.with(getContext()).load(Constants.DOWNLOAD_PHOTO_URL + user.photo.filename)
//                    .into(binding.imgProfile);
//        }
//        binding.birthDateField.txt.setText(user);

            }
        });

        profileViewModel.editIsSuccess.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    profileViewModel.getMyProfile() ;

                    if (profileViewModel.ignoreSuccessMessage){
                        profileViewModel.ignoreSuccessMessage = false ;
                    }else {
                        Tools.showToast(getContext() , "تغییرات با موفقیت ذخیره شد");
                    }
                }
                binding.progressCircular.setVisibility(View.INVISIBLE);
            }
        });

        profileViewModel.errorMessageLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {

                binding.progressCircular.setVisibility(View.INVISIBLE);

                if (profileViewModel.ignoreMessage){
                    profileViewModel.ignoreMessage = false ;
                }else {
                    Tools.showToast(getContext(), errorMessage);
                }
            }
        });

        binding.btnSave.setOnClickListener(view1 -> {
            binding.progressCircular.setVisibility(View.VISIBLE);
            collectUserData();
        });

        binding.profileImageLay.setOnClickListener(view1 -> {
            Dexter.withActivity(getActivity())
                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
//                                showImagePickerOptions();
                                Log.d(TAG, "all permissions granted");
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, 10);

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
            //--------------------------Date picker - start---------------------------------------//
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        profileViewModel.ignoreMessage =  false ;
        profileViewModel.ignoreSuccessMessage =  false ;
    }

    @Override
    public void onStop() {
        super.onStop();
        profileViewModel.ignoreMessage =  true ;
        profileViewModel.ignoreSuccessMessage =  true ;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            path = RealPathUtil.getRealPath(getContext(), uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            binding.imgProfile.setImageBitmap(bitmap);
            Log.d(TAG, "onActivityResult: api called : " + path);
            callUploadFile(path);
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

        }else {
            body.firstName = firstName ;
            body.lastName = lastName ;
            if (!NullEmptyCheck.isNullOrEmpty(birthdate)){
                body.birthday = birthdate;
            }
            //call api
            profileViewModel.editProfile(body);
        }

    }


    public String getPersianDate(String date) {
        String persianDate = "";
        try {

            persianDate = DateTime.getDate(date, TimeZone.getTimeZone("Asia/Tehran"));

        } catch (Exception e) {

            e.printStackTrace();
        }
        return persianDate;
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
//        isFromOut = true;
        startActivityForResult(intent, 101);
    }

    //retrofit
    private void callUploadFile(String path ) {
        /*create file */
        File file = new File(path);

        /*convert to form data */
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body =  MultipartBody.Part.createFormData("image" , file.getName() , requestFile) ;

        /* call okHttp from source */
        new SendPhoto(getContext() , path).execute();

        /*call okhttp */
//        uploadFileWithOkHttp(requestFile);
//        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));


        /* call api retrofit */
//        ProfileRepository repository = new ProfileRepository() ;
//        Call<ResponseUploadFile> call =  repository.callUploadFile(TokenManager.getAccessToken(getContext()) , filePart) ;
//
//        /* response */
//        call.enqueue(new Callback<ResponseUploadFile>() {
//            @Override
//            public void onResponse(Call<ResponseUploadFile> call, Response<ResponseUploadFile> response) {
//                Log.d(TAG, "onResponse: ");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseUploadFile> call, Throwable t) {
//                Log.d(TAG, "onFailure: ");
//
//            }
//        });
    }

    //okhttp
    public void uploadFileWithOkHttp(RequestBody requestBody) {
        try {
            new Thread(new Runnable() {
                public void run() {
                    // call send message here
                    OkHttpClient client = new OkHttpClient();

                    Request request = new Request.Builder()
                            .url("http://moeenkashisaz.ir/laser/api/v1/upload-file/")
                            .post(requestBody)
                            .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                            .addHeader("authorization", TokenManager.getAccessToken(getContext()))
                            .addHeader("cache-control", "no-cache")
//                .addHeader("postman-token", "9b0da601-c5c1-6548-d285-db439bd3773c")
                            .build();


                    Response response = null;
                    try {
                        response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        String khoob = response.body().string();
                        Log.d(TAG, "upload File With OkHttp : " + khoob);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ok http from source
    public class SendPhoto extends AsyncTask {
        Response response;
        OkHttpClient client;
        Request request;
        String path ;
        private Context context ;

        public SendPhoto(Context context , String path) {
            this.path = path ;
            this.context  = context ;
            Log.d(TAG, "SendPhoto: ");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "onPreExecute: ");
            client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();


            File file = new File(path);

            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("text/csv"), file))
//                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file))
                    .build();

//            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("prefs", MODE_PRIVATE);

            request = new Request.Builder()
                    .addHeader("Authorization", TokenManager.getAccessToken(context))
                    .url("http://moeenkashisaz.ir/laser/api/v1/upload-file/")
//                    .url(Operations.Domain + "api/v1/files/upload")
                    .post(requestBody)
                    .build();

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Log.d(TAG, "doInBackground: ");
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    Log.d(TAG, "doInBackground: " + response.body().string());
                    return response.body().string();
                } else {
                    Log.d(TAG, "doInBackground: not success : " + response);
                    return null;
                }
            } catch (Exception e) {
                Log.d(TAG, "doInBackground: err " + e.getMessage());
            }
            return null;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            try {
                if (o != null && response.isSuccessful()) {
//                    JSONObject jsonObject = new JSONObject(String.valueOf(o)).getJSONObject("data");
//                    receiptImage.id = jsonObject.getString("_id");
//                    receiptImage.fileName = jsonObject.getString("fileName");
//                    Picasso.with(getActivity()).load(Operations.Domain + receiptImage.fileName).into(binding.receiptImageShow);
//                    binding.receiptImageShow.setVisibility(View.VISIBLE);

                }
            } catch (Exception e) {

                Log.d(TAG, "onPostExecute: err " + e.getMessage());
            }

        }
    }

}