import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TicTacToeFrame extends JFrame {
    private final int ROW = 3;
    private final int COL = 3;
    private final int MOVES_FOR_WIN = 5;
    private final int MOVES_FOR_TIE = 7;
    private String[][] board = new String[ROW][COL];
    boolean playing = true;
    String player = "X";
    int moveCnt = 0;

    JPanel mainPnl;
    JPanel boardPnl;
    JPanel controlPnl;

    TicTacToeTile[][] tiles = new TicTacToeTile[ROW][COL];
    JButton quitBtn;

    public TicTacToeFrame() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);

        // get screen dimensions
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        // center frame in screen
        setLocation((screenWidth - getWidth()) / 2, (screenHeight - getHeight()) / 2);

        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());

        createBoardPanel();
        mainPnl.add(boardPnl, BorderLayout.CENTER);

        createControlPanel();
        mainPnl.add(controlPnl, BorderLayout.SOUTH);

        add(mainPnl);

        clearBoard(); // Initialize the board for the first game

        setVisible(true);
    }

    private void createBoardPanel() {
        boardPnl = new JPanel();
        boardPnl.setLayout(new GridLayout(3, 3));

        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                tiles[row][col] = new TicTacToeTile(row, col);
                tiles[row][col].setFont(new Font("Arial", Font.BOLD, 50));
                tiles[row][col].addActionListener((e) -> tileClicked(e));
                boardPnl.add(tiles[row][col]);
            }
        }
    }

    public void tileClicked (ActionEvent e) {
        TicTacToeTile clickedTile = (TicTacToeTile) e.getSource();
        int row = clickedTile.getRow();
        int col = clickedTile.getCol();

        clickedTile.setText(player);
        clickedTile.setEnabled(false);

        board[row][col] = player;
        moveCnt++;

        if(moveCnt >= MOVES_FOR_WIN) {
            if(isWin(player)) {
                JOptionPane.showMessageDialog(mainPnl,"Player " + player + " wins!");
                playing = false;
            }
        }

        if(playing && moveCnt >= MOVES_FOR_TIE) {
            if(isTie()) {
                JOptionPane.showMessageDialog(mainPnl,"It's a Tie!");
                playing = false;
            }
        }

        // Switch player
        if(player.equals("X")) {
            player = "O";
        }
        else {
            player = "X";
        }

        if (!playing) {
            int result = JOptionPane.showConfirmDialog(mainPnl, "Would you like to play again?",
                    "Play Again?", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                clearBoard();  // Restart the game if player wants to play again
                player = "X";
                playing = true;
            } else {
                System.exit(0);  // Exit the application if player chooses not to play again
            }
        }
    }

    private void createControlPanel() {
        controlPnl = new JPanel();

        quitBtn = new JButton("Quit!");
        quitBtn.addActionListener((ActionEvent ae) -> System.exit(0));

        controlPnl.add(quitBtn);
    }

    private void clearBoard()
    {
        // sets all the board elements to a space
        for(int row=0; row < ROW; row++)
        {
            for(int col=0; col < COL; col++)
            {
                tiles[row][col].setText(" ");
                tiles[row][col].setEnabled(true);
                board[row][col] = " ";
            }
        }
    }

    private boolean isWin(String player)
    {
        if(isColWin(player) || isRowWin(player) || isDiagonalWin(player))
        {
            return true;
        }

        return false;
    }

    private boolean isColWin(String player)
    {
        // checks for a col win for specified player
        for(int col=0; col < COL; col++)
        {
            if(board[0][col].equals(player) &&
                    board[1][col].equals(player) &&
                    board[2][col].equals(player))
            {
                return true;
            }
        }
        return false; // no col win
    }

    private boolean isRowWin(String player)
    {
        // checks for a row win for the specified player
        for(int row=0; row < ROW; row++)
        {
            if(board[row][0].equals(player) &&
                    board[row][1].equals(player) &&
                    board[row][2].equals(player))
            {
                return true;
            }
        }
        return false; // no row win
    }

    private boolean isDiagonalWin(String player)
    {
        // checks for a diagonal win for the specified player
        if(board[0][0].equals(player) &&
                board[1][1].equals(player) &&
                board[2][2].equals(player) )
        {
            return true;
        }
        if(board[0][2].equals(player) &&
                board[1][1].equals(player) &&
                board[2][0].equals(player) )
        {
            return true;
        }
        return false;
    }

    // checks for a tie before board is filled.
    // check for the win first to be efficient
    private boolean isTie()
    {
        boolean xFlag = false;
        boolean oFlag = false;
        // Check all 8 win vectors for an X and O so
        // no win is possible
        // Check for row ties
        for(int row=0; row < ROW; row++)
        {
            if(board[row][0].equals("X") ||
                    board[row][1].equals("X") ||
                    board[row][2].equals("X"))
            {
                xFlag = true; // there is an X in this row
            }
            if(board[row][0].equals("O") ||
                    board[row][1].equals("O") ||
                    board[row][2].equals("O"))
            {
                oFlag = true; // there is an O in this row
            }

            if(! (xFlag && oFlag) )
            {
                return false; // No tie can still have a row win
            }

            xFlag = oFlag = false;

        }
        // Now scan the columns
        for(int col=0; col < COL; col++)
        {
            if(board[0][col].equals("X") ||
                    board[1][col].equals("X") ||
                    board[2][col].equals("X"))
            {
                xFlag = true; // there is an X in this col
            }
            if(board[0][col].equals("O") ||
                    board[1][col].equals("O") ||
                    board[2][col].equals("O"))
            {
                oFlag = true; // there is an O in this col
            }

            if(! (xFlag && oFlag) )
            {
                return false; // No tie can still have a col win
            }
        }
        // Now check for the diagonals
        xFlag = oFlag = false;

        if(board[0][0].equals("X") ||
                board[1][1].equals("X") ||
                board[2][2].equals("X") )
        {
            xFlag = true;
        }
        if(board[0][0].equals("O") ||
                board[1][1].equals("O") ||
                board[2][2].equals("O") )
        {
            oFlag = true;
        }
        if(! (xFlag && oFlag) )
        {
            return false; // No tie can still have a diagonal win
        }
        xFlag = oFlag = false;

        if(board[0][2].equals("X") ||
                board[1][1].equals("X") ||
                board[2][0].equals("X") )
        {
            xFlag =  true;
        }
        if(board[0][2].equals("O") ||
                board[1][1].equals("O") ||
                board[2][0].equals("O") )
        {
            oFlag =  true;
        }
        if(! (xFlag && oFlag) )
        {
            return false; // No tie can still have a diagonal win
        }

        // Checked every vector so I know I have a tie
        return true;
    }
}