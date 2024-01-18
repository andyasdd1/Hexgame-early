/*
 * Copyright 2012 David Pearson. BSD License.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * An AI that uses Monte Carlo Tree Search to play Hex.
 *
 * @author David Pearson
 */
public class MCAI extends AI {
    private int aiplayer = 0;
    private int minLen = Integer.MAX_VALUE;
    private Location lastPlayed;
    public int diffLevel = 75;
    private int otherplayer = 0;
    private int count = 0;
    private static final int MAX_VALUE = Integer.MAX_VALUE;
    private static int DEFAULT_MIN_LEN = 1000;
    private int numRows = -1;
    private int numCols = -1;
    private int lastaidistance = Integer.MAX_VALUE;
    private int lastplayerdistance = Integer.MAX_VALUE;
    private List<Location> availblebridgeMoves = new ArrayList<>();
    private ArrayList<Location> checkforLocations = new ArrayList<>();

    /**
     * The default constructor. Assumes that the player is 1.
     */
    public MCAI() {
    }

    /**
     * Creates a new instance of MCAI.
     *
     * @param player
     *               The color to play as (see Constants.java)
     */
    public MCAI(int player) {
        aiplayer = player;

    }

    /**
     * Gets the color this AI is playing as.
     *
     * @return The color that the AI is playing as (see Constants.java)
     */
    public int getPlayerCode() {
        return aiplayer;
    }

    public int getotherplayercode() {
        if (aiplayer == 1) {
            otherplayer = 2;
            aiplayer = 1;
        } else
            otherplayer = 1;
        aiplayer = 2;
        return otherplayer;
    }

    public static void printBoard(List<Location> locations) {
        int[][] board = new int[7][7];

        // Process each location in the list
        for (Location loc : locations) {
            if (loc.x >= 0 && loc.x < 7 && loc.y >= 0 && loc.y < 7) {
                board[loc.y][loc.x] += 1; // Increment the count at this location
            }
        }

        // Assuming board is a 7x7 matrix
        if (board == null || board.length != 7 || board[0].length != 7) {
            System.out.println("Invalid board size.");
            return;
        }

        // Print the board
        for (int y = 0; y < 7; y++) {
            // Print trailing spaces for alignment
            for (int space = 0; space < 6 - y; space++) {
                System.out.print(" ");
            }

            // Print each cell in the row
            for (int x = 0; x < 7; x++) {
                System.out.print(board[y][x] + " ");
            }
            System.out.println();
        }
    }




    // Helper method to check if a location is in the list
    private boolean isLocationInList(Location loc, ArrayList<Location> list) {
        for (Location listItem : list) {
            if (loc.equals(listItem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the n-completion value for a game board state. This is (more or
     * less) a nice wrapper around calcN.
     *
     * @param board
     *              The board to calculate based on
     * @return The n-completion value for the board state provided
     */
    public double calcVal(int[][] board, int player) {
        double minDistanceToWin = Double.MAX_VALUE;


        int startEdge = (player == 1) ? 0 : -1;
        int endEdge = (player == 1) ? board[0].length - 1 : -1;


        for (int i = 0; i < board.length; i++) {
            int edgePosition = (player == 1) ? i : 0;
            Location startLoc = (player == 1) ? new Location(edgePosition, startEdge)
                    : new Location(startEdge, edgePosition);


            if (board[0][0] == player) {
                //System.out.println("not for you right?");
                double distance = 10;
                minDistanceToWin = Math.min(minDistanceToWin, distance);
            }
        }
        //System.err.println("what' the number?" + minDistanceToWin);
        return minDistanceToWin;
    }

    public static void printChainLocations(Integer key, Chain chain) {
        System.out.println("Chain for key " + key + ":");
        for (Location location : chain.locations) {
            System.out.println(location + " here the locations!");
        }
    }

    public static void printAllChainLocations(Map<Integer, List<Chain>> chainsMap) {
        for (Map.Entry<Integer, List<Chain>> entry : chainsMap.entrySet()) {
            Integer key = entry.getKey();
            List<Chain> chainsList = entry.getValue();

            System.out.println("Processing chains for key " + key + ":");
            for (Chain chain : chainsList) {
                printChainLocations(key, chain);
            }
        }
    }


    private int determineDirection(int touching, int player) {
        if (player == 1) {
            if (touching == 1)
                return 2; // If touching top, calculate distance to bottom
            if (touching == 2)
                return 1; // If touching bottom, calculate distance to top
        } else if (player == 2) {
            if (touching == 3)
                return 4; // If touching left, calculate distance to right
            if (touching == 4)
                return 3; // If touching right, calculate distance to left
        }
        return 0;
    }

    /**
     * Chooses a location for the next play by this AI.
     *
     * @param board The board state used in determining the play location
     * @param last  The last play in the game, as made by the opponent
     * @return A location to play at, chosen using MCTS
     */
    public Location getPlayLocation(int[][] board, Location last) {
        System.out.println("*******************");

        long t = System.currentTimeMillis();

        lastPlayed = last;

        // Hardcoded first moves for AI players

        if (count == 0) {
            count++;
            if (aiplayer == 1) {
                //return new Location(3, 3); // middle of the board
            } else if (aiplayer == 2) {
                //return last.getAdjacentLocations().get(0); // adjacent to opponent's move
            }
        }

        // Using Minimax evaluation for all possible moves on the board
        Double bestScore = -1.0;
        Location bestMove = new Location(-1, -1);

        // Assuming a fixed depth of 3 for the example, but it should be adjusted based
        // on your game specifics
        int depth = 3;

        int[][] boardscores = evaluateBoard(board);

        /**
         * *
         * 在这里写
         *
         *
         *
         *
         */
        bestMove=minimax(board);
        //System.out.println(bestMove);
        //minimax(boardscores, depth, false, depth, t, null);
        // Somewhere in your game logic, you call the minimax function:
        //MinimaxResult result = minimax(board, depth, true, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY,
        //bestline);
        // Now you can access the best move from the result
        //bestMove = result.move; // This holds the best move determined by the minimax algorithm
        //bestScore = result.score;
        Location bestMove1 = new Location(-1, -1);
        //bestMove1.x=bestMove.y;
        //bestMove1.y=bestMove.x;
        return bestMove;

    }
    public Location minimax(int[][] board) {
        // 第一部分：遍历每个空闲位置
        int[][] boardScores = evaluateBoard(board);

        //Location bestMove = new Location(-1, -1);
        Location bestMove=findMaxScore1(boardScores);
        printBoard2(board);
        printBoard2(boardScores);
        System.out.println(bestMove);


        return bestMove;

    }

    private Location findMaxScore1(int[][] boardScores) {
        Location bestMove = new Location(-1, -1);
        int maxScore = Integer.MIN_VALUE;
        for (int i = 0; i < boardScores.length; i++) {
            for (int j = 0; j < boardScores[i].length; j++) {
                if (boardScores[i][j] > maxScore  ) {
                    maxScore=boardScores[i][j];
                    bestMove = new Location(j, i);
                    //bestMove.x=j;
                    //bestMove.y=i;
                }
            }
        }
        return bestMove;
    }

    public Location minimax1(int[][] board) {
        int bestScore = Integer.MIN_VALUE;
        Location bestMove = new Location(-1, -1);
        Location frist_move = new Location(-1, -1);
        Location second_move = new Location(-1, -1);


        // 第一部分：遍历每个空闲位置
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) { // 空闲位置
                    board[i][j] = 1; // 第一步，AI下在这里

                    int worstScoreForThisMove = Integer.MAX_VALUE;

                    // 第二部分：对于每个第一步的位置，再次遍历所有空闲位置
                    for (int k = 0; k < board.length; k++) {
                        for (int l = 0; l < board[k].length; l++) {
                            if (board[k][l] == 0) {
                                board[k][l] = 2; // 假设对手也是AI，也下在这里

                                // 第三部分：评估这个板的得分
                                int[][] boardScores = evaluateBoard(board);
                                int currentScore = findMaxScore(boardScores);

                                // 第四部分：对于第一步，选择第二步中分数最低的
                                if (currentScore < worstScoreForThisMove) {
                                    second_move.x=l;
                                    second_move.y=k;
                                    worstScoreForThisMove = currentScore;
                                }

                                board[k][l] = 0; // 撤销第二步
                            }
                        }
                    }

                    // 第五部分：在所有第一步的选择中选出分数最高的
                    if (worstScoreForThisMove > bestScore) {
                        frist_move.x=j;
                        frist_move.y=i;

                        bestScore = worstScoreForThisMove;
                        bestMove = new Location(i, j);
                    }

                    board[i][j] = 0; // 撤销第一步
                }
            }
        }
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (board[i][j]==0) {
                    board[i][j] = 1;
                    for (int k = 0; k < 7; k++) {
                        for (int l = 0; l < 7; l++) {
                            if (board[k][l]==0) {
                                board[k][l] = 2;
                                int[][] boardScores = evaluateBoard(board);
                                System.out.println("下完两步之后的分数");
                                printBoard2(boardScores);

                                boardScores[i][j] = -2;
                                boardScores[k][l] = -1;
                                printBoard2(boardScores);

                                board[k][l] = 0;

                            }
                        }
                    }
                    board[i][j] = 0;
                }

            }
        }
        /**

        board[frist_move.y][frist_move.x]=1;
        board[second_move.y][second_move.x]=2;
        int[][] boardScores = evaluateBoard(board);
        System.out.println("下完两步之后的分数");
        //printBoard2(boardScores);

        boardScores[frist_move.y][frist_move.x]=-2;
        boardScores[second_move.y][second_move.x]=-1;
        printBoard2(boardScores);

        board[frist_move.y][frist_move.x]=0;
        board[second_move.y][second_move.x]=0;
         */



