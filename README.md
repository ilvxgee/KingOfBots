# Snake - KingOfBots Wiki

贪食蛇(Snake)是KingOfBots平台上的双人游戏。
**目录**
**作者**
裁判程序 *backcloud* 前端 *web*
**游戏规则**
确切地说，这并不是贪吃蛇。 与传统单人贪吃蛇不同的是，本贪吃蛇为双人对战，每回合玩家同时做出决策控制自己的蛇。
玩家在13*14的网格中操纵一条蛇(蛇是一系列坐标构成的有限不重复有顺序的序列，序列中相邻坐标均相邻，即两坐标的x轴坐标或y轴坐标相同，序列中第一个坐标代表蛇头)，玩家只能控制蛇头的朝向(东、南、西和北)来控制蛇。蛇以恒定的速度前进(前进即为序列头插入蛇头指向方向下一格坐标，并删除序列末尾坐标)。蛇的初始位置在网格中的左下角(地图位置[13,1])与右上角(地图位置[1,14])，初始长度为1格。与传统贪吃蛇不同，本游戏在网格中并没有豆子，但蛇会自动长大(长大即为不删除序列末尾坐标的前进)，前10回合每回合长度增加1，从第11回合开始，每3回合长度增加1。
地图为13*14的网格，由1*1的草地与障碍物构成。
蛇头在网格外、障碍物、自己蛇的身体(即序列重复)、对方蛇的身体(即与对方序列有相同坐标)，或非法操作均判定为死亡。任何一条蛇死亡时，游戏结束。若蛇同时死亡，判定为平局，否则先死的一方输，另一方赢。
**游戏交互方式**
**概述**
本游戏目前作为KingOfBots的第一款游戏，目前只支持用户使用Java编写Bot逻辑参加比赛，或者选择亲自出战。
**具体交互内容**
**bot参赛**
每回合bot接收一个int变量，表示己方蛇的移动方向。
具体格式如下：（**0，1，2，3**分别表示 **上，右，下，左**），样例程序中提供了获取游戏对局信息的各种接口，你只需要编写nextMove方法。
**人类参赛**
键盘输入w d s a控制己方蛇的移动方向。
**样例程序（Java版本）**
这里提供了一个可以自动判断上下左右是否有障碍物，选择合适方向的简易Bot，你可以基于本模板开发自己的Bot程序。

```java
package com.ilvxgee.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

public class Bot implements com.ilvxgee.botrunningsystem.utils.BotInterface {
    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

private boolean check_tail_increasing(int step) {//检验当前回合，蛇的长度是否增加
    if (step <= 10) return true;
    return step % 3 == 1;
}

public List<Cell> getCells(int sx, int sy, String steps) {
    steps = steps.substring(1, steps.length() - 1);
    List<Cell> res = new ArrayList<>();
    int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    int x = sx;
    int y = sy;
    int step = 0;
    res.add(new Cell(x, y));
    for (int i = 0; i < steps.length(); i++) {
        int d = steps.charAt(i) - '0';
        x += dx[d];
        y += dy[d];
        res.add(new Cell(x, y));
        if (!check_tail_increasing(++step)) {
            res.remove(0);
        }
    }
    return res;
}

@Override
public Integer nextMove(String input) {
    String[] strs = input.split("#");
    int[][] g = new int[13][14];
    for (int i = 0, k = 0; i < 13; i++) {
        for (int j = 0; j < 14; j++, k++) {
            if (strs[0].charAt(k) == '1')
                g[i][j] = 1;
        }
    }
    int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
    int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);
    List<Cell> aCells = getCells(aSx, aSy, strs[3]);
    List<Cell> bCells = getCells(bSx, bSy, strs[6]);
    for (Cell c : aCells) g[c.x][c.y] = 1;
    for (Cell c : bCells) g[c.x][c.y] = 1;
    int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
    for (int i = 0; i < 4; i++) {
        int x = aCells.get(aCells.size() - 1).x + dx[i];
        int y = aCells.get(aCells.size() - 1).y + dy[i];
        if (x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {
            return i;
        }
    }
    return 0;
}
}
```


