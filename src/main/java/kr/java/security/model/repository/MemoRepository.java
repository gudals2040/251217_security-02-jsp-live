package kr.java.security.model.repository;

import kr.java.security.model.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {

    @Query("SELECT m FROM Memo m JOIN FETCH m.author WHERE m.author.id = :authorId ORDER BY m.createdAt DESC")
    List<Memo> findByAuthorIdWithAuthor(@Param("authorId") Long authorId);

    @Query("SELECT m FROM Memo m JOIN FETCH m.author WHERE m.id = :id")
    Optional<Memo> findByIdWithAuthor(@Param("id") Long id);
}