package model;

import java.util.Date;

public class News {
    private int id;
    private String title;
    private String content;
    private int categoryId;
    private int authorId;
    private Date publishTime;

    // Helper fields for display (optional, but useful)
    private String categoryName;
    private String authorName;

    public News() {}

    public News(int id, String title, String content, int categoryId, int authorId, Date publishTime) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
        this.authorId = authorId;
        this.publishTime = publishTime;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public Date getPublishTime() { return publishTime; }
    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }
}
