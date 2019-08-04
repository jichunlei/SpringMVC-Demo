package cn.jicl.pojo;

import lombok.Data;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/4 15:55
 * @Description: TODO
 */
@Data
public class Book {
    private String BookName;
    private double price;
    private Person author;
}
