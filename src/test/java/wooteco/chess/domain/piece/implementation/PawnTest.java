package wooteco.chess.domain.piece.implementation;

import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.chess.domain.board.BoardSituation;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.implementation.piece.Pawn;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

class PawnTest {

    private PieceState whitePawn;
    private PieceState blackPawn;
    private BoardSituation boardSituation;
    private Map<Position, Team> boardDto;
    private Team whiteTeam = Team.WHITE;
    private Team blackTeam = Team.BLACK;

    @BeforeEach
    void setUp() {
        whitePawn = Pawn.of(Position.of("B2"), wooteco.chess.domain.player.Team.WHITE);
        blackPawn = Pawn.of(Position.of("A7"), wooteco.chess.domain.player.Team.BLACK);
        boardDto = new HashMap<>();
        boardSituation = BoardSituation.of(boardDto);
    }

    @Test
    @DisplayName("Pawn은 바로 앞에 기물이 있는 경우 전진할 수 없음")
    void moveToAlly() {
        boardDto.put(Position.of("B4"), whiteTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThatThrownBy(() -> whitePawn.move(Position.of("B4"), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("직선으로 진행할 때 진행 타겟에 적군이 있는 경우 예외 발생")
    void frontMoveToEnemy() {
        boardDto.put(Position.of("B4"), blackTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThatThrownBy(() -> whitePawn.move(Position.of("B4"), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("직선 진행 타겟에 아무것도 없는 경우 이동 가능")
    void moveToEmpty() {
        assertThat(whitePawn.move(Position.of("B4"), boardSituation))
            .isInstanceOf(Pawn.class);
    }

    @Test
    @DisplayName("대각선으로 진행할 때 진행 타겟에 적군이 있는 경우 이동 가능")
    void diagonalMoveToEnemy() {
        boardDto.put(Position.of("C3"), blackTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThat(whitePawn.move(Position.of("C3"), boardSituation))
            .isInstanceOf(Pawn.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"C4", "D5", "A4"})
    @DisplayName("진행 규칙에 어긋나는 경우 예외 발생")
    void movePolicyException(String input) {
        assertThatThrownBy(() -> whitePawn.move(Position.of(input), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("진행 타겟에 적군이 있지만 진행 규칙에 어긋나는 경우 예외 발생")
    void moveToEnemyException() {
        boardDto.put(Position.of("D4"), blackTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThatThrownBy(() -> whitePawn.move(Position.of("D4"), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"B3", "B4"})
    @DisplayName("직선으로 진행할 때 진행 타겟에 적군이 있는 경우 예외 발생")
    void frontMoveToEnemy(String target) {
        boardDto.put(Position.of(target), blackTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThatThrownBy(() -> whitePawn.move(Position.of(target), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("직선으로 2칸 진행할 때 진행 경로에 적군이 있는 경우")
    void frontMoveObstacle() {
        boardDto.put(Position.of("B3"), blackTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThatThrownBy(() -> whitePawn.move(Position.of("B4"), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"B3", "B4"})
    @DisplayName("직선 진행 타겟에 아무것도 없는 경우 이동 가능")
    void moveToEmpty(String target) {
        assertThat(whitePawn.move(Position.of(target), boardSituation))
            .isInstanceOf(Pawn.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"D6", "A5"})
    @DisplayName("진행 타겟에 적군이 있지만 진행 규칙에 어긋나는 경우 예외 발생")
    void moveToEnemyException(String target) {
        boardDto.put(Position.of(target), blackTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThatThrownBy(() -> whitePawn.move(Position.of(target), boardSituation))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void movablePositions() {
        boardDto.put(Position.of("A4"), whiteTeam);
        boardSituation = BoardSituation.of(boardDto);
        List<Position> positions = blackPawn.getMovablePositions(boardSituation);
        assertThat(positions).contains(Position.of("A6"), Position.of("A5"));
    }

    @Test
    void attackablePositions() {
        boardDto.put(Position.of("c3"), blackTeam);
        boardSituation = BoardSituation.of(boardDto);
        List<Position> positions = whitePawn.getMovablePositions(boardSituation);
        assertThat(positions).contains(Position.of("c3"));
    }

    @Test
    void score() {
        boardDto.put(Position.of("B4"), whiteTeam);
        boardDto.put(Position.of("B2"), whiteTeam);
        boardSituation = BoardSituation.of(boardDto);
        assertThat(whitePawn.getPoint(boardSituation)).isEqualTo(0.5d);
    }
}
