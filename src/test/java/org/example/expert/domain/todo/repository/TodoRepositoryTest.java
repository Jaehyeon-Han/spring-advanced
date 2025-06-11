package org.example.expert.domain.todo.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
class TodoRepositoryTest {

    @Autowired
    TodoRepository todoRepository;

    /*
    기존 쿼리
    select
        t1_0.id,
        t1_0.contents,
        t1_0.created_at,
        t1_0.modified_at,
        t1_0.title,
        t1_0.user_id,
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.password,
        u1_0.user_role,
        t1_0.weather
    from
        todos t1_0
    left join <- @EntityGraph를 사용하면 join으로 바뀌는데, 이는 INNER JOIN임
        users u1_0
            on u1_0.id=t1_0.user_id
    order by
        t1_0.modified_at desc
    offset
        ? rows
    fetch
        first ? rows only
Hibernate:
    select
        count(t1_0.id)
    from
        todos t1_0
     */
    @Test
    void findAllByOrderByModifiedAtDesc() {
        // 쿼리 확인
        Pageable pageable = PageRequest.of(1, 10);
        todoRepository.findAllByOrderByModifiedAtDesc(pageable);
    }

    /*
    기존 쿼리
    select
        t1_0.id,
        t1_0.contents,
        t1_0.created_at,
        t1_0.modified_at,
        t1_0.title,
        t1_0.user_id,
        u1_0.id,
        u1_0.created_at,
        u1_0.email,
        u1_0.modified_at,
        u1_0.password,
        u1_0.user_role,
        t1_0.weather
    from
        todos t1_0
    left join
        users u1_0
            on u1_0.id=t1_0.user_id
    where
        t1_0.id=?
     */
    @Test
    void findTodoWithUserById() {
        // 쿼리 확인
        todoRepository.findTodoWithUserById(1L);
    }
}