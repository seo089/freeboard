package com.angela.board.service.impl;

import com.angela.board.data.dto.BoardDTO;
import com.angela.board.data.dto.BoardUpdateDTO;
import com.angela.board.data.vo.BoardVO;
import com.angela.board.model.board.Board;
import com.angela.board.model.board.QBoard;
import com.angela.board.model.board.repository.BoardRepository;
import com.angela.board.service.BoardService;
import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@RequiredArgsConstructor
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public Optional<Board> getBoard(String boardName) {
        Board board = new Board();
        board.setName(boardName);
        Example<Board> e = Example.of(board);

        return boardRepository.findOne(e);
    }

    @Override
    public BoardVO getBoardByName(String boardName) {
        return new BoardVO(entityBoardByName(boardName));
    }

    @Override
    public Board entityBoardByName(String boardName) {
        BooleanBuilder builder = new BooleanBuilder();
        QBoard qBoard = QBoard.board;
        builder.and(qBoard.name.equalsIgnoreCase(boardName));

        Optional<Board> getBoard = boardRepository.findOne(builder);
        return getBoard.orElse(null);
    }

    @Override
    public List<BoardVO> getBoardList() {
        List<BoardVO> boardList = null;
        List<Board> result = boardRepository.findAll();
        if (result.size() > 0) {
            boardList = Lists.newArrayList();
            for (Board board : result) {
                BoardVO vo = new BoardVO(board);
                boardList.add(vo);
            }
        }
        return boardList;
    }

    @Override
    public boolean addBoard(BoardDTO param) {
        if (param.getName() == null || param.getName().length() == 0) {
            return false;
        }

        BooleanBuilder builder = new BooleanBuilder();
        QBoard qBoard = QBoard.board;
        builder.and(qBoard.name.equalsIgnoreCase(param.getName()));

        AtomicBoolean result = new AtomicBoolean(false);
        if (boardRepository.findOne(builder).isEmpty()) {
            Board board = new Board();
            board.setName(param.getName());
            boardRepository.save(board);
            result.set(true);
        }

        return result.get();
    }

    @Override
    public boolean updateBoard(BoardUpdateDTO param) {
        if (param.getName() == null || param.getName().length() == 0) {
            return false;
        }

        BooleanBuilder builder = new BooleanBuilder();
        QBoard qBoard = QBoard.board;
        builder.and(qBoard.id.eq(param.getId()));

        AtomicBoolean result = new AtomicBoolean(false);
        boardRepository.findOne(builder).ifPresent(board -> {
            board.setName(param.getName());
            boardRepository.save(board);
            result.set(true);
        });

        return result.get();
    }

    @Override
    public boolean deleteBoard(String name) {
        BooleanBuilder builder = new BooleanBuilder();
        QBoard qBoard = QBoard.board;
        builder.and(qBoard.name.equalsIgnoreCase(name));

        AtomicBoolean result = new AtomicBoolean(false);
        boardRepository.findOne(builder).ifPresent(board -> {
            boardRepository.delete(board);
            result.set(true);
        });

        return result.get();
    }

}
