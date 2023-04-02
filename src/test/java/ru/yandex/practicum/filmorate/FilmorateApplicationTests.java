package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.daoint.FilmDao;
import ru.yandex.practicum.filmorate.daoint.GenreDao;
import ru.yandex.practicum.filmorate.daoint.MpaDao;
import ru.yandex.practicum.filmorate.daoint.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@TestMethodOrder(MethodOrderer.MethodName.class)
class FilmorateApplicationTests {

    private final FilmDao filmDao;
    private final UserDao userDao;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;

    @Test
    @Order(1)
    public void testGetAllUsers() {
        userDao.create(User.builder()
                .email("email")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1972, 02, 12))
                .build());
        userDao.create(User.builder()
                .email("email2")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(1975, 02, 12))
                .build());
        AssertionsForClassTypes.assertThat(userDao.findAll().get(0)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(2)
    public void testCreateUser() {
        userDao.create(User.builder()
                .email("email")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1972, 02, 12))
                .build());
        AssertionsForClassTypes.assertThat(userDao.findAll().get(0)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(3)
    public void testUpdateUser() {
        userDao.create(User.builder()
                .email("email")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1972, 02, 12))
                .build());
        userDao.put(User.builder()
                .id(1)
                .email("email2")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(1973, 02, 12))
                .build());
        AssertionsForClassTypes.assertThat(userDao.getUserById(1)).hasFieldOrPropertyWithValue("email", "email2");
    }


    @Test
    @Order(5)
    public void testFindUserById() {
        userDao.create(User.builder()
                .email("email")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1972, 02, 12))
                .build());
        AssertionsForClassTypes.assertThat(userDao.getUserById(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(6)
    public void testExistsTableUsers() {
        AssertionsForClassTypes.assertThat(userDao.userTableExists(999)).isFalse();
    }

    @Test
    @Order(7)
    public void testGetAllGenres() {
        AssertionsForClassTypes.assertThat(genreDao.findAllGenre().get(0)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(8)
    public void testGetGenresById() {
        AssertionsForClassTypes.assertThat(genreDao.getGenreById(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(9)
    public void testExistsTableGenres() {
        AssertionsForClassTypes.assertThat(genreDao.genreTableExists(999)).isFalse();
    }

    @Test
    @Order(10)
    public void testGetAllMpa() {
        AssertionsForClassTypes.assertThat(mpaDao.findAllMpa().get(0)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(11)
    public void testGetMpaById() {
        AssertionsForClassTypes.assertThat(mpaDao.getMpaById(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(12)
    public void testExistsTableMpa() {
        AssertionsForClassTypes.assertThat(mpaDao.mpaTableExists(999)).isFalse();
    }

    @Test
    @Order(13)
    public void testGetAllFilms() {
        filmDao.addFilm(Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 12, 01))
                .duration(12)
                .mpa(Mpa.builder().name("RG-18").id(1).build())
                .genres(Set.of(Genre.builder().id(1).name("Драма").build()))
                .build());
        AssertionsForClassTypes.assertThat(filmDao.findAllFilms().get(0)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(14)
    public void testAddFilm() {
        filmDao.addFilm(Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 12, 01))
                .duration(12)
                .mpa(Mpa.builder().name("RG-18").id(1).build())
                .genres(Set.of(Genre.builder().id(1).name("Драма").build()))
                .build());
        AssertionsForClassTypes.assertThat(filmDao.getFilmById(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(15)
    public void testPutFilm() {
        filmDao.addFilm(Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 12, 01))
                .duration(12)
                .mpa(Mpa.builder().name("RG-18").id(1).build())
                .genres(Set.of(Genre.builder().id(1).name("Драма").build()))
                .build());
        filmDao.putFilm(Film.builder()
                .id(1)
                .name("name1")
                .description("description1")
                .releaseDate(LocalDate.of(2012, 12, 01))
                .duration(12)
                .mpa(Mpa.builder().name("RG-18").id(1).build())
                .genres(Set.of(Genre.builder().id(1).name("Драма").build()))
                .build());
        AssertionsForClassTypes.assertThat(filmDao.getFilmById(1)).hasFieldOrPropertyWithValue("name", "name1");
    }

    @Test
    @Order(16)
    public void testGetFilmById() {
        filmDao.addFilm(Film.builder()
                .name("name")
                .description("description")
                .releaseDate(LocalDate.of(2012, 12, 01))
                .duration(12)
                .mpa(Mpa.builder().name("RG-18").id(1).build())
                .genres(Set.of(Genre.builder().id(1).name("Драма").build()))
                .build());
        AssertionsForClassTypes.assertThat(filmDao.getFilmById(1)).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    @Order(17)
    public void testExistsFilmTable() {
            AssertionsForClassTypes.assertThat(filmDao.filmsTableExists(1111)).isFalse();
    }

}
