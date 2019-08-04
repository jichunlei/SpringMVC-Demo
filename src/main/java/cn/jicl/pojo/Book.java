package cn.jicl.pojo;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/4 15:55
 * @Description: TODO
 */
@Data
@NoArgsConstructor
public class Book {
    private String BookName;
    private double price;
    private Person author;
}
