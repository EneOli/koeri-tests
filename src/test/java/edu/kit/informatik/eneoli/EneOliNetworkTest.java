package edu.kit.informatik.eneoli;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>
 * sorry for these messy tests
 */

public class EneOliNetworkTest {

    @Test
    public void listConstructorSimpleTest() throws ParseException {
        Network network = new Network(new IP("1.1.1.1"), Arrays.asList(
                new IP("2.2.2.2"),
                new IP("3.3.3.3"),
                new IP("4.4.4.4")
        ));

        List<IP> ips = network.list();

        Assertions.assertEquals("1.1.1.1", ips.get(0).toString());
        Assertions.assertEquals("2.2.2.2", ips.get(1).toString());
        Assertions.assertEquals("3.3.3.3", ips.get(2).toString());
        Assertions.assertEquals("4.4.4.4", ips.get(3).toString());

    }

    @Test
    public void listConstructorNullRoot() throws ParseException {
        Assertions.assertThrows(RuntimeException.class, () -> {
            new Network(null, Arrays.asList(
                    new IP("2.2.2.2"),
                    new IP("3.3.3.3"),
                    new IP("4.4.4.4")
            ));
        });
    }

    @Test
    public void listConstructorNullList() throws ParseException {
        Assertions.assertThrows(RuntimeException.class, () -> new Network(new IP("1.1.1.1"), null));
    }

    @Test
    public void listConstructorEmptyList() throws ParseException {
        Assertions.assertThrows(RuntimeException.class, () -> new Network(new IP("1.1.1.1"), new ArrayList<>()));
    }

    @Test
    public void listConstructorRootInList() throws ParseException {
        Assertions.assertThrows(RuntimeException.class, () -> new Network(new IP("1.1.1.1"), Arrays.asList(
                new IP("3.3.3.3"),
                new IP("4.4.4.4"),
                new IP("1.1.1.1")
        )));
    }

    @Test
    public void listConstructorDoubleNodeInList() throws ParseException {
        Assertions.assertThrows(RuntimeException.class, () -> new Network(new IP("1.1.1.1"), Arrays.asList(
                new IP("3.3.3.3"),
                new IP("4.4.4.4"),
                new IP("3.3.3.3")
        )));
    }

    @Test
    public void listConstructorNullNodeInList() throws ParseException {
        Assertions.assertThrows(RuntimeException.class, () -> new Network(new IP("1.1.1.1"), Arrays.asList(
                new IP("3.3.3.3"),
                null,
                new IP("3.3.3.3")
        )));
    }

    @Test
    public void listConstructorCopyList() throws ParseException {

        Assertions.assertThrows(RuntimeException.class, () -> {
            List<IP> ips = Arrays.asList(
                    new IP("1.1.1.1"),
                    new IP("2.2.2.2")
            );

            Network network = new Network(new IP("0.0.0.0"), ips);

            ips.remove(0);

            Assertions.assertEquals(3, network.list().size());
            Assertions.assertEquals("0.0.0.0", network.list().get(0).toString());
            Assertions.assertEquals("1.1.1.1", network.list().get(1).toString());
            Assertions.assertEquals("2.2.2.2", network.list().get(2).toString());

            ips = null;

            Assertions.assertEquals(3, network.list().size());
            Assertions.assertEquals("0.0.0.0", network.list().get(0).toString());
            Assertions.assertEquals("1.1.1.1", network.list().get(1).toString());
            Assertions.assertEquals("2.2.2.2", network.list().get(2).toString());
        });
    }

    @Test
    public void simpleBracketNotationConstructorTest() throws ParseException {
        Network network = new Network("(231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77)");

        List<IP> ips = network.list();

        Assertions.assertEquals("39.20.222.120", ips.get(0).toString());
        Assertions.assertEquals("77.135.84.171", ips.get(1).toString());
        Assertions.assertEquals("116.132.83.77", ips.get(2).toString());
        Assertions.assertEquals("231.189.0.127", ips.get(3).toString());
        Assertions.assertEquals("252.29.23.0", ips.get(4).toString());

    }

