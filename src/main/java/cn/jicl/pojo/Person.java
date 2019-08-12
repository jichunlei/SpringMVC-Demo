package cn.jicl.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Auther: xianzilei
 * @Date: 2019/8/4 15:56
 * @Description: TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    @NotBlank
    @Length(max = 18,min = 6)
    private String username;

    //Past:必须为过去的时间
    //Future必须为未来的时间
    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birth=new Date();

    @Email
    @JsonIgnore//输出json忽略该字段
    private String email;

    @NumberFormat(pattern = "#,###.##")
    private double salary;

    public Person(String username) {
        this.username = username;
    }
}
