package edu.calvin.cs262;

import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * This module implements a RESTful service for the article table of the monopoly database.
 * Only the article relation is supported, not the game or articlegame objects.
 * The server requires Java 1.7 (not 1.8).
 *
 * I tested these services using IDEA's REST Client test tool. Run the server and open
 * Tools-TestRESTService and set the appropriate HTTP method, host/port, path and request body and then press
 * the green arrow (submit request).
 *
 * See the readme.txt for instructions on how to deploy this application as a webservice.
 *
 * @author kvlinden
 * @version summer, 2015 - original version
 * @version summer, 2016 - upgraded to GSON/JSON; added Article POJO; removed unneeded libraries
 */
@Path("/news")
public class NewsResource {

    /**
     * a hello-world resource
     *
     * @return a simple string value
     */
    @SuppressWarnings("SameReturnValue")
    @GET
    @Path("/hello")
    @Produces("text/plain")
    public String getClichedMessage() {
        return "Hello, Harambe!";
    }

    /**
     * GET method that returns a particular monopoly article based on ID
     *
     * @param id a article id in the monopoly database
     * @return a JSON version of the article record, if any, with the given id
     */
    @GET
    @Path("/article/{id}")
    @Produces("application/json")
    public String getArticle(@PathParam("id") String id) {
        try {
            return new Gson().toJson(retrieveArticle(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * GET method that returns a list of all monopoly articles
     *
     * @return a JSON list representation of the article records
     */
    @GET
    @Path("/articles")
    @Produces("application/json")
    public String getArticles() {
        try {
            return new Gson().toJson(retrieveArticles());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * PUT method for creating an instance of Person with a given ID - If the
     * article already exists, update the fields using the new article field values. We do this
     * because PUT is idempotent, meaning that running the same PUT several
     * times is the same as running it exactly once.
     *
     * @param id         the ID for the new article, assumed to be unique
     * @param articleLine a JSON representation of the article; the id parameter overrides any id in this line
     * @return JSON representation of the updated article, or NULL for errors
     */

    @PUT
    @Path("/article/{id}")
    @Consumes("application/json")
    @Produces("application/json")
    public String putArticle(@PathParam("id") int id, String articleLine) {
        try {
            Article article = new Gson().fromJson(articleLine, Article.class);
            article.setId(id);
            return new Gson().toJson(addOrUpdateArticle(article));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * POST method for creating an instance of Person with a new, unique ID
     * number. We do this because POST is not idempotent, meaning that running
     * the same POST several times creates multiple objects with unique IDs but
     * otherwise having the same field values.
     * <p>
     * The method creates a new, unique ID by querying the article table for the
     * largest ID and adding 1 to that. Using a DB sequence would be a better solution.
     *
     * @param articleLine a JSON representation of the article (ID ignored)
     * @return a JSON representation of the new article
     */

    @POST
    @Path("/article")
    @Consumes("application/json")
    @Produces("application/json")
    public String postArticle(String articleLine) {
        try {
            Article article = new Gson().fromJson(articleLine, Article.class);
            return new Gson().toJson(addNewArticle(article));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * DELETE method for deleting and instance of article with the given ID. If
     * the article doesn't exist, then don't delete anything. DELETE is idempotent, so
     * the result of sending the same command multiple times should be the same as
     * sending it exactly once.
     *
     * @param id the ID of the article to be deleted
     * @return null
     */
    @DELETE
    @Path("/article/{id}")
    @Produces("application/json")
    public String deleteArticle(@PathParam("id") int id) {
        try {
            Article x = new Article(id, "deleted", "deleted", "deleted", "deleted", "deleted");
            Article y = deleteArticle(x);
            return new Gson().toJson(y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** DBMS Utility Functions *********************************************/

    /**
     * Constants for a local Postgresql server with the monopoly database
     */
    private static final String DB_URI = "jdbc:postgresql://localhost:5432/cs262cKnowledgeShare";
    private static final String DB_LOGIN_ID = "postgres";
    private static final String DB_PASSWORD = "Listen-Anywhere-6";;
    private static final String PORT = "8083";

    /**
     * Utility method that does the database query, potentially throwing an SQLException,
     * returning a article object (or null).
     */
    private List<Article> retrieveArticle(String constraint) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Article> articles = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT DISTINCT Article.ID, Author.Email, Author.Name, Article.Subject, Article.Body, Issue.PublishDateTime FROM Author, Article, Issue WHERE Author.ID = Article.ID AND Article.ID = Issue.ID AND Author.Email NOT SIMILAR TO '" + constraint + "' ORDER BY Issue.PublishDateTime DESC");
            //rs = statement.executeQuery("SELECT DISTINCT Article.ID, Author.Email, Author.Name, Article.Subject, Article.Body, Issue.PublishDateTime FROM Author, Article, Issue WHERE Author.ID = Article.ID AND Article.ID = Issue.ID");
            while (rs.next()) {
                articles.add(new Article(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return articles;
    }

    /**
     * Utility method that does the database query, potentially throwing an SQLException,
     * returning a list of name-value map objects (potentially empty).
     */
    private List<Article> retrieveArticles() throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List<Article> articles = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            //rs = statement.executeQuery("SELECT Article.ID, Author.Email, Author.Name, Article.Subject, Article.Body, Issue.PublishDateTime FROM Author, Article, Issue WHERE Author.ID = Article.ID AND Article.ID = Issue.ID AND Author.Email SIMILAR TO 'zand@calvin.edu|johncalvin@calvin.edu'");
            rs = statement.executeQuery("SELECT DISTINCT Article.ID, Author.Email, Author.Name, Article.Subject, Article.Body, Issue.PublishDateTime FROM Author, Article, Issue WHERE Author.ID = Article.ID AND Article.ID = Issue.ID ORDER BY Issue.PublishDateTime DESC");
            while (rs.next()) {
                articles.add(new Article(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return articles;
    }

    /**
     * Utility method that does the database update, potentially throwing an SQLException,
     * returning the article, potentially new.
     */
    private Article addOrUpdateArticle(Article article) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM Article WHERE id=" + article.getId());
            if (rs.next()) {
                statement.executeUpdate("UPDATE Article SET Subject='" + article.getSubject()  + "', Sender='" + article.getSender() + "', Body='" + article.getBody()+ "' WHERE id=" + article.getId());
            } else {
                statement.executeUpdate("INSERT INTO Article VALUES (" + article.getId() + ", '" + article.getSubject() + ", '" + article.getSender() + "', '" + article.getBody() + "')");
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return article;
    }


    /**
     * Utility method that adds the given article using a new,unique ID, potentially throwing an SQLException,
     * returning the new article
     */

    private Article addNewArticle(Article article) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT MAX(ID) FROM Article");
            if (rs.next()) {
                article.setId(rs.getInt(1) + 1);
            } else {
                throw new RuntimeException("failed to find unique ID...");
            }
            statement.executeUpdate("INSERT INTO Article VALUES (" + article.getId() + ", '" + article.getSubject() + ", '" + article.getSender() + "', '" + article.getBody() + "')");
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return article;
    }

    /**
     * Utility method that does the database update, potentially throwing an SQLException,
     * returning the article, potentially new.
     */
    public Article deleteArticle(Article article) throws Exception {
        Connection connection = null;
        Statement statement = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM Article WHERE id=" + article.getId());
        } catch (SQLException e) {
            throw (e);
        } finally {
            statement.close();
            connection.close();
        }
        return article;
    }

    /** Main *****************************************************/

    /**
     * Run this main method to fire up the service.
     *
     * @param args command-line arguments (ignored)
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:" + PORT + "/");
        server.start();

        System.out.println("Server running...");
        System.out.println("Web clients should visit: http://localhost:" + PORT + "/news");
        System.out.println("Android emulators should visit: http://LOCAL_IP_ADDRESS:" + PORT + "/news");
        System.out.println("Hit return to stop...");
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        System.out.println("Stopping server...");
        server.stop(0);
        System.out.println("Server stopped...");
    }
}
