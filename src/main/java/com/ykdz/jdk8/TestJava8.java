package com.ykdz.jdk8;

import org.apache.poi.hssf.record.DVALRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestJava8 {
    // 使用 Stream API 过滤和映射一个整数列表
    public static void main(String[] args) {
//        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
//        numbers.stream()
//                .filter(n -> n % 2 == 0)
//                .map(n -> n * 2)
//                .forEach(System.out::println);
//
//        // 使用 Lambda 表达式简化代码
//        Runnable runnable = () -> System.out.println("Hello, World!");
//        runnable.run();
//
//        // 使用 Function 和 Predicate 接口
//        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
//        names.stream()
//                .filter(name -> name.startsWith("A"))
//                .forEach(System.out::println);
//
//        // 使用 Optional 类处理可能为空的情况
//        Optional<String> optionalName = Optional.ofNullable("Alice");
//        optionalName.ifPresent(System.out::println);
//
//        // 使用 Comparator 接口比较对象
//        List<String> cities = Arrays.asList("New York", "Los Angeles", "Chicago");
//        cities.sort(Comparator.naturalOrder());
//        cities.forEach(System.out::println);


        // 使用 Stream API 并行处理统计文本中的单词数量
        try (Stream<String> lines = Files.lines(Paths.get("D://usr//text.txt"))) {
            long wordCount = lines.flatMap(line -> Arrays.stream(line.split(" \\W+"))) //使用正则表达式分割单词
                    .filter(Objects::nonNull) //过滤空字符串
                    .distinct() //去重复
                    .peek(System.out::println)
                    .count(); //计数
            System.out.println("单词数量总数量是" + wordCount);
        } catch (IOException e) {
            e.printStackTrace();
        }


        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry");
        List<String> lowerCaseFruits = fruits.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        List<List<String>> nestedFruits = Arrays.asList(
                Arrays.asList("Apple", "Banana"),
                Arrays.asList("Cherry", "Date")
        );
        Stream<String> flatFruits = nestedFruits.stream()
                .flatMap(Collection::stream);

        //初始化客户和订单数据

    }


}
