package com.example.compendiumofmateriamedica.ui.profile;


import static model.UtilsApp.generateRandomString;
import static model.UtilsApp.loadImageFromURL;

import android.Manifest;


import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.compendiumofmateriamedica.R;
import com.example.compendiumofmateriamedica.databinding.FragmentProfileBinding;
import com.example.compendiumofmateriamedica.ui.social.PhotoDialogFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import model.Datastructure.NewEventHandler;
import model.Datastructure.PostTreeManager;
import model.Datastructure.User;
import model.Datastructure.UserTreeManager;

/**
 * @author: Xing Chen, Tianhao Shan
 * @uid: u7725171, u7709429
 * @description: Show user's profile, messages and settings entrance
 */
public class ProfileFragment extends Fragment implements NewEventHandler.EventObserver{

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;
    private User currentUser;
    private NewEventHandler eventHandler;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView user_location;
    private static final long NOTIFICATION_UPDATE_INTERVAL = 5000; // 通知状态5秒更新一次
    private TextView notificationCountTextView;
    private Handler handler;
    private Runnable notificationUpdateRunnable;
    private int plantsUserDiscovered;
    private PostTreeManager postTreeManager;
    private ImageView userAvatar;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Initialize PostTreeManager
        postTreeManager = PostTreeManager.getInstance();

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = binding.getRoot();

        currentUser = (User) getActivity().getIntent().getSerializableExtra("User");
        eventHandler = NewEventHandler.getInstance();

        // user avatar
        userAvatar = binding.profileUserAvatar;
        loadImageFromURL(getContext(), UserTreeManager.getInstance().findUserById(currentUser.getUser_id()).getAvatar_url(), userAvatar, "Avatar");
        // click on avatar will show big picture
        userAvatar.setOnClickListener(v -> {
            PhotoDialogFragment photoDialogFragment = PhotoDialogFragment.newInstance(currentUser.getAvatar_url());
            photoDialogFragment.show(getParentFragmentManager(), "avatar");
        });

        // user name
        TextView user_name=binding.profileUsername;
        mViewModel.updateUserName(currentUser.getUsername());
        mViewModel.getUserName().observe(getViewLifecycleOwner(), user_name::setText);

        // user location
        user_location = binding.profileUserLocation;

        // how many plant user has discovered
        plantsUserDiscovered = postTreeManager.getUserPlantDiscovered(currentUser.getUser_id()).size();
        // display user level with icon
        ImageView  userLevel = binding.profileUserLevel;
        setUserLevelImage(userLevel, plantsUserDiscovered);
        // progress bar
        ProgressBar progressBar = binding.profileProgressBar;
        TextView progressText = binding.profileProgress;

        int maxProgress = getMaxProgress(plantsUserDiscovered);

        if (plantsUserDiscovered >= 60) {
            progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(), R.drawable.progress_bar_gold));
            progressBar.setProgress(maxProgress); // maximize progress
            progressBar.setMax(maxProgress);
        } else {
            progressBar.setProgressDrawable(ContextCompat.getDrawable(getContext(), R.drawable.progress_bar_green));
            progressBar.setMax(maxProgress);
            progressBar.setProgress(plantsUserDiscovered);
            progressText.setText(plantsUserDiscovered + " / " + maxProgress);
        }


        // display how many plants has been discovered by user
        TextView userPlantDiscovered = binding.profileUserPlantDiscovered;
        mViewModel.updateUserPlantDiscovered(plantsUserDiscovered);
        mViewModel.getUserPlantsDiscovered().observe(getViewLifecycleOwner(), value -> {
            // Convert integer value to string and set it to TextView
            userPlantDiscovered.setText(String.valueOf(value));
        });

        userPlantDiscovered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PlantDiscovered.class);
                intent.putExtra("CurrentUser", currentUser); // Pass the current user object
                startActivity(intent);
            }
        });

        // user post number
        TextView user_post=binding.profileUserPost;
        mViewModel.updateUserPost(postTreeManager.getPostsByUserId(currentUser.getUser_id()));
        mViewModel.getUserPost().observe(getViewLifecycleOwner(), value -> {
            // Convert integer value to string and set it to TextView
            user_post.setText(String.valueOf(value));
        });

        // messages
        TextView messages=binding.messages;
        notificationCountTextView = binding.messagesCount;
        notificationCountTextView.setText(String.valueOf(eventHandler.getUnreadNotifications()));

        // other features in profile
        TextView random_avatar=binding.randomAvatar;
        TextView settings=binding.settings;

        // Initialize LocationManager
        locationManager = (LocationManager) ContextCompat.getSystemService(requireContext(), LocationManager.class);
        // Define a LocationListener

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Convert latitude and longitude to address
                Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        String addressLine = addresses.get(0).getAddressLine(0);
                        // Set the address to TextView
                        user_location.setText(addressLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            @Override
            public void onProviderEnabled(String provider) {}
            @Override
            public void onProviderDisabled(String provider) {}
        };
        // Request location updates
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        } else {
            // Request location updates
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

        user_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyPost.class);
                intent.putExtra("AppUser", currentUser);
                intent.putExtra("CurrentUser", currentUser); // Pass the current user object
                startActivity(intent);
            }
        });
        random_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(), PersonalInfo.class);
