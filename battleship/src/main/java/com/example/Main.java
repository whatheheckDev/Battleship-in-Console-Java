package com.example;

import java.util.*;

public class Main {
    static char[][] cpu_field = new char[10][10];
    static char[][] self_field = new char[10][10];

    public static void main(String[] args) {
        System.out.println("Welcome to Battleships!");
        System.out.println("Press any button to play");
        Scanner sc = new Scanner(System.in);
        String begin = sc.nextLine();

        int turns = 1;
        int win_count = 0;
        int lose_count = 0;
        cpu_field = generateBoard();
        self_field = generateBoard();
        int[] self_list = selfCords();
        int[][] self_cords = getCords(self_list);
        int turn = 0;
        int[] cpu_list = genCords();
        int[][] cpu_cords = getCords(cpu_list);
        int cpu_dir = -1;
        int cpu_parity = 1;
        boolean cpu_hit = false;
        int dir_check = 0;
        HashMap<int[], Integer> cpu_moves = new HashMap<int[], Integer>();
        int[] cpu_choice = new int[2];
        int length = 0;
        int[] max_len = { 0, 0, 0, 0 };
        while (true) {
            System.out.println("Player: ");
            printBoard(self_field);
            System.out.println("");
            System.out.println("CPU: ");
            printBoard(cpu_field);

            if (turn == 0) {
                System.out.println("Your turn");
                System.out.println("");
                System.out.println("Input coordinates (horizontal -> vertical)");
                char hcord = sc.next().charAt(0);
                sc.nextLine();
                int v_cord = sc.nextInt();
                int h_cord;
                h_cord = (hcord - 'a');
                v_cord--;
                boolean check;
                check = cordCheck(cpu_cords, h_cord, v_cord);
                if (check) {
                    if (cpu_field[v_cord][h_cord] == '-') {
                        cpu_field[v_cord][h_cord] = 'o';
                        win_count++;
                    } else {
                        System.out.println("....but you already hit it");

                    }
                } else {
                    if (cpu_field[v_cord][h_cord] == 'x') {
                        System.out.println("....but you already hit it");
                    }
                    cpu_field[v_cord][h_cord] = 'x';
                    turn = 1;
                }

                if (win_count == 20) {
                    printBoard(cpu_field);
                    System.out.println("You won");
                    break;
                }
            }

            else {
                System.out.println("CPU's turn");
                System.out.println(" ");
                
                if (cpu_hit && length >= 1) {
                    if (cpu_choice[1] <= 0 & cpu_dir == 3) {
                        cpu_dir = 4;
                    } else if (cpu_choice[1] >= 9 & cpu_dir == 1) {
                        cpu_dir = 2;
                    } else if (cpu_choice[0] <= 0 && cpu_dir == 2) {
                        cpu_dir = 3;
                        if (dir_check == 1) {
                            cpu_dir = 4;
                        }
                    } else if (cpu_choice[0] >= 9 && cpu_dir == 0) {
                        cpu_dir = 1;
                    }
                }

                if (cpu_dir == 4) {
                    length = 1;
                    cpu_hit = false;
                    cpu_dir = -1;
                    dir_check = 0;
                }
                cpu_choice = cpuChoice(cpu_choice, cpu_parity, cpu_dir, cpu_hit, dir_check, length);
                int h_cord = cpu_choice[0];
                int v_cord = cpu_choice[1];
                char h_cord_out = (char) (h_cord + 'a');
                System.out.println("CPU shot: " + h_cord_out + (v_cord + 1));
                boolean check;
                check = cordCheck(self_cords, h_cord, v_cord);
                if (check) {
                    if (self_field[v_cord][h_cord] == '-') {
                        self_field[v_cord][h_cord] = 'o';
                        length++;
                        lose_count++;
                        switch (cpu_dir) {
                            case -1:
                                cpu_dir++;
                                cpu_hit = true;
                                break;
                            case 0:
                                if (length == 2) {
                                    dir_check = 1;
                                }
                                break;
                            case 1:
                                if (length == 2) {
                                    dir_check = 2;
                                }
                                break;
                        }
                        
                        
                    } else if(self_field[v_cord][h_cord] == 'o') {
                        System.out.println("But it already hit it");
                        int coordinate_hit = cpu_choice[0] * 10 + cpu_choice[1];
                        int coordinate_real = (cpu_parity * 2) - 2;
                        if (coordinate_hit >= coordinate_real) {
                            cpu_parity++;
                        }
                        cpu_hit = false;
                        cpu_dir = -1;
                        dir_check = 0;
                        length = 0;
                        

                        
                    }

                } else {
                    if (self_field[v_cord][h_cord] == '-') {
                        self_field[v_cord][h_cord] = 'x';
                        
                        if (cpu_hit) {
                            switch (cpu_dir) {
                                case 0:
                                    if (dir_check == 1) {
                                        cpu_dir += 2;
                                        cpu_choice[0] -= (length);

                                    } else {
                                        cpu_dir++;
                                        cpu_choice[0]--;
                                    }

                                    break;
                                case 1:
                                    if (dir_check == 2) {
                                        cpu_dir += 2;
                                        cpu_choice[1] -= (length);

                                    } else {
                                        cpu_dir++;
                                        cpu_choice[1]--;
                                    }

                                    break;
                                case 2:
                                    if (dir_check == 1) {
                                        cpu_dir = -1;
                                        cpu_hit = false;
                                        length = 0;
                                        dir_check = 0;
                                        cpu_parity++;
                                    } else {
                                        cpu_dir++;
                                    }
                                    cpu_choice[0]++;
                                    break;
                                case 3:
                                    cpu_dir = -1;
                                    cpu_hit = false;
                                    length = 0;
                                    dir_check = 0;
                                    cpu_parity++;
                                    break;
                            }

                        }
                        turn = 0;
                        
                    } else if (self_field[v_cord][h_cord] == 'x') {
                        System.out.println("But it already hit it");
                        cpu_parity++;
                        cpu_hit = false;
                        cpu_dir = -1;
                    }
                }
                if (length == 4) {
                    length = 0;
                    cpu_hit = false;
                    cpu_dir = -1;
                    dir_check = 0;
                    cpu_parity++;
                    max_len[0]++;
                }
                else if (length == 3 && max_len[0] == 1) {
                    length = 0;
                    cpu_hit = false;
                    cpu_dir = -1;
                    dir_check = 0;
                    cpu_parity++;
                    max_len[1]++;
                }
                else if (length == 2 && max_len[1] == 2) {
                    length = 0;
                    cpu_hit = false;
                    cpu_dir = -1;
                    dir_check = 0;
                    cpu_parity++;
                    max_len[2]++;
                }
                else if (max_len[2] == 3) {
                    length = 0;
                    cpu_hit = false;
                    cpu_dir = -1;
                    dir_check = 0;
                    cpu_parity++;
                }

                if (lose_count == 20) {
                    printBoard(self_field);
                    System.out.println("You lost");
                    break;
                    
                }

            }

        }

    }

