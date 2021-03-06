package com.angela.board.model.board.repository;

import com.angela.board.model.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board,Long>, QuerydslPredicateExecutor<Board> {
}