        return bestMove;
    }

    // 辅助方法：在棋盘评分中找到最高分
    private int findMaxScore(int[][] boardScores) {
        int maxScore = Integer.MIN_VALUE;
        for (int i = 0; i < boardScores.length; i++) {
            for (int j = 0; j < boardScores[i].length; j++) {
                if (boardScores[i][j] > maxScore) {
                    maxScore = boardScores[i][j];
                }
            }
        }
        return maxScore;
    }




    public class MinimaxResult {
        public double score;
        public Location move;

        public MinimaxResult(double score) {
            this.score = score;
        }

        public MinimaxResult(double score, Location move) {
            this.score = score;
            this.move = move;
        }
    }

    public int[][] scoreChainProximityToEdge(int[][] board, List<Chain> playerChains, List<Chain> aiChains) {
        int[][] scoreBoard = new int[board.length][board[0].length];
        //printChainLocations(aiChains);
        //printChainLocations(playerChains);
        // Score AI chains

        for (Chain chain : aiChains) {
            scoreChain(chain, scoreBoard,board, 2); // 2 for AI
        }

        // Score Player chains
        for (Chain chain : playerChains) {
            scoreChain(chain, scoreBoard,board, 1); // 1 for Player
        }

        return scoreBoard;
    }

    private void scoreChain(Chain chain, int[][] scoreBoard, int[][] board, int player) {
        Location firstClosest = null, secondClosest = null;
        int firstClosestDistance = Integer.MAX_VALUE, secondClosestDistance = Integer.MAX_VALUE;

        // Find the first and second closest pieces to the edge
        for (Location loc : chain.locations) {
            int distance = calculateDistanceToEdge(loc, player, board.length, board[0].length);
            if (distance < firstClosestDistance) {
                secondClosest = firstClosest;
                secondClosestDistance = firstClosestDistance;
                firstClosest = loc;
                firstClosestDistance = distance;
            } else if (distance < secondClosestDistance) {
                secondClosest = loc;
                secondClosestDistance = distance;
            }
        }

        // Score around the first and second closest locations
        if (firstClosest != null) {
            scoreAroundLocation(firstClosest, scoreBoard, player, true);
        }
        if (secondClosest != null) {
            scoreAroundLocation(secondClosest, scoreBoard, player, false);
        }
    }

    private void scoreAroundLocation(Location loc, int[][] scoreBoard, int player, boolean isFirstClosest) {
        ArrayList<Location> adjacentLocations = loc.getAdjacentLocations();

        int scoreAhead = isFirstClosest ? (player == 1 ? 2000 : 1000) : (player == 1 ? 1800 : 800);
        int scoreAround = isFirstClosest ? (player == 1 ? 200 : 100) : (player == 1 ? 180 : 80);

        for (Location adjLoc : adjacentLocations) {
            if (isWithinBoard(adjLoc.x, adjLoc.y, scoreBoard)) {
                //System.out.println("player: "+player);
                //System.out.println("adjloc: "+adjLoc.x + adjLoc.y);
                //System.out.println("loc: "+loc.x + loc.y);
                //System.out.println("truefalse?: "+ isAhead(adjLoc, loc, player));
                //System.out.println("scores: "+scoreAhead);
                if (isAhead(adjLoc, loc, player)) {
                    scoreBoard[adjLoc.y][adjLoc.x] += scoreAhead;

                } else {
                    scoreBoard[adjLoc.y][adjLoc.x] += scoreAround;
                }
            }
        }
    }

    private boolean isAhead(Location adjLoc, Location loc, int player) {
        //System.out.println("adjlocgg: "+ adjLoc.y);
        //System.out.println("locgg: "+ loc.y);
        if (player == 1) { // Player 1 (top to bottom)
            return adjLoc.y == loc.y - 1 || adjLoc.y == loc.y + 1;
        } else { // Player 2 (left to right)
            return adjLoc.x == loc.x - 1 || adjLoc.x == loc.x + 1;
        }
    }

    private int calculateDistanceToEdge(Location loc, int player, int numRows, int numCols) {
        if (player == 1) { // Player 1 (top to bottom)
            return Math.min(loc.y, numRows - 1 - loc.y);
        } else { // Player 2 (left to right)
            return Math.min(loc.x, numCols - 1 - loc.x);
        }
    }

    public int[][] scorePaths(int[][] board) {
        int[][] scoreBoard = new int[board.length][board[0].length];

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == 1 || board[y][x] == 2) {
                    boolean isPlayer = board[y][x] == 1;
                    List<Location> path = calculatePath(x, y, board, isPlayer);
                    int scoreValue = isPlayer ? 10 : 50;  // Adjusted score values

                    // Score the path
                    for (Location loc : path) {
                        scoreBoard[loc.y][loc.x] += scoreValue;
                        //applyDeductionsForBlocks(x, y, loc.x, loc.y, board, scoreBoard, !isPlayer);
                    }

                }
            }
        }

        return scoreBoard;
    }

    private List<Location> calculatePath(int x, int y, int[][] board, boolean isPlayer) {
        List<Location> path = new ArrayList<>();
        int targetY = isPlayer ? (y <= board.length / 2 ? 0 : board.length - 1) : y; // Top or bottom edge for player
        int targetX = isPlayer ? x : (x <= board[0].length / 2 ? 0 : board[0].length - 1); // Left or right edge for AI

        // Determine the step direction for x and y based on the target
        int stepX = (x < targetX) ? 1 : (x > targetX) ? -1 : 0;
        int stepY = (y < targetY) ? 1 : (y > targetY) ? -1 : 0;

        // Calculate the path to the edge
        while (x != targetX || y != targetY) {
            if (isPlayer) {
                // Player moves vertically
                y += stepY;
            } else {
                // AI moves horizontally
                x += stepX;
            }
            path.add(new Location(x, y));
        }

        return path;
    }

    private boolean isWithinBoard(int x, int y, int[][] board) {
        return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
    }

    private void applyDeductionsForBlocks(int origX, int origY, int currX, int currY, int[][] board, int[][] scoreBoard, boolean isAI) {
        int[][] directions = {{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, 0}, {1, -1}};
        boolean isDirectlyBlocked = false;
        boolean isIndirectlyBlocked = false;

        // Check for direct block adjacent to the original location
        for (int[] dir : directions) {
            int newX = origX + dir[0];
            int newY = origY + dir[1];

            if (isWithinBoard(newX, newY, board) && board[newY][newX] == (isAI ? 2 : 1)) {
                isDirectlyBlocked = true;
                break;
            }
        }

        // If no direct block, check for indirect block along the path
        if (!isDirectlyBlocked) {
            int manhattanDistance = calculateManhattanDistance(origX, origY, isAI, board.length, board[0].length);
            isIndirectlyBlocked = isPathBlocked(origX, origY, board, directions, manhattanDistance, isAI);
        }

        // Apply deductions based on blockage type
        if (isDirectlyBlocked) {
            scoreBoard[currY][currX] -= 80; // Higher penalty for direct block
        } else if (isIndirectlyBlocked) {
            scoreBoard[currY][currX] -= 10; // Lower penalty for indirect block
        }
    }
    private int calculateManhattanDistance(int x, int y, boolean isAI, int numRows, int numCols) {
        // Calculate Manhattan distance from the edges based on AI or player
        if (isAI) { // AI (left to right)
            return Math.min(x, numCols - 1 - x);
        } else { // Player (top to bottom)
            return Math.min(y, numRows - 1 - y);
        }
    }

    private boolean isPathBlocked(int startX, int startY, int[][] board, int[][] directions, int distance, boolean isAI) {
        int opponent = isAI ? 2 : 1; // Opponent's piece identifier

        // Check each direction for indirect blocks
        for (int[] dir : directions) {
            int currentX = startX;
            int currentY = startY;
            int steps = 0;

            while (steps < distance) {
                currentX += dir[0];
                currentY += dir[1];

                if (!isWithinBoard(currentX, currentY, board)) {
                    break; // Exit if outside the board
                }

                if (board[currentY][currentX] == opponent) {
                    return true; // Indirect block found
                }

                steps++;
                // Adjust steps for diagonal movement
                if (Math.abs(dir[0]) + Math.abs(dir[1]) == 2) {
                    steps++;
                }
            }
        }

        return false; // No indirect block found
    }





    public int[][] initializeScoreBoard(int[][] board) {
        int[][] scoreBoard = new int[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) { // Empty cell
                    scoreBoard[i][j] = 0;
                } else { // Occupied cell (either AI or player)
                    scoreBoard[i][j] = -9998;
                }
            }
        }

        return scoreBoard;
    }


    public int[][] evaluateBoard(int[][] board) {

        int numRows = board.length;
        int numCols = board[0].length;
        int[][] totalScore = new int[numRows][numCols]; // This will initialize all scores to 0.
        Map<Integer, List<Chain>> allthelines = getallthelines(board);
        //printAllChainLocations(allthelines);

        List<Chain> aiChain =  allthelines.get(2);
        List<Chain> playerChain =  allthelines.get(1);
        //System.out.println("size" + aiChain.size());
        //System.out.println("sizeplayer" + playerChain.size());
        // Other methods assumed to return int[][] with the same dimensions, with some cells potentially uninitialized.
        int[][] initial = initializeScoreBoard(board); // This method must initialize all values.
        int[][] pathScore = scorePaths(board); // Make sure this method doesn't leave uninitialized cells.
        int[][] distanceScore = scoreDistance(board); // Make sure this method doesn't leave uninitialized cells.
        int[][] adjacentScore = scoreExtensionMoves(board, aiChain, playerChain); // Check this method too.
        int[][] bridgeScore = scoreBlockingMoves(board, playerChain, aiChain); // And this one.
        int[][] blocktopways = scoreChainProximityToEdge(board, playerChain, aiChain);
        int[][] edgescore = scoreChainEdgesAndConnections(board, playerChain, aiChain);
        int[][] oneStepWinScore = oneSteptoWin(board, aiChain, playerChain); // Finally, check this method.

        // Combine the scores from different methods
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                totalScore[i][j] = initial[i][j]; // Start with the initial score.
                //totalScore[i][j] += pathScore[i][j]; // Add the path score.
                totalScore[i][j] += distanceScore[i][j];
                totalScore[i][j] += adjacentScore[i][j]; // Add the score for adjacent moves.
                //totalScore[i][j] += bridgeScore[i][j]; // Add the bridge score.
                totalScore[i][j] += edgescore[i][j];
                //totalScore[i][j] += blocktopways[i][j];
                totalScore[i][j] += oneStepWinScore[i][j]; // Add the one-step-to-win score.
            }
        }
        //printBoard2(blocktopways);
        //printBoard2(totalScore);
        // Return the total scores combined
        return totalScore;
    }

    public int[][] scoreDistance(int[][] board){
        int[][] scoreBoard = new int[board.length][board[0].length];

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {

                if (board[y][x] == 0 ) {
                    board[y][x]=2;

                    List<List<Location>> AllLines=findAllLines(board);

                    for (List<Location> oneLine:AllLines
                         ) {
                        System.out.println(AllLines);
                        int distance=minDistanceLine(board,oneLine);

                        System.out.println(distance);
                        scoreBoard[y][x]=(100-distance)*1000;
                    }
                    board[y][x]=0;




                }
            }
        }

        return scoreBoard;
    }

    public static List<List<Location>> findAllLines(int[][] board) {
        List<List<Location>> allLines = new ArrayList<>();

        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                // 只关注AI的棋子，即值为2的位置
                if (board[y][x] == 2) {
                    // 检查周围的元素
                    //下→↘左上 上←
                    //int[][] directions = {{0, 1}, {1, 0}, {1, 1}, {-1, -1}, {0, -1}, {-1, 0}};
                    int[][] directions = {{-1, -1}, {0, -1}, {-1, 0}};
                    int isNoAdj=1;
                    for (int[] dir : directions) {
                        int newX = x + dir[0];
                        int newY = y + dir[1];
                        if (newX >= 0 && newY >= 0 && newX < board[y].length && newY < board.length) {
                            // 检查相邻位置是否也是AI的棋子
                            //*
                            if (board[newY][newX] == 2) {
                                isNoAdj=0;
                                //如果没加过就是null加入newxy
                                //加过就是newxy新的点和原来的组合并
                                /**
                                List<Location> lineContainsXY=containsLine(allLines, new Location(x, y));

                                List<Location> line = new ArrayList<>();
                                line.add(new Location(x, y));
                                line.add(new Location(newX, newY));
                                // 添加前检查是否重复
                                if (lineContainsXY==null) {
                                    //找到newxy所在组加入
                                    allLines.add(line);
                                }else{
                                    //把自己组和newxy合并
                                }
                                 */
                                Location currentLoc = new Location(x, y);
                                Location newLoc = new Location(newX, newY);
                                List<Location> lineContainsCurrent = containsLine(allLines, currentLoc);
                                List<Location> lineContainsNew = containsLine(allLines, newLoc);

                                if (lineContainsCurrent == null ) {
                                    //找到newxy所在组加入
                                    allLines.remove(lineContainsNew);
                                    lineContainsNew.add(currentLoc);
                                    allLines.add(lineContainsNew);
                                } else if (lineContainsCurrent != null) {
                                    //把自己组和newxy合并

                                    allLines.remove(lineContainsNew);
                                    allLines.remove(lineContainsCurrent);

                                    lineContainsCurrent.addAll(lineContainsNew);
                                    lineContainsCurrent.add(currentLoc);
                                    allLines.add(lineContainsCurrent);

                                }
                            }

                        }
                    }
                    if (isNoAdj==1){
                        List<Location> currentLocNewLine=new ArrayList<>();
                        Location currentLoc = new Location(x, y);

                        currentLocNewLine.add(currentLoc);
                        allLines.add(currentLocNewLine);
                    }
                }
            }
        }

        return allLines;
    }

    // 检查allLines是否已包含特定的line
    private static List<Location> containsLine(List<List<Location>> allLines, Location l) {
        for (List<Location> existingLine : allLines) {
            for (Location locationInExistingLine:existingLine){
                if (locationInExistingLine.x==l.x&&locationInExistingLine.y==l.y){
                    return existingLine;
                }
            }

        }
        return null;
    }



    public static List<List<Location>> findAllLines1(int[][] board) {
        List<List<Location>> allLines = new ArrayList<>();
        boolean[][] visited = new boolean[7][7];

        for (int y = 0; y < 7; y++) {
                for (int x = 0; x < 7; x++) {
                        if (board[y][x] == 2 && !visited[y][x]) {
                                List<Location> line = new ArrayList<>();
                                Location current = new Location(x, y);
                                dfs2(board, current, visited, line);
                                if (!line.isEmpty()) {
                                        allLines.add(line);
                                }
                        }
                }
        }

        return allLines;
    }

    private static void dfs2(int[][] board, Location current, boolean[][] visited, List<Location> line) {
        int x = current.x;
        int y = current.y;

        // Check bounds and if the current location is already visited or not AI's piece
        if (x < 0 || y < 0 || x >= board[0].length || y >= board.length || visited[y][x] || board[y][x] != 2) {
                return;
        }

        // Mark the current location as visited
        visited[y][x] = true;
        line.add(current);

        // Get adjacent locations
        ArrayList<Location> adjacentLocations = current.getAdjacentLocations();

        // Visit all adjacent locations
        for (Location adj : adjacentLocations) {
                if (adj.x >= 0 && adj.y >= 0 && adj.x < board[0].length && adj.y < board.length) { // Check bounds for adjacent locations
                        dfs2(board, adj, visited, line);
                }
        }
    }



    public int minDistanceLine(int[][] board,List<Location> lineForDistance){
        int leftDistance=49;
        int rightDistance=49;
        //左边
        for (Location lForDistance:lineForDistance) {
            Location locationNow=new Location(lForDistance.x,lForDistance.y);

            //System.out.println(lForDistance);
            int steps = 0;
            for (int i = 0; i < 49; i++) {

                if (locationNow.x==0){
                    if (steps<=leftDistance){
                        leftDistance=steps;
                    }
                    break;
                }
                steps+=1;
                locationNow=minDistanceLineGoLeft(board,locationNow);
                //走到死路下一个字尝试
                if (locationNow.x==-1){
                    break;
                }
                //走到终点 查看是否更优
                if (locationNow.x==0){
                    if (steps<=leftDistance){
                        leftDistance=steps;
                    }
                    break;
                }
                //if (board)
            }
        }

        //右边
        for (Location lForDistance:lineForDistance) {
            Location locationNow=new Location(lForDistance.x,lForDistance.y);
            int steps = 0;
            for (int i = 0; i < 49; i++) {
                //System.out.println("right");
                //System.out.println(locationNow);
                //System.out.println(locationNow.x);
                if (locationNow.x==board.length-1){
                    if (steps<=rightDistance){
                        rightDistance=steps;
                    }
                    break;
                }
                steps+=1;
                locationNow=minDistanceLineGoRight(board,locationNow);
                //走到死路下一个字尝试
                if (locationNow.x==-1){
                    break;
                }
                //走到终点 查看是否更优
                if (locationNow.x==board.length-1){
                    if (steps<=rightDistance){
                        rightDistance=steps;
                    }
                    break;
                }
                //if (board)
            }
        }
        //System.out.println(leftDistance);
        //System.out.println(rightDistance);
        return leftDistance+rightDistance;
    }

    public Location minDistanceLineGoLeft(int[][] board,Location l){
        if (board[l.y][l.x-1]==0){
            l.x=l.x-1;
        }else {
            l.x=-1;
        }

        return l;
    }

    public Location minDistanceLineGoRight(int[][] board,Location l){
        if (board[l.y][l.x+1]==0){
            l.x=l.x+1;
        }else {
            l.x=-1;
        }

        return l;
    }



    public void printBoard2(int[][] board) {
        if (board == null || board.length != 7 || board[0].length != 7) {
            System.out.println("Invalid board size.");
            return;
        }

        // Print each row of the board
        for (int i = 0; i < board.length; i++) {
            // Print leading spaces for alignment (for hexagonal shape)
            for (int space = 0; space < board.length - i - 1; space++) {
                System.out.print(" ");
            }
            // Print each cell in the row
            for (int j = 0; j < board[i].length; j++) {

                System.out.print(board[i][j] );
                for (int k = 0; k < (6- (Integer.toString(board[i][j])).length()); k++) {
                    System.out.print(" ");
                }
            }
            System.out.println(); // Move to the next line after printing a row
        }
    }


    private boolean isOneStepFromEdge(Location location, int[][] board, int player) {
        int numRows = board.length; // Number of rows in the board
        int numCols = board[0].length; // Number of columns in the board

        if (player == 1) {
            // Player 1 (top to bottom): Check if one step from the bottom edge
            return location.y == numRows - 2;
        } else if (player == 2) {
            // Player 2 (left to right): Check if one step from the right edge
            return location.x == numCols - 2;
        }
        return false;
    }

    // You need to implement these methods
    public boolean gameIsOver(int[][] board) {
        // Check if Player 1 has won by connecting top to bottom
        for (int x = 0; x < board[0].length; x++) {
            if (board[0][x] == 1 && checkend(board, new boolean[board.length][board[0].length], 0, x, 1)) {
                return true; // Player 1 has won
            }
        }

        // Check if Player 2 has won by connecting left to right
        for (int y = 0; y < board.length; y++) {
            if (board[y][0] == 2 && checkend(board, new boolean[board.length][board[0].length], y, 0, 2)) {
                return true; // Player 2 has won
            }
        }

        // No one has won yet
        return false;
    }

    private boolean checkend(int[][] board, boolean[][] visited, int y, int x, int player) {
        // Check bounds
        if (x < 0 || x >= board[0].length || y < 0 || y >= board.length)
            return false;
        // Check if already visited or if it's not the player's piece
        if (visited[y][x] || board[y][x] != player)
            return false;

        // Mark as visited
        visited[y][x] = true;

        // Check if reached the opposite side
        if (player == 1 && y == board.length - 1 || player == 2 && x == board[0].length - 1) {
            return true; // Reached the opposite side
        }

        // Visit all neighbors
        return checkend(board, visited, y - 1, x, player) || // up
                checkend(board, visited, y + 1, x, player) || // down
                checkend(board, visited, y, x - 1, player) || // left
                checkend(board, visited, y, x + 1, player) || // right
                checkend(board, visited, y - 1, x - 1, player) || // up-left
                checkend(board, visited, y + 1, x + 1, player); // down-right
    }

    private void makeMove(int[][] board, Location move, int player) {
        // Place the player's (1 for human, 2 for AI) piece on the board at the move's
        // coordinates
        board[move.y][move.x] = player;
    }

    private void undoMove(int[][] board, Location move) {
        // Remove the piece by resetting the cell back to 0 (empty)
        board[move.y][move.x] = 0;
    }

    private int[][] deepCopy(int[][] original) {
        int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = original[i].clone();
        }
        return result;
    }

    public int[][] oneSteptoWin(int[][] board, List<Chain> aiChains, List<Chain> playerChains) {
        int[][] scoreBoard = new int[board.length][board[0].length];
        int aiWinningScore = 1000; // Large but not maximum integer to avoid overflow
        int playerWinningScore = 2000; // Distinct score for player's winning move

        // Scoring for AI chains
        for (Chain aiChain : aiChains) {
            for (Location hex : aiChain.locations) {
                if (isOneStepFromEdge(hex, board, 2)) { // 2 for AI (left-right)
                    for (int[] dir : new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, -1 }, { -1, 1 } }) {
                        int newX = hex.x + dir[0];
                        int newY = hex.y + dir[1];
                        if (isValidLocation(newX, newY, board) && board[newY][newX] == 0 && (newX == board[0].length - 1 || newX == 0)) {
                            scoreBoard[newY][newX] += aiWinningScore; // One step from winning for AI
                        }
                    }
                }
            }
        }

        // Scoring for Player chains
        for (Chain playerChain : playerChains) {
            for (Location hex : playerChain.locations) {
                if (isOneStepFromEdge(hex, board, 1)) { // 1 for Player (top-down)
                    for (int[] dir : new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, -1 }, { -1, 1 } }) {
                        int newX = hex.x + dir[0];
                        int newY = hex.y + dir[1];
                        if (isValidLocation(newX, newY, board) && board[newY][newX] == 0 && (newY == board.length - 1 || newY == 0)) {
                            scoreBoard[newY][newX] += playerWinningScore; // One step from winning for Player
                        }
                    }
                }
            }
        }

        return scoreBoard;
    }


