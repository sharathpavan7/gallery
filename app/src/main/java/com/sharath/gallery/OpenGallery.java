package com.sharath.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class OpenGallery extends AppCompatActivity {
    private final static int VDEO_GALLERY_REQUEST_CODE = 111;

    private RecyclerView recyclerView;
    private MediaAdapter mAdapter;
    private List<String> mediaList = new ArrayList<>();
    public static List<Boolean> selected = new ArrayList<>();
    public static ArrayList<String> imagesSelected = new ArrayList<>();
    public static String parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_open_gallery);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(VDEO_GALLERY_REQUEST_CODE, intent);
                finish();
            }
        });
        if (imagesSelected.size() > 0) {
            setTitle(String.valueOf(imagesSelected.size()));
        }
        recyclerView = findViewById(R.id.recycler_view);
        parent = getIntent().getExtras().getString("FROM");
        mediaList.clear();
        selected.clear();

        mediaList.addAll(OneFragment.imagesList);
        selected.addAll(OneFragment.selected);

        populateRecyclerView();
    }


    private void populateRecyclerView() {
        for (int i = 0; i < selected.size(); i++) {
            if (imagesSelected.contains(mediaList.get(i))) {
                selected.set(i, true);
            } else {
                selected.set(i, false);
            }
        }
        mAdapter = new MediaAdapter(mediaList, selected, getApplicationContext());
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        if (recyclerView.getItemAnimator() != null) recyclerView.getItemAnimator().setChangeDuration(0);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            int currentSelection;
            @Override
            public void onClick(View view, int position) {
                if (Gallery.maxSelection == 1) {
                    if (selected.get(position).equals(true)){
                        if (imagesSelected.indexOf(mediaList.get(position)) != -1) {
                            imagesSelected.remove(imagesSelected.indexOf(mediaList.get(position)));
                            selected.set(position, !selected.get(position));
                            mAdapter.notifyItemChanged(position);
                        }
                    } else {
                        imagesSelected.clear();
                        imagesSelected.add(mediaList.get(position));
                        selected.set(currentSelection, false);
                        selected.set(position, !selected.get(position));
                        mAdapter.notifyItemChanged(currentSelection);
                        mAdapter.notifyItemChanged(position);
                        currentSelection = position;
                    }

                } else if (!selected.get(position).equals(true) && imagesSelected.size() < Gallery.maxSelection) {
                    imagesSelected.add(mediaList.get(position));
                    selected.set(position, !selected.get(position));
                    mAdapter.notifyItemChanged(position);
                } else if (selected.get(position).equals(true)) {
                    if (imagesSelected.indexOf(mediaList.get(position)) != -1) {
                        imagesSelected.remove(imagesSelected.indexOf(mediaList.get(position)));
                        selected.set(position, !selected.get(position));
                        mAdapter.notifyItemChanged(position);
                    }
                }
                Gallery.selectionTitle = imagesSelected.size();
                if (imagesSelected.size() != 0) {
                    setTitle(String.valueOf(imagesSelected.size()));
                } else {
                    setTitle(Gallery.title);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }

        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

}