    @Test
    public void bracketNotationNullString() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new Network(null));
    }

    @Test
    public void bracketNotationVoidString() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new Network(""));
    }

    @Test
    public void bracketNotationEmptyString() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new Network(" "));
    }

    @Test
    public void bracketNotationConstructorGarbageTest() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new Network("(1.1.1.1hjfb(//(fu/(()((/)"));
    }

    @Test
    public void bracketNotationConstructorNoIpTest() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new Network("()"));
    }

    @Test
    public void bracketNotationConstructorOnlyOneIpTest() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new Network("(1.1.1.1)"));
    }

    @Test
    public void bracketNotationConstructorOnlyOneNestedIpTest() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new Network("(1.1.1.1 (2.2.2.2))"));
    }

    @Test
    public void bracketNotationConstructorSameIpTest() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new Network("(1.1.1.1 (2.2.2.2 3.3.3.3 (4.4.4.4 1.1.1.1)))"));
    }

    @Test
    public void bracketNotationConstructorEqualityTestNotEqual() throws ParseException {
        Network network1 = new Network("(1.1.1.1 (2.2.2.2 3.3.3.3))");
        Network network2 = new Network("(1.1.1.1 (3.3.3.3 2.2.2.2))");

        Assertions.assertFalse(network1.equals(network2));
        Assertions.assertFalse(network2.equals(network1));
    }

    @Test
    public void bracketNotationConstructorEqualityTestDifferentSequence() throws ParseException {
        Network network1 = new Network("(1.1.1.1 (2.2.2.2 3.3.3.3 4.4.4.4))");
        Network network2 = new Network("(1.1.1.1 (2.2.2.2 4.4.4.4 3.3.3.3))");

        Assertions.assertTrue(network1.equals(network2));
        Assertions.assertTrue(network2.equals(network1));
    }

    @Test
    public void bracketNotationConstructorInvalidNotationTest() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new Network("(231.189.0.127 77.135.84 .171 39.20.222.120 252.29.23.0 116.132.83.77)"));
    }

    @Test
    public void complexBracketNotationConstructorTest() throws ParseException {
        Network network = new Network("(85.193.148.81 (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239 " +
                "(231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");

        // 0.146.197.108
        // 34.49.145.239
        // 39.20.222.120
        // 77.135.84.171
        // 85.193.148.81
        // 116.132.83.77
        // 122.117.67.158
        // 141.255.1.133
        // 231.189.0.127
        // 252.29.23.0

        List<IP> ips = network.list();

        Assertions.assertEquals(10, ips.size());

        Assertions.assertEquals("0.146.197.108", ips.get(0).toString());
        Assertions.assertEquals("34.49.145.239", ips.get(1).toString());
        Assertions.assertEquals("39.20.222.120", ips.get(2).toString());
        Assertions.assertEquals("77.135.84.171", ips.get(3).toString());
        Assertions.assertEquals("85.193.148.81", ips.get(4).toString());
        Assertions.assertEquals("116.132.83.77", ips.get(5).toString());
        Assertions.assertEquals("122.117.67.158", ips.get(6).toString());
        Assertions.assertEquals("141.255.1.133", ips.get(7).toString());
        Assertions.assertEquals("231.189.0.127", ips.get(8).toString());
        Assertions.assertEquals("252.29.23.0", ips.get(9).toString());

        Assertions.assertEquals(2, network.getHeight(new IP("85.193.148.81")));

        List<IP> path = network.getRoute(new IP("141.255.1.133"), new IP("77.135.84.171"));

        Assertions.assertEquals(4, path.size());

        Assertions.assertEquals("141.255.1.133", path.get(0).toString());
        Assertions.assertEquals("85.193.148.81", path.get(1).toString());
        Assertions.assertEquals("231.189.0.127", path.get(2).toString());
        Assertions.assertEquals("77.135.84.171", path.get(3).toString());
    }

    @Test
    public void getPathSimpleTest() throws ParseException {
        Network network = new Network("(1.1.1.1 (2.2.2.2 3.3.3.3 4.4.4.4 5.5.5.5))");

        List<IP> path = network.getRoute(new IP("1.1.1.1"), new IP("4.4.4.4"));

        Assertions.assertEquals(3, path.size());

        Assertions.assertEquals("1.1.1.1", path.get(0).toString());
        Assertions.assertEquals("2.2.2.2", path.get(1).toString());
        Assertions.assertEquals("4.4.4.4", path.get(2).toString());

    }

    @Test
    public void getPathComplexTest() throws ParseException {
        Network network = new Network("(85.193.148.81 (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239 (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");

        network.add(new Network("(1.1.1.1 (2.2.2.2 3.3.3.3 4.4.4.4) 5.5.5.5 (6.6.6.6 7.7.7.7))"));

        List<IP> path = network.getRoute(new IP("77.135.84.171"), new IP("85.193.148.81"));

        Assertions.assertEquals(3, path.size());

        Assertions.assertEquals("77.135.84.171", path.get(0).toString());
        Assertions.assertEquals("231.189.0.127", path.get(1).toString());
        Assertions.assertEquals("85.193.148.81", path.get(2).toString());
    }

    @Test
    public void getPathDisjointNodes() throws ParseException {
        Network network = new Network("(85.193.148.81 (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239 (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");

        network.add(new Network("(1.1.1.1 (2.2.2.2 3.3.3.3 4.4.4.4) 5.5.5.5 (6.6.6.6 7.7.7.7))"));

        List<IP> path = network.getRoute(new IP("4.4.4.4"), new IP("85.193.148.81"));

        Assertions.assertEquals(0, path.size());
    }

    @Test
    public void getPathStartNull() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        List<IP> path = network.getRoute(null, new IP("2.2.2.2"));

        Assertions.assertEquals(0, path.size());
    }

    @Test
    public void getPathEndNull() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        List<IP> path = network.getRoute(new IP("1.1.1.1"), null);

        Assertions.assertEquals(0, path.size());
    }

    @Test
    public void getPathStartEndNull() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        List<IP> path = network.getRoute(null, null);

        Assertions.assertEquals(0, path.size());
    }

    @Test
    public void getHeightSimpleTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        Assertions.assertEquals(1, network.getHeight(new IP("1.1.1.1")));
    }

    @Test
    public void getHeightNUllIpTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        Assertions.assertEquals(0, network.getHeight(null));
    }

    @Test
    public void getHeightNonExistingIpTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        Assertions.assertEquals(0, network.getHeight(new IP("0.0.0.0")));
    }

    @Test
    public void listSimpleTest() throws ParseException {
        Network network = new Network("(2.2.2.2 1.1.1.1)");

        List<IP> ips = network.list();

        Assertions.assertEquals(2, ips.size());

        Assertions.assertEquals("1.1.1.1", ips.get(0).toString());
        Assertions.assertEquals("2.2.2.2", ips.get(1).toString());
    }

    @Test
    public void listComplexTest() throws ParseException {

        Network network = new Network("(1.1.1.1 (2.2.2.2 4.4.4.4) 3.3.3.3 (5.5.5.5 0.0.0.0 11.11.11.11) 12.12.12.12 44.44.44.44)");

        List<IP> ips = network.list();

        Assertions.assertEquals(9, ips.size());

        Assertions.assertEquals("0.0.0.0", ips.get(0).toString());
        Assertions.assertEquals("1.1.1.1", ips.get(1).toString());
        Assertions.assertEquals("2.2.2.2", ips.get(2).toString());
        Assertions.assertEquals("3.3.3.3", ips.get(3).toString());
        Assertions.assertEquals("4.4.4.4", ips.get(4).toString());
        Assertions.assertEquals("5.5.5.5", ips.get(5).toString());
        Assertions.assertEquals("11.11.11.11", ips.get(6).toString());
        Assertions.assertEquals("12.12.12.12", ips.get(7).toString());
        Assertions.assertEquals("44.44.44.44", ips.get(8).toString());
    }

    @Test
    public void containsTest() throws ParseException {
        Network network = new Network("(85.193.148.81 (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239 (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");

        Assertions.assertTrue(network.contains(new IP("39.20.222.120")));
        Assertions.assertFalse(network.contains(new IP("42.20.222.120")));

        Assertions.assertFalse(network.contains(null));
    }

    @Test
    public void getLevelsSimpleTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2 3.3.3.3)");

        List<List<IP>> levels = network.getLevels(new IP("1.1.1.1"));

        Assertions.assertEquals(2, levels.size());

        List<IP> level1 = levels.get(0);
        List<IP> level2 = levels.get(1);

        Assertions.assertEquals(1, level1.size());
        Assertions.assertEquals(2, level2.size());

        Assertions.assertEquals("1.1.1.1", level1.get(0).toString());
        Assertions.assertEquals("2.2.2.2", level2.get(0).toString());
        Assertions.assertEquals("3.3.3.3", level2.get(1).toString());
    }

    @Test
    public void getLevelsComplexTest() throws ParseException {
        Network network = new Network("(85.193.148.81 (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239 (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");

        List<List<IP>> levels = network.getLevels(new IP("39.20.222.120"));

        Assertions.assertEquals(5, levels.size());
        Assertions.assertEquals(network.getHeight(new IP("39.20.222.120")) + 1, levels.size());


        List<IP> level1 = levels.get(0);
        List<IP> level2 = levels.get(1);
        List<IP> level3 = levels.get(2);
        List<IP> level4 = levels.get(3);
        List<IP> level5 = levels.get(4);

        Assertions.assertEquals(1, level1.size());

        Assertions.assertEquals("39.20.222.120", level1.get(0).toString());

        // ======

        Assertions.assertEquals(1, level2.size());

        Assertions.assertEquals("231.189.0.127", level2.get(0).toString());

        // ======

        Assertions.assertEquals(4, level3.size());

        Assertions.assertEquals("77.135.84.171", level3.get(0).toString());
        Assertions.assertEquals("85.193.148.81", level3.get(1).toString());
        Assertions.assertEquals("116.132.83.77", level3.get(2).toString());
        Assertions.assertEquals("252.29.23.0", level3.get(3).toString());

        // ======

        Assertions.assertEquals(2, level4.size());

        Assertions.assertEquals("34.49.145.239", level4.get(0).toString());
        Assertions.assertEquals("141.255.1.133", level4.get(1).toString());

        // ====


        Assertions.assertEquals(2, level5.size());

        Assertions.assertEquals("0.146.197.108", level5.get(0).toString());
        Assertions.assertEquals("122.117.67.158", level5.get(1).toString());


    }

    @Test
    public void getLevelNullTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        List<List<IP>> levels = network.getLevels(null);

        Assertions.assertEquals(0, levels.size());
    }


    @Test
    public void getLevelNonExistingIpTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        List<List<IP>> levels = network.getLevels(new IP("0.0.0.0"));

        Assertions.assertEquals(0, levels.size());
    }

    @Test
    public void toStringSimpleTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        Assertions.assertEquals("(1.1.1.1 2.2.2.2)", network.toString(new IP("1.1.1.1")));
    }

    @Test
    public void toStringNullIpTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        Assertions.assertEquals("", network.toString(null));
    }

    @Test
    public void toStringNonExistingIpTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        Assertions.assertEquals("", network.toString(new IP("0.0.0.0")));
    }

    @Test
    public void toStringExample1Test() throws ParseException {
        Network network = new Network("(85.193.148.81 141.255.1.133 34.49.145.239 231.189.0.127)");

        Assertions.assertEquals("(85.193.148.81 34.49.145.239 141.255.1.133 231.189.0.127)", network.toString(new IP("85.193.148.81")));
    }

    @Test
    public void toStringExample2Test() throws ParseException {
        Network network = new Network("(141.255.1.133 122.117.67.158 0.146.197.108)");

        Assertions.assertEquals("(141.255.1.133 0.146.197.108 122.117.67.158)", network.toString(new IP("141.255.1.133")));
    }

    @Test
    public void toStringExample3Test() throws ParseException {
        Network network = new Network("(231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77)");

        Assertions.assertEquals("(231.189.0.127 39.20.222.120 77.135.84.171 116.132.83.77 252.29.23.0)", network.toString(new IP("231.189.0.127")));
    }

    @Test
    public void toStringExample4Test() throws ParseException {
        Network network = new Network("(85.193.148.81 (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239 (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");

        Assertions.assertEquals("(85.193.148.81 34.49.145.239 (141.255.1.133 0.146.197.108 122.117.67.158) (231.189.0.127 39.20.222.120 77.135.84.171 116.132.83.77 252.29.23.0))", network.toString(new IP("85.193.148.81")));
    }

    @Test
    public void toStringExample1TestDifferentNode() throws ParseException {
        Network network = new Network("(85.193.148.81 141.255.1.133 34.49.145.239 231.189.0.127)");

        Assertions.assertEquals("(34.49.145.239 (85.193.148.81 141.255.1.133 231.189.0.127))", network.toString(new IP("34.49.145.239")));
    }

    @Test
    public void toStringExample2TestDifferentNode() throws ParseException {
        Network network = new Network("(141.255.1.133 122.117.67.158 0.146.197.108)");

        Assertions.assertEquals("(122.117.67.158 (141.255.1.133 0.146.197.108))", network.toString(new IP("122.117.67.158")));
    }

    @Test
    public void toStringExample3TestDifferentNode() throws ParseException {
        Network network = new Network("(231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77)");

        Assertions.assertEquals("(39.20.222.120 (231.189.0.127 77.135.84.171 116.132.83.77 252.29.23.0))", network.toString(new IP("39.20.222.120")));
    }

    @Test
    public void toStringExample4TestDifferentNode() throws ParseException {
        Network network = new Network("(85.193.148.81 (141.255.1.133 122.117.67.158 0.146.197.108) 34.49.145.239 (231.189.0.127 77.135.84.171 39.20.222.120 252.29.23.0 116.132.83.77))");

        Assertions.assertEquals("(77.135.84.171 (231.189.0.127 39.20.222.120 (85.193.148.81 34.49.145.239 (141.255.1.133 0.146.197.108 122.117.67.158)) 116.132.83.77 252.29.23.0))", network.toString(new IP("77.135.84.171")));
    }

    @Test
    public void addSimpleTest() throws ParseException {
        Network network1 = new Network("(1.1.1.1 2.2.2.2)");
        Network network2 = new Network("(2.2.2.2 3.3.3.3)");

        boolean result = network1.add(network2);

        Assertions.assertTrue(result);

        List<List<IP>> levels = network1.getLevels(new IP("1.1.1.1"));

        List<IP> level1 = levels.get(0);
        List<IP> level2 = levels.get(1);
        List<IP> level3 = levels.get(2);


        Assertions.assertEquals(2, network1.getHeight(new IP("1.1.1.1")));

        // =====
        Assertions.assertEquals(1, level1.size());
        Assertions.assertEquals("1.1.1.1", level1.get(0).toString());

        // =====
        Assertions.assertEquals(1, level2.size());
        Assertions.assertEquals("2.2.2.2", level2.get(0).toString());

        // =====
        Assertions.assertEquals(1, level3.size());
        Assertions.assertEquals("3.3.3.3", level3.get(0).toString());
    }

    @Test
    public void addSimpleTestDisjointNetworks() throws ParseException {
        Network network1 = new Network("(1.1.1.1 2.2.2.2)");
        Network network2 = new Network("(3.3.3.3 4.4.4.4)");

        boolean result = network1.add(network2);

        Assertions.assertTrue(result);

        List<IP> ips = network1.list();

        Assertions.assertEquals(4, ips.size());
        Assertions.assertEquals("1.1.1.1", ips.get(0).toString());
        Assertions.assertEquals("2.2.2.2", ips.get(1).toString());
        Assertions.assertEquals("3.3.3.3", ips.get(2).toString());
        Assertions.assertEquals("4.4.4.4", ips.get(3).toString());

        Assertions.assertEquals("(1.1.1.1 2.2.2.2)", network1.toString(new IP("1.1.1.1")));
        Assertions.assertEquals("(3.3.3.3 4.4.4.4)", network1.toString(new IP("3.3.3.3")));
    }

    @Test
    public void addTestNullNetwork() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        boolean result = network.add(null);

        Assertions.assertFalse(result);

        List<List<IP>> levels = network.getLevels(new IP("1.1.1.1"));
        List<IP> level1 = levels.get(0);
        List<IP> level2 = levels.get(1);

        Assertions.assertEquals(2, levels.size());

        // ====
        Assertions.assertEquals(1, level1.size());
        Assertions.assertEquals("1.1.1.1", level1.get(0).toString());


        // ====
        Assertions.assertEquals(1, level2.size());
        Assertions.assertEquals("2.2.2.2", level2.get(0).toString());
    }

    @Test
    public void addNoSideEffectsTest() throws ParseException {
        Network network1 = new Network("(1.1.1.1 2.2.2.2 (3.3.3.3 4.4.4.4))");
        Network network2 = new Network("(4.4.4.4 5.5.5.5 6.6.6.6 7.7.7.7)");

        network1.add(network2);

        // manipulate network2

        network2.disconnect(new IP("4.4.4.4"), new IP("5.5.5.5"));
        network2.add(new Network("(8.8.8.8 9.9.9.9)"));
        network2.connect(new IP("1.1.1.1"), new IP("8.8.8.8"));

        List<IP> ips = network1.list();

        Assertions.assertTrue(ips.contains(new IP("5.5.5.5")));
        Assertions.assertFalse(ips.contains(new IP("8.8.8.8")));
        Assertions.assertFalse(ips.contains(new IP("9.9.9.9")));
    }

    @Test
    public void addFailingTest() throws ParseException {
        Network network1 = new Network("(1.1.1.1 2.2.2.2)");
        Network network2 = new Network("(1.1.1.1 (3.3.3.3 2.2.2.2))");

        boolean result = network1.add(network2);

        Assertions.assertFalse(result);

        // check for side effects

        // ====
        List<List<IP>> network1Levels = network1.getLevels(new IP("1.1.1.1"));
        List<List<IP>> network2Levels = network2.getLevels(new IP("1.1.1.1"));


        // === LEVEL 1 ====
        Assertions.assertEquals(2, network1Levels.size());

        List<IP> n1Level1 = network1Levels.get(0);
        List<IP> n1Level2 = network1Levels.get(1);

        Assertions.assertEquals(1, n1Level1.size());
        Assertions.assertEquals("1.1.1.1", n1Level1.get(0).toString());

        Assertions.assertEquals(1, n1Level2.size());
        Assertions.assertEquals("2.2.2.2", n1Level2.get(0).toString());

        // === LEVEL 2 ====
        Assertions.assertEquals(3, network2Levels.size());

        List<IP> n2Level1 = network2Levels.get(0);
        List<IP> n2Level2 = network2Levels.get(1);
        List<IP> n2Level3 = network2Levels.get(2);

        Assertions.assertEquals(1, n2Level1.size());
        Assertions.assertEquals("1.1.1.1", n2Level1.get(0).toString());

        Assertions.assertEquals(1, n2Level2.size());
        Assertions.assertEquals("3.3.3.3", n2Level2.get(0).toString());

        Assertions.assertEquals(1, n2Level3.size());
        Assertions.assertEquals("2.2.2.2", n2Level3.get(0).toString());
    }

    @Test
    public void addMultipleDifferentTopologiesTest() throws ParseException {

        Network network1 = new Network("(1.1.1.1 2.2.2.2 3.3.3.3)");
        Network network1B = new Network("(4.4.4.4 (5.5.5.5 (6.6.6.6 7.7.7.7)))");
        Network network1C = new Network("(8.8.8.8 9.9.9.9)");
        Network network1D = new Network("(11.11.11.11 12.12.12.12)");

        network1.add(network1B);
        network1.add(network1C);
        network1.add(network1D);

        // ======

        Network network2 = new Network("(1.1.1.1 (4.4.4.4 8.8.8.8))");
        Network network2B = new Network("(9.9.9.9 10.10.10.10)");

        network2.add(network2B);

        // ========

        boolean result = network1.add(network2);

        Assertions.assertTrue(result);

        List<List<IP>> levels = network1.getLevels(new IP("1.1.1.1"));

        Assertions.assertEquals(5, levels.size());

        List<IP> level1 = levels.get(0);
        List<IP> level2 = levels.get(1);
        List<IP> level3 = levels.get(2);
        List<IP> level4 = levels.get(3);
        List<IP> level5 = levels.get(4);

        // LEVEL 1
        Assertions.assertEquals(1, level1.size());
        Assertions.assertEquals("1.1.1.1", level1.get(0).toString());

        // LEVEL 2
        Assertions.assertEquals(3, level2.size());
        Assertions.assertEquals("2.2.2.2", level2.get(0).toString());
        Assertions.assertEquals("3.3.3.3", level2.get(1).toString());
        Assertions.assertEquals("4.4.4.4", level2.get(2).toString());

        // LEVEL 3
        Assertions.assertEquals(2, level3.size());
        Assertions.assertEquals("5.5.5.5", level3.get(0).toString());
        Assertions.assertEquals("8.8.8.8", level3.get(1).toString());

        // LEVEL 4
        Assertions.assertEquals(2, level4.size());
        Assertions.assertEquals("6.6.6.6", level4.get(0).toString());
        Assertions.assertEquals("9.9.9.9", level4.get(1).toString());

        // LEVEL 5 - this reminds me of a puzzle I once heard
        Assertions.assertEquals(2, level5.size());
        Assertions.assertEquals("7.7.7.7", level5.get(0).toString());
        Assertions.assertEquals("10.10.10.10", level5.get(1).toString());

        // SECOND TOPOLOGY
        List<List<IP>> levels2 = network1.getLevels(new IP("11.11.11.11"));

        Assertions.assertEquals(2, levels2.size());

        List<IP> level21 = levels2.get(0);
        List<IP> level22 = levels2.get(1);

        Assertions.assertEquals(1, level21.size());
        Assertions.assertEquals("11.11.11.11", level21.get(0).toString());
        Assertions.assertEquals("12.12.12.12", level22.get(0).toString());
    }

    @Test
    public void addWontChangeTest() throws ParseException {
        Network network1 = new Network("(1.1.1.1 2.2.2.2 3.3.3.3)");
        Network network2 = new Network("(1.1.1.1 2.2.2.2)");

        boolean result = network1.add(network2);

        Assertions.assertFalse(result);
    }

    @Test
    public void addComplexFailingTest() throws ParseException {
        Network network1 = new Network("(1.1.1.1 2.2.2.2 3.3.3.3)");
        Network network1B = new Network("(4.4.4.4 (5.5.5.5 (6.6.6.6 7.7.7.7)))");
        Network network1C = new Network("(8.8.8.8 9.9.9.9)");
        Network network1D = new Network("(11.11.11.11 12.12.12.12)");

        network1.add(network1B);
        network1.add(network1C);
        network1.add(network1D);

        // ======

        Network network2 = new Network("(1.1.1.1 (4.4.4.4 8.8.8.8))");
        Network network2B = new Network("(2.2.2.2 3.3.3.3)");

        network2.add(network2B);

        // ========

        boolean result = network1.add(network2);

        Assertions.assertFalse(result);

        // check for side effects

        Assertions.assertEquals("(1.1.1.1 2.2.2.2 3.3.3.3)", network1.toString(new IP("1.1.1.1")));
        Assertions.assertEquals("(4.4.4.4 (5.5.5.5 (6.6.6.6 7.7.7.7)))", network1.toString(new IP("4.4.4.4")));
        Assertions.assertEquals("(8.8.8.8 9.9.9.9)", network1.toString(new IP("8.8.8.8")));
        Assertions.assertEquals("(11.11.11.11 12.12.12.12)", network1.toString(new IP("11.11.11.11")));

        Assertions.assertEquals("(1.1.1.1 (4.4.4.4 8.8.8.8))", network2.toString(new IP("1.1.1.1")));
        Assertions.assertEquals("(2.2.2.2 3.3.3.3)", network2.toString(new IP("2.2.2.2")));
    }

    @Test
    public void connectSimpleTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2 3.3.3.3)");
        network.add(new Network("(4.4.4.4 5.5.5.5 6.6.6.6)"));

        network.connect(new IP("3.3.3.3"), new IP("4.4.4.4"));

        List<List<IP>> levels = network.getLevels(new IP("1.1.1.1"));

        Assertions.assertEquals(4, levels.size());

        List<IP> level1 = levels.get(0);
        List<IP> level2 = levels.get(1);
        List<IP> level3 = levels.get(2);
        List<IP> level4 = levels.get(3);

        // LEVEL 1
        Assertions.assertEquals(1, level1.size());
        Assertions.assertEquals("1.1.1.1", level1.get(0).toString());

        // LEVEL 2
        Assertions.assertEquals(2, level2.size());
        Assertions.assertEquals("2.2.2.2", level2.get(0).toString());
        Assertions.assertEquals("3.3.3.3", level2.get(1).toString());

        // LEVEL 3
        Assertions.assertEquals(1, level3.size());
        Assertions.assertEquals("4.4.4.4", level3.get(0).toString());

        // LEVEL 4
        Assertions.assertEquals(2, level4.size());
        Assertions.assertEquals("5.5.5.5", level4.get(0).toString());
        Assertions.assertEquals("6.6.6.6", level4.get(1).toString());

        // EXTRA

        Assertions.assertEquals(1, network.getTopologies().size());
    }

    @Test
    public void connectNUllTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        boolean result1 = network.connect(null, new IP("1.1.1.1"));
        Assertions.assertFalse(result1);

        boolean result2 = network.connect(new IP("1.1.1.1"), null);
        Assertions.assertFalse(result2);
    }

    @Test
    public void connectSameNode() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        boolean result = network.connect(new IP("1.1.1.1"), new IP("1.1.1.1"));

        Assertions.assertFalse(result);
    }

    @Test
    public void connectNonExistingNode() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        boolean result1 = network.connect(new IP("1.1.1.1"), new IP("0.0.0.0"));
        boolean result2 = network.connect(new IP("0.0.0.0"), new IP("1.1.1.1"));

        Assertions.assertFalse(result1);
        Assertions.assertFalse(result2);
    }

    @Test
    public void connectAlreadyConnectedNode() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        boolean result = network.connect(new IP("1.1.1.1"), new IP("2.2.2.2"));

        Assertions.assertFalse(result);
    }

    @Test
    public void connectCircularTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2 3.3.3.3)");

        boolean result = network.connect(new IP("2.2.2.2"), new IP("3.3.3.3"));

        Assertions.assertFalse(result);

        List<List<IP>> levels = network.getLevels(new IP("2.2.2.2"));

        Assertions.assertEquals(3, levels.size());

        List<IP> level1 = levels.get(0);
        List<IP> level2 = levels.get(1);
        List<IP> level3 = levels.get(2);

        // LEVEL 1
        Assertions.assertEquals(1, level1.size());
        Assertions.assertEquals("2.2.2.2", level1.get(0).toString());

        // LEVEL 2
        Assertions.assertEquals(1, level2.size());
        Assertions.assertEquals("1.1.1.1", level2.get(0).toString());

        // LEVEL 3
        Assertions.assertEquals(1, level3.size());
        Assertions.assertEquals("3.3.3.3", level3.get(0).toString());
    }

    @Test
    public void disconnectSimpleTest() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2 (3.3.3.3 (4.4.4.4 5.5.5.5)))");

        boolean result = network.disconnect(new IP("1.1.1.1"), new IP("3.3.3.3"));

        Assertions.assertTrue(result);

        List<List<IP>> levelsA = network.getLevels(new IP("1.1.1.1"));
        List<List<IP>> levelsB = network.getLevels(new IP("3.3.3.3"));

        // LEVELS A
        Assertions.assertEquals(2, levelsA.size());
        List<IP> levelA1 = levelsA.get(0);
        List<IP> levelA2 = levelsA.get(1);

        Assertions.assertEquals(1, levelA1.size());

        Assertions.assertEquals("1.1.1.1", levelA1.get(0).toString());

        Assertions.assertEquals(1, levelA2.size());
        Assertions.assertEquals("2.2.2.2", levelA2.get(0).toString());


        // LEVELS B
        Assertions.assertEquals(3, levelsB.size());
        List<IP> levelB1 = levelsB.get(0);
        List<IP> levelB2 = levelsB.get(1);
        List<IP> levelB3 = levelsB.get(2);

        Assertions.assertEquals(1, levelB1.size());
        Assertions.assertEquals(1, levelB2.size());
        Assertions.assertEquals(1, levelB3.size());

        // ======

        Assertions.assertEquals(1, levelB1.size());

        Assertions.assertEquals("3.3.3.3", levelB1.get(0).toString());

        // ======

        Assertions.assertEquals(1, levelB2.size());

        Assertions.assertEquals("4.4.4.4", levelB2.get(0).toString());

        // ======

        Assertions.assertEquals(1, levelB3.size());

        Assertions.assertEquals("5.5.5.5", levelB3.get(0).toString());
    }

    @Test
    public void disconnectNullIps() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2 (3.3.3.3 (4.4.4.4 5.5.5.5)))");

        boolean result1 = network.disconnect(null, new IP("4.4.4.4"));

        Assertions.assertFalse(result1);

        boolean result2 = network.disconnect(new IP("3.3.3.3"), null);

        Assertions.assertFalse(result2);

        Assertions.assertEquals("(1.1.1.1 2.2.2.2 (3.3.3.3 (4.4.4.4 5.5.5.5)))", network.toString(new IP("1.1.1.1")));
    }

    @Test
    public void disconnectNonExistingIps() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2 (3.3.3.3 (4.4.4.4 5.5.5.5)))");

        boolean result1 = network.disconnect(new IP("0.0.0.0"), new IP("4.4.4.4"));

        Assertions.assertFalse(result1);

        boolean result2 = network.disconnect(new IP("3.3.3.3"), new IP("0.0.0.0"));

        Assertions.assertFalse(result2);

        Assertions.assertEquals("(1.1.1.1 2.2.2.2 (3.3.3.3 (4.4.4.4 5.5.5.5)))", network.toString(new IP("1.1.1.1")));
    }

    @Test
    public void disconnectNonDirectIps() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2 (3.3.3.3 (4.4.4.4 5.5.5.5)))");

        boolean result1 = network.disconnect(new IP("1.1.1.1"), new IP("4.4.4.4"));

        Assertions.assertFalse(result1);

        Assertions.assertEquals("(1.1.1.1 2.2.2.2 (3.3.3.3 (4.4.4.4 5.5.5.5)))", network.toString(new IP("1.1.1.1")));
    }

    @Test
    public void disconnectIpsInDifferentTopologies() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2 (3.3.3.3 4.4.4.4))");

        network.add(new Network("(5.5.5.5 6.6.6.6)"));

        boolean result1 = network.disconnect(new IP("3.3.3.3"), new IP("5.5.5.5"));

        Assertions.assertFalse(result1);

        Assertions.assertEquals("(1.1.1.1 2.2.2.2 (3.3.3.3 4.4.4.4))", network.toString(new IP("1.1.1.1")));
        Assertions.assertEquals("(5.5.5.5 6.6.6.6)", network.toString(new IP("5.5.5.5")));
    }

    @Test
    public void denyDeletingLastConnection() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");

        boolean result = network.disconnect(new IP("1.1.1.1"), new IP("2.2.2.2"));

        Assertions.assertFalse(result);

        Assertions.assertEquals("(1.1.1.1 2.2.2.2)", network.toString(new IP("1.1.1.1")));
    }

    @Test
    public void disconnectTestRemoveNode() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2 3.3.3.3)");

        network.disconnect(new IP("1.1.1.1"), new IP("3.3.3.3"));

        List<IP> ips = network.list();

        Assertions.assertTrue(ips.contains(new IP("1.1.1.1")));
        Assertions.assertTrue(ips.contains(new IP("2.2.2.2")));

        Assertions.assertFalse(ips.contains(new IP("3.3.3.3")));
    }

    @Test
    public void allowDeletingLastConnectionBecauseMultipleTopologies() throws ParseException {
        Network network = new Network("(1.1.1.1 2.2.2.2)");
        network.add(new Network("(3.3.3.3 4.4.4.4)"));

        boolean result = network.disconnect(new IP("1.1.1.1"), new IP("2.2.2.2"));

        Assertions.assertTrue(result);

        Assertions.assertEquals("(3.3.3.3 4.4.4.4)", network.toString(new IP("3.3.3.3")));

        List<IP> ips = network.list();

        Assertions.assertFalse(ips.contains(new IP("1.1.1.1")));
        Assertions.assertFalse(ips.contains(new IP("2.2.2.2")));
    }

    @Test
    public void complexDisconnectTest() throws ParseException {
        Network network = new Network("(1.1.1.1 (2.2.2.2 (4.4.4.4 (11.11.11.11 (13.13.13.13 15.15.15.15))) (5.5.5.5 12.12.12.12) 6.6.6.6) (3.3.3.3 (7.7.7.7 8.8.8.8 9.9.9.9 (10.10.10.10 14.14.14.14))))");

        boolean result = network.disconnect(new IP("2.2.2.2"), new IP("4.4.4.4"));
        Assertions.assertTrue(result);

        Assertions.assertEquals("(1.1.1.1 (2.2.2.2 (5.5.5.5 12.12.12.12) 6.6.6.6) (3.3.3.3 (7.7.7.7 8.8.8.8 9.9.9.9 (10.10.10.10 14.14.14.14))))", network.toString(new IP("1.1.1.1")));
        Assertions.assertEquals("(4.4.4.4 (11.11.11.11 (13.13.13.13 15.15.15.15)))", network.toString(new IP("4.4.4.4")));

    }

    @Test
    public void complexDisconnectTestRemoveNode() throws ParseException {
        Network network = new Network("(1.1.1.1 (2.2.2.2 (4.4.4.4 (11.11.11.11 (13.13.13.13 15.15.15.15))) (5.5.5.5 12.12.12.12) 6.6.6.6) (3.3.3.3 (7.7.7.7 8.8.8.8 9.9.9.9 (10.10.10.10 14.14.14.14))))");

        boolean result = network.disconnect(new IP("10.10.10.10"), new IP("14.14.14.14"));
        Assertions.assertTrue(result);

        Assertions.assertEquals("(1.1.1.1 (2.2.2.2 (4.4.4.4 (11.11.11.11 (13.13.13.13 15.15.15.15))) (5.5.5.5 12.12.12.12) 6.6.6.6) (3.3.3.3 (7.7.7.7 8.8.8.8 9.9.9.9 10.10.10.10)))", network.toString(new IP("1.1.1.1")));
        Assertions.assertFalse(network.list().contains(new IP("14.14.14.14")));

    }
}