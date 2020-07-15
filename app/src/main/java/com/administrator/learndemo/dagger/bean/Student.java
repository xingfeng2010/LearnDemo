package com.administrator.learndemo.dagger.bean;

public class Student {
    private String name;
    private Course[] courses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course[] getCourses() {
        return courses;
    }

    public void setCourses(Course[] courses) {
        this.courses = courses;
    }

    public static class Course {
        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        private String courseName;
    }
}