    public static char[][] generateBoard() {
        char[][] board = { { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' },
                { '-', '-', '-', '-', '-', '-', '-', '-', '-', '-' } };
        return board;
    }

    public static void printBoard(char[][] board) {
        System.out.println("   A B C D E F G H I J");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + 1 + " ");
            if (i < 9) {
                System.out.print(" ");
            }
            for (int j = 0; j < 10; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public static boolean cordCheck(int[][] cords, int h_cord, int v_cord) {
        boolean res = false;
        for (int i = 0; i < 20; i++) {
            if (cords[i][0] == h_cord && cords[i][1] == v_cord) {
                System.out.println("Hit!");
                res = true;
                break;

            }
        }
        if (!res) {
            System.out.println("Missed");
        }
        return res;
    }

    public static int[] genCords() {
        int[] cords = new int[20];
        int[] dir = new int[10]; // 0 - right, 1 - up, 2 - left, 3 - down
        Random rand = new Random();

        int[][] ships = new int[10][];
        int[] len = { 4, 3, 3, 2, 2, 2, 1, 1, 1, 1 };
        for (int i = 0; i < 10; i++) {
            ships[i] = new int[len[i]];
        }

        for (int i = 0; i < 10; i++) {
            dir[i] = rand.nextInt(0, 3);
        }
        ArrayList<Integer> list = new ArrayList<>();
        boolean placeable = false;
        while (!placeable) {
            cords[0] = rand.nextInt(0, 99);
            placeable = checkBounds(cords[0], dir[0], 4);
        }
        placeable = false;
        ships[0] = shipCords(cords[0], dir[0], 4);
        for (int i = 0; i < 4; i++) {
            cords[i] = ships[0][i];
        }
        list = modifyList(list, ships[0]);

        boolean occupied = true;
        for (int i = 1; i < 10; i++) {
            while (!placeable) {
                int sCord = rand.nextInt(0, 99);
                placeable = checkBounds(sCord, dir[i], ships[i].length);
                if (!placeable) {
                    continue;
                }
                ships[i] = shipCords(sCord, dir[i], ships[i].length);
                occupied = isOccupied(list, ships[i]);
                placeable = !occupied;
            }
            list = modifyList(list, ships[i]);
            placeable = false;
            occupied = true;
        }

        for (int i = 4; i < 7; i++) {
            cords[i] = ships[1][i - 4];
        }
        for (int i = 7; i < 10; i++) {
            cords[i] = ships[2][i - 7];
        }
        for (int i = 10; i < 12; i++) {
            cords[i] = ships[3][i - 10];
        }
        for (int i = 12; i < 14; i++) {
            cords[i] = ships[4][i - 12];
        }
        for (int i = 14; i < 16; i++) {
            cords[i] = ships[5][i - 14];
        }
        for (int i = 16; i < 20; i++) {
            cords[i] = ships[i - 10][0];
        }
        
        return cords;
    }

    public static int[][] getCords(int[] cords) {
        int[][] res = new int[20][2];
        for (int i = 0; i < 20; i++) {
            res[i][0] = cords[i] % 10;
            res[i][1] = cords[i] / 10;
        }
        return res;

    }

    public static boolean checkBounds(int s, int dir, int length) {
        int x = s % 10;
        int y = s / 10;
        switch (dir) {
            case 0:
                if (x > 10 - length) {
                    return false;
                }
                break;
            case 1:
                if (y < length - 1) {
                    return false;
                }
                break;
            case 2:
                if (x < length - 1) {
                    return false;
                }
                break;
            case 3:
                if (y > 10 - length) {
                    return false;
                }
                break;
        }
        return true;
    }

    public static int[] shipCords(int sCord, int dir, int length) {
        int[] cords = new int[length];
        cords[0] = sCord;
        for (int i = 1; i < length; i++) {
            switch (dir) {
                case 0:
                    cords[i] = cords[i - 1] + 1;
                    break;
                case 1:
                    cords[i] = cords[i - 1] - 10;
                    break;
                case 2:
                    cords[i] = cords[i - 1] - 1;
                    break;
                case 3:
                    cords[i] = cords[i - 1] + 10;
                    break;
            }
        }
        return cords;
    }

    public static ArrayList<Integer> modifyList(ArrayList<Integer> list, int[] ship) {
        for (int i = 0; i < ship.length; i++) {
            list.add(ship[i]);
            list.add(ship[i] - 10);
            list.add(ship[i] + 10);
            list.add(ship[i] - 11);
            list.add(ship[i] - 1);
            list.add(ship[i] + 9);
            list.add(ship[i] - 9);
            list.add(ship[i] + 1);
            list.add(ship[i] + 11);
            if (ship[i] % 10 == 9) {
                list.remove(list.size() - 3);
                list.remove(list.size() - 2);
                list.remove(list.size() - 1);
            }
            if (ship[i] % 10 == 0) {
                list.remove(list.size() - 6);
                list.remove(list.size() - 5);
                list.remove(list.size() - 4);

            }

        }
        HashSet<Integer> set = new HashSet<>(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    public static boolean isOccupied(ArrayList<Integer> list, int[] ship) {
        for (int i = 0; i < ship.length; i++) {
            if (list.contains(ship[i])) {
                return true;

            }
        }
        return false;
    }

    public static int[] selfCords() {
        System.out.println("Place your ships!");
        char[][] board = generateBoard();
        int[] cords = new int[20];
        int[] dir = new int[10]; // 0 - right, 1 - up, 2 - left, 3 - down
        Scanner sc = new Scanner(System.in);
        int[][] ships = new int[10][];
        int[] len = { 4, 3, 3, 2, 2, 2, 1, 1, 1, 1 };
        for (int i = 0; i < 10; i++) {
            ships[i] = new int[len[i]];
        }
        ArrayList<Integer> list = new ArrayList();
        for (int i = 0; i < 10; i++) {
            boolean placeable = false;
            boolean occupied = true;
            printBoard(board);
            while (!placeable || occupied) {
                System.out.println("Input starting coordinate of your " + ships[i].length + "-length ship");
                char h = sc.next().charAt(0);
                int v_cord = sc.nextInt();
                v_cord--;
                int h_cord = (h - 'a');
                if (i < 6) {
                    System.out.println("Choose the direction: u - up, d - down, l -left, r - right");
                    dir[i] = direction(sc);

                } else {
                    dir[i] = 0;
                }
                int cord = v_cord * 10 + h_cord;
                placeable = checkBounds(cord, dir[i], ships[i].length);
                ships[i] = shipCords(cord, dir[i], len[i]);
                occupied = isOccupied(list, ships[i]);
                if (!placeable || occupied) {
                    System.out.println("Illegal coordinates or direction");
                } else {

                    list = modifyList(list, ships[i]);
                    for (int j = 0; j < len[i]; j++) {
                        board[ships[i][j] / 10][ships[i][j] % 10] = 'o';
                    }

                }
            }
        }
        for (int i = 0; i < 4; i++) {
            cords[i] = ships[0][i];
        }
        for (int i = 4; i < 7; i++) {
            cords[i] = ships[1][i - 4];
        }
        for (int i = 7; i < 10; i++) {
            cords[i] = ships[2][i - 7];
        }
        for (int i = 10; i < 12; i++) {
            cords[i] = ships[3][i - 10];
        }
        for (int i = 12; i < 14; i++) {
            cords[i] = ships[4][i - 12];
        }
        for (int i = 14; i < 16; i++) {
            cords[i] = ships[5][i - 14];
        }
        for (int i = 16; i < 20; i++) {
            cords[i] = ships[i - 10][0];
        }
        
        return cords;
    }

    public static int direction(Scanner sc) {
        int dir = 0;
        char d = sc.next().charAt(0);
        sc.nextLine();
        switch (d) {
            case 'r':
                dir = 0;
                break;
            case 'u':
                dir = 1;
                break;
            case 'l':
                dir = 2;
                break;
            case 'd':
                dir = 3;
                break;
            default:
                System.out.println("Follow the instructions");
                dir = direction(sc);
                break;

        }
        return dir;
    }

    public static int[] cpuChoice(int[] last_move, int duration, int direction, boolean hit, int dir_check,
            int length) {
        int[] out = new int[2];
        int res = 0;
        boolean oob = false;
        if (hit) {
            switch (direction) {
                case 0:
                    if (last_move[0] < 9) {
                        out[1] = last_move[1];
                        out[0] = last_move[0] + 1;
                    } else {
                        oob = true;
                    }
                    break;
                case 1:
                    if (last_move[1] < 9) {
                        out[1] = last_move[1] + 1;
                        out[0] = last_move[0];
                    } else {
                        oob = true;
                    }
                    break;
                case 2:
                    if (last_move[0] > 0) {
                        out[1] = last_move[1];
                        out[0] = last_move[0] - 1;
                    } else {
                        oob = true;
                    }
                    break;
                case 3:
                    if (last_move[1] > 0) {
                        out[1] = last_move[1] - 1;
                        out[0] = last_move[0];
                    } else {
                        oob = true;
                    }
                    break;
                
            }
            if (oob) {
                if (length > 1) {
                    direction = 4;
                }

            }

        } if (direction == 4 || !hit || length > 4) {
            res = (duration- (duration/25) * 25) * 2 - 2;
            if (duration <= 25) {
                out[1] = res % 10;
                out[0] = (res / 10) * 2;
            } else if (duration <= 50) {
                res = (duration - 25) * 2 - 2;
                out[1] = res % 10 + 1;
                out[0] = (res / 10) * 2 + 1;
            } else if (duration <= 75) {
                out[1] = res % 10;
                out[0] = (res / 10) * 2 + 1;
            }
            else {
                out[1] = res % 10 + 1;
                out[0] = (res / 10) * 2;
            }
            
        }

        return out;
    }

}
