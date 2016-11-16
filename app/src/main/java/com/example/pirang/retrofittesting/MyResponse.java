package com.example.pirang.retrofittesting;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Pirang on 11/22/2016.
 */

public class MyResponse {

    @SerializedName("CODE")
    public String code;
    @SerializedName("MESSAGE")
    public String message;
    @SerializedName("DATA")
    public List<Data> data;
    @SerializedName("PAGINATION")
    public Pagination pagination;

    public static class Author {
        @SerializedName("ID")
        public int id;
        @SerializedName("NAME")
        public String name;
        @SerializedName("EMAIL")
        public String email;
        @SerializedName("GENDER")
        public String gender;
        @SerializedName("TELEPHONE")
        public String telephone;
        @SerializedName("STATUS")
        public String status;
        @SerializedName("FACEBOOK_ID")
        public String facebookId;
        @SerializedName("IMAGE_URL")
        public String imageUrl;
    }

    public static class Category {
        @SerializedName("ID")
        public int id;
        @SerializedName("NAME")
        public String name;
    }

    public static class Data {
        @SerializedName("ID")
        public int id;
        @SerializedName("TITLE")
        public String title;
        @SerializedName("DESCRIPTION")
        public String description;
        @SerializedName("CREATED_DATE")
        public String createdDate;
        @SerializedName("AUTHOR")
        public Author author;
        @SerializedName("STATUS")
        public String status;
        @SerializedName("CATEGORY")
        public Category category;
        @SerializedName("IMAGE")
        public String image;
    }

    public static class Pagination {
        @SerializedName("PAGE")
        public int page;
        @SerializedName("LIMIT")
        public int limit;
        @SerializedName("TOTAL_COUNT")
        public int totalCount;
        @SerializedName("TOTAL_PAGES")
        public int totalPages;
    }
}
