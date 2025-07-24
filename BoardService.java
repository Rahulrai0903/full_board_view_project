
// BoardService.java
import java.sql.*;
import java.util.*;

public class BoardService {
    public BoardView getFullBoardView(int boardId) throws Exception {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/kanban", "user", "password");
        String sql = "SELECT b.id AS board_id, c.id AS column_id, c.name AS column_name, c.order_on_board, " +
                     "t.id AS task_id, t.title AS task_title, t.order_in_column " +
                     "FROM board b JOIN column c ON c.board_id = b.id " +
                     "LEFT JOIN task t ON t.column_id = c.id " +
                     "WHERE b.id = ? ORDER BY c.order_on_board, t.order_in_column";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, boardId);
        ResultSet rs = stmt.executeQuery();

        Map<Integer, ColumnView> columnMap = new LinkedHashMap<>();
        BoardView boardView = new BoardView();
        boardView.boardId = boardId;
        boardView.columns = new ArrayList<>();

        while (rs.next()) {
            int colId = rs.getInt("column_id");
            ColumnView col = columnMap.getOrDefault(colId, null);
            if (col == null) {
                col = new ColumnView();
                col.columnId = colId;
                col.name = rs.getString("column_name");
                col.tasks = new ArrayList<>();
                columnMap.put(colId, col);
                boardView.columns.add(col);
            }
            int taskId = rs.getInt("task_id");
            if (taskId > 0) {
                TaskView task = new TaskView();
                task.taskId = taskId;
                task.title = rs.getString("task_title");
                col.tasks.add(task);
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        return boardView;
    }
}
