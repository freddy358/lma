package com.example.lma.model;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CourseShopRepository {

    private CategoryDAO categoryDAO;
    private CourseDAO courseDAO;

    private LiveData<List<Category>> categories;
    private LiveData<List<Course>> courses;

    public CourseShopRepository(Application application) {
        CourseDatabase courseDatabase = CourseDatabase.getInstance(application);
        categoryDAO = courseDatabase.categoryDAO();
        courseDAO = courseDatabase.courseDAO();
    }

    public LiveData<List<Category>> getCategories() {
        return categoryDAO.getAllCategories();
    }

    public LiveData<List<Course>> getCourses(int categoryID) {
        return courseDAO.getCourses(categoryID);
    }

    private void insertCategory(Category category){
        // Inserting Categories
        categoryDAO.insert(category);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.insert(category);
            }
        });
    }

    public void insertCourse(Course course){
        //Insert Courses
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                courseDAO.insert(course);

                //Do after background execution is done - post execution
            }
        });
    }

    public void deleteCategory (Category category){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.delete(category);

                //Do after background execution is done - post execution
            }
        });
    }

    public void deleteCourse (Course course){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                courseDAO.delete(course);

                //Do after background execution is done - post execution
            }
        });
    }

    public void updateCategory(Category category){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                categoryDAO.update(category);

                //Do after background execution is done - post execution
            }
        });
    }

    public void updateCourse(Course course){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                courseDAO.update(course);

                //Do after background execution is done - post execution
            }
        });
    }
}