//                intent.putExtra("CurrentUser", currentUser); // Pass the current user object
//                startActivity(intent);
                String imageUrl="https://robohash.org/"+generateRandomString();
                Log.w("url",imageUrl);
                UserTreeManager.getInstance().findUserById(currentUser.getUser_id()).setAvatar_url(imageUrl);
                // refresh current user avatar
                userAvatar = binding.profileUserAvatar;
                loadImageFromURL(getContext(), UserTreeManager.getInstance().findUserById(currentUser.getUser_id()).getAvatar_url(), userAvatar, "Avatar");
                Toast.makeText(getContext(), "Surprise", Toast.LENGTH_SHORT).show();
            }
        });
        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventHandler.markAllEventsAsRead();
                Intent intent = new Intent(getActivity(), Messages.class);
                intent.putExtra("CurrentUser", currentUser); // Pass the current user object
                startActivity(intent);
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Settings.class);
                intent.putExtra("CurrentUser", currentUser); // Pass the current user object
                startActivity(intent);
            }
        });

        return root;
    }
    // when fragment is resumed, require update
    public void onResume(){
        super.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        // starting to observe event
        eventHandler.addObserver(this);
        // update numbers
        mViewModel.updateUserPlantDiscovered(postTreeManager.getUserPlantDiscovered(currentUser.getUser_id()).size());
        mViewModel.updateUserPost(postTreeManager.getPostsByUserId(currentUser.getUser_id()));
        // update user avtar
        loadImageFromURL(getContext(), UserTreeManager.getInstance().findUserById(currentUser.getUser_id()).getAvatar_url(), userAvatar, "Avatar");
    }
    // when fragment is paused, close update
    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);
        // stop observing event
        eventHandler.removeObserver(this);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateNotificationCount() {
        notificationCountTextView.setText(String.valueOf(eventHandler.getUnreadNotifications()));
    }
    public static void setUserLevelImage(ImageView image, int plantDiscovered) {
        if (plantDiscovered == 0) {
            image.setImageResource(R.drawable.ic_seed);
        } else if (plantDiscovered >= 1 && plantDiscovered <= 3) {
            image.setImageResource(R.drawable.ic_sprout);
        } else if (plantDiscovered >= 4 && plantDiscovered <= 9) {
            image.setImageResource(R.drawable.ic_seeding);
        } else if (plantDiscovered >= 10 && plantDiscovered <= 19) {
            image.setImageResource(R.drawable.ic_flowering);
        } else if (plantDiscovered >= 20 && plantDiscovered <= 39) {
            image.setImageResource(R.drawable.ic_tree);
        } else if (plantDiscovered >= 40 && plantDiscovered <= 59) {
            image.setImageResource(R.drawable.ic_harvest);
        } else if (plantDiscovered >= 60) {
            image.setImageResource(R.drawable.ic_golden_tree);
        }
    }

    public static int getMaxProgress(int plantDiscovered) {
        if (plantDiscovered < 1) {
            return 1;
        } else if (plantDiscovered < 4) {
            return 4;
        } else if (plantDiscovered < 10) {
            return 10;
        } else if (plantDiscovered < 20) {
            return 20;
        } else if (plantDiscovered < 40) {
            return 40;
        } else if (plantDiscovered < 60) {
            return 60;
        } else {
            return 100;
        }
    }

    @Override
    public void onEventChanged() {
        updateNotificationCount();
    }


}