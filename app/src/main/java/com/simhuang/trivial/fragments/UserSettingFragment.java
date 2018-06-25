package com.simhuang.trivial.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.simhuang.trivial.R;
import com.simhuang.trivial.model.User;
import com.simhuang.trivial.model.UserTag;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class UserSettingFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private TextView editImageTextView;
    private ImageView profileImageView;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private Uri imageFileURI;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        editImageTextView = view.findViewById(R.id.edit_image_text);
        profileImageView = view.findViewById(R.id.profile_image);
        editImageTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

        showExistingData();

        return view;
    }

    /**
     * Retrieve all user setting from settings
     */
    public void showExistingData() {
        mDatabase.child("UserTag").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UserTag userTag = snapshot.getValue(UserTag.class);
                    if(userTag.getUid().equals(firebaseAuth.getCurrentUser().getUid())) {
                        if(userTag.getImageURL() != null) {
                            Picasso.get().load(userTag.getImageURL()).resize(100,100).centerCrop().into(profileImageView);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //ignore
            }
        });
    }

    public void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        MenuInflater menuInflater = popupMenu.getMenuInflater();
        menuInflater.inflate(R.menu.edit_image_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()) {

                    case R.id.camera_option: //take photo
                        takePhoto(); //TODO:There is a bug with this code requires refactoring
                        return true;

                    case R.id.gallery_option: //choose image from gallery
                        choosePhotoFromGallery();
                        return true;

                    default:
                        return false;

                }
            }
        });

        popupMenu.show();
    }

    /**
     * This method allows use to choose a photo from gallery and upload it to firebase
     */
    public void choosePhotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Fragment settingFragment = this;
        settingFragment.startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    /**
     * This method will invoke an existing camera application to take the photo so permission is not needed.
     */
    public void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePhotoIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //TODO:REQUIRE IMPLEMENTATION

        }else if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if(data != null) {
                imageFileURI = data.getData();
                uploadImagetoFirebase();
            }
        }
    }

    /**
     * Upload the image to firebase storage
     */
    public void uploadImagetoFirebase() {
        final StorageReference filepath = storageRef.child(firebaseAuth.getUid()).child(System.currentTimeMillis() + "." + getFileExtension(imageFileURI));
        UploadTask uploadTask = filepath.putFile(imageFileURI);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if(!task.isSuccessful()) {
                    throw task.getException();
                }
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(getContext(), "Image Successfully Changed", Toast.LENGTH_SHORT).show();
                    Picasso.get().load(imageFileURI).resize(50,50).centerCrop().into(profileImageView);
                    String downloadURL = task.getResult().toString();
                    saveImageURItoDB(downloadURL);
                }
            }
        });
    }

    /**
     * Store the image uri to firebase database so it can be retrieved
     * for the specific user in the future
     */
    public void saveImageURItoDB(final String downloadurl) {
        //user image url to "User" node
        mDatabase.child("Users").child(firebaseAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if(currentUser != null) {
                    currentUser.setImageURI(downloadurl);
                }

                mDatabase.child("Users").child(firebaseAuth.getUid()).setValue(currentUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //ignore
            }
        });

        //save image to "UserTag" node
        mDatabase.child("UserTag").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserTag> userTagList = new ArrayList<>();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UserTag tag = snapshot.getValue(UserTag.class);
                    if(tag.getUid().equals(firebaseAuth.getCurrentUser().getUid())) {
                        tag.setImageUR(downloadurl);
                    }
                    userTagList.add(tag);
                }
                mDatabase.child("UserTag").setValue(userTagList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //ignore
            }
        });
    }

    /**
     * Helper method to retrieve the extension of the captured image
     */
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


}
