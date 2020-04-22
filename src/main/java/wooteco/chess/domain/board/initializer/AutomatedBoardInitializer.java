package wooteco.chess.domain.board.initializer;

import wooteco.chess.domain.board.BoardInitializer;
import wooteco.chess.domain.piece.PieceState;
import wooteco.chess.domain.piece.implementation.piece.Bishop;
import wooteco.chess.domain.piece.implementation.piece.King;
import wooteco.chess.domain.piece.implementation.piece.Knight;
import wooteco.chess.domain.piece.implementation.piece.Pawn;
import wooteco.chess.domain.piece.implementation.piece.Queen;
import wooteco.chess.domain.piece.implementation.piece.Rook;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.File;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.Rank;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class AutomatedBoardInitializer implements BoardInitializer {

    private static final Set<Rank> PAWN_RANKS = new HashSet<>();
    private static final EnumMap<Rank, Team> RANK_TEAM = new EnumMap<>(Rank.class);
    private static final EnumMap<File, BiFunction<Position, Team, PieceState>> FILE_PIECE = new EnumMap<>(File.class);

    static {
        RANK_TEAM.put(Rank.EIGHT, Team.BLACK);
        RANK_TEAM.put(Rank.SEVEN, Team.BLACK);
        RANK_TEAM.put(Rank.TWO, Team.WHITE);
        RANK_TEAM.put(Rank.ONE, Team.WHITE);
        PAWN_RANKS.add(Rank.SEVEN);
        PAWN_RANKS.add(Rank.TWO);
        FILE_PIECE.put(File.A, (Rook::of));
        FILE_PIECE.put(File.B, (Knight::of));
        FILE_PIECE.put(File.C, (Bishop::of));
        FILE_PIECE.put(File.D, (Queen::of));
        FILE_PIECE.put(File.E, (King::of));
        FILE_PIECE.put(File.F, (Bishop::of));
        FILE_PIECE.put(File.G, (Knight::of));
        FILE_PIECE.put(File.H, (Rook::of));
    }

    @Override
    public Map<Position, PieceState> create() {
        Map<Position, PieceState> board = new HashMap<>();
        for (Rank rank : RANK_TEAM.keySet()) {
            createPiece(rank, board);
        }
        return board;
    }

    private void createPiece(Rank rank, Map<Position, PieceState> board) {
        for (File file : File.values()) {
            Position position = Position.of(file, rank);
            board.put(position, createPiece(position));
        }
    }

    private PieceState createPiece(Position position) {
        Rank rank = position.getRank();
        File file = position.getFile();
        Team team = RANK_TEAM.get(rank);
        if (PAWN_RANKS.contains(rank)) {
            return Pawn.of(position, team);
        }
        return FILE_PIECE.get(file).apply(position, team);
    }
}
