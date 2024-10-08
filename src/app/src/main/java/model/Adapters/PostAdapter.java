package model.Adapters;

import static model.UtilsApp.formatTimestamp;
import static model.UtilsApp.loadImageFromURL;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.compendiumofmateriamedica.R;
import com.example.compendiumofmateriamedica.ui.profile.ProfileFragment;
import com.example.compendiumofmateriamedica.ui.profile.ProfilePage;
import com.example.compendiumofmateriamedica.ui.social.PhotoDialogFragment;
import com.example.compendiumofmateriamedica.ui.social.SocialFragment;
import com.example.compendiumofmateriamedica.ui.social.SocialViewModel;

import java.util.List;
import java.util.stream.Collectors;

import model.Datastructure.Post;
import model.Datastructure.PostTreeManager;
import model.Datastructure.User;
import model.Datastructure.UserTreeManager;
import model.Parser.Token;

/**
 * @author: Xing Chen
 * @uid: u7725171
 * @description:
 * A post adapter for displaying posts.
 * Each post is shown in a separate view holder arranged using post_item.xml.
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private final Context context;
    private final List<Post> postsList;
    private final FragmentManager fragmentManager;
    private final boolean showLikeButton;
    private final User currentUser;
    private SoundPool soundPool;
    private int soundId;

    // Constructor to initialize the adapter with necessary data
    public PostAdapter(Context context, List<Post> postsList, FragmentManager fragmentManager, Boolean showLikeButton, User currentUser) {
        this.context = context;
        this.postsList = postsList;
        this.fragmentManager = fragmentManager;
        this.showLikeButton = showLikeButton;
        this.currentUser = currentUser;
        initSoundPool();
    }

    // Inflate the post_item layout and creates a new instance of PostViewHolder
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        return new PostViewHolder(v);
    }

    // Binds data to the views inside each post item
    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        // initialize treeManagers
        UserTreeManager userTreeManager = UserTreeManager.getInstance();
        PostTreeManager postTreeManager = PostTreeManager.getInstance();

        Post post = postsList.get(position);
        int post_uid = post.getUser_id();
        User postUser = userTreeManager.findUserById(post_uid);

        if (postUser != null) {
            // Extracting post details
            String postUserUsername = postUser.getUsername();
            String postUserAvatarURL = UserTreeManager.getInstance().findUserById(postUser.getUser_id()).getAvatar_url();
            String postPhotoURL = post.getPhoto_url();
            List<Token> postContent = post.getContent();
            String postTimestamp = post.getTimestamp();

            // Set click listeners for user avatar and username
            holder.userAvatar.setOnClickListener(v -> {
                PhotoDialogFragment avatarDialogFragment = PhotoDialogFragment.newInstance(postUserAvatarURL);
                avatarDialogFragment.show(fragmentManager, "avatar_dialog");
            });

            holder.username.setOnClickListener(v -> {
                if (postUser.getUser_id() != currentUser.getUser_id()) {
                    // Start ProfilePage activity for the selected user
                    Intent intent = new Intent(context, ProfilePage.class);
                    intent.putExtra("AppUser", currentUser);
                    intent.putExtra("ProfileUser", postUser);
                    context.startActivity(intent);
                }
            });

            // Set click listener for post photo
            holder.photo.setOnClickListener(v -> {
                PhotoDialogFragment photoDialogFragment = PhotoDialogFragment.newInstance(postPhotoURL);
                photoDialogFragment.show(fragmentManager, "photo_dialog");
            });

            // Set like button behavior if enabled
            if (showLikeButton) {
                final boolean[] isLiked = {post.isLikedByUser(currentUser.getUser_id())};
                holder.buttonLike.setImageResource(isLiked[0] ? R.drawable.post_like_btn : R.drawable.post_unlike_btn);
                holder.buttonLike.setOnClickListener(v -> {
                    if (!isLiked[0]) {
                        isLiked[0] = true;
                        holder.buttonLike.setImageResource(R.drawable.post_like_btn);
                        if (soundPool != null) {
                            soundPool.play(soundId, 1, 1, 0, 0, 1);
                        }
                        post.likedByUser(currentUser.getUser_id());
                    } else {
                        isLiked[0] = false;
                        holder.buttonLike.setImageResource(R.drawable.post_unlike_btn);
                    }
                });
            }

            // Set delete button behavior if enabled
            holder.buttonDelete.setVisibility(View.GONE); // if has functional bug, use this

            // Load user avatar and post photo from URLs
            loadImageFromURL(context, postUserAvatarURL, holder.userAvatar, "Avatar");
            holder.username.setText(postUserUsername);
            holder.content.setText(postContent.stream().map(Token::getToken).collect(Collectors.joining(" ")));
            holder.timestamp.setText(formatTimestamp(postTimestamp));
            // Set user level image based on the number of plants discovered
            ProfileFragment.setUserLevelImage(holder.userLevel, postTreeManager.getUserPlantDiscovered(post_uid).size());
            loadImageFromURL(context, postPhotoURL, holder.photo, "Photo");

        } else {
            String postPhotoURL = post.getPhoto_url();
            // Show toast if user information is not available
            holder.userAvatar.setOnClickListener(v -> Toast.makeText(context, "No user info available", Toast.LENGTH_SHORT).show());
            holder.username.setText("Unknown User");
            holder.content.setText(post.getContent().stream().map(Token::getToken).collect(Collectors.joining(" ")));
            holder.userAvatar.setImageResource(R.drawable.unknown_user);
            loadImageFromURL(context, postPhotoURL, holder.photo, "Photo");
        }

        // Show divider if the current item is not the last item
        holder.divider.setVisibility(position == postsList.size() - 1 ? View.GONE : View.VISIBLE);
    }

    // Returns the total number of posts in the list
    @Override
    public int getItemCount() {
        return postsList.size();
    }

    // Updates the posts list and notifies the adapter about the change
    public void setPosts(List<Post> posts) {
        postsList.clear();
        postsList.addAll(posts);
        notifyDataSetChanged();
    }
    private void initSoundPool() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }

        soundId = soundPool.load(context, R.raw.sound_like, 1);
    }
    public void releaseSoundPool() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    // ViewHolder class to hold the views inside each post item
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        public final TextView username;
        public final ImageView userAvatar;
        public final TextView content;
        public final ImageView photo;
        public final ImageButton buttonLike;
        public final TextView timestamp;
        public final ImageView userLevel;
        public final ImageButton buttonDelete;
        public final View divider;

        // Constructor to initialize the views
        public PostViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.post_user_name);
            userAvatar = itemView.findViewById(R.id.post_user_avatar);
            content = itemView.findViewById(R.id.post_content);
            photo = itemView.findViewById(R.id.post_photo);
            buttonLike = itemView.findViewById(R.id.button_post_like);
            timestamp = itemView.findViewById(R.id.post_timestamp);
            userLevel = itemView.findViewById(R.id.post_user_level);
            divider=itemView.findViewById(R.id.divider);;
            buttonDelete = itemView.findViewById(R.id.button_post_delete);
        }
    }
}
