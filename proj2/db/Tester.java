package db;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by anastasiav on 2/26/2017.
 */
public class Tester {
    Database d1 = new Database();

    @Test
    public void testLoadFile() {
        String a = d1.transact("load t1");
        assertTrue(d1.getMemory().containsKey("t1"));
        assertEquals("", a);
    }

    @Test
    public void testPrintFile() {
        d1.transact("load t1");
        d1.transact("print t1");
        d1.transact("load teams");
        System.out.print(d1.transact("print teams"));
    }

    @Test
    public void testDropTable() {
        d1.transact("load t1");
        assertTrue(d1.getMemory().containsKey("t1"));
        d1.transact("drop table t1");
        assertFalse(d1.getMemory().containsKey("t1"));
    }

    @Test
    public void testCreateTableCol() {
        d1.transact("create table game (player string, score int)");
        assertTrue(d1.getMemory().containsKey("game"));
        System.out.print(d1.transact("print game"));
        System.out.println();
        d1.transact("insert into game values 'play', 1");
        System.out.print(d1.transact("print game"));
    }

    @Test
    public void testStore() {
        d1.transact("load t1");
        d1.transact("insert into t1 values 65677, 3332");
        d1.transact("store t1");
    }

    @Test
    public void testInsertInto() {
        d1.transact("load t1");
        assertTrue(d1.getMemory().containsKey("t1"));
        System.out.print(d1.transact("print t1"));

        System.out.println("");

        d1.transact("insert into t1 values 2, 169");
        System.out.print(d1.transact("print t1"));
    }

    @Test
    public void testTeams() {
        d1.transact("load records");
        System.out.print(d1.transact("print records"));
    }

    @Test
    public void testCreateTableSelect() {
        d1.transact("load t1");
        d1.transact("load t2");
        d1.transact("create table t3 as select * from t1, t2");
        d1.transact("store t3");
        System.out.print(d1.transact("print t3"));
    }

    @Test
    public void loated() {
        String a = d1.transact("load loated");
        assertTrue(d1.getMemory().containsKey("loated"));
        assertEquals("", a);
    }

    @Test
    public void testCreateTable() {
        d1.transact("load test1");
        d1.transact("load test2");
        d1.transact("create table fansadded as select * from test1,test2");
    }

    @Test
    public void giveMePoints() {
        d1.transact("load fansadded");
        d1.transact("create table loated as select * from fansadded");
        d1.transact("store loated");
        System.out.print(d1.transact("print loated"));
    }

    @Test
    public void joinCartesian() {
        d1.transact("load t1");
        d1.transact("load t4");
        System.out.print(d1.transact("select * from t1, t4"));
    }

    @Test
    public void oneColInt() {
        d1.transact("load t1");
        d1.transact("create table poo as select 2 * y from t1");
        d1.transact("store poo");
        System.out.print(d1.transact("print poo"));
    }

    @Test
    public void oneColFloat() {
        d1.transact("load t1");
        d1.transact("create table poo as select y * 2.0 as yay from t1");
        d1.transact("store poo");
        System.out.print(d1.transact("print poo"));
    }

    @Test
    public void oneColFloat2() {
        d1.transact("load t1");
        d1.transact("create table poo as select 2.0 * y as yay from t1");
        d1.transact("store poo");
        System.out.print(d1.transact("print poo"));
    }

    @Test
    public void twoCol() {
        d1.transact("load t1");
        d1.transact("load poo");
        d1.transact("create table nu as select x * y as moo from t1, poo");
        d1.transact("store nu");
        System.out.print(d1.transact("print nu"));
    }

    @Test
    public void oneColF() {
        d1.transact("load poo");
        d1.transact("create table foo as select 2*y from poo");
        d1.transact("store foo");
        System.out.print(d1.transact("print foo"));
    }

    @Test
    public void twoColString() {
        d1.transact("load test1");
        d1.transact("load test2");
        System.out.print(d1.transact("create table nustring as select b + c "
                + "as moo from test1, test2"));
        d1.transact("store nustring");
        System.out.print(d1.transact("print nustring"));
    }

    @Test
    public void twoColString2() {
        d1.transact("load test1");
        d1.transact("load test2");
        System.out.print(d1.transact("create table nustring as select b * c "
                + "as moo from test1, test2"));
        d1.transact("store nustring");
        System.out.print(d1.transact("print nustring"));
    }

    @Test
    public void twoColStringError() {
        d1.transact("load test1");
        d1.transact("load t1");
        System.out.print(d1.transact("create table nustring as select b + x "
                + "as moo from test1, t1"));
    }

    @Test
    public void sameCol() {
        d1.transact("load fansadd");
        d1.transact("create table same as select * from fansadd, fansadd");
        System.out.print(d1.transact("print same"));
    }

    @Test
    public void foo() {
        d1.transact("load foo");
        System.out.print(d1.transact("print foo"));
    }

