package service;

import dao.NewsDAO;
import model.News;
import model.PageResult;

public class NewsService {
    private NewsDAO newsDAO = new NewsDAO();

    public PageResult<News> findPage(String title, int categoryId, int page, int pageSize) {
        return newsDAO.findPage(title, categoryId, page, pageSize);
    }

    public boolean add(String title, String content, int categoryId, int authorId) {
        News news = new News();
        news.setTitle(title);
        news.setContent(content);
        news.setCategoryId(categoryId);
        news.setAuthorId(authorId);
        return newsDAO.add(news);
    }

    public boolean update(int id, String title, String content, int categoryId) {
        News news = new News();
        news.setId(id);
        news.setTitle(title);
        news.setContent(content);
        news.setCategoryId(categoryId);
        return newsDAO.update(news);
    }

    public boolean delete(int id) {
        return newsDAO.delete(id);
    }
}
