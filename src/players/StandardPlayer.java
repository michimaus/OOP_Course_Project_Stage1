package players;

import common.Constants;
import gameterain.GameMap;

public abstract class StandardPlayer implements PlayerVisitable{
    protected int id;
    protected char type;
    protected int posR;
    protected int posC;
    protected int maxHp;
    protected int currentHp;
    protected int xp;
    protected int level;
    protected int hasDotFor;
    protected int dotDamage;
    protected int stundeFor;
    protected boolean stuned;
    protected GameMap map;
    protected int incomingDamage;

    abstract float getSlamed(PlayerVisitor heroSpell, int level, char land);
    abstract float getFireBlasted(PlayerVisitor heroSpell, int level, char land);
    abstract float getIgnited(PlayerVisitor heroSpell, int level, char land);
    abstract float getExecuted(PlayerVisitor heroSpell, int level, char land);
    abstract float getDrained(PlayerVisitor heroSpell, int level, char land);
    abstract float getDeflected(PlayerVisitor heroSpell, int level, char land, WizardPlayer wizThis);
    abstract float getBaskStabbed(PlayerVisitor heroSpell, int level, char land, int count);
    abstract float getParalyzed(PlayerVisitor heroSpell, int level, char land);
    public abstract void calculateStrike(PlayerVisitor heroSpells, StandardPlayer opponent, char land);

    public char getType() {
        return type;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getPosR() {
        return posR;
    }

    public int getPosC() {
        return posC;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getIncomingDamage() {
        return incomingDamage;
    }

    public void setIncomingDamage(int incomingDamage) {
        this.incomingDamage = incomingDamage;
//        System.out.println(incomingDamage);
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }

    public StandardPlayer(char type, int posR, int posC, int id) {
        this.stuned = false;
        this.stundeFor = 0;
        this.id = id;
        this.dotDamage = 0;
        map = GameMap.getInstance();
        this.type = type;
        this.posR = posR;
        this.posC = posC;
        this.xp = 0;
        this.level = 0;
        this.hasDotFor = 0;
    }
    protected boolean getKillXp(StandardPlayer other) {
        if (other.currentHp > 0)
            return false;
        int xpTransfer = Constants.XP_UPPER_BOUND - (level - other.level) * Constants.XP_INTERVAL;
        if (xpTransfer < 0)
            xpTransfer = 0;
        this.xp += xpTransfer;
        return true;
    }

    protected void checkLevelUp() {
        if (xp >= Constants.INIT_LEVEL + level * Constants.ENCEREASE_LEVEL)
            level += 1 + (xp - (Constants.INIT_LEVEL + level * Constants.ENCEREASE_LEVEL)) / Constants.ENCEREASE_LEVEL;
    }

    public void updatePlayerNewRound(char c) {
        if (hasDotFor != 0) {
            --hasDotFor;
            currentHp -= dotDamage;
        } else {
            dotDamage = 0;
        }

        if (currentHp <= 0) {
            die();
            return;
        }

        if (stundeFor == 0) {
            boolean ok = true;
            final int oldR = posR;
            final int oldC = posC;

            switch (c) {
                case 'U':
                    --posR;
                    break;
                case 'D':
                    ++posR;
                    break;
                case 'L':
                    --posC;
                    break;
                case 'R':
                    ++posC;
                    break;
                default:
                    ok = false;
                    break;
            }
            if (ok && maxHp > 0) {
                map.updatePlayerPosition(oldR, oldC, posR, posC, this);
            }
        } else {
            --stundeFor;
        }
    }

    protected void die() {
        currentHp = -1;
        map.takeOut(this);
    }

    public void getDot(int stunedFor, int hasDotFor, int dotDamage) {
        this.stundeFor = stunedFor;
        this.hasDotFor = hasDotFor;
        this.dotDamage = dotDamage;
    }

    protected void takeDamage() {
        currentHp -= incomingDamage;
        incomingDamage = 0;
        if (currentHp <= 0)
            die();
    }

    public void printData() {
        System.out.println(type + " " + level + " " + xp + " " + currentHp);
    }
}