package com.example.lma;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.lma.databinding.ActivityMainBinding;
import com.example.lma.model.Category;
import com.example.lma.model.Course;
import com.example.lma.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mainActivityViewModel;
    private ArrayList<Category> categoriesList;
    private ActivityMainBinding activityMainBinding;
    private MainActivityClickHandlers handlers;
    private Category selectedCategory;

    //RecycleView
    private RecyclerView courseRecycleView;
    private CourseAdapter courseAdapter;
    private ArrayList<Course> coursesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        handlers = new MainActivityClickHandlers();
        activityMainBinding.setClickHandlers(handlers);

        mainActivityViewModel.getAllCategories().observe(this, categories -> {
            categoriesList = (ArrayList<Category>) categories;
            for (Category c: categories){
                Log.e("___CATEGORIES", c.getCategoryName());
            }

            showOnSpinner();
        });

        mainActivityViewModel.getCoursesOfSelectedCategory(1).observe(this,
                new Observer<List<Course>>() {
                    @Override
                    public void onChanged(List<Course> courses) {
                        for (Course c:courses){
                            Log.v("TAG", c.getCourseName());
                        }
                    }
                });

    }

    private void showOnSpinner() {
        ArrayAdapter<Category> categoryArrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                categoriesList
        );
        categoryArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        activityMainBinding.setSpinnerAdapter(categoryArrayAdapter);
    }


    public void LoadCoursesArrayList(int categoryID){
        mainActivityViewModel.getCoursesOfSelectedCategory(categoryID).observe(this, new Observer<List<Course>>() {
            @Override
            public void onChanged(List<Course> courses) {
                coursesList = (ArrayList<Course>) courses;
                LoadRecyclerView();
            }
        });
    }

    private void LoadRecyclerView() {
        courseRecycleView = activityMainBinding.secondaryLayout.recyclerView;
        courseRecycleView.setLayoutManager(new LinearLayoutManager(this));
        courseRecycleView.setHasFixedSize(true);

        courseAdapter = new CourseAdapter();
        courseRecycleView.setAdapter(courseAdapter);

        courseAdapter.setCourses(coursesList);
    }

    public class MainActivityClickHandlers{
        public void onFABClicked(View view){
            Toast.makeText(getApplicationContext(), "FAB CLICKED", Toast.LENGTH_SHORT).show();
        }

        public void onSelectItem(AdapterView<?> parent, View view, int pos, int id){
            selectedCategory = (Category) parent.getItemAtPosition(pos);

            String message = "id is: " + selectedCategory.getId() + "" +
                    " \n name is " + selectedCategory.getCategoryName();

            Toast.makeText(parent.getContext(), " " + message, Toast.LENGTH_SHORT).show();

            LoadCoursesArrayList(selectedCategory.getId());
        }
    }
}