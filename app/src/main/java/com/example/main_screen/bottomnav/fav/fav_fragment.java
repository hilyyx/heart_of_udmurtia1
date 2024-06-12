package com.example.main_screen.bottomnav.fav;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.main_screen.R;
import com.example.main_screen.databinding.FragmentFavBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import org.checkerframework.checker.calledmethods.qual.EnsuresCalledMethodsIf;

import java.io.IOException;

import adapter.NewsAdapter;
import adapter.ReviewsAdapter;
import model.Newss;

public class fav_fragment extends Fragment {
    private Uri filePaths;
    private FragmentFavBinding binding;
    private List<Newss> NewsList = new ArrayList<>(); // Объявляем переменную NewsList здесь

    RecyclerView recyclenews;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavBinding.inflate(inflater, container, false);

        loadUserInfo(); // Загружаем информацию после того, как объявили NewsList

        return binding.getRoot();
    }

    private void loadUserInfo(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String username = snapshot.child("username").getValue(String.class);
                        String postText = snapshot.child("postText").getValue(String.class);
                        String postNameText = snapshot.child("postNameText").getValue(String.class);
                        String postsImages = snapshot.child("postImages").getValue(String.class);
                        String profileImage = snapshot.child("profileImage").getValue(String.class);

                        NewsList.add(new Newss(1, username, postNameText, postText, profileImage, postsImages));
                        NewsList.add(new Newss(2, "Алексей", "Спектакль Алые Паруса", "Идти было порядочно боязно,все таки судьба этого проекта мне не безразлична. За несколько показов появилось множество абсолютно отличающихся друг от друга мнений. Лично для меня это предмет гордости", "alexey", "alexey_im"));
                        NewsList.add(new Newss(3, "Мария", "Ресторан Panorama ", "Приятный интерьер, хорошее соотношение цена/качество, приветливый персонал.", "maria", "maria_im"));
                        NewsList.add(new Newss(4, "Александра", "Кинотеатр  “Синема парк”", "Нравится наличие касс, быстрое продвижение при наличии очереди, хороший кинозал", "alexandra", "alexandra_im"));
                        setNewsRecycler();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void setNewsRecycler() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclenews = binding.recyclenews;
        recyclenews.setLayoutManager(layoutManager);

        NewsAdapter newsAdapter = new NewsAdapter(getContext(), NewsList);
        recyclenews.setAdapter(newsAdapter);
    }
}
