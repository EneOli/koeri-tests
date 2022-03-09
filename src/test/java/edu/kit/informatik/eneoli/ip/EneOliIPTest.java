package edu.kit.informatik.eneoli.ip;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class EneOliIPTest {

    @Test
    public void createIPTest() throws ParseException {
        Assertions.assertNotNull(new IP("1.1.1.1"));
    }

    @Test
    public void readIpTest() throws ParseException {
        IP ip = new IP("1.1.1.1");
        Assertions.assertEquals("1.1.1.1", ip.toString());
    }

    @Test
    public void garbageIpString() throws ParseException {

        Assertions.assertThrows(ParseException.class, () -> new IP("1..A.1"));
    }

    @Test
    public void garbageIpString2() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new IP("1.1.A.1"));
    }

    @Test
    public void garbageIpString3() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new IP("1.1..1"));
    }

    @Test
    public void nullString() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new IP((String) null));
    }

    @Test
    public void segmentOutOfBoundsMin() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new IP("-1.1.1.1"));
    }

    @Test
    public void segmentOutOfBoundsMax() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new IP("1.256.1.1"));
    }

    @Test
    public void trailingZero() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new IP("01.1.1.1"));
    }

    @Test
    public void minAddress() throws ParseException {
        IP ip = new IP("0.0.0.0");
    }

    @Test
    public void maxAddress() throws ParseException {
        IP ip = new IP("255.255.255.255");
    }

    @Test
    public void lessSegments() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new IP("255.255.255"));
    }

    @Test
    public void moreSegments() throws ParseException {
        Assertions.assertThrows(ParseException.class, () -> new IP("255.255.255.255.255"));
    }

    @Test
    public void addressEquals() throws ParseException {
        IP ip1 = new IP("255.255.255.255");
        IP ip2 = new IP("255.255.255.255");
        IP ip3 = new IP("255.225.255.251");

        Assertions.assertTrue(ip1.equals(ip2));
        Assertions.assertTrue(ip2.equals(ip1));

        Assertions.assertFalse(ip1.equals(ip3));
        Assertions.assertFalse(ip3.equals(ip1));
    }

    @Test
    public void addressOrder() throws ParseException {
        IP ip1 = new IP("1.255.255.255");
        IP ip2 = new IP("100.255.255.255");
        IP ip3 = new IP("1.255.255.42");

        List<IP> list = new ArrayList<IP>(Arrays.asList(ip1, ip2, ip3));

        list.sort(IP::compareTo);

        Assertions.assertEquals(list.get(0), ip3);
        Assertions.assertEquals(list.get(1), ip1);
        Assertions.assertEquals(list.get(2), ip2);
    }

}
