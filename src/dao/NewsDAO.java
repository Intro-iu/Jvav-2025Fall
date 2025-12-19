package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.News;
import model.PageResult;
import util.DBUtil;

public class NewsDAO {

    public PageResult<News> findPage(String searchTitle, int categoryId, int page, int pageSize) {
        List<News> list = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        // Base SQL
        StringBuilder whereSql = new StringBuilder("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (searchTitle != null && !searchTitle.trim().isEmpty()) {
            whereSql.append("AND n.title LIKE ? ");
            params.add("%" + searchTitle.trim() + "%");
        }
        if (categoryId > 0) {
            whereSql.append("AND n.category_id = ? ");
            params.add(categoryId);
        }

        // Count SQL
        String countSql = "SELECT COUNT(*) FROM t_news n " + whereSql.toString();
        int totalCount = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();

            // Execute Count
            pstmt = conn.prepareStatement(countSql);
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            rs.close();
            pstmt.close();

            // Execute Query
            StringBuilder querySql = new StringBuilder(
                    "SELECT n.*, c.name as category_name, u.nickname as author_name " +
                            "FROM t_news n " +
                            "LEFT JOIN t_category c ON n.category_id = c.id " +
                            "LEFT JOIN t_user u ON n.author_id = u.id " +
                            whereSql.toString() +
                            "ORDER BY n.publish_time DESC LIMIT ?, ?");

            pstmt = conn.prepareStatement(querySql.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            pstmt.setInt(params.size() + 1, offset);
            pstmt.setInt(params.size() + 2, pageSize);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                News news = new News();
                news.setId(rs.getInt("id"));
                news.setTitle(rs.getString("title"));
                news.setContent(rs.getString("content"));
                news.setCategoryId(rs.getInt("category_id"));
                news.setAuthorId(rs.getInt("author_id"));
                news.setPublishTime(rs.getTimestamp("publish_time"));
                news.setCategoryName(rs.getString("category_name"));
                news.setAuthorName(rs.getString("author_name"));
                list.add(news);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }

        return new PageResult<>(list, totalCount, page, pageSize);
    }

    public boolean add(News news) {
        String sql = "INSERT INTO t_news (title, content, category_id, author_id, publish_time) VALUES (?, ?, ?, ?, NOW())";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, news.getTitle());
            pstmt.setString(2, news.getContent());
            pstmt.setInt(3, news.getCategoryId());
            pstmt.setInt(4, news.getAuthorId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
    }

    public boolean update(News news) {
        String sql = "UPDATE t_news SET title = ?, content = ?, category_id = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, news.getTitle());
            pstmt.setString(2, news.getContent());
            pstmt.setInt(3, news.getCategoryId());
            pstmt.setInt(4, news.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM t_news WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, pstmt, null);
        }
    }
}
