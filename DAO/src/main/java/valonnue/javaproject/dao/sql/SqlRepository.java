/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package valonnue.javaproject.dao.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import valonnue.javaproject.Models.Actor;
import valonnue.javaproject.Models.Director;
import valonnue.javaproject.Models.Movie;
import valonnue.javaproject.Models.User;
import valonnue.javaproject.dao.*;

public class SqlRepository implements UserRepository, DatabaseRepository, ActorRepository, DirectorRepository, MovieRepository, RelationshipRepository {

    @Override
    public int createUser(User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.CREATE_USER)) {

            stmt.setString(DatabaseConstants.USERNAME, user.getUsername());
            stmt.setString(DatabaseConstants.PASSWORD_HASH, user.getPasswordHash());
            stmt.setString(DatabaseConstants.FIRST_NAME, user.getFirstName());
            stmt.setString(DatabaseConstants.LAST_NAME, user.getLastName());
            stmt.registerOutParameter(DatabaseConstants.ID_USER, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(DatabaseConstants.ID_USER);
        }
    }

    @Override
    public void updateUser(int id, User user) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.UPDATE_USER)) {

            stmt.setInt(DatabaseConstants.ID_USER, id);
            stmt.setString(DatabaseConstants.USERNAME, user.getUsername());
            stmt.setString(DatabaseConstants.PASSWORD_HASH, user.getPasswordHash());
            stmt.setString(DatabaseConstants.FIRST_NAME, user.getFirstName());
            stmt.setString(DatabaseConstants.LAST_NAME, user.getLastName());

            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.DELETE_USER)) {

            stmt.setInt(DatabaseConstants.ID_USER, id);

            stmt.executeUpdate();
        }
    }

    @Override
    public Optional<User> selectUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_USER)) {

            stmt.setInt(DatabaseConstants.ID_USER, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(DatabaseConstants.ID_USER),
                            rs.getString(DatabaseConstants.USERNAME),
                            rs.getString(DatabaseConstants.PASSWORD_HASH),
                            rs.getString(DatabaseConstants.FIRST_NAME),
                            rs.getString(DatabaseConstants.LAST_NAME)
                    )
                    );
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> selectUsers() throws Exception {
        List<User> users = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_USERS); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt(DatabaseConstants.ID_USER),
                        rs.getString(DatabaseConstants.USERNAME),
                        rs.getString(DatabaseConstants.PASSWORD_HASH),
                        rs.getString(DatabaseConstants.FIRST_NAME),
                        rs.getString(DatabaseConstants.LAST_NAME)));
            }
        }
        return users;
    }

    @Override
    public void clearDatabase() {

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.CLEAR_DATABASE)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int createActor(Actor actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.CREATE_ACTOR)) {

            stmt.setString(DatabaseConstants.FULL_NAME, actor.getFullName());
            stmt.registerOutParameter(DatabaseConstants.ID_ACTOR, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(DatabaseConstants.ID_ACTOR);
        }
    }

    @Override
    public int createDirector(Director director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.CREATE_DIRECTOR)) {

            stmt.setString(DatabaseConstants.FULL_NAME, director.getFullName());
            stmt.registerOutParameter(DatabaseConstants.ID_DIRECTOR, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(DatabaseConstants.ID_DIRECTOR);
        }
    }

    @Override
    public int createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.CREATE_MOVIE)) {

            stmt.setString(DatabaseConstants.TITLE, movie.getTitle());
            stmt.setString(DatabaseConstants.DESCRIPTION, movie.getDescription());
            stmt.setString(DatabaseConstants.BANNER_PATH, movie.getBannerPath());
            stmt.setString(DatabaseConstants.LINK, movie.getLink());
            stmt.setDate(DatabaseConstants.PUBLISH_DATE, new Date(movie.getPublishedDate().getTime()));
            stmt.setDate(DatabaseConstants.SHOWING_DATE, new Date(movie.getShowingDate().getTime()));
            stmt.registerOutParameter(DatabaseConstants.ID_MOVIE, Types.INTEGER);

            stmt.executeUpdate();
            int createdMovie = stmt.getInt(DatabaseConstants.ID_MOVIE);

            movie.getActors().stream().forEach(x -> {
                try {
                    int actorCreated = createActor(x);
                    addActorToMovie(actorCreated, createdMovie);
                } catch (Exception ex) {
                    Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            movie.getDirectors().forEach(x -> {
                try {
                    int directorCreated = createDirector(x);
                    addDirectorToMovie(directorCreated, createdMovie);
                } catch (Exception ex) {
                    Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            return createdMovie;
        }
    }

    @Override
    public void addActorToMovie(int actorID, int movieID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.ADD_ACTOR_TO_MOVIE)) {

            stmt.setInt(DatabaseConstants.ACTOR_ID, actorID);
            stmt.setInt(DatabaseConstants.MOVIE_ID, movieID);

            stmt.executeUpdate();
        }
    }

    @Override
    public void addDirectorToMovie(int directorID, int movieID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.ADD_DIRECTOR_TO_MOVIE)) {

            stmt.setInt(DatabaseConstants.DIRECTOR_ID, directorID);
            stmt.setInt(DatabaseConstants.MOVIE_ID, movieID);

            stmt.executeUpdate();
        }
    }

    @Override
    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_MOVIES); ResultSet rs = stmt.executeQuery()) {
            Movie movie;
            while (rs.next()) {
                movie = new Movie(
                        rs.getInt(DatabaseConstants.ID_MOVIE),
                        rs.getString(DatabaseConstants.TITLE),
                        rs.getString(DatabaseConstants.DESCRIPTION),
                        rs.getString(DatabaseConstants.BANNER_PATH),
                        rs.getString(DatabaseConstants.LINK),
                        rs.getDate(DatabaseConstants.PUBLISH_DATE),
                        rs.getDate(DatabaseConstants.SHOWING_DATE)
                );
                movie.setActors(selectActorsForMovie(movie.getId()));
                movie.setDirectors(selectDirectorsForMovie(movie.getId()));
                movies.add(movie);
            }
        }
        return movies;
    }

    @Override
    public Set<Actor> selectActorsForMovie(int movieID) throws Exception {
        Set<Actor> actors = new HashSet<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_ACTORS_FOR_MOVIE)) {
            stmt.setInt(DatabaseConstants.MOVIE_ID, movieID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    actors.add(new Actor(
                            rs.getInt(DatabaseConstants.ID_ACTOR),
                            rs.getString(DatabaseConstants.FULL_NAME)
                    ));
                }
            }
        }
        return actors;
    }

    @Override
    public Set<Director> selectDirectorsForMovie(int movieID) throws Exception {
        Set<Director> directors = new HashSet<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_DIRECTORS_FOR_MOVIE)) {
            stmt.setInt(DatabaseConstants.MOVIE_ID, movieID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    directors.add(new Director(
                            rs.getInt(DatabaseConstants.ID_DIRECTOR),
                            rs.getString(DatabaseConstants.FULL_NAME)
                    ));
                }
            }
        }
        return directors;
    }

    @Override
    public Movie selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_MOVIE)) {
            stmt.setInt(DatabaseConstants.ID_MOVIE, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    Movie movie = new Movie(
                            rs.getInt(DatabaseConstants.ID_MOVIE),
                            rs.getString(DatabaseConstants.TITLE),
                            rs.getString(DatabaseConstants.DESCRIPTION),
                            rs.getString(DatabaseConstants.BANNER_PATH),
                            rs.getString(DatabaseConstants.LINK),
                            rs.getDate(DatabaseConstants.PUBLISH_DATE),
                            rs.getDate(DatabaseConstants.SHOWING_DATE)
                    );

                    movie.setActors(selectActorsForMovie(movie.getId()));
                    movie.setDirectors(selectDirectorsForMovie(movie.getId()));
                    return movie;
                }

            }
        }
        return null;
    }

    @Override
    public void updateMovie(int id, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.UPDATE_MOVIE)) {

            stmt.setInt(DatabaseConstants.ID_MOVIE, id);
            stmt.setString(DatabaseConstants.TITLE, movie.getTitle());
            stmt.setString(DatabaseConstants.DESCRIPTION, movie.getDescription());
            stmt.setString(DatabaseConstants.BANNER_PATH, movie.getBannerPath());
            stmt.setString(DatabaseConstants.LINK, movie.getLink());
            stmt.setDate(DatabaseConstants.PUBLISH_DATE, (Date) movie.getPublishedDate());
            stmt.setDate(DatabaseConstants.SHOWING_DATE, (Date) movie.getShowingDate());

            stmt.executeUpdate();

            Set<Actor> actorsOutdated = selectActorsForMovie(id);
            Set<Actor> actorsUpdated = movie.getActors();
            Set<Director> directorsOutdated = selectDirectorsForMovie(id);
            Set<Director> directorsUpdated = movie.getDirectors();
            if (!actorsOutdated.isEmpty()) {
                actorsOutdated.forEach(x -> {
                    if (!actorsUpdated.contains(x)) try {
                        removeActorFromMovie(x.getId(), id);
                    } catch (Exception ex) {
                        Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            if (!actorsUpdated.isEmpty()) {
                actorsUpdated.forEach(x -> {
                    if (!actorsOutdated.contains(x)) try {
                        addActorToMovie(x.getId(), id);
                    } catch (Exception ex) {
                        Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            if (!directorsOutdated.isEmpty()) {
                directorsOutdated.forEach(x -> {
                    if (!directorsUpdated.contains(x)) try {
                        removeDirectorFromMovie(x.getId(), id);
                    } catch (Exception ex) {
                        Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            if (!directorsUpdated.isEmpty()) {
                directorsUpdated.forEach(x -> {
                    if (!directorsOutdated.contains(x)) try {
                        addDirectorToMovie(x.getId(), id);
                    } catch (Exception ex) {
                        Logger.getLogger(SqlRepository.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
        }

    }

    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.DELETE_MOVIE)) {

            stmt.setInt(DatabaseConstants.ID_MOVIE, id);

            stmt.executeUpdate();
        }

    }

    @Override
    public void removeActorFromMovie(int actorID, int movieID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.REMOVE_ACTOR_FROM_MOVIE)) {

            stmt.setInt(DatabaseConstants.ACTOR_ID, actorID);
            stmt.setInt(DatabaseConstants.MOVIE_ID, movieID);

            stmt.executeUpdate();
        }
    }

    @Override
    public void removeDirectorFromMovie(int directorID, int movieID) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.REMOVE_DIRECTOR_FROM_MOVIE)) {

            stmt.setInt(DatabaseConstants.DIRECTOR_ID, directorID);
            stmt.setInt(DatabaseConstants.MOVIE_ID, movieID);

            stmt.executeUpdate();
        }
    }

    @Override
    public Set<Actor> selectActors() throws Exception {
        Set<Actor> actors = new HashSet<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_ACTORS); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                actors.add(new Actor(
                        rs.getInt(DatabaseConstants.ID_ACTOR),
                        rs.getString(DatabaseConstants.FULL_NAME)
                ));
            }
        }
        return actors;
    }

    @Override
    public Actor selectActor(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_ACTOR)) {

            stmt.setInt(DatabaseConstants.ID_ACTOR, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                   return new Actor(
                            rs.getInt(DatabaseConstants.ID_ACTOR),
                            rs.getString(DatabaseConstants.FULL_NAME)
                    );
                }
            }
        }
        return null;
    }

    @Override
    public Set<Director> selectDirectors() throws Exception {
        Set<Director> directors = new HashSet<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_DIRECTORS); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                directors.add(new Director(
                        rs.getInt(DatabaseConstants.ID_DIRECTOR),
                        rs.getString(DatabaseConstants.FULL_NAME)
                ));
            }
        }
        return directors;
    }

    @Override
    public Director selectDirector(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DatabaseConstants.SELECT_DIRECTOR)) {

            stmt.setInt(DatabaseConstants.ID_DIRECTOR, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new Director(
                            rs.getInt(DatabaseConstants.ID_DIRECTOR),
                            rs.getString(DatabaseConstants.FULL_NAME)
                    );
                }
            }
        }
        return null;
    }

    private class DatabaseConstants {

        private DatabaseConstants() {
        }

        public static final String ID_USER = "IDUser";
        public static final String ID_ACTOR = "IDActor";
        public static final String ID_DIRECTOR = "IDDirector";
        public static final String ID_MOVIE = "IDMovie";
        public static final String MOVIE_ID = "MovieID";
        public static final String ACTOR_ID = "ActorID";
        public static final String DIRECTOR_ID = "DirectorID";
        public static final String USERNAME = "Username";
        public static final String PASSWORD_HASH = "PasswordHash";
        public static final String FIRST_NAME = "FirstName";
        public static final String LAST_NAME = "LastName";
        public static final String FULL_NAME = "FullName";
        public static final String TITLE = "Title";
        public static final String DESCRIPTION = "Description";
        public static final String BANNER_PATH = "BannerPath";
        public static final String LINK = "Link";
        public static final String PUBLISH_DATE = "PublishDate";
        public static final String SHOWING_DATE = "ShowingDate";
        public static final String CREATE_USER = "{ CALL createUser (?,?,?,?,?) }";
        public static final String UPDATE_USER = "{ CALL updateUser (?,?,?,?,?,?) }";
        public static final String DELETE_USER = "{ CALL deleteUser (?) }";
        public static final String SELECT_USER = "{ CALL selectUser (?) }";
        public static final String SELECT_USERS = "{ CALL selectUsers }";
        public static final String CLEAR_DATABASE = "{ CALL clearDatabase }";
        public static final String CREATE_ACTOR = "{ Call createActor (?,?) }";
        public static final String CREATE_DIRECTOR = "{ Call createDirector (?,?) }";
        public static final String CREATE_MOVIE = "{ Call createMovie (?,?,?,?,?,?,?) }";
        public static final String ADD_ACTOR_TO_MOVIE = "{ Call addActorToMovie (?,?) }";
        public static final String ADD_DIRECTOR_TO_MOVIE = "{ Call addDirectorToMovie (?,?) }";
        public static final String SELECT_MOVIES = "{ Call selectMovies }";
        public static final String SELECT_ACTORS_FOR_MOVIE = "{ Call selectActorsForMovie (?) }";
        public static final String SELECT_DIRECTORS_FOR_MOVIE = "{ Call selectDirectorsForMovie (?) }";
        public static final String SELECT_MOVIE = "{ Call selectMovie (?) }";
        public static final String DELETE_MOVIE = "{ Call deleteMovie (?) }";
        public static final String REMOVE_ACTOR_FROM_MOVIE = "{Call removeActorFromMovie (?,?) }";
        public static final String REMOVE_DIRECTOR_FROM_MOVIE = "{Call removeDirectorFromMovie (?,?) }";
        public static final String UPDATE_MOVIE = "{ Call updateMovie (?,?,?,?,?,?,?) }";
        public static final String SELECT_ACTORS = "{ Call selectActors }";
        public static final String SELECT_ACTOR = "{ Call selectActor (?) }";
        public static final String SELECT_DIRECTORS = "{ Call selectDirectors }";
        public static final String SELECT_DIRECTOR = "{ Call selectDirector (?) }";
    }
}