    @Test
    public void one() {
        d1.transact("load fansadded");
        System.out.print(d1.transact("select b from fansadded"));
    }

    @Test
    public void storeAssorted() {
        d1.transact("load fansadd");
        System.out.print(d1.transact("insert into fansadd values 'hello',-100,3.0"));
    }

    @Test
    public void createNoType() {
        System.out.print(d1.transact("create table t (x int,y int)"));
    }

    @Test
    public void add() {
        d1.transact("create table t (x string,y float)");
        d1.transact("insert into t values 'welcome to',-100607.4");
        System.out.print(d1.transact("print t"));
    }

    @Test
    public void edgecases() {
        d1.transact("load fansadd");
        System.out.print(d1.transact("select third + 2 as c from fansadd"));
    }

    @Test
    public void nan() {
        d1.transact("load t8");
        System.out.print(d1.transact("select * from t8 where x < y"));
    }

    @Test
    public void stringAdd() {
        d1.transact("load fansadd");
        assertTrue(d1.getMemory().containsKey("fansadd"));
        System.out.print(d1.transact("select first + 'hi' as new from fansadd"));
    }

    @Test
    public void insertInto() {
        d1.transact("load fansadd");
        d1.transact("insert into fansadd values NOVALUE, NOVALUE, NOVALUE");
        d1.transact("store fansadd");
    }

    @Test
    public void bugs2records() {
        d1.transact("load records");
        System.out.print(d1.transact("select TeamName, Ties from records"));
    }


    @Test
    public void conditions() {
        d1.transact("load records");
        System.out.print(d1.transact("select TeamName,Season,Wins,Losses from records where Wins >= Losses"));
    }

    //FOCUS
    @Test
    public void conditionsAssorted() {
        d1.transact("load records");
        d1.transact("load fans");
        String actual = d1.transact("select TeamName,Season,Wins,Losses from records where Wins >= Losses and TeamName > 'Mets'");
        String expected = "TeamName string,Season int,Wins int,Losses int\n"
                + "'Steelers',2015,10,6\n"
                + "'Steelers',2014,11,5\n"
                + "'Steelers',2013,8,8\n"
                + "'Patriots',2015,12,4\n"
                + "'Patriots',2014,12,4\n"
                + "'Patriots',2013,12,4";
        System.out.println(actual);
        assertTrue(expected.equals(actual));

        String actual1 = d1.transact("select TeamName,Season,Wins,Losses from records where Wins != Losses and Losses > 6 and Season < 2015");
        String expected1 = "TeamName string,Season int,Wins int,Losses int\n"
                + "'Golden Bears',2014,5,7\n"
                + "'Mets',2014,79,83\n"
                + "'Mets',2013,74,88";
        System.out.println(actual1);
        assertTrue(expected1.equals(actual1));
    }

    //FOCUS
    @Test
    public void createSelectAdvanced() {
        d1.transact("load records");
        d1.transact("load teams");
        d1.transact("fans");
        String expected = "City string, Season int, Ratio int\n" +
                "'New York',2014,0\n" +
                "'New York',2013,0\n" +
                "'Berkeley',2016,0\n" +
                "'Berkeley',2014,0";
        String actual = d1.transact("select City,Season,Wins/Losses as Ratio from teams,records");
        System.out.println(actual);
        //assertTrue(expected.equals(actual));
    }

    @Test
    public void selectAssorted() {
        d1.transact("load records");
        System.out.println(d1.transact("select TeamName, Ties from records"));
    }

    @Test
    public void selectExpressionsMixed() {
        d1.transact("loat sel");
        System.out.println(d1.transact("select x + y as a, y*z as b from selectMixed"));
    }

    //FOCUS
    @Test
    public void selectBasicBinary() {
        d1.transact("load records");
        d1.transact("load fans");
        String expected = "'Golden Bears',2015,8,5\n" +
                "'Steelers',2015,10,6\n" +
                "'Steelers',2014,11,5\n" +
                "'Steelers',2013,8,8\n" +
                "'Mets',2015,90,72\n" +
                "'Patriots',2015,12,4\n"+
                "'Patriots',2015,12,4\n"+
                "'Patriots',2015,12,4\n";
        String actual = d1.transact("select TeamName,Season,Wins,Losses from records where Wins >= Losses");
        assertTrue(expected.equals(actual));
    }

    @Test
    public void loadMalformed() {
        System.out.println(d1.transact("load poo"));
    }

    @Test
    public void createBasic() {
        d1.transact("create table poo (a string, b string, c          string)");
        String expected = "a string,b string,c string";
        String actual = d1.transact("print poo");
        System.out.println(actual);
        assertTrue(expected.equals(actual));
    }

    @Test
    public void createBasic1() {
        d1.transact("load t2");
        System.out.print(d1.transact("select Letter,CodeWord from t2 where CodeWord <= 'Mike'"));
    }
}
