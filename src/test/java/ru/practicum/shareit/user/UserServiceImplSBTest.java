package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplSBTest {

    private final EntityManager em;
    private final UserService service;

    @Test
    void saveUserTest() {
        User user = new User();
        user.setEmail("1@1.ru");
        user.setName("1");
        service.save(user);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User actualUser = query.setParameter("email", user.getEmail()).getSingleResult();
        assertThat(actualUser.getId(), notNullValue());
        assertThat(actualUser.getName(), equalTo(user.getName()));
        assertThat(actualUser.getEmail(), equalTo(user.getEmail()));
    }
}