//player is 1, ai is 2, 1 is top down, 2 is left right

    public int[][] scoreChainEdgesAndConnections(int[][] board, List<Chain> aiChains, List<Chain> playerChains) {
        int[][] scoreBoard = new int[numRows][numCols];

        // Score AI chains
        for (Chain chain : aiChains) {
            scoreChain(board, chain, scoreBoard, 40, 60, 2); // AI chain scoring
        }

        // Score Player chains
        for (Chain chain : playerChains) {
            scoreChain(board, chain, scoreBoard, 50, 70, 1); // Player chain scoring
        }

        return scoreBoard;
    }

    private void scoreChain(int[][] board, Chain chain, int[][] scoreBoard, int bridgeScore, int adjacentScore, int playerCode) {
        int opponentCode = playerCode == 1 ? 2 : 1; // Determine opponent code based on player code

        for (Location loc : chain.locations) {
            if (isEdgeTouching(loc, playerCode) != 0) {
                for (Bridge bridge : getPotentialBridges(loc)) { // Assume getBridges(loc) is the correct method to call
                    if (isBridgeCompletable(board, bridge, opponentCode)) {
                        // Score the bridge's endpoint locations
                        scoreBoard[bridge.l1.y][bridge.l1.x] += bridgeScore;
                        scoreBoard[bridge.l2.y][bridge.l2.x] += bridgeScore;

                        // Score the bridge's mid locations
                        for (Location midLoc : bridge.mids) {
                            scoreBoard[midLoc.y][midLoc.x] += adjacentScore; // Use adjacentScore for mids
                        }
                    }
                }
            }

            // Score adjacent locations
            ArrayList<Location> adjacentLocations = loc.getAdjacentLocations(); // Assuming this method exists
            for (Location adjLoc : adjacentLocations) {
                if (isWithinBoard(adjLoc.x, adjLoc.y, board) && board[adjLoc.y][adjLoc.x] == 0) {
                    // Check if the adjacent location can complete a connection for an extra score
                    if (canCompleteConnection(adjLoc, board, playerCode)) {
                        scoreBoard[adjLoc.y][adjLoc.x] += adjacentScore;
                    }
                }
            }
        }
    }


    private boolean canCompleteConnection(Location adjLoc, int[][] board, int playerCode) {
        // Get all adjacent locations for adjLoc
        ArrayList<Location> adjacentLocations = adjLoc.getAdjacentLocations(); // Assuming this method exists

        // Check if any of the adjacent locations contain a piece of the same playerCode
        for (Location nextLoc : adjacentLocations) {
            if (isWithinBoard(nextLoc.x, nextLoc.y, board) &&
                    board[nextLoc.y][nextLoc.x] == playerCode) {
                return true; // Found a connecting piece of the same player
            }
        }

        return false; // No connecting piece found
    }


    public int[][] scoreExtensionMoves(int[][] board, List<Chain> aiChains, List<Chain> playerChains) {
        int[][] scoreBoard = new int[board.length][board[0].length];

        // Scoring adjacent to AI's pieces in all chains
        for (Chain aiChain : aiChains) {
            for (Location hex : aiChain.locations) {
                List<Location> adjacentLocations = hex.getAdjacentLocations();
                for (Location adjLocation : adjacentLocations) {
                    if (isValidLocation(adjLocation.x, adjLocation.y, board) && board[adjLocation.y][adjLocation.x] == 0) {
                        scoreBoard[adjLocation.y][adjLocation.x] += 1; // Empty cell adjacent to AI
                    }
                }
            }
        }

        // Scoring adjacent to Player's pieces in all chains
        for (Chain playerChain : playerChains) {
            for (Location hex : playerChain.locations) {
                List<Location> adjacentLocations = hex.getAdjacentLocations();
                for (Location adjLocation : adjacentLocations) {
                    if (isValidLocation(adjLocation.x, adjLocation.y, board) && board[adjLocation.y][adjLocation.x] == 0) {
                        scoreBoard[adjLocation.y][adjLocation.x] += 10; // Empty cell adjacent to Player
                    }
                }
            }
        }

        return scoreBoard;
    }


    private boolean isValidLocation(int x, int y, int[][] board) {
        return x >= 0 && x < board[0].length && y >= 0 && y < board.length;
    }


    public int[][] scoreBlockingMoves(int[][] board, List<Chain> playerchains, List<Chain> aichains) {
        int[][] scoreBoard = new int[board.length][board[0].length];
        for (Chain aiChain : aichains) {
            // Score blocking AI's bridge formations
            for (Location piece : aiChain.locations) {
                for (Bridge bridge : getPotentialBridges(piece)) {
                    if (isBridgeCompletable(board, bridge, 1)) {
                        for (Location blockingPos : getBlockingPositions(board, bridge)) {
                            if (blockingPos.equals(bridge.l1) || blockingPos.equals(bridge.l2)) {
                                scoreBoard[blockingPos.y][blockingPos.x] += 20; // Increased score for blocking AI's bridge ends
                            } else {
                                scoreBoard[blockingPos.y][blockingPos.x] += 5; // Score for blocking AI's bridge middles
                            }
                        }
                    }
                }
            }
        }
        for (Chain playerChain : playerchains) {
            // Score blocking Player's bridge formations
            for (Location piece : playerChain.locations) {
                for (Bridge bridge : getPotentialBridges(piece)) {
                    if (isBridgeCompletable(board, bridge, 2)) { // Assuming 1 denotes AI
                        for (Location blockingPos : getBlockingPositions(board, bridge)) {
                            if (blockingPos.equals(bridge.l1) || blockingPos.equals(bridge.l2)) {
                                scoreBoard[blockingPos.y][blockingPos.x] += 5; // Score for blocking Player's bridge ends
                            } else {
                                scoreBoard[blockingPos.y][blockingPos.x] += 1; // Increased score for blocking Player's bridge middles
                            }
                        }
                    }
                }
            }
        }
        return scoreBoard;
    }


    private List<Bridge> getPotentialBridges(Location piece) {
        // Assuming piece.getBridges() is already implemented and returns
        // ArrayList<Bridge>
        List<Bridge> a = piece.getBridges();
        //for(int i=0; i < a.size(); i++ ){
        //System.out.println(a.get(i));
        //}

        return piece.getBridges(); // This returns all bridges linked to the piece
    }

    private boolean isBridgeCompletable(int[][] board, Bridge bridge, int opponent) {
        // Check if both ends of the bridge are not occupied by the opponent
        boolean bothEndsNotOpponent = board[bridge.l1.y][bridge.l1.x] != opponent && board[bridge.l2.y][bridge.l2.x] != opponent;

        // Check if the middle location is not occupied by the opponent
        boolean middlesNotOpponent = bridge.mids.stream()
                .allMatch(mid -> board[mid.y][mid.x] != opponent);

        return bothEndsNotOpponent && middlesNotOpponent;
    }

    /*
        private boolean isBridgeCompletable(int[][] board, Bridge bridge, int opponent) {
            // Check if at least one end of the bridge is empty
            boolean atLeastOneEndEmpty = board[bridge.l1.y][bridge.l1.x] == 0 || board[bridge.l2.y][bridge.l2.x] == 0;

            // Check if the other end is empty or occupied by the opponent
            boolean otherEndThreatening = (board[bridge.l1.y][bridge.l1.x] == opponent
                    || board[bridge.l1.y][bridge.l1.x] == 0) &&
                    (board[bridge.l2.y][bridge.l2.x] == opponent || board[bridge.l2.y][bridge.l2.x] == 0);

            // Check if the middle location is empty or occupied by the opponent, which
            // would allow the bridge to be completed
            boolean middlesNotBlocked = bridge.mids.stream()
                    .allMatch(mid -> board[mid.y][mid.x] == 0 || board[mid.y][mid.x] == opponent);

            return atLeastOneEndEmpty && otherEndThreatening && middlesNotBlocked;
        }
    */
    private List<Location> getBlockingPositions(int[][] board, Bridge bridge) {
        List<Location> blockingPositions = new ArrayList<>();
        // Add ends of the bridge as potential blocking positions if they are empty
        if (board[bridge.l1.y][bridge.l1.x] == 0) {
            blockingPositions.add(bridge.l1);
        }
        if (board[bridge.l2.y][bridge.l2.x] == 0) {
            blockingPositions.add(bridge.l2);
        }
        // Add middle locations as potential blocking positions if they are empty
        // In Hex, typically there's only one middle location for a bridge
        for (Location mid : bridge.mids) {
            if (board[mid.y][mid.x] == 0) {
                blockingPositions.add(mid);
            }
        }
        return blockingPositions;
    }

    public boolean isMoveLegal(int[][] board, Location move) {
        // Check if the move is within the bounds of the board
        if (move.x >= 0 && move.x < board.length && move.y >= 0 && move.y < board[0].length) {
            // Check if the move location is unoccupied
            return board[move.y][move.x] == 0;
        }
        // If the move is out of bounds, it's illegal
        return false;
    }

    private Map<Integer, List<Chain>> getallthelines(int[][] board) {
        getotherplayercode();
        getPlayerCode();
        numRows = board.length;
        numCols = board[0].length;

        // Get piece locations
        List<Location> playerLocations = new ArrayList<>();
        List<Location> AILocations = new ArrayList<>();
        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                if (board[j][i] == otherplayer) {
                    Location playerLocation = new Location(i, j);
                    playerLocations.add(playerLocation);
                } else if (board[j][i] == aiplayer) {
                    Location AILocation = new Location(i, j);
                    AILocations.add(AILocation);
                }
            }
        }
        Map<Integer, List<Chain>> chains = evaluateChains(board);
        Map<Integer, List<Integer>> mahatandistance = evaluateDistances(chains);

        // try {
        // printChainLocations(bestchains);
        // } catch (Exception e) {
        // System.err.println("no chains inside!");
        // }

        return chains;
    }



    private Map<Integer, List<Integer>> evaluateDistances(Map<Integer, List<Chain>> playerChains) {
        Map<Integer, List<Integer>> playerDistances = new HashMap<>();

        for (int playerCode : playerChains.keySet()) {
            List<Chain> chains = playerChains.get(playerCode);
            List<Integer> distances = new ArrayList<>();

            for (Chain chain : chains) {
                if (playerCode == 1) { // Player
                    if (chain.start.y == 0 || chain.end.y == numRows - 1) {
                        if (chain.start.y != 0) {
                            chain.startDist = chain.start.y; // Distance to top
                            distances.add(chain.startDist);
                        }
                        if (chain.end.y != numRows - 1) {
                            chain.endDist = numRows - 1 - chain.end.y; // Distance to bottom
                            distances.add(chain.endDist);
                        }
                    } else {
                        chain.startDist = chain.start.y; // Distance to top
                        chain.endDist = numRows - 1 - chain.end.y; // Distance to bottom
                        distances.add(chain.startDist + chain.endDist); // Added together
                    }
                } else if (playerCode == 2) { // AI
                    if (chain.start.x == 0 || chain.end.x == numCols - 1) {
                        if (chain.start.x != 0) {
                            chain.startDist = chain.start.x; // Distance to left
                            distances.add(chain.startDist);
                        }
                        if (chain.end.x != numCols - 1) {
                            chain.endDist = numCols - 1 - chain.end.x; // Distance to right
                            distances.add(chain.endDist);
                        }
                    } else {
                        chain.startDist = chain.start.x; // Distance to left
                        chain.endDist = numCols - 1 - chain.end.x; // Distance to right
                        distances.add(chain.startDist + chain.endDist); // Added together
                    }
                }
            }

            playerDistances.put(playerCode, distances);
        }

        return playerDistances;
    }

    public class Chain {
        int length;
        List<Location> locations;
        Location start;
        Location end;
        public int startDist; // Manhattan distance from the start of the chain to its target edge
        public int endDist; // Manhattan distance from the end of the chain to its target edge

        Chain(int length) {
            this.length = length;
            this.locations = new ArrayList<>();
        }
    }

    public Map<Integer, List<Chain>> evaluateChains(int[][] board) {
        int numRows = board.length;
        int numCols = board[0].length;
        boolean[][] visited = new boolean[numRows][numCols];
        Map<Integer, List<Chain>> playerChains = new HashMap<>(); // 1 for player, 2 for AI
        playerChains.put(1, new ArrayList<>());
        playerChains.put(2, new ArrayList<>());

        for (int i = 0; i < numCols; i++) {
            for (int j = 0; j < numRows; j++) {
                if (!visited[j][i] && (board[j][i] == 1 || board[j][i] == 2)) {
                    Chain chain = new Chain(0);
                    int length = dfs(board, new Location(i, j), board[j][i], visited, chain);
                    chain.length = length;
                    playerChains.get(board[j][i]).add(chain);
                }
            }
        }

        // Handle chaining for both player and AI
        for (int playerCode : playerChains.keySet()) {
            List<Chain> chains = playerChains.get(playerCode);
            //System.err.println(chains.size() + "size!!");
            for (int i = 0; i < chains.size(); i++) {
                //System.err.println(i + "what's i??");
                Chain chain1 = chains.get(i);
                //System.err.println(chain1.locations);

                ArrayList<Bridge> bridgesFromChainEnd = chain1.end.getBridges(); // Gets bridges for chain end location

                for (int j = i + 1; j < chains.size(); j++) {
                    Chain chain2 = chains.get(j);

                    boolean bridgeFound = false;
                    for (Bridge bridge : bridgesFromChainEnd) {
                        if (bridge.l1.equals(chain2.start) || bridge.l2.equals(chain2.start)) {
                            bridgeFound = true;
                            break;
                        }
                    }

                    if (bridgeFound) {
                        chain1.length += chain2.length;
                        chain1.locations.addAll(chain2.locations);
                        chain1.end = chain2.end; // Update the end of chain1 after chaining
                        chains.remove(j);
                        j--; // Adjust the index after removing
                    }
                }
            }
        }

        return playerChains; // This will give separate lists of chains for player and AI
    }

    int dfs2(int[][] board, Location loc, int playerCode, boolean[][] visited, Chain chain) {
        int numRows = board.length;
        int numCols = board[0].length;
        chain.locations.add(loc);

        if (visited[loc.y][loc.x])
            return 0;

        if (playerCode == 1 && loc.y == numRows - 1) {
            chain.end = loc;
            return 1;
        } else if (playerCode == 2 && loc.x == numCols - 1) {
            chain.end = loc;
            return 1;
        }

        visited[loc.y][loc.x] = true;
        int chainLength = 1;

        for (Location neighbor : loc.getAdjacentLocations()) {
            if (isInBounds(neighbor, numRows, numCols) &&
                    !visited[neighbor.y][neighbor.x] &&
                    board[neighbor.y][neighbor.x] == playerCode) {

                if ((playerCode == 1 && neighbor.y >= loc.y) ||
                        (playerCode == 2 && neighbor.x >= loc.x)) {
                    chainLength += dfs(board, neighbor, playerCode, visited, chain); // Pass chain here
                }
            }
        }

        return chainLength;
    }

    int dfs(int[][] board, Location loc, int playerCode, boolean[][] visited, Chain chain) {
        int numRows = board.length;
        int numCols = board[0].length;

        // Check if the current location is already visited or not part of the player's
        // chain
        if (visited[loc.y][loc.x] || board[loc.y][loc.x] != playerCode)
            return 0;

        // Mark the current location as visited
        visited[loc.y][loc.x] = true;

        // Add the location to the chain's locations
        chain.locations.add(loc);

        // Initialize the chain length as 1 for the current location
        int chainLength = 1;

        // Check and update the start and end of the chain
        if (chain.start == null || loc.y < chain.start.y || (loc.y == chain.start.y && loc.x < chain.start.x)) {
            chain.start = loc;
        }
        if (chain.end == null || loc.y > chain.end.y || (loc.y == chain.end.y && loc.x > chain.end.x)) {
            chain.end = loc;
        }

        // Get all the adjacent locations
        for (Location neighbor : loc.getAdjacentLocations()) {
            // Check if the neighbor is within bounds and not visited
            if (isInBounds(neighbor, numRows, numCols)) {
                // Recursive DFS call and add the result to the chain length
                chainLength += dfs(board, neighbor, playerCode, visited, chain);
            }
        }

        // Return the total length of the chain
        return chainLength;
    }

    // Helper function to check if a location is inside the board bounds
    boolean isInBounds(Location loc, int numRows, int numCols) {
        return loc.x >= 0 && loc.x < numCols && loc.y >= 0 && loc.y < numRows;
    }

    private int isEdgeTouching(Location loc, int playerCode) {
        if (playerCode == 1) {
            if (loc.y == 0) {
                return 1; // Player 1 is touching the top edge
            } else if (loc.y == numRows - 1) {
                return 2; // Player 1 is touching the bottom edge
            }
        } else {
            if (loc.x == 0) {
                return 3; // Player 2 is touching the left edge
            } else if (loc.x == numCols - 1) {
                return 4; // Player 2 is touching the right edge
            }
        }
        return 0; // Not touching any edge
    }

}