package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // @Query("SELECT t FROM Todo t JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    @EntityGraph(value = "Todo.withUser")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

//    @Query("SELECT t FROM Todo t " +
//            "LEFT JOIN FETCH t.user " +
//            "WHERE t.id = :todoId")
    @EntityGraph(value = "Todo.withUser")
    Optional<Todo> findTodoWithUserById(Long id);

    int countById(Long todoId);
}
