package org.mine;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.List.of;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(JUnit4.class)
public class FileListTest {

    private static final String REGEXP = ".*[0-9-]+'T'[0-9-.:Z+]+.*";

    private static final DateTimeFormatter FORMAT1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX");
    private static final DateTimeFormatter FORMAT2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateTimeFormatter FORMAT3 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS");
    private static final DateTimeFormatter FORMAT4 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateTimeFormatter FORMAT5 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
    private static final DateTimeFormatter FORMAT6 = DateTimeFormatter.ofPattern("yyyy-MM-dd'_'HH-mm-ss");
    private static final DateTimeFormatter FORMAT7 = DateTimeFormatter.ofPattern("yyyy:MM:dd'_'HH:mm:ss");
    private static final List<DateTimeFormatter> PATTERNS = of(FORMAT1, FORMAT2, FORMAT3,
        FORMAT4, FORMAT5, FORMAT6, FORMAT7);

    @Test
    public void test_error1() {
        var line = "hello 2020-01 fasdfa";
        var pattern = Pattern.compile(REGEXP);
        var matcher = pattern.matcher(line);

        var result = matcher.matches();

        assertFalse(result);
    }

    @Test
    public void test3() {
        var line = "hello 123fff";
        var regexp = "\\d{3}";

        var matcher = Pattern.compile(regexp).matcher(line);
        matcher.find();
        var result = matcher.group();

        System.out.println(result);
    }

    @Test
    public void test4(){
        var line = "2019-02-25T09:41:53.904Z";

        var result4 = LocalDateTime.parse(line, FORMAT4);
        var result5 = LocalDateTime.parse(line, FORMAT5);

        System.out.println(result4);
        System.out.println(result5);
    }
}