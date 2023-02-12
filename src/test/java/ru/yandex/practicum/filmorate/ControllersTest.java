//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ru.yandex.practicum.filmorate;

import java.time.LocalDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public class ControllersTest {

    @Test
    public void testingValidateDateReleaseFilms() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            Film film = new Film(1, "film1", "desk1", LocalDate.of(1894, 12, 12), 120);
            FilmController filmController = new FilmController();
            filmController.validateFilm(film);
        });
        Assertions.assertEquals("Дата фильма не может раньше 28 декабря 1895 года", thrown.getMessage());
    }

    @Test
    public void testingValidatePositiveDurationFilm()  {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            Film film = new Film(1, " ", "desk1", LocalDate.of(2002, 12, 12), -120);
            FilmController filmController = new FilmController();
            filmController.validateFilm(film);
        });
        Assertions.assertEquals("Продолжительность должна быть положительной", thrown.getMessage());
    }

    @Test
    public void testingDescriptionFilm() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            Film film = new Film(1, " ", "des1313442425235523523523536623636325k13332342345235235235235235235235235235sdljfjsgjlsjgjllsrgllslglksfglklkfglkdfgklskfgksdgsjs;dkjgs;djg;sdljg;lsdjg;sldjg;sldjg;sdljg;serjgiirjeg;sengk;sejg;segjse;g;sejg", LocalDate.of(2002, 12, 12), -120);
            FilmController filmController = new FilmController();
            filmController.validateFilm(film);
        });
        Assertions.assertEquals("Длина описания фильма не может быть больше 200 символов", thrown.getMessage());
    }

    @Test
    public void testingValidateNameFilm() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            Film film = new Film(1, "", "desk1", LocalDate.of(2002, 12, 12), 120);
            FilmController filmController = new FilmController();
            filmController.validateFilm(film);
        });
        Assertions.assertEquals("Имя не может быть пустым", thrown.getMessage());
    }

    @Test
    public void testingValidateSetUserLogin() throws ValidationException {
        User user = new User(1, "test@yandex.ru", "login1", " ", LocalDate.of(2000, 12, 21));
        UserController userController = new UserController();
        userController.validateUser(user);
        Assertions.assertEquals(user.getLogin(), "login1");
    }

    @Test
    public void testingValidateBirthdateUser() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            User user = new User(1, "test@yandex.ru", "login1", " ", LocalDate.of(2024, 12, 21));
            UserController userController = new UserController();
            userController.validateUser(user);
        });
        Assertions.assertEquals("Дата рождения не может быть в будущем времени", thrown.getMessage());
    }

    @Test
    public void testingValidateLoginWithSpaceUser() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            User user = new User(1, "test@yandex.ru", "logi n1", " ", LocalDate.of(2020, 12, 21));
            UserController userController = new UserController();
            userController.validateUser(user);
        });
        Assertions.assertEquals("Логин не может содержать пробелы", thrown.getMessage());
    }

    @Test
    public void testingInvalidMail() {
        ValidationException thrown = Assertions.assertThrows(ValidationException.class, () -> {
            User user = new User(1, "testyandex.ru", "login1", " ", LocalDate.of(2020, 12, 21));
            UserController userController = new UserController();
            userController.validateUser(user);
        });
        Assertions.assertEquals("Неверный формат почты", thrown.getMessage());
    }
}
